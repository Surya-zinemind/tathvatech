package com.tathvatech.user.entity;


import com.tathvatech.user.OID.UserOID;

public interface UserBase {

	int getPk();

	String getUserName();

	String getUserType();

	String getEmail();

	String getFirstName();

	String getLastName();
	
	UserOID getOID();

}
