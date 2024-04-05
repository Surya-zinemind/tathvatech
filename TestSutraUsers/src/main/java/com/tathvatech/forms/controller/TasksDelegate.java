package com.tathvatech.forms.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.tathvatech.ts.caf.util.ServiceLocator;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.OID;

public class TasksDelegate {

	public static void saveTask(UserContext userContext,
			OID associatedObjectOid,  OID assignedTo, TaskDefBean taskDef) throws Exception 
	{

        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

            TasksManager.saveTask(userContext, associatedObjectOid,  assignedTo, taskDef);
        }
        catch(Exception ex)
        {
            try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            throw ex;
        }
        finally
        {
            con.commit();
        }
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

        Connection con = null;
        try
        {
            con = ServiceLocator.locate().getConnection();
            con.setAutoCommit(false);

    		TasksManager.markTaskPerformed(context, task, resultStatus);
        }
        catch(Exception ex)
        {
            try {
				con.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            throw ex;
        }
        finally
        {
            con.commit();
        }
	}
	
	
}
