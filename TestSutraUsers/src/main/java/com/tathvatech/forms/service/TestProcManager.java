/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveyForm;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.survey.service.SurveyDefFactory;
import com.tathvatech.survey.service.SurveyResponseManager;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Service
@RequiredArgsConstructor
public class TestProcManager


{
    private static final Logger logger = Logger.getLogger(String.valueOf(TestProcManager.class));
    private final PersistWrapper persistWrapper;

	private final WorkstationService workstationService;

	private final SurveyResponseManager surveyResponseManager;

	/*public  void activateTestProcs(UserContext userContext,
										 List<UnitFormQuery> formsToActivate) throws Exception
	{
		//if there is no response for this form already, create response placeholder..this is needed when opening for the first time 
		//this is required since when filling up responses from
		//different tablets at the same time and submitting, they need to synchronize on the same
		//response id. so a placeholder is created as soon as the form is opened..

		for (Iterator iterator = formsToActivate.iterator(); iterator.hasNext();) 
		{
			UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();
			
			ResponseMasterNew response = surveyResponseManager.getLatestResponseMasterForTest(unitFormQuery.getOID());
			if(response == null)
			{
				SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(unitFormQuery.getFormPk(), unitFormQuery.getFormName()));
				SurveyResponse sResponse = new SurveyResponse(sd);
				sResponse.setSurveyPk(unitFormQuery.getFormPk());
				sResponse.setTestProcPk(unitFormQuery.getPk());
				sResponse.setTestProcPk(unitFormQuery.getPk());
				sResponse.setResponseStartTime(new Date());
				sResponse.setResponseCompleteTime(new Date());
				//ipaddress and mode set
				sResponse.setIpaddress("0.0.0.0");
				sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
				sResponse.setUser((User) userContext.getUser());
				sResponse = SurveyResponseManager.ceateDummyResponse(userContext, sResponse);
				// the save pageresponse automatically creates a new workflow entry for you..
				//so we need not create another one.. so commenting the below one..
			}		
			else
			{
				// the response is there, so if it is in paused status, we need to change it to InProgress
				if(response!= null && ResponseMasterNew.STATUS_PAUSED.equals(response.getStatus()))
				{
					surveyResponseManager.changeResponseStatus(userContext, response.getResponseId(), ResponseMasterNew.STATUS_INPROGRESS);
				}
			}
		
			
			//notify workstation location
			notifyWorkstationFormActivated(userContext, unitFormQuery);
		}
	}
*/

	private  void notifyWorkstationFormActivated(UserContext userContext, UnitFormQuery unitFormQuery) throws Exception
	{
		UnitLocation currentRec = workstationService.getUnitWorkstation(unitFormQuery.getUnitPk(),
				new ProjectOID(unitFormQuery.getProjectPk(), null), new WorkstationOID(unitFormQuery.getWorkstationPk(), null));
		if(currentRec != null && UnitLocation.STATUS_IN_PROGRESS.equals(currentRec.getStatus()))
		{
			//it is already in the inprogress status so all good.
			return;
		}
		
	    //create the new unit location on any change, so we have a history of all activities.
		UnitLocation nuLoc = new UnitLocation();
		nuLoc.setProjectPk(unitFormQuery.getProjectPk());
	    nuLoc.setMovedInBy((int) userContext.getUser().getPk());
	    nuLoc.setMoveInDate(new Date());
	    nuLoc.setUnitPk(unitFormQuery.getUnitPk());
	    nuLoc.setWorkstationPk(unitFormQuery.getWorkstationPk());
	    nuLoc.setStatus(UnitLocation.STATUS_IN_PROGRESS);
	    nuLoc.setCurrent(1);

    	//copy any values from the old rec to the new one. and mark it as not current
	    if(currentRec != null)
		{
	    	currentRec.setCurrent(0);
	    	persistWrapper.update(currentRec);
	    	
	    	nuLoc.setFirstFormAccessDate(currentRec.getFirstFormAccessDate());
	    	nuLoc.setFirstFormLockDate(currentRec.getFirstFormLockDate());
	    	nuLoc.setFirstFormSaveDate(currentRec.getFirstFormSaveDate());
	    	nuLoc.setLastFormAccessDate(currentRec.getLastFormAccessDate());
	    	nuLoc.setLastFormLockDate(currentRec.getLastFormLockDate());
	    	nuLoc.setLastFormUnlockDate(currentRec.getLastFormUnlockDate());
	    	nuLoc.setLastFormSaveDate(currentRec.getLastFormSaveDate());
	    	nuLoc.setCompletedDate(currentRec.getCompletedDate());
		}
	    persistWrapper.createEntity(nuLoc);
	}


