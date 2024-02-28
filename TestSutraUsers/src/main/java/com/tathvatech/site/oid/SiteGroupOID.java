package com.tathvatech.site.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;

import com.tathvatech.user.OID.OID;

public class SiteGroupOID extends OID {

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
		return (int) super.getPk();
	}

}
