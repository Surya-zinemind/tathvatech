package com.tathvatech.workstation.enums;

import java.util.List;


import com.tathvatech.user.OID.Action;

public enum WorkstationActionsEnum implements Action
{
	ViewAllIncidents("ViewAllIncidents", "View All Incidents"),
	VerifyIncident("VerifyIncident", "VerifyIncident");

	String id = null;
	String description = null;
	
	private WorkstationActionsEnum(String id, String description) 
	{
		this.id = id;
		this.description = description;
	}
	
	public List<Action> getAllowedActions()
	{
		return null;
	}
	
	
	public String getId()
	{
		return id;
	}
	public String getDescription()
	{
		return description;
	}
}
