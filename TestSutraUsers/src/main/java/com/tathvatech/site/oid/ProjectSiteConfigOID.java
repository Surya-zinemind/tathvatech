package com.tathvatech.site.oid;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class ProjectSiteConfigOID extends OID {

	public ProjectSiteConfigOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ProjectSiteConfig, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ProjectSiteConfigOID && ((ProjectSiteConfigOID)obj).getPk() == getPk())
			return true;
		return false;
	}
	
}
