/*
 * Created on Feb 6, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LoginLoggerJob extends TimerTask
{
    private static final Logger logger = LoggerFactory.getLogger(LoginLoggerJob.class);
    private LoginLogInfo loginLog;
    
    /**
     * start the process. the main method will call this function
     * @return
     */
    public LoginLoggerJob(LoginLogInfo loginLog)
    {
    	this.loginLog = loginLog;
    }
    public void run() 
    {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = CoreSqls.logLogin;
        if (logger.isDebugEnabled())
        {
            logger.debug("Sql is - " + sql);
        }
        try
        {
        	conn = ServiceLocator.locate().getConnection();
        	stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, loginLog.getAccountPk());
        	stmt.setInt(2, loginLog.getUserPk());
        	stmt.setString(3, loginLog.getIpAddress());
        	stmt.setString(4, "AU");
        	stmt.setObject(5, new java.sql.Timestamp(loginLog.getLoginTime().getTime()));
        	stmt.setString(6, loginLog.getComments());
        	
        	stmt.execute();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
            logger.error("Could not log login for - " + loginLog.toString() + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
        }
        finally
        {
            try
            {
                if(stmt != null)
                {
                    stmt.close();
                }
                if(conn != null)
                {
        			// force close the connection
        			if(conn instanceof MyConnection)
        				((MyConnection) conn).reallyCloseConnection();
        			conn.close();
                }
            }
            catch(Exception e)
            {
            }
        }
    }
}
