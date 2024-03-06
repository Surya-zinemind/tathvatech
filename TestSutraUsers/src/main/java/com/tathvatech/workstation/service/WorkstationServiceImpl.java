package com.tathvatech.workstation.service;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.exception.FormApprovedException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.forms.common.TestProcMatchMaker;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.forms.service.TestProcManager;
import com.tathvatech.openitem.andon.service.AndonManager;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.entity.ProjectPart;
import com.tathvatech.project.entity.ProjectUser;
import com.tathvatech.project.enums.ProjectPropertyEnum;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.project.service.ProjectTemplateManager;
import com.tathvatech.site.entity.ACL;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.site.service.SiteService;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.controller.SurveyResponseDelegate;
import com.tathvatech.survey.service.SurveyDefFactory;
import com.tathvatech.survey.service.SurveyResponseManager;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.dao.UnitInProjectDAO;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.user.service.CommonServicesDelegate;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.common.UnitWorkstationQuery;
import com.tathvatech.workstation.common.WorkstationQuery;
import com.tathvatech.workstation.controller.WorkstationOrderNoController;
import com.tathvatech.workstation.entity.ProjectWorkstation;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.request.WorkstationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkstationServiceImpl implements WorkstationService{
    private static final Logger logger = LoggerFactory.getLogger(WorkstationServiceImpl.class);
    
    private final PersistWrapper persistWrapper;

    private final SiteService siteService;
    
    private final AccountService accountService;

    private final ProjectService projectService;

    private final SurveyResponseManager surveyResponseManager;

    private final ProjectTemplateManager projectTemplateManager;

    private final DummyWorkstation dummyWorkstation;

    public Workstation createWorkstation(UserContext context, Workstation workstation) throws Exception
    {
        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        Site site = siteService.getSite(workstation.getSitePk());
        if (site == null)
            throw new AppException("Invalid Site selected.");

        if (isWorkstationNameExist(acc, site, workstation.getWorkstationName()))
        {
            throw new AppException("Duplicate workstation name, Please choose a different workstation name.");
        }

        int pk;
        synchronized (WorkstationOrderNoController.class)
        {
            int maxOrderNo = persistWrapper.read(Integer.class, "select ifnull(max(orderNo),0) from TAB_WORKSTATION");
            workstation.setOrderNo(maxOrderNo + 1);
            workstation.setAccountPk((int) acc.getPk());
            workstation.setCreatedBy((int) user.getPk());
            workstation.setCreatedDate(new Date());
            workstation.setStatus(Workstation.STATUS_OPEN);
            workstation.setEstatus(EStatusEnum.Active.getValue());
            workstation.setUpdatedBy((int) user.getPk());
            pk = (int) persistWrapper.createEntity(workstation);
        }
        // fetch the new project back
        workstation = (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, pk);
        return workstation;

    }
    public  Workstation updateWorkstation(UserContext context, Workstation workstation) throws Exception
    {
        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        Site site = siteService.getSite(workstation.getSitePk());
        if (site == null)
            throw new AppException("Invalid Site selected.");

        if (isWorkstationNameExistForAnotherWorkstation(acc, site, workstation.getWorkstationName(),
                (int) workstation.getPk()))
        {
            throw new AppException("Another workstation with the name specified exists, Please choose another name.");
        }

        workstation.setUpdatedBy((int) context.getUser().getPk());
        persistWrapper.update(workstation);

        // fetch the new project back
        workstation = (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, workstation.getPk());
        return workstation;
    }
    private  boolean isWorkstationNameExist(Account acc, Site site, String workstationName) throws Exception
    {
        List list = persistWrapper.readList(Workstation.class,
                "select * from TAB_WORKSTATION where accountPk=? and sitePk=? and " + "workstationName =?", acc.getPk(),
                site.getPk(), workstationName);
        if (list.size() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    private  boolean isWorkstationNameExistForAnotherWorkstation(Account acc, Site site, String workstationName,
                                                                       int workstationPk) throws Exception
    {
        try
        {
            List list = persistWrapper.readList(Workstation.class,
                    "select * from TAB_WORKSTATION where accountPk=? and sitePk=? and workstationName =?", acc.getPk(),
                    site.getPk(), workstationName);
            if (list.size() == 0)
            {
                return false;
            } else if (list.size() == 1 && ((Workstation) list.get(0)).getPk() == workstationPk)
            {
                return false;
            } else
            {
                return true;
            }

        }
        catch (Exception e)
        {
            logger.error("Error getting units for project " + " :: " + e.getMessage());
            if (logger.isDebugEnabled())
            {
                logger.debug(e.getMessage(), e);
            }
            throw new Exception();
        }
    }

    public  List<WorkstationQuery> getWorkstationList() throws Exception
    {
        String sql = WorkstationQuery.sql + " and  ws.workstationName != ? order by site.name, ws.orderNo";
        return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY);
    }

    public  Workstation getWorkstation(WorkstationOID workstationOID)
    {
        return (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
    }

    public  WorkstationQuery getWorkstationQueryByPk(WorkstationOID workstationOID)
    {
        List list = persistWrapper.readList(WorkstationQuery.class, WorkstationQuery.sql + " and ws.pk=?",
                workstationOID.getPk());

        if (list != null && list.size() > 0)
        {
            return (WorkstationQuery) list.get(0);
        }
        return null;
    }

    public  List<WorkstationQuery> getWorkstationsForProject(long projectPk)
    {
        String sql = WorkstationQuery.sql
                + " and ws.workstationName != ? and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?) order by ws.orderNo";

        return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY, projectPk);
    }

    public  List<WorkstationQuery> getWorkstations(WorkstationFilter filter)
    {
        StringBuffer sb = new StringBuffer();
        List params = new ArrayList();

        sb.append(" and ws.workstationName != ? and ws.estatus = 1 ");
        params.add(dummyWorkstation.DUMMY);

        if (StringUtils.isNotBlank(filter.getFilterString()))
        {
            // sb.append(" and (upper(ws.workstationName) like ? or
            // upper(ws.description) like ? )");
            // params.add("%" + filter.getFilterString().toUpperCase().trim() +
            // "%");
            // params.add("%" + filter.getFilterString().toUpperCase().trim() +
            // "%");

            sb.append(
                    " and (concat('WS',upper(ws.workstationName),' ',upper(ws.description),' / ',site.name) like ? )");
            params.add("%" + filter.getFilterString().toUpperCase().trim() + "%");
        }

        if (filter.getSiteOIDs() != null && filter.getSiteOIDs().size() > 0)
        {
            String sep = "";
            sb.append(" and ws.sitePk in (");
            for (Iterator iterator = filter.getSiteOIDs().iterator(); iterator.hasNext();)
            {
                SiteOID aSite = (SiteOID) iterator.next();
                sb.append(sep).append("?");
                sep = ", ";
                params.add(aSite.getPk());
            }
            sb.append(")");
        }
        if (filter.getUnitOID() != null)
        {
            sb.append(" and ws.pk in (select workstationPk from TAB_UNIT_WORKSTATIONS where unitPk = ?)");
            params.add(filter.getUnitOID().getPk());
        }
        if (filter.getProjectOID() != null)
        {
            sb.append(" and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk = ?)");
            params.add(filter.getProjectOID().getPk());
        }
        sb.append(" order by ws.orderNo");
        return persistWrapper.readList(WorkstationQuery.class, WorkstationQuery.sql + sb.toString(), params.toArray());
    }

    public  List<WorkstationQuery> getWorkstationsAssignableForProject(ProjectOID projectOID)
    {
        String sql = WorkstationQuery.sql
                + " and ws.workstationName != ? and ws.sitePk in (select siteFk from project_site_config where projectFk=? and estatus = 1) order by ws.orderNo";
        try
        {
            return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY, projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<WorkstationQuery> getWorkstationsForSite(int sitePk) throws Exception
    {
        String sql = WorkstationQuery.sql + " and  ws.workstationName != ? and ws.sitePk=? order by ws.orderNo";
        return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY, sitePk);
    }

    public  List<WorkstationQuery> getWorkstationsForSiteAndProject(int sitePk, int projectPk) throws Exception
    {
        String sql = WorkstationQuery.sql
                + " and  ws.workstationName != ? and ws.sitePk=? and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?) order by ws.orderNo";
        return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY, sitePk, projectPk);
    }

    public  void addWorkstationToProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        ProjectWorkstation pForm = new ProjectWorkstation();
        pForm.setProjectPk(projectPk);
        pForm.setWorkstationPk((int) workstationOID.getPk());

        persistWrapper.createEntity(pForm);
    }

    public  void removeWorkstationFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        // delete workstation forms
        persistWrapper.delete("delete from TAB_PROJECT_FORMS where projectPk=? and workstationPk =?", projectPk,
                workstationOID.getPk());

        // delete workstation users
        persistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk = ? and workstationPk = ?", projectPk,
                workstationOID.getPk());

        // now delete workstations
        persistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=? and workstationPk = ?", projectPk,
                workstationOID.getPk());
    }

    public  void removeAllWorkstationsFromProject(UserContext context, int projectPk) throws Exception
    {
        // delete workstation forms
        persistWrapper.delete(
                "delete from TAB_PROJECT_FORMS where projectPk=? and workstationPk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?)",
                projectPk, projectPk);

        // delete workstation users
        persistWrapper.delete(
                "delete from TAB_PROJECT_USERS where projectPk = ? and workstationPk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?)",
                projectPk, projectPk);

        // now delete workstations
        persistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=?", projectPk);
    }
    public  List<WorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception
    {
        String sql = WorkstationQuery.sql + " and "
                + "ws.workstationName != ? and ws.pk in (select workstationPk from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and estatus = ? ) order by ws.orderNo";
        return persistWrapper.readList(WorkstationQuery.class, sql, dummyWorkstation.DUMMY, unitOID.getPk(), projectOID.getPk(),
                EStatusEnum.Active.getValue());
    }

    /**
     * returns the list of UnitWorkstationQuery for a unit and its children.
     *
     * @param
     * @param includeChildUnits
     * @return
     * @throws Exception
     */
    public List<UnitWorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID,
                                                             boolean includeChildUnits) throws Exception
    {
        if (includeChildUnits)
        {
            UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

            String sql = UnitWorkstationQuery.sql + " and " + "w.workstationName != ?  "
                    + " and ( (u.pk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) )"
                    + " and uw.projectPk = ? and uw.estatus = ? order by uprh.level, w.orderNo";

            return persistWrapper.readList(UnitWorkstationQuery.class, sql, dummyWorkstation.DUMMY, unitInProject.getUnitPk(),
                    unitInProject.getRootParentPk(), unitInProject.getHeiCode() + ".%", projectOID.getPk(),
                    EStatusEnum.Active.getValue());
        } else
        {
            String sql = UnitWorkstationQuery.sql + " and "
                    + "w.workstationName != ? and uw.unitpk  = ? and uw.projectPk = ? and uw.estatus = ? order by w.orderNo";

            return persistWrapper.readList(UnitWorkstationQuery.class, sql, dummyWorkstation.DUMMY, unitOID.getPk(),
                    projectOID.getPk(), EStatusEnum.Active.getValue());
        }
    }

    public  UnitWorkstation addWorkstationToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                                       WorkstationOID workstationOID) throws Exception
    {
        int uwPk = 0;

        UnitWorkstation existingOne = persistWrapper.read(UnitWorkstation.class,
                "select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?",
                unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());
        if (existingOne == null)
        {
            UnitWorkstation pForm = new UnitWorkstation();
            pForm.setEstatus(EStatusEnum.Active.getValue());
            pForm.setUpdatedBy((int) context.getUser().getPk());
            pForm.setProjectPk((int) projectOID.getPk());
            pForm.setUnitPk((int) unitOID.getPk());
            pForm.setWorkstationPk((int) workstationOID.getPk());

            uwPk = (int) persistWrapper.createEntity(pForm);
        } else
        {
            uwPk = (int) existingOne.getPk();
        }
        Project proj = projectService.getProject(projectOID.getPk());



        String defaultWorkstationStatusValue = (String) new CommonServicesDelegate().getEntityPropertyValue(projectOID,
                ProjectPropertyEnum.SetNewWorkstationsDefaultStatusTo.getId(), String.class);
        if (defaultWorkstationStatusValue != null && (!(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue))))
        {
            if(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue) || UnitLocation.STATUS_IN_PROGRESS.equals(defaultWorkstationStatusValue)
                    || UnitLocation.STATUS_COMPLETED.equals(defaultWorkstationStatusValue))
            {
                setUnitWorkstationStatus(context, (int) unitOID.getPk(), projectOID, workstationOID,
                        defaultWorkstationStatusValue);
            }
            else
            {
                throw new AppException("Default status value configured for workstation status is invalid. Please contact support");
            }
        }

        return (UnitWorkstation) persistWrapper.readByPrimaryKey(UnitWorkstation.class, uwPk);
    }

    public  void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                                 WorkstationOID workstationOID) throws Exception
    {
        // if the forms related to workstations have been attempted, the
        // workstation cannot be removed from the unit
        // int count = persistWrapper.read(Integer.class, "select count(pk) from
        // TAB_RESPONSE where unitPk=? and workstationPk=?", unitPk,
        // workstationPk);

        int count = persistWrapper.read(Integer.class, "select count(*) from TAB_RESPONSE res "
                        + " join unit_testproc ut on res.testProcPk = ut.pk "
                        + " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.formFk = res.surveyPk and tfa.current = 1 "
                        + " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
                        + " where   "
                        + " uth.unitPk = ? and uth.projectPk = ? and uth.workstationPk=? ", unitOID.getPk(),
                projectOID.getPk(), workstationOID.getPk());

        if (count > 0)
        {
            List errors = new ArrayList();
            errors.add("Workstation cannot be removed as forms have been filled out at this workstation for the unit.");
            throw new AppException((String[]) errors.toArray(new String[errors.size()]));
        }
        // delete workstation forms
        removeAllFormsFromUnit(context, unitOID.getPk(), projectOID, workstationOID);

        // delete workstation users
        persistWrapper.delete("delete from TAB_UNIT_USERS where unitPk = ? and projectPk = ? and workstationPk = ?",
                unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());

        // now delete workstations
        UnitWorkstation uw = getUnitWorkstationSetting((int) unitOID.getPk(), projectOID, workstationOID);
        uw.setUpdatedBy((int) context.getUser().getPk());
        uw.setEstatus(EStatusEnum.Deleted.getValue());
        persistWrapper.update(uw);
    }

    /*
     * Not used anywhere. Please check and remove if so.
     */
    @Deprecated
    public  List<UnitLocationQuery> getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID)
            throws Exception
    {
        // we only need to get the workstations where the form count is greater
        // than 0;
        List<Map<String, Object>> validWs = persistWrapper.readListAsMap(
                "select count(*) as count, uth.workstationPk as wspk from unit_testproc ut "
                        + " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
                        + " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
                        + " where uth.unitPk = ? and uth.projectPk = ? and uth.workstationPk != ? group by uth.workstationPk having count(*) > 0 ;",
                unitOID.getPk(), projectOID.getPk(), dummyWorkstation.getPk());

        if (validWs == null || validWs.size() == 0)
            return new ArrayList<UnitLocationQuery>();

        StringBuffer sb = new StringBuffer(UnitLocationQuery.fetchSQL)
                .append(" and uloc.unitPk = ? and uloc.projectPk = ?").append(" and uloc.workstationPk in (");

        String sep = "";
        for (Iterator iterator = validWs.iterator(); iterator.hasNext();)
        {
            Map<String, Object> aWs = (Map<String, Object>) iterator.next();
            sb.append(sep);
            sb.append(aWs.get("wspk"));
            sep = ",";
        }
        sb.append(")");
        sb.append(" and uloc.current = 1");
        return persistWrapper.readList(UnitLocationQuery.class, sb.toString(), unitOID.getPk(), projectOID.getPk());
    }

    public  UnitLocation getUnitWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception
    {
        String sql = "select * from TAB_UNIT_LOCATION where  unitPk = ? and projectPk = ? and workstationPk=? and current = 1 ";
        return persistWrapper.read(UnitLocation.class, sql, unitPk, projectOID.getPk(), workstationOID.getPk());
    }

    public  UnitLocationQuery getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID,
                                                             WorkstationOID workstationOID)
    {
        String sql = UnitLocationQuery.fetchSQL
                + " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.workstationPk=? and uloc.current = 1";
        return persistWrapper.read(UnitLocationQuery.class, sql, unitOID.getPk(), projectOID.getPk(),
                workstationOID.getPk());
    }

    public  List<UnitLocationQuery> getUnitWorkstationStatusHistory(int unitPk, ProjectOID projectOID,
                                                                          WorkstationOID workstationOID) throws Exception
    {
        String sql = UnitLocationQuery.fetchSQL
                + " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.workstationPk=? order by moveInDate desc";
        return persistWrapper.readList(UnitLocationQuery.class, sql, unitPk, projectOID.getPk(),
                workstationOID.getPk());
    }
    public  void setUnitWorkstationStatus(UserContext userContext, int unitPk, ProjectOID projectOID,
                                                WorkstationOID workstationOID, String status) throws Exception
    {

        // create the new unit location on any change, so we have a history of
        // all activities.
        UnitLocation nuLoc = new UnitLocation();
        nuLoc.setProjectPk((int) projectOID.getPk());
        nuLoc.setMovedInBy((int) userContext.getUser().getPk());
        nuLoc.setMoveInDate(new Date());
        nuLoc.setUnitPk(unitPk);
        nuLoc.setWorkstationPk((int) workstationOID.getPk());
        nuLoc.setStatus(status);
        nuLoc.setCurrent(1);

        // copy any values from the old rec to the new one. and mark it as not
        // current
        UnitLocation currentRec = getUnitWorkstation(unitPk, projectOID, workstationOID);
        if (currentRec != null)
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

        // if the status is being changed to completed, set the completed date
        if (UnitLocation.STATUS_COMPLETED.equals(status))
        {
            nuLoc.setCompletedDate(new Date());

            // if response is in inprogress status, we need to change it to
            // paused status
            List<UnitFormQuery> currentLocationTestList = TestProcManager.getTestProcsForItem(userContext, unitPk,
                    projectOID, workstationOID, false);
            for (Iterator iterator = currentLocationTestList.iterator(); iterator.hasNext();)
            {
                UnitFormQuery testProc = (UnitFormQuery) iterator.next();
                ResponseMasterNew response = surveyResponseManager
                        .getLatestResponseMasterForTest(new TestProcOID(testProc.getPk(), null));
                if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
                {
                    surveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
                            ResponseMasterNew.STATUS_PAUSED);
                }
            }

            // any andons in on this unit in this workstation should be closed
            // when the workstation is marked as completed
            AndonManager.markAllAndonsForUnitOnWorkstationAsClosed(userContext, new UnitOID(unitPk), projectOID,
                    workstationOID);
        }

        persistWrapper.createEntity(nuLoc);

        if (UnitLocation.STATUS_IN_PROGRESS.equals(status) || UnitLocation.STATUS_WAITING.equals(status))
        {
            // create the placeholder response and workflow entries for the
            // forms associated to that unit in that location
            List<UnitFormQuery> currentLocationTestList = TestProcManager.getTestProcsForItem(userContext, unitPk,
                    projectOID, workstationOID, false);
            for (Iterator iterator = currentLocationTestList.iterator(); iterator.hasNext();)
            {
                UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();

                if (UnitLocation.STATUS_IN_PROGRESS.equals(status))
                {

                    // if there is no response for this form already, create
                    // response placeholder..this is needed when opening for the
                    // first time
                    // this is required since when filling up responses from
                    // different tablets at the same time and submitting, they
                    // need to synchronize on the same
                    // response id. so a placeholder is created as soon as the
                    // form is opened..

                    ResponseMasterNew response = surveyResponseManager
                            .getLatestResponseMasterForTest(unitFormQuery.getOID());
                    if (response == null)
                    {
                        SurveyDefinition sd = SurveyDefFactory
                                .getSurveyDefinition(new FormOID(unitFormQuery.getFormPk(), null));
                        SurveyResponse sResponse = new SurveyResponse(sd);
                        sResponse.setSurveyPk(unitFormQuery.getFormPk());
                        sResponse.setTestProcPk(unitFormQuery.getPk());
                        sResponse.setResponseStartTime(new Date());
                        sResponse.setResponseCompleteTime(new Date());
                        // ipaddress and mode set
                        sResponse.setIpaddress("0.0.0.0");
                        sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
                        sResponse.setUser((User) userContext.getUser());
                        sResponse = surveyResponseManager.ceateDummyResponse(userContext, sResponse);
                        // the save pageresponse automatically creates a new
                        // workflow entry for you..
                        // so we need not create another one.. so commenting the
                        // below one..
                    } else
                    {
                        // the response is there, so if it is in paused status,
                        // we need to change it to InProgress
                        if (response != null && ResponseMasterNew.STATUS_PAUSED.equals(response.getStatus()))
                        {
                            surveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
                                    ResponseMasterNew.STATUS_INPROGRESS);
                        }
                    }
                } else if (UnitLocation.STATUS_WAITING.equals(status))
                {
                    // if response is in inprogress status, we need to change it
                    // to paused status
                    ResponseMasterNew response = surveyResponseManager
                            .getLatestResponseMasterForTest(unitFormQuery.getOID());
                    if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
                    {
                        surveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
                                ResponseMasterNew.STATUS_PAUSED);
                    }
                }
            }
        }
    }

    public  void recordWorkstationFormAccess(TestProcOID testProcOID)
    {
        try
        {
            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());
            UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
                    new ProjectOID(testProc.getProjectPk(), null),
                    new WorkstationOID(testProc.getWorkstationPk(), null));
            if (currentRec != null)
            {
                Date now = new Date();
                if (currentRec.getFirstFormAccessDate() == null)
                    currentRec.setFirstFormAccessDate(now);
                currentRec.setLastFormAccessDate(now);

                persistWrapper.update(currentRec);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void recordWorkstationSave(TestProcOID testProcOID)
    {
        try
        {
            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());
            UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
                    new ProjectOID(testProc.getProjectPk(), null),
                    new WorkstationOID(testProc.getWorkstationPk(), null));
            if (currentRec != null)
            {
                Date now = new Date();
                if (currentRec.getFirstFormSaveDate() == null)
                    currentRec.setFirstFormSaveDate(now);
                currentRec.setLastFormSaveDate(now);

                persistWrapper.update(currentRec);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void recordWorkstationFormLock(TestProcOID testProcOID)
    {
        try
        {
            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());
            UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
                    new ProjectOID(testProc.getProjectPk(), null),
                    new WorkstationOID(testProc.getWorkstationPk(), null));
            if (currentRec != null)
            {
                Date now = new Date();
                if (currentRec.getFirstFormLockDate() == null)
                    currentRec.setFirstFormLockDate(now);
                currentRec.setLastFormLockDate(now);

                persistWrapper.update(currentRec);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  void recordWorkstationFormUnlock(TestProcOID testProcOID)
    {
        try
        {
            TestProcDAO dao = new TestProcDAO();
            TestProcObj testProc = dao.getTestProc((int) testProcOID.getPk());
            UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
                    new ProjectOID(testProc.getProjectPk(), null),
                    new WorkstationOID(testProc.getWorkstationPk(), null));
            if (currentRec != null)
            {
                Date now = new Date();
                currentRec.setLastFormUnlockDate(now);

                persistWrapper.update(currentRec);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public  void deleteWorstation(UserContext context, WorkstationOID workstationOID) throws Exception
    {
        List errors = new ArrayList();

        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        Workstation workstation = (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());

        List pws = persistWrapper.readList(ProjectWorkstation.class,
                "select * from TAB_PROJECT_WORKSTATIONS where workstationPk=?", workstationOID.getPk());
        if (pws != null && pws.size() > 0)
        {
            errors.add(
                    "Workstation is associated with projects; Please remove workstation from projects and try again.");
        }
        pws = persistWrapper.readList(UnitWorkstation.class,
                "select * from TAB_UNIT_WORKSTATIONS where workstationPk=?", workstationOID.getPk());
        if (pws != null && pws.size() > 0)
        {
            errors.add("Workstation is associated with units; Please remove workstation from units and try again.");
        }
        if (errors.size() > 0)
            throw new AppException((String[]) errors.toArray(new String[errors.size()]));

        // mark the workstation as deleted
        workstation.setEstatus(EStatusEnum.Deleted.getValue());
        workstation.setUpdatedBy((int) context.getUser().getPk());
        persistWrapper.update(workstation);
    }
    public  void setWorkstationsAndTeamsOnUnitOpen(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                                          UnitInProjectObj unitInProject,
                                                          boolean copyProjectWorkstationUsersToUnit, boolean copyProjectWorkstationFormsToUnit) throws Exception
    {
        // the status is being changed from planned to open, so copy the
        // workstation, users, forms etc.
        // copy project workstations to unitworkstations
        List<WorkstationQuery> workstations = getWorkstationsForProject(projectOID.getPk());
        for (Iterator iterator = workstations.iterator(); iterator.hasNext();)
        {
            if(copyProjectWorkstationFormsToUnit == false && copyProjectWorkstationUsersToUnit == false)
                continue;

            WorkstationQuery workstationQuery = (WorkstationQuery) iterator.next();

            // if workstation is already added, skip;
            UnitWorkstation uw = getUnitWorkstationSetting(unitOID.getPk(), projectOID,
                    workstationQuery.getOID());
            if (uw != null)
                continue;

            // copy forms
            if (unitInProject.getProjectPartPk() == null)
                continue;

            List<ProjectFormQuery> pForms = projectTemplateManager.getTestProcsForProjectPart(projectOID,
                    new ProjectPartOID(unitInProject.getProjectPartPk(), null), (int) workstationQuery.getPk());

            // check if there are any users defined for that workstation
            List<ProjectUserQuery> pUsers = projectService.getProjectUserQueryList(projectOID,
                    new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getOID());

            if (pForms.size() == 0 && pUsers.size() == 0)
                continue; // dont copy the workstation for which there are no
            // forms added for part type or there are no teams
            // defined at that workstation.

            // create unit workstation
            UnitWorkstation unitWorkstation = new UnitWorkstation();
            unitWorkstation.setEstatus(EStatusEnum.Active.getValue());
            unitWorkstation.setUpdatedBy((int) context.getUser().getPk());
            unitWorkstation.setProjectPk((int) projectOID.getPk());
            unitWorkstation.setUnitPk((int) unitOID.getPk());
            unitWorkstation.setWorkstationPk((int) workstationQuery.getPk());
            int unitWorkstationPk = (int) persistWrapper.createEntity(unitWorkstation);

            // copy projectUsers to unitusers
            if(copyProjectWorkstationUsersToUnit)
                copyProjectUsersToUnit(projectOID, unitOID, workstationQuery.getOID(), true);

            if(copyProjectWorkstationFormsToUnit)
                copyProjectFormsToUnit(context, projectOID, pForms, unitOID, workstationQuery.getOID());

            String defaultWorkstationStatusValue = (String) new CommonServicesDelegate().getEntityPropertyValue(projectOID,
                    ProjectPropertyEnum.SetNewWorkstationsDefaultStatusTo.getId(), String.class);
            if (defaultWorkstationStatusValue != null && (!(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue))))
            {
                if(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue) || UnitLocation.STATUS_IN_PROGRESS.equals(defaultWorkstationStatusValue)
                        || UnitLocation.STATUS_COMPLETED.equals(defaultWorkstationStatusValue))
                {
                    setUnitWorkstationStatus(context, (int) unitOID.getPk(), projectOID, workstationQuery.getOID(),
                            defaultWorkstationStatusValue);
                }
                else
                {
                    throw new AppException("Default status value configured for workstation status is invalid. Please contact support");
                }
            }

        }
    }
    /**
     * Method to copy the workstation to units, here the forms or team is not
     * copied, only the workstation is copied
     *
     * @param context
     * @param projectQuery
     * @param workstationQuery
     * @param selectedUnits
     * @throws Exception
     */
    public  void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                              WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {
        long workstationPk = workstationQuery.getPk();

        // teams
        for (int i = 0; i < selectedUnits.length; i++)
        {
            int unitPk = selectedUnits[i];
            UnitObj unit = getUnitByPk(new UnitOID(unitPk));
            List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());

            if (unitWorkstations.contains(workstationQuery))
            {
            } else
            {
                // unit does not contain that workstation
                addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
            }
        }
    }

    /**
     * Method to set the teams for a project part to units, here the forms is
     * not copied
     *
     * @param context
     * @param projectQuery
     * @param workstationQuery
     * @param selectedUnits
     * @throws Exception
     */
    public  void setWorkstationProjectPartTeamSetupToUnits(UserContext context, ProjectQuery projectQuery,
                                                                 WorkstationQuery workstationQuery, ProjectPartOID projectPartOID, Integer[] selectedUnits,
                                                                 boolean copyDefaultTeamIfNoProjectPartTeamIsSet) throws Exception
    {
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();
        // teams
        for (int i = 0; i < selectedUnits.length; i++)
        {
            int unitPk = selectedUnits[i];
            UnitObj unit = getUnitByPk(new UnitOID(unitPk));

            UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
            if (unitInProject == null)
                throw new AppException("Unit is not associated with the project, Team cannot be set.");

            if (unitInProject.getProjectPartPk() == null || unitInProject.getProjectPartPk() != projectPartOID.getPk())
                continue;

            List<WorkstationQuery> unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
            if (unitWorkstations.contains(workstationQuery))
            {
                if (copyDefaultTeamIfNoProjectPartTeamIsSet)
                    copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
                else
                    copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), false);
            }
        }
    }

    public  void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                                 WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam)
            throws Exception
    {
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();
        // teams
        for (int i = 0; i < selectedUnitsForTeam.length; i++)
        {
            int unitPk = selectedUnitsForTeam[i];
            UnitObj unit = getUnitByPk(new UnitOID(unitPk));

            UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
            if (unitInProject == null)
                throw new AppException("Unit is not associated with the project, It cannot opened.");

            if (unitInProject.getProjectPartPk() == null)
                continue;

            List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());

            UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
                    workstationQuery.getOID());
            if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
            {
                continue;
            } else if (unitWorkstations.contains(workstationQuery))
            {
                // now take care of the users, they can just be deleted and
                // replaced
                copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
            } else
            {
                // unit does not contain that workstation
                addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
                copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
            }
        }

        // forms
        TestProcDAO testProcDAO = new TestProcDAO();
        for (int i = 0; i < selectedUnitsForForm.length; i++)
        {
            int unitPk = selectedUnitsForForm[i];
            UnitObj unit = getUnitByPk(new UnitOID(unitPk));

            UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
            if (unitInProject == null)
                throw new AppException("Unit is not associated with the project, It cannot opened.");

            if (unitInProject.getProjectPartPk() == null)
                continue;

            List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
            UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
                    workstationQuery.getOID());
            if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
            {
                continue;
            }
            if (unitWorkstations.contains(workstationQuery))
            {
                // unit already contains this workstation
                // copy the forms which are not there,
                // if the form is getting upgraded, then we should upgrade the
                // form inside the testproc.
                List<ProjectFormQuery> pForms = projectTemplateManager.getTestProcsForProjectPart(projectQuery.getOID(),
                        new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());
                List<UnitFormQuery> uForms = TestProcManager.getTestProcsForItem(context, unitPk, projectQuery.getOID(),
                        workstationQuery.getOID(), false);
                for (Iterator iterator = pForms.iterator(); iterator.hasNext();)
                {
                    ProjectFormQuery aPForm = (ProjectFormQuery) iterator.next();
                    if (TestProcMatchMaker.testItemContainsForm(aPForm, uForms) != null)
                    {
                    } else
                    {
                        UnitFormQuery previousRevTestProc = TestProcMatchMaker.getMatchingLowerVersionOfForm(aPForm,
                                uForms);
                        if (previousRevTestProc != null)
                        {
                            // upgrade the testproc with the new form version
                            TestProcObj testProc = new TestProcDAO().getTestProc(previousRevTestProc.getPk());
                            testProc.setFormPk(aPForm.getFormPk());
                            testProc.setAppliedByUserFk((int) context.getUser().getPk());

                            testProcDAO.saveTestProc(context, testProc);
                        } else
                        {
                            // need to create a new testproc on the unit
                            TestProcObj testProc = new TestProcObj();
                            testProc.setProjectPk(projectQuery.getPk());
                            testProc.setUnitPk(unitPk);
                            testProc.setWorkstationPk((int) workstationQuery.getPk());
                            testProc.setFormPk(aPForm.getFormPk());
                            testProc.setProjectTestProcPk(aPForm.getPk());
                            testProc.setName(aPForm.getName());
                            testProc.setAppliedByUserFk(aPForm.getAppliedByUserFk());

                            testProcDAO.saveTestProc(context, testProc);
                        }
                    }
                }
                // now remove any forms from the unit that are not there in the
                // project, if they were added directly to the unit, leave them
                // there.

                // we have to re-fetch the unit forms list here, else,
                // the forms upgrades will get removed as they are not part of
                // the unit list fetched earlier
                uForms = TestProcManager.getTestProcsForItem(context, unitPk, projectQuery.getOID(),
                        workstationQuery.getOID(), false);
                for (Iterator iterator = uForms.iterator(); iterator.hasNext();)
                {
                    UnitFormQuery aForm = (UnitFormQuery) iterator.next();
                    try
                    {
                        if (aForm.getProjectTestProcPk() == 0)
                        {
                            continue; // this means the form was added directly
                            // to the unit, so dont remove it.
                        }
                        if (TestProcMatchMaker.projectContainsForm(aForm, pForms))
                        {
                            continue;
                        }
                        deleteTestProcFromUnit(context, aForm.getOID());
                    }
                    catch (FormApprovedException fx)
                    {
                    }
                }
            } else
            {
                // unit does not contain that workstation

                List<ProjectFormQuery> pForms = projectTemplateManager.getTestProcsForProjectPart(projectQuery.getOID(),
                        new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());

                addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
                copyProjectFormsToUnit(context, projectQuery.getOID(), pForms, unit.getOID(),
                        workstationQuery.getOID());
            }
            if (uLocationQ != null && UnitLocation.STATUS_IN_PROGRESS.equals(uLocationQ.getStatus()))
            {
                setUnitWorkstationStatus(context, unitPk, projectQuery.getOID(),
                        workstationQuery.getOID(), UnitLocation.STATUS_IN_PROGRESS);
            }
        }
    }

    public  void deleteWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                                WorkstationQuery workstationQuery, UnitObj[] selectedUnits) throws Exception
    {
        for (int i = 0; i < selectedUnits.length; i++)
        {
            boolean allTestsRemoved = true;

            UnitObj unit = selectedUnits[i];
            List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
            UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
                    workstationQuery.getOID());
            if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
            {
                continue;
            }
            if (unitWorkstations.contains(workstationQuery))
            {
                // unit contains this workstation
                // remove any forms from the unit that are not there in the
                // project
                List<UnitFormQuery> uForms = TestProcManager.getTestProcsForItem(context, unit.getPk(),
                        projectQuery.getOID(), workstationQuery.getOID(), true);
                for (Iterator iterator = uForms.iterator(); iterator.hasNext();)
                {
                    UnitFormQuery testProc = (UnitFormQuery) iterator.next();
                    try
                    {
                        deleteTestProcFromUnit(context, testProc.getOID());
                    }
                    catch (FormApprovedException fx)
                    {
                        allTestsRemoved = false;
                        // do not delete from this unit
                        continue;
                    }
                }

                // now take care of the users, they can just be deleted and
                // replaced
                if (allTestsRemoved)
                {
                    removeAllUsersFromUnit(context, unit.getOID(), projectQuery.getOID(), workstationQuery.getOID());
                }
            }

            // remove the workstation from unit
            if (allTestsRemoved)
            {
                removeWorkstationFromUnit(context, unit.getOID(), projectQuery.getOID(), workstationQuery.getOID());
            }
        }
    }

    public  List<Project> getProjectsForWorkstation(UserContext context, WorkstationOID workstationOID)
            throws Exception
    {
        return persistWrapper.readList(Project.class,
                "select * from TAB_PROJECT where pk in "
                        + "(select projectPk from TAB_PROJECT_WORKSTATIONS where workstationPk=?)",
                workstationOID.getPk());
    }
    public  float getWorkstationPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
                                                      WorkstationOID workstationOID, boolean includeChildren) throws Exception
    {
        return getWorkstationPercentCompleteInt(context, unitPk, projectOID, workstationOID, includeChildren);
    }

    public  float getWorkstationPercentCompleteInt(UserContext context, int unitPk, ProjectOID projectOID,
                                                          WorkstationOID workstationOID, boolean includeChildren) throws Exception
    {
        List<UnitFormQuery> fm = null;
        if (workstationOID == null)
        {
            fm = TestProcManager.getTestProcsForItem(context, unitPk, projectOID, includeChildren);
        } else
        {
            fm = TestProcManager.getTestProcsForItem(context, unitPk, projectOID, workstationOID, includeChildren);
        }
        int formCount = 0;
        float percenCompleteAccumulator = 0f;
        for (Iterator iterator = fm.iterator(); iterator.hasNext();)
        {
            UnitFormQuery formQuery = (UnitFormQuery) iterator.next();
            formCount++;
            final ResponseMasterNew formResponse = SurveyResponseDelegate
                    .getLatestResponseMasterForTest(formQuery.getOID());

            if (formResponse != null)
            {
                percenCompleteAccumulator += formResponse.getPercentComplete();
            }
        }

        return (formCount > 0) ? (percenCompleteAccumulator * 100) / (float) (100 * formCount) : 0;
    }
    public  void moveWorkstationOrderUp(UserContext context, WorkstationOID workstationOID)
    {
        try
        {
            Workstation ws = (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
            if (ws != null)
            {
                Workstation previousOne = persistWrapper.read(Workstation.class,
                        "select * from TAB_WORKSTATION where orderNo < ? order by orderNo desc limit 0, 1",
                        ws.getOrderNo());
                if (previousOne != null)
                {
                    int orderNoTemp = previousOne.getOrderNo();
                    previousOne.setOrderNo(ws.getOrderNo());
                    ws.setOrderNo(orderNoTemp);
                    persistWrapper.update(previousOne);
                    persistWrapper.update(ws);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception changing workstation order", e);
            throw new AppException("Could not change workstation order, please try again later");
        }
    }

    public  void moveWorkstationOrderDown(UserContext context, WorkstationOID workstationOID)
    {
        try
        {
            Workstation ws = (Workstation) persistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
            if (ws != null)
            {
                Workstation nextOne = persistWrapper.read(Workstation.class,
                        "select * from TAB_WORKSTATION where orderNo > ? order by orderNo limit 0, 1", ws.getOrderNo());
                if (nextOne != null)
                {
                    int orderNoTemp = nextOne.getOrderNo();
                    nextOne.setOrderNo(ws.getOrderNo());
                    ws.setOrderNo(orderNoTemp);
                    persistWrapper.update(nextOne);
                    persistWrapper.update(ws);
                }
            }
        }
        catch (Exception e)
        {
            logger.error("Exception changing workstation order", e);
            throw new AppException("Could not change workstation order, please try again later");
        }
    }

    public  UnitWorkstation getUnitWorkstationSetting(long unitPk, ProjectOID projectOID,
                                                            WorkstationOID workstationOID)
    {
        return persistWrapper.read(UnitWorkstation.class,
                "select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?", unitPk,
                projectOID.getPk(), workstationOID.getPk());
    }

    public  UnitWorkstation updateUnitWorkstationSetting(UnitWorkstation unitWorkstation) throws Exception
    {
        persistWrapper.update(unitWorkstation);
        return (UnitWorkstation) persistWrapper.readByPrimaryKey(UnitWorkstation.class, unitWorkstation.getPk());
    }
    public  void copyWorkstationSettings(UserContext context, ProjectOID copyFromProjectOID,
                                               ProjectOID destinationProjectOID, boolean copySites, boolean copyProjectFunctionTeams, boolean copyParts,
                                               boolean copyOpenItemTeam, boolean copyProjectCoordinators, List<Object[]> workstationsToCopy)
            throws Exception
    {
        HashMap<ProjectPart, ProjectPart> projectPartMap = new HashMap();
        HashMap<Integer, ProjectPart> allProjectPartMap = new HashMap();

        try
        {
            if (copySites)
            {
                List<Site> sites = getSitesForProject(copyFromProjectOID);
                saveSitesForProject(destinationProjectOID, sites);
            }

            if (copyProjectFunctionTeams)
            {
                List<ProjectSiteConfig> config = getProjectSiteConfigs(destinationProjectOID);
                for (Iterator iterator = config.iterator(); iterator.hasNext();)
                {
                    ProjectSiteConfig projectSiteConfig = (ProjectSiteConfig) iterator.next();
                    new AuthorizationManager().removeAllAcls(projectSiteConfig.getOID());

                    // get the corresponding projectSite config on the
                    // sourceproject
                    ProjectSiteConfig sourcePf = getProjectSiteConfig(copyFromProjectOID,
                            new SiteOID(projectSiteConfig.getSiteFk(), null));
                    if (sourcePf != null)
                    {
                        List<ACL> acls = new AuthorizationManager().getAcls(sourcePf.getOID());

                        for (Iterator iterator2 = acls.iterator(); iterator2.hasNext();)
                        {
                            ACL sAcl = (ACL) iterator2.next();
                            ACL dAcl = new ACL();
                            dAcl.setCreatedDate(new Date());
                            dAcl.setObjectPk((int) projectSiteConfig.getPk());
                            dAcl.setObjectType(projectSiteConfig.getOID().getEntityType().getValue());
                            dAcl.setRoleId(sAcl.getRoleId());
                            dAcl.setUserPk(sAcl.getUserPk());
                            persistWrapper.createEntity(dAcl);

                        }
                    }
                }
            }

            if (copyParts)
            {
                // delete existing
                List<ProjectPart> projectPartsCurrent = persistWrapper.readList(ProjectPart.class,
                        "select * from project_part where projectPk = ? and project_part.estatus=1",
                        destinationProjectOID.getPk());
                for (Iterator iterator = projectPartsCurrent.iterator(); iterator.hasNext();)
                {
                    ProjectPart projectPart = (ProjectPart) iterator.next();
                    persistWrapper.deleteEntity(projectPart);
                }

                List<ProjectPart> projectParts = persistWrapper.readList(ProjectPart.class,
                        "select * from project_part where projectPk = ? and project_part.estatus=1",
                        copyFromProjectOID.getPk());
                for (Iterator iterator = projectParts.iterator(); iterator.hasNext();)
                {
                    ProjectPart sourcePart = (ProjectPart) iterator.next();

                    ProjectPart newPart = new ProjectPart();
                    newPart.setDescription(sourcePart.getDescription());
                    newPart.setEstatus(EStatusEnum.Active.getValue());
                    newPart.setLocationCode(sourcePart.getLocationCode());
                    newPart.setName(sourcePart.getName());
                    newPart.setOrderNo(sourcePart.getOrderNo());
                    newPart.setPartNo(sourcePart.getPartNo());
                    newPart.setPartPk(sourcePart.getPartPk());
                    newPart.setPartTypePk(sourcePart.getPartTypePk());
                    newPart.setProjectPk((int) destinationProjectOID.getPk());
                    newPart.setWbs(sourcePart.getWbs());
                    newPart.setCreatedDate(new Date());

                    int pk = (int) persistWrapper.createEntity(newPart);
                    newPart = (ProjectPart) persistWrapper.readByPrimaryKey(ProjectPart.class, pk);

                    projectPartMap.put(sourcePart, newPart);
                    allProjectPartMap.put(pk, newPart);
                    allProjectPartMap.put((int) sourcePart.getPk(), sourcePart);
                }

                // now the parent heirarchy has to be set for the newly created
                // parts.
                for (Iterator iterator = projectPartMap.keySet().iterator(); iterator.hasNext();)
                {
                    ProjectPart sourcePart = (ProjectPart) iterator.next();
                    ProjectPart destPart = projectPartMap.get(sourcePart);
                    if (sourcePart.getParentPk() == null)
                        continue;

                    ProjectPart sourceParent = allProjectPartMap.get(sourcePart.getParentPk());
                    ProjectPart destParent = projectPartMap.get(sourceParent);
                    destPart.setParentPk((int) destParent.getPk());
                    persistWrapper.update(destPart);
                }
            }

            if (copyOpenItemTeam)
            {
                List<User> testList = projectService.getUsersForProjectInRole(copyFromProjectOID.getPk(),
                        dummyWorkstation.getOID(), User.ROLE_TESTER);
                List<User> verifyList = projectService.getUsersForProjectInRole(copyFromProjectOID.getPk(),
                        dummyWorkstation.getOID(), User.ROLE_VERIFY);
                List<User> readonlyList = projectService.getUsersForProjectInRole(copyFromProjectOID.getPk(),
                        dummyWorkstation.getOID(), User.ROLE_READONLY);

                removeAllUsersFromProject(context,
                        destinationProjectOID.getPk(), dummyWorkstation.getOID());
                if (testList != null)
                {
                    for (Iterator iterator = testList.iterator(); iterator.hasNext();)
                    {
                        User aUser = (User) iterator.next();
                        addUserToProject(context,
                                destinationProjectOID.getPk(), dummyWorkstation.getOID(), aUser.getPk(),
                                User.ROLE_TESTER);
                    }
                }
                if (verifyList != null)
                {
                    for (Iterator iterator = verifyList.iterator(); iterator.hasNext();)
                    {
                        User aUser = (User) iterator.next();
                        addUserToProject(context,
                                destinationProjectOID.getPk(), dummyWorkstation.getOID(), aUser.getPk(),
                                User.ROLE_VERIFY);
                    }
                }
                if (readonlyList != null)
                {
                    for (Iterator iterator = readonlyList.iterator(); iterator.hasNext();)
                    {
                        User aUser = (User) iterator.next();
                        addReadonlyUserToProject(context,
                                destinationProjectOID.getPk(), dummyWorkstation.getOID(), aUser.getPk());
                    }
                }
            }

            if (copyProjectCoordinators)
            {
                List<User> existingMgrACList = accountService.getACLs((int) copyFromProjectOID.getPk(),
                        UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_MANAGER);
                List<User> existingReadonlyACList = accountService.getACLs((int) copyFromProjectOID.getPk(),
                        UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_READONLY);
                List<User> existingDataClerkACList = accountService.getACLs((int) copyFromProjectOID.getPk(),
                        UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);

                accountService.setUserPermissions((int) destinationProjectOID.getPk(),
                        new List[] { existingMgrACList, existingReadonlyACList, existingDataClerkACList },
                        new String[] { UserPerms.ROLE_MANAGER, UserPerms.ROLE_READONLY, UserPerms.ROLE_DATACLERK });
            }

            if (workstationsToCopy != null && workstationsToCopy.size() > 0)
            {
                removeAllWorkstationsFromProject(context, (int) destinationProjectOID.getPk());

                for (Iterator iterator = workstationsToCopy.iterator(); iterator.hasNext();)
                {
                    Object[] objects = (Object[]) iterator.next();
                    WorkstationOID wsOID = (WorkstationOID) objects[0];
                    Boolean copyForms = (Boolean) objects[1];
                    Boolean copyTeam = (Boolean) objects[2];

                    addWorkstationToProject(context, (int) destinationProjectOID.getPk(), wsOID);

                    if (copyForms != null && copyForms == true)
                    {
                        List<ProjectFormQuery> wsForms = projectService.getProjectFormsForProject((int) copyFromProjectOID.getPk(), wsOID);
                        for (Iterator iterator2 = wsForms.iterator(); iterator2.hasNext();)
                        {
                            ProjectFormQuery projectFormQuery = (ProjectFormQuery) iterator2.next();

                            ProjectPart sourcePart = (ProjectPart) persistWrapper.readByPrimaryKey(ProjectPart.class,
                                    projectFormQuery.getProjectPartPk());
                            ProjectPart destPart = projectPartMap.get(sourcePart);

                            ProjectForm newPForm = new ProjectForm();
                            newPForm.setAppliedByUserFk(context.getUser().getPk());
                            newPForm.setFormPk(projectFormQuery.getFormPk());
                            newPForm.setName(projectFormQuery.getName());
                            newPForm.setProjectPartPk(destPart.getPk());
                            newPForm.setProjectPk(destinationProjectOID.getPk());
                            newPForm.setWorkstationPk(wsOID.getPk());
                            persistWrapper.createEntity(newPForm);
                        }
                    }

                    if (copyTeam != null && copyTeam == true)
                    {
                        List<ProjectUserQuery> projectUsers = projectService.getProjectUserQueryList(copyFromProjectOID, wsOID);
                        for (Iterator iterator2 = projectUsers.iterator(); iterator2.hasNext();)
                        {
                            ProjectUserQuery projectUserQuery = (ProjectUserQuery) iterator2.next();

                            ProjectPart sourcePart = null;
                            ProjectPart destPart = null;
                            if (projectUserQuery.getProjectPartPk() != 0)
                            {
                                sourcePart = (ProjectPart) persistWrapper.readByPrimaryKey(ProjectPart.class,
                                        projectUserQuery.getProjectPartPk());
                                destPart = projectPartMap.get(sourcePart);
                            }

                            ProjectUser pUser = new ProjectUser();
                            if (destPart != null)
                                pUser.setProjectPartPk((int) destPart.getPk());
                            pUser.setProjectPk((int) destinationProjectOID.getPk());
                            pUser.setRole(projectUserQuery.getRole());
                            pUser.setUserPk(projectUserQuery.getUserPk());
                            pUser.setWorkstationPk((int) wsOID.getPk());
                            persistWrapper.createEntity(pUser);
                        }
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }
    public  List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID)
    {
        return persistWrapper.readList(ProjectWorkstation.class,
                "select * from TAB_PROJECT_WORKSTATIONS where projectPk = ?",
                projectOID.getPk());

    }



}
