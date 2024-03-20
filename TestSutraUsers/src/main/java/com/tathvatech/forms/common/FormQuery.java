package com.tathvatech.forms.common;

import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.user.OID.FormOID;

import java.util.Date;




public class FormQuery
{
	private int pk;
	private int formMainPk;
	private int accountPk;
	private int formType;
	private String identityNumber;
	private String description;
	private String description1;
	private String revision;
	private String responsibleDivision;
	private String responsibleDivisionName;
	private int preparedByPk;
	private String preparedByFirstName;
	private String preparedByLastName;
	private int approvedByPk;
	private String approvedByFirstName;
	private String approvedByLastName;
	private Date approvedDate;
	private Date effectiveDate;
	private Date createdDate;
	private int versionNo;
	private String versionComment;
	private int superseded;
	
	private String status;
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getFormMainPk()
	{
		return formMainPk;
	}
	public void setFormMainPk(int formMainPk)
	{
		this.formMainPk = formMainPk;
	}
	public int getAccountPk()
	{
		return accountPk;
	}
	public void setAccountPk(int accountPk)
	{
		this.accountPk = accountPk;
	}
	public int getFormType() {
		return formType;
	}
	public void setFormType(int formType) {
		this.formType = formType;
	}
	public String getIdentityNumber()
	{
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber)
	{
		this.identityNumber = identityNumber;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription1()
	{
		return description1;
	}
	public void setDescription1(String description1)
	{
		this.description1 = description1;
	}
	public String getResponsibleDivision()
	{
		return responsibleDivision;
	}
	public String getRevision()
	{
		return revision;
	}
	public void setRevision(String revision)
	{
		this.revision = revision;
	}
	public void setResponsibleDivision(String responsibleDivision)
	{
		this.responsibleDivision = responsibleDivision;
	}
	public String getResponsibleDivisionName()
	{
		return responsibleDivisionName;
	}
	public void setResponsibleDivisionName(String responsibleDivisionName)
	{
		this.responsibleDivisionName = responsibleDivisionName;
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
		if (approvedByFirstName == null){
			return "" ;
		} else {
			return approvedByFirstName;
		}
	}
	public void setApprovedByFirstName(String approvedByFirstName)
	{
		this.approvedByFirstName = approvedByFirstName;
	}
	
	public String getApprovedByLastName()
	{
		if (approvedByLastName == null){
			
			return "";
		} else {
			return approvedByLastName;
		}
	}
	public void setApprovedByLastName(String approvedByLastName)
	{
		this.approvedByLastName = approvedByLastName;
	}
	
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	public Date getEffectiveDate()
	{
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate)
	{
		this.effectiveDate = effectiveDate;
	}
	public Date getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}
	public int getVersionNo()
	{
		return versionNo;
	}
	public void setVersionNo(int versionNo)
	{
		this.versionNo = versionNo;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	@Override
	public boolean equals(Object obj)
	{
		FormQuery f = (FormQuery)obj;
		
		if(f != null && this.getPk() == f.getPk())
		{
			return true;
		}
		return false;
	}

	public void setVersionComment(String versionComment) {
		this.versionComment = versionComment;
	}
	public String getVersionComment() {
		return versionComment;
	}

	public int getSuperseded()
	{
		return superseded;
	}
	public void setSuperseded(int superseded)
	{
		this.superseded = superseded;
	}

	public FormOID getOID()
	{
		return new FormOID(pk, identityNumber + " - " + ListStringUtil.showString(description, 0) + "(" + revision + "/" + versionNo + ")");
	}
}
