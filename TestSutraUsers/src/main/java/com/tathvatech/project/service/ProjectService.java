package com.tathvatech.project.service;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectFilter;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.common.ProjectSignatorySetBean;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.entity.ProjectPart;
import com.tathvatech.project.entity.ProjectStage;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.user.OID.ProjectFormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Account;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;

import java.util.Collection;
import java.util.List;

public interface ProjectService {
    List<ProjectQuery> getProjectList(UserContext context, ProjectFilter filter);

    List<Project> getProjectsAtSite(SiteOID siteOID);
    Project getProjectByName(String projectName) throws Exception;
    Project getProject(long projectPk);
    ProjectQuery getProjectByPk(int projectPk);
    List<Project> getActiveProjects() throws Exception;
    boolean isProjectNameExist(Account acc, String projectName) throws Exception;
    boolean isProjectNameExistForAnotherProject(String projectName, int projectPk) throws Exception;
    ProjectQuery createProject(UserContext context, Project project) throws Exception;
    ProjectQuery updateProject(UserContext context, Project project) throws Exception;
    ProjectPart getProjectPart(ProjectPartOID projectpartOID);
    void addFormToProjectPart(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                              WorkstationOID workstationOID, int formPk, String testName) throws Exception;
    void deleteProjectForm(UserContext context, ProjectFormOID projectFormOID) throws Exception;



    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, WorkstationOID workstationOID);

    List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                   WorkstationOID workstationOID);
    List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception;
    List<User> getUsersForProjectInRole(long projectPk, WorkstationOID workstationOID, String roleName) throws Exception;
    List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID);
    List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID);
    List<FormQuery> getFormsForProject(ProjectOID projectOID) throws Exception;
    List<FormQuery> getFormsForProject(int projectPk, WorkstationOID workstationOID) throws Exception;
    void addUserToProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
                          int workstationPk, int userPk, String role) throws Exception;
    void addUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID, int userPk,
                          String role) throws Exception;
    void addReadonlyUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID,
                                  int userPk) throws Exception;
    void updateProjectTeam(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                           WorkstationOID wsOID, Collection testList, Collection verifyList, Collection approveList) throws Exception;
    List<User> getUsersForProjectPartInRole(int projectPk, ProjectPartOID projectPartOID,
                                            WorkstationOID workstationOID, String roleName) throws Exception;
    List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName)
            throws Exception;
    List<User> getUsersForProject(int projectPk, WorkstationOID workstationOID) throws Exception;
    List<Project> getProjectsWhereTheUserIsReadOnly(UserContext context);
    boolean isUserCoordinatorForProject(UserContext userContext, int projectPk);
    void removeUserFromProject(UserContext context, int projectPk, int workstationPk, int userPk,
                               String role) throws Exception;
    void removeUserFromProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
                               int workstationPk, int userPk, String role) throws Exception;
    void removeAllUsersFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception;
    boolean isUsersForProjectInRole(int userPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                    String roleName);
    void deleteProject(int projectPk) throws Exception;
    void closeProject(ProjectQuery projectQuery) throws Exception;
    void openProject(ProjectQuery projectQuery) throws Exception;
    List<ProjectFormQuery> getProjectFormAssignmentsForForm(FormMainOID formMainOID) throws Exception;
    List<User> getProjectManagers(ProjectOID projectOID);
    List<User> getDataClerks(ProjectQuery projectQuery) throws Exception;
    ProjectStage getProjectStage(ProjectStageOID projectStageOID);
    List<ProjectStage> getProjectStages(ProjectOID projectOID);
    ProjectStage addProjectStage(ProjectStage projectStage) throws Exception;
    ProjectStage updateProjectStage(ProjectStage projectStage) throws Exception;
    void removeProjectStage(int projectStagePk) throws Exception;
    ProjectSignatorySetBean getProjectSignatorySet(ProjectSignatorySetOID sigSetOID);
    List<ProjectSignatorySetBean> getProjectSignatorySets(ProjectOID projectOID);
    ProjectSignatorySetBean addProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception;
    ProjectSignatorySetBean updateProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception;
    void removeProjectSignatorySet(ProjectSignatorySetOID setOID) throws Exception;
    List<Project> getProjectsForPart(PartOID partOID);
    List<ProjectPartQuery> getProjectPartsWithTeams(ProjectOID projectOID, WorkstationOID workstationOID);
    List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID);
    List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID,
                                                       ProjectPartOID projectPartOID);
    List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID);
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
