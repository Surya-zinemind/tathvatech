package com.tathvatech.unit.request;

import com.tathvatech.ts.core.part.PartOID;
import com.tathvatech.ts.core.project.UnitOID;

public class UnitEntityListReportRequest 
{

	UnitOID unitOID;
	
	private String searchString; // searches in unitName unit description and serial number
	private String unitName;
	private String serialNo;
	private PartOID partOID;
	private String partNo;
	private boolean showManufacturedOnAProject; // if this is set, the list should show the units which are manufactured at least in 1 project
	
	boolean showProjectPartsAssignedOnly; // if this is set, the list should show only the units whose partNo is a projectPart on some project
	
	public String getSearchString()
	{
		return searchString;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
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

	public UnitOID getUnitOID()
	{
		return unitOID;
	}

	public void setUnitOID(UnitOID unitOID)
	{
		this.unitOID = unitOID;
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

	public boolean getShowManufacturedOnAProject()
	{
		return showManufacturedOnAProject;
	}

	public void setShowManufacturedOnAProject(boolean showManufacturedOnAProject)
	{
		this.showManufacturedOnAProject = showManufacturedOnAProject;
	}

	public boolean getShowProjectPartsAssignedOnly() {
		return showProjectPartsAssignedOnly;
	}

	public void setShowProjectPartsAssignedOnly(boolean showProjectPartsAssignedOnly) {
		this.showProjectPartsAssignedOnly = showProjectPartsAssignedOnly;
	}
}
