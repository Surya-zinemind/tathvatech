package com.tathvatech.user.service;

import java.util.TimeZone;


import com.tathvatech.common.exception.AppException;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceContextCreator
{
	private  final Logger logger = LoggerFactory.getLogger(DeviceContextCreator.class);
	public  UserContext createContext(User user) throws Exception
	{

		String planId = PlanSecurityManager.ID_ADMINPLAN;

		// initialize the security manager accordingly
		PlanSecurityManager psManager = null;

		psManager = PlanSecurityManager.getSecurityManager(user);
		if (logger.isDebugEnabled())
		{
			logger.debug("Security Manager is - " + psManager.getClass().getName());
		}

		TimeZone tZone = null; // TimeZone.getTimeZone(user.getTimezone()); TODO

		if (psManager == null)
		{
			throw new AppException("Could not initialize connection, Please try again later");
		}

		UserContextImpl context = new UserContextImpl(TimeZone.getTimeZone(user.getTimezone()));

		context.setSecurityManager(psManager);
		context.setUser(user);
		Site site= SiteDelegate.getSite(user.getSitePk());
		context.setSite(site);
		context.setTimeZone(tZone);

		return context;
	}

	public  UserContext createContext(User user, HttpServletRequest request)throws Exception
	{
		
		String planId = PlanSecurityManager.ID_ADMINPLAN;

		//initialize the security manager accordingly
		PlanSecurityManager psManager = null;

		psManager = PlanSecurityManager.getSecurityManager(user);
		if(logger.isDebugEnabled())
		{
			logger.debug("Security Manager is - " + psManager.getClass().getName());
		}

        TimeZone tZone = null; //TimeZone.getTimeZone(user.getTimezone()); TODO

		if(psManager == null)
		{
			throw new AppException("Could not initialize connection, Please try again later");
		}

		UserContextImpl context = new UserContextImpl(TimeZone.getTimeZone(user.getTimezone()));

		context.setSecurityManager(psManager);
		context.setUser(user);
		context.setTimeZone(tZone);
		Site site= SiteDelegate.getSite(user.getSitePk());
		context.setSite(site);
		return context;
	}
}
