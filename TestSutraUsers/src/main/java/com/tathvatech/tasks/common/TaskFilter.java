package com.tathvatech.tasks.common;


import com.tathvatech.forms.enums.TaskTypes;
import com.tathvatech.tasks.enums.TaskStatus;
import com.tathvatech.user.OID.OID;


public class TaskFilter {
	public int createdBy;
	public TaskTypes taskType;
	public OID objectOID; //associated To object pk .. optional
	public OID assignedToOID; // user/role etc.. in the future.
	public TaskStatus[] status;
}
