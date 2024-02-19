package com.tathvatech.user.OID;

import java.util.ArrayList;
import java.util.List;

import com.tathvatech.common.enums.EntityTypeEnum;

public class ModeOID extends OID{

	public ModeOID(int pk, String displayText)
	{
		super(pk, EntityTypeEnum.Mode, displayText);
	}

	@Override
	public int hashCode() {
		return (int) getPk();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof ModeOID && ((ModeOID)obj).getPk() == getPk())
			return true;
		return false;
	}

	@Override
	public List<? extends Role> getSupportedRoles() 
	{
		return new ArrayList();
	}

	@Override
	public List<? extends Action> getSupportedActions() 
	{
		return new ArrayList();
	}
}
