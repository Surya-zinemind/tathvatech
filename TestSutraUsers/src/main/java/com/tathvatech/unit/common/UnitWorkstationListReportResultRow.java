/*
 * Created on Jan 28, 2020
 *
 */
package com.tathvatech.unit.common;

import com.tathvatech.workstation.oid.UnitWorkstationOID;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UnitWorkstationListReportResultRow
{
	private int pk;
	private int unitPk;
	private String unitName;
	private String unitDescription;
	private String unitSerialNumber;
	private int partPk;
	private String partName;
	private String partType;
	private int projectPartPk;
	private String projectPartName;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int workstationPk;
	private String workstationName;
	private String workstationDescription;
	private String workstationStatus;
	private String workstationTimezoneId;
	
	private Date forecastStartDate;
	private Date forecastEndDate;
	private Float forecastHours;
	private String forecastComment;
	private Integer forecastCreatedBy;
	private Date forecastCreatedDate;
	private String forecastCreatedByFirstName;
	private String forecastCreatedByLastName;	
		
	
	public String getDisplayDescriptor()
	{
		return String.format("Unit:%s %s- Workstation:%s %s", unitName, unitDescription, workstationName, workstationDescription).toString();
	}
	
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}
	public int getProjectPk() {
		return projectPk;
	}
	public void setProjectPk(int projectPk) {
		this.projectPk = projectPk;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDescription()
	{
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription = projectDescription;
	}

	public int getWorkstationPk() 
	{
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) 
	{
		this.workstationPk = workstationPk;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitDescription()
	{
		return unitDescription;
	}

	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
	}

	public String getUnitSerialNumber()
	{
		return unitSerialNumber;
	}

	public void setUnitSerialNumber(String unitSerialNumber)
	{
		this.unitSerialNumber = unitSerialNumber;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
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
	public int getProjectPartPk() {
		return projectPartPk;
	}
	public void setProjectPartPk(int projectPartPk) {
		this.projectPartPk = projectPartPk;
	}
	public String getProjectPartName() {
		return projectPartName;
	}
	public void setProjectPartName(String projectPartName) {
		this.projectPartName = projectPartName;
	}
	public String getWorkstationName() {
		return workstationName;
	}
	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}
	public String getWorkstationDescription()
	{
		return workstationDescription;
	}
	public void setWorkstationDescription(String workstationDescription)
	{
		this.workstationDescription = workstationDescription;
	}
	public String getWorkstationStatus()
	{
		return workstationStatus;
	}
	public void setWorkstationStatus(String workstationStatus)
	{
		this.workstationStatus = workstationStatus;
	}
	public String getWorkstationTimezoneId() {
		return workstationTimezoneId;
	}

	public void setWorkstationTimezoneId(String workstationTimezoneId) {
		this.workstationTimezoneId = workstationTimezoneId;
	}

	public Date getForecastStartDate() {
		return forecastStartDate;
	}

	public void setForecastStartDate(Date forecastStartDate) {
		this.forecastStartDate = forecastStartDate;
	}

	public Date getForecastEndDate() {
		return forecastEndDate;
	}

	public void setForecastEndDate(Date forecastEndDate) {
		this.forecastEndDate = forecastEndDate;
	}


	public Float getForecastHours() {
		return forecastHours;
	}

	public void setForecastHours(Float forecastHours) {
		this.forecastHours = forecastHours;
	}

	public String getForecastComment() {
		return forecastComment;
	}

	public void setForecastComment(String forecastComment) {
		this.forecastComment = forecastComment;
	}

	public Integer getForecastCreatedBy() {
		return forecastCreatedBy;
	}

	public void setForecastCreatedBy(Integer forecastCreatedBy) {
		this.forecastCreatedBy = forecastCreatedBy;
	}

	public Date getForecastCreatedDate() {
		return forecastCreatedDate;
	}

	public void setForecastCreatedDate(Date forecastCreatedDate) {
		this.forecastCreatedDate = forecastCreatedDate;
	}

	public String getForecastCreatedByFirstName() {
		return forecastCreatedByFirstName;
	}

	public void setForecastCreatedByFirstName(String forecastCreatedByFirstName) {
		this.forecastCreatedByFirstName = forecastCreatedByFirstName;
	}

	public String getForecastCreatedByLastName() {
		return forecastCreatedByLastName;
	}

	public void setForecastCreatedByLastName(String forecastCreatedByLastName) {
		this.forecastCreatedByLastName = forecastCreatedByLastName;
	}

	public UnitWorkstationOID getOID() {
		return new UnitWorkstationOID(pk, "");
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)return false;
		if(obj instanceof UnitWorkstationListReportResultRow)
			return this.pk == ((UnitWorkstationListReportResultRow)obj).getPk();
		
		return false;			
	}

	@Override
	public int hashCode() 
	{
		return pk;
	}

}

