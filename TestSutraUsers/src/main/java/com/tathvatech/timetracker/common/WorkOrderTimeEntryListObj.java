package com.tathvatech.timetracker.common;

import com.tathvatech.timetracker.entity.Workorder;
import com.tathvatech.timetracker.entity.WorkorderTimeEntry;

import java.util.List;

public class WorkOrderTimeEntryListObj
{
	Workorder workorder;
	List<WorkorderTimeEntry> timeEntries;
	
	public Workorder getWorkorder()
	{
		return workorder;
	}
	public void setWorkorder(Workorder workorder)
	{
		this.workorder = workorder;
	}
	public List<WorkorderTimeEntry> getTimeEntries()
	{
		return timeEntries;
	}
	public void setTimeEntries(List<WorkorderTimeEntry> timeEntries)
	{
		this.timeEntries = timeEntries;
	}
	
	
}
