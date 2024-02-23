package com.tathvatech.user.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.SecurityManagerBase;
import com.tathvatech.user.entity.*;
import com.tathvatech.user.service.PlanSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserContextImpl implements UserContext
{
	private  final Logger logger = LoggerFactory.getLogger(UserContext.class);

	private HashMap properties = new HashMap();

	// todo:: I am faking the permissions associated with the person here.. fix
	// it later on
	public UserContextImpl(TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.caf.security.UserContext#getProperty(java.lang.Object)
	 */
	public Object getProperty(Object key)
	{
		return properties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.caf.security.UserContext#setProperty(java.lang.Object,
	 * java.lang.Object)
	 */
	public void setProperty(Object key, Object value)
	{
		properties.remove(key);
		properties.put(key, value);
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public Date getLoginTime()
	{
		return loginTime;
	}

	public TimeZone getTimeZone()
	{
		if (timeZone == null)
		{
			if (user != null)
				timeZone = TimeZone.getTimeZone(user.getTimezone());
			else
				timeZone = TimeZone.getTimeZone(ApplicationProperties.getDefaultTimezone());
		}
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone)
	{
		this.timeZone = timeZone;

	}

	public Site getSite()
	{
		return site;
	}

	public void setSite(Site site)
	{
		this.site = site;
	}

	User user;
	Account account;
	Site site;
	TimeZone timeZone;
	Date loginTime;
	private String sessionId;
	private boolean proxyLogin = false;
	private List groups;
	private List permissions;
	private PlanSecurityManager securityManager;

	@Override
	public User getUser()
	{
		return user;
	}

	@Override
	public Account getAccount()
	{
		return account;
	}

	public void setAccount(AccountBase acc)
	{
		this.account = (Account) acc;
	}

	public void setUser(UserBase user)
	{
		this.user = (User) user;
	}

	public PlanSecurityManager getSecurityManager()
	{
		securityManager.setUserContext(this);
		return securityManager;
	}

	public void setSecurityManager(SecurityManagerBase securityManager)
	{
		this.securityManager = (PlanSecurityManager) securityManager;
	}

	@Override
	public String getName()
	{
		return getUser().getDisplayString();
	}
}
