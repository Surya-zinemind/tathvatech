/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class SectionResponseQuery
{
	private int pk;
	private int responseId;
	private int formSectionFk;
	private int testProcSectionFk;
	private int workorderFk;
	private String sectionId;
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
	
	private Date lastUpdated;
	
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

	public int getFormSectionFk()
	{
		return formSectionFk;
	}

	public void setFormSectionFk(int formSectionFk)
	{
		this.formSectionFk = formSectionFk;
	}

	public int getTestProcSectionFk()
	{
		return testProcSectionFk;
	}

	public void setTestProcSectionFk(int testProcSectionFk)
	{
		this.testProcSectionFk = testProcSectionFk;
	}

	public int getWorkorderFk()
	{
		return workorderFk;
	}

	public void setWorkorderFk(int workorderFk)
	{
		this.workorderFk = workorderFk;
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

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	
	public static String SQL = "select secResp.pk, resp.responseId, formSec.pk as formSectionFk, " 
	+ " tfs.pk as testProcSectionFk, wo.pk as workorderFk, " 
	+ " formSec.sectionId, formSecMain.itemNo as name, formSecMain.description, " 
	+ " secResp.percentComplete,  "
	+ " secResp. noOfComments, secResp.totalQCount, secResp.totalACount, secResp.passCount, secResp.failCount, " 
	+ " secResp.dimentionalFailCount, secResp.naCount, secResp.submittedBy, secResp.startDate, secResp.completionDate,  " 
	+ " u.firstName as submittedByFirstName, u.lastName as submittedByLastName, secResp.lastUpdated  "
	+ " from  "
	+ " testproc_form_section tfs "
	+ " join form_section formSec on tfs.formSectionFk = formSec.pk "
	+ " join form_section_main formSecMain on formSec.formSectionMainPk = formSecMain.pk "
	+ " join testproc_form_assign tfa on tfs.testProcFormAssignFk = tfa.pk and tfa.current = 1 "
	+ " join unit_testproc ut on tfa.testProcFk = ut.pk "
	+ " left join workorder wo on wo.entityPk = tfs.pk and wo.entityType = 36 "
	+ " left join tab_response resp on resp.testprocPk = tfa.testProcFk and resp.surveyPk = tfa.formFk and resp.current = 1 "
	+ " left join tab_section_response secResp on secResp.responseId = resp.responseId and secResp.formSectionFk = formSec.pk "
	+ " left join TAB_USER u on secResp.submittedBy = u.pk ";	
	
	
	@Override
	/**
	 * it is assumed that you are checking against the appropriate responseId. only the section Id is matched
	 */
	public boolean equals(Object inSectionResponse)
	{
		//TODO this equals check will be a problem when we have to deal with multiple revisions of a form..
		//we cannot check the equals based on sectionId alone.
		if(!(inSectionResponse instanceof SectionResponseQuery))
		{
			return false;
		}
		
		if(sectionId != null && sectionId.equals(((SectionResponseQuery)inSectionResponse).getSectionId()))
		{
			return true;
		}
		return false;
	}
	
	
}
