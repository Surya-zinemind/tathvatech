/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.response;

import com.tathvatech.forms.enums.FormItemTypeEnum;
import com.tathvatech.forms.response.FormItemResponseBase;
import com.tathvatech.survey.enums.SectionLockStatusEnum;

import java.util.Date;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SectionResponseBean extends FormItemResponseBase
{
	private int pk;
	private int responseId;
	private int workorderFk;
	private String name;
	private String description;
	private int submittedBy;
	private String submittedByFirstName;
	private String submittedByLastName;
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
	
	SectionLockStatusEnum lockStatus;
	int lockedByUserPk;
	String lockedByUserDisplayName;
	
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
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

	public int getWorkorderFk()
	{
		return workorderFk;
	}

	public void setWorkorderFk(int workorderFk)
	{
		this.workorderFk = workorderFk;
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

	public int getSubmittedBy()
	{
		return submittedBy;
	}

	public void setSubmittedBy(int submittedBy)
	{
		this.submittedBy = submittedBy;
	}

	public String getSubmittedByFirstName()
	{
		return submittedByFirstName;
	}

	public void setSubmittedByFirstName(String submittedByFirstName)
	{
		this.submittedByFirstName = submittedByFirstName;
	}

	public String getSubmittedByLastName()
	{
		return submittedByLastName;
	}

	public void setSubmittedByLastName(String submittedByLastName)
	{
		this.submittedByLastName = submittedByLastName;
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

	public SectionLockStatusEnum getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(SectionLockStatusEnum lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getLockedByUserPk() {
		return lockedByUserPk;
	}

	public void setLockedByUserPk(int lockedByUserPk) {
		this.lockedByUserPk = lockedByUserPk;
	}

	public String getLockedByUserDisplayName() {
		return lockedByUserDisplayName;
	}

	public void setLockedByUserDisplayName(String lockedByUserDisplayName) {
		this.lockedByUserDisplayName = lockedByUserDisplayName;
	}

	@Override
	public FormItemTypeEnum getFormItemType() {
		return FormItemTypeEnum.Section;
	}
}
