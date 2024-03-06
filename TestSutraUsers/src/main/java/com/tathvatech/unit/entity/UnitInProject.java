package com.tathvatech.unit.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.unit.oid.UnitInProjectOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="unit_project_ref")
public class UnitInProject extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int unitPk;
	private int projectPk;
	private String unitOriginType;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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
		return new UnitInProjectOID((int) pk);
	}

	public static String STATUS_PLANNED = "Planned";
    public static String STATUS_OPEN = "Open";
    public static final String STATUS_CLOSED = "Closed";
    public static final String STATUS_REMOVED = "Removed";
	
}
