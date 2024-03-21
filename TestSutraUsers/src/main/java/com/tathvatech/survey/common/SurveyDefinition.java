/*
 * Created on Sep 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.inf.SurveyItemBase;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


/**
 * @author Hari
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SurveyDefinition implements SurveyDefinitionBase
{
   private static final Logger logger = LoggerFactory.getLogger(SurveyDefinition.class);
    
    public static final String DB_SUFFIX = "_desc";
    public static final String TEXT_SUFFIX = "_text";
    public static final int EOSValue = Integer.MAX_VALUE;

    private Survey surveyConfig;

    protected List surveyItems = new ArrayList();
    protected List surveyParams = new ArrayList();
    File formInstructionFile;
    String formInsructionFileDisplayName;
    
    private List flags = new ArrayList();

    public SurveyDefinition()
    {

    }

   /* public SurveyDefinition(Survey survey, Element rootElement)
    {
        this.surveyConfig = survey;

        List items = loadSurveyItems(rootElement, null);
        this.surveyItems = items;
        
        loadSurveyParams(rootElement);
        
        loadFormInstruction(rootElement);
    }
    
    public Survey getSurveyConfig()
    {
        return surveyConfig;
    }

    public String getSurveyName()
    {
        return surveyConfig.getIdentityNumber();
    }
    
    public File getFormInstructionFile()
    {
    	return this.formInstructionFile;
    }
    
    public void setFormInstructionFile(File file)
    {
    	this.formInstructionFile = file;
    }
    
    public String getFormInstructionFileDisplayName()
    {
    	return this.formInsructionFileDisplayName;
    }
    
    public void setFormInstructionFileDisplayName(String fileName)
    {
    	this.formInsructionFileDisplayName = fileName;
    }
    
	public void addFlags(String flag)
	{
		if(flag != null)
		{
			StringTokenizer st = new StringTokenizer(flag, ",");
			while(st.hasMoreTokens())
			{
				String aTok = st.nextToken().trim();
				if(aTok != null && aTok.trim().length() > 0)
				{
					if(flags.contains(aTok.toUpperCase()))
					{}
					else
					{
						flags.add(aTok.toUpperCase());
					}
				}
			}
		}
	}
	
	public List getFlags()
	{
		return flags;
	}

    public List<SurveyItemBase> getFormSections()
    {
    	List returnList = new ArrayList();
    	List qs = this.getQuestions();
    	for (Iterator iterator = qs.iterator(); iterator.hasNext();)
		{
			SurveyItemBase aItem = (SurveyItemBase) iterator.next();
			if(aItem instanceof SectionBase)
			{
				returnList.add(aItem);
			}
		}
    	
    	return returnList;
    }
    
    public int getSectionIndex(SurveyItemBase section)
    {
    	List<SurveyItemBase> sections  = getFormSections();
    	for (int i = 0; i<sections.size(); i++)
    	{
    		if(sections.get(i).getSurveyItemId().equals(section.getSurveyItemId()))
    			return i;
    	}
    	return -1;
    }
    

    public List<SurveyItemBase> getQuestionsLinear()
    {
    	List<SurveyItemBase> qList = new ArrayList();
    	
    	for (Iterator iterator = this.surveyItems.iterator(); iterator.hasNext();)
		{
			SurveyItemBase surveyItem = (SurveyItemBase) iterator.next();
			addChildrenToList(surveyItem, qList);
		}
    	return qList;
    }
    

    public List<SurveyItemBase> getQuestionsLinear(String surveyItemId)
    {
    	List<SurveyItemBase> qList = new ArrayList();
    	
    	SurveyItemBase topItem = getQuestion(surveyItemId);
    	if(topItem instanceof ContainerBase)
    	{
	    	for (Iterator iterator = ((ContainerBase)topItem).getChildren().iterator(); iterator.hasNext();)
			{
				SurveyItemBase surveyItem = (SurveyItemBase) iterator.next();
				addChildrenToList(surveyItem, qList);
			}
    	}
    	return qList;
    }
    
    

    private void addChildrenToList(SurveyItemBase parent, List<SurveyItemBase> qList)
    {
    	qList.add(parent);
    	if(parent instanceof ContainerBase)
    	{
    		List children = ((ContainerBase)parent).getChildren();
    		for (Iterator iterator = children.iterator(); iterator.hasNext();)
			{
				SurveyItemBase aChild = (SurveyItemBase) iterator.next();
				addChildrenToList(aChild, qList);
			}
    	}
    }
    

    public SurveyItemBase getQuestion(String questionId)
    {
    	if(questionId == null)
    		return null;
    	
        for (Iterator qIter = this.getQuestionsLinear().iterator(); qIter.hasNext();)
        {
            SurveyItemBase quest = (SurveyItemBase) qIter.next();
            if(quest.getSurveyItemId().equals(questionId))
            {
                return quest;
            }
        }
        
        //if question is null check inside the survey parames also
        for (Iterator qIter = surveyParams.iterator(); qIter.hasNext();)
        {
        	SurveyItemBase quest = (SurveyItemBase) qIter.next();
            if(quest.getSurveyItemId().equals(questionId))
            {
                return quest;
            }
        }
        
        return null;
    }

    public SurveyItemBase getQuestionBySectionIndexAndQuestionIdentifier(int sectionIndex, String questionIdentifier)
    {
    	List<SurveyItemBase> sections = getFormSections();
    	
        for (Iterator qIter = this.getQuestionsLinear().iterator(); qIter.hasNext();)
        {
            SurveyItemBase quest = (SurveyItemBase) qIter.next();
            if(quest.getIdentifier().equals(questionIdentifier))
            {
            	SectionBase sec = null;
            	
            	SurveyItemBase parent = quest.getParent();
            	if(parent instanceof SectionBase)
            		sec = (SectionBase) parent;
            	else
            		sec = (SectionBase) parent.getParent();
            	
            	if(sections.get(sectionIndex).getSurveyItemId() == sec.getSurveyItemId())
            		return quest;
            }
        }
        
        return null;
    }


    public List getQuestions()
    {
        return surveyItems;
    }

    public List getSurveyParams()
    {
        return this.surveyParams;
    }
    
    public List loadSurveyItems(Element rootElement, SurveyItemBase parent)
    {
        List returnList = new ArrayList();

        List qList = rootElement.getChildren("surveyItem");
        
        if (qList != null)
        {
            for (Iterator iter = qList.iterator(); iter.hasNext();)
            {
                Element qElement = (Element) iter.next();
                SurveyItemBase item = SurveyItemManager.getInstance().getSurveyItem(qElement.getAttributeValue("type"));
                if (logger.isDebugEnabled())
                {
                    logger.debug("Loading item - " + item);
                }
                if(item != null)
                {
                    item.setSurveyDefinition(this);
                    item.setParent((SurveyItemBase) parent);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("surveyDefinition Set");
                    }
                    item.setConfiguration(qElement);
                    if (logger.isDebugEnabled())
                    {
                        logger.debug("configurtion Set");
                    }
                    returnList.add(item);
                }
            }
        }
        
        //sort the list according to the orderNum
        Collections.sort(returnList, new SurveyItemOrderComparator());
        
        return returnList;
    }

    private void loadSurveyParams(Element rootElement)
    {
        Element paramsRoot = rootElement.getChild("surveyParams");
        {
            if(paramsRoot == null)
            {
                return;
            }
        }
        
        List qList = paramsRoot.getChildren("surveyParam");
        
        if (qList != null)
        {
            for (Iterator iter = qList.iterator(); iter.hasNext();)
            {
                Element qElement = (Element) iter.next();
                String pName = qElement.getAttributeValue("name");
                int pType = Integer.parseInt(qElement.getAttributeValue("dataType"));
                SurveyParamBase aParam = (SurveyParamBase) SurveyItemManager.getInstance().getSurveyItem(SurveyItemManager.SurveyParam.getName());
                aParam.setSurveyDefinition(this);
                aParam.setDataType(pType);
                aParam.setName(pName);
                if (logger.isDebugEnabled())
                {
                    logger.debug("Loading surveyParam - " + pName);
                }
                surveyParams.add(aParam);
            }
        }
    }

    private void loadFormInstruction(Element rootElement)
    {
        try
		{
			Element fiRoot = rootElement.getChild("formInstruction");
			{
			    if(fiRoot == null)
			    {
			        return;
			    }
			}

			String fPath = fiRoot.getAttributeValue("filePath");
			this.formInstructionFile = FileStoreManager.getFile(fPath);
			this.formInsructionFileDisplayName = fiRoot.getAttributeValue("fileName");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
*/
}
