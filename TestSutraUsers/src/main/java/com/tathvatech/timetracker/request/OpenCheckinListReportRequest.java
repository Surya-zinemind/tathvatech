package com.tathvatech.timetracker.request;


import com.tathvatech.timetracker.oid.ShiftInstanceOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.ReworkOrderOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.OID.WorkstationOID;

public class OpenCheckinListReportRequest
{
	private ReworkOrderOID[] workorders;
	private UserOID[] users;
	private ProjectOID projectOID;
	private WorkstationOID workstationOID;
	private ShiftInstanceOID shiftInstanceOID;
	
	public ReworkOrderOID[] getWorkorders()
	{
		return workorders;
	}
	public void setWorkorders(ReworkOrderOID[] workorders)
	{
		this.workorders = workorders;
	}
	public UserOID[] getUsers()
	{
		return users;
	}
	public void setUsers(UserOID[] users)
	{
		this.users = users;
	}
	public ProjectOID getProjectOID()
	{
		return projectOID;
	}
	public void setProjectOID(ProjectOID projectOID)
	{
		this.projectOID = projectOID;
	}
	public WorkstationOID getWorkstationOID()
	{
		return workstationOID;
	}
	public void setWorkstationOID(WorkstationOID workstationOID)
	{
		this.workstationOID = workstationOID;
	}
	public ShiftInstanceOID getShiftInstanceOID()
	{
		return shiftInstanceOID;
	}
	public void setShiftInstanceOID(ShiftInstanceOID shiftInstanceOID)
	{
		this.shiftInstanceOID = shiftInstanceOID;
	}
}
