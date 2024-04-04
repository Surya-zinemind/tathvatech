/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import java.util.Date;

import com.tathvatech.ts.core.project.FormMainOID;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Table(name = "TAB_FORMS_MAIN")
public class FormMain
{
	private int pk;
	private int accountPk;
	private String identityNumber;
	private int responsibleDivision;
	private Date createdDate;
	private String status;
	private Date lastUpdated;
	
	/**
     * 
     */
    public FormMain()
    {
        super();
    }

    public static String STATUS_ACTIVE = "Active";
    public static final String STATUS_DELETED = "Deleted";

	@Column(autoGenerated=true)
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
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
	
	public String getIdentityNumber()
	{
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber)
	{
		this.identityNumber = identityNumber;
	}

	public int getResponsibleDivision()
	{
		return responsibleDivision;
	}

	public void setResponsibleDivision(int responsibleDivision)
	{
		this.responsibleDivision = responsibleDivision;
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

	@Column(autoGenerated=true)
	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public FormMainOID getOID() {
		return new FormMainOID(pk, identityNumber);
	}

}
