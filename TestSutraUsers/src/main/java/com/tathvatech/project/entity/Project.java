package com.tathvatech.project.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Authorizable;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.enums.ProjectRolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_PROJECT")
public class Project extends AbstractEntity implements Serializable, Authorizable
{
	@Id
	private long pk;
	private int accountPk;
	private String projectName;
	private String projectDescription;
	private String projectRefNo;
	private String customerName;
	private String projectRefNoCustomer;
	private String contractNo;
	private String copyrightNotice;
	private Integer managerPk;
	private int createdBy;
	private Date createdDate;
	private Boolean disableDailySummaryEmails;
	private Boolean disableNotifications;
	private Boolean enableChecksheets;
	private String status;
	private Date lastUpdated;
	
	
	@Id
	public long getPk()
	{
		return pk;
	}

	public void setPk(long pk)
	{
		this.pk = pk;
	}

	public int getAccountPk()
	{
		return accountPk;
	}

	public void setAccountPk(int accountPk)
	{
		this.accountPk = accountPk;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		if (projectName != null){
			this.projectName = projectName.trim();
		}
	}

	public String getProjectDescription()
	{
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription = projectDescription;
	}

	public String getProjectRefNo() {
		return projectRefNo;
	}

	public void setProjectRefNo(String projectRefNo) {
		this.projectRefNo = projectRefNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProjectRefNoCustomer() {
		return projectRefNoCustomer;
	}

	public void setProjectRefNoCustomer(String projectRefNoCustomer) {
		this.projectRefNoCustomer = projectRefNoCustomer;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getCopyrightNotice() {
		return copyrightNotice;
	}

	public void setCopyrightNotice(String copyrightNotice) {
		this.copyrightNotice = copyrightNotice;
	}

	public Integer getManagerPk()
	{
		return managerPk;
	}

	public void setManagerPk(Integer managerPk)
	{
		this.managerPk = managerPk;
	}

	public int getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
	public Boolean getDisableDailySummaryEmails() {
		return disableDailySummaryEmails;
	}

	public void setDisableDailySummaryEmails(Boolean disableDailySummaryEmails) {
		this.disableDailySummaryEmails = disableDailySummaryEmails;
	}

	public Boolean getDisableNotifications() {
		return disableNotifications;
	}

	public void setDisableNotifications(Boolean disableNotifications) {
		this.disableNotifications = disableNotifications;
	}

	public Boolean getEnableChecksheets()
	{
		return enableChecksheets;
	}

	public void setEnableChecksheets(Boolean enableChecksheets)
	{
		this.enableChecksheets = enableChecksheets;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	

	@Override
	public int hashCode() 
	{
		if (pk == 0)
			return super.hashCode();
		return (int)pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(pk == 0 || obj == null)
			return super.equals(obj);
		return pk == ((Project)obj).getPk();
	}

	@Override
	public String toString()
	{
		return projectName;
	}

	/**
     * 
     */
    public Project()
    {
    }
	public ProjectOID getOID()
	{
		return new ProjectOID((int) pk, projectName + ((projectDescription != null)? (" - " + projectDescription):""));
	}

	public static String STATUS_OPEN = "Open";
	public static final String STATUS_CLOSED = "Closed";

	public static final String AttachmentContext_CustomerLogo = "custLogo";
	public static final String AttachmentContext_LogbookPreface = "logbookPreface";
	public static final String AttachmentContext_PartDrawings = "PartDrawing";
	public static final String AttachmentContext_WorkstationLayout = "WorkstationLayout";

	public static final String AllowClientSubmissionOnFormsPropertyKey = "AllowClientSubmissionOnForms";
	public static String EnableAddAttachmentInFormResponseMode = "EnableAddAttachmentInFormResponseMode"; // Default is false - Allow attachments to be added while filling up the forms.
	public static String DisableWorkstationCloseWhenAllFormsApproved = "DisableWorkstationCloseWhenAllFormsApproved"; // Default is true - When all forms are approved on a workstation, ask if the workstation could be marked as completed

	public static String AllowApproveWithCommentsForChecksheets = "AllowApproveWithCommentsForChecksheets"; // If enabled, approver can Approve or Approve with comments the checksheet after verification

	public static String EnableUnitCreationAtWorkstation = "EnableUnitCreationAtWorkstation";  // if Enabled, you can create a unit at a specific workstation. no workstation level settings will be applied to the unit. only part level settings are applied to this unit.


	public static String ProjectType = "ProjectType"; // the value can be one of the ProjectTypeEnums
	public static enum ProjectTypeEnum{RollingStock, Signalling}

	@Override
	public EntityType getEntityType()
	{
		return EntityTypeEnum.Project;
	}

	@Override
	public String getDisplayText()
	{
		return projectName + " - " + getProjectDescription();
	}

	@Override
	public List<? extends Role> getSupportedRoles()
	{
		return Arrays.asList(ProjectRolesEnum.values());
	}

	@Override
	public List<? extends Action> getSupportedActions()
	{
		return new ArrayList<>();
	};
}
