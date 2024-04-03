package com.tathvatech.unit.common;

import com.tathvatech.forms.oid.FormAssignable;
import com.tathvatech.site.oid.SiteGroupOID;
import com.tathvatech.unit.entity.UnitInProject;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SupplierOID;
import com.tathvatech.user.OID.UnitOID;

import java.io.Serializable;
import java.util.Date;




public class UnitQuery implements Serializable, FormAssignable
{
    int unitPk;
    private String unitOriginTypeString;
	private UnitOriginType unitOriginType;
    int projectPk;
    private String projectName;
	private Integer projectPartPk;
	private String projectPartName;
	private String partName;
	private String partType;
    private int partPk;
    private Integer partRevisionPk;
    private String partRevision;
	private String partNo;
	private int supplierFk;
	private int siteGroupFk;
    private int orderNo;
	private Integer parentPk;
	private Integer rootParentPk;
	private int level;
	private boolean hasChildren;
	private String heiCode;
	String serialNo;
	String unitName;
	String displayName;
    String description;
    Date createDate;
    String createdByFirstName;
    String createdByLastName;
    Integer projectPkUnitIsOpen;
    String projectNameUnitIsOpen;
    String projectDescriptionUnitIsOpen;
    Date lastUpdatedDate;
    
    private int estatus;
    String status;

    
	// we need to remove this field.. only used for the Andon App functinality.
    @Deprecated //.. user getUnitPk()
	public int getPk() {
		return unitPk;
	}

    @Deprecated // user setUnitPk instead .. 
	public void setPk(int unitPk) {
		this.unitPk = unitPk;
	}

    @Override
    public OID getFormAssignableObjectOID()
    {
    	return getUnitOID();
    }
    
	public int getUnitPk()
    {
    	return unitPk;
    }

    public void setUnitPk(int unitPk)
    {
    	this.unitPk = unitPk;
    }

    public String getUnitOriginTypeString() {
		return unitOriginTypeString;
	}

