/*
 * Created on Dec 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.tathvatech.activitylogging.common.ActivityLogQuery;
import com.tathvatech.activitylogging.controller.ActivityLoggingDelegate;
import com.tathvatech.common.enums.BaseActions;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.AssignedTestsQuery;
import com.tathvatech.forms.common.EntityVersionReviewProxy;
import com.tathvatech.forms.common.ObjectLockQuery;
import com.tathvatech.forms.entity.*;
import com.tathvatech.forms.response.FormResponseBean;
import com.tathvatech.forms.response.ResponseMaster;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.survey.Request.*;
import com.tathvatech.survey.common.*;
import com.tathvatech.survey.entity.ResponseSubmissionBookmark;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.service.SurveyMasterService;
import com.tathvatech.survey.service.WorkflowManager;
import com.tathvatech.unit.enums.Actions;
import com.tathvatech.unit.service.UnitManager;

import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.forms.service.TestProcService;
import com.tathvatech.project.entity.Project;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
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
    private final SurveyMasterService surveyMasterService;
    private final WorkstationService workstationService;
	private final PersistWrapper persistWrapper;
	private  final WorkflowManager workflowManager;

    /**
     * Called when the workstation is changed to in progress
     */
    @PostMapping("/ceateDummyResponse")
	public SurveyResponse ceateDummyResponse(@RequestBody
											 SurveyResponse surveyResponse) throws Exception
    {
		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		SurveyResponse resp = surveyResponseService.ceateDummyResponse(context,
			    surveyResponse);
        return resp;
    }
    // Commenting the methods using Vaadin
    
    /**
     * 
     */
   /* public  SurveyResponse savePageResponse(UserContext context,
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
    */
    
    /**
     * this function is just a save of the current response. we also take a history printout of the current response data and save it as an attachment
     * we could also email the manager or do some other actions based on this user interaction.
     * added as part of the signalling project implementation.
     * @param context
     * @param surveyResponse
     * @return
     * @throws Exception
     */
   /* public  SurveyResponse submitInterimResponse(UserContext context,
    	    SurveyResponse surveyResponse) throws Exception
    {

		    
    	
		    SurveyResponse sResponseAfterSave = savePageResponse(context, surveyResponse);
		    
		    
		    surveyResponseService.saveResponseStateAsSubmitRecord(context, sResponseAfterSave.getResponseId(), ResponseSubmissionBookmark.SubmissionTypeEnum.Interim);
			return sResponseAfterSave;
	}*/

    /**
     * save a specific set of questions and not a complete section.
     */
   /* public  SurveyResponse saveQuestionResponses(UserContext context,
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

    }*/

    /**
     * @param
     * @param
     */
   /* public  void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
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

    }*/

    @GetMapping("/getResponseSubmissionBookmarks")
	public  List<ResponseSubmissionBookmark> getResponseSubmissionBookmarks(@RequestBody TestProcOID testprocOID)
    {
    	return surveyResponseService.getResponseSubmissionBookmarks(testprocOID);
    }

    @GetMapping("/getLastResponseSubmissionBookmark")
	public  ResponseSubmissionBookmark getLastResponseSubmissionBookmark(@RequestBody TestProcOID testprocOID)
    {
    	return surveyResponseService.getLastResponseSubmissionBookmark(testprocOID);
    }

    /**
     * Adds a new response entry to a form. Function called by response bulk
     * import action. difference between addSurveyResponse and this one is that
     * addSurveyResponse calls the notify function after adding the response.
     * @param
     * @param
     * @return
     * @throws Exception
     */
	//commenting methods using vaadin components
    /*public  long importSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
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

    }*/

    /*/**
     * called when the verifier or approver edits the response
     * @param userContext
     * @param surveyDef
     * @param responseId
     * @param surveyResponse
     * @throws Exception
     *//*/*/
   /* public  SurveyResponse updateSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
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
				List<SectionResponse> sections = persistWrapper.readList(SectionResponse.class, "select * from TAB_SECTION_RESPONSE where responseId = ?", surveyResponse.getResponseId());
				List<FormResponseDesc> itemResponses = persistWrapper.readList(FormResponseDesc.class, "select * from TAB_RESPONSE_desc where responseId = ?", surveyResponse.getResponseId());
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

    }*/

    @GetMapping("/getSurveyItemResponse")
	public  List getSurveyItemResponse(@RequestBody GetSurveyItemResponseRequest getSurveyItemResponseRequest)
	    throws Exception
    {
    	return surveyResponseService.getSurveyItemResponse(getSurveyItemResponseRequest.getSurveyDef(),
		getSurveyItemResponseRequest.getSurveyItemId(), getSurveyItemResponseRequest.getResponseMasterSet());
    }

	@GetMapping("/getSurveyResponse/{responseId}")
	public  SurveyResponse getSurveyResponse(@PathVariable("responseId") int responseId) throws Exception
	{
		return surveyResponseService.getSurveyResponse(responseId);
	}

	@GetMapping("/getSurveyResponse")
	public  SurveyResponse getSurveyResponse(@RequestBody GetSurveyResponseRequest getSurveyResponseRequest) throws Exception
    {
    	return surveyResponseService.getSurveyResponse(getSurveyResponseRequest.getSurveyDef(), getSurveyResponseRequest.getResponseId());
    }


    /**
     * count of responses recieved for a survey between the given dates.
     * includes the responses recieved on the start and end date
     * 
     * @param
     * @param
     */
    // public  long getResponseCountByDateRange(Survey survey, Date
    // startDate, Date endDate)throws Exception
    // {
    // return SurveyResponseManager.getResponseCountByDateRange(survey,
    // startDate, endDate);
    // }

	@GetMapping("/getResponseStatusSetForForm")
	public  List<String> getResponseStatusSetForForm(@RequestBody Survey survey) throws Exception
	{
		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return surveyResponseService.getResponseStatusSetForForm(context, survey);
	}

    /**
     * returns all the responseMaster records for a survey by a respondent in
     * descenting order of responseTime
     * 
     * @param
     * @param
     * @return
     */
    @GetMapping("/getResponseMastersForRespondent")
	public  List getResponseMastersForRespondent(@RequestBody GetResponseMastersForRespondentRequest getResponseMastersForRespondentRequest) throws Exception
    {
    	return surveyResponseService.getResponseMastersForRespondent(getResponseMastersForRespondentRequest.getSurveyPk(),
    			getResponseMastersForRespondentRequest.getRespondentPk());
    }

    /**
     * @param
     * @param
     * @return
     */
   @GetMapping("/getResponseMaster")
   public  ResponseMaster getResponseMaster(@RequestBody GetResponseMasterRequest getResponseMasterRequest) throws Exception
    {
    	return surveyResponseService.getResponseMaster(getResponseMasterRequest.getSurveyPk(), getResponseMasterRequest.getResponseId());
    }


    // ///////////////// for testsutra
	@GetMapping("/getResponseMaster/{responseId}")
	public  ResponseMasterNew getResponseMaster(@PathVariable("responseId") int responseId) throws Exception
	{
		return surveyResponseService.getResponseMaster(responseId);
	}

	@GetMapping("/getLatestResponseMasterForTest")
	public  ResponseMasterNew getLatestResponseMasterForTest(@RequestBody TestProcOID testProcOid) throws Exception
	{
		return surveyResponseService.getLatestResponseMasterForTest(testProcOid);
	}
    @GetMapping("/getLatestResponseMastersForUnitInWorkstation")
	public  ResponseMasterNew[] getLatestResponseMastersForUnitInWorkstation(@RequestBody GetLatestResponseMastersForUnitInWorkstationRequest getLatestResponseMastersForUnitInWorkstationRequest) throws Exception
	{
		return surveyResponseService.getLatestResponseMastersForUnitInWorkstation(new UnitOID(getLatestResponseMastersForUnitInWorkstationRequest.getUnitPk(), null), getLatestResponseMastersForUnitInWorkstationRequest.getProjectOID(),getLatestResponseMastersForUnitInWorkstationRequest.getWorkstationOID());
	}
    
	@GetMapping("/getResponseMasterForTestHistoryRecord")
	public  ResponseMasterNew getResponseMasterForTestHistoryRecord(@RequestBody GetResponseMasterForTestHistoryRecordRequest getResponseMasterForTestHistoryRecordRequest)
	{
		return surveyResponseService.getResponseMasterForTestHistoryRecord(
				getResponseMasterForTestHistoryRecordRequest.getTestProcOID(), getResponseMasterForTestHistoryRecordRequest.getFormOID());
	}
    

    @PutMapping("/verifyResponse")
	public  void verifyResponse(
	    @RequestBody VerifyResponseRequest verifyResponseRequest) throws Exception
    {

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyResponseService.verifyResponse(userContext,
				   verifyResponseRequest.getSResponse(), verifyResponseRequest.getComments());
			TestProcObj testProc = testProcService.getTestProc(verifyResponseRequest.getSResponse().getTestProcPk());
		    UnitObj unit =unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
		    ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.verifyForm,
					"Form Verified", new Date(), new Date(), (int) project.getPk(), testProc.getPk(), (int) unit.getPk(), testProc.getWorkstationPk(),
					verifyResponseRequest.getSResponse().getSurveyPk(), null, verifyResponseRequest.getSResponse().getResponseId());
			activityLoggingDelegate.logActivity(aLog);




    }

    @PutMapping("/rejectResponse")
	public  void rejectResponse(
	    @RequestBody RejectResponseRequest rejectResponseRequest) throws Exception
    {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
			surveyResponseService.rejectResponse(userContext,
				    rejectResponseRequest.getSResponse(),rejectResponseRequest.getComments());
			TestProcObj testProc = testProcService.getTestProc(rejectResponseRequest.getSResponse().getTestProcPk());
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectVerifyForm,
					"Form Verification Rejected", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
                    (int) unit.getPk(), testProc.getWorkstationPk(),
					rejectResponseRequest.getSResponse().getSurveyPk(), null, rejectResponseRequest.getSResponse().getResponseId());
			activityLoggingDelegate.logActivity(aLog);
	


    }

   @PutMapping("/approveResponse")
   public  void approveResponse(
    		@RequestBody ApproveResponseRequest  approveResponseRequest) throws Exception
    {
		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	    	surveyResponseService.approveResponse(userContext,approveResponseRequest.getResp(), approveResponseRequest.getComments());
	    	TestProcObj testProc = testProcService.getTestProc(approveResponseRequest.getResp().getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
	    				"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, approveResponseRequest.getResp().getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }

    @PutMapping("/approveResponseWithComments")
	public  void approveResponseWithComments(
    		@RequestBody ApproveResponseWithCommentsRequest approveResponseWithCommentsRequest) throws Exception
    {

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	surveyResponseService.approveResponseWithComments(userContext, approveResponseWithCommentsRequest.getResp(), approveResponseWithCommentsRequest.getComments());
	    	TestProcObj testProc = testProcService.getTestProc(approveResponseWithCommentsRequest.getResp().getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
	    				"Form approved with comments", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, approveResponseWithCommentsRequest.getResp().getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }

	@PutMapping("/approveResponseInBulk")
	public  void approveResponseInBulk(@RequestBody ApproveResponseInBulkRequest approveResponseInBulkRequest) throws Exception
	{

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
    		for (Iterator iterator =approveResponseInBulkRequest.getSelectedList().iterator(); iterator.hasNext();) {
				AssignedTestsQuery assignedTestsQuery = (AssignedTestsQuery) iterator.next();

				ResponseMasterNew resp = surveyResponseService.getResponseMaster(assignedTestsQuery.getResponseId());

				surveyResponseService.approveResponse(userContext, resp, approveResponseInBulkRequest.getComment());

				TestProcObj testProc = testProcService.getTestProc(resp.getTestProcPk());

				ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), Actions.approveForm,
						"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(),
						testProc.getUnitPk(), testProc.getWorkstationPk(),
						resp.getFormPk(), null, resp.getResponseId());
				activityLoggingDelegate.logActivity(aLog);
			}
	}
	
    
    @PutMapping("/rejectApproval")
	public  void rejectApproval(
	    @RequestBody RejectApprovalRequest  rejectApprovalRequest) throws Exception
    {

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyResponseService.rejectApproval(userContext,
				    rejectApprovalRequest.getSResponse(), rejectApprovalRequest.getComments());
			TestProcObj testProc = testProcService.getTestProc(rejectApprovalRequest.getSResponse().getTestProcPk());
		    UnitObj unit =unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectApproveForm,
					"Form Approval Rejected", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
					testProc.getUnitPk(), testProc.getWorkstationPk(), 
					rejectApprovalRequest.getSResponse().getSurveyPk(), null, rejectApprovalRequest.getSResponse().getResponseId());
		activityLoggingDelegate.logActivity(aLog);
	

    }
    

    @PutMapping("/reopenApproved ")
	public  void reopenApproved( @RequestBody ReopenApprovedRequest reopenApprovedRequest) throws Exception
    {

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	surveyResponseService.reopenApprovedForm(userContext,
	    		    reopenApprovedRequest.getSResponse(), reopenApprovedRequest.getComments());
	    	TestProcObj testProc = testProcService.getTestProc(reopenApprovedRequest.getSResponse().getTestProcPk());
		    UnitObj unit = unitService.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = projectService.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery((int) userContext.getUser().getPk(), (BaseActions) Actions.rejectApproveForm,
	    			"Approved form reopened", new Date(), new Date(), (int) project.getPk(), testProc.getPk(),
	    			testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    			reopenApprovedRequest.getSResponse().getSurveyPk(), null, reopenApprovedRequest.getSResponse().getResponseId());
	    	activityLoggingDelegate.logActivity(aLog);
	

    }
        
    
    /**
     * returns if sections are locked by other users
     * parameter surveyQuestions is filledout by this function with the sections that should be saved.
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
	@GetMapping("/getQuestionsToSaveResponsesFor")
	private  boolean getQuestionsToSaveResponsesFor(@RequestBody GetQuestionsToSaveResponsesForRequest getQuestionsToSaveResponsesForRequest)throws Exception
	{
		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean hasSectionsLockedByOthers = false;
		for (Iterator iter = getQuestionsToSaveResponsesForRequest.getSurveyDef().getQuestions().iterator(); iter.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iter.next();
			if(aItem instanceof Section)
			{
				ObjectLockQuery lock = surveyMasterService.getCurrentLock(getQuestionsToSaveResponsesForRequest.getResponseOID(),
						aItem.getSurveyItemId());
			
				if(lock == null)
				{
					
				}
				else if(lock.getUserPk() == context.getUser().getPk())
				{
					//locked my me.. 
					getQuestionsToSaveResponsesForRequest.getSurveyQuestions().add((Section)aItem);
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

	@GetMapping("/validateResponse")
	public  LinkedHashMap<String, List<String>> validateResponse(@RequestBody ValidateResponseRequest validateResponseRequest)
	{
		LinkedHashMap<String, List<String>> errors = new LinkedHashMap<String, List<String>>();
		for (Iterator iter = validateResponseRequest.getSurveyQuestions().iterator(); iter.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iter.next();
			if(aItem instanceof SurveySaveItem)
			{
				SurveyItemResponse sItemResponse =validateResponseRequest.getSurveyResponse().getAnswer((SurveySaveItem)aItem);
				List<String> er = ((SurveySaveItem)aItem).validateResponse(sItemResponse);
				if(er != null)
					errors.put(aItem.getSurveyItemId(), er);
			}
		}
		return errors;
	}
	
	
	/*public  List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd)
	{
		return surveyResponseService.getSectionResponseSummary(sd);
	}*/

	@GetMapping("/getSectionResponseSummary")
	public  List<SectionResponseQuery> getSectionResponseSummary(@RequestBody GetSectionResponseSummaryRequest getSectionResponseSummaryRequest)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(getSectionResponseSummaryRequest.getSd(), getSectionResponseSummaryRequest.getResponseId());
	}
