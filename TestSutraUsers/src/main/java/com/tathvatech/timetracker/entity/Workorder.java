package com.tathvatech.timetracker.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.ReworkOrderOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;



@Entity
@Table(name = "workorder")
public class Workorder extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private String workorderNumber;
	private int entityPk;
	private int entityType;
	private int createdBy;
	private Date createdDate;
	private int estatus;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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


	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public ReworkOrderOID getOID()
	{
		return new ReworkOrderOID((int) pk, "TT-"+workorderNumber);
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
		return (int) pk;
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
