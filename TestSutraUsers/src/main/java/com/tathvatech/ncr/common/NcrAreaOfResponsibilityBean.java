package com.tathvatech.ncr.common;

import com.tathvatech.user.OID.TSBeanBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;




public class NcrAreaOfResponsibilityBean extends TSBeanBase implements Serializable
{
	private int pk;
	private String name;
	private String description;
	private int estatus;
	private Integer parentFk;
	private Date createdDate;
	private int createdBy;
	private Date lastUpdated;
	List<NcrAreaOfResponsibilityBean> children = new ArrayList<NcrAreaOfResponsibilityBean>();

	public long getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}

	public Integer getParentFk()
	{
		return parentFk;
	}

	public void setParentFk(Integer parentFk)
	{
		this.parentFk = parentFk;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public List<NcrAreaOfResponsibilityBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<NcrAreaOfResponsibilityBean> children)
	{
		this.children = children;
	}

	@Override
	public String getDisplayText()
	{
		return ((this.getParentFk() != null)? this.getParentFk() + "/" : "") + this.getPk() + "-" + this.getName();
	}

}
