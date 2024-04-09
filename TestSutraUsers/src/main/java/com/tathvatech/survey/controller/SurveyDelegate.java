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


import com.tathvatech.common.enums.WorkItem;
import com.tathvatech.forms.common.FormFilter;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectLockQuery;
import com.tathvatech.forms.entity.FormMain;
import com.tathvatech.forms.entity.FormPrintFormat;
import com.tathvatech.forms.entity.ObjectLock;
import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.pdf.config.PdfTemplatePrintLocationConfig;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserQuery;
import com.tathvatech.workstation.service.WorkstationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyDelegate
{
	private static final Logger logger = LoggerFactory.getLogger(SurveyDelegate.class);
	private final SurveyMaster surveyMaster;
	private final SurveyResponseService surveyResponseService;
	private final WorkstationService workstationService;

    public SurveyDelegate(SurveyMaster surveyMaster, SurveyResponseService surveyResponseService, WorkstationService workstationService) {
        this.surveyMaster = surveyMaster;
        this.surveyResponseService = surveyResponseService;
        this.workstationService = workstationService;
    }

    //TODO:: implement a cache with this hashmap
    //private static HashMap surveyMap = new HashMap();
    
    /**
     * @param surveyPk
     * @return
     */
    public  String getSurveyDefFileName(int _surveyPk)throws Exception
    {
        return surveyMaster.getSurveyDefFileName(_surveyPk);
    }

    /**
     * @param surveyPk
     * @return
     */
    public  Survey getSurveyByPk(int surveyPk)throws Exception
    {
        return surveyMaster.getSurveyByPk(surveyPk);
    }


    public  Survey createSurvey(UserContext context, Survey survey) throws Exception
    {


        	Survey sur = surveyMaster.createSurvey(context, survey);


    	    
    	    return sur;

    }

    public Survey createSurveyNewVersion(UserContext context, Survey newVersion, FormQuery baseRevision) throws Exception
    {


        	Survey sur = surveyMaster.createSurveyNewVersion(context, newVersion, baseRevision);

			return sur;

    }

    public  Survey createSurveyByCopy(UserContext context, Survey survey, int sourceSurveyPk) throws Exception
    {


        	survey = surveyMaster.createSurveyByCopy(context, survey, sourceSurveyPk);


            
            return survey;

    }

    /**
     * mark the survey status as deleted.// the survey list will not show these items .
     * a complete removal should be done manually.
     * @param surveyDef
     */
    public  void deleteSurvey(int surveyPk)throws Exception
    {


            surveyMaster.deleteSurvey(surveyPk);

    }

    public  Survey updateSurvey(Survey survey)throws Exception
    {


            survey = surveyMaster.updateSurvey(survey);


            
            return survey;

	    }
    }

	public static FormQuery getFormByPk(int formPk)
	{
		return SurveyMaster.getFormByPk(formPk);
	}

	public List<FormQuery> getSurveyList() throws Exception
	{
		return surveyMaster.getSurveyList();
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

	public  void deleteSurveyVersion(UserContext context, int surveyVersionPk) throws Exception
	{


    		surveyMaster.deleteSurveyVersion(context, surveyVersionPk);


	}

	public  FormMain getFormMain(FormQuery formQuery) throws Exception
	{
		return surveyMaster.getFormMain(formQuery);
	}

	public static void publishSurvey(UserContext context, int surveyPk,
									 List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap) throws Exception
	{


    		SurveyMaster.publishSurvey(context, surveyPk, projectUpgradeList, projectNotificationMap, formsUpgradeMap);



	}

	public static void applyFormUpgradePublish(UserContext context, int surveyPk, List projectListToUpgrade, List unitFormsListToUpgrade) throws Exception
	{


    		SurveyMaster.applyFormUpgradePublish(context, surveyPk, projectListToUpgrade, unitFormsListToUpgrade);



	}

	public static UserQuery getAttributionUser(WorkItem workItem)throws Exception
	{
		return SurveyMaster.getAttributionUser(workItem);
	}
	
	public static void setAttribution(UserContext context, WorkItem workItem, UserOID attributeToUserOID)throws Exception
	{


    		SurveyMaster.setAttribution(context, workItem, attributeToUserOID);


	}
	
	public  void resetAttribution(UserContext context, WorkItem workItem)throws Exception
	{

    		surveyMaster.resetAttribution(context, workItem);
            

	}
	
	public  synchronized ObjectLock lockSectionToEdit(UserContext context, User lockForUser, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception
	{

    		ObjectLock l = surveyMaster.lockSectionToEdit(context, lockForUser, responseOID, sectionId);
            
    		//record in workstation
			ResponseMasterNew respMaster = surveyResponseService.getResponseMaster(responseOID.getPk());
    		workstationService.recordWorkstationFormLock(new TestProcOID(respMaster.getTestProcPk()));
    		

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

    		SurveyMaster.releaseSectionEditLock(context, responseOID, sectionId);
    		
    		ResponseMasterNew respMaster = SurveyResponseManager.getResponseMaster(responseOID.getPk());
    		
    		ProjectManager.recordWorkstationFormUnlock(new TestProcOID(respMaster.getTestProcPk()));
    		

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


            PdfTemplatePrintLocationConfig c = SurveyMaster.saveFormPrintTemplateLocationConfig(context, formOID, config);
    		


	
}
