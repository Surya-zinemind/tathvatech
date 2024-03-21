package com.tathvatech.forms.oid;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class TestProcSectionOID extends OID implements WorkItem{

	public TestProcSectionOID(int pk)
	{
		super(pk, EntityTypeEnum.TestProcSection, null);
	}

	public TestProcSectionOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.TestProcSection, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((TestProcSectionOID)obj).getPk() == getPk())
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
