package com.tathvatech.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.entity.EntityConfigData;
import com.tathvatech.common.entity.EntityVersion;
import com.tathvatech.common.enums.VersionableEntity;



public class CommonServiceManager
{
	public static void saveSnapshot(UserContext context, VersionableEntity versionableEntity)
	{
		saveSnapshot(context, null, versionableEntity);
	}

	public static void saveSnapshot(UserContext context, String versionContext, VersionableEntity versionableEntity)
	{
		try {
			EntityVersion v = new EntityVersion();
			v.setBackupString(((VersionableEntity)versionableEntity).getBackupString());
			
			if(versionContext != null && versionContext.trim().length() > 0)
				v.setVersionContext(versionContext);
			
			if(context != null)
				v.setCreatedBy(context.getUser().getPk());
			v.setCreatedDate(new Date());
			v.setEntityPk(((VersionableEntity) versionableEntity).getEntityPk());
			v.setEntityType(((VersionableEntity) versionableEntity).getEntityType().getValue());
			PersistWrapper.createEntity(v);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public <T> Object getEntityPropertyValue(OID entityOID, String property, Integer intParam1, String stringParam1, Class<T> type)
	{
		StringBuilder sb = new StringBuilder("select value from entity_config_data where objectPk=? and objectType=? and property=?");
		List params = new ArrayList<>();
		params.add(entityOID.getPk());
		params.add(entityOID.getEntityType().getValue());
		params.add(property);
		
		if(intParam1 != null)
		{
			sb.append(" and intParam1 = ? ");
			params.add(intParam1);
		}
		else
			sb.append(" and intParam1 is null ");
		
		if(stringParam1 != null)
		{
			sb.append(" and stringParam1 = ? ");
			params.add(stringParam1);
		}
		else
			sb.append(" and stringParam1 is null ");

		String val = PersistWrapper.read(String.class, 
				sb.toString(), params.toArray());
		
		if(type == Boolean.class)
		{	
			if(val == null)
				return false;
			return new Boolean(val);
		}
		else if(type == Integer.class)
		{
			if(val == null)
				return 0;
			return Integer.parseInt(val);
		}
		else if (type == Float.class)
		{
			if(val == null)
				return 0;
			return Float.parseFloat(val);
		}
		else
			return val;
	}

	/**
	 * 
	 * @param entityOID
	 * @param property
	 * @param intParam1 pass null if intParam1 is not required
	 * @param stringParam1 pass null if stringParam1 is not required
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static EntityConfigData saveEntityConfig(OID entityOID, String property, Integer intParam1, String stringParam1, String value) throws Exception
	{
		StringBuilder sb = new StringBuilder("select * from entity_config_data where "
				+ " objectPk=? and objectType=? and property=?");
		List params = new ArrayList<>();
		params.add(entityOID.getPk());
		params.add(entityOID.getEntityType().getValue());
		params.add(property);
		
		if(intParam1 != null)
		{
			sb.append(" and intParam1 = ? ");
			params.add(intParam1);
		}
		else
			sb.append(" and intParam1 is null ");
		
		if(stringParam1 != null)
		{
			sb.append(" and stringParam1 = ? ");
			params.add(stringParam1);
		}
		else
			sb.append(" and stringParam1 is null ");

		EntityConfigData cdata = PersistWrapper.read(EntityConfigData.class,
				sb.toString(), params.toArray());
		
		if(cdata == null)
			cdata = new EntityConfigData();
		
		cdata.setObjectPk(entityOID.getPk());
		cdata.setObjectType(entityOID.getEntityType().getValue());
		cdata.setProperty(property);
		cdata.setIntParam1(intParam1);
		cdata.setStringParam1(stringParam1);
		cdata.setValue(value);
		
		int pkRet = 0;
		if(cdata.getPk() == 0)
			pkRet = PersistWrapper.createEntity(cdata);
		else
		{
			PersistWrapper.update(cdata);
			pkRet = cdata.getPk();
		}
		return PersistWrapper.readByPrimaryKey(EntityConfigData.class, pkRet);
	}

	public static void removeEntityConfig(OID entityOID, String property, Integer intParam1, String stringParam1) throws Exception
	{
		StringBuilder sb = new StringBuilder("select * from entity_config_data where "
				+ " objectPk=? and objectType=? and property=?");
		List params = new ArrayList<>();
		params.add(entityOID.getPk());
		params.add(entityOID.getEntityType().getValue());
		params.add(property);
		
		if(intParam1 != null)
		{
			sb.append(" and intParam1 = ? ");
			params.add(intParam1);
		}
		else
			sb.append(" and intParam1 is null ");
		
		if(stringParam1 != null)
		{
			sb.append(" and stringParam1 = ? ");
			params.add(stringParam1);
		}
		else
			sb.append(" and stringParam1 is null ");


		EntityConfigData cdata = PersistWrapper.read(EntityConfigData.class,
				sb.toString(), params.toArray());
		
		if(cdata != null)
			PersistWrapper.deleteEntity(cdata);
	}

	public static UserPreferencesData saveUserPreferenceData(UserPreferencesDataBean bean) throws Exception
	{
		if(StringUtils.isEmpty(bean.getValue()))
			throw new AppException("Value string cannot be empty");
		if(StringUtils.isEmpty(bean.getName()))
			throw new AppException("Name cannot be empty");
		if(bean.getUserOID() == null)
			throw new AppException("User information cannot be empty");
		
		UserPreferencesData data = new UserPreferencesData();
		if(bean.getPk() == 0)
		{
			data.setCreatedDate(new Date());
		}
		else
		{
			data.setCreatedDate(bean.getCreatedDate());
		}
		if(bean.getAnchorEntityOID() != null)
		{
			data.setEntityPk(bean.getAnchorEntityOID().getPk());
			data.setEntityType(bean.getAnchorEntityOID().getEntityType().getValue());
		}
		data.setName(bean.getName());
		data.setPk(bean.getPk());
		data.setProperty(bean.getProperty());
		data.setUserPk(bean.getUserOID().getPk());
		data.setValue(bean.getValue());
		
		int pkRet = 0;
		if(data.getPk() == 0)
			pkRet = PersistWrapper.createEntity(data);
		else
		{
			PersistWrapper.update(data);
			pkRet = data.getPk();
		}
		return PersistWrapper.readByPrimaryKey(UserPreferencesData.class, pkRet);
	}
	
	public static List<UserPreferencesData> getUserPreferenceData(UserOID userOID, OID anchorObjectOID, String property)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("select * from user_preferences_data where userPk = ? ");
		params.add(userOID.getPk());
		
		if(anchorObjectOID != null)
		{
			sb.append(" and entityPk= ? and entityType = ? ");
			params.add(anchorObjectOID.getPk());
			params.add(anchorObjectOID.getEntityType().getValue());
		}
		else
		{
			sb.append(" and entityPk is null and entityType is null ");
		}
		sb.append(" and property=? ");
		params.add(property);
		
		return PersistWrapper.readList(UserPreferencesData.class, sb.toString(), params.toArray());
	}

	public static List<EntityReference> getEntityReferences(OID fromOID) 
	{
		List currentList = PersistWrapper.readList(EntityReference.class, "select * from reference where referenceFromPk = ? and referenceFromType = ?", 
				fromOID.getPk(), fromOID.getEntityType().getValue());
		return currentList;
	}
	
	public static void createEntityReference(UserContext context, OID fromOID, OID toOID)throws Exception
	{
		List currentList = PersistWrapper.readList(EntityReference.class, "select * from reference where referenceFromPk = ? and referenceFromType = ? and "
				+ " referenceToPk = ? and referenceToType = ?", 
				fromOID.getPk(), fromOID.getEntityType().getValue(), toOID.getPk(), toOID.getEntityType().getValue());
		if(currentList != null && currentList.size() > 0)
			return;
		
		EntityReference ref = new EntityReference();
		ref.setReferenceFromPk(fromOID.getPk());
		ref.setReferenceFromType(fromOID.getEntityType().getValue());
		ref.setReferenceToPk(toOID.getPk());
		ref.setReferenceToType(toOID.getEntityType().getValue());
		ref.setCreatedBy(context.getUser().getPk());
		ref.setCreatedDate(new Date());
		PersistWrapper.createEntity(ref);
	}
	
	public static List getAttachments(int objectPk, int objectType) 
	{
		return PersistWrapper.readList(Attachment.class, "select * from TAB_ATTACHMENT where objectPk=? and objectType=? and estatus != 9 ", 
				objectPk, objectType);
	}

	public static List getAttachments(Integer[] objectPkList, int objectType) 
	{
		String pks = Arrays.deepToString(objectPkList);
		pks = pks.replace('[', '(');
		pks = pks.replace(']', ')');
		return PersistWrapper.readList(Attachment.class, "select * from TAB_ATTACHMENT where objectPk in " + pks + " and objectType=?", 
				objectType);
	}

	public static List<Attachment> getAttachments(int objectPk, int objectType, String attachmentcontext)
	{
		if(attachmentcontext == null)
		{
			return PersistWrapper.readList(Attachment.class, "select * from TAB_ATTACHMENT where objectPk=? and objectType=? and attachContext is null and estatus != 9 "
					+ " order by createdDate ", 
					objectPk, objectType);
		}
		else
		{
			return PersistWrapper.readList(Attachment.class, "select * from TAB_ATTACHMENT where objectPk=? and objectType=? and attachContext=? and estatus != 9 "
				+ " order by createdDate ", 
				objectPk, objectType, attachmentcontext);
		}
	}

	
	public static void addAttachments(UserContext context, int objectPk, int objectType,
			List<AttachmentIntf> attachedFiles) throws Exception 
	{
		if(attachedFiles == null)
			return;
		
		for (Iterator iterator = attachedFiles.iterator(); iterator.hasNext();) 
		{
			Attachment attachment = (Attachment) iterator.next();
			if(attachment.getPk() != 0)
				continue; // we only create attachments here.

				attachment.setCreatedDate(new Date());
				attachment.setCreatedBy(context.getUser().getPk());
				attachment.setObjectType(objectType);
				attachment.setObjectPk(objectPk);
				PersistWrapper.createEntity(attachment);
		}
	}

	
	/**
	 * 
	 * @param context
	 * @param objectPk
	 * @param objectType
	 * @param attachedFiles files to attach to the object, if the list is null, no files are added or removed.
	 * @param deleteItemsNotInList attachements not in the passed list which area already available under this object is removed if deleteItemsNotInList is true. They are only added or updated
	 * @throws Exception
	 */
	public static void saveAttachments(UserContext context, int objectPk, int objectType,
			List<AttachmentIntf> attachedFiles, boolean deleteItemsNotInList) throws Exception 
	{
		if(attachedFiles == null)
			return;
		List currentAtts = getAttachments(objectPk, objectType);
		
		for (Iterator iterator = attachedFiles.iterator(); iterator.hasNext();) 
		{
			Attachment attachment = (Attachment) iterator.next();
			if(attachment.getPk() == 0)
			{
				attachment.setCreatedDate(new Date());
				attachment.setCreatedBy(context.getUser().getPk());
				attachment.setObjectType(objectType);
				attachment.setObjectPk(objectPk);
				int i=PersistWrapper.createEntity(attachment);
				System.out.println(i);
			}
			else
			{
				PersistWrapper.update(attachment);
				currentAtts.remove(attachment);
			}
		}
		
		if(deleteItemsNotInList)
		{
			for (Iterator iterator = currentAtts.iterator(); iterator.hasNext();) {
				Attachment aAtt = (Attachment) iterator.next();
				PersistWrapper.deleteEntity(aAtt);
			}
		}
	}

	public static void saveAttachments(UserContext context, int objectPk, int objectType, String attachmentContext,
			List<? extends AttachmentIntf> attachedFiles) throws Exception 
	{
		List currentAtts = getAttachments(objectPk, objectType, attachmentContext);
		
		for (Iterator iterator = attachedFiles.iterator(); iterator.hasNext();) 
		{
			Attachment attachment = (Attachment) iterator.next();
			if(attachment.getPk() == 0)
			{
				attachment.setCreatedDate(new Date());
				attachment.setCreatedBy(context.getUser().getPk());
				attachment.setObjectType(objectType);
				attachment.setObjectPk(objectPk);
				PersistWrapper.createEntity(attachment);
			}
			else
			{
				PersistWrapper.update(attachment);
				currentAtts.remove(attachment);
			}
		}
		
		for (Iterator iterator = currentAtts.iterator(); iterator.hasNext();) 
		{
			Attachment aAtt = (Attachment) iterator.next();
			PersistWrapper.deleteEntity(aAtt);
		}
		
	}

	public static void removeAttachment(UserContext userContext, Attachment attachment) throws Exception
	{
		String fileName = attachment.getFileName();
		
		PersistWrapper.deleteEntity(attachment);

		File aFile = FileStoreManager.getFile(fileName);
		aFile.delete();
	}
		
	
}
