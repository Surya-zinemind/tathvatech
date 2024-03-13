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
@Table(name="TAB_USER_DEVICE_ASSIGN")
public class UserDeviceAssign extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int userPk;
	private int devicePk;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getUserPk()
	{
		return userPk;
	}
	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}
	public int getDevicePk()
	{
		return devicePk;
	}
	public void setDevicePk(int devicePk)
	{
		this.devicePk = devicePk;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
}
