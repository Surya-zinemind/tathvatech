package com.tathvatech.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.sarvasutra.etest.api.model.AttachmentInfoBean;
import com.tathvatech.testsutra.openitemv2.common.OpenItemCategoryEnum;
import com.tathvatech.testsutra.openitemv2.common.OpenItemInfoBean;
import com.tathvatech.testsutra.openitemv2.common.OpenItemQuery;
import com.tathvatech.testsutra.openitemv2.common.OpenItemTypeEnum;
import com.tathvatech.testsutra.openitemv2.service.OILManager;
import com.tathvatech.testsutra.openitemv2.service.OpenItemV2;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.AccountManager;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.common.Attachment;
import com.tathvatech.ts.core.common.DummyWorkstation;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.service.CommonServiceManager;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.UnitQuery;
import com.thirdi.surveyside.project.ProjectDelegate;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.project.ProjectQuery;
public class ModServiceImpl
{

	public static List<Mode> getModes(int projectPk) throws Exception 
	{
		return PersistWrapper.readList(Mode.class, "select * from TAB_MODE where projectPk=? order by createdDate desc", projectPk);
	}

	public static Mode getMode(int modPk) throws Exception 
	{
		return PersistWrapper.readByPrimaryKey(Mode.class, modPk);
	}

	public static Mode getModByModNo(int projectPk, String modeNo) throws Exception 
	{
		return PersistWrapper.read(Mode.class, "select * from TAB_MODE where projectPk=? and modeNo=?", projectPk, modeNo);
	}

	public static OpenItemV2 getModeInstanceResponseMasterForUnit(ModeUnit modeUnitInfo, Mode mode) throws Exception
	{
		if(modeUnitInfo == null || mode == null || mode.getPk() == 0)
			return null;
		
		
		// i am getting the modPk which is the responseId from the unit in 2 ways. we did not have
		//the responseId stored in this table initially. this was causing lots of issues. so added it
		//old ones could still be there which does not have this populated.
		if(modeUnitInfo.getUnitModPk() != null)
		{
			int responseId = modeUnitInfo.getUnitModPk();
			return OILManager.getOpenItem(responseId);
		}
		else
		{
			return OILManager.getOpenItemByReferenceNoForUnit(new UnitOID(modeUnitInfo.getUnitPk(), null), mode.getModeNo());
		}
	}

	public static Mode getModeMaster(UnitOID unitOID, int unitResponseId) throws Exception
	{
		Mode mode = null;
		mode = PersistWrapper.read(Mode.class, "select * from TAB_MODE where pk in (select modePk from TAB_MODE_UNIT where unitPk=? and unitModPk=?)", unitOID.getPk(), unitResponseId);
		if(mode == null)
		{
			// we were not initially tracking unit level responseId in the TAB_MODE_UNIT table. so that could be null
			//in that case use the modeUnitNo in that table to get the connection.
			OpenItemV2 resp = OILManager.getOpenItem(unitResponseId);
			if(resp != null)
			{
				mode = PersistWrapper.read(Mode.class, "select * from TAB_MODE where pk in (select modePk from TAB_MODE_UNIT where unitPk=? and unitModeNo=?)", unitOID.getPk(), resp.getReferenceNo());
			}
		}
		return mode;
	}
	
