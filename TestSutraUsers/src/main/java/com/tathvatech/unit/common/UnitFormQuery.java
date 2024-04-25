/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.common;

import java.util.Date;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.workstation.common.DummyWorkstation;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Component
public class UnitFormQuery
{
	private int pk;
	private String name;
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
	private String workstationSiteName;
	private String workstationTimezoneId;
	private int formMainPk;
	private int formPk;
	private String formName;
	private String formDescription;
	private String formResponsibleDivision;
	private String formRevision;
	private int formVersion;
	private int appliedByUserFk;


	
	private int		responseId;
	private String	responseRefNo;
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

	private int		percentComplete;
	private int		noOfComments;
	private int		totalQCount;
	private int 	totalACount;
	private int 	passCount;
	private int 	failCount;
	private int		dimentionalFailCount;
	private int 	naCount;

	private String	responseStatus;

	private Date forecastStartDate;
	private Date forecastEndDate;
	private Float forecastHours;
	private String forecastComment;
	private Integer forecastCreatedBy;
	private Date forecastCreatedDate;
	private String forecastCreatedByFirstName;
	private String forecastCreatedByLastName;

    public UnitFormQuery(DummyWorkstation dummyWorkstation) {
        this.dummyWorkstation = dummyWorkstation;
    }


    public String getDisplayDescriptor()
	{
		if(name != null && name.trim().length() > 0)
		{
			return name + " - " + formName;
		}
		else
		{
			return formName;
		}
	}
	
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getWorkstationSiteName() {
		return workstationSiteName;
	}

	public void setWorkstationSiteName(String workstationSiteName) {
		this.workstationSiteName = workstationSiteName;
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

	public int getAppliedByUserFk() {
		return appliedByUserFk;
	}

	public void setAppliedByUserFk(int appliedByUserFk) {
		this.appliedByUserFk = appliedByUserFk;
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

	public String getVerifyComment()
	{
		return verifyComment;
	}

	public void setVerifyComment(String verifyComment)
	{
		this.verifyComment = verifyComment;
	}

	public int getApprovedByPk()
	{
		return approvedByPk;
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

	public TestProcOID getOID() {
		return new TestProcOID(pk, "");
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)return false;
		if(obj instanceof UnitFormQuery)
			return this.pk == ((UnitFormQuery)obj).getPk();
		
		return false;			
	}


	@Transient
	private final DummyWorkstation dummyWorkstation;

	@Override
	public int hashCode() 
	{
		return pk;
	}

	private String sql; // Declare the SQL string without initialization

	// Method to initialize the SQL string
	public String getSql() {
		if (sql == null) { // Check if the SQL string is not initialized
			sql = "select ut.pk as pk, uth.name as name, uth.projectTestProcPk as projectTestProcPk, "
					+ " uth.projectPk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription, tfa.appliedByUserFk, "
					+ " part.pk as partPk, part.name as partName, partType.typeName as partType, pp.name as projectPartName, "
					+ " uth.workstationPk as workstationPk, w.workstationName as workstationName, w.description as workstationDescription, "
					+ " case when ul.status is null then '" + UnitLocation.STATUS_WAITING + "' else ul.status end as workstationStatus, site.name as workstationSiteName, site.timeZone as workstationTimezoneId, "
					+ " uth.unitPk as unitPk, uh.unitName as unitName, uprh.projectPartPk as projectPartPk, tfa.formFk as formPk, f.formMainPk as formMainPk, "
					+ " f.identityNumber as formName, f.description as formDescription, "
					+ " f.responsibleDivision as formResponsibleDivision, f.revision as formRevision, "
					+ " f.versionNo as formVersion, "
					+ " res.responseId, res.responseRefNo, "
					+ " res.noOfComments as noOfComments, res.percentComplete as percentComplete, "
					+ " res.totalQCount, res.totalACount, res.passCount, res.failCount, res.dimentionalFailCount, res.naCount, "
					+ " responseStartTime, responseCompleteTime, responseSyncTime, "
					+ " res.userPk as preparedByPk, prepUser.firstName as preparedByFirstName, res.status as responseStatus, "
					+ " prepUser.lastName as preparedByLastName, res.verifiedBy as verifiedByPk, verifyUser.firstName as verifiedByFirstName,"
					+ " verifyUser.lastName as verifiedByLastName, res.approvedBy as approvedByPk, apprUser.firstName as approvedByFirstName,"
					+ " apprUser.lastName as approvedByLastName, res.verifiedDate, res.verifyComment, res.approvedDate, res.approveComment, "
					+ " es.forecastStartDate, es.forecastEndDate, es.estimateHours as forecastHours, es.comment as forecastComment, es.createdBy as forecastCreatedBy, es.createdDate as forecastCreatedDate,"
					+ " esUser.firstName as forecastCreatedByFirstName, esUser.lastName as forecastCreatedByLastName "
					+ " from unit_testproc ut"
					+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
					+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
					+ " join TAB_UNIT u on uth.unitPk = u.pk "
					+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
					+ " join TAB_PROJECT p on uth.projectPk = p.pk  "
					+ " join TAB_WORKSTATION w on uth.workstationPk = w.pk  and w.pk != " + dummyWorkstation.getPk()
					+ " join site on w.sitePk = site.pk"
					+ " join TAB_SURVEY f on tfa.formFk = f.pk "
					+ " join unit_project_ref upr on upr.unitPk = uth.unitPk and upr.projectPk = uth.projectPk "
					+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
					+ " join project_part pp on uprh.projectPartPk = pp.pk "
					+ " join part on pp.partPk = part.pk "
					+ " join part_type partType on pp.partTypePk = partType.pk  "
					+ " left outer join TAB_UNIT_LOCATION ul on uth.unitPk = ul.unitPk and uth.projectPk = ul.projectPk and uth.workstationPk = ul.workstationPk and ul.current = 1"
					+ " left outer join TAB_RESPONSE res on res.testProcPk = ut.pk and res.surveyPk = tfa.formFk and res.current = 1"
					+ " left outer join entity_schedule es on es.objectPk = ut.pk and es.objectType = " + EntityTypeEnum.TestProc.getValue() + " and now() between es.effectiveDateFrom and es.effectiveDateTo "
					+ " left outer join TAB_USER esUser on es.createdBy = esUser.pk "
					+ " left join TAB_USER prepUser on res.userPk = prepUser.pk "
					+ " left join TAB_USER verifyUser on res.verifiedBy = verifyUser.pk "
					+ " left join TAB_USER apprUser on res.approvedBy = apprUser.pk "
					+ " where 1 = 1";
		}
		return sql;
	}
}

