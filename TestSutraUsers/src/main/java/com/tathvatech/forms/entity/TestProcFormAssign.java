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
import com.tathvatech.forms.common.TestProcFormAssignBean;
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
@Table(name="testproc_form_assign")
public class TestProcFormAssign extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int testProcFk;
	private long formFk;
	private Integer appliedByUserFk;
	private int current;
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

	public Integer getAppliedByUserFk()
	{
		return appliedByUserFk;
	}

	public void setAppliedByUserFk(Integer appliedByUserFk)
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
		return pk == ((TestProcFormAssign)obj).getPk();
	}

	public TestProcFormAssignBean getBean()
	{
		TestProcFormAssignBean bean = new TestProcFormAssignBean();
		bean.setAppliedByUserFk(appliedByUserFk);
		bean.setCreatedBy(createdBy);
		bean.setCreatedDate(createdDate);
		bean.setCurrent(current);
		bean.setFormFk(formFk);
		bean.setPk((int) pk);
		bean.setTestProcFk(testProcFk);
		
		return bean;
	}

}
