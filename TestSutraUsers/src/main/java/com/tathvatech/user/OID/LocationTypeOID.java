package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class LocationTypeOID extends OID{

	public LocationTypeOID()
	{
		super();
	}
	
	public LocationTypeOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.LocationType, null);
	}
	
	public LocationTypeOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.LocationType, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((LocationTypeOID)obj).getPk() == getPk())
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
