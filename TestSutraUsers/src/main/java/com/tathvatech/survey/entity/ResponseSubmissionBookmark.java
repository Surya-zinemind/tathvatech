/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.survey.oid.ResponseSubmissionBookmarkOID;
import com.tathvatech.user.OID.Authorizable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;



/**
 * @author Hari
 *
 * This class stores the information related to a response submission by a user.
 * User can do an interim submission and this needs to be printed as a PDF and saved
 * as a histoty record for future reference. 
 * 
 * The actual PDF file is saved in the filesystem and an attachment entry is created.
 * The attachment thus created is associated with this record
 */
@Entity
@Table(name = "RESPONSE_SUBMISSION_BOOKMARK")
public class ResponseSubmissionBookmark extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int testProcFk;
	private int responseId;
	private String submissionType;
	private String revisionNo;
	private String comment;
	private String responseJSON;
	private int estatus;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;

	/**
     * 
     */
    public ResponseSubmissionBookmark()
    {
        super();
    }

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getTestProcFk()
	{
		return testProcFk;
	}

	public void setTestProcFk(int testProcFk)
	{
		this.testProcFk = testProcFk;
	}

	public int getResponseId()
	{
		return responseId;
	}

	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}

	public String getSubmissionType()
	{
		return submissionType;
	}

	public void setSubmissionType(String submissionType)
	{
		this.submissionType = submissionType;
	}

	public String getRevisionNo()
	{
		return revisionNo;
	}

	public void setRevisionNo(String revisionNo)
	{
		this.revisionNo = revisionNo;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getResponseJSON()
	{
		return responseJSON;
	}

	public void setResponseJSON(String responseJSON)
	{
		this.responseJSON = responseJSON;
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public ResponseSubmissionBookmarkOID getOID()
	{
		return new ResponseSubmissionBookmarkOID((int) pk, revisionNo);
	}
    
	public enum SubmissionTypeEnum{Interim, Final};
}
