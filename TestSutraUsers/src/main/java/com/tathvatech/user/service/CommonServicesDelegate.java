package com.tathvatech.user.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import com.tathvatech.common.common.EntityVersionFilter;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.entity.EntityConfigData;
import com.tathvatech.common.entity.EntityVersion;
import com.tathvatech.common.entity.TSTimeZone;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.OIDGeneric;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Attachment;
import com.tathvatech.user.entity.UserPreferencesData;
import com.tathvatech.user.entity.UserPreferencesDataBean;
import com.tathvatech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommonServicesDelegate 
{
	
	 private final PersistWrapper persistWrapper;
	 private final CommonServiceManager commonServiceManager;
	
	public List<TSTimeZone> getSupportedTimeZoneIDs()
	{
		List<TSTimeZone> ts = persistWrapper.readList(TSTimeZone.class, "select * from timezones order by id", null);
		for (Iterator iterator = ts.iterator(); iterator.hasNext();) 
		{
			TSTimeZone tsTimeZone = (TSTimeZone) iterator.next();
			if(tsTimeZone.getDescription() == null)
				tsTimeZone.setDescription(displayTimeZone(tsTimeZone.getId()));
		}
		return ts;
	}
	
	
	private  String displayTimeZone(String id) 
	{
		Date now = new Date();
		TimeZone tz = TimeZone.getTimeZone(id);
		
		long hours = TimeUnit.MILLISECONDS.toHours(tz.getOffset(now.getTime()));
		
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getOffset(now.getTime())) 
                                  - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) 
		{
			result = String.format("%s - %s (GMT+%d:%02d)",  tz.getDisplayName(tz.inDaylightTime(now), TimeZone.LONG), tz.getID(), hours, minutes);
		}
		else 
		{
			result = String.format("%s - %s (GMT%d:%02d)",  tz.getDisplayName(tz.inDaylightTime(now), TimeZone.LONG), tz.getID(), hours, minutes);
		}

		return result;

	}
	
	public  List<EntityVersion> getEntityVersions(EntityVersionFilter filter)
	{
		if(filter.getEntityType() == null)
			throw new AppException("Invalid entityType");
		if(filter.getEntityPk() == null)
			throw new AppException("Invalid entity");
			
		
		List<Object> params = new ArrayList();

		StringBuffer sb = new StringBuffer("select * from entity_version where entityType = ? and entityPk = ? ");
		params.add(filter.getEntityType().getValue());
		params.add(filter.getEntityPk());
		if(filter.getVersionContext() != null)
		{
			sb.append(" and versionContext = ?");
			params.add(filter.getVersionContext());
		}
		if(filter.getCreatedDateFrom() != null)
		{
			sb.append(" and createdDate > ?");
			params.add(filter.getCreatedDateFrom());
		}
		if(filter.getCreatedDateTo() != null)
		{
			sb.append(" and createdDate < ?");
			params.add(filter.getCreatedDateTo());
		}
		
		sb.append(" order by createdDate desc ");
		
		if(filter.getLimitFetchRecords() != null)
			sb.append(" limit 0, " + filter.getLimitFetchRecords());
		
		return persistWrapper.readList(EntityVersion.class, sb.toString(), params.toArray());
	}
	
	public Object getObjectByPk(Class class1, int pk)
	{


		return persistWrapper.readByPrimaryKey(class1, pk);
	}

	public  EntityConfigData saveEntityConfig(OID entityOID, String property, String value) throws Exception
	{
		return saveEntityConfig(entityOID, property, null, null, value);
	}
	
	public  EntityConfigData saveEntityConfig(OID entityOID, String property, Integer intParam1, String stringParam1, String value) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            EntityConfigData e = commonServiceManager.saveEntityConfig(entityOID, property, intParam1, stringParam1, value);
            con.commit();
            return e;
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}
	
	
	public <T> Object getEntityPropertyValue(OID entityOID, String property, Class<T> type)
	{
		return  commonServiceManager.getEntityPropertyValue(entityOID, property, null, null, type);
	}

	public <T> Object getEntityPropertyValue(OID entityOID, String property, Integer intParam1, String stringParam1, Class<T> type)
	{
		return  commonServiceManager.getEntityPropertyValue(entityOID, property, intParam1, stringParam1, type);
	}
	

	public  void removeEntityConfig(OID entityOID, String property, Integer intParam1, String stringParam1) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            commonServiceManager.removeEntityConfig(entityOID, property, intParam1, stringParam1);
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}
	
	public  String saveImageAsByteArrayToFileStore(String fileNameOrginal, String imageString)throws Exception
	{
    	if(imageString != null && imageString.length() > 0)
    	{
			try 
			{
				int dotIndex = fileNameOrginal.lastIndexOf('.');
				String fileNamePart = null;
				String sufixPart = null;
				if(dotIndex == -1)
				{
					fileNamePart = fileNameOrginal;
					sufixPart = "jpg";
				}
				else
				{
					fileNamePart = fileNameOrginal.substring(0, dotIndex-1);
					sufixPart = fileNameOrginal.substring(dotIndex+1);
				}
				String fileName = fileNamePart + "_" + new Date().getTime()+"." + sufixPart;
				File file = FileStoreManager.getFile(fileName);
				byte[] bytes = Base64.decodeBase64(imageString.getBytes());
	            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
	            ImageIO.write(bufferedImage, sufixPart, file);
				return fileName;
			}
			catch(Exception e)
			{
				e.printStackTrace();
	            throw new Exception("Image could not be saved, Please try again later");
			}
    	}
    	return null;
	}
	
	public  UserPreferencesDataBean saveUserPreferenceData(UserPreferencesDataBean bean)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            UserPreferencesData e = commonServiceManager.saveUserPreferenceData(bean);
            
            UserPreferencesDataBean b = new UserPreferencesDataBean();
            b.setCreatedDate(e.getCreatedDate());
            b.setLastUpdated(e.getLastUpdated());
            b.setName(e.getName());
            b.setPk(e.getPk());
            b.setProperty(e.getProperty());
            if(e.getEntityPk() != null && e.getEntityType() != null)
            {
            	b.setAnchorEntityOID(new OIDGeneric(e.getEntityPk(), EntityTypeEnum.fromValue(e.getEntityType())));
            }
            b.setUserOID(UserRepository.getInstance().getUser(e.getUserPk()).getOID());
            b.setValue(e.getValue());
            
            con.commit();
            return b;
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
	}
	
	public  List<UserPreferencesDataBean> getUserPreferenceData(UserOID userOID, OID anchorObjectOID, String property)
	{
		List<UserPreferencesDataBean> returnList = new ArrayList<UserPreferencesDataBean>();
		List<UserPreferencesData> list = commonServiceManager.getUserPreferenceData(userOID, anchorObjectOID, property);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UserPreferencesData e = (UserPreferencesData) iterator.next();
			
            UserPreferencesDataBean b = new UserPreferencesDataBean();
            b.setCreatedDate(e.getCreatedDate());
            b.setLastUpdated(e.getLastUpdated());
            b.setName(e.getName());
            b.setPk(e.getPk());
            b.setProperty(e.getProperty());
            if(e.getEntityPk() != null && e.getEntityType() != null)
            {
            	b.setAnchorEntityOID(new OIDGeneric(e.getEntityPk(), EntityTypeEnum.fromValue(e.getEntityType())));
            }
            b.setUserOID(UserRepository.getInstance().getUser(e.getUserPk()).getOID());
            b.setValue(e.getValue());
            
            returnList.add(b);
		}
		return returnList;
	}


	public  List getEntityReferences(TestProcOID oid)
	{
		return commonServiceManager.getEntityReferences(oid);
	}	
	
	public  List getAttachments(int objectPk, int objectType)  
	{
		return commonServiceManager.getAttachments(objectPk, objectType);
	}

	public  List getAttachments(Integer[] objectPks, int objectType)  
	{
		return commonServiceManager.getAttachments(objectPks, objectType);
	}

	public  List getAttachments(int objectPk, int objectType,
			String attachmentcontext) throws Exception 
	{
		return commonServiceManager.getAttachments(objectPk, objectType, attachmentcontext);
	}

	public  void addAttachments(UserContext context, int objectPk, int objectType,
									  List<AttachmentIntf> attachedFiles) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            commonServiceManager.addAttachments(context, objectPk, objectType, attachedFiles);
    		
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}
	
	public  void saveAttachments(UserContext context, int objectPk, int objectType, 
			List<AttachmentIntf> attachedFiles, boolean deleteItemsNotInList) throws Exception 
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            commonServiceManager.saveAttachments(context, objectPk, objectType, attachedFiles, deleteItemsNotInList);
    		
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}

	public  void saveAttachments(UserContext context, int objectPk, int objectType, String attachmentContext, 
			List<AttachmentIntf> attachedFiles) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		commonServiceManager.saveAttachments(context, objectPk, objectType, attachmentContext, attachedFiles);
    		
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}


	public  void removeAttachment(UserContext userContext, Attachment attachment)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		commonServiceManager.removeAttachment(userContext, attachment);
    		
            con.commit();
        }
        catch(Exception ex)
        {
            con.rollback();
            throw ex;
        }
	    finally
	    {
	    }
	}
}
