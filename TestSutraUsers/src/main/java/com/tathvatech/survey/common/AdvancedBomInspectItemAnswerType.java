/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.TimeZone;

import com.tathvatech.common.common.DataTypes;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.user.common.Option;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.exception.InvalidResponseException;
import com.tathvatech.common.utils.LineSeperatorUtil;
import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.user.utils.OptionList;
import com.tathvatech.forms.common.ExpectedNumericValue;
import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.forms.common.FormEventListner;
import com.tathvatech.forms.controller.TestProcController;
import com.tathvatech.forms.entity.FormItemResponse;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.logic.common.Logic;
import com.tathvatech.ncr.common.NcrItemQuery;
import com.tathvatech.ncr.oid.NcrItemOID;
import com.tathvatech.openitem.andon.entity.OpenItemV2;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.report.enums.ReportTypes;
import com.tathvatech.report.request.ReportRequest;

import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.enums.BomTypesEnum;
import com.tathvatech.survey.intf.LogicSubject;
import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.survey.service.SurveyItemManager;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.survey.service.SurveyResponseService;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.common.UnitObj;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.unit.response.ResponseUnit;
import com.tathvatech.unit.service.UnitService;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.AttachmentInfoBean;
import com.tathvatech.user.common.SecurityContext;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.service.PlanSecurityManager;
import org.aspectj.apache.bcel.classfile.Field;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AdvancedBomInspectItemAnswerType extends SurveySaveItem implements BaseInspectionLineItemAnswerType, SurveyDisplayItem, LogicSubject
{
	private static final Logger logger = LoggerFactory.getLogger(AdvancedBomInspectItemAnswerType.class);
	private final SurveyResponseService surveyResponseService;
	private final SurveyMaster surveyMaster;
	private final ProjectService projectService;
	private final UnitService unitService;
	private enum DisplayModeEnum {ViewMode, DataEntryMode}; // this is currently used only for the P8Transfer Menu.
	

	// if no maxlength is defined, set it to 1
	private static final int DEFAULT_MAXLENGTH = 5000;

	private int dataType = DataTypes.DATATYPE_STRING;

	public Integer commentFieldMaxLength = null;

	private String questionText;
	protected String questionTextDescription = "";
	protected OptionList customFields = new OptionList();

	// private int colCount;
	private boolean showPassFail;
	private boolean showNotApplicable;
	private boolean showComments;
	private boolean showExpectedValue;
	private boolean showActualValue = false; // if any actualValue field exists in this item it is set to true
	private boolean showUnit;
	private boolean showEquipmentSelector;
	private boolean makeAttachmentMandatory;
	

	public static int selectionKey = 2;
	public static int commentsKey = 3;
	public static int imageKey = 4;
	public static int equipmentSelectorKey = 5;

	public static int passVal = 11;
	public static int failVal = 12;
	public static int naVal = 13;
	
	ExpandableTextArea commentsTxt;
	SingleSelectCheckboxGroup result;
	HashMap tableFields = new HashMap<Integer, Field>();
	EquipmentSelector equipmentSelector = new EquipmentSelector();

	Label commentsLabel;
	
	Table table; // Table to which it is added as a row to. 
	
	String imageAttachCol = null; //column where image should be attached.

	UnitFormQuery testProc = null;
	List<String> responseImageFileNames = new ArrayList<String>(); // the name of the image that is attached as part of the response.
	String passFailResult = "";
	String canswer = "";
	HashMap tableAnswers = new HashMap<Integer, String>();	
	FormItemResponse formItemResponse;
	List<TestItemOILTransferQuery> oilItemList; // openItem associated with this item

	/**
     *
     */
	public AdvancedBomInspectItemAnswerType(SurveyResponseService surveyResponseService, SurveyMaster surveyMaster, ProjectService projectService, UnitService unitService)
	{
		super();

        this.surveyResponseService = surveyResponseService;
        this.surveyMaster = surveyMaster;
        this.projectService = projectService;
        this.unitService = unitService;
    }

	/**
	 * @param _survey
	 */
	public AdvancedBomInspectItemAnswerType(SurveyDefinition _survey, SurveyResponseService surveyResponseService, SurveyMaster surveyMaster, ProjectService projectService, UnitService unitService)
	{
		super(_survey);
        this.surveyResponseService = surveyResponseService;
        this.surveyMaster = surveyMaster;
        this.projectService = projectService;
        this.unitService = unitService;
    }

	public String getTypeName()
	{
		return "AdvancedBomInspectItemAnswerType";
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
		Option[] actualFieldOptions = getActualFieldOptions();
		Option[] addManFields = getAdditionalMandatoryFieldOptions();
		
		if(showPassFail || showComments || actualFieldOptions.length > 0 || addManFields.length > 0)
		{
			return true;
		}
		else
		{
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

	public boolean isShowEquipmentSelector()
	{
		return showEquipmentSelector;
	}

	public void setShowEquipmentSelector(boolean showEquipmentSelector)
	{
		this.showEquipmentSelector = showEquipmentSelector;
	}

	public boolean isMakeAttachmentMandatory()
	{
		return makeAttachmentMandatory;
	}

	public void setMakeAttachmentMandatory(boolean makeAttachmentMandatory)
	{
		this.makeAttachmentMandatory = makeAttachmentMandatory;
	}

	public boolean isShowPassFail()
	{
		return showPassFail;
	}

	public void setShowPassFail(boolean showPassFail)
	{
		this.showPassFail = showPassFail;
	}

	@Override
	public boolean isShowActualValue()
	{
		return showActualValue;
	}

	public void setShowActualValue(boolean showActualValue)
	{
		this.showActualValue = showActualValue;
	}

	public boolean isShowComments()
	{
		return showComments;
	}

	public void setShowComments(boolean showComments)
	{
		this.showComments = showComments;
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

	public Integer getCommentFieldMaxLength()
	{
		return commentFieldMaxLength;
	}

	public void setCommentFieldMaxLength(Integer commentFieldMaxLength)
	{
		this.commentFieldMaxLength = commentFieldMaxLength;
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
	
	public Section getEnclosingSection()
	{
		return (Section) this.getParent().getParent();
	}
	
	public OptionList getCustomFields()
	{
		return customFields;
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
				aUnit.setKey1(aOption.getValue());
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

			answer = (String[]) paramMap.get((getSurveyItemId() + "_" + equipmentSelectorKey));
			if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
			{
				String answerText = answer[0];

				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey2(equipmentSelectorKey);
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
		this.setShowUnit(new Boolean(element.getAttributeValue("showUnit")).booleanValue());
		this.setShowPassFail(new Boolean(element.getAttributeValue("showPassFail")).booleanValue());
		this.setShowNotApplicable(new Boolean(element.getAttributeValue("showNotApplicable")).booleanValue());
		this.setShowComments(new Boolean(element.getAttributeValue("showComments")).booleanValue());
		this.setShowEquipmentSelector(new Boolean(element.getAttributeValue("showEquipmentSelector")).booleanValue());
		this.setMakeAttachmentMandatory(new Boolean(element.getAttributeValue("makeAttachmentMandatory")).booleanValue());
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
				aOption.setValue(Integer.parseInt(fElement.getAttributeValue("value")));
				aOption.setType(BomTypesEnum.fromValueString(fElement.getAttributeValue("type")));
				if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.TextBox.getValue())
					showActualValue = true;
				cfList.add(aOption);
			}
			this.customFields = cfList;
		}
	}

	public void setConfiguration(List<List<String>> fileContents)
	{
		//not required for now.. set from the BomGroup
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
		element.setAttribute("showUnit", new Boolean(showUnit).toString());
		element.setAttribute("showPassFail", new Boolean(showPassFail).toString());
		element.setAttribute("showNotApplicable", new Boolean(showNotApplicable).toString());
		element.setAttribute("showComments", new Boolean(showComments).toString());
		element.setAttribute("showEquipmentSelector", new Boolean(showEquipmentSelector).toString());
		element.setAttribute("makeAttachmentMandatory", new Boolean(makeAttachmentMandatory).toString());
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
				optionElement.setAttribute("value", ""+aOption.getValue());
				optionElement.setAttribute("type", (aOption.getType() != null)?""+aOption.getType().getValue():"");
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


	public Element writeResponseXML(SurveyItemResponse itemResponse) throws Exception
	{
		Element elem = new Element("itemResponse");
		elem.setAttribute("questionId", this.surveyItemId);

		for (int r = 0; r < itemResponse.getResponseUnits().size(); r++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(r);

			Element uElem = new Element("u");
			uElem.setAttribute("k1", new Integer(aUnit.getKey1()).toString());
			
//			if(aUnit.getKey2() != 0)
//			{
				uElem.setAttribute("k2", new Integer(aUnit.getKey2()).toString());
//			}
//			if(aUnit.getKey3() != 0)
//			{
				uElem.setAttribute("k3", new Integer(aUnit.getKey3()).toString());
//			}
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

	public void addItemToDesignViewInt(final Table groupTable, boolean isPreviewMode, String addAfterItemId, final FormDesignListener formDesignListener)
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
		try {
			String expectedValHeading = null;
			String expectedVal = null;
			String unitVal = null;
			for (int j = 0; j < this.getCustomFields().size(); j++)
			{
				Option aOption = (Option)customFields.getOptionByIndex(j);
				String headingText = aOption.getText();
				if (AdvancedBomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(headingText))
				{
					expectedValHeading = headingText;
					expectedVal = aOption.getTextDesc();
				}
				else if (AdvancedBomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(headingText))
				{
						unitVal = aOption.getTextDesc();
				}
				else if (AdvancedBomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(headingText))
				{
					if(showPassFail)
					{
						SingleSelectCheckboxGroup o1 = new SingleSelectCheckboxGroup();
						o1.addItem(AdvancedBomInspectItemAnswerType.passVal);
						o1.setItemCaption(AdvancedBomInspectItemAnswerType.passVal, "Pass");

						o1.addItem(AdvancedBomInspectItemAnswerType.failVal);
						o1.setItemCaption(AdvancedBomInspectItemAnswerType.failVal, "Fail");
						if(this.isShowNotApplicable())
						{
							o1.addItem(AdvancedBomInspectItemAnswerType.naVal);
							o1.setItemCaption(AdvancedBomInspectItemAnswerType.naVal, "Not Applicable");
						}
						table.getContainerProperty(id, headingText).setValue(o1);
					}
					else
					{
						if(table.getContainerProperty(id, headingText) != null) // was causing a nullpointer .. dont know why
							table.getContainerProperty(id, headingText).setValue(null);
					}
				}
				else if (AdvancedBomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
				{
				}
				else if (AdvancedBomInspectItemGroupAnswerType.HEADING_DATATYPE.equalsIgnoreCase(headingText))
				{
				}
				else if (AdvancedBomInspectItemGroupAnswerType.HEADING_COMMENTS.equalsIgnoreCase(headingText))
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
						if(table.getContainerProperty(id, headingText) != null)
							table.getContainerProperty(id, headingText).setValue(null);
					}
				}
				else
				{
					BomTypesEnum cType = aOption.getType();
					if(cType != null && BomTypesEnum.TextBox.getValue() == cType.getValue())
					{
						TextField txt = new TextField();
						txt.setColumns(5);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else if(cType != null && BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
					{
						TextField txt = new TextField();
						txt.setColumns(5);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else if(cType != null 
							&& (BomTypesEnum.RadioGroup.getValue() == cType.getValue() || BomTypesEnum.RadioGroupMandatory.getValue() == cType.getValue()))
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						if(choices.length < 5)
						{
							SingleSelectCheckboxGroup o1 = new SingleSelectCheckboxGroup();
							for (int i = 0; i < choices.length; i++)
							{
								o1.addItem(i);
								o1.setItemCaption(i, choices[i]);
							}
							VerticalLayout l = new VerticalLayout();
							l.addComponent(o1);
							table.getContainerProperty(id, headingText).setValue(l);
						}
						else
						{
							ComboBox o1 = new ComboBox();
							for (int i = 0; i < choices.length; i++)
							{
								o1.addItem(i);
								o1.setItemCaption(i, choices[i]);
							}
							VerticalLayout l = new VerticalLayout();
							l.addComponent(o1);
							table.getContainerProperty(id, headingText).setValue(l);
						}
					}
					else if(cType != null 
							&& (BomTypesEnum.CheckboxGroup.getValue() == cType.getValue() || BomTypesEnum.CheckboxGroupMandatory.getValue() == cType.getValue()))
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						OptionGroup o1 = new OptionGroup();
						o1.setMultiSelect(true);
						for (int i = 0; i < choices.length; i++)
						{
							o1.addItem(i);
							o1.setItemCaption(i, choices[i]);
						}
						VerticalLayout l = new VerticalLayout();
						l.addComponent(o1);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
					{
						TextArea txt = new TextArea();
						txt.setColumns(20);
						txt.setRows(3);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else if(cType != null 
							&& (BomTypesEnum.Date.getValue() == cType.getValue() || BomTypesEnum.DateMandatory.getValue() == cType.getValue()))
					{
						DateField txt = new DateField();
						txt.setDateFormat("dd/MM/yyyy");
						txt.setResolution(Resolution.DAY);
						txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else if(cType != null 
							&& (BomTypesEnum.DateTime.getValue() == cType.getValue() || BomTypesEnum.DateTimeMandatory.getValue() == cType.getValue()))
					{
						DateField txt = new DateField();
						txt.setDateFormat("dd/MM/yyyy HH:mm");
						txt.setResolution(Resolution.MINUTE);
						txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(id, headingText).setValue(l);
					}
					else
					{
						VerticalLayout l;
						if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() != 0)
						{
							l = getMultiLine(aOption.getTextDesc());
						}
						else
						{
							l = new VerticalLayout();
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

								l.addComponent(new VSpacer(10));
								l.addComponent(img);
							}
							catch (Exception e)
							{
								logger.error("Cannot read file - " + imageFileName, e);
							}
						}
					}
					if(table.getContainerProperty(id, expectedValHeading) != null)
					{
						table.getContainerProperty(id, expectedValHeading).setValue(l);
					}
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
							VerticalLayout v = new VerticalLayout();
							win.setWidth("800px");
							win.setHeight("600px");
							v.addComponent(drawConfigurationView(formDesignListener));
							win.setContent(v);
							UI.getCurrent().addWindow(win);
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
							try
							{
								Component parent = table.getParent();
								while(parent!= null)
								{
									if(parent instanceof SurveyItemConfigHolder && 
											((SurveyItemConfigHolder)parent).getSurveyItem() instanceof BaseInspectionItemGroupAnswerType)
									{
										
										((SurveyItemConfigHolder)parent).getFormDesigner().addNewItemToForm(
												SurveyItemManager.AdvancedBomInspectItemAnswerType.getDescription(),
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
		catch (Exception e) 
		{
			logger.error("Exception from AdvancedInspectionAnwerType.drawDesignViewInt on id" + id, e);
			e.printStackTrace();
		}
	}

	@Override
	public Component drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse, Component parent, String[] flags, FormEventListner formEventListner)
	{
		return null;
	}

	protected void drawResponseFieldInt(UnitFormQuery testProc, final SurveyResponse sResponse, 
			TSTableContainer table, int index, String[] flags,  final FormEventListner formEventListner)
	{
		try
		{
			this.testProc = testProc;
			final UserContext userContext = ((EtestApplication)UI.getCurrent()).getUserContext();
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
			
			int selectedValue = 0;
			String canswer = "";
			HashMap tableAnswers = new HashMap<Integer, String>();
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
					}
					else if (this.imageKey == aUnit.getKey2() && aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
					{
						responseImageFileNames.add(aUnit.getKey4().trim());
					}
					else if (this.equipmentSelectorKey == aUnit.getKey2())
					{
						String eqPk = aUnit.getKey4();
						try
						{
							if(eqPk != null && eqPk.trim().length() > 0)
							{
								try
								{
									EquipmentListFilter filter = new EquipmentListFilter();
									filter.setEquipmentOID(new EquipmentOID(Integer.parseInt(eqPk)));
									List<EquipmentListReportRow> eQList = (List<EquipmentListReportRow>) new EquipmentListReport(EtestApplication.getInstance().getUserContext(), 
											ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.EquipmentList, filter)).runReport().getReportData();
									if(eQList != null && eQList.size() > 0)
										equipmentSelector.setValue(eQList.get(0));
								}
								catch (Exception e)
								{
									logger.error("Invalid equipment attached to checksheet: " + eqPk);
								}
							}
						}
						catch (Exception e)
						{
							logger.error("Invalid equipment attached to checksheet: " + eqPk);
						}
					}
					
					//now the additional table fields
					else
					{
						int ind = aUnit.getKey1();
						Option aOption = (Option) this.getCustomFields().getOptionByValue(ind);
						if(aOption.getType() != null 
								&& (aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue()))
						{
							String tiAnswer = ""+aUnit.getKey3();
							tableAnswers.put(ind, tiAnswer);
						}
						else if(aOption.getType() != null 
								&& (aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue()))
						{
							String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
							tableAnswers.put(ind, tiAnswer);
						}
						else if(aOption.getType() != null && 
								(aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.TextBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.Date.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateTime.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue())
								)
						{
							String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
							tableAnswers.put(ind, tiAnswer);
						}
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
				Option aOption = (Option) customFields.getOptionByIndex(j);
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
				else if (BomInspectItemGroupAnswerType.HEADING_RESULT.equalsIgnoreCase(headingText))
				{
					if(showPassFail)
					{
						this.result = new SingleSelectCheckboxGroup();
//						result.addListener(new BlurListener() {
	//	
//							@Override
//							public void blur(BlurEvent event)
//							{
//								formEventListner.fillDataEvent(BomInspectItemAnswerType.this, validate());
//							}
//						});
						result.addItem(this.passVal);
						result.setItemCaption(passVal, "Pass");
		
						result.addItem(this.failVal);
						result.setItemCaption(failVal, "Fail");
		
						if(showNotApplicable)
						{
							result.addItem(this.naVal);
							result.setItemCaption(naVal, "Not Applicable");
						}
						
						try
						{
							table.getContainerProperty(tableRowId, headingText).setValue(result);
						}
						catch (Exception e)
						{
							logger.error(String.format("Error adding item to column:%s", headingText));
							throw e;
						}
		
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
								formEventListner.fillDataEvent(AdvancedBomInspectItemAnswerType.this, validate());
							}
						});
						commentsTxt.setValue(canswer);
						try
						{
							table.getContainerProperty(tableRowId, headingText).setValue(commentsTxt);
						}
						catch (Exception e)
						{
							logger.error(String.format("Error adding item to table with heading %s", headingText));
							throw e;
						}
					}
				}
				else
				{
					BomTypesEnum cType = aOption.getType();
					if(cType != null && BomTypesEnum.TextBox.getValue() == cType.getValue())
					{
						TextField txt = new TextField();
						txt.setColumns(5);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), txt);
						txt.addListener(new BlurListener() {
							
							@Override
							public void blur(BlurEvent event)
							{
								formEventListner.fillDataEvent(AdvancedBomInspectItemAnswerType.this, validate());
								
								if(dataType != DataTypes.DATATYPE_INTEGER || result == null || tableFields.size() == 0)
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
									boolean allAreEmpty = true;
									for (Iterator iterator = getActualFields().iterator(); iterator.hasNext();)
									{
										Field aField = (Field) iterator.next();
										if(!(aField.getValue() == null || ((String)aField.getValue()).trim().length() == 0))
										{
											allAreEmpty = false;
										}
									}
									if(allAreEmpty)
									{
										result.setEnabled(naVal, true);
									}
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
									boolean allGood = true;
									List actualFields = getActualFields();
									for (Iterator iterator = actualFields.iterator(); iterator.hasNext();)
									{
										Field aField = (Field) iterator.next();
										double actVal = Double.parseDouble(""+aField.getValue());
										if(expUL.isValueInRange(actVal))
										{
											//with in the range
										}
										else
										{
											allGood = false;
										}
									}
									if(allGood)
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
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							txt.setValue((String) tableAnswers.get(aOption.getValue()));
						}

						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							txt.setEnabled(false);
						}
					}
					else if(cType != null 
							&& (BomTypesEnum.RadioGroup.getValue() == cType.getValue() || BomTypesEnum.RadioGroupMandatory.getValue() == cType.getValue()))
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						if(choices.length < 5)
						{
							SingleSelectCheckboxGroup o1 = new SingleSelectCheckboxGroup();
							for (int i = 0; i < choices.length; i++)
							{
								o1.addItem(i);
								o1.setItemCaption(i, choices[i]);
							}
							VerticalLayout l = new VerticalLayout();
							l.addComponent(o1);
							table.getContainerProperty(tableRowId, headingText).setValue(l);
							tableFields.put(aOption.getValue(), o1);
							
							if(tableAnswers.get(aOption.getValue()) != null)
							{
								o1.setValue(Integer.parseInt((String)tableAnswers.get(aOption.getValue())));
							}
							//when the verifier or approver edits the response, he need to edit only the comments so disable all others
							if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
							{
								o1.setEnabled(false);
							}
						}
						else
						{
							ComboBox o1 = new ComboBox();
							for (int i = 0; i < choices.length; i++)
							{
								o1.addItem(i);
								o1.setItemCaption(i, choices[i]);
							}
							VerticalLayout l = new VerticalLayout();
							l.addComponent(o1);
							table.getContainerProperty(tableRowId, headingText).setValue(l);
							tableFields.put(aOption.getValue(), o1);
							
							if(tableAnswers.get(aOption.getValue()) != null)
							{
								o1.setValue(Integer.parseInt((String)tableAnswers.get(aOption.getValue())));
							}
							//when the verifier or approver edits the response, he need to edit only the comments so disable all others
							if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
							{
								o1.setEnabled(false);
							}
						}
					}
					else if(cType != null 
							&& (BomTypesEnum.CheckboxGroup.getValue() == cType.getValue() || BomTypesEnum.CheckboxGroupMandatory.getValue() == cType.getValue()))
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						OptionGroup o1 = new OptionGroup();
						o1.setMultiSelect(true);
						for (int i = 0; i < choices.length; i++)
						{
							o1.addItem(i);
							o1.setItemCaption(i, choices[i]);
						}
						VerticalLayout l = new VerticalLayout();
						l.addComponent(o1);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), o1);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							String answerList = (String)tableAnswers.get(aOption.getValue());

							VerticalLayout v = new VerticalLayout();

							String[] answers = answerList.split(",");
							List<Integer> values = new ArrayList<>();
							for (int i = 0; i < answers.length; i++)
							{
								int ch = Integer.parseInt(answers[i].trim());
								values.add(ch);
							}
							if(values.size() == 0)
							{
								
							}
							else if(values.size() == 1)
							{
								o1.setValue(values.get(0));
							}
							else
							{
								o1.setValue(values);
							}
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							o1.setEnabled(false);
						}
					}
					else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
					{
						ExpandableTextArea txt = new ExpandableTextArea();
						txt.setColumns(20);
						txt.setRows(3);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), txt);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							txt.setValue(tableAnswers.get(aOption.getValue()));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							txt.setEnabled(false);
						}
					}
					else if(cType != null 
							&& (BomTypesEnum.Date.getValue() == cType.getValue() || BomTypesEnum.DateMandatory.getValue() == cType.getValue()))
					{
						DateField txt = new DateField();
						txt.setDateFormat("dd/MM/yyyy");
						txt.setResolution(Resolution.DAY);
						txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());

						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), txt);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							txt.setValue(new Date(Long.parseLong((String) tableAnswers.get(aOption.getValue()))));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							txt.setEnabled(false);
						}
					}
					else if(cType != null && 
							(BomTypesEnum.DateTime.getValue() == cType.getValue() || BomTypesEnum.DateTimeMandatory.getValue() == cType.getValue()))
					{
						DateField txt = new DateField();
						txt.setDateFormat("dd/MM/yyyy HH:mm");
						txt.setResolution(Resolution.MINUTE);
						txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());

						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), txt);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							txt.setValue(new Date(Long.parseLong((String) tableAnswers.get(aOption.getValue()))));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							txt.setEnabled(false);
						}
					}
					else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
					{
						ExpandableTextArea txt = new ExpandableTextArea();
						txt.setColumns(20);
						txt.setRows(3);
						VerticalLayout l = new VerticalLayout();
						l.addComponent(txt);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), txt);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							txt.setValue(tableAnswers.get(aOption.getValue()));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							txt.setEnabled(false);
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
			}
			if(dataType == DataTypes.DATATYPE_INTEGER && result != null && getActualFields().size() > 0 && showExpectedValue && expectedVal != null && expectedVal.trim().length() > 0)
			{
				ExpectedNumericValue expUL = getExpectedUpperLower();
				if(expUL != null)
				{
					result.setEnabled(this.passVal, false);
					result.setEnabled(this.failVal, false);
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
			
			if(showEquipmentSelector)
			{
//				if(equipmentSelectedPk != null)
//				{
//					if(eqBean != null)
//					{
//						DateFormatter dateFormatter = DateFormatter.getInstance(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));
//						StringBuilder sb = new StringBuilder();
//						sb.append("Equipment S/N: " + eqBean.getSerialNo());
//						sb.append("\n Calibration Reference No: " + ListStringUtil.showString(eqBean.getCalibrationBean().getCalibrationReferenceNo()));
//						sb.append("\n Expiry date: "); 
//						if(eqBean.getCalibrationBean().getNextCalibrationDate() != null)
//							sb.append(dateFormatter.formatDate(eqBean.getCalibrationBean().getNextCalibrationDate()));
//
//						equipButton.setDescription(sb.toString());
//					}
//				}
				
				attach.addComponent(equipmentSelector);
			}
			
			Option[] fields = getResponseFieldOptions();
			if(this.showPassFail || fields.length > 0 || showComments)
			{
				if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS, new SecurityContext(null, null, null, null, null)))
				{
					final Button detailButton = new Button("", new ClickListener() {
		
						@Override
						public void buttonClick(ClickEvent event)
						{
							try
							{
								EtestApplication.getInstance().showWindow( 
								new InspectionItemAnswerViewerReport(userContext, sResponse.getTestProcPk(), sResponse.getSurveyDefinition(), 
										sResponse.getSurveyPk(), AdvancedBomInspectItemAnswerType.this));
							}
							catch (Exception e)
							{
								e.printStackTrace();
								((EtestApplication)UI.getCurrent()).showError("Could not open details for units, Please try again later");
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
		
			if(sResponse != null)
			{

				formItemResponse = surveyResponseService.getFormItemResponse(sResponse.getResponseId(), AdvancedBomInspectItemAnswerType.this.getSurveyItemId(), true);
				oilItemList = OILDelegate.getTestProcItemFailureQuery(formItemResponse.getOID());
				
	// no need for comments during dataentry			
//				CommentsThreadPopup commentsB = new CommentsThreadPopup(EntityTypeEnum.FormItemResponse, formItemResponse.getPk(), FormItemResponse.COMMENT_CONTEXT_NOT_TO_PRINT, CommentsThreadPopup.ICON_STYLE_SMALL);
//				attach.addComponent(commentsB);
				
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
		catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				Survey s = surveyMaster.getSurveyByPk(testProc.getFormPk());
				logger.error(String.format("Error displaying AdvancedBomInspectionAnswerType, Id:%s in surveyFile:%s ", this.getSurveyItemId(), s.getDefFileName()));
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	
	/**
	 * This is used by OpenItemType after the change in AdvancedBom to use TSTable.. for now writing it here.
	 * @param
	 * @param sResponse
	 * @param table
	 * @param index
	 * @param flags
	 * @param formEventListner
	 */
	public void drawResponseFieldInt(UnitFormQuery testProc, final SurveyResponse sResponse, 
			Table table, int index, String[] flags,  final FormEventListner formEventListner)
	{
		this.testProc = testProc;
		
		final UserContext userContext = ((EtestApplication)UI.getCurrent()).getUserContext();
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
		
		int selectedValue = 0;
		String canswer = "";
		HashMap tableAnswers = new HashMap<Integer, String>();
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
				}
				else if (this.imageKey == aUnit.getKey2())
				{
					responseImageFileNames.add(aUnit.getKey4());
				}
				
				//now the additional table fields
				else
				{
					int ind = aUnit.getKey1();
					Option aOption = (Option) this.getCustomFields().getOptionByValue(ind);
					if(aOption.getType() != null 
							&& (aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue()))
					{
						String tiAnswer = ""+aUnit.getKey3();
						tableAnswers.put(ind, tiAnswer);
					}
					if(aOption.getType() != null 
							&& (aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue()))
					{
						String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
						tableAnswers.put(ind, tiAnswer);
					}
					else if(aOption.getType() != null && 
							(aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.TextBox.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.Date.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.DateTime.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue() ||
							aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue())
							)
					{
						String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
						tableAnswers.put(ind, tiAnswer);
					}
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
			Option aOption = (Option) customFields.getOptionByIndex(j);
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
							formEventListner.fillDataEvent(AdvancedBomInspectItemAnswerType.this, validate());
						}
					});
					commentsTxt.setValue(canswer);
					table.getContainerProperty(tableRowId, headingText).setValue(commentsTxt);
				}
			}
			else
			{
				BomTypesEnum cType = aOption.getType();
				if(cType != null && BomTypesEnum.TextBox.getValue() == cType.getValue())
				{
					TextField txt = new TextField();
					txt.setColumns(5);
					VerticalLayout l = new VerticalLayout();
					l.addComponent(txt);
					table.getContainerProperty(tableRowId, headingText).setValue(l);
					tableFields.put(aOption.getValue(), txt);
					txt.addListener(new BlurListener() {
						
						@Override
						public void blur(BlurEvent event)
						{
							formEventListner.fillDataEvent(AdvancedBomInspectItemAnswerType.this, validate());
							
							if(dataType != DataTypes.DATATYPE_INTEGER || result == null || tableFields.size() == 0)
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
								boolean allAreEmpty = true;
								for (Iterator iterator = getActualFields().iterator(); iterator.hasNext();)
								{
									Field aField = (Field) iterator.next();
									if(!(aField.getValue() == null || ((String)aField.getValue()).trim().length() == 0))
									{
										allAreEmpty = false;
									}
								}
								if(allAreEmpty)
								{
									result.setEnabled(naVal, true);
								}
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
								boolean allGood = true;
								List actualFields = getActualFields();
								for (Iterator iterator = actualFields.iterator(); iterator.hasNext();)
								{
									Field aField = (Field) iterator.next();
									double actVal = Double.parseDouble(""+aField.getValue());
									if(expUL.isValueInRange(actVal))
									{
										//with in the range
									}
									else
									{
										allGood = false;
									}
								}
								if(allGood)
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
					
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						txt.setValue((String) tableAnswers.get(aOption.getValue()));
					}

					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						txt.setEnabled(false);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.RadioGroup.getValue() == cType.getValue() || BomTypesEnum.RadioGroupMandatory.getValue() == cType.getValue()))
				{
					String[] choices = findRadioOptions(aOption.getTextDesc());
					if(choices.length < 5)
					{
						SingleSelectCheckboxGroup o1 = new SingleSelectCheckboxGroup();
						for (int i = 0; i < choices.length; i++)
						{
							o1.addItem(i);
							o1.setItemCaption(i, choices[i]);
						}
						VerticalLayout l = new VerticalLayout();
						l.addComponent(o1);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), o1);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							o1.setValue(Integer.parseInt((String)tableAnswers.get(aOption.getValue())));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							o1.setEnabled(false);
						}
					}
					else
					{
						ComboBox o1 = new ComboBox();
						for (int i = 0; i < choices.length; i++)
						{
							o1.addItem(i);
							o1.setItemCaption(i, choices[i]);
						}
						VerticalLayout l = new VerticalLayout();
						l.addComponent(o1);
						table.getContainerProperty(tableRowId, headingText).setValue(l);
						tableFields.put(aOption.getValue(), o1);
						
						if(tableAnswers.get(aOption.getValue()) != null)
						{
							o1.setValue(Integer.parseInt((String)tableAnswers.get(aOption.getValue())));
						}
						//when the verifier or approver edits the response, he need to edit only the comments so disable all others
						if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
						{
							o1.setEnabled(false);
						}
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.CheckboxGroup.getValue() == cType.getValue() || BomTypesEnum.CheckboxGroupMandatory.getValue() == cType.getValue()))
				{
					String[] choices = findRadioOptions(aOption.getTextDesc());
					OptionGroup o1 = new OptionGroup();
					o1.setMultiSelect(true);
					for (int i = 0; i < choices.length; i++)
					{
						o1.addItem(i);
						o1.setItemCaption(i, choices[i]);
					}
					VerticalLayout l = new VerticalLayout();
					l.addComponent(o1);
					table.getContainerProperty(tableRowId, headingText).setValue(l);
					tableFields.put(aOption.getValue(), o1);
					
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						o1.setValue(Integer.parseInt((String)tableAnswers.get(aOption.getValue())));
					}
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						o1.setEnabled(false);
					}
				}
				else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
				{
					ExpandableTextArea txt = new ExpandableTextArea();
					txt.setColumns(20);
					txt.setRows(3);
					VerticalLayout l = new VerticalLayout();
					l.addComponent(txt);
					table.getContainerProperty(tableRowId, headingText).setValue(l);
					tableFields.put(aOption.getValue(), txt);
					
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						txt.setValue(tableAnswers.get(aOption.getValue()));
					}
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						txt.setEnabled(false);
					}
				}
				else if(cType != null
						&& (BomTypesEnum.Date.getValue() == cType.getValue() || BomTypesEnum.DateMandatory.getValue() == cType.getValue()))
				{
					DateField txt = new DateField();
					txt.setDateFormat("dd/MM/yyyy");
					txt.setResolution(Resolution.DAY);
					txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());

					VerticalLayout l = new VerticalLayout();
					l.addComponent(txt);
					table.getContainerProperty(tableRowId, headingText).setValue(l);
					tableFields.put(aOption.getValue(), txt);
					
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						txt.setValue(new Date(Long.parseLong((String) tableAnswers.get(aOption.getValue()))));
					}
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						txt.setEnabled(false);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.DateTime.getValue() == cType.getValue() || BomTypesEnum.DateTimeMandatory.getValue() == cType.getValue()))
				{
					DateField txt = new DateField();
					txt.setDateFormat("dd/MM/YYYY HH:mm");
					txt.setResolution(Resolution.MINUTE);
					txt.setTimeZone(EtestApplication.getInstance().getUserContext().getTimeZone());

					VerticalLayout l = new VerticalLayout();
					l.addComponent(txt);
					table.getContainerProperty(tableRowId, headingText).setValue(l);
					tableFields.put(aOption.getValue(), txt);
					
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						txt.setValue(new Date(Long.parseLong((String) tableAnswers.get(aOption.getValue()))));
					}
					//when the verifier or approver edits the response, he need to edit only the comments so disable all others
					if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
					{
						txt.setEnabled(false);
					}
				}
				else
				{
					VerticalLayout l; 
					if(aOption.getTextDesc() != null && aOption.getTextDesc().trim().length() > 0)
					{
						l = getMultiLine(aOption.getTextDesc());
					}
					else
					{
						l = new VerticalLayout();
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
					table.getContainerProperty(tableRowId, headingText).setValue(l);
				}
			}
		}
		if(dataType == DataTypes.DATATYPE_INTEGER && result != null && getActualFields().size() > 0 && showExpectedValue && expectedVal != null && expectedVal.trim().length() > 0)
		{
			ExpectedNumericValue expUL = getExpectedUpperLower();
			if(expUL != null)
			{
				result.setEnabled(this.passVal, false);
				result.setEnabled(this.failVal, false);
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

							l.addComponent(new VSpacer(10));
							l.addComponent(img);
						}
						catch (Exception e)
						{
							logger.error("Cannot read file - " + imageFileName, e);
						}
					}
				}
				if(table.getContainerProperty(tableRowId, expectedValHeading) != null)
				{
					table.getContainerProperty(tableRowId, expectedValHeading).setValue(l);
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
		
		Option[] fields = getResponseFieldOptions();
		if(this.showPassFail || fields.length > 0 || showComments)
		{
			if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS, new SecurityContext(null, null, null, null, null)))
			{
				final Button detailButton = new Button("", new ClickListener() {
	
					@Override
					public void buttonClick(ClickEvent event)
					{
						try
						{
							EtestApplication.getInstance().showWindow( 
							new InspectionItemAnswerViewerReport(userContext, sResponse.getTestProcPk(), sResponse.getSurveyDefinition(), 
									sResponse.getSurveyPk(), AdvancedBomInspectItemAnswerType.this));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							((EtestApplication)UI.getCurrent()).showError("Could not open details for units, Please try again later");
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

		//table.addItem(cols, index);
	}
	
	public VerticalLayout getMultiLine(String data) {
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

	public String[] findRadioOptions(String data) 
	{
		List<String> list = new ArrayList();

		if (data == null || data.trim().length() == 0)
		{
			return null;
		}
		String[] ary = data.split("\n");
		for (int j = 0; j < ary.length; j++)
		{
			String s = ary[j];
			if(s == null || s.trim().length() == 0)
				continue;
			if(
				s.equalsIgnoreCase(BomTypesEnum.RadioGroup.getToken()) 
				|| s.equalsIgnoreCase(BomTypesEnum.RadioGroupMandatory.getToken())
				|| s.equalsIgnoreCase(BomTypesEnum.CheckboxGroup.getToken())
				|| s.equalsIgnoreCase(BomTypesEnum.CheckboxGroupMandatory.getToken())
					)
				continue;
			list.add(s);
		}
		return list.toArray(new String[list.size()]);
	}


	@Override
	public SurveyItemResponse captureResponse() throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();

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
		
		if(showEquipmentSelector == true && equipmentSelector.getValue() != null)
		{
			ResponseUnit sUnit = new ResponseUnit();
			sUnit.setKey2(equipmentSelectorKey);
			sUnit.setKey4(""+((EquipmentListReportRow)equipmentSelector.getValue()).getPk());
			itemResponse.addResponseUnit(sUnit);
		}

		for (Iterator iterator = tableFields.keySet().iterator(); iterator.hasNext();)
		{
			Integer aKey = (Integer) iterator.next();
			Component comp = (Component) tableFields.get(aKey);
			Option aOption = (Option) customFields.getOptionByValue(aKey.intValue());
			if(aOption.getType()!= null 
					&& (aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue()))
			{
				Object tfSelection;
				if(comp instanceof SingleSelectCheckboxGroup)
				{
					tfSelection = ((SingleSelectCheckboxGroup)comp).getValue();
				}
				else
				{
					tfSelection = ((ComboBox)comp).getValue();
				}
				if (tfSelection != null)
				{
					ResponseUnit sUnit = new ResponseUnit();
					sUnit.setKey1(aKey.intValue());
					sUnit.setKey3(((Integer)tfSelection).intValue());
					itemResponse.addResponseUnit(sUnit);
				}
			}
			if(aOption.getType()!= null 
					&& (aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue()))
			{
				Object tfSelection = ((OptionGroup)comp).getValue();
				if (tfSelection != null)
				{
					if(tfSelection instanceof Integer) // single checkbox was selected
					{
						ResponseUnit sUnit = new ResponseUnit();
						sUnit.setKey1(aKey.intValue());
						sUnit.setKey4(((Integer)tfSelection).toString());
						itemResponse.addResponseUnit(sUnit);
					}
					else // multiple checkboxes are selected
					{
						Collection items = (Collection)tfSelection;
						StringBuilder sb = new StringBuilder();
						String sep = "";
						for (Iterator iterator2 = items.iterator(); iterator2.hasNext();)
						{
							Integer object = (Integer) iterator2.next();
							sb.append(sep).append(object.toString());
							sep = ",";
						}
						if(sb.length() > 0)
						{
							ResponseUnit sUnit = new ResponseUnit();
							sUnit.setKey1(aKey.intValue());
							sUnit.setKey4(sb.toString());
							itemResponse.addResponseUnit(sUnit);
						}
					}
					
					
				}
			}
			else if(aOption.getType()!= null && aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue())
			{
				Object tA = ((ExpandableTextArea)comp).getValue();
				if(tA != null && ((String)tA).trim().length() > 0)
				{
					ResponseUnit sUnit = new ResponseUnit();
					sUnit.setKey1(aKey.intValue());
					sUnit.setKey4(((String)tA).trim());
					itemResponse.addResponseUnit(sUnit);
				}
			}
			else if(aOption.getType()!= null && 
						(aOption.getType().getValue() == BomTypesEnum.Date.getValue()
						|| aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue()
						|| aOption.getType().getValue() == BomTypesEnum.DateTime.getValue()
						|| aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue()
						)
					)
			{
				Date tA = ((DateField)comp).getValue();
				if(tA != null)
				{
					ResponseUnit sUnit = new ResponseUnit();
					sUnit.setKey1(aKey.intValue());
					sUnit.setKey4(tA.getTime() + "");
					itemResponse.addResponseUnit(sUnit);
				}
			}
			else if(aOption.getType()!= null &&
						(aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
						aOption.getType().getValue() == BomTypesEnum.TextBox.getValue()
						)
					)
			{
				Object tA = ((Field)comp).getValue();
				if(tA != null && ((String)tA).trim().length() > 0)
				{
					ResponseUnit sUnit = new ResponseUnit();
					sUnit.setKey1(aKey.intValue());
					sUnit.setKey4(((String)tA).trim());
					itemResponse.addResponseUnit(sUnit);
				}
			}
		}
			
		return itemResponse;
	}
	
	public List<String> validateResponse(SurveyItemResponse itemResponse)
	{
		List<String> errors = new ArrayList<String>();
		Option[] actualFields = getActualFieldOptions();
		Option[] mandatoryFields = getAdditionalMandatoryFieldOptions();
		if(showPassFail || showComments || actualFields.length > 0 || mandatoryFields.length > 0)
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
		
		InspectionLineItemAnswerStatus answerStatus = getAnswerStatus(itemResponse);
		
		if(showPassFail)
		{
			if(answerStatus.isPassFailAnswered() == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Pass, Fail or Not applicable should be selected.");
			}
		}
		else if(showActualValue)
		{
			if(answerStatus.isActualAnswered() == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - All actual value fields should be entered.");
			}
		}
		else if(showComments)
		{
			if(answerStatus.isCommentAnswered() == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Comment should be entered.");
			}
			else
			{
				if(commentFieldMaxLength != null)
				{
					if(answerStatus.getCommentAnswer().length() > commentFieldMaxLength)
					{
						Section sec = (Section) this.getParent().getParent();
						errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Comment too long.");
					}
				}
			}
		}
		
		else if(showEquipmentSelector)
		{
			if(equipmentSelector.getValue() == null)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Equipment should be selected.");
			}
			else
			{
				if(commentFieldMaxLength != null)
				{
					if(answerStatus.getCommentAnswer().length() > commentFieldMaxLength)
					{
						Section sec = (Section) this.getParent().getParent();
						errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Comment too long.");
					}
				}
			}
		}

		if(makeAttachmentMandatory)
		{
			if(answerStatus.isAttachmentAdded() == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Attachment is mandatory.");
			}
		}
		if(showPassFail && showComments)
		{
			if(answerStatus.isPassFailAnswered() == true && answerStatus.isFailSelected() == true && 
					answerStatus.isCommentAnswered() == false)
			{
				Section sec = (Section) this.getParent().getParent();
				errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - When Fail is selected, Comments should be entered.");
			}
		}
		if(answerStatus.isAllAdditonalMandatoryFieldsAnswered() == false)
		{
			Section sec = (Section) this.getParent().getParent();
			errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Additional fields should not be left unanswered.");
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
			final TestProcController testProcController) throws Exception {
		this.testProc = testProc;
		
		List thisItemFlagList = this.getFlagsAsList();
		boolean matchFlagsToDisplay = false;
		boolean flagsMatch = false;
		
		if (sResponse != null)
		{
			formItemResponse = testProcController.getFormItemResponseMap().get(getSurveyItemId());
			if(formItemResponse == null)
				formItemResponse = surveyResponseService.createFormItemResponse(sResponse.getResponseId(), AdvancedBomInspectItemAnswerType.this.getSurveyItemId());
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
		
		responseImageFileNames.clear(); // the name of the image that is attached as part of the response.
		passFailResult = "";
		canswer = "";
		tableAnswers = new HashMap<Integer, String>();
		if (itemResponse != null)
		{
			DateFormatter dateFormatter = DateFormatter.getInstance(TimeZone.getTimeZone(testProc.getWorkstationTimezoneId()));
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
				else if(this.imageKey == aUnit.getKey2() && aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
				{
					responseImageFileNames.add(aUnit.getKey4());
				}
				else if (this.equipmentSelectorKey == aUnit.getKey2())
				{
					String eqPk = aUnit.getKey4();
					if(eqPk != null && eqPk.trim().length() > 0)
					{
						try
						{
							EquipmentListFilter filter = new EquipmentListFilter();
							filter.setEquipmentOID(new EquipmentOID(Integer.parseInt(eqPk)));
							List<EquipmentListReportRow> eQList = (List<EquipmentListReportRow>) new EquipmentListReport(EtestApplication.getInstance().getUserContext(), 
									ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.EquipmentList, filter)).runReport().getReportData();
							if(eQList != null && eQList.size() > 0)
								equipmentSelector.setValue(eQList.get(0));
						}
						catch (Exception e)
						{
							logger.error("Invalid equipment attached to checksheet: " + eqPk);
						}
					}
				}
				//now the additional table fields
				else
				{
					int ind = aUnit.getKey1();
					Option aOption = (Option) this.getCustomFields().getOptionByValue(ind);
					if(aOption.getType() != null 
							&& (aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue()))
					{
						String tiAnswer = ""+aUnit.getKey3();
						tableAnswers.put(ind, tiAnswer);
					}
					if(aOption.getType() != null 
							&& (aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue() || aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue()))
					{
						String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
						tableAnswers.put(ind, tiAnswer);
					}
					else if(aOption.getType() != null && 
								(aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.TextBox.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.Date.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateTime.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue() ||
								aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue()
								)
							)
					{
						String tiAnswer = (aUnit.getKey4() != null)?aUnit.getKey4():"";
						tableAnswers.put(ind, tiAnswer);
					}
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
						testProcController.itemSelected(AdvancedBomInspectItemAnswerType.this, formItemResponse);
					else
						testProcController.itemDeSelected(AdvancedBomInspectItemAnswerType.this, formItemResponse);
						
				}
			});
			table.getContainerProperty(tableRowId, AdvancedBomInspectItemGroupAnswerType.HEADING_CHECKBOX).setValue(selectCheckbox);
		}
		
		VerticalLayout descVLayout = new VerticalLayout();
		//descVLayout.setWidth("200px");
		descVLayout.setWidth(0, Sizeable.Unit.PIXELS);
     	Label descField = new Label(this.getQuestionTextDescription());
     	descField.setContentMode(Label.CONTENT_PREFORMATTED);
     	descVLayout.addComponent(descField);
     	
     	String expectedValHeading = null;
		String expectedVal = null;
		String unitVal = null;
		Option unitOption = customFields.getOptionByName(BomInspectItemGroupAnswerType.HEADING_UNIT);
		if(unitOption != null)
		{
			unitVal = unitOption.getTextDesc();
		}
		for (int j = 0; j < customFields.size(); j++)
		{
			Option aOption = (Option) customFields.getOptionByIndex(j);
			String headingText = aOption.getText();
			if (BomInspectItemGroupAnswerType.HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				expectedValHeading = headingText;
				expectedVal = aOption.getTextDesc();
			}
			else if (BomInspectItemGroupAnswerType.HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				// this value identified earlier
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
				BomTypesEnum cType = aOption.getType();
				if(cType != null && BomTypesEnum.TextBox.getValue() == cType.getValue() ||
						cType != null && BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						VerticalLayout v = new VerticalLayout();
						v.addComponent(new Label((String)tableAnswers.get(aOption.getValue()) + ((unitVal != null)?(" " + unitVal): "")));
						table.getContainerProperty(tableRowId, headingText).setValue(v);
					}
				}
				else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						VerticalLayout v = new VerticalLayout();
						v.addComponent(new Label((String)tableAnswers.get(aOption.getValue())));
						table.getContainerProperty(tableRowId, headingText).setValue(v);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.Date.getValue() == cType.getValue() || BomTypesEnum.DateMandatory.getValue() == cType.getValue()))
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						VerticalLayout v = new VerticalLayout();
						Date dateVal = new Date(Long.parseLong((String)tableAnswers.get(aOption.getValue())));
						v.addComponent(new Label(DateFormatter.getInstance(EtestApplication.getInstance().getUserContext().getTimeZone()).formatDate(dateVal)));
						table.getContainerProperty(tableRowId, headingText).setValue(v);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.DateTime.getValue() == cType.getValue() || BomTypesEnum.DateTimeMandatory.getValue() == cType.getValue()))
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						VerticalLayout v = new VerticalLayout();
						Date dateVal = new Date(Long.parseLong((String)tableAnswers.get(aOption.getValue())));
						v.addComponent(new Label(DateFormatter.getInstance(EtestApplication.getInstance().getUserContext().getTimeZone()).formatDateTime(dateVal)));
						table.getContainerProperty(tableRowId, headingText).setValue(v);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.RadioGroup.getValue() == cType.getValue() || BomTypesEnum.RadioGroupMandatory.getValue() == cType.getValue()))
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						int ch = Integer.parseInt((String)tableAnswers.get(aOption.getValue()));
						String ans = choices[ch];
						VerticalLayout v = new VerticalLayout();
						v.addComponent(new Label(ans));
						table.getContainerProperty(tableRowId, headingText).setValue(v);
					}
				}
				else if(cType != null 
						&& (BomTypesEnum.CheckboxGroup.getValue() == cType.getValue() || BomTypesEnum.CheckboxGroupMandatory.getValue() == cType.getValue()))
				{
					if(tableAnswers.get(aOption.getValue()) != null)
					{
						String[] choices = findRadioOptions(aOption.getTextDesc());
						String answerList = (String)tableAnswers.get(aOption.getValue());

						VerticalLayout v = new VerticalLayout();

						String[] answers = answerList.split(",");
						for (int i = 0; i < answers.length; i++)
						{
							int ch = Integer.parseInt(answers[i]);
							String ans = choices[ch];
							v.addComponent(new Label(ans));
						}
						table.getContainerProperty(tableRowId, headingText).setValue(v);
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
///////////////////
			}
		}

		if(expectedVal != null || unitVal != null)
		{
			if(showExpectedValue && table.getContainerProperty(tableRowId, expectedValHeading) != null)
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

		VerticalLayout icons = new VerticalLayout();
		icons.setSpacing(true);
		icons.setSizeUndefined();
		table.getContainerProperty(tableRowId, BomInspectItemGroupAnswerType.HEADING_ICONS).setValue(icons);

		Option[] fields = getResponseFieldOptions();
		if(this.showPassFail || fields.length > 0 || showComments)
		{
			if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSE_ACROSS_UNITS, new SecurityContext(null, null, null, null, null)))
			{
				final Button detailButton = new Button("", new ClickListener() {
	
					@Override
					public void buttonClick(ClickEvent event)
					{
						try
						{
							EtestApplication.getInstance().showWindow( 
							new InspectionItemAnswerViewerReport(userContext, sResponse.getTestProcPk(), sResponse.getSurveyDefinition(), 
									sResponse.getSurveyPk(), AdvancedBomInspectItemAnswerType.this));
						}
						catch (Exception e)
						{
							e.printStackTrace();
							EtestApplication.getInstance().showError("Could not open details for units, Please try again later");
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
		
		if(showEquipmentSelector)
		{
			icons.addComponent(equipmentSelector);
		}
		
		//previous answers to question
		if(userContext.getSecurityManager().checkAccess(PlanSecurityManager.RESPONSE_COMPARE_RESPONSEITEM_WITH_OLD_VERSIONS, new SecurityContext(null, null, null, null, null)))
		{
			final Button detailButton = new Button("", new ClickListener() {

				@Override
				public void buttonClick(ClickEvent event)
				{
					if(sResponse == null)
					{
						((EtestApplication)UI.getCurrent()).showMessage("Form has no data.");
					}
					try
					{
						new QuestionSummaryViewer( sResponse.getTestProcPk(), sResponse.getSurveyDefinition(), 
								sResponse.getSurveyPk(), AdvancedBomInspectItemAnswerType.this).showQuestionResponseHistoryUnit();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						((EtestApplication)UI.getCurrent()).showError("Could not open history information for unit, Please try again later");
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
		

		if (sResponse != null)
		{
			if(isLatestResponse)
			{
				CommentsThreadPopup commentsB = new CommentsThreadPopup(EntityTypeEnum.FormItemResponse, formItemResponse.getPk(), FormItemResponse.COMMENT_CONTEXT_NOT_TO_PRINT, CommentsThreadPopup.ICON_STYLE_SMALL);
				icons.addComponent(commentsB);
			}
			
			oilItemList  = testProcController.getOilTransferMap().get(getSurveyItemId());

			//button to create a p8
			if ((ResponseMasterNew.STATUS_COMPLETE.equals(sResponse.getStatus()) 
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

		List actualFields = getActualFields();
		for (Iterator iterator = actualFields.iterator(); iterator.hasNext();)
		{
			TextField aField = (TextField) iterator.next();
			String answerText = (String) aField.getValue();
			if (answerText != null && answerText.trim().length() > DEFAULT_MAXLENGTH)
			{
				int index = (Integer)aField.getData();
				msgs.add(this.getIdentifier()+ " - " + this.getCustomFields().getOptionByValue(index).getText() + ": Answer should not be longer than " + DEFAULT_MAXLENGTH);
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
		AdvancedBomInspectItemAnswerType surveyItem;
		ComboBox imageAttachCol = null;
		TextField flagField = null;
		TextField commentFieldLengthField = new TextField("Max comment length");
		CheckBox enableEquipmentSelector = new CheckBox("Show equipment selector");
		CheckBox makeAttachmentMandatoryField = new CheckBox("Make attachment mandatory");
		FileUploadForm fileUpload = null;
		public ConfigForm(AdvancedBomInspectItemAnswerType surveyItem, FormDesignListener formDesignListener)
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
			Window w = root.findAncestor(Window.class);
			w.setCaption("Advanced Inspection Item Details");
			
			Panel panel = new Panel();
			panel.setHeight("400px");
			panel.setWidth("100%");
			
			root.addComponent(panel);
			
			theForm = new Form();
			
			VerticalLayout v = new VerticalLayout();
			v.addComponent(theForm);
			panel.setContent(v);
			
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
					Option aOption = new Option(parentOption.getText(), "", k);
					childCustomFields.add(aOption);
				}
				surveyItem.setCustomFields(childCustomFields);
			}			

			List<String> imageAttachCols = new ArrayList<String>(); 
			for (Iterator iterator = surveyItem.getCustomFields().iterator(); iterator.hasNext();)
			{
				Option aOption = (Option) iterator.next();
				if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_EXPECTED))
				{
					TextArea f = new TextArea(aOption.getText());
					f.setColumns(40);
					f.setNullRepresentation("");
					f.setValue(aOption.getTextDesc());
					theForm.addField(aOption.getText(), f);
					
					imageAttachCols.add(aOption.getText());
				}
				else if(aOption.getText().equalsIgnoreCase(BomInspectItemGroupAnswerType.HEADING_UNIT) ||
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

					if(BomTypesEnum.fromToken(aOption.getTextDesc()) == null)
					{
						imageAttachCols.add(aOption.getText());
					}
				}
			}

			if(surveyItem.isShowEquipmentSelector())
				enableEquipmentSelector.setValue(true);
			theForm.addField("EquipmentSelector", enableEquipmentSelector);
			
			if(surveyItem.isMakeAttachmentMandatory())
				makeAttachmentMandatoryField.setValue(true);
			theForm.addField("MakeAttachmentMandatory", makeAttachmentMandatoryField);
			
			flagField = new TextField("Flags");
			flagField.setColumns(40);
			flagField.setValue(surveyItem.getFlags());
			flagField.setNullRepresentation("");
			theForm.addField("Flags", flagField);

			if(imageAttachCols.size() > 0)
			{
				fileUpload = new FileUploadForm("Attach Picture", "jpg,jpeg,png,gif,tiff,tif", imageFileDisplayName, 
						(imageFileName != null)? FileStoreManager.getFile(imageFileName):null, false, new FileUploadForm.FileUploadListener() {
							
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
			
				imageAttachCol = new ComboBox();
				imageAttachCol.setCaption("Show Image under Column");
				for (Iterator iterator = imageAttachCols.iterator(); iterator.hasNext();)
				{
					imageAttachCol.addItem((String) iterator.next());
				}
				theForm.getLayout().addComponent(imageAttachCol);
				imageAttachCol.setValue(AdvancedBomInspectItemAnswerType.this.imageAttachCol);
			}	
			
			root.addComponent(new VSpacer(20));
			
			/* Add buttons in the form. */
			HorizontalLayout buttonArea = new HorizontalLayout();
			buttonArea.setSpacing(true);
			root.addComponent(buttonArea);

			Button okbutton = new Button("", new ClickListener() {
				
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
				
				if(enableEquipmentSelector.getValue() != null && true == enableEquipmentSelector.getValue())
				{
					surveyItem.setShowEquipmentSelector(true);
				}
				else
				{
					surveyItem.setShowEquipmentSelector(false);
				}
				
				if(makeAttachmentMandatoryField.getValue() != null && true == makeAttachmentMandatoryField.getValue())
				{
					surveyItem.setMakeAttachmentMandatory(true);
				}
				else
				{
					surveyItem.setMakeAttachmentMandatory(false);
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
						AdvancedBomInspectItemAnswerType.this.imageAttachCol = (String) imageAttachCol.getValue();
						if(AdvancedBomInspectItemGroupAnswerType.HEADING_EXPECTED.equals(AdvancedBomInspectItemAnswerType.this.imageAttachCol))
						{
							surveyItem.setShowExpectedValue(true);
						}
					}
					else
					{
						imageFileName = null;
						imageFileDisplayName = null;
						AdvancedBomInspectItemAnswerType.this.imageAttachCol = null;
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
							}
						}
						else
						{
							aOption.setTextDesc("");
							surveyItem.setShowComments(false);
							commentFieldMaxLength = null;
							surveyItem.setCommentFieldMaxLength(null);
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
						String fVal = (String)theForm.getField(aOption.getText()).getValue();
						aOption.setTextDesc((fVal != null)?fVal.trim():"");
						aOption.setType(BomTypesEnum.fromToken(fVal));
						if(aOption.getType() != null && aOption.getType().getValue() == BomTypesEnum.TextBox.getValue())
							surveyItem.setShowActualValue(true);
					}
				}
				
//				surveyItem.drawDesignViewInt(id, this.getApplication(), false, formDesignListener);
				formDesignListener.formItemConfigurationComplete(surveyItem);

				UI.getCurrent().removeWindow(root.findAncestor(Window.class));
			}
			catch(Validator.InvalidValueException iv)
			{
				this.setComponentError(new UserError(iv.getMessage()));
			}
			catch(CommitException cx)
			{
				this.setComponentError(new UserError(cx.getMessage()));
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
			UI.getCurrent().removeWindow(this.findAncestor(Window.class));
			formDesignListener.formItemConfigurationCancelled(surveyItem);
		}
	}

	private List<Field> getActualFields()
	{
		List<Field> actualFields = new ArrayList();
		for (Iterator iterator = tableFields.keySet().iterator(); iterator.hasNext();)
		{
			Integer fieldIndex = (Integer) iterator.next();
			Object f = tableFields.get(fieldIndex);
			if(f instanceof TextField)
				actualFields.add((Field) f);
		}
		return actualFields;
	}
	
	@Override
	public Option[] getActualFieldOptions()
	{
		List<Option> l = new ArrayList<Option>();
		for (Iterator iterator = this.getCustomFields().iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			if(Objects.equals(BomInspectItemGroupAnswerType.HEADING_COMMENTS, aOption.getText())
					|| Objects.equals(BomInspectItemGroupAnswerType.HEADING_RESULT, aOption.getText()) 
					|| Objects.equals(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE, aOption.getText())
					)
			{
				//ignore if users have put a BomTypeMarker in any of these fields.
				continue;
			}

			if(aOption.getType() != null && (aOption.getType().getValue() == BomTypesEnum.TextBox.getValue()
					|| aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ))
			{
				l.add(aOption);
			}
		}
		return l.toArray(new Option[l.size()]);
	}
	
	public Option[] getAdditionalMandatoryFieldOptions()
	{
		List<Option> l = new ArrayList<Option>();
		for (Iterator iterator = this.getCustomFields().iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();

			if(Objects.equals(BomInspectItemGroupAnswerType.HEADING_COMMENTS, aOption.getText())
					|| Objects.equals(BomInspectItemGroupAnswerType.HEADING_RESULT, aOption.getText()) 
					|| Objects.equals(BomInspectItemGroupAnswerType.HEADING_NOTAPPLICABLE, aOption.getText())
					)
			{
				//ignore if users have put a BomTypeMarker in any of these fields.
				continue;
			}

			if(aOption.getType() != null && 
					(
					aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue()
					)
				)
			{
				l.add(aOption);
			}
		}
		return l.toArray(new Option[l.size()]);
	}

	public Option[] getResponseFieldOptions()
	{
		List<Option> l = new ArrayList<Option>();
		for (Iterator iterator = this.getCustomFields().iterator(); iterator.hasNext();)
		{
			Option aOption = (Option) iterator.next();
			if(aOption.getType() != null && 
					(
					(aOption.getType().getValue() == BomTypesEnum.TextBox.getValue()|| 
					aOption.getType().getValue() == BomTypesEnum.NumericTextBox.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.CommentBox.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.RadioGroup.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.CheckboxGroup.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.Date.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.DateMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.RadioGroupMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.CheckboxGroupMandatory.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.DateTime.getValue() ||
					aOption.getType().getValue() == BomTypesEnum.DateTimeMandatory.getValue())
					))
			{
				l.add(aOption);
			}
		}
		return l.toArray(new Option[l.size()]);
	}

	@Override
	public InspectionLineItemAnswerStatus getAnswerStatus(SurveyItemResponse sItemResponse)
	{
		InspectionLineItemAnswerStatus answerStatus = new InspectionLineItemAnswerStatus();

		boolean passFailSelected = false;
		int actualValuesAnswered = 0;
		boolean commentEntered = false;
		boolean failSelected = false;
		boolean equipmentSelected = false;
		int addlMandatoryFieldsAnsweredCount = 0;
		boolean attachmentAdded = false;

		for(int i=0; i<sItemResponse.getResponseUnits().size(); i++)
		{
			ResponseUnit aR = (ResponseUnit) sItemResponse.getResponseUnits().get(i);
			if(aR.getKey2() == selectionKey)
			{
				passFailSelected = true;
				if(aR.getKey3() == failVal)
				{
					failSelected = true;
				}
				if(passVal == aR.getKey3())
				{
					answerStatus.setPassSelected(true);
				}
				if(naVal == aR.getKey3())
				{
					answerStatus.setNaSelected(true);
				}
			}
			else if(aR.getKey2() == commentsKey)
			{
				String comm = aR.getKey4();
				answerStatus.setCommentAnswer(comm);
				
				if(comm != null&& comm.trim().length() > 0)
				{
					commentEntered = true;
				}
			}
			else if(aR.getKey2() == equipmentSelectorKey)
			{
				String comm = aR.getKey4();
				answerStatus.setEquipmentPkString(comm);
				
				if(comm != null&& comm.trim().length() > 0)
				{
					equipmentSelected = true;
				}
			}
			else if(aR.getKey2() == imageKey)
			{
				String comm = aR.getKey4();
				if(comm != null&& comm.trim().length() > 0)
				{
					attachmentAdded = true;
				}
			}
			else
			{
				int key1 = aR.getKey1();
				Option opt = customFields.getOptionByValue(key1);
				if(opt != null && opt.getType()!= null)
				{
					if(BomTypesEnum.TextBox.getValue() == opt.getType().getValue())
					{
						String comm = aR.getKey4();
						if(comm != null&& comm.trim().length() > 0)
						{
							actualValuesAnswered++;
						}
					}
					else if(BomTypesEnum.RadioGroupMandatory.getValue() == opt.getType().getValue())
					{
						addlMandatoryFieldsAnsweredCount++; // this response unit will be present only if some answer was entered.
					}
					else if(BomTypesEnum.CheckboxGroupMandatory.getValue() == opt.getType().getValue()
							||BomTypesEnum.DateMandatory.getValue() == opt.getType().getValue()
							||BomTypesEnum.DateTimeMandatory.getValue() == opt.getType().getValue()
							)
					{
						String comm = aR.getKey4();
						if(comm != null&& comm.trim().length() > 0)
						{
							addlMandatoryFieldsAnsweredCount++;
						}
					}
				}
			}
		}

		answerStatus.setPassFailAnswered(passFailSelected);
		answerStatus.setCommentAnswered(commentEntered);
		answerStatus.setFailSelected(failSelected);
		answerStatus.setEquipmentSelected(equipmentSelected);
		answerStatus.setAttachmentAdded(attachmentAdded);
		
		Option[] actualFields = getActualFieldOptions();
		if(actualFields.length > 0 && actualFields.length == actualValuesAnswered)
		{
			answerStatus.setActualAnswered(true);
		}
		
		Option[] addlMandatoryFields = getAdditionalMandatoryFieldOptions();	
		if(addlMandatoryFields.length == 0 || (addlMandatoryFields.length > 0 && addlMandatoryFields.length == addlMandatoryFieldsAnsweredCount))
		{
			answerStatus.setAllAdditonalMandatoryFieldsAnswered(true);
		}
		return answerStatus;
	}
	
	/**
	 * Used by TestProcController to get the description when creating ncrs.
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
	 * @return
	 */
	public String buildOpenItemTransferDescription()
	{
		return buildOpenItemTransferDescription(testProc, (Section) this.getParent().getParent(), passFailResult, tableAnswers, canswer);
	}
	private String buildOpenItemTransferDescription(UnitFormQuery testProcQuery, Section sectionItem, String passFailResult, HashMap tableAnswers, String testerComment)
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
			else if(cType != null && BomTypesEnum.TextBox.getValue() == cType.getValue() ||
					cType != null && BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
			{
				String ans = (String)tableAnswers.get(aOption.getValue());
				if(ans != null && ans.trim().length() > 0)
				{
					actualCols.append(actualColsep).append(headingText).append(": ").append(ans);
					actualColsep = "\n\n";
				}
			}
			else if(cType != null && BomTypesEnum.CommentBox.getValue() == cType.getValue())
			{
				String ans = (String)tableAnswers.get(aOption.getValue());
				if(ans != null && ans.trim().length() > 0)
				{
					actualCols.append(actualColsep).append(headingText).append(": ").append(ans);
					actualColsep = "\n\n";
				}
			}
			else if(cType != null 
					&& (BomTypesEnum.RadioGroup.getValue() == cType.getValue() || BomTypesEnum.RadioGroupMandatory.getValue() == cType.getValue()))
			{
			}
			else if(cType != null 
					&& (BomTypesEnum.CheckboxGroup.getValue() == cType.getValue() || BomTypesEnum.CheckboxGroupMandatory.getValue() == cType.getValue()))
			{
			}
			else if(cType != null 
					&& (BomTypesEnum.Date.getValue() == cType.getValue() || BomTypesEnum.DateMandatory.getValue() == cType.getValue()))
			{
			}
			else if(cType != null 
					&& (BomTypesEnum.DateTime.getValue() == cType.getValue() || BomTypesEnum.DateTimeMandatory.getValue() == cType.getValue()))
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


	private void buildAdditionalOpenItemTransferFields(TestProcItemFailureTransferContext itemFailureContext, HashMap tableAnswers)
	{
		for (int j = 0; j < customFields.size(); j++)
		{
			Option aOption = customFields.getOptionByIndex(j);
			if("Part No".equalsIgnoreCase(aOption.getText().trim()) 
					|| "Part Number".equalsIgnoreCase(aOption.getText().trim())
					|| "PartNo".equalsIgnoreCase(aOption.getText().trim())
					|| "PartNumber".equalsIgnoreCase(aOption.getText().trim())
					|| "PartNumber.".equalsIgnoreCase(aOption.getText().trim())
					|| "PartNo.".equalsIgnoreCase(aOption.getText().trim())
					)
			{
				String cellVal = null;
				
				BomTypesEnum cType = aOption.getType();
				if(cType == null)
				{
					cellVal = aOption.getTextDesc();
				}
				else if(BomTypesEnum.TextBox.getValue() == cType.getValue() || BomTypesEnum.CommentBox.getValue() == cType.getValue() || BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
				{
					cellVal = (String)tableAnswers.get(aOption.getValue());
				}
				if(cellVal != null && cellVal.trim().length() > 0)
					itemFailureContext.setPartNo(cellVal);
			}
			else if("Quantity".equalsIgnoreCase(aOption.getText().trim()) 
					|| "Qty".equalsIgnoreCase(aOption.getText().trim())
					|| "Total per train".equalsIgnoreCase(aOption.getText().trim())
					)
			{
				String cellVal = null;
				BomTypesEnum cType = aOption.getType();
				if(cType == null)
				{
					cellVal = aOption.getTextDesc();
				}
				else if(BomTypesEnum.TextBox.getValue() == cType.getValue() || BomTypesEnum.CommentBox.getValue() == cType.getValue() || BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
				{
					cellVal = (String)tableAnswers.get(aOption.getValue());
				}
				if(cellVal != null && cellVal.trim().length() > 0)
					itemFailureContext.setQuantiy(cellVal);
			}
			
			else if ("Unit of Measure".equalsIgnoreCase(aOption.getText().trim())
					|| "UnitofMeasure".equalsIgnoreCase(aOption.getText().trim()))
			{
				String cellVal = null;
				BomTypesEnum cType = aOption.getType();
				if(cType == null)
				{
					cellVal = aOption.getTextDesc();
				}
				else if(BomTypesEnum.TextBox.getValue() == cType.getValue() || BomTypesEnum.CommentBox.getValue() == cType.getValue() || BomTypesEnum.NumericTextBox.getValue() == cType.getValue())
				{
					cellVal = (String)tableAnswers.get(aOption.getValue());
				}
				if(cellVal != null && cellVal.trim().length() > 0)
				{
					for (Iterator iterator = UnitOfMeasures.getEnumList(UnitOfMeasures.class).values().iterator(); iterator.hasNext();)
					{
						UnitOfMeasures aM = (UnitOfMeasures) iterator.next();
						if(aM.getName().equalsIgnoreCase(cellVal))
						{
							itemFailureContext.setUnitOfMeasure(aM);
							break;
						}
					}
				}
			}
		}
	}
	
	
	@Override
	public ExpectedNumericValue getExpectedUpperLower() 
	{
		return ExpectedNumericValue.getExpectedUpperLower(getExpectedValue());
	}
	
	
	private class P8TransferMenuBar extends MenuBar
	{
		DisplayModeEnum displayMode;
		SurveyResponse sResponse;
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
							UnitObj unit = unitService.getUnitByPk(new UnitOID(AdvancedBomInspectItemAnswerType.this.testProc.getUnitPk()));
							UnitQuery unitQuery = unitService.getUnitQueryByPk((int) unit.getPk(), new ProjectOID(AdvancedBomInspectItemAnswerType.this.testProc.getProjectPk()));

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
				formItemResponse =surveyResponseService.getFormItemResponse(sResponse.getResponseId(), AdvancedBomInspectItemAnswerType.this.getSurveyItemId(), true);

				UnitObj unit = unitService.getUnitByPk(new UnitOID(AdvancedBomInspectItemAnswerType.this.testProc.getUnitPk()));
				UnitQuery unitQuery = unitService.getUnitQueryByPk((int) unit.getPk(), new ProjectOID(AdvancedBomInspectItemAnswerType.this.testProc.getProjectPk()));
				
				OpenItemV2.StatusEnum[] openItemStatus = null;
				List<User> projectManagers =projectService.getProjectManagers(unitQuery.getProjectOID());
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
				formItemResponse =surveyResponseService.getFormItemResponse(sResponse.getResponseId(), AdvancedBomInspectItemAnswerType.this.getSurveyItemId(), true);

				UnitObj unit = unitService.getUnitByPk(new UnitOID(AdvancedBomInspectItemAnswerType.this.testProc.getUnitPk()));
				UnitQuery unitQuery = unitService.getUnitQueryByPk((int) unit.getPk(), new ProjectOID(AdvancedBomInspectItemAnswerType.this.testProc.getProjectPk()));
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
					itemFailureContext.setWorkstationPk(AdvancedBomInspectItemAnswerType.this.testProc.getWorkstationPk());
					itemFailureContext.setTransferDescription(buildOpenItemTransferDescription(AdvancedBomInspectItemAnswerType.this.testProc, 
							(Section) AdvancedBomInspectItemAnswerType.this.getParent().getParent(), passFailResult, tableAnswers, canswer));
					itemFailureContext.setQuestionAttachedImageName(getImageFileName());

					//TODO:: currently we are moving only the first response image to open item. We need to move all responses 
					if(responseImageFileNames != null && responseImageFileNames.size() > 0)
						itemFailureContext.setResponseImageName(responseImageFileNames.get(0));

					
					buildAdditionalOpenItemTransferFields(itemFailureContext, tableAnswers);
					
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

					//Collect the answers from the textFields
					HashMap currentAnswers = new HashMap<Integer, String>();
					for (Iterator iterator = tableFields.keySet().iterator(); iterator.hasNext();) 
					{
						Integer key = (Integer) iterator.next();
						Field f = (Field) tableFields.get(key);
						currentAnswers.put(key, f.getValue());
					}
					
					String testerComment = null;
					if(commentsTxt!= null && commentsTxt.getValue() != null)
						testerComment = (String) commentsTxt.getValue();
					
					TestProcItemFailureTransferContext itemFailureContext = new TestProcItemFailureTransferContext();
					
					itemFailureContext.setFormItemResponsePk(formItemResponse.getPk());
					itemFailureContext.setWorkstationPk(AdvancedBomInspectItemAnswerType.this.testProc.getWorkstationPk());
					itemFailureContext.setTransferDescription(buildOpenItemTransferDescription(AdvancedBomInspectItemAnswerType.this.testProc, 
							(Section) AdvancedBomInspectItemAnswerType.this.getParent().getParent(), passFailResult, currentAnswers, testerComment));
					itemFailureContext.setQuestionAttachedImageName(getImageFileName());

					//TODO:: currently we are moving only the first response image to open item. We need to move all responses 
					if(responseImageFileNames != null && responseImageFileNames.size() > 0)
						itemFailureContext.setResponseImageName(responseImageFileNames.get(0));
					
					buildAdditionalOpenItemTransferFields(itemFailureContext, currentAnswers);

					openItemEditForm.setTestProcFailureTransferContext(itemFailureContext);
					
					openItemEditForm.addToView();
				}
				
			});
			
		}
	}
}
