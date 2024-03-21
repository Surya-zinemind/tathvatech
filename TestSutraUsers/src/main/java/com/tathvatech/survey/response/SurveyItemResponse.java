/*
 * Created on Mar 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.response;

import java.util.List;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SurveyItemResponse
{
    public List<ResponseUnit> getResponseUnits();

    /**
     * @param unit
     */
    public void addResponseUnit(ResponseUnit unit);
    
    public void updateResponseUnit(int key1, ResponseUnit unit);
    public void updateResponseUnit(int key1, int key2, ResponseUnit unit);
    public void updateResponseUnit(int key1, int key2, int key3, ResponseUnit unit);
}
