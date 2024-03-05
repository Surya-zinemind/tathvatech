package com.tathvatech.workstation.oid;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class UnitInProjectOID extends OID{

	public UnitInProjectOID(int pk)
	{
		super(pk, EntityTypeEnum.UnitInProject, null);
	}
	
	public UnitInProjectOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.UnitInProject, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((UnitInProjectOID)obj).getPk() == getPk())
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
