/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.tathvatech.ts.caf.core.exception.BaseException;
import com.tathvatech.ts.caf.core.exception.ConvertorNotDefinedException;
import com.tathvatech.ts.caf.core.exception.InvalidDataFormatException;
import com.tathvatech.ts.caf.core.framework.typeconvertor.DataTypeConvertor;
import com.tathvatech.ts.caf.core.metadata.MetadataFactory;

public class DataConversionUtil
{
	public static final Logger logger = Logger
			.getLogger(DataConversionUtil.class);
	
    public static String inputDateFormat = "MM/dd/yyyy";
    
    public static String FORMAT_MEDIUM = "dd MMM, yyyy";
    public static String FORMAT_DATETIME = "dd MMM yyyy hh:mm a (z)";
    public static String FORMAT_DATE24TIME = "dd MMM yyyy HH:mm"; //this format is used in MasterSQLTransformer. 
    public static String FORMAT_DATE = "dd MMM yyyy"; //this format is used in SurveyStatsReport. 
    public static String FORMAT_DATE_SURVEY = "dd MMM yy"; //format used for import
    public static String FORMAT_DATE_IMPORT = "dd MMM yy"; //format used for import
    public static String FORMAT_DATETIME_IMPORT = "dd MMM yy hh:mm a"; //format used for import

    public static DataTypeConvertor getDataConvertor(String dataType)
    {
        String convertor = MetadataFactory.getInstance().getDataTypeConvertor(dataType);

        if(convertor != null)
        {
            try
            {
            	DataTypeConvertor convertorClass = (DataTypeConvertor)Class.forName(convertor).newInstance();
                return convertorClass;
            }
            catch(ClassNotFoundException cx)
            {
	            throw new ConvertorNotDefinedException();
            }
            catch(InstantiationException ix)
            {
                throw new BaseException();
            }
            catch(IllegalAccessException iax)
            {
                throw new BaseException();
            }
        }
        else
        {
            throw new ConvertorNotDefinedException();
        }
    }

    public static Date getDateFromString(String dateString)
    {
        if(dateString == null || dateString.trim().equals(""))
        {
            return null;
        }

        try
        {
        	SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormat);
        	return dateFormat.parse(dateString);
        }
        catch(Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            throw new InvalidDataFormatException();
        }
    }

    public static Date getDateFromString(String dateString, String format)
    {
        if(dateString == null || dateString.trim().equals(""))
        {
            return null;
        }

        try
        {
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        	return dateFormat.parse(dateString);
        }
        catch(Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            throw new InvalidDataFormatException();
        }
    }

    public static Date getDateFromString(String dateString, String format, TimeZone tZone)
    {
        if(dateString == null || dateString.trim().equals(""))
        {
            return null;
        }

        try
        {
        	SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        	dateFormat.setTimeZone(tZone);
        	return dateFormat.parse(dateString);
        }
        catch(Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            throw new InvalidDataFormatException();
        }
    }

    public static String getStringFromDate(Date date)
	{
		if(date == null)
		{
			return "";
		}
		
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(inputDateFormat);

			return dateFormat.format(date);
		}
		catch(Exception e)
		{
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new InvalidDataFormatException();
		}
	}

	public static String getStringFromDate(Date date, String format)
	{
		if(date == null)
		{
			return "";
		}
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);

			return dateFormat.format(date);
		}
		catch(Exception e)
		{
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new InvalidDataFormatException();
		}
	}

	public static String getStringFromDate(Date date, String format, TimeZone timeZone)
	{
		if(date == null)
		{
			return "";
		}
		try
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setTimeZone(timeZone);
			return dateFormat.format(date);
		}
		catch(Exception e)
		{
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new InvalidDataFormatException();
		}
	}

	public static Integer getIntegerFromString(String dateString)
    {
        if(dateString == null || dateString.trim().equals(""))
        {
            return null;
        }

        try
        {
        	return new Integer(dateString);
        }
        catch(Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            throw new InvalidDataFormatException();
        }
    }

    public static Float getFloatFromString(String dateString)
    {
        if(dateString == null || dateString.trim().equals(""))
        {
            return null;
        }

        try
        {
        	return new Float(dateString);
        }
        catch(Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            throw new InvalidDataFormatException();
        }
    }
}
