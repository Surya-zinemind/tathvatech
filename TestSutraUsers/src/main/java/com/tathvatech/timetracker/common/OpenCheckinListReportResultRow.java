package com.tathvatech.timetracker.common;

import java.util.Date;

import net.sf.persist.annotations.NoTable;

@NoTable
public class OpenCheckinListReportResultRow
{
	private Integer workorderPk;
	private String workorderNumber;
	private Integer shiftInstanceFk;
	private Integer userPk;
	private String userName;
	private String userFullName;
	private String userProfilePicFileName;
	private String timeSlotTimeType;
	private Date timeslotStartTime;
	
	
	public Integer getWorkorderPk()
	{
		return workorderPk;
	}
	public void setWorkorderPk(Integer workorderPk)
	{
		this.workorderPk = workorderPk;
	}
	public String getWorkorderNumber()
	{
		return workorderNumber;
	}
	public void setWorkorderNumber(String workorderNumber)
	{
		this.workorderNumber = workorderNumber;
	}
	public Integer getShiftInstanceFk()
	{
		return shiftInstanceFk;
	}
	public void setShiftInstanceFk(Integer shiftInstanceFk)
	{
		this.shiftInstanceFk = shiftInstanceFk;
	}
	public Integer getUserPk()
	{
		return userPk;
	}
	public void setUserPk(Integer userPk)
	{
		this.userPk = userPk;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getUserFullName()
	{
		return userFullName;
	}
	public void setUserFullName(String userFullName)
	{
		this.userFullName = userFullName;
	}
	public String getUserProfilePicFileName()
	{
		return userProfilePicFileName;
	}
	public void setUserProfilePicFileName(String userProfilePicFileName)
	{
		this.userProfilePicFileName = userProfilePicFileName;
	}
	public String getTimeSlotTimeType()
	{
		return timeSlotTimeType;
	}
	public void setTimeSlotTimeType(String timeSlotTimeType)
	{
		this.timeSlotTimeType = timeSlotTimeType;
	}
	public Date getTimeslotStartTime()
	{
		return timeslotStartTime;
	}
	public void setTimeslotStartTime(Date timeslotStartTime)
	{
		this.timeslotStartTime = timeslotStartTime;
	}
}
