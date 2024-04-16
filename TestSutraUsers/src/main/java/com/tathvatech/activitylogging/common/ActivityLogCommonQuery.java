/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.activitylogging.common;

import java.util.Date;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ActivityLogCommonQuery
{

	private int pk;
	private int action;
	private Date actionTime;
	private int executedByPk;
	private String executedByFirstName;
	private String executedByLastName;
	private int executedByUserSitePk;
	private String executedByUserSiteName;
	private String comment;
	private int attachmentPk;
	private Date lastUpdated;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
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

	public int getExecutedByPk()
	{
		return executedByPk;
	}

	public void setExecutedByPk(int executedByPk)
	{
		this.executedByPk = executedByPk;
	}

	public String getExecutedByFirstName()
	{
		return executedByFirstName;
	}

	public void setExecutedByFirstName(String executedByFirstName)
	{
		this.executedByFirstName = executedByFirstName;
	}

	public String getExecutedByLastName()
	{
		return executedByLastName;
	}

	public void setExecutedByLastName(String executedByLastName)
	{
		this.executedByLastName = executedByLastName;
	}

	public int getExecutedByUserSitePk()
	{
		return executedByUserSitePk;
	}

	public void setExecutedByUserSitePk(int executedByUserSitePk)
	{
		this.executedByUserSitePk = executedByUserSitePk;
	}

	public String getExecutedByUserSiteName()
	{
		return executedByUserSiteName;
	}

	public void setExecutedByUserSiteName(String executedByUserSiteName)
	{
		this.executedByUserSiteName = executedByUserSiteName;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public int getAttachmentPk()
	{
		return attachmentPk;
	}

	public void setAttachmentPk(int attachmentPk)
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
    public ActivityLogCommonQuery()
    {
    }

    public static String fetchQuery = "select a.pk, a.actionTime, a.executedByPk, u.firstName as executedByFirstName, u.lastName as executedByLastName, "
    		+ "site.pk as executedByUserSitePk, site.name as executedByUserSiteName, "
    		+ "a.action, a.comment, a.attachmentPk, a.lastUpdated from  activity_log a "
    		+ "join TAB_USER u on a.executedByPk = u.pk "
    		+ "join site on u.sitePk = site.pk where 1 = 1 "; 

}
