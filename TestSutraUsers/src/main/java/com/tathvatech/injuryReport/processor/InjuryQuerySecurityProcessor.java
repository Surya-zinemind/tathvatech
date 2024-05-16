package com.tathvatech.injuryReport.processor;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.injuryReport.common.InjuryFilter;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;
import lombok.RequiredArgsConstructor;

import java.util.List;



@RequiredArgsConstructor
public class InjuryQuerySecurityProcessor {
     private final AuthorizationManager  authorizationManager;
    public void addAuthorizationFilterParams(UserContext context, InjuryFilter injuryFilter) throws Exception
    {
        if(User.USER_PRIMARY.equals(context.getUser().getUserType()))
        {
            return;
        }
        List<Integer> list = authorizationManager.getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSECoordinator);
        list.addAll(authorizationManager.getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSEDirector));
        if(list == null || list.size() == 0)
        {
            injuryFilter.setSitePks(new int[]{-1});
            return;
        }

        int[] sitePks = new int[list.size()];
        for (int i=0; i<list.size(); i++)
        {
            sitePks[i] = list.get(i);
        }
        injuryFilter.setSitePks(sitePks);
    }

}
