package com.tathvatech.user.entity;

public class UserEmailChangeRequest
{
	private String newEmail;
	private String generatedOTP;
	
	public String getNewEmail()
	{
		return newEmail;
	}
	public void setNewEmail(String newEmail)
	{
		this.newEmail = newEmail;
	}
	public String getGeneratedOTP()
	{
		return generatedOTP;
	}
	public void setGeneratedOTP(String generatedOTP)
	{
		this.generatedOTP = generatedOTP;
	}
}
