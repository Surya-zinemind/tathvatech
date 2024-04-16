/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.activitylogging.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 * The ActivityLog class is specific to FormSaveActions.. this should take care of the common ones.
 * TAB_ACTIVITY_LOG table is only for Form actions.
 */
@Entity
@Table(name="activity_log")
public class ActivityLogCommon extends AbstractEntity implements Serializable
{

	@Id
	private long pk;
	private int executedByPk;
	private int action;
	private Date actionTime;
	private String comment;
	private Integer attachmentPk;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getExecutedByPk()
	{
		return executedByPk;
	}

	public void setExecutedByPk(int executedByPk)
	{
		this.executedByPk = executedByPk;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Date getActionTime()
	{
		return actionTime;
	}

	public void setActionTime(Date actionTime)
	{
		this.actionTime = actionTime;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Integer getAttachmentPk()
	{
		return attachmentPk;
	}

	public void setAttachmentPk(Integer attachmentPk)
	{
		this.attachmentPk = attachmentPk;
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
    public ActivityLogCommon()
    {
    }
}
