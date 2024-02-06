package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class UnitOID extends OID{

	public UnitOID()
	{
		super();
	}
	
	@JsonCreator
	public UnitOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Unit, null);
	}
	
	public UnitOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Unit, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((UnitOID)obj).getPk() == getPk())
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
		return super.getPk();
	}

}
