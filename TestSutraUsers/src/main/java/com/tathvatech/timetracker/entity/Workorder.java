package com.tathvatech.timetracker.entity;

import java.util.Date;

import com.tathvatech.ts.core.common.ReworkOrderOID;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

@Table(name = "workorder")
public class Workorder {
	private int pk;
	private String workorderNumber;
	private int entityPk;
	private int entityType;
	private int createdBy;
	private Date createdDate;
	private int estatus;
	private Date lastUpdated;

	@Column(autoGenerated = true)
	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getWorkorderNumber()
	{
		return workorderNumber;
	}

	public void setWorkorderNumber(String workorderNumber)
	{
		this.workorderNumber = workorderNumber;
	}

	public int getEntityPk()
	{
		return entityPk;
	}

	public void setEntityPk(int entityPk)
	{
		this.entityPk = entityPk;
	}

	public int getEntityType()
	{
		return entityType;
	}

	public void setEntityType(int entityType)
	{
		this.entityType = entityType;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(autoGenerated = true)
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public ReworkOrderOID getOID()
	{
		return new ReworkOrderOID(pk, "TT-"+workorderNumber);
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}

	@Override
	public int hashCode()
	{
		return pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj != null && pk == ((Workorder)obj).getPk())
			return true;
		else
		return false;
	}
	
}