	public static OpenItemV2 syncModeToUnit(UserContext context, UnitQuery unitQuery, Mode mode) throws Exception 
	{
		OpenItemInfoBean openItemForModBean = null;
		
		OpenItemV2 openItemForMod = null;
		ModeUnit modeUnitInfo = getModeUnitInfo(unitQuery.getUnitOID(), mode);
		if(modeUnitInfo != null)
		{
			openItemForMod = OILManager.getOpenItem(modeUnitInfo.getUnitModPk());
			if(openItemForMod == null)
				throw new AppException("Invalid mod entry for Unit, Please contact your administrator");
			
			//mod is already available at the Unit.class 
			//check if the unitlevel mod is in completed or closed status before updating it
			if((OpenItemV2.StatusEnum.Closed.name().equals(openItemForMod.getStatus()) || OpenItemV2.StatusEnum.Completed.name().equals(openItemForMod.getStatus())))
			{
				//dont need to do anything here.
				return openItemForMod;
			}

			else // we need to sync the information here.
			{
				if(!(mode.getModeNo().equals(openItemForMod.getReferenceNo())))
				{
					// reference number has changed.. so we need to update it.
					OpenItemV2 aItemFromRefNo = OILManager.getOpenItemByReferenceNoForUnit(unitQuery.getUnitOID(), mode.getModeNo());
					if(aItemFromRefNo != null)
					{
						// mode no used by some one else, better to rollback the entire sync process.
						throw new AppException("Open Item no already used for another P8 in the unit, " + unitQuery.getUnitName());
					}
				}

				openItemForModBean = OpenItemInfoBean.getOpenItemInfoBean(openItemForMod);
				openItemForModBean.setReferenceNo(mode.getModeNo());
			}
		}
		else // mod is not available in the unit, so it has be synched afresh
		{
			// check if the modeNo is already taken up by another open item
			OpenItemV2 aItemFromRefNo = OILManager.getOpenItemByReferenceNoForUnit(unitQuery.getUnitOID(), mode.getModeNo());
			if(aItemFromRefNo != null)
			{
				// mode no used by some one else, better to rollback the entire sync process.
				throw new AppException("Open Item no already used for another P8 in the unit, " + unitQuery.getUnitName());
			}
			
			// ready to create mod in the unit
			openItemForModBean = new OpenItemInfoBean();
			openItemForModBean.setType(OpenItemTypeEnum.Mod);
			openItemForModBean.setUnitOID(unitQuery.getUnitOID());
			openItemForModBean.setProjectOID(new ProjectOID(unitQuery.getProjectPk(), null));
			openItemForModBean.setCreatedDate(new Date());
			openItemForModBean.setCreatedBy(context.getUser().getOID());
			openItemForModBean.setReferenceNo(mode.getModeNo());
		
		}
		
		// now ready to sync
		
		
		openItemForModBean.setDescription(mode.getDescription());
		openItemForModBean.setComment(mode.getComments());
		
		
		//update the custodian in the unit only if the unit level custodian is empty
		UserOID currentCustOID = openItemForModBean.getCustodianOID();
		if(currentCustOID != null)
		{
			//dont change it if it is already assigned,, set the same value... use the change custodian function if the unitlevel custodian should be changed.				
		}
		else if(mode.getAssignedTo() != null)
		{
			User assignedUser = AccountManager.getUser(mode.getAssignedTo());
			List<User> users = ProjectDelegate.getUsersForProject(unitQuery.getProjectPk(), DummyWorkstation.getOID());
			if(assignedUser != null && users != null && users.contains(assignedUser))
			{
				openItemForModBean.setCustodianOID(new UserOID(mode.getAssignedTo(), null));
			}
		}
	
		openItemForModBean.setPartRequiredDesc(mode.getPartsRequiredDesc());
		openItemForModBean.setReworkOrder(mode.getProductionOrder());
		openItemForModBean.setHours(mode.getHours());
		
		if(mode.getCategory() != null)
		{
			OpenItemCategoryEnum cat = (OpenItemCategoryEnum) OpenItemCategoryEnum.getEnum(OpenItemCategoryEnum.class, mode.getCategory());
			openItemForModBean.setCategory(cat);
		}
		else
			openItemForModBean.setCategory(null);

		

		//now copy the attachments
		copyModAttachmentsToUnitLevel(mode, openItemForModBean);

		openItemForModBean = OILManager.saveOpenItemFromWeb(context, openItemForModBean, null);
		if(OpenItemV2.StatusEnum.Draft == openItemForModBean.getStatus())
			OILManager.publishOpenItem(context, openItemForModBean.getOID(), new Date());	

		//now update the sync time
		if(modeUnitInfo == null)
		{
			modeUnitInfo = new ModeUnit();
			modeUnitInfo.setUnitPk(unitQuery.getUnitPk());
			modeUnitInfo.setProjectPk(unitQuery.getProjectPk());
			modeUnitInfo.setModePk(mode.getPk());
			modeUnitInfo.setSyncDate(new Date());
			modeUnitInfo.setSynchedBy(context.getUser().getPk());
			modeUnitInfo.setUnitModPk(openItemForModBean.getPk());
			modeUnitInfo.setUnitModeNo(mode.getModeNo());
			PersistWrapper.createEntity(modeUnitInfo);
		}
		else
		{
			modeUnitInfo.setSynchedBy(context.getUser().getPk());
			modeUnitInfo.setSyncDate(new Date());
			modeUnitInfo.setUnitModPk(openItemForModBean.getPk());
			modeUnitInfo.setUnitModeNo(mode.getModeNo());
			PersistWrapper.update(modeUnitInfo);
		}
		
		// now return the openitem
		openItemForMod = OILManager.getOpenItem(openItemForModBean.getPk());
		return openItemForMod;
	}
	
