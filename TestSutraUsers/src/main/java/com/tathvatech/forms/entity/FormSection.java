/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.FormSectionOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name = "form_section")
public class FormSection extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int formPk;
	private int formSectionMainPk;
	private String sectionId; // this is temporary needed to match the xml id with the db record.
	private float orderNo;
    String instructionFileName;
    String instructionFileDisplayName;
	private Date createdDate;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getFormPk()
	{
		return formPk;
	}

	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}

	public int getFormSectionMainPk()
	{
		return formSectionMainPk;
	}

	public void setFormSectionMainPk(int formSectionMainPk)
	{
		this.formSectionMainPk = formSectionMainPk;
	}

	public float getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(float orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public String getInstructionFileName()
	{
		return instructionFileName;
	}

	public void setInstructionFileName(String instructionFileName)
	{
		this.instructionFileName = instructionFileName;
	}

	public String getInstructionFileDisplayName()
	{
		return instructionFileDisplayName;
	}

	public void setInstructionFileDisplayName(String instructionFileDisplayName)
	{
		this.instructionFileDisplayName = instructionFileDisplayName;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public FormSectionOID getOID() {
		return new FormSectionOID((int) pk);
	}

}
