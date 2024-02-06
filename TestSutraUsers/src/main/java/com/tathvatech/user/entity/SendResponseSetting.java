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
@Table(name="TAB_SENDRESPONSE_SETTING")
public class SendResponseSetting extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int userPk;
	private int surveyPk;
	private String notifyOn;
	private String notifyTo;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getUserPk()
	{
		return userPk;
	}
	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}
	public int getSurveyPk()
	{
		return surveyPk;
	}
	public void setSurveyPk(int surveyPk)
	{
		this.surveyPk = surveyPk;
	}
	public String getNotifyOn()
	{
		return notifyOn;
	}
	public void setNotifyOn(String notifyOn)
	{
		this.notifyOn = notifyOn;
	}
	public String getNotifyTo()
	{
		return notifyTo;
	}
	public void setNotifyTo(String notifyTo)
	{
		this.notifyTo = notifyTo;
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
