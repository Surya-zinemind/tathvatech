/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class TestProcFormAssignBean
{
	private int pk;
	private int testProcFk;
	private long formFk;
	private long appliedByUserFk;
	private int current;
	private int createdBy;
	private Date createdDate;
	
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getTestProcFk()
	{
		return testProcFk;
	}

	public void setTestProcFk(int testProcFk)
	{
		this.testProcFk = testProcFk;
	}

	public long getFormFk()
	{
		return formFk;
	}

	public void setFormFk(long formFk)
	{
		this.formFk = formFk;
	}

	public long getAppliedByUserFk()
	{
		return appliedByUserFk;
	}

	public void setAppliedByUserFk(long appliedByUserFk)
	{
		this.appliedByUserFk = appliedByUserFk;
	}

	public int getCurrent()
	{
		return current;
	}

	public void setCurrent(int current)
	{
		this.current = current;
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

	@Override
	public int hashCode() 
	{
		if (pk == 0)
			return super.hashCode();
		return pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(pk == 0 || obj == null)
			return super.equals(obj);
		return pk == ((TestProcFormAssignBean)obj).getPk();
	}

}
