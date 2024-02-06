package com.tathvatech.user.OID;

import java.util.List;

import com.tathvatech.common.EntityType;

public interface Authorizable 
{
	public int getPk();
	public EntityType getEntityType();
	public String getDisplayText();
	public List<? extends Role> getSupportedRoles();
	public List<? extends Action> getSupportedActions();
}
