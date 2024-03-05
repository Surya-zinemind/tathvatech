package com.tathvatech.workstation.common;

import com.tathvatech.workstation.oid.UnitInProjectOID;

import java.util.Date;




public class UnitInProjectObj
{
	private int pk;
	private int unitPk;
	private int projectPk;
	private String unitOriginType;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;
	private Integer projectPartPk;
	private String status;
	private Integer parentPk;
	private Integer rootParentPk;
	private int level;
	private boolean hasChildren;
	private int orderNo;
	private String heiCode;
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}
	public int getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	public String getUnitOriginType()
	{
		return unitOriginType;
	}
	public void setUnitOriginType(String unitOriginType)
	{
		this.unitOriginType = unitOriginType;
	}
	public int getCreatedBy()
	{
		return createdBy;
	}
	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}
	public Date getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}
	public Integer getProjectPartPk()
	{
		return projectPartPk;
	}
	public void setProjectPartPk(Integer projectPartPk)
	{
		this.projectPartPk = projectPartPk;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Integer getParentPk()
	{
		return parentPk;
	}
	public void setParentPk(Integer parentPk)
	{
		this.parentPk = parentPk;
	}
	public Integer getRootParentPk()
	{
		return rootParentPk;
	}
	public void setRootParentPk(Integer rootParentPk)
	{
		this.rootParentPk = rootParentPk;
	}
	public int getLevel()
	{
		return level;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}
	public boolean getHasChildren()
	{
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren)
	{
		this.hasChildren = hasChildren;
	}
	public int getOrderNo()
	{
		return orderNo;
	}
	public void setOrderNo(int orderNo)
	{
		this.orderNo = orderNo;
	}
	public String getHeiCode()
	{
		return heiCode;
	}
	public void setHeiCode(String heiCode)
	{
		this.heiCode = heiCode;
	}
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public UnitInProjectOID getOID()
	{
		return new UnitInProjectOID(pk);
	}
}
