package com.tathvatech.user.OID;

import javax.swing.Action;
import java.util.Collection;

public interface Role 
{
	// Dont change the id value once it goes live and data is saved in to the db. as this is the key which gets saved in the db.
	// if at all you are changing the name, you need to update the database values too.
	
	public Collection<? extends Action> getAllowedActions();
	
	public String getRoleType();
	public String getId();
	public String getName();
	public String getDescription();
	public String[] getAllowedUserTypes();
	public boolean getUsersWithEmailOnly();
}
