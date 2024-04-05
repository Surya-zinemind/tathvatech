/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import java.util.Date;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.entity.FormWorkflow;
import com.tathvatech.forms.response.ResponseMasterNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tathvatech.user.OID.TestProcOID;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkflowManager
{
	private final PersistWrapper persistWrapper;
	private static final Logger logger = LoggerFactory.getLogger(WorkflowManager.class);

    public WorkflowManager(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

    public  FormWorkflow getLastEntry(TestProcOID testProcOID)throws Exception
    {
		return persistWrapper.read(FormWorkflow.class, "select * from TAB_FORM_WORKFLOW where testProcPk=? order by pk desc limit 0,1",
				testProcOID.getPk());
    }
    
	public  void addWorkflowEntry(String action, int performedByUserPk, int responseId, int testProcPk, String resultStatus, String comments) throws Exception
	{
		FormWorkflow lastEntry = persistWrapper.read(FormWorkflow.class, "select * from TAB_FORM_WORKFLOW where testProcPk = ? and current = 1", testProcPk);
		
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
			persistWrapper.update(lastEntry);
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
		
		persistWrapper.createEntity(nwf);
	}

	public  List<FormWorkflowQuery> getWorkflowsForTestProc(TestProcOID testProcOid) throws Exception
	{
		return persistWrapper.readList(FormWorkflowQuery.class,
			    FormWorkflowQuery.sql + " and fwf.testProcPk=? and fwf.action != ?", 
			    testProcOid.getPk(), FormWorkflow.ACTION_START);
	}
}