package com.tathvatech.forms.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.sarvasutra.etest.EntitySchedule;
import com.sarvasutra.etest.EntityScheduleDAO;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.project.FormOID;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.project.TestProcObj;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.UnitTestProc;
import com.tathvatech.ts.core.project.UnitTestProcH;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.tathvatech.ts.core.survey.Survey;
import com.thirdi.surveyside.survey.FormSection;
import com.thirdi.surveyside.survey.SurveyMaster;
import com.thirdi.surveyside.utils.DateUtils;

public class TestProcDAO
{
	Logger logger = Logger.getLogger(TestProcDAO.class);
	Date now;
	
	public TestProcDAO()
	{
		now = DateUtils.getNowDateForEffectiveDateFrom();
	}

	public TestProcObj getTestProc(int testProcPk)
	{
		return PersistWrapper.read(TestProcObj.class, fetchSql + " and ut.pk = ?", testProcPk);
	}

	public TestProcFormAssign getCurrentTestProcFormEntity(TestProcOID testProcOID)
	{
		TestProcFormAssign currentTestProcFormEntity = PersistWrapper.read(TestProcFormAssign.class, 
				"select * from testproc_form_assign where testProcFk = ? and current = 1", testProcOID.getPk());
		return currentTestProcFormEntity;
	}
	
	public TestProcFormAssign getTestProcFormEntity(TestProcOID testProcOID, FormOID formOID)
	{
		TestProcFormAssign currentTestProcFormEntity = PersistWrapper.read(TestProcFormAssign.class, 
				"select * from testproc_form_assign where testProcFk = ? and formFk = ? ", testProcOID.getPk(), formOID.getPk());
		return currentTestProcFormEntity;
	}
	
	public List<TestProcFormAssignBean> getTestProcFormUpgradeHistory(TestProcOID testProcOID) throws Exception
	{
		List<TestProcFormAssignBean> returnList = new ArrayList<TestProcFormAssignBean>();
		
		//get the current TestProcFormEntity
		TestProcFormAssign currentTestProcFormEntity = getCurrentTestProcFormEntity(testProcOID);
		if(currentTestProcFormEntity == null)
			return null;
		
		Survey currentForm = SurveyMaster.getSurveyByPk((int) currentTestProcFormEntity.getFormFk());

		List<TestProcFormAssign> allFormList = PersistWrapper.readList(TestProcFormAssign.class, 
				"select tfa.* from testproc_form_assign  tfa "
						+ " join tab_survey form on tfa.formFk = form.pk "
						+ " where tfa.testProcFk = ? "
						+ " and form.versionNo < ? "
						+ " order by form.versionNo desc", 
						testProcOID.getPk(), currentForm.getVersionNo());
		
		if(allFormList == null || allFormList.size() == 0)
			return null;
		
		for (Iterator iterator = allFormList.iterator(); iterator.hasNext();)
		{
			TestProcFormAssign unitTestProcFormEntity = (TestProcFormAssign) iterator.next();
			
			returnList.add(unitTestProcFormEntity.getBean());
		}
		return returnList;
	}
	
