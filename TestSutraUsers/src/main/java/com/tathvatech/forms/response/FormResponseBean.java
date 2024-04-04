package com.tathvatech.forms.response;

import java.util.Date;
import java.util.List;

import com.sarvasutra.etest.api.BaseResponseBean;
import com.sarvasutra.etest.util.ListStringUtil;
import com.tathvatech.ts.core.survey.response.FormResponseStats;

public class FormResponseBean extends BaseResponseBean{
	public static enum ResponseSubmitMode{saveItems, saveSections, saveAndSubmit;};
	
	int testProcPk;
	int responseId;
	int formPk;
	ResponseSubmitMode submitMode;
	Date responseStartTime;
	Date responseEndTime;
	Date responseSyncTime;
	int submittedByUserPk;
	String submittedByUserDisplayName;
	String responseRefNo;
	FormResponseStats responseStats;
	
	List<FormItemResponseBase> formItemResponses;
	
	List<AttachmentInfoBean> attachments;
	
	long objectVersionKey;
	
	public int getTestProcPk() {
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}
	public int getResponseId() {
		return responseId;
	}
	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}
	public int getFormPk() {
		return formPk;
	}
	public void setFormPk(int formPk) {
		this.formPk = formPk;
	}
	public ResponseSubmitMode getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(ResponseSubmitMode submitMode) {
		this.submitMode = submitMode;
	}
	public Date getResponseStartTime() {
		return responseStartTime;
	}
	public void setResponseStartTime(Date responseStartTime) {
		this.responseStartTime = responseStartTime;
	}
	public Date getResponseEndTime() {
		return responseEndTime;
	}
	public void setResponseEndTime(Date responseEndTime) {
		this.responseEndTime = responseEndTime;
	}
	public Date getResponseSyncTime() {
		return responseSyncTime;
	}
	public void setResponseSyncTime(Date responseSyncTime) {
		this.responseSyncTime = responseSyncTime;
	}
	public int getSubmittedByUserPk() {
		return submittedByUserPk;
	}
	public void setSubmittedByUserPk(int submittedByUserPk) {
		this.submittedByUserPk = submittedByUserPk;
	}
	public String getSubmittedByUserDisplayName() {
		return submittedByUserDisplayName;
	}
	public void setSubmittedByUserDisplayName(String submittedByUserDisplayName) {
		this.submittedByUserDisplayName = submittedByUserDisplayName;
	}
	public String getResponseRefNo() {
		return responseRefNo;
	}
	public void setResponseRefNo(String responseRefNo) {
		this.responseRefNo = responseRefNo;
	}
	public FormResponseStats getResponseStats() {
		return responseStats;
	}
	public void setResponseStats(FormResponseStats responseStats) {
		this.responseStats = responseStats;
	}
	public List<FormItemResponseBase> getFormItemResponses() {
		return formItemResponses;
	}
	public void setFormItemResponses(List<FormItemResponseBase> formItemResponses) {
		this.formItemResponses = formItemResponses;
	}
	
	public List<AttachmentInfoBean> getAttachments()
	{
		return attachments;
	}
	public void setAttachments(List<AttachmentInfoBean> attachments)
	{
		this.attachments = attachments;
	}
	
	public long getObjectVersionKey()
	{
		return objectVersionKey;
	}
	public void setObjectVersionKey(long objectVersionKey)
	{
		this.objectVersionKey = objectVersionKey;
	}
	@Override
	public String toString()
	{

		return "testProcPk:" + ListStringUtil.showString(testProcPk) + "; responseId:" + ListStringUtil.showString(responseId)
				+ "; formPk:" + ListStringUtil.showString(formPk) + "; submitMode:"
				+ ListStringUtil.showString(submitMode) + "; responseStartTime:" + ListStringUtil.showString(responseStartTime)
				+ "; responseEndTime:" + ListStringUtil.showString(responseEndTime) + "; responseSyncTime:"
				+ ListStringUtil.showString(responseSyncTime) + "; submittedByUserPk:" + ListStringUtil.showString(submittedByUserPk)
				+ "; submittedByUserDisplayName:" + ListStringUtil.showString(submittedByUserDisplayName) + "; responseRefNo:"
				+ ListStringUtil.showString(responseRefNo) + "; responseStats:"
				+ ListStringUtil.showString(responseStats) ;

	

	}
}
