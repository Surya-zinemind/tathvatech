package com.tathvatech.openitem.andon.oids;

import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;

import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.Role;

public class AndonOID extends OID {

	public AndonOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Andon, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof AndonOID && ((AndonOID)obj).getPk() == getPk())
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
