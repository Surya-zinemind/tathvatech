/*
 * Created on Dec 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.tathvatech.activitylogging.common.ActivityLogQuery;
import com.tathvatech.activitylogging.controller.ActivityLoggingDelegate;
import com.tathvatech.common.enums.BaseActions;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.EntityVersionReviewProxy;
import com.tathvatech.forms.common.ObjectLockQuery;
import com.tathvatech.forms.entity.*;
import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.forms.response.FormResponseBean;
import com.tathvatech.forms.response.ResponseMaster;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.survey.common.*;
import com.tathvatech.survey.entity.ResponseSubmissionBookmark;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.exception.LockedByAnotherUserException;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.survey.service.WorkflowManager;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.unit.service.UnitManager;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.forms.service.TestProcService;
import com.tathvatech.project.entity.Project;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

@RequiredArgsConstructor
public class SurveyResponseController
{
    private  final Logger logger = LoggerFactory
	    .getLogger(SurveyResponseController.class);

	private final TestProcService testProcService;
	private final SurveyResponseService surveyResponseService;
    private final UnitManager unitManager;
     private final UnitService unitService;
	 private final ActivityLoggingDelegate activityLoggingDelegate;
    private final ProjectService projectService;
    private final SurveyMaster surveyMaster;
    private final WorkstationService workstationService;
	private final PersistWrapper persistWrapper;
	private  final WorkflowManager workflowManager;
    /**
     * Called when the workstation is changed to in progress
     */
    public SurveyResponse ceateDummyResponse(UserContext context,
											 SurveyResponse surveyResponse) throws Exception
    {
		SurveyResponse resp = surveyResponseService.ceateDummyResponse(context,
			    surveyResponse);
        return resp;
    }
    
    
    /**
     * 
     */
    public  SurveyResponse savePageResponse(UserContext context, 
	    SurveyResponse surveyResponse) throws Exception
    {

		    
		    TestProcObj testProc = testProcService.getTestProc(surveyResponse.getTestProcPk());
		    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
		    {
		    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
		    }
		    UnitObj unit =unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = unitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = projectService.getProject(testProc.getProjectPk());
		    if(Project.STATUS_CLOSED.equals(project.getStatus()))
		    {
		    	throw new AppException("Project is closed, You cannot submit any changes to the form");
		    }
	
		    //check if the form is still assigned to the workstation, if the form was upgraded 
		    boolean formIsValid = false;
		    if(testProc.getFormPk() == surveyResponse.getSurveyPk())
		    	formIsValid = true;
		    if(formIsValid == false)
		    {
				throw new AppException("Form is not valid anymore, It could have been removed or upgraded. Cannot save or submit the form.");
		    }
		    
		    //check the workstation status.
			UnitLocationQuery unitWorkstation = workstationService.getUnitWorkstationStatus(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }
	
		    ArrayList sectionsToSave = new ArrayList();
			List<String> lockedSectionIds = surveyMaster.getLockedSectionIds((User) context.getUser(), surveyResponse.getOID());
			
			SurveyDefinition sd = surveyResponse.getSurveyDefinition();
			for (Iterator iterator = sd.getQuestions().iterator(); iterator.hasNext();)
			{
				Section aSection = (Section) iterator.next();
				if(lockedSectionIds.contains(aSection.getSurveyItemId()))
				{
					sectionsToSave.add(aSection);
				}
			}
			SurveyResponse resp = surveyResponseService.saveSectionResponses(context, project, unit,
			    surveyResponse, sectionsToSave);
		    
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			workflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, (int) context.getUser().getPk(), resp.getResponseId(),
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			//record workstation save
			workstationService.recordWorkstationSave(testProc.getOID());
			
			//TODO lines below is added for testing purpose. we create sync error record when a normal save is done.
			//This needs to be removed.
//			FormResponseBean formResponseBean = getFormResponseBean(context, resp.getResponseId());
//			saveSyncErrorResponse(context, resp.getResponseId(), formResponseBean);

		return resp;

    }
    
    
    /**
     * this function is just a save of the current response. we also take a history printout of the current response data and save it as an attachment
     * we could also email the manager or do some other actions based on this user interaction.
     * added as part of the signalling project implementation.
     * @param context
     * @param surveyResponse
     * @return
     * @throws Exception
     */
    public  SurveyResponse submitInterimResponse(UserContext context, 
    	    SurveyResponse surveyResponse) throws Exception
    {

		    
    	
		    SurveyResponse sResponseAfterSave = savePageResponse(context, surveyResponse);
		    
		    
		    surveyResponseService.saveResponseStateAsSubmitRecord(context, sResponseAfterSave.getResponseId(), ResponseSubmissionBookmark.SubmissionTypeEnum.Interim);
			return sResponseAfterSave;
	}

    /**
     * save a specific set of questions and not a complete section.
     */
    public  SurveyResponse saveQuestionResponses(UserContext context, 
	    SurveyResponse surveyResponse, List questionsToSave) throws Exception
    {

		    
		    TestProcObj testProc = testProcService.getTestProc(surveyResponse.getTestProcPk());
		    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
		    {
		    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
		    }
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = unitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit/Car is complete, You cannot submit any changes to the form");
		    }
		    Project project = projectService.getProject(testProc.getProjectPk());
		    if(Project.STATUS_CLOSED.equals(project.getStatus()))
		    {
		    	throw new AppException("Project is closed, You cannot submit any changes to the form");
		    }
	
		    //check if the form is still assigned to the workstation, if the form was upgraded 
		    boolean formIsValid = false;
		    if(testProc.getFormPk() == surveyResponse.getSurveyPk())
		    	formIsValid = true;
		    if(formIsValid == false)
		    {
				throw new AppException("Form is not valid anymore, It could have been removed or upgraded. Cannot save or submit the form.");
		    }
		    
		    //check the workstation status.
			UnitLocationQuery unitWorkstation = workstationService.getUnitWorkstationStatus(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }
	
			SurveyResponse resp = surveyResponseService.saveSpecificQuestionResponse(context, project, unit,
			    surveyResponse, questionsToSave);
		    
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			workflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, (int) context.getUser().getPk(), resp.getResponseId(),
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			//record workstation save
			workstationService.recordWorkstationSave(testProc.getOID());
			

	
		    return resp;

    }

    /**
     * @param
     * @param
     */
    public  void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {


	    TestProcObj testProc = testProcService.getTestProc(surveyResponse.getTestProcPk());
	    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
	    {
	    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
	    }
		try
		{
			UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = unitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = projectService.getProject(testProc.getProjectPk());
		    if(Project.STATUS_CLOSED.equals(project.getStatus()))
		    {
		    	throw new AppException("Project is closed, You cannot submit any changes to the form");
		    }
	
		    //check if the form is still assigned to the workstation, if the form was upgraded 
		    boolean formIsValid = false;
		    if(testProc.getFormPk() == surveyResponse.getSurveyPk())
		    	formIsValid = true;
		    if(formIsValid == false)
		    {
				throw new AppException("Form is not available at this workstation, It could have been removed or upgraded. Cannot save or submit the form.");
		    }

		    ArrayList sectionsToSave = new ArrayList();
			List<String> lockedSectionIds = surveyMaster.getLockedSectionIds((User) userContext.getUser(),
					surveyResponse.getOID());
			
			SurveyDefinition sd = surveyResponse.getSurveyDefinition();
			for (Iterator iterator = sd.getQuestions().iterator(); iterator.hasNext();)
			{
				Section aSection = (Section) iterator.next();
				if(lockedSectionIds.contains(aSection.getSurveyItemId()))
				{
					sectionsToSave.add(aSection);
				}
			}
		    
		    //check the workstation status.
			UnitLocationQuery unitWorkstation = workstationService.getUnitWorkstationStatus(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be submitted");
			}
			else if(UnitLocation.STATUS_WAITING.equals(unitWorkstation.getStatus()))
			{
				//save the response
				surveyResponse = surveyResponseService.saveSectionResponses(userContext, project, unit,
						surveyResponse, sectionsToSave);
				
				//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
				// clicking through from the myassignments table.
				workflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, (int) userContext.getUser().getPk(), surveyResponse.getResponseId(),
						testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

				throw new AppException("Workstation associated with the test is in Waiting status, Form Saved but not Submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }

			//save the response
			surveyResponse = surveyResponseService.saveSectionResponses(userContext, project, unit,
					surveyResponse, sectionsToSave);
			
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			workflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, (int) userContext.getUser().getPk(), surveyResponse.getResponseId(),
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, (String) null);

			boolean hasSectionsLockedByOtherUsers = false;
			List<ObjectLock> lockedSections = surveyMaster.getLockedSectionIds(surveyResponse.getOID());
			for (Iterator iterator = lockedSections.iterator(); iterator.hasNext();)
			{
				ObjectLock aLock = (ObjectLock) iterator.next();
				if(aLock.getUserPk() != userContext.getUser().getPk())
				{
					hasSectionsLockedByOtherUsers = true;
					break;
				}
			}
			
			if(hasSectionsLockedByOtherUsers)
			{
				throw new AppException("Form has sections locked by other testers, Form Saved but not Submitted");
			}
			else
			{
				//validate response with all the questions in the form. because there could be
				//sections that are not filled yet.
				List allQuestions = surveyDef.getQuestionsLinear();
				LinkedHashMap<String, List<String>> errors = validateResponse(surveyResponse, allQuestions);
				if(errors.size() > 0)
				{
					throw new AppException("Form has sections which has errors or not completed by other testers, Form Saved but not Submitted");
				}
				else
				{
					surveyResponseService.finalizeSurveyResponse(userContext, surveyDef, surveyResponse.getResponseId());
					ActivityLogQuery aLog1 = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.submitForm,
							"Form Submitted", new Date(), new Date(), (int) project.getPk(), testProc.getPk(), testProc.getUnitPk(), testProc.getWorkstationPk(),
							surveyResponse.getSurveyPk(), null, surveyResponse.getResponseId());
					activityLoggingDelegate.logActivity(aLog1);
				}
			}

			
			
			
	    	//release locks held by me
			for (Iterator iter = surveyDef.getQuestions().iterator(); iter.hasNext();)
			{
				SurveyItem aItem = (SurveyItem) iter.next();
				if(aItem instanceof Section)
				{
					try
					{
						surveyMaster.releaseSectionEditLock(userContext, (User) userContext.getUser(),
								surveyResponse.getOID(), aItem.getSurveyItemId());
					}
					catch(LockedByAnotherUserException lx)
					{
						//ignore..  TODO we should take only the sections locked by the current user and try to unlock
						// it.. dont want to loop again here.. we should return that information from the questionlist
						//building loop..
					}
				}
			}
			
			

		}
		catch(AppException ax)
		{
			ax.printStackTrace();
			
	    	//release locks held by me
			for (Iterator iter = surveyDef.getQuestions().iterator(); iter.hasNext();)
			{
				SurveyItem aItem = (SurveyItem) iter.next();
				if(aItem instanceof Section)
				{
					try
					{
						surveyMaster.releaseSectionEditLock(userContext, (User) userContext.getUser(),
								surveyResponse.getOID(), aItem.getSurveyItemId());
					}
					catch(LockedByAnotherUserException lx)
					{
						//ignore..  TODO we should take only the sections locked by the current user and try to unlock
						// it.. dont want to loop again here.. we should return that information from the questionlist
						//building loop..
					}
				}
			}

			throw ax;
		}

    }

    public  List<ResponseSubmissionBookmark> getResponseSubmissionBookmarks(TestProcOID testprocOID)
    {
    	return surveyResponseService.getResponseSubmissionBookmarks(testprocOID);
    }

    public  ResponseSubmissionBookmark getLastResponseSubmissionBookmark(TestProcOID testprocOID)
    {
    	return surveyResponseService.getLastResponseSubmissionBookmark(testprocOID);
    }

    /**
     * Adds a new response entry to a form. Function called by response bulk
     * import action. difference between addSurveyResponse and this one is that
     * addSurveyResponse calls the notify function after adding the response.
     * @param surveyDef
     * @param surveyResponse
     * @return
     * @throws Exception
     */
    public  long importSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {

	    
	    TestProcObj testProc = testProcService.getTestProc(surveyResponse.getTestProcPk());
	    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
	    {
	    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
	    }
	    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
	    Project project = projectService.getProject(testProc.getProjectPk());

	    SurveyResponse resp = surveyResponseService.saveSectionResponses(userContext, project, unit,
		    surveyResponse, surveyDef.getQuestions());
		
	    //add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
		// clicking through from the myassignments table.
		workflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, (int) userContext.getUser().getPk(), resp.getResponseId(),
				testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);


		surveyResponseService.finalizeSurveyResponse(userContext, surveyDef,
		    resp.getResponseId(),
		    surveyResponse.getResponseCompleteTime());



	    return resp.getResponseId();

    }

    /*/**
     * called when the verifier or approver edits the response
     * @param userContext
     * @param surveyDef
     * @param responseId
     * @param surveyResponse
     * @throws Exception
     *//*/*/
    public  SurveyResponse updateSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {

		    
		    if(ResponseMasterNew.STATUS_COMPLETE.equals(surveyResponse.getStatus()) || ResponseMasterNew.STATUS_VERIFIED.equals(surveyResponse.getStatus()))
		    {
		    }
		    else
		    {
		    	throw new AppException("Testing is not complete on the form, edit failed");
		    }
		    
		    TestProcObj testProc = testProcService.getTestProc(surveyResponse.getTestProcPk());
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = unitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = projectService.getProject(testProc.getProjectPk());
		    if(Project.STATUS_CLOSED.equals(project.getStatus()))
		    {
		    	throw new AppException("Project is closed, You cannot submit any changes to the form");
		    }
	
		    //check if the form is still assigned to the workstation, if the form was upgraded 
		    boolean formIsValid = false;
		    if(testProc.getFormPk() == surveyResponse.getSurveyPk())
		    	formIsValid = true;
		    if(formIsValid == false)
		    {
				throw new AppException("Form is not valid anymore, It could have been removed or upgraded. Cannot save or submit the form.");
		    }
		    
		    //check the workstation status.
			UnitLocationQuery unitWorkstation =workstationService.getUnitWorkstationStatus(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}

			//when editing a response, to maintain history of what the tester had submitted, the response should be copied to old.
			// find the last workflow entry and if the action on that is editform, the response has been copied and edited, so continue edit on that. 
			//else save the current response to a backup,
			FormWorkflow lastEntry = workflowManager.getLastEntry(testProc.getOID());

			SurveyResponse responseToReturn;
			if(FormWorkflow.ACTION_EDIT_RESPONSE.equals(lastEntry.getAction()))
			{
				SurveyResponse resp = surveyResponseService.updateSurveyResponse(userContext, project, unit, surveyDef,
					    surveyResponse.getResponseId(), surveyResponse.getSurveyItemAnswerMap(), surveyDef.getQuestions());
				responseToReturn = resp;
			}
			else
			{
				//when copyResponse is called, the new response is created as current, mark the existing one as old 
				ResponseMasterNew responseMaster = getResponseMaster(surveyResponse.getResponseId());
				surveyResponseService.markResponseAsOld(userContext, responseMaster);
				
				//create a copy of the response and mark it as incomplete so that the tester can
				//carry on editing the form and resubmitting it
				//create the new response, and set the current = true
				FormResponseMaster respMaster = persistWrapper.readByResponseId(FormResponseMaster.class, surveyResponse.getResponseId());
				List<SectionResponse> sections = PersistWrapper.readList(SectionResponse.class, "select * from TAB_SECTION_RESPONSE where responseId = ?", surveyResponse.getResponseId());
				List<FormResponseDesc> itemResponses = PersistWrapper.readList(FormResponseDesc.class, "select * from TAB_RESPONSE_desc where responseId = ?", surveyResponse.getResponseId());
				respMaster.setResponseId(0);
				respMaster.setCurrent(true);
				int newResponseId = (int) persistWrapper.createEntity(respMaster);
				
				for (Iterator iterator = sections.iterator(); iterator.hasNext();) 
				{
					SectionResponse sectionResponse = (SectionResponse) iterator.next();
					sectionResponse.setPk(0);
					sectionResponse.setResponseId(newResponseId);
					persistWrapper.createEntity(sectionResponse);
				}
				for (Iterator iterator = itemResponses.iterator(); iterator.hasNext();) 
				{
					FormResponseDesc formResponseDesc = (FormResponseDesc) iterator.next();
					formResponseDesc.setResponseId(newResponseId);
					persistWrapper.createEntity(formResponseDesc);
				}
				
				// TODO:: hack hack. here since the responseId changes, the FormItemResponse reference will be lost since the
				// responseId is changed.. we should actually have the copy archieved and the current one continued.. but that need to be tested properly.
				// so for now I am changing the responseId to the new one.
				persistWrapper.executeUpdate("update TAB_ITEM_RESPONSE set responseId = ? where responseId = ?", newResponseId, surveyResponse.getResponseId());

				SurveyResponse newResponse = surveyResponseService.updateSurveyResponse(userContext, project, unit, surveyDef,
					    newResponseId, surveyResponse.getSurveyItemAnswerMap(), surveyDef.getQuestions());
	
				
				
				workflowManager.addWorkflowEntry(FormWorkflow.ACTION_EDIT_RESPONSE, (int) userContext.getUser().getPk(), newResponse.getResponseId(),
						testProc.getPk(), lastEntry.getResultStatus(), "Edit performed");


				responseToReturn = surveyResponseService.getSurveyResponse(newResponseId);
			}
			//record workstation save
			workstationService.recordWorkstationSave(testProc.getOID());
			


			return responseToReturn;

    }

    public  List getSurveyItemResponse(SurveyDefinition surveyDef,
	    String surveyItemId, ResponseMaster[] responseMasterSet)
	    throws Exception
    {
    	return surveyResponseService.getSurveyItemResponse(surveyDef,
		surveyItemId, responseMasterSet);
    }

	public  SurveyResponse getSurveyResponse(int responseId) throws Exception
	{
		return surveyResponseService.getSurveyResponse(responseId);
	}

	public  SurveyResponse getSurveyResponse(SurveyDefinition surveyDef,
	    int responseId) throws Exception
    {
    	return surveyResponseService.getSurveyResponse(surveyDef, responseId);
    }


    /**
     * count of responses recieved for a survey between the given dates.
     * includes the responses recieved on the start and end date
     * 
     * @param startDate
     * @param endDate
     */
    // public  long getResponseCountByDateRange(Survey survey, Date
    // startDate, Date endDate)throws Exception
    // {
    // return SurveyResponseManager.getResponseCountByDateRange(survey,
    // startDate, endDate);
    // }

	public  List<String> getResponseStatusSetForForm(UserContext context, Survey survey) throws Exception
	{
		return surveyResponseService.getResponseStatusSetForForm(context, survey);
	}

    /**
     * returns all the responseMaster records for a survey by a respondent in
     * descenting order of responseTime
     * 
     * @param surveyPk
     * @param respondentPk
     * @return
     */
    public  List getResponseMastersForRespondent(int surveyPk,
	    int respondentPk) throws Exception
    {
    	return surveyResponseService.getResponseMastersForRespondent(surveyPk,
    			respondentPk);
    }

    /**
     * @param surveyPk
     * @param responseId
     * @return
     */
    public  ResponseMaster getResponseMaster(int surveyPk,
	    String responseId) throws Exception
    {
    	return surveyResponseService.getResponseMaster(surveyPk, responseId);
    }


    // ///////////////// for testsutra
	public  ResponseMasterNew getResponseMaster(int responseId) throws Exception
	{
		return surveyResponseService.getResponseMaster(responseId);
	}

	public  ResponseMasterNew getLatestResponseMasterForTest(TestProcOID testProcOid) throws Exception
	{
		return surveyResponseService.getLatestResponseMasterForTest(testProcOid);
	}
    public  ResponseMasterNew[] getLatestResponseMastersForUnitInWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception
	{
		return surveyResponseService.getLatestResponseMastersForUnitInWorkstation(new UnitOID(unitPk, null), projectOID, workstationOID);
	}
    
	public  ResponseMasterNew getResponseMasterForTestHistoryRecord(TestProcOID testProcOID, FormOID formOID)
	{
		return surveyResponseService.getResponseMasterForTestHistoryRecord(testProcOID, formOID);
	}
    

    public  void verifyResponse(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {


			surveyResponseService.verifyResponse(userContext,
				    sResponse, comments);
			TestProcObj testProc = testProcService.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit =unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
		    ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.verifyForm,
					"Form Verified", new Date(), new Date(), (int) project.getPk(), testProc.getPk(), (int) unit.getPk(), testProc.getWorkstationPk(),
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
			activityLoggingDelegate.logActivity(aLog);




    }

    public  void rejectResponse(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {

	
			surveyResponseService.rejectResponse(userContext,
				    sResponse, comments);
			TestProcObj testProc = testProcService.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectVerifyForm,
					"Form Verification Rejected", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
                    (int) unit.getPk(), testProc.getWorkstationPk(),
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
			activityLoggingDelegate.logActivity(aLog);
	


    }

    public  void approveResponse(UserContext userContext,
    		ResponseMasterNew resp, String comments) throws Exception
    {

	
	    	surveyResponseService.approveResponse(userContext, resp, comments);
	    	TestProcObj testProc = testProcService.getTestProc(resp.getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
	    				"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, resp.getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }

    public  void approveResponseWithComments(UserContext userContext,
    		ResponseMasterNew resp, String comments) throws Exception
    {

	
	    	surveyResponseService.approveResponseWithComments(userContext, resp, comments);
	    	TestProcObj testProc = testProcService.getTestProc(resp.getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
	    				"Form approved with comments", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, resp.getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }

	public  void approveResponseInBulk(UserContext userContext, List<AssignedTestsQuery> selectedList, String comment) throws Exception
	{


	
    		for (Iterator iterator = selectedList.iterator(); iterator.hasNext();) {
				AssignedTestsQuery assignedTestsQuery = (AssignedTestsQuery) iterator.next();

				ResponseMasterNew resp = surveyResponseService.getResponseMaster(assignedTestsQuery.getResponseId());

				surveyResponseService.approveResponse(userContext, resp, comment);

				TestProcObj testProc = testProcService.getTestProc(resp.getTestProcPk());

				ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
						"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(),
						testProc.getUnitPk(), testProc.getWorkstationPk(),
						resp.getFormPk(), null, resp.getResponseId());
				activityLoggingDelegate.logActivity(aLog);
			}
	}
	
    
    public  void rejectApproval(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {

	
			surveyResponseService.rejectApproval(userContext,
				    sResponse, comments);
			TestProcObj testProc = testProcService.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit =unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectApproveForm,
					"Form Approval Rejected", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
					testProc.getUnitPk(), testProc.getWorkstationPk(), 
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
		activityLoggingDelegate.logActivity(aLog);
	

    }
    

    public  void reopenApproved(UserContext userContext, SurveyResponse sResponse, String comments) throws Exception
    {

	
	    	surveyResponseService.reopenApprovedForm(userContext,
	    		    sResponse, comments);
	    	TestProcObj testProc = testProcService.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectApproveForm,
	    			"Approved form reopened", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
	    			testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    			sResponse.getSurveyPk(), null, sResponse.getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }
        
    
    /**
     * returns if sections are locked by other users
     * parameter surveyQuestions is filledout by this function with the sections that should be saved.
     * @param surveyResponse
     * @param surveyQuestions
     * @param errors
     * @return
     * @throws Exception
     */
	private  boolean getQuestionsToSaveResponsesFor(UserContext context, SurveyDefinition surveyDef, List<Section> surveyQuestions, 
			FormResponseOID responseOID)throws Exception
	{
		boolean hasSectionsLockedByOthers = false;
		for (Iterator iter = surveyDef.getQuestions().iterator(); iter.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iter.next();
			if(aItem instanceof Section)
			{
				ObjectLockQuery lock = surveyMaster.getCurrentLock(responseOID,
						aItem.getSurveyItemId());
			
				if(lock == null)
				{
					
				}
				else if(lock.getUserPk() == context.getUser().getPk())
				{
					//locked my me.. 
					surveyQuestions.add((Section)aItem);
				}
				else
				{
					//locked by other user..
					hasSectionsLockedByOthers = true;
				}
			}
		}
		return hasSectionsLockedByOthers;
	}

	public  LinkedHashMap<String, List<String>> validateResponse(SurveyResponse surveyResponse, List surveyQuestions)
	{
		LinkedHashMap<String, List<String>> errors = new LinkedHashMap<String, List<String>>();
		for (Iterator iter = surveyQuestions.iterator(); iter.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iter.next();
			if(aItem instanceof SurveySaveItem)
			{
				SurveyItemResponse sItemResponse = surveyResponse.getAnswer((SurveySaveItem)aItem);
				List<String> er = ((SurveySaveItem)aItem).validateResponse(sItemResponse);
				if(er != null)
					errors.put(aItem.getSurveyItemId(), er);
			}
		}
		return errors;
	}
	
	
	public  List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd)
	{
		return surveyResponseService.getSectionResponseSummary(sd);
	}

	public  List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd, int responseId)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(sd, responseId);
	}

	public  SectionResponseQuery getSectionResponseSummary(SurveyDefinition sd, int responseId, String sectionId)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(sd, responseId, sectionId);
	}

	public  SectionResponseQuery getSectionResponseSummary(int responseId, String sectionId)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(responseId, sectionId);
	}


	public  void addToResponseVAComments(UserContext userContext,	ResponseMasterNew responseMaster, String vCommentToAdd,
			String aCommentToAdd) throws Exception 
	{


    		surveyResponseService.addToResponseVAComments(userContext, responseMaster, vCommentToAdd, aCommentToAdd);
    		

	}

