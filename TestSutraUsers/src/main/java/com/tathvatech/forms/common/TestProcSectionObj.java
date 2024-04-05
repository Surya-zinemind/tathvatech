/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import com.tathvatech.forms.oid.TestProcSectionOID;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestProcSectionObj
{
	private int pk;
	private int testProcFormAssignFk;
	private long formSectionFk;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;

	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getTestProcFormAssignFk() {
		return testProcFormAssignFk;
	}
	public void setTestProcFormAssignFk(int testProcFormAssignFk) {
		this.testProcFormAssignFk = testProcFormAssignFk;
	}
	public long getFormSectionFk() {
		return formSectionFk;
	}
	public void setFormSectionFk(long formSectionFk) {
		this.formSectionFk = formSectionFk;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
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

	public TestProcSectionOID getOID() {
		return new TestProcSectionOID(pk, null);
	}
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		if(((TestProcSectionObj)obj).getPk() == pk)
			return true;
		else
			return false;
	}
	@Override
	public int hashCode() {
		return pk;
	}
}
