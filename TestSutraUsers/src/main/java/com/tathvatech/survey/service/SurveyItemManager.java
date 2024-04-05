/*
 * Created on Oct 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import com.tathvatech.site.service.SiteServiceImpl;
import com.tathvatech.survey.common.SurveyItemType;
import com.tathvatech.survey.inf.SurveyItemBase;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyItemManager
{
	private static final Logger logger = LoggerFactory.getLogger(SurveyItemManager.class);

    private static SurveyItemManager instance = null;
    private HashMap itemTypesMap = new HashMap();
    private HashMap itemTypesMapByDesc = new HashMap();
    private List itemTypesList = new ArrayList();
    
    public static SurveyItemManager getInstance()
    {
        if(instance == null)
        {
            instance = new SurveyItemManager();
        }
        return instance;
    }
    
//    public SurveyItemBase getAnswerType(Question _question)throws Exception
//    {
//        try
//        {
//            String answerTypeName = _question.getAnswerTypeName();
//            String className = (String)answerTypesMap.get(answerTypeName);
//            Constructor constru = Class.forName(className).getConstructor(new Class[]{Question.class});
//            return (AnswerType)constru.newInstance(new Object[]{_question});
//        }
//        catch(Exception ex)
//        {
//            logger.debug(e.getMessage(), e);
//            throw ex;
//        }
//    }

    public SurveyItemType getTypeByName(String name)
    {
        return (SurveyItemType)itemTypesMap.get(name);
    }
    
    public SurveyItemBase getSurveyItem(String _itemName)
    {
    	if(_itemName == null || _itemName.trim().length() == 0)
    		return null;
    	
        try
        {
            SurveyItemType itemType = (SurveyItemType)itemTypesMap.get(_itemName); 
            String className = itemType.getTypeClass();
            SurveyItemBase item = (SurveyItemBase)Class.forName(className).newInstance();
            item.setSurveyItemType(itemType);
            return item;
        }
        catch(Exception ex)
      	{
            logger.error("Could not load surveyItem " + _itemName + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
      	}
        return null;
    }
    
    public SurveyItemBase getSurveyItemByDesc(String _itemDesc)
    {
        try
        {
            SurveyItemType itemType = (SurveyItemType)itemTypesMapByDesc.get(_itemDesc); 
            String className = itemType.getTypeClass();
            SurveyItemBase item = (SurveyItemBase)Class.forName(className).newInstance();
            item.setSurveyItemType(itemType);
            return item;
        }
        catch(Exception ex)
      	{
            logger.error("Could not load surveyItem " + _itemDesc + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
      	}
        return null;
    }

    public String getDescription(String _itemName)
    {
    	if(_itemName == null || _itemName.trim().length() == 0)
    		return null;
    	
        try
        {
            SurveyItemType itemType = (SurveyItemType)itemTypesMap.get(_itemName); 
            return itemType.getDescription();
        }
        catch(Exception ex)
      	{
            logger.error("Could not find surveyItem " + _itemName + " :: " + ex.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(ex.getMessage(), ex);
			}
      	}
        return null;
    }
    
    public List getAllSurveyItemTypes()
    {
        return itemTypesList;
    }
    
    private SurveyItemManager()
    {
    	loadSurveyItemTypesSimple();
    }
    
    private void loadSurveyItemTypesSimple()
    {
    	itemTypesList.add(RadioButtonAnswerType);
    	itemTypesMap.put(RadioButtonAnswerType.getName(), RadioButtonAnswerType);
    	itemTypesMapByDesc.put(RadioButtonAnswerType.getDescription(), RadioButtonAnswerType);
    	
    	itemTypesList.add(TextBoxAnswerType);
    	itemTypesMap.put(TextBoxAnswerType.getName(), TextBoxAnswerType);
    	itemTypesMapByDesc.put(TextBoxAnswerType.getDescription(), TextBoxAnswerType);
    	
    	itemTypesList.add(TextAreaAnswerType);
    	itemTypesMap.put(TextAreaAnswerType.getName(), TextAreaAnswerType);
    	itemTypesMapByDesc.put(TextAreaAnswerType.getDescription(), TextAreaAnswerType);

    	itemTypesList.add(BomInspectItemAnswerType);
    	itemTypesMap.put(BomInspectItemAnswerType.getName(), BomInspectItemAnswerType);
    	itemTypesMapByDesc.put(BomInspectItemAnswerType.getDescription(), BomInspectItemAnswerType);

    	itemTypesList.add(BomInspectItemGroupAnswerType);
    	itemTypesMap.put(BomInspectItemGroupAnswerType.getName(), BomInspectItemGroupAnswerType);
    	itemTypesMapByDesc.put(BomInspectItemGroupAnswerType.getDescription(), BomInspectItemGroupAnswerType);

    	itemTypesList.add(Picture);
    	itemTypesMap.put(Picture.getName(), Picture);
    	itemTypesMapByDesc.put(Picture.getDescription(), Picture);

    	itemTypesList.add(Informative_Text);
    	itemTypesMap.put(Informative_Text.getName(), Informative_Text);
    	itemTypesMapByDesc.put(Informative_Text.getDescription(), Informative_Text);

    	itemTypesList.add(Section);
    	itemTypesMap.put(Section.getName(), Section);
    	itemTypesMapByDesc.put(Section.getDescription(), Section);

    	itemTypesList.add(AdvancedBomInspectItemAnswerType);
    	itemTypesMap.put(AdvancedBomInspectItemAnswerType.getName(), AdvancedBomInspectItemAnswerType);
    	itemTypesMapByDesc.put(AdvancedBomInspectItemAnswerType.getDescription(), AdvancedBomInspectItemAnswerType);

    	itemTypesList.add(AdvancedBomInspectItemGroupAnswerType);
    	itemTypesMap.put(AdvancedBomInspectItemGroupAnswerType.getName(), AdvancedBomInspectItemGroupAnswerType);
    	itemTypesMapByDesc.put(AdvancedBomInspectItemGroupAnswerType.getDescription(), AdvancedBomInspectItemGroupAnswerType);

    	itemTypesList.add(OpenItemAnswerType);
    	itemTypesMap.put(OpenItemAnswerType.getName(), OpenItemAnswerType);
    	itemTypesMapByDesc.put(OpenItemAnswerType.getDescription(), OpenItemAnswerType);

    	itemTypesList.add(OpenItemGroupAnswerType);
    	itemTypesMap.put(OpenItemGroupAnswerType.getName(), OpenItemGroupAnswerType);
    	itemTypesMapByDesc.put(OpenItemGroupAnswerType.getDescription(), OpenItemGroupAnswerType);
    
    	itemTypesList.add(PrintPageBreak);
    	itemTypesMap.put(PrintPageBreak.getName(), PrintPageBreak);
    	itemTypesMapByDesc.put(PrintPageBreak.getDescription(), PrintPageBreak);

    	itemTypesList.add(SignatureCapture);
    	itemTypesMap.put(SignatureCapture.getName(), SignatureCapture);
    	itemTypesMapByDesc.put(SignatureCapture.getDescription(), SignatureCapture);
    }
    //TODO:: change the file location statement below
    private void loadSurveyItemTypes()
    {
		try
		{
		    SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File("answertypes.xml"));
			
			List aList = doc.getRootElement().getChildren("surveyItemType");
			for (Iterator aListItr = aList.iterator(); aListItr.hasNext();)
            {
                Element aTypeElement = (Element) aListItr.next();
                
                SurveyItemType type = new SurveyItemType();
                type.setName(aTypeElement.getAttributeValue("name"));
                type.setDescription(aTypeElement.getAttributeValue("description"));
                type.setTypeClass(aTypeElement.getAttributeValue("typeClass"));
                itemTypesMap.put(aTypeElement.getAttributeValue("name"), type);
                type.setDisplayImage(aTypeElement.getAttributeValue("displayImage"));
                itemTypesList.add(type);
            }
		}
		catch (Throwable e)
		{
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
		}
    }

    public static SurveyItemType RadioButtonAnswerType = new SurveyItemType("RadioButtonAnswerType", "Multiple Choice", "com.thirdi.surveyside.survey.surveyitem.RadioButtonAnswerType", "img/qtypes/text_singleline.png");
	public static SurveyItemType TextBoxAnswerType = new SurveyItemType("TextBoxAnswerType", "Text-Single input box", "com.thirdi.surveyside.survey.surveyitem.TextBoxAnswerType", "img/qtypes/text_singleline.png");
	public static SurveyItemType TextAreaAnswerType = new SurveyItemType("TextAreaAnswerType", "Text-Paragraph", "com.thirdi.surveyside.survey.surveyitem.TextAreaAnswerType", "img/qtypes/text_paragraph.png");
	public static SurveyItemType BomInspectItemAnswerType = new SurveyItemType("BomInspectItemAnswerType", "Inspection Line Item", "com.thirdi.surveyside.survey.surveyitem.BomInspectItemAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType BomInspectItemGroupAnswerType = new SurveyItemType("BomInspectItemGroupAnswerType", "Inspection Group", "com.thirdi.surveyside.survey.surveyitem.BomInspectItemGroupAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType Picture = new SurveyItemType("Picture", "Picture", "com.thirdi.surveyside.survey.surveyitem.PictureType", "img/qtypes/newpage.png");
	public static SurveyItemType Informative_Text = new SurveyItemType("Informative Text", "Informative Text", "com.thirdi.surveyside.survey.surveyitem.InformationText", "img/qtypes/infotext.png");
	public static SurveyItemType Section = new SurveyItemType("Section", "Section", "com.thirdi.surveyside.survey.surveyitem.Section", "img/qtypes/infotext.png");
	public static SurveyItemType AdvancedBomInspectItemAnswerType = new SurveyItemType("AdvancedBomInspectItemAnswerType", "Advanced Inspection Line Item", "com.thirdi.surveyside.survey.surveyitem.AdvancedBomInspectItemAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType AdvancedBomInspectItemGroupAnswerType = new SurveyItemType("AdvancedBomInspectItemGroupAnswerType", "Advanced Inspection Group", "com.thirdi.surveyside.survey.surveyitem.AdvancedBomInspectItemGroupAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType OpenItemGroupAnswerType = new SurveyItemType("OpenItemGroupAnswerType", "Open Item Group", "com.thirdi.surveyside.survey.surveyitem.OpenItemGroupAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType OpenItemAnswerType = new SurveyItemType("OpenItemAnswerType", "Open Item", "com.thirdi.surveyside.survey.surveyitem.OpenItemAnswerType", "img/qtypes/newpage.png");
	public static SurveyItemType SignatureCapture = new SurveyItemType("SignatureCapture", "Signature Capture", "com.thirdi.surveyside.survey.surveyitem.SignatureCaptureAnswerType", "img/qtypes/newpage.png");;
	public static SurveyItemType PrintPageBreak = new SurveyItemType("Print PageBreak", "Print PageBreak", "com.thirdi.surveyside.survey.surveyitem.PrintPageBreak", "img/qtypes/newpage.png");;
	public static SurveyItemType SurveyParam = new SurveyItemType("SurveyParamType", "Form Parameter", "com.thirdi.surveyside.survey.SurveyParam", "img/qtypes/newpage.png");;
	
}
