package com.tathvatech.tasks.enums;

public enum TaskStatus {
	New,
	Attended,
	InProgress,
	Completed,
	Rejected
	;
	
	public int getValue()
	{
		return ordinal();
	}

	public static TaskStatus valueOf(int value)
	{
		return TaskStatus.values()[value];
	}
}
