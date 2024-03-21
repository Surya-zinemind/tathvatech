package com.tathvatech.forms.common;

import java.util.Date;


import com.tathvatech.user.OID.OID;

public class ObjectScheduleRequestBean
{
	private OID objectOID;
	private Date startForecast;
	private Date endForecast;
	private Float hoursEstimate;
	
	public ObjectScheduleRequestBean(OID objectOID, Date startForecast, Date endForecast, Float hoursEstimate)
	{
		super();
		this.objectOID = objectOID;
		this.startForecast = startForecast;
		this.endForecast = endForecast;
		this.hoursEstimate = hoursEstimate;
	}
	public OID getObjectOID()
	{
		return objectOID;
	}
	public void setObjectOID(OID objectOID)
	{
		this.objectOID = objectOID;
	}
	public Date getStartForecast()
	{
		return startForecast;
	}
	public void setStartForecast(Date startForecast)
	{
		this.startForecast = startForecast;
	}
	public Date getEndForecast()
	{
		return endForecast;
	}
	public void setEndForecast(Date endForecast)
	{
		this.endForecast = endForecast;
	}
	public Float getHoursEstimate()
	{
		return hoursEstimate;
	}
	public void setHoursEstimate(Float hoursEstimate)
	{
		this.hoursEstimate = hoursEstimate;
	}
	
}
