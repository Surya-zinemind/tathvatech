package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class WorkstationOID extends OID{

	public WorkstationOID()
	{
		super();
	}
	
	@JsonCreator
	public WorkstationOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Workstation, null);
	}

	public WorkstationOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Workstation, displayText);
	}

	@Override
	public int hashCode() {
		return getPk();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof WorkstationOID))
			return false;
		return(((WorkstationOID)obj).getPk() == getPk());
	}
	

}
