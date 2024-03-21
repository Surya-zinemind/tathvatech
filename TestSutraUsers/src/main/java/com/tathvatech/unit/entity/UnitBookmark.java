package com.tathvatech.unit.entity;

import java.util.Date;

import com.tathvatech.ts.core.project.UnitInProjectOID;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

@Table(name="unit_bookmark")
public class UnitBookmark
{
	private int pk;
	private long userFk;
	private int unitFk;
	private int projectFk;
	private String mode;
	private Date createdDate; // if added again, the createdDate need to be updated. 
	private Date lastUpdated;

	@Column(autoGenerated=true)
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
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
	@Column(autoGenerated=true)
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

	public static enum BookmarkModeEnum {Auto, ByUser};
}
