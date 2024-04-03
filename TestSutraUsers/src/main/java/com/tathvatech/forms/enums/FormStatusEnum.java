package com.tathvatech.forms.enums;

public enum FormStatusEnum 
{
	NOTSTARTED("Not Started"),
	INPROGRESS("In Progress"),
	PAUSED("Paused"),
	COMPLETE("Complete"),
	VERIFIED("Verified"),
	APPROVED("Approved"),
	APPROVED_WITH_COMMENTS("Approved with comments"),
	;
	
	private String statusString;
	
	FormStatusEnum(String statusString)
	{
		this.statusString = statusString;
	}
	
	public String getStatusString()
	{
		return statusString;
	}
	
	public static FormStatusEnum fromStatusString(String statusString)
	{
		for (FormStatusEnum e : FormStatusEnum.values()) 
		{
			if(e.statusString.equals(statusString))
			{
				return e;
			}
		}
		return null;
	}

}
