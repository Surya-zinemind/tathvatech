/*
 * Created on Jan 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jdom.Element;

import com.sarvasutra.etest.FormDesignListener;
import com.sarvasutra.etest.FormEventListner;
import com.sarvasutra.etest.TestProcController;
import com.sarvasutra.etest.server.FormResponseContext;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.project.UnitFormQuery;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.response.SurveyResponse;
import com.tathvatech.ts.core.survey.surveyitem.SurveyItemBase;
import com.tathvatech.ts.core.survey.surveyitem.SurveyItemOrderComparator;
import com.thirdi.surveyside.survey.logic.Logic;
import com.vaadin.ui.Component;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class SurveyItem extends SurveyItemBase
{
    private static final Logger logger = Logger.getLogger(SurveyItem.class);

    protected SurveyResponse surveyResponse;

    private FormResponseContext formResponseContext;

    public SurveyItem()
    {

    }

    public SurveyItem(SurveyDefinition _survey)
    {
	this.surveyDef = _survey;
    }

    public void setSurveyResponse(SurveyResponse surveyResponse)
    {
	this.surveyResponse = surveyResponse;
    }

    @Override
    public boolean equals(Object obj)
    {
		if (obj instanceof SurveyItem)
		{
		    if (((SurveyItem) obj).getSurveyItemId().equals(
			    this.getSurveyItemId()))
		    {
			return true;
		    }
		}
		return false;
    }

	@Override
	public int hashCode() 
	{
		return getSurveyItemId().hashCode();
	}

    
	public List getFlagsAsList()
	{
		List flagList = new ArrayList();
		if(flags != null)
		{
			StringTokenizer st = new StringTokenizer(flags, ",");
			while(st.hasMoreTokens())
			{
				String aTok = st.nextToken().trim();
				if(aTok != null && aTok.trim().length() > 0)
				{
					if(flagList.contains(aTok.toUpperCase()))
					{}
					else
					{
						flagList.add(aTok.toUpperCase());
					}
				}
			}
		}
		return flagList;
	}
	
	public String getImageFileName()
	{
		return imageFileName;
	}

	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}

	public String getImageFileDisplayName()
	{
		return imageFileDisplayName;
	}

	public void setImageFileDisplayName(String imageFileDisplayName)
	{
		this.imageFileDisplayName = imageFileDisplayName;
	}

    protected void setSurveyItemConfiguration(Element element)
    {
		this.setSurveyItemId(element.getAttributeValue("id"));
		this.orderNum = Float.parseFloat(element.getAttributeValue("orderNum"));

		if(element.getAttribute("flags") != null)
		{
			flags = element.getAttributeValue("flags");
			this.surveyDef.addFlags(flags);
		}
		
		if(element.getAttribute("imageFileName") != null)
		{
			imageFileName = element.getAttributeValue("imageFileName");
		}

		if(element.getAttribute("imageFileDisplayName") != null)
		{
			imageFileDisplayName = element.getAttributeValue("imageFileDisplayName");
		}

		if (this instanceof Container)
		{
		    // do the recursive call to load child items
		    List c = this.surveyDef.loadSurveyItems(element, this);
		    ((Container) this).setChildren(c);
		}

		this.loadLogicItems(element);
    }

    public abstract Component drawConfigurationView(
	    FormDesignListener formDesignListener);

    public abstract Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener);

    public abstract Component drawResponseField(UnitFormQuery testProc, SurveyResponse response, Component parent, String[] flags,  FormEventListner formEventListner);
    public abstract Component drawResponseDetail(UserContext userContext, UnitFormQuery testProc, SurveyResponse response, Component parent, 
    		boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController);

    /**
     * This text identifies the item uniquely, For a question, it could be the
     * question text, and for a page break, it could be a word "PageBreak"
     * 
     * @return
     */
    public List getLogicList()
    {
	return logicList;
    }

    public void setLogicList(List logicList)
    {
	this.logicList = logicList;
    }

    /**
     * Should be overridden by child classes.
     * 
     * @param logicId
     * @return
     */
    public abstract Logic getLogic(String logicId);

    /**
     * returns the logic with the specified id.
     * 
     * @param logicId
     * @return
     */
    protected Logic getLogicFromList(String logicId)
    {
	if (logicList != null)
	{
	    for (Iterator lIter = logicList.iterator(); lIter.hasNext();)
	    {
		Logic logic = (Logic) lIter.next();
		if (logic.getSurveyItemId().equals(logicId))
		{
		    if (logger.isDebugEnabled())
		    {
			logger.debug("Returning from getLogicFromList(), logic.itemID() - "
				+ logic.getSurveyItemId());
		    }
		    return logic;
		}
	    }
	}
	return null;
    }

    private void loadLogicItems(Element element)
    {
	List returnList = new ArrayList();

	List qList = element.getChildren("logic");

	if (qList != null)
	{
	    for (Iterator iter = qList.iterator(); iter.hasNext();)
	    {
		Element lElement = (Element) iter.next();

		Logic item = new Logic((SurveyDefinition) this.surveyDef);

		item.setConfiguration(lElement);
		returnList.add(item);
	    }
	}
	// sort the list according to the orderNum
	Collections.sort(returnList, new SurveyItemOrderComparator());

	logicList = returnList;
    }

    public FormResponseContext getFormResponseContext()
    {
    	return formResponseContext;
    }
    
	public void setFormResponseContext(FormResponseContext formResponseContext)
	{
		this.formResponseContext = formResponseContext;
	}

}
