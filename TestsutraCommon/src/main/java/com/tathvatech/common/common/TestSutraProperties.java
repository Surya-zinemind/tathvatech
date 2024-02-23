/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSutraProperties
{
	private static final Logger logger = LoggerFactory.getLogger(TestSutraProperties.class);

	public static String getProperty(String propName)
	{
	    return ApplicationProperties.getProperty(propName);
	}
	
	public static String getFormDefRoot()throws Exception
	{
		return ApplicationProperties.getFormDefRoot();
	}
	public static String getReportDefRoot()throws Exception
	{
		return ApplicationProperties.getReportDefRoot();
	}
	
	public static String getSiteUrl()
	{
		return ApplicationProperties.getPropertyFromConfig("config/server/url", "");
	}

}
