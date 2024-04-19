package com.tathvatech.forms.request;


import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;

public class FormSummaryRequestBean
{
	private SiteOID siteOID;
	private WorkstationOID workstationOID;
	private ProjectOID projectOID;
	private UnitOID unitOID;

	public SiteOID getSiteOID()
	{
		return siteOID;
	}

	public void setSiteOID(SiteOID siteOID)
	{
		this.siteOID = siteOID;
	}

	public WorkstationOID getWorkstationOID()
	{
		return workstationOID;
	}

	public void setWorkstationOID(WorkstationOID workstationOID)
	{
		this.workstationOID = workstationOID;
	}

	public ProjectOID getProjectOID()
	{
		return projectOID;
	}

	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
	}

	@Override
	public String toString()
	{
		return "siteOID:" + ListStringUtil.showString(siteOID) + "; workstationOID:"
				+ ListStringUtil.showString(workstationOID) + "; projectOID:" + ListStringUtil.showString(projectOID)
				+ "; unitOID:" + ListStringUtil.showString(unitOID);
	}

}
