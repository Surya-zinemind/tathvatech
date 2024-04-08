package com.tathvatech.tasks.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.TaskDefBean;
import com.tathvatech.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.config.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
public class TasksManager {
	private final PersistWrapper persistWrapper;

	public  void saveTask(UserContext userContext,
								OID associatedObjectOid, OID assignedTo, TaskDefBean taskDef) throws Exception
	{
        Task task = new Task();
        if(assignedTo != null)
        {
        	task.setAssignedToPk(assignedTo.getPk());
        	task.setAssignedToType(assignedTo.getEntityType().getValue());
        }
        task.setTaskDef(taskDef.getDefString());
        task.setTaskType(taskDef.getTaskType().getValue());
        task.setObjectPk(associatedObjectOid.getPk());
        task.setObjectType(associatedObjectOid.getEntityType().getValue());
        task.setStatus(TaskStatus.New.getValue());
        

		if(task.getPk() == 0)
		{
	        task.setCreatedBy(userContext.getUser().getPk());
	        task.setCreatedDate(new Date());
			persistWrapper.createEntity(task);
		}
		else
		{
			persistWrapper.update(task);
		}
	}

	public  Task getTask(int taskPk)throws Exception
	{
		return persistWrapper.readByPrimaryKey(Task.class, taskPk);
	}
	
	public  List<Task> getTasks(UserContext userContext, TaskFilter taskFilter) throws Exception
	{
    	if(taskFilter == null)
    		taskFilter = new TaskFilter();
    	if(taskFilter.status == null || taskFilter.status.length == 0)
    		taskFilter.status = new TaskStatus[]{TaskStatus.New, TaskStatus.Attended, TaskStatus.InProgress};
    	
    	if(taskFilter.assignedToOID == null)
    	{
    		taskFilter.assignedToOID = userContext.getUser().getOID();
    	}

    	List<Object> params = new ArrayList();
		StringBuffer sb = new StringBuffer("select * from task where 1 = 1 ");
		
        String sep = "";
        sb.append(" and status in (");
        for (int i = 0; i < taskFilter.status.length; i++) 
        {
			sb.append(sep).append(taskFilter.status[i].getValue());
			sep = ", ";
		}
        sb.append(")");
        
		if(taskFilter.taskType != null)
		{
			sb.append(" and taskType = ?");
			params.add(taskFilter.taskType.getValue());
		}
		if(taskFilter.createdBy != 0)
		{
			sb.append(" and createdBy = ?");
			params.add(taskFilter.createdBy);
		}
		if(taskFilter.assignedToOID != null)
		{
	        if(User.USER_PRIMARY.equals(userContext.getUser().getUserType()))
	        {
	        	sb.append(" and (assignedToPk is null or (assignedToPk = ? and assignedToType = ?))"); // new user requests have assigned to as null
				params.add(taskFilter.assignedToOID.getPk());
				params.add(taskFilter.assignedToOID.getEntityType().getValue());
	        }
	        else
	        {
				sb.append(" and assignedToPk = ? and assignedToType = ?");
				params.add(taskFilter.assignedToOID.getPk());
				params.add(taskFilter.assignedToOID.getEntityType().getValue());
	        }
		}
		if(taskFilter.objectOID != null)
		{
			sb.append(" and objectPk = ? and objectType = ?");
			params.add(taskFilter.objectOID.getPk());
			params.add(taskFilter.objectOID.getEntityType().getValue());
		}
		
		sb.append(" order by createdDate desc");
		
		return persistWrapper.readList(Task.class,
				sb.toString(), params.toArray(new Object[params.size()]));
	}

	public  void markTaskPerformed(UserContext context, Task task,
			TaskStatus resultStatus) 
	{
		task.setStatus(resultStatus.getValue());
		task.setPerformedBy(context.getUser().getPk());
		task.setPerformedDate(new Date());

		persistWrapper.update(task);
	}

}
