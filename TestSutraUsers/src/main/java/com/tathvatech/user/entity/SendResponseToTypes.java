/*
 * Created on Dec 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SendResponseToTypes
{
	public static final Logger logger = LoggerFactory.getLogger(SendResponseToTypes.class);
	
    private String displayName;
    private String value;
    private String notifyMethod;

    private static List itemList = new ArrayList();

    /**
     * @param displayName
     * @param value
     * @Param notifyMethod
     */
    public SendResponseToTypes(String displayName, String value, String notifyMethod)
    {
        this.displayName = displayName;
        this.value = value;
        this.notifyMethod = notifyMethod;
        
        itemList.add(this);
    }

    /**
     * @return 
     */
    public String getDisplayName()
    {
        return displayName;
    }
    
    public String getValue()
    {
    	return value;
    }
    
    public String getNotifyMethod()
    {
    	return notifyMethod;
    }
    
    public static SendResponseToTypes fromValue(String value)
    {
    	for (Iterator iter = itemList.iterator(); iter.hasNext();) 
    	{
			SendResponseToTypes element = (SendResponseToTypes) iter.next();
			if(element.getValue().equals(value))
			{
				return element;
			}
		}
    	return null;
    }

    public boolean equals(Object inType)
    {
    	if(inType != null && inType instanceof SendResponseToTypes)
    	{
    		if(((SendResponseToTypes)inType).getValue().equals(this.value))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    /**
     * @return 
     */
    public static List getNotifyTypesList()
    {
        return itemList;
    }

    public static final String METHOD_EMAIL = "Email";
    
    public static final SendResponseToTypes EMAIL = new SendResponseToTypes("Primary email", "priEmail", METHOD_EMAIL); 
    public static final SendResponseToTypes ALT_EMAIL = new SendResponseToTypes("Alternate email", "secEmail", METHOD_EMAIL); 
}
