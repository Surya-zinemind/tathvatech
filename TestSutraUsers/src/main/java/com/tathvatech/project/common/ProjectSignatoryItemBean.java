/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.common;

import java.util.Date;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProjectSignatoryItemBean
{
	private int pk;
	private int projectSignatorySetFk;
	private String roleId;
	private int orderNo;
	private Date lastUpdated;
	
    public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getProjectSignatorySetFk()
	{
		return projectSignatorySetFk;
	}

	public void setProjectSignatorySetFk(int projectSignatorySetFk)
	{
		this.projectSignatorySetFk = projectSignatorySetFk;
	}

	public String getRoleId()
	{
		return roleId;
	}

	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}

	public int getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(int orderNo)
	{
		this.orderNo = orderNo;
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
	public int hashCode()
	{
		return pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return pk == ((ProjectSignatoryItemBean)obj).getPk();
	}
	
}
