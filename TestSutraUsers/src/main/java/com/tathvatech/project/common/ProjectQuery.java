package com.tathvatech.project.common;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tathvatech.ts.core.project.ProjectOID;

import net.sf.persist.annotations.NoTable;

@NoTable
public class ProjectQuery implements Serializable
{
	private int pk;
	private String projectName;
	private String projectDescription;
	private String projectRefNo;
	private String customerName;
	private String projectRefNoCustomer;
	private String contractNo;
	private String copyrightNotice;
	private String status;
	private int createdByPk;
	private String createdByFirstName;
	private String createdByLastName;
	private int managerPk;
	private String managerFirstName;
	private String managerLastName;
	private Boolean disableDailySummaryEmails;
	private Boolean disableNotifications;
	private Boolean enableChecksheets;
	private Date createdDate;
	private int unitCount;
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
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
	public String getProjectRefNo() 
	{
		return projectRefNo;
	}
	public void setProjectRefNo(String projectRefNo) 
	{
		this.projectRefNo = projectRefNo;
	}
	public String getCustomerName() 
	{
		return customerName;
	}
	public void setCustomerName(String customerName) 
	{
		this.customerName = customerName;
	}
	public String getProjectRefNoCustomer() 
	{
		return projectRefNoCustomer;
	}
	public void setProjectRefNoCustomer(String projectRefNoCustomer) 
	{
		this.projectRefNoCustomer = projectRefNoCustomer;
	}
	public String getContractNo() 
	{
		return contractNo;
	}
	public void setContractNo(String contractNo) 
	{
		this.contractNo = contractNo;
	}
	public String getCopyrightNotice() 
	{
		return copyrightNotice;
	}
	public void setCopyrightNotice(String copyrightNotice) 
	{
		this.copyrightNotice = copyrightNotice;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public int getCreatedByPk()
	{
		return createdByPk;
	}
	public void setCreatedByPk(int createdByPk)
	{
		this.createdByPk = createdByPk;
	}
	public String getCreatedByFirstName()
	{
		return createdByFirstName;
	}
	public void setCreatedByFirstName(String createdByFirstName)
	{
		this.createdByFirstName = createdByFirstName;
	}
	public String getCreatedByLastName()
	{
		return createdByLastName;
	}
	public void setCreatedByLastName(String createdByLastName)
	{
		this.createdByLastName = createdByLastName;
	}
	public int getManagerPk()
	{
		return managerPk;
	}
	public void setManagerPk(int managerPk)
	{
		this.managerPk = managerPk;
	}
	public String getManagerFirstName()
	{
		return managerFirstName;
	}
	public void setManagerFirstName(String managerFirstName)
	{
		this.managerFirstName = managerFirstName;
	}
	public String getManagerLastName()
	{
		return managerLastName;
	}
	public void setManagerLastName(String managerLastName)
	{
		this.managerLastName = managerLastName;
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
	public Date getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}
	
	public int getUnitCount()
	{
		return unitCount;
	}
	public void setUnitCount(int unitCount)
	{
		this.unitCount = unitCount;
	}
	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((ProjectQuery)obj).getPk() == this.pk)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public String getDisplayString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(projectName);
		if(!(StringUtils.isEmpty(projectDescription)))
		{
			sb.append(" - ").append(projectDescription);
		}
		return sb.toString();
	}
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return getDisplayString();
	}
	@Override
	public int hashCode() {
		return pk;
	}

	public ProjectOID getOID()
	{
		return new ProjectOID(pk, projectName + ((projectDescription != null)? (" - " + projectDescription):""));
	}
	public static String fetchSQL = "select project.pk, project.projectName, project.projectDescription, project.disableDailySummaryEmails, project.disableNotifications, " 
			+ "project.enableChecksheets, project.projectRefNo, project.customerName, project.projectRefNoCustomer, project.contractNo, " 
			+ "project.copyrightNotice, project.status as status, " 
			+ "project.createdDate, project.createdBy as createdByPk, user.firstName as createdByFirstName, user.lastName as createdByLastName, " 
			+ "project.managerPk as managerPk, pm.firstName as managerFirstName, pm.lastName as managerLastName " 
			+ "from TAB_PROJECT project "
			+ "left join TAB_USER pm on  project.managerPk = pm.pk "
			+ " join TAB_USER user on project.createdBy = user.pk ";
}
