package com.tathvatech.user.OID;

import com.tathvatech.common.EntityTypeEnum;


public class FormItemResponseOID extends OID{

	public FormItemResponseOID()
	{
		super();
	}

	public FormItemResponseOID(int pk)
	{
		super(pk, EntityTypeEnum.FormItemResponse, null);
	}

	public FormItemResponseOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.FormItemResponse, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof FormItemResponseOID && ((FormItemResponseOID)obj).getPk() == getPk())
			return true;
		return false;
	}
	
}
