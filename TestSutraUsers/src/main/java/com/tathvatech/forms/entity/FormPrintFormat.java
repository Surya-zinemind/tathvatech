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
import com.tathvatech.user.OID.Authorizable;
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

@Table(name = "form_print_format")
public class FormPrintFormat extends AbstractEntity implements Serializable, Authorizable
{
	@Id
	private long pk;
	private long formFk;
	private String printClassName;
	private String templateName;
	private String printAreaDef;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public long getFormFk() {
		return formFk;
	}

	public void setFormFk(long formFk) {
		this.formFk = formFk;
	}

	public String getPrintClassName() {
		return printClassName;
	}

	public void setPrintClassName(String printClassName) {
		this.printClassName = printClassName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getPrintAreaDef() {
		return printAreaDef;
	}

	public void setPrintAreaDef(String printAreaDef) {
		this.printAreaDef = printAreaDef;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
}
