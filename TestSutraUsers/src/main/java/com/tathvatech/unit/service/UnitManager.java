package com.tathvatech.unit.service;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.utils.Base62Util;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.unit.common.UnitEntityQuery;
import com.tathvatech.unit.common.UnitInProjectQuery;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.unit.dao.UnitInProjectDAO;
import com.tathvatech.unit.entity.Unit;
import com.tathvatech.unit.entity.UnitBookmark;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.unit.oid.UnitInProjectOID;
import com.tathvatech.unit.request.SerialNumberFilter;
import com.tathvatech.unit.request.UnitEntityListReportRequest;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.service.WorkstationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


@Service("unitManager")
@RequiredArgsConstructor
public class UnitManager
{
	private static final Logger logger = LoggerFactory.getLogger(UnitManager.class);
	private final PersistWrapper persistWrapper;
	private final UnitInProjectDAO unitInProjectDAO;
	public  int getRootUnitPk(UnitOID unitOID, ProjectOID projectOID) throws Exception
	{
		String sql = " select rootupr.unitPk " 
				+ " from unit_project_ref rootupr " 
				+ " join unit_project_ref_h uprh on uprh.rootParentPk = rootupr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
				+ " join unit_project_ref upr on upr.pk = uprh.unitInProjectPk "
				+ " where upr.unitPk = ? and upr.projectPk = ? " ;
		Integer val = persistWrapper.read(Integer.class, sql, unitOID.getPk(), projectOID.getPk());
		if(val == null)
			logger.error("Unable to find root unit Pk for unitOID:" + unitOID.getPk() + " on project:" + projectOID.getPk());
		
		return val;
	}

	public  UnitInProjectObj getUnitInProject(UnitInProjectOID uprOID)
	{
		return unitInProjectDAO.getUnitInProject(uprOID);
	}
	
	public  UnitInProjectObj getUnitInProject(UnitOID unitOID, ProjectOID projectOID)
	{
		return unitInProjectDAO.getUnitInProject(unitOID, projectOID);
	}
	
	public  boolean isUnitPartOfAnyProjects(UnitOID unitOID)
	{
		int count = persistWrapper.read(Integer.class, "select count(*) from unit_project_ref where unitPk = ?", unitOID.getPk());
		if(count == 0)
			return false;
		else
			return true;
		
	}

	public  boolean isUnitOpenInAnyProjects(UnitOID unitOID)
	{
		int count = persistWrapper.read(Integer.class, "select count(*) from unit_project_ref upr join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo"
				+ " where upr.unitPk = ? and uprh.status = ?", unitOID.getPk(), UnitInProject.STATUS_OPEN);
		if(count == 0)
			return false;
		else
			return true;
		
	}

	
	/*public static void moveUnitOrderUp(UserContext context, UnitOID unitOID, ProjectOID projectOID)
	{
		UnitInProjectDAO uprDAO = unitInProjectDAO;
		try 
		{
			UnitInProjectObj unitInProjectToMove = uprDAO.getUnitInProject(unitOID, projectOID);
			if(unitInProjectToMove != null)
			{
				UnitInProjectObj previousOne = uprDAO.getPreviousOneInOrder(projectOID, unitInProjectToMove);
				if(previousOne != null)
				{
					int orderNoTemp = previousOne.getOrderNo();
					previousOne.setOrderNo(unitInProjectToMove.getOrderNo());
					unitInProjectToMove.setOrderNo(orderNoTemp);
					uprDAO.saveUnitInProject(context, unitInProjectToMove, new Actions[][]{Actions.changeUnitOrder});
					uprDAO.saveUnitInProject(context, previousOne, null);
				}
			}
		} catch (Exception e) 
		{
			logger.error("Exception changing unit order", e);
			throw new AppException("Could not change unit order, please try again later");
		}
	}
*/
	/*public static void moveUnitOrderDown(UserContext context, UnitOID unitOID, ProjectOID projectOID)
	{
		UnitInProjectDAO uprDAO = unitInProjectDAO;
		try 
		{
//			Unit unit = PersistWrapper.readByPrimaryKey(Unit.class, unitOID.getPk());
			UnitInProjectObj unitInProjectToMove = uprDAO.getUnitInProject(unitOID, projectOID);
			if(unitInProjectToMove != null)
			{
				UnitInProjectObj nextOne = uprDAO.getNextOneInOrder(projectOID, unitInProjectToMove);
				if(nextOne != null)
				{
					int orderNoTemp = nextOne.getOrderNo();
					nextOne.setOrderNo(unitInProjectToMove.getOrderNo());
					unitInProjectToMove.setOrderNo(orderNoTemp);
					uprDAO.saveUnitInProject(context, unitInProjectToMove, new Actions[]{Actions.changeUnitOrder});
					uprDAO.saveUnitInProject(context, nextOne, null);
				}
			}
		} catch (Exception e) 
		{
			logger.error("Exception changing unit order", e);
			throw new AppException("Could not change unit order, please try again later");
		}
	}
	*/
	public  List<UnitQuery> getChildrenUnits(ProjectOID projectOID, UnitOID parentUnitOID)
	{
		if(parentUnitOID == null)
		{
			return persistWrapper.readList(UnitQuery.class, UnitQuery.sql + " where 1 = 1 and uprh.parentPk is null"
					, projectOID.getPk()
					, projectOID.getPk()
					, projectOID.getPk()
					);
		}
		else
		{
			UnitInProjectObj parentUpr = getUnitInProject(parentUnitOID, projectOID);
			return persistWrapper.readList(UnitQuery.class, UnitQuery.sql + " where 1 = 1 and uprh.parentPk = ?"
					, projectOID.getPk()
					, projectOID.getPk()
					, projectOID.getPk()
					, parentUpr.getPk());
		}
	}

