package com.tathvatech.ncr.oid;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class NcrItemOID extends OID {

	public NcrItemOID(int pk) 
	{
		super(pk, EntityTypeEnum.NCR, null);
	}

	public NcrItemOID(int pk, String displayText) 
	{
		super(pk, EntityTypeEnum.NCR, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof NcrItemOID && ((NcrItemOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
