/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.common;

import java.util.Date;
import java.util.List;

import com.tathvatech.ts.core.part.SupplierOID;
import com.tathvatech.ts.core.sites.SiteGroupOID;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UnitBean
{
	private int pk;
	private int rootParentPk;
	private String heiCode;
	private int unitLevel;
	private int partPk;
	private Integer partRevisionPk;
	private String partName;
	private String partNo;
	private SupplierOID supplierOID;
	private SiteGroupOID siteGroupOID;
	private UnitOriginType unitOriginType;
	private int projectPk;
	private Integer projectPartPk;
	private Integer parentPk;
	private String serialNo;
	private String unitName;
	private String displayName;
	private String unitDescription;
	private int createdBy;
	private Date createdDate;
	private String status;
	private Date lastUpdated;
	
	private UnitBean parent;
	private List<UnitBean> children;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getRootParentPk() {
		return rootParentPk;
	}

	public void setRootParentPk(int rootParentPk) {
		this.rootParentPk = rootParentPk;
	}

	public String getHeiCode() {
		return heiCode;
	}

	public void setHeiCode(String heiCode) {
		this.heiCode = heiCode;
	}

	public int getUnitLevel() {
		return unitLevel;
	}

	public void setUnitLevel(int unitLevel) {
		this.unitLevel = unitLevel;
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

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getPartNo()
	{
		return partNo;
	}

	public void setPartNo(String partNo)
	{
		this.partNo = partNo;
	}

	public SupplierOID getSupplierOID() {
		return supplierOID;
	}

	public void setSupplierOID(SupplierOID supplierOID) {
		this.supplierOID = supplierOID;
	}

	public SiteGroupOID getSiteGroupOID() {
		return siteGroupOID;
	}

	public void setSiteGroupOID(SiteGroupOID siteGroupOID) {
		this.siteGroupOID = siteGroupOID;
	}

	public UnitOriginType getUnitOriginType() {
		return unitOriginType;
	}

	public void setUnitOriginType(UnitOriginType unitOriginType) {
		this.unitOriginType = unitOriginType;
	}

	public int getProjectPk()
	{
		return projectPk;
	}

	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}

	public Integer getProjectPartPk() {
		return projectPartPk;
	}

	public void setProjectPartPk(Integer projectPartPk) {
		this.projectPartPk = projectPartPk;
	}

	public Integer getParentPk() {
		return parentPk;
	}

	public void setParentPk(Integer parentPk) {
		this.parentPk = parentPk;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		if (unitName != null){
			this.unitName = unitName.trim();
		}
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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
     * 
     */
    public UnitBean()
    {
    }


	public UnitBean getParent() {
		return parent;
	}

	public void setParent(UnitBean parent) {
		this.parent = parent;
	}

	public List<UnitBean> getChildren() {
		return children;
	}

	public void setChildren(List<UnitBean> children) {
		this.children = children;
	}

	public UnitOID getOID() {
		return new UnitOID(pk, unitName);
	}

	
	@Override
	public int hashCode()
	{
		return pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof UnitBean))
			return false;
		
		return pk == ((UnitBean)obj).getPk();
	}

	public String getDisplayDescriptor()
	{
		StringBuffer sb = new StringBuffer(this.unitName);
		if(unitDescription != null && unitDescription.trim().length() > 0)
		{
			sb.append(" - ").append(unitDescription);
		}
		return sb.toString();
	}

	public String toString()
	{
		return getDisplayDescriptor();
	}

}
