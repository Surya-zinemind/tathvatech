/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_ACCOUNT_ALERT")
public class AccountAlert extends AbstractEntity implements Serializable
{
    public static final String STATUS_OPEN = "open";
    public static final String STATUS_DISMISSED = "dismissed";

	@Id
    private long pk;
    private long accountPk;
    private Date submitTime;
    private String text;
    private String status;
    private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public long getAccountPk()
	{
		return accountPk;
	}
	public void setAccountPk(long accountPk)
	{
		this.accountPk = accountPk;
	}
	public Date getSubmitTime()
	{
		return submitTime;
	}
	public void setSubmitTime(Date submitTime)
	{
		this.submitTime = submitTime;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
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
