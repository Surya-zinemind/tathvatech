package com.tathvatech.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOID extends OID{

	public UserOID()
	{
		super();
	}
	
	@JsonCreator
	public UserOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.User, null);
	}

	public UserOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.User, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		return (getPk() == ((UserOID)obj).getPk()); 
	}
	
	
}
