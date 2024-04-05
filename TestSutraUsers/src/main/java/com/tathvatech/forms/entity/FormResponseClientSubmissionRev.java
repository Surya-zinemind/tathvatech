/*
 * Created on Jun 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.Authorizable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 * 
 */

@Entity
@Table(name="RESPONSE_CLIENT_SUBMISSION_REV")
public class FormResponseClientSubmissionRev extends AbstractEntity implements Serializable, Authorizable
{
	@Id
	private long pk;
	private int	responseId;
	private String submissionRevision;
	private Date createdDate;
	private int createdBy;
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getResponseId()
	{
		return responseId;
	}

	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}

	public String getSubmissionRevision()
	{
		return submissionRevision;
	}

	public void setSubmissionRevision(String submissionRevision)
	{
		this.submissionRevision = submissionRevision;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getEffectiveDateFrom()
	{
		return effectiveDateFrom;
	}

	public void setEffectiveDateFrom(Date effectiveDateFrom)
	{
		this.effectiveDateFrom = effectiveDateFrom;
	}

	public Date getEffectiveDateTo()
	{
		return effectiveDateTo;
	}

	public void setEffectiveDateTo(Date effectiveDateTo)
	{
		this.effectiveDateTo = effectiveDateTo;
	}


	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
