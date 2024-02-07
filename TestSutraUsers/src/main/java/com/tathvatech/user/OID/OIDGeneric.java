package com.tathvatech.user.OID;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class OIDGeneric extends OID{

	public OIDGeneric(int pk, EntityType type)
	{
		super(pk, type, null);
	}
	
	public OIDGeneric(EntityTypeEnum type, int pk, String displayText)
	{
		super(pk, type, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((OIDGeneric)obj).getPk() == getPk())
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
