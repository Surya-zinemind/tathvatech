/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

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
@Table(name="entity_schedule")
public class EntitySchedule extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int objectPk;
	private int objectType;
	private Date forecastStartDate;
	private Date forecastEndDate;
	private Float estimateHours;
	private String comment;
	private Integer createdBy;
	private Date createdDate;
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getObjectPk()
	{
		return objectPk;
	}

	public void setObjectPk(int objectPk)
	{
		this.objectPk = objectPk;
	}

	public int getObjectType()
	{
		return objectType;
	}

	public void setObjectType(int objectType)
	{
		this.objectType = objectType;
	}

	public Date getForecastStartDate()
	{
		return forecastStartDate;
	}

	public void setForecastStartDate(Date forecastStartDate)
	{
		this.forecastStartDate = forecastStartDate;
	}

	public Date getForecastEndDate()
	{
		return forecastEndDate;
	}

	public void setForecastEndDate(Date forecastEndDate)
	{
		this.forecastEndDate = forecastEndDate;
	}

	public Integer getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy)
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

	public Float getEstimateHours()
	{
		return estimateHours;
	}

	public void setEstimateHours(Float estimateHours)
	{
		this.estimateHours = estimateHours;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
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


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

}
