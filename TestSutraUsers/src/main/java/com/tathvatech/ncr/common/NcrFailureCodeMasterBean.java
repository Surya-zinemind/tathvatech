package com.tathvatech.ncr.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.persist.annotations.NoTable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.ts.core.common.TSBeanBase;

@NoTable
@JsonIgnoreProperties(ignoreUnknown = true)
public class NcrFailureCodeMasterBean extends TSBeanBase implements Serializable
{
	private int pk;
	private String name;
	private String description;
	private int estatus;
	private Integer parentFk;
	private Date createdDate;
	private int createdBy;
	private Date lastUpdated;
	List<NcrFailureCodeMasterBean> children = new ArrayList<NcrFailureCodeMasterBean>();

	public List<NcrFailureCodeMasterBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<NcrFailureCodeMasterBean> children)
	{
		this.children = children;
	}

	public int getPk()
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

	@Override
	public String getDisplayText()
	{
		return pk + "-" + name;
	}

}
