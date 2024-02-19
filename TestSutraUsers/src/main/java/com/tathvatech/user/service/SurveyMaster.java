/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarvasutra.etest.pdfprint.templatelocationconfig.ItemPrintAreaDef;
import com.sarvasutra.etest.pdfprint.templatelocationconfig.PdfTemplatePrintLocationConfig;
import com.tathvatech.testsutra.timetrack.common.WorkorderRequestBean;
import com.tathvatech.testsutra.timetrack.service.TimeEntryManager;
import com.tathvatech.testsutra.timetrack.service.WorkorderManager;
import com.tathvatech.ts.caf.ApplicationProperties;
import com.tathvatech.ts.caf.core.SequenceIdGenerator;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.Account;
import com.tathvatech.ts.core.accounts.AccountManager;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.accounts.UserQuery;
import com.tathvatech.ts.core.accounts.UserRepository;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.WorkItem;
import com.tathvatech.ts.core.common.utils.AsyncProcessor;
import com.tathvatech.ts.core.common.utils.EmailMessageInfo;
import com.tathvatech.ts.core.project.FormMainOID;
import com.tathvatech.ts.core.project.FormOID;
import com.tathvatech.ts.core.project.FormSectionOID;
import com.tathvatech.ts.core.project.ProjectForm;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.project.TestProcObj;
import com.tathvatech.ts.core.project.TestProcSectionOID;
import com.tathvatech.ts.core.project.TestProcSectionObj;
import com.tathvatech.ts.core.survey.FormPrintFormat;
import com.tathvatech.ts.core.survey.ObjectLock;
import com.tathvatech.ts.core.survey.ObjectLockQuery;
import com.tathvatech.ts.core.survey.Survey;
import com.tathvatech.ts.core.survey.response.FormResponseMaster;
import com.tathvatech.ts.core.survey.response.FormResponseOID;
import com.tathvatech.ts.core.survey.response.ResponseMasterNew;
import com.tathvatech.ts.report.ReportTypes;
import com.tathvatech.ts.tasks.TasksDelegate;
import com.thirdi.surveyside.project.FormFilter;
import com.thirdi.surveyside.project.Project;
import com.thirdi.surveyside.project.ProjectManager;
import com.thirdi.surveyside.project.TestProcManager;
import com.thirdi.surveyside.project.TestProcSectionDAO;
import com.thirdi.surveyside.project.UnitBookmark;
import com.thirdi.surveyside.project.UnitManager;
import com.thirdi.surveyside.reportv2.ReportRequest;
import com.thirdi.surveyside.reportv2.ReportResponse;
import com.thirdi.surveyside.survey.response.SurveyResponseManager;
import com.thirdi.surveyside.wizard.xml.SurveyDefFactory;

