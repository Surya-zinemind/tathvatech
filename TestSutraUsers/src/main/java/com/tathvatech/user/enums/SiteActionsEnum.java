package com.tathvatech.user.enums;

import java.util.List;


import com.tathvatech.user.OID.Action;

public enum SiteActionsEnum implements Action
{
	ResetUserPassword("ResetUserPassword", "Reset User Password"),
	
	ViewAllSuggestions("ViewAllSuggestions", "View All Suggestions from a site"),
	AcknowlegeSuggestion("AcknowlegeSuggestion", "Acknowlege Suggestion"),
	CloseSuggestion("CloseSuggestion", "Close Suggestion"),
	
	ViewAllIncidents("ViewAllIncidents", "View All Incidents"),
	AcknowlegeIncident("AcknowlegeIncident", "AcknowlegeIncident"),
	CloseIncident("CloseIncident", "Close Incident");

	String id = null;
	String description = null;
	
	private SiteActionsEnum(String id, String description) 
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
