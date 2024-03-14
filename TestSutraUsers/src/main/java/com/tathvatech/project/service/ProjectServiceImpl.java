package com.tathvatech.project.service;
import com.tathvatech.project.common.*;
import com.tathvatech.project.entity.ProjectPart;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.user.service.AccountService;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.project.entity.Project;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.project.entity.ProjectForm;
import com.tathvatech.project.entity.ProjectStage;
import com.tathvatech.project.entity.ProjectUser;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.site.entity.ProjectSiteConfig;
import com.tathvatech.site.enums.ProjectSiteConfigRolesEnum;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Account;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserPerms;
import com.tathvatech.user.enums.ProjectRolesEnum;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;
import com.tathvatech.workstation.entity.ProjectWorkstation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private  final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final AccountService accountService;
    private final PersistWrapper persistWrapper;
    private final DummyWorkstation dummyWorkstation;
    private final ProjectService projectService;
    private final AuthorizationManager authorizationManager;
    public  List<ProjectQuery> getProjectList(UserContext context, ProjectFilter filter) {
        if (User.USER_PRIMARY.equals(context.getUser().getUserType())) // all
        // projects
        {
            List<Object> params = new ArrayList<>();
            StringBuffer sb = new StringBuffer(ProjectQuery.fetchSQL);

            if (filter.getUnitOID() != null)
            {
                sb.append(" join unit_project_ref upr on upr.projectPk = project.pk and upr.unitPk = ? ");
                params.add(filter.getUnitOID().getPk());
            }
            if (filter.getSiteOIDs() != null && filter.getSiteOIDs().size() > 0)
            {
                sb.append(" join project_site_config psc on psc.projectFk = project.pk and psc.siteFk in ( ");
                String sep = "";
                for (Iterator iterator = filter.getSiteOIDs().iterator(); iterator.hasNext();)
                {
                    SiteOID aSite = (SiteOID) iterator.next();
                    sb.append(sep).append("?");
                    params.add(aSite.getPk());
                    sep = ", ";
                }
                sb.append(") ");
            }
            sb.append(" where 1 = 1 ");

            if (StringUtils.isNotEmpty(filter.getSearchString()))
            {
                sb.append(" and ( upper(projectName) like ? or upper(projectDescription) like ?) ");
                params.add("%" + filter.getSearchString().toUpperCase() + "%");
                params.add("%" + filter.getSearchString().toUpperCase() + "%");
            }

            sb.append(" order by project.createdDate desc");

            List<ProjectQuery> list = persistWrapper.readList(ProjectQuery.class, sb.toString(), params.toArray());
            if (list != null)
            {
                List<Integer> projectPks = list.stream().map(query -> query.getPk()).collect(Collectors.toList());
                HashMap<ProjectOID, Integer> projectUnit = null;
                try {
                    projectUnit = getUnitCount(projectPks, false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < list.size(); i++)
                {
                    if (projectUnit.containsKey(list.get(i).getOID()))
                    {
                        list.get(i).setUnitCount(projectUnit.get(list.get(i).getOID()));
                    }
                }
            }
            return list;

        } else
        {
            // list of projectpks which should be returned ignoring of the
            // enable checksheets setting on the project and the project filter
            // value .
            List<Integer> respectEnableCheckSheetFilterSettingPkList = new ArrayList<Integer>();

            // list of projectpks which should be returned respecting of the
            // enable checksheets setting on the project and the project filter
            // value .
            List<Integer> ignoreEnableCheckSheetFilterSettingPkList = new ArrayList<Integer>();

            if (filter.getValidRoles() != null)
            {
                List<SiteRolesEnum> siteRoles = new ArrayList<SiteRolesEnum>();
                List<ProjectSiteConfigRolesEnum> projectSiteRoles = new ArrayList<ProjectSiteConfigRolesEnum>();
                for (int i = 0; i < filter.getValidRoles().length; i++)
                {
                    Role aRole = filter.getValidRoles()[i];
                    if (aRole instanceof SiteRolesEnum)
                        siteRoles.add((SiteRolesEnum) aRole);
                    else if (aRole instanceof ProjectSiteConfigRolesEnum)
                        projectSiteRoles.add((ProjectSiteConfigRolesEnum) aRole);
                }

                // get the sitePks from the valid site roles in the filter
                if (siteRoles.size() > 0)
                {
                    List<Integer> sitesPksFromRole = new ArrayList(authorizationManager.getEntitiesWithRole(
                            context, EntityTypeEnum.Site, siteRoles.toArray(new SiteRolesEnum[siteRoles.size()])));

                    // get the projectpks from the project_site_config table for
                    // these sites.
                    if (sitesPksFromRole.size() > 0)
                    {
                        String str = Arrays.deepToString(sitesPksFromRole.toArray());
                        str = str.replace('[', '(');
                        str = str.replace(']', ')');

                        List<Integer> projectPksFromSites = persistWrapper.readList(Integer.class,
                                "select distinct projectFk from project_site_config where siteFk in " + str);
                        ignoreEnableCheckSheetFilterSettingPkList.addAll(projectPksFromSites);
                    }
                }

                // now get the projectPks from the projectSiteconfig roles in
                // the validRoles
                if (projectSiteRoles.size() > 0)
                {
                    List<Integer> projectSitesPks = new ArrayList(
                            authorizationManager.getEntitiesWithRole(context, EntityTypeEnum.ProjectSiteConfig,
                                    projectSiteRoles.toArray(new ProjectSiteConfigRolesEnum[projectSiteRoles.size()])));

                    if (projectSitesPks.size() > 0)
                    {
                        String str = Arrays.deepToString(projectSitesPks.toArray());
                        str = str.replace('[', '(');
                        str = str.replace(']', ')');

                        List<Integer> projectPksFromProjectSiteRoles = persistWrapper.readList(Integer.class,
                                "select distinct projectFk from project_site_config where pk in " + str);
                        ignoreEnableCheckSheetFilterSettingPkList.addAll(projectPksFromProjectSiteRoles);
                    }
                }
            }
            // if we set valid roles, project level assignments are ignored by
            // default. getIncludeProjectAndUnitUserAssignments is set to true
            // to look for such projects also
            // if no roles are specified in the filter or if roles are specified
            // and this flag is set, look for the other project user
            // assignments.
            if (filter.getValidRoles() == null || filter.getIncludeProjectAndUnitUserAssignments() == true)
            {
                // where I am the manager
                ignoreEnableCheckSheetFilterSettingPkList.addAll(persistWrapper.readList(Integer.class,
                        "select pk from TAB_PROJECT where managerPk = ?", context.getUser().getPk()));

                // where i am the project coordinator or data clerk or readonly
                List<Integer> mgrProjects = persistWrapper.readList(Integer.class,
                        "select objectPk from TAB_USER_PERMS where userPk=? and objectType=? and role in (?)",
                        context.getUser().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_MANAGER);
                for (Iterator iterator = mgrProjects.iterator(); iterator.hasNext();)
                {
                    Integer integer = (Integer) iterator.next();
                    if (!(ignoreEnableCheckSheetFilterSettingPkList.contains(integer)))
                        ignoreEnableCheckSheetFilterSettingPkList.add(integer);
                }
                List<Integer> otherUserProjects = persistWrapper.readList(Integer.class,
                        "select objectPk from TAB_USER_PERMS where userPk=? and objectType=? and role in (?,?)",
                        context.getUser().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK,
                        UserPerms.ROLE_READONLY);
                for (Iterator iterator = otherUserProjects.iterator(); iterator.hasNext();)
                {
                    Integer integer = (Integer) iterator.next();
                    if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
                        respectEnableCheckSheetFilterSettingPkList.add(integer);
                }

                // acl readonly users for project. Now we need to change the
                // name of
                // this role.
                List<Integer> intList = authorizationManager.getEntitiesWithRole(context, EntityTypeEnum.Project,
                        ProjectRolesEnum.ReadOnlyUsers);
                for (Iterator iterator = intList.iterator(); iterator.hasNext();)
                {
                    Integer integer = (Integer) iterator.next();
                    if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
                        respectEnableCheckSheetFilterSettingPkList.add(integer);
                }

                // Projects where the user is added as a dataclerk in
                // tab_project_users
                List<Integer> pkList = persistWrapper.readList(Integer.class,
                        "select projectPk from TAB_PROJECT_USERS where userPk=? and workstationPk=? and role=?",
                        context.getUser().getPk(), dummyWorkstation.getPk(), User.ROLE_READONLY);
                for (Iterator iterator = pkList.iterator(); iterator.hasNext();)
                {
                    Integer integer = (Integer) iterator.next();
                    if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
                        respectEnableCheckSheetFilterSettingPkList.add(integer);
                }

                List<Integer> testerApproverList = persistWrapper.readList(Integer.class,
                        "(select projectPk from TAB_PROJECT_USERS where userPk = ?)" + " union "
                                + " (select upr.projectPk from unit_project_ref upr, TAB_UNIT u, TAB_UNIT_USERS uu "
                                + " where upr.unitPk = u.pk and u.pk = uu.unitPk and userPk = ?)",
                        context.getUser().getPk(), context.getUser().getPk());
                for (Iterator iterator = testerApproverList.iterator(); iterator.hasNext();)
                {
                    Integer integer = (Integer) iterator.next();
                    if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
                        respectEnableCheckSheetFilterSettingPkList.add(integer);
                }
            }

            if ((ignoreEnableCheckSheetFilterSettingPkList == null
                    || ignoreEnableCheckSheetFilterSettingPkList.size() == 0)
                    && (respectEnableCheckSheetFilterSettingPkList == null
                    || respectEnableCheckSheetFilterSettingPkList.size() == 0))
                return new ArrayList();

            StringBuffer mainSql = new StringBuffer("select * from (");
            List params = new ArrayList<>();
            String unionAllOp = "";
            if (ignoreEnableCheckSheetFilterSettingPkList != null
                    && ignoreEnableCheckSheetFilterSettingPkList.size() > 0)
            {
                String str = Arrays.deepToString(ignoreEnableCheckSheetFilterSettingPkList.toArray());
                str = str.replace('[', '(');
                str = str.replace(']', ')');

                mainSql.append(ProjectQuery.fetchSQL);
                if (filter.getUnitOID() != null)
                {
                    mainSql.append(" join unit_project_ref upr on upr.projectPk = project.pk and upr.unitPk = ? ");
                    params.add(filter.getUnitOID().getPk());
                }
                if (filter.getSiteOIDs() != null && filter.getSiteOIDs().size() > 0)
                {
                    mainSql.append(" join project_site_config psc on psc.projectFk = project.pk and psc.siteFk in ( ");
                    String sep = "";
                    for (Iterator iterator = filter.getSiteOIDs().iterator(); iterator.hasNext();)
                    {
                        SiteOID aSite = (SiteOID) iterator.next();
                        mainSql.append(sep).append("?");
                        params.add(aSite.getPk());
                        sep = ", ";
                    }
                    mainSql.append(") ");
                }
                mainSql.append(" where 1 = 1 ");

                if (StringUtils.isNotEmpty(filter.getSearchString()))
                {
                    mainSql.append(" and upper(projectName) like ?");
                    params.add("%" + filter.getSearchString().toUpperCase() + "%");
                }

                mainSql.append(" and project.status=? " + " and project.pk in " + str);
                params.add(Project.STATUS_OPEN);

                unionAllOp = " union ";
            }
            if (respectEnableCheckSheetFilterSettingPkList != null
                    && respectEnableCheckSheetFilterSettingPkList.size() > 0)
            {
                mainSql.append(unionAllOp);

                String str = Arrays.deepToString(respectEnableCheckSheetFilterSettingPkList.toArray());
                str = str.replace('[', '(');
                str = str.replace(']', ')');

                mainSql.append(ProjectQuery.fetchSQL);
                if (filter.getUnitOID() != null)
                {
                    mainSql.append(" join unit_project_ref upr on upr.projectPk = project.pk and upr.unitPk = ? ");
                    params.add(filter.getUnitOID().getPk());
                }
                if (filter.getSiteOIDs() != null && filter.getSiteOIDs().size() > 0)
                {
                    mainSql.append(" join project_site_config psc on psc.projectFk = project.pk and psc.siteFk in ( ");
                    String sep = "";
                    for (Iterator iterator = filter.getSiteOIDs().iterator(); iterator.hasNext();)
                    {
                        SiteOID aSite = (SiteOID) iterator.next();
                        mainSql.append(sep).append("?");
                        params.add(aSite.getPk());
                        sep = ", ";
                    }
                    mainSql.append(") ");
                }
                mainSql.append(" where 1 = 1 ");

                if (filter.isShowChecksheetEnabled() != null)
                {
                    if (filter.isShowChecksheetEnabled())
                        mainSql.append(" and project.enableChecksheets = 1");
                    else
                        mainSql.append(" and project.enableChecksheets = 0");

                }

                if (StringUtils.isNotEmpty(filter.getSearchString()))
                {
                    mainSql.append(" and upper(projectName) like ?");
                    params.add("%" + filter.getSearchString().toUpperCase() + "%");
                }

                mainSql.append(" and project.status=? " + " and project.pk in " + str);
                params.add(Project.STATUS_OPEN);

            }
            mainSql.append(") projects order by projects.createdDate desc");

            List<ProjectQuery> list = persistWrapper.readList(ProjectQuery.class, mainSql.toString(),
                    params.toArray(new Object[params.size()]));
            if (list != null)
            {
                List<Integer> projectPks = list.stream().map(query -> query.getPk()).collect(Collectors.toList());
                HashMap<ProjectOID, Integer> projectUnit = null;
                try {
                    projectUnit = getUnitCount(projectPks, false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < list.size(); i++)
                {
                    if (projectUnit.containsKey(list.get(i).getOID()))
                    {
                        list.get(i).setUnitCount(projectUnit.get(list.get(i).getOID()));
                    }
                }
            }

            return list;

        }

    }
    public  List<Project> getProjectsAtSite(SiteOID siteOID)
    {
        String sql = "select p.* from tab_project p join project_site_config psc on psc.projectFk = p.pk "
                + "where p.status= ? and psc.siteFk = ? and estatus = 1 order by p.createdDate desc ";

        return persistWrapper.readList(Project.class, sql, Project.STATUS_OPEN, siteOID.getPk());
    }
    public  Project getProjectByName(String projectName) throws Exception
    {
        List<Project> pList = (List<Project>) persistWrapper.readList(Project.class,
                "select * from TAB_PROJECT where projectName = ?", projectName);
        if (pList.size() > 0)
            return pList.get(0);
        else
            return null;
    }
    public Project getProject(long projectPk)
    {
        return (Project) persistWrapper.readByPrimaryKey(Project.class, projectPk);
    }
    public  ProjectQuery getProjectByPk(int projectPk)
    {
        ProjectQuery projectQuery = persistWrapper.read(ProjectQuery.class,
                ProjectQuery.fetchSQL + " where 1 = 1 and project.pk=?", projectPk);
        if (projectQuery != null)
        {
            HashMap<ProjectOID, Integer> projectUnit = null;
            try {
                projectUnit = getUnitCount(Arrays.asList(projectQuery.getPk()), false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (projectUnit.containsKey(projectQuery.getOID()))
            {
                projectQuery.setUnitCount(projectUnit.get(projectQuery.getOID()));
            }

        }

        return projectQuery;
    }
    public  List<Project> getActiveProjects() throws Exception
    {
        List list = persistWrapper.readList(Project.class, "select * from TAB_PROJECT where status =?",
                new Object[] { Project.STATUS_OPEN });
        return list;
    }

    /**
     * @param acc
     * @param projectName
     * @return
     */
    public  boolean isProjectNameExist(Account acc, String projectName) throws Exception
    {
        List list = persistWrapper.readList(Project.class,
                "select * from TAB_PROJECT where accountPk=? and " + "projectName =?", acc.getPk(), projectName);
        if (list.size() > 0)
        {
            return true;
        } else
        {
            return false;
        }
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
    public  boolean isProjectNameExistForAnotherProject(String projectName, int projectPk) throws Exception
    {
        try
        {
            List list = persistWrapper.readList(Project.class, "select * from TAB_PROJECT where projectName =?",
                    new Object[] { projectName });
            if (list.size() == 0)
            {
                return false;
            } else if (list.size() == 1 && ((Project) list.get(0)).getPk() == projectPk)
            {
                return false;
            } else
            {
                return true;
            }

        }
        catch (Exception e)
        {
            logger.error("Error getting units for project " + " :: " + e.getMessage());
            if (logger.isDebugEnabled())
            {
                logger.debug(e.getMessage(), e);
            }
            throw new Exception();
        }
    }

    public  ProjectQuery createProject(UserContext context, Project project) throws Exception
    {
        Account acc = (Account) context.getAccount();
        User user = (User) context.getUser();

        if (isProjectNameExist(acc, project.getProjectName()))
        {
            throw new AppException("Duplicate project name, Please choose a different project name.");
        }

        project.setAccountPk((int) acc.getPk());
        project.setCreatedBy((int) user.getPk());
        project.setCreatedDate(new Date());
        project.setStatus(Project.STATUS_OPEN);

        int pk = (int) persistWrapper.createEntity(project);

        // fetch the new project back

        ProjectQuery projectQuery = persistWrapper.read(ProjectQuery.class,
                ProjectQuery.fetchSQL + " where 1 = 1 and project.pk=?", pk);
        if (projectQuery != null)
        {
            HashMap<ProjectOID, Integer> projectUnit = getUnitCount(Arrays.asList(projectQuery.getPk()), false);
            if (projectUnit.containsKey(projectQuery.getOID()))
            {
                projectQuery.setUnitCount(projectUnit.get(projectQuery.getOID()));
            }

        }

        return projectQuery;

    }
    public  ProjectQuery updateProject(UserContext context, Project project) throws Exception
    {

        String name = project.getProjectName();
        if (isProjectNameExistForAnotherProject(name, (int) project.getPk()))
        {
            throw new AppException("Duplicate project name, Please choose a different project name.");
        }

        persistWrapper.update(project);
        ProjectQuery projectQuery = persistWrapper.read(ProjectQuery.class,
                ProjectQuery.fetchSQL + " where 1 = 1 and project.pk=?", project.getPk());
        if (projectQuery != null)
        {
            HashMap<ProjectOID, Integer> projectUnit = getUnitCount(Arrays.asList(projectQuery.getPk()), false);
            if (projectUnit.containsKey(projectQuery.getOID()))
            {
                projectQuery.setUnitCount(projectUnit.get(projectQuery.getOID()));
            }

        }

        return projectQuery;
    }
    public  HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren) throws Exception {
        HashMap<ProjectOID, Integer> result = new HashMap<ProjectOID, Integer>();
        List<Object> params = new ArrayList<Object>();

        StringBuffer sql = new StringBuffer(
                "select upr.projectPk as projectPk, project.projectName as projectName, project.projectDescription as projectDescription, count(*) as unitCount from TAB_UNIT u ");
        sql.append(" join unit_project_ref upr on upr.unitPk = u.pk ");
        if (projectPks != null && projectPks.size() > 0)
        {
            sql.append(" and upr.projectPk in (");

            String ssep = "";
            for (Integer pk : projectPks)
            {
                sql.append(ssep).append("?");
                ssep = ",";
                params.add(pk);
            }
            sql.append(") ");
        }
        sql.append(" join TAB_PROJECT project on project.pk=upr.projectPk");
        sql.append(
                " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo");
        sql.append(" where upr.unitOriginType = ? and uprh.status != 'Removed' ");
        params.add(UnitOriginType.Manufactured.name());
        if (!includeChildren)
        {
            sql.append(" and uprh.parentPk is null ");
        }
        sql.append("group by upr.projectPk");
        List<Map<String, Object>> fetchedData = persistWrapper.readListAsMap(sql.toString(), params.toArray());
        if (fetchedData != null)
        {
            for (Map<String, Object> mapData : fetchedData)
            {
                int projPk = (int) mapData.get("projectpk");
                String projectName = (String) mapData.get("projectname");
                String projectDescription = (String) mapData.get("projectdescription");
                long unitCount = (long) mapData.get("unitcount");
                result.put(
                        new ProjectOID(projPk,
                                projectName + ((projectDescription != null) ? (" - " + projectDescription) : "")),
                        (int) unitCount);
            }
        }
        return result;

    }
    public  ProjectPart getProjectPart(ProjectPartOID projectpartOID)
    {
        return (ProjectPart) persistWrapper.readByPrimaryKey(ProjectPart.class, projectpartOID.getPk());
    }
    public  void addFormToProjectPart(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                                            WorkstationOID workstationOID, int formPk, String testName) throws Exception
    {
        ProjectForm pForm = new ProjectForm();
        pForm.setProjectPk(projectOID.getPk());
        pForm.setProjectPartPk(projectPartOID.getPk());
        pForm.setWorkstationPk(workstationOID.getPk());
        pForm.setFormPk(formPk);
        if (testName != null && testName.trim().length() > 0)
            pForm.setName(testName);

        pForm.setAppliedByUserFk(context.getUser().getPk());

        persistWrapper.createEntity(pForm);
    }

    public  void deleteProjectForm(UserContext context, ProjectFormOID projectFormOID) throws Exception
    {
        ProjectForm pForm = (ProjectForm) persistWrapper.readByPrimaryKey(ProjectForm.class, projectFormOID.getPk());
        persistWrapper.deleteEntity(pForm);
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
    public  List<FormQuery> getFormsForProject(ProjectOID projectOID) throws Exception
    {
        FormFilter filter = new FormFilter();
        filter.setStatus(new String[] { Survey.STATUS_OPEN });
        filter.setShowAllFormRevisions(true);
        filter.setProjectFormFilter(filter.new ProjectFormAssignmentFilter(projectOID, true));
        ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
        req.setFilter(filter);
        req.setFetchRowCount(false);
        req.setFetchAllRows(true);
        ReportResponse response = new FormListReport().runReport(req);
        List<FormQuery> list = (List<FormQuery>) response.getReportData();
        if (list != null)
            return list;

        return new ArrayList();
    }

    public  List<FormQuery> getFormsForProject(int projectPk, WorkstationOID workstationOID) throws Exception
    {
        FormFilter filter = new FormFilter();
        filter.setStatus(new String[] { Survey.STATUS_OPEN });
        filter.setShowAllFormRevisions(true);
        filter.setProjectFormFilter(filter.new ProjectFormAssignmentFilter(new ProjectOID(projectPk), workstationOID));
        ReportRequest req = new ReportRequest(ReportTypes.FormListReport);
        req.setFilter(filter);
        req.setFetchRowCount(false);
        req.setFetchAllRows(true);
        ReportResponse response = new FormListReport().runReport(req);
        List<FormQuery> list = (List<FormQuery>) response.getReportData();
        if (list != null)
            return list;

        return new ArrayList();
    }

    public  void addUserToProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
                                        int workstationPk, int userPk, String role) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk(projectPk);
        if (projectPartOID != null)
            pUser.setProjectPartPk((int) projectPartOID.getPk());
        pUser.setWorkstationPk(workstationPk);
        pUser.setUserPk(userPk);
        pUser.setRole(role);

        persistWrapper.createEntity(pUser);
    }

    public  void addUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID, int userPk,
                                        String role) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk(projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk(userPk);
        pUser.setRole(role);

        persistWrapper.createEntity(pUser);
    }

    public  void addReadonlyUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID,
                                                int userPk) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk(projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk(userPk);
        pUser.setRole(User.ROLE_READONLY);

        persistWrapper.createEntity(pUser);
    }
    public  void updateProjectTeam(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
                                         WorkstationOID wsOID, Collection testList, Collection verifyList, Collection approveList) throws Exception
    {
        List<User> workstationTesters = new ArrayList();
        if (projectPartOID != null)
            workstationTesters = projectService.getUsersForProjectPartInRole((int) projectOID.getPk(), projectPartOID, wsOID,
                    User.ROLE_TESTER);
        else
            workstationTesters = projectService.getUsersForProjectInRole(projectOID.getPk(), wsOID, User.ROLE_TESTER);
        if (testList != null)
        {
            for (Iterator iterator = testList.iterator(); iterator.hasNext();)
            {
                User newUser = (User) iterator.next();
                boolean containsNow = workstationTesters.remove(newUser);
                if (!(containsNow))
                {
                    addUserToProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) newUser.getPk(),
                            User.ROLE_TESTER);
                }
            }
        }
        for (Iterator iterator = workstationTesters.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            if (projectPartOID != null)
                removeUserFromProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) user.getPk(),
                        User.ROLE_TESTER);
            else
                removeUserFromProject(context, (int) projectOID.getPk(), (int) wsOID.getPk(), (int) user.getPk(), User.ROLE_TESTER);

        }

        List<User> workstationVerifiers = new ArrayList();
        if (projectPartOID != null)
            workstationVerifiers = projectService.getUsersForProjectPartInRole((int) projectOID.getPk(), projectPartOID,
                    wsOID, User.ROLE_VERIFY);
        else
            workstationVerifiers = projectService.getUsersForProjectInRole(projectOID.getPk(), wsOID, User.ROLE_VERIFY);
        if (verifyList != null)
        {
            for (Iterator iterator = verifyList.iterator(); iterator.hasNext();)
            {
                User newUser = (User) iterator.next();
                boolean containsNow = workstationVerifiers.remove(newUser);
                if (!(containsNow))
                {
                    addUserToProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) newUser.getPk(),
                            User.ROLE_VERIFY);
                }
            }
        }
        for (Iterator iterator = workstationVerifiers.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            if (projectPartOID != null)
                removeUserFromProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) user.getPk(),
                        User.ROLE_VERIFY);
            else
                removeUserFromProject(context, (int) projectOID.getPk(), (int) wsOID.getPk(), (int) user.getPk(), User.ROLE_VERIFY);
        }

        List<User> workstationApprovers = new ArrayList();

        if (projectPartOID != null)
            workstationApprovers = projectService.getUsersForProjectPartInRole((int) projectOID.getPk(), projectPartOID,
                    wsOID, User.ROLE_APPROVE);
        else
            workstationApprovers = projectService.getUsersForProjectInRole(projectOID.getPk(), wsOID,
                    User.ROLE_APPROVE);

        if (approveList != null)
        {
            for (Iterator iterator = approveList.iterator(); iterator.hasNext();)
            {
                User newUser = (User) iterator.next();
                boolean containsNow = workstationApprovers.remove(newUser);
                if (!(containsNow))
                {
                    addUserToProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) newUser.getPk(),
                            User.ROLE_APPROVE);
                }
            }
        }
        for (Iterator iterator = workstationApprovers.iterator(); iterator.hasNext();)
        {
            User user = (User) iterator.next();
            if (projectPartOID != null)
                removeUserFromProject(context, (int) projectOID.getPk(), projectPartOID, (int) wsOID.getPk(), (int) user.getPk(),
                        User.ROLE_APPROVE);
            else
                removeUserFromProject(context, (int) projectOID.getPk(), (int) wsOID.getPk(), (int) user.getPk(), User.ROLE_APPROVE);
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
    public  List<User> getUsersForProjectPartInRole(int projectPk, ProjectPartOID projectPartOID,
                                                          WorkstationOID workstationOID, String roleName) throws Exception
    {
        if (projectPartOID == null) // we need the ones where the projectPartPk
        // is null which is the default list
        {
            return new ArrayList();
        } else
        {
            return persistWrapper.readList(User.class, "select distinct u.* from TAB_USER u "
                            + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
                            + " where tpu.projectPk=? and tpu.projectPartPk = ? and tpu.workstationPk=? and tpu.role=? order by u.firstName asc",
                    projectPk, projectPartOID.getPk(), workstationOID.getPk(), roleName);
        }
    }



    public  List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName)
            throws Exception
    {
        return persistWrapper.readList(User.class, "select distinct u.* from TAB_USER u "
                        + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
                        + "where tpu.projectPk=? and tpu.projectPartPk is null and tpu.workstationPk=? and tpu.role=? order by firstName asc",
                projectPk, workstationOID.getPk(), roleName);
    }

    public  List<User> getUsersForProject(int projectPk, WorkstationOID workstationOID) throws Exception
    {
        return persistWrapper.readList(User.class,
                "select distinct u.* from TAB_USER u " + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
                        + " where tpu.projectPk=? and tpu.workstationPk=? order by firstName asc",
                projectPk, workstationOID.getPk());
    }

    public  List<Project> getProjectsWhereTheUserIsReadOnly(UserContext context)
    {
        try
        {
            return persistWrapper.readList(Project.class,
                    "select distinct p.* from TAB_PROJECT p "
                            + " inner join TAB_PROJECT_USERS tpu on tpu.projectPk = p.pk "
                            + " where tpu.userPk=? and tpu.workstationPk=? and tpu.role in (?, ?, ?)",
                    context.getUser().getPk(), dummyWorkstation.getPk(), User.ROLE_TESTER, User.ROLE_VERIFY,
                    User.ROLE_READONLY);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  boolean isUserCoordinatorForProject(UserContext userContext, int projectPk)
    {
        try
        {
            Project p = (Project) persistWrapper.readByPrimaryKey(Project.class, projectPk);
            if (p != null && p.getManagerPk() != null && p.getManagerPk() == userContext.getUser().getPk())
            {
                return true;
            }

            List<UserPerms> l = persistWrapper.readList(UserPerms.class,
                    "select * from TAB_USER_PERMS where objectPk=? and objectType=? " + "and userPk=? ", projectPk,
                    UserPerms.OBJECTTYPE_PROJECT, userContext.getUser().getPk());
            for (Iterator iterator = l.iterator(); iterator.hasNext();)
            {
                UserPerms userPerms = (UserPerms) iterator.next();
                if (UserPerms.ROLE_MANAGER.equals(userPerms.getRole()))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public  void removeUserFromProject(UserContext context, int projectPk, int workstationPk, int userPk,
                                             String role) throws Exception
    {
        persistWrapper.delete(
                "delete from TAB_PROJECT_USERS where projectPk=? and projectPartPk is null and workstationPk=? and userPk = ? and role = ?",
                projectPk, workstationPk, userPk, role);
    }

    public  void removeUserFromProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
                                             int workstationPk, int userPk, String role) throws Exception
    {
        persistWrapper.delete(
                "delete from TAB_PROJECT_USERS where projectPk=? and projectPartPk = ? and workstationPk=? and userPk = ? and role = ?",
                projectPk, projectPartOID.getPk(), workstationPk, userPk, role);
    }

    public  void removeAllUsersFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
            throws Exception
    {
        persistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk=? and workstationPk=?", projectPk,
                workstationOID.getPk());
    }

    public  boolean isUsersForProjectInRole(int userPk, ProjectOID projectOID, WorkstationOID workstationOID,
                                                  String roleName)
    {
        List list = null;
        try
        {
            list = persistWrapper.readList(User.class,
                    "select distinct u.* from TAB_USER u, TAB_PROJECT_USERS uu where uu.userPk = u.pk and u.pk = ? and uu.projectPk=? and uu.workstationPk=? and uu.role=?",
                    userPk, projectOID.getPk(), workstationOID.getPk(), roleName);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (list != null && list.size() > 0)
        {
            return true;
        } else
        {
            return false;
        }
    }
    public  void deleteProject(int projectPk) throws Exception
    {
        int unitCount = persistWrapper.read(Integer.class,
                "select count(u.pk) from TAB_UNIT u join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk=?",
                projectPk);
        if (unitCount == 0)
        {
            persistWrapper.delete("delete from TAB_PROJECT_FORMS where projectPk=?", projectPk);
            persistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk=?", projectPk);
            persistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=?", projectPk);
            persistWrapper.delete("delete from TAB_PROJECT where pk=?", projectPk);
        } else
        {
            List errors = new ArrayList();
            errors.add("Project cannot be deleted if Units are created for the project.");
            throw new AppException((String[]) errors.toArray(new String[errors.size()]));
        }
    }
    public  void closeProject(ProjectQuery projectQuery) throws Exception
    {
        // TODO some other logic need to be done when a project is closed
        Project p = (Project) persistWrapper.readByPrimaryKey(Project.class, projectQuery.getPk());
        p.setStatus(Project.STATUS_CLOSED);
        persistWrapper.update(p);
    }

    public  void openProject(ProjectQuery projectQuery) throws Exception
    {
        // TODO some other process need to be done when a project is opened
        Project p = (Project) persistWrapper.readByPrimaryKey(Project.class, projectQuery.getPk());
        p.setStatus(Project.STATUS_OPEN);
        persistWrapper.update(p);
    }
    public  List<ProjectFormQuery> getProjectFormAssignmentsForForm(FormMainOID formMainOID) throws Exception
    {
        return persistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and form.formMainPk = ?",
                formMainOID.getPk());
    }
    public  List<User> getProjectManagers(ProjectOID projectOID)
    {
        try
        {
            List<User> returnList = new ArrayList<User>();
            Project proj = getProject(projectOID.getPk());
            if (proj.getManagerPk() != 0)
            {
                User mgr = (User) persistWrapper.readByPrimaryKey(User.class, proj.getManagerPk());
                returnList.add(mgr);
            }

            List<User> existingMgrACList = accountService.getACLs((int) proj.getPk(), UserPerms.OBJECTTYPE_PROJECT,
                    UserPerms.ROLE_MANAGER);
            if (existingMgrACList != null)
                returnList.addAll(existingMgrACList);

            return returnList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }

    public  List<User> getDataClerks(ProjectQuery projectQuery) throws Exception
    {
        List<User> existingDataClerkACList = accountService.getACLs(projectQuery.getPk(), UserPerms.OBJECTTYPE_PROJECT,
                UserPerms.ROLE_DATACLERK);
        return existingDataClerkACList;
    }


    public  ProjectStage getProjectStage(ProjectStageOID projectStageOID)
    {
        return persistWrapper.readByPrimaryKey(ProjectStage.class, projectStageOID.getPk());
    }

    public  List<ProjectStage> getProjectStages(ProjectOID projectOID)
    {
        try
        {
            return persistWrapper.readList(ProjectStage.class,
                    "select * from project_stage where projectFk = ? and estatus = 1", projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ProjectStage addProjectStage(ProjectStage projectStage) throws Exception
    {
        if(projectStage.getProjectFk() == 0)
            throw new AppException("Project not specified; Cannot add stage");
        Project project = getProject(projectStage.getProjectFk());
        if(project == null)
            throw new AppException("Invalid Project; Cannot add stage");

        projectStage.setEstatus(EStatusEnum.Active.getValue());
        int newPk = (int) persistWrapper.createEntity(projectStage);

        return (ProjectStage) persistWrapper.readByPrimaryKey(ProjectStage.class, newPk);
    }

    public  ProjectStage updateProjectStage(ProjectStage projectStage) throws Exception
    {
        ProjectStage ps = (ProjectStage) persistWrapper.readByPrimaryKey(ProjectStage.class, projectStage.getPk());
        if(ps == null)
            throw new AppException("Inalid Project stage; remove failed");

        if(projectStage.getProjectFk() == 0)
            throw new AppException("Project not specified; Cannot add stage");
        Project project = getProject(projectStage.getProjectFk());
        if(project == null)
            throw new AppException("Invalid Project; Cannot add stage");

        ps.setDescription(projectStage.getDescription());
        ps.setName(projectStage.getName());
        ps.setProjectFk(projectStage.getProjectFk());
        ps.setEstatus(EStatusEnum.Active.getValue());

        persistWrapper.update(ps);
        return (ProjectStage) persistWrapper.readByPrimaryKey(ProjectStage.class, projectStage.getPk());
    }

    public  void removeProjectStage(int projectStagePk) throws Exception
    {
        ProjectStage ps = (ProjectStage) persistWrapper.readByPrimaryKey(ProjectStage.class, projectStagePk);
        if(ps == null)
            throw new AppException("Inalid Project stage; remove failed");

        ps.setEstatus(EStatusEnum.Deleted.getValue());
        persistWrapper.update(ps);
    }

    public ProjectSignatorySetBean getProjectSignatorySet(ProjectSignatorySetOID sigSetOID)
    {
        ProjectSignatorySet set = persistWrapper.readByPrimaryKey(ProjectSignatorySet.class, sigSetOID.getPk());
        if(set != null)
            return set.getBean();

        return null;
    }

    public  List<ProjectSignatorySetBean> getProjectSignatorySets(ProjectOID projectOID)
    {
        List<ProjectSignatorySet> list = persistWrapper.readList(ProjectSignatorySet.class,
                "select * from project_signatory_set where projectFk = ? and estatus = 1", projectOID.getPk());

        List<ProjectSignatorySetBean> returnList = new ArrayList<>();
        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            ProjectSignatorySet set = (ProjectSignatorySet) iterator.next();
            returnList.add(set.getBean());
        }
        return returnList;
    }
    public  ProjectSignatorySetBean addProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {
        if(sBean.getProjectFk() == 0)
            throw new AppException("Project not specified; Cannot add signatory");
        Project project = getProject(sBean.getProjectFk());
        if(project == null)
            throw new AppException("Invalid Project; Cannot add signatory");

        ProjectSignatorySet set = new ProjectSignatorySet();
        set.setDescription(sBean.getDescription());
        set.setEstatus(EStatusEnum.Active.getValue());
        set.setName(sBean.getName());
        set.setProjectFk(sBean.getProjectFk());
        int pk = persistWrapper.createEntity(set);

        for (Iterator iterator = sBean.getSigatoryItems().iterator(); iterator.hasNext();)
        {
            ProjectSignatoryItemBean aItemBean = (ProjectSignatoryItemBean) iterator.next();
            ProjectSignatoryItem item = new ProjectSignatoryItem();
            item.setOrderNo(aItemBean.getOrderNo());
            item.setProjectSignatorySetFk(pk);
            item.setRoleId(aItemBean.getRoleId());
            persistWrapper.createEntity(item);
        }

        return getProjectSignatorySet(new ProjectSignatorySetOID(pk));
    }

    public  ProjectSignatorySetBean updateProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
    {
        if(sBean.getPk() == 0)
        {
            logger.error("Pk cannot be 0 when updating");
            throw new AppException("Invalid request.");
        }
        ProjectSignatorySet set = persistWrapper.readByPrimaryKey(ProjectSignatorySet.class, sBean.getPk());
        if(set == null)
        {
            logger.error("Invalid SignatorySet Pk: " + sBean.getPk());
            throw new AppException("Invalid request");
        }

        if(sBean.getProjectFk() != set.getProjectFk())
        {
            throw new AppException("Invalid project specified in request");
        }

        Project project = getProject(sBean.getProjectFk());
        if(project == null)
            throw new AppException("Invalid Project; Cannot update signatory");


        List<ProjectSignatoryItem> currentItems = persistWrapper.readList(ProjectSignatoryItem.class,
                "select * from project_signatory_item where projectSignatorySetFk = ? ", sBean.getPk());
        HashMap<Integer, ProjectSignatoryItem> currentItemsMap = new HashMap<>();
        for (Iterator iterator = currentItems.iterator(); iterator.hasNext();)
        {
            ProjectSignatoryItem aItem = (ProjectSignatoryItem) iterator.next();
            currentItemsMap.put(aItem.getPk(), aItem);
        }

        set.setDescription(sBean.getDescription());
        set.setName(sBean.getName());
        persistWrapper.update(set);

        for (Iterator iterator = sBean.getSigatoryItems().iterator(); iterator.hasNext();)
        {
            ProjectSignatoryItemBean aItemBean = (ProjectSignatoryItemBean) iterator.next();

            if(aItemBean.getPk() > 0)
            {
                ProjectSignatoryItem theItem = currentItemsMap.remove(aItemBean.getPk());
                if(theItem == null)
                {
                    throw new AppException("Invalid request, Please refresh and try again later.");
                }
                theItem.setOrderNo(aItemBean.getOrderNo());
                theItem.setRoleId(aItemBean.getRoleId());
                persistWrapper.update(theItem);
            }
            else
            {
                ProjectSignatoryItem theItem = new ProjectSignatoryItem();
                theItem.setProjectSignatorySetFk(set.getPk());
                theItem.setOrderNo(aItemBean.getOrderNo());
                theItem.setRoleId(aItemBean.getRoleId());
                persistWrapper.createEntity(theItem);
            }
        }

        //what ever is remaining in the current HashMap needs to be removed.
        for (Iterator iterator = currentItemsMap.values().iterator(); iterator.hasNext();)
        {
            ProjectSignatoryItem projectSignatoryItem = (ProjectSignatoryItem) iterator.next();
            persistWrapper.deleteEntity(projectSignatoryItem);
        }

        return getProjectSignatorySet(new ProjectSignatorySetOID(23));
    }

    public  void removeProjectSignatorySet(ProjectSignatorySetOID setOID) throws Exception
    {
        ProjectSignatorySet set = persistWrapper.readByPrimaryKey(ProjectSignatorySet.class, setOID.getPk());
        if(set == null)
        {
            logger.error("Invalid SignatorySet Pk: " + setOID.getPk());
            throw new AppException("Invalid request");
        }

        set.setEstatus(EStatusEnum.Deleted.getValue());
        persistWrapper.update(set);
    }
    public  List<Project> getProjectsForPart(PartOID partOID)
    {
        try
        {
            return persistWrapper.readList(Project.class,
                    "select * from TAB_PROJECT where pk in (select projectPk from project_part where partPk = ? and estatus = 1)",
                    partOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public  List<ProjectPartQuery> getProjectPartsWithTeams(ProjectOID projectOID, WorkstationOID workstationOID)
    {
        try
        {
            return persistWrapper.readList(ProjectPartQuery.class, ProjectPartQuery.sql
                            + " and pp.pk in (select distinct projectPartPk from TAB_PROJECT_USERS where projectPk = ? and workstationPk = ? )",
                    projectOID.getPk(), workstationOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    /**
     * Get all project part specific forms setup for the project.
     * For part specific forms, we store them in the same tab_project_forms table with workstationPk as null
     * @param projectOID
     * @param projectPartOID
     * @return
     */
    public  List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID)
    {
        try
        {
            return persistWrapper.readList(ProjectFormQuery.class,
                    ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? and pf.workstationPk is null  ",
                    projectOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get project part specific forms setup for a part on the project.
     * For part specific forms, we store them in the same tab_project_forms table with workstationPk as null
     * @param projectOID
     * @param projectPartOID
     * @return
     */
    public  List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID,
                                                                     ProjectPartOID projectPartOID)
    {
        try
        {
            return persistWrapper.readList(ProjectFormQuery.class,
                    ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? and projectPartPk = ? and pf.workstationPk is null  ",
                    projectOID.getPk(), projectPartOID.getPk());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public  List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID)
    {
        return persistWrapper.readList(ProjectWorkstation.class,
                "select * from TAB_PROJECT_WORKSTATIONS where projectPk = ?",
                projectOID.getPk());

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
        pUser.setProjectPk((int) projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk((int) userPk);
        pUser.setRole(role);

        persistWrapper.createEntity(pUser);
    }

    public  void addReadonlyUserToProject(UserContext context, long projectPk, WorkstationOID workstationOID,
                                          long userPk) throws Exception
    {
        ProjectUser pUser = new ProjectUser();
        pUser.setProjectPk((int) projectPk);
        pUser.setWorkstationPk((int) workstationOID.getPk());
        pUser.setUserPk((int) userPk);
        pUser.setRole(User.ROLE_READONLY);

        persistWrapper.createEntity(pUser);
    }


}
