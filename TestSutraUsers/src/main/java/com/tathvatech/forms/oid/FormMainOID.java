package com.tathvatech.forms.oid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;


public class FormMainOID extends OID {

	public FormMainOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.FormMain, null);
	}

	public FormMainOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.FormMain, displayText);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null || !(obj instanceof FormMainOID))
			return false;
		return (getPk() == ((FormMainOID)obj).getPk());
	}

	@Override
	public int hashCode() {
		return (int) super.getPk();
	}

}
