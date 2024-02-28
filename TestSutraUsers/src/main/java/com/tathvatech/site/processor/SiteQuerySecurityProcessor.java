package com.tathvatech.site.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.common.RoleRepository;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.CommonServicesDelegate;
import org.apache.commons.lang3.ArrayUtils;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.site.entity.SiteFilter;


public class SiteQuerySecurityProcessor {

	public void addAuthorizationFilterParams(UserContext context, SiteFilter siteFilter) throws Exception 
	{
		if(User.USER_PRIMARY.equals(context.getUser().getUserType()))
		{
			return;
		}
		if(siteFilter.getValidRoles() != null)
		{
			List<Integer> returnList = new ArrayList();
			
			// site Level roles
			List<Role> siteLevelRoles = new ArrayList();
			List<Role> projectSiteLevelRoles = new ArrayList<>();
			for (int i = 0; i < siteFilter.getValidRoles().length; i++)
			{
				Role aRole = siteFilter.getValidRoles()[i];
				EntityTypeEnum type = RoleRepository.getInstance().getAuthorizableEntityTypeForRoleId(aRole.getId());
				if(EntityTypeEnum.Site == type)
				{
					siteLevelRoles.add(aRole);
				}
				else if(EntityTypeEnum.ProjectSiteConfig == type)
				{
					projectSiteLevelRoles.add(aRole);
				}
			}
			if(siteLevelRoles.size() > 0)
			{
				List<Integer> sitePks = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, siteLevelRoles.toArray(new Role[siteLevelRoles.size()]));
				returnList.addAll(sitePks);
			}
			if(projectSiteLevelRoles.size() > 0)
			{
				List<Integer> projectSitePks = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.ProjectSiteConfig, projectSiteLevelRoles.toArray(new Role[projectSiteLevelRoles.size()]));
				for (Iterator iterator = projectSitePks.iterator(); iterator.hasNext();)
				{
					Integer aProjectSitePk = (Integer) iterator.next();
					ProjectSiteConfig  aPConfig = (ProjectSiteConfig)new CommonServicesDelegate().getObjectByPk(ProjectSiteConfig.class, aProjectSitePk);
					if(aPConfig != null)
					{
						if(!(returnList.contains(aPConfig.getSiteFk())))
							returnList.add(aPConfig.getSiteFk());
					}
				}
			}
			if(returnList.size() == 0)
				returnList.add(-1); // no access to any sites
			
			siteFilter.setSitePks(ArrayUtils.toPrimitive(returnList.toArray(new Integer[returnList.size()])));
			return;
		}
		else
		{
			// this is kept here for backward compatability.. All calls to getSites should use the validRoles to get the sites with those roles.
			List<Integer> list = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSECoordinator);
			list.addAll(new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSEDirector));
			list.addAll(new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.NCRUser));
			list.addAll(new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.SiteAdmin));
			if(list == null || list.size() == 0)
			{
				siteFilter.setSitePks(new int[]{((User)context.getUser()).getSitePk()});
				return;
			}
			else
			{
				int[] sitePks = new int[list.size()];
				for (int i=0; i<list.size(); i++) 
				{
					sitePks[i] = list.get(i);
				}
				siteFilter.setSitePks(sitePks);
				return;
			}
		}
	}

}
