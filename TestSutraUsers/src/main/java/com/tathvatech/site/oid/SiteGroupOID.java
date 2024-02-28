package com.tathvatech.site.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class SiteGroupOID extends OID{

	@JsonCreator
	public SiteGroupOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.SiteGroup, null);
	}
	
	public SiteGroupOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.SiteGroup, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((SiteGroupOID)obj).getPk() == getPk())
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
