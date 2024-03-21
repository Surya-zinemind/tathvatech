package com.tathvatech.forms.common;

import java.util.List;


import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.DateRangeFilter;


public class TestProcFilter extends ReportFilter
{
	private ProjectOID projectOIDForUnitHeirarchy; // the list is called on this project. so units in this project are considered.
	private UnitOID unitOID;
	private boolean includeChildren;
	private List<UnitOID> unitOIDList;
	private ProjectOID projectOID;
	private WorkstationOID workstationOID;
	private FormOID formOID;
	private String unitNameSearchString;
	private String formNameSearchString;
	private String testNameSearchString;
	private FormStatusEnum[] testStatus;
	private DateRangeFilter forecastTestStartDateFilter;
	private DateRangeFilter forecastTestCompletionDateFilter;
	private boolean fetchWorkstationForecastAsTestForecast = true;
	
	/**
	 * creates the filter with 
	 * @param
	 */
	public TestProcFilter()
	{
	}

	public TestProcFilter(ProjectOID projectOID)
	{
		this.projectOIDForUnitHeirarchy = projectOID;
		this.projectOID = projectOID;
		includeChildren = true;
	}
	
	public TestProcFilter(ProjectOID projectOIDForUnitHeirarchy, ProjectOID projectOID, UnitOID unitOID, boolean includeChildren,
			WorkstationOID workstationOID, FormOID formOID)
	{
		super();
		this.projectOIDForUnitHeirarchy = projectOIDForUnitHeirarchy;
		this.projectOID = projectOID;
		this.unitOID = unitOID;
		this.includeChildren = includeChildren;
		this.workstationOID = workstationOID;
		this.formOID = formOID;
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
	public ProjectOID getProjectOIDForUnitHeirarchy()
	{
		return projectOIDForUnitHeirarchy;
	}

	public void setProjectOIDForUnitHeirarchy(ProjectOID projectOIDForUnitHeirarchy)
	{
		this.projectOIDForUnitHeirarchy = projectOIDForUnitHeirarchy;
	}

	public boolean isIncludeChildren()
	{
		return includeChildren;
	}
	public void setIncludeChildren(boolean includeChildren)
	{
		this.includeChildren = includeChildren;
	}
	public List<UnitOID> getUnitOIDList()
	{
		return unitOIDList;
	}

	public void setUnitOIDList(List<UnitOID> unitOIDList)
	{
		this.unitOIDList = unitOIDList;
	}

	public String getUnitNameSearchString()
	{
		return unitNameSearchString;
	}

	public void setUnitNameSearchString(String unitNameSearchString)
	{
		this.unitNameSearchString = unitNameSearchString;
	}

	public WorkstationOID getWorkstationOID()
	{
		return workstationOID;
	}
	public void setWorkstationOID(WorkstationOID workstationOID)
	{
		this.workstationOID = workstationOID;
	}
	public FormOID getFormOID()
	{
		return formOID;
	}
	public void setFormOID(FormOID formOID)
	{
		this.formOID = formOID;
	}

	public String getFormNameSearchString()
	{
		return formNameSearchString;
	}

	public void setFormNameSearchString(String formNameSearchString)
	{
		this.formNameSearchString = formNameSearchString;
	}

	public String getTestNameSearchString()
	{
		return testNameSearchString;
	}

	public void setTestNameSearchString(String testNameSearchString)
	{
		this.testNameSearchString = testNameSearchString;
	}

	public FormStatusEnum[] getTestStatus()
	{
		return testStatus;
	}

	public void setTestStatus(FormStatusEnum[] testStatus)
	{
		this.testStatus = testStatus;
	}

	public DateRangeFilter getForecastTestStartDateFilter()
	{
		return forecastTestStartDateFilter;
	}

	public void setForecastTestStartDateFilter(DateRangeFilter forecastTestStartDateFilter)
	{
		this.forecastTestStartDateFilter = forecastTestStartDateFilter;
	}

	public DateRangeFilter getForecastTestCompletionDateFilter()
	{
		return forecastTestCompletionDateFilter;
	}

	public void setForecastTestCompletionDateFilter(DateRangeFilter forecastTestCompletionDateFilter)
	{
		this.forecastTestCompletionDateFilter = forecastTestCompletionDateFilter;
	}

	public boolean getFetchWorkstationForecastAsTestForecast()
	{
		return fetchWorkstationForecastAsTestForecast;
	}

	public void setFetchWorkstationForecastAsTestForecast(boolean fetchWorkstationForecastAsTestForecast)
	{
		this.fetchWorkstationForecastAsTestForecast = fetchWorkstationForecastAsTestForecast;
	}
}
