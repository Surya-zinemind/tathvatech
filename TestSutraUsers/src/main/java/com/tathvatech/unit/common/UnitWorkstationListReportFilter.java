package com.tathvatech.unit.common;

import java.util.List;

import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.thirdi.surveyside.reportv2.ReportFilter;

public class UnitWorkstationListReportFilter extends ReportFilter
{
	private ProjectOID projectOID;
	private UnitOID unitOID;
	private boolean includeChildren;
	private WorkstationOID workstationOID;
	private List<String> workstationStatus;

	public UnitWorkstationListReportFilter(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}

	public UnitWorkstationListReportFilter(ProjectOID projectOID, UnitOID unitOID, boolean includeChildren,
			WorkstationOID workstationOID)
	{
		super();
		this.projectOID = projectOID;
		this.unitOID = unitOID;
		this.includeChildren = includeChildren;
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

	public boolean isIncludeChildren()
	{
		return includeChildren;
	}

	public void setIncludeChildren(boolean includeChildren)
	{
		this.includeChildren = includeChildren;
	}

	public WorkstationOID getWorkstationOID()
	{
		return workstationOID;
	}

	public void setWorkstationOID(WorkstationOID workstationOID)
	{
		this.workstationOID = workstationOID;
	}

	public List<String> getWorkstationStatus()
	{
		return workstationStatus;
	}

	public void setWorkstationStatus(List<String> workstationStatus)
	{
		this.workstationStatus = workstationStatus;
	}
	
}
