package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReworkOrderOID extends OID {

	@JsonCreator
	public ReworkOrderOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.WorkOrder, null);
	}

	public ReworkOrderOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.WorkOrder, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ReworkOrderOID && ((ReworkOrderOID)obj).getPk() == getPk())
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		return "pk:" + pk + ", Workorder No:" + getDisplayText();
	}
}
