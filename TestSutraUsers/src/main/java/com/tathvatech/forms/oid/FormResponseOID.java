package com.tathvatech.forms.oid;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class FormResponseOID extends OID{

	public FormResponseOID(int pk)
	{
		super(pk, EntityTypeEnum.Response, null);
	}
	
	public FormResponseOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Response, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((FormResponseOID)obj).getPk() == getPk())
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
