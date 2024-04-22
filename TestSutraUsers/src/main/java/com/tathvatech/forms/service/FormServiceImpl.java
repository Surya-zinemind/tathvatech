package com.tathvatech.forms.service;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.*;
import com.tathvatech.forms.dao.EntityScheduleDAO;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.entity.EntitySchedule;
import com.tathvatech.forms.oid.TestProcSectionOID;
import com.tathvatech.forms.report.TestProcListReport;
import com.tathvatech.forms.report.TestProcSectionListReport;
import com.tathvatech.forms.report.TestProcStatusSummaryReport;
import com.tathvatech.forms.request.TestProcStatusSummaryReportRequest;
import com.tathvatech.forms.request.TestProcStatusSummaryReportResult;
import com.tathvatech.forms.request.TestProcStatusSummaryReportResultRow;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveyForm;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.survey.service.SurveyDefFactory;
import com.tathvatech.survey.service.SurveyMasterService;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.common.UnitWorkstationListReportFilter;
import com.tathvatech.unit.common.UnitWorkstationListReportResultRow;
import com.tathvatech.unit.dao.UnitInProjectDAO;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.report.UnitWorkstationListReport;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.user.utils.DateUtils;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.oid.UnitWorkstationOID;
import com.tathvatech.workstation.service.WorkstationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service

public class FormServiceImpl implements  FormService{
    private  final Logger logger = Logger.getLogger(String.valueOf(FormServiceImpl.class));

    private final TestProcService testProcService;
    private final DummyWorkstation dummyWorkstation;
    private final PersistWrapper persistWrapper;
    private final SurveyMasterService surveyMasterService;
    private final WorkstationService workstationService;
    private final UnitManager unitManager;
    private final CommonServiceManager commonServiceManager;
    private final UnitInProjectDAO unitInProjectDAO;
    private final SurveyResponseService surveyResponseService;
    private final SurveyDefFactory surveyDefFactory;
    private UnitFormQuery unitFormQuery;

    public FormServiceImpl(TestProcService testProcService, DummyWorkstation dummyWorkstation, PersistWrapper persistWrapper, @Lazy SurveyMasterService surveyMasterService, WorkstationService workstationService, UnitManager unitManager, CommonServiceManager commonServiceManager, UnitInProjectDAO unitInProjectDAO, SurveyResponseService surveyResponseService, SurveyDefFactory surveyDefFactory) {
        this.testProcService = testProcService;
        this.dummyWorkstation = dummyWorkstation;
        this.persistWrapper = persistWrapper;
        this.surveyMasterService = surveyMasterService;
        this.workstationService = workstationService;
        this.unitManager = unitManager;
        this.commonServiceManager = commonServiceManager;
        this.unitInProjectDAO = unitInProjectDAO;
        this.surveyResponseService = surveyResponseService;
        this.surveyDefFactory = surveyDefFactory;
    }

    @Override
    public  void saveTestProcSchedule(UserContext context, TestProcOID testProcOID,
                                            ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception
    {
        Date now = DateUtils.getNowDateForEffectiveDateFrom();

        UnitFormQuery testProc = testProcService.getTestProcQuery((int) testProcOID.getPk());
        if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProc.getForecastStartDate())
                && Objects.equals(objectScheduleRequestBean.getEndForecast(), testProc.getForecastEndDate())
                && Objects.equals(objectScheduleRequestBean.getHoursEstimate(), testProc.getForecastHours()))
        {
            return;
        } else
        {
            EntitySchedule es = persistWrapper.read(EntitySchedule.class,
                    "select * from entity_schedule where objectPk = ? and objectType = ? and now() between effectiveDateFrom and effectiveDateTo",
                    objectScheduleRequestBean.getObjectOID().getPk(),
                    objectScheduleRequestBean.getObjectOID().getEntityType().getValue());

            if (es == null)
            {
                es = new EntitySchedule();
                es.setObjectPk((int) objectScheduleRequestBean.getObjectOID().getPk());
                es.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
                es.setCreatedBy((int) context.getUser().getPk());
                es.setCreatedDate(new Date());
                es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
                es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
                es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
                es.setEffectiveDateFrom(now);
                es.setEffectiveDateTo(DateUtils.getMaxDate());
                persistWrapper.createEntity(es);
            } else
            {
                Calendar calEx = new GregorianCalendar();
                calEx.setTime(es.getCreatedDate());
                calEx.setTimeZone(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));

                Calendar calNow = new GregorianCalendar();
                calNow.setTime(new Date());
                calNow.setTimeZone(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));

