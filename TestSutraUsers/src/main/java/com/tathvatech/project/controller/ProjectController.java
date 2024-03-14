package com.tathvatech.project.controller;

import com.tathvatech.project.entity.Project;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.user.OID.PartOID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            ProjectQuery newProject = projectService.createProject(context, project);

            // Project copyFromProject =
            // projectService.getProjectByPk(copyFromProjectPk);
            con.commit();
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
        }
    }

    public  ProjectQuery createProject(UserContext context, Project project) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            return projectService.createProject(context, project);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            // a call to commit in the finally is ok as if there is an exception
            // rollback is already called..
            // and a commit call will not do anything. else you need to store
            // the return val in a local variable
            // call commit and then return that variable. unwanted lines of
            // code.
            con.commit();
        }
    }
    public  ProjectQuery updateProject(UserContext context, Project pVal) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            return projectService.updateProject(context, pVal);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  ProjectPart getProjectPart(ProjectPartOID projectpartOID)
    {
        return projectService.getProjectPart(projectpartOID);
    }
    public  void updateFormsToProjectPart(UserContext context, ProjectOID projectOID,
                                                List<ProjectPartOID> projectPartList, WorkstationOID workstationOID, Collection<FormQuery> selectedFormList,
                                                String testName) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

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
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

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
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void removeProjectFromFromProject(UserContext userContext, ProjectFormOID projectFormOID)
            throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.deleteProjectForm(userContext, projectFormOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
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

    public  List<FormQuery> getFormsForProject(ProjectOID projectOID) throws Exception
    {
        return projectService.getFormsForProject(projectOID);
    }

    public  List<FormQuery> getFormsForProject(int projectPk, WorkstationOID workstationOID) throws Exception
    {
        return projectService.getFormsForProject(projectPk, workstationOID);
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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.removeAllUsersFromProject(context, projectPk, workstationOID);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }

    }
    public  void addUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID, int userPk,
                                        String role) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.addUserToProject(context, projectPk, workstationOID, userPk, role);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void addReadonlyUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID,
                                                int userPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.addReadonlyUserToProject(context, projectPk, workstationOID, userPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void deleteProject(int projectPk) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.deleteProject(projectPk);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  void closeProject(ProjectQuery projectQuery) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.closeProject(projectQuery);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }

    public  void openProject(ProjectQuery projectQuery) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.openProject(projectQuery);
        }
        catch (Exception ex)
        {
            con.rollback();
            throw ex;
        }
        finally
        {
            con.commit();
        }
    }
    public  List<User> getprojectServices(ProjectOID projectOID)
    {
        return projectService.getprojectServices(projectOID);
    }

    public  List<User> getDataClerks(ProjectQuery projectQuery) throws Exception
    {
        return projectService.getDataClerks(projectQuery);
    }
    public  void updateProjectTeam(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                                         WorkstationOID wsOID, Collection testList, Collection verifyList, Collection approveList) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.updateProjectTeam(context, projectOID, projectPartOID, wsOID, testList, verifyList,
                    approveList);
            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            int a = projectService.saveSitesForProject(projectOID, siteList);
            con.commit();
            if (a == 0)
            {
                return;
            } else if (a == 1)
            {
                throw new AppException(
                        "Some sites could not be removed as workstations from those sites are part of the project.");
            }

        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  ProjectStage getProjectStage(ProjectStageOID projectStageOID)
    {
        return projectService.getProjectStage(projectStageOID);
    }

    public  List<ProjectStage> getProjectStages(ProjectOID projectOID)
    {
        return projectService.getProjectStages(projectOID);
    }

    public  ProjectStage addProjectStage(ProjectStage projectStage) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            ProjectStage ps = projectService.addProjectStage(projectStage);
            con.commit();

            return ps;

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  ProjectStage updateProjectStage(ProjectStage projectStage) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            ProjectStage ps = projectService.updateProjectStage(projectStage);
            con.commit();

            return ps;

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  void removeProjectStage(int projectStagePk)throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.removeProjectStage(projectStagePk);
            con.commit();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  ProjectSignatorySetBean getProjectSignatorySet(ProjectSignatorySetOID sigSetOID)
    {
        return projectService.getProjectSignatorySet(sigSetOID);
    }

    public  List<ProjectSignatorySetBean> getProjectSignatorySets(ProjectOID projectOID)
    {
        return projectService.getProjectSignatorySets(projectOID);
    }

    public  ProjectSignatorySetBean addProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            ProjectSignatorySetBean set = projectService.addProjectSignatorySet(sBean);
            con.commit();
            return set;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  ProjectSignatorySetBean updateProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            ProjectSignatorySetBean set = projectService.updateProjectSignatorySet(sBean);
            con.commit();
            return set;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  void removeProjectSignatorySet(ProjectSignatorySetOID setOID) throws Exception
    {
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            projectService.removeProjectSignatorySet(setOID);
            con.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  List<Project> getProjectsForPart(PartOID partOID)
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
        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            // TODO:: add code here to check if the attachment has open items added to this drawing and throw an error if so.

            new CommonServiceManager().removeAttachment(userContext, attachment);

            con.commit();
        }
        catch (Exception ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException e)
            {
                logger.error("Count not rollback transaction", ex);
            }
            throw ex;
        }
        finally
        {
        }
    }

    public  List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID)
    {
        return projectService.getProjectWorkstations(projectOID);
    }
}
