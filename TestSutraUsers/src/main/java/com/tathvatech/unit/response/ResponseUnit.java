/*
 * Created on Mar 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.unit.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ResponseUnit
{
	@JsonIgnore
	public static final int DEFAULT_VAL = -99;
    
	int key1 = DEFAULT_VAL;
    int key2 = DEFAULT_VAL;
    int key3 = DEFAULT_VAL;
    String key4;
    /**
     * @return Returns the key1.
     */
    public int getKey1()
    {
        return key1;
    }
    /**
     * @param key1 The key1 to set.
     */
    public void setKey1(int key1)
    {
        this.key1 = key1;
    }
    /**
     * @return Returns the key2.
     */
    public int getKey2()
    {
        return key2;
    }
    /**
     * @param key2 The key2 to set.
     */
    public void setKey2(int key2)
    {
        this.key2 = key2;
    }
    /**
     * @return Returns the key3.
     */
    public int getKey3()
    {
        return key3;
    }
    /**
     * @param key3 The key3 to set.
     */
    public void setKey3(int key3)
    {
        this.key3 = key3;
    }
    /**
     * @return Returns the key4.
     */
    public String getKey4()
    {
        return key4;
    }
    /**
     * @param key4 The key4 to set.
     */
    public void setKey4(String key4)
    {
        this.key4 = key4;
    }
    
    @JsonIgnore
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("key1:").append(key1);
        sb.append(",key2:").append(key2);
        sb.append(",key3:").append(key3);
        sb.append(",key4:").append(key4);
        
        return sb.toString();
    }
}
