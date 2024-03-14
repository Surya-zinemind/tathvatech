package com.tathvatech.openitem.andon.enums;

public enum OILTypesEnum {

//	TestForm(1, "Routine Test Form"),
	P8(2, "P8"),
	NCR(3, "NCR"),
	Mod(4, "Mod"),
	TT(5, "TT"),
	RT(6, "RT"),
	OSAT(7, "OSAT"),
	Andon(8, "Andon")
	;
	
	private int type;
	private String typeName;
	
	OILTypesEnum(int type, String typeName)
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
	
	public static OILTypesEnum byTypeName(String typeName)
	{
		for (OILTypesEnum e : OILTypesEnum.values()) 
		{
			if(e.typeName.equals(typeName))
			{
				return e;
			}
		}
		return null;
	}

	public static OILTypesEnum byType(int type)
	{
		for (OILTypesEnum e : OILTypesEnum.values()) 
		{
			if(e.type == type)
			{
				return e;
			}
		}
		return null;
	}
}
