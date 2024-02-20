/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_ACCOUNT_NOTE")
public class AccountNote  extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int accountPk;
	private Date submitTime;
	private String noteText;
	private String submittedBy;
	private Date lastUpdated;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getAccountPk()
	{
		return accountPk;
	}
	public void setAccountPk(int accountPk)
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
	public String getNoteText()
	{
		return noteText;
	}
	public void setNoteText(String noteText)
	{
		this.noteText = noteText;
	}

	public String getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
