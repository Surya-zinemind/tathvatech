package com.tathvatech.user.common;

import com.tathvatech.user.OID.TestProcOID;
import com.tathvatech.user.OID.UnitOID;

import java.util.Date;

public class TestProcObj
{
	private int pk;
	private Integer appliedByUserFk;
	private String name;
	private int projectPk;
	private int unitPk;
	private int workstationPk;
	private int formPk;
	private Integer projectTestProcPk;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;
	private int estatus;
	
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}
	public int getWorkstationPk() 
	{
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) 
	{
		this.workstationPk = workstationPk;
	}
	public int getFormPk()
	{
		return formPk;
	}
	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}
	public Integer getProjectTestProcPk() {
		return projectTestProcPk;
	}
	public void setProjectTestProcPk(Integer projectTestProcPk) {
		this.projectTestProcPk = projectTestProcPk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getAppliedByUserFk() {
		return appliedByUserFk;
	}
	public void setAppliedByUserFk(Integer appliedByUserFk) {
		this.appliedByUserFk = appliedByUserFk;
	}
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)
			return false;
		if(((TestProcObj)obj).getPk() == pk)
			return true;
		else
			return false;
	}
	@Override
	public int hashCode() {
		return pk;
	}
	public TestProcOID getOID() {
		return new TestProcOID((int) pk,name );
	}
}
