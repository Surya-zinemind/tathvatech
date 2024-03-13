package com.tathvatech.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TaskDefBean {

	private TaskTypes taskType;

	public TaskDefBean(TaskTypes taskType) 
	{
		this.taskType = taskType;
	}

	public TaskTypes getTaskType() {
		return taskType;
	}
	
	public void setTaskType(TaskTypes taskType) {
		this.taskType = taskType;
	}

	/**
	 * TODO:: we need to internationalize this
	 * and even save this in the task table as a description. to display it in the list
	 * now I am using TaskDefBeanFactory to load the bean from the task and then display the description.
	 */
	@JsonIgnore
	public abstract String getDescription();
	
	@JsonIgnore
	public String getDefString() throws Exception {
        ObjectMapper jm = new ObjectMapper();
        return jm.writeValueAsString(this);
	}
}
