package com.tathvatech.timetracker.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;


public class ShiftInstanceOID extends OID {

	@JsonCreator
	public ShiftInstanceOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.ShiftInstance, null);
	}
	
	public ShiftInstanceOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ShiftInstance, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((ShiftInstanceOID)obj).getPk() == getPk())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return (int) super.getPk();
	}

}
