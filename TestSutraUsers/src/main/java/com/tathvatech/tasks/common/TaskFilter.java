package com.tathvatech.tasks.common;

import com.tathvatech.ts.core.common.OID;


public class TaskFilter {
	int createdBy;
	TaskTypes taskType;
	OID objectOID; //associated To object pk .. optional
	OID assignedToOID; // user/role etc.. in the future.
	TaskStatus[] status;
}
