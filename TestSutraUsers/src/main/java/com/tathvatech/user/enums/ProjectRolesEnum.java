package com.tathvatech.user.enums;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.enums.Action;
import com.tathvatech.common.enums.Role;
import com.tathvatech.user.common.RoleRepository;
import com.tathvatech.user.entity.User;

public enum ProjectRolesEnum implements Role
{
	//TODO Change the name of this enum here and in the DB at the same time.. search in the code base and change the comment where ever it is mentioned 
	ChecksheetCoordinators("ChecksheetCoordinators", "Users having access to add checksheets to multiple units on project","Users having access to add checksheets to multiple units on project"),
	ReadOnlyUsers("ReadOnlyUsers", "Additional Users having access on project","Additional Users having access on project"),
	CreateUnitPermittedUser("CreateUnitPermittedUser", "Users who can create new units under the project","Users who can create new units under the project", User.USER_ENGINEER + "," + User.USER_MANAGER + "," + User.USER_TECHNICIAN),
	ChangeUnitParentUser("ChangeUnitParentUser", "Users who can change the heirarchy of units under this project", "Users who can change the heirarchy of units under this project", User.USER_ENGINEER + "," + User.USER_MANAGER + "," + User.USER_TECHNICIAN),
	AddChecksheetsToUnit("AddChecksheetsToUnit", "Allow user to add new checksheet to a unit","Allow user to add new checksheet to a unit"),
	OpenItemPowerUsers("OpenItemPowerUser", "Users who can mark open items external/internal etc. under this project", "Users who can mark open items external/internal etc. under this project", User.USER_ENGINEER + "," + User.USER_MANAGER + "," + User.USER_TECHNICIAN),
	ProjectSchedulerUsers("ProjectSchedulerUsers", "Users who can manage schedules for units under this project", "Users who can manage schedules for units under this project", User.USER_ENGINEER + "," + User.USER_MANAGER),
	MRFManagerUser("MRFManagerUser", "Users who can create MRFs on this project", "Users who can create MRFs on this project", User.USER_ENGINEER + "," + User.USER_MANAGER + ", " + User.USER_TECHNICIAN),

	UserGroup_Consultants("UserGroup_Consultants", "Consultants", "Consultants on project who should be signatories on checksheets", User.USER_ENGINEER+"", "UserGroups"),
	UserGroup_ClientRepresentative("UserGroup_ClientRepresentative", "Client representatives", "Client representative on project who should be signatories on checksheets", User.USER_ENGINEER+"", "UserGroups");
	
	String id = null;
	String name = null;
	String description = null;
	String allowedUserTypes = null; //coma separated string value with no spaces between the user types
	String roleType = null;
	
	private ProjectRolesEnum(String id, String name, String description) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.Project);
	}
	
	private ProjectRolesEnum(String id, String name, String description, String allowedUserTypes) 
	{
		this(id, name, description, allowedUserTypes, null);
	}
	
	private ProjectRolesEnum(String id, String name, String description, String allowedUserTypes, String roleType) 
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.allowedUserTypes = allowedUserTypes;
		this.roleType = roleType;
		
		RoleRepository.getInstance().registerSystemRole(this, EntityTypeEnum.Project);
	}
	
	public List<Action> getAllowedActions()
	{
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
		return roleType;
	}
	
	@Override
	public String[] getAllowedUserTypes()
	{
		if(allowedUserTypes == null || allowedUserTypes.trim().length() == 0)
			return new String[]{};
		else
		{
			String[] types = allowedUserTypes.split(",");
			return types;
		}
	}

	@Override
	public boolean getUsersWithEmailOnly()
	{
		return false;
	}
}
