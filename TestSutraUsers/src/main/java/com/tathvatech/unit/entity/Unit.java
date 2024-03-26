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
import com.tathvatech.site.oid.SiteGroupOID;
import com.tathvatech.unit.common.TestableEntity;
import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.unit.service.UnitManagerCore;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SupplierOID;
import com.tathvatech.user.OID.UnitOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="TAB_UNIT")
public class Unit  extends AbstractEntity implements Serializable,TestableEntity
{
	@Transient
	@Autowired
	private  UnitManagerCore unitManagerCore;
	@Id
	private long pk;
	private int partPk;
	private Integer partRevisionPk;
	private Integer supplierFk;
	private Integer siteGroupFk;
	private int createdBy;
	private Date createdDate;
	private int estatus; 
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public Integer getPartRevisionPk() {
		return partRevisionPk;
	}

	public void setPartRevisionPk(Integer partRevisionPk) {
		this.partRevisionPk = partRevisionPk;
	}

	public Integer getSupplierFk() {
		return supplierFk;
	}

	public void setSupplierFk(Integer supplierFk) {
		this.supplierFk = supplierFk;
	}

	public Integer getSiteGroupFk() {
		return siteGroupFk;
	}

	public void setSiteGroupFk(Integer siteGroupFk) {
		this.siteGroupFk = siteGroupFk;
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

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public Unit() {
	}

	/**
     * 
     */


	public Unit(UnitBean unitBean, UnitManagerCore unitManagerCore)
	{
        this.unitManagerCore = unitManagerCore;
        this.setPk(unitBean.getPk());
		this.setPartPk(unitBean.getPartPk());
		this.setPartRevisionPk(unitBean.getPartRevisionPk());
		this.setSupplierFk((int) unitBean.getSupplierOID().getPk());
		this.setSiteGroupFk((int) unitBean.getSiteGroupOID().getPk());
		this.setCreatedBy(unitBean.getCreatedBy());
		this.setCreatedDate(unitBean.getCreatedDate());
		this.setLastUpdated(unitBean.getLastUpdated());
	}
	
	public UnitBean getUnitBean(ProjectOID projectOID)
	{
		Date now = new Date();
		UnitH unitH = unitManagerCore.getUnitH(this.getOID(), now);
		UnitInProject unitInProject = unitManagerCore.getUnitInProject(this.getOID(), projectOID);
		UnitInProjectH unitInProjectH = unitManagerCore.getUnitInProjectH(unitInProject.getOID(), now);

		UnitBean unitBean = new UnitBean();
		
		unitBean.setPk((int) this.getPk());
		unitBean.setPartPk(this.getPartPk());
		unitBean.setPartRevisionPk(this.getPartRevisionPk());
		unitBean.setSupplierOID(new SupplierOID(this.getSupplierFk()));
		unitBean.setSiteGroupOID(new SiteGroupOID(this.getSiteGroupFk()));
		unitBean.setUnitOriginType(UnitOriginType.valueOf(unitInProject.getUnitOriginType()));
		unitBean.setCreatedBy(this.getCreatedBy());
		unitBean.setCreatedDate(this.getCreatedDate());
		unitBean.setLastUpdated(this.getLastUpdated());
		unitBean.setParentPk(unitInProjectH.getParentPk());
		unitBean.setProjectPartPk(unitInProjectH.getProjectPartPk());
		unitBean.setProjectPk(unitInProject.getProjectPk());
		unitBean.setSerialNo(unitH.getSerialNo());
		unitBean.setStatus(unitInProjectH.getStatus());
		unitBean.setUnitDescription(unitH.getUnitDescription());
		unitBean.setUnitName(unitH.getUnitName());
		
		return unitBean;
	}
    
	@Override
	public UnitOID getOID() {
		return new UnitOID((int) pk, null);
	}
}
