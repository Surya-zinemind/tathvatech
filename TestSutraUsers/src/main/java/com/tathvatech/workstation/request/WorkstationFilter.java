package com.tathvatech.workstation.request;

import java.util.List;

import com.tathvatech.ts.core.sites.SiteOID;

public class WorkstationFilter
{
	private String filterString;
	private List<SiteOID> siteOIDs;
	private UnitOID unitOID;
	private ProjectOID projectOID;

	public String getFilterString()
	{
		return filterString;
	}

	public void setFilterString(String filterString)
	{
		this.filterString = filterString;
	}

	public List<SiteOID> getSiteOIDs()
	{
		return siteOIDs;
	}

	public void setSiteOIDs(List<SiteOID> siteOIDs)
	{
		this.siteOIDs = siteOIDs;
	}

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
	}

	public ProjectOID getProjectOID()
	{
		return projectOID;
	}

	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}

}
