/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.forms.oid.TestProcSectionOID;
import com.tathvatech.user.OID.OID;
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
@Table(name="testproc_form_section")
public class TestProcFormSection extends AbstractEntity implements Serializable
{
	@Id
	private long  pk;
	private int testProcFormAssignFk;
	private long formSectionFk;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getTestProcFormAssignFk()
	{
		return testProcFormAssignFk;
	}

	public void setTestProcFormAssignFk(int testProcFormAssignFk)
	{
		this.testProcFormAssignFk = testProcFormAssignFk;
	}

	public long getFormSectionFk()
	{
		return formSectionFk;
	}

	public void setFormSectionFk(long formSectionFk)
	{
		this.formSectionFk = formSectionFk;
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
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(pk == 0 || obj == null)
			return super.equals(obj);
		return pk == ((TestProcFormSection)obj).getPk();
	}

	public OID getOID()
	{
		return new TestProcSectionOID((int) pk);
	}

}
