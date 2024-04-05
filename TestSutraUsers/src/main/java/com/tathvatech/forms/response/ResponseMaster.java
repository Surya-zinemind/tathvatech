/*
 * Created on Jun 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.response;

import java.util.Date;

import com.tathvatech.ts.core.UserBase;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResponseMaster
{
    private int responseId;
    private String responseRefNo;
    private Date responseTime;
    private Date responseCompleteTime;
    private Date responseSyncTime;
    private ResponseFlags flag;
    private String ipAddress;
    private String status;
    
    private UserBase user;
    
    public Date getResponseTime() 
    {
		return responseTime;
	}

    public void setResponseTime(Date responseTime) 
    {
		this.responseTime = responseTime;
	}
	
    public Date getResponseCompleteTime()
	{
		return responseCompleteTime;
	}

	public void setResponseCompleteTime(Date responseCompleteTime)
	{
		this.responseCompleteTime = responseCompleteTime;
	}

	public Date getResponseSyncTime()
	{
		return responseSyncTime;
	}

	public void setResponseSyncTime(Date responseSyncTime)
	{
		this.responseSyncTime = responseSyncTime;
	}

	public ResponseFlags getFlag()
	{
		return flag;
	}

	public void setFlag(ResponseFlags flag)
	{
		this.flag = flag;
	}

	public String getIpAddress() 
    {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}
	/**
     * @return Returns the responseId.
     */
    public int getResponseId()
    {
        return responseId;
    }
    /**
     * @param responseId The responseId to set.
     */
    public void setResponseId(int responseId)
    {
        this.responseId = responseId;
    }
    
	public String getResponseRefNo()
	{
		return responseRefNo;
	}

	public void setResponseRefNo(String responseRefNo)
	{
		this.responseRefNo = responseRefNo;
	}

	/**
     * @return Returns the status.
     */
    public String getStatus()
    {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    public UserBase getUser()
	{
		return user;
	}

	public void setUser(UserBase user)
	{
		this.user = user;
	}

}