                // need to check if the last record is made on the same day.. if
                // so just update. else we create a history record.
                if (calEx.get(Calendar.DAY_OF_MONTH) == calNow.get(Calendar.DAY_OF_MONTH)
                        && calEx.get(Calendar.MONTH) == calNow.get(Calendar.MONTH)
                        && calEx.get(Calendar.YEAR) == calNow.get(Calendar.YEAR))
                {
                    // so update
                    es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
                    es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
                    es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
                    persistWrapper.update(es);
                } else
                {
                    // invalidate old and create new
                    es.setEffectiveDateTo(new Date(now.getTime() - 1000));
                    persistWrapper.update(es);

                    EntitySchedule esNew = new EntitySchedule();
                    esNew.setObjectPk((int) objectScheduleRequestBean.getObjectOID().getPk());
                    esNew.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
                    esNew.setCreatedBy((int) context.getUser().getPk());
                    esNew.setCreatedDate(new Date());
                    esNew.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
                    esNew.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
                    esNew.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
                    esNew.setEffectiveDateFrom(now);
                    esNew.setEffectiveDateTo(DateUtils.getMaxDate());
                    persistWrapper.createEntity(esNew);
                }
            }
        }

    }

    public  void saveTestProcSchedules(UserContext context, ProjectOID projectOID, UnitOID rootUnitOID,
                                             List<ObjectScheduleRequestBean> scheduleList) throws Exception
    {
        Date now = DateUtils.getNowDateForEffectiveDateFrom();

        UnitWorkstationListReportFilter req = new UnitWorkstationListReportFilter(projectOID);
        req.setProjectOID(projectOID);
        req.setUnitOID(rootUnitOID);
        req.setIncludeChildren(true);
        List<UnitWorkstationListReportResultRow> wsSummaryRows = new UnitWorkstationListReport(
                context, req, persistWrapper,  dummyWorkstation, unitManager).runReport();

        // create a hashmap of UnitWorkstationOID and the wsStatusSummary
        HashMap<UnitWorkstationOID, UnitWorkstationListReportResultRow> unitWorkstationMap = new HashMap<UnitWorkstationOID, UnitWorkstationListReportResultRow>();
        for (Iterator iterator = wsSummaryRows.iterator(); iterator.hasNext();)
        {
            UnitWorkstationListReportResultRow unitWorkstationRow = (UnitWorkstationListReportResultRow) iterator
                    .next();
            unitWorkstationMap.put(new UnitWorkstationOID(unitWorkstationRow.getPk()), unitWorkstationRow);
        }

        // load all the testprocs and create a hashmap of the TestProcOID and
        // TestProc
        TestProcFilter filter = new TestProcFilter(projectOID);
        filter.setUnitOID(rootUnitOID);
        filter.setIncludeChildren(true);
        filter.setFetchWorkstationForecastAsTestForecast(false);
        List<UnitFormQuery> list = new TestProcListReport(context, filter, persistWrapper,  unitManager, dummyWorkstation).getTestProcs();
        HashMap<TestProcOID, UnitFormQuery> testProcMap = new HashMap<TestProcOID, UnitFormQuery>();
        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();
            testProcMap.put(new TestProcOID(unitFormQuery.getPk()), unitFormQuery);
        }

        // load all the testprocs sections and create a hashmap of the
        // TestProcSectionOID and TestProcSection
        TestProcSectionListFilter secFilter = new TestProcSectionListFilter();
        secFilter.setProjectOID(projectOID);
        secFilter.setUnitOID(rootUnitOID);
        secFilter.setIncludeChildren(true);
        secFilter.setFetchWorkstationOrTestForecastAsSectionForecast(false);
        List<TestProcSectionListReportResultRow> secList = new TestProcSectionListReport(context, secFilter, persistWrapper,  dummyWorkstation, unitManager)
                .getTestProcs();
        HashMap<TestProcSectionOID, TestProcSectionListReportResultRow> testProcSectionMap = new HashMap<TestProcSectionOID, TestProcSectionListReportResultRow>();
        for (Iterator iterator = secList.iterator(); iterator.hasNext();)
        {
            TestProcSectionListReportResultRow aSection = (TestProcSectionListReportResultRow) iterator.next();
            testProcSectionMap.put(new TestProcSectionOID(aSection.getPk()), aSection);
        }

        // now loop through the scheduleList from ui. and find the matching
        // object from the hashmap and save as required.
        for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();)
        {
            ObjectScheduleRequestBean objectScheduleRequestBean = (ObjectScheduleRequestBean) iterator.next();
            if (objectScheduleRequestBean.getObjectOID() instanceof UnitWorkstationOID)
            {
                UnitWorkstationListReportResultRow unitWorkstationRow = unitWorkstationMap
                        .get(objectScheduleRequestBean.getObjectOID());
                if (Objects.equals(objectScheduleRequestBean.getStartForecast(),
                        unitWorkstationRow.getForecastStartDate())
                        && Objects.equals(objectScheduleRequestBean.getEndForecast(),
                        unitWorkstationRow.getForecastEndDate())
                        && Objects.equals(objectScheduleRequestBean.getHoursEstimate(),
                        unitWorkstationRow.getForecastHours()))
                {
                    continue;
                } else
                {
                    new EntityScheduleDAO(TimeZone.getTimeZone(unitWorkstationRow.getWorkstationTimezoneId()))
                            .save(context, objectScheduleRequestBean);
                }
            } else if (objectScheduleRequestBean.getObjectOID() instanceof TestProcOID)
            {
                UnitFormQuery testProc = testProcMap.get(objectScheduleRequestBean.getObjectOID());
                if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProc.getForecastStartDate())
                        && Objects.equals(objectScheduleRequestBean.getEndForecast(), testProc.getForecastEndDate())
                        && Objects.equals(objectScheduleRequestBean.getHoursEstimate(), testProc.getForecastHours()))
                {
                    continue;
                } else
                {
                    new EntityScheduleDAO(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId())).save(context,
                            objectScheduleRequestBean);
                }
            } else if (objectScheduleRequestBean.getObjectOID() instanceof TestProcSectionOID)
            {
                TestProcSectionListReportResultRow testProcSection = testProcSectionMap
                        .get(objectScheduleRequestBean.getObjectOID());
                if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProcSection.getForecastStartDate())
                        && Objects.equals(objectScheduleRequestBean.getEndForecast(),
                        testProcSection.getForecastEndDate())
                        && Objects.equals(objectScheduleRequestBean.getHoursEstimate(),
                        testProcSection.getForecastHours()))
                {
                    continue;
                } else
                {
                    new EntityScheduleDAO(TimeZone.getTimeZone(testProcSection.getWorkstationTimezoneId()))
                            .save(context, objectScheduleRequestBean);
                }
            }
        }
    }

    public  void moveTestProcsToUnit(UserContext userContext, List<TestProcOID> testProcsToMove,
                                           UnitOID unitOIDToMoveTo, ProjectOID projectOIDToMoveTo) throws Exception
    {
        List<WorkstationOID> workstations = new ArrayList<WorkstationOID>();

        UnitInProjectObj unitInProject = unitInProjectDAO.getUnitInProject(unitOIDToMoveTo, projectOIDToMoveTo);
        if (unitInProject == null)
            throw new AppException("Invalid target unit, or target unit is not part of the project.");

        // load the forms in the target unit. and put it into a hashmap for easy
        // lookup
        TestProcFilter listFilter = new TestProcFilter(projectOIDToMoveTo);
        listFilter.setUnitOID(unitOIDToMoveTo);
        listFilter.setIncludeChildren(false);
        List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter,persistWrapper,  unitManager, dummyWorkstation).getTestProcs();
        HashMap<String, UnitFormQuery> targetUnitTestMap = new HashMap<String, UnitFormQuery>();
        for (Iterator iterator = testList.iterator(); iterator.hasNext();)
        {
            UnitFormQuery aTest = (UnitFormQuery) iterator.next();
            targetUnitTestMap.put(aTest.getName() + "-" + aTest.getFormMainPk() + "-" + aTest.getWorkstationPk(),
                    aTest);
        }

        for (Iterator iterator = testProcsToMove.iterator(); iterator.hasNext();)
        {
            TestProcOID testProcOID = (TestProcOID) iterator.next();

            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());

            if (testProc.getUnitPk() == unitOIDToMoveTo.getPk())
                continue;

            // confirm that the target unit is also part of the same project
            if (testProc.getProjectPk() != projectOIDToMoveTo.getPk())
                throw new AppException("You can only move a form within the same project.");

            Survey form = surveyMasterService.getSurveyByPk(testProc.getFormPk());
            UnitFormQuery targetTest = targetUnitTestMap
                    .get(testProc.getName() + "-" + form.getFormMainPk() + "-" + testProc.getWorkstationPk());
            if (targetTest != null)
            {
                String testName = targetTest.getName();
                if (testName == null)
                    testName = "{None}";

                throw new AppException(String.format(
                        "Test with form %s and name %s exists at workstation %s in the target unit. Move failed",
                        targetTest.getFormName(), testName, targetTest.getWorkstationName()));
            }

            // we also need to make sure the workstation exists in the target
            // unit and make sure its status is set accordingly.
            if (!(workstations.contains(new WorkstationOID(testProc.getWorkstationPk()))))
            {
                // if the workstation is not there on the unit, add it
                UnitWorkstation existingOne = persistWrapper.read(UnitWorkstation.class,
                        "select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?",
                        unitOIDToMoveTo.getPk(), projectOIDToMoveTo.getPk(), testProc.getWorkstationPk());
                if (existingOne == null)
                {
                    UnitWorkstation pForm = new UnitWorkstation();
                    pForm.setEstatus(EStatusEnum.Active.getValue());
                    pForm.setUpdatedBy((int) userContext.getUser().getPk());
                    pForm.setProjectPk((int) projectOIDToMoveTo.getPk());
                    pForm.setUnitPk((int) unitOIDToMoveTo.getPk());
                    pForm.setWorkstationPk(testProc.getWorkstationPk());

                    persistWrapper.createEntity(pForm);
                }
                workstations.add(new WorkstationOID(testProc.getWorkstationPk()));
            }

            testProc.setUnitPk((int) unitOIDToMoveTo.getPk());
            dao.saveTestProc(userContext, testProc);
        }

        // now set the workstation status to Inprogress if the are inprogress
        // forms in the workstation after the move.
        // get the workstation testproc status count report
        TestProcStatusSummaryReportRequest request = new TestProcStatusSummaryReportRequest();
        request.setProjectOIDList(Arrays.asList(new ProjectOID[] { projectOIDToMoveTo }));
        request.setUnitOID(unitOIDToMoveTo);
        request.setIncludeChildrenUnits(false);
        request.setProjectOIDForUnitHeirarchy(projectOIDToMoveTo);
        request.setGroupingSet(Arrays.asList(new TestProcStatusSummaryReportRequest.GroupingCol[] { TestProcStatusSummaryReportRequest.GroupingCol.workstation }));
        TestProcStatusSummaryReport report = new TestProcStatusSummaryReport( persistWrapper, dummyWorkstation, unitManager, userContext, request);
        TestProcStatusSummaryReportResult result = report.runReport();
        List<TestProcStatusSummaryReportResultRow> resultRows = result.getReportResult();
        HashMap<WorkstationOID, TestProcStatusSummaryReportResultRow> unitWsLookupMap = new HashMap<>();
        for (Iterator iterator = resultRows.iterator(); iterator.hasNext();)
        {
            TestProcStatusSummaryReportResultRow testProcStatusSummaryReportResultRow = (TestProcStatusSummaryReportResultRow) iterator
                    .next();
            unitWsLookupMap.put(new WorkstationOID(testProcStatusSummaryReportResultRow.getWorkstationPk()),
                    testProcStatusSummaryReportResultRow);
        }

        // now try to set the status of the workstations based on the moved
        // forms
        for (Iterator iterator = workstations.iterator(); iterator.hasNext();)
        {
            WorkstationOID workstationOID = (WorkstationOID) iterator.next();

            String currentStatus = UnitLocation.STATUS_WAITING;
            UnitLocation currentWsStatus = workstationService.getUnitWorkstation((int) unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
                    workstationOID);
            if (currentWsStatus != null)
                currentStatus = currentWsStatus.getStatus();

            TestProcStatusSummaryReportResultRow row = unitWsLookupMap.get(workstationOID);
            if (row != null)
            {
                if (row.getInProgressCount() > 0)
                {
                    if (!(UnitLocation.STATUS_IN_PROGRESS.equals(currentStatus)))
                    {
                        workstationService.setUnitWorkstationStatus(userContext, (int) unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
                                workstationOID, UnitLocation.STATUS_IN_PROGRESS);
                    }
                }
            }
        }
    }

    public  void renameTestForms(UserContext userContext, List<TestProcOID> selectedTestProcs,
                                       List<OID> referencesToAdd, String nameChangeText, String renameOption) throws Exception
    {
        // the best option is to get one testproc and get the root unit and
        // project and load all testprocs to check if we are duplicating the
        // name
        HashMap<String, UnitFormQuery> targetUnitTestMap = new HashMap<String, UnitFormQuery>();
        if (selectedTestProcs.size() > 0)
        {
            TestProcObj aTest = new TestProcDAO().getTestProc((int) selectedTestProcs.get(0).getPk());
            int rootUnitPk = unitManager.getRootUnitPk(new UnitOID(aTest.getUnitPk()),
                    new ProjectOID(aTest.getProjectPk()));

            // load the forms in the target unit. and put it into a hashmap for
            // easy lookup
            TestProcFilter listFilter = new TestProcFilter(new ProjectOID(aTest.getProjectPk()));
            listFilter.setUnitOID(new UnitOID(rootUnitPk));
            listFilter.setIncludeChildren(true);
            List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter,persistWrapper, unitManager, dummyWorkstation).getTestProcs();
            for (Iterator iterator = testList.iterator(); iterator.hasNext();)
            {
                UnitFormQuery test = (UnitFormQuery) iterator.next();
                targetUnitTestMap.put(test.getUnitPk() + "-" + test.getWorkstationPk() + "-" + test.getFormMainPk()
                        + "-" + test.getName(), test);
            }

        }
        for (Iterator iterator = selectedTestProcs.iterator(); iterator.hasNext();)
        {
            TestProcOID testProcOID = (TestProcOID) iterator.next();

            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());

            Survey form = surveyMasterService.getSurveyByPk(testProc.getFormPk());

            if ("Append text to existing test name".equals(renameOption) || "Rename test".equals(renameOption))
            {
                if (nameChangeText == null)
                    throw new AppException("Name cannot be blank");

                String newName = testProc.getName();
                if ("Append text to existing test name".equals(renameOption))
                {
                    newName = ListStringUtil.showString(newName, "") + nameChangeText;
                } else
                {
                    newName = nameChangeText;
                }

                UnitFormQuery targetTest = targetUnitTestMap.get(testProc.getUnitPk() + "-"
                        + testProc.getWorkstationPk() + "-" + form.getFormMainPk() + "-" + newName);
                if (targetTest != null)
                {
                    String testName = targetTest.getName();
                    if (testName == null)
                        testName = "{None}";

                    throw new AppException(String.format(
                            "Test with form %s and name %s exists at workstation %s in the target unit. Rename failed",
                            targetTest.getFormName(), testName, targetTest.getWorkstationName()));
                } else
                {
                    testProc.setName(newName);
                    dao.saveTestProc(userContext, testProc);
                }

            }

            if (referencesToAdd != null)
            {
                for (Iterator iterator2 = referencesToAdd.iterator(); iterator2.hasNext();)
                {
                    OID oid = (OID) iterator2.next();
                    commonServiceManager.createEntityReference(userContext, testProcOID, oid);
                }
            }
        }

    }
    @Override
    public  List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception
    {
        List<UnitFormQuery> l = persistWrapper.readList(UnitFormQuery.class,
                unitFormQuery.sql + " and tfa.formFk in "
                        + "(select pk from TAB_SURVEY where formMainPk=? and formType = 1) order by unitPk desc",
                formQuery.getFormMainPk());

        return l;
    }

    @Override
    public  TestProcObj upgradeFormForUnit(UserContext context, TestProcOID testProcOID, int surveyPk)
            throws Exception
    {
        // we have to delete the responses for the selected projects
        // i am changing the mind, i dont think we need o delete the response,
        // but we can one
        // with a new one.. so the old one is available in history.
        // ResponseMasterNew[] responses =
        // SurveyResponseManager.getLatestResponseMastersForUnitForForm(
        // context, aUnitPk, survey.getFormMainPk());
        // for (int i = 0; i < responses.length; i++)
        // {
        // ResponseMasterNew aResponse = responses[i];
        // SurveyResponseManager.deleteResponse(aResponse.getFormPk(), new
        // int[]{aResponse.getResponseId()});
        // }

        TestProcDAO tpDAO = new TestProcDAO();
        TestProcObj testProc = tpDAO.getTestProc((int) testProcOID.getPk());

        // now mark the old response as old and create the new dummy response
        // for the updated testProc if one existed for it...
        ResponseMasterNew response = surveyResponseService.getLatestResponseMasterForTest(testProc.getOID());
        if (response != null && response.getFormPk() != surveyPk)
        {
            // means the response for this testProc already exists.. now the
            // formPk on the testProc changes..
            surveyResponseService.markResponseAsOld(context, response);
        }

        // update the formPk in the testProc. A new _H record will get created.
        testProc.setFormPk(surveyPk);
        testProc.setAppliedByUserFk((int) context.getUser().getPk());
        testProc = tpDAO.saveTestProc(context, testProc);

        // create the new dummy response for the updated testProc if one existed
        // for the old response...
        if (response != null && response.getFormPk() != surveyPk)
        {
            SurveyDefinition sd = surveyDefFactory.getSurveyDefinition(new FormOID(surveyPk, null));
            SurveyResponse sResponse = new SurveyResponse(sd);
            sResponse.setSurveyPk(surveyPk);
            sResponse.setTestProcPk(testProc.getPk());
            sResponse.setResponseStartTime(new Date());
            sResponse.setResponseCompleteTime(new Date());
            // ipaddress and mode set
            sResponse.setIpaddress("0.0.0.0");
            sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
            sResponse.setUser((User) context.getUser());
            sResponse = surveyResponseService.ceateDummyResponse(context, sResponse);
            // the createDummyResponse automatically creates a new workflow
            // entry for you..
            // so we need not create another one..
            logger.info("Created the new dummy response");
        }

        return testProc;
    }
    public  List<AssignedTestsQuery> getAssignedFormsNew(UserOID user, String role) throws Exception
    {
        String lookForTestStatus = "";
        String workFlowStatus = "";
        String commonWorkFlowStatus = ResponseMasterNew.STATUS_REJECTED;
        if (role.equals(User.ROLE_TESTER))
        {
            lookForTestStatus = ResponseMasterNew.STATUS_INPROGRESS;
            workFlowStatus = ResponseMasterNew.STATUS_INPROGRESS;
        } else if (role.equals(User.ROLE_VERIFY))
        {
            lookForTestStatus = ResponseMasterNew.STATUS_COMPLETE;
            workFlowStatus = ResponseMasterNew.STATUS_COMPLETE;
        } else if (role.equals(User.ROLE_APPROVE))
        {
            lookForTestStatus = ResponseMasterNew.STATUS_VERIFIED;
            workFlowStatus = ResponseMasterNew.STATUS_VERIFIED;
        }

        String sql = "select ut.pk as testProcPk, uth.name as testProcName,"
                + " p.pk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription,"
                + " uth.unitPk as unitPk, uh.unitName as unitName, uh.unitDescription as unitDescription,"
                + " uth.workstationPk as workstationPk, w.workstationName as workstationName, w.description as workstationDescription, w.orderNo as workstationOrderNo,"
                + " site.name as workstationSiteName, f.formMainPk as formMainPk, tfa.formFk as formPk, f.identityNumber as formName, f.description as formDescription,"
                + " f.revision as formRevision," + " f.versionNo as formVersion,"
                + " fwf.comments as comment, fwf.date as assignedDate,"
                + " r.responseId, r.percentComplete, r.totalQCount, r.totalACount, r.noOfComments as commentCount, r.passCount, r.failCount,"
                + " r.dimentionalFailCount, r.naCount, r.status as responseStatus, "
                + " transferData.transferCount as oilTransferCount" + " from "
                + " unit_testproc ut "
                + " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
                + " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
                + " join TAB_RESPONSE r on r.testProcPk = ut.pk and r.surveyPk = tfa.formFk and r.current = 1 "
                + " left outer join " + "		(select ut1.pk ufPk, count(*) as transferCount "
                + "		from unit_testproc ut1 "
                + "     join testproc_form_assign tfa1 on tfa1.testProcFk = ut1.pk and tfa1.current = 1 "
                + " 	join unit_testproc_h uth1 on uth1.unitTestProcFk = ut1.pk and now() between uth1.effectiveDateFrom and uth1.effectiveDateTo "
                + " 	join  TAB_RESPONSE r on r.testProcPk = ut1.pk and r.surveyPk = tfa1.formFk and r.current = 1 "
                + "		join TAB_UNIT_USERS uu on uu.unitPk = uth1.unitPk and uu.projectPk = uth1.projectPk and uu.workstationPk = uth1.workstationPk "
                + " 	join  TAB_ITEM_RESPONSE ires on ires.responseId = r.responseId "
                + " 	join TESTITEM_OIL_TRANSFER oilTransfer on oilTransfer.itemResponsePk = ires.pk "
                + "		where " + "		uu.userPk = ? and uu.role = ? and r.status=? "
                + " 	group by ut1.pk) transferData on transferData.ufPk = ut.pk "
                + " join TAB_UNIT u on uth.unitPk = u.pk "
                + " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
                + " join TAB_PROJECT p on uth.projectPk = p.pk and p.status = 'Open' "
                + " join TAB_WORKSTATION w on uth.workstationPk = w.pk "
                + " join unit_project_ref upr on upr.projectPk = uth.projectPk and upr.unitPk = uth.unitPk "
                + " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status= 'Open' "
                + " join site on w.sitePk = site.pk "
                + " join TAB_UNIT_USERS uu on uu.unitPk = uth.unitPk and uu.projectPk = uth.projectPk and uu.workstationPk = uth.workstationPk "
                + " join TAB_UNIT_LOCATION ul on ul.unitPk = uth.unitPk and ul.projectPk = uth.projectPk and ul.workstationPk = uth.workstationPk and ul.status = 'In Progress' and ul.current = 1 "
                + " join TAB_FORM_WORKFLOW fwf on fwf.responseId = r.responseId and fwf.current  = 1  "
                + " join TAB_SURVEY f on r.surveyPk = f.pk and f.formType = 1 " + " where "
                + " uu.userPk = ? and uu.role = ? and r.status=? " + " order by fwf.date desc";

        List<AssignedTestsQuery> myAssignments = persistWrapper.readList(AssignedTestsQuery.class, sql, user.getPk(),
                role, lookForTestStatus, user.getPk(), role, lookForTestStatus);
        return myAssignments;
    }

}
