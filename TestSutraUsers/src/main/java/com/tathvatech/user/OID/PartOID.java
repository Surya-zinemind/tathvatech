package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class PartOID extends OID {

	public PartOID()
	{
		super();
	}

	@JsonCreator
	public PartOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Part, null);
	}

	public PartOID(int pk, String name)
	{
		super(pk, EntityTypeEnum.Part, name);
	}

	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((PartOID)obj).getPk() == getPk())
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
