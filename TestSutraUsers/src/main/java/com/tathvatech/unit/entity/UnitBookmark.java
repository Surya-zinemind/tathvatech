package com.tathvatech.unit.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.unit.oid.UnitInProjectOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;



@Entity
@Table(name="unit_bookmark")
public class UnitBookmark extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private long userFk;
	private int unitFk;
	private int projectFk;
	private String mode;
	private Date createdDate; // if added again, the createdDate need to be updated. 
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public long getUserFk() {
		return userFk;
	}
	public void setUserFk(long userFk) {
		this.userFk = userFk;
	}
	public int getUnitFk() {
		return unitFk;
	}
	public void setUnitFk(int unitFk) {
		this.unitFk = unitFk;
	}
	public int getProjectFk() {
		return projectFk;
	}
	public void setProjectFk(int projectFk) {
		this.projectFk = projectFk;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
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

	public static enum BookmarkModeEnum {Auto, ByUser};
}
