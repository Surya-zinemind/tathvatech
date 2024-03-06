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

import org.apache.log4j.Logger;

import com.sarvasutra.etest.api.model.FormResponseBean;
import com.tathvatech.ts.caf.activitylogging.ActivityLogQuery;
import com.tathvatech.ts.caf.activitylogging.ActivityLoggingDelegate;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.project.FormOID;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.project.TestProcObj;
import com.tathvatech.ts.core.project.UnitInProject;
import com.tathvatech.ts.core.project.UnitLocation;
import com.tathvatech.ts.core.project.UnitLocationQuery;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.UnitObj;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.tathvatech.ts.core.survey.ObjectLock;
import com.tathvatech.ts.core.survey.ObjectLockQuery;
import com.tathvatech.ts.core.survey.Survey;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.response.EntityVersionReviewProxy;
import com.tathvatech.ts.core.survey.response.FormItemResponse;
import com.tathvatech.ts.core.survey.response.FormItemResponseOID;
import com.tathvatech.ts.core.survey.response.FormResponseClientSubmissionRev;
import com.tathvatech.ts.core.survey.response.FormResponseDesc;
import com.tathvatech.ts.core.survey.response.FormResponseMaster;
import com.tathvatech.ts.core.survey.response.FormResponseOID;
import com.tathvatech.ts.core.survey.response.ResponseMaster;
import com.tathvatech.ts.core.survey.response.ResponseMasterNew;
import com.tathvatech.ts.core.survey.response.SectionResponse;
import com.tathvatech.ts.core.survey.response.SurveyItemResponse;
import com.tathvatech.ts.core.survey.response.SurveyResponse;
import com.tathvatech.ts.core.workflow.FormWorkflow;
import com.thirdi.surveyside.project.AssignedTestsQuery;
import com.thirdi.surveyside.project.Project;
import com.thirdi.surveyside.project.ProjectDelegate;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.project.TestProcManager;
import com.thirdi.surveyside.project.UnitInProjectObj;
import com.thirdi.surveyside.project.UnitManager;
import com.thirdi.surveyside.project.WorkflowManager;
import com.thirdi.surveyside.security.Actions;
import com.thirdi.surveyside.survey.LockedByAnotherUserException;
import com.thirdi.surveyside.survey.SurveyItem;
import com.thirdi.surveyside.survey.SurveyMaster;
import com.thirdi.surveyside.survey.delegate.SurveyDelegate;
import com.thirdi.surveyside.survey.response.ResponseSubmissionBookmark;
import com.thirdi.surveyside.survey.response.SectionResponseQuery;
import com.thirdi.surveyside.survey.response.SurveyResponseManager;
import com.thirdi.surveyside.survey.surveyitem.Section;
import com.thirdi.surveyside.survey.surveyitem.SurveySaveItem;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyResponseDelegate
{
    private static final Logger logger = Logger
	    .getLogger(SurveyResponseDelegate.class);


    /**
     * Called when the workstation is changed to in progress
     */
    public static SurveyResponse ceateDummyResponse(UserContext context, 
	    SurveyResponse surveyResponse) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);

		    SurveyResponse resp = SurveyResponseManager.ceateDummyResponse(context, 
			    surveyResponse);
		    
			conn.commit();
	
		    return resp;
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
     * 
     */
    public static SurveyResponse savePageResponse(UserContext context, 
	    SurveyResponse surveyResponse) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
		    
		    TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
		    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
		    {
		    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
		    }
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = UnitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
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
			UnitLocationQuery unitWorkstation = ProjectDelegate.getUnitWorkstationStatus(testProc.getUnitPk(), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }
	
		    ArrayList sectionsToSave = new ArrayList();
			List<String> lockedSectionIds = SurveyDelegate.getLockedSectionIds((User) context.getUser(), surveyResponse.getOID());
			
			SurveyDefinition sd = surveyResponse.getSurveyDefinition();
			for (Iterator iterator = sd.getQuestions().iterator(); iterator.hasNext();)
			{
				Section aSection = (Section) iterator.next();
				if(lockedSectionIds.contains(aSection.getSurveyItemId()))
				{
					sectionsToSave.add(aSection);
				}
			}
			SurveyResponse resp = SurveyResponseManager.saveSectionResponses(context, project, unit, 
			    surveyResponse, sectionsToSave);
		    
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, context.getUser().getPk(), resp.getResponseId(), 
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			//record workstation save
			ProjectManager.recordWorkstationSave(testProc.getOID());
			
			//TODO lines below is added for testing purpose. we create sync error record when a normal save is done.
			//This needs to be removed.
