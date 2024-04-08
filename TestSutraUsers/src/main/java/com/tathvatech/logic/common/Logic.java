/*
 * Created on Jan 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.common;

import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.common.SurveyItem;
import com.tathvatech.survey.inf.LogicBase;
import com.tathvatech.survey.inf.LogicIntf;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.user.common.UserContext;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Logic extends SurveyItem implements LogicIntf, LogicBase
{
    private static final Logger logger = LoggerFactory.getLogger(Logic.class);
    
    private List conditionList = new ArrayList();
    private LogicAction action;
    
    public Logic(SurveyDefinition sd)
    {
        super(sd);
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        if(conditionList == null || conditionList.size() == 0)
        {
            sb.append("<b>For all responses</b> <br>");
        }
        else
        {
            sb.append("<b>If</b> <br> ");
            for (Iterator iter = conditionList.iterator(); iter.hasNext();) 
            {
				Condition aCondition = (Condition) iter.next();
	        	sb.append(aCondition.toString() + "<br>");
			}
            sb.append("<b>Then</b> <br>");
        }
        
        if(action == null)
        {
            sb.append("No action defined. <br>");
        }
        else
        {
            sb.append(action.toString());
        }
        
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#toXML()
     */
    public Element toXML()
    {
        Element element = new Element("logic");

        super.toXML(element);

        //XMLize the condition elements associated to the logic
        if(conditionList != null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("conditionList size for " + this.getIdentifier() + " - " + conditionList.size());
            }
            for (Iterator iter = conditionList.iterator(); iter.hasNext();)
            {
                Condition condition = (Condition) iter.next();
            	Element elm = condition.toXML();
            	if(elm != null)
            	{
            	    element.addContent(elm);
            	}
            }
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("conditionList for " + this.getIdentifier() + " is empty ");
            }
        }

        if(action != null)
        {
            Element actionElement = action.toXML();
            if(actionElement != null)
            {
                element.addContent(actionElement);
            }
        }
        return element;
    }
    
    /**
     * @return Returns the action.
     */
    public LogicAction getAction()
    {
        return action;
    }
    /**
     * @param action The action to set.
     */
    public void setAction(LogicAction action)
    {
        this.action = action;
    }

    /**
     * @return Returns the condition list associated with this logic
     */
    public List getConditionList()
    {
        return conditionList;
    }

    /**
     * @return
     */
    public void setConditionList(List conditionList)
    {
        this.conditionList = conditionList;
    }


    public String drawConfigurationForm()
    {
        return "";
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#setConfiguration(java.util.Map)
     */
    public void setConfiguration(Map paramMap)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Inside setConfiguration");
        }
    }

	public void setConfiguration(List<List<String>> fileContents)
	{
		//nothing to do here.
	}

	/* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#setConfiguration(org.jdom.Element)
     */
    public void setConfiguration(Element element)
    {
        super.setSurveyItemConfiguration(element);
        
        loadConditionItems(element);
        
        loadActionItems(element);
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#getTypeName()
     */
    public String getTypeName()
    {
        return "Logic";
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#getIdentifier()
     */
    public String getIdentifier()
    {
        return "Logic";
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#getLogic(java.lang.String)
     */
    public Logic getLogic(String logicId)
    {
        return null;
    }

    private void loadConditionItems(Element element)
    {
        List returnList = new ArrayList();

        List qList = element.getChildren("condition");
        
        if (qList != null)
        {
            for (Iterator iter = qList.iterator(); iter.hasNext();)
            {
                Element qElement = (Element) iter.next();
                String subjectId = qElement.getAttributeValue("subject");
                String subjectType = qElement.getAttributeValue("subjectType");

                if (logger.isDebugEnabled())
                {
                    logger.debug("SubjectId - " + subjectId);
                    logger.debug("SubjectTypeName - " + subjectType);
                }
                LogicConfigurationManager lcm = new LogicConfigurationManager((SurveyDefinition) surveyDef);
                
                Condition item = lcm.getCondition(subjectType, subjectId);
                if(item != null)
                {
                    item.setConfiguration(qElement);
                    returnList.add(item);
                }
            }
        }
        
        conditionList = returnList;
    }
    
    private void loadActionItems(Element element)
    {
        List actionList = element.getChildren("action"); 
        for (Iterator iterator = actionList.iterator(); iterator.hasNext();)
        {
            Element actionElement = (Element) iterator.next();
            String actionName = actionElement.getAttributeValue("actionType");

            if (logger.isDebugEnabled())
            {
                logger.debug("ActionName - " + actionName);
            }
            LogicAction lAction = RuleActionManager.getInstance((SurveyDefinition) this.surveyDef).getAction(actionName);
            lAction.setConfiguration(actionElement);

            if (logger.isDebugEnabled())
            {
                logger.debug("action - " + lAction);
            }
            
            //for now there is only one action supported. so just take the first one and break
            this.action = lAction;
            break;
            
        }
    }
    /**
     * @param condition
     */
    public void addCondition(Condition condition)
    {
        this.conditionList.add(condition);
    }


	@Override
	public Component drawConfigurationView(FormDesignListener formDesignListener)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse,
                                       Component parent, String[] flags, FormEventListner formEventListner)
	{
	    // TODO Auto-generated method stub
	    return null;
	}


	@Override
	public Component drawResponseDetail(UserContext userContext, UnitFormQuery testProc, SurveyResponse sResponse,
                                        Component parent, boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController)
	{
	    // TODO Auto-generated method stub
	    return null;
	}
}
