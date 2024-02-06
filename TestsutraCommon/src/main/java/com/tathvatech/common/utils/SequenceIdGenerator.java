/*
 * Created on Dec 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.utils;

import com.tathvatech.common.wrapper.PersistWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SequenceIdGenerator
{
    private final PersistWrapper persistWrapper;

    private static final Logger logger = LoggerFactory.getLogger(SequenceIdGenerator.class);

    public static final String ACCOUNT = "Account";
    public static final String SUPPORT_TICKET = "Ticket";
    public static final String DEVICE = "Device";
    public static final String SURVEY = "Survey";
    public static final String SURVEYFILE = "SurveyFile";
    public static final String QUESTION = "Question";
    public static final String RESPONSE = "Response";
    public static final String LOGIC = "Logic";
    public static final String REPORTFILE = "ReportFile";
    
    private static String[] validKeys = new String[]{"Account", "Ticket", "Device", "Survey", "SurveyFile", "Question", "Response", "Logic", "ReportFile"};

    public SequenceIdGenerator(PersistWrapper persistWrapper) {
        this.persistWrapper = persistWrapper;
    }

    /**
     * returns the next sequence id for the key.
     * A seperate sequence is maintained for each key
     * The returned key will be encrypted by default. 
     * @param sequenceKey
     * @return
     */
    public static String getNext(String sequenceKey)throws Exception
    {
        return getNext(sequenceKey, false);
    }

    /**
     * returns the next sequence id for the key.
     * A seperate sequence is maintained for each key 
     * @param sequenceKey
     * @param encrypt specifies if the id should be encrypted
     * @return
     */
    public static String getNext(String sequenceKey, boolean encrypt)throws Exception
    {
        long id ;

        boolean isValid = isValidKey(sequenceKey);
        if(isValid)
        {
        	id = getNextSequence(sequenceKey, null, null, null, null);
        }
        else
        {
            Thread.sleep(1);
            id = new java.util.Date().getTime();
        }
        
        if(encrypt)
        {
            return OnewayEncryptUtils.encryptSeqId(Long.toString(id));
        }
        else
        {
            return Long.toString(id);
        }
    }
    
    /**
     * returns if the sequenceKey is registered. all registered keys are maintained in the array
     * @param key
     * @return
     */
    private static boolean isValidKey(String key)
    {
        for(int i=0; i<validKeys.length; i++)
        {
            if(validKeys[i].equals(key))
            {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * for now I am doing a static synchronized method.. this is bad. we need to do row level locking to get this done.
     * @param sequenceKey
     * @param param1
     * @param param2
     * @param param3
     * @param param4
     * @return
     * @throws Exception
     */
    public synchronized int getNextSequence(String sequenceKey, String param1, String param2, String param3, String param4) throws Exception
    {
        try
        {
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("sequenceKey = '").append(sequenceKey).append("'");
            if(param1 != null)
            {
            	whereClause.append(" and param1 = '").append(param1).append("'");
            }
            if(param2 != null)
            {
            	whereClause.append(" and param2 = '").append(param2).append("'");
            }
            if(param3 != null)
            {
            	whereClause.append(" and param3 = '").append(param3).append("'");
            }
            if(param4 != null)
            {
            	whereClause.append(" and param4 = '").append(param4).append("'");
            }
            
            Integer val = persistWrapper.read(Integer.class, "select value from TAB_KEYSEQUENCE where " + whereClause.toString(), null);
            if(val == null)
            {
                persistWrapper.executeUpdate("insert into TAB_KEYSEQUENCE (sequenceKey, param1, param2, param3, param4, value) "
            			+ "values (?, ?, ?, ?,?, ?)", sequenceKey, param1, param2, param3, param4, 1);
            	return 1;
            }
            else
            {
        	    int rowUpdated = persistWrapper.executeUpdate("update TAB_KEYSEQUENCE set value= value+1 where " + whereClause.toString(), null);
        	    if(rowUpdated == 0)
        	    	throw new Exception("Unable to get the next sequence for sequenceKey:" + sequenceKey);
        	    int returnKey = persistWrapper.read(Integer.class, "select value from TAB_KEYSEQUENCE where " + whereClause.toString(), null);
        	    return returnKey;
            }
        }
        catch(Exception ex)
        {
            logger.error("Unable to get the next sequence for sequenceKey:" + sequenceKey, ex);
            throw ex;
        }
    }

    /**
     * @param sequenceKey
     * @param param1
     * @param param2
     * @param param3
     * @param param4
     * @return
     * @throws Exception
     */
    public synchronized Integer getCurrentSequence(String sequenceKey, String param1, String param2, String param3, String param4) throws Exception
    {
        try
        {
            StringBuffer whereClause = new StringBuffer();
            whereClause.append("sequenceKey = '").append(sequenceKey).append("'");
            if(param1 != null)
            {
            	whereClause.append(" and param1 = '").append(param1).append("'");
            }
            if(param2 != null)
            {
            	whereClause.append(" and param2 = '").append(param2).append("'");
            }
            if(param3 != null)
            {
            	whereClause.append(" and param3 = '").append(param3).append("'");
            }
            if(param4 != null)
            {
            	whereClause.append(" and param4 = '").append(param4).append("'");
            }
            
            Integer val = persistWrapper.read(Integer.class, "select value from TAB_KEYSEQUENCE where " + whereClause.toString(), null);
            return val;
        }
        catch(Exception ex)
        {
            logger.error("Unable to get the next sequence for sequenceKey:" + sequenceKey, ex);
            throw ex;
        }
    }

}