//	public  String addToSurveyItemResponseComment(UserContext userContext,	int responseId, String surveyItemId,
//			String commentToAdd) throws Exception 
//	{
//        Connection con = null;
//        try
//        {
//            con = ServiceLocator.locate().getConnection();
//            con.setAutoCommit(false);
//
//    		return SurveyResponseManager.addToSurveyItemResponseComment(userContext, responseId, surveyItemId, commentToAdd);
//        }
//        catch(Exception ex)
//        {
//            con.rollback();
//            throw ex;
//        }
//	    finally
//	    {
//            con.commit();
//	    }
//	}

	
	public FormItemResponse getFormItemResponse(FormItemResponseOID formItemResponseOID)
	{
		return	surveyResponseService.getFormItemResponse(formItemResponseOID);
	}

	
	public  FormItemResponse createFormItemResponse(int responseId, String surveyItemId) throws Exception {


    		FormItemResponse r = surveyResponseService.createFormItemResponse(responseId, surveyItemId);

			return r;
	}

	public  FormItemResponse getFormItemResponse(int responseId, String surveyItemId, boolean createIfNoExist) throws Exception {


    		FormItemResponse r = surveyResponseService.getFormItemResponse(responseId, surveyItemId, createIfNoExist);

			return r;


	}

	public  HashMap<String, FormItemResponse> getFormItemResponses(int responseId)
	{
		return	surveyResponseService.getFormItemResponses(responseId);
	}


	public  FormResponseClientSubmissionRev getFormClientSubmissionRevision(int responseId)
	{
		return surveyResponseService.getFormClientSubmissionRevision(responseId);
}
	
	public  FormResponseClientSubmissionRev saveFormClientSubmissionRevision(UserContext context, int responseId, String revision) throws Exception
	{


            FormResponseClientSubmissionRev r = surveyResponseService.saveFormClientSubmissionRevision(context, responseId, revision);

			return r;

	}


	public  void saveSyncErrorResponse(UserContext context, int responseId, FormResponseBean formResponseBean) throws Exception
	{


            surveyResponseService.saveSyncErrorResponse(context, responseId, formResponseBean);



	}
	
	public  FormResponseBean getFormResponseBean(UserContext context, int responseId)throws Exception
	{
		return surveyResponseService.getFormResponseBean(context, responseId);
	}
	
	public EntityVersionReviewProxy getEntityRevisionReviewProxy(UserContext context, int responseId) throws Exception
	{
		return surveyResponseService.getEntityRevisionForReview(context.getUser().getOID(), responseId);
	}
	
	public FormResponseBean getFormResponseBeanForSyncErrorReview(int entityVersionReviewPk) throws Exception
	{
		return surveyResponseService.getFormResponseBeanForSyncErrorReview(entityVersionReviewPk);
	}
}
