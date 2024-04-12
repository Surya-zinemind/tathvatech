/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tathvatech.common.exception.AppException;
import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.forms.controller.TestProcController;
import com.tathvatech.logic.common.Logic;
import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.response.ResponseUnit;
import com.tathvatech.user.common.UserContext;
import org.aspectj.apache.bcel.classfile.Field;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RadioButtonAnswerType extends SurveySaveItem implements SurveyDisplayItem, MultipleChoiceType, OneDOptionType, HasOtherType, LogicSubject
{
    private static final Logger logger = LoggerFactory.getLogger(RadioButtonAnswerType.class);

    //if no maxlength is defined, set it to 1
    private static final int DEFAULT_MAXLENGTH = 1;

    private boolean multiSelect;
    
    /**
     *
     */
    public RadioButtonAnswerType()
    {
        super();
    }

    int maxLength = DEFAULT_MAXLENGTH;
    protected String questionText;
    protected String questionTextDescription = "";
    protected String dbColumn;
    protected OptionList options = new OptionList();
    protected Boolean isRequired;
    protected boolean randomizeChoices;
    protected boolean addOtherField;
    protected String otherFieldLabel = RadioButtonAnswerType.OTHER;
    protected String otherFieldType;

    private OptionGroup optionGroup;
    /**
     * @param _survey
     */
    public RadioButtonAnswerType(SurveyDefinition _survey)
    {
        super(_survey);
    }

    public String getTypeName()
    {
        return "RadioButtonAnswerType";
    }

    public boolean isMultiSelect()
	{
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect)
	{
		this.multiSelect = multiSelect;
	}

	public boolean hasSomethingToDisplay()
    {
    	return true;
    }

    /**
     * @return Returns the isRequired.
     */
    public boolean isRequired()
    {
    	if(isRequired == null)
    		return true;
        return isRequired;
    }
    /**
     * @param isRequired The isRequired to set.
     */
    public void setRequired(boolean isRequired)
    {
        this.isRequired = isRequired;
    }
    /**
     * @return Returns the addOtherField.
     */
    public boolean isAddOtherField()
    {
        return addOtherField;
    }
    /**
     * @param addOtherField The addOtherField to set.
     */
    public void setAddOtherField(boolean addOtherField)
    {
        this.addOtherField = addOtherField;
    }
    public String getOtherFieldType() 
    {
		return otherFieldType;
	}
	/* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.HasOtherType#getOtherFieldLabel()
     */
    public String getOtherFieldLabel()
    {
        return otherFieldLabel;
    }
    /**
     * @return Returns the randomizeChoices.
     */
    public boolean isRandomizeChoices()
    {
        return randomizeChoices;
    }
    /**
     * @param randomizeChoices The randomizeChoices to set.
     */
    public void setRandomizeChoices(boolean randomizeChoices)
    {
        this.randomizeChoices = randomizeChoices;
    }
    /**
     * @return Returns the questionText.
     */
    public String getQuestionText()
    {
        return questionText;
    }
    /**
     * @param questionText The questionText to set.
     */
    public void setQuestionText(String questionText)
    {
        this.questionText = questionText;
    }

    public String getQuestionTextDescription()
	{
		return this.questionTextDescription;
	}

    public void setQuestionTextDescription(String questionTextDescription)
	{
		this.questionTextDescription = questionTextDescription;
	}

    /**
     * @return Returns the dbColumn.
     */
    public String getDbColumn()
    {
        return dbColumn;
    }
    /**
     * @param dbColumn The dbColumn to set.
     */
    public void setDbColumn(String dbColumn)
    {
        this.dbColumn = dbColumn;
    }

    public String getIdentifier()
    {
		if(hidden)
		{
			return ("[Hidden]-&nbsp;" + questionText);
		}
		else
		{
			return questionText;
		}
    }

    public OptionList getOptions()
    {
        return options;
    }

    public void setOptions(OptionList options)
    {
        this.options = options;
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveySaveItem#getResponse(java.util.Map)
     */
    public SurveyItemResponse getResponse(Map paramMap, Properties props)throws InvalidResponseException
    {
        SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();
        String[] answer = (String[])paramMap.get(this.getSurveyItemId());
        if(answer != null && answer.length > 0)
        {
            ResponseUnit aUnit = new ResponseUnit();
            int key1 = Integer.parseInt(answer[0]);
            aUnit.setKey1(key1);
            logger.debug("RadioButton answer - " + answer[0]);
            if(key1 == RadioButtonAnswerType.OTHER_KEY)
            {
                String[] otherVal = (String[])paramMap.get(getSurveyItemId()+"_"+RadioButtonAnswerType.OTHER_VAL);
                logger.debug("Other Value - " + otherVal);
                if(CommentFieldType.COMMENT_PARAGRAPH.getId().equals(otherFieldType))
                {
                    if(otherVal != null && otherVal.length > 0)
                    {
                        String otherText = otherVal[0];
                        if(otherText.length() > TextAreaAnswerType.DEFAULT_MAXLENGTH)
                        {
                            throw new InvalidResponseException("The text you entered cannot be more than " + TextAreaAnswerType.DEFAULT_MAXLENGTH + " characters long.");
                        }
                        else
                        {
                            aUnit.setKey4(otherVal[0]);
                        }
                    }
                }
                else
                {
                    if(otherVal != null && otherVal.length > 0)
                    {
                        logger.debug("Other Value - " + otherVal[0]);
                        aUnit.setKey4(otherVal[0]);
                    }
                }
            }
                itemResponse.addResponseUnit(aUnit);
        }

        if(isRequired() && itemResponse.getResponseUnits().size() == 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Required for " + getIdentifier() + " is true and no answer was entered, throwing InvalidResponseException");
            }
            //denotes that there was no answer entered for this question
            //if the answer is required then, an error message should be throws back to the page.
            throw new InvalidResponseException("Survey_questionRequiredMessage");
        }
        return itemResponse;
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#getPersistor(java.lang.Object)
     */
    public AnswerPersistor getPersistor()
    {
        return new SimpleAnswerPersistor(this);
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#getPersistor(java.lang.Object)
     */
    public AnswerPersistor getPersistor(SurveyItemResponse answer)
    {
        return new SimpleAnswerPersistor(this, answer);
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#getDataType()
     */
    public int getDataType()
    {
        return DataTypes.DATATYPE_INTEGER;
    }


    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#saveConfiguration(java.util.Map)
     */
    public void setConfiguration(Map paramMap) throws Exception
    {
//        long respCount = SurveyResponseDelegate.getResponseForQuestion(this);

        if (logger.isDebugEnabled())
        {
            logger.debug("Inside setConfiguration(map)-- value of options is " + ((String[])paramMap.get("options"))[0]);
        }

        String[] hVal = (String[])paramMap.get("isHidden");
        if(hVal != null)
        {
            hidden = true;
        }
        else
        {
        	hidden = false;
        }

        String[] rVal = (String[])paramMap.get("isRequired");
        if(rVal != null)
        {
            isRequired = true;
        }
        else
        {
            isRequired = false;
        }

        String[] oVal = (String[])paramMap.get("addOtherField");
        if(oVal != null)
        {
            addOtherField = true;

            //find the otherFieldLabel and OtherFieldType
            String[] olVal = (String[])paramMap.get("otherFieldLabel");
            otherFieldLabel = olVal[0];

            String[] otVal = (String[])paramMap.get("otherFieldType");
            if(otVal == null )
            {
                otherFieldType=CommentFieldType.COMMENT_ONELINE.getId();
            }
            else
            {
                otherFieldType = otVal[0];
            }
        }
        else
        {
            addOtherField = false;
        }

        String[] tVal = (String[])paramMap.get("questionText");
        if(tVal != null)
        {
            questionText = LineSeperatorUtil.changeSystemLineSeperatorToBR(tVal[0]);
        }

        String[] tdVal = (String[])paramMap.get("questionTextDescription");
        if(tdVal != null)
        {
            questionTextDescription = LineSeperatorUtil.changeSystemLineSeperatorToBR(tdVal[0]);
        }

        OptionList tempOptionList = new OptionList();
        String[] vals = (String[])paramMap.get("options");
	    if(vals != null && vals[0].trim().length() > 0)
	    {
		    List optionItems = LineSeperatorUtil.tokenizeSystemLineSeperator(vals[0].trim());
		    for (Iterator iterator = optionItems.iterator(); iterator.hasNext();)
			{
				String aItem = (String) iterator.next();
				Option aOption = new Option(aItem, tempOptionList.size() + 1); // TODO:: as of now value can be incremental as item cannot be deleted.
				tempOptionList.add(aOption);
			}
	    }
        options = tempOptionList;

        String[] val = (String[])paramMap.get("randomizeChoices");
        if(val != null)
        {
            randomizeChoices = true;
        }
        else
        {
            randomizeChoices = false;
        }
    }

	public void setConfiguration(List<List<String>> fileContents)
	{
		List<String> firstRow = fileContents.get(0);
		try
		{
			this.questionText = firstRow.get(0);
			this.questionTextDescription = firstRow.get(1);
		}
		catch (Exception e)
		{
		}

		this.isRequired = false;
		try
		{
			if(firstRow.get(2).trim().equalsIgnoreCase("y"))
				this.isRequired = true;
		}
		catch (Exception e)
		{
		}

		this.multiSelect = false;
		try
		{
			if(firstRow.get(3).trim().equalsIgnoreCase("y"))
				this.multiSelect = true;
		}
		catch (Exception e)
		{
		}

		//look at the options
		if(fileContents != null && fileContents.size() > 1)
		{
			for (int i = 1; i < fileContents.size(); i++)
			{
				try
				{
					List<String> optionRow = fileContents.get(i);
					Option aOption = new Option(optionRow.get(0).trim(), i);
					options.add(aOption);
				}
				catch (Exception e)
				{
				}
			}
		}
	}
 
    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#setConfiguration(org.jdom.Element)
     */
    public void setConfiguration(Element element)
    {
        super.setSurveyItemConfiguration(element);
        this.setRequired(new Boolean(element.getAttributeValue("required")).booleanValue());
        this.setDbColumn(element.getAttributeValue("dbColumn"));
        this.setQuestionText(element.getAttributeValue("text"));
        this.questionTextDescription = element.getAttributeValue("textDescription");
        if(element.getAttribute("multiSelect") != null)
        	this.setMultiSelect(new Boolean(element.getAttributeValue("multiSelect")));
        
        Element optGroup = element.getChild("options");
        if(optGroup != null)
        {
	        for (Iterator iter = optGroup.getChildren("option").iterator(); iter.hasNext();)
	        {
	            Element optionElement = (Element) iter.next();
	            Option aOption = new Option(optionElement.getAttributeValue("text"), Integer.parseInt(optionElement.getAttributeValue("value")));
	            options.add(aOption);
	        }
        }

        String oVal = element.getAttributeValue("addOtherField");
        if (logger.isDebugEnabled())
        {
            logger.debug("addOtherField value is " + oVal);
        }
        if(oVal != null)
        {
            addOtherField = new Boolean(oVal).booleanValue();
            if(addOtherField)
            {
                otherFieldLabel = element.getAttributeValue("otherFieldLabel");
                otherFieldType = element.getAttributeValue("otherFieldType");
            }
        }

        String cVal = element.getAttributeValue("randomizeChoices");
        if (logger.isDebugEnabled())
        {
            logger.debug("randomizeChoices value is " + cVal);
        }
        if(cVal != null)
        {
            randomizeChoices = new Boolean(cVal).booleanValue();
        }
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.AnswerType#toXML(org.jdom.Element)
     */
    public Element toXML()
    {
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("Inside toXML -- adding options to element - value is " + optionString);
//        }

        Element element = new Element("surveyItem");

        super.toXML(element);

        element.setAttribute("required", new Boolean(isRequired).toString());

        if(questionText != null)
        {
            element.setAttribute("text", questionText);
        }

        if(questionTextDescription != null)
        {
        	element.setAttribute("textDescription", questionTextDescription);
        }
    	element.setAttribute("multiSelect", new Boolean(multiSelect).toString());
        

        if(dbColumn != null)
        {
            element.setAttribute("dbColumn", dbColumn);
        }

        if(options != null)
        {
            Element optGroup = new Element("options");
            	for (Iterator iter = options.iterator(); iter.hasNext();)
                {
                    Option aOption = (Option) iter.next();
                    Element optionElement = new Element("option");
                    optionElement.setAttribute("text", aOption.getText());
                    optionElement.setAttribute("value", new Integer(aOption.getValue()).toString());
                    optGroup.addContent(optionElement);
                }
            element.addContent(optGroup);
        }

        element.setAttribute("addOtherField", Boolean.toString(addOtherField));
        if(addOtherField)
        {
            element.setAttribute("otherFieldLabel", otherFieldLabel);
            element.setAttribute("otherFieldType", otherFieldType);
        }
        element.setAttribute("randomizeChoices", Boolean.toString(randomizeChoices));
        return element;
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveyItem#getLogic(java.lang.String)
     */
    public Logic getLogic(String logicId)
    {
        return super.getLogicFromList(logicId);
    }

    /* (non-Javadoc)
     * @see com.thirdi.surveyside.survey.SurveySaveItem#getResponseAsCSVString(com.thirdi.surveyside.survey.response.SurveyItemResponse)
     */
    public String getResponseAsCSVString(SurveyItemResponse itemResponse)
    {
        StringBuffer sb = new StringBuffer();

        for (int i=0; i<options.size(); i++)
        {
            Option choice = (Option) options.getOptionByIndex(i);
            String choiceString = choice.getText();
            int choiceValue = Integer.parseInt(choice.getValue());
            boolean answer = false;
            if(itemResponse != null)
            {
                for(int r=0; r<itemResponse.getResponseUnits().size(); r++)
			    {
				    ResponseUnit aUnit = (ResponseUnit)itemResponse.getResponseUnits().get(r);
				    if(choiceValue == aUnit.getKey1())
				    {
				        answer = true;
				    }
			    }
            }
    		sb.append(answer);
    		sb.append(",");
        }
    	//check for other , the value will be there in the key4
	    boolean answer = false;
	    String otherVal = "";
	    if(itemResponse != null)
	    {
		    for(int i=0; i<itemResponse.getResponseUnits().size(); i++)
		    {
		        ResponseUnit aUnit = (ResponseUnit)itemResponse.getResponseUnits().get(i);
			    if(RadioButtonAnswerType.OTHER_KEY == (aUnit.getKey1()))
			    {
			        answer = true;
	                if(aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
	                {
	                    otherVal = aUnit.getKey4();
	                }
			    }
		    }
	    }
		sb.append(answer + ",");

		otherVal = otherVal.replaceAll("\"", "\\\""); // replaces all " with a \"
		if(otherVal.indexOf(",") != -1)
		{
		    sb.append("\"" + otherVal + "\"");
		}
		else
		{
		    sb.append(otherVal);
		}
		return sb.toString();
    }

    public Element writeResponseXML(SurveyItemResponse itemResponse)throws Exception
	{
    	Element elem = new Element("itemResponse");
		elem.setAttribute("questionId", this.surveyItemId);
			
		for (int r = 0; r < itemResponse.getResponseUnits().size(); r++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse
					.getResponseUnits().get(r);
			
			Element uElem = new Element("u");
			uElem.setAttribute("k1", new Integer(aUnit.getKey1()).toString());
			if (RadioButtonAnswerType.OTHER_KEY == aUnit.getKey1())
			{
				if (aUnit.getKey4() != null
						&& aUnit.getKey4().trim().length() > 0)
				{
					uElem.setAttribute("k4", aUnit.getKey4());
				}
			}
			elem.addContent(uElem);
		}			
		return elem;
	 }


	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		Panel p = new Panel();
		VerticalLayout qLayout = new VerticalLayout();
		qLayout.addStyleName("text_box_type");

		p.setContent(qLayout);
		
		VerticalLayout questionTitleArea = new VerticalLayout();
		questionTitleArea.addStyleName("question_title_area");
		qLayout.addComponent(questionTitleArea);
		
		Label heading = new Label(questionText);
		heading.addStyleName("question_heading");
		questionTitleArea.addComponent(heading);
		Label desc = new Label(questionTextDescription);
		heading.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);

		this.optionGroup = new OptionGroup();
		if(multiSelect)
			optionGroup.setMultiSelect(true);
		
		questionArea.addComponent(optionGroup);
		
		for (Iterator iterator = options.iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			optionGroup.addItem(aOption);
			optionGroup.setItemCaption(aOption, aOption.getText());
		}

		return p;
	}

	@Override
	public TextAreaAnswerType.ConfigForm drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse,
                                       Component parent, String[] flags, FormEventListner formEventListner)
	{
		List<Integer> answers = new ArrayList<Integer>();
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
            if(itemResponse != null)
            {
                for(int r=0; r<itemResponse.getResponseUnits().size(); r++)
			    {
				    ResponseUnit aUnit = (ResponseUnit)itemResponse.getResponseUnits().get(r);
				    if(aUnit.getKey1() != 0)
				    {
				        answers.add(aUnit.getKey1());
				    }
			    }
            }
		}

		List<String> flagList = new ArrayList();
		for (int i = 0; i < flags.length; i++)
		{
			flagList.add(flags[i]);
		}
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDBUTMANDATORYONLY))
		{
			if(isRequired() && answers.size() == 0)
			{
			}
			else
			{
				return null;
			}
		}
		else if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDONLY))
		{
			if(answers.size() == 0)
			{
			}
			else
			{
				return null;
			}
		}

		Panel p = new Panel();
		VerticalLayout qLayout = new VerticalLayout();
		qLayout.addStyleName("text_box_type");

		p.setContent(qLayout);
		
		VerticalLayout questionTitleArea = new VerticalLayout();
		questionTitleArea.addStyleName("question_title_area");
		qLayout.addComponent(questionTitleArea);
		
		Label heading = new Label(questionText);
		heading.addStyleName("question_heading");
		questionTitleArea.addComponent(heading);
		Label desc = new Label(questionTextDescription);
		heading.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);

		this.optionGroup = new OptionGroup();
		for (Iterator iterator = options.iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			optionGroup.addItem(aOption.getValue());
			optionGroup.setItemCaption(aOption.getValue(), aOption.getText());
		}

		if(multiSelect)
		{
			optionGroup.setMultiSelect(true);
			optionGroup.setValue(answers);
		}
		else
		{
			if(answers.size() > 0)
				optionGroup.setValue(answers.get(0));
		}

		//when the verifier or approver edits the response, he need to edit only the comments so disable all others
		if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
		{
			optionGroup.setEnabled(false);
		}

		questionArea.addComponent(optionGroup);
		
		return p;
	}

	@Override
	public SurveyItemResponse captureResponse()
		throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();

		if(multiSelect)
		{
			Collection answers = (Collection)optionGroup.getValue();
			for (Iterator iterator = answers.iterator(); iterator.hasNext();)
			{
				Integer aAns = (Integer) iterator.next();
				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey1(aAns);
				itemResponse.addResponseUnit(aUnit);
			}
		}
		else
		{
			Integer ans = (Integer)optionGroup.getValue();
			if(ans != null)
			{
				int selection = ans;
				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey1(selection);
				itemResponse.addResponseUnit(aUnit);
			}
		}		
		return itemResponse;
	}

	public List validateResponse(SurveyItemResponse itemResponse)
	{
		return null;
	}

	@Override
	public Component drawResponseDetail(UserContext userContext, UnitFormQuery testProc, SurveyResponse sResponse,
                                        Component parent, boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController)
	{
		List thisItemFlagList = this.getFlagsAsList();
		boolean matchFlagsToDisplay = false;
		boolean flagsMatch = false;
		
		List flagList = new ArrayList();
		for (int i = 0; i < flags.length; i++)
		{
			flagList.add(flags[i]);
			
			if(surveyDef.getFlags().contains(flags[i]))
			{
				matchFlagsToDisplay = true;
				// user is trying to filter by flags in the survey def
				if(thisItemFlagList.contains(flags[i]))
				{
					flagsMatch = true;
				}
			}
		}
		if(matchFlagsToDisplay == true && flagsMatch == false)
		{
			return null;
		}

		List<Integer> answers = new ArrayList<Integer>();
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
            if(itemResponse != null)
            {
                for(int r=0; r<itemResponse.getResponseUnits().size(); r++)
			    {
				    ResponseUnit aUnit = (ResponseUnit)itemResponse.getResponseUnits().get(r);
				    if(aUnit.getKey1() != 0)
				    {
				        answers.add(aUnit.getKey1());
				    }
			    }
            }
		}

		Panel p = new Panel();
		VerticalLayout qLayout = new VerticalLayout();
		qLayout.addStyleName("text_box_type");

		p.setContent(qLayout);
		
		VerticalLayout questionTitleArea = new VerticalLayout();
		questionTitleArea.addStyleName("question_title_area");
		qLayout.addComponent(questionTitleArea);
		
		Label heading = new Label(questionText);
		heading.addStyleName("question_heading");
		questionTitleArea.addComponent(heading);
		Label desc = new Label(questionTextDescription);
		heading.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);

		this.optionGroup = new OptionGroup();
		this.optionGroup.addStyleName("radiobuttontype");
		for (Iterator iterator = options.iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			optionGroup.addItem(aOption.getValue());
			optionGroup.setItemCaption(aOption.getValue(), aOption.getText());
		}

		if(multiSelect)
		{
			optionGroup.setMultiSelect(true);
			optionGroup.setValue(answers);
		}
		else
		{
			if(answers.size() > 0)
				optionGroup.setValue(answers.get(0));
		}
		this.optionGroup.setEnabled(false);
		questionArea.addComponent(optionGroup);
		
		return p;
	}
	
	public class ConfigForm extends CustomComponent
	{
		FormDesignListener formDesignListener;
		RadioButtonAnswerType surveyItem;
		
		BeanFieldGroup<RadioButtonAnswerType> fieldGroup;
		FormLayout content;

		Panel optionsLayout = new Panel();
		VerticalLayout optionsLayoutContent = new VerticalLayout();
		
		List<HorizontalLayout> optionItems = new ArrayList<HorizontalLayout>();

		public ConfigForm(RadioButtonAnswerType surveyItem, FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			content = new FormLayout();
			content.setMargin(true);
			this.setCompositionRoot(content);
			
			optionsLayoutContent.setSpacing(true);
			optionsLayout.setContent(optionsLayoutContent);

			BeanItem<RadioButtonAnswerType> item = new BeanItem<RadioButtonAnswerType>(surveyItem);

			fieldGroup = new BeanFieldGroup<RadioButtonAnswerType>(RadioButtonAnswerType.class);
			
			// We need an item data source before we create the fields to be able to
	        // find the properties, otherwise we have to specify them by hand
			fieldGroup.setItemDataSource(item);
			fieldGroup.setBuffered(true);

			List<String> displayFields = Arrays.asList( "questionText", "questionTextDescription", "multiSelect", "flags", "required");
			
	    	for (Iterator iterator = displayFields.iterator(); iterator.hasNext();) 
	    	{
				String pid = (String) iterator.next();
		        Field field = DefaultFieldFactory.get().createField(item,
						pid, this);
				if (field instanceof TextField)
				{
					((TextField) field).setNullRepresentation("");
				}
		        content.addComponent(field);
		        fieldGroup.bind(field, pid);
	    	}

//			this.setVisibleItemProperties(new String[] { "questionText", "questionTextDescription", "multiSelect", "required" });

			// set the project name as required
			((TextField)fieldGroup.getField("questionText")).setColumns(40);
			fieldGroup.getField("questionText").setRequired(true);
			fieldGroup.getField("questionText").setRequiredError("Question text cannot be empty");

			((TextField)fieldGroup.getField("questionTextDescription")).setColumns(40);
			fieldGroup.getField("questionTextDescription").setCaption("Description");

			fieldGroup.getField("multiSelect").setCaption("Allow multiple selection");

			fieldGroup.getField("questionText").focus();

			//Add the choice options box
			content.addComponent(optionsLayout);
			optionsLayout.setImmediate(true);
			optionsLayout.setHeight("250px");
			
			OptionList options = surveyItem.getOptions();
			for (int i=0; i<options.size(); i++)
			{
				Option aOption = (Option) options.getOptionByIndex(i);
				final HorizontalLayout hL = new HorizontalLayout();
				hL.setSpacing(true);
				optionItems.add(hL);
				optionsLayoutContent.addComponent(hL);
				
				TextField t = new TextField();
				t.setColumns(40);
				t.setMaxLength(70);
				t.setValue(aOption.getText());
				t.setData(aOption);
				hL.addComponent(t);
				hL.setData(t);
				
				Button deleteButton = new Button("", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event)
					{
						optionItems.remove(hL);
						optionsLayoutContent.removeComponent(hL);
					}
				});
				deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
				deleteButton.setIcon(new ThemeResource("Images/delete.png"));
				deleteButton.setDescription("Delete option");
				hL.addComponent(deleteButton);
				hL.setComponentAlignment(deleteButton, Alignment.MIDDLE_LEFT);
			}
			
			
			Button addButton = new Button("Add option", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event)
				{
					final HorizontalLayout hL = new HorizontalLayout();
					hL.setSpacing(true);
					optionItems.add(hL);
					optionsLayoutContent.addComponent(hL);
					
					TextField t = new TextField();
					t.setColumns(40);
					t.setMaxLength(70);
					t.focus();
					hL.addComponent(t);
					hL.setData(t);
					
					Button deleteButton = new Button("", new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event)
						{
							optionItems.remove(hL);
							optionsLayoutContent.removeComponent(hL);
						}
					});
					deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
					deleteButton.setIcon(new ThemeResource("Images/delete.png"));
					deleteButton.setDescription("Delete option");
					hL.addComponent(deleteButton);
					hL.setComponentAlignment(deleteButton, Alignment.MIDDLE_LEFT);
				}
			});
			content.addComponent(addButton);
		
			
			content.addComponent(new HSpacer(20));
			
			/* Add buttons in the form. */
			HorizontalLayout buttonArea = new HorizontalLayout();
			buttonArea.setSpacing(true);
			content.addComponent(buttonArea);

			Button okbutton = new Button("", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					commit();
				}
			});
			okbutton.setIcon(new ThemeResource("Images/save.png"));
			okbutton.addStyleName(BaseTheme.BUTTON_LINK);
			okbutton.setDescription("Save");
			buttonArea.addComponent(okbutton);
			Button cancelbutton = new Button("", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					discard();
				}
			});
			cancelbutton.setIcon(new ThemeResource("Images/popup-cancel.png"));
			cancelbutton.addStyleName(BaseTheme.BUTTON_LINK);
			cancelbutton.setDescription("Cancel");
			buttonArea.addComponent(cancelbutton);

		}

		public void commit()
		{
			try
			{
				fieldGroup.commit();
				
				if(optionItems.size() == 0)
					throw new AppException("There should be atleast one option specified.");
				
				OptionList oList = new OptionList();
				int maxVal = 0;
				for (Iterator iterator = optionItems.iterator(); iterator.hasNext();)
				{
					HorizontalLayout aItem = (HorizontalLayout) iterator.next();
					TextField t = (TextField)aItem.getData();
					if(t.getValue() == null || ((String)t.getValue()).trim().length() == 0)
					{
						throw new Validator.InvalidValueException("Option cannot be blank");
					}
					Option aOption = (Option)t.getData();
					if(aOption == null)
					{
						aOption = new Option((String)t.getValue(), 0);
					}
					else
					{
						aOption.setText((String)t.getValue());
						if(aOption.getValue() > maxVal)
							maxVal = aOption.getValue();
					}
					oList.add(aOption);
				}
				// now the change the value 0 to the correct next value
				for (Iterator iterator = oList.iterator(); iterator.hasNext();)
				{
					Option aOption = (Option) iterator.next();
					if(aOption.getValue() == 0)
						aOption.setValue(++maxVal);
				}
				surveyItem.setOptions(oList);
				
				formDesignListener.formItemConfigurationComplete(surveyItem);
			}
			catch(Validator.InvalidValueException iv)
			{
//				this.setComponentError(new UserError(iv.getMessage()));
			}
			catch(AppException iv)
			{
				this.setComponentError(new UserError(iv.getMessage()));
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void discard()
		{
			fieldGroup.discard();
			formDesignListener.formItemConfigurationCancelled(surveyItem);
		}
	}
	
	
}
