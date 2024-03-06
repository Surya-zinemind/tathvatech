package com.tathvatech.project.service;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.entity.ProjectUser;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final PersistWrapper persistWrapper;
    public Project getProject(long projectPk)
    {
        return (Project) persistWrapper.readByPrimaryKey(Project.class, projectPk);
    }

    public List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(ProjectUserQuery.class,
                    ProjectUserQuery.sql + " and pu.projectPk=? and pu.workstationPk=? order by u.firstName asc",
                    projectOID.getPk(), workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }


    }
    public  List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, ProjectPartOID projectPartOID,
                                                                 WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(ProjectUserQuery.class, ProjectUserQuery.sql
                            + " and pu.projectPk=? and projectPartPk = ? and pu.workstationPk=? order by u.firstName asc",
                    projectOID.getPk(), projectPartOID.getPk(), workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception
    {
        return persistWrapper.readList(User.class,
                "select distinct u.* from TAB_USER u " + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
                        + "where tpu.projectPk=? and tpu.projectPartPk is null and tpu.role=? order by u.firstName asc",
                projectOID.getPk(), roleName);
    }

    public  List<User> getUsersForProjectInRole(long projectPk, WorkstationOID workstationOID, String roleName)
            throws Exception
    {
        return persistWrapper.readList(User.class, "select distinct u.* from TAB_USER u "
                        + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
                        + "where tpu.projectPk=? and tpu.projectPartPk is null and tpu.workstationPk=? and tpu.role=? order by firstName asc",
                projectPk, workstationOID.getPk(), roleName);
    }

    public  List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID)
    {
        try
        {
            return persistWrapper.readList(ProjectFormQuery.class,
                    ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? ",
                    projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(ProjectFormQuery.class,
                    ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? and pf.workstationPk=? ",
                    projectPk, workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<Site> getSitesForProject(ProjectOID projectOID)
    {
        try
        {
            return persistWrapper.readList(Site.class,
                    "select site.* from site inner join project_site_config psc on psc.siteFk = site.pk where psc.projectFk = ? and psc.estatus = 1 and site.estatus = 1",
                    projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  int saveSitesForProject(ProjectOID projectOID, Collection<Site> siteList) throws Exception
    {
        List<Site> existingSites = getSitesForProject(projectOID);

        for (Iterator iterator = siteList.iterator(); iterator.hasNext();)
        {
            Site aNewSite = (Site) iterator.next();
            Site temp = new Site();
            temp.setPk(aNewSite.getPk());
            if (existingSites.remove(temp) == false)
            {
                ProjectSiteConfig c = new ProjectSiteConfig();
                c.setProjectFk((int) projectOID.getPk());
                c.setSiteFk((int) aNewSite.getPk());
                c.setEstatus(EStatusEnum.Active.getValue());
                persistWrapper.createEntity(c);
            }
        }

        boolean couldNotReportAll = false;
        // remaining are the ones that are to be removed.
        for (Iterator iterator = existingSites.iterator(); iterator.hasNext();)
        {
            Site site = (Site) iterator.next();
            // check if any workstation in this site is part of the project.
            int count = persistWrapper.read(Integer.class,
                    "select count(w.pk) from TAB_WORKSTATION w, TAB_PROJECT_WORKSTATIONS pw where w.pk = pw.workstationPk and pw.projectPk = ? and w.sitePk = ?",
                    projectOID.getPk(), site.getPk());
            if (count > 0)
            {
                couldNotReportAll = true;
                continue;
            }

            // check for unit workstations
            // TODO:: we should not allow workstations which are not part of the
            // project to be added to a unit. if that is implemented
            // we can remove this check.
            count = persistWrapper.read(Integer.class,
                    "select count(distinct(w.pk)) from TAB_WORKSTATION w, TAB_UNIT_WORKSTATIONS uw "
                            + "where w.pk = uw.workstationPk and uw.projectPk = ? and w.sitePk = ?",
                    projectOID.getPk(), site.getPk());

            if (count > 0)
            {
                couldNotReportAll = true;
                continue;
            }

            // delete
            ProjectSiteConfig config = persistWrapper.read(ProjectSiteConfig.class,
                    "select * from project_site_config where projectFk = ? and siteFk = ?",
                    new Object[] { projectOID.getPk(), site.getPk() });
            persistWrapper.deleteEntity(config);
        }

        if (couldNotReportAll == true)
            return 1;
        else
            return 0;
    }

    public  List<ProjectSiteConfig> getProjectSiteConfigs(ProjectOID projectOID)
    {
        try
        {
            return persistWrapper.readList(ProjectSiteConfig.class,
                    "select * from project_site_config where projectFk = ?", projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public  ProjectSiteConfig getProjectSiteConfig(ProjectOID projectOID, SiteOID siteOID)
    {
        return persistWrapper.read(ProjectSiteConfig.class,
                "select * from project_site_config where projectFk = ? and siteFk = ?", projectOID.getPk(),
                siteOID.getPk());
    }

    public  void removeAllUsersFromProject(UserContext context, long projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        persistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk=? and workstationPk=?", projectPk,
                workstationOID.getPk());
    }

    public  void addUserToProject(UserContext context, long projectPk, WorkstationOID workstationOID, long userPk,
                                  String role) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk(projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk(userPk);
        pUser.setRole(role);

        persistWrapper.createEntity(pUser);
    }

    public  void addReadonlyUserToProject(UserContext context, long projectPk, WorkstationOID workstationOID,
                                          long userPk) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk(projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk(userPk);
        pUser.setRole(User.ROLE_READONLY);

        persistWrapper.createEntity(pUser);
    }


}