	private static void copyModAttachmentsToUnitLevel(Mode mode, OpenItemInfoBean bean)
	{
		List<Attachment> modAttachments = CommonServiceManager.getAttachments(mode.getPk(), EntityTypeEnum.Mode.getValue());
		List<AttachmentInfoBean> currentOpenItemAttachments = bean.getAttachments();
		
		List<AttachmentInfoBean> newList = new ArrayList();
		for (Iterator iterator = modAttachments.iterator(); iterator.hasNext();)
		{
			Attachment attachment = (Attachment) iterator.next();
			boolean foundMatch = false;
			for (Iterator iterator2 = currentOpenItemAttachments.iterator(); iterator2.hasNext();)
			{
				AttachmentInfoBean attachmentInfoBean = (AttachmentInfoBean) iterator2.next();
				if(Objects.equals(attachment.getFileName(), attachmentInfoBean.getFileName()))
				{
					newList.add(attachmentInfoBean);
					foundMatch = true;
					break;
				}
			}
			if(foundMatch == false)
			{
				//its not already there, so we need to add it
				AttachmentInfoBean newAttachment = new AttachmentInfoBean();
				newAttachment.setFileDescription(attachment.getFileDescription());
				newAttachment.setFileDisplayName(attachment.getFileDisplayName());
				newAttachment.setFileName(attachment.getFileName());
				newList.add(newAttachment);
			}
		}
		bean.setAttachments(newList);
	}

	public static Mode saveMode(UserContext context, Mode modeToSave,
			ProjectQuery projectQuery) throws Exception
	{
		Mode dupli = PersistWrapper.read(Mode.class, 
				"select * from TAB_MODE where modeNo = ? and projectPk=?", 
				modeToSave.getModeNo(), projectQuery.getPk());
		if(dupli != null && dupli.getPk() != modeToSave.getPk())
		{
			throw new AppException("Duplicate Mod No, Please enter a new number");
		}

		modeToSave.setUpdateDate(new Date());
		if(modeToSave.getPk() == 0)
		{
			modeToSave.setProjectPk(projectQuery.getPk());
			modeToSave.setCreatedBy(context.getUser().getPk());
			modeToSave.setOwnerPk(context.getUser().getPk());
			modeToSave.setCreatedDate(new Date());
			modeToSave.setStatus(Mode.STATUS_OPEN);
			int newPk = PersistWrapper.createEntity(modeToSave);
			
			return PersistWrapper.readByPrimaryKey(Mode.class, newPk);
		}
		else
		{
			PersistWrapper.update(modeToSave);
			return PersistWrapper.readByPrimaryKey(Mode.class, modeToSave.getPk());
		}
		
	}

	public static boolean isModeClosedOnAllAppliedUnits(Mode mode) throws Exception
	{
		//check if the mod is closed under all units if so close the master mod
		StringBuffer sb = new StringBuffer("select count(*) from openitem_v2 op")
				.append(" join TAB_MODE_UNIT tmu on tmu.unitModPk = op.pk ")
				.append(" where tmu.modePk = ? and op.status != ?");
		

		int i = PersistWrapper.read(Integer.class, sb.toString(), mode.getPk(), OpenItemV2.StatusEnum.Closed.name());
		if(i > 0)
			return false;
		else
			return true;
	}
	
	public static void closeMode(UserContext userContext, Mode aMode, String closeComment) 
	{
		aMode.setApprovedDate(new Date());
		aMode.setApprovedBy(userContext.getUser().getPk());
		aMode.setApproveComment(closeComment);
		aMode.setStatus(Mode.STATUS_CLOSED);
		PersistWrapper.update(aMode);
	}


	public static Mode openMode(UserContext userContext, Mode aMode)throws Exception 
	{
		aMode.setStatus(Mode.STATUS_OPEN);
		PersistWrapper.update(aMode);
		
		return PersistWrapper.readByPrimaryKey(Mode.class, aMode.getPk());
	}

