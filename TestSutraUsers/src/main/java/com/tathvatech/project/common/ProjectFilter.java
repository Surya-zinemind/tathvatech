package com.tathvatech.project.common;

import java.util.List;


import com.tathvatech.user.OID.Role;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UnitOID;

public class ProjectFilter
{
	private Boolean showChecksheetEnabled;
	private UnitOID unitOID;
	private Role[] validRoles;
	private String searchString;
	private List<SiteOID> siteOIDs;
	
	//if we set valid roles, project level assignments are ignored by default. set this to true to enable that.
	private boolean includeProjectAndUnitUserAssignments = false; 

	public Role[] getValidRoles()
	{
		return validRoles;
	}

	public void setValidRoles(Role[] validRoles)
	{
		this.validRoles = validRoles;
	}

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
	}

	public String getSearchString()
	{
		return searchString;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	public Boolean isShowChecksheetEnabled()
	{
		return showChecksheetEnabled;
	}

	public void setShowChecksheetEnabled(Boolean showChecksheetEnabled)
	{
		this.showChecksheetEnabled = showChecksheetEnabled;
	}

	public Boolean getShowChecksheetEnabled()
	{
		return showChecksheetEnabled;
	}

	public List<SiteOID> getSiteOIDs()
	{
		return siteOIDs;
	}

	public void setSiteOIDs(List<SiteOID> siteOIDs)
	{
		this.siteOIDs = siteOIDs;
	}

	public boolean getIncludeProjectAndUnitUserAssignments()
	{
		return includeProjectAndUnitUserAssignments;
	}

	public void setIncludeProjectAndUnitUserAssignments(boolean includeProjectAndUnitUserAssignments)
	{
		this.includeProjectAndUnitUserAssignments = includeProjectAndUnitUserAssignments;
	}
	
	

}
