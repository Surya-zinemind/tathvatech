package com.tathvatech.unit.service;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.entity.ProjectForm;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.enums.CommonEnums;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.WorkstationQuery;

import java.util.HashMap;
import java.util.List;

public interface UnitService {

     UnitObj getUnitByPk(UnitOID unitOID);

    List<UnitQuery> getUnitsBySerialNos(String[] serialNos, ProjectOID projectOID);

    List<UnitQuery> getUnitsByPks(int[] unitPks, ProjectOID projectOID);

    List<UnitQuery> getUnitsForProjectNew(UnitFilterBean unitFilter, UnitOID parent) throws Exception;

    UnitObj createUnit(UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit, boolean pendingReview) throws Exception;

    UnitObj createUnitAtWorkstation(UserContext context, ProjectOID projectOID, WorkstationOID workstationOID, UnitBean unit, boolean copyPartSpecificFormsToWorkstation, boolean pendingReview);

    void updateUnit(UserContext context, UnitObj unit);

    UnitQuery getUnitQueryByPk(int unitPk, ProjectOID projectOID);

    TestProcOID addFormToUnit(UserContext context, ProjectForm projectForm, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int formPk, String testName, boolean makeWorkstationInProgress, boolean reviewPending);

    void removeAllFormsFromUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID);

    void deleteTestProcFromUnit(UserContext context, TestProcOID testProcOid);

    List<FormQuery> getAllFormsForUnit(UnitOID unitOID, ProjectOID projectOID);

    List<FormQuery> getFormsForUnit(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID);

    List<User> getUsersForUnit(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID);

    List<User> getUsersForProjectInRole(long pk, WorkstationOID workstationOID, String roleName);

    List<User> getUsersForUnitInRole(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String roleName);

    boolean isUsersForProjectInRole(int userPk, ProjectOID projectOID, WorkstationOID workstationOID, String roleName);

    boolean isUsersForUnitInRole(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String roleName);

    void removeAllUsersFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID);

    void addTesterToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk);

    void addVerifierToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk);

    void addApproverToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk);

    void addReadonlyUserToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk);

    List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID);

    void removeUnitFromProject(UserContext context, UnitOID unitOID, ProjectOID projectOID, CommonEnums.DeleteOptionEnum deleteUnitOption);

    void openUnit(UserContext context, UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList, ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID);

    void removeUserFromUnit(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String role);

    void updateUnit(UnitObj unit);

    List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID, boolean includeChildren);

    float getUnitPercentComplete(UserContext context, int unitPk, ProjectOID projectOID, boolean includeChildren);

    HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren);

    int getUnitCount(int projectPk, boolean includeChildren);

    List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, WorkstationOID workstationOID);

    List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, ProjectPartOID projectPartOID, WorkstationOID workstationOID);

    void addUnitToProject(UserContext context, ProjectOID sourceProjectOID, ProjectOID destinationProjectOID, UnitBean rootUnitBean, List<UnitBean> unitBeanAndChildrenList, boolean addAsPlannedUnit);

    void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, Integer[] selectedUnits);
    void setWorkstationProjectPartTeamSetupToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, ProjectPartOID projectPartOID, Integer[] selectedUnits, boolean copyDefaultTeamIfNoProjectPartTeamIsSet);

    void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam);

    void deleteWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, UnitObj[] array);

    void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID);


}
