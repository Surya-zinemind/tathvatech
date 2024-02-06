package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class FormSectionOID extends OID {

	@JsonCreator
	public FormSectionOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.FormSection, null);
	}

	public FormSectionOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.FormSection, displayText);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null || !(obj instanceof FormSectionOID))
			return false;
		return (getPk() == ((FormSectionOID)obj).getPk());
	}

	@Override
	public int hashCode() {
		return super.getPk();
	}

}
