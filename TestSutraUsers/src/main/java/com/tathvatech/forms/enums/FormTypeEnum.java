package com.tathvatech.forms.enums;

public enum FormTypeEnum {

	TestForm(1, "Routine Test Form")
	;
	
	private int type;
	private String typeName;
	
	FormTypeEnum(int type, String typeName)
	{
		this.type = type;
		this.typeName = typeName;
	}
	
	public int value()
	{
		return type;
	}
	
	public String getTypeName()
	{
		return typeName;
	}
	
	public static FormTypeEnum byTypeName(String typeName)
	{
		for (FormTypeEnum e : FormTypeEnum.values()) 
		{
			if(e.typeName.equals(typeName))
			{
				return e;
			}
		}
		return null;
	}

	public static FormTypeEnum byType(int type)
	{
		for (FormTypeEnum e : FormTypeEnum.values()) 
		{
			if(e.type == type)
			{
				return e;
			}
		}
		return null;
	}
}
