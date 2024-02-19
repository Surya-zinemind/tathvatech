package com.tathvatech.user.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_PROJECT")
public class Project extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private long accountPk;
	private String projectName;
	private String projectDescription;
	private String projectRefNo;
	private String customerName;
	private String projectRefNoCustomer;
	private String contractNo;
	private String copyrightNotice;
	private Integer managerPk;
	private long createdBy;
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

	public long getAccountPk() {
		return accountPk;
	}

	public void setAccountPk(long accountPk) {
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

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
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
}
