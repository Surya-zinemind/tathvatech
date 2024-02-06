
package com.tathvatech.common.common;

import java.sql.Connection;



public interface EnvironmentInterface
{
	public Scheduler getScheduler() throws Exception;
	
//    public DataSource getDataSource();
    public Connection getConnection() throws Exception;
}
