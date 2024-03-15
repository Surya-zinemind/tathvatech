package com.tathvatech.project.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class ProjectSignatorySetOID extends OID{

	public ProjectSignatorySetOID()
	{
		super();
	}
	
	@JsonCreator
	public ProjectSignatorySetOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.ProjectSignatorySet, null);
	}

	public ProjectSignatorySetOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ProjectSignatorySet, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ProjectSignatorySetOID && ((ProjectSignatorySetOID)obj).getPk() == getPk())
			return true;
		return false;
	}
}