	public static TestProcObj getTestProc(int testProcPk) throws Exception 
	{
		return new TestProcDAO().getTestProc(testProcPk);
	}
	
	/*public  UnitFormQuery getTestProcQuery(int testProcPk)
	{
		try 
		{
			return persistWrapper.read(UnitFormQuery.class, UnitFormQuery.sql + " and ut.pk=?", testProcPk);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}*/
	

	public static List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk)throws Exception
	{
		TestProcFilter filter = new TestProcFilter();
		filter.setUnitOID(new UnitOID(entityPk));
		return new TestProcListReport(context, filter).getTestProcs();
	}

	public static List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk, ProjectOID projectOID, boolean includeChildren)throws Exception
	{
		return getTestProcsForItemImpl(context, entityPk, projectOID, null, includeChildren);
	}

	public static List<UnitFormQuery> getTestProcsForItem(UserContext context, int entityPk, ProjectOID projectOID, WorkstationOID workstationOID, boolean includeChildren)throws Exception
	{
		return getTestProcsForItemImpl(context, entityPk, projectOID, workstationOID, includeChildren);
	}

	private static List<UnitFormQuery> getTestProcsForItemImpl(UserContext context, int entityPk, ProjectOID projectOID, WorkstationOID workstationOID, boolean includeChildren)throws Exception
	{
		TestProcFilter filter = new TestProcFilter(projectOID);
		filter.setUnitOID(new UnitOID(entityPk));
		filter.setWorkstationOID(workstationOID);
		filter.setIncludeChildren(includeChildren);
		return new TestProcListReport(context, filter).getTestProcs();
	}
	
/*	public static TestProcFormAssign getCurrentTestProcFormEntity(TestProcOID testProcOID)
	{
		return new TestProcDAO().getCurrentTestProcFormEntity(testProcOID);
	}
	
	public static List<TestProcFormAssignBean> getTestProcFormUpgradeHistory(TestProcOID testProcOID) throws Exception
	{
		return new TestProcDAO().getTestProcFormUpgradeHistory(testProcOID);
	}

	public static TestProcSectionObj getTestProcSection(TestProcOID testProcOID, FormSectionOID formSectionOID)
	{
		return new TestProcSectionDAO().getTestProcSection(testProcOID, formSectionOID);
	}

	public static TestProcSectionObj getTestProcSection(TestProcSectionOID testProcSectionOID)
	{
		return new TestProcSectionDAO().getTestProcSection(testProcSectionOID);
	}


	public static TestProcFormAssign getTestProcFormEntity(TestProcOID testProcOID, FormOID formOID)
	{
		return new TestProcDAO().getTestProcFormEntity(testProcOID, formOID);
	}


	public TestProcObj getTestProc(TestProcSectionOID testProcSectionOID) throws Exception
	{
		TestProcSectionObj sectionObj = getTestProcSection(testProcSectionOID);
		TestProcFormAssign tpFormAssign = persistWrapper.readByPrimaryKey(TestProcFormAssign.class, sectionObj.getTestProcFormAssignFk());
		return getTestProc(tpFormAssign.getTestProcFk());
	}*/
	
}