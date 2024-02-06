package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class FormOID extends OID {

	@JsonCreator
	public FormOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Form, null);
	}

	public FormOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Form, displayText);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null || !(obj instanceof FormOID))
			return false;
		return (getPk() == ((FormOID)obj).getPk());
	}

	@Override
	public int hashCode() {
		return super.getPk();
	}

}
