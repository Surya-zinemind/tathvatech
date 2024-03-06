/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.common;

import com.tathvatech.site.oid.SiteGroupOID;
import com.tathvatech.unit.entity.UnitH;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.entity.UnitInProjectH;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.unit.service.UnitManagerCore;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SupplierOID;
import com.tathvatech.user.OID.UnitOID;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UnitObj implements TestableEntity
{
	private int pk;
	private int partPk;
	private Integer partRevisionPk;
	private Integer supplierFk;
	private Integer siteGroupFk;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;
	private String serialNo;
	private String unitName;
	private String displayName;
	private String unitDescription;
	private String status;
	private int estatus;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
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

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public String getSerialNo()
	{
		return serialNo;
	}

	public void setSerialNo(String serialNo)
	{
		this.serialNo = serialNo;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUnitDescription()
	{
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
     * 
     */
    public UnitObj()
    {
    }


	public UnitObj(UnitBean unitBean)
	{
		this.setPk(unitBean.getPk());
		this.setUnitName(unitBean.getUnitName());
		this.setDisplayName(unitBean.getDisplayName());
		
		this.setUnitDescription(unitBean.getUnitDescription());
		this.setSerialNo(unitBean.getSerialNo());
		this.setStatus(unitBean.getStatus());
		this.setPartPk(unitBean.getPartPk());
		this.setPartRevisionPk(unitBean.getPartRevisionPk());
		if(unitBean.getSupplierOID() != null)
			this.setSupplierFk((int) unitBean.getSupplierOID().getPk());
		if(unitBean.getSiteGroupOID() != null)
			this.setSiteGroupFk((int) unitBean.getSiteGroupOID().getPk());
		this.setCreatedBy(unitBean.getCreatedBy());
		this.setCreatedDate(unitBean.getCreatedDate());
		this.setLastUpdated(unitBean.getLastUpdated());
	}
	
	public UnitBean getUnitBean(ProjectOID projectOID)
	{
		Date now = new Date();
		UnitH unitH = UnitManagerCore.getUnitH(this.getOID(), now);
		UnitInProject unitInProject = UnitManagerCore.getUnitInProject(this.getOID(), projectOID);
		UnitInProjectH unitInProjectH = UnitManagerCore.getUnitInProjectH(unitInProject.getOID(), now);
		// TODO:: should load the part info and add the part name and partNo to this. in TS7 now so cant add 
		
		UnitBean unitBean = new UnitBean();
		
		
		unitBean.setPk(this.getPk());
		unitBean.setPartPk(this.getPartPk());
		unitBean.setPartRevisionPk(this.getPartRevisionPk());
		if(this.getSupplierFk() != null)
			unitBean.setSupplierOID(new SupplierOID(this.getSupplierFk()));
		if(this.getSiteGroupFk() != null)
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
		unitBean.setDisplayName(unitH.getDisplayName());
		
		return unitBean;
	}
    

	public UnitOID getOID() {
		return new UnitOID(pk, unitName);
	}
}
