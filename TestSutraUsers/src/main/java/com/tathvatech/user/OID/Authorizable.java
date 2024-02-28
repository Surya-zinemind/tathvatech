package com.tathvatech.user.OID;

import java.util.List;


import com.tathvatech.common.enums.EntityType;
import com.tathvatech.user.enums.SiteActionsEnum;
import com.tathvatech.user.enums.SiteRolesEnum;

public interface Authorizable 
{
	public long getPk();
	public EntityType getEntityType();
	public String getDisplayText();
	public List<SiteRolesEnum> getSupportedRoles();
	public List<SiteActionsEnum> getSupportedActions();
}
