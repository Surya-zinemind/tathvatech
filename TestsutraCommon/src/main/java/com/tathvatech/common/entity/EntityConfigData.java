/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="entity_config_data")
public class EntityConfigData
{
	@Id
	private long pk;
	private int objectPk;
	private int objectType;
	private String property;
	private Integer intParam1;
	private String stringParam1;
	private String value;
	private Date lastUpdated;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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
	public String getProperty()
	{
		return property;
	}
	public void setProperty(String property)
	{
		this.property = property;
	}
	public Integer getIntParam1() {
		return intParam1;
	}
	public void setIntParam1(Integer intParam1) {
		this.intParam1 = intParam1;
	}
	public String getStringParam1() {
		return stringParam1;
	}
	public void setStringParam1(String stringParam1) {
		this.stringParam1 = stringParam1;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public static final String UserDashboardTeamMemberIds = "UserDashboardTeamMemberIds";
}
