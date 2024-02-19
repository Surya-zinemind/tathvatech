package com.tathvatech.user.OID;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class ProjectPartOID extends OID {

	public ProjectPartOID(int pk)
	{
		super(pk, EntityTypeEnum.ProjectPart, null);
	}

	public ProjectPartOID(int pk, String name)
	{
		super(pk, EntityTypeEnum.ProjectPart, name);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		
		if(getPk() == ((ProjectPartOID)obj).getPk())
			return true;
		return false;
	}
	
}
