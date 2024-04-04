package com.tathvatech.ncr.oid;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

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
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof NcrItemOID && ((NcrItemOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