	public TestProcObj saveTestProc(UserContext context, TestProcObj obj) throws Exception
	{
		UnitTestProc unitForm = null;

		if (obj.getPk() <= 0)
		{
			unitForm = new UnitTestProc();
			unitForm.setCreatedBy(context.getUser().getPk());
			unitForm.setCreatedDate(now);
			unitForm.setEstatus(obj.getEstatus());
			int pk = PersistWrapper.createEntity(unitForm);
			unitForm.setPk(pk);
			
			TestProcFormAssign testProcFormAssign = new TestProcFormAssign();
			testProcFormAssign.setAppliedByUserFk(obj.getAppliedByUserFk());
			testProcFormAssign.setCreatedBy(context.getUser().getPk());
			testProcFormAssign.setCreatedDate(now);
			testProcFormAssign.setCurrent(1);
			testProcFormAssign.setFormFk(obj.getFormPk());
			testProcFormAssign.setTestProcFk(unitForm.getPk());
			int testProcFormAssignPk = PersistWrapper.createEntity(testProcFormAssign);
			testProcFormAssign = PersistWrapper.readByPrimaryKey(TestProcFormAssign.class, testProcFormAssignPk);
			

			UnitTestProcH uHNew = new UnitTestProcH();
			uHNew.setUnitTestProcFk(unitForm.getPk());
			uHNew.setName(obj.getName());
			uHNew.setProjectPk(obj.getProjectPk());
			uHNew.setUnitPk(obj.getUnitPk());
			uHNew.setWorkstationPk(obj.getWorkstationPk());
			uHNew.setProjectTestProcPk(obj.getProjectTestProcPk());
			uHNew.setCreatedBy(context.getUser().getPk());
			uHNew.setCreatedDate(now);
			uHNew.setEffectiveDateFrom(now);
			uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
			uHNew.setUpdatedBy(context.getUser().getPk());
			PersistWrapper.createEntity(uHNew);
			
			obj = getTestProc(pk);
			//when creating a unitForm we need to create the TestProcSection Entries
			createTestProcSections(context, obj, testProcFormAssign);
			
			return obj;
		} 
		else
		{
			unitForm = PersistWrapper.readByPrimaryKey(UnitTestProc.class, obj.getPk());
			TestProcFormAssign currentTestProcFormEntity = getCurrentTestProcFormEntity(unitForm.getOID());
			
			
			long currentFormPk = currentTestProcFormEntity.getFormFk();
			
			if(currentFormPk != obj.getFormPk())
			{
				currentTestProcFormEntity.setCurrent(0);
				PersistWrapper.update(currentTestProcFormEntity);
				
				TestProcFormAssign newTestProcFormEntity = new TestProcFormAssign();
				newTestProcFormEntity.setAppliedByUserFk(obj.getAppliedByUserFk());
				newTestProcFormEntity.setCreatedBy(context.getUser().getPk());
				newTestProcFormEntity.setCreatedDate(now);
				newTestProcFormEntity.setCurrent(1);
				newTestProcFormEntity.setFormFk(obj.getFormPk());
				newTestProcFormEntity.setTestProcFk(unitForm.getPk());
				int testProcFormAssignPk = PersistWrapper.createEntity(newTestProcFormEntity);
				newTestProcFormEntity = PersistWrapper.readByPrimaryKey(TestProcFormAssign.class, testProcFormAssignPk);

				
				//we need to create the TestProcSection Entries
				createTestProcSections(context, obj, newTestProcFormEntity);
				
				//now we need to map any scheduling information set on the old sections to the corresponding sections on the new form.
				copySchedulesToNewForm(context, currentTestProcFormEntity, newTestProcFormEntity);
			}

			UnitTestProcH uHCurrent = PersistWrapper.read(UnitTestProcH.class, 
					"select * from unit_testproc_h where unitTestProcFk = ? and now() between effectiveDateFrom and effectiveDateTo", obj.getPk());
			
			if(Objects.equals(uHCurrent.getName(), obj.getName())
					&&Objects.equals(uHCurrent.getProjectPk(), obj.getProjectPk())
					&&Objects.equals(uHCurrent.getUnitPk(), obj.getUnitPk())
					&&Objects.equals(uHCurrent.getWorkstationPk(), obj.getWorkstationPk())
					)
			{
				//all same so dont create a new entry. just ignore
				// the appliedByUser field on UnitTestProcH can be removed. its there in the TestProcFormAssign
			}
			else
			{
				if(uHCurrent.getEffectiveDateFrom().equals(now))
				{
					// we have to do an update of the h record here.. since it is from the same DAO context as the now date is the same.
					uHCurrent.setName(obj.getName());
					uHCurrent.setProjectPk(obj.getProjectPk());
					uHCurrent.setUnitPk(obj.getUnitPk());
					uHCurrent.setWorkstationPk(obj.getWorkstationPk());
					uHCurrent.setProjectTestProcPk(obj.getProjectTestProcPk());
					uHCurrent.setAppliedByUserFk(obj.getAppliedByUserFk());
					uHCurrent.setEffectiveDateFrom(now);
					uHCurrent.setEffectiveDateTo(DateUtils.getMaxDate());
					uHCurrent.setUpdatedBy(context.getUser().getPk());
					PersistWrapper.update(uHCurrent);
				}
				else
				{
					uHCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
					PersistWrapper.update(uHCurrent);
					

					UnitTestProcH uHNew = new UnitTestProcH();
					uHNew.setUnitTestProcFk(obj.getPk());
					uHNew.setName(obj.getName());
					uHNew.setProjectPk(obj.getProjectPk());
					uHNew.setUnitPk(obj.getUnitPk());
					uHNew.setWorkstationPk(obj.getWorkstationPk());
					uHNew.setProjectTestProcPk(obj.getProjectTestProcPk());
					uHNew.setAppliedByUserFk(obj.getAppliedByUserFk());
					uHNew.setCreatedBy(context.getUser().getPk());
					uHNew.setCreatedDate(now);
					uHNew.setEffectiveDateFrom(now);
					uHNew.setEffectiveDateTo(DateUtils.getMaxDate());
					uHNew.setUpdatedBy(context.getUser().getPk());
					PersistWrapper.createEntity(uHNew);
				}
			}
			
			obj = getTestProc(obj.getPk());

			return obj;
		}
	}

