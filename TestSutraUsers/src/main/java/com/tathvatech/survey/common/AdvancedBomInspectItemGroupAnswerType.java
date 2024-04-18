/*
 * Created on Oct 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;


import com.tathvatech.common.common.DataTypes;
import com.tathvatech.user.utils.OptionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AdvancedBomInspectItemGroupAnswerType //extends SurveyItem implements BaseInspectionItemGroupAnswerType, SurveyDisplayItem,
		//LogicSubject
{
	private static final Logger logger					= LoggerFactory.getLogger(AdvancedBomInspectItemGroupAnswerType.class);
	private static final int 	DEFAULT_TABLE_PAGE_SIZE = 10;
	// if no maxlength is defined, set it to 1
	private static final int	DEFAULT_MAXLENGTH		= 255;

	public static final String	HEADING_ATTACH			= "Attach";
	public static final String	HEADING_COMMENTS		= "Comments";
	public static final String	HEADING_ITEMNO			= "Item No";
	public static final String	HEADING_DESCRIPTION		= "Description";
	public static final String	HEADING_LOCATION		= "Location";
	public static final String	HEADING_RESULT			= "Result";
	public static final String	HEADING_NOTAPPLICABLE	= "Not Applicable";
	public static final String	HEADING_EXPECTED		= "Expected Result";
	public static final String	HEADING_UNIT		= "Unit";
	public static final String	HEADING_DATATYPE		= "Is Numeric";
	public static final String	HEADING_ICONS		= "Icons";
	public static final String	HEADING_FLAGS		= "Flags";
	public static final String	HEADING_BREAK		= "Break";
	public static final String	HEADING_EDIT		= "Edit";
	public static final String	HEADING_ADD_ROW		= "Add Row";
	public static final String	HEADING_DELETE_ROW		= "Delete Row";
	public static final String	HEADING_CHECKBOX		= "CheckBox";
	

	private int					dataType				= DataTypes.DATATYPE_STRING;
	protected String			questionText;
	protected String			questionTextDescription	= "";
	protected OptionList customFields			= new OptionList();
	protected List<SurveyItem>	children				= new ArrayList<SurveyItem>();
	
	boolean showExpectedField = false;
	boolean showUnit = false;
	boolean showPassFail = false;
	boolean showComments = false;
	
	//Table table;

	/**
     *
     */
	public AdvancedBomInspectItemGroupAnswerType()
	{
		super();

	}
