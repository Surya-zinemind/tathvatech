/*
 * Created on Mar 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LineSeperatorUtil
{
    private static final Logger logger = Logger.getLogger(LineSeperatorUtil.class);
    
    public static String changeSystemLineSeperatorToBR(String inString)
    {
        //see comments for the function below
        inString = inString.replaceAll("\r\n", System.getProperty("line.separator"));

        List tokens = tokenizeString(inString, System.getProperty("line.separator"));
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<tokens.size(); i++)
        {
            String aTok = (String) tokens.get(i);
            sb.append(aTok);
            if(i != (tokens.size()-1))
            {
                sb.append("<br>");
            }
        }
        
        return sb.toString();
    }
    
    public static List tokenizeSystemLineSeperator(String inString)
    {
        List newTokens = new ArrayList();
        
        //simply tokenizing with system line seperator wil not work as the browser could be on windows which sends a \r\n as the new line
        //and the server is hosted on a unix which has \n for the lineseperator. this will leave the \r as the last char in the tokens
        //to avoid this before tokenizing , change the \r\n char sequence to the system line sequence and then tokenize
        //in windows the replaced string will be the same and in unix the newlines will gets changed to unix newlines.
        inString = inString.replaceAll("\r\n", System.getProperty("line.separator"));
        
        List tokens = tokenizeString(inString, System.getProperty("line.separator"));
        for (int i=0; i<tokens.size(); i++)
        {
            String aTok = (String) tokens.get(i);
            if(aTok.endsWith("\r"))
            {
                aTok = aTok.substring(0, (aTok.length()-1));
            }
            newTokens.add(aTok);
        }
        
        return newTokens;
    }


    
    /**
     * generic String tokenizer
     * @param inString
     * @return
     */
    public static List tokenizeString(String inString, String token)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("inString - " + inString);
            logger.debug("token - " + token);
        }
        
        List tokenList = new ArrayList();
        
        if(inString != null)
        {
	        int start = 0;
	        int end = inString.length();
	        while (start < end)
	        {
	            if (logger.isDebugEnabled())
                {
                    logger.debug("Inside while, start - " + start + ", end - " + end);
                }
	            int delimIdx = inString.indexOf(token,start);
	            if (delimIdx < 0)
	            {
	                String tok = inString.substring(start);
	                if (logger.isDebugEnabled())
                    {
                        logger.debug("Token is - " + tok);
                    }
	                if(tok == null || tok.trim().length() == 0)
	                {
		                start = end;
	                    continue;
	                }
		            tokenList.add(tok);
	                start = end;
	            }
	            else
	            {
		            String tok = inString.substring(start, delimIdx);
	                if (logger.isDebugEnabled())
                    {
                        logger.debug("Token is - " + tok);
                    }
	                if(tok == null || tok.trim().length() == 0)
	                {
	                    start = delimIdx + token.length();
	                    continue;
	                }

		            tokenList.add(tok);
		            start = delimIdx + token.length();
	            }
	        }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Return Token List size - " + tokenList.size());
        }
	    return tokenList;
    }

    public static String changeBRToSystemLineSeperator(String inString)
    {
        List tokens = tokenizeString(inString, "<br>");
        StringBuffer sb = new StringBuffer();
        for (int i=0; i< tokens.size(); i++)
        {
            String aTok = (String) tokens.get(i);
            sb.append(aTok);
            if(i != (tokens.size()-1))
            {
                sb.append(System.getProperty("line.separator"));
            }
        }
        
        return sb.toString();
    }
}
