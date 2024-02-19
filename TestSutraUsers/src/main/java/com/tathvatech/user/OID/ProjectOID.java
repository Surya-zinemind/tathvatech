package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.Action;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.enums.Role;
import com.tathvatech.user.enums.ProjectRolesEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProjectOID extends OID{

	public ProjectOID()
	{
		super();
	}
	
	@JsonCreator
	public ProjectOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Project, null);
	}

	public ProjectOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Project, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ProjectOID && ((ProjectOID)obj).getPk() == getPk())
			return true;
		return false;
	}

	@Override
	public List<? extends Role> getSupportedRoles()
	{
		return Arrays.asList(ProjectRolesEnum.values());
	}

	@Override
	public List<? extends Action> getSupportedActions()
	{
		return new ArrayList();
	}
}
