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
import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.user.OID.Authorizable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */

@Entity

@Table(name="TAB_RESPONSE")
public  class FormResponseMaster extends AbstractEntity implements Serializable
{
	private int		responseId;

	private int unitPk;
	private int workstationPk;
	private int		testProcPk;
	private int		surveyPk;
	private int 	userPk;
	private Integer		verifiedBy;
	private Date	verifiedDate;
	private String 	verifyComment;
	private Integer 	approvedBy;
	private Date	approvedDate;
	private String	approveComment;
	private String	responseMode;
	private Date	responseStartTime;
	private Date	responseCompleteTime;
	private Date	responseSyncTime;
	private Long	flagPk;
	private String 	ipaddress;
	private String	responseRefNo;
	private int percentComplete;
	private int noOfComments;
	private int totalQCount;
	private int totalACount;
	private int passCount;
	private int failCount;
	private int dimentionalFailCount;
	private int naCount;
	private String status;
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

	@Deprecated
	public int getUnitPk() {
		return unitPk;
	}

	@Deprecated
	public void setUnitPk(int unitPk) {
		this.unitPk = unitPk;
	}

	@Deprecated
	public int getWorkstationPk() {
		return workstationPk;
	}

	@Deprecated
	public void setWorkstationPk(int workstationPk) {
		this.workstationPk = workstationPk;
	}

	public int getTestProcPk() {
		return testProcPk;
	}

	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}

	public int getSurveyPk() {
		return surveyPk;
	}

	public void setSurveyPk(int surveyPk) {
		this.surveyPk = surveyPk;
	}

	public int getUserPk() {
		return userPk;
	}

	public void setUserPk(int userPk) {
		this.userPk = userPk;
	}

	public Integer getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(Integer verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getVerifyComment() {
		return verifyComment;
	}

	public void setVerifyComment(String verifyComment) {
		this.verifyComment = verifyComment;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApproveComment() {
		return approveComment;
	}

	public void setApproveComment(String approveComment) {
		this.approveComment = approveComment;
	}

	public String getResponseMode() {
		return responseMode;
	}

	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}

	public Date getResponseStartTime() {
		return responseStartTime;
	}

	public void setResponseStartTime(Date responseStartTime) {
		this.responseStartTime = responseStartTime;
	}

	public Date getResponseCompleteTime() {
		return responseCompleteTime;
	}

	public void setResponseCompleteTime(Date responseCompleteTime) {
		this.responseCompleteTime = responseCompleteTime;
	}

	public Date getResponseSyncTime() {
		return responseSyncTime;
	}

	public void setResponseSyncTime(Date responseSyncTime) {
		this.responseSyncTime = responseSyncTime;
	}

	public Long getFlagPk() {
		return flagPk;
	}

	public void setFlagPk(Long flagPk) {
		this.flagPk = flagPk;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getResponseRefNo() {
		return responseRefNo;
	}

	public void setResponseRefNo(String responseRefNo) {
		this.responseRefNo = responseRefNo;
	}

	public int getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(int percentComplete) {
		this.percentComplete = percentComplete;
	}

	public int getNoOfComments() {
		return noOfComments;
	}

	public void setNoOfComments(int noOfComments) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
	
	public FormResponseOID getOID()
	{
		return new FormResponseOID(responseId);
	}

	@Override
	public long getPk() {
		return 0;
	}
}