	/**
	 * copy the schedules from the existing testProc and testProcSections to the ones associated to the new form.
	 * we are not removing the schedules from the old testProc/Sections . that way a revert if needed does not need any changes here. 
	 * @param context
	 * @param currentTPForm
	 * @param newTPForm
	 * @throws Exception
	 */
	private void copySchedulesToNewForm(UserContext context, TestProcFormAssign currentTPForm, TestProcFormAssign newTPForm) throws Exception
	{
		// the formlevel schedule need not be copied as it is linked to testproc and not to the testprocFormEntity
		
		EntityScheduleDAO esDAO = new EntityScheduleDAO(context.getTimeZone());
		
		// put the form section of the old form in a map with sectionPk as the key
		HashMap<Long, FormSection> currentFormSectionMap = new HashMap<>();
		List<FormSection> currentFormSections = PersistWrapper.readList(FormSection.class, "select * from form_section where formPk = ?",  currentTPForm.getFormFk());
		for (Iterator iterator = currentFormSections.iterator(); iterator.hasNext();)
		{
			FormSection formSection = (FormSection) iterator.next();
			currentFormSectionMap.put((long) formSection.getPk(), formSection);
		}
		
		
		// put the form sections on the new form in a map with the sectionMainPk as the key
		HashMap<Long, FormSection> newFormSectionMap = new HashMap<>();
		List<FormSection> newFormSections = PersistWrapper.readList(FormSection.class, "select * from form_section where formPk = ?",  newTPForm.getFormFk());
		for (Iterator iterator = newFormSections.iterator(); iterator.hasNext();)
		{
			FormSection formSection = (FormSection) iterator.next();
			newFormSectionMap.put((long) formSection.getFormSectionMainPk(), formSection);
		}
		
		//get the  testProcSections for the new form and put them in a map with formSectionPk as the key
		HashMap<Long, TestProcFormSection> newTPFormSectionMap = new HashMap<>();
		List<TestProcFormSection> tpSectionsNew = PersistWrapper.readList(TestProcFormSection.class, 
				"select * from testproc_form_section where testProcFormAssignFk = ? ", newTPForm.getPk());
		for (Iterator iterator = tpSectionsNew.iterator(); iterator.hasNext();)
		{
			TestProcFormSection unitTestProcSectionNew = (TestProcFormSection) iterator.next();
			newTPFormSectionMap.put(unitTestProcSectionNew.getFormSectionFk(), unitTestProcSectionNew);
		}		
		
		//get the  testProcSections for the old form
		List<TestProcFormSection> tpSectionsOld = PersistWrapper.readList(TestProcFormSection.class, 
				"select * from testproc_form_section where testProcFormAssignFk = ? ", currentTPForm.getPk());
		for (Iterator iterator = tpSectionsOld.iterator(); iterator.hasNext();)
		{
			TestProcFormSection unitTestProcSectionOld = (TestProcFormSection) iterator.next();
			EntitySchedule secSchedule = esDAO.getEntitySchedule(unitTestProcSectionOld.getOID());
			if(secSchedule != null)
			{
				long currentSectionFk = unitTestProcSectionOld.getFormSectionFk();
				// get the sectionFk for the corresponding section in the new version of the form.
				FormSection currentFormSection = currentFormSectionMap.get(currentSectionFk);
				
				FormSection newFormSection = newFormSectionMap.get((long)currentFormSection.getFormSectionMainPk());
				if(newFormSection != null)
				{
					TestProcFormSection newTPSection = newTPFormSectionMap.get((long)newFormSection.getPk());
					

					EntitySchedule newSchedule = new EntitySchedule();
					newSchedule.setComment(secSchedule.getComment());
					newSchedule.setCreatedBy(secSchedule.getCreatedBy());
					newSchedule.setCreatedDate(secSchedule.getCreatedDate());
					newSchedule.setEffectiveDateFrom(secSchedule.getEffectiveDateFrom());
					newSchedule.setEffectiveDateTo(secSchedule.getEffectiveDateTo());
					newSchedule.setEstimateHours(secSchedule.getEstimateHours());
					newSchedule.setForecastEndDate(secSchedule.getForecastEndDate());
					newSchedule.setForecastStartDate(secSchedule.getForecastStartDate());
					newSchedule.setObjectPk(newTPSection.getPk());
					newSchedule.setObjectType(EntityTypeEnum.TestProcSection.getValue());
					PersistWrapper.createEntity(newSchedule);
				}
				
			}
		}
	}

