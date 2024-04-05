/*
 * Created on Dec 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.util.Comparator;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyItemOrderComparator implements Comparator
{

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object arg0, Object arg1)
    {
        float a = ((SurveyItemBase)arg0).getOrderNum();
        float b = ((SurveyItemBase)arg1).getOrderNum();
        if(a < b)
            return -1;
        else if(a > b)
            return 1;
        else
            return 0;
    }
}
