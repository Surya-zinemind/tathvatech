package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class InjuryAfterTreatmentOID extends OID {

	public InjuryAfterTreatmentOID()
	{
		super();
	}

	@JsonCreator
	public InjuryAfterTreatmentOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.InjuryAfterTreatment, null);
	}

	public InjuryAfterTreatmentOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.InjuryAfterTreatment, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof InjuryAfterTreatmentOID && ((InjuryAfterTreatmentOID)obj).getPk() == getPk())
			return true;
		return false;
	}
}
