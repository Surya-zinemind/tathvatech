package com.tathvatech.user.common;


import com.tathvatech.user.OID.*;

public class SecurityContext
{
	private ProjectOID projectOID;
	private UnitOID unitOID;
	private WorkstationOID workstationOID;
	private ModeOID modeOID;
	private FormOID formOID;
	
	public SecurityContext()
	{
		
	}
	
	public SecurityContext(ProjectOID projectOID, UnitOID unitOID, WorkstationOID workstationOID, ModeOID modeOID, FormOID formOID)
	{
		super();
		this.projectOID = projectOID;
		this.unitOID = unitOID;
		this.workstationOID = workstationOID;
		this.modeOID = modeOID;
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
	public WorkstationOID getWorkstationOID()
	{
		return workstationOID;
	}
	public void setWorkstationOID(WorkstationOID workstationOID)
	{
		this.workstationOID = workstationOID;
	}
	public ModeOID getModeOID()
	{
		return modeOID;
	}
	public void setModeOID(ModeOID modeOID)
	{
		this.modeOID = modeOID;
	}
	public FormOID getFormOID()
	{
		return formOID;
	}
	public void setFormOID(FormOID formOID)
	{
		this.formOID = formOID;
	}
}
