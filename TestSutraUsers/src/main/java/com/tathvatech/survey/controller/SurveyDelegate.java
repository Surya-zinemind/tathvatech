/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.sarvasutra.etest.pdfprint.templatelocationconfig.PdfTemplatePrintLocationConfig;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.accounts.UserQuery;
import com.tathvatech.ts.core.common.WorkItem;
import com.tathvatech.ts.core.project.FormOID;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.survey.FormPrintFormat;
import com.tathvatech.ts.core.survey.ObjectLock;
import com.tathvatech.ts.core.survey.ObjectLockQuery;
import com.tathvatech.ts.core.survey.Survey;
import com.tathvatech.ts.core.survey.response.FormResponseOID;
import com.tathvatech.ts.core.survey.response.ResponseMasterNew;
import com.thirdi.surveyside.project.FormFilter;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.survey.FormMain;
import com.thirdi.surveyside.survey.FormQuery;
import com.thirdi.surveyside.survey.LockedByAnotherUserException;
import com.thirdi.surveyside.survey.SurveyMaster;
import com.thirdi.surveyside.survey.response.SurveyResponseManager;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyDelegate
{
    private static final Logger logger = Logger.getLogger(SurveyDelegate.class);
    
    //TODO:: implement a cache with this hashmap
    //private static HashMap surveyMap = new HashMap();
    
    /**
     * @param surveyPk
     * @return
     */
    public static String getSurveyDefFileName(int _surveyPk)throws Exception
    {
        return SurveyMaster.getSurveyDefFileName(_surveyPk);
    }

    /**
     * @param surveyPk
     * @return
     */
    public static Survey getSurveyByPk(int surveyPk)throws Exception
    {
        return SurveyMaster.getSurveyByPk(surveyPk);
    }


    public static Survey createSurvey(UserContext context, Survey survey) throws Exception
    {
    	Connection conn = null;
    	try
    	{
    	    conn = ServiceLocator.locate().getConnection();
    	    conn.setAutoCommit(false);

        	Survey sur = SurveyMaster.createSurvey(context, survey);

    	    conn.commit();
    	    
    	    return sur;
    	}
    	catch (Exception ex)
    	{
    	    conn.rollback();
    	    throw ex;
    	}
    }

    public static Survey createSurveyNewVersion(UserContext context, Survey newVersion, FormQuery baseRevision) throws Exception
    {
    	Connection conn = null;
    	try
    	{
    	    conn = ServiceLocator.locate().getConnection();
    	    conn.setAutoCommit(false);

        	Survey sur = SurveyMaster.createSurveyNewVersion(context, newVersion, baseRevision);

    	    conn.commit();
    	    
    	    return sur;
    	}
    	catch (Exception ex)
    	{
    	    conn.rollback();
    	    throw ex;
    	}
    }

    public static Survey createSurveyByCopy(UserContext context, Survey survey, int sourceSurveyPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

        	survey = SurveyMaster.createSurveyByCopy(context, survey, sourceSurveyPk);

            con.commit();
            
            return survey;
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

    /**
     * mark the survey status as deleted.// the survey list will not show these items .
     * a complete removal should be done manually.
     * @param surveyDef
     */
    public static void deleteSurvey(int surveyPk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SurveyMaster.deleteSurvey(surveyPk);

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

    public static Survey updateSurvey(Survey survey)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            survey = SurveyMaster.updateSurvey(survey);

            con.commit();
            
            return survey;
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

	public static FormQuery getFormByPk(int formPk)
	{
		return SurveyMaster.getFormByPk(formPk);
	}

	public static List<FormQuery> getSurveyList() throws Exception
	{
		return SurveyMaster.getSurveyList();
	}

	public static List<FormQuery> getOpenSurveyList() throws Exception
	{
		return SurveyMaster.getOpenSurveyList();
	}

	public static List<FormQuery> getSurveyList(FormFilter filter)
	{
		return SurveyMaster.getSurveyList(filter);
	}

	public static List<FormQuery> getAllVersionsForForm(int formMainPk)
	{
		return SurveyMaster.getAllVersionsForForm(formMainPk);
	}

	public static FormQuery getLatestVersionForForm(int formMainPk) throws Exception
	{
		return SurveyMaster.getLatestVersionForForm(formMainPk);
	}

	public static FormQuery getLatestPublishedVersionForForm(int formMainPk) throws Exception
	{
		return SurveyMaster.getLatestPublishedVersionForForm(formMainPk);
	}

	public static FormQuery getLatestPublishedVersionOfForm(int formPk) throws Exception
	{
		return SurveyMaster.getLatestPublishedVersionOfForm(formPk);
	}

	public static void deleteSurveyVersion(UserContext context, int surveyVersionPk) throws Exception 
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.deleteSurveyVersion(context, surveyVersionPk);

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

	public static FormMain getFormMain(FormQuery formQuery) throws Exception 
	{
		return SurveyMaster.getFormMain(formQuery);
	}

	public static void publishSurvey(UserContext context, int surveyPk, 
			List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.publishSurvey(context, surveyPk, projectUpgradeList, projectNotificationMap, formsUpgradeMap);

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

	public static void applyFormUpgradePublish(UserContext context, int surveyPk, List projectListToUpgrade, List unitFormsListToUpgrade) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.applyFormUpgradePublish(context, surveyPk, projectListToUpgrade, unitFormsListToUpgrade);

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

	public static UserQuery getAttributionUser(WorkItem workItem)throws Exception
	{
		return SurveyMaster.getAttributionUser(workItem);
	}
	
	public static void setAttribution(UserContext context, WorkItem workItem, UserOID attributeToUserOID)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.setAttribution(context, workItem, attributeToUserOID);
            
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
	
	public static void resetAttribution(UserContext context, WorkItem workItem)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.resetAttribution(context, workItem);
            
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
	
	public static synchronized ObjectLock lockSectionToEdit(UserContext context, User lockForUser, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		ObjectLock l = SurveyMaster.lockSectionToEdit(context, lockForUser, responseOID, sectionId);
            
    		//record in workstation
			ResponseMasterNew respMaster = SurveyResponseManager.getResponseMaster(responseOID.getPk());
    		ProjectManager.recordWorkstationFormLock(new TestProcOID(respMaster.getTestProcPk()));
    		
    		con.commit();
            return l;
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

	public static ObjectLockQuery getCurrentLock(FormResponseOID responseOID, String sectionId)throws Exception
	{
		return SurveyMaster.getCurrentLock(responseOID, sectionId);
	}

	public static List<ObjectLock> getLockedSectionIds(FormResponseOID responseOID)throws Exception
	{
		return SurveyMaster.getLockedSectionIds(responseOID);
	}

	public static List<String> getLockedSectionIds(User user, FormResponseOID responseOID)throws Exception
	{
		return SurveyMaster.getLockedSectionIds(user, responseOID);
	}

	public static boolean isSectionLocked(UserContext context, User user, FormResponseOID responseOID, String sectionId)throws Exception
	{
		return SurveyMaster.isSectionLocked(user, responseOID, sectionId);
	}

	public static void releaseSectionEditLock(UserContext context, User user, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception
	{
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
			SurveyMaster.releaseSectionEditLock(context, user, responseOID, sectionId);
	
			ResponseMasterNew respMaster = SurveyResponseManager.getResponseMaster(responseOID.getPk());
    		ProjectManager.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));

    		conn.commit();
		}
		catch (Exception ex)
		{
		    conn.rollback();
		    throw ex;
		}
		finally
		{
		}
	}

	/**
	 * this is called by the admin useronly
	 * @param unitPk
	 * @param workstationPk
	 * @param formPk
	 * @param sectionId
	 * @throws Exception
	 */
	public static void releaseSectionEditLock(UserContext context, FormResponseOID responseOID, String sectionId)throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyMaster.releaseSectionEditLock(context, responseOID, sectionId);
    		
    		ResponseMasterNew respMaster = SurveyResponseManager.getResponseMaster(responseOID.getPk());
    		
    		ProjectManager.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));
    		
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

	public static FormPrintFormat getFormPrintFormat(FormOID formOID)
	{
		return SurveyMaster.getFormPrintFormat(formOID);
	}

	public static PdfTemplatePrintLocationConfig getFormPrintTemplateLocationConfig(FormOID formOID) throws Exception
	{
		return SurveyMaster.getFormPrintTemplateLocationConfig(formOID);
	}
	
	public static PdfTemplatePrintLocationConfig saveFormPrintTemplateLocationConfig(UserContext context, FormOID formOID, PdfTemplatePrintLocationConfig config) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            PdfTemplatePrintLocationConfig c = SurveyMaster.saveFormPrintTemplateLocationConfig(context, formOID, config);
    		
            con.commit();
            return c;
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