	public void setUnitOriginTypeString(String unitOriginTypeString) {
		this.unitOriginTypeString = unitOriginTypeString;
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

    public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
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

	public String getProjectPartName() {
		return projectPartName;
	}

	public void setProjectPartName(String projectPartName) {
		this.projectPartName = projectPartName;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
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

	public String getPartRevision() {
		return partRevision;
	}

	public void setPartRevision(String partRevision) {
		this.partRevision = partRevision;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public int getSupplierFk() {
		return supplierFk;
	}

	public void setSupplierFk(int supplierFk) {
		this.supplierFk = supplierFk;
	}

	public int getSiteGroupFk() {
		return siteGroupFk;
	}

	public void setSiteGroupFk(int siteGroupFk) {
		this.siteGroupFk = siteGroupFk;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getParentPk() {
		return parentPk;
	}

	public void setParentPk(Integer parentPk) {
		this.parentPk = parentPk;
	}

	public Integer getRootParentPk() {
		return rootParentPk;
	}

	public void setRootParentPk(Integer rootParentPk) {
		this.rootParentPk = rootParentPk;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public String getHeiCode()
	{
		return heiCode;
	}

	public void setHeiCode(String heiCode)
	{
		this.heiCode = heiCode;
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
	this.unitName = unitName;
    }

    public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

    public Date getCreateDate()
    {
	return createDate;
    }

    public void setCreateDate(Date createDate)
    {
	this.createDate = createDate;
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

	public String getCreatedByFirstName()
	{
		return createdByFirstName;
	}

	public void setCreatedByFirstName(String createdByFirstName)
	{
		this.createdByFirstName = createdByFirstName;
	}

	public String getCreatedByLastName()
	{
		return createdByLastName;
	}

	public void setCreatedByLastName(String createdByLastName)
	{
		this.createdByLastName = createdByLastName;
	}

	public Integer getProjectPkUnitIsOpen()
	{
		return projectPkUnitIsOpen;
	}

	public void setProjectPkUnitIsOpen(Integer projectPkUnitIsOpen)
	{
		this.projectPkUnitIsOpen = projectPkUnitIsOpen;
	}

	public String getProjectNameUnitIsOpen()
	{
		return projectNameUnitIsOpen;
	}

	public void setProjectNameUnitIsOpen(String projectNameUnitIsOpen)
	{
		this.projectNameUnitIsOpen = projectNameUnitIsOpen;
	}

	public String getProjectDescriptionUnitIsOpen()
	{
		return projectDescriptionUnitIsOpen;
	}

	public void setProjectDescriptionUnitIsOpen(String projectDescriptionUnitIsOpen)
	{
		this.projectDescriptionUnitIsOpen = projectDescriptionUnitIsOpen;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		UnitQuery ob = (UnitQuery)obj;
		if(ob.getUnitPk() == 0 || ob.getProjectPk() == 0 || this.getUnitPk() == 0 || this.getProjectPk() == 0)
			return false;
		if(ob.getUnitPk() == this.getUnitPk() && ob.getProjectPk() == this.getProjectPk())
			return true;
		else
			return false;
	}
	
	@Override
	public String toString()
	{
		return  unitName + "/" + projectName;
	}
	
	public String getDisplayDescriptor()
	{
		StringBuffer sb = new StringBuffer(this.unitName);
		if(description != null && description.trim().length() > 0)
		{
			sb.append(" - ").append(description);
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return (unitPk + "-" + projectPk).hashCode();
	}

	public UnitOID getUnitOID() {
		return new UnitOID(unitPk, unitName);
	}

	public ProjectOID getProjectOID() {
		return new ProjectOID(projectPk, projectName);
	}

	public UnitBean getUnitBean()
	{
		final UnitBean unitBean = new UnitBean();
		unitBean.setPk(this.getUnitPk());
		unitBean.setProjectPk(this.getProjectPk());
		unitBean.setPartPk(this.getPartPk());
		unitBean.setPartRevisionPk(this.getPartRevisionPk());
		unitBean.setPartName(this.getPartName());
		unitBean.setPartNo(this.getPartNo());
		unitBean.setProjectPartPk(this.getProjectPartPk());
		unitBean.setSerialNo(this.getSerialNo());
		unitBean.setStatus(this.getStatus());
		unitBean.setSupplierOID(new SupplierOID(this.getSupplierFk()));
		unitBean.setSiteGroupOID(new SiteGroupOID(this.getSiteGroupFk()));
		unitBean.setUnitDescription(this.getDescription());
		unitBean.setUnitName(this.getUnitName());
		unitBean.setUnitOriginType(UnitOriginType.valueOf(this.getUnitOriginTypeString()));
		unitBean.setParentPk(this.getParentPk());
		return unitBean;
	}
	
	public static String sql = "select u.pk as unitPk, upr.unitOriginType as unitOriginTypeString, upr.projectPk, p.projectName, u.estatus, "
			+ " uprh.orderNo, uprh.projectPartPk, project_part.name as projectPartName, "
			+ " part_revision.pk as partRevisionPk, part_revision.revision as partRevision, "
			+ " part.pk as partPk, part.name as partName, part_type.typeName as partType, part.partNo as partNo, u.supplierFk, u.siteGroupFk, "
			+ " parentUnitPkRef.unitPk as parentPk, rootParentUnitPkRef.unitPk as rootParentPk, "
			+ " uprh.level, uprh.hasChildren, uh.unitName, "
			+ " uh.serialNo, uh.unitDescription as description, u.lastUpdated as lastUpdatedDate, "
			+ " u.createdDate as createDate, uprh.status as status, createdByUser.firstName as createdByFirstName, createdByUser.lastName as createdByLastName, "
			+ " unitOpenInProjectTab.projectPk as projectPkUnitIsOpen, unitOpenInProjectTab.projectName as projectNameUnitIsOpen, unitOpenInProjectTab.projectDescription as projectDescriptionUnitIsOpen "
			+ " from "
			+ " TAB_UNIT u"
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
			+ " join part on u.partPk = part.pk "
			+ " left join part_revision on u.partRevisionPk = part_revision.pk"
			+ " join TAB_USER createdByUser on u.createdBy=createdByUser.pk "
			+ " join unit_project_ref upr on upr.unitPk = u.pk and upr.projectPk = ?"
			+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
			+ " join TAB_PROJECT p on upr.projectPk = p.pk"
			+ " left outer join (select pk, unitPk from unit_project_ref where projectPk = ? ) parentUnitPkRef on parentUnitPkRef.pk = uprh.parentPk"
			+ " left outer join (select pk, unitPk from unit_project_ref where projectPk = ? ) rootParentUnitPkRef on rootParentUnitPkRef.pk = uprh.rootParentPk "
			+ " left outer join (select unitPk, projectPk, projectName, projectDescription from unit_project_ref upr2, unit_project_ref_h uprh2, TAB_PROJECT p2 where uprh2.unitInProjectPk = upr2.pk and now() between uprh2.effectiveDateFrom and uprh2.effectiveDateTo and upr2.projectPk = p2.pk and uprh2.status = '"+ UnitInProject.STATUS_OPEN+"' ) unitOpenInProjectTab on unitOpenInProjectTab.unitPk = u.pk "
			+ " left outer join project_part on uprh.projectPartPk = project_part.pk "
			+ " left outer join part_type on project_part.partTypePk = part_type.pk ";
}
