/*
 * Created on Oct 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.enums;

import java.sql.Connection;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AnswerPersistor
{
    /**
     * @return Returns the answer.
     */
    public Object getAnswer();


    /**
     * return the sqls for persisting the answer
     * @param responseId
     * @return
     */
    public String[] getPersistSql(long responseId);

    /**
     * returns sql to delete the response for this perticular response for a given responseId
     * @param responseId
     * @return
     */
    public String[] getDeleteResponseSQL(long responseId);


	/**
	 * Persists the answer into the store
	 * @param conn
	 * @param responseId
	 */
	public void persistAnswer(Connection conn, int responseId) throws Exception;
}
