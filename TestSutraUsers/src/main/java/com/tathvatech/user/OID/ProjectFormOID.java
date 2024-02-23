package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class ProjectFormOID extends OID{

	@JsonCreator
	public ProjectFormOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.ProjectForm, null);
	}

	public ProjectFormOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.ProjectForm, displayText);
	}
}
