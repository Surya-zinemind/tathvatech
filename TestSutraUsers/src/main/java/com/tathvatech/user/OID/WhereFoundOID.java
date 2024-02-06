package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.EntityTypeEnum;


public class WhereFoundOID extends OID{

	public WhereFoundOID()
	{
		super();
	}
	
	public WhereFoundOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.WhereFoundType, null);
	}
	
	public WhereFoundOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.WhereFoundType, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((WhereFoundOID)obj).getPk() == getPk())
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
