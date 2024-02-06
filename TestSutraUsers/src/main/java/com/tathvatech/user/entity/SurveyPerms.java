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
@Table(name="TAB_SURVEY_PERMS")
public class SurveyPerms extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int surveyPk;
	private int userPk;
	private boolean dataEntry;
	private boolean mDataEntry;
	private boolean view;
	private boolean edit;
	private boolean publish;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getSurveyPk()
	{
		return surveyPk;
	}
	public void setSurveyPk(int surveyPk)
	{
		this.surveyPk = surveyPk;
	}
	public int getUserPk()
	{
		return userPk;
	}
	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}
	public boolean isDataEntry()
	{
		return dataEntry;
	}
	public void setDataEntry(boolean dataEntry)
	{
		this.dataEntry = dataEntry;
	}
	public boolean ismDataEntry()
	{
		return mDataEntry;
	}
	public void setmDataEntry(boolean mDataEntry)
	{
		this.mDataEntry = mDataEntry;
	}
	public boolean isView()
	{
		return view;
	}
	public void setView(boolean view)
	{
		this.view = view;
	}
	public boolean isEdit()
	{
		return edit;
	}
	public void setEdit(boolean edit)
	{
		this.edit = edit;
	}
	public boolean isPublish()
	{
		return publish;
	}
	public void setPublish(boolean publish)
	{
		this.publish = publish;
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