//			FormResponseBean formResponseBean = getFormResponseBean(context, resp.getResponseId());
//			saveSyncErrorResponse(context, resp.getResponseId(), formResponseBean);

			
			conn.commit();
	
		    return resp;
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
     * this function is just a save of the current response. we also take a history printout of the current response data and save it as an attachment
     * we could also email the manager or do some other actions based on this user interaction.
     * added as part of the signalling project implementation.
     * @param context
     * @param surveyResponse
     * @return
     * @throws Exception
     */
    public static SurveyResponse submitInterimResponse(UserContext context, 
    	    SurveyResponse surveyResponse) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
		    
    	
		    SurveyResponse sResponseAfterSave = savePageResponse(context, surveyResponse);
		    
		    
		    SurveyResponseManager.saveResponseStateAsSubmitRecord(context, sResponseAfterSave.getResponseId(), ResponseSubmissionBookmark.SubmissionTypeEnum.Interim);

			conn.commit();
    	
	    	return sResponseAfterSave;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		    conn.rollback();
		    throw ex;
		}
		finally
		{
		}
	}    

    /**
     * save a specific set of questions and not a complete section.
     */
    public static SurveyResponse saveQuestionResponses(UserContext context, 
	    SurveyResponse surveyResponse, List questionsToSave) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
		    
		    TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
		    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
		    {
		    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
		    }
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = UnitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit/Car is complete, You cannot submit any changes to the form");
		    }
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
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
			UnitLocationQuery unitWorkstation = ProjectDelegate.getUnitWorkstationStatus(testProc.getUnitPk(), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }
	
			SurveyResponse resp = SurveyResponseManager.saveSpecificQuestionResponse(context, project, unit, 
			    surveyResponse, questionsToSave);
		    
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, context.getUser().getPk(), resp.getResponseId(), 
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			//record workstation save
			ProjectManager.recordWorkstationSave(testProc.getOID());
			
			conn.commit();
	
		    return resp;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		    conn.rollback();
		    throw ex;
		}
		finally
		{
		}
    }

    /**
     * @param sd
     * @param responseId
     */
    public static void finalizeSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {
		Connection conn = null;
	    conn = ServiceLocator.locate().getConnection();
	    conn.setAutoCommit(false);

	    TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
	    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
	    {
	    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
	    }
		try
		{
			UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = UnitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
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
			List<String> lockedSectionIds = SurveyDelegate.getLockedSectionIds((User) userContext.getUser(), 
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
			UnitLocationQuery unitWorkstation = ProjectDelegate.getUnitWorkstationStatus(testProc.getUnitPk(), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be submitted");
			}
			else if(UnitLocation.STATUS_WAITING.equals(unitWorkstation.getStatus()))
			{
				//save the response
				surveyResponse = SurveyResponseManager.saveSectionResponses(userContext, project, unit, 
						surveyResponse, sectionsToSave);
				
				//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
				// clicking through from the myassignments table.
				WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), surveyResponse.getResponseId(), 
						testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

				throw new AppException("Workstation associated with the test is in Waiting status, Form Saved but not Submitted");
			}
			else if(!(ResponseMasterNew.STATUS_INPROGRESS.equals(surveyResponse.getStatus())))
		    {
				throw new AppException("Form has already been submitted, You cannot submit any changes to the form");
		    }

			//save the response
			surveyResponse = SurveyResponseManager.saveSectionResponses(userContext, project, unit, 
					surveyResponse, sectionsToSave);
			
			//add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
			// clicking through from the myassignments table.
			WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), surveyResponse.getResponseId(), 
					testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);

			boolean hasSectionsLockedByOtherUsers = false;
			List<ObjectLock> lockedSections = SurveyMaster.getLockedSectionIds(surveyResponse.getOID());
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
					SurveyResponseManager.finalizeSurveyResponse(userContext, surveyDef, surveyResponse.getResponseId());
					ActivityLogQuery aLog1 = new ActivityLogQuery(userContext.getUser().getPk(), Actions.submitForm, 
							"Form Submitted", new Date(), new Date(), project.getPk(), testProc.getPk(), testProc.getUnitPk(), testProc.getWorkstationPk(), 
							surveyResponse.getSurveyPk(), null, surveyResponse.getResponseId());
					ActivityLoggingDelegate.logActivity(aLog1);
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
						SurveyMaster.releaseSectionEditLock(userContext, (User) userContext.getUser(), 
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
			
			
		    conn.commit();
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
						SurveyMaster.releaseSectionEditLock(userContext, (User) userContext.getUser(), 
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
			conn.commit();
			throw ax;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		    conn.rollback();
		    throw ex;
		}
		finally
		{
		}
    }

    public static List<ResponseSubmissionBookmark> getResponseSubmissionBookmarks(TestProcOID testprocOID)
    {
    	return SurveyResponseManager.getResponseSubmissionBookmarks(testprocOID);
    }

    public static ResponseSubmissionBookmark getLastResponseSubmissionBookmark(TestProcOID testprocOID)
    {
    	return SurveyResponseManager.getLastResponseSubmissionBookmark(testprocOID);
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
    public static long importSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {
	Connection conn = null;
	try
	{
	    conn = ServiceLocator.locate().getConnection();
	    conn.setAutoCommit(false);
	    
	    TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
	    if(surveyResponse.getSurveyPk() != testProc.getFormPk())
	    {
	    	throw new AppException("Invalid checksheet, Form cannot be submitted: TP-" + surveyResponse.getTestProcPk());
	    }
	    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
	    Project project = ProjectManager.getProject(testProc.getProjectPk());

	    SurveyResponse resp = SurveyResponseManager.saveSectionResponses(userContext, project, unit,  
		    surveyResponse, surveyDef.getQuestions());
		
	    //add a workflow entry with the new responseId and the same status.. that way the response will be pickedup when
		// clicking through from the myassignments table.
		WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_START, userContext.getUser().getPk(), resp.getResponseId(), 
				testProc.getPk(), ResponseMasterNew.STATUS_INPROGRESS, null);


		SurveyResponseManager.finalizeSurveyResponse(userContext, surveyDef,
		    resp.getResponseId(),
		    surveyResponse.getResponseCompleteTime());

	    conn.commit();

	    return resp.getResponseId();
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
     * called when the verifier or approver edits the response
     * @param userContext
     * @param surveyDef
     * @param responseId
     * @param surveyResponse
     * @throws Exception
     */
    public static SurveyResponse updateSurveyResponse(UserContext userContext, SurveyDefinition surveyDef,
	    SurveyResponse surveyResponse) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
		    
		    if(ResponseMasterNew.STATUS_COMPLETE.equals(surveyResponse.getStatus()) || ResponseMasterNew.STATUS_VERIFIED.equals(surveyResponse.getStatus()))
		    {
		    }
		    else
		    {
		    	throw new AppException("Testing is not complete on the form, edit failed");
		    }
		    
		    TestProcObj testProc = TestProcManager.getTestProc(surveyResponse.getTestProcPk());
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    UnitInProjectObj upr = UnitManager.getUnitInProject(new UnitOID(testProc.getUnitPk()), new ProjectOID(testProc.getProjectPk()));
		    if(UnitInProject.STATUS_CLOSED.equals(upr.getStatus()))
		    {
		    	throw new AppException("Unit is complete, You cannot submit any changes to the form");
		    }
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
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
			UnitLocationQuery unitWorkstation = ProjectDelegate.getUnitWorkstationStatus(testProc.getUnitPk(), new ProjectOID(testProc.getProjectPk(), null), new WorkstationOID(testProc.getWorkstationPk(), null));
			if(UnitLocation.STATUS_COMPLETED.equals(unitWorkstation.getStatus()))
			{
				throw new AppException("Workstation is complete for the unit, Form cannot be saved or submitted");
			}

			//when editing a response, to maintain history of what the tester had submitted, the response should be copied to old.
			// find the last workflow entry and if the action on that is editform, the response has been copied and edited, so continue edit on that. 
			//else save the current response to a backup,
			FormWorkflow lastEntry = WorkflowManager.getLastEntry(testProc.getOID());

			SurveyResponse responseToReturn;
			if(FormWorkflow.ACTION_EDIT_RESPONSE.equals(lastEntry.getAction()))
			{
				SurveyResponse resp = SurveyResponseManager.updateSurveyResponse(userContext, project, unit, surveyDef,
					    surveyResponse.getResponseId(), surveyResponse.getSurveyItemAnswerMap(), surveyDef.getQuestions());
				responseToReturn = resp;
			}
			else
			{
				//when copyResponse is called, the new response is created as current, mark the existing one as old 
				ResponseMasterNew responseMaster = getResponseMaster(surveyResponse.getResponseId());
				SurveyResponseManager.markResponseAsOld(userContext, responseMaster);
				
				//create a copy of the response and mark it as incomplete so that the tester can
				//carry on editing the form and resubmitting it
				//create the new response, and set the current = true
				FormResponseMaster respMaster = PersistWrapper.readByResponseId(FormResponseMaster.class, surveyResponse.getResponseId());
				List<SectionResponse> sections = PersistWrapper.readList(SectionResponse.class, "select * from TAB_SECTION_RESPONSE where responseId = ?", surveyResponse.getResponseId());
				List<FormResponseDesc> itemResponses = PersistWrapper.readList(FormResponseDesc.class, "select * from TAB_RESPONSE_desc where responseId = ?", surveyResponse.getResponseId());
				respMaster.setResponseId(0);
				respMaster.setCurrent(true);
				int newResponseId = PersistWrapper.createEntity(respMaster);
				
				for (Iterator iterator = sections.iterator(); iterator.hasNext();) 
				{
					SectionResponse sectionResponse = (SectionResponse) iterator.next();
					sectionResponse.setPk(0);
					sectionResponse.setResponseId(newResponseId);
					PersistWrapper.createEntity(sectionResponse);
				}
				for (Iterator iterator = itemResponses.iterator(); iterator.hasNext();) 
				{
					FormResponseDesc formResponseDesc = (FormResponseDesc) iterator.next();
					formResponseDesc.setResponseId(newResponseId);
					PersistWrapper.createEntity(formResponseDesc);
				}
				
				// TODO:: hack hack. here since the responseId changes, the FormItemResponse reference will be lost since the
				// responseId is changed.. we should actually have the copy archieved and the current one continued.. but that need to be tested properly.
				// so for now I am changing the responseId to the new one.
				PersistWrapper.executeUpdate("update TAB_ITEM_RESPONSE set responseId = ? where responseId = ?", newResponseId, surveyResponse.getResponseId());

				SurveyResponse newResponse = SurveyResponseManager.updateSurveyResponse(userContext, project, unit, surveyDef,
					    newResponseId, surveyResponse.getSurveyItemAnswerMap(), surveyDef.getQuestions());
	
				
				
				WorkflowManager.addWorkflowEntry(FormWorkflow.ACTION_EDIT_RESPONSE, userContext.getUser().getPk(), newResponse.getResponseId(), 
						testProc.getPk(), lastEntry.getResultStatus(), "Edit performed");


				responseToReturn = SurveyResponseManager.getSurveyResponse(newResponseId);
			}
			//record workstation save
			ProjectManager.recordWorkstationSave(testProc.getOID());
			
			conn.commit();

			return responseToReturn;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		    conn.rollback();
		    throw ex;
		}
		finally
		{
		}
    }

    public static List getSurveyItemResponse(SurveyDefinition surveyDef,
	    String surveyItemId, ResponseMaster[] responseMasterSet)
	    throws Exception
    {
    	return SurveyResponseManager.getSurveyItemResponse(surveyDef,
		surveyItemId, responseMasterSet);
    }

	public static SurveyResponse getSurveyResponse(int responseId) throws Exception
	{
		return SurveyResponseManager.getSurveyResponse(responseId);
	}

	public static SurveyResponse getSurveyResponse(SurveyDefinition surveyDef,
	    int responseId) throws Exception
    {
    	return SurveyResponseManager.getSurveyResponse(surveyDef, responseId);
    }


    /**
     * count of responses recieved for a survey between the given dates.
     * includes the responses recieved on the start and end date
     * 
     * @param startDate
     * @param endDate
     */
    // public static long getResponseCountByDateRange(Survey survey, Date
    // startDate, Date endDate)throws Exception
    // {
    // return SurveyResponseManager.getResponseCountByDateRange(survey,
    // startDate, endDate);
    // }

	public static List<String> getResponseStatusSetForForm(UserContext context, Survey survey) throws Exception
	{
		return SurveyResponseManager.getResponseStatusSetForForm(context, survey);
	}

    /**
     * returns all the responseMaster records for a survey by a respondent in
     * descenting order of responseTime
     * 
     * @param surveyPk
     * @param respondentPk
     * @return
     */
    public static List getResponseMastersForRespondent(int surveyPk,
	    int respondentPk) throws Exception
    {
    	return SurveyResponseManager.getResponseMastersForRespondent(surveyPk,
    			respondentPk);
    }

    /**
     * @param surveyPk
     * @param responseId
     * @return
     */
    public static ResponseMaster getResponseMaster(int surveyPk,
	    String responseId) throws Exception
    {
    	return SurveyResponseManager.getResponseMaster(surveyPk, responseId);
    }


    // ///////////////// for testsutra
	public static ResponseMasterNew getResponseMaster(int responseId) throws Exception
	{
		return SurveyResponseManager.getResponseMaster(responseId);
	}

	public static ResponseMasterNew getLatestResponseMasterForTest(TestProcOID testProcOid) throws Exception
	{
		return SurveyResponseManager.getLatestResponseMasterForTest(testProcOid);
	}
    public static ResponseMasterNew[] getLatestResponseMastersForUnitInWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception
	{
		return SurveyResponseManager.getLatestResponseMastersForUnitInWorkstation(new UnitOID(unitPk, null), projectOID, workstationOID);
	}
    
	public static ResponseMasterNew getResponseMasterForTestHistoryRecord(TestProcOID testProcOID, FormOID formOID)
	{
		return SurveyResponseManager.getResponseMasterForTestHistoryRecord(testProcOID, formOID);
	}
    

    public static void verifyResponse(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
			SurveyResponseManager.verifyResponse(userContext,
				    sResponse, comments);
			TestProcObj testProc = TestProcManager.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
		    ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.verifyForm, 
					"Form Verified", new Date(), new Date(), project.getPk(), testProc.getPk(), unit.getPk(), testProc.getWorkstationPk(), 
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
			ActivityLoggingDelegate.logActivity(aLog);
	
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

    public static void rejectResponse(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
			SurveyResponseManager.rejectResponse(userContext,
				    sResponse, comments);
			TestProcObj testProc = TestProcManager.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.rejectVerifyForm, 
					"Form Verification Rejected", new Date(), new Date(), project.getPk(), testProc.getPk(), 
					unit.getPk(), testProc.getWorkstationPk(), 
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
			ActivityLoggingDelegate.logActivity(aLog);
	
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

    public static void approveResponse(UserContext userContext,
    		ResponseMasterNew resp, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
	    	SurveyResponseManager.approveResponse(userContext, resp, comments);
	    	TestProcObj testProc = TestProcManager.getTestProc(resp.getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.approveForm, 
	    				"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, resp.getResponseId());
	    	ActivityLoggingDelegate.logActivity(aLog);
	
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

    public static void approveResponseWithComments(UserContext userContext,
    		ResponseMasterNew resp, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
	    	SurveyResponseManager.approveResponseWithComments(userContext, resp, comments);
	    	TestProcObj testProc = TestProcManager.getTestProc(resp.getTestProcPk());
//		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
//		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.approveForm, 
	    				"Form approved with comments", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
	    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    				testProc.getFormPk(), null, resp.getResponseId());
	    	ActivityLoggingDelegate.logActivity(aLog);
	
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

	public static void approveResponseInBulk(UserContext userContext, List<AssignedTestsQuery> selectedList, String comment) throws Exception
	{

		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
    		for (Iterator iterator = selectedList.iterator(); iterator.hasNext();)
			{
				AssignedTestsQuery assignedTestsQuery = (AssignedTestsQuery) iterator.next();

				ResponseMasterNew resp = SurveyResponseManager.getResponseMaster(assignedTestsQuery.getResponseId());
				
		    	SurveyResponseManager.approveResponse(userContext, resp, comment);
			    
		    	TestProcObj testProc = TestProcManager.getTestProc(resp.getTestProcPk());

			    ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.approveForm, 
		    				"Form Approved", new Date(), new Date(), testProc.getProjectPk(), testProc.getPk(), 
		    				testProc.getUnitPk(), testProc.getWorkstationPk(), 
		    				resp.getFormPk(), null, resp.getResponseId());
		    	ActivityLoggingDelegate.logActivity(aLog);
			}
	
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
	
    
    public static void rejectApproval(UserContext userContext,
	    SurveyResponse sResponse, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
			SurveyResponseManager.rejectApproval(userContext,
				    sResponse, comments);
			TestProcObj testProc = TestProcManager.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
			ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.rejectApproveForm, 
					"Form Approval Rejected", new Date(), new Date(), project.getPk(), testProc.getPk(), 
					testProc.getUnitPk(), testProc.getWorkstationPk(), 
					sResponse.getSurveyPk(), null, sResponse.getResponseId());
			ActivityLoggingDelegate.logActivity(aLog);
	
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
    

    public static void reopenApproved(UserContext userContext, SurveyResponse sResponse, String comments) throws Exception
    {
		Connection conn = null;
		try
		{
		    conn = ServiceLocator.locate().getConnection();
		    conn.setAutoCommit(false);
	
	    	SurveyResponseManager.reopenApprovedForm(userContext,
	    		    sResponse, comments);
	    	TestProcObj testProc = TestProcManager.getTestProc(sResponse.getTestProcPk());
		    UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(testProc.getUnitPk()));
		    Project project = ProjectManager.getProject(testProc.getProjectPk());
	    	ActivityLogQuery aLog = new ActivityLogQuery(userContext.getUser().getPk(), Actions.rejectApproveForm, 
	    			"Approved form reopened", new Date(), new Date(), project.getPk(), testProc.getPk(), 
	    			testProc.getUnitPk(), testProc.getWorkstationPk(), 
	    			sResponse.getSurveyPk(), null, sResponse.getResponseId());
	    	ActivityLoggingDelegate.logActivity(aLog);
	
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
     * returns if sections are locked by other users
     * parameter surveyQuestions is filledout by this function with the sections that should be saved.
     * @param surveyResponse
     * @param surveyQuestions
     * @param errors
     * @return
     * @throws Exception
     */
	private static boolean getQuestionsToSaveResponsesFor(UserContext context, SurveyDefinition surveyDef, List<Section> surveyQuestions, 
			FormResponseOID responseOID)throws Exception
	{
		boolean hasSectionsLockedByOthers = false;
		for (Iterator iter = surveyDef.getQuestions().iterator(); iter.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iter.next();
			if(aItem instanceof Section)
			{
				ObjectLockQuery lock = SurveyDelegate.getCurrentLock(responseOID, 
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

	public static LinkedHashMap<String, List<String>> validateResponse(SurveyResponse surveyResponse, List surveyQuestions)
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
	
	
	public static List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd)
	{
		return SurveyResponseManager.getSectionResponseSummary(sd);
	}

	public static List<SectionResponseQuery> getSectionResponseSummary(SurveyDefinition sd, int responseId)throws Exception
	{
		return SurveyResponseManager.getSectionResponseSummary(sd, responseId);
	}

	public static SectionResponseQuery getSectionResponseSummary(SurveyDefinition sd, int responseId, String sectionId)throws Exception
	{
		return SurveyResponseManager.getSectionResponseSummary(sd, responseId, sectionId);
	}

	public static SectionResponseQuery getSectionResponseSummary(int responseId, String sectionId)throws Exception
	{
		return SurveyResponseManager.getSectionResponseSummary(responseId, sectionId);
	}


	public static void addToResponseVAComments(UserContext userContext,	ResponseMasterNew responseMaster, String vCommentToAdd,
			String aCommentToAdd) throws Exception 
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		SurveyResponseManager.addToResponseVAComments(userContext, responseMaster, vCommentToAdd, aCommentToAdd);
    		
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

//	public static String addToSurveyItemResponseComment(UserContext userContext,	int responseId, String surveyItemId,
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

	
	public static FormItemResponse getFormItemResponse(FormItemResponseOID formItemResponseOID)
	{
		return	SurveyResponseManager.getFormItemResponse(formItemResponseOID);
	}

	
	public static FormItemResponse createFormItemResponse(int responseId, String surveyItemId)
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		FormItemResponse r = SurveyResponseManager.createFormItemResponse(responseId, surveyItemId);
			con.commit();
			return r;
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
	    finally
	    {
        	try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
        return null;
	}

	public static FormItemResponse getFormItemResponse(int responseId, String surveyItemId, boolean createIfNoExist)
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		FormItemResponse r = SurveyResponseManager.getFormItemResponse(responseId, surveyItemId, createIfNoExist);
			con.commit();
			return r;
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	    finally
	    {
	    }
        return null;
	}
	
	public static HashMap<String, FormItemResponse> getFormItemResponses(int responseId)
	{
		return	SurveyResponseManager.getFormItemResponses(responseId);
	}


	public static FormResponseClientSubmissionRev getFormClientSubmissionRevision(int responseId)
	{
		return SurveyResponseManager.getFormClientSubmissionRevision(responseId);
}
	
	public static FormResponseClientSubmissionRev saveFormClientSubmissionRevision(UserContext context, int responseId, String revision) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            FormResponseClientSubmissionRev r = SurveyResponseManager.saveFormClientSubmissionRevision(context, responseId, revision);
			con.commit();
			return r;
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	throw ex;
        }
	    finally
	    {
	    }
	}


	public static void saveSyncErrorResponse(UserContext context, int responseId, FormResponseBean formResponseBean) throws Exception
	{
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            SurveyResponseManager.saveSyncErrorResponse(context, responseId, formResponseBean);
			con.commit();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	throw ex;
        }
	    finally
	    {
	    }
	}
	
	public static FormResponseBean getFormResponseBean(UserContext context, int responseId)throws Exception
	{
		return SurveyResponseManager.getFormResponseBean(context, responseId);
	}
	
	public static EntityVersionReviewProxy getEntityRevisionReviewProxy(UserContext context, int responseId) throws Exception
	{
		return SurveyResponseManager.getEntityRevisionForReview(context.getUser().getOID(), responseId);
	}
	
	public static FormResponseBean getFormResponseBeanForSyncErrorReview(int entityVersionReviewPk) throws Exception
	{
		return SurveyResponseManager.getFormResponseBeanForSyncErrorReview(entityVersionReviewPk);
	}
}
