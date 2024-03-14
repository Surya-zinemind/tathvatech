package com.tathvatech.project.controller;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectFilter;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.common.ProjectSignatorySetBean;
import com.tathvatech.project.entity.Project;
import com.tathvatech.project.entity.ProjectPart;
import com.tathvatech.project.entity.ProjectStage;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Account;
import com.tathvatech.user.entity.Attachment;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.CommonServiceManager;
import com.tathvatech.workstation.entity.ProjectWorkstation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/project")
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private  final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public  List<ProjectQuery> getProjectList(UserContext context, ProjectFilter filter)
    {
        return projectService.getProjectList(context, filter);
    }

    public  List<Project> getProjectsAtSite(SiteOID siteOID)
    {
        return projectService.getProjectsAtSite(siteOID);
    }

    public  Project getProjectByName(String projectName)
    {
        try
        {
            return projectService.getProjectByName(projectName);
        }
        catch (Exception e)
        {
            logger.error("Error getting project by name", e);
        }
        return null;
    }

    public  Project getProject(int projectPk)
    {
        return projectService.getProject(projectPk);
    }

    /**
     * @param projectPk
     * @return
     */
    public  ProjectQuery getProjectQueryByPk(int projectPk)
    {
        return projectService.getProjectByPk(projectPk);
    }

    public  List<Project> getActiveProjects() throws Exception
    {
        return projectService.getActiveProjects();
    }

    /**
     * @param acc
     * @param projectName
     * @return
     * @throws Exception
     */
    public  boolean isProjectNameExist(Account acc, String projectName) throws Exception
    {
        return projectService.isProjectNameExist(acc, projectName);
    }

    /**
     * Check if the name exists for a project other than the one specified as
     * the projectPk argument
     *
     * @param acc
     * @param projectName
     * @param projectPk
     * @return
     * @throws Exception
     */
    public  boolean isProjectNameExistForAnotherProject(Account acc, String projectName, int projectPk)
            throws Exception
    {
        return projectService.isProjectNameExistForAnotherProject(projectName, projectPk);
    }

    public  void createProjectByCopy(UserContext context, Project project, String copyFromProjectPk)
            throws Exception
    {
        ProjectQuery newProject = projectService.createProject(context, project);

    }

    public  ProjectQuery createProject(UserContext context, Project project) throws Exception
    {
        return projectService.createProject(context, project);
        }

    public  ProjectQuery updateProject(UserContext context, Project pVal) throws Exception
    {
        return projectService.updateProject(context, pVal);
        }

    public ProjectPart getProjectPart(ProjectPartOID projectpartOID)
    {
        return projectService.getProjectPart(projectpartOID);
    }
    public  void updateFormsToProjectPart(UserContext context, ProjectOID projectOID,
                                                List<ProjectPartOID> projectPartList, WorkstationOID workstationOID, Collection<FormQuery> selectedFormList,
                                                String testName) throws Exception
    {

            // projectService.deleteAllFormsFromProjectPart(context,
            // projectPartOID, workstationPk);

            for (Iterator iterator = projectPartList.iterator(); iterator.hasNext();)
            {
                ProjectPartOID projectPartOID = (ProjectPartOID) iterator.next();

                for (Iterator iterator1 = selectedFormList.iterator(); iterator1.hasNext();)
                {
                    FormQuery formQuery = (FormQuery) iterator1.next();
                    projectService.addFormToProjectPart(context, projectOID, projectPartOID, workstationOID,
                            formQuery.getPk(), testName);
                }
            }


    }

    /**
     * We are setting a form on a project part. with no workstation information. the workstationPk will be null in the database
     * @param context
     * @param projectOID
     * @param projectPartList
     * @param selectedFormList
     * @param testName
     * @throws Exception
     */
    public  void updateFormsToProjectPart(UserContext context, ProjectOID projectOID,
                                                List<ProjectPartOID> projectPartList, Collection<FormQuery> selectedFormList,
                                                String testName) throws Exception
    {


            // projectService.deleteAllFormsFromProjectPart(context,
            // projectPartOID, workstationPk);

            for (Iterator iterator = projectPartList.iterator(); iterator.hasNext();)
            {
                ProjectPartOID projectPartOID = (ProjectPartOID) iterator.next();

                for (Iterator iterator1 = selectedFormList.iterator(); iterator1.hasNext();)
                {
                    FormQuery formQuery = (FormQuery) iterator1.next();
                    projectService.addFormToProjectPart(context, projectOID, projectPartOID,
                            formQuery.getPk(), testName);
                }
            }

    }

    public  void removeProjectFromFromProject(UserContext userContext, ProjectFormOID projectFormOID)
            throws Exception
    {


            projectService.deleteProjectForm(userContext, projectFormOID);

    }
    public  List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID)
    {
        return projectService.getProjectFormsForProject(projectOID);
    }

    public  List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        return projectService.getProjectFormsForProject(projectPk, workstationOID);
    }


    public  List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception
    {
        return projectService.getUsersForProjectInRole(projectOID, roleName);
    }

    public  List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName)
            throws Exception
    {
        return projectService.getUsersForProjectInRole(projectPk, workstationOID, roleName);
    }

    public  List<User> getUsersForProject(int projectPk, WorkstationOID workstationOID) throws Exception
    {
        return projectService.getUsersForProject(projectPk, workstationOID);
    }
    public  List<Project> getProjectsWhereTheUserIsReadOnly(UserContext context)
    {
        return projectService.getProjectsWhereTheUserIsReadOnly(context);
    }
    public  boolean isUserCoordinatorForProject(UserContext userContext, int projectPk)
    {
        return projectService.isUserCoordinatorForProject(userContext, projectPk);
    }
    public  void removeAllUsersFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {

            projectService.removeAllUsersFromProject(context, projectPk, workstationOID);
        }




    public  void addUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID, int userPk,
                                        String role) throws Exception
    {

            projectService.addUserToProject(context, projectPk, workstationOID, userPk, role);

    }

    public  void addReadonlyUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID,
                                                int userPk) throws Exception
    {


            projectService.addReadonlyUserToProject(context, projectPk, workstationOID, userPk);

    }

    public  void deleteProject(int projectPk) throws Exception
    {


            projectService.deleteProject(projectPk);

    }
    public  void closeProject(ProjectQuery projectQuery) throws Exception
    {


            projectService.closeProject(projectQuery);

    }

    public  void openProject(ProjectQuery projectQuery) throws Exception
    {


            projectService.openProject(projectQuery);

    }
    public  List<User> getprojectManagers(ProjectOID projectOID)
    {
        return projectService.getProjectManagers(projectOID);
    }

    public  List<User> getDataClerks(ProjectQuery projectQuery) throws Exception
    {
        return projectService.getDataClerks(projectQuery);
    }
    public  void updateProjectTeam(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                                   WorkstationOID wsOID, Collection testList, Collection verifyList, Collection approveList) throws Exception
    {


            projectService.updateProjectTeam(context, projectOID, projectPartOID, wsOID, testList, verifyList,
                    approveList);

    }

    public  List<Site> getSitesForProject(ProjectOID projectOID)
    {
        return projectService.getSitesForProject(projectOID);
    }

    public  List<ProjectSiteConfig> getProjectSiteConfigs(ProjectOID projectOID) throws AppException
    {
        return projectService.getProjectSiteConfigs(projectOID);
    }

    public  void saveSitesForProject(ProjectOID projectOID, Collection<Site> siteList) throws Exception
    {

            int a = projectService.saveSitesForProject(projectOID, siteList);


    }

    public ProjectStage getProjectStage(ProjectStageOID projectStageOID)
    {
        return projectService.getProjectStage(projectStageOID);
    }

    public  List<ProjectStage> getProjectStages(ProjectOID projectOID)
    {
        return projectService.getProjectStages(projectOID);
    }

    public  ProjectStage addProjectStage(ProjectStage projectStage) throws Exception
    {


            ProjectStage ps = projectService.addProjectStage(projectStage);
            return ps;

    }

    public  ProjectStage updateProjectStage(ProjectStage projectStage) throws Exception
    {


            ProjectStage ps = projectService.updateProjectStage(projectStage);

            return ps;

    }

    public  void removeProjectStage(int projectStagePk)throws Exception
    {


            projectService.removeProjectStage(projectStagePk);


    }

    public ProjectSignatorySetBean getProjectSignatorySet(ProjectSignatorySetOID sigSetOID)
    {
        return projectService.getProjectSignatorySet(sigSetOID);
    }

    public  List<ProjectSignatorySetBean> getProjectSignatorySets(ProjectOID projectOID)
    {
        return projectService.getProjectSignatorySets(projectOID);
    }

    public  ProjectSignatorySetBean addProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {

            ProjectSignatorySetBean set = projectService.addProjectSignatorySet(sBean);

            return set;

    }

    public  ProjectSignatorySetBean updateProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {


            ProjectSignatorySetBean set = projectService.updateProjectSignatorySet(sBean);

            return set;

    }

    public  void removeProjectSignatorySet(ProjectSignatorySetOID setOID) throws Exception
    {

            projectService.removeProjectSignatorySet(setOID);

    }

    public List<Project> getProjectsForPart(PartOID partOID)
    {
        return projectService.getProjectsForPart(partOID);
    }
    public  List<ProjectPartQuery> getProjectPartsWithTeams(ProjectOID projectOID, WorkstationOID workstationOID)
    {
        return projectService.getProjectPartsWithTeams(projectOID, workstationOID);
    }
    public  List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID)
    {
        return projectService.getProjectPartAssignedForms(projectOID);
    }

    public  List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID, ProjectPartOID projectPartOID)
    {
        return projectService.getProjectPartAssignedForms(projectOID, projectPartOID);
    }

    public  void removeProjectAttachment(UserContext userContext, Attachment attachment) throws Exception
    {

            // TODO:: add code here to check if the attachment has open items added to this drawing and throw an error if so.

            new CommonServiceManager().removeAttachment(userContext, attachment);





    }

    public  List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID)
    {
        return projectService.getProjectWorkstations(projectOID);
    }
}
