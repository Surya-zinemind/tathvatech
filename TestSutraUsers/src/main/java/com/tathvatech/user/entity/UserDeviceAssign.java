/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.util.Date;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Table(name="TAB_USER_DEVICE_ASSIGN")
public class UserDeviceAssign
{
	private int pk;
	private int userPk;
	private int devicePk;
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
	@Column(autoGenerated=true)
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
}
