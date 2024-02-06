package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PersonOID extends OID {

	public PersonOID()
	{
		super();
	}

	@JsonCreator
	public PersonOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Person, null);
	}

	public PersonOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Person, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof PersonOID && ((PersonOID)obj).getPk() == getPk())
			return true;
		return false;
	}

}
