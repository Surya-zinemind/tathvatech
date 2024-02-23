package com.tathvatech.user.OID;

import java.util.List;


import com.tathvatech.common.enums.EntityType;

public interface Authorizable 
{
	public long getPk();
	public EntityType getEntityType();
	public String getDisplayText();
	public List<? extends Role> getSupportedRoles();
	public List<? extends Action> getSupportedActions();
}