//commenting methods using vaadin components
	/*public  SectionResponseQuery getSectionResponseSummary(SurveyDefinition sd, int responseId, String sectionId)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(sd, responseId, sectionId);
	}*/

	@GetMapping("/getSectionResponseSummarys")
	public  SectionResponseQuery getSectionResponseSummary(@RequestBody GetSectionResponseSummarysRequest  getSectionResponseSummarysRequest)throws Exception
	{
		return surveyResponseService.getSectionResponseSummary(getSectionResponseSummarysRequest.getResponseId(),getSectionResponseSummarysRequest.getSectionId());
	}


	@PostMapping("/addToResponseVAComments")
	public  void addToResponseVAComments(@RequestBody AddToResponseVACommentsRequest addToResponseVACommentsRequest) throws Exception
	{

		UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		surveyResponseService.addToResponseVAComments(userContext, addToResponseVACommentsRequest.getResponseMaster(),addToResponseVACommentsRequest.getVCommentToAdd(), addToResponseVACommentsRequest.getACommentToAdd());
    		

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

	
	@GetMapping("/getFormItemResponses")
	public FormItemResponse getFormItemResponses(@RequestBody  FormItemResponseOID formItemResponseOID)
	{
		return	surveyResponseService.getFormItemResponse(formItemResponseOID);
	}

	
	@PostMapping("/createFormItemResponse")
	public  FormItemResponse createFormItemResponse(@RequestBody CreateFormItemResponseRequest createFormItemResponseRequest) throws Exception {


    		FormItemResponse r = surveyResponseService.createFormItemResponse(createFormItemResponseRequest.getResponseId(), createFormItemResponseRequest.getSurveyItemId());

			return r;
	}

	@GetMapping("/getFormItemResponse")
	public  FormItemResponse getFormItemResponse(@RequestBody GetFormItemResponseRequest getFormItemResponseRequest) throws Exception {


    		FormItemResponse r = surveyResponseService.getFormItemResponse(getFormItemResponseRequest.getResponseId(), getFormItemResponseRequest.getSurveyItemId(), getFormItemResponseRequest.isCreateIfNoExist());

			return r;


	}

	@GetMapping("/getFormItemResponses/{responseId}")
	public  HashMap<String, FormItemResponse> getFormItemResponses(@PathVariable("responseId") int responseId)
	{
		return	surveyResponseService.getFormItemResponses(responseId);
	}


	@GetMapping("/getFormClientSubmissionRevision/{responseId}")
	public  FormResponseClientSubmissionRev getFormClientSubmissionRevision(@PathVariable("responseId") int responseId)
	{
		return surveyResponseService.getFormClientSubmissionRevision(responseId);
}
	
	@PostMapping("/saveFormClientSubmissionRevision")
	public  FormResponseClientSubmissionRev saveFormClientSubmissionRevision(@RequestBody SaveFormClientSubmissionRevisionRequest saveFormClientSubmissionRevisionRequest) throws Exception
	{

		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            FormResponseClientSubmissionRev r = surveyResponseService.saveFormClientSubmissionRevision(context, saveFormClientSubmissionRevisionRequest.getResponseId(), saveFormClientSubmissionRevisionRequest.getRevision());

			return r;

	}


	@PostMapping("/saveSyncErrorResponse")
	public  void saveSyncErrorResponse(@RequestBody SaveSyncErrorResponseRequest saveSyncErrorResponseRequest) throws Exception
	{
		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            surveyResponseService.saveSyncErrorResponse(context, saveSyncErrorResponseRequest.getResponseId(),saveSyncErrorResponseRequest.getFormResponseBean());



	}

//commenting methods using vaadin component
/*	public  FormResponseBean getFormResponseBean(UserContext context, int responseId)throws Exception
	{
		return surveyResponseService.getFormResponseBean(context, responseId);
	}*/
	
	@GetMapping("/getEntityRevisionReviewProxy/{responseId}")
	public EntityVersionReviewProxy getEntityRevisionReviewProxy(@PathVariable("responseId") int responseId) throws Exception
	{
		UserContext context = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return surveyResponseService.getEntityRevisionForReview(context.getUser().getOID(), responseId);
	}
	
	@GetMapping("/getFormResponseBeanForSyncErrorReview/{entityVersionReviewPk}")
	public FormResponseBean getFormResponseBeanForSyncErrorReview(@PathVariable("entityVersionReviewPk") int entityVersionReviewPk) throws Exception
	{
		return surveyResponseService.getFormResponseBeanForSyncErrorReview(entityVersionReviewPk);
	}
}
