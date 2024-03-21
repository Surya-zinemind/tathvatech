package com.tathvatech.survey.inf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.tathvatech.ts.core.survey.SurveyDefinitionBase;

public abstract class SurveyItemBase {
	
	private static Logger logger = Logger.getLogger(SurveyItemBase.class);
	
    private SurveyItemBase parent;
    
    protected String surveyItemId;
    private SurveyItemType itemType;
    protected float orderNum;
    protected String flags;
    protected String imageFileName = null;
    protected String imageFileDisplayName = null;
    protected List logicList = new ArrayList(); // stores the list of logic
    // associated with the question

    protected SurveyDefinitionBase surveyDef;
    
    /**
     * when overriding this method, setSurveyItemConfiguration(Element) should
     * be called.
     * 
     * @param element
     */
    public abstract void setConfiguration(Element element);

    public abstract void setConfiguration(List<List<String>> fileContents);

    public abstract Element toXML();

    public SurveyItemType getSurveyItemType()
    {
    	return itemType;
    }

    /**
     * @param itemType
     */
    public void setSurveyItemType(SurveyItemType itemType)
    {
    	this.itemType = itemType;
    }

    public abstract String getIdentifier();

    public String getShortIdentifier()
    {
	String identifier = getIdentifier();
	if (identifier != null && identifier.length() > 80)
	{
	    identifier = identifier.substring(0, 77);
	    identifier = identifier + "...";
	}
	return identifier;
    }

    public abstract String getTypeName();

    public SurveyItemBase getParent()
	{
		return parent;
	}

	public void setParent(SurveyItemBase parent)
	{
		this.parent = parent;
	}

    /**
     * 
     */
    public SurveyDefinitionBase getSurveyDefinition()
    {
    	return surveyDef;
    }

    public void setSurveyDefinition(SurveyDefinitionBase survey)
    {
    	this.surveyDef = survey;
    }

	/**
     * toXML for the parameters of SurveyItem, should be called by child classes
     * when overriding toXML(Element)
     * 
     * @param qElement
     * @return
     */
    protected Element toXML(Element qElement)
    {
    	if (surveyItemId != null)
    	{
    		qElement.setAttribute("id", surveyItemId);
    	}
	
    	if (flags != null)
    	{
    		qElement.setAttribute("flags", flags);
    	}

    	if (imageFileName != null)
    	{
    		qElement.setAttribute("imageFileName", imageFileName);
    	}

    	if (imageFileDisplayName != null)
    	{
    		qElement.setAttribute("imageFileDisplayName", imageFileDisplayName);
    	}

    	qElement.setAttribute("orderNum", Float.toString(orderNum));

		qElement.setAttribute("type", getTypeName());

		// XMLize the logic elements associated to the question
		if (logicList != null)
		{
		    if (logger.isDebugEnabled())
		    {
			logger.debug("logicList size for " + this.getIdentifier()
				+ " - " + logicList.size());
		    }
		    for (Iterator iter = logicList.iterator(); iter.hasNext();)
		    {
		    	LogicIntf logic = (LogicIntf) iter.next();
		    	Element elm = logic.toXML();
			qElement.addContent(elm);
		    }
		}

		// toXML the children
		if (this instanceof ContainerBase)
		{
		    ContainerBase con = (ContainerBase) this;
		    if (con.getChildren() != null && con.getChildren().size() > 0)
		    {
				for (Iterator iterator = con.getChildren().iterator(); iterator
					.hasNext();)
				{
				    SurveyItemBase aItem = (SurveyItemBase) iterator.next();
				    Element cElem = aItem.toXML();
				    qElement.addContent(cElem);
				}
		    }
		}

		return qElement;
    }

    /**
     * @return Returns the surveyItemId.
     */
    public String getSurveyItemId()
    {
	return surveyItemId;
    }

    /**
     * @param surveyItemId
     *            The surveyItemId to set.
     */
    public void setSurveyItemId(String surveyItemId)
    {
	this.surveyItemId = surveyItemId;
    }

	/**
     * @return Returns the orderNum.
     */
    public float getOrderNum()
    {
    	return orderNum;
    }

    /**
     * @param orderNum
     *            The orderNum to set.
     */
    public void setOrderNum(float orderNum)
    {
    	this.orderNum = orderNum;
    }
    
    public String getFlags()
	{
		return flags;
	}

	public void setFlags(String flags)
	{
		this.flags = flags;
	}

    
}
