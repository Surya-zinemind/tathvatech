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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.tathvatech.common.common.DataTypes;
import com.tathvatech.common.exception.InvalidResponseException;
import com.tathvatech.common.utils.DataConversionUtil;
import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.forms.controller.TestProcController;
import com.tathvatech.logic.common.Logic;
import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.intf.LogicSubject;
import com.tathvatech.survey.intf.MultiDataTypeQuestionType;
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
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TextAreaAnswerType extends SurveySaveItem implements SurveyDisplayItem, LogicSubject, MultiDataTypeQuestionType
{
	private static final Logger logger = LoggerFactory.getLogger(TextAreaAnswerType.class);

	// if no maxlength is defined, set it to 255
	public static final int		DEFAULT_MAXLENGTH		= 5000;
	private int					dataType				= DataTypes.DATATYPE_STRING;
	protected Integer			maxLength				= null;
	private String				questionText;
	private String				questionTextDescription	= "";
	private boolean				required;

	private TextArea textField;

	/**
     *
     */
	public TextAreaAnswerType()
	{
		super();
	}

	/**
	 * @param _survey
	 */
	public TextAreaAnswerType(SurveyDefinition _survey)
	{
		super(_survey);
	}

	public String getTypeName()
	{
		return "TextAreaAnswerType";
	}

	public boolean hasSomethingToDisplay()
	{
		return true;
	}

	/**
	 * @return Returns the maxLength.
	 */
	 public Integer getMaxLength()
	 {
		 return maxLength;
	 }

	/**
	 * @param maxLength
	 *            The maxLength to set.
	 */
	 public void setMaxLength(Integer maxLength)
	 {
		 this.maxLength = maxLength;
	 }

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	/**
	 * @return Returns the questionText.
	 */
	public String getQuestionText()
	{
		return questionText;
	}

	/**
	 * @param questionText
	 *            The questionText to set.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.SurveySaveItem#getResponse(java.util.Map)
	 */
	public SurveyItemResponse getResponse(Map paramMap, Properties props)
			throws InvalidResponseException
	{
		int maxAnswerLengthDB = DEFAULT_MAXLENGTH;
		if(maxLength != null)
			maxAnswerLengthDB = maxLength;
		
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();
		String[] answer = (String[]) paramMap.get(this.getSurveyItemId());
		String answerString = null;
		if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
		{
			answerString = answer[0];
			if (answerString != null && answerString.length() > maxAnswerLengthDB)
			{
				throw new InvalidResponseException("Survey_invalidAnswerMessage");
			}

			if (answerString != null && dataType == DataTypes.DATATYPE_INTEGER)
			{
				try
				{
					Double.parseDouble(answerString);
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
			} else if (answerString != null && dataType == DataTypes.DATATYPE_DATE)
			{
				try
				{
					TimeZone tZone = (TimeZone) props.get("timeZone");
					Date date = DataConversionUtil.getDateFromString(answerString,
							DataConversionUtil.FORMAT_DATE_SURVEY, tZone);
					answerString = new Long(date.getTime()).toString();
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
			} else if (answerString != null && DataTypes.DATATYPE_EMAIL == dataType)
			{
				if (!(EmailValidator.validateEmail(answerString)))
				{
					throw new InvalidResponseException("Survey_invalidAnswerMessage");
				}
			}

			ResponseUnit aUnit = new ResponseUnit();
			aUnit.setKey4(answerString);
			itemResponse.addResponseUnit(aUnit);
		}

		if (isRequired() && (answerString == null || answerString.trim().length() == 0))
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

	public Logic getLogic(String logicId)
	{
		return super.getLogicFromList(logicId);
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

	public String getIdentifier()
	{
		if (hidden)
		{
			return ("[Hidden]-&nbsp;" + questionText);
		} else
		{
			return questionText;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#drawQuestionConfigurationForm()
	 */
	public ConfigForm drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		Panel p = new Panel();
		p.setWidth("100%");
		
		VerticalLayout qLayout = new VerticalLayout();
		qLayout.setSizeFull();
		qLayout.addStyleName("text_box_type");

		p.setContent(qLayout);
		
		VerticalLayout questionTitleArea = new VerticalLayout();
		questionTitleArea.addStyleName("question_title_area");
		qLayout.addComponent(questionTitleArea);
		
		HorizontalLayout qTitle = new HorizontalLayout();
		qTitle.setSpacing(true);
		questionTitleArea.addComponent(qTitle);

		Label heading = new Label(questionText);
		heading.setSizeUndefined();
		heading.addStyleName("question_heading");
		qTitle.addComponent(heading);

		if(isRequired())
		{
			Label reqStar = new Label("*");
			reqStar.addStyleName("red_label");
			qTitle.addComponent(reqStar);
		}

		Label desc = new Label(questionTextDescription);
		heading.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);
		
		this.textField = new TextArea();
		textField.setRows(5);
		if(maxLength != null)
			textField.setMaxLength(maxLength);
		textField.setWidth("100%");
		questionArea.addComponent(textField);

		return p;
	}

	public void setConfiguration(List<List<String>> fileContents)
	{
		try
		{
			if(fileContents.get(0).size() >= 1)
				this.questionText = fileContents.get(0).get(0);
			if(fileContents.get(0).size() >= 2)
				this.questionTextDescription = fileContents.get(0).get(1);
		}
		catch (Exception e)
		{
			logger.info("question text or question description could not be set - "+ e.getMessage());
		}

		this.required = false;
		if(fileContents.get(0).size() >= 3)
		{
			try
			{
				if(fileContents.get(0).get(2).trim().equalsIgnoreCase("y"))
					this.required = true;
			}
			catch (Exception e)
			{
				logger.info("required could not be set - "+ e.getMessage());
			}
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
		this.setRequired(new Boolean(element.getAttributeValue("required")).booleanValue());
		this.setQuestionText(element.getAttributeValue("text"));
		this.questionTextDescription = element.getAttributeValue("textDescription");
		if(element.getAttribute("maxLength") != null)
			this.maxLength = Integer.parseInt(element.getAttributeValue("maxLength"));
		this.setDataType(Integer.parseInt(element.getAttributeValue("dataType")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.AnswerType#toXML(org.jdom.Element)
	 */
	public Element toXML()
	{
		Element element = new Element("surveyItem");

		super.toXML(element);

		element.setAttribute("required", new Boolean(required).toString());

		if (questionText != null)
		{
			element.setAttribute("text", questionText);
		}

		if (questionTextDescription != null)
		{
			element.setAttribute("textDescription", questionTextDescription);
		}

		if(maxLength != null)
			element.setAttribute("maxLength", Integer.toString(maxLength));
		element.setAttribute("dataType", Integer.toString(dataType));
		return element;
	}

	public Element writeResponseXML(SurveyItemResponse itemResponse) throws Exception
	{
		Element elem = new Element("itemResponse");
		elem.setAttribute("questionId", this.surveyItemId);

		for (int r = 0; r < itemResponse.getResponseUnits().size(); r++)
		{
			ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(r);

			Element uElem = new Element("u");
			if (aUnit.getKey4() != null && aUnit.getKey4().trim().length() > 0)
			{
				uElem.setAttribute("k4", aUnit.getKey4());
			}
			elem.addContent(uElem);
		}
		return elem;
	}

	public class ConfigForm extends CustomComponent
	{
		FormDesignListener	formDesignListener;
		TextAreaAnswerType	surveyItem;
		
		BeanFieldGroup<TextAreaAnswerType> fieldGroup;
		FormLayout content;

		public ConfigForm(TextAreaAnswerType surveyItem, FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			content = new FormLayout();
			content.setMargin(true);
			this.setCompositionRoot(content);
			
			BeanItem<TextAreaAnswerType> item = new BeanItem<TextAreaAnswerType>(surveyItem);

			fieldGroup = new BeanFieldGroup<TextAreaAnswerType>(TextAreaAnswerType.class);
			
			// We need an item data source before we create the fields to be able to
	        // find the properties, otherwise we have to specify them by hand
			fieldGroup.setItemDataSource(item);
			fieldGroup.setBuffered(true);

			List<String> displayFields = Arrays.asList( "questionText", "questionTextDescription", "maxLength",
					"required" );
			
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

			
			// set the project name as required
	    	fieldGroup.getField("questionText").setRequired(true);
			((TextField)fieldGroup.getField("questionText")).setColumns(40);
			((TextField)fieldGroup.getField("questionText")).setMaxLength(80);
			fieldGroup.getField("questionText").setRequiredError("Question text cannot be empty");

			fieldGroup.getField("questionTextDescription").setCaption("Description");
			((TextField)fieldGroup.getField("questionTextDescription")).setColumns(40);
			((TextField)fieldGroup.getField("questionTextDescription")).setMaxLength(200);

			fieldGroup.getField("maxLength").setCaption("Maximun text length");
			((TextField)fieldGroup.getField("maxLength")).setColumns(6);
			((TextField)fieldGroup.getField("maxLength")).setMaxLength(4);
			((TextField)fieldGroup.getField("maxLength")).setConversionError("Numeric value expected");

			fieldGroup.getField("questionText").focus();

			/* Add buttons in the form. */
			HorizontalLayout buttonArea = new HorizontalLayout();
			buttonArea.setSpacing(true);
			content.addComponent(buttonArea);

			Button okbutton = new Button("");
			okbutton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					commit();
				}
			});
			okbutton.setIcon(new ThemeResource("Images/save.png"));
			okbutton.addStyleName(BaseTheme.BUTTON_LINK);
			okbutton.setDescription("Save");
			buttonArea.addComponent(okbutton);
			Button cancelbutton = new Button("");
			cancelbutton.addClickListener(new ClickListener() {
				
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
				formDesignListener.formItemConfigurationComplete(surveyItem);
			}
			catch(Validator.InvalidValueException iv)
			{
//				this.setComponentError(new UserError(iv.getMessage()));
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

	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse, Component parent, String[] flags,
									   final FormEventListner formEventListner)
	{
		String answer = "";
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
			if (itemResponse != null && itemResponse.getResponseUnits().size() > 0)
			{
				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
				answer = aUnit.getKey4();
			}
		}

		List<String> flagList = new ArrayList();
		for (int i = 0; i < flags.length; i++)
		{
			flagList.add(flags[i]);
		}
		if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDBUTMANDATORYONLY))
		{
			if(isRequired() && answer.length() == 0)
			{
			}
			else
			{
				return null;
			}
		}
		else if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDONLY))
		{
			if(answer.length() == 0)
			{
			}
			else
			{
				return null;
			}
		}

		Panel p = new Panel();
		p.setWidth("100%");
		VerticalLayout qLayout = new VerticalLayout();
		qLayout.addStyleName("text_box_type");
		
		p.setContent(qLayout);
		
		VerticalLayout questionTitleArea = new VerticalLayout();
		questionTitleArea.addStyleName("question_title_area");
		qLayout.addComponent(questionTitleArea);
		
		HorizontalLayout qTitle = new HorizontalLayout();
		qTitle.setSpacing(true);
		questionTitleArea.addComponent(qTitle);

		Label heading = new Label(questionText);
		heading.setSizeUndefined();
		heading.addStyleName("question_heading");
		qTitle.addComponent(heading);

		if(isRequired())
		{
			Label reqStar = new Label("*");
			reqStar.addStyleName("red_label");
			qTitle.addComponent(reqStar);
		}

		Label desc = new Label(questionTextDescription);
		heading.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);

		this.textField = new TextArea();
		textField.addListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event)
			{
				formEventListner.fillDataEvent(TextAreaAnswerType.this, validate());
			}
		});
		if(maxLength != null)
			textField.setMaxLength(maxLength);
		textField.setRows(5);
		textField.setWidth("100%");
		textField.setValue(answer);
		questionArea.addComponent(textField);

		return p;
	}

	@Override
	public SurveyItemResponse captureResponse() throws InvalidResponseException
	{
		int maxAnswerLengthDB = DEFAULT_MAXLENGTH;
		if(maxLength != null)
			maxAnswerLengthDB = maxLength;
		
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();

		String answerString = (String) textField.getValue();

		if (answerString != null && answerString.trim().length() > maxAnswerLengthDB)
		{
			throw new InvalidResponseException(this.getQuestionText() + " - Text entered is too long.");
		}
			
		if (answerString != null && answerString.trim().length() > 0)
		{
			ResponseUnit aUnit = new ResponseUnit();
			aUnit.setKey4(answerString);
			itemResponse.addResponseUnit(aUnit);
		}

		return itemResponse;
	}

	public List<String> validateResponse(SurveyItemResponse itemResponse)
	{
		int maxAnswerLengthDB = DEFAULT_MAXLENGTH;
		if(maxLength != null)
			maxAnswerLengthDB = maxLength;
		
		String answerString = null;
		if(itemResponse != null && itemResponse.getResponseUnits() != null && itemResponse.getResponseUnits().size() > 0)
		{
			answerString = ((ResponseUnit)itemResponse.getResponseUnits().get(0)).getKey4();
		}
		
		List<String> errors = new ArrayList<String>();
		if (answerString != null && answerString.trim().length() > maxAnswerLengthDB)
		{
			Section sec = (Section) this.getParent();
			errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Text entered is too long.");
		}
		
		if (isRequired() && (answerString == null || answerString.trim().length() == 0))
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Required for " + getIdentifier()
						+ " is true and no answer was entered, throwing InvalidResponseException");
			}
			// denotes that there was no answer entered for this question
			// if the answer is required then, an error message should be throws
			// back to the page.
			Section sec = (Section) this.getParent();
			errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Cannot be left blank.");
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

		String answer = "";
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
			if (itemResponse != null && itemResponse.getResponseUnits().size() > 0)
			{
				ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
				answer = aUnit.getKey4();
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

		this.textField = new TextArea();
		textField.setRows(5);
		textField.setWidth("100%");
		textField.setValue(answer);
		textField.setReadOnly(true);
		questionArea.addComponent(textField);

		return p;
	}

	private String[] validate()
	{
		List msgs = new ArrayList();

		String answerString = (String) textField.getValue();
		if (maxLength != null && answerString != null && answerString.length() > maxLength)
		{
			msgs.add(this.questionText + ">> Answer is too long, it cannot be longer than "
					+ maxLength);
		}

		if (answerString != null && dataType == DataTypes.DATATYPE_INTEGER)
		{
			try
			{
				Double.parseDouble(answerString);
			}
			catch (Exception ex)
			{
				logger.warn(ex + " :: " + ex.getMessage());
				if (logger.isDebugEnabled())
				{
					logger.debug(ex.getMessage(), ex);
				}
				msgs.add(this.questionText + ">> Answer can only be numeric");
			}
		} else if (answerString != null && dataType == DataTypes.DATATYPE_DATE)
		{
			try
			{
				TimeZone tZone = (EtestApplication.getInstance()).getUserContext()
						.getTimeZone();
				Date date = DataConversionUtil.getDateFromString(answerString,
						DataConversionUtil.FORMAT_DATE_SURVEY, tZone);
				answerString = new Long(date.getTime()).toString();
			}
			catch (Exception ex)
			{
				logger.warn(ex + " :: " + ex.getMessage());
				if (logger.isDebugEnabled())
				{
					logger.debug(ex.getMessage(), ex);
				}
				msgs.add(this.questionText + ">> Answer should be a valid date in the format "
						+ DataConversionUtil.FORMAT_DATE_SURVEY);
			}
		} else if (answerString != null && DataTypes.DATATYPE_EMAIL == dataType)
		{
			if (!(EmailValidator.validateEmail(answerString)))
			{
				msgs.add(this.questionText + ">> Answer should be a valid email");
			}
		}

		if (isRequired() && (answerString == null || answerString.trim().length() == 0))
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Required for " + getIdentifier()
						+ " is true and no answer was entered, throwing InvalidResponseException");
			}
			// denotes that there was no answer entered for this question
			// if the answer is required then, an error message should be throws
			// back to the page.
			msgs.add(this.questionText + ">> Answer cannot be empty");
		}

		return (msgs.size() == 0) ? null : (String[]) msgs.toArray(new String[msgs.size()]);

	}
}
