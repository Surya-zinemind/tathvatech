/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.survey.response.ResponseMasterNew;
import com.tathvatech.ts.core.workflow.FormWorkflow;
import com.tathvatech.ts.core.workflow.FormWorkflowQuery;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkflowManager
{
    private static final Logger logger = Logger.getLogger(WorkflowManager.class);

    public static FormWorkflow getLastEntry(TestProcOID testProcOID)throws Exception
    {
		return PersistWrapper.read(FormWorkflow.class, "select * from TAB_FORM_WORKFLOW where testProcPk=? order by pk desc limit 0,1",
				testProcOID.getPk());
    }
    
	public static void addWorkflowEntry(String action, int performedByUserPk, int responseId, int testProcPk, String resultStatus, String comments) throws Exception
	{
		FormWorkflow lastEntry = PersistWrapper.read(FormWorkflow.class, "select * from TAB_FORM_WORKFLOW where testProcPk = ? and current = 1", testProcPk);
		
		if(ResponseMasterNew.STATUS_INPROGRESS.equals(resultStatus))
		{
			// find if the previous workflow entry is also in the same resultStatus.. which means it is not moving to another step
			//so copy the comment from the previous entry if a comment is not provided.
			if(comments == null || comments.trim().length() == 0)
			{
				if(lastEntry != null && ResponseMasterNew.STATUS_INPROGRESS.equals(lastEntry.getResultStatus()))
				{
					comments = lastEntry.getComments();
				}
			}			
		}

		if(lastEntry != null)
		{
			lastEntry.setCurrent(0);
			PersistWrapper.update(lastEntry);
		}
		
		FormWorkflow nwf = new FormWorkflow();
		nwf.setCurrent(1);
		nwf.setAction(action);
		nwf.setPerformedBy(performedByUserPk);
		nwf.setResponseId(responseId);
		nwf.setTestProcPk(testProcPk);
		nwf.setResultStatus(resultStatus);
		if(comments != null && comments.trim().length() > 0)
			nwf.setComments(comments);
		
		nwf.setDate(new Date());
		
		PersistWrapper.createEntity(nwf);
	}

	public static List<FormWorkflowQuery> getWorkflowsForTestProc(TestProcOID testProcOid) throws Exception 
	{
		return PersistWrapper.readList(FormWorkflowQuery.class, 
			    FormWorkflowQuery.sql + " and fwf.testProcPk=? and fwf.action != ?", 
			    testProcOid.getPk(), FormWorkflow.ACTION_START);
	}
}