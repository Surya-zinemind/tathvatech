package com.tathvatech.ncr.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tathvatech.ts.core.common.TSBeanBase;
import com.tathvatech.ts.core.project.WhereFoundOID;

import net.sf.persist.annotations.NoTable;

@NoTable
@JsonTypeName("NcrWhereFoundBean")
public class NcrWhereFoundBean extends TSBeanBase implements Serializable
{
	private int pk;
	private String name;
	private String description;
	private int estatus;
	private Integer parentFk;
	private Date createdDate;
	private int createdBy;
	private Date lastUpdated;
	List<NcrWhereFoundBean> children = new ArrayList<NcrWhereFoundBean>();

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

	public List<NcrWhereFoundBean> getChildren()
	{
		return children;
	}

	public void setChildren(List<NcrWhereFoundBean> children)
	{
		this.children = children;
	}

	@Override
	public String getDisplayText()
	{
		return ((this.getParentFk() != null) ? this.getParentFk() + "/" : "") + this.getPk() + "-" + this.getName();
	}

	public WhereFoundOID getOID()
	{
		return new WhereFoundOID(pk, getDisplayText());
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof NcrWhereFoundBean))
			return false;

		return getPk() == ((NcrWhereFoundBean) obj).getPk();
	}

}
