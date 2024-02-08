/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;


import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.UserOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="user_preferences_data")
public class UserPreferencesDataBean extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private UserOID userOID;
	private OID anchorEntityOID;
	private String name;
	private String property;
	private String value;
	private Date createdDate;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public UserOID getUserOID() {
		return userOID;
	}
	public void setUserOID(UserOID userOID) {
		this.userOID = userOID;
	}
	public OID getAnchorEntityOID() {
		return anchorEntityOID;
	}
	public void setAnchorEntityOID(OID anchorEntityOID) {
		this.anchorEntityOID = anchorEntityOID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getProperty()
	{
		return property;
	}
	public void setProperty(String property)
	{
		this.property = property;
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
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		return (pk == ((UserPreferencesDataBean)obj).getPk());
	}
	@Override
	public int hashCode() 
	{
		return (int) pk;
	}
}
