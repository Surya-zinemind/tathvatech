package com.tathvatech.project.service;

import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;

import java.util.Collection;
import java.util.List;

public interface ProjectService {
    Project getProject(long projectPk);

    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, WorkstationOID workstationOID);

    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                   WorkstationOID workstationOID);
    List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception;
    List<User> getUsersForProjectInRole(long projectPk, WorkstationOID workstationOID, String roleName) throws Exception;
    List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID);
    List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID);
    List<Site> getSitesForProject(ProjectOID projectOID);

    int saveSitesForProject(ProjectOID projectOID, Collection<Site> siteList) throws Exception;
    List<ProjectSiteConfig> getProjectSiteConfigs(ProjectOID projectOID);
    ProjectSiteConfig getProjectSiteConfig(ProjectOID projectOID, SiteOID siteOID);
    void removeAllUsersFromProject(UserContext context, long projectPk, WorkstationOID workstationOID) throws Exception;
    void addUserToProject(UserContext context, long projectPk, WorkstationOID workstationOID, long userPk,
                          String role) throws Exception;
    void addReadonlyUserToProject(UserContext context, long projectPk, WorkstationOID workstationOID,
                                  long userPk) throws Exception;
}
