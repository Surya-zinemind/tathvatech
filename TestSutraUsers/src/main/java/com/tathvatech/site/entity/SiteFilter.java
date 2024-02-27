package com.tathvatech.site.entity;


import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.Role;

public class SiteFilter
{
	private String searchString; // this is usually a user entered string, match this to site name in case of Site
	private int[] sitePks;
	private String siteName;
	private ProjectOID projectOID;
	private Role[] validRoles;
	
	public String getSearchString() {
		return searchString;
	}
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
	public int[] getSitePks() {
		return sitePks;
	}
	public void setSitePks(int[] sitePks) {
		this.sitePks = sitePks;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public ProjectOID getProjectOID()
	{
		return projectOID;
	}
	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}
	public Role[] getValidRoles()
	{
		return validRoles;
	}
	public void setValidRoles(Role[] validRoles)
	{
		this.validRoles = validRoles;
	}
	
}
