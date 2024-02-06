/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.user.common;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;
import java.util.TimeZone;

import com.tathvatech.common.common.SecurityManagerBase;
import com.tathvatech.user.entity.AccountBase;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.UserBase;

public interface UserContext extends Principal, Serializable
{
	public UserBase getUser();
	
	public AccountBase getAccount();
	
	public void setAccount(AccountBase acc);

	public void setUser(UserBase user);
	
    public String getSessionId();

	public Date getLoginTime();
	
	public TimeZone getTimeZone();
	
	public Site getSite();
	
	public void setTimeZone(TimeZone timeZone);
	
	public void setProperty(Object key, Object value);
	
	public Object getProperty(Object key);
	
	public SecurityManagerBase getSecurityManager();
	
	public void setSecurityManager(SecurityManagerBase securityManager);
}
