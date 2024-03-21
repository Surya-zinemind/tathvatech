/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
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
@Table(name="unit_testproc_h")
public class UnitTestProcH extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private String name;
	private int unitTestProcFk;
	private int unitPk;
	private int projectPk;
	private Integer projectTestProcPk;
	private int workstationPk;
	private Integer appliedByUserFk;
	private int createdBy;
	private Date createdDate;
	private Date effectiveDateFrom;
	private Date effectiveDateTo;
	private int updatedBy;
	private Date lastUpdated;
	
	private Integer formPk; // not used anywhere. just to keep the column in the database.

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getUnitTestProcFk() {
		return unitTestProcFk;
	}
	public void setUnitTestProcFk(int unitTestProcFk) {
		this.unitTestProcFk = unitTestProcFk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	public Integer getProjectTestProcPk() {
		return projectTestProcPk;
	}
	public void setProjectTestProcPk(Integer projectTestProcPk) {
		this.projectTestProcPk = projectTestProcPk;
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
	public Integer getAppliedByUserFk() {
		return appliedByUserFk;
	}
	public void setAppliedByUserFk(Integer appliedByUserFk) {
		this.appliedByUserFk = appliedByUserFk;
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
	public Date getEffectiveDateFrom() {
		return effectiveDateFrom;
	}
	public void setEffectiveDateFrom(Date effectiveDateFrom) {
		this.effectiveDateFrom = effectiveDateFrom;
	}
	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}
	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	@Deprecated
	/**
	 * Not to be used anymore
	 * @return
	 */
	public Integer getFormPk() {
		return formPk;
	}
	
	@Deprecated
	/**
	 * Not to be used anymore.
	 * @param formPk
	 */
	public void setFormPk(Integer formPk) {
		this.formPk = formPk;
	}
}

