package com.tathvatech.forms.service;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;

import java.util.List;

public class FormServiceImpl implements  FormService{
    @Override
    public  void saveTestProcSchedule(UserContext context, TestProcOID testProcOID,
                                            ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception
    {
        Date now = DateUtils.getNowDateForEffectiveDateFrom();

        UnitFormQuery testProc = TestProcServiceImpl.getTestProcQuery(testProcOID.getPk());
        if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProc.getForecastStartDate())
                && Objects.equals(objectScheduleRequestBean.getEndForecast(), testProc.getForecastEndDate())
                && Objects.equals(objectScheduleRequestBean.getHoursEstimate(), testProc.getForecastHours()))
        {
            return;
        } else
        {
            EntitySchedule es = PersistWrapper.read(EntitySchedule.class,
                    "select * from entity_schedule where objectPk = ? and objectType = ? and now() between effectiveDateFrom and effectiveDateTo",
                    objectScheduleRequestBean.getObjectOID().getPk(),
                    objectScheduleRequestBean.getObjectOID().getEntityType().getValue());

            if (es == null)
            {
                es = new EntitySchedule();
                es.setObjectPk(objectScheduleRequestBean.getObjectOID().getPk());
                es.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
                es.setCreatedBy(context.getUser().getPk());
                es.setCreatedDate(new Date());
                es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
                es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
                es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
                es.setEffectiveDateFrom(now);
                es.setEffectiveDateTo(DateUtils.getMaxDate());
                PersistWrapper.createEntity(es);
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
                    PersistWrapper.update(es);
                } else
                {
                    // invalidate old and create new
                    es.setEffectiveDateTo(new Date(now.getTime() - 1000));
                    PersistWrapper.update(es);

                    EntitySchedule esNew = new EntitySchedule();
                    esNew.setObjectPk(objectScheduleRequestBean.getObjectOID().getPk());
                    esNew.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
                    esNew.setCreatedBy(context.getUser().getPk());
                    esNew.setCreatedDate(new Date());
                    esNew.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
                    esNew.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
                    esNew.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
                    esNew.setEffectiveDateFrom(now);
                    esNew.setEffectiveDateTo(DateUtils.getMaxDate());
                    PersistWrapper.createEntity(esNew);
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
                context, req).runReport();

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
        List<UnitFormQuery> list = new TestProcListReport(context, filter).getTestProcs();
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
        List<TestProcSectionListReportResultRow> secList = new TestProcSectionListReport(context, secFilter)
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

        UnitInProjectObj unitInProject = new UnitInProjectDAO().getUnitInProject(unitOIDToMoveTo, projectOIDToMoveTo);
        if (unitInProject == null)
            throw new AppException("Invalid target unit, or target unit is not part of the project.");

        // load the forms in the target unit. and put it into a hashmap for easy
        // lookup
        TestProcFilter listFilter = new TestProcFilter(projectOIDToMoveTo);
        listFilter.setUnitOID(unitOIDToMoveTo);
        listFilter.setIncludeChildren(false);
        List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter).getTestProcs();
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
            TestProcObj testProc = dao.getTestProc(testProcOID.getPk());

            if (testProc.getUnitPk() == unitOIDToMoveTo.getPk())
                continue;

            // confirm that the target unit is also part of the same project
            if (testProc.getProjectPk() != projectOIDToMoveTo.getPk())
                throw new AppException("You can only move a form within the same project.");

            Survey form = SurveyMaster.getSurveyByPk(testProc.getFormPk());
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
                UnitWorkstation existingOne = PersistWrapper.read(UnitWorkstation.class,
                        "select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?",
                        unitOIDToMoveTo.getPk(), projectOIDToMoveTo.getPk(), testProc.getWorkstationPk());
                if (existingOne == null)
                {
                    UnitWorkstation pForm = new UnitWorkstation();
                    pForm.setEstatus(EStatusEnum.Active.getValue());
                    pForm.setUpdatedBy(userContext.getUser().getPk());
                    pForm.setProjectPk(projectOIDToMoveTo.getPk());
                    pForm.setUnitPk(unitOIDToMoveTo.getPk());
                    pForm.setWorkstationPk(testProc.getWorkstationPk());

                    PersistWrapper.createEntity(pForm);
                }
                workstations.add(new WorkstationOID(testProc.getWorkstationPk()));
            }

            testProc.setUnitPk(unitOIDToMoveTo.getPk());
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
        request.setGroupingSet(Arrays.asList(new GroupingCol[] { GroupingCol.workstation }));
        TestProcStatusSummaryReport report = new TestProcStatusSummaryReport(
                userContext, request);
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
            UnitLocation currentWsStatus = getUnitWorkstation(unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
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
                        setUnitWorkstationStatus(userContext, unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
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
            TestProcObj aTest = new TestProcDAO().getTestProc(selectedTestProcs.get(0).getPk());
            int rootUnitPk = UnitManager.getRootUnitPk(new UnitOID(aTest.getUnitPk()),
                    new ProjectOID(aTest.getProjectPk()));

            // load the forms in the target unit. and put it into a hashmap for
            // easy lookup
            TestProcFilter listFilter = new TestProcFilter(new ProjectOID(aTest.getProjectPk()));
            listFilter.setUnitOID(new UnitOID(rootUnitPk));
            listFilter.setIncludeChildren(true);
            List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter).getTestProcs();
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
            TestProcObj testProc = dao.getTestProc(testProcOID.getPk());

            Survey form = SurveyMaster.getSurveyByPk(testProc.getFormPk());

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
                    CommonServiceManager.createEntityReference(userContext, testProcOID, oid);
                }
            }
        }

    }
    @Override
    public  List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception
    {
        List<UnitFormQuery> l = PersistWrapper.readList(UnitFormQuery.class,
                UnitFormQuery.sql + " and tfa.formFk in "
                        + "(select pk from TAB_SURVEY where formMainPk=? and formType = 1) order by unitPk desc",
                formQuery.getFormMainPk());

        return l;
    }
}
