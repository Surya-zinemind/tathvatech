package com.tathvatech.forms.controller;

import com.tathvatech.forms.service.TasksManager;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.TaskDefBean;
import org.springframework.scheduling.config.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



public class TasksDelegate {

	public static void saveTask(UserContext userContext,
                                OID associatedObjectOid, OID assignedTo, TaskDefBean taskDef) throws Exception
	{



            TasksManager.saveTask(userContext, associatedObjectOid,  assignedTo, taskDef);

	}

	public static List<Task> getTasks(UserContext userContext, TaskFilter taskFilter) throws Exception
	{
		return TasksManager.getTasks(userContext, taskFilter);
	}

	public static Task getTask(int taskPk)throws Exception
	{
		return TasksManager.getTask(taskPk);
	}

	public static void markTaskPerformed(UserContext context, Task task,
			TaskStatus resultStatus) throws Exception
	{



    		TasksManager.markTaskPerformed(context, task, resultStatus);



	}
	
	
}
