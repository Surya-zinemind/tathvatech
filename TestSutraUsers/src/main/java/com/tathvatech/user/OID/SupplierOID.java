package com.tathvatech.user.OID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;


public class SupplierOID extends OID {

	public SupplierOID()
	{
		super();
	}
	
	@JsonCreator
	public SupplierOID(@JsonProperty("pk") int pk)
	{
		super(pk, EntityTypeEnum.Supplier, null);
	}
	
	public SupplierOID(int pk, String name)
	{
		super(pk, EntityTypeEnum.Supplier, name);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		if(obj instanceof SupplierOID)
			return (getPk() ==((SupplierOID)obj).getPk()) ;
		else
			return false;
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return (int) getPk();
	}
	
}
