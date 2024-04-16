/*
 * Created on Jan 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.intf.LogicAnchor;
import com.tathvatech.survey.intf.SurveySaveItemBase;
import com.tathvatech.survey.response.SurveyItemResponse;
import org.jdom2.Element;

import java.util.List;
import java.util.Map;
import java.util.Properties;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SurveySaveItem extends SurveyItem implements SurveySaveItemBase, LogicAnchor
{

	public static final int NONE_SELECTED_VALUE = 0;

	protected boolean hidden = false;
    /**
     * 
     */
    public SurveySaveItem()
    {
        super();
    }
    /**
     * @param _survey
     */
    public SurveySaveItem(SurveyDefinition _survey)
    {
        super(_survey);
    }

    /**
     * @return Returns the isRequired.
     */
    public abstract boolean isRequired();

    /**
     * @return
     */
    public abstract AnswerPersistor getPersistor();

    /**
     * @param answer
     * @return
     */
    public abstract AnswerPersistor getPersistor(SurveyItemResponse answer);

    /**
     * @return
     */
    public abstract int getDataType();
    
    /**
     * @param paramMap
     * @return
     * @throws
     */
    public abstract SurveyItemResponse getResponse(Map paramMap, Properties props) throws InvalidResponseException;
	
    public boolean isHidden() 
	{
		return hidden;
	}
	
    public void setHidden(boolean hidden) 
    {
		this.hidden = hidden;
	}
    
    protected void setSurveyItemConfiguration(Element element)
    {
        super.setSurveyItemConfiguration(element);
        
        if(element.getAttributeValue("hidden") != null)
        {
        	this.hidden = new Boolean(element.getAttributeValue("hidden")).booleanValue();
        }
        else
        {
        	hidden = false;
        }
    }

    protected Element toXML(Element qElement)
    {
        super.toXML(qElement);
        
        qElement.setAttribute("hidden", new Boolean(hidden).toString());

        return qElement;
    }
    
    public abstract Element writeResponseXML(SurveyItemResponse itemResponse)throws Exception;
	
    public abstract SurveyItemResponse captureResponse() throws InvalidResponseException;
    
	public abstract List<String> validateResponse(SurveyItemResponse sItemResponse);

}
