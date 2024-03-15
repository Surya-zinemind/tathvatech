package com.tathvatech.project.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class ProjectStageOID extends OID{

	public ProjectStageOID()
	{
		super();
	}
	
	@JsonCreator
	public ProjectStageOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.ProjectStage, null);
	}

	public ProjectStageOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ProjectStage, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ProjectStageOID && ((ProjectStageOID)obj).getPk() == getPk())
			return true;
		return false;
	}
}
