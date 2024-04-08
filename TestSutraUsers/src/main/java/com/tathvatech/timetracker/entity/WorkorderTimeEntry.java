package com.tathvatech.timetracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;



@Entity

@Table(name = "workorder_timeentry")
public class WorkorderTimeEntry {
	@Id
	private long  pk;
	private int shiftInstanceFk;
	private int workorderFk;
	private Integer userFk; // whose time is being booked.
	private Integer unitFk;
	private Integer projectFk;
	private Integer workstationFk;
	private Integer formFk;
	private Integer ncrFk;
	private Integer ncrUnitAssignFk;
	private Integer openItemFk;
	private Integer modFk;
	private Integer vcrFk;
	private Integer testProcFk;
	private Integer testProcSectionFk;
	private Date startTime;
	private Date endTime;
	private String timeType; //work or wait or adjustment.
	private Integer timeQualifierFk;
	private int createdBy;
	private Date createdDate;
	private int committed;
	private Date lastUpdated;


	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getShiftInstanceFk()
	{
		return shiftInstanceFk;
	}
	public void setShiftInstanceFk(int shiftInstanceFk)
	{
		this.shiftInstanceFk = shiftInstanceFk;
	}
	public int getWorkorderFk()
	{
		return workorderFk;
	}
	public void setWorkorderFk(int workorderFk)
	{
		this.workorderFk = workorderFk;
	}
	public Integer getUserFk()
	{
		return userFk;
	}
	public void setUserFk(Integer userFk)
	{
		this.userFk = userFk;
	}
	public Integer getUnitFk()
	{
		return unitFk;
	}
	public void setUnitFk(Integer unitFk)
	{
		this.unitFk = unitFk;
	}
	public Integer getProjectFk()
	{
		return projectFk;
	}
	public void setProjectFk(Integer projectFk)
	{
		this.projectFk = projectFk;
	}
	public Integer getWorkstationFk()
	{
		return workstationFk;
	}
	public void setWorkstationFk(Integer workstationFk)
	{
		this.workstationFk = workstationFk;
	}
	public Integer getFormFk()
	{
		return formFk;
	}
	public void setFormFk(Integer formFk)
	{
		this.formFk = formFk;
	}
	public Integer getNcrFk()
	{
		return ncrFk;
	}
	public void setNcrFk(Integer ncrFk)
	{
		this.ncrFk = ncrFk;
	}
	public Integer getNcrUnitAssignFk()
	{
		return ncrUnitAssignFk;
	}
	public void setNcrUnitAssignFk(Integer ncrUnitAssignFk)
	{
		this.ncrUnitAssignFk = ncrUnitAssignFk;
	}
	public Integer getOpenItemFk()
	{
		return openItemFk;
	}
	public void setOpenItemFk(Integer openItemFk)
	{
		this.openItemFk = openItemFk;
	}
	public Integer getModFk()
	{
		return modFk;
	}
	public void setModFk(Integer modFk)
	{
		this.modFk = modFk;
	}
	public Integer getVcrFk()
	{
		return vcrFk;
	}
	public void setVcrFk(Integer vcrFk)
	{
		this.vcrFk = vcrFk;
	}
	public Integer getTestProcFk()
	{
		return testProcFk;
	}
	public void setTestProcFk(Integer testProcFk)
	{
		this.testProcFk = testProcFk;
	}
	public Integer getTestProcSectionFk()
	{
		return testProcSectionFk;
	}
	public void setTestProcSectionFk(Integer testProcSectionFk)
	{
		this.testProcSectionFk = testProcSectionFk;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public String getTimeType()
	{
		return timeType;
	}
	public void setTimeType(String timeType)
	{
		this.timeType = timeType;
	}
	public Integer getTimeQualifierFk()
	{
		return timeQualifierFk;
	}
	public void setTimeQualifierFk(Integer timeQualifierFk)
	{
		this.timeQualifierFk = timeQualifierFk;
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
	public int getCommitted()
	{
		return committed;
	}
	public void setCommitted(int committed)
	{
		this.committed = committed;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
}
