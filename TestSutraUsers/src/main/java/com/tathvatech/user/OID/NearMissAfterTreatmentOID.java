package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.EntityTypeEnum;

public class NearMissAfterTreatmentOID extends OID {

	@JsonCreator
	public NearMissAfterTreatmentOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.NearMissAfterTreatment, null);
	}

	public NearMissAfterTreatmentOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.NearMissAfterTreatment, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof NearMissAfterTreatmentOID && ((NearMissAfterTreatmentOID)obj).getPk() == getPk())
			return true;
		return false;
	}
}
