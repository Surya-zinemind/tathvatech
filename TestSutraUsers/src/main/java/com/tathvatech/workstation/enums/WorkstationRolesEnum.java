package com.tathvatech.workstation.enums;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.common.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public enum WorkstationRolesEnum implements Role
{
	HSESupervisor("HSESupervisor", "Hazard and Safety Supervisor", "Hazard and Safety Supervisor");
	
	String id = null;
	String name = null;
	String description = null;
	
	private WorkstationRolesEnum(String id, String name, String description) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.Workstation);
	}
	
	public List<Action> getAllowedActions()
	{
		if(this == WorkstationRolesEnum.HSESupervisor)
			return Arrays.asList(new Action[]{WorkstationActionsEnum.VerifyIncident, WorkstationActionsEnum.ViewAllIncidents});
		else
			return new ArrayList();
	}
	
	
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getRoleType()
	{
		return null;
	}
	
	@Override
	public String[] getAllowedUserTypes()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getUsersWithEmailOnly()
	{
		return false;
	}
}
