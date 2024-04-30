package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tathvatech.common.enums.EntityTypeEnum;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("SiteOID")
public class SiteOID extends OID{

	public SiteOID()
	{
		super();
	}
	
	@JsonCreator
	public SiteOID(@JsonProperty("pk") long pk)
	{
		super(pk, EntityTypeEnum.Site, null);
	}
	
	public SiteOID(long pk, String displayText)
	{
		super(pk, EntityTypeEnum.Site, displayText);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((SiteOID)obj).getPk() == getPk())
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
