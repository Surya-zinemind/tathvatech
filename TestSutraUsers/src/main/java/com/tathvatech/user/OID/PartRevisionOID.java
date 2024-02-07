package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class PartRevisionOID extends OID {

	public PartRevisionOID()
	{
		super();
	}

	@JsonCreator
	public PartRevisionOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.PartRevision, null);
	}

	public PartRevisionOID(int pk, String name)
	{
		super(pk, EntityTypeEnum.PartRevision, name);
	}

	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((PartRevisionOID)obj).getPk() == getPk())
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
