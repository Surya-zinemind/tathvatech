package com.tathvatech.injuryReport.processor;

import java.util.List;

import com.tathvatech.testsutra.injury.common.InjuryFilter;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.authorization.AuthorizationDelegate;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.sites.SiteRolesEnum;

public class InjuryQuerySecurityProcessor {

    public void addAuthorizationFilterParams(UserContext context, InjuryFilter injuryFilter) throws Exception
    {
        if(User.USER_PRIMARY.equals(context.getUser().getUserType()))
        {
            return;
        }
        List<Integer> list = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSECoordinator);
        list.addAll(new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site, SiteRolesEnum.HSEDirector));
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
