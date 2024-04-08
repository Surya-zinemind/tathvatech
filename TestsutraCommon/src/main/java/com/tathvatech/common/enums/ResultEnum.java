package com.tathvatech.common.enums;

public enum ResultEnum {
	pass(1), fail(2), notApplicable(3);
	
	private int value;
	
	ResultEnum(int value)
	{
		this.value = value;
	}
	
	public int value()
	{
		return value;
	}
	
	public static ResultEnum fromValue(int val)
	{
		ResultEnum[] enums = ResultEnum.values();
		for (int i = 0;  i< enums.length; i++) 
		{
			if(val == enums[i].value())
			{
				return enums[i];
			}
		}
		return null;
	}
}
