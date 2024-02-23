package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class MRFOID extends OID {

	public MRFOID()
	{
		super();
	}

	@JsonCreator
	public MRFOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.MRF, null);
	}

	public MRFOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.MRF, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof MRFOID && ((MRFOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
