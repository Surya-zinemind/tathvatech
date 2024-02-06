package com.tathvatech.common.common;

public class ErrorCode 
{
	public static ErrorCode SavingUnLockedSections = new ErrorCode(1005, "Save failed, Trying to save items not locked by user.");
	public static ErrorCode SavingItemsUpdatedByOthers = new ErrorCode(1006, "Save failed, Items were updated by other users.");
	private ErrorCode()
	{
		
	}
	
	private ErrorCode(int code, String description)
	{
		this.errorCode = code;
		this.description = description;
	}
	int errorCode;
	String description;
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
