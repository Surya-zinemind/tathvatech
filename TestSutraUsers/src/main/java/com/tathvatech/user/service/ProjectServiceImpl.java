/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Hari
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
@Service
public class ProjectServiceImpl
{
	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	private final PersistWrapper persistWrapper;

    public ProjectServiceImpl(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

    // TODO:: implement a cache with this hashmap
	// private static HashMap surveyMap = new HashMap();
	public static List<ProjectQuery> getProjectList(UserContext context, ProjectFilter filter)
	{
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

			List<ProjectQuery> list = PersistWrapper.readList(ProjectQuery.class, sb.toString(), params.toArray());
			if (list != null)
			{
				List<Integer> projectPks = list.stream().map(query -> query.getPk()).collect(Collectors.toList());
				HashMap<ProjectOID, Integer> projectUnit = getUnitCount(projectPks, false);
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
					List<Integer> sitesPksFromRole = new ArrayList(new AuthorizationDelegate().getEntitiesWithRole(
							context, EntityTypeEnum.Site, siteRoles.toArray(new SiteRolesEnum[siteRoles.size()])));

					// get the projectpks from the project_site_config table for
					// these sites.
					if (sitesPksFromRole.size() > 0)
					{
						String str = Arrays.deepToString(sitesPksFromRole.toArray());
						str = str.replace('[', '(');
						str = str.replace(']', ')');

						List<Integer> projectPksFromSites = PersistWrapper.readList(Integer.class,
								"select distinct projectFk from project_site_config where siteFk in " + str);
						ignoreEnableCheckSheetFilterSettingPkList.addAll(projectPksFromSites);
					}
				}

				// now get the projectPks from the projectSiteconfig roles in
				// the validRoles
				if (projectSiteRoles.size() > 0)
				{
					List<Integer> projectSitesPks = new ArrayList(
							new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.ProjectSiteConfig,
									projectSiteRoles.toArray(new ProjectSiteConfigRolesEnum[projectSiteRoles.size()])));

					if (projectSitesPks.size() > 0)
					{
						String str = Arrays.deepToString(projectSitesPks.toArray());
						str = str.replace('[', '(');
						str = str.replace(']', ')');

						List<Integer> projectPksFromProjectSiteRoles = PersistWrapper.readList(Integer.class,
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
				ignoreEnableCheckSheetFilterSettingPkList.addAll(PersistWrapper.readList(Integer.class,
						"select pk from TAB_PROJECT where managerPk = ?", context.getUser().getPk()));

				// where i am the project coordinator or data clerk or readonly
				List<Integer> mgrProjects = PersistWrapper.readList(Integer.class,
						"select objectPk from TAB_USER_PERMS where userPk=? and objectType=? and role in (?)",
						context.getUser().getPk(), UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_MANAGER);
				for (Iterator iterator = mgrProjects.iterator(); iterator.hasNext();)
				{
					Integer integer = (Integer) iterator.next();
					if (!(ignoreEnableCheckSheetFilterSettingPkList.contains(integer)))
						ignoreEnableCheckSheetFilterSettingPkList.add(integer);
				}
				List<Integer> otherUserProjects = PersistWrapper.readList(Integer.class,
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
				List<Integer> intList = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Project,
						ProjectRolesEnum.ReadOnlyUsers);
				for (Iterator iterator = intList.iterator(); iterator.hasNext();)
				{
					Integer integer = (Integer) iterator.next();
					if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
						respectEnableCheckSheetFilterSettingPkList.add(integer);
				}

				// Projects where the user is added as a dataclerk in
				// tab_project_users
				List<Integer> pkList = PersistWrapper.readList(Integer.class,
						"select projectPk from TAB_PROJECT_USERS where userPk=? and workstationPk=? and role=?",
						context.getUser().getPk(), DummyWorkstation.getPk(), User.ROLE_READONLY);
				for (Iterator iterator = pkList.iterator(); iterator.hasNext();)
				{
					Integer integer = (Integer) iterator.next();
					if (!(respectEnableCheckSheetFilterSettingPkList.contains(integer)))
						respectEnableCheckSheetFilterSettingPkList.add(integer);
				}

				List<Integer> testerApproverList = PersistWrapper.readList(Integer.class,
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

			List<ProjectQuery> list = PersistWrapper.readList(ProjectQuery.class, mainSql.toString(),
					params.toArray(new Object[params.size()]));
			if (list != null)
			{
				List<Integer> projectPks = list.stream().map(query -> query.getPk()).collect(Collectors.toList());
				HashMap<ProjectOID, Integer> projectUnit = getUnitCount(projectPks, false);
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

	/**
	 * @param projectPk
	 * @return
	 */
	public static Project getProject(int projectPk)
	{
		return PersistWrapper.readByPrimaryKey(Project.class, projectPk);
	}

	/**
	 * @param projectPk
	 * @return
	 */
	public static Project getProjectByName(String projectName) throws Exception
	{
		List<Project> pList = (List<Project>) PersistWrapper.readList(Project.class,
				"select * from TAB_PROJECT where projectName = ?", projectName);
		if (pList.size() > 0)
			return pList.get(0);
		else
			return null;
	}

	public static List<Project> getProjectsAtSite(SiteOID siteOID)
	{
		String sql = "select p.* from tab_project p join project_site_config psc on psc.projectFk = p.pk "
				+ "where p.status= ? and psc.siteFk = ? and estatus = 1 order by p.createdDate desc ";

		return PersistWrapper.readList(Project.class, sql, Project.STATUS_OPEN, siteOID.getPk());
	}

	/**
	 * @param projectPk
	 * @return
	 */
	public static ProjectQuery getProjectByPk(int projectPk)
	{
		ProjectQuery projectQuery = PersistWrapper.read(ProjectQuery.class,
				ProjectQuery.fetchSQL + " where 1 = 1 and project.pk=?", projectPk);
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

	public static List<Project> getActiveProjects() throws Exception
	{
		List list = PersistWrapper.readList(Project.class, "select * from TAB_PROJECT where status =?",
				new Object[] { Project.STATUS_OPEN });
		return list;
	}

	/**
	 * @param acc
	 * @param projectName
	 * @return
	 */
	public static boolean isProjectNameExist(Account acc, String projectName) throws Exception
	{
		List list = PersistWrapper.readList(Project.class,
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
	public static boolean isProjectNameExistForAnotherProject(String projectName, int projectPk) throws Exception
	{
		try
		{
			List list = PersistWrapper.readList(Project.class, "select * from TAB_PROJECT where projectName =?",
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

	public static ProjectQuery createProject(UserContext context, Project project) throws Exception
	{
		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		if (isProjectNameExist(acc, project.getProjectName()))
		{
			throw new AppException("Duplicate project name, Please choose a different project name.");
		}

		project.setAccountPk(acc.getPk());
		project.setCreatedBy(user.getPk());
		project.setCreatedDate(new Date());
		project.setStatus(Project.STATUS_OPEN);

		int pk = PersistWrapper.createEntity(project);

		// fetch the new project back

		ProjectQuery projectQuery = PersistWrapper.read(ProjectQuery.class,
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

	public static List<UnitQuery> getUnitsBySerialNos(String[] serialNos, ProjectOID projectOID)
	{
		StringBuffer sb = new StringBuffer(UnitQuery.sql);

		List<Object> params = new ArrayList();
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());

		sb.append(" where 1 = 1 ");
		
		if (true)
		{
			String sep = " ";
			sb.append(" and u.serialNo in (");
			for (int i = 0; i < serialNos.length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
			}
			sb.append(") ");
		}
		List<UnitQuery> units;
		try
		{
			units = PersistWrapper.readList(UnitQuery.class, sb.toString(), serialNos, params.toArray());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			units = new ArrayList<>();
		}

		return units;
	}

	public static List<UnitQuery> getUnitsByPks(int[] unitPks, ProjectOID projectOID)
	{
		List<Object> params = new ArrayList();

		StringBuffer sb = new StringBuffer(UnitQuery.sql);
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());

		sb.append(" where 1 = 1 ");
		
		if (true)
		{
			String sep = " ";
			sb.append(" and u.pk in (");
			for (int i = 0; i < unitPks.length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
			}
			sb.append(") ");
		}
		List<UnitQuery> units;
		try
		{
			units = PersistWrapper.readList(UnitQuery.class, sb.toString(), unitPks);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			units = new ArrayList<>();
		}

		return units;
	}

	public static List<Project> getProjectsWhereUnitIsManufactured(UnitOID unitOID)
	{
		return PersistWrapper.readList(Project.class, "select * from TAB_PROJECT where pk in "
				+ "(select projectPk from unit_project_ref upr where upr.unitPk = ? and upr.unitOriginType = ?)",
				unitOID.getPk(), UnitOriginType.Manufactured.name());
	}

	public static Project getProjectWhereUnitIsOpen(UnitOID unitOID)
	{
		return PersistWrapper.read(Project.class, "select * from TAB_PROJECT where pk = "
				+ "(select projectPk from unit_project_ref upr join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo where upr.unitPk = ? and uprh.status = ?)",
				unitOID.getPk(), UnitInProject.STATUS_OPEN);
	}

	public static Project getProjectWhereUnitWasOpenLast(UnitOID unitOID)
	{
		Integer pk = PersistWrapper.read(Integer.class,
				"select upr.projectPk from unit_project_ref upr join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk where upr.unitPk = ? and uprh.status = ? order by uprh.createdDate desc limit 0, 1",
				unitOID.getPk(), UnitInProject.STATUS_OPEN);
		if (pk != null)
			return getProject(pk);
		else
			return null;
	}

	public static List<UnitQuery> getChildUnitsForProject(ProjectOID projectOID, UnitOID parent) throws Exception
	{
		List<Object> params = new ArrayList();

		StringBuffer sb = new StringBuffer(UnitQuery.sql);
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());
		params.add(projectOID.getPk());

		sb.append(" where 1 = 1 ");
		
		if (parent == null)
		{
			sb.append(" and uprh.parentPk is null order by uprh.orderNo desc");
		} else
		{
			UnitInProjectObj uprObj = UnitManager.getUnitInProject(parent, projectOID);
			sb.append(" and uprh.parentPk  = ? order by uprh.orderNo");
			params.add(uprObj.getPk());
		}

		List<UnitQuery> units = PersistWrapper.readList(UnitQuery.class, sb.toString(), params.toArray());

		return units;
	}

	public static List<UnitQuery> getUnitsForProjectNew(UnitFilterBean unitFilter, UnitOID parent) throws Exception
	{
		UnitInProjectListReportRequest request = new UnitInProjectListReportRequest(unitFilter.getProjectoid());
		request.setShowRootUnitsOnly(unitFilter.getShowRootUnitsOnly());
		request.setPartNo(unitFilter.getPartNo());
		request.setProjectParts(unitFilter.getProjectParts());
		request.setSerialNo(unitFilter.getSerialNo());
		request.setShowProjectPartsAssignedOnly(unitFilter.getShowProjectPartsAssignedOnly());
		request.setSortOrder(unitFilter.getSortOrder());
		request.setUnitName(unitFilter.getUnitName());
		request.setUnitPks(unitFilter.getUnitPks());
		request.setUnitStatusInProject(unitFilter.getUnitStatus());
		request.setUnitsAtWorkstationOID(unitFilter.getUnitsAtWorkstationOID());
		request.setParentUnitOID(parent);

		UnitInProjectListReport report = new UnitInProjectListReport(request);
		return report.runReport();
	}

	@Deprecated
	/**
	 * returns only the children units of the parent
	 * 
	 * @param projectPk
	 * @param unitStatus
	 * @param nameFilter
	 * @param parent
	 * @return
	 * @throws Exception
	 *             Trying to migrate to the method above --
	 *             getUnitsForProjectNew which uses the report.
	 */
	public static List<UnitQuery> getUnitsForProject(UnitFilterBean unitFilter, UnitOID parent) throws Exception
	{
		if (unitFilter.getSortOrder() != null)
		{
			throw new Exception(
					"Sorting not supported for heirarchical list, It is predefined with Top level desc and children desc");
		}

		if (unitFilter.getUnitStatus() == null || unitFilter.getUnitStatus().length == 0)
		{
			unitFilter.setUnitStatus(new String[] { UnitInProject.STATUS_OPEN });
		}

		List<Object> params = new ArrayList();
		StringBuffer sb = new StringBuffer(UnitQuery.sql);
		params.add(unitFilter.getProjectoid().getPk());
		params.add(unitFilter.getProjectoid().getPk());
		params.add(unitFilter.getProjectoid().getPk());

		if(unitFilter.getUnitsAtWorkstationOID() != null)
		{
			sb.append(" join tab_unit_location ul on ul.projectPk = p.pk and ul.unitPk = u.pk and ul.current = 1 and ul.workstationPk = ? ");
			params.add(unitFilter.getUnitsAtWorkstationOID().getPk());
		}
		
		sb.append(" where 1 = 1 ");
		
		if (true)
		{
			String sep = " ";
			sb.append(" and uprh.status in (");
			for (int i = 0; i < unitFilter.getUnitStatus().length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
			}
			sb.append(") ");

			params.addAll(Arrays.asList(unitFilter.getUnitStatus()));
		}
		if (unitFilter.getProjectParts() != null && unitFilter.getProjectParts().length > 0)
		{
			String sep = " ";
			sb.append("uprh.projectPartPk in (");
			for (int i = 0; i < unitFilter.getProjectParts().length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
				params.add((unitFilter.getProjectParts()[i].getPk()));
			}
			sb.append(") ");
		}

		sb.append(" and upr.projectPk = ? ");
		params.add(unitFilter.getProjectoid().getPk());

		if (unitFilter.getUnitName() != null && unitFilter.getUnitName().trim().length() > 0)
		{
			sb.append(" and lower(uh.unitName) like lower(?) ");
			params.add("%" + unitFilter.getUnitName().trim() + "%");
		}
		if (unitFilter.getSerialNo() != null && unitFilter.getSerialNo().trim().length() > 0)
		{
			sb.append(" and lower(uh.serialNo) like lower(?) ");
			params.add("%" + unitFilter.getSerialNo().trim() + "%");
		}
		if (unitFilter.getPartNo() != null && unitFilter.getPartNo().trim().length() > 0)
		{
			sb.append(" and lower(part.partNo) like lower(?) ");
			params.add("%" + unitFilter.getPartNo().trim() + "%");
		}

		if (unitFilter.getShowProjectPartsAssignedOnly() != null
				&& true == unitFilter.getShowProjectPartsAssignedOnly())
		{
			if (unitFilter.getProjectoid() == null)
				throw new Exception(
						"Project should be specified when using the show project parts assigned only filter option");

			sb.append(" and uprh.projectPartPk in (select pk from project_part where projectPk = ?)");
			params.add(unitFilter.getProjectoid().getPk());
		}

		if (parent == null)
		{
			sb.append(" and uprh.parentPk is null order by uprh.orderNo desc");
		} else
		{
			UnitInProjectObj uprObj = UnitManager.getUnitInProject(parent, unitFilter.getProjectoid());
			sb.append(" and uprh.parentPk  = ? order by uprh.orderNo");
			params.add(uprObj.getPk());
		}

		List<UnitQuery> units = PersistWrapper.readList(UnitQuery.class, sb.toString(), params.toArray());

		return units;
	}

	public static List<UnitQuery> getUnitsForProjectLinear(UnitFilterBean unitFilter, boolean showOnlyTopLevel)
			throws Exception
	{
		String[] unitStatus = unitFilter.getUnitStatus();

		if (unitStatus == null || unitStatus.length == 0)
		{
			unitStatus = new String[] { UnitInProject.STATUS_OPEN };
		}
		if (unitFilter.getSortOrder() == null)
			unitFilter.setSortOrder(UnitSortOrder.Desc);

		List<Object> params = new ArrayList();

		StringBuffer sb = new StringBuffer(UnitQuery.sql);
		params.add(unitFilter.getProjectoid().getPk());
		params.add(unitFilter.getProjectoid().getPk());
		params.add(unitFilter.getProjectoid().getPk());

		if(unitFilter.getUnitsAtWorkstationOID() != null)
		{
			sb.append(" join tab_unit_location ul on ul.projectPk = p.pk and ul.unitPk = u.pk and ul.current = 1 and ul.workstationPk = ? ");
			params.add(unitFilter.getUnitsAtWorkstationOID().getPk());
		}
		
		sb.append(" where 1 = 1 ");
		
		if (true)
		{
			String sep = " ";
			sb.append(" and uprh.status in (");
			for (int i = 0; i < unitStatus.length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
			}
			sb.append(") ");
			params.addAll(Arrays.asList(unitStatus));
		}

		if (unitFilter.getProjectParts() != null && unitFilter.getProjectParts().length > 0)
		{
			String sep = " ";
			sb.append(" and uprh.projectPartPk in (");
			for (int i = 0; i < unitFilter.getProjectParts().length; i++)
			{
				sb.append(sep);
				sb.append("?");
				sep = ",";
				params.add((unitFilter.getProjectParts()[i].getPk()));
			}
			sb.append(") ");
		}

		sb.append(" and upr.projectPk=? ");
		params.add(unitFilter.getProjectoid().getPk());

		if (unitFilter.getUnitPks() != null && unitFilter.getUnitPks().length > 0)
		{
			String psep = " ";
			sb.append(" and u.pk in (");
			for (int i = 0; i < unitStatus.length; i++)
			{
				sb.append(psep);
				sb.append(unitFilter.getUnitPks()[i]);
				psep = ",";
			}
			sb.append(") ");
		}

		if (unitFilter.getUnitName() != null && unitFilter.getUnitName().trim().length() > 0)
		{
			sb.append(" and lower(uh.unitName) like lower(?) ");
			params.add("%" + unitFilter.getUnitName().trim() + "%");
		}
		if (unitFilter.getSerialNo() != null && unitFilter.getSerialNo().trim().length() > 0)
		{
			sb.append(" and lower(uh.serialNo) like lower(?) ");
			params.add("%" + unitFilter.getSerialNo().trim() + "%");
		}
		if (unitFilter.getPartNo() != null && unitFilter.getPartNo().trim().length() > 0)
		{
			sb.append(" and lower(part.partNo) like lower(?) ");
			params.add("%" + unitFilter.getPartNo().trim() + "%");
		}

		if (unitFilter.getShowProjectPartsAssignedOnly() != null
				&& true == unitFilter.getShowProjectPartsAssignedOnly())
		{
			if (unitFilter.getProjectoid() == null)
				throw new Exception(
						"Project should be specified when using the show project parts assigned only filter option");

			sb.append(" and uprh.projectPartPk in (select pk from project_part where projectPk = ?)");
			params.add(unitFilter.getProjectoid().getPk());
		}

		List<UnitQuery> units;
		if (showOnlyTopLevel)
		{
			sb.append(" and uprh.parentPk is null");
			sb.append(" order by uprh.orderNo " + ((unitFilter.getSortOrder() == UnitSortOrder.Desc
					|| unitFilter.getSortOrder() == UnitSortOrder.TopDescAsc) ? "desc" : "asc"));
			units = PersistWrapper.readList(UnitQuery.class, sb.toString(), params.toArray());
		} else
		{
			if (unitFilter.getSortOrder() == UnitSortOrder.Desc || unitFilter.getSortOrder() == UnitSortOrder.Asc)
			{
				sb.append(" order by uprh.orderNo "
						+ ((unitFilter.getSortOrder() == UnitSortOrder.Desc) ? "desc" : "asc"));

				units = PersistWrapper.readList(UnitQuery.class, sb.toString(), params.toArray());
			} else
			{
				// sort is TopDescAsc
				// we need 2 queries here one for top level and one for bottom
				units = PersistWrapper.readList(UnitQuery.class,
						sb.toString() + " and uprh.parentPk is null order by uprh.orderNo desc", params.toArray());

				units.addAll(PersistWrapper.readList(UnitQuery.class,
						sb.toString() + " and uprh.parentPk is not null order by uprh.orderNo asc", params.toArray()));
			}
		}

		return units;
	}

	public static List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(UnitQuery.class, UnitQuery.sql
					+ " where 1 = 1 and u.pk in (select unitPk from TAB_UNIT_WORKSTATIONS where projectPk = ? and workstationPk = ?)",
					projectOID.getPk(), projectOID.getPk(), projectOID.getPk(), projectOID.getPk(),
					workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<UnitQuery> getUnitsWithWorkstationsAssigned(ProjectOID projectOID, ProjectPartOID projectPartOID,
			WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(UnitQuery.class, UnitQuery.sql
					+ " where 1 = 1 and uprh.projectPartPk = ? and u.pk in (select unitPk from TAB_UNIT_WORKSTATIONS where projectPk = ? and workstationPk = ?)",
					projectOID.getPk(), projectOID.getPk(), projectOID.getPk(), projectPartOID.getPk(),
					projectOID.getPk(), workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static ProjectQuery updateProject(UserContext context, Project project) throws Exception
	{

		String name = project.getProjectName();
		if (isProjectNameExistForAnotherProject(name, project.getPk()))
		{
			throw new AppException("Duplicate project name, Please choose a different project name.");
		}

		PersistWrapper.update(project);
		ProjectQuery projectQuery = PersistWrapper.read(ProjectQuery.class,
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

	public static UnitObj createUnit(UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit)
			throws Exception
	{
		return createUnit(context, projectPk, unitBean, createAsPlannedUnit, false);
	}
	
	public static UnitObj createUnit(UserContext context, int projectPk, UnitBean unitBean, boolean createAsPlannedUnit, boolean pendingReview)
			throws Exception
	{
		UnitDAO unitDAO = new UnitDAO();
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();

		Project project = PersistWrapper.readByPrimaryKey(Project.class, projectPk);

		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		if (unitBean.getPartPk() == 0)
			throw new Exception(" Cannot create unit without specifying part no.");
		// if(unit.getSupplierFk() == 0)
		// throw new Exception("Cannot create unit without specifying
		// supplier");
		if (unitBean.getUnitOriginType() == null)
			throw new Exception("Please specify if the unit is Manufactured or Procured");


		UnitOID parentOID = null;
		if (unitBean.getParentPk() != null && unitBean.getParentPk() != 0)
			parentOID = new UnitOID(unitBean.getParentPk());

		boolean copyFormsConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(project.getOID(), ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
		boolean copyUsersConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(project.getOID(), ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);

		return createUnitInt(context, unitDAO, uprDAO, user, project, unitBean, parentOID, 
				createAsPlannedUnit, 
				copyUsersConfig, copyFormsConfig, false, null, pendingReview);
	}

	public static UnitObj createUnitAtWorkstation(UserContext context, ProjectOID projectOID, 
			WorkstationOID workstationOID, UnitBean unitBean,
			boolean copyPartSpecificFormsToWorkstation, boolean pendingReview)
			throws Exception
	{
		UnitDAO unitDAO = new UnitDAO();
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();

		Project project = PersistWrapper.readByPrimaryKey(Project.class, projectOID.getPk());

		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		if (unitBean.getPartPk() == 0)
			throw new Exception(" Cannot create unit without specifying part no.");
		// if(unit.getSupplierFk() == 0)
		// throw new Exception("Cannot create unit without specifying
		// supplier");
		if (unitBean.getUnitOriginType() == null)
			throw new Exception("Please specify if the unit is Manufactured or Procured");

		UnitOID parentOID = null;
		if (unitBean.getParentPk() != null && unitBean.getParentPk() != 0)
			parentOID = new UnitOID(unitBean.getParentPk());
		
		boolean copyFormsConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(projectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
		boolean copyUsersConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(projectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);

		return createUnitInt(context, unitDAO, uprDAO, user, project, unitBean, parentOID, false, 
				copyUsersConfig, copyFormsConfig, copyPartSpecificFormsToWorkstation, workstationOID, pendingReview);
	}


	private static UnitObj createUnitInt(UserContext context, UnitDAO unitDAO, UnitInProjectDAO uprDAO, User user,
			Project project, UnitBean unitBean, UnitOID parentOID, 
			boolean createAsPlannedUnit, 
			boolean copyProjectWorkstationUsersToUnit,
			boolean copyProjectWorkstationFormsToUnit,
			boolean copyPartSpecificFormsToWorkstation, 
			WorkstationOID workstationToCopyPartSpecificFormsTo, 
			boolean pendingReview) throws Exception
	{
		// if a unit is already created as manufactured unit in any project, you
		// cannot use the createUnit flow to create another manufactured unit
		// with the same name,
		// you should use the addExistingUnitToProject flow to add that unit to
		// the project.

		// if you are trying to create a new Procured unit, you cannot use an
		// existing unit name.
		
		UnitObj unit = null;
		if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
		{
			// check if the unitName is taken by a unit under a parent under the same project which is marked as
			// Manufactured. We dont have to check it across all projects units etc. its too much pain to deal with such a checking.
			String manufacSql = "select count(*) from tab_unit u join tab_unit_h uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom  and uh.effectiveDateTo "
					+ " join unit_project_ref upr on upr.unitPk = u.pk and upr.unitOriginType = ? "
					+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
					+ " where upper(uh.unitName) = upper(?) ";
			Integer count = 0;
			if(parentOID == null)
			{
				manufacSql = manufacSql + " and uprh.parentPk is null ";
				count = PersistWrapper.read(Integer.class, manufacSql, UnitOriginType.Manufactured.name(),
						unitBean.getUnitName());
			}
			else
			{
				UnitInProjectObj parentUpr = UnitManager.getUnitInProject(parentOID, project.getOID());

				manufacSql = manufacSql + " and uprh.parentPk =  ? ";
				count = PersistWrapper.read(Integer.class, manufacSql, UnitOriginType.Manufactured.name(),
						unitBean.getUnitName(), parentUpr.getPk());
			}
			
			if (count > 0)
			{
				throw new AppException(
						"Duplicate unit name:" + unitBean.getUnitName() + ", Please choose a different unit name.");
			}
			
			if (unitBean.getUnitName() == null || unitBean.getUnitName().trim().length() == 0)
				unitBean.setUnitName(unitBean.getSerialNo());

			unitBean.setProjectPk(project.getPk());
			unitBean.setCreatedBy(user.getPk());
			unitBean.setCreatedDate(new Date());
			if (true == createAsPlannedUnit)
				unitBean.setStatus(UnitInProject.STATUS_PLANNED);
			else
				unitBean.setStatus(UnitInProject.STATUS_OPEN);
			
			unit = new UnitObj(unitBean);
			if(pendingReview == true)
			{
				unit.setEstatus(EStatusEnum.PendingReview.getValue());
			}
			else
			{
				unit.setEstatus(EStatusEnum.Active.getValue());
			}
			unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.createUnit });
		} 
		else if (UnitOriginType.Procured == unitBean.getUnitOriginType())
		{
			// check if the unitName is taken by a unit which is marked either
			// as Manufactured or Procured in any project.
			String manufacSql = "select count(*) from tab_unit u join tab_unit_h uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom  and uh.effectiveDateTo "
					+ " join unit_project_ref upr on upr.unitPk = u.pk " + " where upper(uh.unitName) = upper(?) ";
			Integer count = PersistWrapper.read(Integer.class, manufacSql, unitBean.getUnitName());
			if (count > 0)
			{
				throw new AppException(
						"Duplicate unit name:" + unitBean.getUnitName() + ", Please choose a different unit name.");
			}

			if (unitBean.getUnitName() == null || unitBean.getUnitName().trim().length() == 0)
				unitBean.setUnitName(unitBean.getSerialNo());

			unitBean.setProjectPk(project.getPk());
			unitBean.setCreatedBy(user.getPk());
			unitBean.setCreatedDate(new Date());

			unit = new UnitObj(unitBean);
			unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.createUnit });
		}

		if(unit == null)
		{
			throw new AppException("Unexpected error while creating a unit, Please contact support.");
		}
		
		unitBean.setPk(unit.getPk());
		unitBean.setCreatedBy(unit.getCreatedBy());
		unitBean.setCreatedDate(unit.getCreatedDate());
		unitBean.setLastUpdated(unit.getLastUpdated());

		// add unit to the project, create the project_unit_ref entry
		UnitInProjectObj pur = addUnitToProjectInt(context, uprDAO, unitBean, project.getOID(), parentOID,
				createAsPlannedUnit, 
				copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit, 
				copyPartSpecificFormsToWorkstation, workstationToCopyPartSpecificFormsTo);

		// create any children if
		List<UnitBean> children = unitBean.getChildren();
		if (children != null)
		{
			for (Iterator iterator = children.iterator(); iterator.hasNext();)
			{
				UnitBean cUnit = (UnitBean) iterator.next();
				createUnitInt(context, unitDAO, uprDAO, user, project, cUnit, unitBean.getOID(), createAsPlannedUnit, 
						copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit, 
						copyPartSpecificFormsToWorkstation, workstationToCopyPartSpecificFormsTo, pendingReview);
			}
		}

		return unit;
	}

	private static UnitInProjectObj addUnitToProjectInt(UserContext context, UnitInProjectDAO uprDAO, UnitBean unitBean,
			ProjectOID projectOID, UnitOID parentOID, boolean createAsPlannedUnit, 
			boolean copyProjectWorkstationUsersToUnit, boolean copyProjectWorkstationFormsToUnit,
			boolean copyPartSpecificFormsToWorkstation, WorkstationOID workstationToCopyPartSpecificFormsTo) throws Exception
	{
		UnitInProjectObj pur = new UnitInProjectObj();
		pur.setUnitPk(unitBean.getPk());
		pur.setProjectPk(projectOID.getPk());
		pur.setUnitOriginType(unitBean.getUnitOriginType().name());
		if (unitBean.getProjectPartPk() != null)
		{
			pur.setProjectPartPk(unitBean.getProjectPartPk());
		}
		pur.setCreatedBy(context.getUser().getPk());
		pur.setCreatedDate(new Date());

		if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
		{
			if (true == createAsPlannedUnit)
			{
				pur.setStatus(UnitInProject.STATUS_PLANNED);
				pur = uprDAO.saveUnitInProject(context, pur, new Actions[] { Actions.addUnitToProject });
			} else
			{
				pur.setStatus(UnitInProject.STATUS_OPEN);
				pur = uprDAO.saveUnitInProject(context, pur,
						new Actions[] { Actions.addUnitToProject, Actions.openUnitInProject });
			}
		} else // procured units.
		{
			pur.setStatus(UnitInProject.STATUS_OPEN);
			pur = uprDAO.saveUnitInProject(context, pur, new Actions[] { Actions.addUnitToProject });
		}

		String heiCode = Base62Util.encode(pur.getPk());
		if (parentOID != null)
		{
			UnitInProjectObj parentUprObj = uprDAO.getUnitInProject(parentOID, projectOID);
			pur.setParentPk(parentUprObj.getPk());
			pur.setRootParentPk(parentUprObj.getRootParentPk());
			pur.setLevel(parentUprObj.getLevel() + 1);
			pur.setHeiCode(parentUprObj.getHeiCode() + "." + heiCode);
			pur.setHasChildren(false);

			if (parentUprObj.getHasChildren() == false && UnitOriginType.Manufactured == unitBean.getUnitOriginType())
			{
				// we are marking the parent unit as hasChildren = true only if
				// we are adding a Manufactured unit to its children.,
				// adding Producred to a parent are currently not treated as
				// adding children
				parentUprObj.setHasChildren(true);
				uprDAO.saveUnitInProject(context, parentUprObj);
			}
		} else
		{
			pur.setParentPk(null);
			pur.setRootParentPk(pur.getPk());
			pur.setLevel(0);
			pur.setHasChildren(false);
			pur.setHeiCode(heiCode);
		}
		pur.setOrderNo(pur.getPk());

		pur = uprDAO.saveUnitInProject(context, pur);

		// take care of the project level settings on the unit.
		if (createAsPlannedUnit == false && UnitOriginType.Manufactured == unitBean.getUnitOriginType())
		{
			// copy project workstations to unitworkstations
			setWorkstationsAndTeamsOnUnitOpen(context, projectOID, unitBean.getOID(), pur, 
					copyProjectWorkstationUsersToUnit, copyProjectWorkstationFormsToUnit);
		}
		
		if(copyPartSpecificFormsToWorkstation == true)
		{
			setPartSpecificSettingsToUnit(context, projectOID, unitBean.getOID(), pur, workstationToCopyPartSpecificFormsTo);
		}
		
		
		return pur;
	}

	public static UnitObj updateUnit(UserContext context, UnitObj unit) throws Exception
	{
		if (unit.getPk() == 0)
			throw new AppException("Invalid unit, Unit cannot be saved");

		// update
		if (isUnitNameExistForAnotherUnit(unit))
		{
			throw new AppException("Duplicate unit name, Please choose a different unit name.");
		}

		UnitDAO unitDAO = new UnitDAO();
		unit = unitDAO.saveUnit(context, unit, new Actions[] { Actions.updateUnit });
		// fetch the new project back
		return unit;
	}

	private static void copyProjectFormsToUnit(UserContext context, ProjectOID projectOID,
			List<ProjectFormQuery> projectTests, UnitOID unitOID, WorkstationOID workstationOID) throws Exception
	{
		// copy projectForms to unitforms
		for (Iterator iterator2 = projectTests.iterator(); iterator2.hasNext();)
		{
			ProjectFormQuery pForm = (ProjectFormQuery) iterator2.next();

			TestProcObj unitForm = new TestProcObj();
			unitForm.setProjectPk(projectOID.getPk());
			unitForm.setUnitPk(unitOID.getPk());
			unitForm.setWorkstationPk(workstationOID.getPk());
			unitForm.setFormPk(pForm.getFormPk());
			unitForm.setProjectTestProcPk(pForm.getPk());
			unitForm.setName(pForm.getName());
			unitForm.setAppliedByUserFk(pForm.getAppliedByUserFk());

			new TestProcDAO().saveTestProc(context, unitForm);
		}
	}

	private static void copyProjectUsersToUnit(ProjectOID projectOID, UnitOID unitOID, WorkstationOID workstationOID,
			boolean copyDefaultTeam) throws Exception
	{
		UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

		List<User> pWorkstationTesters = getUsersForProjectPartInRole(projectOID.getPk(),
				new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_TESTER);
		if (copyDefaultTeam == true && (pWorkstationTesters == null || pWorkstationTesters.size() == 0))
			pWorkstationTesters = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_TESTER);
		List<User> uWorkstationTesters = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
				User.ROLE_TESTER);

		for (Iterator iterator2 = pWorkstationTesters.iterator(); iterator2.hasNext();)
		{
			User pUser = (User) iterator2.next();
			boolean containsNow = uWorkstationTesters.remove(pUser);
			if (!(containsNow))
			{
				UnitUser unitUser = new UnitUser();
				unitUser.setProjectPk(projectOID.getPk());
				unitUser.setUnitPk(unitOID.getPk());
				unitUser.setWorkstationPk(workstationOID.getPk());
				unitUser.setUserPk(pUser.getPk());
				unitUser.setRole(User.ROLE_TESTER);
				PersistWrapper.createEntity(unitUser);
			}
		}
		for (Iterator iterator = uWorkstationTesters.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_TESTER);
		}

		List<User> pWorkstationVerifiers = getUsersForProjectPartInRole(projectOID.getPk(),
				new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_VERIFY);
		if (copyDefaultTeam == true && (pWorkstationVerifiers == null || pWorkstationVerifiers.size() == 0))
			pWorkstationVerifiers = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_VERIFY);
		List<User> uWorkstationVerifiers = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
				User.ROLE_VERIFY);

		for (Iterator iterator2 = pWorkstationVerifiers.iterator(); iterator2.hasNext();)
		{
			User pUser = (User) iterator2.next();
			boolean containsNow = uWorkstationVerifiers.remove(pUser);
			if (!(containsNow))
			{
				UnitUser unitUser = new UnitUser();
				unitUser.setProjectPk(projectOID.getPk());
				unitUser.setUnitPk(unitOID.getPk());
				unitUser.setWorkstationPk(workstationOID.getPk());
				unitUser.setUserPk(pUser.getPk());
				unitUser.setRole(User.ROLE_VERIFY);
				PersistWrapper.createEntity(unitUser);
			}
		}
		for (Iterator iterator = uWorkstationVerifiers.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_VERIFY);
		}

		List<User> pWorkstationApprovers = getUsersForProjectPartInRole(projectOID.getPk(),
				new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationOID, User.ROLE_APPROVE);
		if (copyDefaultTeam == true && (pWorkstationApprovers == null || pWorkstationApprovers.size() == 0))
			pWorkstationApprovers = getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_APPROVE);
		List<User> uWorkstationApprovers = getUsersForUnitInRole(unitOID.getPk(), projectOID, workstationOID,
				User.ROLE_APPROVE);

		for (Iterator iterator2 = pWorkstationApprovers.iterator(); iterator2.hasNext();)
		{
			User pUser = (User) iterator2.next();
			boolean containsNow = uWorkstationApprovers.remove(pUser);
			if (!(containsNow))
			{
				UnitUser unitUser = new UnitUser();
				unitUser.setProjectPk(projectOID.getPk());
				unitUser.setUnitPk(unitOID.getPk());
				unitUser.setWorkstationPk(workstationOID.getPk());
				unitUser.setUserPk(pUser.getPk());
				unitUser.setRole(User.ROLE_APPROVE);
				PersistWrapper.createEntity(unitUser);
			}
		}
		for (Iterator iterator = uWorkstationApprovers.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			removeUserFromUnit((UnitOID) unitOID, workstationOID, user.getOID(), User.ROLE_APPROVE);
		}

	}

	private static void copyProjectOilTeamToUnit(int projectPk, int unitPk) throws Exception
	{
		List<ProjectUser> users = PersistWrapper.readList(ProjectUser.class,
				"select * from TAB_PROJECT_USERS where projectPk=? and workstationPk=?", projectPk,
				DummyWorkstation.getPk());
		for (Iterator iterator2 = users.iterator(); iterator2.hasNext();)
		{
			ProjectUser projectUser = (ProjectUser) iterator2.next();

			UnitUser unitUser = new UnitUser();
			unitUser.setProjectPk(projectPk);
			unitUser.setUnitPk(unitPk);
			unitUser.setWorkstationPk(DummyWorkstation.getPk());
			unitUser.setUserPk(projectUser.getUserPk());
			unitUser.setRole(projectUser.getRole());
			PersistWrapper.createEntity(unitUser);
		}
	}

	public static boolean isUnitNameExist(String unitName) throws Exception
	{
		StringBuffer query = new StringBuffer("select count(*) from TAB_UNIT u "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " where uh.unitName =?");

		Integer count = PersistWrapper.read(Integer.class, query.toString(), unitName);
		if (count > 0)
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
	public static boolean isUnitNameExistForAnotherUnit(UnitObj unit) throws Exception
	{
		List list = PersistWrapper.readList(Unit.class, "select u.* from TAB_UNIT u "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " where uh.unitName =?", unit.getUnitName());
		if (list.size() == 0)
		{
			return false;
		} else if (list.size() == 1 && ((Unit) list.get(0)).getPk() == unit.getPk())
		{
			return false;
		} else
		{
			return true;
		}
	}

	public static void updateUnit(UnitObj unit) throws Exception
	{
		if (isUnitNameExistForAnotherUnit(unit))
		{
			throw new AppException(
					"Duplicate unit name:" + unit.getUnitName() + ", Please choose a different unit name.");
		}

		PersistWrapper.update(unit);
	}

	public static void deleteAllFormsFromProjectPart(UserContext context, ProjectPartOID projectPartOID,
			int workstationPk) throws Exception
	{
		List list = PersistWrapper.readList(ProjectForm.class,
				"select * from TAB_PROJECT_FORMS where projectPartPk=? and workstationPk=?", projectPartOID.getPk(),
				workstationPk);
		if (list != null)
			for (Iterator iterator = list.iterator(); iterator.hasNext();)
			{
				ProjectForm object = (ProjectForm) iterator.next();
				PersistWrapper.deleteEntity(object);
			}
	}

	public static void deleteProjectForm(UserContext context, ProjectFormOID projectFormOID) throws Exception
	{
		ProjectForm pForm = PersistWrapper.readByPrimaryKey(ProjectForm.class, projectFormOID.getPk());
		PersistWrapper.deleteEntity(pForm);
	}

	public static void addFormToProjectPart(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
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

		PersistWrapper.createEntity(pForm);
	}

	/**
	 * We are setting a form on a project part. with no workstation information. the workstationPk will be null in the database
	 * @param context
	 * @param projectOID
	 * @param projectPartOID
	 * @param formPk
	 * @param testName
	 * @throws Exception
	 */
	public static void addFormToProjectPart(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
			int formPk, String testName) throws Exception
	{
		ProjectForm pForm = new ProjectForm();
		pForm.setProjectPk(projectOID.getPk());
		pForm.setProjectPartPk(projectPartOID.getPk());
		pForm.setFormPk(formPk);
		if (testName != null && testName.trim().length() > 0)
			pForm.setName(testName);

		pForm.setAppliedByUserFk(context.getUser().getPk());

		PersistWrapper.createEntity(pForm);
	}

	/*
	 * If we are adding a form to a workstation which is inProgress, we create the response right away.
	 * if the workstation status is waiting, we dont have to create the response. it will get created when the workstation is made InProgress
	 * But if the form is added from tablet, we need the response created right away. so we call this function
	 * with forceResponseCreation = true from the tablet.
	 */
	public static TestProcOID addFormToUnit(UserContext context, ProjectForm projectTestProc, int unitPk,
			ProjectOID projectOID, WorkstationOID workstationOID, int formPk, 
			String testName, boolean makeWorkstationInProgress, boolean reviewPending) throws Exception
	{
		Survey form = SurveyMaster.getSurveyByPk(formPk);
		if(form == null)
			throw new AppException("Invalid form");
		if(!(Survey.STATUS_OPEN.equals(form.getStatus())))
			throw new AppException("Form is not published, Please publish the form and try again");
				
		UnitWorkstation uw = ProjectDelegate.getUnitWorkstationSetting(unitPk, projectOID, workstationOID);
		if(uw == null || uw.getPk() == 0) //this means the workstation should be added to the unit.
		{
			uw = addWorkstationToUnit(context, projectOID, new UnitOID(unitPk), workstationOID);
			
			// copy projectUsers to unitusers
			copyProjectUsersToUnit(projectOID, new UnitOID(unitPk), workstationOID, true);
			
			
			// if the user who added the form is not a tester on that workstaton for that unit, add him.
			List<User> uWorkstationTesters = getUsersForUnitInRole(unitPk, projectOID, workstationOID, User.ROLE_TESTER);
			if(!(uWorkstationTesters.contains(context.getUser())))
			{
				UnitUser unitUser = new UnitUser();
				unitUser.setProjectPk(projectOID.getPk());
				unitUser.setUnitPk(unitPk);
				unitUser.setWorkstationPk(workstationOID.getPk());
				unitUser.setUserPk(context.getUser().getPk());
				unitUser.setRole(User.ROLE_TESTER);
				PersistWrapper.createEntity(unitUser);
			}
		}

		String currentWsStatus = null;
		UnitLocationQuery uLoc = ProjectManager.getUnitWorkstationStatus(new UnitOID(uw.getUnitPk(), null), projectOID, workstationOID);
		if(uLoc != null)
			currentWsStatus = uLoc.getStatus();
		if(UnitLocation.STATUS_COMPLETED.equals(currentWsStatus))
		{
			throw new AppException("Workstation is marked as complete for unit, cannot add new form");
		}

		boolean mandatoryTestNameProperty = (boolean) new CommonServicesDelegate().getEntityPropertyValue(projectOID,
				ProjectPropertyEnum.MakeTestNamesForChecksheetsMandatory.getId(), Boolean.class);
		if (mandatoryTestNameProperty == true && (testName == null || testName.trim().length() == 0))
		{
			throw new AppException("Test name is mandatory for this project, Please provide a Test Name and try again");
		}

		if (testName == null || testName.trim().length() == 0)
		{
			int count = PersistWrapper.read(Integer.class, "select count(*) from unit_testproc ut "
					+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
					+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
					+ " where uth.unitPk=? and uth.projectPk = ? and uth.workstationPk=? and tfa.formFk=? and (uth.name is null or name = '') ",
					unitPk, projectOID.getPk(), workstationOID.getPk(), formPk);
			if (count > 0)
				throw new AppException(
						"Form already added with empty test name, you should specify a test name to add it again");
		} else
		{
			int count = PersistWrapper.read(Integer.class, "select count(*) from unit_testproc ut "
					+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
					+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
					+ " where uth.unitPk=? and uth.projectPk = ? and uth.workstationPk=? and tfa.formFk=? and uth.name = ? ",
					unitPk, projectOID.getPk(), workstationOID.getPk(), formPk, testName.trim());
			if (count > 0)
				throw new AppException(
						"Form already added with the test name, you should specify a different test name to add it again");
		}

		TestProcObj uForm = new TestProcObj();
		uForm.setProjectPk(projectOID.getPk());
		uForm.setUnitPk(unitPk);
		uForm.setWorkstationPk(workstationOID.getPk());
		uForm.setFormPk(formPk);
		uForm.setName(testName);

		if (projectTestProc != null)// null will happen when it is an oil form
									// ::TODO change when new oil impl is in
									// force.
		{
			uForm.setProjectTestProcPk(projectTestProc.getPk());
		}
		uForm.setAppliedByUserFk(context.getUser().getPk());

		if(reviewPending == true)
			uForm.setEstatus(EStatusEnum.PendingReview.getValue());
		else
			uForm.setEstatus(EStatusEnum.Active.getValue());
			
		TestProcObj obj = new TestProcDAO().saveTestProc(context, uForm);
		
		ResponseMasterNew response = null;
		if(UnitLocation.STATUS_IN_PROGRESS.equals(currentWsStatus))
		{
			//create the dummy response entry
			response = SurveyResponseManager.getLatestResponseMasterForTest(obj.getOID());
			if (response == null)
			{
				SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(obj.getFormPk(), null));
				SurveyResponse sResponse = new SurveyResponse(sd);
				sResponse.setSurveyPk(obj.getFormPk());
				sResponse.setTestProcPk(obj.getPk());
				sResponse.setResponseStartTime(new Date());
				sResponse.setResponseCompleteTime(new Date());
				// ipaddress and mode set
				sResponse.setIpaddress("0.0.0.0");
				sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
				sResponse.setUser((User) context.getUser());
				sResponse = SurveyResponseManager.ceateDummyResponse(context, sResponse);
				response = SurveyResponseManager.getResponseMaster(sResponse.getResponseId());
			}
		}
		else
		{
			if(makeWorkstationInProgress == true)
			{
				setUnitWorkstationStatus(context, unitPk, projectOID, workstationOID, UnitLocation.STATUS_IN_PROGRESS);
			}
		}
		
		return obj.getOID();
	}

	public static void deleteTestProcFromUnit(UserContext context, TestProcOID testProcOID) throws Exception
	{
		// see if there are any responses for that form
		ResponseMasterNew respM = SurveyResponseManager.getLatestResponseMasterForTest(testProcOID);
		if (respM != null 
				&& (ResponseMasterNew.STATUS_APPROVED.equals(respM.getStatus()) || ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS.equals(respM.getStatus()))
				)
		{
			throw new FormApprovedException("The form is already approved, It cannot be removed.");
		}

		new TestProcDAO().deleteTestProc(context, testProcOID);
	}

	public static void removeAllFormsFromUnit(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID) throws Exception
	{
		new TestProcDAO().deleteAllTestProcsMatching(context, new UnitOID(unitPk), projectOID, workstationOID);
	}

	public static UnitObj getUnitByPk(UnitOID unitOID)
	{
		return new UnitDAO().getUnit(unitOID.getPk());
	}

	public static Car getCarByPk(int carPk) throws Exception
	{
		return PersistWrapper.readByPrimaryKey(Car.class, carPk);
	}

	public static UnitQuery getUnitQueryByPk(int unitPk, ProjectOID projectOID)
	{
		List<UnitQuery> list = null;
		try
		{
			list = PersistWrapper.readList(UnitQuery.class, UnitQuery.sql + " where 1 = 1 and u.pk=?", projectOID.getPk(),
					projectOID.getPk(), projectOID.getPk(), unitPk);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public static CarQuery getCarQueryByPk(int carPk) throws Exception
	{
		return PersistWrapper.read(CarQuery.class, CarQuery.select + " where car.pk=?", carPk);

	}

	public static List<FormQuery> getAllFormsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception
	{
		FormFilter filter = new FormFilter();
		filter.setStatus(new String[] { Survey.STATUS_OPEN });
		filter.setShowAllFormRevisions(true);
		filter.setUnitFormFilter(filter.new UnitFormAssignmentFilter(unitOID, projectOID, null));
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

	public static List<FormQuery> getFormsForUnit(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID)
			throws Exception
	{
		FormFilter filter = new FormFilter();
		filter.setStatus(new String[] { Survey.STATUS_OPEN });
		filter.setShowAllFormRevisions(true);
		filter.setUnitFormFilter(filter.new UnitFormAssignmentFilter(unitOID, projectOID, workstationOID));
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

	public static Workstation createWorkstation(UserContext context, Workstation workstation) throws Exception
	{
		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		Site site = SiteManager.getSite(workstation.getSitePk());
		if (site == null)
			throw new AppException("Invalid Site selected.");

		if (isWorkstationNameExist(acc, site, workstation.getWorkstationName()))
		{
			throw new AppException("Duplicate workstation name, Please choose a different workstation name.");
		}

		int pk;
		synchronized (WorkstationOrderNoController.class)
		{
			int maxOrderNo = PersistWrapper.read(Integer.class, "select ifnull(max(orderNo),0) from TAB_WORKSTATION");
			workstation.setOrderNo(maxOrderNo + 1);
			workstation.setAccountPk(acc.getPk());
			workstation.setCreatedBy(user.getPk());
			workstation.setCreatedDate(new Date());
			workstation.setStatus(Workstation.STATUS_OPEN);
			workstation.setEstatus(EStatusEnum.Active.getValue());
			workstation.setUpdatedBy(user.getPk());
			pk = PersistWrapper.createEntity(workstation);
		}
		// fetch the new project back
		workstation = PersistWrapper.readByPrimaryKey(Workstation.class, pk);
		return workstation;

	}

	public static Workstation updateWorkstation(UserContext context, Workstation workstation) throws Exception
	{
		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		Site site = SiteManager.getSite(workstation.getSitePk());
		if (site == null)
			throw new AppException("Invalid Site selected.");

		if (isWorkstationNameExistForAnotherWorkstation(acc, site, workstation.getWorkstationName(),
				workstation.getPk()))
		{
			throw new AppException("Another workstation with the name specified exists, Please choose another name.");
		}

		workstation.setUpdatedBy(context.getUser().getPk());
		PersistWrapper.update(workstation);

		// fetch the new project back
		workstation = PersistWrapper.readByPrimaryKey(Workstation.class, workstation.getPk());
		return workstation;
	}

	/**
	 * @param acc
	 * @param projectName
	 * @return
	 */
	private static boolean isWorkstationNameExist(Account acc, Site site, String workstationName) throws Exception
	{
		List list = PersistWrapper.readList(Workstation.class,
				"select * from TAB_WORKSTATION where accountPk=? and sitePk=? and " + "workstationName =?", acc.getPk(),
				site.getPk(), workstationName);
		if (list.size() > 0)
		{
			return true;
		} else
		{
			return false;
		}
	}

	private static boolean isWorkstationNameExistForAnotherWorkstation(Account acc, Site site, String workstationName,
			int workstationPk) throws Exception
	{
		try
		{
			List list = PersistWrapper.readList(Workstation.class,
					"select * from TAB_WORKSTATION where accountPk=? and sitePk=? and workstationName =?", acc.getPk(),
					site.getPk(), workstationName);
			if (list.size() == 0)
			{
				return false;
			} else if (list.size() == 1 && ((Workstation) list.get(0)).getPk() == workstationPk)
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

	public static List<WorkstationQuery> getWorkstationList() throws Exception
	{
		String sql = WorkstationQuery.sql + " and  ws.workstationName != ? order by site.name, ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY);
	}

	public static Workstation getWorkstation(WorkstationOID workstationOID)
	{
		return PersistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
	}

	public static WorkstationQuery getWorkstationQueryByPk(WorkstationOID workstationOID)
	{
		List list = PersistWrapper.readList(WorkstationQuery.class, WorkstationQuery.sql + " and ws.pk=?",
				workstationOID.getPk());

		if (list != null && list.size() > 0)
		{
			return (WorkstationQuery) list.get(0);
		}
		return null;
	}

	public static List<WorkstationQuery> getWorkstationsForProject(int projectPk)
	{
		String sql = WorkstationQuery.sql
				+ " and ws.workstationName != ? and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?) order by ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY, projectPk);
	}

	public static List<WorkstationQuery> getWorkstations(WorkstationFilter filter)
	{
		StringBuffer sb = new StringBuffer();
		List params = new ArrayList();

		sb.append(" and ws.workstationName != ? and ws.estatus = 1 ");
		params.add(DummyWorkstation.DUMMY);

		if (StringUtils.isNotBlank(filter.getFilterString()))
		{
			// sb.append(" and (upper(ws.workstationName) like ? or
			// upper(ws.description) like ? )");
			// params.add("%" + filter.getFilterString().toUpperCase().trim() +
			// "%");
			// params.add("%" + filter.getFilterString().toUpperCase().trim() +
			// "%");

			sb.append(
					" and (concat('WS',upper(ws.workstationName),' ',upper(ws.description),' / ',site.name) like ? )");
			params.add("%" + filter.getFilterString().toUpperCase().trim() + "%");
		}

		if (filter.getSiteOIDs() != null && filter.getSiteOIDs().size() > 0)
		{
			String sep = "";
			sb.append(" and ws.sitePk in (");
			for (Iterator iterator = filter.getSiteOIDs().iterator(); iterator.hasNext();)
			{
				SiteOID aSite = (SiteOID) iterator.next();
				sb.append(sep).append("?");
				sep = ", ";
				params.add(aSite.getPk());
			}
			sb.append(")");
		}
		if (filter.getUnitOID() != null)
		{
			sb.append(" and ws.pk in (select workstationPk from TAB_UNIT_WORKSTATIONS where unitPk = ?)");
			params.add(filter.getUnitOID().getPk());
		}
		if (filter.getProjectOID() != null)
		{
			sb.append(" and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk = ?)");
			params.add(filter.getProjectOID().getPk());
		}
		sb.append(" order by ws.orderNo");
		return PersistWrapper.readList(WorkstationQuery.class, WorkstationQuery.sql + sb.toString(), params.toArray());
	}

	public static List<WorkstationQuery> getWorkstationsAssignableForProject(ProjectOID projectOID)
	{
		String sql = WorkstationQuery.sql
				+ " and ws.workstationName != ? and ws.sitePk in (select siteFk from project_site_config where projectFk=? and estatus = 1) order by ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		try
		{
			return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY, projectOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<WorkstationQuery> getWorkstationsForSite(int sitePk) throws Exception
	{
		String sql = WorkstationQuery.sql + " and  ws.workstationName != ? and ws.sitePk=? order by ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY, sitePk);
	}

	public static List<WorkstationQuery> getWorkstationsForSiteAndProject(int sitePk, int projectPk) throws Exception
	{
		String sql = WorkstationQuery.sql
				+ " and  ws.workstationName != ? and ws.sitePk=? and ws.pk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?) order by ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY, sitePk, projectPk);
	}

	public static void addWorkstationToProject(UserContext context, int projectPk, WorkstationOID workstationOID)
			throws Exception
	{
		ProjectWorkstation pForm = new ProjectWorkstation();
		pForm.setProjectPk(projectPk);
		pForm.setWorkstationPk(workstationOID.getPk());

		PersistWrapper.createEntity(pForm);
	}

	public static void removeWorkstationFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
			throws Exception
	{
		// delete workstation forms
		PersistWrapper.delete("delete from TAB_PROJECT_FORMS where projectPk=? and workstationPk =?", projectPk,
				workstationOID.getPk());

		// delete workstation users
		PersistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk = ? and workstationPk = ?", projectPk,
				workstationOID.getPk());

		// now delete workstations
		PersistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=? and workstationPk = ?", projectPk,
				workstationOID.getPk());
	}

	public static void removeAllWorkstationsFromProject(UserContext context, int projectPk) throws Exception
	{
		// delete workstation forms
		PersistWrapper.delete(
				"delete from TAB_PROJECT_FORMS where projectPk=? and workstationPk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?)",
				projectPk, projectPk);

		// delete workstation users
		PersistWrapper.delete(
				"delete from TAB_PROJECT_USERS where projectPk = ? and workstationPk in (select workstationPk from TAB_PROJECT_WORKSTATIONS where projectPk=?)",
				projectPk, projectPk);

		// now delete workstations
		PersistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=?", projectPk);
	}

	public static List<ProjectFormQuery> getProjectFormsForProject(ProjectOID projectOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectFormQuery.class,
					ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? ",
					projectOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<ProjectFormQuery> getProjectFormsForProject(int projectPk, WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectFormQuery.class,
					ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? and pf.workstationPk=? ",
					projectPk, workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<FormQuery> getFormsForProject(ProjectOID projectOID) throws Exception
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

	public static List<FormQuery> getFormsForProject(int projectPk, WorkstationOID workstationOID) throws Exception
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

	public static void addUserToProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
			int workstationPk, int userPk, String role) throws Exception
	{
		ProjectUser pUser = new ProjectUser();
		pUser.setProjectPk(projectPk);
		if (projectPartOID != null)
			pUser.setProjectPartPk(projectPartOID.getPk());
		pUser.setWorkstationPk(workstationPk);
		pUser.setUserPk(userPk);
		pUser.setRole(role);

		PersistWrapper.createEntity(pUser);
	}

	public static void addUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID, int userPk,
			String role) throws Exception
	{
		ProjectUser pUser = new ProjectUser();
		pUser.setProjectPk(projectPk);
		pUser.setWorkstationPk(workstationOID.getPk());
		pUser.setUserPk(userPk);
		pUser.setRole(role);

		PersistWrapper.createEntity(pUser);
	}

	public static void addReadonlyUserToProject(UserContext context, int projectPk, WorkstationOID workstationOID,
			int userPk) throws Exception
	{
		ProjectUser pUser = new ProjectUser();
		pUser.setProjectPk(projectPk);
		pUser.setWorkstationPk(workstationOID.getPk());
		pUser.setUserPk(userPk);
		pUser.setRole(User.ROLE_READONLY);

		PersistWrapper.createEntity(pUser);
	}

	public static void updateProjectTeam(UserContext context, ProjectOID projectOID, ProjectPartOID projectPartOID,
			WorkstationOID wsOID, Collection testList, Collection verifyList, Collection approveList) throws Exception
	{
		List<User> workstationTesters = new ArrayList();
		if (projectPartOID != null)
			workstationTesters = ProjectManager.getUsersForProjectPartInRole(projectOID.getPk(), projectPartOID, wsOID,
					User.ROLE_TESTER);
		else
			workstationTesters = ProjectManager.getUsersForProjectInRole(projectOID.getPk(), wsOID, User.ROLE_TESTER);
		if (testList != null)
		{
			for (Iterator iterator = testList.iterator(); iterator.hasNext();)
			{
				User newUser = (User) iterator.next();
				boolean containsNow = workstationTesters.remove(newUser);
				if (!(containsNow))
				{
					addUserToProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), newUser.getPk(),
							User.ROLE_TESTER);
				}
			}
		}
		for (Iterator iterator = workstationTesters.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			if (projectPartOID != null)
				removeUserFromProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), user.getPk(),
						User.ROLE_TESTER);
			else
				removeUserFromProject(context, projectOID.getPk(), wsOID.getPk(), user.getPk(), User.ROLE_TESTER);

		}

		List<User> workstationVerifiers = new ArrayList();
		if (projectPartOID != null)
			workstationVerifiers = ProjectManager.getUsersForProjectPartInRole(projectOID.getPk(), projectPartOID,
					wsOID, User.ROLE_VERIFY);
		else
			workstationVerifiers = ProjectManager.getUsersForProjectInRole(projectOID.getPk(), wsOID, User.ROLE_VERIFY);
		if (verifyList != null)
		{
			for (Iterator iterator = verifyList.iterator(); iterator.hasNext();)
			{
				User newUser = (User) iterator.next();
				boolean containsNow = workstationVerifiers.remove(newUser);
				if (!(containsNow))
				{
					addUserToProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), newUser.getPk(),
							User.ROLE_VERIFY);
				}
			}
		}
		for (Iterator iterator = workstationVerifiers.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			if (projectPartOID != null)
				removeUserFromProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), user.getPk(),
						User.ROLE_VERIFY);
			else
				removeUserFromProject(context, projectOID.getPk(), wsOID.getPk(), user.getPk(), User.ROLE_VERIFY);
		}

		List<User> workstationApprovers = new ArrayList();

		if (projectPartOID != null)
			workstationApprovers = ProjectManager.getUsersForProjectPartInRole(projectOID.getPk(), projectPartOID,
					wsOID, User.ROLE_APPROVE);
		else
			workstationApprovers = ProjectManager.getUsersForProjectInRole(projectOID.getPk(), wsOID,
					User.ROLE_APPROVE);

		if (approveList != null)
		{
			for (Iterator iterator = approveList.iterator(); iterator.hasNext();)
			{
				User newUser = (User) iterator.next();
				boolean containsNow = workstationApprovers.remove(newUser);
				if (!(containsNow))
				{
					addUserToProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), newUser.getPk(),
							User.ROLE_APPROVE);
				}
			}
		}
		for (Iterator iterator = workstationApprovers.iterator(); iterator.hasNext();)
		{
			User user = (User) iterator.next();
			if (projectPartOID != null)
				removeUserFromProject(context, projectOID.getPk(), projectPartOID, wsOID.getPk(), user.getPk(),
						User.ROLE_APPROVE);
			else
				removeUserFromProject(context, projectOID.getPk(), wsOID.getPk(), user.getPk(), User.ROLE_APPROVE);
		}
	}

	public static void addTesterToUnit(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, int userPk) throws Exception
	{
		// check if the it is already there..
		UnitUser uu = PersistWrapper.read(UnitUser.class,
				"select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
				userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_TESTER);
		if (uu == null)
		{
			UnitUser pUser = new UnitUser();
			pUser.setProjectPk(projectOID.getPk());
			pUser.setUnitPk(unitPk);
			pUser.setWorkstationPk(workstationOID.getPk());
			pUser.setUserPk(userPk);
			pUser.setRole(User.ROLE_TESTER);

			PersistWrapper.createEntity(pUser);
		}
	}

	public static void addVerifierToUnit(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, int userPk) throws Exception
	{
		// check if the it is already there..
		UnitUser uu = PersistWrapper.read(UnitUser.class,
				"select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
				userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_VERIFY);
		if (uu == null)
		{
			UnitUser pUser = new UnitUser();
			pUser.setProjectPk(projectOID.getPk());
			pUser.setUnitPk(unitPk);
			pUser.setWorkstationPk(workstationOID.getPk());
			pUser.setUserPk(userPk);
			pUser.setRole(User.ROLE_VERIFY);

			PersistWrapper.createEntity(pUser);
		}
	}

	public static void addApproverToUnit(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, int userPk) throws Exception
	{
		// check if the it is already there..
		UnitUser uu = PersistWrapper.read(UnitUser.class,
				"select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
				userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_APPROVE);
		if (uu == null)
		{
			UnitUser pUser = new UnitUser();
			pUser.setProjectPk(projectOID.getPk());
			pUser.setUnitPk(unitPk);
			pUser.setWorkstationPk(workstationOID.getPk());
			pUser.setUserPk(userPk);
			pUser.setRole(User.ROLE_APPROVE);

			PersistWrapper.createEntity(pUser);
		}
	}

	public static void addReadonlyUserToUnit(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, int userPk) throws Exception
	{
		// check if the it is already there..
		UnitUser uu = PersistWrapper.read(UnitUser.class,
				"select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
				userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), User.ROLE_READONLY);
		if (uu == null)
		{
			UnitUser pUser = new UnitUser();
			pUser.setProjectPk(projectOID.getPk());
			pUser.setUnitPk(unitPk);
			pUser.setWorkstationPk(workstationOID.getPk());
			pUser.setUserPk(userPk);
			pUser.setRole(User.ROLE_READONLY);

			PersistWrapper.createEntity(pUser);
		}
	}

	public static List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectUserQuery.class,
					ProjectUserQuery.sql + " and pu.projectPk=? and pu.workstationPk=? order by u.firstName asc",
					projectOID.getPk(), workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<ProjectUserQuery> getProjectUserQueryList(ProjectOID projectOID, ProjectPartOID projectPartOID,
			WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectUserQuery.class, ProjectUserQuery.sql
					+ " and pu.projectPk=? and projectPartPk = ? and pu.workstationPk=? order by u.firstName asc",
					projectOID.getPk(), projectPartOID.getPk(), workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<User> getUsersForProjectPartInRole(int projectPk, ProjectPartOID projectPartOID,
			WorkstationOID workstationOID, String roleName) throws Exception
	{
		if (projectPartOID == null) // we need the ones where the projectPartPk
									// is null which is the default list
		{
			return new ArrayList();
		} else
		{
			return PersistWrapper.readList(User.class, "select distinct u.* from TAB_USER u "
					+ " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
					+ " where tpu.projectPk=? and tpu.projectPartPk = ? and tpu.workstationPk=? and tpu.role=? order by u.firstName asc",
					projectPk, projectPartOID.getPk(), workstationOID.getPk(), roleName);
		}
	}

	public static List<User> getUsersForProjectInRole(ProjectOID projectOID, String roleName) throws Exception
	{
		return PersistWrapper.readList(User.class,
				"select distinct u.* from TAB_USER u " + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
						+ "where tpu.projectPk=? and tpu.projectPartPk is null and tpu.role=? order by u.firstName asc",
				projectOID.getPk(), roleName);
	}

	public static List<User> getUsersForProjectInRole(int projectPk, WorkstationOID workstationOID, String roleName)
			throws Exception
	{
		return PersistWrapper.readList(User.class, "select distinct u.* from TAB_USER u "
				+ " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
				+ "where tpu.projectPk=? and tpu.projectPartPk is null and tpu.workstationPk=? and tpu.role=? order by firstName asc",
				projectPk, workstationOID.getPk(), roleName);
	}

	public static List<User> getUsersForProject(int projectPk, WorkstationOID workstationOID) throws Exception
	{
		return PersistWrapper.readList(User.class,
				"select distinct u.* from TAB_USER u " + " inner join TAB_PROJECT_USERS tpu on tpu.userPk = u.pk  "
						+ " where tpu.projectPk=? and tpu.workstationPk=? order by firstName asc",
				projectPk, workstationOID.getPk());
	}

	public static List<Project> getProjectsWhereTheUserIsReadOnly(UserContext context)
	{
		try
		{
			return PersistWrapper.readList(Project.class,
					"select distinct p.* from TAB_PROJECT p "
							+ " inner join TAB_PROJECT_USERS tpu on tpu.projectPk = p.pk "
							+ " where tpu.userPk=? and tpu.workstationPk=? and tpu.role in (?, ?, ?)",
					context.getUser().getPk(), DummyWorkstation.getPk(), User.ROLE_TESTER, User.ROLE_VERIFY,
					User.ROLE_READONLY);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static boolean isUserCoordinatorForProject(UserContext userContext, int projectPk)
	{
		try
		{
			Project p = PersistWrapper.readByPrimaryKey(Project.class, projectPk);
			if (p != null && p.getManagerPk() != null && p.getManagerPk() == userContext.getUser().getPk())
			{
				return true;
			}

			List<UserPerms> l = PersistWrapper.readList(UserPerms.class,
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

	public static void removeUserFromProject(UserContext context, int projectPk, int workstationPk, int userPk,
			String role) throws Exception
	{
		PersistWrapper.delete(
				"delete from TAB_PROJECT_USERS where projectPk=? and projectPartPk is null and workstationPk=? and userPk = ? and role = ?",
				projectPk, workstationPk, userPk, role);
	}

	public static void removeUserFromProject(UserContext context, int projectPk, ProjectPartOID projectPartOID,
			int workstationPk, int userPk, String role) throws Exception
	{
		PersistWrapper.delete(
				"delete from TAB_PROJECT_USERS where projectPk=? and projectPartPk = ? and workstationPk=? and userPk = ? and role = ?",
				projectPk, projectPartOID.getPk(), workstationPk, userPk, role);
	}

	public static void removeAllUsersFromProject(UserContext context, int projectPk, WorkstationOID workstationOID)
			throws Exception
	{
		PersistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk=? and workstationPk=?", projectPk,
				workstationOID.getPk());
	}

	public static void removeUserFromUnit(UnitOID unitOID, WorkstationOID wsOID, UserOID userOID, String role)
			throws Exception
	{
		PersistWrapper.delete(
				"delete from TAB_UNIT_USERS where unitPk=? and workstationPk=? and userPk = ? and role = ?",
				unitOID.getPk(), wsOID.getPk(), userOID.getPk(), role);
	}

	public static void removeAllUsersFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
			WorkstationOID workstationOID) throws Exception
	{
		PersistWrapper.delete("delete from TAB_UNIT_USERS where unitPk=? and projectPk = ? and workstationPk=?",
				unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());
	}

	public static List<User> getUsersForUnit(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
			throws Exception
	{
		return PersistWrapper.readList(User.class,
				"select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? order by firstName",
				unitPk, projectOID.getPk(), workstationOID.getPk());
	}

	public static List<User> getUsersForUnitInRole(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
			String roleName) throws Exception
	{
		return PersistWrapper.readList(User.class,
				"select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? and uu.role=?",
				unitPk, projectOID.getPk(), workstationOID.getPk(), roleName);
	}

	public static boolean isUsersForProjectInRole(int userPk, ProjectOID projectOID, WorkstationOID workstationOID,
			String roleName)
	{
		List list = null;
		try
		{
			list = PersistWrapper.readList(User.class,
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

	public static boolean isUsersForUnitInRole(int userPk, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, String roleName)
	{
		List list = null;
		try
		{
			list = PersistWrapper.readList(User.class,
					"select distinct u.* from TAB_USER u, TAB_UNIT_USERS uu where uu.userPk = u.pk and u.pk = ? and uu.unitPk=? and uu.projectPk = ? and uu.workstationPk=? and uu.role=?",
					userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), roleName);
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

	public static List<WorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID) throws Exception
	{
		String sql = WorkstationQuery.sql + " and "
				+ "ws.workstationName != ? and ws.pk in (select workstationPk from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and estatus = ? ) order by ws.orderNo";
		PersistWrapper p = new PersistWrapper();
		return p.readList(WorkstationQuery.class, sql, DummyWorkstation.DUMMY, unitOID.getPk(), projectOID.getPk(),
				EStatusEnum.Active.getValue());
	}

	/**
	 * returns the list of UnitWorkstationQuery for a unit and its children.
	 * 
	 * @param unitPk
	 * @param includeChildUnits
	 * @return
	 * @throws Exception
	 */
	public static List<UnitWorkstationQuery> getWorkstationsForUnit(UnitOID unitOID, ProjectOID projectOID,
			boolean includeChildUnits) throws Exception
	{
		if (includeChildUnits)
		{
			UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

			String sql = UnitWorkstationQuery.sql + " and " + "w.workstationName != ?  "
					+ " and ( (u.pk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) )"
					+ " and uw.projectPk = ? and uw.estatus = ? order by uprh.level, w.orderNo";
			PersistWrapper p = new PersistWrapper();
			return p.readList(UnitWorkstationQuery.class, sql, DummyWorkstation.DUMMY, unitInProject.getUnitPk(),
					unitInProject.getRootParentPk(), unitInProject.getHeiCode() + ".%", projectOID.getPk(),
					EStatusEnum.Active.getValue());
		} else
		{
			String sql = UnitWorkstationQuery.sql + " and "
					+ "w.workstationName != ? and uw.unitpk  = ? and uw.projectPk = ? and uw.estatus = ? order by w.orderNo";
			PersistWrapper p = new PersistWrapper();
			return p.readList(UnitWorkstationQuery.class, sql, DummyWorkstation.DUMMY, unitOID.getPk(),
					projectOID.getPk(), EStatusEnum.Active.getValue());
		}
	}

	public static UnitWorkstation addWorkstationToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
			WorkstationOID workstationOID) throws Exception
	{
		int uwPk = 0;

		UnitWorkstation existingOne = PersistWrapper.read(UnitWorkstation.class,
				"select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?",
				unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());
		if (existingOne == null)
		{
			UnitWorkstation pForm = new UnitWorkstation();
			pForm.setEstatus(EStatusEnum.Active.getValue());
			pForm.setUpdatedBy(context.getUser().getPk());
			pForm.setProjectPk(projectOID.getPk());
			pForm.setUnitPk(unitOID.getPk());
			pForm.setWorkstationPk(workstationOID.getPk());

			uwPk = PersistWrapper.createEntity(pForm);
		} else
		{
			uwPk = existingOne.getPk();
		}
		Project proj = getProject(projectOID.getPk());
		
		
		
		String defaultWorkstationStatusValue = (String) new CommonServicesDelegate().getEntityPropertyValue(projectOID,
				ProjectPropertyEnum.SetNewWorkstationsDefaultStatusTo.getId(), String.class);
		if (defaultWorkstationStatusValue != null && (!(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue))))
		{
			if(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue) || UnitLocation.STATUS_IN_PROGRESS.equals(defaultWorkstationStatusValue)
					|| UnitLocation.STATUS_COMPLETED.equals(defaultWorkstationStatusValue))
			{
				setUnitWorkstationStatus(context, unitOID.getPk(), projectOID, workstationOID,
						defaultWorkstationStatusValue);
			}
			else
			{
				throw new AppException("Default status value configured for workstation status is invalid. Please contact support");
			}
		}
		
		return PersistWrapper.readByPrimaryKey(UnitWorkstation.class, uwPk);
	}

	public static void removeWorkstationFromUnit(UserContext context, UnitOID unitOID, ProjectOID projectOID,
			WorkstationOID workstationOID) throws Exception
	{
		// if the forms related to workstations have been attempted, the
		// workstation cannot be removed from the unit
		// int count = PersistWrapper.read(Integer.class, "select count(pk) from
		// TAB_RESPONSE where unitPk=? and workstationPk=?", unitPk,
		// workstationPk);

		int count = PersistWrapper.read(Integer.class, "select count(*) from TAB_RESPONSE res "
				+ " join unit_testproc ut on res.testProcPk = ut.pk "
				+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.formFk = res.surveyPk and tfa.current = 1 "
				+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
				+ " where   "
				+ " uth.unitPk = ? and uth.projectPk = ? and uth.workstationPk=? ", unitOID.getPk(),
				projectOID.getPk(), workstationOID.getPk());

		if (count > 0)
		{
			List errors = new ArrayList();
			errors.add("Workstation cannot be removed as forms have been filled out at this workstation for the unit.");
			throw new AppException((String[]) errors.toArray(new String[errors.size()]));
		}
		// delete workstation forms
		removeAllFormsFromUnit(context, unitOID.getPk(), projectOID, workstationOID);

		// delete workstation users
		PersistWrapper.delete("delete from TAB_UNIT_USERS where unitPk = ? and projectPk = ? and workstationPk = ?",
				unitOID.getPk(), projectOID.getPk(), workstationOID.getPk());

		// now delete workstations
		UnitWorkstation uw = getUnitWorkstationSetting(unitOID.getPk(), projectOID, workstationOID);
		uw.setUpdatedBy(context.getUser().getPk());
		uw.setEstatus(EStatusEnum.Deleted.getValue());
		PersistWrapper.update(uw);
	}

	/*
	 * Not used anywhere. Please check and remove if so.
	 */
	@Deprecated
	public static List<UnitLocationQuery> getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID)
			throws Exception
	{
		// we only need to get the workstations where the form count is greater
		// than 0;
		List<Map<String, Object>> validWs = PersistWrapper.readListAsMap(
				"select count(*) as count, uth.workstationPk as wspk from unit_testproc ut "
						+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
						+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
						+ " where uth.unitPk = ? and uth.projectPk = ? and uth.workstationPk != ? group by uth.workstationPk having count(*) > 0 ;",
				unitOID.getPk(), projectOID.getPk(), DummyWorkstation.getPk());

		if (validWs == null || validWs.size() == 0)
			return new ArrayList<UnitLocationQuery>();

		StringBuffer sb = new StringBuffer(UnitLocationQuery.fetchSQL)
				.append(" and uloc.unitPk = ? and uloc.projectPk = ?").append(" and uloc.workstationPk in (");

		String sep = "";
		for (Iterator iterator = validWs.iterator(); iterator.hasNext();)
		{
			Map<String, Object> aWs = (Map<String, Object>) iterator.next();
			sb.append(sep);
			sb.append(aWs.get("wspk"));
			sep = ",";
		}
		sb.append(")");
		sb.append(" and uloc.current = 1");
		return PersistWrapper.readList(UnitLocationQuery.class, sb.toString(), unitOID.getPk(), projectOID.getPk());
	}

	public static UnitLocation getUnitWorkstation(int unitPk, ProjectOID projectOID, WorkstationOID workstationOID)
			throws Exception
	{
		String sql = "select * from TAB_UNIT_LOCATION where  unitPk = ? and projectPk = ? and workstationPk=? and current = 1 ";
		return PersistWrapper.read(UnitLocation.class, sql, unitPk, projectOID.getPk(), workstationOID.getPk());
	}

	public static UnitLocationQuery getUnitWorkstationStatus(UnitOID unitOID, ProjectOID projectOID,
			WorkstationOID workstationOID)
	{
		String sql = UnitLocationQuery.fetchSQL
				+ " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.workstationPk=? and uloc.current = 1";
		return PersistWrapper.read(UnitLocationQuery.class, sql, unitOID.getPk(), projectOID.getPk(),
				workstationOID.getPk());
	}

	public static List<UnitLocationQuery> getUnitWorkstationStatusHistory(int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID) throws Exception
	{
		String sql = UnitLocationQuery.fetchSQL
				+ " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.workstationPk=? order by moveInDate desc";
		return PersistWrapper.readList(UnitLocationQuery.class, sql, unitPk, projectOID.getPk(),
				workstationOID.getPk());
	}

	public static List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID)
			throws Exception
	{
		return getUnitLocationHistory(unitOID, projectOID, false);
	}

	public static List<UnitLocationQuery> getUnitLocationHistory(UnitOID unitOID, ProjectOID projectOID,
			boolean includeChildUnits) throws Exception
	{
		if (includeChildUnits)
		{
			UnitInProjectObj unitInProject = UnitManager.getUnitInProject(unitOID, projectOID);

			String sql = UnitLocationQuery.fetchSQL + " and uloc.unitPk in (select pk from TAB_UNIT where "
					+ " ( (pk = ?) or (rootParentPk = ? and heiCode like ?) ) " + " ) "
					+ "and uloc.projectPk = ? and uloc.current = 1";
			return PersistWrapper.readList(UnitLocationQuery.class, sql, unitInProject.getUnitPk(),
					unitInProject.getRootParentPk(), unitInProject.getHeiCode() + ".%", projectOID.getPk());
		} else
		{
			String sql = UnitLocationQuery.fetchSQL
					+ " and uloc.unitPk = ? and uloc.projectPk = ? and uloc.current = 1";
			return PersistWrapper.readList(UnitLocationQuery.class, sql, unitOID.getPk(), projectOID.getPk());
		}
	}

	public static void setUnitWorkstationStatus(UserContext userContext, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, String status) throws Exception
	{

		// create the new unit location on any change, so we have a history of
		// all activities.
		UnitLocation nuLoc = new UnitLocation();
		nuLoc.setProjectPk(projectOID.getPk());
		nuLoc.setMovedInBy(userContext.getUser().getPk());
		nuLoc.setMoveInDate(new Date());
		nuLoc.setUnitPk(unitPk);
		nuLoc.setWorkstationPk(workstationOID.getPk());
		nuLoc.setStatus(status);
		nuLoc.setCurrent(1);

		// copy any values from the old rec to the new one. and mark it as not
		// current
		UnitLocation currentRec = getUnitWorkstation(unitPk, projectOID, workstationOID);
		if (currentRec != null)
		{
			currentRec.setCurrent(0);
			PersistWrapper.update(currentRec);

			nuLoc.setFirstFormAccessDate(currentRec.getFirstFormAccessDate());
			nuLoc.setFirstFormLockDate(currentRec.getFirstFormLockDate());
			nuLoc.setFirstFormSaveDate(currentRec.getFirstFormSaveDate());
			nuLoc.setLastFormAccessDate(currentRec.getLastFormAccessDate());
			nuLoc.setLastFormLockDate(currentRec.getLastFormLockDate());
			nuLoc.setLastFormUnlockDate(currentRec.getLastFormUnlockDate());
			nuLoc.setLastFormSaveDate(currentRec.getLastFormSaveDate());
			nuLoc.setCompletedDate(currentRec.getCompletedDate());
		}

		// if the status is being changed to completed, set the completed date
		if (UnitLocation.STATUS_COMPLETED.equals(status))
		{
			nuLoc.setCompletedDate(new Date());

			// if response is in inprogress status, we need to change it to
			// paused status
			List<UnitFormQuery> currentLocationTestList = TestProcManager.getTestProcsForItem(userContext, unitPk,
					projectOID, workstationOID, false);
			for (Iterator iterator = currentLocationTestList.iterator(); iterator.hasNext();)
			{
				UnitFormQuery testProc = (UnitFormQuery) iterator.next();
				ResponseMasterNew response = SurveyResponseDelegate
						.getLatestResponseMasterForTest(new TestProcOID(testProc.getPk(), null));
				if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
				{
					SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
							ResponseMasterNew.STATUS_PAUSED);
				}
			}

			// any andons in on this unit in this workstation should be closed
			// when the workstation is marked as completed
			AndonManager.markAllAndonsForUnitOnWorkstationAsClosed(userContext, new UnitOID(unitPk), projectOID,
					workstationOID);
		}

		PersistWrapper.createEntity(nuLoc);

		if (UnitLocation.STATUS_IN_PROGRESS.equals(status) || UnitLocation.STATUS_WAITING.equals(status))
		{
			// create the placeholder response and workflow entries for the
			// forms associated to that unit in that location
			List<UnitFormQuery> currentLocationTestList = TestProcManager.getTestProcsForItem(userContext, unitPk,
					projectOID, workstationOID, false);
			for (Iterator iterator = currentLocationTestList.iterator(); iterator.hasNext();)
			{
				UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();

				if (UnitLocation.STATUS_IN_PROGRESS.equals(status))
				{

					// if there is no response for this form already, create
					// response placeholder..this is needed when opening for the
					// first time
					// this is required since when filling up responses from
					// different tablets at the same time and submitting, they
					// need to synchronize on the same
					// response id. so a placeholder is created as soon as the
					// form is opened..

					ResponseMasterNew response = SurveyResponseManager
							.getLatestResponseMasterForTest(unitFormQuery.getOID());
					if (response == null)
					{
						SurveyDefinition sd = SurveyDefFactory
								.getSurveyDefinition(new FormOID(unitFormQuery.getFormPk(), null));
						SurveyResponse sResponse = new SurveyResponse(sd);
						sResponse.setSurveyPk(unitFormQuery.getFormPk());
						sResponse.setTestProcPk(unitFormQuery.getPk());
						sResponse.setResponseStartTime(new Date());
						sResponse.setResponseCompleteTime(new Date());
						// ipaddress and mode set
						sResponse.setIpaddress("0.0.0.0");
						sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
						sResponse.setUser((User) userContext.getUser());
						sResponse = SurveyResponseManager.ceateDummyResponse(userContext, sResponse);
						// the save pageresponse automatically creates a new
						// workflow entry for you..
						// so we need not create another one.. so commenting the
						// below one..
					} else
					{
						// the response is there, so if it is in paused status,
						// we need to change it to InProgress
						if (response != null && ResponseMasterNew.STATUS_PAUSED.equals(response.getStatus()))
						{
							SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
									ResponseMasterNew.STATUS_INPROGRESS);
						}
					}
				} else if (UnitLocation.STATUS_WAITING.equals(status))
				{
					// if response is in inprogress status, we need to change it
					// to paused status
					ResponseMasterNew response = SurveyResponseDelegate
							.getLatestResponseMasterForTest(unitFormQuery.getOID());
					if (response != null && ResponseMasterNew.STATUS_INPROGRESS.equals(response.getStatus()))
					{
						SurveyResponseManager.changeResponseStatus(userContext, response.getResponseId(),
								ResponseMasterNew.STATUS_PAUSED);
					}
				}
			}
		}
	}

	public static void recordWorkstationFormAccess(TestProcOID testProcOID)
	{
		try
		{
			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());
			UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
					new ProjectOID(testProc.getProjectPk(), null),
					new WorkstationOID(testProc.getWorkstationPk(), null));
			if (currentRec != null)
			{
				Date now = new Date();
				if (currentRec.getFirstFormAccessDate() == null)
					currentRec.setFirstFormAccessDate(now);
				currentRec.setLastFormAccessDate(now);

				PersistWrapper.update(currentRec);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void recordWorkstationSave(TestProcOID testProcOID)
	{
		try
		{
			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());
			UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
					new ProjectOID(testProc.getProjectPk(), null),
					new WorkstationOID(testProc.getWorkstationPk(), null));
			if (currentRec != null)
			{
				Date now = new Date();
				if (currentRec.getFirstFormSaveDate() == null)
					currentRec.setFirstFormSaveDate(now);
				currentRec.setLastFormSaveDate(now);

				PersistWrapper.update(currentRec);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void recordWorkstationFormLock(TestProcOID testProcOID)
	{
		try
		{
			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());
			UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
					new ProjectOID(testProc.getProjectPk(), null),
					new WorkstationOID(testProc.getWorkstationPk(), null));
			if (currentRec != null)
			{
				Date now = new Date();
				if (currentRec.getFirstFormLockDate() == null)
					currentRec.setFirstFormLockDate(now);
				currentRec.setLastFormLockDate(now);

				PersistWrapper.update(currentRec);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void recordWorkstationFormUnlock(TestProcOID testProcOID)
	{
		try
		{
			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());
			UnitLocation currentRec = getUnitWorkstation(testProc.getUnitPk(),
					new ProjectOID(testProc.getProjectPk(), null),
					new WorkstationOID(testProc.getWorkstationPk(), null));
			if (currentRec != null)
			{
				Date now = new Date();
				currentRec.setLastFormUnlockDate(now);

				PersistWrapper.update(currentRec);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static List<AssignedTestsQuery> getAssignedFormsNew(UserOID user, String role) throws Exception
	{
		String lookForTestStatus = "";
		String workFlowStatus = "";
		String commonWorkFlowStatus = ResponseMasterNew.STATUS_REJECTED;
		if (role.equals(User.ROLE_TESTER))
		{
			lookForTestStatus = ResponseMasterNew.STATUS_INPROGRESS;
			workFlowStatus = ResponseMasterNew.STATUS_INPROGRESS;
		} else if (role.equals(User.ROLE_VERIFY))
		{
			lookForTestStatus = ResponseMasterNew.STATUS_COMPLETE;
			workFlowStatus = ResponseMasterNew.STATUS_COMPLETE;
		} else if (role.equals(User.ROLE_APPROVE))
		{
			lookForTestStatus = ResponseMasterNew.STATUS_VERIFIED;
			workFlowStatus = ResponseMasterNew.STATUS_VERIFIED;
		}

		String sql = "select ut.pk as testProcPk, uth.name as testProcName,"
				+ " p.pk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription,"
				+ " uth.unitPk as unitPk, uh.unitName as unitName, uh.unitDescription as unitDescription,"
				+ " uth.workstationPk as workstationPk, w.workstationName as workstationName, w.description as workstationDescription, w.orderNo as workstationOrderNo,"
				+ " site.name as workstationSiteName, f.formMainPk as formMainPk, tfa.formFk as formPk, f.identityNumber as formName, f.description as formDescription,"
				+ " f.revision as formRevision," + " f.versionNo as formVersion,"
				+ " fwf.comments as comment, fwf.date as assignedDate,"
				+ " r.responseId, r.percentComplete, r.totalQCount, r.totalACount, r.noOfComments as commentCount, r.passCount, r.failCount,"
				+ " r.dimentionalFailCount, r.naCount, r.status as responseStatus, "
				+ " transferData.transferCount as oilTransferCount" + " from " 
				+ " unit_testproc ut "
				+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
				+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
				+ " join TAB_RESPONSE r on r.testProcPk = ut.pk and r.surveyPk = tfa.formFk and r.current = 1 "
				+ " left outer join " + "		(select ut1.pk ufPk, count(*) as transferCount "
				+ "		from unit_testproc ut1 "
				+ "     join testproc_form_assign tfa1 on tfa1.testProcFk = ut1.pk and tfa1.current = 1 "
				+ " 	join unit_testproc_h uth1 on uth1.unitTestProcFk = ut1.pk and now() between uth1.effectiveDateFrom and uth1.effectiveDateTo "
				+ " 	join  TAB_RESPONSE r on r.testProcPk = ut1.pk and r.surveyPk = tfa1.formFk and r.current = 1 "
				+ "		join TAB_UNIT_USERS uu on uu.unitPk = uth1.unitPk and uu.projectPk = uth1.projectPk and uu.workstationPk = uth1.workstationPk "
				+ " 	join  TAB_ITEM_RESPONSE ires on ires.responseId = r.responseId "
				+ " 	join TESTITEM_OIL_TRANSFER oilTransfer on oilTransfer.itemResponsePk = ires.pk "
				+ "		where " + "		uu.userPk = ? and uu.role = ? and r.status=? "
				+ " 	group by ut1.pk) transferData on transferData.ufPk = ut.pk "
				+ " join TAB_UNIT u on uth.unitPk = u.pk "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
				+ " join TAB_PROJECT p on uth.projectPk = p.pk and p.status = 'Open' "
				+ " join TAB_WORKSTATION w on uth.workstationPk = w.pk "
				+ " join unit_project_ref upr on upr.projectPk = uth.projectPk and upr.unitPk = uth.unitPk "
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status= 'Open' "
				+ " join site on w.sitePk = site.pk "
				+ " join TAB_UNIT_USERS uu on uu.unitPk = uth.unitPk and uu.projectPk = uth.projectPk and uu.workstationPk = uth.workstationPk "
				+ " join TAB_UNIT_LOCATION ul on ul.unitPk = uth.unitPk and ul.projectPk = uth.projectPk and ul.workstationPk = uth.workstationPk and ul.status = 'In Progress' and ul.current = 1 "
				+ " join TAB_FORM_WORKFLOW fwf on fwf.responseId = r.responseId and fwf.current  = 1  "
				+ " join TAB_SURVEY f on r.surveyPk = f.pk and f.formType = 1 " + " where "
				+ " uu.userPk = ? and uu.role = ? and r.status=? " + " order by fwf.date desc";

		List<AssignedTestsQuery> myAssignments = PersistWrapper.readList(AssignedTestsQuery.class, sql, user.getPk(),
				role, lookForTestStatus, user.getPk(), role, lookForTestStatus);
		return myAssignments;
	}

	@Deprecated
	/**
	 * Use method public static List <AssignedTestsQuery>
	 * getAssignedFormsNew(User user, String role)throws Exception
	 * 
	 * @param user
	 * @param role
	 * @return
	 * @throws Exception
	 */

	public static OElement getFormWorkflowEvents(int testProcPk) throws Exception
	{
		List<FormWorkflowQuery> workFlows = PersistWrapper.readList(FormWorkflowQuery.class,
				FormWorkflowQuery.sql + " and fwf.testProcPk=? and fwf.action != ? order by fwf.date desc", testProcPk,
				FormWorkflow.ACTION_START);

		OElement root = new OElement("root");
		for (Iterator iterator = workFlows.iterator(); iterator.hasNext();)
		{
			FormWorkflowQuery formWorkflow = (FormWorkflowQuery) iterator.next();

			ResponseMasterNew response = SurveyResponseDelegate.getResponseMaster(formWorkflow.getResponseId());

			OElement fE = new OElement("workitem");
			fE.setAttribute("form", formWorkflow);
			fE.setAttribute("response", response);

			root.addChild(fE);
		}

		return root;
	}

	public static void deleteProject(int projectPk) throws Exception
	{
		int unitCount = PersistWrapper.read(Integer.class,
				"select count(u.pk) from TAB_UNIT u join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk=?",
				projectPk);
		if (unitCount == 0)
		{
			PersistWrapper.delete("delete from TAB_PROJECT_FORMS where projectPk=?", projectPk);
			PersistWrapper.delete("delete from TAB_PROJECT_USERS where projectPk=?", projectPk);
			PersistWrapper.delete("delete from TAB_PROJECT_WORKSTATIONS where projectPk=?", projectPk);
			PersistWrapper.delete("delete from TAB_PROJECT where pk=?", projectPk);
		} else
		{
			List errors = new ArrayList();
			errors.add("Project cannot be deleted if Units are created for the project.");
			throw new AppException((String[]) errors.toArray(new String[errors.size()]));
		}
	}

	public static void deleteWorstation(UserContext context, WorkstationOID workstationOID) throws Exception
	{
		List errors = new ArrayList();

		Account acc = (Account) context.getAccount();
		User user = (User) context.getUser();

		Workstation workstation = PersistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());

		List pws = PersistWrapper.readList(ProjectWorkstation.class,
				"select * from TAB_PROJECT_WORKSTATIONS where workstationPk=?", workstationOID.getPk());
		if (pws != null && pws.size() > 0)
		{
			errors.add(
					"Workstation is associated with projects; Please remove workstation from projects and try again.");
		}
		pws = PersistWrapper.readList(UnitWorkstation.class,
				"select * from TAB_UNIT_WORKSTATIONS where workstationPk=?", workstationOID.getPk());
		if (pws != null && pws.size() > 0)
		{
			errors.add("Workstation is associated with units; Please remove workstation from units and try again.");
		}
		if (errors.size() > 0)
			throw new AppException((String[]) errors.toArray(new String[errors.size()]));

		// mark the workstation as deleted
		workstation.setEstatus(EStatusEnum.Deleted.getValue());
		workstation.setUpdatedBy(context.getUser().getPk());
		PersistWrapper.update(workstation);
	}

	public static void removeUnitFromProject(UserContext context, UnitOID unitOID, ProjectOID projectOID,
			DeleteOptionEnum deleteUnitOption) throws Exception
	{
		// I have to check in the Response_desc table to see if there are any
		// entries made..
		// this is because as soon as a workstation is activated, a dummy
		// response is entered into TAB_RESPONSE
		// to coordinate save/submit between tablets..
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();
		UnitInProjectObj uprToDelete = uprDAO.getUnitInProject(unitOID, projectOID);
		if (DeleteOptionEnum.MoveChildrenToRoot == deleteUnitOption)
		{
			List<UnitInProjectObj> childs = uprDAO.getDirectChildren(uprToDelete.getOID());
			for (Iterator iterator = childs.iterator(); iterator.hasNext();)
			{
				UnitInProjectObj upr = (UnitInProjectObj) iterator.next();
				UnitManager.changeUnitParent(context, null, new UnitOID(upr.getUnitPk()), projectOID);
			}
		} else if (DeleteOptionEnum.DeleteTree == deleteUnitOption)
		{
			List<UnitInProjectObj> childs = uprDAO.getAllChildrenInTree(uprToDelete.getOID());
			for (int i = 0; i < childs.size(); i++)
			{
				removeUnitFromProjectInt(context, childs.get(i));
			}
		}
		removeUnitFromProjectInt(context, uprToDelete);

		// check of the unit is part of any other project. If not we rename the
		// unit with a Del marker
		// this way the unitName can be re-used. users could create a new unit
		// with improper settings, then delete it and try to re-create it.
		List<ProjectQuery> assignedProjects = UnitManager.getUnitAssignedProjects(new UnitOID(uprToDelete.getUnitPk()));
		if (assignedProjects.size() == 1 && assignedProjects.get(0).getPk() == projectOID.getPk())
		{
			// this means the unit is assigned only to this project

			List<UnitH> unitHRecords = PersistWrapper.readList(UnitH.class,
					"select * from tab_unit_h where unitPk = ? ", uprToDelete.getUnitPk());
			for (Iterator iterator = unitHRecords.iterator(); iterator.hasNext();)
			{
				UnitH unitH = (UnitH) iterator.next();
				unitH.setUnitName(unitH.getUnitName() + "-Del-" + uprToDelete.getUnitPk());
				unitH.setSerialNo(unitH.getSerialNo() + "-Del-" + uprToDelete.getUnitPk());
				PersistWrapper.update(unitH);
			}
		}

	}

	private static void removeUnitFromProjectInt(UserContext context, UnitInProjectObj unitInProjectObj)
			throws Exception
	{
		int count = PersistWrapper.read(Integer.class,
				"select count(*) from TAB_RESPONSE r " + " join TAB_UNIT_FORMS uf on r.testProcPk = uf.pk " + " where "
						+ " r.status in (?,?,?,?,?,?) and " + " uf.unitPk=? and uf.projectPk = ? and uf.estatus = 1",
				ResponseMasterNew.STATUS_COMPLETE, ResponseMasterNew.STATUS_VERIFIED, 
				ResponseMasterNew.STATUS_APPROVED, ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS,
				ResponseMasterNew.STATUS_PAUSED, ResponseMasterNew.STATUS_REJECTED, unitInProjectObj.getUnitPk(),
				unitInProjectObj.getProjectPk());
		if (count > 0)
			throw new AppException("Forms have been submitted for this unit, Unit cannot be deleted");

		count = PersistWrapper.read(Integer.class, "select count(*) from openitem_v2 where unitPk=? and projectPk = ?",
				unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
		if (count > 0)
			throw new AppException("Open items have been created for this unit, Unit cannot be deleted");

		count = PersistWrapper.read(Integer.class,
				"select count(*) from ncr_unit_assign nua where nua.unitFk=? and nua.projectFk = ?",
				unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
		if (count > 0)
			throw new AppException("NCRs have been associated to this unit, Unit cannot be deleted");

		count = PersistWrapper.read(Integer.class, "select count(*) from andon where unitPk=? and projectPk = ?",
				unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
		if (count > 0)
			throw new AppException("Andons have been raised for this unit, Unit cannot be deleted");

		PersistWrapper.delete("delete from TAB_UNIT_USERS where unitPk=? and projectPk = ?",
				unitInProjectObj.getUnitPk(), unitInProjectObj.getProjectPk());
		new UnitInProjectDAO().removeUnit(context, unitInProjectObj);
	}

	public static void closeProject(ProjectQuery projectQuery) throws Exception
	{
		// TODO some other logic need to be done when a project is closed
		Project p = PersistWrapper.readByPrimaryKey(Project.class, projectQuery.getPk());
		p.setStatus(Project.STATUS_CLOSED);
		PersistWrapper.update(p);
	}

	public static void openProject(ProjectQuery projectQuery) throws Exception
	{
		// TODO some other process need to be done when a project is opened
		Project p = PersistWrapper.readByPrimaryKey(Project.class, projectQuery.getPk());
		p.setStatus(Project.STATUS_OPEN);
		PersistWrapper.update(p);
	}

	public static void openUnit(UserContext context, UnitBean rootUnitToOpen, List<UnitBean> unitBeanAndChildrenList,
			ProjectOID lastOpenProjectOID, ProjectOID destinationProjectOID) throws Exception
	{
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();

		HashMap<UnitOID, UnitBean> unitBeanMap = new HashMap<>();
		for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
		{
			UnitBean unitBean = (UnitBean) iterator.next();
			unitBeanMap.put(unitBean.getOID(), unitBean);
		}

		// build the new UnitBeanHeirarchy from the unitBeanList
		List<UnitBean> rootBeanList = new ArrayList<>();
		for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
		{
			UnitBean unitBean = (UnitBean) iterator.next();

			UnitBean parentBean = null;
			if (unitBean.getParentPk() != null)
			{
				parentBean = unitBeanMap.get(new UnitOID(unitBean.getParentPk()));
			}
			if (parentBean == null)
			{
				rootBeanList.add(unitBean);
			} else
			{
				unitBean.setParent(parentBean);
				if (parentBean.getChildren() == null)
					parentBean.setChildren(new ArrayList<UnitBean>());
				parentBean.getChildren().add(unitBean);
			}
		}
		if (rootBeanList.size() != 1)
		{
			throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
		}

		List<UnitQuery> destProjectUnitList = UnitManager.getAllChildrenUnitsRecursive(rootUnitToOpen.getOID(),
				destinationProjectOID);

		openUnitRec(context, uprDAO, rootUnitToOpen, unitBeanMap, lastOpenProjectOID, destinationProjectOID,
				destProjectUnitList);

		// what ever is remaining in the destProjectUnitList are the ones that
		// we should move to root.
		// find the parents of the unit trees which should be moved as root
		// units in project.
		// add the unitLists to a hashmap for easy retrieval
		HashMap<UnitOID, UnitQuery> unitMap = new HashMap<>();
		for (Iterator iterator = destProjectUnitList.iterator(); iterator.hasNext();)
		{
			UnitQuery unitQuery = (UnitQuery) iterator.next();
			unitMap.put(unitQuery.getUnitOID(), unitQuery);
		}

		List<UnitOID> roots = new ArrayList<>();
		for (Iterator iterator = destProjectUnitList.iterator(); iterator.hasNext();)
		{
			UnitQuery unitQ = (UnitQuery) iterator.next();
			UnitQuery parentQ = null;
			if (unitQ.getParentPk() != null)
			{
				parentQ = unitMap.get(new UnitOID(unitQ.getParentPk()));
			}
			if (parentQ == null)
			{
				roots.add(unitQ.getUnitOID());
			}
		}

		for (Iterator iterator = roots.iterator(); iterator.hasNext();)
		{
			UnitOID unitOID = (UnitOID) iterator.next();
			UnitManager.changeUnitParent(context, null, unitOID, destinationProjectOID);
		}

	}

	private static void openUnitRec(UserContext context, UnitInProjectDAO uprDAO, UnitBean unitBean,
			HashMap<UnitOID, UnitBean> unitBeanMap, ProjectOID srcProjectOID, ProjectOID destProjectOID,
			List<UnitQuery> destProjectUnitList) throws Exception
	{
		// set the projectPartFk which is mapped by the user into the unitBean
		UnitBean mappedBean = unitBeanMap.get(unitBean.getOID());
		if (mappedBean != null)
		{
			unitBean.setProjectPartPk(mappedBean.getProjectPartPk());
		}

		// remove this unit from the distinationUnitList
		UnitQuery toRemoveCheck = new UnitQuery();
		toRemoveCheck.setUnitPk(unitBean.getPk());
		toRemoveCheck.setProjectPk(destProjectOID.getPk());
		destProjectUnitList.remove(toRemoveCheck);

		// if the unit is not part of the desination project, we need to add
		// this unit to the project.
		UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unitBean.getOID(), destProjectOID);
		if (unitInProject == null)
		{
			boolean copyFormConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
			boolean copyUserConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);
			
			// add unit to the project, create the project_unit_ref entry
			unitInProject = addUnitToProjectInt(context, uprDAO, unitBean, destProjectOID,
					new UnitOID(unitBean.getParentPk()), false, copyUserConfig, copyFormConfig, false, null);
		} 
		else
		{
			// check if we need to update the projectPart value
			if (unitInProject.getProjectPartPk() == null && unitBean.getProjectPartPk() != null)
			{
				unitInProject.setProjectPartPk(unitBean.getProjectPartPk());
				uprDAO.saveUnitInProject(context, unitInProject, new Actions[] { Actions.updateUnit });
				unitInProject = uprDAO.getUnitInProject(unitBean.getOID(), destProjectOID);
			}
		}

		if (UnitOriginType.Manufactured == unitBean.getUnitOriginType())
		{
			boolean isUnitOpenInAnyProject = UnitManager.isUnitOpenInAnyProjects(unitBean.getOID());
			if (isUnitOpenInAnyProject)
			{
				Project project = getProjectWhereUnitIsOpen(unitBean.getOID());
				if (project.getPk() != destProjectOID.getPk())
					throw new AppException(
							String.format("Unit %s is open in project %s, Unit cannot be opened in this project",
									unitBean.getOID().getDisplayText(), project.getDisplayString()));
			}
			if (UnitInProject.STATUS_PLANNED.equals(unitInProject.getStatus()))
			{
				// the status is being changed from planned to open, so copy the
				// workstation, users, forms etc.
				// copy project workstations to unitworkstations
				boolean copyFormConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
				boolean copyUserConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);
				setWorkstationsAndTeamsOnUnitOpen(context, destProjectOID, unitBean.getOID(), unitInProject, copyUserConfig, copyFormConfig);

			}
		}

		if (!(UnitInProject.STATUS_OPEN.equals(unitInProject.getStatus())))
		{
			unitInProject.setStatus(UnitInProject.STATUS_OPEN);
			uprDAO.saveUnitInProject(context, unitInProject, new Actions[] { Actions.openUnitInProject });
		}

		// now we have to open all chileren units of this unit
		List<UnitQuery> children = UnitManager.getChildrenUnits(srcProjectOID, unitBean.getOID());
		List<UnitBean> childrenUnits = new ArrayList<UnitBean>();
		for (Iterator iterator = children.iterator(); iterator.hasNext();)
		{
			UnitQuery unitQuery = (UnitQuery) iterator.next();
			UnitBean aBean = unitQuery.getUnitBean();
			aBean.setProjectPk(destProjectOID.getPk());
			childrenUnits.add(aBean);

		}
		if (childrenUnits != null)
		{
			for (Iterator iterator = childrenUnits.iterator(); iterator.hasNext();)
			{
				UnitBean aUnit = (UnitBean) iterator.next();
				openUnitRec(context, uprDAO, aUnit, unitBeanMap, srcProjectOID, destProjectOID, destProjectUnitList);
			}
		}
	}

	private static void setWorkstationsAndTeamsOnUnitOpen(UserContext context, ProjectOID projectOID, UnitOID unitOID,
			UnitInProjectObj unitInProject, 
			boolean copyProjectWorkstationUsersToUnit, boolean copyProjectWorkstationFormsToUnit) throws Exception
	{
		// the status is being changed from planned to open, so copy the
		// workstation, users, forms etc.
		// copy project workstations to unitworkstations
		List<WorkstationQuery> workstations = ProjectManager.getWorkstationsForProject(projectOID.getPk());
		for (Iterator iterator = workstations.iterator(); iterator.hasNext();)
		{
			if(copyProjectWorkstationFormsToUnit == false && copyProjectWorkstationUsersToUnit == false)
				continue;
			
			WorkstationQuery workstationQuery = (WorkstationQuery) iterator.next();

			// if workstation is already added, skip;
			UnitWorkstation uw = ProjectDelegate.getUnitWorkstationSetting(unitOID.getPk(), projectOID,
					workstationQuery.getOID());
			if (uw != null)
				continue;

			// copy forms
			if (unitInProject.getProjectPartPk() == null)
				continue;

			List<ProjectFormQuery> pForms = ProjectTemplateManager.getTestProcsForProjectPart(projectOID,
					new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());

			// check if there are any users defined for that workstation
			List<ProjectUserQuery> pUsers = ProjectManager.getProjectUserQueryList(projectOID,
					new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getOID());

			if (pForms.size() == 0 && pUsers.size() == 0)
				continue; // dont copy the workstation for which there are no
							// forms added for part type or there are no teams
							// defined at that workstation.

			// create unit workstation
			UnitWorkstation unitWorkstation = new UnitWorkstation();
			unitWorkstation.setEstatus(EStatusEnum.Active.getValue());
			unitWorkstation.setUpdatedBy(context.getUser().getPk());
			unitWorkstation.setProjectPk(projectOID.getPk());
			unitWorkstation.setUnitPk(unitOID.getPk());
			unitWorkstation.setWorkstationPk(workstationQuery.getPk());
			int unitWorkstationPk = PersistWrapper.createEntity(unitWorkstation);

			// copy projectUsers to unitusers
			if(copyProjectWorkstationUsersToUnit)
				copyProjectUsersToUnit(projectOID, unitOID, workstationQuery.getOID(), true);

			if(copyProjectWorkstationFormsToUnit)
				copyProjectFormsToUnit(context, projectOID, pForms, unitOID, workstationQuery.getOID());

			String defaultWorkstationStatusValue = (String) new CommonServicesDelegate().getEntityPropertyValue(projectOID,
					ProjectPropertyEnum.SetNewWorkstationsDefaultStatusTo.getId(), String.class);
			if (defaultWorkstationStatusValue != null && (!(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue))))
			{
				if(UnitLocation.STATUS_WAITING.equals(defaultWorkstationStatusValue) || UnitLocation.STATUS_IN_PROGRESS.equals(defaultWorkstationStatusValue)
						|| UnitLocation.STATUS_COMPLETED.equals(defaultWorkstationStatusValue))
				{
					setUnitWorkstationStatus(context, unitOID.getPk(), projectOID, workstationQuery.getOID(),
							defaultWorkstationStatusValue);
				}
				else
				{
					throw new AppException("Default status value configured for workstation status is invalid. Please contact support");
				}
			}

		}
	}

	private static void setPartSpecificSettingsToUnit(UserContext context, ProjectOID projectOID, UnitOID unitOID,
			UnitInProjectObj unitInProject, WorkstationOID workstationOID) throws Exception
	{
		// if workstation is already added, skip;
		UnitWorkstation uw = ProjectDelegate.getUnitWorkstationSetting(unitOID.getPk(), projectOID,
				workstationOID);
		
		if (uw != null)
			return;

		// copy forms
		if (unitInProject.getProjectPartPk() == null)
			return;

		List<ProjectFormQuery> pForms = getProjectPartAssignedForms(projectOID,
				new ProjectPartOID(unitInProject.getProjectPartPk()));

		// check if there are any users defined for that workstation
		List<ProjectUserQuery> pUsers = ProjectManager.getProjectUserQueryList(projectOID,
				new ProjectPartOID(unitInProject.getProjectPartPk()), workstationOID);


		// create unit workstation
		UnitWorkstation unitWorkstation = new UnitWorkstation();
		unitWorkstation.setEstatus(EStatusEnum.Active.getValue());
		unitWorkstation.setUpdatedBy(context.getUser().getPk());
		unitWorkstation.setProjectPk(projectOID.getPk());
		unitWorkstation.setUnitPk(unitOID.getPk());
		unitWorkstation.setWorkstationPk(workstationOID.getPk());
		int unitWorkstationPk = PersistWrapper.createEntity(unitWorkstation);

		// copy projectUsers to unitusers
		copyProjectUsersToUnit(projectOID, unitOID, workstationOID, true);

		copyProjectFormsToUnit(context, projectOID, pForms, unitOID, workstationOID);
		
		setUnitWorkstationStatus(context, unitOID.getPk(), projectOID, workstationOID, UnitLocation.STATUS_IN_PROGRESS);
		
	}

	public static void removeUserFromUnit(int userPk, int unitPk, ProjectOID projectOID, WorkstationOID workstationOID,
			String role) throws Exception
	{
		// I think we need not do any check for this.. the user was there
		// earlier.. but
		// now he needs to be removed.. so he does not have access moving
		// forward. i think this is ok

		UnitUser uu = PersistWrapper.read(UnitUser.class,
				"select * from TAB_UNIT_USERS where userPk=? and unitPk=? and projectPk = ? and workstationPk=? and role=?",
				userPk, unitPk, projectOID.getPk(), workstationOID.getPk(), role);
		PersistWrapper.deleteEntity(uu);
	}

	public static List<ProjectFormQuery> getProjectFormAssignmentsForForm(FormMainOID formMainOID) throws Exception
	{
		return PersistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and form.formMainPk = ?",
				formMainOID.getPk());
	}

	public static List<UnitFormQuery> getTestProcsByForm(FormQuery formQuery) throws Exception
	{
		List<UnitFormQuery> l = PersistWrapper.readList(UnitFormQuery.class,
				UnitFormQuery.sql + " and tfa.formFk in "
						+ "(select pk from TAB_SURVEY where formMainPk=? and formType = 1) order by unitPk desc",
				formQuery.getFormMainPk());

		return l;
	}

	public static void markUnitAsClosed(UserContext context, UnitOID unitOID, ProjectOID projectOID) throws Exception
	{
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();

		// get the workstation testproc status count report and hash it based on
		// unitPk-workstationPk
		TestProcStatusSummaryReportRequest request = new TestProcStatusSummaryReportRequest();
		request.setProjectOIDList(Arrays.asList(new ProjectOID[] { projectOID }));
		request.setUnitOID(unitOID);
		request.setIncludeChildrenUnits(true);
		request.setProjectOIDForUnitHeirarchy(projectOID);
		request.setGroupingSet(Arrays.asList(new GroupingCol[] { GroupingCol.unit, GroupingCol.workstation }));
		TestProcStatusSummaryReport report = new TestProcStatusSummaryReport(context, request);
		TestProcStatusSummaryReportResult result = report.runReport();
		List<TestProcStatusSummaryReportResultRow> resultRows = result.getReportResult();
		HashMap<String, TestProcStatusSummaryReportResultRow> unitWsLookupMap = new HashMap<>();
		for (Iterator iterator = resultRows.iterator(); iterator.hasNext();)
		{
			TestProcStatusSummaryReportResultRow testProcStatusSummaryReportResultRow = (TestProcStatusSummaryReportResultRow) iterator
					.next();
			unitWsLookupMap.put(
					testProcStatusSummaryReportResultRow.getUnitPk() + "-"
							+ testProcStatusSummaryReportResultRow.getWorkstationPk(),
					testProcStatusSummaryReportResultRow);
		}

		// loop through the workstations for units and mark them either complete
		// or paused
		// get the unitWorkstationList for the unit and its children.
		List<UnitWorkstationQuery> unitWorkstationList = ProjectManager.getWorkstationsForUnit(unitOID, projectOID,
				true);
		for (Iterator iterator = unitWorkstationList.iterator(); iterator.hasNext();)
		{
			UnitWorkstationQuery aUnitWorkstation = (UnitWorkstationQuery) iterator.next();
			if (!(UnitLocation.STATUS_COMPLETED.equals(aUnitWorkstation.getStatus())))
			{
				// if the workstation status is not complete, we should change
				// it to Waiting so that all incomplete forms will become
				// paused.
				// but check if all forms are approved..if that is the case we
				// can mark it as complete

				TestProcStatusSummaryReportResultRow testProcStatus = unitWsLookupMap
						.get(aUnitWorkstation.getUnitPk() + "-" + aUnitWorkstation.getWorkstationPk());
				long totalTestProcsInWs = 0;
				long approvedTestProcsInWs = 0;
				if (testProcStatus != null)
				{
					totalTestProcsInWs = testProcStatus.getApprovedCount() + testProcStatus.getVerifiedCount()
							+ testProcStatus.getCompletedCount() + testProcStatus.getPausedCount()
							+ testProcStatus.getInProgressCount() + testProcStatus.getNotStartedCount();
					approvedTestProcsInWs = testProcStatus.getApprovedCount();
				}

				if (totalTestProcsInWs == approvedTestProcsInWs)
				{
					// this workstation is actually complete. so we can mark it
					// as complete.
					setUnitWorkstationStatus(context, unitOID.getPk(), projectOID,
							new WorkstationOID(aUnitWorkstation.getWorkstationPk()), UnitLocation.STATUS_COMPLETED);
				} else
				{
					setUnitWorkstationStatus(context, unitOID.getPk(), projectOID,
							new WorkstationOID(aUnitWorkstation.getWorkstationPk()), UnitLocation.STATUS_WAITING);
				}

			}
		}

		// mark the unit and its children as complete in the project
		List<UnitQuery> units = UnitManager.getAllChildrenUnitsRecursive(unitOID, projectOID);
		for (Iterator iterator = units.iterator(); iterator.hasNext();)
		{
			UnitQuery aUnit = (UnitQuery) iterator.next();
			UnitInProjectObj unitInProject = uprDAO.getUnitInProject(aUnit.getUnitOID(), projectOID);
			if (!(UnitInProject.STATUS_CLOSED.equals(unitInProject.getStatus())))
			{
				unitInProject.setStatus(UnitInProject.STATUS_CLOSED);
				uprDAO.saveUnitInProject(context, unitInProject, new Actions[] { Actions.closeUnitInProject });
			}
		}

	}

	private static ResponseMasterNew getFormResponse(FormQuery form, ResponseMasterNew[] responseMasterSet)
	{
		if (responseMasterSet == null || responseMasterSet.length == 0)
			return null;

		for (int i = 0; i < responseMasterSet.length; i++)
		{
			if (responseMasterSet[i].getFormPk() == form.getPk())
				return responseMasterSet[i];
		}

		return null;
	}

	public static ProjectFormQuery upgradeFormForProject(UserContext context, ProjectFormOID projectFormOID,
			int newSurveyPk) throws Exception
	{
		ProjectForm projectForm = PersistWrapper.readByPrimaryKey(ProjectForm.class, projectFormOID.getPk());
		projectForm.setFormPk(newSurveyPk);
		PersistWrapper.update(projectForm);

		return PersistWrapper.read(ProjectFormQuery.class, ProjectFormQuery.sql + " and pf.pk = ?",
				projectForm.getPk());
	}

	public static TestProcObj upgradeFormForUnit(UserContext context, TestProcOID testProcOID, int surveyPk)
			throws Exception
	{
		// we have to delete the responses for the selected projects
		// i am changing the mind, i dont think we need o delete the response,
		// but we can one
		// with a new one.. so the old one is available in history.
		// ResponseMasterNew[] responses =
		// SurveyResponseManager.getLatestResponseMastersForUnitForForm(
		// context, aUnitPk, survey.getFormMainPk());
		// for (int i = 0; i < responses.length; i++)
		// {
		// ResponseMasterNew aResponse = responses[i];
		// SurveyResponseManager.deleteResponse(aResponse.getFormPk(), new
		// int[]{aResponse.getResponseId()});
		// }

		TestProcDAO tpDAO = new TestProcDAO();
		TestProcObj testProc = tpDAO.getTestProc(testProcOID.getPk());

		// now mark the old response as old and create the new dummy response
		// for the updated testProc if one existed for it...
		ResponseMasterNew response = SurveyResponseManager.getLatestResponseMasterForTest(testProc.getOID());
		if (response != null && response.getFormPk() != surveyPk)
		{
			// means the response for this testProc already exists.. now the
			// formPk on the testProc changes..
			SurveyResponseManager.markResponseAsOld(context, response);
		}

		// update the formPk in the testProc. A new _H record will get created.
		testProc.setFormPk(surveyPk);
		testProc.setAppliedByUserFk(context.getUser().getPk());
		testProc = tpDAO.saveTestProc(context, testProc);

		// create the new dummy response for the updated testProc if one existed
		// for the old response...
		if (response != null && response.getFormPk() != surveyPk)
		{
			SurveyDefinition sd = SurveyDefFactory.getSurveyDefinition(new FormOID(surveyPk, null));
			SurveyResponse sResponse = new SurveyResponse(sd);
			sResponse.setSurveyPk(surveyPk);
			sResponse.setTestProcPk(testProc.getPk());
			sResponse.setResponseStartTime(new Date());
			sResponse.setResponseCompleteTime(new Date());
			// ipaddress and mode set
			sResponse.setIpaddress("0.0.0.0");
			sResponse.setResponseMode(SurveyForm.RESPONSEMODE_NORMAL);
			sResponse.setUser((User) context.getUser());
			sResponse = SurveyResponseManager.ceateDummyResponse(context, sResponse);
			// the createDummyResponse automatically creates a new workflow
			// entry for you..
			// so we need not create another one..
			logger.info("Created the new dummy response");
		}

		return testProc;
	}

	/**
	 * Method to copy the workstation to units, here the forms or team is not
	 * copied, only the workstation is copied
	 * 
	 * @param context
	 * @param projectQuery
	 * @param workstationQuery
	 * @param selectedUnits
	 * @throws Exception
	 */
	public static void copyWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
			WorkstationQuery workstationQuery, Integer[] selectedUnits) throws Exception
	{
		int workstationPk = workstationQuery.getPk();

		// teams
		for (int i = 0; i < selectedUnits.length; i++)
		{
			int unitPk = selectedUnits[i];
			UnitObj unit = getUnitByPk(new UnitOID(unitPk));
			List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());

			if (unitWorkstations.contains(workstationQuery))
			{
			} else
			{
				// unit does not contain that workstation
				addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
			}
		}
	}

	/**
	 * Method to set the teams for a project part to units, here the forms is
	 * not copied
	 * 
	 * @param context
	 * @param projectQuery
	 * @param workstationQuery
	 * @param selectedUnits
	 * @throws Exception
	 */
	public static void setWorkstationProjectPartTeamSetupToUnits(UserContext context, ProjectQuery projectQuery,
			WorkstationQuery workstationQuery, ProjectPartOID projectPartOID, Integer[] selectedUnits,
			boolean copyDefaultTeamIfNoProjectPartTeamIsSet) throws Exception
	{
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();
		// teams
		for (int i = 0; i < selectedUnits.length; i++)
		{
			int unitPk = selectedUnits[i];
			UnitObj unit = getUnitByPk(new UnitOID(unitPk));

			UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
			if (unitInProject == null)
				throw new AppException("Unit is not associated with the project, Team cannot be set.");

			if (unitInProject.getProjectPartPk() == null || unitInProject.getProjectPartPk() != projectPartOID.getPk())
				continue;

			List<WorkstationQuery> unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
			if (unitWorkstations.contains(workstationQuery))
			{
				if (copyDefaultTeamIfNoProjectPartTeamIsSet)
					copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
				else
					copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), false);
			}
		}
	}

	public static void cascadeWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
			WorkstationQuery workstationQuery, Integer[] selectedUnitsForForm, Integer[] selectedUnitsForTeam)
			throws Exception
	{
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();
		// teams
		for (int i = 0; i < selectedUnitsForTeam.length; i++)
		{
			int unitPk = selectedUnitsForTeam[i];
			UnitObj unit = getUnitByPk(new UnitOID(unitPk));

			UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
			if (unitInProject == null)
				throw new AppException("Unit is not associated with the project, It cannot opened.");

			if (unitInProject.getProjectPartPk() == null)
				continue;

			List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());

			UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
					workstationQuery.getOID());
			if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
			{
				continue;
			} else if (unitWorkstations.contains(workstationQuery))
			{
				// now take care of the users, they can just be deleted and
				// replaced
				copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
			} else
			{
				// unit does not contain that workstation
				addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
				copyProjectUsersToUnit(projectQuery.getOID(), unit.getOID(), workstationQuery.getOID(), true);
			}
		}

		// forms
		TestProcDAO testProcDAO = new TestProcDAO();
		for (int i = 0; i < selectedUnitsForForm.length; i++)
		{
			int unitPk = selectedUnitsForForm[i];
			UnitObj unit = getUnitByPk(new UnitOID(unitPk));

			UnitInProjectObj unitInProject = uprDAO.getUnitInProject(unit.getOID(), projectQuery.getOID());
			if (unitInProject == null)
				throw new AppException("Unit is not associated with the project, It cannot opened.");

			if (unitInProject.getProjectPartPk() == null)
				continue;

			List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
			UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
					workstationQuery.getOID());
			if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
			{
				continue;
			}
			if (unitWorkstations.contains(workstationQuery))
			{
				// unit already contains this workstation
				// copy the forms which are not there,
				// if the form is getting upgraded, then we should upgrade the
				// form inside the testproc.
				List<ProjectFormQuery> pForms = ProjectTemplateManager.getTestProcsForProjectPart(projectQuery.getOID(),
						new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());
				List<UnitFormQuery> uForms = TestProcManager.getTestProcsForItem(context, unitPk, projectQuery.getOID(),
						workstationQuery.getOID(), false);
				for (Iterator iterator = pForms.iterator(); iterator.hasNext();)
				{
					ProjectFormQuery aPForm = (ProjectFormQuery) iterator.next();
					if (TestProcMatchMaker.testItemContainsForm(aPForm, uForms) != null)
					{
					} else
					{
						UnitFormQuery previousRevTestProc = TestProcMatchMaker.getMatchingLowerVersionOfForm(aPForm,
								uForms);
						if (previousRevTestProc != null)
						{
							// upgrade the testproc with the new form version
							TestProcObj testProc = new TestProcDAO().getTestProc(previousRevTestProc.getPk());
							testProc.setFormPk(aPForm.getFormPk());
							testProc.setAppliedByUserFk(context.getUser().getPk());

							testProcDAO.saveTestProc(context, testProc);
						} else
						{
							// need to create a new testproc on the unit
							TestProcObj testProc = new TestProcObj();
							testProc.setProjectPk(projectQuery.getPk());
							testProc.setUnitPk(unitPk);
							testProc.setWorkstationPk(workstationQuery.getPk());
							testProc.setFormPk(aPForm.getFormPk());
							testProc.setProjectTestProcPk(aPForm.getPk());
							testProc.setName(aPForm.getName());
							testProc.setAppliedByUserFk(aPForm.getAppliedByUserFk());

							testProcDAO.saveTestProc(context, testProc);
						}
					}
				}
				// now remove any forms from the unit that are not there in the
				// project, if they were added directly to the unit, leave them
				// there.

				// we have to re-fetch the unit forms list here, else,
				// the forms upgrades will get removed as they are not part of
				// the unit list fetched earlier
				uForms = TestProcManager.getTestProcsForItem(context, unitPk, projectQuery.getOID(),
						workstationQuery.getOID(), false);
				for (Iterator iterator = uForms.iterator(); iterator.hasNext();)
				{
					UnitFormQuery aForm = (UnitFormQuery) iterator.next();
					try
					{
						if (aForm.getProjectTestProcPk() == 0)
						{
							continue; // this means the form was added directly
										// to the unit, so dont remove it.
						}
						if (TestProcMatchMaker.projectContainsForm(aForm, pForms))
						{
							continue;
						}
						deleteTestProcFromUnit(context, aForm.getOID());
					}
					catch (FormApprovedException fx)
					{
					}
				}
			} else
			{
				// unit does not contain that workstation

				List<ProjectFormQuery> pForms = ProjectTemplateManager.getTestProcsForProjectPart(projectQuery.getOID(),
						new ProjectPartOID(unitInProject.getProjectPartPk(), null), workstationQuery.getPk());

				addWorkstationToUnit(context, projectQuery.getOID(), unit.getOID(), workstationQuery.getOID());
				copyProjectFormsToUnit(context, projectQuery.getOID(), pForms, unit.getOID(),
						workstationQuery.getOID());
			}
			if (uLocationQ != null && UnitLocation.STATUS_IN_PROGRESS.equals(uLocationQ.getStatus()))
			{
				ProjectManager.setUnitWorkstationStatus(context, unitPk, projectQuery.getOID(),
						workstationQuery.getOID(), UnitLocation.STATUS_IN_PROGRESS);
			}
		}
	}

	public static void deleteWorkstationToUnits(UserContext context, ProjectQuery projectQuery,
			WorkstationQuery workstationQuery, UnitObj[] selectedUnits) throws Exception
	{
		for (int i = 0; i < selectedUnits.length; i++)
		{
			boolean allTestsRemoved = true;

			UnitObj unit = selectedUnits[i];
			List unitWorkstations = getWorkstationsForUnit(unit.getOID(), projectQuery.getOID());
			UnitLocationQuery uLocationQ = getUnitWorkstationStatus(unit.getOID(), projectQuery.getOID(),
					workstationQuery.getOID());
			if (uLocationQ != null && UnitLocation.STATUS_COMPLETED.equals(uLocationQ.getStatus()))
			{
				continue;
			}
			if (unitWorkstations.contains(workstationQuery))
			{
				// unit contains this workstation
				// remove any forms from the unit that are not there in the
				// project
				List<UnitFormQuery> uForms = TestProcManager.getTestProcsForItem(context, unit.getPk(),
						projectQuery.getOID(), workstationQuery.getOID(), true);
				for (Iterator iterator = uForms.iterator(); iterator.hasNext();)
				{
					UnitFormQuery testProc = (UnitFormQuery) iterator.next();
					try
					{
						deleteTestProcFromUnit(context, testProc.getOID());
					}
					catch (FormApprovedException fx)
					{
						allTestsRemoved = false;
						// do not delete from this unit
						continue;
					}
				}

				// now take care of the users, they can just be deleted and
				// replaced
				if (allTestsRemoved)
				{
					removeAllUsersFromUnit(context, unit.getOID(), projectQuery.getOID(), workstationQuery.getOID());
				}
			}

			// remove the workstation from unit
			if (allTestsRemoved)
			{
				removeWorkstationFromUnit(context, unit.getOID(), projectQuery.getOID(), workstationQuery.getOID());
			}
		}
	}

	public static List<Project> getProjectsForWorkstation(UserContext context, WorkstationOID workstationOID)
			throws Exception
	{
		return PersistWrapper.readList(Project.class,
				"select * from TAB_PROJECT where pk in "
						+ "(select projectPk from TAB_PROJECT_WORKSTATIONS where workstationPk=?)",
				workstationOID.getPk());
	}

	public static float getUnitPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
			boolean includeChildren) throws Exception
	{
		return getWorkstationPercentCompleteInt(context, unitPk, projectOID, null, includeChildren);
	}

	public static float getWorkstationPercentComplete(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, boolean includeChildren) throws Exception
	{
		return getWorkstationPercentCompleteInt(context, unitPk, projectOID, workstationOID, includeChildren);
	}

	private static float getWorkstationPercentCompleteInt(UserContext context, int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID, boolean includeChildren) throws Exception
	{
		List<UnitFormQuery> fm = null;
		if (workstationOID == null)
		{
			fm = TestProcManager.getTestProcsForItem(context, unitPk, projectOID, includeChildren);
		} else
		{
			fm = TestProcManager.getTestProcsForItem(context, unitPk, projectOID, workstationOID, includeChildren);
		}
		int formCount = 0;
		float percenCompleteAccumulator = 0f;
		for (Iterator iterator = fm.iterator(); iterator.hasNext();)
		{
			UnitFormQuery formQuery = (UnitFormQuery) iterator.next();
			formCount++;
			final ResponseMasterNew formResponse = SurveyResponseDelegate
					.getLatestResponseMasterForTest(formQuery.getOID());

			if (formResponse != null)
			{
				percenCompleteAccumulator += formResponse.getPercentComplete();
			}
		}

		return (formCount > 0) ? (percenCompleteAccumulator * 100) / (float) (100 * formCount) : 0;
	}

	public static List<User> getProjectManagers(ProjectOID projectOID)
	{
		try
		{
			List<User> returnList = new ArrayList<User>();
			Project proj = getProject(projectOID.getPk());
			if (proj.getManagerPk() != 0)
			{
				User mgr = PersistWrapper.readByPrimaryKey(User.class, proj.getManagerPk());
				returnList.add(mgr);
			}

			List<User> existingMgrACList = AccountDelegate.getACLs(proj.getPk(), UserPerms.OBJECTTYPE_PROJECT,
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

	public static List<User> getAnnouncers(Account account) throws Exception
	{
		List<User> existingAnnouncerACList = AccountDelegate.getACLs(account.getPk(), UserPerms.OBJECTTYPE_ACCOUNT,
				UserPerms.ROLE_ANNOUNCER);
		return existingAnnouncerACList;
	}

	public static List<User> getDataClerks(ProjectQuery projectQuery) throws Exception
	{
		List<User> existingDataClerkACList = AccountDelegate.getACLs(projectQuery.getPk(), UserPerms.OBJECTTYPE_PROJECT,
				UserPerms.ROLE_DATACLERK);
		return existingDataClerkACList;
	}

	public static void moveWorkstationOrderUp(UserContext context, WorkstationOID workstationOID)
	{
		try
		{
			Workstation ws = PersistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
			if (ws != null)
			{
				Workstation previousOne = PersistWrapper.read(Workstation.class,
						"select * from TAB_WORKSTATION where orderNo < ? order by orderNo desc limit 0, 1",
						ws.getOrderNo());
				if (previousOne != null)
				{
					int orderNoTemp = previousOne.getOrderNo();
					previousOne.setOrderNo(ws.getOrderNo());
					ws.setOrderNo(orderNoTemp);
					PersistWrapper.update(previousOne);
					PersistWrapper.update(ws);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Exception changing workstation order", e);
			throw new AppException("Could not change workstation order, please try again later");
		}
	}

	public static void moveWorkstationOrderDown(UserContext context, WorkstationOID workstationOID)
	{
		try
		{
			Workstation ws = PersistWrapper.readByPrimaryKey(Workstation.class, workstationOID.getPk());
			if (ws != null)
			{
				Workstation nextOne = PersistWrapper.read(Workstation.class,
						"select * from TAB_WORKSTATION where orderNo > ? order by orderNo limit 0, 1", ws.getOrderNo());
				if (nextOne != null)
				{
					int orderNoTemp = nextOne.getOrderNo();
					nextOne.setOrderNo(ws.getOrderNo());
					ws.setOrderNo(orderNoTemp);
					PersistWrapper.update(nextOne);
					PersistWrapper.update(ws);
				}
			}
		}
		catch (Exception e)
		{
			logger.error("Exception changing workstation order", e);
			throw new AppException("Could not change workstation order, please try again later");
		}
	}

	public static Comment addComment(UserContext userContext, int objectPk, int objectType, String commentText,
			String commentContext) throws Exception
	{
		Comment comm = new Comment();
		comm.setCommentText(commentText);
		comm.setCreatedDate(new Date());
		comm.setObjectPk(objectPk);
		comm.setObjectType(objectType);
		comm.setCreatedBy(userContext.getUser().getPk());
		comm.setCommentContext(commentContext);
		int pk = PersistWrapper.createEntity(comm);
		return PersistWrapper.readByPrimaryKey(Comment.class, pk);
	}

	public static Comment updateComment(UserContext userContext, Comment comment) throws Exception
	{
		PersistWrapper.update(userContext,comment);
		return PersistWrapper.readByPrimaryKey(Comment.class, comment.getPk());
	}

	public static void deleteComment(UserContext userContext, int pk) throws Exception
	{
		Comment comm = PersistWrapper.readByPrimaryKey(Comment.class, pk);
		comm.setEstatus(EStatusEnum.Deleted.getValue());
		PersistWrapper.update(userContext, comm);
	}

	public static Comment getComment(int pk) throws Exception
	{
		return PersistWrapper.read(Comment.class, "select * from TAB_COMMENT where pk=? ", pk);
	}

	public static List<Comment> getComments(int objectPk, EntityType objectType) throws Exception
	{
		return PersistWrapper.readList(Comment.class,
				"select * from TAB_COMMENT where objectPk=? and objectType=? and estatus != 9 order by createdDate",
				objectPk, objectType.getValue());
	}

	public static List<Comment> getComments(Integer[] objectPks, EntityType objectType)
	{
		String pks = Arrays.deepToString(objectPks);
		pks = pks.replace('[', '(');
		pks = pks.replace(']', ')');
		return PersistWrapper.readList(Comment.class,
				"select * from TAB_COMMENT where objectPk in " + pks + " and objectType=? order by createdDate",
				objectType.getValue());
	}

	public static List<Comment> getComments(int objectPk, EntityTypeEnum objectType, String commentContext)
			throws Exception
	{
		return PersistWrapper.readList(Comment.class,
				"select * from TAB_COMMENT where objectPk=? and objectType=? and commentContext = ?  and estatus != 9 order by createdDate",
				objectPk, objectType.getValue(), commentContext);
	}

	public static Comment getLatestComment(int objectPk, EntityTypeEnum objectType) throws Exception
	{
		return PersistWrapper.read(Comment.class,
				"select * from TAB_COMMENT where objectPk=? and objectType=?  and estatus != 9 order by createdDate desc limit 0,1",
				objectPk, objectType.getValue());
	}

	public static HashMap<ProjectOID, Integer> getUnitCount(List<Integer> projectPks, boolean includeChildren)
	{
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
		List<Map<String, Object>> fetchedData = PersistWrapper.readListAsMap(sql.toString(), params.toArray());
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

	public static int getUnitCount(int projectPk, boolean includeChildren) throws Exception
	{
		if (includeChildren)
		{
			return PersistWrapper.read(Integer.class, "select count(*) from TAB_UNIT u "
					+ " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ? "
					+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo"
					+ " where upr.unitOriginType = ? and uprh.status != 'Removed' ", projectPk,
					UnitOriginType.Manufactured.name());
		} else
		{
			// look for top level units only
			return PersistWrapper.read(Integer.class, "select count(*) from TAB_UNIT u "
					+ " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ? "
					+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo "
					+ " where upr.unitOriginType = ?  and uprh.status != 'Removed' and uprh.parentPk is null",
					projectPk, UnitOriginType.Manufactured.name());
		}
	}

	public static UnitWorkstation getUnitWorkstationSetting(int unitPk, ProjectOID projectOID,
			WorkstationOID workstationOID)
	{
		return PersistWrapper.read(UnitWorkstation.class,
				"select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?", unitPk,
				projectOID.getPk(), workstationOID.getPk());
	}

	public static UnitWorkstation updateUnitWorkstationSetting(UnitWorkstation unitWorkstation) throws Exception
	{
		PersistWrapper.update(unitWorkstation);
		return PersistWrapper.readByPrimaryKey(UnitWorkstation.class, unitWorkstation.getPk());
	}

	public static ProjectSiteConfig getProjectSiteConfig(int pk)
	{
		return PersistWrapper.readByPrimaryKey(ProjectSiteConfig.class, pk);
	}

	public static ProjectSiteConfig getProjectSiteConfig(ProjectOID projectOID, SiteOID siteOID)
	{
		return PersistWrapper.read(ProjectSiteConfig.class,
				"select * from project_site_config where projectFk = ? and siteFk = ?", projectOID.getPk(),
				siteOID.getPk());
	}

	public static List<Site> getSitesForProject(ProjectOID projectOID)
	{
		try
		{
			return PersistWrapper.readList(Site.class,
					"select site.* from site inner join project_site_config psc on psc.siteFk = site.pk where psc.projectFk = ? and psc.estatus = 1 and site.estatus = 1",
					projectOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static List<ProjectSiteConfig> getProjectSiteConfigs(ProjectOID projectOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectSiteConfig.class,
					"select * from project_site_config where projectFk = ?", projectOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static ProjectStage getProjectStage(ProjectStageOID projectStageOID)
	{
		return PersistWrapper.readByPrimaryKey(ProjectStage.class, projectStageOID.getPk());
	}
	
	public static List<ProjectStage> getProjectStages(ProjectOID projectOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectStage.class,
					"select * from project_stage where projectFk = ? and estatus = 1", projectOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public static ProjectStage addProjectStage(ProjectStage projectStage) throws Exception
	{
		if(projectStage.getProjectFk() == 0)
			throw new AppException("Project not specified; Cannot add stage");
		Project project = getProject(projectStage.getProjectFk());
		if(project == null)
			throw new AppException("Invalid Project; Cannot add stage");
			
		projectStage.setEstatus(EStatusEnum.Active.getValue());
		int newPk = PersistWrapper.createEntity(projectStage);
		
		return PersistWrapper.readByPrimaryKey(ProjectStage.class, newPk);
	}

	public static ProjectStage updateProjectStage(ProjectStage projectStage) throws Exception
	{
		ProjectStage ps = PersistWrapper.readByPrimaryKey(ProjectStage.class, projectStage.getPk());
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

		PersistWrapper.update(ps);
		return PersistWrapper.readByPrimaryKey(ProjectStage.class, projectStage.getPk());
	}

	public static void removeProjectStage(int projectStagePk) throws Exception
	{
		ProjectStage ps = PersistWrapper.readByPrimaryKey(ProjectStage.class, projectStagePk);
		if(ps == null)
			throw new AppException("Inalid Project stage; remove failed");
		
		ps.setEstatus(EStatusEnum.Deleted.getValue());
		PersistWrapper.update(ps);
	}

	
// //////////////////////////// Project signatory functions	
	
	public static ProjectSignatorySetBean getProjectSignatorySet(ProjectSignatorySetOID sigSetOID)
	{
		ProjectSignatorySet set = PersistWrapper.readByPrimaryKey(ProjectSignatorySet.class, sigSetOID.getPk());
		if(set != null)
			return set.getBean();
		
		return null;
	}
	
	public static List<ProjectSignatorySetBean> getProjectSignatorySets(ProjectOID projectOID)
	{
		List<ProjectSignatorySet> list = PersistWrapper.readList(ProjectSignatorySet.class,
				"select * from project_signatory_set where projectFk = ? and estatus = 1", projectOID.getPk());
		
		List<ProjectSignatorySetBean> returnList = new ArrayList<>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			ProjectSignatorySet set = (ProjectSignatorySet) iterator.next();
			returnList.add(set.getBean());
		}
		return returnList;
	}
	
	public static ProjectSignatorySetBean addProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
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
		int pk = PersistWrapper.createEntity(set);
		
		for (Iterator iterator = sBean.getSigatoryItems().iterator(); iterator.hasNext();)
		{
			ProjectSignatoryItemBean aItemBean = (ProjectSignatoryItemBean) iterator.next();
			ProjectSignatoryItem item = new ProjectSignatoryItem();
			item.setOrderNo(aItemBean.getOrderNo());
			item.setProjectSignatorySetFk(pk);
			item.setRoleId(aItemBean.getRoleId());
			PersistWrapper.createEntity(item);
		}
		
		return getProjectSignatorySet(new ProjectSignatorySetOID(pk));
	}

	public static ProjectSignatorySetBean updateProjectSignatorySet(ProjectSignatorySetBean sBean) throws Exception
	{
		if(sBean.getPk() == 0)
		{
			logger.error("Pk cannot be 0 when updating");
			throw new AppException("Invalid request.");
		}
		ProjectSignatorySet set = PersistWrapper.readByPrimaryKey(ProjectSignatorySet.class, sBean.getPk());
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
			
		
		List<ProjectSignatoryItem> currentItems = PersistWrapper.readList(ProjectSignatoryItem.class, 
				"select * from project_signatory_item where projectSignatorySetFk = ? ", sBean.getPk());
		HashMap<Integer, ProjectSignatoryItem> currentItemsMap = new HashMap<>();
		for (Iterator iterator = currentItems.iterator(); iterator.hasNext();)
		{
			ProjectSignatoryItem aItem = (ProjectSignatoryItem) iterator.next();
			currentItemsMap.put(aItem.getPk(), aItem);
		}
		
		set.setDescription(sBean.getDescription());
		set.setName(sBean.getName());
		PersistWrapper.update(set);
		
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
				PersistWrapper.update(theItem);
			}
			else
			{
				ProjectSignatoryItem theItem = new ProjectSignatoryItem();
				theItem.setProjectSignatorySetFk(set.getPk());
				theItem.setOrderNo(aItemBean.getOrderNo());
				theItem.setRoleId(aItemBean.getRoleId());
				PersistWrapper.createEntity(theItem);
			}
		}
		
		//what ever is remaining in the current HashMap needs to be removed.
		for (Iterator iterator = currentItemsMap.values().iterator(); iterator.hasNext();)
		{
			ProjectSignatoryItem projectSignatoryItem = (ProjectSignatoryItem) iterator.next();
			PersistWrapper.deleteEntity(projectSignatoryItem);
		}
		
		return getProjectSignatorySet(new ProjectSignatorySetOID(23));
	}

	public static void removeProjectSignatorySet(ProjectSignatorySetOID setOID) throws Exception
	{
		ProjectSignatorySet set = PersistWrapper.readByPrimaryKey(ProjectSignatorySet.class, setOID.getPk());
		if(set == null)
		{
			logger.error("Invalid SignatorySet Pk: " + setOID.getPk());
			throw new AppException("Invalid request");
		}
		
		set.setEstatus(EStatusEnum.Deleted.getValue());
		PersistWrapper.update(set);
	}
	
// ///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * tries to add the new sites and removes the unselected ones. return 1 if
	 * some unselected sites could not be removed from the project. else returns
	 * 0
	 * 
	 * @param projectOID
	 * @param siteList
	 * @return
	 * @throws Exception
	 */
	public static int saveSitesForProject(ProjectOID projectOID, Collection<Site> siteList) throws Exception
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
				c.setProjectFk(projectOID.getPk());
				c.setSiteFk(aNewSite.getPk());
				c.setEstatus(EStatusEnum.Active.getValue());
				PersistWrapper.createEntity(c);
			}
		}

		boolean couldNotReportAll = false;
		// remaining are the ones that are to be removed.
		for (Iterator iterator = existingSites.iterator(); iterator.hasNext();)
		{
			Site site = (Site) iterator.next();
			// check if any workstation in this site is part of the project.
			int count = PersistWrapper.read(Integer.class,
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
			count = PersistWrapper.read(Integer.class,
					"select count(distinct(w.pk)) from TAB_WORKSTATION w, TAB_UNIT_WORKSTATIONS uw "
							+ "where w.pk = uw.workstationPk and uw.projectPk = ? and w.sitePk = ?",
					projectOID.getPk(), site.getPk());

			if (count > 0)
			{
				couldNotReportAll = true;
				continue;
			}

			// delete
			ProjectSiteConfig config = PersistWrapper.read(ProjectSiteConfig.class,
					"select * from project_site_config where projectFk = ? and siteFk = ?",
					new Object[] { projectOID.getPk(), site.getPk() });
			PersistWrapper.deleteEntity(config);
		}

		if (couldNotReportAll == true)
			return 1;
		else
			return 0;
	}

	public static List<Project> getProjectsForPart(PartOID partOID)
	{
		try
		{
			return PersistWrapper.readList(Project.class,
					"select * from TAB_PROJECT where pk in (select projectPk from project_part where partPk = ? and estatus = 1)",
					partOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static ProjectPart getProjectPart(ProjectPartOID projectpartOID)
	{
		return PersistWrapper.readByPrimaryKey(ProjectPart.class, projectpartOID.getPk());
	}

	/**
	 * return project parts where a team is defined for the project and
	 * workstation.
	 * 
	 * @param projectOID
	 * @param workstationOID
	 * @return
	 */
	public static List<ProjectPartQuery> getProjectPartsWithTeams(ProjectOID projectOID, WorkstationOID workstationOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectPartQuery.class, ProjectPartQuery.sql
					+ " and pp.pk in (select distinct projectPartPk from TAB_PROJECT_USERS where projectPk = ? and workstationPk = ? )",
					projectOID.getPk(), workstationOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static void copyWorkstationSettings(UserContext context, ProjectOID copyFromProjectOID,
			ProjectOID destinationProjectOID, boolean copySites, boolean copyProjectFunctionTeams, boolean copyParts,
			boolean copyOpenItemTeam, boolean copyProjectCoordinators, List<Object[]> workstationsToCopy)
			throws Exception
	{
		HashMap<ProjectPart, ProjectPart> projectPartMap = new HashMap();
		HashMap<Integer, ProjectPart> allProjectPartMap = new HashMap();

		try
		{
			if (copySites)
			{
				List<Site> sites = getSitesForProject(copyFromProjectOID);
				saveSitesForProject(destinationProjectOID, sites);
			}

			if (copyProjectFunctionTeams)
			{
				List<ProjectSiteConfig> config = getProjectSiteConfigs(destinationProjectOID);
				for (Iterator iterator = config.iterator(); iterator.hasNext();)
				{
					ProjectSiteConfig projectSiteConfig = (ProjectSiteConfig) iterator.next();
					new AuthorizationManager().removeAllAcls(projectSiteConfig.getOID());

					// get the corresponding projectSite config on the
					// sourceproject
					ProjectSiteConfig sourcePf = getProjectSiteConfig(copyFromProjectOID,
							new SiteOID(projectSiteConfig.getSiteFk(), null));
					if (sourcePf != null)
					{
						List<ACL> acls = new AuthorizationManager().getAcls(sourcePf.getOID());

						for (Iterator iterator2 = acls.iterator(); iterator2.hasNext();)
						{
							ACL sAcl = (ACL) iterator2.next();
							ACL dAcl = new ACL();
							dAcl.setCreatedDate(new Date());
							dAcl.setObjectPk(projectSiteConfig.getPk());
							dAcl.setObjectType(projectSiteConfig.getOID().getEntityType().getValue());
							dAcl.setRoleId(sAcl.getRoleId());
							dAcl.setUserPk(sAcl.getUserPk());
							PersistWrapper.createEntity(dAcl);

						}
					}
				}
			}

			if (copyParts)
			{
				// delete existing
				List<ProjectPart> projectPartsCurrent = PersistWrapper.readList(ProjectPart.class,
						"select * from project_part where projectPk = ? and project_part.estatus=1",
						destinationProjectOID.getPk());
				for (Iterator iterator = projectPartsCurrent.iterator(); iterator.hasNext();)
				{
					ProjectPart projectPart = (ProjectPart) iterator.next();
					PersistWrapper.deleteEntity(projectPart);
				}

				List<ProjectPart> projectParts = PersistWrapper.readList(ProjectPart.class,
						"select * from project_part where projectPk = ? and project_part.estatus=1",
						copyFromProjectOID.getPk());
				for (Iterator iterator = projectParts.iterator(); iterator.hasNext();)
				{
					ProjectPart sourcePart = (ProjectPart) iterator.next();

					ProjectPart newPart = new ProjectPart();
					newPart.setDescription(sourcePart.getDescription());
					newPart.setEstatus(EStatusEnum.Active.getValue());
					newPart.setLocationCode(sourcePart.getLocationCode());
					newPart.setName(sourcePart.getName());
					newPart.setOrderNo(sourcePart.getOrderNo());
					newPart.setPartNo(sourcePart.getPartNo());
					newPart.setPartPk(sourcePart.getPartPk());
					newPart.setPartTypePk(sourcePart.getPartTypePk());
					newPart.setProjectPk(destinationProjectOID.getPk());
					newPart.setWbs(sourcePart.getWbs());
					newPart.setCreatedDate(new Date());

					int pk = PersistWrapper.createEntity(newPart);
					newPart = PersistWrapper.readByPrimaryKey(ProjectPart.class, pk);

					projectPartMap.put(sourcePart, newPart);
					allProjectPartMap.put(pk, newPart);
					allProjectPartMap.put(sourcePart.getPk(), sourcePart);
				}

				// now the parent heirarchy has to be set for the newly created
				// parts.
				for (Iterator iterator = projectPartMap.keySet().iterator(); iterator.hasNext();)
				{
					ProjectPart sourcePart = (ProjectPart) iterator.next();
					ProjectPart destPart = projectPartMap.get(sourcePart);
					if (sourcePart.getParentPk() == null)
						continue;

					ProjectPart sourceParent = allProjectPartMap.get(sourcePart.getParentPk());
					ProjectPart destParent = projectPartMap.get(sourceParent);
					destPart.setParentPk(destParent.getPk());
					PersistWrapper.update(destPart);
				}
			}

			if (copyOpenItemTeam)
			{
				List<User> testList = ProjectManager.getUsersForProjectInRole(copyFromProjectOID.getPk(),
						DummyWorkstation.getOID(), User.ROLE_TESTER);
				List<User> verifyList = ProjectManager.getUsersForProjectInRole(copyFromProjectOID.getPk(),
						DummyWorkstation.getOID(), User.ROLE_VERIFY);
				List<User> readonlyList = ProjectManager.getUsersForProjectInRole(copyFromProjectOID.getPk(),
						DummyWorkstation.getOID(), User.ROLE_READONLY);

				removeAllUsersFromProject(context,
						destinationProjectOID.getPk(), DummyWorkstation.getOID());
				if (testList != null)
				{
					for (Iterator iterator = testList.iterator(); iterator.hasNext();)
					{
						User aUser = (User) iterator.next();
						addUserToProject(context,
								destinationProjectOID.getPk(), DummyWorkstation.getOID(), aUser.getPk(),
								User.ROLE_TESTER);
					}
				}
				if (verifyList != null)
				{
					for (Iterator iterator = verifyList.iterator(); iterator.hasNext();)
					{
						User aUser = (User) iterator.next();
						addUserToProject(context,
								destinationProjectOID.getPk(), DummyWorkstation.getOID(), aUser.getPk(),
								User.ROLE_VERIFY);
					}
				}
				if (readonlyList != null)
				{
					for (Iterator iterator = readonlyList.iterator(); iterator.hasNext();)
					{
						User aUser = (User) iterator.next();
						addReadonlyUserToProject(context,
								destinationProjectOID.getPk(), DummyWorkstation.getOID(), aUser.getPk());
					}
				}
			}

			if (copyProjectCoordinators)
			{
				List<User> existingMgrACList = AccountManager.getACLs(copyFromProjectOID.getPk(),
						UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_MANAGER);
				List<User> existingReadonlyACList = AccountManager.getACLs(copyFromProjectOID.getPk(),
						UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_READONLY);
				List<User> existingDataClerkACList = AccountManager.getACLs(copyFromProjectOID.getPk(),
						UserPerms.OBJECTTYPE_PROJECT, UserPerms.ROLE_DATACLERK);

				AccountManager.setUserPermissions(destinationProjectOID.getPk(),
						new List[] { existingMgrACList, existingReadonlyACList, existingDataClerkACList },
						new String[] { UserPerms.ROLE_MANAGER, UserPerms.ROLE_READONLY, UserPerms.ROLE_DATACLERK });
			}

			if (workstationsToCopy != null && workstationsToCopy.size() > 0)
			{
				removeAllWorkstationsFromProject(context, destinationProjectOID.getPk());

				for (Iterator iterator = workstationsToCopy.iterator(); iterator.hasNext();)
				{
					Object[] objects = (Object[]) iterator.next();
					WorkstationOID wsOID = (WorkstationOID) objects[0];
					Boolean copyForms = (Boolean) objects[1];
					Boolean copyTeam = (Boolean) objects[2];

					addWorkstationToProject(context, destinationProjectOID.getPk(), wsOID);

					if (copyForms != null && copyForms == true)
					{
						List<ProjectFormQuery> wsForms = getProjectFormsForProject(copyFromProjectOID.getPk(), wsOID);
						for (Iterator iterator2 = wsForms.iterator(); iterator2.hasNext();)
						{
							ProjectFormQuery projectFormQuery = (ProjectFormQuery) iterator2.next();

							ProjectPart sourcePart = PersistWrapper.readByPrimaryKey(ProjectPart.class,
									projectFormQuery.getProjectPartPk());
							ProjectPart destPart = projectPartMap.get(sourcePart);

							ProjectForm newPForm = new ProjectForm();
							newPForm.setAppliedByUserFk(context.getUser().getPk());
							newPForm.setFormPk(projectFormQuery.getFormPk());
							newPForm.setName(projectFormQuery.getName());
							newPForm.setProjectPartPk(destPart.getPk());
							newPForm.setProjectPk(destinationProjectOID.getPk());
							newPForm.setWorkstationPk(wsOID.getPk());
							PersistWrapper.createEntity(newPForm);
						}
					}

					if (copyTeam != null && copyTeam == true)
					{
						List<ProjectUserQuery> projectUsers = getProjectUserQueryList(copyFromProjectOID, wsOID);
						for (Iterator iterator2 = projectUsers.iterator(); iterator2.hasNext();)
						{
							ProjectUserQuery projectUserQuery = (ProjectUserQuery) iterator2.next();

							ProjectPart sourcePart = null;
							ProjectPart destPart = null;
							if (projectUserQuery.getProjectPartPk() != 0)
							{
								sourcePart = PersistWrapper.readByPrimaryKey(ProjectPart.class,
										projectUserQuery.getProjectPartPk());
								destPart = projectPartMap.get(sourcePart);
							}

							ProjectUser pUser = new ProjectUser();
							if (destPart != null)
								pUser.setProjectPartPk(destPart.getPk());
							pUser.setProjectPk(destinationProjectOID.getPk());
							pUser.setRole(projectUserQuery.getRole());
							pUser.setUserPk(projectUserQuery.getUserPk());
							pUser.setWorkstationPk(wsOID.getPk());
							PersistWrapper.createEntity(pUser);
						}
					}
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	public static void addUnitToProject(UserContext context, ProjectOID sourceProjectOID,
			ProjectOID destinationProjectOID, UnitBean rootUnitBean, List<UnitBean> unitBeanAndChildrenList,
			boolean addAsPlannedUnit) throws Exception
	{
		UnitInProjectDAO uprDAO = new UnitInProjectDAO();

		HashMap<UnitOID, UnitBean> unitBeanMap = new HashMap<>();
		for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
		{
			UnitBean unitBean = (UnitBean) iterator.next();
			unitBeanMap.put(unitBean.getOID(), unitBean);
		}

		// build the new UnitBeanHeirarchy from the unitBeanList
		List<UnitBean> rootBeanList = new ArrayList<>();
		for (Iterator iterator = unitBeanAndChildrenList.iterator(); iterator.hasNext();)
		{
			UnitBean unitBean = (UnitBean) iterator.next();

			UnitBean parentBean = null;
			if (unitBean.getParentPk() != null)
			{
				parentBean = unitBeanMap.get(new UnitOID(unitBean.getParentPk()));
			}
			if (parentBean == null)
			{
				rootBeanList.add(unitBean);
			} else
			{
				unitBean.setParent(parentBean);
				if (parentBean.getChildren() == null)
					parentBean.setChildren(new ArrayList<UnitBean>());
				parentBean.getChildren().add(unitBean);
			}
		}
		if (rootBeanList.size() != 1)
		{
			throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
		}
		if (rootUnitBean.getPk() != rootBeanList.get(0).getPk())
		{
			throw new AppException("Error in reading the Unit heirarchy, Please contact you administrator");
		}

		boolean copyFormConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destinationProjectOID, ProjectPropertyEnum.DisableProjectFormCopyToUnits.getId(), Boolean.class);
		boolean copyUserConfig = !(boolean) new CommonServicesDelegate().getEntityPropertyValue(destinationProjectOID, ProjectPropertyEnum.DisableProjectUsersCopyToUnit.getId(), Boolean.class);
		addUnitToProjectRec(context, uprDAO, sourceProjectOID, destinationProjectOID, rootUnitBean, unitBeanMap,
				addAsPlannedUnit, copyUserConfig, copyFormConfig);
	}

	private static void addUnitToProjectRec(UserContext context, UnitInProjectDAO uprDAO, ProjectOID sourceProjectOID,
			ProjectOID destinationProjectOID, UnitBean unitBeanToAdd, HashMap<UnitOID, UnitBean> unitBeanMap,
			boolean addAsPlannedUnit, boolean copyUserConfig, boolean copyFormConfig) throws Exception
	{
		// set the projectPartFk which is mapped by the user into the unitBean
		UnitBean mappedBean = unitBeanMap.get(unitBeanToAdd.getOID());
		if (mappedBean != null)
		{
			unitBeanToAdd.setProjectPartPk(mappedBean.getProjectPartPk());
			unitBeanToAdd.setUnitOriginType(mappedBean.getUnitOriginType());
		}

		// check if the unit is already associated with this project, in that
		// case. we need to change the
		// status and then move it under the parent as moved from the source
		// project.
		UnitInProjectObj uprObj = UnitManager.getUnitInProject(unitBeanToAdd.getOID(), destinationProjectOID);
		if (uprObj != null)
		{
			uprObj.setProjectPartPk(unitBeanToAdd.getProjectPartPk());
			uprObj.setUnitOriginType(unitBeanToAdd.getUnitOriginType().name());

			/*
			 * change the status of the unit inside the project to open or
			 * planned and then call the change parent method on that unit so
			 * that its current children are also properly moved along with this
			 * unit.
			 */
			if (addAsPlannedUnit == false || unitBeanToAdd.getUnitOriginType() == UnitOriginType.Procured)
				uprObj.setStatus(UnitInProject.STATUS_OPEN);
			else
				uprObj.setStatus(UnitInProject.STATUS_PLANNED);

			uprDAO.saveUnitInProject(context, uprObj, new Actions[] { Actions.addUnitToProject });

			UnitOID parentUnitOID = null;
			if (unitBeanToAdd.getParentPk() != null)
				parentUnitOID = new UnitOID(unitBeanToAdd.getParentPk());
			UnitManager.changeUnitParent(context, parentUnitOID, unitBeanToAdd.getOID(), destinationProjectOID);
		} else
		{
			ProjectPart destProjectPart = null;
			if (UnitOriginType.Manufactured == unitBeanToAdd.getUnitOriginType())
			{
				if (unitBeanToAdd.getProjectPartPk() == null || unitBeanToAdd.getProjectPartPk() == 0)
					throw new AppException("Invalid project part mapping specified");

				destProjectPart = (ProjectPart) new CommonServicesDelegate().getObjectByPk(ProjectPart.class,
						unitBeanToAdd.getProjectPartPk());
				if (destProjectPart == null || destProjectPart.getProjectPk() != destinationProjectOID.getPk()
						|| destProjectPart.getPartPk() != unitBeanToAdd.getPartPk())
					throw new AppException("Invalid project part mapping specified");
			}

			// add unit to the project, create the project_unit_ref entry
			UnitOID parentUnitOID = null;
			if (unitBeanToAdd.getParentPk() != null)
				parentUnitOID = new UnitOID(unitBeanToAdd.getParentPk());

			UnitInProjectObj pur = addUnitToProjectInt(context, uprDAO, unitBeanToAdd, destinationProjectOID,
					parentUnitOID, addAsPlannedUnit, copyUserConfig, copyFormConfig, false, null);

			// take care of the project level settings on the unit.
			if (destProjectPart != null && addAsPlannedUnit == false)
			{
				// copy project workstations to unitworkstations
				setWorkstationsAndTeamsOnUnitOpen(context, destinationProjectOID, unitBeanToAdd.getOID(), pur, copyUserConfig, copyFormConfig);
			}

		}

		// process the children units of this unit.
		List<UnitQuery> unitList = UnitManager.getChildrenUnits(sourceProjectOID, unitBeanToAdd.getOID());
		for (Iterator iterator = unitList.iterator(); iterator.hasNext();)
		{
			UnitQuery cUnit = (UnitQuery) iterator.next();

			UnitBean cUnitBean = cUnit.getUnitBean();
			cUnitBean.setProjectPk(destinationProjectOID.getPk());

			addUnitToProjectRec(context, uprDAO, sourceProjectOID, destinationProjectOID, cUnitBean, unitBeanMap,
					addAsPlannedUnit, copyUserConfig, copyFormConfig);
		}
	}

	public static void saveTestProcSchedule(UserContext context, TestProcOID testProcOID,
			ObjectScheduleRequestBean objectScheduleRequestBean) throws Exception
	{
		Date now = DateUtils.getNowDateForEffectiveDateFrom();

		UnitFormQuery testProc = TestProcManager.getTestProcQuery(testProcOID.getPk());
		if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProc.getForecastStartDate())
				&& Objects.equals(objectScheduleRequestBean.getEndForecast(), testProc.getForecastEndDate())
				&& Objects.equals(objectScheduleRequestBean.getHoursEstimate(), testProc.getForecastHours()))
		{
			return;
		} else
		{
			EntitySchedule es = PersistWrapper.read(EntitySchedule.class,
					"select * from entity_schedule where objectPk = ? and objectType = ? and now() between effectiveDateFrom and effectiveDateTo",
					objectScheduleRequestBean.getObjectOID().getPk(),
					objectScheduleRequestBean.getObjectOID().getEntityType().getValue());

			if (es == null)
			{
				es = new EntitySchedule();
				es.setObjectPk(objectScheduleRequestBean.getObjectOID().getPk());
				es.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
				es.setCreatedBy(context.getUser().getPk());
				es.setCreatedDate(new Date());
				es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
				es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
				es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
				es.setEffectiveDateFrom(now);
				es.setEffectiveDateTo(DateUtils.getMaxDate());
				PersistWrapper.createEntity(es);
			} else
			{
				Calendar calEx = new GregorianCalendar();
				calEx.setTime(es.getCreatedDate());
				calEx.setTimeZone(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));

				Calendar calNow = new GregorianCalendar();
				calNow.setTime(new Date());
				calNow.setTimeZone(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));

				// need to check if the last record is made on the same day.. if
				// so just update. else we create a history record.
				if (calEx.get(Calendar.DAY_OF_MONTH) == calNow.get(Calendar.DAY_OF_MONTH)
						&& calEx.get(Calendar.MONTH) == calNow.get(Calendar.MONTH)
						&& calEx.get(Calendar.YEAR) == calNow.get(Calendar.YEAR))
				{
					// so update
					es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
					es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
					es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
					PersistWrapper.update(es);
				} else
				{
					// invalidate old and create new
					es.setEffectiveDateTo(new Date(now.getTime() - 1000));
					PersistWrapper.update(es);

					EntitySchedule esNew = new EntitySchedule();
					esNew.setObjectPk(objectScheduleRequestBean.getObjectOID().getPk());
					esNew.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
					esNew.setCreatedBy(context.getUser().getPk());
					esNew.setCreatedDate(new Date());
					esNew.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
					esNew.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
					esNew.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
					esNew.setEffectiveDateFrom(now);
					esNew.setEffectiveDateTo(DateUtils.getMaxDate());
					PersistWrapper.createEntity(esNew);
				}
			}
		}

	}

	public static void saveTestProcSchedules(UserContext context, ProjectOID projectOID, UnitOID rootUnitOID,
			List<ObjectScheduleRequestBean> scheduleList) throws Exception
	{
		Date now = DateUtils.getNowDateForEffectiveDateFrom();

		UnitWorkstationListReportFilter req = new UnitWorkstationListReportFilter(projectOID);
		req.setProjectOID(projectOID);
		req.setUnitOID(rootUnitOID);
		req.setIncludeChildren(true);
		List<UnitWorkstationListReportResultRow> wsSummaryRows = new UnitWorkstationListReport(
				context, req).runReport();

		// create a hashmap of UnitWorkstationOID and the wsStatusSummary
		HashMap<UnitWorkstationOID, UnitWorkstationListReportResultRow> unitWorkstationMap = new HashMap<UnitWorkstationOID, UnitWorkstationListReportResultRow>();
		for (Iterator iterator = wsSummaryRows.iterator(); iterator.hasNext();)
		{
			UnitWorkstationListReportResultRow unitWorkstationRow = (UnitWorkstationListReportResultRow) iterator
					.next();
			unitWorkstationMap.put(new UnitWorkstationOID(unitWorkstationRow.getPk()), unitWorkstationRow);
		}

		// load all the testprocs and create a hashmap of the TestProcOID and
		// TestProc
		TestProcFilter filter = new TestProcFilter(projectOID);
		filter.setUnitOID(rootUnitOID);
		filter.setIncludeChildren(true);
		filter.setFetchWorkstationForecastAsTestForecast(false);
		List<UnitFormQuery> list = new TestProcListReport(context, filter).getTestProcs();
		HashMap<TestProcOID, UnitFormQuery> testProcMap = new HashMap<TestProcOID, UnitFormQuery>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();
			testProcMap.put(new TestProcOID(unitFormQuery.getPk()), unitFormQuery);
		}

		// load all the testprocs sections and create a hashmap of the
		// TestProcSectionOID and TestProcSection
		TestProcSectionListFilter secFilter = new TestProcSectionListFilter();
		secFilter.setProjectOID(projectOID);
		secFilter.setUnitOID(rootUnitOID);
		secFilter.setIncludeChildren(true);
		secFilter.setFetchWorkstationOrTestForecastAsSectionForecast(false);
		List<TestProcSectionListReportResultRow> secList = new TestProcSectionListReport(context, secFilter)
				.getTestProcs();
		HashMap<TestProcSectionOID, TestProcSectionListReportResultRow> testProcSectionMap = new HashMap<TestProcSectionOID, TestProcSectionListReportResultRow>();
		for (Iterator iterator = secList.iterator(); iterator.hasNext();)
		{
			TestProcSectionListReportResultRow aSection = (TestProcSectionListReportResultRow) iterator.next();
			testProcSectionMap.put(new TestProcSectionOID(aSection.getPk()), aSection);
		}

		// now loop through the scheduleList from ui. and find the matching
		// object from the hashmap and save as required.
		for (Iterator iterator = scheduleList.iterator(); iterator.hasNext();)
		{
			ObjectScheduleRequestBean objectScheduleRequestBean = (ObjectScheduleRequestBean) iterator.next();
			if (objectScheduleRequestBean.getObjectOID() instanceof UnitWorkstationOID)
			{
				UnitWorkstationListReportResultRow unitWorkstationRow = unitWorkstationMap
						.get(objectScheduleRequestBean.getObjectOID());
				if (Objects.equals(objectScheduleRequestBean.getStartForecast(),
						unitWorkstationRow.getForecastStartDate())
						&& Objects.equals(objectScheduleRequestBean.getEndForecast(),
								unitWorkstationRow.getForecastEndDate())
						&& Objects.equals(objectScheduleRequestBean.getHoursEstimate(),
								unitWorkstationRow.getForecastHours()))
				{
					continue;
				} else
				{
					new EntityScheduleDAO(TimeZone.getTimeZone(unitWorkstationRow.getWorkstationTimezoneId()))
							.save(context, objectScheduleRequestBean);
				}
			} else if (objectScheduleRequestBean.getObjectOID() instanceof TestProcOID)
			{
				UnitFormQuery testProc = testProcMap.get(objectScheduleRequestBean.getObjectOID());
				if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProc.getForecastStartDate())
						&& Objects.equals(objectScheduleRequestBean.getEndForecast(), testProc.getForecastEndDate())
						&& Objects.equals(objectScheduleRequestBean.getHoursEstimate(), testProc.getForecastHours()))
				{
					continue;
				} else
				{
					new EntityScheduleDAO(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId())).save(context,
							objectScheduleRequestBean);
				}
			} else if (objectScheduleRequestBean.getObjectOID() instanceof TestProcSectionOID)
			{
				TestProcSectionListReportResultRow testProcSection = testProcSectionMap
						.get(objectScheduleRequestBean.getObjectOID());
				if (Objects.equals(objectScheduleRequestBean.getStartForecast(), testProcSection.getForecastStartDate())
						&& Objects.equals(objectScheduleRequestBean.getEndForecast(),
								testProcSection.getForecastEndDate())
						&& Objects.equals(objectScheduleRequestBean.getHoursEstimate(),
								testProcSection.getForecastHours()))
				{
					continue;
				} else
				{
					new EntityScheduleDAO(TimeZone.getTimeZone(testProcSection.getWorkstationTimezoneId()))
							.save(context, objectScheduleRequestBean);
				}
			}
		}
	}

	public static void moveTestProcsToUnit(UserContext userContext, List<TestProcOID> testProcsToMove,
			UnitOID unitOIDToMoveTo, ProjectOID projectOIDToMoveTo) throws Exception
	{
		List<WorkstationOID> workstations = new ArrayList<WorkstationOID>();

		UnitInProjectObj unitInProject = new UnitInProjectDAO().getUnitInProject(unitOIDToMoveTo, projectOIDToMoveTo);
		if (unitInProject == null)
			throw new AppException("Invalid target unit, or target unit is not part of the project.");

		// load the forms in the target unit. and put it into a hashmap for easy
		// lookup
		TestProcFilter listFilter = new TestProcFilter(projectOIDToMoveTo);
		listFilter.setUnitOID(unitOIDToMoveTo);
		listFilter.setIncludeChildren(false);
		List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter).getTestProcs();
		HashMap<String, UnitFormQuery> targetUnitTestMap = new HashMap<String, UnitFormQuery>();
		for (Iterator iterator = testList.iterator(); iterator.hasNext();)
		{
			UnitFormQuery aTest = (UnitFormQuery) iterator.next();
			targetUnitTestMap.put(aTest.getName() + "-" + aTest.getFormMainPk() + "-" + aTest.getWorkstationPk(),
					aTest);
		}

		for (Iterator iterator = testProcsToMove.iterator(); iterator.hasNext();)
		{
			TestProcOID testProcOID = (TestProcOID) iterator.next();

			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());

			if (testProc.getUnitPk() == unitOIDToMoveTo.getPk())
				continue;

			// confirm that the target unit is also part of the same project
			if (testProc.getProjectPk() != projectOIDToMoveTo.getPk())
				throw new AppException("You can only move a form within the same project.");

			Survey form = SurveyMaster.getSurveyByPk(testProc.getFormPk());
			UnitFormQuery targetTest = targetUnitTestMap
					.get(testProc.getName() + "-" + form.getFormMainPk() + "-" + testProc.getWorkstationPk());
			if (targetTest != null)
			{
				String testName = targetTest.getName();
				if (testName == null)
					testName = "{None}";

				throw new AppException(String.format(
						"Test with form %s and name %s exists at workstation %s in the target unit. Move failed",
						targetTest.getFormName(), testName, targetTest.getWorkstationName()));
			}

			// we also need to make sure the workstation exists in the target
			// unit and make sure its status is set accordingly.
			if (!(workstations.contains(new WorkstationOID(testProc.getWorkstationPk()))))
			{
				// if the workstation is not there on the unit, add it
				UnitWorkstation existingOne = PersistWrapper.read(UnitWorkstation.class,
						"select * from TAB_UNIT_WORKSTATIONS where unitPk=? and projectPk = ? and workstationPk=?",
						unitOIDToMoveTo.getPk(), projectOIDToMoveTo.getPk(), testProc.getWorkstationPk());
				if (existingOne == null)
				{
					UnitWorkstation pForm = new UnitWorkstation();
					pForm.setEstatus(EStatusEnum.Active.getValue());
					pForm.setUpdatedBy(userContext.getUser().getPk());
					pForm.setProjectPk(projectOIDToMoveTo.getPk());
					pForm.setUnitPk(unitOIDToMoveTo.getPk());
					pForm.setWorkstationPk(testProc.getWorkstationPk());

					PersistWrapper.createEntity(pForm);
				}
				workstations.add(new WorkstationOID(testProc.getWorkstationPk()));
			}

			testProc.setUnitPk(unitOIDToMoveTo.getPk());
			dao.saveTestProc(userContext, testProc);
		}

		// now set the workstation status to Inprogress if the are inprogress
		// forms in the workstation after the move.
		// get the workstation testproc status count report
		TestProcStatusSummaryReportRequest request = new TestProcStatusSummaryReportRequest();
		request.setProjectOIDList(Arrays.asList(new ProjectOID[] { projectOIDToMoveTo }));
		request.setUnitOID(unitOIDToMoveTo);
		request.setIncludeChildrenUnits(false);
		request.setProjectOIDForUnitHeirarchy(projectOIDToMoveTo);
		request.setGroupingSet(Arrays.asList(new GroupingCol[] { GroupingCol.workstation }));
		TestProcStatusSummaryReport report = new TestProcStatusSummaryReport(
				userContext, request);
		TestProcStatusSummaryReportResult result = report.runReport();
		List<TestProcStatusSummaryReportResultRow> resultRows = result.getReportResult();
		HashMap<WorkstationOID, TestProcStatusSummaryReportResultRow> unitWsLookupMap = new HashMap<>();
		for (Iterator iterator = resultRows.iterator(); iterator.hasNext();)
		{
			TestProcStatusSummaryReportResultRow testProcStatusSummaryReportResultRow = (TestProcStatusSummaryReportResultRow) iterator
					.next();
			unitWsLookupMap.put(new WorkstationOID(testProcStatusSummaryReportResultRow.getWorkstationPk()),
					testProcStatusSummaryReportResultRow);
		}

		// now try to set the status of the workstations based on the moved
		// forms
		for (Iterator iterator = workstations.iterator(); iterator.hasNext();)
		{
			WorkstationOID workstationOID = (WorkstationOID) iterator.next();

			String currentStatus = UnitLocation.STATUS_WAITING;
			UnitLocation currentWsStatus = getUnitWorkstation(unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
					workstationOID);
			if (currentWsStatus != null)
				currentStatus = currentWsStatus.getStatus();

			TestProcStatusSummaryReportResultRow row = unitWsLookupMap.get(workstationOID);
			if (row != null)
			{
				if (row.getInProgressCount() > 0)
				{
					if (!(UnitLocation.STATUS_IN_PROGRESS.equals(currentStatus)))
					{
						setUnitWorkstationStatus(userContext, unitOIDToMoveTo.getPk(), projectOIDToMoveTo,
								workstationOID, UnitLocation.STATUS_IN_PROGRESS);
					}
				}
			}
		}
	}

	public static void renameTestForms(UserContext userContext, List<TestProcOID> selectedTestProcs,
			List<OID> referencesToAdd, String nameChangeText, String renameOption) throws Exception
	{
		// the best option is to get one testproc and get the root unit and
		// project and load all testprocs to check if we are duplicating the
		// name
		HashMap<String, UnitFormQuery> targetUnitTestMap = new HashMap<String, UnitFormQuery>();
		if (selectedTestProcs.size() > 0)
		{
			TestProcObj aTest = new TestProcDAO().getTestProc(selectedTestProcs.get(0).getPk());
			int rootUnitPk = UnitManager.getRootUnitPk(new UnitOID(aTest.getUnitPk()),
					new ProjectOID(aTest.getProjectPk()));

			// load the forms in the target unit. and put it into a hashmap for
			// easy lookup
			TestProcFilter listFilter = new TestProcFilter(new ProjectOID(aTest.getProjectPk()));
			listFilter.setUnitOID(new UnitOID(rootUnitPk));
			listFilter.setIncludeChildren(true);
			List<UnitFormQuery> testList = new TestProcListReport(userContext, listFilter).getTestProcs();
			for (Iterator iterator = testList.iterator(); iterator.hasNext();)
			{
				UnitFormQuery test = (UnitFormQuery) iterator.next();
				targetUnitTestMap.put(test.getUnitPk() + "-" + test.getWorkstationPk() + "-" + test.getFormMainPk()
						+ "-" + test.getName(), test);
			}

		}
		for (Iterator iterator = selectedTestProcs.iterator(); iterator.hasNext();)
		{
			TestProcOID testProcOID = (TestProcOID) iterator.next();

			TestProcDAO dao = new TestProcDAO();
			TestProcObj testProc = dao.getTestProc(testProcOID.getPk());

			Survey form = SurveyMaster.getSurveyByPk(testProc.getFormPk());

			if ("Append text to existing test name".equals(renameOption) || "Rename test".equals(renameOption))
			{
				if (nameChangeText == null)
					throw new AppException("Name cannot be blank");

				String newName = testProc.getName();
				if ("Append text to existing test name".equals(renameOption))
				{
					newName = ListStringUtil.showString(newName, "") + nameChangeText;
				} else
				{
					newName = nameChangeText;
				}

				UnitFormQuery targetTest = targetUnitTestMap.get(testProc.getUnitPk() + "-"
						+ testProc.getWorkstationPk() + "-" + form.getFormMainPk() + "-" + newName);
				if (targetTest != null)
				{
					String testName = targetTest.getName();
					if (testName == null)
						testName = "{None}";

					throw new AppException(String.format(
							"Test with form %s and name %s exists at workstation %s in the target unit. Rename failed",
							targetTest.getFormName(), testName, targetTest.getWorkstationName()));
				} else
				{
					testProc.setName(newName);
					dao.saveTestProc(userContext, testProc);
				}

			}

			if (referencesToAdd != null)
			{
				for (Iterator iterator2 = referencesToAdd.iterator(); iterator2.hasNext();)
				{
					OID oid = (OID) iterator2.next();
					CommonServiceManager.createEntityReference(userContext, testProcOID, oid);
				}
			}
		}

	}

	/**
	 * Get all project part specific forms setup for the project.
	 * For part specific forms, we store them in the same tab_project_forms table with workstationPk as null
	 * @param projectOID
	 * @param projectPartOID
	 * @return
	 */
	public static List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectFormQuery.class,
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
	public static List<ProjectFormQuery> getProjectPartAssignedForms(ProjectOID projectOID,
			ProjectPartOID projectPartOID)
	{
		try
		{
			return PersistWrapper.readList(ProjectFormQuery.class,
					ProjectFormQuery.sql + " and form.formType = 1 and pf.projectPk=? and projectPartPk = ? and pf.workstationPk is null  ",
					projectOID.getPk(), projectPartOID.getPk());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public static List<ProjectWorkstation> getProjectWorkstations(ProjectOID projectOID)
	{
		return PersistWrapper.readList(ProjectWorkstation.class,
				"select * from TAB_PROJECT_WORKSTATIONS where projectPk = ?", 
				projectOID.getPk());
		
	}
}