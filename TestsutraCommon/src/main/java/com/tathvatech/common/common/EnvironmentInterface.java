
package com.tathvatech.common.common;

import org.quartz.Scheduler;

import java.sql.Connection;



public interface EnvironmentInterface
{
	public Scheduler getScheduler() throws Exception;
	
//    public DataSource getDataSource();
    public Connection getConnection() throws Exception;
}
