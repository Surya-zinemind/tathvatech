package com.tathvatech.common.enums;

public enum EStatusEnum implements EntityType{
	Active(1),
	PendingReview(8), // denotes that the item is created by still not approved by the authority
    Deleted(9),
	;

	EStatusEnum(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	public static EStatusEnum fromValue(int value)
	{
		EStatusEnum[] vs = EStatusEnum.values();
		for (int i = 0; i < vs.length; i++) 
		{
			if(vs[i].value == value)
			{
				return vs[i];
			}
		}
		return null;
	}
	
	private int value;
}
