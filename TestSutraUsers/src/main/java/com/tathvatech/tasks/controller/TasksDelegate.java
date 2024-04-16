package com.tathvatech.tasks.controller;

import com.tathvatech.tasks.common.TaskFilter;
import com.tathvatech.tasks.entity.Task;
import com.tathvatech.tasks.enums.TaskStatus;
import com.tathvatech.tasks.service.TasksManager;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.TaskDefBean;


import java.util.List;



public class TasksDelegate {
	private final TasksManager tasksManager;

    public TasksDelegate(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    public  void saveTask(UserContext userContext,
                                OID associatedObjectOid, OID assignedTo, TaskDefBean taskDef) throws Exception
	{



            tasksManager.saveTask(userContext, associatedObjectOid,  assignedTo, taskDef);

	}

	public  List<Task> getTasks(UserContext userContext, TaskFilter taskFilter) throws Exception
	{
		return tasksManager.getTasks(userContext, taskFilter);
	}

	public  Task getTask(int taskPk)throws Exception
	{
		return tasksManager.getTask(taskPk);
	}

	public  void markTaskPerformed(UserContext context, Task task,
			TaskStatus resultStatus) throws Exception
	{



    		tasksManager.markTaskPerformed(context, task, resultStatus);



	}
	
	
}
