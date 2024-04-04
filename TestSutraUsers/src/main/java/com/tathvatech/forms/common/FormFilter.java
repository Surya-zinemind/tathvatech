package com.tathvatech.forms.common;

import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.UnitOID;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.thirdi.surveyside.reportv2.ReportFilter;


public class FormFilter extends ReportFilter
{
	Integer formPk;
	Integer formMainPk;
	String searchString; // this is the user entered search string. for form , its the name and description
	String[] status;
	boolean showAllFormRevisions = false;
	ProjectFormAssignmentFilter projectFormFilter;
	UnitFormAssignmentFilter unitFormFilter;
	
	public Integer getFormPk()
	{
		return formPk;
	}
	public void setFormPk(Integer formPk)
	{
		this.formPk = formPk;
	}
	public Integer getFormMainPk()
	{
		return formMainPk;
	}
	public void setFormMainPk(Integer formMainPk)
	{
		this.formMainPk = formMainPk;
	}
	public String getSearchString()
	{
		return searchString;
	}
	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}
	public String[] getStatus()
	{
		return status;
	}
	public void setStatus(String[] status)
	{
		this.status = status;
	}
	public boolean getShowAllFormRevisions()
	{
		return showAllFormRevisions;
	}
	public void setShowAllFormRevisions(boolean showAllFormRevisions)
	{
		this.showAllFormRevisions = showAllFormRevisions;
	}
	public ProjectFormAssignmentFilter getProjectFormFilter()
	{
		return projectFormFilter;
	}
	public void setProjectFormFilter(ProjectFormAssignmentFilter projectFormFilter)
	{
		this.projectFormFilter = projectFormFilter;
	}
	public UnitFormAssignmentFilter getUnitFormFilter()
	{
		return unitFormFilter;
	}
	public void setUnitFormFilter(UnitFormAssignmentFilter unitFormFilter)
	{
		this.unitFormFilter = unitFormFilter;
	}

	public class ProjectFormAssignmentFilter
	{
		ProjectOID projectOID;
		WorkstationOID workstationOID;
		Boolean notNullWorkstationsOnly;
		
		public ProjectFormAssignmentFilter(ProjectOID projectOID)
		{
			super();
			this.projectOID = projectOID;
		}
		public ProjectFormAssignmentFilter(ProjectOID projectOID, Boolean notNullWorkstationsOnly)
		{
			super();
			this.projectOID = projectOID;
			this.notNullWorkstationsOnly = notNullWorkstationsOnly;
		}
		public ProjectFormAssignmentFilter(ProjectOID projectOID, WorkstationOID workstationOID)
		{
			super();
			this.projectOID = projectOID;
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
		public WorkstationOID getWorkstationOID()
		{
			return workstationOID;
		}
		public void setWorkstationOID(WorkstationOID workstationOID)
		{
			this.workstationOID = workstationOID;
		}
		public Boolean getNotNullWorkstationsOnly()
		{
			return notNullWorkstationsOnly;
		}
		public void setNotNullWorkstationsOnly(Boolean notNullWorkstationsOnly)
		{
			this.notNullWorkstationsOnly = notNullWorkstationsOnly;
		}
	}
	
	public class UnitFormAssignmentFilter
	{
		UnitOID unitOID;
		ProjectOID projectOID;
		WorkstationOID workstationOID;
		public UnitFormAssignmentFilter(UnitOID unitOID, ProjectOID projectOID, WorkstationOID workstationOID)
		{
			super();
			this.unitOID = unitOID;
			this.projectOID = projectOID;
			this.workstationOID = workstationOID;
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
		public WorkstationOID getWorkstationOID()
		{
			return workstationOID;
		}
		public void setWorkstationOID(WorkstationOID workstationOID)
		{
			this.workstationOID = workstationOID;
		}
	}

}
