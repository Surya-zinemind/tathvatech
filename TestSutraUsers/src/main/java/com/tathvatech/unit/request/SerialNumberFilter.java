package com.tathvatech.unit.request;


import com.tathvatech.user.OID.PartOID;
import com.tathvatech.user.OID.ProjectOID;

public class SerialNumberFilter 
{
	PartOID partOID;
	String searchString; // this is the user entered search string. for serial number, its the serial number or unit name
	ProjectOID projectOID;
	String[] unitInProjectStatus;
	boolean showTopLevelOnly = false;
	
	public PartOID getPartOID()
	{
		return partOID;
	}
	public void setPartOID(PartOID partOID)
	{
		this.partOID = partOID;
	}
	public String getSearchString()
	{
		return searchString;
	}
	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}
	public ProjectOID getProjectOID()
	{
		return projectOID;
	}
	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}
	public String[] getUnitInProjectStatus()
	{
		return unitInProjectStatus;
	}
	public void setUnitInProjectStatus(String[] unitInProjectStatus)
	{
		this.unitInProjectStatus = unitInProjectStatus;
	}
	public boolean isShowTopLevelOnly()
	{
		return showTopLevelOnly;
	}
	public void setShowTopLevelOnly(boolean showTopLevelOnly)
	{
		this.showTopLevelOnly = showTopLevelOnly;
	}
	
}
