package com.tathvatech.common.utils;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnewayEncryptUtils
{
    private static final Logger logger = LoggerFactory.getLogger(OnewayEncryptUtils.class);

    private static String getString(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            sb.append((int) (0x00FF & b));
            if (i + 1 < bytes.length)
            {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    private static String getBase64String(byte[] bytes)
    {
        return new String(Base64.encodeBase64(bytes));
    }

    private static byte[] getBytes(String str)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(str, "-", false);
        while (st.hasMoreTokens())
        {
            int i = Integer.parseInt(st.nextToken());
            bos.write((byte) i);
        }
        return bos.toByteArray();
    }

    public static String md5(String source)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            return getString(bytes);
            
        }
        catch (Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            return null;
        }
    }

    public static String sha(String source)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source.getBytes());
            return getString(bytes);
        }
        catch (Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            return null;
        }
    }

    public static String encryptSeqId(String source)
    {
//        return source;
        
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            String encString =  getBase64String(bytes);
            encString = encString.substring(0, encString.length() -2);
            encString = encString.replace('+', '-');
            encString = encString.replace('/', '_');
            return encString;
        }
        catch (Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            return source;
        }

    }

    public static String encryptString(String source)
    {
//        return source;
        
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            String encString =  getBase64String(bytes);
            encString = encString.substring(0, encString.length() -2);
            encString = encString.replace('+', '-');
            encString = encString.replace('/', '_');
            return encString;
        }
        catch (Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
            return source;
        }

    }
    
    public static void main(String[] args)
    {
    	String source = "1234asdasddfsdf";
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            String encString =  getBase64String(bytes);
            System.out.println(encString);
        }
        catch (Exception e)
        {
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
        }

    }
}