//commenting methods using vaadin components
	/**
	 * @param _survey
	 */
	/*public AdvancedBomInspectItemGroupAnswerType(SurveyDefinition _survey)
	{
		super(_survey);
	}

	public String getTypeName()
	{
		return "AdvancedBomInspectItemGroupAnswerType";
	}

	@Override
	public List getChildren()
	{
		return children;
	}

	@Override
	public void setChildren(List children)
	{
		this.children = children;

	}

	public boolean hasSomethingToDisplay()
	{
		return true;
	}

	*//**
	 * @return Returns the isRequired.
	 *//*
	public boolean isRequired()
	{
		return true;
	}

	public OptionList getCustomFields()
	{
		return customFields;
	}

	public void setCustomFields(OptionList customFields)
	{
		this.customFields = customFields;
	}

	public void setQuestionTextDescription(String questionTextDescription)
	{
		this.questionTextDescription = questionTextDescription;
	}

	*//**
	 * @return Returns the questionText.
	 *//*
	public String getQuestionText()
	{
		return questionText;
	}

	*//**
	 * @param questionText
	 *            The questionText to set.
	 *//*
	public void setQuestionText(String questionText)
	{
		this.questionText = questionText;
	}

	public String getQuestionTextDescription()
	{
		return this.questionTextDescription;
	}

	public boolean isShowExpectedField()
	{
		return showExpectedField;
	}

	public void setShowExpectedField(boolean showExpectedField)
	{
		this.showExpectedField = showExpectedField;
	}

	public boolean isShowUnit()
	{
		return showUnit;
	}

	public void setShowUnit(boolean showUnit)
	{
		this.showUnit = showUnit;
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

	public String getIdentifier()
	{
		return questionText;
	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.AnswerType#getDataType()
	 *//*
	public int getDataType()
	{
		return dataType;
	}

	public void setDataType(int dataType)
	{
		this.dataType = dataType;
	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#drawQuestionConfigurationForm()
	 *//*
	public String drawConfigurationForm() throws Exception
	{
		return "";
	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#saveConfiguration(java.util.Map)
	 *//*
	public void setConfiguration(Map paramMap) throws Exception
	{

	}

	@Override
	public void setConfiguration(List<List<String>> fileContents)
	{
		if(fileContents == null || fileContents.size() == 0 || fileContents.get(0) == null)
		{
			return;
		}
		List<String> qrow = fileContents.get(0);
		if(qrow.size() > 0)
		{
			this.questionText = qrow.get(0);
		}
		if(qrow.size() > 1)
		{
			this.questionTextDescription = qrow.get(1);
		}
		List<List<String>> mod = fileContents.subList(1, fileContents.size());
		setConfigurationInt(mod);
	}

	public void setConfigurationInt(List<List<String>> fileContents)
	{
		// analyse the heading the findout what all is required in the
		// table
		List<String> heading = fileContents.get(0);
		// identify the location of expected value so that we can find
		// the custom fields
		
		int expectedColIndex = -1;
		int unitColIndex = -1;
		int passFailColIndex = -1;
		int naColIndex = -1;
		int commentsColIndex = -1;
		int datatypeColIndex = -1;
		int flagsColIndex = -1;

		customFields = new OptionList();
		for (int i = 0; i < heading.size(); i++)
		{
			if(heading.get(i) == null || heading.get(i).trim().length() == 0)
				continue;
			String aHeading = heading.get(i).trim();
			if (HEADING_EXPECTED.equalsIgnoreCase(aHeading))
			{
				expectedColIndex = i;
			}
			else if (HEADING_UNIT.equalsIgnoreCase(aHeading))
			{
				unitColIndex = i;
			}
			else if (HEADING_RESULT.equalsIgnoreCase(aHeading))
			{
				passFailColIndex = i;
			}
			else if (HEADING_NOTAPPLICABLE.equalsIgnoreCase(aHeading))
			{
				naColIndex = i;
			}
			else if (HEADING_COMMENTS.equalsIgnoreCase(aHeading))
			{
				commentsColIndex = i;
			}
			else if (HEADING_DATATYPE.equalsIgnoreCase(aHeading))
			{
				datatypeColIndex = i;
			}
			else if (HEADING_FLAGS.equalsIgnoreCase(aHeading))
			{
				flagsColIndex = i;
			}
			//add the headings to the custom fields
			if(HEADING_FLAGS.equalsIgnoreCase(aHeading))
			{
			}
			else
			{
				customFields.add(new Option(aHeading, i));
			}
		}

		try 
		{
			//look at the columns and apply default column widths. Item no should have the smallest width, desc should have the biggest and rest all same
			int tableViewColCount = 0;
			boolean hasInstructionCol = false;
			for (Iterator iterator = customFields.iterator(); iterator.hasNext();) 
			{
				Option ao = (Option) iterator.next();
				if(!(HEADING_FLAGS.equals(ao.getText()))
						|| HEADING_FLAGS.equals(ao.getText())
						|| HEADING_UNIT.equals(ao.getText())
						|| HEADING_NOTAPPLICABLE.equals(ao.getText())
						|| HEADING_DATATYPE.equals(ao.getText())
						|| HEADING_ITEMNO.equals(ao.getText()) || (ao.getText() != null && ao.getText().startsWith(HEADING_ITEMNO))
						|| HEADING_DESCRIPTION.equals(ao.getText())
						)
				{
					tableViewColCount++;
				}
				if("Instruction".equalsIgnoreCase(ao.getText()) || "Instructions".equalsIgnoreCase(ao.getText()))
				{
					hasInstructionCol = true;
				}
			}
			int itemNoWidth = 5;
			int descWidith = 50;
			int instructionColWidth = 0;
			if(hasInstructionCol)
				instructionColWidth = 15;
			if(tableViewColCount > 6)
			{
				itemNoWidth = 7;
				descWidith = 40;
			}
			else if(tableViewColCount > 8)
			{
				itemNoWidth = 10;
				descWidith = 30;
			}
			if(tableViewColCount < 3) tableViewColCount = 3; // nothing special so that there are no div by 0 errors 
			int restForEach = (100-itemNoWidth - descWidith - instructionColWidth)/(tableViewColCount-2); // -2 because take out itemNo and desc col
			for (Iterator iterator = customFields.iterator(); iterator.hasNext();) 
			{
				Option ao = (Option) iterator.next();
				if(HEADING_ITEMNO.equals(ao.getText()) || (ao.getText() != null && ao.getText().startsWith(HEADING_ITEMNO)))
					ao.setTextDesc(itemNoWidth +"");
				else if(HEADING_DESCRIPTION.equals(ao.getText()))
					ao.setTextDesc("" + descWidith);
				else if("Instruction".equals(ao.getText()) || "Instructions".equals(ao.getText()))
					ao.setTextDesc("" + instructionColWidth);
				else
					ao.setTextDesc(restForEach+"");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}		
		
		children = new ArrayList<SurveyItem>();
		// the first row is the heading which is ignored
		for (int i = 1; i < fileContents.size(); i++)
		{
			List<String> row = fileContents.get(i);
//			if(row[0] == null || row[0].trim().length() == 0)
//			{
//				continue; // questionText which is the item number is a mandatory field. skip the rows if that is the case.
//			}

			AdvancedBomInspectItemAnswerType aChild = new AdvancedBomInspectItemAnswerType();
			try
			{
				if(row.get(expectedColIndex).trim().length() > 0)
				{
					aChild.setShowExpectedValue(true);
					showExpectedField = true;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if(row.get(unitColIndex).trim().length() > 0)
				{
					aChild.setShowUnit(true);
					showUnit = true;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if("y".equalsIgnoreCase(row.get(passFailColIndex).trim()))
				{
					aChild.setShowPassFail(true);
					showPassFail = true;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if("y".equalsIgnoreCase(row.get(naColIndex).trim()))
				{
					aChild.setShowNotApplicable(true);
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if("y".equalsIgnoreCase(row.get(commentsColIndex).trim()))
				{
					aChild.setShowComments(true);
					showComments = true;
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if("y".equalsIgnoreCase(row.get(datatypeColIndex).trim()))
				{
					aChild.setDataType(DataTypes.DATATYPE_INTEGER);
				}
			}
			catch (Exception e)
			{
			}
			try
			{
				if(row.get(flagsColIndex).trim().length() > 0)
				{
					aChild.setFlags(row.get(flagsColIndex).trim());
				}
			}
			catch (Exception e)
			{
			}
			
			OptionList childCustomFields = new OptionList();
			for (int k = 0; k < customFields.size(); k++)
			{
				Option aF = (Option) customFields.getOptionByIndex(k);
				if(row.size() > aF.getValue())
				{
					String rowVal = row.get(aF.getValue());
					Option aOption = new Option(aF.getText(), rowVal, 
							aF.getValue(), BomTypesEnum.fromToken(rowVal));
					childCustomFields.add(aOption);
				}
				else
				{
					Option aOption = new Option(aF.getText(), "", 
							aF.getValue());
					childCustomFields.add(aOption);
				}
			}
			aChild.setCustomFields(childCustomFields);

			children.add(aChild);
		}
		
		//if show pass/fail is true, comments should be true, that way if fail is selected, comments should be entered 
		if(showPassFail == true)
		{
			showComments = true;
		}
	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#setConfiguration(org.jdom.Element
	 * )
	 *//*
	public void setConfiguration(Element element)
	{
		super.setSurveyItemConfiguration(element);
		this.setQuestionText(element.getAttributeValue("text"));
		this.questionTextDescription = element.getAttributeValue("textDescription");

		this.setShowExpectedField(new Boolean(element.getAttributeValue("showExpectedField")).booleanValue());
		this.setShowUnit(new Boolean(element.getAttributeValue("showUnit")).booleanValue());
		this.setShowPassFail(new Boolean(element.getAttributeValue("showPassFail")).booleanValue());
		this.setShowComments(new Boolean(element.getAttributeValue("showComments")).booleanValue());

		Element cfGroup = element.getChild("customFields");
		if (cfGroup != null)
		{
			OptionList cfList = new OptionList();
			for (Iterator iter = cfGroup.getChildren("field").iterator(); iter.hasNext();)
			{
				Element fElement = (Element) iter.next();
//				Option aOption = new Option(fElement.getAttributeValue("text"),
//						fElement.getAttributeValue("textDesc"));
				Option aOption = new Option(fElement.getAttributeValue("text"),
						fElement.getAttributeValue("textDesc"));
				cfList.add(aOption);
			}
			this.customFields = cfList;
		}

	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.AnswerType#toXML(org.jdom.Element)
	 *//*
	public Element toXML()
	{
		// if (logger.isDebugEnabled())
		// {
		// logger.debug("Inside toXML -- adding options to element - value is "
		// + optionString);
		// }

		Element element = new Element("surveyItem");

		super.toXML(element);

		element.setAttribute("text", (questionText != null)?questionText:"");
		element.setAttribute("textDescription", (questionTextDescription != null)?questionTextDescription:"");

		element.setAttribute("showExpectedField", new Boolean(showExpectedField).toString());
		element.setAttribute("showUnit", new Boolean(showUnit).toString());
		element.setAttribute("showPassFail", new Boolean(showPassFail).toString());
		element.setAttribute("showComments", new Boolean(showComments).toString());

		if (customFields != null)
		{
			Element optGroup = new Element("customFields");
			for (Iterator iter = customFields.iterator(); iter.hasNext();)
			{
				Option aOption = (Option) iter.next();
				Element optionElement = new Element("field");
				optionElement.setAttribute("text", (aOption.getText() != null)?aOption.getText():"");
				optionElement.setAttribute("textDesc", (aOption.getTextDesc() != null)?aOption.getTextDesc():"");
				optGroup.addContent(optionElement);
			}
			element.addContent(optGroup);
		}

		return element;
	}

	*//*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.SurveyItem#getLogic(java.lang.String)
	 *//*
	public Logic getLogic(String logicId)
	{
		return super.getLogicFromList(logicId);
	}

	public Component drawResponseDetail(UserContext userContext, UnitFormQuery testProc, SurveyResponse sResponse, Component parent,
			boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController)
	{
	    TSTableContainer table = new TSTableContainer();
		table.setTitleText(questionText);
		table.setTitleDescription(questionTextDescription);
		
		List<String> vCols = new ArrayList<String>();

		// add the checkbox for selecting lineItems on a form detail view.
		table.addContainerProperty(HEADING_CHECKBOX, CheckBox.class, null);
		table.setColumnHeader(HEADING_CHECKBOX, "");
		table.setColumnWidth(HEADING_CHECKBOX, 30);
		vCols.add(HEADING_CHECKBOX);
		
		
		for (Iterator iterator = customFields.iterator(); iterator.hasNext();)
		{
			Option aField = (Option) iterator.next();
			String headingText = aField.getText();
			float colExpandRatio = 0;
			if(aField.getTextDesc() != null && aField.getTextDesc().trim().length() > 0)
			{
				try
				{
					colExpandRatio = Float.parseFloat(aField.getTextDesc().trim());
					colExpandRatio = (float) (colExpandRatio * 0.98); // leave 2 % for the icons
				}catch(Exception e){}
			}
			if (HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				if(showExpectedField)
				{
					table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
					vCols.add(aField.getText());
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					table.addContainerProperty(aField.getText(), String.class, null);
					vCols.add(aField.getText());
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_FLAGS.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					table.addContainerProperty(aField.getText(), Label.class, null);
					vCols.add(aField.getText());
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else
			{
				table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
				vCols.add(aField.getText());
				table.setColumnExpandRatio(aField.getText(), colExpandRatio);
			}
		}

		//icons
		table.addContainerProperty(HEADING_ICONS, AbstractLayout.class, null);
		vCols.add(HEADING_ICONS);
		table.setColumnHeader(HEADING_ICONS, "");
		table.setColumnWidth(HEADING_ICONS, 30);

		int childTableIndex = 0;
		for (int i = 0; i < children.size(); i++)
		{
			AdvancedBomInspectItemAnswerType aChild = (AdvancedBomInspectItemAnswerType) children.get(i);
			try 
			{
				boolean childDisplayed = aChild.drawResponseDetailInt(userContext, testProc, sResponse, table,
						childTableIndex, isLatestResponse, flags, testProcController);
				if (childDisplayed)
				{
					childTableIndex++;
				}
			} 
			catch (Throwable e) 
			{
				logger.error("Error rendering item - " + aChild.getSurveyItemId(), e);
				e.printStackTrace();
				throw e;
			}
		}

		if(table.getRowCount() == 0)
			return null;
		
		TSTable tableComponent = null;
		try 
		{
			tableComponent = new TSTable(table);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableComponent;
	}


	@Override
	public Component drawDesignView(boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		VerticalLayout vl = new VerticalLayout();
		vl.addStyleName(ValoTheme.LAYOUT_CARD);
		vl.addStyleName("inspection_group_type");
		
		if(isPreviewMode == false) //preview mode is displayed when configuring the form.. no need of the header, we are only showing the table.
		{
			VerticalLayout questionTitleArea = new VerticalLayout();
			questionTitleArea.addStyleName("question_title_area");
			vl.addComponent(questionTitleArea);
	
			Label heading = new Label(questionText);
			heading.addStyleName("question_heading");
			questionTitleArea.addComponent(heading);
	
			Label desc = new Label(questionTextDescription);
			desc.addStyleName("question_description");
			questionTitleArea.addComponent(desc);
		}
		
		table = new Table();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.setImmediate(true);
		table.setWidth("100%");
		if(this.children.size() > 5)
			table.setHeight("600px");
		else
			table.setPageLength(0);
//		int pageLength = (this.children.size() > DEFAULT_TABLE_PAGE_SIZE) ? DEFAULT_TABLE_PAGE_SIZE : this.children.size();
//		table.setPageLength(pageLength);
		for (Iterator iterator = customFields.iterator(); iterator.hasNext();)
		{
			Option aField = (Option) iterator.next();
			String headingText = aField.getText();
			float colExpandRatio = 1;
			if(aField.getTextDesc() != null && aField.getTextDesc().trim().length() > 0)
			{
				try
				{
					colExpandRatio = Float.parseFloat(aField.getTextDesc().trim());
				}catch(Exception e){}
			}
			
			if (HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				if(showExpectedField)
				{
					table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_FLAGS.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					table.addContainerProperty(aField.getText(), SingleSelectCheckboxGroup.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					table.addContainerProperty(aField.getText(), TextArea.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else
			{
				table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
				table.setColumnExpandRatio(aField.getText(), colExpandRatio);
			}
		}
		if(isPreviewMode == false)
		{
			table.addContainerProperty(HEADING_ADD_ROW, Button.class, null);
			table.setColumnHeader(HEADING_ADD_ROW, "");

			table.addContainerProperty(HEADING_DELETE_ROW, Button.class, null);
			table.setColumnHeader(HEADING_DELETE_ROW, "");

			table.addContainerProperty(HEADING_BREAK, Button.class, null);
			table.setColumnHeader(HEADING_BREAK, "");

			table.addContainerProperty(HEADING_EDIT, Button.class, null);
			table.setColumnHeader(HEADING_EDIT, "");
		}

		for (int i = 0; i < children.size(); i++)
		{
			final AdvancedBomInspectItemAnswerType aChild = (AdvancedBomInspectItemAnswerType) children.get(i);
			aChild.addItemToDesignViewInt(table, isPreviewMode, null, formDesignListener);
		}

		if(isPreviewMode == false)
		{
			vl.addComponent(table);
			return vl;
		}
		else
		{
			return table;
		}
		

	}
	
	public void addBomItemToView(BaseInspectionLineItemAnswerType aChild, boolean isPreviewMode, String addAfterItemId, FormDesignListener formDesignListener)
	{
		aChild.addItemToDesignViewInt(table, isPreviewMode, addAfterItemId, formDesignListener);
	}	
	
	public void updateBomItemInView(BaseInspectionLineItemAnswerType aChild, boolean isPreviewMode, FormDesignListener formDesignListener)
	{
		aChild.drawDesignViewInt(aChild.getSurveyItemId(), isPreviewMode, formDesignListener);
	}	
	

	private VerticalLayout getMultiLine(String data) {
		VerticalLayout vlayout = new VerticalLayout();
		// descVLayout.setWidth("200px");
		vlayout.setWidthUndefined();
		if (data == null){
			vlayout.addComponent(new Label(""));
			return vlayout;
		}
		String[] ary = data.split("\n");
		for (String s:ary){
			Label descField = new Label(s);
			descField.setContentMode(Label.CONTENT_PREFORMATTED);
			vlayout.addComponent(descField);
		}
		return vlayout;
	}
	@Override
	public Component drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	public class ConfigForm extends CustomComponent implements Upload.Receiver, Upload.SucceededListener,
			Upload.FailedListener
	{
		BeanFieldGroup<AdvancedBomInspectItemGroupAnswerType> fieldGroup;
		VerticalLayout content;
		
		FormDesignListener				formDesignListener;
		AdvancedBomInspectItemGroupAnswerType	surveyItem;
		File							file;
		List<List<String>>				fileContents;

		TSUpload						fileUpload;
		HorizontalLayout				buttonArea;

		Label messageLable = new Label();
		
		VerticalLayout				filePreviewArea;
		Table previewTable;

		public ConfigForm(AdvancedBomInspectItemGroupAnswerType surveyItem,
				FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			content = new VerticalLayout();
			content.setMargin(true);
			content.setSpacing(true);
			this.setCompositionRoot(content);
			
			this.setCaption("Please upload a .xls file");
			messageLable.addStyleName("red_label");
			
			BeanItem<AdvancedBomInspectItemGroupAnswerType> item = new BeanItem<AdvancedBomInspectItemGroupAnswerType>(
					surveyItem);

			fieldGroup = new BeanFieldGroup<AdvancedBomInspectItemGroupAnswerType>(AdvancedBomInspectItemGroupAnswerType.class);
			
			// We need an item data source before we create the fields to be able to
	        // find the properties, otherwise we have to specify them by hand
			fieldGroup.setItemDataSource(item);
			fieldGroup.setBuffered(true);

			List<String> displayFields = Arrays.asList( "questionText", "questionTextDescription" );
			
	    	for (Iterator iterator = displayFields.iterator(); iterator.hasNext();) 
	    	{
				String pid = (String) iterator.next();
				Field aField;
				if ("customFields".equals(pid))
				{
					TextArea area = new TextArea();
					aField = area;
				}
		        else
		        {
			        Field field = DefaultFieldFactory.get().createField(item,
							pid, this);
					if (field instanceof TextField)
					{
						((TextField) field).setNullRepresentation("");
					}
					aField = field;
		        }
		        content.addComponent(aField);
		        fieldGroup.bind(aField, pid);
	    	}
			
// TODO7			this.setWriteThrough(false);

			// set the project name as required
//			this.getField("questionText").setRequired(true);
			((TextField)fieldGroup.getField("questionText")).setColumns(40);
//			this.getField("questionText").setRequiredError("Question text cannot be empty");

			fieldGroup.getField("questionTextDescription").setCaption("Description");
			((TextField)fieldGroup.getField("questionTextDescription")).setColumns(40);

			fieldGroup.getField("questionText").focus();

			*//* add the file upload control *//*
			fileUpload = new TSUpload("Upload now", this);
			// fileUpload.setStyleName("upload");
			fileUpload.addStyleName("uploadBttn");
			fileUpload.addListener((Upload.SucceededListener) this);
			fileUpload.addListener((Upload.FailedListener) this);
			// fileUpload.setButtonCaption(null);
			content.addComponent(fileUpload);
		}
		
		public void attach()
		{
			super.attach();
			HorizontalLayout messageArea = new HorizontalLayout();
			content.addComponent(messageArea);
			messageArea.addComponent(messageLable);
			
			// file preview area
			filePreviewArea = new VerticalLayout();
			content.addComponent(filePreviewArea);
			filePreviewArea.setVisible(false);
			
			if(surveyItem != null && surveyItem.getCustomFields() != null && surveyItem.getCustomFields().size() > 0)
			{
				showItemPreview();
			}
				
			
			*//* Add buttons in the form. *//*
			buttonArea = new HorizontalLayout();
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
			if(surveyItem == null || surveyItem.getCustomFields() == null || surveyItem.getCustomFields().size() == 0)
			{// in the edit mode when changing the heading and column widths, the file would be null which is ok
				if(file == null || fileContents == null)
				{
					messageLable.setValue("File not uploaded, Please upload a file and try again");
					return;
				}
			}
			try
			{
				fieldGroup.commit();

				//configureTheItem();
				
				// set the column widths from the preview table
				if(previewTable != null)
				{
					int tableWidth = 0;
					String[] tabCols = previewTable.getColumnHeaders();
					List<String> tabColList = new ArrayList<String>();
					for (int i = 0; i < tabCols.length; i++)
					{
						int w = previewTable.getColumnWidth(tabCols[i]);
						tableWidth+=w;
						tabColList.add(tabCols[i]);
					}

					// if user does not touch the resize columns, they come back as -1 screwing up the layout.
					// so check for > 0
					if(tableWidth > 0)
					{
						for (Iterator iterator = surveyItem.getCustomFields().iterator(); iterator.hasNext();)
						{
							Option aField = (Option) iterator.next();
							if(tabColList.contains(aField.getText()))
							{
								float w = previewTable.getColumnWidth(aField.getText());
								float widthRatio = w*100 /tableWidth;
								
								aField.setTextDesc("" + widthRatio);
							}
							else
							{
								aField.setTextDesc(""+0);
							}
						}
					}
				}
				else
				{
					System.out.println("preview table does not exist.. creating is happenning");
				}
				
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

		// Callback method to begin receiving the upload.
		public OutputStream receiveUpload(String filename, String MIMEType)
		{
			try
			{
				Context ctx = new InitialContext();
				// reset the existing fileContents
				this.fileContents = null;

				FileOutputStream fos = null; // Output stream to write to
				file = FileStoreManager.getFile(filename);
				if (!(file.getAbsolutePath().toUpperCase().endsWith("XLS") || file.getAbsolutePath().toUpperCase().endsWith("XLSX")))
				{
					throw new FileFormatUnsupported("Only xls and xlsx formats are supported.");
				}
				// Open the file for writing.
				fos = new FileOutputStream(file);
				
				return fos; // Return the output stream to write to
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
		}

		// This is called if the upload is finished.
		public void uploadSucceeded(Upload.SucceededEvent event)
		{
			try
			{
				if(file == null)
				{
					throw new Exception("File was not uploaded, Please try again");
				}
				InputStream is = new FileInputStream(file);
				this.fileContents = new POIExcelParser(is).getFileContents();

				if (fileContents.size() == 0)
				{
					throw new ZeroLengthFile();
				}
				
				configureTheItem();
				showItemPreview();
				
				
				fileUpload.setMessage("");
				fileUpload.setComponentError(null);
				messageLable.setValue("");
			}
			catch(org.apache.poi.hssf.OldExcelFormatException ox)
			{
				messageLable.setValue("Unsupported file format, For .xls file make sure you use Excel versions 97/2000/XP/2003 and above");
			}
			catch (ZeroLengthFile e) {
				messageLable.setValue("File could not be uploaded, Please try again later");
			}
			catch (Exception e) {
				e.printStackTrace();
				messageLable.setValue("Unexpected error while reading the file, Please try again later");
			}
			
		}

		private void configureTheItem()
		{
			setConfigurationInt(fileContents);
		}
		

		private void showItemPreview()
		{
			filePreviewArea.removeAllComponents();
			Label l = new Label("Adjust the widths of the table columns by dragging the column heading seperators.");
			Label l1 = new Label("Please note that the measurements are relative since the width of the screen and the width of the prints would be different.");
			l.addStyleName("bold_label");
			l1.addStyleName("bold_label");
			filePreviewArea.addComponent(l);
			filePreviewArea.addComponent(l);

			Embedded ruler = new Embedded(null, new ThemeResource("Images/ruler.gif"));
			filePreviewArea.addComponent(ruler);
			
			previewTable = (Table)surveyItem.drawDesignView(true, formDesignListener);
			previewTable.setPageLength(5);
			previewTable.setWidth("100%");
			filePreviewArea.addComponent(previewTable);
			filePreviewArea.setVisible(true);

//			Window w = EtestApplication.getEnclosingWindow(filePreviewArea); 
//			w.setWidth("80%");
//			w.setPositionX(10);
		}
		
		// This is called if the upload fails.
		public void uploadFailed(Upload.FailedEvent event)
		{
			fileUpload.setMessage(event.getReason().getMessage());
		}
	}

	@Override
	public Component drawResponseField(UnitFormQuery testProc, SurveyResponse sResponse, Component parent, String[] flags,
			FormEventListner formEventListner)
	{
		TSTableContainer table = new TSTableContainer();
		table.setTitleText(questionText);
		table.setTitleDescription(questionTextDescription);
		
		for (Iterator iterator = customFields.iterator(); iterator.hasNext();)
		{
			Option aField = (Option) iterator.next();
			String headingText = aField.getText();
			float colExpandRatio = 0;
			if(aField.getTextDesc() != null && aField.getTextDesc().trim().length() > 0)
			{
				try
				{
					colExpandRatio = Float.parseFloat(aField.getTextDesc().trim());
					colExpandRatio = (float) (colExpandRatio * 0.98); // leave 2 % for the icons
				}catch(Exception e){}
			}
			if (HEADING_EXPECTED.equalsIgnoreCase(headingText))
			{
				if(showExpectedField)
				{
					table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_UNIT.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_RESULT.equalsIgnoreCase(headingText))
			{
				if(showPassFail)
				{
					table.addContainerProperty(aField.getText(), SingleSelectCheckboxGroup.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else if (HEADING_NOTAPPLICABLE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_DATATYPE.equalsIgnoreCase(headingText))
			{
				// no column for this one
			}
			else if (HEADING_COMMENTS.equalsIgnoreCase(headingText))
			{
				if(showComments)
				{
					table.addContainerProperty(aField.getText(), ExpandableTextArea.class, null);
					table.setColumnExpandRatio(aField.getText(), colExpandRatio);
				}
			}
			else
			{
				table.addContainerProperty(aField.getText(), AbstractLayout.class, null);
				table.setColumnExpandRatio(aField.getText(), colExpandRatio);
			}
		}
		table.addContainerProperty(HEADING_ATTACH, AbstractLayout.class, null);
		table.setColumnHeader(HEADING_ATTACH, "");
		table.setColumnWidth(HEADING_ATTACH, 30);
		table.setColumnAlignment(HEADING_ATTACH, Table.Align.CENTER);

		for (int i = 0; i < children.size(); i++)
		{
			AdvancedBomInspectItemAnswerType aChild = (AdvancedBomInspectItemAnswerType) children.get(i);
			try
			{
				aChild.drawResponseFieldInt(testProc, sResponse, table, i, flags, formEventListner);
			}
			catch (Throwable e) 
			{
				logger.error("Error rendering item - " + aChild.getSurveyItemId(), e);
				e.printStackTrace();
				throw e;
			}
		}
		if(table.getRowCount() == 0)
			return null;
		
		TSTable tableComponent = null;
		try 
		{
			tableComponent = new TSTable(table);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableComponent;
	}

	@Override
	public AbstractOrderedLayout getChildrenLayoutContainer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractOrderedLayout getManageChildrenControlsLayoutArea()
	{
		return null;
	}
	*/
}
