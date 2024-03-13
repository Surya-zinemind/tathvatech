package com.tathvatech.common.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DateFormats 
{
	String id;
	String format;
	String shortFormat;
	String displayName;
	String description;
	
	private static List dateFormatList = new ArrayList(2);
	
	private DateFormats(String id, String format, String shortFormat, String displayName, String description)
	{
		this.displayName = displayName;
		this.id = id;
		this.format = format;
		this.shortFormat = shortFormat;
		this.description = description;

		dateFormatList.add(this);
	}
	

	public String getDisplayName() 
	{
		return displayName;
	}

	public String getId() 
	{
		return id;
	}
	
	public String getFormat()
	{
		return format;
	}
	
	public String getShortFormat()
	{
		return shortFormat;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public static List getDateFormats()
	{
		return dateFormatList;
	}
	
	public static DateFormats fromId(String id)
	{
		for (Iterator iter = dateFormatList.iterator(); iter.hasNext();) 
		{
			DateFormats aFormat = (DateFormats) iter.next();
			if(aFormat.getId().equals(id))
			{
				return aFormat;
			}
		} 
		return null;
	}

		public static DateFormats ddMMyyyy = new DateFormats("ddMMyyyy", "dd-MM-yyyy HH:mm", "dd-MM-yyyy", "dd-mm-yyyy (21-4-2008)", "21-4-2008 or 21-4-2008 13:55");
		public static DateFormats MMddyyyy = new DateFormats("MMddyyyy", "MM-dd-yyyy HH:mm", "MM-dd-yyyy", "mm-dd-yyyy (4-21-2008)", "4-21-2008 or 4-21-2008 13:55");
		public static DateFormats MMMddyyyy = new DateFormats("MMMddyyyy", "MMM dd, yyyy:h:mm:a", "MMM dd, yyyy", "Feb 18, 2011", "Feb 18, 2011");
}
