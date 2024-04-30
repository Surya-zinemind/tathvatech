package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.*;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.enums.EntityTypeEnum;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("UserOID")
public class UserOID extends OID{

	public UserOID()
	{
		super();
	}
	
	@JsonCreator
	public UserOID(@JsonProperty("pk") long pk)
	{
		super(pk, EntityTypeEnum.User, null);
	}

	public UserOID(long pk, String displayText)
	{
		super(pk, EntityTypeEnum.User, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		return (getPk() == ((UserOID)obj).getPk()); 
	}
	
	
}
