package com.tathvatech.unit.request;


import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.enums.UnitOriginType;
import com.tathvatech.unit.enums.UnitSortOrder;
import com.tathvatech.user.OID.PartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;

public class UnitInProjectListReportRequest 
{
	ProjectOID projectOID;

	UnitOID unitOID;
	Integer[] unitPks;
	
	String unitName;
	String serialNo;
	PartOID partOID;
	String partNo;
	
	boolean showRootUnitsOnly;
	UnitOID parentUnitOID;
	boolean allChildrenRecursive;
	
	ProjectPartOID[] projectParts;
	String[] unitStatusInProject;
	UnitSortOrder sortOrder;

	UnitOriginType unitOriginType;
	boolean showProjectPartsAssignedOnly; // if this is set, the list should show only the units which are added to the project.
	
	WorkstationOID unitsAtWorkstationOID; // return only the units where the unit_location entry is inprogress.
	
	public UnitInProjectListReportRequest(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
		this.showRootUnitsOnly = showRootUnitsOnly;
	}
	
	public UnitInProjectListReportRequest(ProjectOID projectOID, UnitOID parentUnitOID, boolean allChildrenRecursive)
	{
		this.parentUnitOID = parentUnitOID;
		this.allChildrenRecursive = allChildrenRecursive;
	}
	
	public UnitInProjectListReportRequest(ProjectOID projectOID, boolean showProjectPartsAssignedOnly, UnitOID parentUnitOID, boolean allChildrenRecursive)
	{
		this.projectOID = projectOID;
		this.showProjectPartsAssignedOnly = showProjectPartsAssignedOnly;
		this.parentUnitOID = parentUnitOID;
		this.allChildrenRecursive = allChildrenRecursive;
	}

	public ProjectOID getProjectOID()
	{
		return projectOID;
	}

	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}

	public PartOID getPartOID()
	{
		return partOID;
	}

	public void setPartOID(PartOID partOID)
	{
		this.partOID = partOID;
	}

	public String getPartNo()
	{
		return partNo;
	}

	public void setPartNo(String partNo)
	{
		this.partNo = partNo;
	}

	public UnitOID getParentUnitOID()
	{
		return parentUnitOID;
	}

	public void setParentUnitOID(UnitOID parentUnitOID)
	{
		this.parentUnitOID = parentUnitOID;
	}

	public UnitSortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(UnitSortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
	}

	public Integer[] getUnitPks()
	{
		return unitPks;
	}

	public void setUnitPks(Integer[] unitPks)
	{
		this.unitPks = unitPks;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String[] getUnitStatusInProject() {
		return unitStatusInProject;
	}

	public void setUnitStatusInProject(String[] unitStatusInProject) {
		this.unitStatusInProject = unitStatusInProject;
	}

	public ProjectPartOID[] getProjectParts() {
		return projectParts;
	}

	public void setProjectParts(ProjectPartOID[] projectParts) {
		this.projectParts = projectParts;
	}

	public boolean getShowProjectPartsAssignedOnly() {
		return showProjectPartsAssignedOnly;
	}

	public void setShowProjectPartsAssignedOnly(boolean showProjectPartsAssignedOnly) {
		this.showProjectPartsAssignedOnly = showProjectPartsAssignedOnly;
	}

	public UnitOriginType getUnitOriginType()
	{
		return unitOriginType;
	}

	public void setUnitOriginType(UnitOriginType unitOriginType)
	{
		this.unitOriginType = unitOriginType;
	}

	public boolean getShowRootUnitsOnly()
	{
		return showRootUnitsOnly;
	}

	public void setShowRootUnitsOnly(boolean showRootUnitsOnly)
	{
		this.showRootUnitsOnly = showRootUnitsOnly;
	}

	public boolean isAllChildrenRecursive()
	{
		return allChildrenRecursive;
	}

	public void setAllChildrenRecursive(boolean allChildrenRecursive)
	{
		this.allChildrenRecursive = allChildrenRecursive;
	}

	public WorkstationOID getUnitsAtWorkstationOID()
	{
		return unitsAtWorkstationOID;
	}

	public void setUnitsAtWorkstationOID(WorkstationOID unitsAtWorkstationOID)
	{
		this.unitsAtWorkstationOID = unitsAtWorkstationOID;
	}
}
