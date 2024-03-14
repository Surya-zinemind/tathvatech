/*
 * Created on Jun 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;



/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */


public class ResponseMasterNew implements Serializable
{
	private int		responseId;
	private String	responseRefNo;

	private int		testProcPk;

	private int		formPk;
	private String	formName;
	private int		versionNo;

	private Date	responseStartTime;
	private Date	responseCompleteTime;
	private Date	responseSyncTime;

	private int	preparedByPk;
	private String	preparedByFirstName;
	private String	preparedByLastName;

	private int	verifiedByPk;
	private String	verifiedByFirstName;
	private String	verifiedByLastName;
	private Date	verifiedDate;
	private String verifyComment;

	private int	approvedByPk;
	private String	approvedByFirstName;
	private String	approvedByLastName;
	private Date	approvedDate;
	private String approveComment;

	private int		completionStatus;
	private int		percentComplete;
	private int		noOfComments;
	private int		totalQCount;
	private int 	totalACount;
	private int 	passCount;
	private int 	failCount;
	private int		dimentionalFailCount;
	private int 	naCount;
	

	private String	status;
	private boolean current;
	
	private Date lastUpdated;

	public int getResponseId()
	{
		return responseId;
	}

	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}

	public String getResponseRefNo()
	{
		return responseRefNo;
	}

	public void setResponseRefNo(String responseRefNo)
	{
		this.responseRefNo = responseRefNo;
	}

	public int getTestProcPk() {
		return testProcPk;
	}

	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}

	public int getFormPk()
	{
		return formPk;
	}

	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}

	public String getFormName()
	{
		return formName;
	}

	public void setFormName(String formName)
	{
		this.formName = formName;
	}

	public int getVersionNo()
	{
		return versionNo;
	}

	public void setVersionNo(int versionNo)
	{
		this.versionNo = versionNo;
	}

	public Date getResponseStartTime()
	{
		return responseStartTime;
	}

	public void setResponseStartTime(Date responseTime)
	{
		this.responseStartTime = responseTime;
	}

	public Date getResponseCompleteTime()
	{
		return responseCompleteTime;
	}

	public void setResponseCompleteTime(Date responseCompleteTime)
	{
		this.responseCompleteTime = responseCompleteTime;
	}

	public Date getResponseSyncTime()
	{
		return responseSyncTime;
	}

	public void setResponseSyncTime(Date responseSyncTime)
	{
		this.responseSyncTime = responseSyncTime;
	}

	public int getPreparedByPk()
	{
		return preparedByPk;
	}

	public void setPreparedByPk(int preparedByPk)
	{
		this.preparedByPk = preparedByPk;
	}

	public String getPreparedByFirstName()
	{
		return preparedByFirstName;
	}

	public void setPreparedByFirstName(String preparedByFirstName)
	{
		this.preparedByFirstName = preparedByFirstName;
	}

	public String getPreparedByLastName()
	{
		return preparedByLastName;
	}

	public void setPreparedByLastName(String preparedByLastName)
	{
		this.preparedByLastName = preparedByLastName;
	}

	public int getVerifiedByPk()
	{
		return verifiedByPk;
	}

	public void setVerifiedByPk(int verifiedByPk)
	{
		this.verifiedByPk = verifiedByPk;
	}

	public String getVerifiedByFirstName()
	{
		return verifiedByFirstName;
	}

	public void setVerifiedByFirstName(String verifiedByFirstName)
	{
		this.verifiedByFirstName = verifiedByFirstName;
	}

	public String getVerifiedByLastName()
	{
		return verifiedByLastName;
	}

	public void setVerifiedByLastName(String verifiedByLastName)
	{
		this.verifiedByLastName = verifiedByLastName;
	}

	public Date getVerifiedDate()
	{
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate)
	{
		this.verifiedDate = verifiedDate;
	}

	public int getApprovedByPk()
	{
		return approvedByPk;
	}

	public String getVerifyComment()
	{
		return verifyComment;
	}

	public void setVerifyComment(String verifyComment)
	{
		this.verifyComment = verifyComment;
	}

	public void setApprovedByPk(int approvedByPk)
	{
		this.approvedByPk = approvedByPk;
	}

	public String getApprovedByFirstName()
	{
		return approvedByFirstName;
	}

	public void setApprovedByFirstName(String approvedByFirstName)
	{
		this.approvedByFirstName = approvedByFirstName;
	}

	public String getApprovedByLastName()
	{
		return approvedByLastName;
	}

	public void setApprovedByLastName(String approvedByLastName)
	{
		this.approvedByLastName = approvedByLastName;
	}

	public Date getApprovedDate()
	{
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate)
	{
		this.approvedDate = approvedDate;
	}

	public String getApproveComment()
	{
		return approveComment;
	}

	public void setApproveComment(String approveComment)
	{
		this.approveComment = approveComment;
	}

	public int getCompletionStatus()
	{
		return completionStatus;
	}

	public void setCompletionStatus(int completionStatus)
	{
		this.completionStatus = completionStatus;
	}

	public int getPercentComplete()
	{
		return percentComplete;
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public boolean getCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	//fix it later
	@JsonIgnore
	/*public FormResponseOID getOID()
	{
		return new FormResponseOID(responseId);
	}*/

	public static String	fetchQuery		
						= "select res.responseId, res.responseRefNo, "
									+ "res.surveyPk as formPk, res.testProcPk as testProcPk, "
									+ "form.identityNumber as formName, form.versionNo as versionNo, "
									+ "res.noOfComments as noOfComments, res.percentComplete as percentComplete, "
									+ "res.totalQCount, res.totalACount, res.passCount, res.failCount, res.dimentionalFailCount, res.naCount, "
									+ "responseStartTime, responseCompleteTime, responseSyncTime, res.lastUpdated, "
									+ "res.userPk as preparedByPk, prepUser.firstName as preparedByFirstName, res.status, res.current, "
									+ "prepUser.lastName as preparedByLastName, res.verifiedBy as verifiedByPk, verifyUser.firstName as verifiedByFirstName,"
									+ "verifyUser.lastName as verifiedByLastName, res.approvedBy as approvedByPk, apprUser.firstName as approvedByFirstName,"
									+ "apprUser.lastName as approvedByLastName, res.verifiedDate, res.verifyComment, res.approvedDate, res.approveComment from "
									+ "TAB_RESPONSE res "
									+ "left join TAB_USER prepUser on res.userPk = prepUser.pk "
									+ "left join TAB_USER verifyUser on res.verifiedBy = verifyUser.pk "
									+ "left join TAB_USER apprUser on res.approvedBy = apprUser.pk, "
									+ "TAB_SURVEY form where "
									+ "res.surveyPk = form.pk ";

	public static String	STATUS_NOTSTARTED	= "Not Started";
	public static String	STATUS_INPROGRESS	= "In Progress";
	public static String	STATUS_PAUSED		= "Paused";
	public static String	STATUS_COMPLETE		= "Complete";
	public static String	STATUS_VERIFIED		= "Verified";
	public static String	STATUS_APPROVED		= "Approved";
	public static String	STATUS_REJECTED		= "Rejected";
	public static String	STATUS_APPROVED_WITH_COMMENTS		= "Approved with comments";
	
	public static String ATTACHMENTCONTEXT_COVERSHEET = "Coversheeet";
	
}
