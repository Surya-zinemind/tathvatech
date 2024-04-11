/*
 * Created on Oct 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import com.tathvatech.logic.common.LogicAction;
import com.tathvatech.logic.common.LogicActionConfig;
import com.tathvatech.survey.common.SurveyDefinition;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RuleActionManager
{
    private static final Logger logger = LoggerFactory.getLogger( RuleActionManager.class);
    
    private HashMap actionTypesMap = new HashMap();
    private List actionTypesList = new ArrayList();
    private SurveyDefinition sd ;
    
    public static RuleActionManager getInstance(SurveyDefinition sd)
    {
        RuleActionManager ram = new RuleActionManager();
        ram.sd = sd;
        
        return ram;
    }
    
    public LogicAction getAction(String _actionName)
    {
        try
        {
            LogicActionConfig lConfig = (LogicActionConfig)actionTypesMap.get(_actionName);
            String className = lConfig.getTypeClass();
            if (logger.isDebugEnabled())
            {
                logger.debug("Instantiating action class - " + className);
            }
            LogicAction item = (LogicAction)Class.forName(className).newInstance();
            item.setSurveyDefinition(sd);
            item.setActionConfig(lConfig);
            return item;
        }
        catch(Exception ex)
      	{
            logger.error("Could not instantiate Logic Action  " + _actionName + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
      	}
        return null;
    }
    
    public List getAllLogicActions()
    {
        return actionTypesList;
    }
    
    private RuleActionManager()
    {
        loadLogicActions();
    }
    
    private void loadLogicActions()
    {
		try
		{
		    SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File("ruleactions.xml"));
			
			List aList = doc.getRootElement().getChildren("action");
			for (Iterator aListItr = aList.iterator(); aListItr.hasNext();)
            {
                Element aTypeElement = (Element) aListItr.next();
                
                LogicActionConfig type = new LogicActionConfig();
                type.setName(aTypeElement.getAttributeValue("name"));
                type.setDescription(aTypeElement.getAttributeValue("description"));
                type.setTypeClass(aTypeElement.getAttributeValue("typeClass"));
                actionTypesMap.put(aTypeElement.getAttributeValue("name"), type);
                actionTypesList.add(type);
            }
		}
		catch (Throwable e)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
		}
    }
}
