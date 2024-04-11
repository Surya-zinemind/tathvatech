/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import java.util.Date;

import com.tathvatech.forms.oid.TestProcSectionOID;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestProcSectionListReportResultRow
{
	private int pk;
	private String sectionName;
	private String sectionDescription;
	private int testProcPk;
	private String testName;
	private int projectTestProcPk;
	private int unitPk;
	private String unitName;
	private String unitDescription;
	private String unitSerialNumber;
	private int partPk;
	private String partName;
	private String partType;
	private int projectPartPk;
	private String projectPartName;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int workstationPk;
	private String workstationName;
	private String workstationDescription;
	private String workstationStatus;
	private String workstationTimezoneId;
	private int formMainPk;
	private int formPk;
	private String formName;
	private String formDescription;
	private String formResponsibleDivision;
	private String formRevision;
	private int formVersion;

	
	private int		responseId;
	private String	responseRefNo;
	private Date	responseStartTime;
	private Date	responseCompleteTime;
	private Date	responseSyncTime;

	private int		percentComplete;
	private int		noOfComments;
	private int		totalQCount;
	private int 	totalACount;
	private int 	passCount;
	private int 	failCount;
	private int		dimentionalFailCount;
	private int 	naCount;

	private String	responseStatus;

	private int 	secResponsePk;
	private int		secPercentComplete;
	private int		secNoOfComments;
	private int		secTotalQCount;
	private int 	secTotalACount;
	private int 	secPassCount;
	private int 	secFailCount;
	private int		secDimentionalFailCount;
	private int 	secNaCount;
	private Date 	actualStartDate;
	private Date	actualCompletionDate;

	private Date forecastStartDate;
	private Date forecastEndDate;
	private Float forecastHours;
	private String forecastComment;
	private Integer forecastCreatedBy;
	private Date forecastCreatedDate;
	private String forecastCreatedByFirstName;
	private String forecastCreatedByLastName;	
		
	
	public String getDisplayDescriptor()
	{
		return sectionName + " - " + sectionDescription;
	}
	
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public String getSectionName()
	{
		return sectionName;
	}

	public void setSectionName(String sectionName)
	{
		this.sectionName = sectionName;
	}

	public String getSectionDescription()
	{
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription)
	{
		this.sectionDescription = sectionDescription;
	}

	public int getTestProcPk()
	{
		return testProcPk;
	}

	public void setTestProcPk(int testProcPk)
	{
		this.testProcPk = testProcPk;
	}

	public String getTestName()
	{
		return testName;
	}

	public void setTestName(String testName)
	{
		this.testName = testName;
	}

	public int getSecResponsePk()
	{
		return secResponsePk;
	}

	public void setSecResponsePk(int secResponsePk)
	{
		this.secResponsePk = secResponsePk;
	}

	public int getSecPercentComplete()
	{
		return secPercentComplete;
	}

	public void setSecPercentComplete(int secPercentComplete)
	{
		this.secPercentComplete = secPercentComplete;
	}

	public int getSecNoOfComments()
	{
		return secNoOfComments;
	}

	public void setSecNoOfComments(int secNoOfComments)
	{
		this.secNoOfComments = secNoOfComments;
	}

	public int getSecTotalQCount()
	{
		return secTotalQCount;
	}

	public void setSecTotalQCount(int secTotalQCount)
	{
		this.secTotalQCount = secTotalQCount;
	}

	public int getSecTotalACount()
	{
		return secTotalACount;
	}

	public void setSecTotalACount(int secTotalACount)
	{
		this.secTotalACount = secTotalACount;
	}

	public int getSecPassCount()
	{
		return secPassCount;
	}

	public void setSecPassCount(int secPassCount)
	{
		this.secPassCount = secPassCount;
	}

	public int getSecFailCount()
	{
		return secFailCount;
	}

	public void setSecFailCount(int secFailCount)
	{
		this.secFailCount = secFailCount;
	}

	public int getSecDimentionalFailCount()
	{
		return secDimentionalFailCount;
	}

	public void setSecDimentionalFailCount(int secDimentionalFailCount)
	{
		this.secDimentionalFailCount = secDimentionalFailCount;
	}

	public int getSecNaCount()
	{
		return secNaCount;
	}

	public void setSecNaCount(int secNaCount)
	{
		this.secNaCount = secNaCount;
	}

	public Date getActualStartDate()
	{
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate)
	{
		this.actualStartDate = actualStartDate;
	}

	public Date getActualCompletionDate()
	{
		return actualCompletionDate;
	}

	public void setActualCompletionDate(Date actualCompletionDate)
	{
		this.actualCompletionDate = actualCompletionDate;
	}

	public int getProjectTestProcPk() {
		return projectTestProcPk;
	}
	public void setProjectTestProcPk(int projectTestProcPk) {
		this.projectTestProcPk = projectTestProcPk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}
	public int getProjectPk() {
		return projectPk;
	}
	public void setProjectPk(int projectPk) {
		this.projectPk = projectPk;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDescription()
	{
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription = projectDescription;
	}

	public int getWorkstationPk() 
	{
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) 
	{
		this.workstationPk = workstationPk;
	}
	public int getFormMainPk() {
		return formMainPk;
	}

	public void setFormMainPk(int formMainPk) {
		this.formMainPk = formMainPk;
	}
	public int getFormPk()
	{
		return formPk;
	}
	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitDescription()
	{
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
	}

	public String getUnitSerialNumber()
	{
		return unitSerialNumber;
	}

	public void setUnitSerialNumber(String unitSerialNumber)
	{
		this.unitSerialNumber = unitSerialNumber;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartType() {
		return partType;
	}
	public void setPartType(String partType) {
		this.partType = partType;
	}
	public int getProjectPartPk() {
		return projectPartPk;
	}
	public void setProjectPartPk(int projectPartPk) {
		this.projectPartPk = projectPartPk;
	}
	public String getProjectPartName() {
		return projectPartName;
	}
	public void setProjectPartName(String projectPartName) {
		this.projectPartName = projectPartName;
	}
	public String getWorkstationName() {
		return workstationName;
	}
	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}
	public String getWorkstationDescription()
	{
		return workstationDescription;
	}
	public void setWorkstationDescription(String workstationDescription)
	{
		this.workstationDescription = workstationDescription;
	}
	public String getWorkstationStatus()
	{
		return workstationStatus;
	}
	public void setWorkstationStatus(String workstationStatus)
	{
		this.workstationStatus = workstationStatus;
	}
	public String getWorkstationTimezoneId() {
		return workstationTimezoneId;
	}

	public void setWorkstationTimezoneId(String workstationTimezoneId) {
		this.workstationTimezoneId = workstationTimezoneId;
	}

	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public String getFormResponsibleDivision() {
		return formResponsibleDivision;
	}
	public void setFormResponsibleDivision(String formResponsibleDivision) {
		this.formResponsibleDivision = formResponsibleDivision;
	}
	public String getFormRevision() {
		return formRevision;
	}
	public void setFormRevision(String formRevision) {
		this.formRevision = formRevision;
	}
	public int getFormVersion() {
		return formVersion;
	}
	public void setFormVersion(int formVersion) {
		this.formVersion = formVersion;
	}

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

	public Date getResponseStartTime()
	{
		return responseStartTime;
	}

	public void setResponseStartTime(Date responseStartTime)
	{
		this.responseStartTime = responseStartTime;
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

	public int getTotalQCount()
	{
		return totalQCount;
	}

	public void setTotalQCount(int totalQCount)
	{
		this.totalQCount = totalQCount;
	}

	public int getTotalACount()
	{
		return totalACount;
	}

	public void setTotalACount(int totalACount)
	{
		this.totalACount = totalACount;
	}

	public int getPassCount()
	{
		return passCount;
	}

	public void setPassCount(int passCount)
	{
		this.passCount = passCount;
	}

	public int getFailCount()
	{
		return failCount;
	}

	public void setFailCount(int failCount)
	{
		this.failCount = failCount;
	}

	public int getDimentionalFailCount()
	{
		return dimentionalFailCount;
	}

	public void setDimentionalFailCount(int dimentionalFailCount)
	{
		this.dimentionalFailCount = dimentionalFailCount;
	}

	public int getNaCount()
	{
		return naCount;
	}

	public void setNaCount(int naCount)
	{
		this.naCount = naCount;
	}

	public String getResponseStatus()
	{
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	public Date getForecastStartDate() {
		return forecastStartDate;
	}

	public void setForecastStartDate(Date forecastStartDate) {
		this.forecastStartDate = forecastStartDate;
	}

	public Date getForecastEndDate() {
		return forecastEndDate;
	}

	public void setForecastEndDate(Date forecastEndDate) {
		this.forecastEndDate = forecastEndDate;
	}


	public Float getForecastHours() {
		return forecastHours;
	}

	public void setForecastHours(Float forecastHours) {
		this.forecastHours = forecastHours;
	}

	public String getForecastComment() {
		return forecastComment;
	}

	public void setForecastComment(String forecastComment) {
		this.forecastComment = forecastComment;
	}

	public Integer getForecastCreatedBy() {
		return forecastCreatedBy;
	}

	public void setForecastCreatedBy(Integer forecastCreatedBy) {
		this.forecastCreatedBy = forecastCreatedBy;
	}

	public Date getForecastCreatedDate() {
		return forecastCreatedDate;
	}

	public void setForecastCreatedDate(Date forecastCreatedDate) {
		this.forecastCreatedDate = forecastCreatedDate;
	}

	public String getForecastCreatedByFirstName() {
		return forecastCreatedByFirstName;
	}

	public void setForecastCreatedByFirstName(String forecastCreatedByFirstName) {
		this.forecastCreatedByFirstName = forecastCreatedByFirstName;
	}

	public String getForecastCreatedByLastName() {
		return forecastCreatedByLastName;
	}

	public void setForecastCreatedByLastName(String forecastCreatedByLastName) {
		this.forecastCreatedByLastName = forecastCreatedByLastName;
	}

	public TestProcSectionOID getOID() {
		return new TestProcSectionOID(pk, "");
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)return false;
		if(obj instanceof TestProcSectionListReportResultRow)
			return this.pk == ((TestProcSectionListReportResultRow)obj).getPk();
		
		return false;			
	}

	@Override
	public int hashCode() 
	{
		return pk;
	}

}

