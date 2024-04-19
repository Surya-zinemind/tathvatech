/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.survey.oid.SectionResponseOID;
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
@Table(name="TAB_SECTION_RESPONSE")
public class SectionResponse extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int responseId;
	private int formSectionFk;
	private String sectionId;
	private String name;
	private String description;
	private int submittedBy;
	private Date startDate;
	private Date completionDate;
	private int percentComplete;
	private int noOfComments;

	int totalQCount = 0;
	int totalACount = 0;
	int passCount = 0;
	int failCount = 0;
	int dimentionalFailCount = 0; // where the item is isNumeric
	int naCount = 0;
	
	private Date lastUpdated;



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

	public int getFormSectionFk() {
		return formSectionFk;
	}

	public void setFormSectionFk(int formSectionFk) {
		this.formSectionFk = formSectionFk;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public int getPercentComplete()
	{
		// in legacy the totalQCount is not tracked and will be 0 so get the percentComplete
		// in new percentComplete is not stored but calculated.
		if(totalQCount == 0)
		{
			return percentComplete;
		}
		else
		{
			int percentComplete = 0;
			if(totalQCount > 0)
			{
				percentComplete = totalACount*100/totalQCount; 
			}
			return percentComplete;
		}
	}

	public void setPercentComplete(int percentComplete)
	{
		this.percentComplete = percentComplete;
	}

	public int getNoOfComments()
	{
		return noOfComments;
	}

	public void setNoOfComments(int noOfComments)
	{
		this.noOfComments = noOfComments;
	}

	public int getSubmittedBy()
	{
		return submittedBy;
	}

	public void setSubmittedBy(int submittedBy)
	{
		this.submittedBy = submittedBy;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public int getTotalQCount() {
		return totalQCount;
	}

	public void setTotalQCount(int totalQCount) {
		this.totalQCount = totalQCount;
	}

	public int getTotalACount() {
		return totalACount;
	}

	public void setTotalACount(int totalACount) {
		this.totalACount = totalACount;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getDimentionalFailCount() {
		return dimentionalFailCount;
	}

	public void setDimentionalFailCount(int dimentionalFailCount) {
		this.dimentionalFailCount = dimentionalFailCount;
	}

	public int getNaCount() {
		return naCount;
	}

	public void setNaCount(int naCount) {
		this.naCount = naCount;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public SectionResponseOID getOID()
	{
		return new SectionResponseOID((int) pk);
	}
}
