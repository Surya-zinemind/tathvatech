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

import org.apache.log4j.Logger;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AccountStatusTypes
{
	public static final Logger logger = Logger.getLogger(AccountStatusTypes.class);
	
    private String id;
    private String name;

    private static List itemList = new ArrayList();

    /**
     * @param id
     * @param value
     */
    public AccountStatusTypes(String id, String name)
    {
        this.id = id;
        this.name = name;
        
        itemList.add(this);
    }

    /**
     * @return 
     */
    public String getId()
    {
        return id;
    }
    
    public String getName()
    {
    	return name;
    }
    
    public static AccountStatusTypes fromId(String id)
    {
    	if (logger.isDebugEnabled()) 
    	{
			logger.debug("Looking up LineItem for id - " + id);
		}
    	for (Iterator iter = itemList.iterator(); iter.hasNext();) 
    	{
			AccountStatusTypes element = (AccountStatusTypes) iter.next();
			if(element.getId().equals(id))
			{
				return element;
			}
		}
    	return null;
    }
    
    public String toString()
    {
    	return name;
    }

    /**
     * @return 
     */
    public static List getItemList()
    {
        return itemList;
    }


    public static final AccountStatusTypes STATUS_ACTIVE = new AccountStatusTypes("Active", "Active"); 
    public static final AccountStatusTypes STATUS_PENDING_CANCEL = new AccountStatusTypes("PendingCancel", "Pending Cancel"); 
    public static final AccountStatusTypes STATUS_INACTIVE = new AccountStatusTypes("Inactive", "Inactive");
    public static final AccountStatusTypes STATUS_PENDING_PAYMENTAUTH = new AccountStatusTypes("PendingPayment", "Pending Payment");
    public static final AccountStatusTypes STATUS_NEWACCOUNT = new AccountStatusTypes("NewAccount", "New Account");
	public static final AccountStatusTypes STATUS_SUSPENDED = new AccountStatusTypes("Suspended", "Suspended");
}