	public static void deleteMode(UserContext userContext, Mode aMode) throws Exception 
	{
		if(Mode.STATUS_CLOSED.equals(aMode.getStatus()))
		{
			throw new AppException("Closed Mod cannot be deleted");
		}
		
		Integer unitModeCount = PersistWrapper.read(Integer.class, "select count(*) from TAB_MODE_UNIT where modePk=?", aMode.getPk());
		if(unitModeCount != null && unitModeCount > 0)
		{
			throw new AppException("Mod updated to Units, Remove mod from units and try again.");
		}
		PersistWrapper.delete("delete from TAB_MODE_UNIT where modePk = ? ", aMode.getPk());
		PersistWrapper.deleteEntity(aMode);
	}


	public static List<OpenItemQuery> getOpenItemsForMod(Mode mode)
	{
		StringBuffer sb = new StringBuffer(OpenItemQuery.sql);
		sb.append("join TAB_MODE_UNIT tmu on tmu.unitModPk = op.pk and modePk = ?");
		
		return PersistWrapper.readList(OpenItemQuery.class, sb.toString(), mode.getPk());
	}

	public static List<UnitQuery> getModAppliedUnits(Mode mode) throws Exception 
	{
		return PersistWrapper.readList(UnitQuery.class, UnitQuery.sql + " where 1 = 1 and u.pk in (select unitPk from TAB_MODE_UNIT where modePk=?)", 
				mode.getProjectPk(), mode.getProjectPk(), mode.getProjectPk(), mode.getPk());
	}

	public static ModeUnit getModeUnitInfo(UnitOID unitOID, Mode mode) throws Exception 
	{
		if(mode == null || mode.getPk() == 0)
			return null;
		return PersistWrapper.read(ModeUnit.class, "select * from TAB_MODE_UNIT where unitPk=? and modePk=?", unitOID.getPk(), mode.getPk());
	}

	public static void removeModeFromUnit(UserContext context, UnitOID unitOID, Mode mode) throws Exception 
	{
		ModeUnit modeUnitInfo = getModeUnitInfo(unitOID, mode);
		if(modeUnitInfo != null)
		{
			OpenItemV2  openItem = OILManager.getOpenItem(modeUnitInfo.getUnitModPk());
			if(openItem != null)
			{
				PersistWrapper.deleteEntity(openItem);
			}
			PersistWrapper.deleteEntity(modeUnitInfo);
		}
		else
		{
			throw new AppException("Mod not found in the Unit");
		}
	}

	public static OpenItemV2 getModeInstanceForUnit(ModeUnit modeUnitInfo, Mode mode) throws Exception
	{
		return getModeInstanceResponseMasterForUnit(modeUnitInfo, mode);
	}

	public static List<UnitQuery> updateUnitLevelCustodians(UserContext context,
			ProjectQuery projectQuery, Mode mode, List<OpenItemQuery> openItems, User selectedCustodian) throws Exception
	{
		List<UnitQuery> affectedUnits = new ArrayList();
		for (Iterator iterator = openItems.iterator(); iterator.hasNext();) 
		{
			final OpenItemQuery aOpenItem = (OpenItemQuery) iterator.next();
			
			OpenItemV2 openItem = OILManager.getOpenItem(aOpenItem.getPk());
			if(openItem == null)
				continue;
			
			if(Objects.equals(selectedCustodian.getPk(), openItem.getCustodianPk()))
				continue;

			openItem.setCustodianPk(selectedCustodian.getPk());
			PersistWrapper.update(openItem);

			UnitQuery unitQuery = ProjectManager.getUnitQueryByPk(openItem.getUnitPk(), new ProjectOID(aOpenItem.getProjectPk()));
			affectedUnits.add(unitQuery);
		}

		return affectedUnits;
	}

	public static int updateModOwner(int projectPk, int currentOwnerPk, int newOwnerPk) throws Exception 
	{
		User newOwner = PersistWrapper.readByPrimaryKey(User.class, newOwnerPk);
		if(newOwner == null)
			return 0;
		int count = PersistWrapper.read(Integer.class, "select count(*) from TAB_MODE where projectPk=? and ownerPk = ?", projectPk, currentOwnerPk);
		PersistWrapper.executeUpdate("update TAB_MODE set ownerPk = ? where projectPk=? and ownerPk = ?", newOwnerPk, projectPk, currentOwnerPk);
		return count;
	}
}
