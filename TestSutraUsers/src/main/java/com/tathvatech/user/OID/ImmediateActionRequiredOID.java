package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.EntityTypeEnum;

public class ImmediateActionRequiredOID extends OID {

	@JsonCreator
	public ImmediateActionRequiredOID(@JsonProperty("pk") int pk) {
		super(pk, EntityTypeEnum.HazardImmediateActionRequiredType, null);
	}

	public ImmediateActionRequiredOID(int pk, String displayText) {
		super(pk, EntityTypeEnum.HazardImmediateActionRequiredType, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ImmediateActionRequiredOID && ((ImmediateActionRequiredOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
