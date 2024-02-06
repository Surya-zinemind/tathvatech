package com.tathvatech.user.entity;

import java.util.Date;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

@Table(name="TAB_USER_PERMS")
public class UserPerms
{
	private int pk;
	private int objectPk;
	private int objectType;
	private int userPk;
	private String role;
	private Date lastUpdated;

	@Column(autoGenerated=true)
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getObjectPk()
	{
		return objectPk;
	}
	public void setObjectPk(int objectPk)
	{
		this.objectPk = objectPk;
	}
	public int getObjectType()
	{
		return objectType;
	}
	public void setObjectType(int objectType)
	{
		this.objectType = objectType;
	}
	public int getUserPk()
	{
		return userPk;
	}
	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}
	public String getRole()
	{
		return role;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	@Column(autoGenerated=true)
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public static String ROLE_MANAGER = "Manager";
	public static String ROLE_READONLY = "ReadOnly";
	public static String ROLE_DATACLERK = "Data Clerk";
	public static String ROLE_ANNOUNCER = "Announcer";
	
	
	public static int OBJECTTYPE_PROJECT = 1;
	public static int OBJECTTYPE_UNIT = 2;
	public static int OBJECTTYPE_FORM = 3;
	public static int OBJECTTYPE_MOD = 4;
	public static int OBJECTTYPE_ACCOUNT = 5;
}

