package com.tathvatech.activitylogging.common;

import com.tathvatech.ts.core.accounts.UserOID;

public class ActivityLogRequestBeanBase
{
	private int pk;
	private UserOID executedBy;
	private BaseActions action;
	private String comment;
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public UserOID getExecutedBy()
	{
		return executedBy;
	}
	public void setExecutedBy(UserOID executedBy)
	{
		this.executedBy = executedBy;
	}
	public BaseActions getAction()
	{
		return action;
	}
	public void setAction(BaseActions action)
	{
		this.action = action;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
}
