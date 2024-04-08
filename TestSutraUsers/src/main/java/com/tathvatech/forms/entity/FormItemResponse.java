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
import com.tathvatech.user.OID.FormItemResponseOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

@Entity
@Table(name="TAB_ITEM_RESPONSE")
public class FormItemResponse extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int		responseId;
	private String	questionId;
	private Integer updatedBy;
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

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public FormItemResponseOID getOID()
	{
		return new FormItemResponseOID((int) pk, "");
	}
	
	public static String COMMENT_CONTEXT_NOT_TO_PRINT = "No Prints";
	public static String COMMENT_CONTEXT_TO_PRINT = "To Print"; // print in the comments columns when printing the sheets
}
