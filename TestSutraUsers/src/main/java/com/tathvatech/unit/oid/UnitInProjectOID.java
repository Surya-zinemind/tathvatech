package com.tathvatech.unit.oid;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class UnitInProjectOID extends OID {

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
		return (int) super.getPk();
	}

}
