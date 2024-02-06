/*
 * Created on Sep 29, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.Asynch;

import java.util.Date;
import java.util.Timer;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.email.EmailMessageInfo;
import com.tathvatech.common.email.EmailServiceDelegate;
import org.apache.log4j.Logger;

import com.tathvatech.ts.caf.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AsyncProcessor
{
	private static final Logger logger = LoggerFactory.getLogger(AsyncProcessor.class);

    @Deprecated 
    /**
     * TODO
     * move this method to EmailServiceDelegate/Manager
     * @param messageInfo
     * @return
     */
    public static void scheduleEmail(EmailMessageInfo messageInfo)
    {
    	scheduleEmail(new EmailMessageInfo[]{messageInfo});
    }
    
    @Deprecated 
    /**
     * TODO
     * move this method to EmailServiceDelegate/Manager
     * @param messageInfo
     * @return
     */
    public static void scheduleEmail(EmailMessageInfo[] messageInfoSet)
    {
    	System.out.println(String.format("Scheduling an email job to send %s number of emails", messageInfoSet.length));
    	String emailEnabled = ApplicationProperties.isEmailEnabled();
    	if(messageInfoSet == null || messageInfoSet.length == 0)
    		return ;
    	
    	String printMailTextToConsole = ApplicationProperties.getProperty("config/email/printMailTextToConsole");
    	
    	for (int i = 0; i < messageInfoSet.length; i++)
		{
    		try
			{
	    		EmailMessageInfo messageInfo = messageInfoSet[i];
	        	if("true".equals(printMailTextToConsole))
	        	{
	        		System.out.println("HTML:\n" + messageInfo.getMessageHtml());
	        		System.out.println("TEXT:\n" + messageInfo.getMessageText());
	        	}

	        	if("false".equalsIgnoreCase(emailEnabled))
	        	{
	            	if(logger.isInfoEnabled())
	            		logger.info("Email is disabled, so not scheduling the email.");
	        		continue;
	        	}
	        	
	        	// If it is a customer support request, send it to the real customer support address and dont change it.
	        	// use this class only for issues raised by customers/clients through the system and not for system generated emails
	        	if(!(messageInfo instanceof CustomerSupportEmailMessageInfo))
	        	{	
	    	    	String emailTestMode = ApplicationProperties.isEmailInTestMode();
	    	    	if("true".equalsIgnoreCase(emailTestMode))
	    	    	{
	    	    		String toEmail = ApplicationProperties.getEmailTestToAddress();
	    	    		messageInfo.setToAddress(new String[]{toEmail});
	    	        	if(logger.isInfoEnabled())
	    	        		logger.info("Email set to test mode, changing the to address to " + toEmail);
	    	    	}
	        	}
		    	
	        	EmailServiceDelegate.scheduleEmail(messageInfo);
	        	if(logger.isInfoEnabled())
	        		logger.info("Adding a send email job to the scheduler, email to - " + messageInfo.getToAddress());
			}
			catch (Exception ex)
			{
	    		logger.error("Could not schedule an email sender job" + " :: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
    }

    public static int logLogin(int accountPk, int userPk, String userName, String ipAddress, Date loginTime, String comments)
    {
   		try
   		{
   			Timer timer = new Timer();
   			LoginLogInfo loginLog = new LoginLogInfo(accountPk, userPk, userName, ipAddress, loginTime, comments);
   			timer.schedule(new LoginLoggerJob(loginLog), 10);
			if (logger.isDebugEnabled())
            {
                logger.debug("New LogLogin job scheduled..");
            }
			
			return 0;

   		}
   		catch (Exception ex) 
   		{
    		logger.error("Could not schedule a LogLogin job" + " :: " + ex.getMessage());
   		    if (logger.isDebugEnabled())
            {
   		    	logger.debug(ex.getMessage(), ex);
            }
   		    
   		    return -1;
    	}
    }
}
