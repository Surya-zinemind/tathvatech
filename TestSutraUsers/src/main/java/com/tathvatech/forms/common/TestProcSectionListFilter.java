package com.tathvatech.forms.common;


import com.tathvatech.forms.enums.FormStatusEnum;
import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.DateRangeFilter;

public class TestProcSectionListFilter extends ReportFilter
{
	private ProjectOID projectOID;
	private UnitOID unitOID;
	private boolean includeChildren;
	private WorkstationOID workstationOID;
	private TestProcOID testProcOID;
	private FormOID formOID;
	private String formNameSearchString;
	private String testNameSearchString;
	private FormStatusEnum[] testStatus;
	private DateRangeFilter forecastTestStartDateFilter;
	private DateRangeFilter forecastTestCompletionDateFilter;
	private boolean fetchWorkstationOrTestForecastAsSectionForecast = true;
	
	public TestProcSectionListFilter()
	{
		this.projectOID = projectOID;
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
	public TestProcOID getTestProcOID()
	{
		return testProcOID;
	}

	public void setTestProcOID(TestProcOID testProcOID)
	{
		this.testProcOID = testProcOID;
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

	public boolean getFetchWorkstationOrTestForecastAsSectionForecast()
	{
		return fetchWorkstationOrTestForecastAsSectionForecast;
	}

	public void setFetchWorkstationOrTestForecastAsSectionForecast(boolean fetchWorkstationOrTestForecastAsSectionForecast)
	{
		this.fetchWorkstationOrTestForecastAsSectionForecast = fetchWorkstationOrTestForecastAsSectionForecast;
	}

}
