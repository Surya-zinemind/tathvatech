package com.tathvatech.project.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.common.ProjectUserQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.entity.Project;
import com.tathvatech.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public  List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName)
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
}