	public void deleteTestProc(UserContext context, TestProcOID testProcOID) throws Exception
	{
		logger.info("deleteTestProc:: testprocOID:" + testProcOID.getLoggingString());

		TestProcFormAssign tpForm = getCurrentTestProcFormEntity(testProcOID);
		if(tpForm != null)
		{
			tpForm.setCurrent(0);
			PersistWrapper.update(tpForm);
		}
		
		UnitTestProcH uHCurrent = PersistWrapper.read(UnitTestProcH.class, 
				"select * from unit_testproc_h where unitTestProcFk = ? and now() between effectiveDateFrom and effectiveDateTo", testProcOID.getPk());

		if(uHCurrent != null)
		{
			uHCurrent.setUpdatedBy(context.getUser().getPk());
			uHCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
			PersistWrapper.update(uHCurrent);
		}
	}

	public void deleteAllTestProcsMatching(UserContext context, UnitOID unitOID, ProjectOID projectOID,
			WorkstationOID workstationOID) throws Exception
	{
		logger.info("deleteAllTestProcsMatching:: unitOID:" + unitOID.getLoggingString() + ", projectOID:" + projectOID.getLoggingString());

		//mark the testproc_form entries as current = 0;
		String sql1 = "update unit_testproc ut, testproc_form_assign tfa, unit_testproc_h uth "
				+ " set tfa.current = 0 where tfa.testprocFk = ut.pk and tfa.current = 1 "
				+ " and uth.unitTestProcFk = ut.pk and "
				+ " uth.unitPk=? and uth.projectPk = ? and uth.workstationPk=? and now() between uth.effectiveDateFrom and uth.effectiveDateTo ";
		PersistWrapper.executeUpdate(sql1, unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());
		
		// change the effectiveToDate in the h records
		List<UnitTestProcH> uHCurrentList = PersistWrapper.readList(UnitTestProcH.class, 
				"select * from unit_testproc_h where unitPk=? and projectPk = ? and workstationPk=? and now() between effectiveDateFrom and effectiveDateTo", 
				unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());

		for (Iterator iterator = uHCurrentList.iterator(); iterator.hasNext();)
		{
			UnitTestProcH uHCurrent = (UnitTestProcH) iterator.next();
			uHCurrent.setUpdatedBy(context.getUser().getPk());
			uHCurrent.setEffectiveDateTo(new Date(now.getTime() - 1000));
			PersistWrapper.update(uHCurrent);
		}
	}

	/**
	 * @param context
	 * @param testProcObj
	 * @throws Exception
	 */
	private void createTestProcSections(UserContext context, TestProcObj testProcObj, TestProcFormAssign newTestProcFormEntity) throws Exception
	{
		// we only need to create the testprocSection records for the new form in the testproc.

		//get the sections for the new form
		List<FormSection> formSections = PersistWrapper.readList(FormSection.class, "select * from form_section where formPk = ?",  testProcObj.getFormPk());
		for (Iterator iterator2 = formSections.iterator(); iterator2.hasNext();)
		{
			FormSection formSection = (FormSection) iterator2.next();
			TestProcFormSection tSec = new TestProcFormSection();
			tSec.setCreatedBy(context.getUser().getPk());
			tSec.setCreatedDate(now);
			tSec.setFormSectionFk(formSection.getPk());
			tSec.setTestProcFormAssignFk(newTestProcFormEntity.getPk());
			PersistWrapper.createEntity(tSec);
		}
	}
	
	private static String fetchSql = "select ut.pk as pk, uth.name as name, tfa.appliedByUserFk, uth.projectTestProcPk as projectTestProcPk, "
			+ " uth.projectPk as projectPk, uth.workstationPk as workstationPk, uth.unitPk as unitPk, "
			+ " tfa.formFk as formPk, uth.createdBy, uth.createdDate "
			+ " from unit_testproc ut"
			+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
			+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
			+ " where 1 = 1 ";

}
