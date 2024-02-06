package com.tathvatech.common.email;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.accounts.AccountManager;
import com.tathvatech.ts.core.accounts.User;

public class EmailServiceManager
{
	public static void scheduleEmail(EmailMessageInfo emailMessage) throws Exception
	{
		Date nowDate = new Date();
		Date expiryDate = new Date(nowDate.getTime() + EmailSenderConfig.emailExpiryWindowMilliseconds);
		EmailQueue eq = new EmailQueue();
		eq.setCreatedDate(nowDate);
		eq.setExpiryDate(expiryDate);
		eq.setFromAddress(emailMessage.getFromAddress());
		eq.setMessageHtml(emailMessage.getMessageHtml());
		eq.setMessageText(emailMessage.getMessageText());
		eq.setReplyTo(emailMessage.getReplyTo());
		eq.setStatus(EmailQueue.Status.Pending.name());
		eq.setSubject(emailMessage.getSubject());
		
		//coma separated toAddress
		StringBuffer sb = new StringBuffer();
		String sep = "";
		for (int i = 0; i < emailMessage.getToAddress().length; i++)
		{
			String emailAddress = emailMessage.getToAddress()[i];
			User user = AccountManager.findUserByEmail(emailAddress);
			if(user != null)
			{
				if(User.STATUS_ACTIVE.contentEquals(user.getStatus()))
				{
					// if the email is associated to a user, send the email only if the user is active 
					sb.append(sep).append(emailAddress);
					sep = ",";
				}
			}
			else
			{
				// an email to be sent to a address which is not related to a user. let it send the email
				sb.append(sep).append(emailAddress);
				sep = ",";
			}
		}
		if(sb.length() == 0)
			return;
		
		eq.setToAddressList(sb.toString());
		
		//store attachment list as json
		if(emailMessage.getAttachments() != null && emailMessage.getAttachments().size() > 0)
		{
			ByteArrayOutputStream oc = new ByteArrayOutputStream();
	        OutputStreamWriter writer = new OutputStreamWriter(oc, "UTF-8");
	    	ObjectMapper jm = new ObjectMapper();
	        jm.writeValue(writer, emailMessage.getAttachments());
			eq.setAttachmentsJson(oc.toString());
	        writer.close();
	        oc.close();
		}
		
		
		PersistWrapper.createEntity(eq);
		
	}
	
	public static void noteEmailSuccess(int emailQueueItemPk)
	{
        try
        {
        	Connection con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(true);

            EmailQueue item = PersistWrapper.readByPrimaryKey(EmailQueue.class, emailQueueItemPk);
            item.setTryCount(item.getTryCount() + 1);
            item.setLastTryDate(new Date());
            item.setStatus(EmailQueue.Status.Completed.name());
            PersistWrapper.update(item);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
	}

	public static void noteEmailFailure(int emailQueueItemPk)
	{
        try
        {
        	Connection con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(true);

            EmailQueue item = PersistWrapper.readByPrimaryKey(EmailQueue.class, emailQueueItemPk);
            item.setTryCount(item.getTryCount() + 1);
            item.setLastTryDate(new Date());
            if(item.getTryCount() == EmailSenderConfig.maxEmailTries)
            	item.setStatus(EmailQueue.Status.Failed.name());
            else
            	item.setStatus(EmailQueue.Status.Pending.name());
            	
            PersistWrapper.update(item);
        }
        catch(Exception ex)
        {
			ex.printStackTrace();
        }
	}

}
