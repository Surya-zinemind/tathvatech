package com.tathvatech.site.oid;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class ProjectSiteConfigOID extends OID{

	public ProjectSiteConfigOID(long pk, String displayText)
	{
		super(pk, EntityTypeEnum.ProjectSiteConfig, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ProjectSiteConfigOID && ((ProjectSiteConfigOID)obj).getPk() == getPk())
			return true;
		return false;
	}
	
}
