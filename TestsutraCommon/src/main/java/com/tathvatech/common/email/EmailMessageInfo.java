/*
 * Created on Sep 29, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.email;

import com.tathvatech.common.entity.AttachmentIntf;

import java.util.List;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EmailMessageInfo
{
    private String fromAddress;
    private String replyTo;
    private String[] toAddress;
    private String subject;
    private String messageText;
    private String messageHtml;

    private List<AttachmentIntf> attachments;
    
    /**
     * @param fromAddress
     * @param toAddress
     * @param subject
     * @param message
     */
    public EmailMessageInfo(String fromAddress, String[] toAddress, String subject, String messageText)
    {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.subject = subject;
        this.messageText = messageText;
    }

    public EmailMessageInfo(String fromAddress, String replyTo, String[] toAddress, String subject, 
    		String messageText, String messageHtml, List<AttachmentIntf> attachments)
    {
        this.fromAddress = fromAddress;
        this.replyTo = replyTo;
        this.toAddress = toAddress;
        this.subject = subject;
        this.messageText = messageText;
        this.messageHtml = messageHtml;
        this.attachments = attachments;
    }

    /**
     * @return Returns the fromAddress.
     */
    public String getFromAddress()
    {
        return fromAddress;
    }
    /**
     * @param fromAddress The fromAddress to set.
     */
    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }
    public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

    public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageHtml() {
		return messageHtml;
	}

	public void setMessageHtml(String messageHtml) {
		this.messageHtml = messageHtml;
	}

	/**
     * @return Returns the subject.
     */
    public String getSubject()
    {
        return subject;
    }
    /**
     * @param subject The subject to set.
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }
    /**
     * @return Returns the toAddress.
     */
    public String[] getToAddress()
    {
        return toAddress;
    }
    /**
     * @param toAddress The toAddress to set.
     */
    public void setToAddress(String[] toAddress)
    {
        this.toAddress = toAddress;
    }
	public List<AttachmentIntf> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentIntf> attachments) {
		this.attachments = attachments;
	}
}
