package com.tathvatech.forms.processor;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.entity.TestProcFormAssign;
import com.tathvatech.forms.service.TestProcService;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.workstation.service.WorkstationService;
import com.tathvatech.common.exception.AppException;
import java.util.Iterator;
import java.util.List;
import com.tathvatech.forms.response.ResponseMasterNew;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class FormUpgradeRevertProcessor
{
	private final WorkstationService workstationService;
    private final SurveyResponseService surveyResponseService;
	private final PersistWrapper persistWrapper;
	private final TestProcService testProcService;
	private final SurveyMaster surveyMaster;
    public FormUpgradeRevertProcessor(@Lazy WorkstationService workstationService, SurveyResponseService surveyResponseService, PersistWrapper persistWrapper, TestProcService testProcService, SurveyMaster surveyMaster) {
        this.workstationService = workstationService;
        this.surveyResponseService = surveyResponseService;
        this.persistWrapper = persistWrapper;
        this.testProcService = testProcService;
        this.surveyMaster = surveyMaster;
    }

    // we should remove the new records created as part of the upgrade. As this is a mistake by the user. we dont have to keep it in history.
	public void process(TestProcOID testProcOID, FormOID currentFormOID, FormOID revertToFormOID) throws Exception
	{
		//make sure that the workstation is in Waiting status when reverting the form
		TestProcObj testProc = new TestProcDAO().getTestProc((int) testProcOID.getPk());
		UnitLocation unitLocation = workstationService.getUnitWorkstation(testProc.getUnitPk(), new ProjectOID(testProc.getProjectPk()), new WorkstationOID(testProc.getWorkstationPk()));
		if(unitLocation != null 
				&& (UnitLocation.STATUS_COMPLETED.equals(unitLocation.getStatus()) || UnitLocation.STATUS_IN_PROGRESS.equals(unitLocation.getStatus()) )
				)
		{
			throw new AppException("Workstation should be in Waiting status before a form can be reverted.");
		}
		
		ResponseMasterNew respMasterCurrent = surveyResponseService.getLatestResponseMasterForTest(testProcOID);
		if(respMasterCurrent != null && respMasterCurrent.getFormPk() != currentFormOID.getPk())
		{
			throw new AppException("Latest form revision on the test does not match the current revision, Form cannot be reverted");
		}
		
		
		ResponseMasterNew respMasterRevertTo = surveyResponseService.getResponseMasterForTestHistoryRecord(testProcOID, revertToFormOID);
		
		//get the testprocFormEntity record for the current form revision
		TestProcFormAssign tpFormEntityCurrent = testProcService.getCurrentTestProcFormEntity(testProcOID);
		
		if(tpFormEntityCurrent == null)
		{
			throw new AppException("Invalid form assigned to test, Form cannot be reverted");
		}
		if(tpFormEntityCurrent.getFormFk() != currentFormOID.getPk())
		{
			throw new AppException("Current form in request is not the current form assigned to the test, Form cannot be reverted");
		}
		Survey formCurrent = surveyMaster.getSurveyByPk((int) tpFormEntityCurrent.getFormFk());
		if(formCurrent == null)
		{
			throw new AppException("Invalid current form. Form cannot be reverted");
		}
		
		// now get the tpFormEntity record for the one to revert to
		TestProcFormAssign tpFormToRevertTo = testProcService.getTestProcFormEntity(testProcOID, revertToFormOID);
		if(tpFormToRevertTo == null)
		{
			throw new AppException("Invalid forms selected to revert, Revert cannot be performed");
		}
		if(tpFormToRevertTo.getFormFk() != revertToFormOID.getPk())
		{
			throw new AppException("Revert to form in request was not assigned to the test earlier, Form cannot be reverted");
		}
		Survey formToRevertTo = surveyMaster.getSurveyByPk((int) tpFormToRevertTo.getFormFk());
		if(formToRevertTo == null)
		{
			throw new AppException("Invalid form to revert to. Form cannot be reverted");
		}
		
		if(formCurrent.getVersionNo() < formToRevertTo.getVersionNo())
		{
			throw new AppException("Cannot revert to a higher version of the form, Revert cannot be performed");
		}
		
		processResponseRelatedEntries(testProcOID, tpFormEntityCurrent, respMasterRevertTo);

		revertSchedulingEntryUpdates();
		
		tpFormEntityCurrent.setCurrent(0);
		persistWrapper.update(tpFormEntityCurrent);
		
		tpFormToRevertTo.setCurrent(1);
		persistWrapper.update(tpFormToRevertTo);
		
	}
	
	private void revertSchedulingEntryUpdates()
	{
		//nothing to be done here because, when we upgrade a form, we are only copying the entries to the new form and not removing anything.
	}

	private void processResponseRelatedEntries(TestProcOID testProcOID, TestProcFormAssign tpFormEntityCurrent, ResponseMasterNew respMasterRevertTo) throws Exception
	{
		//get the responseIds corresponding to the current form for the testproc
		List<Integer> responseIdList = persistWrapper.readList(Integer.class, "select distinct responseId from tab_response where testprocPk = ? and surveyPk = ? ",
				testProcOID.getPk(), tpFormEntityCurrent.getFormFk());
		
		// remove the workflow enteries if any for the new responseIds on the current form.
		if(responseIdList != null && responseIdList.size() > 0)
		{
			StringBuilder sql7 = new StringBuilder("delete from TAB_FORM_WORKFLOW where testprocPk = ? and responseId in ( ");
			String sep = "";
			for (Iterator iterator = responseIdList.iterator(); iterator.hasNext();)
			{
				Integer integer = (Integer) iterator.next();
				sql7.append(sep).append(integer+"");
				sep = ", ";
			}
			sql7.append(")");
			
			persistWrapper.executeUpdate(sql7.toString(), testProcOID.getPk());
		}

		if(respMasterRevertTo != null)
		{
			//we need to set the last workflow entry for the previous responseId as the current one
			Integer wfpk = persistWrapper.read(Integer.class,
					"select pk from TAB_FORM_WORKFLOW where testprocPk = ? and responseId = ? order by pk desc limit 0, 1", 
					testProcOID.getPk(), respMasterRevertTo.getResponseId());
			
			String sql8 = "update TAB_FORM_WORKFLOW set current = 1 where pk = ? "; 
			persistWrapper.executeUpdate(sql8, wfpk);
		}
		
		//remove the responses for the currentForm and set the revert to one as current
		if(responseIdList != null && responseIdList.size() > 0)
		{
			//remove the current section response
			StringBuilder sql2_0 = new StringBuilder("delete from tab_section_response WHERE responseId in ( ");
			String sep = "";
			for (Iterator iterator = responseIdList.iterator(); iterator.hasNext();)
			{
				Integer integer = (Integer) iterator.next();
				sql2_0.append(sep).append(integer+"");
				sep = ", ";
			}
			sql2_0.append(")");
			
			persistWrapper.executeUpdate(sql2_0.toString());
			
			//now remove the response
			StringBuilder sql2 = new StringBuilder("delete from tab_response WHERE responseId in ( ");
			sep = "";
			for (Iterator iterator = responseIdList.iterator(); iterator.hasNext();)
			{
				Integer integer = (Integer) iterator.next();
				sql2.append(sep).append(integer+"");
				sep = ", ";
			}
			sql2.append(")");
			
			persistWrapper.executeUpdate(sql2.toString());
		}

		//mark the older one as current
		if(respMasterRevertTo != null)
		{
			String sql1 = "UPDATE tab_response SET current = 1 WHERE responseId = ? ";
			persistWrapper.executeUpdate(sql1, respMasterRevertTo.getResponseId());
		}
		
		
		
	}
}
