/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.common;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

import com.tathvatech.ts.caf.core.envr.EnvironmentInterface;


public class ServiceAdaptor implements EnvironmentInterface
{
	private static Logger logger = Logger.getLogger(ServiceAdaptor.class);

	private static ServiceAdaptor instance;

	ThreadLocal<MyConnection> connection = new ThreadLocal<MyConnection>()
	{
		protected MyConnection initialValue()
		{
			Connection conn;
			try
			{
				conn = ds.getConnection();
				MyConnection myCon = new MyConnection(conn);
				return myCon;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException("Could not get database connection");
			}
		};
	};
	
	private static InitialContext ctx = null;
	private static DataSource ds;
	
	private SchedulerFactory schedulerFactory;
	
	public static ServiceAdaptor getInstance()
	{
		if (instance == null)
		{
			synchronized (com.tathvatech.ts.caf.util.ServiceAdaptor.class)
			{
				if (instance == null)
				{
			        ServiceAdaptor.instance = new ServiceAdaptor();
			        
			        //initialize the datasource
			        try
					{
						instance.initializeDataSource();
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException();
					}
				}
			}
		}
		return instance;
	}

	private void initializeDataSource()throws Exception
	{
	    String dbRef = "java:comp/env/jdbc/testsutra";
		try
		{
			ctx = new InitialContext();
	        ds = (DataSource)ctx.lookup(dbRef);
//	        ds = (DataSource)ctx.lookup("java:comp/env/jdbc/SurveyMainDB");

		    logger.info("Datasource initialialized successfully.");
		}
		catch (Exception e)
		{
		    logger.error("Initializing datasource failed. application will not run" + " :: "+ e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
			throw new Exception();
		}
	}

    /* (non-Javadoc)
     * @see com.tathvatech.ts.caf.core.envr.EnvironmentInterface#getScheduler()
     */
    public Scheduler getScheduler() throws Exception
    {
        Scheduler sch = null;
        try
        {
//        	SchedulerFactory schF = (SchedulerFactory)ctx.lookup("java:comp/env/bean/SchedulerFactory");
        	sch = schedulerFactory.getScheduler();
        }
        catch(Exception ex)
        {
			logger.warn(ex + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
        }
        return sch;
    }
	

    public static void init()throws Exception
    {
        ServiceAdaptor.getInstance();
    }

//	@Override
//	public DataSource getDataSource()
//	{
//		return ds;
//	}
	
	public Connection getConnection()throws Exception
	{
		MyConnection conn = connection.get();
		if(conn.isClosed())
		{
			conn = new MyConnection(ds.getConnection());
			connection.set(conn);
		}
		return conn;
	}
	
	public void closeConnection()throws Exception
	{
		Connection conn = getConnection();
		((MyConnection) conn).reallyCloseConnection();
	}
}
