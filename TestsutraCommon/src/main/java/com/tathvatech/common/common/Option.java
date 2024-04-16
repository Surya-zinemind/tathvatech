package com.tathvatech.common.common;

public class Option
{
	String text;
	String textDesc;
	int value;
	BomTypesEnum type; // currently used with AdvancedBom only

	public Option()
	{
		
	}
	
	public Option(String text, int value)
	{
		this.text = text;
		this.value = value;
	}

	public Option(String text, String textDesc)
	{
		this.text = text;
		this.textDesc = textDesc;
	}

	public Option(String text, String textDesc, int value)
	{
		this.text = text;
		this.textDesc = textDesc;
		this.value = value;
	}

	public Option(String text, String textDesc, int value, BomTypesEnum type)
	{
		this.text = text;
		this.textDesc = textDesc;
		this.value = value;
		this.type = type;
	}

	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getTextDesc()
	{
		return textDesc;
	}

	public void setTextDesc(String textDesc)
	{
		this.textDesc = textDesc;
	}

	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public BomTypesEnum getType()
	{
		return type;
	}

	public void setType(BomTypesEnum type)
	{
		this.type = type;
	}
}
