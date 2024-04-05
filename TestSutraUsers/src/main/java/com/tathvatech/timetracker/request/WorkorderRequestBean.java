package com.tathvatech.timetracker.request;


import com.tathvatech.user.OID.OID;

public class WorkorderRequestBean
{
	protected String extWorkorderNo; 
	protected OID entityOID;
	
	public WorkorderRequestBean(OID entityOID)
	{
		this.entityOID = entityOID;
	}
	public OID getEntityOID()
	{
		return entityOID;
	}
	public void setEntityOID(OID entityOID)
	{
		this.entityOID = entityOID;
	}
	public String getExtWorkorderNo()
	{
		return extWorkorderNo;
	}
	public void setExtWorkorderNo(String extWorkorderNo)
	{
		this.extWorkorderNo = extWorkorderNo;
	}
}
