package com.tathvatech.survey.oid;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class SectionResponseOID extends OID {

	public SectionResponseOID(int pk)
	{
		super(pk, EntityTypeEnum.SectionResponse, null);
	}
	
	public SectionResponseOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.SectionResponse, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((SectionResponseOID)obj).getPk() == getPk())
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
