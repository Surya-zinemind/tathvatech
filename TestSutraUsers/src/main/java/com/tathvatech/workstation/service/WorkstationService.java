package com.tathvatech.workstation.service;

import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.common.UnitLocationQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Project;
import com.tathvatech.workstation.common.UnitInProjectObj;
import com.tathvatech.workstation.common.UnitWorkstationQuery;
import com.tathvatech.workstation.common.WorkstationQuery;
import com.tathvatech.workstation.entity.ProjectWorkstation;
import com.tathvatech.workstation.entity.UnitWorkstation;
import com.tathvatech.workstation.entity.Workstation;
import com.tathvatech.workstation.request.WorkstationFilter;

import java.util.List;

public interface WorkstationService {

    Workstation createWorkstation(UserContext context, Workstation workstation) throws Exception;

    Workstation updateWorkstation(UserContext context, Workstation workstation) throws Exception;

    List<WorkstationQuery> getWorkstationList() throws Exception;

    Workstation getWorkstation(WorkstationOID workstationOID);

    WorkstationQuery getWorkstationQueryByPk(WorkstationOID workstationOID);

    List<WorkstationQuery> getWorkstationsForProject(long projectPk);

    List<WorkstationQuery> getWorkstations(WorkstationFilter filter);

    List<WorkstationQuery> getWorkstationsAssignableForProject(ProjectOID projectOID);

    List<WorkstationQuery> getWorkstationsForSite(int sitePk) throws Exception;

    List<WorkstationQuery> getWorkstationsForSiteAndProject(int sitePk, int projectPk) throws Exception;

    void addWorkstationToProject(UserContext context, int projectPk, WorkstationOID workstationOID) throws Exception;

    void removeWorkstationFromProject(UserContext context, int projectPk, WorkstationOID workstationOID) throws Exception;

    void removeAllWorkstationsFromProject(UserContext context, int projectPk) throws Exception;
    List<WorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception;

    List<UnitWorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID, boolean includeChildUnits) throws Exception;

    UnitWorkstation addWorkstationToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
                                         WorkstationOID workstationOID) throws Exception;

   /* void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
                                   WorkstationOID workstationOID) throws Exception;*/

    List<UnitLocationQuery> getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID)
            throws Exception;

    UnitLocation getUnitWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
            throws Exception;

    UnitLocationQuery getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID,
                                               WorkstationOID workstationOID);

    List<UnitLocationQuery> getUnitWorkstationStatusHistory(int unitPk, ProjectOID projectOID,
                                                            WorkstationOID workstationOID) throws Exception;

    void setUnitWorkstationStatus(UserContext userContext, int unitPk, ProjectOID projectOID,
                                  WorkstationOID workstationOID, String status) throws Exception;

    void recordWorkstationFormAccess(TestProcOID testProcOID);

    void recordWorkstationSave(TestProcOID testProcOID);

    void recordWorkstationFormLock(TestProcOID testProcOID);

    void recordWorkstationFormUnlock(TestProcOID testProcOID);

    void deleteWorstation(UserContext context, WorkstationOID workstationOID) throws Exception;

    List<Project> getProjectsForWorkstation(UserContext context, WorkstationOID workstationOID)
            throws Exception;

    float getWorkstationPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
                                        WorkstationOID workstationOID, boolean includeChildren) throws Exception;

    float getWorkstationPercentCompleteInt(UserContext context, int unitPk, ProjectOID projectOID,
                                           WorkstationOID workstationOID, boolean includeChildren) throws Exception;

    void moveWorkstationOrderUp(UserContext context, WorkstationOID workstationOID);

    void moveWorkstationOrderDown(UserContext context, WorkstationOID workstationOID);

    UnitWorkstation getUnitWorkstationSetting(long unitPk, ProjectOID projectOID,
                                              WorkstationOID workstationOID);

    UnitWorkstation updateUnitWorkstationSetting(UnitWorkstation unitWorkstation) throws Exception;

    void copyWorkstationSettings(UserContext context, ProjectOID copyFromProjectOID,
                                 ProjectOID destinationProjectOID, boolean copySites, boolean copyProjectFunctionTeams, boolean copyParts,
                                 boolean copyOpenItemTeam, boolean copyProjectCoordinators, List<Object[]> workstationsToCopy)
            throws Exception;

    List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID);

}