/**
 * @author Hari
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyMaster
{
	private static final Logger logger = Logger.getLogger(SurveyMaster.class);

	// TODO:: implement a cache with this hashmap
	// private static HashMap surveyMap = new HashMap();

	/**
	 * @param surveyPk
	 * @return
	 */
	public static String getSurveyDefFileName(int _surveyPk)
			throws Exception
	{
		Survey sur = getSurveyByPk(_surveyPk);
		if (sur != null)
		{
			return sur.getDefFileName();
		} else
		{
			return null;
		}
	}

	/**
	 * @param acc
	 * @param surveyName
	 * @return
	 * @throws Exception
	 */
	private static boolean isSurveyNameExist(String surveyName)
			throws Exception
	{
		List list = PersistWrapper.readList(
				FormMain.class,
						"select * from tab_forms_main where identityNumber =? ",
						surveyName);

		if (list.size() > 0)
		{
			return true;
		} 
		else
		{
			return false;
		}
	}

	/**
	 * Check if the name exists for a survey other than the one specified as the
	 * formMainOID argument
	 *
	 * @param acc
	 * @param surveyName
	 * @param formMainOID
	 * @return
	 * @throws Exception
	 */
	private static boolean isSurveyNameExistForAnotherSurvey(String surveyName, FormMainOID formMainOID) throws Exception
	{
		List list = PersistWrapper.readList(
						FormMain.class,
						"select * from tab_forms_main where identityNumber =? and pk != ?",
						surveyName, formMainOID.getPk());

		if (list.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @param surveyPk
	 * @return
	 */
	public static Survey getSurveyByPk(int surveyPk) throws Exception
	{
		Survey sur = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		return sur;
	}

	/**
	 * @param account
	 * @param _surveyName
	 * @param _fileName
	 * @param tableName
	 */
	public static Survey createSurvey(UserContext context, Survey survey) throws Exception
	{
		User user = (User)context.getUser();

		if (isSurveyNameExist(survey.getIdentityNumber()))
		{
			throw new AppException("Duplicate form name, Please choose a different form name.");
		}

        //survey def file name
		String nextSurveySeq = SequenceIdGenerator.getNext(SequenceIdGenerator.SURVEYFILE, false);
        String fileName = "sd_" + new Date().getTime() + "_" + nextSurveySeq + ".xml";

        //table name identification
        String tableName = "TAB_RESPONSE";
        
        FormMain formMain = new FormMain();
        formMain.setIdentityNumber(survey.getIdentityNumber());
        formMain.setResponsibleDivision(survey.getResponsibleDivision());
        formMain.setStatus(FormMain.STATUS_ACTIVE);

		survey.setCreatedBy(user.getPk());
        survey.setDbTable(tableName);
		survey.setDefFileName(fileName);
		survey.setCreatedDate(new Date());
		survey.setVersionNo(1);
		survey.setStatus(Survey.STATUS_CLOSED);

		
		SurveyDefFactory.createSurveyDefFile(survey);


        int surveyMainPk = PersistWrapper.createEntity(formMain);

        survey.setFormMainPk(surveyMainPk);
        int surveyPk = PersistWrapper.createEntity(survey);

		// fetch the new survey back
		survey = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		return survey;
	}

	/**
	 * @param account
	 * @param _surveyName
	 * @param _fileName
	 * @param tableName
	 */
	public static Survey createSurveyNewVersion(UserContext context, Survey newVersion, FormQuery baseRevision) throws Exception
	{
		Survey survey = SurveyMaster.getSurveyByPk(baseRevision.getPk());
		
		Account account = (Account)context.getAccount();
		User user = (User)context.getUser();

        //survey def file name
		String nextSurveySeq = SequenceIdGenerator.getNext(SequenceIdGenerator.SURVEYFILE, false);
        String fileName = "sd_" + new Date().getTime() + "_" + nextSurveySeq + ".xml";

        //table name identification
        String tableName = "TAB_RESPONSE";
        int maxVersionNo = PersistWrapper.read(Integer.class, "select max(versionNo) from TAB_SURVEY where formType = 1 and formMainPk=?", survey.getFormMainPk());
        
        newVersion.setFormType(survey.getFormType());
        newVersion.setAccountPk(survey.getAccountPk());
        newVersion.setCreatedBy(user.getPk());
        newVersion.setCreatedDate(new Date());
        newVersion.setDbTable(tableName);
        newVersion.setDefFileName(fileName);
        newVersion.setEffectiveDate(new Date());
        newVersion.setFormMainPk(survey.getFormMainPk());
        newVersion.setIdentityNumber(survey.getIdentityNumber());
        newVersion.setResponsibleDivision(survey.getResponsibleDivision());
        newVersion.setDescription(survey.getDescription());
        newVersion.setDescription1(survey.getDescription1());
        newVersion.setVersionNo(maxVersionNo+1);
        newVersion.setStatus(Survey.STATUS_CLOSED);
        
		SurveyDefFactory.createSurveyByCopy(newVersion, baseRevision.getPk());


        int newSurveyPk = PersistWrapper.createEntity(newVersion);

		// fetch the new survey back
		survey = PersistWrapper.readByPrimaryKey(Survey.class, newSurveyPk);
		return survey;
	}

	/**
	 * @param account
	 * @param _surveyName
	 * @param _fileName
	 * @param tableName
	 */
	public static Survey createSurveyByCopy(UserContext context, Survey survey, int sourceSurveyPk) throws Exception
	{
		User user = (User)context.getUser();

		if (isSurveyNameExist(survey.getIdentityNumber()))
		{
			throw new AppException("Duplicate form name, Please choose a different form name.");
		}

        //survey def file name
		String nextSurveySeq = SequenceIdGenerator.getNext(SequenceIdGenerator.SURVEYFILE, false);
        String fileName = "sd_" + new Date().getTime() + "_" + nextSurveySeq + ".xml";

        //table name identification
        String tableName = "TAB_RESPONSE";
        
        FormMain formMain = new FormMain();
        formMain.setIdentityNumber(survey.getIdentityNumber());
        formMain.setResponsibleDivision(survey.getResponsibleDivision());
        formMain.setStatus(FormMain.STATUS_ACTIVE);

		survey.setCreatedBy(user.getPk());
        survey.setDbTable(tableName);
		survey.setDefFileName(fileName);
		survey.setCreatedDate(new Date());
		survey.setVersionNo(1);
		survey.setStatus(Survey.STATUS_CLOSED);

		
		SurveyDefFactory.createSurveyByCopy(survey, sourceSurveyPk);


        int formMainPk = PersistWrapper.createEntity(formMain);

        survey.setFormMainPk(formMainPk);
        int surveyPk = PersistWrapper.createEntity(survey);

		// fetch the new survey back
		survey = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		return survey;
	}

	/**
	 * mark the survey status as deleted.// the survey list will not show these
	 * items . a complete removal should be done manually.
	 *
	 * @param surveyDef
	 */
	public static void deleteSurvey(int surveyPk) throws Exception
	{
		List errors = new ArrayList();
		
		Survey surveyConfig = SurveyMaster.getSurveyByPk(surveyPk);
		int masterPk = surveyConfig.getFormMainPk();

		if(!(Survey.STATUS_CLOSED.equals(surveyConfig.getStatus())))
		{
			errors.add("A published form cannot be deleted.");
		}
//		long count = PersistWrapper.read(Long.class, "select count(*) from TAB_PROJECT_FORMS pf join TAB_SURVEY f on pf.formPk = f.pk and f.formType = 1 where f.formMainPk=? ", masterPk);
//		if( count > 0)
//		{
//			errors.add("Form is associated with Projects, Please remove the form from projects and try again. ");
//		}
//		count = PersistWrapper.read(Long.class, "select count(*) from testproc_form_assign tfa join TAB_SURVEY f on tfa.formFk = f.pk and tfa.current = 1 and f.formType = 1 "
//				+ " where formMainPk=? ", masterPk);
//		if( count > 0)
//		{
//			errors.add("Form is associated with Units, Please remove the form from units and try again. ");
//		}
//		
		if(errors.size() > 0)
			throw new AppException((String[])errors.toArray(new String[errors.size()]));
		
		surveyConfig.setStatus(Survey.STATUS_DELETED);
		PersistWrapper.update(surveyConfig);

		Integer formCountInSet = PersistWrapper.read(Integer.class, 
				"select count(*) from tab_survey where formMainPk = ? and status != ? ", surveyConfig.getFormMainPk(), Survey.STATUS_DELETED);
		if(formCountInSet == 0)
		{
			FormMain formMain = PersistWrapper.readByPrimaryKey(FormMain.class, surveyConfig.getFormMainPk());
			formMain.setIdentityNumber(surveyConfig.getIdentityNumber() + "-del-" + formMain.getPk());
			PersistWrapper.update(formMain);
		}

	}

	public static Survey updateSurvey(Survey survey) throws Exception
	{
		boolean nameCheck = isSurveyNameExistForAnotherSurvey(survey.getIdentityNumber(), new FormMainOID(survey.getFormMainPk()));
		if(nameCheck)
			throw new AppException("Duplicate form name, Please choose a different form name.");

		PersistWrapper.update(survey);
		
		// fetch the new survey back
		survey = PersistWrapper.readByPrimaryKey(Survey.class, survey.getPk());
		return survey;
	}

	public static FormQuery getFormByPk(int formPk)
	{
		FormFilter filter = new FormFilter();
		filter.setFormPk(formPk);
		filter.setShowAllFormRevisions(true);
		filter.setStatus(new String[]{Survey.STATUS_CLOSED, Survey.STATUS_DELETED, Survey.STATUS_OPEN});
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFetchRowCount(false);
		req.setFilter(filter);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null && list.size() > 0)
			return list.get(0);
		
		return null;
	}

	public static List<FormQuery> getSurveyList() throws Exception
	{
		FormFilter filter = new FormFilter();
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFetchRowCount(false);
		req.setFilter(filter);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null)
			return list;
		
		return new ArrayList();
	}

	public static List<FormQuery> getSurveyList(FormFilter filter)
	{
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFetchRowCount(false);
		req.setFilter(filter);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null)
			return list;
		
		return new ArrayList();
	}

	public static List<FormQuery> getOpenSurveyList() throws Exception
	{
		FormFilter filter = new FormFilter();
		filter.setStatus(new String[]{Survey.STATUS_OPEN});
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFetchRowCount(false);
		req.setFilter(filter);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null)
			return list;
		
		return new ArrayList();
	}

	public static List<FormQuery> getAllVersionsForForm(int formMainPk)
	{
		FormFilter filter = new FormFilter();
		filter.setFormMainPk(formMainPk);
		filter.setShowAllFormRevisions(true);
		filter.setStatus(new String[]{Survey.STATUS_OPEN, Survey.STATUS_CLOSED, Survey.STATUS_DELETED});
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFilter(filter);
		req.setFetchRowCount(false);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null)
			return list;
		
		return new ArrayList();
	}

	public static FormQuery getLatestVersionForForm(int formMainPk) throws Exception
	{
		FormFilter filter = new FormFilter();
		filter.setFormMainPk(formMainPk);
		filter.setShowAllFormRevisions(false); // this will fetch the max version
		filter.setStatus(new String[]{Survey.STATUS_OPEN, Survey.STATUS_CLOSED, Survey.STATUS_DELETED});
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFilter(filter);
		req.setFetchRowCount(false);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null && list.size() > 0)
			return list.get(0);
		
		return null;
	}

	public static FormQuery getLatestPublishedVersionOfForm(int formPk) throws Exception
	{
		Survey s = PersistWrapper.readByPrimaryKey(Survey.class, formPk);
		return getLatestPublishedVersionForForm(s.getFormMainPk());
	}

	public static FormQuery getLatestPublishedVersionForForm(int formMainPk) throws Exception
	{
		FormFilter filter = new FormFilter();
		filter.setFormMainPk(formMainPk);
		filter.setShowAllFormRevisions(false); // this will fetch the max version
		filter.setStatus(new String[]{Survey.STATUS_OPEN});
		ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
		req.setFilter(filter);
		req.setFetchRowCount(false);
		req.setFetchAllRows(true);
		ReportResponse response = new FormListReport().runReport(req);
		List<FormQuery> list = (List<FormQuery>) response.getReportData();
		if(list != null && list.size() > 0)
			return list.get(0);
		
		return null;
	}

	public static void deleteSurveyVersion(UserContext context, int surveyVersionPk) throws Exception 
	{
		List errors = new ArrayList();
		
		Survey surveyConfig = SurveyMaster.getSurveyByPk(surveyVersionPk);
		if(!(Survey.STATUS_CLOSED.equals(surveyConfig.getStatus())))
		{
			throw new AppException("Only a form in draft status can be deleted.");
		}

		surveyConfig.setStatus(Survey.STATUS_DELETED);
		PersistWrapper.update(surveyConfig);

		//if there are no active versions of the form, delete the formMain
		Integer formCountInSet = PersistWrapper.read(Integer.class, 
				"select count(*) from tab_survey where formMainPk = ? and status != ? ", surveyConfig.getFormMainPk(), Survey.STATUS_DELETED);
		if(formCountInSet == 0)
		{
			FormMain formMain = PersistWrapper.readByPrimaryKey(FormMain.class, surveyConfig.getFormMainPk());
			formMain.setIdentityNumber(surveyConfig.getIdentityNumber() + "-del-" + formMain.getPk());
			formMain.setStatus(FormMain.STATUS_DELETED);
			PersistWrapper.update(formMain);
		}

		
		
//		String fileName = SurveyMaster.getSurveyDefFileName(surveyVersionPk);
//		String filePath = ApplicationProperties
//				.getProperty(ApplicationProperties.getFormDefRoot());
//		
//		File file = new File(filePath + fileName);
//		boolean success = file.delete();
//		
//		PersistWrapper.deleteEntity(Survey.class, surveyVersionPk);
	}

	public static FormMain getFormMain(FormQuery formQuery) throws Exception 
	{
		return PersistWrapper.read(FormMain.class, "select * from TAB_FORMS_MAIN where pk = (select formMainPk from TAB_SURVEY where pk=?)", formQuery.getPk());
	}

	public static void publishSurvey(UserContext context, int surveyPk, 
			List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap)throws Exception
	{
		Survey survey = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		if(!(Survey.STATUS_CLOSED.equals(survey.getStatus())))
			return;
		
		survey.setStatus(Survey.STATUS_OPEN);
		survey.setApprovedBy(context.getUser().getPk());
		Date now = new Date();
		survey.setApprovedDate(now);
		survey.setEffectiveDate(now);
		PersistWrapper.update(survey);
		survey = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		
		supersedPreviousFormVersions(survey);
		
		new FormDBManager().addSurveyToDB(context, survey);
		
		notifyFormPublish(context, surveyPk, projectUpgradeList, projectNotificationMap, formsUpgradeMap);
	}
	
	private static void notifyFormPublish(UserContext context, int surveyPk, 
			List<ProjectOID> projectUpgradeList, HashMap<ProjectOID, User> projectNotificationMap, HashMap<ProjectOID, List<Integer>> formsUpgradeMap) throws Exception
	{
		FormQuery formQuery = SurveyMaster.getFormByPk(surveyPk);
		
		
		StringBuffer sbText = new StringBuffer();
		StringBuffer sbHtml = new StringBuffer();
		
		sbText.append("Hi\n\n").append("Revision ").append(formQuery.getRevision()).append("(")
			.append(formQuery.getVersionNo()).append(") of form ").append(formQuery.getIdentityNumber()).append(" - ")
			.append(formQuery.getDescription()).append(" has been published and recommended for upgrade on project ");
		sbText.append("<project>").append("\n\n");
		sbText.append("The version comment for the new revision is ").append(formQuery.getVersionComment()).append("\n\n");
		sbText.append("Please login to TestSutra and approve the upgrade request. \n");
		sbText.append("Thank You\n").append("TestSutra Support\n");

		sbHtml.append("Hi<br/><br/>").append("Revision ").append(formQuery.getRevision()).append("(")
			.append(formQuery.getVersionNo()).append(") of form ").append(formQuery.getIdentityNumber()).append(" - ")
			.append(formQuery.getDescription()).append(" has been published and recommended for upgrade on project ");
		sbHtml.append("<project>").append("<br/><br/>");
		sbHtml.append("The version comment for the new revision is ").append(formQuery.getVersionComment()).append("<br/><br/>");
		sbHtml.append("Please login to TestSutra and approve the upgrade request. <br/>");
		sbHtml.append("Thank You<br/>").append("TestSutra Support<br/>");

		String subject = "New revision of form"  + formQuery.getIdentityNumber() + " - "  + formQuery.getDescription() +" published";
		
		//create a task for the approver of these projects
		for (Iterator iterator = projectNotificationMap.keySet().iterator(); iterator
				.hasNext();) 
		{
			ProjectOID projectOID = (ProjectOID) iterator.next();
			Project project = ProjectManager.getProject(projectOID.getPk());
			User manager = projectNotificationMap.get(projectOID);
			
			FormVersionUpgradeForProjectTaskBean taskBean = new FormVersionUpgradeForProjectTaskBean();
			taskBean.setProjectPk(projectOID.getPk());
			taskBean.setProjectName(project.getProjectName());
			taskBean.setFormPk(surveyPk);
			taskBean.setFormName(formQuery.getIdentityNumber());
			taskBean.setFormDescription(formQuery.getDescription());
			taskBean.setNewRevision(formQuery.getRevision());
			taskBean.setNewVersion(formQuery.getVersionNo());
			taskBean.setRevisionLog(formQuery.getVersionComment());
			if(projectUpgradeList.contains(projectOID))
				taskBean.setUpgradeProjectForm(true);
			taskBean.setUnitForms(formsUpgradeMap.get(projectOID));
			
			
			TasksDelegate.saveTask(context, projectOID, manager.getOID(), taskBean);
			
			String text = sbText.toString();
			String html = sbHtml.toString();
			text = text.replace("<project>", project.getProjectName());
			html = html.replace("<project>", project.getProjectName());
			EmailMessageInfo emailInfo = new EmailMessageInfo(ApplicationProperties.getEmailFromAddress(), null,
				new String[]{manager.getEmail()}, subject, text, html, null);
			AsyncProcessor.scheduleEmail(emailInfo);
		}
		
	}

	public static void applyFormUpgradePublish(UserContext context, int surveyPk, List projectListToUpgrade, List unitFormsListToUpgrade)throws Exception
	{
		Survey survey = PersistWrapper.readByPrimaryKey(Survey.class, surveyPk);
		if(!(Survey.STATUS_OPEN.equals(survey.getStatus())))
			throw new AppException("Form is still not published, it cannot be assigned to projects");
	
		//upgrade selected projects to this version 
		if(projectListToUpgrade != null)
		{
			for (Iterator iterator = projectListToUpgrade.iterator(); iterator.hasNext();)
			{
				Integer aProjectPk = (Integer) iterator.next();
				List<ProjectForm> projectForms = PersistWrapper.readList(ProjectForm.class, 
						"select * from TAB_PROJECT_FORMS where projectPk = ? and formPk in (select pk from TAB_SURVEY where formMainPk=?)", 
						aProjectPk, survey.getFormMainPk());
				for (Iterator iterator2 = projectForms.iterator(); iterator2.hasNext();)
				{
					ProjectForm projectForm = (ProjectForm) iterator2.next();
					
					Survey current = getSurveyByPk(projectForm.getFormPk());
					if(current.getVersionNo() < survey.getPk())
					{
						// do the upgrade only if the formversion is lower
						projectForm.setFormPk(survey.getPk());
						projectForm.setAppliedByUserFk(context.getUser().getPk());
						PersistWrapper.update(projectForm);
					}
				}
			}
		}
		
		//upgrade selected units to this version
		if(unitFormsListToUpgrade != null)
		{
			for (Iterator iterator = unitFormsListToUpgrade.iterator(); iterator.hasNext();)
			{
				Integer aUnitFormPk = (Integer) iterator.next();
				TestProcObj uForm = TestProcManager.getTestProc(aUnitFormPk);
				
				Survey current = getSurveyByPk(uForm.getFormPk());
				if(current.getVersionNo() < survey.getPk())
				{
					ProjectManager.upgradeFormForUnit(context, uForm.getOID(), survey.getPk());
				}				
			}
		}
	}
	
	private static void supersedPreviousFormVersions(Survey survey) throws Exception
	{
		List<Survey> previousVersions = PersistWrapper.readList(Survey.class, 
				"select * from TAB_SURVEY where formMainPk=? and versionNo < ?", survey.getFormMainPk(), survey.getVersionNo());
		for (Iterator iterator = previousVersions.iterator(); iterator.hasNext();)
		{
			Survey aSurvey = (Survey) iterator.next();
			if(!(aSurvey.getStatus().equals(Survey.STATUS_DELETED)))
			{
				aSurvey.setSuperseded(1);
				PersistWrapper.update(aSurvey);
			}
		}
	}

	public static UserQuery getAttributionUser(WorkItem workItem)throws Exception
	{
		if(EntityTypeEnum.TestProcSection == workItem.getEntityType())
		{
			TestProcSectionObj testProcSectionObj = new TestProcManager().getTestProcSection(new TestProcSectionOID(workItem.getPk()));
			TestProcObj testProc = new TestProcManager().getTestProc(new TestProcSectionOID(workItem.getPk()));
			ResponseMasterNew resp = SurveyResponseManager.getLatestResponseMasterForTest(testProc.getOID());

			ObjectLock ol = getObjectLock(resp.getResponseId(), (int) testProcSectionObj.getFormSectionFk());
			if(ol != null && ol.getAttributionUserFk() != null)
			{
				return UserRepository.getInstance().getUser(ol.getAttributionUserFk());
			}
		}
		return null;
	}

	public static void setAttribution(UserContext context, WorkItem workItem, UserOID attributeToUserOID)throws Exception
	{
		if(EntityTypeEnum.TestProcSection == workItem.getEntityType())
		{
			TestProcSectionObj testProcSectionObj = new TestProcManager().getTestProcSection(new TestProcSectionOID(workItem.getPk()));
			TestProcObj testProc = new TestProcManager().getTestProc(new TestProcSectionOID(workItem.getPk()));
			ResponseMasterNew resp = SurveyResponseManager.getLatestResponseMasterForTest(testProc.getOID());

			ObjectLock ol = getObjectLock(resp.getResponseId(), (int) testProcSectionObj.getFormSectionFk());
			if(attributeToUserOID != null)
				ol.setAttributionUserFk(attributeToUserOID.getPk());
			else
				ol.setAttributionUserFk(ol.getUserPk());
			PersistWrapper.update(ol);
		}
	}

	public static void resetAttribution(UserContext context, WorkItem workItem)throws Exception
	{
		if(EntityTypeEnum.TestProcSection == workItem.getEntityType())
		{
			TestProcSectionObj testProcSectionObj = new TestProcManager().getTestProcSection(new TestProcSectionOID(workItem.getPk()));
			TestProcObj testProc = new TestProcManager().getTestProc(new TestProcSectionOID(workItem.getPk()));
			ResponseMasterNew resp = SurveyResponseManager.getLatestResponseMasterForTest(testProc.getOID());

			ObjectLock ol = getObjectLock(resp.getResponseId(), (int) testProcSectionObj.getFormSectionFk());
			ol.setAttributionUserFk(ol.getUserPk());
			PersistWrapper.update(ol);
		}
	}

	public static ObjectLock getObjectLock(int responseFk, int formSectionFk)
	{
		return PersistWrapper.read(ObjectLock.class, "select * from tab_sectionlock where responseFk = ? and formSectionFk = ? ", 
				responseFk, formSectionFk);
	}

	public synchronized static ObjectLock lockSectionToEdit(UserContext context, User lockForUser, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception
	{
		FormResponseMaster respMaster = PersistWrapper.readByResponseId(FormResponseMaster.class, responseOID.getPk());
		FormSection formSection = new FormDBManager().getFormSection(sectionId, respMaster.getSurveyPk());
		
		ObjectLock objectLock = PersistWrapper.read(ObjectLock.class, "select * from tab_sectionlock where " +
				"responseFk=? and sectionId=?", 
				responseOID.getPk(), sectionId);
		
		if(objectLock != null)
		{
			User lockedBy = PersistWrapper.readByPrimaryKey(User.class, objectLock.getUserPk());
			
			objectLock.setLockedByUser(lockedBy);
			
			if(objectLock.getUserPk() == lockForUser.getPk())
			{ // same user trying to lock again.. so it is like a success only
				return objectLock;
			}
			else
			{
				throw new LockedByAnotherUserException(objectLock);
			}
		}
		else
		{
			ObjectLock lock = new ObjectLock();
			lock.setResponseFk(responseOID.getPk());
			lock.setFormSectionFk(formSection.getPk());
			lock.setSectionId(sectionId);
			lock.setUserPk(lockForUser.getPk());
			lock.setAttributionUserFk(lockForUser.getPk());
			lock.setLockDate(new Date());
			int newPk = PersistWrapper.createEntity(lock);

			ObjectLock newLock = PersistWrapper.readByPrimaryKey(ObjectLock.class, newPk);
			newLock.setLockedByUser(lockForUser);
			
			
			//create the workorder for this section if not already created
			ResponseMasterNew resp = SurveyResponseManager.getResponseMaster(responseOID.getPk());
			TestProcSectionObj tsObj = new TestProcSectionDAO().getTestProcSection(new TestProcOID(resp.getTestProcPk()), new FormSectionOID(newLock.getFormSectionFk()));
			WorkorderRequestBean woBean = new WorkorderRequestBean(tsObj.getOID());
			WorkorderManager.createWorkorder(context, woBean);
			
			
			
			//add the unit to favourites
			TestProcObj testProc = TestProcManager.getTestProc(resp.getTestProcPk());
			UnitManager.addUnitBookMark(context, testProc.getUnitPk(), testProc.getProjectPk(), UnitBookmark.BookmarkModeEnum.Auto);
			
			
			return newLock;
		}
	}
	
	
	public static ObjectLockQuery getCurrentLock(FormResponseOID responseOID, String sectionId)throws Exception
	{
		List<ObjectLockQuery> oList = PersistWrapper.readList(ObjectLockQuery.class,  ObjectLockQuery.sql + 
				" and responseFk=? and sectionId=? order by pk desc", 
				responseOID.getPk(), sectionId);
		if(oList == null || oList.size() == 0)
		{
			return null;
		}
		else if(oList.size() == 1)
			return oList.get(0);
		else
		{
			logger.error("Error:: multiple lock records for responseOID:"+ responseOID.getPk() + ", sectionId:"+ sectionId);
			return oList.get(0);
		}
	}

	public static List<String> getLockedSectionIds(User user, FormResponseOID responseOID)throws Exception
	{
		List l = PersistWrapper.readList(ObjectLock.class, ObjectLockQuery.sql + 
				" and userPk = ? and responseFk=?", 
				user.getPk(), responseOID.getPk());

		List returnList = new ArrayList<String>();
		for (Iterator iterator = l.iterator(); iterator.hasNext();)
		{
			ObjectLock object = (ObjectLock) iterator.next();
			returnList.add(object.getSectionId());
		}
		return returnList;
	}

	public static List<ObjectLock> getLockedSectionIds(FormResponseOID responseOID)throws Exception
	{
		List<ObjectLock> l = PersistWrapper.readList(ObjectLock.class, "select * from tab_sectionlock where responseFk=? ", 
				responseOID.getPk());
		
		for (Iterator iterator = l.iterator(); iterator.hasNext();)
		{
			ObjectLock objectLock = (ObjectLock) iterator.next();
			User lockedBy = AccountManager.getUser(objectLock.getUserPk());
			objectLock.setLockedByUser(lockedBy);
		}
		
		return l;
	}

	public static boolean isSectionLocked(User user, FormResponseOID responseOID, String sectionId)throws Exception
	{
		ObjectLock objectLock = PersistWrapper.read(ObjectLock.class, ObjectLockQuery.sql +
				" and userPk = ? and responseFk=? and sectionId=?", 
				user.getPk(), responseOID.getPk(), sectionId);
		if(objectLock == null)
			return false;
		else
			return true;
	}

	public static void releaseSectionEditLock(UserContext context, User user, FormResponseOID responseOID, String sectionId)throws LockedByAnotherUserException, Exception
	{
		ObjectLock objectLock = PersistWrapper.read(ObjectLock.class, ObjectLockQuery.sql +
				" and responseFk=? and sectionId=?", 
				responseOID.getPk(), sectionId);
		if(objectLock == null)
		{
			
		}
		else if(objectLock.getUserPk() == user.getPk())
		{
			ResponseMasterNew resp = SurveyResponseManager.getResponseMaster(objectLock.getResponseFk());
			TestProcSectionObj tsObj = new TestProcSectionDAO().getTestProcSection(new TestProcOID(resp.getTestProcPk()), new FormSectionOID(objectLock.getFormSectionFk()));
			TimeEntryManager.checkOutAllFromWorkOrder(context, tsObj.getOID());

			PersistWrapper.deleteEntity(ObjectLock.class, objectLock.getPk());
		}
		else
		{
			User lockedBy = PersistWrapper.readByPrimaryKey(User.class, objectLock.getUserPk());
			objectLock.setLockedByUser(lockedBy);
			throw new LockedByAnotherUserException(objectLock);
		}
	}

	public static void releaseSectionEditLock(UserContext context, FormResponseOID responseOID, String sectionId)throws Exception
	{
		ObjectLock objectLock = PersistWrapper.read(ObjectLock.class, ObjectLockQuery.sql +
				" and responseFk=? and sectionId=?", 
				responseOID.getPk(), sectionId);
		if(objectLock !=null)
		{
			ResponseMasterNew resp = SurveyResponseManager.getResponseMaster(objectLock.getResponseFk());
			TestProcSectionObj tsObj = new TestProcSectionDAO().getTestProcSection(new TestProcOID(resp.getTestProcPk()), new FormSectionOID(objectLock.getFormSectionFk()));
			TimeEntryManager.checkOutAllFromWorkOrder(context, tsObj.getOID());

			PersistWrapper.deleteEntity(ObjectLock.class, objectLock.getPk());
		}
	}

	public static FormPrintFormat getFormPrintFormat(FormOID formOID)
	{
		return PersistWrapper.read(FormPrintFormat.class, 
				"select * from form_print_format where formFk = ? ", formOID.getPk());
	}

	public static PdfTemplatePrintLocationConfig getFormPrintTemplateLocationConfig(FormOID formOID) throws Exception
	{
		FormPrintFormat pf = getFormPrintFormat(formOID);
		if(pf != null && pf.getPrintAreaDef() != null)
		{
			ObjectMapper jm = new ObjectMapper();
			
			PdfTemplatePrintLocationConfig config = jm.readValue(pf.getPrintAreaDef(), PdfTemplatePrintLocationConfig.class);
			
			PdfTemplatePrintLocationConfig newConfig = new PdfTemplatePrintLocationConfig();
			newConfig.setPageDefs(config.getPageDefs());
			for (Iterator iterator = config.getItemPrintAreaList().iterator(); iterator.hasNext();)
			{
				ItemPrintAreaDef aDef = (ItemPrintAreaDef) iterator.next();
				String identifier = aDef.getIdentifier();
				if(aDef.isIdentifierEncoded())
				{
					//this was encoded when saving. so decode it back.
					ItemPrintAreaDef newDef = new ItemPrintAreaDef();
					newDef.setIdentifier(new String(Base64.decodeBase64(identifier)));
					newDef.setIdentifierEncoded(false);
					newDef.setPrintAreas(aDef.getPrintAreas());
					newConfig.addItemPrintArea(newDef);
				}
				else
				{
					//not encoded so copy as is
					newConfig.addItemPrintArea(aDef);
				}
			}
			
			
			return newConfig;
		}
		return null;
	}

	public static PdfTemplatePrintLocationConfig saveFormPrintTemplateLocationConfig(UserContext context, FormOID formOID, PdfTemplatePrintLocationConfig config) throws Exception
	{
		FormPrintFormat pf = getFormPrintFormat(formOID);
		if(pf == null)
		{
			pf = new FormPrintFormat();
			pf.setFormFk(formOID.getPk());
			pf.setPrintClassName("com.sarvasutra.etest.pdfprint.templatelocationconfig.PdfPrinterTemplateBased");
		}
		
		
		//encode the identifier strings if they contain special chars.
		PdfTemplatePrintLocationConfig newConfig = new PdfTemplatePrintLocationConfig();
		newConfig.setPageDefs(config.getPageDefs());
		for (Iterator iterator = config.getItemPrintAreaList().iterator(); iterator.hasNext();)
		{
			ItemPrintAreaDef aDef = (ItemPrintAreaDef) iterator.next();
			String identifier = aDef.getIdentifier();
			if(identifier.matches("\\A\\p{ASCII}*\\z"))
			{
				//only contains assci
				newConfig.addItemPrintArea(aDef);
			}
			else
			{
				//there are non-ascii chars. so endode the identifier.
				ItemPrintAreaDef newDef = new ItemPrintAreaDef();
				newDef.setIdentifier(Base64.encodeBase64String(identifier.getBytes()));
				newDef.setIdentifierEncoded(true);
				newDef.setPrintAreas(aDef.getPrintAreas());
				newConfig.addItemPrintArea(newDef);
			}
		}
		
		ObjectMapper m = new ObjectMapper();
		String s = m.writeValueAsString(newConfig);
		pf.setPrintAreaDef(s);

		if(pf.getPk() == 0)
			PersistWrapper.createEntity(pf);
		else
			PersistWrapper.update(pf);
		
		
		return getFormPrintTemplateLocationConfig(formOID);
	}

}
