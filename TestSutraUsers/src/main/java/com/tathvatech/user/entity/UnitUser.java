/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_UNIT_USERS")
public class UnitUser extends AbstractEntity implements Serializable {
	@Id
	private long pk;
	private int projectPk;
	private int unitPk;
	private int workstationPk;
	private int userPk;
	private String role;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getProjectPk()
	{
		return projectPk;
	}

	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}

	public int getUnitPk() 
	{
		return unitPk;
	}

	public void setUnitPk(int unitPk) 
	{
		this.unitPk = unitPk;
	}

	public int getWorkstationPk() 
	{
		return workstationPk;
	}

	public void setWorkstationPk(int workstationPk) 
	{
		this.workstationPk = workstationPk;
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


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public UnitUser()
    {
        super();
    }
}
