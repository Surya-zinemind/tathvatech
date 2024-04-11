/*
 * Created on Mar 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import java.util.Map;

import com.tathvatech.survey.inf.SurveyItemBase;

import org.jdom2.Element;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Condition
{
    /**
     * @return Returns the subject.
     */
    public String getSubjectId();
    
    public SurveyItemBase getSubject();

    public Element toXML();

    /**
     * @param element
     */
    public void setConfiguration(Element element);

    public String drawConfigurationForm();

    /**
     * @param paramMap
     */
    public void setConfiguration(Map paramMap);
}
