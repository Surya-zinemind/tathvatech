package com.tathvatech.site.enums;

import java.util.List;

import com.tathvatech.user.OID.Action;

public enum ProjectSiteConfigActionsEnum implements Action
{
	ApproveNCRDisposition("ApproveNCRDisposition", "Approve NCR Disposition"),
	RejectNCRDisposition("RejectNCRDisposition", "Reject NCR Disposition");
	
	String id = null;
	String description = null;
	
	private ProjectSiteConfigActionsEnum(String id, String description) 
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
