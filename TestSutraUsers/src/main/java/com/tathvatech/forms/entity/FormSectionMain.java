/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import java.io.Serializable;
import java.util.Date;


import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.survey.oid.FormSectionMainOID;
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
@Table(name = "form_section_main")
public class FormSectionMain extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int formMainPk;
	private String itemNo;
	private String description;
	private Date createdDate;
	private Date lastUpdated;
	
	/**
     * 
     */
    public FormSectionMain()
    {
        super();
    }

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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

	public String getItemNo()
	{
		return itemNo;
	}

	public void setItemNo(String itemNo)
	{
		this.itemNo = itemNo;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

	public FormSectionMainOID getOID() {
		return new FormSectionMainOID((int) pk, itemNo);
	}

}
