/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;
import com.tathvatech.common.exception.InvalidResponseException;
import com.tathvatech.survey.intf.LogicSubject;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.tathvatech.survey.intf.MultiDataTypeQuestionType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.imageio.ImageIO;


import com.tathvatech.common.common.DataTypes;
import com.tathvatech.common.common.FileStoreManager;
import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.forms.common.FormDesignListener;
import com.tathvatech.forms.controller.TestProcController;
import com.tathvatech.forms.response.SignatureCaptureItemResponse;
import com.tathvatech.logic.common.Logic;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyItemResponse;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.response.ResponseUnit;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.Attachment;
import jakarta.persistence.Embedded;
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
public class SignatureCaptureAnswerType extends SurveySaveItem implements SurveyDisplayItem, LogicSubject,
		MultiDataTypeQuestionType
{
	private static final Logger logger = LoggerFactory.getLogger(SignatureCaptureAnswerType.class);

	public static int MAX_IMAGE_WIDTH = 900;
	
	private int dataType = DataTypes.DATATYPE_STRING;
	private String questionText;
	private String questionTextDescription = "";
	private boolean required;

	private FileUploadFormMulti signatureField;
	private TextField signedByField = new TextField("Signed by:");

	
	// value gets saved into these attributes when the file upload happens.
	private String signatureFileName = null;
	private Date signatureTimeStamp = null;
	private String signedBy = null;
	

	/**
     *
     */
	public SignatureCaptureAnswerType()
	{
		super();
	}

	/**
	 * @param _survey
	 */
	public SignatureCaptureAnswerType(SurveyDefinition _survey)
	{
		super(_survey);
	}

	public String getTypeName()
	{
		return "SignatureCapture";
	}

	public boolean hasSomethingToDisplay()
	{
		return true;
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
	public SurveyItemResponse getResponse(Map paramMap, Properties props) throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();
		String[] answer = (String[]) paramMap.get(this.getSurveyItemId());
		String signatureFileName = null;
		if (answer != null && answer.length > 0 && answer[0].trim().length() > 0)
		{
			signatureFileName = answer[0];
			ResponseUnit aUnit = new ResponseUnit();
			aUnit.setKey4(signatureFileName);
			itemResponse.addResponseUnit(aUnit);
		}

		if (isRequired() && (signatureFileName == null || signatureFileName.trim().length() == 0))
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
		}
		else
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
	public  Component  drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		Panel p = new Panel();
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

		this.signatureField = new FileUploadFormMulti(null, "jpg,jpeg,png", null, false, null);
		signatureField.setEnabled(false);
		questionArea.addComponent(signatureField);
		
		TextField signedByField = new TextField("Signed by");
		signedByField.setRequired(true);
		questionArea.addComponent(signedByField);

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
		FormDesignListener formDesignListener;
		SignatureCaptureAnswerType surveyItem;

		BeanFieldGroup<SignatureCaptureAnswerType> fieldGroup;
		FormLayout content;

		public ConfigForm(SignatureCaptureAnswerType surveyItem, FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			content = new FormLayout();
			content.setMargin(true);
			this.setCompositionRoot(content);
			
			BeanItem<SignatureCaptureAnswerType> item = new BeanItem<SignatureCaptureAnswerType>(surveyItem);

			fieldGroup = new BeanFieldGroup<SignatureCaptureAnswerType>(SignatureCaptureAnswerType.class);
			
			// We need an item data source before we create the fields to be able to
	        // find the properties, otherwise we have to specify them by hand
			fieldGroup.setItemDataSource(item);
			fieldGroup.setBuffered(true);

			List<String> displayFields = Arrays.asList( "questionText", "questionTextDescription", "required" );
			
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

			fieldGroup.setBuffered(false);

			// set the project name as required
			fieldGroup.getField("questionText").setRequired(true);
			((TextField)fieldGroup.getField("questionText")).setColumns(40);
			((TextField)fieldGroup.getField("questionText")).setMaxLength(80);
			fieldGroup.getField("questionText").setRequiredError("Question text cannot be empty");

			fieldGroup.getField("questionTextDescription").setCaption("Description");
			((TextField)fieldGroup.getField("questionTextDescription")).setColumns(40);
			((TextField)fieldGroup.getField("questionTextDescription")).setMaxLength(200);

			fieldGroup.getField("questionText").focus();

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
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
			if (itemResponse != null && itemResponse.getResponseUnits().size() > 0)
			{
				try
				{
					ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
					String data = aUnit.getKey4();
					
					SignatureCaptureItemResponse val = new ObjectMapper().readValue(data, SignatureCaptureItemResponse.class);
					signatureFileName = val.getImageFileName(); 
					signatureTimeStamp = val.getSignatureTimestamp();
					signedBy = val.getSignedBy();
				}
				catch (Exception e)
				{
					logger.error("Error reading signature capture response from json", e);
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
			if(isRequired() && (signatureFileName == null || signatureFileName.length() == 0))
			{
			}
			else
			{
				return null;
			}
		}
		else if(flagList.contains(BomInspectItemGroupAnswerType.FLAG_SHOWUNANSWEREDONLY))
		{
			if(signatureFileName == null || signatureFileName.length() == 0)
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
		desc.addStyleName("question_description");
		questionTitleArea.addComponent(desc);

		VerticalLayout questionArea = new VerticalLayout();
		questionArea.setMargin(true);
		qLayout.addComponent(questionArea);

		List<Attachment> attachments = new ArrayList<>();
		if(signatureFileName != null && signatureFileName.trim().length() > 0)
		{
			Attachment att = new Attachment();
			att.setFileDisplayName(signatureFileName);
			att.setFileName(signatureFileName);
			attachments.add(att);
		}
		
		this.signatureField = new FileUploadFormMulti(null, "jpeg,jpg,png,gif", attachments, false, true, false, false, new FileUploadListener() {
			
			@Override
			public void uploadComplete(List<AttachmentIntf> attachedFiles)
			{
				if(attachedFiles != null && attachedFiles.size() > 0)
				{
					signatureFileName = attachedFiles.get(0).getFileName();
					signatureTimeStamp = new Date();
				}
			}
			
			@Override
			public void uploadCancelled()
			{
				// TODO Auto-generated method stub
				
			}
		});
		questionArea.addComponent(signatureField);
		
		signedByField.setRequired(true);
		signedByField.setNullRepresentation("");
		signedByField.setValue(signedBy);
		questionArea.addComponent(signedByField);

		if(signatureTimeStamp != null)
		{
			TimeZone timeZone = TimeZone.getTimeZone(testProc.getWorkstationTimezoneId());
			questionArea.addComponent(new Label("Signed on: " + DateFormatter.getInstance(timeZone).formatDateTime(signatureTimeStamp)));
		}
		
		//when the verifier or approver edits the response, he need to edit only the comments so disable all others
		if(Arrays.asList(flags).contains(BomInspectItemGroupAnswerType.FLAG_EDIT_COMMENTS_ONLY))
		{
			signatureField.setEnabled(false);
		}

		return p;
	}

	@Override
	public SurveyItemResponse captureResponse() throws InvalidResponseException
	{
		SimpleSurveyItemResponse itemResponse = new SimpleSurveyItemResponse();

		
		if(signatureFileName != null && signatureFileName.length() > 0)
		{
			try
			{
				SignatureCaptureItemResponse itemResp = new SignatureCaptureItemResponse();
				itemResp.setImageFileName(signatureFileName);
				itemResp.setSignatureTimestamp(new Date());
				itemResp.setSignedBy(signedByField.getValue());
				
				ResponseUnit aUnit = new ResponseUnit();
				aUnit.setKey4(new ObjectMapper().writeValueAsString(itemResp));

				itemResponse.addResponseUnit(aUnit);
			}
			catch (Exception e)
			{
				logger.error("Error converting signature capture response to json", e);
			}
		}
		
		return itemResponse;
	}

	public List<String> validateResponse(SurveyItemResponse itemResponse)
	{
		SignatureCaptureItemResponse val  = null;
		if(itemResponse != null && itemResponse.getResponseUnits() != null && itemResponse.getResponseUnits().size() > 0)
		{
			try
			{
				val = new ObjectMapper().readValue(((ResponseUnit)itemResponse.getResponseUnits().get(0)).getKey4(), 
						SignatureCaptureItemResponse.class);
			}
			catch (Exception e)
			{
				logger.error("Error reading signature capture response from json", e);
			} 
		}
		
		List<String> errors = new ArrayList<String>();
		if (isRequired() && (val == null || val.getImageFileName() == null || val.getImageFileName().trim().length() == 0))
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
		
		if (val != null && val.getImageFileName() != null && val.getSignedBy() == null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Signed by required when signature is added for " + getIdentifier()
						+ " , throwing InvalidResponseException");
			}
			// denotes that there was no answer entered for this question
			// if the answer is required then, an error message should be throws
			// back to the page.
			Section sec = (Section) this.getParent();
			errors.add("Section:" + sec.getQuestionText() + ", Item:" + this.getQuestionText() + " - Signed by required when signature is added.");
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

		
		SignatureCaptureItemResponse val = null;
		if (sResponse != null)
		{
			SurveyItemResponse itemResponse = sResponse.getAnswer(this);
			if (itemResponse != null && itemResponse.getResponseUnits().size() > 0)
			{
				try
				{
					ResponseUnit aUnit = (ResponseUnit) itemResponse.getResponseUnits().get(0);
					String data = aUnit.getKey4();
					
					val = new ObjectMapper().readValue(data, SignatureCaptureItemResponse.class); 
				}
				catch (Exception e)
				{
					logger.error("Error reading signature capture response from json", e);
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

		if(val != null && val.getImageFileName() != null && val.getImageFileName().trim().length() > 0)
		{
			try
			{
				File iFile = FileStoreManager.getFile(val.getImageFileName());
				Image img = ImageIO.read(iFile);
				
				VerticalLayout questionArea = new VerticalLayout();
				questionArea.setMargin(true);
				qLayout.addComponent(questionArea);

				final StreamResource imageResource = new StreamResource(new PictureRenderer(val.getImageFileName()), val.getImageFileName());
				imageResource.setCacheTime(0);
				Embedded em = new Embedded(null, imageResource);
				
				if(img.getWidth(null) <= MAX_IMAGE_WIDTH)
				{
					em.setWidth(img.getWidth(null), com.vaadin.server.Sizeable.Unit.PIXELS);
					em.setHeight(img.getHeight(null), com.vaadin.server.Sizeable.Unit.PIXELS);
				}
				else
				{
					float scale = (float)MAX_IMAGE_WIDTH/(float)img.getWidth(null);
					em.setWidth(img.getWidth(null)*scale, com.vaadin.server.Sizeable.Unit.PIXELS);
					em.setHeight(img.getHeight(null)*scale, com.vaadin.server.Sizeable.Unit.PIXELS);
				}
				questionArea.addComponent(em);
				
				if(val.getSignatureTimestamp() != null && val.getSignedBy() != null)
				{
					TimeZone timeZone = TimeZone.getTimeZone(testProc.getWorkstationTimezoneId());
					questionArea.addComponent(new Label(String.format("Signed by : %s on %s", 
							val.getSignedBy(), DateFormatter.getInstance(timeZone).formatDateTime(val.getSignatureTimestamp()))));
				}
				
			}
			catch (IOException e)
			{
				logger.error("Error loading signature file: " + val);
				e.printStackTrace();
				EtestApplication.getInstance().showError("Error loading signature, Please contact support.");
			}

		}
		return p;
	}

	private String[] validate()
	{
		List msgs = new ArrayList();
		
		String signatureFileName = null;
		String signedBy = null;
		if(signatureField.getAttachments() != null && signatureField.getAttachments().size() > 0)
		{
			signatureFileName = signatureField.getAttachments().get(0).getFileName();
		}

		signedBy = signedByField.getValue();
		
		if (isRequired()
			&& (signatureFileName == null || signatureFileName.trim().length() == 0))
		{
		    if (logger.isDebugEnabled())
		    {
			logger.debug("Required for "
				+ getIdentifier()
				+ " is true and no answer was entered, throwing InvalidResponseException");
		    }
		    // denotes that there was no answer entered for this question
		    // if the answer is required then, an error message should be throws
		    // back to the page.
		    msgs.add(this.questionText + ">> Answer cannot be empty");
		}
		
		if (signatureFileName != null && (signedBy == null || signedBy.trim().length() == 0))
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Signed by required when signature is added for " + getIdentifier()
						+ " , throwing InvalidResponseException");
			}
		    msgs.add(this.questionText + ">> Signed by name cannot be empty");
		}

		return (msgs.size() == 0)?null:(String[])msgs.toArray(new String[msgs.size()]);

	}
	
	public class PictureRenderer extends StreamSource {
		String fileName;
		public PictureRenderer(String fileName)
		{
			this.fileName = fileName;
		}


		public InputStream getStream()
		{
			try
			{
				File iFile = FileStoreManager.getFile(fileName);
				return new FileInputStream(iFile);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	
	
	public static void main(String[] args)
	{
		long now = new Date().getTime();
		System.out.println(now);
		
		int t = (int) (now/1000);
		System.out.println(t);
		long tl = new Long(t).longValue() * 1000;
		System.out.println(tl);
		System.out.println(new Date(tl));
	}
	
}
