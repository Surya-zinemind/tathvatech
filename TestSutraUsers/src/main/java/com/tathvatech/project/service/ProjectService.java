package com.tathvatech.project.service;

import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.entity.User;

import java.util.List;

public interface ProjectService {
    Project getProject(long projectPk);

    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, WorkstationOID workstationOID);

    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                   WorkstationOID workstationOID);
    List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception;
    List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName) throws Exception;
    List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID);
    List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID);
}
