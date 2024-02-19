package com.tathvatech.common.enums;

import java.util.List;

public interface Role 
{
	// Dont change the id value once it goes live and data is saved in to the db. as this is the key which gets saved in the db.
	// if at all you are changing the name, you need to update the database values too.
	
	public List<Action> getAllowedActions();
	
	public String getRoleType();
	public String getId();
	public String getName();
	public String getDescription();
	public String[] getAllowedUserTypes();
	public boolean getUsersWithEmailOnly();
}
