package com.tathvatech.user.security.config;


public interface SecurityCredential 
{
	String getGuardedObjectPk();
	boolean isPermitted(int key);
}
