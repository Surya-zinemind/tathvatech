/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.entity;

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
@Table(name="TAB_UNIT_H")
public class UnitH extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int unitPk;
	private String serialNo;
	private String unitName;
	private String displayName;
	private String unitDescription;
	private int createdBy;
	private Date createdDate;
	private String status;
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private Integer actionPk;
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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		if (unitName != null){
			this.unitName = unitName.trim();
		}
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUnitDescription()
	{
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Date getEffectiveDateFrom()
	{
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom)
	{
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo()
	{
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo)
	{
		this.effectiveDateTo = effectiveDateTo;
	}

	public Integer getActionPk()
	{
		return actionPk;
	}

	public void setActionPk(Integer actionPk)
	{
		this.actionPk = actionPk;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
     * 
     */
    public UnitH()
    {
    }
}
