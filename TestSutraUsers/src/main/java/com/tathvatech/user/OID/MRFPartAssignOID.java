package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.EntityTypeEnum;

public class MRFPartAssignOID extends OID {

	@JsonCreator
	public MRFPartAssignOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.MRFPartAssign, null);
	}

	public MRFPartAssignOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.MRFPartAssign, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof MRFPartAssignOID && ((MRFPartAssignOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