	public static UnitEntityQuery getUnitEntityQueryByPk(UnitOID unitOID)
	{
		List<UnitEntityQuery> list = null;
		try
		{
			UnitEntityListReportRequest req = new UnitEntityListReportRequest();
			req.setUnitOID(unitOID);
			list = new UnitEntityListReport(req).runReport();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public  List<UnitQuery> getAllChildrenUnitsRecursive(UnitOID parentUnitOID, ProjectOID projectOID)
	{
		try
		{
			UnitInProjectObj unitInProject = getUnitInProject(parentUnitOID, projectOID);

			String sql = UnitQuery.sql + " where 1 = 1 and ( (u.pk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) ) ";
			List<UnitQuery> children = persistWrapper.readList(UnitQuery.class,
					sql, 
					projectOID.getPk(), 
					projectOID.getPk(), 
					projectOID.getPk(), 
					parentUnitOID.getPk(), unitInProject.getRootParentPk(), unitInProject.getHeiCode()+".%");
			
			return children;
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public  Unit getUnitBySerialNo(String serialNo)
	{
		try 
		{
			List<Unit> units = persistWrapper.readList(Unit.class, "select u.* from TAB_UNIT u "
					+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
					+ " where uh.serialNo = ?", serialNo);
			
			if(units == null || units.size() == 0)
				return null;
			else if(units.size() == 1)
				return units.get(0);
			else
			{
				logger.error("Multiple units found for serial no:" + serialNo + ", returning the first one");
				return units.get(0);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public  List<UnitQuery> getUnits(SerialNumberFilter serialNumberFilter)
	{
		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(UnitQuery.sql);
		params.add(serialNumberFilter.getProjectOID().getPk());
		params.add(serialNumberFilter.getProjectOID().getPk());
		params.add(serialNumberFilter.getProjectOID().getPk());

		if(serialNumberFilter.getProjectOID() == null)
		{
			throw new AppException("Project is mandatory to load units.");
		}
		
		sb.append(" where 1 = 1 ");
		
		if(serialNumberFilter.getPartOID() != null)
		{
			sb.append(" and u.partPk = ?");
			params.add(serialNumberFilter.getPartOID().getPk());
		}
		if(serialNumberFilter.getSearchString() != null && serialNumberFilter.getSearchString().trim().length() > 0)
		{
			sb.append(" and (upper(uh.unitName) like ? or upper(uh.serialNo) like ?)");
			params.add("%"+serialNumberFilter.getSearchString().trim()+"%");
			params.add("%"+serialNumberFilter.getSearchString().trim()+"%");
		}
		if(serialNumberFilter.getUnitInProjectStatus() != null && serialNumberFilter.getUnitInProjectStatus().length > 0)
		{
			sb.append(" and uprh.status in (");
			String sep = "";
			for (int i = 0; i < serialNumberFilter.getUnitInProjectStatus().length; i++)
			{
				sb.append(sep).append("?");
				params.add(serialNumberFilter.getUnitInProjectStatus()[i]);
				sep = ", ";
			}
			sb.append(")");
		}
		if(serialNumberFilter.isShowTopLevelOnly())
		{
			sb.append(" and uprh.parentPk is null");
		}
		
		return persistWrapper.readList(UnitQuery.class, sb.toString(), params.toArray());
	}
	
	public  void changeUnitParent(UserContext userContext,
			UnitOID selectedParent, UnitOID unitToChangeOID, ProjectOID projectOID) throws Exception
	{
		UnitInProjectDAO uprDAO = unitInProjectDAO;
		UnitInProjectObj childUPR = getUnitInProject(unitToChangeOID, projectOID);
		UnitInProjectObj selectedParentUPR = null;
		UnitInProjectOID selectedParentUPROID = null;
		
		//first check of the unit is already a child of the same parent, then nothing to be done
		UnitOID currentParentOID = null;
		if(childUPR.getParentPk() != null)
		{
			UnitInProjectObj currentParentUPR = uprDAO.getUnitInProject(new UnitInProjectOID(childUPR.getParentPk()));
			currentParentOID = new UnitOID(currentParentUPR.getUnitPk());
		}
		if(Objects.equals(currentParentOID, selectedParent))
		{
			// they are the same, so return without doing anything.
			return;
		}
		
		// the unit to be moved could be open or closed,
		//either both should be open or both should be closed.
		if(childUPR == null)
		{
			throw new AppException("Unit to move is not part of the project.");
		}
		
		if(selectedParent != null)
		{
			selectedParentUPR = getUnitInProject(selectedParent, projectOID);
			if(selectedParentUPR == null)
			{
				throw new AppException("Selected parent is not part of the project.");
			}
			if((UnitInProject.STATUS_OPEN.equals(selectedParentUPR.getStatus())) && !(UnitInProject.STATUS_OPEN.equals(childUPR.getStatus())))
			{
				throw new AppException("You cannot move a Closed unit under an Open unit.");
			}
			if((UnitInProject.STATUS_CLOSED.equals(selectedParentUPR.getStatus())) && !(UnitInProject.STATUS_CLOSED.equals(childUPR.getStatus())))
			{
				throw new AppException("You cannot move an Open unit under a Closed unit. ");
			}
			selectedParentUPROID = selectedParentUPR.getOID(); 
		}
		changeUnitParentInt(userContext, uprDAO, selectedParentUPROID, childUPR.getOID(), projectOID);
		
		return;
	}

	private static void changeUnitParentInt(UserContext userContext, UnitInProjectDAO uprDAO, 
			UnitInProjectOID selectedParentOID, UnitInProjectOID unitToChangeOID, ProjectOID projectOID) throws Exception
	{
		UnitInProjectObj unitToChange = uprDAO.getUnitInProject(unitToChangeOID);
		List<UnitInProjectObj> allChildrenOfUnitToChange = uprDAO.getAllChildrenInTree(unitToChangeOID);

		UnitInProjectObj selectedParent = null;
		if(selectedParentOID != null)
		{
			selectedParent = uprDAO.getUnitInProject(selectedParentOID);
		}

		UnitInProjectObj currentParent = null;
		if(unitToChange.getParentPk() != null && unitToChange.getParentPk() != 0)
		{
			currentParent = uprDAO.getUnitInProject(new UnitInProjectOID(unitToChange.getParentPk()));
			if(currentParent == null) // just a sanity check to confirm that the current parentPk is valid
			{
				logger.error("Invalid parentPk for unitInProject with pk:" + unitToChangeOID.getPk());
				throw new Exception("Could not change parent for unit, please contact your administrator");
			}
		}
		
		//save the current level
		int currentLevel = unitToChange.getLevel();
		String currentHeiCode = unitToChange.getHeiCode();
		int newLevel = 0;
		String newHeiCode = null;
		int newRootParentPk = 0;
		if(selectedParent == null || selectedParent.getPk() == 0)
		{
			newLevel = 0;
			newHeiCode = Base62Util.encode(unitToChange.getPk());
			newRootParentPk = unitToChange.getPk();
			
			unitToChange.setParentPk(null);
			unitToChange.setRootParentPk(newRootParentPk);
			unitToChange.setLevel(newLevel);
			unitToChange.setHeiCode(newHeiCode);
			uprDAO.saveUnitInProject(userContext, unitToChange, new Actions[]{Actions.changeUnitParent});
		}
		else
		{
			newLevel = selectedParent.getLevel() + 1;
			newHeiCode = selectedParent.getHeiCode() + "." + Base62Util.encode((int) unitToChangeOID.getPk());
			newRootParentPk = selectedParent.getRootParentPk();
			
			unitToChange.setParentPk(selectedParent.getPk());
			unitToChange.setRootParentPk(newRootParentPk);
			unitToChange.setLevel(newLevel);
			unitToChange.setHeiCode(newHeiCode);
			uprDAO.saveUnitInProject(userContext, unitToChange, new Actions[]{Actions.changeUnitParent});
			
			
			//update the new parent with hasChildren = true
			if(selectedParent.getHasChildren() == false)
			{
				selectedParent.setHasChildren(true);
				uprDAO.saveUnitInProject(userContext, selectedParent);
			}
		}
		// get all the children of the unitToChange and update their levels and heiCode.
		for (Iterator iterator = allChildrenOfUnitToChange.iterator(); iterator.hasNext();)
		{
			UnitInProjectObj aChild = (UnitInProjectObj) iterator.next();
			
			aChild.setRootParentPk(newRootParentPk);
			aChild.setLevel(aChild.getLevel() + (newLevel-currentLevel));
			String newChildHeiCode = aChild.getHeiCode().replace(currentHeiCode+".", newHeiCode+".");
			aChild.setHeiCode(newChildHeiCode);
			uprDAO.saveUnitInProject(userContext, aChild);
		}
		
		//now check if the old parent still has more children
		if(currentParent != null)
		{
			Integer currentParentChildCount = uprDAO.getCountDirectChildren(currentParent.getOID());
			if(currentParentChildCount == 0)
			{
				currentParent.setHasChildren(false);
				uprDAO.saveUnitInProject(userContext, currentParent);
			}
		}
	}
	

	/**
	 * This method does not consider the status of the unit inside the project. so i think it does not work properly... check before using it.
	 * @param unitOID
	 * @return
	 */
	public  List<ProjectQuery> getUnitAssignedProjects(UnitOID unitOID)
	{
		String sql = ProjectQuery.fetchSQL 
				+ " join unit_project_ref upr on upr.projectPk = project.pk and upr.unitPk = ?";
		return persistWrapper.readList(ProjectQuery.class, sql, unitOID.getPk());
	}

	public  List<UnitInProjectQuery> getProjectAssignmentsForUnit(UnitOID unitOID)
	{
		return persistWrapper.readList(UnitInProjectQuery.class, UnitInProjectQuery.sql + " and u.pk = ? order by addedDate ", unitOID.getPk());
	}

	public  UnitQuery addUnitBookMark(UserContext context, int unitPk, int projectPk, UnitBookmark.BookmarkModeEnum mode)throws Exception
	{
		UnitBookmark bookmark = persistWrapper.read(UnitBookmark.class,
				"select * from unit_bookmark where userFk= ? and unitFk = ? and projectFk = ?", 
				context.getUser().getPk(), unitPk, projectPk);
		
		int pk;
		if(bookmark != null)
		{
			pk= (int) bookmark.getPk();
			//bookmark is already there.
			if(bookmark.getMode().equals(UnitBookmark.BookmarkModeEnum.ByUser.name()))
			{
				//already added by user , ignore
			}
			else if(bookmark.getMode().equals(UnitBookmark.BookmarkModeEnum.Auto.name()))
			{
				if(UnitBookmark.BookmarkModeEnum.ByUser.name().equals(mode.name()))
				{
					// upgrade to an ByUser bookmark
					bookmark.setMode(mode.name());
					persistWrapper.update(bookmark);
				}
				else
				{
					// set the createdDate to now so that it comes in front in the queue
					bookmark.setCreatedDate(new Date());
					persistWrapper.update(bookmark);
				}
			}
		}
		else
		{
			if(UnitBookmark.BookmarkModeEnum.ByUser.name().equals(mode.name()))
			{
				//user is adding a bookmark. so just add
				UnitBookmark bookmarkNew = new UnitBookmark();
				bookmarkNew.setCreatedDate(new Date());
				bookmarkNew.setMode(mode.name());
				bookmarkNew.setProjectFk(projectPk);
				bookmarkNew.setUnitFk(unitPk);
				bookmarkNew.setUserFk(context.getUser().getPk());
				pk= (int) persistWrapper.createEntity(bookmarkNew);
			}
			else
			{
				// a new bookmark is getting auto added. 
				List<UnitBookmark> currentList = persistWrapper.readList(UnitBookmark.class,
						"select * from unit_bookmark where userFk = ? and mode = ? order by createdDate", 
						context.getUser().getPk(), UnitBookmark.BookmarkModeEnum.Auto.name());
				if(currentList != null && currentList.size() > 9) // keep 10 auto bookmarks per user
				{
					//remove the first one and add the new one
					
					UnitBookmark first = currentList.get(0);
					persistWrapper.deleteEntity(first);
				}	
					
				//now add a new one, adding happens without removal if the current list is still not 10
				UnitBookmark bookmarkNew = new UnitBookmark();
				bookmarkNew.setCreatedDate(new Date());
				bookmarkNew.setMode(mode.name());
				bookmarkNew.setProjectFk(projectPk);
				bookmarkNew.setUnitFk(unitPk);
				bookmarkNew.setUserFk(context.getUser().getPk());
				pk= (int) persistWrapper.createEntity(bookmarkNew);
			}
		}
		return getBookmarkedUnit(pk);
		
	}
	
	public  UnitQuery getBookmarkedUnit(int pk)
	{
		StringBuffer sb = new StringBuffer("select u.pk as unitPk, upr.unitOriginType as unitOriginTypeString, "
				+ " part_revision.pk as partRevisionPk, part_revision.revision as partRevision, "
				+ " part.pk as partPk, part.name as partName, part.partNo as partNo, "
				+ " u.supplierFk, u.siteGroupFk, "
				+ " uh.unitName, uh.serialNo, uh.unitDescription as description, u.lastUpdated as lastUpdatedDate, "
				+ " u.createdDate as createDate, createdByUser.firstName as createdByFirstName, createdByUser.lastName as createdByLastName, "
				+ " uprh.projectPartPk, project_part.name as projectPartName, upr.projectPk, p.projectName, part_type.typeName as partType, uprh.status as status, "
				+ " uprh.orderNo, "
				+ " parentUnitPkRef.unitPk as parentPk, rootParentUnitPkRef.unitPk as rootParentPk, "
				+ " uprh.level, uprh.heiCode, uprh.hasChildren, "
				+ " unitOpenInProjectTab.projectPk as projectPkUnitIsOpen, unitOpenInProjectTab.projectName as projectNameUnitIsOpen, unitOpenInProjectTab.projectDescription as projectDescriptionUnitIsOpen "
				+ " from unit_bookmark ubkm"
				+ " join unit_project_ref upr on upr.unitPk = ubkm.unitFk and upr.projectPk = ubkm.projectFk "
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
				+ " join TAB_UNIT u on ubkm.unitFk = u.pk "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join TAB_PROJECT p on ubkm.projectFk = p.pk "
				+ " join TAB_USER createdByUser on u.createdBy=createdByUser.pk "
				+ " join part on u.partPk = part.pk "
				+ " left join part_revision on u.partRevisionPk = part_revision.pk"
				+ " left outer join (select pk, unitPk,projectPk from unit_project_ref  ) parentUnitPkRef on parentUnitPkRef.pk = uprh.parentPk and parentUnitPkRef.projectPk = ubkm.projectFk "
				+ " left outer join (select pk, unitPk,projectPk from unit_project_ref ) rootParentUnitPkRef on rootParentUnitPkRef.pk = uprh.rootParentPk and rootParentUnitPkRef.projectPk = ubkm.projectFk "
				+ " left outer join (select unitPk, projectPk, projectName, projectDescription from unit_project_ref upr2, unit_project_ref_h uprh2, TAB_PROJECT p2 where uprh2.unitInProjectPk = upr2.pk and now() between uprh2.effectiveDateFrom and uprh2.effectiveDateTo and upr2.projectPk = p2.pk and uprh2.status = '"+UnitInProject.STATUS_OPEN+"' ) unitOpenInProjectTab on unitOpenInProjectTab.unitPk = u.pk "
				+ " left outer join project_part on uprh.projectPartPk = project_part.pk "
				+ " left outer join part_type on project_part.partTypePk = part_type.pk "
				+ " where ubkm.pk = ?");
		return persistWrapper.read(UnitQuery.class, sb.toString(),pk);
		
	}
	
	
	public  List<UnitQuery> getBookmarkedUnits(UserContext context, UnitBookmark.BookmarkModeEnum mode)
	{
		StringBuffer sb = new StringBuffer("select u.pk as unitPk, upr.unitOriginType as unitOriginTypeString, "
				+ " part_revision.pk as partRevisionPk, part_revision.revision as partRevision, "
				+ " part.pk as partPk, part.name as partName, part.partNo as partNo, "
				+ " u.supplierFk, u.siteGroupFk, "
				+ " uh.unitName, uh.serialNo, uh.unitDescription as description, u.lastUpdated as lastUpdatedDate, "
				+ " u.createdDate as createDate, createdByUser.firstName as createdByFirstName, createdByUser.lastName as createdByLastName, "
				+ " uprh.projectPartPk, project_part.name as projectPartName, upr.projectPk, p.projectName, part_type.typeName as partType, uprh.status as status, "
				+ " uprh.orderNo, "
				+ " parentUnitPkRef.unitPk as parentPk, rootParentUnitPkRef.unitPk as rootParentPk, "
				+ " uprh.level, uprh.heiCode, uprh.hasChildren, "
				+ " unitOpenInProjectTab.projectPk as projectPkUnitIsOpen, unitOpenInProjectTab.projectName as projectNameUnitIsOpen, unitOpenInProjectTab.projectDescription as projectDescriptionUnitIsOpen "
				+ " from unit_bookmark ubkm"
				+ " join unit_project_ref upr on upr.unitPk = ubkm.unitFk and upr.projectPk = ubkm.projectFk "
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
				+ " join TAB_UNIT u on ubkm.unitFk = u.pk "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join TAB_PROJECT p on ubkm.projectFk = p.pk "
				+ " join TAB_USER createdByUser on u.createdBy=createdByUser.pk "
				+ " join part on u.partPk = part.pk "
				+ " left join part_revision on u.partRevisionPk = part_revision.pk"
				+ " left outer join (select pk, unitPk,projectPk from unit_project_ref  ) parentUnitPkRef on parentUnitPkRef.pk = uprh.parentPk and parentUnitPkRef.projectPk = ubkm.projectFk "
				+ " left outer join (select pk, unitPk,projectPk from unit_project_ref ) rootParentUnitPkRef on rootParentUnitPkRef.pk = uprh.rootParentPk and rootParentUnitPkRef.projectPk = ubkm.projectFk "
				+ " left outer join (select unitPk, projectPk, projectName, projectDescription from unit_project_ref upr2, unit_project_ref_h uprh2, TAB_PROJECT p2 where uprh2.unitInProjectPk = upr2.pk and now() between uprh2.effectiveDateFrom and uprh2.effectiveDateTo and upr2.projectPk = p2.pk and uprh2.status = '"+UnitInProject.STATUS_OPEN+"' ) unitOpenInProjectTab on unitOpenInProjectTab.unitPk = u.pk "
				+ " left outer join project_part on uprh.projectPartPk = project_part.pk "
				+ " left outer join part_type on project_part.partTypePk = part_type.pk "
				+ " where ubkm.userFk = ? and ubkm.mode = ? ");
		
		List<UnitQuery> list = persistWrapper.readList(UnitQuery.class, sb.toString(),
				context.getUser().getPk(), mode.name());
		
		return list;
		
	}
	
	public  void removeUnitBookmark(UserContext context, int unitPk, int projectPk) throws Exception {
		UnitBookmark bookmark = persistWrapper.read(UnitBookmark.class,
				"select * from unit_bookmark where userFk= ? and unitFk = ? and projectFk = ?", 
				context.getUser().getPk(), unitPk, projectPk);
		
		if(bookmark != null)
			persistWrapper.deleteEntity(bookmark);
	}
	
}
