/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.project.common.ProjectSignatoryItemBean;
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
@Table(name="project_signatory_item")
public class ProjectSignatoryItem extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int projectSignatorySetFk;
	private String roleId;
	private int orderNo;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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

	public ProjectSignatoryItem()
    {
        super();
    }

	public ProjectSignatoryItemBean getBean()
	{
		ProjectSignatoryItemBean bean = new ProjectSignatoryItemBean();
		bean.setProjectSignatorySetFk(projectSignatorySetFk);
		bean.setLastUpdated(lastUpdated);
		bean.setOrderNo(orderNo);
		bean.setPk((int) pk);
		bean.setRoleId(roleId);
		
		return bean;
	}

	@Override
	public int hashCode()
	{
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return pk == ((ProjectSignatoryItem)obj).getPk();
	}
	
}
