package com.tathvatech.openitem.andon.oids;

import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.Role;

public class OpenItemOID extends OID {

	public OpenItemOID(int pk)
	{
		super(pk, EntityTypeEnum.OpenItem, null);
	}

	public OpenItemOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.OpenItem, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof OpenItemOID && ((OpenItemOID)obj).getPk() == getPk())
			return true;
		return false;
	}

	@Override
	public List<? extends Role> getSupportedRoles()
	{
		return null;
	}

	@Override
	public List<? extends Action> getSupportedActions()
	{
		return null;
	}
}
