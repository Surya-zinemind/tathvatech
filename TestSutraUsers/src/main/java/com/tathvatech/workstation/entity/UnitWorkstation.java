/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.workstation.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

@Entity
@Table(name="TAB_UNIT_WORKSTATIONS")
public class UnitWorkstation extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int projectPk;
	private int unitPk;
	private int workstationPk;
	private Date completionForecast;
	private int estatus;
	private int updatedBy;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}

	public int getWorkstationPk() {
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) {
		this.workstationPk = workstationPk;
	}
	public Date getCompletionForecast() {
		return completionForecast;
	}
	public void setCompletionForecast(Date completionForecast) {
		this.completionForecast = completionForecast;
	}
	public int getEstatus()
	{
		return estatus;
	}
	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}
	public int getUpdatedBy()
	{
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy)
	{
		this.updatedBy = updatedBy;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public UnitWorkstationOID getOID() 
	{
		return new UnitWorkstationOID(pk, null);
	}
	
	public static String STATUS_OPEN = "Open";
    public static final String STATUS_CLOSED = "Closed";
    public static final String STATUS_DELETED = "Deleted";
}

