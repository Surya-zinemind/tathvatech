package com.tathvatech.unit.service;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.entity.ProjectForm;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.unit.enums.CommonEnums;
import com.tathvatech.unit.request.UnitFilterBean;
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

    UnitObj createUnitAtWorkstation(UserContext context, ProjectOID projectOID, WorkstationOID workstationOID, UnitBean unit, boolean copyPartSpecificFormsToWorkstation, boolean pendingReview) throws Exception;

    UnitObj updateUnit(UserContext context, UnitObj unit) throws Exception;

    UnitQuery getUnitQueryByPk(int unitPk, ProjectOID projectOID);

    //TestProcOID addFormToUnit(UserContext context, ProjectForm projectForm, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int formPk, String testName, boolean makeWorkstationInProgress, boolean reviewPending);

    void removeAllFormsFromUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception;

    void deleteTestProcFromUnit(UserContext context, TestProcOID testProcOid) throws Exception;

   /* List<FormQuery> getAllFormsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception;

    List<FormQuery> getFormsForUnit(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception;*/

    List<User> getUsersForUnit(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception;

    List<User> getUsersForProjectInRole(int pk, WorkstationOID workstationOID, String roleName) throws Exception;

    List<User> getUsersForUnitInRole(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String roleName) throws Exception;

    boolean isUsersForUnitInRole(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String roleName);

    void removeAllUsersFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID);

    void addTesterToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk);

    void addVerifierToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk) throws Exception;

    void addApproverToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk) throws Exception;

    void addReadonlyUserToUnit(UserContext context, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, int userPk) throws Exception;

    List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID) throws Exception;

    void removeUnitFromProject(UserContext context, UnitOID unitOID, ProjectOID projectOID, CommonEnums.DeleteOptionEnum deleteUnitOption) throws Exception;

    void openUnit(UserContext context, UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList, ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID) throws Exception;

    void removeUserFromUnit(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID, String role) throws Exception;

    void updateUnit(UnitObj unit) throws Exception;

    List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID, boolean includeChildren) throws Exception;

    float getUnitPercentComplete(UserContext context, int unitPk, ProjectOID projectOID, boolean includeChildren) throws Exception;

    HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren) throws Exception;

    int getUnitCount(int projectPk, boolean includeChildren) throws Exception;

    List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, WorkstationOID workstationOID);

    List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, ProjectPartOID projectPartOID, WorkstationOID workstationOID);

    void addUnitToProject(UserContext context, ProjectOID sourceProjectOID, ProjectOID destinationProjectOID, UnitBean rootUnitBean, List<UnitBean> unitBeanAndChildrenList, boolean addAsPlannedUnit) throws Exception;

    void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, Integer[] selectedUnits);
    void setWorkstationProjectPartTeamSetupToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, ProjectPartOID projectPartOID, Integer[] selectedUnits, boolean copyDefaultTeamIfNoProjectPartTeamIsSet) throws Exception;

    void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam) throws Exception;

    void deleteWorkstationToUnits(UserContext context, ProjectQuery projectQuery, WorkstationQuery workstationQuery, UnitObj[] array) throws Exception;

    void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID) throws Exception;


}
