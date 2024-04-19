package com.tathvatech.forms.request;


import com.tathvatech.forms.response.BaseResponseBean;
import com.tathvatech.forms.response.SectionResponseBean;

public class SectionLockUnlockRequestBean extends BaseResponseBean {
	String deviceId;
	String sessionKey;
	int testProcPk;
	int responseId;
	SectionResponseBean[] sectionsToProcess;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
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
	public SectionResponseBean[] getSectionsToProcess() {
		return sectionsToProcess;
	}
	public void setSectionsToProcess(SectionResponseBean[] sectionsToProcess) {
		this.sectionsToProcess = sectionsToProcess;
	}
}
