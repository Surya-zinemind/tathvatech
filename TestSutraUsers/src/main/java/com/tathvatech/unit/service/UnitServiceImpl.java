package com.tathvatech.unit.service;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.entity.Project;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.project.service.ProjectTemplateManager;
import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.dao.UnitDAO;
import com.tathvatech.unit.dao.UnitInProjectDAO;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Account;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.service.WorkstationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{

    private final PersistWrapper persistWrapper;

    private final WorkstationService workstationService;
    
    private final ProjectTemplateManager projectTemplateManager;
    
    private final ProjectService projectService;
    private final CommonServiceManager commonServiceManager;

    public  List<UnitQuery> getUnitsBySerialNos(String[] serialNos, ProjectOID projectOID)
    {
        StringBuffer sb = new StringBuffer(UnitQuery.sql);

        List<Object> params = new ArrayList();
        params.add(projectOID.getPk());
        params.add(projectOID.getPk());
        params.add(projectOID.getPk());

        sb.append(" where 1 = 1 ");

        if (true)
        {
            String sep = " ";
            sb.append(" and u.serialNo in (");
            for (int i = 0; i < serialNos.length; i++)
            {
                sb.append(sep);
                sb.append("?");
                sep = ",";
            }
            sb.append(") ");
        }
        List<UnitQuery> units;
        try
        {
            units = persistWrapper.readList(UnitQuery.class, sb.toString(), serialNos, params.toArray());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            units = new ArrayList<>();
        }

        return units;
    }

    public  List<UnitQuery> getUnitsByPks(int[] unitPks, ProjectOID projectOID)
    {
        List<Object> params = new ArrayList();

        StringBuffer sb = new StringBuffer(UnitQuery.sql);
        params.add(projectOID.getPk());
        params.add(projectOID.getPk());
        params.add(projectOID.getPk());

        sb.append(" where 1 = 1 ");

        if (true)
        {
            String sep = " ";
            sb.append(" and u.pk in (");
            for (int i = 0; i < unitPks.length; i++)
            {
                sb.append(sep);
                sb.append("?");
                sep = ",";
            }
            sb.append(") ");
        }
        List<UnitQuery> units;
        try
        {
            units = persistWrapper.readList(UnitQuery.class, sb.toString(), unitPks);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            units = new ArrayList<>();
        }

        return units;
    }

    public  List<UnitQuery> getUnitsForProjectNew(UnitFilterBean unitFilter, UnitOID parent) throws Exception
    {
        UnitInProjectListReportRequest request = new UnitInProjectListReportRequest(unitFilter.getProjectoid());
        request.setShowRootUnitsOnly(unitFilter.getShowRootUnitsOnly());
        request.setPartNo(unitFilter.getPartNo());
        request.setProjectParts(unitFilter.getProjectParts());
        request.setSerialNo(unitFilter.getSerialNo());
        request.setShowProjectPartsAssignedOnly(unitFilter.getShowProjectPartsAssignedOnly());
        request.setSortOrder(unitFilter.getSortOrder());
        request.setUnitName(unitFilter.getUnitName());
        request.setUnitPks(unitFilter.getUnitPks());
        request.setUnitStatusInProject(unitFilter.getUnitStatus());
        request.setUnitsAtWorkstationOID(unitFilter.getUnitsAtWorkstationOID());
        request.setParentUnitOID(parent);

        UnitInProjectListReport report = new UnitInProjectListReport(request);
        return report.runReport();
    }

    public  UnitObj createUnit(UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit, boolean pendingReview)
            throws Exception
    {
        UnitDAO unitDAO = new UnitDAO();
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();

        Project project = (Project) persistWrapper.readByPrimaryKey(Project.class, projectPk);

        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        if (unitBean.getPartPk() == 0)
            throw new Exception(" Cannot create unit without specifying part no.");
        // if(unit.getSupplierFk() == 0)
        // throw new Exception("Cannot create unit without specifying
        // supplier");
        if (unitBean.getUnitOriginType() == null)
            throw new Exception("Please specify if the unit is Manufactured or Procured");


        UnitOID parentOID = null;
        if (unitBean.getParentPk() != null && unitBean.getParentPk() != 0)
            parentOID = new UnitOID(unitBean.getParentPk());

        boolean copyFormsConfig = !(boolean) commonServiceManager.getEntityPropertyValue(project.getOID(), ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
        boolean copyUsersConfig = !(boolean) commonServiceManager.getEntityPropertyValue(project.getOID(), ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);

        return createUnitInt(context, unitDAO, uprDAO, user, project, unitBean, parentOID,
                createAsPlannedUnit,
                copyUsersConfig, copyFormsConfig, false, null, pendingReview);
    }
    private  UnitObj createUnitInt(UserContext context, UnitDAO unitDAO, UnitInProjectDAO uprDAO, User user,
                                         Project project, UnitBean unitBean, UnitOID parentOID,
                                         boolean createAsPlannedUnit,
                                         boolean copyProjectWorkstationUsersToUnit,
                                         boolean copyProjectWorkstationFormsToUnit,
                                         boolean copyPartSpecificFormsToWorkstation,
                                         WorkstationOID workstationToCopyPartSpecificFormsTo,
                                         boolean pendingReview) throws Exception
    {
        // if a unit is already created as manufactured unit in any project, you
        // cannot use the createUnit flow to create another manufactured unit
        // with the same name,
        // you should use the addExistingUnitToProject flow to add that unit to
        // the project.

        // if you are trying to create a new Procured unit, you cannot use an
        // existing unit name.

        UnitObj unit = null;
        if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
        {
            // check if the unitName is taken by a unit under a parent under the same project which is marked as
            // Manufactured. We dont have to check it across all projects units etc. its too much pain to deal with such a checking.
            String manufacSql = "select count(*) from tab_unit u join tab_unit_h uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom  and uh.effectiveDateTo "
                    + " join unit_project_ref upr on upr.unitPk = u.pk and upr.unitOriginType = ? "
                    + " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
                    + " where upper(uh.unitName) = upper(?) ";
            Integer count = 0;
            if(parentOID == null)
            {
                manufacSql = manufacSql + " and uprh.parentPk is null ";
                count = persistWrapper.read(Integer.class, manufacSql, UnitOriginType.Manufactured.name(),
                        unitBean.getUnitName());
            }
            else
            {
                UnitInProjectObj parentUpr = UnitManager.getUnitInProject(parentOID, project.getOID());

                manufacSql = manufacSql + " and uprh.parentPk =  ? ";
                count = persistWrapper.read(Integer.class, manufacSql, UnitOriginType.Manufactured.name(),
                        unitBean.getUnitName(), parentUpr.getPk());
            }

            if (count > 0)
            {
                throw new AppException(
                        "Duplicate unit name:" + unitBean.getUnitName() + ", Please choose a different unit name.");
            }

            if (unitBean.getUnitName() == null || unitBean.getUnitName().trim().length() == 0)
                unitBean.setUnitName(unitBean.getSerialNo());

            unitBean.setProjectPk(project.getPk());
            unitBean.setCreatedBy(user.getPk());
            unitBean.setCreatedDate(new Date());
            if (true == createAsPlannedUnit)
                unitBean.setStatus(UnitInProject.STATUS_PLANNED);
            else
                unitBean.setStatus(UnitInProject.STATUS_OPEN);

            unit = new UnitObj(unitBean);
            if(pendingReview == true)
            {
                unit.setEstatus(EStatusEnum.PendingReview.getValue());
            }
            else
            {
                unit.setEstatus(EStatusEnum.Active.getValue());
            }
            unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.createUnit });
        }
        else if (UnitOriginType.Procured == unitBean.getUnitOriginType())
        {
            // check if the unitName is taken by a unit which is marked either
            // as Manufactured or Procured in any project.
            String manufacSql = "select count(*) from tab_unit u join tab_unit_h uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom  and uh.effectiveDateTo "
                    + " join unit_project_ref upr on upr.unitPk = u.pk " + " where upper(uh.unitName) = upper(?) ";
            Integer count = persistWrapper.read(Integer.class, manufacSql, unitBean.getUnitName());
            if (count > 0)
            {
                throw new AppException(
                        "Duplicate unit name:" + unitBean.getUnitName() + ", Please choose a different unit name.");
            }

            if (unitBean.getUnitName() == null || unitBean.getUnitName().trim().length() == 0)
                unitBean.setUnitName(unitBean.getSerialNo());

            unitBean.setProjectPk(project.getPk());
            unitBean.setCreatedBy(user.getPk());
            unitBean.setCreatedDate(new Date());

            unit = new UnitObj(unitBean);
            unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.createUnit });
        }

        if(unit == null)
        {
            throw new AppException("Unexpected error while creating a unit, Please contact support.");
        }

        unitBean.setPk(unit.getPk());
        unitBean.setCreatedBy(unit.getCreatedBy());
        unitBean.setCreatedDate(unit.getCreatedDate());
        unitBean.setLastUpdated(unit.getLastUpdated());

        // add unit to the project, create the project_unit_ref entry
        UnitInProjectObj pur = addUnitToProjectInt(context, uprDAO, unitBean, project.getOID(), parentOID,
                createAsPlannedUnit,
                copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit,
                copyPartSpecificFormsToWorkstation, workstationToCopyPartSpecificFormsTo);

        // create any children if
        List<UnitBean> children = unitBean.getChildren();
        if (children != null)
        {
            for (Iterator iterator = children.iterator(); iterator.hasNext();)
            {
                UnitBean cUnit = (UnitBean) iterator.next();
                createUnitInt(context, unitDAO, uprDAO, user, project, cUnit, unitBean.getOID(), createAsPlannedUnit,
                        copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit,
                        copyPartSpecificFormsToWorkstation, workstationToCopyPartSpecificFormsTo, pendingReview);
            }
        }

        return unit;
    }
    private  UnitInProjectObj addUnitToProjectInt(UserContext context, UnitInProjectDAO uprDAO, UnitBean unitBean,
                                                        ProjectOID projectOID, UnitOID parentOID, boolean createAsPlannedUnit,
                                                        boolean copyProjectWorkstationUsersToUnit, boolean copyProjectWorkstationFormsToUnit,
                                                        boolean copyPartSpecificFormsToWorkstation, WorkstationOID workstationToCopyPartSpecificFormsTo) throws Exception
    {
        UnitInProjectObj pur = new UnitInProjectObj();
        pur.setUnitPk(unitBean.getPk());
        pur.setProjectPk(projectOID.getPk());
        pur.setUnitOriginType(unitBean.getUnitOriginType().name());
        if (unitBean.getProjectPartPk() != null)
        {
            pur.setProjectPartPk(unitBean.getProjectPartPk());
        }
        pur.setCreatedBy(context.getUser().getPk());
        pur.setCreatedDate(new Date());

        if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
        {
            if (true == createAsPlannedUnit)
            {
                pur.setStatus(UnitInProject.STATUS_PLANNED);
                pur = uprDAO.saveUnitInProject(context, pur, new Actions[] { Actions.addUnitToProject });
            } else
            {
                pur.setStatus(UnitInProject.STATUS_OPEN);
                pur = uprDAO.saveUnitInProject(context, pur,
                        new Actions[] { Actions.addUnitToProject, Actions.openUnitInProject });
            }
        } else // procured units.
        {
            pur.setStatus(UnitInProject.STATUS_OPEN);
            pur = uprDAO.saveUnitInProject(context, pur, new Actions[] { Actions.addUnitToProject });
        }

        String heiCode = Base62Util.encode(pur.getPk());
        if (parentOID != null)
        {
            UnitInProjectObj parentUprObj = uprDAO.getUnitInProject(parentOID, projectOID);
            pur.setParentPk(parentUprObj.getPk());
            pur.setRootParentPk(parentUprObj.getRootParentPk());
            pur.setLevel(parentUprObj.getLevel() + 1);
            pur.setHeiCode(parentUprObj.getHeiCode() + "." + heiCode);
            pur.setHasChildren(false);

            if (parentUprObj.getHasChildren() == false && UnitOriginType.Manufactured == unitBean.getUnitOriginType())
            {
                // we are marking the parent unit as hasChildren = true only if
                // we are adding a Manufactured unit to its children.,
                // adding Producred to a parent are currently not treated as
                // adding children
                parentUprObj.setHasChildren(true);
                uprDAO.saveUnitInProject(context, parentUprObj);
            }
        } else
        {
            pur.setParentPk(null);
            pur.setRootParentPk(pur.getPk());
            pur.setLevel(0);
            pur.setHasChildren(false);
            pur.setHeiCode(heiCode);
        }
        pur.setOrderNo(pur.getPk());

        pur = uprDAO.saveUnitInProject(context, pur);

        // take care of the project level settings on the unit.
        if (createAsPlannedUnit == false && UnitOriginType.Manufactured == unitBean.getUnitOriginType())
        {
            // copy project workstations to unitworkstations
            setWorkstationsAndTeamsOnUnitOpen(context, projectOID, unitBean.getOID(), pur,
                    copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit);
        }

        if(copyPartSpecificFormsToWorkstation == true)
        {
            setPartSpecificSettingsToUnit(context, projectOID, unitBean.getOID(), pur, workstationToCopyPartSpecificFormsTo);
        }


        return pur;
    }
    private  void setWorkstationsAndTeamsOnUnitOpen(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                                          UnitInProjectObj unitInProject,
                                                          boolean copyProjectWorkstationUsersToUnit, boolean copyProjectWorkstationFormsToUnit) throws Exception
    {
        // the status is being changed from planned to open, so copy the
        // workstation, users, forms etc.
        // copy project workstations to unitworkstations
        List<WorkstationQuery> workstations = workstationService.getWorkstationsForProject(projectOID.getPk());
        for (Iterator iterator = workstations.iterator(); iterator.hasNext();)
        {
            if(copyProjectWorkstationFormsToUnit == false && copyProjectWorkstationUsersToUnit == false)
                continue;

            WorkstationQuery workstationQuery = (WorkstationQuery) iterator.next();

            // if workstation is already added, skip;
            UnitWorkstation uw = workstationService.getUnitWorkstationSetting(unitOID.getPk(), projectOID,
                    workstationQuery.getOID());
            if (uw != null)
                continue;

            // copy forms
            if (unitInProject.getProjectPartPk() == null)
                continue;

            List<ProjectFormQuery> pForms = projectTemplateManager.getTestProcsForProjectPart(projectOID,
                    new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());

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
            unitWorkstation.setUpdatedBy(context.getUser().getPk());
            unitWorkstation.setProjectPk(projectOID.getPk());
            unitWorkstation.setUnitPk(unitOID.getPk());
            unitWorkstation.setWorkstationPk(workstationQuery.getPk());
            int unitWorkstationPk = persistWrapper.createEntity(unitWorkstation);

            // copy projectUsers to unitusers
            if(copyProjectWorkstationUsersToUnit)
                copyProjectUsersToUnit(projectOID, unitOID, workstationQuery.getOID(), true);

            if(copyProjectWorkstationFormsToUnit)
                copyProjectFormsToUnit(context, projectOID, pForms, unitOID, workstationQuery.getOID());

            String defaultWorkstationStatusValue = (String) commonServiceManager.getEntityPropertyValue(projectOID,
                    ProjectPropertyEnum.SetNewWorkstationsDefaultStatusTo.getId(), String.class);
            if (defaultWorkstationStatusValue != null && (!(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue))))
            {
                if(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue) || UnitLocation.STATUS_IN_PROGRESS.equals(defaultWorkstationStatusValue)
                        || UnitLocation.STATUS_COMPLETED.equals(defaultWorkstationStatusValue))
                {
                    setUnitWorkstationStatus(context, unitOID.getPk(), projectOID, workstationQuery.getOID(),
                            defaultWorkstationStatusValue);
                }
                else
                {
                    throw new AppException("Default status value configured for workstation status is invalid. Please contact support");
                }
            }

        }
    }

    private  void setPartSpecificSettingsToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                                      UnitInProjectObj unitInProject, WorkstationOID workstationOID) throws Exception
    {
        // if workstation is already added, skip;
        UnitWorkstation uw = ProjectDelegate.getUnitWorkstationSetting(unitOID.getPk(), projectOID,
                workstationOID);

        if (uw != null)
            return;

        // copy forms
        if (unitInProject.getProjectPartPk() == null)
            return;

        List<ProjectFormQuery> pForms = getProjectPartAssignedForms(projectOID,
                new ProjectPartOID(unitInProject.getProjectPartPk()));

        // check if there are any users defined for that workstation
        List<ProjectUserQuery> pUsers = ProjectManager.getProjectUserQueryList(projectOID,
                new ProjectPartOID(unitInProject.getProjectPartPk()), workstationOID);


        // create unit workstation
        UnitWorkstation unitWorkstation = new UnitWorkstation();
        unitWorkstation.setEstatus(EStatusEnum.Active.getValue());
        unitWorkstation.setUpdatedBy(context.getUser().getPk());
        unitWorkstation.setProjectPk(projectOID.getPk());
        unitWorkstation.setUnitPk(unitOID.getPk());
        unitWorkstation.setWorkstationPk(workstationOID.getPk());
        int unitWorkstationPk = persistWrapper.createEntity(unitWorkstation);

        // copy projectUsers to unitusers
        copyProjectUsersToUnit(projectOID, unitOID, workstationOID, true);

        copyProjectFormsToUnit(context, projectOID, pForms, unitOID, workstationOID);

        setUnitWorkstationStatus(context, unitOID.getPk(), projectOID, workstationOID, UnitLocation.STATUS_IN_PROGRESS);

    }
    private  void copyProjectUsersToUnit(ProjectOID projectOID, UnitOID unitOID, WorkstationOID workstationOID,
                                               boolean copyDefaultTeam) throws Exception
    {
        UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

        List<User> pWorkstationTesters = getUsersForProjectPartInRole(projectOID.getPk(),
                new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_TESTER);
        if (copyDefaultTeam == true && (pWorkstationTesters == null || pWorkstationTesters.size() == 0))
            pWorkstationTesters = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_TESTER);
        List<User> uWorkstationTesters = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
                User.ROLE_TESTER);

        for (Iterator iterator2 = pWorkstationTesters.iterator(); iterator2.hasNext();)
        {
            User pUser = (User) iterator2.next();
            boolean containsNow = uWorkstationTesters.remove(pUser);
            if (!(containsNow))
            {
                UnitUser unitUser = new UnitUser();
                unitUser.setProjectPk(projectOID.getPk());
                unitUser.setUnitPk(unitOID.getPk());
                unitUser.setWorkstationPk(workstationOID.getPk());
                unitUser.setUserPk(pUser.getPk());
                unitUser.setRole(User.ROLE_TESTER);
                persistWrapper.createEntity(unitUser);
            }
        }
        for (Iterator iterator = uWorkstationTesters.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_TESTER);
        }

        List<User> pWorkstationVerifiers = getUsersForProjectPartInRole(projectOID.getPk(),
                new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_VERIFY);
        if (copyDefaultTeam == true && (pWorkstationVerifiers == null || pWorkstationVerifiers.size() == 0))
            pWorkstationVerifiers = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_VERIFY);
        List<User> uWorkstationVerifiers = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
                User.ROLE_VERIFY);

        for (Iterator iterator2 = pWorkstationVerifiers.iterator(); iterator2.hasNext();)
        {
            User pUser = (User) iterator2.next();
            boolean containsNow = uWorkstationVerifiers.remove(pUser);
            if (!(containsNow))
            {
                UnitUser unitUser = new UnitUser();
                unitUser.setProjectPk(projectOID.getPk());
                unitUser.setUnitPk(unitOID.getPk());
                unitUser.setWorkstationPk(workstationOID.getPk());
                unitUser.setUserPk(pUser.getPk());
                unitUser.setRole(User.ROLE_VERIFY);
                persistWrapper.createEntity(unitUser);
            }
        }
        for (Iterator iterator = uWorkstationVerifiers.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_VERIFY);
        }

        List<User> pWorkstationApprovers = getUsersForProjectPartInRole(projectOID.getPk(),
                new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_APPROVE);
        if (copyDefaultTeam == true && (pWorkstationApprovers == null || pWorkstationApprovers.size() == 0))
            pWorkstationApprovers = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_APPROVE);
        List<User> uWorkstationApprovers = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
                User.ROLE_APPROVE);

        for (Iterator iterator2 = pWorkstationApprovers.iterator(); iterator2.hasNext();)
        {
            User pUser = (User) iterator2.next();
            boolean containsNow = uWorkstationApprovers.remove(pUser);
            if (!(containsNow))
            {
                UnitUser unitUser = new UnitUser();
                unitUser.setProjectPk(projectOID.getPk());
                unitUser.setUnitPk(unitOID.getPk());
                unitUser.setWorkstationPk(workstationOID.getPk());
                unitUser.setUserPk(pUser.getPk());
                unitUser.setRole(User.ROLE_APPROVE);
                persistWrapper.createEntity(unitUser);
            }
        }
        for (Iterator iterator = uWorkstationApprovers.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_APPROVE);
        }

    }
    private  void copyProjectFormsToUnit(UserContext context, ProjectOID projectOID,
                                               List<ProjectFormQuery> projectTests, UnitOID unitOID, WorkstationOID workstationOID) throws Exception
    {
        // copy projectForms to unitforms
        for (Iterator iterator2 = projectTests.iterator(); iterator2.hasNext();)
        {
            ProjectFormQuery pForm = (ProjectFormQuery) iterator2.next();

            TestProcObj unitForm = new TestProcObj();
            unitForm.setProjectPk(projectOID.getPk());
            unitForm.setUnitPk(unitOID.getPk());
            unitForm.setWorkstationPk(workstationOID.getPk());
            unitForm.setFormPk(pForm.getFormPk());
            unitForm.setProjectTestProcPk(pForm.getPk());
            unitForm.setName(pForm.getName());
            unitForm.setAppliedByUserFk(pForm.getAppliedByUserFk());

            new TestProcDAO().saveTestProc(context, unitForm);
        }
    }
    public  void setUnitWorkstationStatus(UserContext userContext, int unitPk, ProjectOID projectOID,
                                                WorkstationOID workstationOID, String status) throws Exception
    {

        // create the new unit location on any change, so we have a history of
        // all activities.
        UnitLocation nuLoc = new UnitLocation();
        nuLoc.setProjectPk(projectOID.getPk());
        nuLoc.setMovedInBy(userContext.getUser().getPk());
        nuLoc.setMoveInDate(new Date());
        nuLoc.setUnitPk(unitPk);
        nuLoc.setWorkstationPk(workstationOID.getPk());
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
                ResponseMasterNew response = SurveyResponseDelegate
                        .getLatestResponseMasterForTest(new TestProcOID(testProc.getPk(), null));
                if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
                {
                    SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
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

                    ResponseMasterNew response = SurveyResponseManager
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
                        sResponse = SurveyResponseManager.ceateDummyResponse(userContext, sResponse);
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
                            SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
                                    ResponseMasterNew.STATUS_INPROGRESS);
                        }
                    }
                } else if (UnitLocation.STATUS_WAITING.equals(status))
                {
                    // if response is in inprogress status, we need to change it
                    // to paused status
                    ResponseMasterNew response = SurveyResponseDelegate
                            .getLatestResponseMasterForTest(unitFormQuery.getOID());
                    if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
                    {
                        SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
                                ResponseMasterNew.STATUS_PAUSED);
                    }
                }
            }
        }
    }
    public  UnitObj createUnitAtWorkstation(UserContext context, ProjectOID projectOID,
                                                  WorkstationOID workstationOID, UnitBean unitBean,
                                                  boolean copyPartSpecificFormsToWorkstation, boolean pendingReview)
            throws Exception
    {
        UnitDAO unitDAO = new UnitDAO();
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();

        Project project = persistWrapper.readByPrimaryKey(Project.class, projectOID.getPk());

        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        if (unitBean.getPartPk() == 0)
            throw new Exception(" Cannot create unit without specifying part no.");
        // if(unit.getSupplierFk() == 0)
        // throw new Exception("Cannot create unit without specifying
        // supplier");
        if (unitBean.getUnitOriginType() == null)
            throw new Exception("Please specify if the unit is Manufactured or Procured");

        UnitOID parentOID = null;
        if (unitBean.getParentPk() != null && unitBean.getParentPk() != 0)
            parentOID = new UnitOID(unitBean.getParentPk());

        boolean copyFormsConfig = !(boolean) commonServiceManager.getEntityPropertyValue(projectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
        boolean copyUsersConfig = !(boolean) commonServiceManager.getEntityPropertyValue(projectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);

        return createUnitInt(context, unitDAO, uprDAO, user, project, unitBean, parentOID, false,
                copyUsersConfig, copyFormsConfig, copyPartSpecificFormsToWorkstation, workstationOID, pendingReview);
    }
    public  UnitObj updateUnit(UserContext context, UnitObj unit) throws Exception
    {
        if (unit.getPk() == 0)
            throw new AppException("Invalid unit, Unit cannot be saved");

        // update
        if (isUnitNameExistForAnotherUnit(unit))
        {
            throw new AppException("Duplicate unit name, Please choose a different unit name.");
        }

        UnitDAO unitDAO = new UnitDAO();
        unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.updateUnit });
        // fetch the new project back
        return unit;
    }
    public  void updateUnit(UnitObj unit) throws Exception
    {
        if (isUnitNameExistForAnotherUnit(unit))
        {
            throw new AppException(
                    "Duplicate unit name:" + unit.getUnitName() + ", Please choose a different unit name.");
        }

        persistWrapper.update(unit);
    }

    public  boolean isUnitNameExistForAnotherUnit(UnitObj unit) throws Exception
    {
        List list = persistWrapper.readList(Unit.class, "select u.* from TAB_UNIT u "
                + " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
                + " where uh.unitName =?", unit.getUnitName());
        if (list.size() == 0)
        {
            return false;
        } else if (list.size() == 1 && ((Unit) list.get(0)).getPk() == unit.getPk())
        {
            return false;
        } else
        {
            return true;
        }
    }

    public UnitObj getUnitByPk(UnitOID unitOID)
    {
        return new UnitDAO().getUnit(unitOID.getPk());
    }

    public  UnitQuery getUnitQueryByPk(int unitPk, ProjectOID projectOID)
    {
        List<UnitQuery> list = null;
        try
        {
            list = persistWrapper.readList(UnitQuery.class, UnitQuery.sql + " where 1 = 1 and u.pk=?", projectOID.getPk(),
                    projectOID.getPk(), projectOID.getPk(), unitPk);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    /*
     * If we are adding a form to a workstation which is inProgress, we create the response right away.
     * if the workstation status is waiting, we dont have to create the response. it will get created when the workstation is made InProgress
     * But if the form is added from tablet, we need the response created right away. so we call this function
     * with forceResponseCreation = true from the tablet.
     */
    public  TestProcOID addFormToUnit(UserContext context, ProjectForm projectTestProc, int unitPk,
                                            ProjectOID projectOID, WorkstationOID workstationOID, int formPk,
                                            String testName, boolean makeWorkstationInProgress, boolean reviewPending) throws Exception
    {
        Survey form = SurveyMaster.getSurveyByPk(formPk);
        if(form == null)
            throw new AppException("Invalid form");
        if(!(Survey.STATUS_OPEN.equals(form.getStatus())))
            throw new AppException("Form is not published, Please publish the form and try again");

        UnitWorkstation uw = workstationService.getUnitWorkstationSetting(unitPk, projectOID, workstationOID);
        if(uw == null || uw.getPk() == 0) //this means the workstation should be added to the unit.
        {
            uw = addWorkstationToUnit(context, projectOID, new UnitOID(unitPk), workstationOID);

            // copy projectUsers to unitusers
            copyProjectUsersToUnit(projectOID, new UnitOID(unitPk), workstationOID, true);


            // if the user who added the form is not a tester on that workstaton for that unit, add him.
            List<User> uWorkstationTesters = getUsersForUnitInRole(unitPk, projectOID, workstationOID, User.ROLE_TESTER);
            if(!(uWorkstationTesters.contains(context.getUser())))
            {
                UnitUser unitUser = new UnitUser();
                unitUser.setProjectPk(projectOID.getPk());
                unitUser.setUnitPk(unitPk);
                unitUser.setWorkstationPk(workstationOID.getPk());
                unitUser.setUserPk(context.getUser().getPk());
                unitUser.setRole(User.ROLE_TESTER);
                persistWrapper.createEntity(unitUser);
            }
        }

        String currentWsStatus = null;
        UnitLocationQuery uLoc = workstationService.getUnitWorkstationStatus(new UnitOID(uw.getUnitPk(), null), projectOID, workstationOID);
        if(uLoc != null)
            currentWsStatus = uLoc.getStatus();
        if(UnitLocation.STATUS_COMPLETED.equals(currentWsStatus))
        {
            throw new AppException("Workstation is marked as complete for unit, cannot add new form");
        }

        boolean mandatoryTestNameProperty = (boolean) commonServiceManager.getEntityPropertyValue(projectOID,
                ProjectPropertyEnum.MakeTestNamesForChecksheetsMandatory.getId(), Boolean.class);
        if (mandatoryTestNameProperty == true && (testName == null || testName.trim().length() == 0))
        {
            throw new AppException("Test name is mandatory for this project, Please provide a Test Name and try again");
        }

        if (testName == null || testName.trim().length() == 0)
        {
            int count = persistWrapper.read(Integer.class, "select count(*) from unit_testproc ut "
                            + " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
                            + " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
                            + " where uth.unitPk=? and uth.projectPk = ? and uth.workstationPk=? and tfa.formFk=? and (uth.name is null or name = '') ",
                    unitPk, projectOID.getPk(), workstationOID.getPk(), formPk);
            if (count > 0)
                throw new AppException(
                        "Form already added with empty test name, you should specify a test name to add it again");
        } else
        {
            int count = persistWrapper.read(Integer.class, "select count(*) from unit_testproc ut "
                            + " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
                            + " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
                            + " where uth.unitPk=? and uth.projectPk = ? and uth.workstationPk=? and tfa.formFk=? and uth.name = ? ",
                    unitPk, projectOID.getPk(), workstationOID.getPk(), formPk, testName.trim());
            if (count > 0)
                throw new AppException(
                        "Form already added with the test name, you should specify a different test name to add it again");
        }

        TestProcObj uForm = new TestProcObj();
        uForm.setProjectPk(projectOID.getPk());
        uForm.setUnitPk(unitPk);
        uForm.setWorkstationPk(workstationOID.getPk());
        uForm.setFormPk(formPk);
        uForm.setName(testName);

        if (projectTestProc != null)// null will happen when it is an oil form
        // ::TODO change when new oil impl is in
        // force.
        {
            uForm.setProjectTestProcPk(projectTestProc.getPk());
        }
        uForm.setAppliedByUserFk(context.getUser().getPk());

        if(reviewPending == true)
            uForm.setEstatus(EStatusEnum.PendingReview.getValue());
        else
            uForm.setEstatus(EStatusEnum.Active.getValue());

        TestProcObj obj = new TestProcDAO().saveTestProc(context, uForm);

        ResponseMasterNew response = null;
        if(UnitLocation.STATUS_IN_PROGRESS.equals(currentWsStatus))
        {
            //create the dummy response entry
            response = SurveyResponseManager.getLatestResponseMasterForTest(obj.getOID());
            if (response == null)
            {
                SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(obj.getFormPk(), null));
                SurveyResponse sResponse = new SurveyResponse(sd);
                sResponse.setSurveyPk(obj.getFormPk());
                sResponse.setTestProcPk(obj.getPk());
                sResponse.setResponseStartTime(new Date());
                sResponse.setResponseCompleteTime(new Date());
                // ipaddress and mode set
                sResponse.setIpaddress("0.0.0.0");
                sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
                sResponse.setUser((User) context.getUser());
                sResponse = SurveyResponseManager.ceateDummyResponse(context, sResponse);
                response = SurveyResponseManager.getResponseMaster(sResponse.getResponseId());
            }
        }
        else
        {
            if(makeWorkstationInProgress == true)
            {
                setUnitWorkstationStatus(context, unitPk, projectOID, workstationOID, UnitLocation.STATUS_IN_PROGRESS);
            }
        }

        return obj.getOID();
    }

    public  void removeAllFormsFromUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                              WorkstationOID workstationOID) throws Exception
    {
        new TestProcDAO().deleteAllTestProcsMatching(context, new UnitOID(unitPk), projectOID, workstationOID);
    }

    public  void deleteTestProcFromUnit(UserContext context, TestProcOID testProcOID) throws Exception
    {
        // see if there are any responses for that form
        ResponseMasterNew respM = SurveyResponseManager.getLatestResponseMasterForTest(testProcOID);
        if (respM != null
                && (ResponseMasterNew.STATUS_APPROVED.equals(respM.getStatus()) || ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS.equals(respM.getStatus()))
        )
        {
            throw new FormApprovedException("The form is already approved, It cannot be removed.");
        }

        new TestProcDAO().deleteTestProc(context, testProcOID);
    }
    public  List<FormQuery> getAllFormsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception
    {
        FormFilter filter = new FormFilter();
        filter.setStatus(new String[] { Survey.STATUS_OPEN });
        filter.setShowAllFormRevisions(true);
        filter.setUnitFormFilter(filter.new UnitFormAssignmentFilter(unitOID, projectOID, null));
        ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
        req.setFilter(filter);
        req.setFetchRowCount(false);
        req.setFetchAllRows(true);
        ReportResponse response = new FormListReport().runReport(req);
        List<FormQuery> list = (List<FormQuery>) response.getReportData();
        if (list != null)
            return list;

        return new ArrayList();
    }

    public  List<FormQuery> getFormsForUnit(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception
    {
        FormFilter filter = new FormFilter();
        filter.setStatus(new String[] { Survey.STATUS_OPEN });
        filter.setShowAllFormRevisions(true);
        filter.setUnitFormFilter(filter.new UnitFormAssignmentFilter(unitOID, projectOID, workstationOID));
        ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
        req.setFilter(filter);
        req.setFetchRowCount(false);
        req.setFetchAllRows(true);
        ReportResponse response = new FormListReport().runReport(req);
        List<FormQuery> list = (List<FormQuery>) response.getReportData();
        if (list != null)
            return list;

        return new ArrayList();
    }

    public  List<User> getUsersForUnit(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception
    {
        return persistWrapper.readList(User.class,
                "select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? order by firstName",
                unitPk, projectOID.getPk(), workstationOID.getPk());
    }

    public  List<User> getUsersForUnitInRole(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                                   String roleName) throws Exception
    {
        return persistWrapper.readList(User.class,
                "select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? and uu.role=?",
                unitPk, projectOID.getPk(), workstationOID.getPk(), roleName);
    }
    public  boolean isUsersForUnitInRole(int userPk, int unitPk, ProjectOID projectOID,
                                               WorkstationOID workstationOID, String roleName)
    {
        List list = null;
        try
        {
            list = persistWrapper.readList(User.class,
                    "select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and u.pk = ? and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? and uu.role=?",
                    userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), roleName);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (list != null && list.size() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public  void removeAllUsersFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                              WorkstationOID workstationOID) throws Exception
    {
        persistWrapper.delete("delete from TAB_UNIT_USERS where unitPk=? and projectPk = ? and workstationPk=?",
                unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());
    }
    public  void addTesterToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                       WorkstationOID workstationOID, int userPk) throws Exception
    {
        // check if the it is already there..
        UnitUser uu = persistWrapper.read(UnitUser.class,
                "select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
                userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_TESTER);
        if (uu == null)
        {
            UnitUser pUser = new UnitUser();
            pUser.setProjectPk(projectOID.getPk());
            pUser.setUnitPk(unitPk);
            pUser.setWorkstationPk(workstationOID.getPk());
            pUser.setUserPk(userPk);
            pUser.setRole(User.ROLE_TESTER);

            persistWrapper.createEntity(pUser);
        }
    }

    public  void addVerifierToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                         WorkstationOID workstationOID, int userPk) throws Exception
    {
        // check if the it is already there..
        UnitUser uu = persistWrapper.read(UnitUser.class,
                "select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
                userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_VERIFY);
        if (uu == null)
        {
            UnitUser pUser = new UnitUser();
            pUser.setProjectPk(projectOID.getPk());
            pUser.setUnitPk(unitPk);
            pUser.setWorkstationPk(workstationOID.getPk());
            pUser.setUserPk(userPk);
            pUser.setRole(User.ROLE_VERIFY);

            persistWrapper.createEntity(pUser);
        }
    }

    public  void addApproverToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                         WorkstationOID workstationOID, int userPk) throws Exception
    {
        // check if the it is already there..
        UnitUser uu = persistWrapper.read(UnitUser.class,
                "select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
                userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_APPROVE);
        if (uu == null)
        {
            UnitUser pUser = new UnitUser();
            pUser.setProjectPk(projectOID.getPk());
            pUser.setUnitPk(unitPk);
            pUser.setWorkstationPk(workstationOID.getPk());
            pUser.setUserPk(userPk);
            pUser.setRole(User.ROLE_APPROVE);

            persistWrapper.createEntity(pUser);
        }
    }

    public  void addReadonlyUserToUnit(UserContext context, int unitPk, ProjectOID projectOID,
                                             WorkstationOID workstationOID, int userPk) throws Exception
    {
        // check if the it is already there..
        UnitUser uu = persistWrapper.read(UnitUser.class,
                "select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
                userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_READONLY);
        if (uu == null)
        {
            UnitUser pUser = new UnitUser();
            pUser.setProjectPk(projectOID.getPk());
            pUser.setUnitPk(unitPk);
            pUser.setWorkstationPk(workstationOID.getPk());
            pUser.setUserPk(userPk);
            pUser.setRole(User.ROLE_READONLY);

            persistWrapper.createEntity(pUser);
        }
    }
    public  List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID)
            throws Exception
    {
        return getUnitLocationHistory(unitOID, projectOID, false);
    }

    public  List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID,
                                                                 boolean includeChildUnits) throws Exception
    {
        if (includeChildUnits)
        {
            UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

            String sql = UnitLocationQuery.fetchSQL + " and uloc.unitPk in (select pk from TAB_UNIT where "
                    + " ( (pk = ?) or (rootParentPk = ? and heiCode like ?) ) " + " ) "
                    + "and uloc.projectPk = ? and uloc.current = 1";
            return persistWrapper.readList(UnitLocationQuery.class, sql, unitInProject.getUnitPk(),
                    unitInProject.getRootParentPk(), unitInProject.getHeiCode() + ".%", projectOID.getPk());
        } else
        {
            String sql = UnitLocationQuery.fetchSQL
                    + " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.current = 1";
            return persistWrapper.readList(UnitLocationQuery.class, sql, unitOID.getPk(), projectOID.getPk());
        }
    }
    public  void removeUnitFromProject(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                             DeleteOptionEnum deleteUnitOption) throws Exception
    {
        // I have to check in the Response_desc table to see if there are any
        // entries made..
        // this is because as soon as a workstation is activated, a dummy
        // response is entered into TAB_RESPONSE
        // to coordinate save/submit between tablets..
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();
        UnitInProjectObj uprToDelete = uprDAO.getUnitInProject(unitOID, projectOID);
        if (DeleteOptionEnum.MoveChildrenToRoot == deleteUnitOption)
        {
            List<UnitInProjectObj> childs = uprDAO.getDirectChildren(uprToDelete.getOID());
            for (Iterator iterator = childs.iterator(); iterator.hasNext();)
            {
                UnitInProjectObj upr = (UnitInProjectObj) iterator.next();
                UnitManager.changeUnitParent(context, null, new UnitOID(upr.getUnitPk()), projectOID);
            }
        } else if (DeleteOptionEnum.DeleteTree == deleteUnitOption)
        {
            List<UnitInProjectObj> childs = uprDAO.getAllChildrenInTree(uprToDelete.getOID());
            for (int i = 0; i < childs.size(); i++)
            {
                removeUnitFromProjectInt(context, childs.get(i));
            }
        }
        removeUnitFromProjectInt(context, uprToDelete);

        // check of the unit is part of any other project. If not we rename the
        // unit with a Del marker
        // this way the unitName can be re-used. users could create a new unit
        // with improper settings, then delete it and try to re-create it.
        List<ProjectQuery> assignedProjects = UnitManager.getUnitAssignedProjects(new UnitOID(uprToDelete.getUnitPk()));
        if (assignedProjects.size() == 1 && assignedProjects.get(0).getPk() == projectOID.getPk())
        {
            // this means the unit is assigned only to this project

            List<UnitH> unitHRecords = persistWrapper.readList(UnitH.class,
                    "select * from tab_unit_h where unitPk = ? ", uprToDelete.getUnitPk());
            for (Iterator iterator = unitHRecords.iterator(); iterator.hasNext();)
            {
                UnitH unitH = (UnitH) iterator.next();
                unitH.setUnitName(unitH.getUnitName() + "-Del-" + uprToDelete.getUnitPk());
                unitH.setSerialNo(unitH.getSerialNo() + "-Del-" + uprToDelete.getUnitPk());
                persistWrapper.update(unitH);
            }
        }

    }

    private  void removeUnitFromProjectInt(UserContext context, UnitInProjectObj unitInProjectObj)
            throws Exception
    {
        int count = persistWrapper.read(Integer.class,
                "select count(*) from TAB_RESPONSE r " + " join TAB_UNIT_FORMS uf on r.testProcPk = uf.pk " + " where "
                        + " r.status in (?,?,?,?,?,?) and " + " uf.unitPk=? and uf.projectPk = ? and uf.estatus = 1",
                ResponseMasterNew.STATUS_COMPLETE, ResponseMasterNew.STATUS_VERIFIED,
                ResponseMasterNew.STATUS_APPROVED, ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS,
                ResponseMasterNew.STATUS_PAUSED, ResponseMasterNew.STATUS_REJECTED, unitInProjectObj.getUnitPk(),
                unitInProjectObj.getProjectPk());
        if (count > 0)
            throw new AppException("Forms have been submitted for this unit, Unit cannot be deleted");

        count = persistWrapper.read(Integer.class, "select count(*) from openitem_v2 where unitPk=? and projectPk = ?",
                unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
        if (count > 0)
            throw new AppException("Open items have been created for this unit, Unit cannot be deleted");

        count = persistWrapper.read(Integer.class,
                "select count(*) from ncr_unit_assign nua where nua.unitFk=? and nua.projectFk = ?",
                unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
        if (count > 0)
            throw new AppException("NCRs have been associated to this unit, Unit cannot be deleted");

        count = persistWrapper.read(Integer.class, "select count(*) from andon where unitPk=? and projectPk = ?",
                unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
        if (count > 0)
            throw new AppException("Andons have been raised for this unit, Unit cannot be deleted");

        persistWrapper.delete("delete from TAB_UNIT_USERS where unitPk=? and projectPk = ?",
                unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
        new UnitInProjectDAO().removeUnit(context, unitInProjectObj);
    }
    public  void openUnit(UserContext context, UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList,
                                ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID) throws Exception
    {
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();

        HashMap<UnitOID, UnitBean> unitBeanMap = new HashMap<>();
        for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
        {
            UnitBean unitBean = (UnitBean) iterator.next();
            unitBeanMap.put(unitBean.getOID(), unitBean);
        }

        // build the new UnitBeanHeirarchy from the unitBeanList
        List<UnitBean> rootBeanList = new ArrayList<>();
        for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
        {
            UnitBean unitBean = (UnitBean) iterator.next();

            UnitBean parentBean = null;
            if (unitBean.getParentPk() != null)
            {
                parentBean = unitBeanMap.get(new UnitOID(unitBean.getParentPk()));
            }
            if (parentBean == null)
            {
                rootBeanList.add(unitBean);
            } else
            {
                unitBean.setParent(parentBean);
                if (parentBean.getChildren() == null)
                    parentBean.setChildren(new ArrayList<UnitBean>());
                parentBean.getChildren().add(unitBean);
            }
        }
        if (rootBeanList.size() != 1)
        {
            throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
        }

        List<UnitQuery> destProjectUnitList = UnitManager.getAllChildrenUnitsRecursive(rootUnitToOpen.getOID(),
                destinationProjectOID);

        openUnitRec(context, uprDAO, rootUnitToOpen, unitBeanMap, lastOpenProjectOID, destinationProjectOID,
                destProjectUnitList);

        // what ever is remaining in the destProjectUnitList are the ones that
        // we should move to root.
        // find the parents of the unit trees which should be moved as root
        // units in project.
        // add the unitLists to a hashmap for easy retrieval
        HashMap<UnitOID, UnitQuery> unitMap = new HashMap<>();
        for (Iterator iterator = destProjectUnitList.iterator(); iterator.hasNext();)
        {
            UnitQuery unitQuery = (UnitQuery) iterator.next();
            unitMap.put(unitQuery.getUnitOID(), unitQuery);
        }

        List<UnitOID> roots = new ArrayList<>();
        for (Iterator iterator = destProjectUnitList.iterator(); iterator.hasNext();)
        {
            UnitQuery unitQ = (UnitQuery) iterator.next();
            UnitQuery parentQ = null;
            if (unitQ.getParentPk() != null)
            {
                parentQ = unitMap.get(new UnitOID(unitQ.getParentPk()));
            }
            if (parentQ == null)
            {
                roots.add(unitQ.getUnitOID());
            }
        }

        for (Iterator iterator = roots.iterator(); iterator.hasNext();)
        {
            UnitOID unitOID = (UnitOID) iterator.next();
            UnitManager.changeUnitParent(context, null, unitOID, destinationProjectOID);
        }

    }
    private  void openUnitRec(UserContext context, UnitInProjectDAO uprDAO, UnitBean unitBean,
                                    HashMap<UnitOID, UnitBean> unitBeanMap, ProjectOID srcProjectOID, ProjectOID destProjectOID,
                                    List<UnitQuery> destProjectUnitList) throws Exception
    {
        // set the projectPartFk which is mapped by the user into the unitBean
        UnitBean mappedBean = unitBeanMap.get(unitBean.getOID());
        if (mappedBean != null)
        {
            unitBean.setProjectPartPk(mappedBean.getProjectPartPk());
        }

        // remove this unit from the distinationUnitList
        UnitQuery toRemoveCheck = new UnitQuery();
        toRemoveCheck.setUnitPk(unitBean.getPk());
        toRemoveCheck.setProjectPk(destProjectOID.getPk());
        destProjectUnitList.remove(toRemoveCheck);

        // if the unit is not part of the desination project, we need to add
        // this unit to the project.
        UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unitBean.getOID(), destProjectOID);
        if (unitInProject == null)
        {
            boolean copyFormConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
            boolean copyUserConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);

            // add unit to the project, create the project_unit_ref entry
            unitInProject = addUnitToProjectInt(context, uprDAO, unitBean, destProjectOID,
                    new UnitOID(unitBean.getParentPk()), false, copyUserConfig, copyFormConfig, false, null);
        }
        else
        {
            // check if we need to update the projectPart value
            if (unitInProject.getProjectPartPk() == null && unitBean.getProjectPartPk() != null)
            {
                unitInProject.setProjectPartPk(unitBean.getProjectPartPk());
                uprDAO.saveUnitInProject(context, unitInProject, new Actions[] { Actions.updateUnit });
                unitInProject = uprDAO.getUnitInProject(unitBean.getOID(), destProjectOID);
            }
        }

        if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
        {
            boolean isUnitOpenInAnyProject = UnitManager.isUnitOpenInAnyProjects(unitBean.getOID());
            if (isUnitOpenInAnyProject)
            {
                Project project = getProjectWhereUnitIsOpen(unitBean.getOID());
                if (project.getPk() != destProjectOID.getPk())
                    throw new AppException(
                            String.format("Unit %s is open in project %s, Unit cannot be opened in this project",
                                    unitBean.getOID().getDisplayText(), project.getDisplayString()));
            }
            if (UnitInProject.STATUS_PLANNED.equals(unitInProject.getStatus()))
            {
                // the status is being changed from planned to open, so copy the
                // workstation, users, forms etc.
                // copy project workstations to unitworkstations
                boolean copyFormConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
                boolean copyUserConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);
                setWorkstationsAndTeamsOnUnitOpen(context, destProjectOID, unitBean.getOID(), unitInProject, copyUserConfig, copyFormConfig);

            }
        }

        if (!(UnitInProject.STATUS_OPEN.equals(unitInProject.getStatus())))
        {
            unitInProject.setStatus(UnitInProject.STATUS_OPEN);
            uprDAO.saveUnitInProject(context, unitInProject, new Actions[] { Actions.openUnitInProject });
        }

        // now we have to open all chileren units of this unit
        List<UnitQuery> children = UnitManager.getChildrenUnits(srcProjectOID, unitBean.getOID());
        List<UnitBean> childrenUnits = new ArrayList<UnitBean>();
        for (Iterator iterator = children.iterator(); iterator.hasNext();)
        {
            UnitQuery unitQuery = (UnitQuery) iterator.next();
            UnitBean aBean = unitQuery.getUnitBean();
            aBean.setProjectPk(destProjectOID.getPk());
            childrenUnits.add(aBean);

        }
        if (childrenUnits != null)
        {
            for (Iterator iterator = childrenUnits.iterator(); iterator.hasNext();)
            {
                UnitBean aUnit = (UnitBean) iterator.next();
                openUnitRec(context, uprDAO, aUnit, unitBeanMap, srcProjectOID, destProjectOID, destProjectUnitList);
            }
        }
    }
    public  void removeUserFromUnit(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                          String role) throws Exception
    {
        // I think we need not do any check for this.. the user was there
        // earlier.. but
        // now he needs to be removed.. so he does not have access moving
        // forward. i think this is ok

        UnitUser uu = persistWrapper.read(UnitUser.class,
                "select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
                userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), role);
        persistWrapper.deleteEntity(uu);
    }
    public  float getUnitPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
                                               boolean includeChildren) throws Exception
    {
        return getWorkstationPercentCompleteInt(context, unitPk, projectOID, null, includeChildren);
    }\public  HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren)
    {
        HashMap<ProjectOID, Integer> result = new HashMap<ProjectOID, Integer>();
        List<Object> params = new ArrayList<Object>();

        StringBuffer sql = new StringBuffer(
                "select upr.projectPk as projectPk, project.projectName as projectName, project.projectDescription as projectDescription, count(*) as unitCount from TAB_UNIT u ");
        sql.append(" join unit_project_ref upr on upr.unitPk = u.pk ");
        if (projectPks != null && projectPks.size() > 0)
        {
            sql.append(" and upr.projectPk in (");

            String ssep = "";
            for (Integer pk : projectPks)
            {
                sql.append(ssep).append("?");
                ssep = ",";
                params.add(pk);
            }
            sql.append(") ");
        }
        sql.append(" join TAB_PROJECT project on project.pk=upr.projectPk");
        sql.append(
                " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo");
        sql.append(" where upr.unitOriginType = ? and uprh.status != 'Removed' ");
        params.add(UnitOriginType.Manufactured.name());
        if (!includeChildren)
        {
            sql.append(" and uprh.parentPk is null ");
        }
        sql.append("group by upr.projectPk");
        List<Map<String, Object>> fetchedData = persistWrapper.readListAsMap(sql.toString(), params.toArray());
        if (fetchedData != null)
        {
            for (Map<String, Object> mapData : fetchedData)
            {
                int projPk = (int) mapData.get("projectpk");
                String projectName = (String) mapData.get("projectname");
                String projectDescription = (String) mapData.get("projectdescription");
                long unitCount = (long) mapData.get("unitcount");
                result.put(
                        new ProjectOID(projPk,
                                projectName + ((projectDescription != null) ? (" - " + projectDescription) : "")),
                        (int) unitCount);
            }
        }
        return result;

    }

    public  int getUnitCount(int projectPk, boolean includeChildren) throws Exception
    {
        if (includeChildren)
        {
            return persistWrapper.read(Integer.class, "select count(*) from TAB_UNIT u "
                            + " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ? "
                            + " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo"
                            + " where upr.unitOriginType = ? and uprh.status != 'Removed' ", projectPk,
                    UnitOriginType.Manufactured.name());
        } else
        {
            // look for top level units only
            return persistWrapper.read(Integer.class, "select count(*) from TAB_UNIT u "
                            + " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ? "
                            + " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
                            + " where upr.unitOriginType = ?  and uprh.status != 'Removed' and uprh.parentPk is null",
                    projectPk, UnitOriginType.Manufactured.name());
        }
    }
    public  List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(UnitQuery.class, UnitQuery.sql
                            + " where 1 = 1 and u.pk in (select unitPk from TAB_UNIT_WORKSTATIONS where projectPk = ? and workstationPk = ?)",
                    projectOID.getPk(), projectOID.getPk(), projectOID.getPk(), projectOID.getPk(),
                    workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                                   WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(UnitQuery.class, UnitQuery.sql
                            + " where 1 = 1 and uprh.projectPartPk = ? and u.pk in (select unitPk from TAB_UNIT_WORKSTATIONS where projectPk = ? and workstationPk = ?)",
                    projectOID.getPk(), projectOID.getPk(), projectOID.getPk(), projectPartOID.getPk(),
                    projectOID.getPk(), workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public  void addUnitToProject(UserContext context, ProjectOID sourceProjectOID,
                                        ProjectOID destinationProjectOID, UnitBean rootUnitBean, List<UnitBean> unitBeanAndChildrenList,
                                        boolean addAsPlannedUnit) throws Exception
    {
        UnitInProjectDAO uprDAO = new UnitInProjectDAO();

        HashMap<UnitOID, UnitBean> unitBeanMap = new HashMap<>();
        for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
        {
            UnitBean unitBean = (UnitBean) iterator.next();
            unitBeanMap.put(unitBean.getOID(), unitBean);
        }

        // build the new UnitBeanHeirarchy from the unitBeanList
        List<UnitBean> rootBeanList = new ArrayList<>();
        for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
        {
            UnitBean unitBean = (UnitBean) iterator.next();

            UnitBean parentBean = null;
            if (unitBean.getParentPk() != null)
            {
                parentBean = unitBeanMap.get(new UnitOID(unitBean.getParentPk()));
            }
            if (parentBean == null)
            {
                rootBeanList.add(unitBean);
            } else
            {
                unitBean.setParent(parentBean);
                if (parentBean.getChildren() == null)
                    parentBean.setChildren(new ArrayList<UnitBean>());
                parentBean.getChildren().add(unitBean);
            }
        }
        if (rootBeanList.size() != 1)
        {
            throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
        }
        if (rootUnitBean.getPk() != rootBeanList.get(0).getPk())
        {
            throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
        }

        boolean copyFormConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destinationProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
        boolean copyUserConfig = !(boolean) commonServiceManager.getEntityPropertyValue(destinationProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);
        addUnitToProjectRec(context, uprDAO, sourceProjectOID, destinationProjectOID, rootUnitBean, unitBeanMap,
                addAsPlannedUnit, copyUserConfig, copyFormConfig);
    }

    private  void addUnitToProjectRec(UserContext context, UnitInProjectDAO uprDAO, ProjectOID sourceProjectOID,
                                            ProjectOID destinationProjectOID, UnitBean unitBeanToAdd, HashMap<UnitOID, UnitBean> unitBeanMap,
                                            boolean addAsPlannedUnit, boolean copyUserConfig, boolean copyFormConfig) throws Exception
    {
        // set the projectPartFk which is mapped by the user into the unitBean
        UnitBean mappedBean = unitBeanMap.get(unitBeanToAdd.getOID());
        if (mappedBean != null)
        {
            unitBeanToAdd.setProjectPartPk(mappedBean.getProjectPartPk());
            unitBeanToAdd.setUnitOriginType(mappedBean.getUnitOriginType());
        }

        // check if the unit is already associated with this project, in that
        // case. we need to change the
        // status and then move it under the parent as moved from the source
        // project.
        UnitInProjectObj uprObj = UnitManager.getUnitInProject(unitBeanToAdd.getOID(), destinationProjectOID);
        if (uprObj != null)
        {
            uprObj.setProjectPartPk(unitBeanToAdd.getProjectPartPk());
            uprObj.setUnitOriginType(unitBeanToAdd.getUnitOriginType().name());

            /*
             * change the status of the unit inside the project to open or
             * planned and then call the change parent method on that unit so
             * that its current children are also properly moved along with this
             * unit.
             */
            if (addAsPlannedUnit == false || unitBeanToAdd.getUnitOriginType() == UnitOriginType.Procured)
                uprObj.setStatus(UnitInProject.STATUS_OPEN);
            else
                uprObj.setStatus(UnitInProject.STATUS_PLANNED);

            uprDAO.saveUnitInProject(context, uprObj, new Actions[] { Actions.addUnitToProject });

            UnitOID parentUnitOID = null;
            if (unitBeanToAdd.getParentPk() != null)
                parentUnitOID = new UnitOID(unitBeanToAdd.getParentPk());
            UnitManager.changeUnitParent(context, parentUnitOID, unitBeanToAdd.getOID(), destinationProjectOID);
        } else
        {
            ProjectPart destProjectPart = null;
            if (UnitOriginType.Manufactured == unitBeanToAdd.getUnitOriginType())
            {
                if (unitBeanToAdd.getProjectPartPk() == null || unitBeanToAdd.getProjectPartPk() == 0)
                    throw new AppException("Invalid project part mapping specified");

                destProjectPart = (ProjectPart) commonServiceManager.getObjectByPk(ProjectPart.class,
                        unitBeanToAdd.getProjectPartPk());
                if (destProjectPart == null || destProjectPart.getProjectPk() != destinationProjectOID.getPk()
                        || destProjectPart.getPartPk() != unitBeanToAdd.getPartPk())
                    throw new AppException("Invalid project part mapping specified");
            }

            // add unit to the project, create the project_unit_ref entry
            UnitOID parentUnitOID = null;
            if (unitBeanToAdd.getParentPk() != null)
                parentUnitOID = new UnitOID(unitBeanToAdd.getParentPk());

            UnitInProjectObj pur = addUnitToProjectInt(context, uprDAO, unitBeanToAdd, destinationProjectOID,
                    parentUnitOID, addAsPlannedUnit, copyUserConfig, copyFormConfig, false, null);

            // take care of the project level settings on the unit.
            if (destProjectPart != null && addAsPlannedUnit == false)
            {
                // copy project workstations to unitworkstations
                setWorkstationsAndTeamsOnUnitOpen(context, destinationProjectOID, unitBeanToAdd.getOID(), pur, copyUserConfig, copyFormConfig);
            }

        }

        // process the children units of this unit.
        List<UnitQuery> unitList = UnitManager.getChildrenUnits(sourceProjectOID, unitBeanToAdd.getOID());
        for (Iterator iterator = unitList.iterator(); iterator.hasNext();)
        {
            UnitQuery cUnit = (UnitQuery) iterator.next();

            UnitBean cUnitBean = cUnit.getUnitBean();
            cUnitBean.setProjectPk(destinationProjectOID.getPk());

            addUnitToProjectRec(context, uprDAO, sourceProjectOID, destinationProjectOID, cUnitBean, unitBeanMap,
                    addAsPlannedUnit, copyUserConfig, copyFormConfig);
        }
    }
    public  void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
                                              WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
    {
        int workstationPk = workstationQuery.getPk();

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
                            testProc.setAppliedByUserFk(context.getUser().getPk());

                            testProcDAO.saveTestProc(context, testProc);
                        } else
                        {
                            // need to create a new testproc on the unit
                            TestProcObj testProc = new TestProcObj();
                            testProc.setProjectPk(projectQuery.getPk());
                            testProc.setUnitPk(unitPk);
                            testProc.setWorkstationPk(workstationQuery.getPk());
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
                ProjectManager.setUnitWorkstationStatus(context, unitPk, projectQuery.getOID(),
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
        UnitWorkstation uw = getUnitWorkstationSetting(unitOID.getPk(), projectOID, workstationOID);
        uw.setUpdatedBy(context.getUser().getPk());
        uw.setEstatus(EStatusEnum.Deleted.getValue());
        persistWrapper.update(uw);
    }


}
