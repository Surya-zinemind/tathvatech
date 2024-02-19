package com.tathvatech.user.OID;

public interface Action 
{
	// Dont change the id value once it goes live and data is saved in to the db. as this is the key which gets saved in the db.
	// if at all you are changing the name, you need to update the database values too.
	
	String id = null;
	String description = null;
}
