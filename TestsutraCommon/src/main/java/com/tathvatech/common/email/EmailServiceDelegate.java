package com.tathvatech.common.email;

import com.tathvatech.common.common.ServiceLocator;

import java.sql.Connection;


public class EmailServiceDelegate
{
	public static void scheduleEmail(EmailMessageInfo emailMessage) throws Exception
	{
	    Connection con = null;
	    try
	    {
	        con = ServiceLocator.locate().getConnection();
	        con.setAutoCommit(false);

	        EmailServiceManager.scheduleEmail(emailMessage);
	    }
	    catch(Exception ex)
	    {
	        con.rollback();
	        throw ex;
	    }
	    finally
	    {
	        con.commit();
	    }
	}
}
