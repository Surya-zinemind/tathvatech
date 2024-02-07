/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;


/**
 * @author Hari
 * Represents an email queue entry in the database, a timer job will pickup these and send the email
 */
@Entity
@Table(name="email_queue")
public class EmailQueue
{
	public static enum Status{Pending, Completed, Failed};

	@Id
	private long pk;
	private String fromAddress;
	private String toAddressList;
	private String replyTo;
	private String subject ;
    private String messageText;
    private String messageHtml;
	private String attachmentsJson; // this is the json representation of any attachments
	private Date createdDate;
	private Date expiryDate;
	private Date lastTryDate;
	private int tryCount;
	private String status;
	private Date lastUpdated;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public String getFromAddress()
	{
		return fromAddress;
	}

	public void setFromAddress(String fromAddress)
	{
		this.fromAddress = fromAddress;
	}

	public String getToAddressList()
	{
		return toAddressList;
	}

	public void setToAddressList(String toAddressList)
	{
		this.toAddressList = toAddressList;
	}

	public String getReplyTo()
	{
		return replyTo;
	}

	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getMessageText()
	{
		return messageText;
	}

	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}

	public String getMessageHtml()
	{
		return messageHtml;
	}

	public void setMessageHtml(String messageHtml)
	{
		this.messageHtml = messageHtml;
	}

	public String getAttachmentsJson()
	{
		return attachmentsJson;
	}

	public void setAttachmentsJson(String attachmentsJson)
	{
		this.attachmentsJson = attachmentsJson;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public Date getExpiryDate()
	{
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate)
	{
		this.expiryDate = expiryDate;
	}

	public Date getLastTryDate()
	{
		return lastTryDate;
	}

	public void setLastTryDate(Date lastTryDate)
	{
		this.lastTryDate = lastTryDate;
	}

	public int getTryCount()
	{
		return tryCount;
	}

	public void setTryCount(int tryCount)
	{
		this.tryCount = tryCount;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
     * 
     */
    public EmailQueue()
    {
    }

    @Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj) 
    {
    	if(obj == null || !(obj instanceof EmailQueue))
    		return false;
    	if(((EmailQueue)obj).getPk() == this.getPk())
    		return true;
    	
    	return false;
    }
}
