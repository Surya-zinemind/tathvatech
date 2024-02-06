/*
 * Created on Dec 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.utils;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Sequence
{
    //is the value that was returned for the last getNext() call
    private long lastKey;
    
    // is the maximum value that is fetched from the database.
    //if lastKey == currentMaxVal, new lot should be fetched from the database. 
    private long currentMaxVal;

    /**
     * @param val
     * @param newVal
     */
    public Sequence(long val, long newVal)
    {
        this.lastKey = val;
        this.currentMaxVal = newVal;
    }
    /**
     * @return Returns the currentMaxVal.
     */
    public long getCurrentMaxVal()
    {
        return currentMaxVal;
    }
    /**
     * @param currentMaxVal The currentMaxVal to set.
     */
    public void setCurrentMaxVal(long currentMaxVal)
    {
        this.currentMaxVal = currentMaxVal;
    }
    /**
     * @return Returns the lastKey.
     */
    public long getLastKey()
    {
        return lastKey;
    }
    /**
     * @param lastKey The lastKey to set.
     */
    public void setLastKey(long lastKey)
    {
        this.lastKey = lastKey;
    }
}
