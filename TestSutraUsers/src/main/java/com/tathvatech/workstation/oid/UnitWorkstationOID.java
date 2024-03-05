package com.tathvatech.workstation.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class UnitWorkstationOID extends OID{

	@JsonCreator
	public UnitWorkstationOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.UnitWorkstation, null);
	}
	
	public UnitWorkstationOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.UnitWorkstation, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((UnitWorkstationOID)obj).getPk() == getPk())
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
