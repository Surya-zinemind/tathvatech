/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.forms.common.ExpectedNumericValue;
import com.tathvatech.forms.entity.FormItemResponse;
import com.tathvatech.logic.common.Logic;
import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.response.ResponseUnit;
import jakarta.persistence.Table;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;

import static com.tathvatech.survey.service.SurveyItemManager.BomInspectItemGroupAnswerType;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class BomInspectItemAnswerType extends SurveySaveItem implements BaseInspectionLineItemAnswerType, SurveyDisplayItem, LogicSubject
{

	private static final Logger logger = LoggerFactory.getLogger(BomInspectItemAnswerType.class);
	private enum DisplayModeEnum {ViewMode, DataEntryMode}; // this is currently used only for the P8Transfer Menu.

	// if no maxlength is defined, set it to 1
	private static final int DEFAULT_MAXLENGTH = 5000;

	private int dataType = DataTypes.DATATYPE_STRING;

	public Integer commentFieldMaxLength = null;

	private String questionText;
	protected String questionTextDescription = "";
	private OptionList customFields = new OptionList();

	// private int colCount;
	private boolean showPassFail;
	private boolean showNotApplicable;
	private boolean showComments;
	private boolean showExpectedValue;
	private boolean showActualValue;
	private boolean showUnit;
	

	public static int actualValueKey = 1;
	public static int selectionKey = 2;
	public static int commentsKey = 3;
	public static int imageKey = 4;

	public static int passVal = 11;
	public static int failVal = 12;
	public static int naVal = 13;
	
	TextField actualValueTxt;
	ExpandableTextArea commentsTxt;
	SingleSelectCheckboxGroup result;

	Table table; // Table to which it is added as a row to.
	
	String imageAttachCol = null; //column where image should be attached.
	String answer = "";
	String canswer = "";
	String passFailResult = "";
	
	UnitFormQuery testProc = null;
	FormItemResponse formItemResponse;
	List<TestItemOILTransferQuery> oilItemList; // openItem associated with this item

	List<String> responseImageFileNames = new ArrayList<String>(); // the name of the image that is attached as part of the response.
	/**
     *
     */
	public BomInspectItemAnswerType()
	{
		super();

	}

	/**
	 * @param _survey
	 */
	public BomInspectItemAnswerType(SurveyDefinition _survey)
	{
		super(_survey);
	}

	public String getTypeName()
	{
		return "BomInspectItemAnswerType";
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
		if(showActualValue  || showPassFail || showComments)
		{
			return true;
		}
		else
		{ // no fields are there to be entered for this..
			return false;
		}
	}

	public boolean isShowExpectedValue()
	{
		return showExpectedValue;
	}

	public void setShowExpectedValue(boolean showExpectedValue)
	{
		this.showExpectedValue = showExpectedValue;
	}

	public boolean isShowUnit()
	{
		return showUnit;
	}

	public void setShowUnit(boolean showUnit)
	{
		this.showUnit = showUnit;
	}

	public boolean isShowActualValue()
	{
		return showActualValue;
	}

	public void setShowActualValue(boolean showActualValue)
	{
		this.showActualValue = showActualValue;
	}

	public boolean isShowPassFail()
	{
		return showPassFail;
	}

	public void setShowPassFail(boolean showPassFail)
	{
		this.showPassFail = showPassFail;
	}

	public boolean isShowComments()
	{
		return showComments;
	}

	public void setShowComments(boolean showComments)
	{
		this.showComments = showComments;
	}

	public Integer getCommentFieldMaxLength()
	{
		return commentFieldMaxLength;
	}

	public void setCommentFieldMaxLength(Integer commentFieldMaxLength)
	{
		commentFieldMaxLength = commentFieldMaxLength;
	}

	public boolean isShowNotApplicable()
	{
		return showNotApplicable;
	}

	public void setShowNotApplicable(boolean showNotApplicable)
	{
		this.showNotApplicable = showNotApplicable;
	}

	public String getImageAttachCol()
	{
		return imageAttachCol;
	}

	public void setImageAttachCol(String imageAttachCol)
	{
		this.imageAttachCol = imageAttachCol;
	}

	/**
	 * @return Returns the questionText.
	 */
	public String getQuestionText()
	{
		if(customFields != null && customFields.size() > 0)
		{
			return customFields.getOptionByIndex(0).getTextDesc();
		}
		return "[No Value]";
	}

	public String getQuestionTextDescription()
	{
		if(customFields != null)
		{
			Option aOption = customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_DESCRIPTION);
			if(aOption != null)
			{
				return aOption.getTextDesc();
			}
		}
		return "";
	}

	public String getUnit()
	{
		if(customFields != null)
		{
			Option aOption = customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_UNIT);
			if(aOption != null)
			{
				return aOption.getTextDesc();
			}
		}
		return "";
	}
	
	public String getLocation()
	{
		if(customFields != null)
		{
			Option aOption = customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_LOCATION);
			if(aOption != null)
			{
				return aOption.getTextDesc();
			}
		}
		return "";
	}

	public String getExpectedValue()
	{
		if(customFields != null)
		{
			Option aOption = customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_EXPECTED);
			if(aOption != null)
			{
				return aOption.getTextDesc();
			}
		}
		return "";
	}

	public ExpectedNumericValue getExpectedUpperLower()
	{
		return ExpectedNumericValue.getExpectedUpperLower(getExpectedValue());
	}
	
	public Section getEnclosingSection()
	{
		return (Section) this.getParent().getParent();
	}
	
	public OptionList getFlexiFields()
	{
		List opts = new ArrayList();
		for (int i = 0; i < customFields.size(); i++)
		{
			Option aOption = customFields.getOptionByIndex(i);
			if(aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_ITEMNO)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DESCRIPTION)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_LOCATION)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_EXPECTED)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_UNIT)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_COMMENTS)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_DATATYPE)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_FLAGS)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE)
					|| aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_RESULT)
					)
			{
				
			}
			else
			{
				opts.add(aOption);
			}
		}
		return new OptionList(opts);
	}

	public OptionList getCustomFields()
	{
		return customFields;
	}

	public void setCustomFields(OptionList customFields)
	{
		this.customFields = customFields;
	}

	public String getIdentifier()
	{
		if (hidden)
		{
			return ("[Hidden]-&nbsp;" + questionText);
		}
		else
		{
			return getQuestionText();
		}
	}

	// public OptionList getOptions()
	// {
	// return options;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.SurveySaveItem#getResponse(java.util.Map)
	 */
	public SurveyItemResponse getResponse(Map paramMap, Properties props) throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();
		for (int i = 0; i < customFields.size(); i++)
		{
			Option aOption = customFields.getOptionByIndex(i);

			String[] answer = (String[]) paramMap.get((getSurveyItemId() + "_" + aOption.getValue()));
			if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
			{
				String answerText = answer[0];
				if (answerText.trim().length() > DEFAULT_MAXLENGTH)
				{
					throw new InvalidResponseException("Survey_invalidAnswerMessage");
				}
				if (answerText != null && dataType == DataTypes.DATATYPE_INTEGER)
				{
					try
					{
						Double.parseDouble(answerText);
					}
					catch (Exception ex)
					{
						logger.warn(ex + " :: " + ex.getMessage());
						if (logger.isDebugEnabled())
						{
							logger.debug(ex.getMessage(), ex);
						}
						throw new InvalidResponseException("Survey_invalidAnswerMessage");
					}
				}

				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey1(Integer.parseInt(aOption.getValue()));
				aUnit.setKey4(answerText.trim());
				itemResponse.addResponseUnit(aUnit);
			}

			answer = (String[]) paramMap.get((getSurveyItemId() + "_" + actualValueKey));
			if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
			{
				String answerText = answer[0];

				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey2(actualValueKey);
				aUnit.setKey4(answerText.trim());
				itemResponse.addResponseUnit(aUnit);
			}

			answer = (String[]) paramMap.get((getSurveyItemId() + "_" + selectionKey));
			if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
			{
				String answerText = answer[0];

				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey2(selectionKey);
				aUnit.setKey4(answerText.trim());
				itemResponse.addResponseUnit(aUnit);
			}

			answer = (String[]) paramMap.get((getSurveyItemId() + "_" + commentsKey));
			if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
			{
				String answerText = answer[0];

				if(commentFieldMaxLength != null && answerText.trim().length() > commentFieldMaxLength)
					throw new InvalidResponseException("Survey_invalidAnswerMessage");

				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey2(commentsKey);
				aUnit.setKey4(answerText.trim());
				itemResponse.addResponseUnit(aUnit);
			}
		}

		if (isRequired() && itemResponse.getResponseUnits().size() == 0)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Required for " + getIdentifier()
						+ " is true and no answer was entered, throwing InvalidResponseException");
			}
			// denotes that there was no answer entered for this question
			// if the answer is required then, an error message should be throws
			// back to the page.
			throw new InvalidResponseException("Survey_questionRequiredMessage");
		}
		return itemResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#getPersistor(java.lang.Object)
	 */
	public AnswerPersistor getPersistor()
	{
		return new SimpleAnswerPersistor(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#getPersistor(java.lang.Object)
	 */
	public AnswerPersistor getPersistor(SurveyItemResponse answer)
	{
		return new SimpleAnswerPersistor(this, answer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.AnswerType#getDataType()
	 */
	public int getDataType()
	{
		return dataType;
	}

	public void setDataType(int dataType)
	{
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#saveConfiguration(java.util.Map)
	 */
	public void setConfiguration(Map paramMap) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Inside setConfiguration(map)-- value of options is "
					+ ((String[]) paramMap.get("options"))[0]);
		}

		String[] hVal = (String[]) paramMap.get("isHidden");
		if (hVal != null)
		{
			hidden = true;
		}
		else
		{
			hidden = false;
		}

		String[] tVal = (String[]) paramMap.get("questionText");
		if (tVal != null)
		{
			questionText = LineSeperatorUtil.changeSystemLineSeperatorToBR(tVal[0]);
		}

		String[] tdVal = (String[]) paramMap.get("questionTextDescription");
		if (tdVal != null)
		{
			questionTextDescription = LineSeperatorUtil.changeSystemLineSeperatorToBR(tdVal[0]);
		}

		OptionList tempCustomList = new OptionList();

		String[] dVal = (String[]) paramMap.get("dataType");
		if (dVal != null)
		{
			dataType = Integer.parseInt(dVal[0]);
		}
	}

	public void setConfiguration(List<List<String>> fileContents)
	{
		//not required.. set by the BomGroup
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#setConfiguration(org.jdom.Element
	 * )
	 */
	public void setConfiguration(Element element)
	{
		super.setSurveyItemConfiguration(element);
		this.setShowExpectedValue(new Boolean(element.getAttributeValue("showExpectedValue")).booleanValue());
		this.setShowActualValue(new Boolean(element.getAttributeValue("showActualValue")).booleanValue());
		this.setShowUnit(new Boolean(element.getAttributeValue("showUnit")).booleanValue());
		this.setShowPassFail(new Boolean(element.getAttributeValue("showPassFail")).booleanValue());
		this.setShowNotApplicable(new Boolean(element.getAttributeValue("showNotApplicable")).booleanValue());
		this.setShowComments(new Boolean(element.getAttributeValue("showComments")).booleanValue());
		this.imageAttachCol = (element.getAttributeValue("imageAttachCol") == null)? null:element.getAttributeValue("imageAttachCol");
		if(element.getAttributeValue("commentFieldMaxLength") != null)
			this.commentFieldMaxLength = Integer.parseInt(element.getAttributeValue("commentFieldMaxLength"));
		
		String dType = element.getAttributeValue("dataType");
		if (dType != null && dType.trim().length() > 0)
		{
			this.setDataType(Integer.parseInt(element.getAttributeValue("dataType")));
		}
		else
		{
			this.setDataType(DataTypes.DATATYPE_STRING);
		}

		Element cfGroup = element.getChild("customFields");
		if (cfGroup != null)
		{
			OptionList cfList = new OptionList();
			for (Iterator iter = cfGroup.getChildren("field").iterator(); iter.hasNext();)
			{
				Element fElement = (Element) iter.next();
				Option aOption = new Option(fElement.getAttributeValue("text"), fElement
						.getAttributeValue("textDesc"));
				cfList.add(aOption);
			}
			this.customFields = cfList;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.AnswerType#toXML(org.jdom.Element)
	 */
	public Element toXML()
	{
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Inside toXML -- adding options to element - value is "
		// + optionString);
		// }

		Element element = new Element("surveyItem");

		super.toXML(element);

		element.setAttribute("showExpectedValue", new Boolean(showExpectedValue).toString());
		element.setAttribute("showActualValue", new Boolean(showActualValue).toString());
		element.setAttribute("showUnit", new Boolean(showUnit).toString());
		element.setAttribute("showPassFail", new Boolean(showPassFail).toString());
		element.setAttribute("showNotApplicable", new Boolean(showNotApplicable).toString());
		element.setAttribute("showComments", new Boolean(showComments).toString());
		if(commentFieldMaxLength != null)
			element.setAttribute("commentFieldMaxLength", commentFieldMaxLength.toString());

		element.setAttribute("dataType", Integer.toString(dataType));
		element.setAttribute("imageAttachCol", (imageAttachCol== null)?"":imageAttachCol);

		if (customFields != null)
		{
			Element optGroup = new Element("customFields");
			for (Iterator iter = customFields.iterator(); iter.hasNext();)
			{
				Option aOption = (Option) iter.next();
				Element optionElement = new Element("field");
				optionElement.setAttribute("text", ListStringUtil.showString(aOption.getText(), 0));
				optionElement.setAttribute("textDesc", ListStringUtil.showString(aOption.getTextDesc(), 0));
				optGroup.addContent(optionElement);
			}
			element.addContent(optGroup);
		}
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.SurveyItem#getLogic(java.lang.String)
	 */
	public Logic getLogic(String logicId)
	{
		return super.getLogicFromList(logicId);
	}

// commented on 27/4/2012 .. looks like it is not being used
//	public void drawResponseDetailInt(SurveyItemResponse itemResponse, Table table, int index)
//	{
//		int colCount = 6;
//		colCount += this.customFields.size();
//		if (showPass)
//			colCount++;
//		if (showFail)
//			colCount++;
//		if (showNotApplicable)
//			colCount++;
//		if (showComments)
//			colCount++;
//
//		Object[] cols = new Object[colCount];
//
//		cols[0] = this.getQuestionText();
//		table.addItem(cols[0]);
//		
//		table.getContainerProperty(cols[0], "Item No").setValue(cols[0]);
//		cols[1] = this.getQuestionTextDescription();
//		
//
//		VerticalLayout descVLayout = new VerticalLayout();
//		//descVLayout.setWidth("200px");
//		descVLayout.setWidth(Sizeable.SIZE_UNDEFINED, 0);
//		
//     	Label descField = new Label(this.getQuestionTextDescription());
//     	descField.setContentMode(Label.CONTENT_PREFORMATTED);
//     	descVLayout.addComponent(descField);
//     	
//		table.getContainerProperty(cols[0], "Description").setValue(descVLayout);
//		cols[2] = this.getLocation();
//		table.getContainerProperty(cols[0], "Location").setValue(cols[2]);
//		int nextCol = 3;
//		for (int j = 0; j < customFields.size(); j++)
//		{
//			Option aOption = (Option) customFields.get(j);
//			String optionString = aOption.getText();
//			String optionStringDesc = aOption.getTextDesc();
//
//			cols[nextCol] = optionStringDesc;
//			table.getContainerProperty(cols[0], "Custom"+(j+1)).setValue(optionStringDesc);
//			nextCol++;
//		}
//
//		cols[nextCol] = this.getExpectedValue();
//		table.getContainerProperty(cols[0], "Expected Result").setValue(this.getExpectedValue());
//		nextCol++;
//
//		String answer = "";
//		if (itemResponse != null)
//		{
//			for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
//			{
//				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
//				if (this.actualValueKey == aUnit.getKey2() && aUnit.getKey4() != null)
//				{
//					answer = aUnit.getKey4();
//				}
//			}
//		}
//		cols[nextCol] = answer;
//		table.getContainerProperty(cols[0], "Actual").setValue(answer);
//		nextCol++;
//
//		table.getContainerProperty(cols[nextCol], "Unit").setValue(this.getUnit());
//		nextCol++;
//
//		int selectedValue = 0;
//		if (itemResponse != null)
//		{
//			for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
//			{
//				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
//				if (this.selectionKey == aUnit.getKey2())
//				{
//					selectedValue = aUnit.getKey3();
//				}
//			}
//		}
//
//		if (showPass)
//		{
//			if (passVal == selectedValue){
//				cols[nextCol] = new String("Y");
//				table.getContainerProperty(cols[0], "Y").setValue(pass);
//			}
//			else{
//				cols[nextCol] = new String("");
//				table.getContainerProperty(cols[0], "").setValue(pass);
//				}
//			nextCol++;
//		}
//		if (showFail)
//		{
//			if (failVal == selectedValue){
//				cols[nextCol] = new String("Y");
//				table.getContainerProperty(cols[0], "Y").setValue(fail);
//				}
//			else{
//				cols[nextCol] = new String("");
//				table.getContainerProperty(cols[0], "").setValue(fail);
//				}
//			nextCol++;
//		}
//		if (showNotApplicable)
//		{
//			if (naVal == selectedValue){
//				cols[nextCol] = new String("Y");
//				table.getContainerProperty(cols[0], "Y").setValue(na);	
//			}
//			else{
//				cols[nextCol] = new String("");
//				table.getContainerProperty(cols[0], "").setValue(na);	
//			}
//			nextCol++;
//		}
//
//		if (showComments)
//		{
//			String canswer = "";
//			if (itemResponse != null)
//			{
//				for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
//				{
//					ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
//					if (this.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null)
//					{
//						canswer = aUnit.getKey4();
//					}
//				}
//			}
//			cols[nextCol] = canswer;
//			table.getContainerProperty(cols[0], "Comments").setValue(canswer);
//			nextCol++;
//		}
//
//		//table.addItem(cols, index);
//
//	}


	public Element writeResponseXML(SurveyItemResponse itemResponse) throws Exception
	{
		Element elem = new Element("itemResponse");
		elem.setAttribute("questionId", this.surveyItemId);

		for (int r = 0; r < itemResponse.getResponseUnits().size(); r++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(r);

			Element uElem = new Element("u");
			uElem.setAttribute("k1", new Integer(aUnit.getKey1()).toString());
			
			if(aUnit.getKey2() != 0)
			{
				uElem.setAttribute("k2", new Integer(aUnit.getKey2()).toString());
			}
			if(aUnit.getKey3() != 0)
			{
				uElem.setAttribute("k3", new Integer(aUnit.getKey3()).toString());
			}
			if (aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
			{
				uElem.setAttribute("k4", aUnit.getKey4());
			}
			elem.addContent(uElem);
		}
		return elem;
	}

	private ResponseUnit getResponseUnit(IndexKeys matchKey, int matchKeyValue, SurveyItemResponse itemResponse)
	{
		if (itemResponse != null)
		{
			for (int j = 0; j < itemResponse.getResponseUnits().size(); j++)
			{
				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(j);

				switch (matchKey)
				{
				case key1:
					if (matchKeyValue == aUnit.getKey1())
					{
						return aUnit;
					}
					break;
				case key2:
					if (matchKeyValue == aUnit.getKey2())
					{
						return aUnit;
					}
					break;
				case key3:
					if (matchKeyValue == aUnit.getKey3())
					{
						return aUnit;
					}
					break;
				}
			}
		}
		return null;
	}

	@Override
	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void addItemToDesignViewInt(Table groupTable, boolean isPreviewMode, String addAfterItemId, final FormDesignListener formDesignListener)
	{
		this.table = groupTable;
		String rowId = this.getSurveyItemId();
		if(rowId == null)
		{
			rowId = ""+table.getItemIds().size() + 1;
		}
		if(addAfterItemId == null)
		{
			table.addItem(rowId);
		}
		else
		{
			table.addItemAfter(addAfterItemId, rowId);
		}
		drawDesignViewInt(rowId, isPreviewMode, formDesignListener);
	}
	
	public void drawDesignViewInt(String id, boolean isPreviewMode, final FormDesignListener formDesignListener)
	{
		String expectedValHeading = null;
		String expectedVal = null;
		String unitVal = null;
		for (int j = 0; j < this.getCustomFields().size(); j++)
		{
			Option aOption = customFields.getOptionByIndex(j);
			String headingText = aOption.getText();
			if (BomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				expectedValHeading = headingText;
				expectedVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(headingText))
			{
					unitVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_ACTUAL.equalsIgnoreCase(headingText))
			{
				if(showActualValue)
				{
					TextField txt = new TextField();
					txt.setColumns(5);
					table.getContainerProperty(id, headingText).setValue(txt);
				}
				else
				{
					table.getContainerProperty(id, headingText).setValue(null);
				}
			}
			else if (BomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					SingleSelectCheckboxGroup o1 = new SingleSelectCheckboxGroup();
					o1.addItem(BomInspectItemAnswerType.passVal);
					o1.setItemCaption(BomInspectItemAnswerType.passVal, "Pass");
	
					o1.addItem(BomInspectItemAnswerType.failVal);
					o1.setItemCaption(BomInspectItemAnswerType.failVal, "Fail");
					if(this.isShowNotApplicable())
					{
						o1.addItem(BomInspectItemAnswerType.naVal);
						o1.setItemCaption(BomInspectItemAnswerType.naVal, "Not Applicable");
					}
					table.getContainerProperty(id, headingText).setValue(o1);
				}
				else
				{
					table.getContainerProperty(id, headingText).setValue(null);
				}
			}
			else if (BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					TextArea ta = new TextArea();
					ta.setColumns(20);
					ta.setRows(3);
					if(commentFieldMaxLength != null)
						ta.setMaxLength(commentFieldMaxLength);

					table.getContainerProperty(id, headingText).setValue(ta);
				}
				else
				{
					table.getContainerProperty(id, headingText).setValue(null);
				}
			}
			else
			{
				VerticalLayout l;
				if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() > 0)
				{
					l = getMultiLine(aOption.getTextDesc());
					l.setImmediate(true);
				}
				else
				{
					l = new VerticalLayout();
					l.setImmediate(true);
				}
				if(headingText.equals(imageAttachCol))
				{
					if(imageFileName != null)
					{
						try
						{
							float colWidthPercent = 0;
							try
							{
								Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(headingText);
								colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
							}catch(Exception e){}
							InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

							if(l.getComponentCount() > 0)
								l.addComponent(new VSpacer(10));
							l.addComponent(img);
						}
						catch (Exception e)
						{
							logger.error("Cannot read file - " + imageFileName, e);
						}
					}
				}
				table.getContainerProperty(id, headingText).setValue(l);
			}
		}
		if(showExpectedValue)
		{
			if(expectedVal != null || unitVal != null)
			{
				StringBuffer sb = new StringBuffer();
				if(expectedVal != null)
					sb.append(expectedVal);
				if(expectedVal != null && unitVal != null)
					sb.append(" ");
				if(unitVal != null)
					sb.append(unitVal);
				VerticalLayout l = getMultiLine(sb.toString());
				if(expectedValHeading.equals(imageAttachCol))
				{
					if(imageFileName != null)
					{
						try
						{
							float colWidthPercent = 0;
							try
							{
								Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(expectedValHeading);
								colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
							}catch(Exception e){}
							InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

							if(l.getComponentCount() > 0)
								l.addComponent(new VSpacer(10));
							l.addComponent(img);
						}
						catch (Exception e)
						{
							logger.error("Cannot read file - " + imageFileName, e);
						}
					}
				}
				table.getContainerProperty(id, expectedValHeading).setValue(l);
			}
			else
			{
				table.getContainerProperty(id, expectedValHeading).setValue(null);
			}
		}
		
		if(isPreviewMode == false)
		{
			Button breakButton = new Button("", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event)
				{
					try
					{
						Component parent = table.getParent();
						while(parent!= null)
						{
							if(parent instanceof SurveyItemConfigHolder && 
									((SurveyItemConfigHolder)parent).getSurveyItem() instanceof Section)
							{
								((SurveyItemConfigHolder)parent).breakBomTable(getSurveyItemId());
								break;
							}
							parent = parent.getParent();
						}
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			breakButton.setIcon(new ThemeResource("Images/break.png"));
			breakButton.setStyleName(BaseTheme.BUTTON_LINK);
			breakButton.setDescription("Break the inspection group below this line");
			table.getContainerProperty(id, "Break").setValue(breakButton);

		
			final Button editButton = new Button("", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event)
				{
					try
					{
						Window win = new Window();
						win.setWidth("800px");
						win.setHeight("600px");
						win.setContent(drawConfigurationView(formDesignListener));
						EtestApplication.getInstance().showWindow(win);
						win.center();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			editButton.setIcon(new ThemeResource("Images/edit.png"));
			editButton.setStyleName(BaseTheme.BUTTON_LINK);
			editButton.setDescription("Edit");
			table.getContainerProperty(id, BomInspectItemGroupAnswerType.HEADING_EDIT).setValue(editButton);
		
			final Button addButton = new Button("", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event)
				{
					try
					{
						Component parent = table.getParent();
						while(parent!= null)
						{
							if(parent instanceof SurveyItemConfigHolder && 
									((SurveyItemConfigHolder)parent).getSurveyItem() instanceof BaseInspectionItemGroupAnswerType)
							{
								
								((SurveyItemConfigHolder)parent).getFormDesigner().addNewItemToForm(
										SurveyItemManager.BomInspectItemAnswerType.getDescription(), 
										((SurveyItemConfigHolder)parent), getSurveyItemId());
								break;
							}
							parent = parent.getParent();
						}
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			addButton.setIcon(new ThemeResource("Images/add.png"));
			addButton.setStyleName(BaseTheme.BUTTON_LINK);
			addButton.setDescription("Add new row");
			table.getContainerProperty(id, BomInspectItemGroupAnswerType.HEADING_ADD_ROW).setValue(addButton);
		
			
			final Button deleteButton = new Button("", new Button.ClickListener() {
				@Override
				public void buttonClick(ClickEvent event)
				{
					try
					{
						EtestApplication.getInstance().showWindow(new ConfirmWindow("Are you sure you want to delete this item?", new ConfirmWindow.ConfirmListner()
						{
							@Override
							public void cancelled()
							{
							}
		
							@Override
							public void confirm()
							{
								try
								{
									Component parent = table.getParent();
									while(parent!= null)
									{
										if(parent instanceof SurveyItemConfigHolder && 
												((SurveyItemConfigHolder)parent).getSurveyItem() instanceof BaseInspectionItemGroupAnswerType)
										{
											((SurveyItemConfigHolder)parent).deleteFormItem(getSurveyItemId());
											table.removeItem(getSurveyItemId());
											break;
										}
										parent = parent.getParent();
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}));
					}
					catch(Exception e)
					{
						e.printStackTrace();	
					}
				}
			});
			deleteButton.setIcon(new ThemeResource("Images/delete.png"));
			deleteButton.setStyleName(BaseTheme.BUTTON_LINK);
			deleteButton.setDescription("Delete row");
			table.getContainerProperty(id, BomInspectItemGroupAnswerType.HEADING_DELETE_ROW).setValue(deleteButton);
		
		}
	}

	@Override
	public RadioButtonAnswerType.ConfigForm drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse, Component parent, String[] flags,  FormEventListner formEventListner)
	{
		return null;
	}

	public void drawResponseFieldInt(UnitFormQuery testProc, final SurveyResponse sResponse, 
			TSTableContainer table, int index,  String[] flags, final FormEventListner formEventListner)
	{
		this.testProc = testProc;
		
		final UserContext userContext = (EtestApplication.getInstance()).getUserContext();
		SurveyItemResponse itemResponse = null;
	    if (sResponse != null)
	    {
	    	itemResponse = sResponse.getAnswer(this);
	    }

		List<String> flagList = new ArrayList();
		for (int i = 0; i < flags.length; i++)
		{
			flagList.add(flags[i]);
		}
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDBUTMANDATORYONLY) ||
				flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDONLY))
		{
			if(isRequired())
			{
				List errors = validateResponse(itemResponse);
				if(errors != null && errors.size() > 0)
				{
					//print this item
				}
				else
				{
					return; // dont print this item
				}
			}
		}
		
		
		String answer = "";
		int selectedValue = 0;
		String canswer = "";
		if (itemResponse != null)
		{
			for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
			{
				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
				if (this.actualValueKey == aUnit.getKey2() && aUnit.getKey4() != null)
				{
					answer = aUnit.getKey4();
				}
				else if (this.selectionKey == aUnit.getKey2())
				{
					selectedValue = aUnit.getKey3();
				}
				else if (this.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null)
				{
					canswer = aUnit.getKey4();
				}
				else if (this.imageKey == aUnit.getKey2())
				{
					responseImageFileNames.add(aUnit.getKey4());
				}
			}
		}
		
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWFAILONLY))
		{
			if(selectedValue == BomInspectItemAnswerType.failVal)
			{
			}
			else
			{
				return;
			}
		}
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWCOMMENTONLY))
		{
			if(canswer.trim().length() == 0)
			{
				return;
			}
		}

		String tableRowId = this.getSurveyItemId();
		table.addItem(tableRowId);

		String expectedValHeading = null;
		String expectedVal = null;
		String unitVal = null;
		for (int j = 0; j < customFields.size(); j++)
		{
			Option aOption = customFields.getOptionByIndex(j);
			String headingText = aOption.getText();
			if (BomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				expectedValHeading = headingText;
				expectedVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				unitVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_ACTUAL.equalsIgnoreCase(headingText))
			{
				if(showActualValue)
				{
					actualValueTxt = new TextField();
					actualValueTxt.setMaxLength(16);
					actualValueTxt.addListener(new BlurListener() {
	
						@Override
						public void blur(BlurEvent event)
						{
							formEventListner.fillDataEvent(BomInspectItemAnswerType.this, validate());
							
							if(dataType != DataTypes.DATATYPE_INTEGER || result == null || actualValueTxt == null)
							{
								return;
							}
							
							//disable the NA box if text is entered in the actual field
							if(((TextField)event.getComponent()).getValue() != null && 
									((String)((TextField)event.getComponent()).getValue()).trim().length() > 0)
							{
								result.setEnabled(naVal, false);
							}
							else
							{
								result.setEnabled(naVal, true);
							}

							//check if th range is correct and select/deselect the pass/fail fox
							String expectedValue = getExpectedValue();
							if(expectedValue == null || expectedValue.trim().length() == 0)
							{
								return;
							}
							
							try
							{
								ExpectedNumericValue expUL = getExpectedUpperLower();
								double actVal = Double.parseDouble(""+actualValueTxt.getValue());
								if(expUL.isValueInRange(actVal))
								{
									result.setValue(AdvancedBomInspectItemAnswerType.passVal);
								}
								else
								{
									result.setValue(AdvancedBomInspectItemAnswerType.failVal);
								}
							}
							catch(Exception e)
							{
								result.unset();
							}
						}
					});
					actualValueTxt.setValue(answer);
					table.getContainerProperty(tableRowId, headingText).setValue(actualValueTxt);
					
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						actualValueTxt.setEnabled(false);
					}
				}
			}
			else if (BomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					this.result = new SingleSelectCheckboxGroup();
//					result.addListener(new BlurListener() {
//	
//						@Override
//						public void blur(BlurEvent event)
//						{
//							formEventListner.fillDataEvent(BomInspectItemAnswerType.this, validate());
//						}
//					});
					result.addItem(this.passVal);
					result.setItemCaption(passVal, "Pass");
	
					result.addItem(this.failVal);
					result.setItemCaption(failVal, "Fail");
	
					if(showNotApplicable)
					{
						result.addItem(this.naVal);
						result.setItemCaption(naVal, "Not Applicable");
					}
					
					table.getContainerProperty(tableRowId, headingText).setValue(result);
	
					if(selectedValue != 0)
					{
						result.setValue(selectedValue);
					}
					
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						result.setEnabled(false);
					}
				}
			}
			else if (BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					commentsTxt = new ExpandableTextArea();
					if(commentFieldMaxLength != null)
						commentsTxt.setMaxLength(commentFieldMaxLength);
					else
						commentsTxt.setMaxLength(5000);
					
					commentsTxt.addListener(new BlurListener() {
	
						@Override
						public void blur(BlurEvent event)
						{
							formEventListner.fillDataEvent(BomInspectItemAnswerType.this, validate());
						}
					});
					commentsTxt.setValue(canswer);
					table.getContainerProperty(tableRowId, headingText).setValue(commentsTxt);
				}
			}
			else
			{
				String txt = "";
				if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() > 0)
				{
					txt = aOption.getTextDesc().replace("\n", "<br/>");
				}
				if(headingText.equals(imageAttachCol) && imageFileName != null)
				{
					VerticalLayout l = new VerticalLayout(); 
					l.addComponent(new Label(txt, Label.CONTENT_XHTML));
					try
					{
						float colWidthPercent = 0;
						try
						{
							Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(headingText);
							colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
						}catch(Exception e){}
						InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

						if(l.getComponentCount() > 0)
							l.addComponent(new VSpacer(10));
						l.addComponent(img);
					}
					catch (Exception e)
					{
						logger.error("Cannot read file - " + imageFileName, e);
					}
					table.getContainerProperty(tableRowId, headingText).setValue(l);
				}
				else
				{
					table.getContainerProperty(tableRowId, headingText).setValue(new Label(txt, Label.CONTENT_XHTML));
				}
			}
		}

		if(dataType == DataTypes.DATATYPE_INTEGER && result != null && showActualValue && showExpectedValue && expectedVal != null && expectedVal.trim().length() > 0)
		{
			try
			{
				ExpectedNumericValue expUL = getExpectedUpperLower();
				if(expUL != null)
				{
					result.setEnabled(this.passVal, false);
					result.setEnabled(this.failVal, false);
				}
			}
			catch(Exception e)
			{
				logger.info("Expected value not in value(lower, upper) format, ");
			}
		}
		
		if(expectedVal != null || unitVal != null)
		{
			if(showExpectedValue && table.getContainerProperty(tableRowId, expectedValHeading) != null)//checking for the heading as these is a potential bug in table design
			{
				StringBuffer sb = new StringBuffer();
				if(expectedVal != null)
					sb.append(expectedVal);
				if(expectedVal != null && unitVal != null)
					sb.append(" ");
				if(unitVal != null)
					sb.append(unitVal);
				String txt = sb.toString().replace("\n", "<br/>");
				if(expectedValHeading.equals(imageAttachCol) && imageFileName != null)
				{
					VerticalLayout l = getMultiLine(sb.toString());
					try
					{
						float colWidthPercent = 0;
						try
						{
							Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(expectedValHeading);
							colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
						}catch(Exception e){}
						InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

						if(l.getComponentCount() > 0)
							l.addComponent(new VSpacer(10));
						l.addComponent(img);
					}
					catch (Exception e)
					{
						logger.error("Cannot read file - " + imageFileName, e);
					}
					table.getContainerProperty(tableRowId, expectedValHeading).setValue(l);
				}
				else
				{
					table.getContainerProperty(tableRowId, expectedValHeading).setValue(new Label(txt, Label.CONTENT_XHTML));
				}
			}
		}

		//response image attached to this item if any
		VerticalLayout attach;
		if(table.getContainerProperty(tableRowId, BomInspectItemGroupAnswerType.HEADING_ATTACH).getValue() == null)
		{
			attach = new VerticalLayout();
			attach.setSizeUndefined();
			attach.setSpacing(true);
			table.getContainerProperty(tableRowId, BomInspectItemGroupAnswerType.HEADING_ATTACH).setValue(attach);
		}
		else
		{
			attach = (VerticalLayout)table.getContainerProperty(tableRowId, BomInspectItemGroupAnswerType.HEADING_ATTACH).getValue();
			attach.removeAllComponents();
		}

		List<AttachmentInfoBean> attachments = new ArrayList<AttachmentInfoBean>();
		for (Iterator iterator = responseImageFileNames.iterator(); iterator.hasNext();)
		{
			String aFileName = (String) iterator.next();
			
			AttachmentInfoBean aBean = new AttachmentInfoBean();
			aBean.setFileDisplayName(aFileName);
			aBean.setFileName(aFileName);
			attachments.add(aBean);
		}
		Component imageAttachField = new AttachmentField(attachments, false, true, new AttachmentFieldListener() {
			
			@Override
			public void attachmentAdded(AttachmentInfoBean attachmentBean)
			{
				responseImageFileNames.add(attachmentBean.getFileName());
			}
		});
		attach.addComponent(imageAttachField);
		attach.setComponentAlignment(imageAttachField, Alignment.MIDDLE_CENTER);
		
		if(this.showPassFail || this.showActualValue || this.showComments)
		{
			if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS, new SecurityContext(null, null, null, null, null)))
			{
				final Button detailButton = new Button("", new ClickListener() {
	
					@Override
					public void buttonClick(ClickEvent event)
					{
						try
						{
							(EtestApplication.getInstance()).showWindow( 
							new InspectionItemAnswerViewerReport(userContext, BomInspectItemAnswerType.this.testProc.getPk(), sResponse.getSurveyDefinition(), 
									sResponse.getSurveyPk(), BomInspectItemAnswerType.this));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							(EtestApplication.getInstance()).showError("Could not open details for units, Please try again later");
						} 
					}
				});
				detailButton.setStyleName(BaseTheme.BUTTON_LINK);
				detailButton.setIcon(new ThemeResource("Images/barchart.png"));
				detailButton.setDescription("View details for all Units");
				
	    		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWGRAPHBUTTON))
	    		{
	    			attach.addComponent(detailButton);
	    			attach.setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
	    		}
			}
		}
	
		
		if (sResponse != null)
		{
			final FormItemResponse formItemResponse = SurveyResponseDelegate.getFormItemResponse(sResponse.getResponseId(), BomInspectItemAnswerType.this.getSurveyItemId(), true);
			oilItemList = OILDelegate.getTestProcItemFailureQuery(formItemResponse.getOID());

// no need for comments during dataentry			
//			CommentsThreadPopup commentsB = new CommentsThreadPopup(EntityTypeEnum.FormItemResponse, formItemResponse.getPk(), FormItemResponse.COMMENT_CONTEXT_NOT_TO_PRINT, CommentsThreadPopup.ICON_STYLE_SMALL);
//			attach.addComponent(commentsB);
		
			//button to create a p8
			if ((ResponseMasterNew.STATUS_INPROGRESS.equals(sResponse.getStatus())))
			{
				try 
				{
					
					P8TransferMenuBar p8Menubar = new P8TransferMenuBar(sResponse, DisplayModeEnum.DataEntryMode);
					p8Menubar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
					attach.addComponent(p8Menubar);
					p8Menubar.setVisible(true);
				}						
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//table.addItem(cols, index);
	}
	
	private VerticalLayout getMultiLine(String data) {
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setImmediate(true);
		vlayout.addStyleName("inTable_verticalLayout");
		// descVLayout.setWidth("200px");
		vlayout.setWidth("100%");
		if (data == null){
			vlayout.addComponent(new Label(""));
			return vlayout;
		}
		String[] ary = data.split("\n");
		for (String s:ary){
			Label descField = new Label(s);
			//descField.setContentMode(Label.CONTENT_PREFORMATTED);
			vlayout.addComponent(descField);
		}
		return vlayout;
	}

	@Override
	public SurveyItemResponse captureResponse() throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();

		if(actualValueTxt != null)
		{
			String answerText = (String) actualValueTxt.getValue();
			if(answerText != null && answerText.trim().length() > 0)
			{
				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey2(actualValueKey);
				aUnit.setKey4(answerText.trim());
				itemResponse.addResponseUnit(aUnit);
				if(this.getDataType() == DataTypes.DATATYPE_INTEGER)
				{
					try
					{
						Float.parseFloat(answerText.trim());
					}
					catch(Exception e)
					{
						throw new InvalidResponseException(this.getQuestionText() + " - Actual Value should be Numeric");
					}
				}
			}
		}
		
		if(commentsTxt != null)
		{
			String commText = (String) commentsTxt.getValue();
			if(commText != null && commText.trim().length() > 0)
			{
				ResponseUnit cUnit = new ResponseUnit();
				cUnit.setKey2(commentsKey);
				cUnit.setKey4(commText.trim());
				itemResponse.addResponseUnit(cUnit);
			}
		}
		
		if(result != null)
		{
			Object tfSelection = this.result.getValue();
			if (tfSelection != null && ((Integer)tfSelection).intValue() != 0)
			{
				int resultVal = ((Integer)tfSelection).intValue(); 
				ResponseUnit sUnit = new ResponseUnit();
				sUnit.setKey2(selectionKey);
				sUnit.setKey3(resultVal);
				itemResponse.addResponseUnit(sUnit);
			}
		}
		
		if(responseImageFileNames != null)
		{
			for (Iterator iterator = responseImageFileNames.iterator(); iterator.hasNext();)
			{
				String aFileName = (String) iterator.next();

				ResponseUnit sUnit = new ResponseUnit();
				sUnit.setKey2(imageKey);
				sUnit.setKey4(aFileName);
				itemResponse.addResponseUnit(sUnit);
			}
		}
			
		return itemResponse;
	}
	
	public List<String> validateResponse(SurveyItemResponse itemResponse)
	{
		List<String> errors = new ArrayList<String>();
		if(showActualValue  || showPassFail || showComments)
		{
			if(itemResponse == null || itemResponse.getResponseUnits() == null || itemResponse.getResponseUnits().size()==0)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Cannot be left unanswered");
				return errors;
			}
		}
		else
		{ // no fields are there to be entered for this.. so nothing to validate
			return null;
		}
		
		boolean passFailSelected = false;
		boolean failSelected = false;
		boolean actualValueEntered = false;
		boolean commentEntered = false;
		String commentAnswer = null;
		for(int i=0; i<itemResponse.getResponseUnits().size(); i++)
		{
			ResponseUnit aR = (ResponseUnit) itemResponse.getResponseUnits().get(i);
			if(aR.getKey2() == selectionKey)
			{
				passFailSelected = true;
				if(aR.getKey3() == failVal)
				{
					failSelected = true;
				}
			}
			else if(aR.getKey2() == actualValueKey)
			{
				String act = aR.getKey4();
				if(act != null&& act.trim().length() > 0)
				{
					actualValueEntered = true;
				}
			}
			else if(aR.getKey2() == commentsKey)
			{
				commentAnswer = aR.getKey4();
				if(commentAnswer != null&& commentAnswer.trim().length() > 0)
				{
					commentEntered = true;
				}
			}
		}
		
		if(showPassFail)
		{
			if(passFailSelected == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Pass, Fail or Not applicable should be selected.");
			}
		}
		else if(showActualValue)
		{
			if(actualValueEntered == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Actual value should be entered.");
			}
		}
		else if(showComments)
		{
			if(commentEntered == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Comment should be entered.");
			}
			else
			{
				if(commentFieldMaxLength != null)
				{
					if(commentAnswer.length() > commentFieldMaxLength)
					{
						Section sec = (Section) this.getParent().getParent();
						errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Comment too long.");
					}
				}
			}
		}
		
		if(showPassFail && showComments)
		{
			if(passFailSelected == true && failSelected == true && commentEntered == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - When Fail is selected, Comments should be entered.");
			}
		}
		if(errors.size() == 0)
			return null;
		else
			return errors;
			
	}

	@Override
	public Component drawResponseDetail(UserContext userContext, UnitFormQuery testProc, SurveyResponse sResponse, Component parent, 
			boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean drawResponseDetailInt(final UserContext userContext, UnitFormQuery testProc,  
			final SurveyResponse sResponse, TSTableContainer  table, int index, boolean isLatestResponse, String[] flags,
			final TestProcController testProcController)
	{
		this.testProc = testProc;
		List thisItemFlagList = this.getFlagsAsList();
		boolean matchFlagsToDisplay = false;
		boolean flagsMatch = false;
		
		if (sResponse != null)
		{
			formItemResponse = testProcController.getFormItemResponseMap().get(getSurveyItemId());
			if(formItemResponse == null)
				formItemResponse = SurveyResponseDelegate.createFormItemResponse(sResponse.getResponseId(), BomInspectItemAnswerType.this.getSurveyItemId());
		}		

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
			return false;
		}
		
		oilItemList  = testProcController.getOilTransferMap().get(getSurveyItemId());
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWTRANSFERREDITEMSONLY) && (oilItemList == null || oilItemList.size() == 0))
		{
			return false;
		}
		
		boolean isFail = false;
		boolean hasComment = false;
		
		SurveyItemResponse itemResponse = null;
	    if (sResponse != null)
	    {
	    	itemResponse = sResponse.getAnswer(this);
	    }

		int selectedValue = 0;

		answer = "";
		canswer = "";
		passFailResult = "";
		if (itemResponse != null)
		{
			for (int k = 0; k < itemResponse.getResponseUnits().size(); k++)
			{
				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(k);
				if (this.selectionKey == aUnit.getKey2())
				{
					selectedValue = aUnit.getKey3();
				}
				else if (this.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null)
				{
					canswer = aUnit.getKey4();
					if(canswer != null && canswer.trim().length() > 0)
					{
						hasComment = true;
					}
				}
				else if (this.actualValueKey == aUnit.getKey2() && aUnit.getKey4() != null)
				{
					answer = aUnit.getKey4();
				}
				else if(this.imageKey == aUnit.getKey2())
				{
					responseImageFileNames.add(aUnit.getKey4());
				}
			}
		}

		if (selectedValue == this.passVal)
		{
			passFailResult = "Pass";
		}
		else if (selectedValue == this.failVal)
		{
			passFailResult = "Fail";
			isFail = true;
		}
		else if (selectedValue == this.naVal)
		{
			passFailResult = "Not Applicable";
		}

		boolean b = flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWCOMMENTONLY); 
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWFAILONLY) && isFail == false)
		{
			return false;
		}
		else if(b== true && hasComment == false)
		{
			return false;
		}
		
		String tableRowId = this.getSurveyItemId();
		table.addItem(tableRowId);

		if (formItemResponse != null)
		{
			CheckBox selectCheckbox = new CheckBox();
			selectCheckbox.addValueChangeListener(new ValueChangeListener() {
			
				@Override
				public void valueChange(ValueChangeEvent event) 
				{
					if(event.getProperty().getValue() != null && true == (Boolean)event.getProperty().getValue())
						testProcController.itemSelected(BomInspectItemAnswerType.this, formItemResponse);
					else
						testProcController.itemDeSelected(BomInspectItemAnswerType.this, formItemResponse);
						
				}
			});
			table.getContainerProperty(tableRowId, AdvancedBomInspectItemGroupAnswerType.HEADING_CHECKBOX).setValue(selectCheckbox);
		}
		
		
		VerticalLayout descVLayout = new VerticalLayout();
		//descVLayout.setWidth("200px");
		descVLayout.setWidthUndefined();
     	Label descField = new Label(this.getQuestionTextDescription());
     	descField.setContentMode(Label.CONTENT_PREFORMATTED);
     	descVLayout.addComponent(descField);
     	
     	String actualValHeading = null;
     	String actualVal = null;
     	String expectedValHeading = null;
		String expectedVal = null;
		String unitVal = null;
		for (int j = 0; j < customFields.size(); j++)
		{
			Option aOption = customFields.getOptionByIndex(j);
			String headingText = aOption.getText();
			if (BomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				expectedValHeading = headingText;
				expectedVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				unitVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_ACTUAL.equalsIgnoreCase(headingText))
			{
				actualVal = answer;
				actualValHeading = headingText;
			}
			else if (BomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					table.getContainerProperty(tableRowId, headingText).setValue(passFailResult);
				}
			}
			else if (BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
			}
			else if (BomInspectItemGroupAnswerType.HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					Label lbl = new Label(canswer);
					lbl.setDescription(canswer.replaceAll("\n", "<br/>"));
					table.getContainerProperty(tableRowId, headingText).setValue(lbl);
				}
			}
			else
			{
				String txt = "";
				if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() > 0)
				{
					txt = aOption.getTextDesc().replace("\n", "<br/>");
				}

				if(headingText.equals(imageAttachCol) && imageFileName != null)
				{
					VerticalLayout l = new VerticalLayout(); 
					l.addComponent(new Label(txt, Label.CONTENT_XHTML));
					try
					{
						float colWidthPercent = 0;
						try
						{
							Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(headingText);
							colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
						}catch(Exception e){}
						InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

						if(l.getComponentCount() > 0)
							l.addComponent(new VSpacer(10));
						l.addComponent(img);
					}
					catch (Exception e)
					{
						logger.error("Cannot read file - " + imageFileName, e);
					}
					table.getContainerProperty(tableRowId, aOption.getText()).setValue(l);
				}
				else
				{
					table.getContainerProperty(tableRowId, aOption.getText()).setValue(new Label(txt, Label.CONTENT_XHTML));
				}
			}
		}

		if(expectedVal != null || unitVal != null)
		{
			if(showExpectedValue)
			{
				StringBuffer sb = new StringBuffer();
				if(expectedVal != null)
					sb.append(expectedVal);
				if(expectedVal != null && unitVal != null)
					sb.append(" ");
				if(unitVal != null)
					sb.append(unitVal);

				String txt = sb.toString().replace("\n", "<br/>");
				if(expectedValHeading.equals(imageAttachCol) && imageFileName != null)
				{
					VerticalLayout l = new VerticalLayout();
					l.addComponent(new Label(txt, Label.CONTENT_XHTML));
					try
					{
						float colWidthPercent = 0;
						try
						{
							Option parentOption = ((BaseInspectionItemGroupAnswerType)this.getParent()).getCustomFields().getOptionByName(expectedValHeading);
							colWidthPercent = Float.parseFloat(parentOption.getTextDesc());
						}catch(Exception e){}
						InspectionItemImage img = new InspectionItemImage(imageFileName, colWidthPercent);

						if(l.getComponentCount() > 0)
							l.addComponent(new VSpacer(10));
						l.addComponent(img);
					}
					catch (Exception e)
					{
						logger.error("Cannot read file - " + imageFileName, e);
					}
					table.getContainerProperty(tableRowId, expectedValHeading).setValue(l);
				}
				else
				{
					table.getContainerProperty(tableRowId, expectedValHeading).setValue(new Label(txt, Label.CONTENT_XHTML));
				}
			}
		}
		if(showActualValue)
		{
			if(actualVal != null && actualVal.trim().length() > 0)
			{
				StringBuffer sb = new StringBuffer(actualVal);
				if(unitVal != null && unitVal.trim().length() > 0)
				{
					sb.append(" " + unitVal);
				}
				table.getContainerProperty(tableRowId, actualValHeading).setValue(sb.toString());
			}
		}


		VerticalLayout icons = new VerticalLayout();
		icons.setSpacing(true);
		icons.setSizeUndefined();
		table.getContainerProperty(tableRowId, BomInspectItemGroupAnswerType.HEADING_ICONS).setValue(icons);

		if(this.showPassFail || this.showActualValue || this.showComments)
		{
			if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS, new SecurityContext(null, null, null, null, null)))
			{
				final Button detailButton = new Button("", new ClickListener() {
	
					@Override
					public void buttonClick(ClickEvent event)
					{
						try
						{
							(EtestApplication.getInstance()).showWindow( 
							new InspectionItemAnswerViewerReport(userContext, BomInspectItemAnswerType.this.testProc.getPk(), sResponse.getSurveyDefinition(), 
									sResponse.getSurveyPk(), BomInspectItemAnswerType.this));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							(EtestApplication.getInstance()).showError("Could not open details for units, Please try again later");
						} 
					}
				});
				detailButton.setStyleName(BaseTheme.BUTTON_LINK);
				detailButton.setIcon(new ThemeResource("Images/barchart.png"));
				detailButton.setDescription("View details for all Units");
				
	    		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWGRAPHBUTTON))
	    		{
	    			icons.addComponent(detailButton);
	    			icons.setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
	    		}
			}
		}

		
		//image attached to this item if any
		if(responseImageFileNames != null && responseImageFileNames.size() > 0)
		{
			List<AttachmentInfoBean> attachments = new ArrayList<AttachmentInfoBean>();
			for (Iterator iterator = responseImageFileNames.iterator(); iterator.hasNext();)
			{
				String aFileName = (String) iterator.next();
				
				AttachmentInfoBean aBean = new AttachmentInfoBean();
				aBean.setFileDisplayName(aFileName);
				aBean.setFileName(aFileName);
				attachments.add(aBean);
			}
			Component imageAttachField = new AttachmentField(attachments, true, true, null);
			icons.addComponent(imageAttachField);
			icons.setComponentAlignment(imageAttachField, Alignment.MIDDLE_CENTER);
		}
		
		//previous answers to question
		if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSEITEM_WITH_OLD_VERSIONS, new SecurityContext(null, null, null, null, null)))
		{
			final Button detailButton = new Button("", new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event)
				{
					try
					{
						new QuestionSummaryViewer(BomInspectItemAnswerType.this.testProc.getPk(), sResponse.getSurveyDefinition(), 
								sResponse.getSurveyPk(), BomInspectItemAnswerType.this).showQuestionResponseHistoryUnit();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						(EtestApplication.getInstance()).showError("Could not open history information for unit, Please try again later");
					} 
				}
			});
			detailButton.setStyleName(BaseTheme.BUTTON_LINK);
			detailButton.setIcon(new ThemeResource("Images/versions.png"));
			detailButton.setDescription("View previous answers for this unit");
			
    		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWPREVIOUS_ENTRIES_BUTTON))
    		{
    			icons.addComponent(detailButton);
    			icons.setComponentAlignment(detailButton, Alignment.MIDDLE_CENTER);
    		}
		}
		
		//button to create a p8
		if (sResponse != null)
		{
			oilItemList  = testProcController.getOilTransferMap().get(getSurveyItemId());

			if(isLatestResponse)
			{
				CommentsThreadPopup commentsB = new CommentsThreadPopup(EntityTypeEnum.FormItemResponse, formItemResponse.getPk(), FormItemResponse.COMMENT_CONTEXT_NOT_TO_PRINT, CommentsThreadPopup.ICON_STYLE_SMALL);
				icons.addComponent(commentsB);
			}
			
			if((ResponseMasterNew.STATUS_COMPLETE.equals(sResponse.getStatus()) 
					|| ResponseMasterNew.STATUS_VERIFIED.equals(sResponse.getStatus())
					|| ResponseMasterNew.STATUS_APPROVED.equals(sResponse.getStatus())
					|| ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS.equals(sResponse.getStatus())
					))
			{
				try 
				{

					P8TransferMenuBar p8Menubar = new P8TransferMenuBar(sResponse, DisplayModeEnum.ViewMode);
					p8Menubar.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
					icons.addComponent(p8Menubar);
					if(isLatestResponse == false)
					{
						p8Menubar.setVisible(false);
					}
					
					
// when 1 ncr was linked, we were disabling the checkbox. this is not needed, users can create multiple ncrs and p8s from a form item					
//					if(oilItemList != null && oilItemList.size() > 0)
//					{
//						if(table.getContainerProperty(tableRowId, AdvancedBomInspectItemGroupAnswerType.HEADING_CHECKBOX) != null)
//						{
//							CheckBox cBox = (CheckBox) table.getContainerProperty(tableRowId, AdvancedBomInspectItemGroupAnswerType.HEADING_CHECKBOX).getValue();
//							if(cBox != null)
//								cBox.setEnabled(false);
//						}
//					}
					
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}

	public String[] validate()
	{
		List<String> msgs = new ArrayList();

		if(actualValueTxt != null)
		{
			String answerText = (String) actualValueTxt.getValue();
			if (answerText != null && answerText.trim().length() > DEFAULT_MAXLENGTH)
			{
				msgs.add(this.getIdentifier()+ " - " + "Actual value" + ": Answer should not be longer than " + DEFAULT_MAXLENGTH);
			}
		}
		
		String commText = null;
		if(commentsTxt != null)
		{
			commText = (String) commentsTxt.getValue();
			if (commText != null && commText.trim().length() > DEFAULT_MAXLENGTH)
			{
				msgs.add(this.getIdentifier()+ " - " + "Comments" + ": Answer should not be longer than " + DEFAULT_MAXLENGTH);
			}
		}
		
		if(this.result != null)
		{
			if (this.result.getValue() != null) {
				int resultVal = ((Integer)this.result.getValue()).intValue();
				
				if (resultVal == 0)
				{
					msgs.add(this.getIdentifier()+ " - " + "One of Pass, Fail or N/A should be selected");
				}
				// if fail is selected, comments should be written
				if(commentsTxt != null)
				{
					if (resultVal == failVal && commText.trim().length() == 0)
					{
						msgs.add(this.getIdentifier()+ " - " + "When Fail is selected, Comments should be provided");
					}
				}
			}
		}		
		
		return (String[])msgs.toArray(new String[msgs.size()]);
	}

	public class ConfigForm extends CustomComponent
	{
		VerticalLayout root;
		Form theForm;
		FormDesignListener formDesignListener;
		BomInspectItemAnswerType surveyItem;
		Select imageAttachCol = null;
		TextField flagField = null;
		TextField commentFieldLengthField = new TextField("Max comment length");
		FileUploadForm fileUpload = null;
		public ConfigForm(BomInspectItemAnswerType surveyItem, FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			root = new VerticalLayout();
			this.setCompositionRoot(root);
		}
		
		public void attach()
		{
			super.attach();
			root.removeAllComponents();
			
			root.findAncestor(Window.class).setCaption("Inspection Item Details");
			
			Panel panel = new Panel();
			panel.setContent(new VerticalLayout());
			((VerticalLayout)panel.getContent()).setMargin(true);
			panel.setHeight("400px");
			panel.setWidth("100%");
			
			root.addComponent(panel);
			
			theForm = new Form();
			((ComponentContainer)panel.getContent()).addComponent(theForm);
			
			//if the Item.customFields is null, copy the custom fields from the parent to this item.. do a deep copy
			//This condition happens when a new item is created from the gui.. The designform where the new object is created is a wrong place to do that
			//so do it here..
			if(surveyItem.getCustomFields() == null || surveyItem.getCustomFields().size() == 0)
			{
				OptionList childCustomFields = new OptionList();
				OptionList parentCustomFields = ((BaseInspectionItemGroupAnswerType)surveyItem.getParent()).getCustomFields(); 
				for (int k = 0; k < parentCustomFields.size(); k++)
				{
					Option parentOption = parentCustomFields.getOptionByIndex(k);
					Option aOption = new Option(parentOption.getText(), "");
					childCustomFields.add(aOption);
				}
				surveyItem.setCustomFields(childCustomFields);
			}			
			
			List<String> imageAttachCols = new ArrayList<String>(); 
			for (Iterator iterator = surveyItem.getCustomFields().iterator(); iterator.hasNext();)
			{
				Option aOption = (Option) iterator.next();
				if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_UNIT) ||
						aOption.getText().equals(BomInspectItemGroupAnswerType.HEADING_ITEMNO))
				{
					TextField f = new TextField(aOption.getText());
					f.setColumns(40);
					f.setValue(aOption.getTextDesc());
					f.setNullRepresentation("");
					theForm.addField(aOption.getText(), f);
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ATTACH) ||
						aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_EDIT) ||
						aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ICONS))
				{
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE))
				{
					CheckBox k = new CheckBox("Show Not Applicable");
					if(surveyItem.isShowNotApplicable())
						k.setValue(true);
					theForm.addField(aOption.getText(), k);
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_COMMENTS))
				{
					CheckBox k = new CheckBox("Show Comments");
					k.setImmediate(true);
					
					k.addValueChangeListener(new ValueChangeListener() {
						
						@Override
						public void valueChange(ValueChangeEvent event)
						{
							if(true == k.getValue())
							{
								commentFieldLengthField.setVisible(true);
								if(commentFieldMaxLength != null)
									commentFieldLengthField.setValue(commentFieldMaxLength.toString());
							}
							else
							{
								commentFieldLengthField.setVisible(false);
								commentFieldLengthField.setValue(null);
							}
						}
					});

					theForm.addField(aOption.getText(), k);

					commentFieldLengthField.setMaxLength(4);
					commentFieldLengthField.setColumns(6);
					commentFieldLengthField.setNullRepresentation("");
					commentFieldLengthField.setVisible(false);
					theForm.addField("commentFieldMaxLength", commentFieldLengthField);

					if(surveyItem.isShowComments())
						k.setValue(true);
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_RESULT))
				{
					CheckBox k = new CheckBox("Show Pass/Fail");
					if(surveyItem.isShowPassFail())
						k.setValue(true);
					theForm.addField(aOption.getText(), k);
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ACTUAL))
				{
					CheckBox k = new CheckBox("Show Actual Field");
					if(surveyItem.isShowActualValue())
						k.setValue(true);
					theForm.addField(aOption.getText(), k);
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_DATATYPE))
				{
					CheckBox k = new CheckBox("Is Numeric");
					if(surveyItem.getDataType() == DataTypes.DATATYPE_INTEGER)
						k.setValue(true);
					theForm.addField(aOption.getText(), k);
				}
				else
				{
					TextArea f = new TextArea(aOption.getText());
					f.setColumns(40);
					f.setNullRepresentation("");
					f.setValue(aOption.getTextDesc());
					theForm.addField(aOption.getText(), f);

					imageAttachCols.add(aOption.getText());
				}
			}
			
			flagField = new TextField("Flags");
			flagField.setColumns(40);
			flagField.setValue(surveyItem.getFlags());
			flagField.setNullRepresentation("");
			theForm.addField("Flags", flagField);

			if(imageAttachCols.size() > 0)
			{
				fileUpload = new FileUploadForm("Attach Picture", "jpg,jpeg,png,gif,tiff,tif", imageFileDisplayName, 
						(imageFileName != null)?FileStoreManager.getFile(imageFileName):null, false, new FileUploadForm.FileUploadListener() {
							
							@Override
							public void uploadComplete(File file, String fileDisplayName)
							{
							}
							
							@Override
							public void uploadCancelled()
							{
							}
				});
				theForm.getLayout().addComponent(fileUpload);
			
				imageAttachCol = new Select();
				imageAttachCol.setCaption("Show Image under Column");
				for (Iterator iterator = imageAttachCols.iterator(); iterator.hasNext();)
				{
					imageAttachCol.addItem((String) iterator.next());
				}
				theForm.getLayout().addComponent(imageAttachCol);
				imageAttachCol.setValue(BomInspectItemAnswerType.this.imageAttachCol);
			}	
			
			root.addComponent(new VSpacer(20));
			
			/* Add buttons in the form. */
			HorizontalLayout buttonArea = new HorizontalLayout();
			buttonArea.setSpacing(true);
			root.addComponent(buttonArea);

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
			Button cancelbutton = new Button("", new ClickListener() {
				
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
				theForm.commit();

				if(flagField != null && flagField.getValue() != null)
				{
					surveyItem.setFlags((String)flagField.getValue());
				}
				else
				{
					surveyItem.setFlags(null);
				}
				if(fileUpload != null)
				{
					File uFile = fileUpload.getUploadedFile();
					String uFileName = fileUpload.getUploadedFileName();
							
					if(uFile != null && imageAttachCol.getValue() == null)
					{
						throw new AppException("Please select the column under which the image should be displayed");
					}

					if(uFile != null)
					{
						imageFileName = uFile.getName();
						imageFileDisplayName = uFileName;
						BomInspectItemAnswerType.this.imageAttachCol = (String) imageAttachCol.getValue();
						if(BomInspectItemGroupAnswerType.HEADING_EXPECTED.equals(BomInspectItemAnswerType.this.imageAttachCol))
						{
							surveyItem.setShowExpectedValue(true);
						}
					}
					else
					{
						imageFileName = null;
						imageFileDisplayName = null;
						BomInspectItemAnswerType.this.imageAttachCol = "";
					}
				}

				for (Iterator iterator = surveyItem.getCustomFields().iterator(); iterator.hasNext();)
				{
					Option aOption = (Option) iterator.next();
					if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_EXPECTED))
					{
						aOption.setTextDesc((String)theForm.getField(aOption.getText()).getValue());
						if(aOption.getTextDesc() == null)
							aOption.setTextDesc("");
						if(aOption.getTextDesc().trim().length() > 0)
							surveyItem.setShowExpectedValue(true);
					}
					if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_UNIT))
					{
						aOption.setTextDesc((String)theForm.getField(aOption.getText()).getValue());
						if(aOption.getTextDesc() == null)
							aOption.setTextDesc("");
						if(aOption.getTextDesc().trim().length() > 0)
							surveyItem.setShowUnit(true);
					}
					if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ITEMNO))
					{
						aOption.setTextDesc((String)theForm.getField(aOption.getText()).getValue());
						if(aOption.getTextDesc() == null)
							aOption.setTextDesc("");
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ATTACH) ||
							aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_EDIT) ||
							aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ICONS)
							)
					{
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE))
					{
						CheckBox k = (CheckBox)theForm.getField(aOption.getText());
						if(k.booleanValue() == true)
						{
							aOption.setTextDesc("y");
							surveyItem.setShowNotApplicable(true);
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setShowNotApplicable(false);
						}
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_COMMENTS))
					{
						CheckBox k = (CheckBox)theForm.getField(aOption.getText());
						if(k.booleanValue() == true)
						{
							aOption.setTextDesc("y");
							surveyItem.setShowComments(true);
							
							if(commentFieldLengthField.getValue() != null && commentFieldLengthField.getValue().trim().length() > 0)
							{
								try
								{
									commentFieldMaxLength = Integer.parseInt(commentFieldLengthField.getValue());
									surveyItem.setCommentFieldMaxLength(commentFieldMaxLength);
								}
								catch (Exception e)
								{
									commentFieldLengthField.setComponentError(new UserError("Numeric value expected"));
									return;
								}
							}
							else
							{
								commentFieldMaxLength = null;
								surveyItem.setCommentFieldMaxLength(null);
								commentFieldMaxLength = null;
								surveyItem.setCommentFieldMaxLength(null);
							}
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setShowComments(false);
						}
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_RESULT))
					{
						CheckBox k = (CheckBox)theForm.getField(aOption.getText());
						if(k.booleanValue() == true)
						{
							aOption.setTextDesc("y");
							surveyItem.setShowPassFail(true);
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setShowPassFail(false);
						}
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_ACTUAL))
					{
						CheckBox k = (CheckBox)theForm.getField(aOption.getText());
						if(k.booleanValue() == true)
						{
							aOption.setTextDesc("y");
							surveyItem.setShowActualValue(true);
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setShowActualValue(false);
						}
					}
					else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_DATATYPE))
					{
						CheckBox k = (CheckBox)theForm.getField(aOption.getText());
						if(k.booleanValue() == true)
						{
							aOption.setTextDesc("y");
							surveyItem.setDataType(DataTypes.DATATYPE_INTEGER);
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setDataType(DataTypes.DATATYPE_STRING);
						}
					}
					else
					{
						aOption.setTextDesc((String)theForm.getField(aOption.getText()).getValue());
						if(aOption.getTextDesc() == null)
							aOption.setTextDesc("");
					}
				}
				formDesignListener.formItemConfigurationComplete(surveyItem);

				UI.getCurrent().removeWindow(EtestApplication.getEnclosingWindow(this));
			}
			catch(Validator.InvalidValueException iv)
			{
//				this.setComponentError(new UserError(iv.getMessage()));
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				theForm.setComponentError(new UserError(e.getMessage()));
			}
		}

		public void discard()
		{
			theForm.discard();
			UI.getCurrent().removeWindow(EtestApplication.getEnclosingWindow(this));
			formDesignListener.formItemConfigurationCancelled(surveyItem);
		}
	}

	@Override
	public Option[] getResponseFieldOptions()
	{
		Option option = new Option("Actual Value", "", BomInspectItemAnswerType.actualValueKey);
		Option coption = new Option("Comments", "", BomInspectItemAnswerType.commentsKey);
		Option roption = new Option("Result", "", BomInspectItemAnswerType.selectionKey);
		return new Option[]{option, coption, coption, roption};
	}

	@Override
	public Option[] getActualFieldOptions()
	{
		Option option = new Option("Actual Value", "", BomInspectItemAnswerType.actualValueKey);
		return new Option[]{option};
	}

	public Option[] getAdditionalMandatoryFieldOptions()
	{
		return new Option[] {};
	}
	
	@Override
	public InspectionLineItemAnswerStatus getAnswerStatus(SurveyItemResponse sItemResponse)
	{
		InspectionLineItemAnswerStatus answerStatus = new InspectionLineItemAnswerStatus();
		for (int k = 0; k < sItemResponse.getResponseUnits().size(); k++)
		{
			ResponseUnit aUnit = (ResponseUnit) sItemResponse.getResponseUnits().get(k);
			if (BomInspectItemAnswerType.selectionKey == aUnit.getKey2())
			{
				if(aUnit.getKey3() != ResponseUnit.DEFAULT_VAL)
					answerStatus.setPassFailAnswered(true);
			
				if(BomInspectItemAnswerType.failVal == aUnit.getKey3())
				{
					answerStatus.setFailSelected(true);
				}
				if(BomInspectItemAnswerType.passVal == aUnit.getKey3())
				{
					answerStatus.setPassSelected(true);
				}
				if(BomInspectItemAnswerType.naVal == aUnit.getKey3())
				{
					answerStatus.setNaSelected(true);
				}
			}
			else if (BomInspectItemAnswerType.commentsKey == aUnit.getKey2() && aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
			{
				answerStatus.setCommentAnswered(true);
			}
			else if (BomInspectItemAnswerType.actualValueKey == aUnit.getKey2() && aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
			{
				answerStatus.setActualAnswered(true);
			}
		}
		return answerStatus;
	}

	/**
	 * Used by TestProcController to get the description when creating ncrs.
	 * @param testProcQuery
	 * @param sectionItem
	 * @param passFailResult
	 * @param tableAnswers
	 * @param testerComment
	 * @return
	 */
	public String buildOpenItemTransferDescription()
	{
		return buildOpenItemTransferDescription(testProc, (Section) this.getParent().getParent(), passFailResult, answer, canswer);
	}
	private String buildOpenItemTransferDescription(UnitFormQuery testProcQuery, Section sectionItem, String passFailResult, String actual, String testerComment)
	{
		StringBuffer labelCols = new StringBuffer();
		String labelColsep = "";
		StringBuffer actualCols = new StringBuffer();
		String actualColsep = "";
		StringBuffer commentCols = new StringBuffer();
		String commentColsep = "";
		for (int j = 0; j < customFields.size(); j++)
		{
			Option aOption = customFields.getOptionByIndex(j);
			String headingText = aOption.getText();
			BomTypesEnum cType = aOption.getType();

			if(BomInspectItemGroupAnswerType.HEADING_ITEMNO.equalsIgnoreCase(aOption.getText()))
			{
				labelCols.append(labelColsep).append("Item No: ").append(ListStringUtil.showString(aOption.getTextDesc()));
				labelColsep = "\n\n";
			}
			else if(BomInspectItemGroupAnswerType.HEADING_DESCRIPTION.equalsIgnoreCase(aOption.getText()))
			{
				labelCols.append(labelColsep).append("Description: ").append(ListStringUtil.showString(aOption.getTextDesc()));
				labelColsep = "\n\n";
			}
			else if(BomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(aOption.getText()))
			{
				String ans = passFailResult;
				if(ans != null && ans.trim().length() > 0)
				{
					commentCols.append(commentColsep).append("Pass/Fail").append(": ").append(ans);
					commentColsep = "\n\n";
				}
			}
			else if(BomInspectItemGroupAnswerType.HEADING_ACTUAL.equalsIgnoreCase(aOption.getText()))
			{
				if(actual != null && actual.trim().length() > 0)
				{
					actualCols.append(actualColsep).append(headingText).append(": ").append(actual);
					actualColsep = "\n\n";
				}
			}
			else if(BomInspectItemGroupAnswerType.HEADING_COMMENTS.equalsIgnoreCase(aOption.getText()))
			{
				if(testerComment != null && testerComment.trim().length() > 0)
				{
					commentCols.append(commentColsep).append(headingText).append(": ").append(testerComment);
					commentColsep = "\n\n";
				}
			}
			else if(BomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(aOption.getText()))
			{
				labelCols.append(labelColsep).append("Expected Value (Unit): ").append(ListStringUtil.showString(aOption.getTextDesc()));
				aOption = (Option) customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_UNIT);
				if(aOption != null && aOption.getTextDesc() != null)
				{
					labelCols.append("(").append(aOption.getTextDesc()).append(")");
				}
				labelColsep = "\n\n";
			}
			else if(BomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(aOption.getText())
					|| BomInspectItemGroupAnswerType.HEADING_DATATYPE.equalsIgnoreCase(aOption.getText())
					|| BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE.equalsIgnoreCase(aOption.getText())
					|| BomInspectItemGroupAnswerType.HEADING_FLAGS.equalsIgnoreCase(aOption.getText())
					|| BomInspectItemGroupAnswerType.HEADING_ATTACH.equalsIgnoreCase(aOption.getText()))
			{
			}
			else if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() > 0)
			{
				labelCols.append(labelColsep).append(headingText).append(": ").append(ListStringUtil.showString(aOption.getTextDesc()));
				labelColsep = "\n\n";
			}				
		}
		
		StringBuffer sb = new StringBuffer();
		if(testProcQuery.getName() != null && testProcQuery.getName().trim().length() > 0)
		{
			sb.append("Name: ").append(testProcQuery.getName()).append(", ");
		}
		sb.append("Form: ").append(testProcQuery.getFormName()).append(", ");
		sb.append("Revision(Version): ").append(testProcQuery.getFormRevision()).append("(").append(testProcQuery.getFormVersion()).append("), ");
		sb.append("Section: ").append(sectionItem.getIdentifier()).append(", ");
		if(labelCols.length() > 0)sb.append(labelCols);
		if(actualCols.length() > 0)sb.append("\n\n").append(actualCols);
		if(commentCols.length() > 0)sb.append("\n\n").append(commentCols);
		return sb.toString();
	}
	
	private class P8TransferMenuBar extends MenuBar
	{
		SurveyResponse sResponse;
		DisplayModeEnum displayMode;
		
		public P8TransferMenuBar(SurveyResponse sResponse, DisplayModeEnum displayMode)
		{
			this.sResponse = sResponse;
			this.displayMode = displayMode;
		}
		
		public void attach()
		{
			super.attach();
			buildMenu();
		}
		
		public void buildMenu()
		{
			this.removeItems();
			
			MenuItem p8Root;
			if(oilItemList != null && oilItemList.size() > 0)
			{
				p8Root = this.addItem(((oilItemList == null)?"": ("" + oilItemList.size())), new ThemeResource("Images/openitem_transfer_done.png"), null);
			}
			else
			{
				p8Root = this.addItem(((oilItemList == null)?"": ("" + oilItemList.size())), new ThemeResource("Images/openitem_transfer.png"), null);
			}
			if(oilItemList != null && oilItemList.size() > 0)
			{
				for(TestItemOILTransferQuery aOil : oilItemList)
				{
					p8Root.addItem(aOil.getOilReferenceNo(), selectedItem -> 
					{
						if(aOil.getOilItemType() == EntityTypeEnum.OpenItem.getValue())
						{
							UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(BomInspectItemAnswerType.this.testProc.getUnitPk()));
							UnitQuery unitQuery = ProjectManager.getUnitQueryByPk(unit.getPk(), new ProjectOID(BomInspectItemAnswerType.this.testProc.getProjectPk()));

							OpenItemInfoBean openItemInfoBean = OILDelegate.getOpenItem(aOil.getOilItemPk());
							OpenItemEditForm openItemEditForm = new OpenItemEditForm(OpenItemEditForm.FormMode.DetailViewMode, unitQuery, new ProjectOID(unitQuery.getProjectPk(), null), null, openItemInfoBean, new OpenItemFormListener() {
								
								@Override
								public void openItemUpdated(OpenItemInfoBean openItem) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void openItemRemoved(int openItemPk) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void openItemAdded(OpenItemInfoBean openItem) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void formCancelled() {
									// TODO Auto-generated method stub
									
								}
							});
							
							openItemEditForm.addToView();
						}
						else if(aOil.getOilItemType() == EntityTypeEnum.NCR.getValue())
						{
							NcrItemQuery ncrItemQuery = NcrDelegate.getNcrItemQuery(EtestApplication.getInstance().getUserContext(), new NcrItemOID(aOil.getOilItemPk(), null));
							int ncrGroupPk = ncrItemQuery.getNcrGroupFk();
							NcrGroupBean ncrGroupBean = NcrDelegate.getNcrGroupBean(EtestApplication.getInstance().getUserContext(),new NcrGroupOID(ncrGroupPk, "") );

							NcrGroupForm ncrGroupForm = new NcrGroupForm(ncrGroupBean, EtestApplication.getInstance().getCurrentPanel(), new ObjectEditListener() {
								
								@Override
								public void commited(Object object) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void cancelled() {
									// TODO Auto-generated method stub
									
								}
							}, NcrGroupViewMode.VIEW);		
							
							EtestApplication.getInstance().showPanel(ncrGroupForm);
							
						}
					});
				}
			}

			final MenuItem seperator = p8Root.addSeparator();
			
			p8Root.addItem("Link to an existing open item", selectedItem -> 
			{
				formItemResponse = SurveyResponseDelegate.getFormItemResponse(sResponse.getResponseId(), BomInspectItemAnswerType.this.getSurveyItemId(), true);

				UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(BomInspectItemAnswerType.this.testProc.getUnitPk()));
				UnitQuery unitQuery = ProjectManager.getUnitQueryByPk(unit.getPk(), new ProjectOID(BomInspectItemAnswerType.this.testProc.getProjectPk()));
				
				OpenItemV2.StatusEnum[] openItemStatus = null;
				List<User> projectManagers = ProjectManager.getProjectManagers(unitQuery.getProjectOID());
				if(projectManagers.contains(EtestApplication.getInstance().getUserContext().getUser()))
				{
					openItemStatus = new OpenItemV2.StatusEnum[]{OpenItemV2.StatusEnum.Draft, OpenItemV2.StatusEnum.Open, OpenItemV2.StatusEnum.Completed};
				}
				else
				{
					openItemStatus = new OpenItemV2.StatusEnum[]{OpenItemV2.StatusEnum.Draft, OpenItemV2.StatusEnum.Open};
				}
				P8ListSelectorComponent p8ListSelector = new P8ListSelectorComponent(unitQuery.getProjectOID(), unitQuery, 
						new OpenItemTypeEnum[]{OpenItemTypeEnum.P8}, openItemStatus);

				
				WindowWithFooter p8SelectorWindow = new WindowWithFooter("Select open items");
				p8SelectorWindow.setWidth("90%");
				p8SelectorWindow.setHeight("80%");
				p8SelectorWindow.setWindowContent(p8ListSelector);
				
				Button cancelButton = new Button("Cancel", event -> EtestApplication.getInstance().removeWindow(p8SelectorWindow));
				p8SelectorWindow.addFooterButton(cancelButton);
				
				Button okButton = new Button("Ok", new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event)
					{
						if(p8ListSelector.getSelectedItems() == null || p8ListSelector.getSelectedItems().size() == 0)
						{
							EtestApplication.getInstance().showError("No open items selected");
							return;
						}

						try
						{
							OILDelegate.createTestItemOILTransferForP8(
									EtestApplication.getInstance().getUserContext(),
									formItemResponse,
									p8ListSelector.getSelectedItems().toArray(new Integer[p8ListSelector.getSelectedItems().size()]));
							EtestApplication.getInstance().showMessage("Open item(s) reference created successfully.");
							
							EtestApplication.getInstance().removeWindow(p8SelectorWindow);
							
							//reload the oilTransferlist and rebuild the menu
							oilItemList = OILDelegate.getTestProcItemFailureQuery(formItemResponse.getOID());
							buildMenu();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							EtestApplication.getInstance().removeWindow(p8SelectorWindow);

							EtestApplication.getInstance().showError("Error setting reference to open items. Please try later.");
							return;
						}
					}
				});
				p8SelectorWindow.addFooterButton(okButton);
				
				EtestApplication.getInstance().showWindow(p8SelectorWindow);
				
			});

			p8Root.addItem("Create new open item", selectedItem -> 
			{
				formItemResponse = SurveyResponseDelegate.getFormItemResponse(sResponse.getResponseId(), BomInspectItemAnswerType.this.getSurveyItemId(), true);

				UnitObj unit = ProjectManager.getUnitByPk(new UnitOID(BomInspectItemAnswerType.this.testProc.getUnitPk()));
				UnitQuery unitQuery = ProjectManager.getUnitQueryByPk(unit.getPk(), new ProjectOID(BomInspectItemAnswerType.this.testProc.getProjectPk()));
				OpenItemEditForm openItemEditForm = new OpenItemEditForm(OpenItemEditForm.FormMode.EditViewMode, unitQuery, new ProjectOID(unitQuery.getProjectPk(), null), null, new OpenItemInfoBean(), new OpenItemFormListener() {

					@Override
					public void openItemUpdated(OpenItemInfoBean openItem) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void openItemRemoved(int openItemPk) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void openItemAdded(OpenItemInfoBean openItem) 
					{
						//reload the oilTransferlist and rebuild the menu
						oilItemList = OILDelegate.getTestProcItemFailureQuery(formItemResponse.getOID());
						buildMenu();
					}
					
					@Override
					public void formCancelled() {
						// TODO Auto-generated method stub
						
					}
				});
				
				if(DisplayModeEnum.ViewMode == displayMode)
				{
					openItemEditForm.setAllowedOILTypes(new OpenItemTypeEnum[]{OpenItemTypeEnum.P8});
					
					TestProcItemFailureTransferContext itemFailureContext = new TestProcItemFailureTransferContext();
					itemFailureContext.setFormItemResponsePk(formItemResponse.getPk());
					itemFailureContext.setWorkstationPk(BomInspectItemAnswerType.this.testProc.getWorkstationPk());
					itemFailureContext.setTransferDescription(buildOpenItemTransferDescription(BomInspectItemAnswerType.this.testProc, (Section)BomInspectItemAnswerType.this.getParent().getParent(), passFailResult, answer, canswer));
					itemFailureContext.setQuestionAttachedImageName(getImageFileName());
					
					//TODO:: currently we are moving only the first response image to open item. We need to move all responses 
					if(responseImageFileNames != null && responseImageFileNames.size() > 0)
						itemFailureContext.setResponseImageName(responseImageFileNames.get(0));

					openItemEditForm.setTestProcFailureTransferContext(itemFailureContext);

					openItemEditForm.addToView();
				}
				else // this is the data entry mode for the Insection item
				{
					openItemEditForm.setAllowedOILTypes(new OpenItemTypeEnum[]{OpenItemTypeEnum.P8});

					
					String passFailResult = null;
					if(result != null && result.getValue() != null && result.getValue() != 0)
					{
						if(passVal == result.getValue())
							passFailResult = "Pass";
						else if(failVal == result.getValue())
							passFailResult = "Fail";
						else if(naVal == result.getValue())
							passFailResult = "Not Applicable";
					}
					
					String actualValAnser = null;
					if(actualValueTxt != null && actualValueTxt.getValue() != null)
						actualValAnser = (String) actualValueTxt.getValue();
					
					String testerComment = null;
					if(commentsTxt != null && commentsTxt.getValue() != null)
						testerComment = (String) commentsTxt.getValue();

					TestProcItemFailureTransferContext itemFailureContext = new TestProcItemFailureTransferContext();
					
					itemFailureContext.setFormItemResponsePk(formItemResponse.getPk());
					itemFailureContext.setWorkstationPk(BomInspectItemAnswerType.this.testProc.getWorkstationPk());
					itemFailureContext.setTransferDescription(buildOpenItemTransferDescription(BomInspectItemAnswerType.this.testProc, (Section)BomInspectItemAnswerType.this.getParent().getParent(), passFailResult, actualValAnser, testerComment));
					itemFailureContext.setQuestionAttachedImageName(getImageFileName());

					//TODO:: currently we are moving only the first response image to open item. We need to move all responses 
					if(responseImageFileNames != null && responseImageFileNames.size() > 0)
						itemFailureContext.setResponseImageName(responseImageFileNames.get(0));

					openItemEditForm.setTestProcFailureTransferContext(itemFailureContext);
					
					openItemEditForm.addToView();
				}
				
			});
			
		}
	}
	
}
