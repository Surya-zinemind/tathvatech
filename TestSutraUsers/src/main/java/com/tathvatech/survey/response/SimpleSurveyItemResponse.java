/*
 * Created on Mar 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleSurveyItemResponse implements SurveyItemResponse
{
    List<ResponseUnit> responseUnitList = new ArrayList<ResponseUnit>();
    
    /**
     * @param unit
     */
    public void addResponseUnit(ResponseUnit unit)
    {
        responseUnitList.add(unit);
    }
    
    public void updateResponseUnit(int key1, ResponseUnit unit)
    {
    	boolean updated = false;
		ResponseUnit toRemove = null;
    	for (Iterator iterator = responseUnitList.iterator(); iterator.hasNext();) {
			ResponseUnit aUnit = (ResponseUnit) iterator.next();
			if(aUnit.getKey1() == key1 || (key1==0 && aUnit.getKey1()==-99))
			{
				if(unit != null)
				{
					aUnit.key2 = unit.key2;
					aUnit.key3 = unit.key3;
					aUnit.key4 = unit.key4;
				}
				else
				{
					toRemove = aUnit;
				}
				updated = true;
			}
		}
    	if(updated == false && unit != null)
    	{
    		addResponseUnit(unit);
    	}
		if(toRemove != null)
			responseUnitList.remove(toRemove);
    }

    public void updateResponseUnit(int key1, int key2, ResponseUnit unit)
    {
    	boolean updated = false;
		ResponseUnit toRemove = null;
    	for (Iterator iterator = responseUnitList.iterator(); iterator.hasNext();) 
    	{
			ResponseUnit aUnit = (ResponseUnit) iterator.next();
			if((aUnit.getKey1() == key1 || (key1==0 && aUnit.getKey1()==-99)) && 
					(aUnit.getKey2() == key2 || (key2==0 && aUnit.getKey2()==-99)))
			{
				if(unit != null)
				{
					aUnit.key3 = unit.key3;
					aUnit.key4 = unit.key4;
				}
				else
				{
					toRemove = aUnit;
				}
				updated = true;
			}
		}
    	if(updated == false && unit != null)
    	{
    		addResponseUnit(unit);
    	}
		if(toRemove != null)
			responseUnitList.remove(toRemove);
    }

    public void updateResponseUnit(int key1, int key2, int key3, ResponseUnit unit)
    {
    	boolean updated = false;
		ResponseUnit toRemove = null;
    	for (Iterator iterator = responseUnitList.iterator(); iterator.hasNext();) {
			ResponseUnit aUnit = (ResponseUnit) iterator.next();
			if((aUnit.getKey1() == key1 || (key1==0 && aUnit.getKey1()==-99)) && 
					(aUnit.getKey2() == key2 || (key2==0 && aUnit.getKey2()==-99)) && 
					(aUnit.getKey3() == key3 || (key3==0 && aUnit.getKey3()==-99)))
			{
				if(unit != null)
				{
					aUnit.key4 = unit.key4;
					updated = true;
				}
				else
				{
					toRemove = aUnit;
				}
			}
		}
    	if(updated == false && unit != null)
    	{
    		addResponseUnit(unit);
    	}
		if(toRemove != null)
			responseUnitList.remove(toRemove);
    }

    public List<ResponseUnit> getResponseUnits()
    {
        return responseUnitList;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator iter = responseUnitList.iterator(); iter.hasNext();)
        {
            ResponseUnit aResponse = (ResponseUnit) iter.next();
            sb.append(aResponse.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
