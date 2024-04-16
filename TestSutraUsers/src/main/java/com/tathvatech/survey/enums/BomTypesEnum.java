package com.tathvatech.survey.enums;

public enum BomTypesEnum {

	TextBox("t", 1),
	NumericTextBox("{NText}", 2),
	CommentBox("c", 3),
	RadioGroup("o", 4),
	CheckboxGroup("mso", 5),
	Date("d", 6),
	DateMandatory("d-m", 7),
	DateTime("dt", 8),
	DateTimeMandatory("dt-m", 9),
	RadioGroupMandatory("o-m", 10),
	CheckboxGroupMandatory("mso-m", 11);
	
	
	BomTypesEnum(String token, int value)
	{
		this.token = token;
		this.value = value;
	}
	int value;
	String token;
	
	
	public int getValue()
	{
		return value;
	}
	
	public String getToken()
	{
		return token;
	}
	
	public static BomTypesEnum fromValueString(String valueString)
	{
		try
		{
			int value = Integer.parseInt(valueString);
			if(TextBox.value == value)
			{
				return TextBox;
			}
			else if(NumericTextBox.value == value)
			{
				return NumericTextBox;
			}
			else if(CommentBox.value == value)
			{
				return CommentBox;
			}
			else if(RadioGroup.value == value)
			{
				return RadioGroup;
			}
			else if(CheckboxGroup.value == value)
			{
				return CheckboxGroup;
			}
			else if(Date.value == value)
			{
				return Date;
			}
			else if(DateMandatory.value == value)
			{
				return DateMandatory;
			}
			else if(DateTime.value == value)
			{
				return DateTime;
			}
			else if(DateTimeMandatory.value == value)
			{
				return DateTimeMandatory;
			}
			else if(RadioGroupMandatory.value == value)
			{
				return RadioGroupMandatory;
			}
			else if(CheckboxGroupMandatory.value == value)
			{
				return CheckboxGroupMandatory;
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static BomTypesEnum fromToken(String token)
	{
		if(token == null)
			return null;
		
		if(token.trim().equalsIgnoreCase(TextBox.token))
		{
			return TextBox;
		}
		else if(token.trim().equalsIgnoreCase(NumericTextBox.token))
		{
			return NumericTextBox;
		}
		else if(token.trim().equalsIgnoreCase(CommentBox.token))
		{
			return CommentBox;
		}
		else if(token.trim().equalsIgnoreCase(Date.token))
		{
			return Date;
		}
		else if(token.trim().equalsIgnoreCase(DateMandatory.token))
		{
			return DateMandatory;
		}
		else if(token.trim().equalsIgnoreCase(DateTime.token))
		{
			return DateTime;
		}
		else if(token.trim().equalsIgnoreCase(DateTimeMandatory.token))
		{
			return DateTimeMandatory;
		}
		else
		{
			String[] rs = token.trim().split("\n");
			if(rs.length > 1 && RadioGroup.token.equals(rs[0].trim())) // there should be atleast one option so the >1
				return RadioGroup;
			else if(rs.length > 1 && CheckboxGroup.token.equals(rs[0].trim())) // there should be atleast one option so the >1
				return CheckboxGroup;
			else if(rs.length > 1 && RadioGroupMandatory.token.equals(rs[0].trim())) // there should be atleast one option so the >1
				return RadioGroupMandatory;
			else if(rs.length > 1 && CheckboxGroupMandatory.token.equals(rs[0].trim())) // there should be atleast one option so the >1
				return CheckboxGroupMandatory;
		}
		return null;
	}
}
