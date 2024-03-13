package com.tathvatech.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;



@Entity
@Table(name = "TAB_DEVICE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends AbstractEntity
{
	@Id
	private long pk;
	private long userPk;
	private String status;
	private String sessionKey;
	
    private Date lastUpdated;

    public long getPk()
    {
        return pk;
    }
    public void setPk(long pk)
    {
        this.pk = pk;
    }
	public long getUserPk()
	{
		return userPk;
	}
	public void setUserPk(long userPk)
	{
		this.userPk = userPk;
	}
	public String getSessionKey()
	{
		return sessionKey;
	}
	public void setSessionKey(String sessionKey)
	{
		this.sessionKey = sessionKey;
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
}
