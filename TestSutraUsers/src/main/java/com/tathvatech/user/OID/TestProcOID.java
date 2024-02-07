package com.tathvatech.user.OID;


import com.tathvatech.common.enums.EntityTypeEnum;

public class TestProcOID extends OID {

	public TestProcOID(int pk)
	{
		super(pk, EntityTypeEnum.TestProc, null);
	}

	public TestProcOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.TestProc, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((TestProcOID)obj).getPk() == getPk())
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
