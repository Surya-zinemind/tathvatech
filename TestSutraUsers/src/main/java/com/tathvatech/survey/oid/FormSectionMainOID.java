package com.tathvatech.survey.oid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class FormSectionMainOID extends OID {

	public FormSectionMainOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.FormSectionMain, null);
	}

	public FormSectionMainOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.FormSectionMain, displayText);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null || !(obj instanceof FormSectionMainOID))
			return false;
		return (getPk() == ((FormSectionMainOID)obj).getPk());
	}

	@Override
	public int hashCode() {
		return super.getPk();
	}

}
