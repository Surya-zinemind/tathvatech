package com.tathvatech.project.controller;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.Request.*;
import com.tathvatech.project.common.ProjectFilter;
import com.tathvatech.project.common.ProjectPartQuery;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.common.ProjectSignatorySetBean;
import com.tathvatech.project.entity.Project;
import com.tathvatech.project.entity.ProjectPart;
import com.tathvatech.project.entity.ProjectStage;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.project.oid.ProjectSignatorySetOID;
import com.tathvatech.project.oid.ProjectStageOID;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RequestMapping("/project")
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private  final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;
    private final CommonServiceManager commonServiceManager;

   @GetMapping("/getProjectList")
   public  List<ProjectQuery> getProjectList( @RequestBody ProjectFilter filter)
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.getProjectList(context, filter);
    }

    @GetMapping("/getProjectsAtSite")
    public  List<Project> getProjectsAtSite(@RequestBody SiteOID siteOID)
    {
        return projectService.getProjectsAtSite(siteOID);
    }

    @GetMapping("/getProjectByName/{projectName}")
    public  Project getProjectByName(@PathVariable("projectName") String projectName) throws Exception {
        return projectService.getProjectByName(projectName);

    }

    @GetMapping("/getProject/{projectPk}")
    public  Project getProject(@PathVariable("projectPk") int projectPk)
    {
        return projectService.getProject(projectPk);
    }

    /**
     * @param projectPk
     * @return
     */
    @GetMapping("/getProjectQueryByPk/{projectPk}")
    public  ProjectQuery getProjectQueryByPk(@PathVariable("projectPk") int projectPk)
    {
        return projectService.getProjectByPk(projectPk);
    }

    @GetMapping("/getActiveProjects")
    public  List<Project> getActiveProjects() throws Exception
    {
        return projectService.getActiveProjects();
    }

    /**
     * @param
     * @param
     * @return
     * @throws Exception
     */
   @GetMapping("/isProjectNameExist")
   public  boolean isProjectNameExist(@RequestBody IsProjectNameExistRequest isProjectNameExistRequest) throws Exception
    {
        return projectService.isProjectNameExist(isProjectNameExistRequest.getAcc(), isProjectNameExistRequest.getProjectName());
    }

    /**
     * Check if the name exists for a project other than the one specified as
     * the projectPk argument
     *
     * @param
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @GetMapping("/isProjectNameExistForAnotherProject")
    public  boolean isProjectNameExistForAnotherProject(@RequestBody IsProjectNameExistForAnotherProjectRequest isProjectNameExistForAnotherProjectRequest)
            throws Exception
    {
        return projectService.isProjectNameExistForAnotherProject(isProjectNameExistForAnotherProjectRequest.getProjectName(),isProjectNameExistForAnotherProjectRequest.getProjectPk());
    }

    /*@PostMapping("/createProjectByCopy")
   public  void createProjectByCopy(@RequestBody CreateProjectByCopyRequest createProjectByCopyRequest)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectQuery newProject = projectService.createProject(context, createProjectByCopyRequest.getProject());

    }

    @PostMapping("/createProject")
    public  ProjectQuery createProject(@RequestBody Project project) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.createProject(context, project);
        }
*/
    @PutMapping("/updateProject")
    public  ProjectQuery updateProject(@RequestBody Project pVal) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.updateProject(context, pVal);
        }

    @GetMapping("/getProjectPart")
    public ProjectPart getProjectPart(@RequestBody ProjectPartOID projectpartOID)
    {
        return projectService.getProjectPart(projectpartOID);
    }
   @PutMapping("/updateFormsToProjectPart")
   public  void updateFormsToProjectPart( @RequestBody UpdateFormsToProjectPartRequest updateFormsToProjectPartRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // projectService.deleteAllFormsFromProjectPart(context,
            // projectPartOID, workstationPk);

            for (Iterator iterator = updateFormsToProjectPartRequest.getProjectPartList().iterator(); iterator.hasNext();)
            {
                ProjectPartOID projectPartOID = (ProjectPartOID) iterator.next();

                for (Iterator iterator1 = updateFormsToProjectPartRequest.getSelectedFormList().iterator(); iterator1.hasNext();)
                {
                    FormQuery formQuery = (FormQuery) iterator1.next();
                    projectService.addFormToProjectPart(context, updateFormsToProjectPartRequest.getProjectOID(), projectPartOID, updateFormsToProjectPartRequest.getWorkstationOID(),
                            formQuery.getPk(), updateFormsToProjectPartRequest.getTestName());
                }
            }


    }

    /**
     * We are setting a form on a project part. with no workstation information. the workstationPk will be null in the database
     * @param
     * @param
     * @param
     * @param
     * @param
     * @throws Exception
     */
    @PutMapping("/updateFormsToProjectParts")
    public  void updateFormsToProjectPart( @RequestBody UpdateFormsToProjectPartsRequest updateFormsToProjectPartsRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // projectService.deleteAllFormsFromProjectPart(context,
            // projectPartOID, workstationPk);

            for (Iterator iterator = updateFormsToProjectPartsRequest.getProjectPartList().iterator(); iterator.hasNext();)
            {
                ProjectPartOID projectPartOID = (ProjectPartOID) iterator.next();

                for (Iterator iterator1 = updateFormsToProjectPartsRequest.getSelectedFormList().iterator(); iterator1.hasNext();)
                {
                    FormQuery formQuery = (FormQuery) iterator1.next();
                    projectService.addFormToProjectPart(context, updateFormsToProjectPartsRequest.getProjectOID(), projectPartOID,
                            formQuery.getPk(),updateFormsToProjectPartsRequest.getTestName());
                }
            }

    }

    @DeleteMapping("/removeProjectFromFromProject")
    public  void removeProjectFromFromProject(@RequestBody ProjectFormOID projectFormOID)
            throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            projectService.deleteProjectForm(context, projectFormOID);

    }
   @GetMapping("/getProjectFormsForProject")
   public  List<ProjectFormQuery> getProjectFormsForProject(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectFormsForProject(projectOID);
    }

    @GetMapping("/getProjectFormsForProjects")
    public  List<ProjectFormQuery> getProjectFormsForProject(@RequestBody GetProjectFormsForProjectRequest getProjectFormsForProjectRequest)
            throws Exception
    {
        return projectService.getProjectFormsForProject(getProjectFormsForProjectRequest.getProjectPk(), getProjectFormsForProjectRequest.getWorkstationOID());
    }


   @GetMapping("/getUsersForProjectInRole")
   public  List<User> getUsersForProjectInRole(@RequestBody GetUsersForProjectInRoleRequest  getUsersForProjectInRoleRequest) throws Exception
    {
        return projectService.getUsersForProjectInRole(getUsersForProjectInRoleRequest.getProjectOID(), getUsersForProjectInRoleRequest.getRoleName());
    }

   @GetMapping("/getUsersForProjectInRoles")
   public  List<User> getUsersForProjectInRole(@RequestBody GetUsersForProjectInRolesRequest getUsersForProjectInRolesRequest)
            throws Exception
    {
        return projectService.getUsersForProjectInRole(getUsersForProjectInRolesRequest.getProjectPk(), getUsersForProjectInRolesRequest.getWorkstationOID(), getUsersForProjectInRolesRequest.getRoleName());
    }

    @GetMapping("/getUsersForProject")
    public  List<User> getUsersForProject(@RequestBody GetUsersForProjectRequest getUsersForProjectRequest) throws Exception
    {
        return projectService.getUsersForProject(getUsersForProjectRequest.getProjectPk(), getUsersForProjectRequest.getWorkstationOID());
    }
    @GetMapping("/getProjectsWhereTheUserIsReadOnly")
    public  List<Project> getProjectsWhereTheUserIsReadOnly()
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.getProjectsWhereTheUserIsReadOnly(context);
    }
    @GetMapping("/isUserCoordinatorForProject/{projectPk}")
    public  boolean isUserCoordinatorForProject( @PathVariable("projectPk") int projectPk)
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return projectService.isUserCoordinatorForProject(userContext, projectPk);
    }
    @DeleteMapping("/removeAllUsersFromProject")
    public  void removeAllUsersFromProject( @RequestBody RemoveAllUsersFromProjectRequest removeAllUsersFromProjectRequest)
            throws Exception
    {
        UserContext userContext= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            projectService.removeAllUsersFromProject(userContext, removeAllUsersFromProjectRequest.getProjectPk(), removeAllUsersFromProjectRequest.getWorkstationOID());
        }




   @PostMapping("/addUserToProject")
   public  void addUserToProject(@RequestBody AddUserToProjectRequest addUserToProjectRequest) throws Exception
    {
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            projectService.addUserToProject(context,addUserToProjectRequest.getProjectPk() , addUserToProjectRequest.getWorkstationOID(),addUserToProjectRequest.getUserPk(),addUserToProjectRequest.getRole());

    }

    @PostMapping("/addReadonlyUserToProject")
    public  void addReadonlyUserToProject( @RequestBody AddReadonlyUserToProjectRequest addReadonlyUserToProjectRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            projectService.addReadonlyUserToProject(context, addReadonlyUserToProjectRequest.getProjectPk(), addReadonlyUserToProjectRequest.getWorkstationOID(), addReadonlyUserToProjectRequest.getUserPk());

    }

    @DeleteMapping("/deleteProject/{projectPk}")
    public  void deleteProject(@PathVariable("projectPk") int projectPk) throws Exception
    {


            projectService.deleteProject(projectPk);

    }
   @PutMapping("/closeProject")
   public  void closeProject(@RequestBody ProjectQuery projectQuery) throws Exception
    {


            projectService.closeProject(projectQuery);

    }

    @PutMapping("/openProject")
    public  void openProject(@RequestBody ProjectQuery projectQuery) throws Exception
    {


            projectService.openProject(projectQuery);

    }
   @GetMapping("/getprojectManagers")
   public  List<User> getprojectManagers(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectManagers(projectOID);
    }

    @GetMapping("/getDataClerks")
    public  List<User> getDataClerks(@RequestBody ProjectQuery projectQuery) throws Exception
    {
        return projectService.getDataClerks(projectQuery);
    }
   @PutMapping("/updateProjectTeam")
   public  void updateProjectTeam( @RequestBody UpdateProjectTeamRequest updateProjectTeamRequest) throws Exception
    {

        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            projectService.updateProjectTeam(context, updateProjectTeamRequest.getProjectOID(), updateProjectTeamRequest.getProjectPartOID(),updateProjectTeamRequest.getWsOID(), updateProjectTeamRequest.getTestList(),updateProjectTeamRequest.getVerifyList(),
                    updateProjectTeamRequest.getApproveList());

    }

   @GetMapping("/getSitesForProject")
   public  List<Site> getSitesForProject(@RequestBody ProjectOID projectOID)
    {
        return projectService.getSitesForProject(projectOID);
    }

   @GetMapping("/getProjectSiteConfigs")
   public  List<ProjectSiteConfig> getProjectSiteConfigs(@RequestBody ProjectOID projectOID) throws AppException
    {
        return projectService.getProjectSiteConfigs(projectOID);
    }

    @PostMapping("/saveSitesForProject")
    public  void saveSitesForProject(@RequestBody SaveSitesForProjectRequest saveSitesForProjectRequest) throws Exception
    {

            int a = projectService.saveSitesForProject(saveSitesForProjectRequest.getProjectOID(),saveSitesForProjectRequest.getSiteList());


    }

   @GetMapping("/getProjectStage")
   public ProjectStage getProjectStage(@RequestBody ProjectStageOID projectStageOID)
    {
        return projectService.getProjectStage(projectStageOID);
    }

    @GetMapping("/getProjectStages")
    public  List<ProjectStage> getProjectStages(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectStages(projectOID);
    }

    @PostMapping("/addProjectStage")
    public  ProjectStage addProjectStage(@RequestBody ProjectStage projectStage) throws Exception
    {


            ProjectStage ps = projectService.addProjectStage(projectStage);
            return ps;

    }

   @PutMapping("/updateProjectStage")
   public  ProjectStage updateProjectStage(@RequestBody ProjectStage projectStage) throws Exception
    {


            ProjectStage ps = projectService.updateProjectStage(projectStage);

            return ps;

    }

   @DeleteMapping("/removeProjectStage/{projectStagePk}")
   public  void removeProjectStage(@PathVariable("projectStagePk") int projectStagePk)throws Exception
    {


            projectService.removeProjectStage(projectStagePk);


    }

  /* @GetMapping("/getProjectSignatorySet")
   public ProjectSignatorySetBean getProjectSignatorySet(@RequestBody ProjectSignatorySetOID sigSetOID)
    {
        return projectService.getProjectSignatorySet(sigSetOID);
    }

    @GetMapping("/getProjectSignatorySets")
    public  List<ProjectSignatorySetBean> getProjectSignatorySets(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectSignatorySets(projectOID);
    }

   @PostMapping("/addProjectSignatorySet")
   public  ProjectSignatorySetBean addProjectSignatorySet(@RequestBody ProjectSignatorySetBean sBean) throws Exception
    {

            ProjectSignatorySetBean set = projectService.addProjectSignatorySet(sBean);

            return set;

    }

  @PutMapping("/updateProjectSignatorySet")
  public  ProjectSignatorySetBean updateProjectSignatorySet(@RequestBody ProjectSignatorySetBean sBean) throws Exception
    {


            ProjectSignatorySetBean set = projectService.updateProjectSignatorySet(sBean);

            return set;

    }

   @DeleteMapping("/removeProjectSignatorySet")
   public  void removeProjectSignatorySet(@RequestBody ProjectSignatorySetOID setOID) throws Exception
    {

            projectService.removeProjectSignatorySet(setOID);

    }*/

    @GetMapping("/getProjectsForPart")
    public List<Project> getProjectsForPart(@RequestBody PartOID partOID)
    {
        return projectService.getProjectsForPart(partOID);
    }
    @GetMapping("/getProjectPartsWithTeams")
    public  List<ProjectPartQuery> getProjectPartsWithTeams(@RequestBody GetProjectPartsWithTeamsRequest getProjectPartsWithTeamsRequest)
    {
        return projectService.getProjectPartsWithTeams(getProjectPartsWithTeamsRequest.getProjectOID(), getProjectPartsWithTeamsRequest.getWorkstationOID());
    }
   @GetMapping("/getProjectPartAssignedFormsProject")
   public  List<ProjectFormQuery> getProjectPartAssignedForms(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectPartAssignedForms(projectOID);
    }

  @GetMapping("/getProjectPartAssignedForms")
  public  List<ProjectFormQuery> getProjectPartAssignedForms(@RequestBody GetProjectPartAssignedFormsRequest getProjectPartAssignedFormsRequest)
    {
        return projectService.getProjectPartAssignedForms(getProjectPartAssignedFormsRequest.getProjectOID(), getProjectPartAssignedFormsRequest.getProjectPartOID());
    }

   @DeleteMapping("/removeProjectAttachment")
   public  void removeProjectAttachment(@RequestBody Attachment attachment) throws Exception
    {

            // TODO:: add code here to check if the attachment has open items added to this drawing and throw an error if so.
        UserContext context= (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commonServiceManager.removeAttachment(context, attachment);





    }

   @GetMapping("/getProjectWorkstations")
   public  List<ProjectWorkstation> getProjectWorkstations(@RequestBody ProjectOID projectOID)
    {
        return projectService.getProjectWorkstations(projectOID);
    }
}
