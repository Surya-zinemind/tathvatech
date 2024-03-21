/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;


import com.tathvatech.user.OID.FormOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name = "TAB_SURVEY")
public class Survey extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int accountPk; //TODO accountPk should be removed from this table
	private int formMainPk;
	private int formType;
	private String identityNumber;
	private String description;
	private String description1;
	private String revision;
	private int responsibleDivision;
	private int versionNo;
	private String versionComment;
	private int superseded;
	private int createdBy;
	private Date createdDate;
	private int approvedBy;
	private Date approvedDate;
	private Date deletedDate;
	private Date effectiveDate;
	private String status;	
	private String defFileName;
	private Date lastUpdated;
	private String dbTable;

	/**
     * 
     */
    public Survey()
    {
        super();
    }

    public static enum SurveyPropertyEnum {MarkUnansweredItemsAsNotApplicableOnSubmit};
    
    public static String STATUS_OPEN = "Published";
    public static final String STATUS_CLOSED = "Draft";
    public static final String STATUS_DELETED = "Deleted";

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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
	
	public int getFormMainPk()
	{
		return formMainPk;
	}

	public void setFormMainPk(int formMainPk)
	{
		this.formMainPk = formMainPk;
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
		if (identityNumber != null){
			this.identityNumber = identityNumber.trim();
		}
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

	public int getResponsibleDivision()
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

	public void setResponsibleDivision(int responsibleDivision)
	{
		this.responsibleDivision = responsibleDivision;
	}

	public int getVersionNo()
	{
		return versionNo;
	}

	public void setVersionNo(int versionNo)
	{
		this.versionNo = versionNo;
	}

	public String getVersionComment()
	{
		return versionComment;
	}

	public void setVersionComment(String versionComment)
	{
		this.versionComment = versionComment;
	}

	public int getSuperseded()
	{
		return superseded;
	}

	public void setSuperseded(int superseded)
	{
		this.superseded = superseded;
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

	public int getApprovedBy()
	{
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy)
	{
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate()
	{
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate)
	{
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

	public Date getDeletedDate()
	{
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate)
	{
		this.deletedDate = deletedDate;
	}

	public String getDbTable()
	{
		return dbTable;
	}

	public void setDbTable(String dbTable)
	{
		this.dbTable = dbTable;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getDefFileName()
	{
		return defFileName;
	}

	public void setDefFileName(String defFileName)
	{
		this.defFileName = defFileName;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public FormOID getOID() {
		return new FormOID((int) pk, identityNumber + ((description != null)? (" - " + description):""));
	}
    

}
