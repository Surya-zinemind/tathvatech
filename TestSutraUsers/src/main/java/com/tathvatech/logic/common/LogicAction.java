/*
 * Created on Jan 12, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import java.util.Map;
import java.util.Properties;


import com.tathvatech.survey.common.SurveyDefinition;
import org.jdom2.Element;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface LogicAction
{
    public void execute(Properties props);

    /**
     * @param config
     */
    public void setActionConfig(LogicActionConfig config);

    /**
     * @param element
     */
    public void setConfiguration(Element element);

    /**
     * @return
     */
    public Element toXML();

    /**
     * @param parameterMap
     */
    public void setConfiguration(Map parameterMap);

    /**
     * @param sd
     */
    public void setSurveyDefinition(SurveyDefinition sd);
}
