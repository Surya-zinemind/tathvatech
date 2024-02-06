package com.tathvatech.common.common;

import com.tathvatech.common.email.EmailMessageInfo;
import com.tathvatech.common.entity.AttachmentIntf;

import java.util.List;

/**
 * 
 * @author Harikrishnan
 * use this class only for issues raised by customers/clients through the system and not for system generated emails
 */
public class CustomerSupportEmailMessageInfo extends EmailMessageInfo
{

	public CustomerSupportEmailMessageInfo(String fromAddress, String replyTo,
			String[] toAddress, String subject, String messageText,
			String messageHtml, List<AttachmentIntf> attachments)
	{
		super(fromAddress, replyTo, toAddress, subject, messageText, messageHtml,
				attachments);
		// TODO Auto-generated constructor stub
	}

}
