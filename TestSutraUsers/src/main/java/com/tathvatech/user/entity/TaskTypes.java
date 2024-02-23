package com.tathvatech.user.entity;

public enum TaskTypes {
	CreateUserRequest("New user request"),
	FormVersionUpgradeForProject("New Form Published")
	;
	
	public String getDisplayDescriptor()
	{
		return name;
	}
	
	public String getValue()
	{
		return toString();
	}
	
	private TaskTypes(String name) {
		this.name = name;
	}
	
	private String name;
}
