package com.tathvatech.common.utils;

public class ListStringUtil 
{
	/**
	 * returns empty if null, returns whole string if displayLength = 0 else displays only the display length and adds ...
	 * @param inString
	 * @param
	 * @return
	 */
	public static String showString(String inString)
	{
		return showString(inString, 0);
	}

	public static String showString(Object obj, String nullRepresentation)
	{
		if(obj == null)
			return nullRepresentation;
		return showString(obj.toString(), 0);
	}

	public static String showString(Object obj)
	{
		if(obj == null)
			return "";
		return showString(obj.toString(), 0);
	}

	public static String showString(Object obj, String nullRepresentation, int displayLength)
	{
		if(obj == null)
			return nullRepresentation;
		return showString(obj.toString(), displayLength);
	}
	/**
	 * returns empty if null, returns whole string if displayLength = 0 else displays only the display length and adds ...
	 * @param inString
	 * @param displayLength
	 * @return
	 */
	public static String showString(String inString, int displayLength)
	{
		if(inString == null)
			return "";
		if(displayLength == 0)
			return inString;
		if(inString.trim().length() > displayLength)
		{
			return inString.substring(0, displayLength) + "...";
		}
		else
		{
			return inString;
		}
	}
	public static String showLastString(String inString)
	{
		return showLastString(inString, 0);
	}

	public static String showLastString(Object obj, String nullRepresentation)
	{
		if(obj == null)
			return nullRepresentation;
		return showLastString(obj.toString(), 0);
	}

	public static String showLastString(Object obj)
	{
		if(obj == null)
			return "";
		return showLastString(obj.toString(), 0);
	}
	public static String showLastString(String inString, int displayLength)
	{
		if(inString == null)
			return "";
		if(displayLength == 0)
			return inString;
		if(inString.trim().length() > displayLength)
		{  
			return "..." +inString.substring(inString.length() - displayLength);
		}
		else
		{
			return inString;
		}
	}
	public static String toHtmlFormat(String data) {
		// descVLayout.setWidth("200px");
		if (data == null)
		{
			return data;
		}
		String[] ary = data.split("\n");
		StringBuffer sb = new StringBuffer();
		for (String s:ary)
		{
			sb.append(s);
			sb.append("<br/>");
		}
		return sb.toString();
	}

	public static boolean equals(String string1, String string2) 
	{
		if(string1 == null && string2 == null)
			return true;
		else if(string1 == null && string2 != null)
			return false;
		else if(string1 != null && string2 == null)
			return false;
		else
			return string1.equals(string2);
	}
	
	public static String appendIgnoreNulls(String seperator, String... params)
	{
		if(params == null || params.length == 0)
			return "";
		
		StringBuilder sb = new StringBuilder();
		String sep = "";
		for (int i = 0; i < params.length; i++)
		{
			if(params[i] == null)
				continue;
			else
			{
				sb.append(sep).append(params[i]);
				sep = seperator;
			}
		}
		return sb.toString();
	}
	
}
