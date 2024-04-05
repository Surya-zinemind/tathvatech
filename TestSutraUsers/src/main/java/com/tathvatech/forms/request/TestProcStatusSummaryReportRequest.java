package com.tathvatech.forms.request;

import com.tathvatech.forms.enums.FormStatusEnum;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.DateRangeFilter;

import java.util.ArrayList;
import java.util.List;



public class TestProcStatusSummaryReportRequest
{
	public static enum GroupingCol {
		project, unit, workstation, ProjectPart, form, forecastTestStartDate, forecastTestEndDate
	};

	private List<ProjectOID> projectOIDList;
	private String projectNameSearchString;
	private String unitNameSearchString;
	private UnitOID unitOID;
	private boolean includeChildrenUnits = false;
	private ProjectOID projectOIDForUnitHeirarchy;
	private List<UnitOID> unitOIDList;
	private List<WorkstationOID> workstationOIDList;
	private List<SiteOID> siteOIDList;
	private FormOID formOID;
	private FormStatusEnum[] responseStatus;
	private DateRangeFilter forecastStartDate;
	private DateRangeFilter forecastEndDate;
	private DateRangeFilter submittedDateFilter;
	private DateRangeFilter verifiedDateFilter;
	private DateRangeFilter approvedDateFilter;

	private List<GroupingCol> groupingSet = new ArrayList<>();

	public List<ProjectOID> getProjectOIDList()
	{
		return projectOIDList;
	}

	public String getProjectNameSearchString()
	{
		return projectNameSearchString;
	}

	public void setProjectNameSearchString(String projectNameSearchString)
	{
		this.projectNameSearchString = projectNameSearchString;
	}

	public String getUnitNameSearchString()
	{
		return unitNameSearchString;
	}

	public ProjectOID getProjectOIDForUnitHeirarchy()
	{
		return projectOIDForUnitHeirarchy;
	}

	public void setProjectOIDForUnitHeirarchy(ProjectOID projectOIDForUnitHeirarchy)
	{
		this.projectOIDForUnitHeirarchy = projectOIDForUnitHeirarchy;
	}

	public void setUnitNameSearchString(String unitNameSearchString)
	{
		this.unitNameSearchString = unitNameSearchString;
	}

	public void setProjectOIDList(List<ProjectOID> projectOIDList)
	{
		this.projectOIDList = projectOIDList;
	}

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
	}

	public boolean isIncludeChildrenUnits()
	{
		return includeChildrenUnits;
	}

	public void setIncludeChildrenUnits(boolean includeChildrenUnits)
	{
		this.includeChildrenUnits = includeChildrenUnits;
	}

	public List<WorkstationOID> getWorkstationOIDList()
	{
		return workstationOIDList;
	}

	public void setWorkstationOIDList(List<WorkstationOID> workstationOIDList)
	{
		this.workstationOIDList = workstationOIDList;
	}

	public List<SiteOID> getSiteOIDList()
	{
		return siteOIDList;
	}

	public void setSiteOIDList(List<SiteOID> siteOIDList)
	{
		this.siteOIDList = siteOIDList;
	}

	public List<UnitOID> getUnitOIDList()
	{
		return unitOIDList;
	}

	public void setUnitOIDList(List<UnitOID> unitOIDList)
	{
		this.unitOIDList = unitOIDList;
	}

	public FormOID getFormOID()
	{
		return formOID;
	}

	public void setFormOID(FormOID formOID)
	{
		this.formOID = formOID;
	}

	public FormStatusEnum[] getResponseStatus()
	{
		return responseStatus;
	}

	public void setResponseStatus(FormStatusEnum[] responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	public List<GroupingCol> getGroupingSet()
	{
		return groupingSet;
	}

	public void setGroupingSet(List<GroupingCol> groupingSet)
	{
		this.groupingSet = groupingSet;
	}

	public DateRangeFilter getForecastStartDate()
	{
		return forecastStartDate;
	}

	public void setForecastStartDate(DateRangeFilter forecastStartDate)
	{
		this.forecastStartDate = forecastStartDate;
	}

	public DateRangeFilter getForecastEndDate()
	{
		return forecastEndDate;
	}

	public void setForecastEndDate(DateRangeFilter forecastEndDate)
	{
		this.forecastEndDate = forecastEndDate;
	}

	public DateRangeFilter getSubmittedDateFilter()
	{
		return submittedDateFilter;
	}

	public void setSubmittedDateFilter(DateRangeFilter submittedDateFilter)
	{
		this.submittedDateFilter = submittedDateFilter;
	}

	public DateRangeFilter getVerifiedDateFilter()
	{
		return verifiedDateFilter;
	}

	public void setVerifiedDateFilter(DateRangeFilter verifiedDateFilter)
	{
		this.verifiedDateFilter = verifiedDateFilter;
	}

	public DateRangeFilter getApprovedDateFilter()
	{
		return approvedDateFilter;
	}

	public void setApprovedDateFilter(DateRangeFilter approvedDateFilter)
	{
		this.approvedDateFilter = approvedDateFilter;
	}

}
