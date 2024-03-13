package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class NearMissOID extends OID {

	@JsonCreator
	public NearMissOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.NearMiss, null);
	}

	public NearMissOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.NearMiss, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof NearMissOID && ((NearMissOID)obj).getPk() == getPk())
			return true;
		return false;
	}
}