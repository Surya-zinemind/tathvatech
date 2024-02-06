package com.tathvatech.common.common;

import java.util.Date;

public class LoginLogInfo 
{
	int accountPk;
	int userPk;
	String userName;
	String ipAddress;
	Date loginTime;
	String comments;
	
	public LoginLogInfo(int accountPk, int userPk, String userName,
			String ipAddress, Date loginTime, String comments) {
		super();
		this.accountPk = accountPk;
		this.userPk = userPk;
		this.userName = userName;
		this.ipAddress = ipAddress;
		this.loginTime = loginTime;
		this.comments = comments;
	}

	public int getAccountPk() {
		return accountPk;
	}

	public void setAccountPk(int accountPk) {
		this.accountPk = accountPk;
	}

	public int getUserPk() {
		return userPk;
	}

	public void setUserPk(int userPk) {
		this.userPk = userPk;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
