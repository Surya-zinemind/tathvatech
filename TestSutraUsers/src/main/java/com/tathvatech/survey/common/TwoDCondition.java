/*
 * Created on Mar 5, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.surveyitem.SurveyItemBase;
import com.thirdi.surveyside.survey.DataTypes;
import com.thirdi.surveyside.survey.surveyitem.SurveySaveItem;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TwoDCondition implements Condition
{
    private static final Logger logger = Logger.getLogger(TwoDCondition.class);
    
    protected SurveyDefinition surveyDef;

    protected String subjectId;
    protected String keyId;
    protected String operator;
    protected String value;
    
    public TwoDCondition(SurveyDefinition surveyDef)
    {
        this.surveyDef = surveyDef;
    }
    
//    public SimpleCondition(String subject, String operator, String value)
//    {
//        this.subject = subject;
//        this.operator = operator;
//        this.value = value;
//    }

    /**
     * @return Returns the subjectId.
     */
    public String getSubjectId()
    {
        return subjectId;
    }
    /**
     * @param subjectId The subjectId to set.
     */
    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }
    
    public String getKeyId() 
    {
		return keyId;
	}

	public void setKeyId(String keyId) 
	{
		this.keyId = keyId;
	}

	/**
     * 
     * @return Returns the subject of this condition. it is the same subject referred by the getSubjectId call
     */
    public SurveyItemBase getSubject()
    {
    	return this.surveyDef.getQuestion(subjectId);
    }
    
    /**
     * @return Returns the operator.
     */
    public String getOperator()
    {
        return operator;
    }
    /**
     * @param operator The operator to set.
     */
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    /**
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value)
    {
        this.value = value;
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

        String[] kVal = (String[])paramMap.get("keyId");
        if(kVal != null)
        {
            keyId = kVal[0];
        }

        String[] cVal = (String[])paramMap.get("operator");
        if(cVal != null)
        {
            operator = cVal[0];
        }

        String[] val = (String[])paramMap.get("value");
        String theVal = null;
        if(val != null)
        {
        	theVal = val[0];
        }	
        if(theVal == null || theVal.trim().length() == 0)
        {
        	throw new AppException("MSG-valueRequiredForCondition");
        }
    	SurveySaveItem sItem = (SurveySaveItem)this.getSubject();
    	if(DataTypes.DATATYPE_INTEGER == sItem.getDataType())
    	{
    		try
    		{
    			Double.parseDouble(theVal);
    		}
    		catch(Exception ex)
    		{
            	throw new AppException("MSG-invalidValueForCondition");
    		}
    	}
        value = theVal;
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#setConfiguration(org.jdom.Element)
     */
    public void setConfiguration(Element element)
    {
        this.setSubjectId(element.getAttributeValue("subject"));
        this.setKeyId(element.getAttributeValue("keyId"));
        this.setOperator(element.getAttributeValue("operator"));
        this.setValue(element.getAttributeValue("value"));
    }

    public Element toXML()
    {
        //write the condition to xml only of the surveyItem referred by the subject exists.
        //thus if a question is later deleted after having a logic refer to it, that condition
        //will be automatically removed.
        String subjectType = null;
        SurveyItemBase sItem = surveyDef.getQuestion(subjectId);

        Element element = new Element("condition");

        if(subjectId != null)
        {
            element.setAttribute("subject", subjectId);
        }
        else
        {
            element.setAttribute("subject", "unknown");
        }
        
        if(sItem != null)
        {
            subjectType = sItem.getTypeName();
            element.setAttribute("subjectType", subjectType);
        }
        else
        {
            element.setAttribute("subjectType", "unknown");
        }

        if(keyId != null)
        {
            element.setAttribute("keyId", keyId);
        }
        else
        {
            element.setAttribute("keyId", "unknown");
        }

        if(operator != null)
        {
            element.setAttribute("operator", operator);
        }
        if(value != null)
        {
            element.setAttribute("value", value);
        }
        return element;
        
    }
}
