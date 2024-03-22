package com.tathvatech.unit.request;


import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.unit.enums.UnitSortOrder;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;

public class UnitFilterBean {
	
	ProjectOID projectoid;
	UnitSortOrder sortOrder;
	String unitName;
	String serialNo;
	String[] unitStatus;
	Integer[] unitPks;
	ProjectPartOID[] projectParts;
	String partNo;
	
	boolean showRootUnitsOnly;
	
	Boolean showProjectPartsAssignedOnly; // if this is set, the filter should show only the units with partPk added to the ProjectPart setup.
	
	WorkstationOID unitsAtWorkstationOID; // return only the units where the unit_location entry is inprogress.
	
	
	public UnitFilterBean(ProjectOID projectoid)
	{
		this.projectoid = projectoid;
	}
	
	public UnitFilterBean(ProjectOID projectoid, Integer[] unitPks, UnitSortOrder sortOrder, String unitName, String serialNo, String[] unitStatus)
	{
		this(projectoid, unitPks, sortOrder, unitName, serialNo, unitStatus, null);
	}

	public UnitFilterBean(ProjectOID projectoid, Integer[] unitPks, UnitSortOrder sortOrder, String unitName, String serialNo, String[] unitStatus, ProjectPartOID[] projectParts)
	{
		this.projectoid = projectoid;
		this.unitPks = unitPks;
		this.sortOrder = sortOrder;
		this.unitName = unitName;
		this.serialNo = serialNo;
		this.unitStatus = unitStatus;
		this.projectParts = projectParts;
	}

	public ProjectOID getProjectoid() {
		return projectoid;
	}

	public void setProjectoid(ProjectOID projectoid) {
		this.projectoid = projectoid;
	}

	public boolean getShowRootUnitsOnly()
	{
		return showRootUnitsOnly;
	}

	public void setShowRootUnitsOnly(boolean showRootUnitsOnly)
	{
		this.showRootUnitsOnly = showRootUnitsOnly;
	}

	public Integer[] getUnitPks() {
		return unitPks;
	}

	public void setUnitPks(Integer[] unitPks) {
		this.unitPks = unitPks;
	}

	public UnitSortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(UnitSortOrder sortOrder) {
		this.sortOrder = sortOrder;
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

	public String[] getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(String[] unitStatus) {
		this.unitStatus = unitStatus;
	}

	public ProjectPartOID[] getProjectParts() {
		return projectParts;
	}

	public void setProjectParts(ProjectPartOID[] projectParts) {
		this.projectParts = projectParts;
	}

	public Boolean getShowProjectPartsAssignedOnly() {
		return showProjectPartsAssignedOnly;
	}

	public void setShowProjectPartsAssignedOnly(Boolean showProjectPartsAssignedOnly) {
		this.showProjectPartsAssignedOnly = showProjectPartsAssignedOnly;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public WorkstationOID getUnitsAtWorkstationOID() {
		return unitsAtWorkstationOID;
	}

	public void setUnitsAtWorkstationOID(WorkstationOID unitsAtWorkstationOID) {
		this.unitsAtWorkstationOID = unitsAtWorkstationOID;
	}
}
