/*
 * Created on Jan 1, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.sarvasutra.etest.EtestApplication;
import com.sarvasutra.etest.FormDesignListener;
import com.sarvasutra.etest.FormEventListner;
import com.sarvasutra.etest.TestProcController;
import com.sarvasutra.etest.components.ExpandPanel;
import com.sarvasutra.etest.components.FileUploadFormMulti;
import com.sarvasutra.etest.components.FileUploadFormMulti.FileUploadListener;
import com.sarvasutra.etest.components.LockUnlockExpandPanel;
import com.sarvasutra.etest.components.SectionDetailViewExpandPanel;
import com.sarvasutra.etest.pdfprint.PdfPrinter;
import com.tathvatech.testsutra.timetrack.reports.workorderlistreport.WorkorderListReportFilter;
import com.tathvatech.testsutra.timetrack.service.Workorder;
import com.tathvatech.testsutra.timetrack.service.WorkorderDelegate;
import com.tathvatech.testsutra.timetrack.web.WorkOrderListView;
import com.tathvatech.ts.core.SecurityContext;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.accounts.delegate.AccountDelegate;
import com.tathvatech.ts.core.common.Attachment;
import com.tathvatech.ts.core.common.FileStoreManager;
import com.tathvatech.ts.core.common.ReworkOrderOID;
import com.tathvatech.ts.core.common.utils.AttachmentIntf;
import com.tathvatech.ts.core.common.utils.LineSeperatorUtil;
import com.tathvatech.ts.core.project.ProjectOID;
import com.tathvatech.ts.core.project.TestProcSectionObj;
import com.tathvatech.ts.core.project.UnitFormQuery;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.tathvatech.ts.core.survey.ObjectLock;
import com.tathvatech.ts.core.survey.ObjectLockQuery;
import com.tathvatech.ts.core.survey.SurveyDefinition;
import com.tathvatech.ts.core.survey.response.SurveyResponse;
import com.tathvatech.ts.core.survey.surveyitem.SectionBase;
import com.thirdi.surveyside.project.ProjectDelegate;
import com.thirdi.surveyside.project.TestProcManager;
import com.thirdi.surveyside.security.PlanSecurityManager;
import com.thirdi.surveyside.survey.Container;
import com.thirdi.surveyside.survey.FormDBManager;
import com.thirdi.surveyside.survey.FormSection;
import com.thirdi.surveyside.survey.SurveyDisplayItem;
import com.thirdi.surveyside.survey.SurveyItem;
import com.thirdi.surveyside.survey.delegate.SurveyDelegate;
import com.thirdi.surveyside.survey.logic.Logic;
import com.thirdi.surveyside.survey.response.SectionResponseQuery;
import com.thirdi.surveyside.survey.response.delegate.SurveyResponseDelegate;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Hari
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Section extends SurveyItem implements SectionBase, SurveyDisplayItem, Container
{
	private String				pageTitle;
	private String 				description;
    String instructionFileName;
    String instructionFileDisplayName;
	
	private List<SurveyItem>	children	= new ArrayList<SurveyItem>();
	
	LockUnlockExpandPanel exp = null;
	SectionDetailViewExpandPanel expp = null;
	
	VerticalLayout childLayoutContainer;
	HorizontalLayout manageChildrenControlLayoutArea;
	
	public Section()
	{
		super();
	}

	/**
	 * @param _survey
	 */
	public Section(SurveyDefinition _survey)
	{
		super(_survey);
		// TODO Auto-generated constructor stub
	}

	public String getQuestionText()
	{
		return pageTitle;
	}

	public String getQuestionTextDescription()
	{
		return description;
	}

	public String getInstructionFileName()
	{
		return instructionFileName;
	}

	public void setInstructionFileName(String instructionFileName)
	{
		this.instructionFileName = instructionFileName;
	}

	public String getInstructionFileDisplayName()
	{
		return instructionFileDisplayName;
	}

	public void setInstructionFileDisplayName(String instructionFileDisplayName)
	{
		this.instructionFileDisplayName = instructionFileDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.SurveyItem#getTypeName()
	 */
	public String getTypeName()
	{
		return "Section";
	}

	public String getIdentifier()
	{
		return pageTitle;
	}

	public String getShortIdentifier()
	{
		String identifier = getIdentifier();
		if (identifier != null && identifier.length() > 80)
		{
			identifier = identifier.substring(0, 77);
			identifier = identifier + "...";
		} else
		{
			identifier = "Section (No title)";
		}

		return identifier;
	}

	public boolean hasSomethingToDisplay()
	{
		return true;
	}

	/**
	 * @return Returns the pageTitle.
	 */
	public String getPageTitle()
	{
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            The pageTitle to set.
	 */
	public void setPageTitle(String pageTitle)
	{
		this.pageTitle = pageTitle;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#drawQuestionConfigurationForm()
	 */
	public Component drawConfigurationView(FormDesignListener formDesignListener)
	{
		return new ConfigForm(this, formDesignListener);
	}

	@Override
	public Component drawDesignView(boolean isPreviewMode, final FormDesignListener formDesignListener)
	{
		ExpandPanel p = new ExpandPanel(new ExpandPanel.ExpandCollapseListener() {
			
			@Override
			public void expaned()
			{
				formDesignListener.itemExpanded();
			}
			
			@Override
			public void collapsed()
			{
				formDesignListener.itemCollapsed();
			}
		});
		String title = pageTitle + ((description!=null)?(" - " + description):"");
		p.setCaption(title);
//		p.setDescription(description);
		
		VerticalLayout qLayout = new VerticalLayout();
		p.setBodyContent(qLayout);
		
		childLayoutContainer = new VerticalLayout();
		childLayoutContainer.addStyleName("section_child_container");
		childLayoutContainer.setWidth("100%");
		childLayoutContainer.setSpacing(true);
		childLayoutContainer.setMargin(true);
		qLayout.addComponent(childLayoutContainer);
		
		manageChildrenControlLayoutArea = new HorizontalLayout();
		manageChildrenControlLayoutArea.setWidth("100%");
		manageChildrenControlLayoutArea.setSpacing(true);
		p.setFooterContent(manageChildrenControlLayoutArea);
		return p;
	}
	
	public AbstractOrderedLayout getChildrenLayoutContainer()
	{
		return childLayoutContainer;
	}

	public AbstractOrderedLayout getManageChildrenControlsLayoutArea()
	{
		return manageChildrenControlLayoutArea;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.SurveyItem#setConfiguration(java.util.Map)
	 */
	public void setConfiguration(Map paramMap)
	{
		String[] tVal = (String[]) paramMap.get("pageTitle");
		if (tVal != null)
		{
			pageTitle = LineSeperatorUtil.changeSystemLineSeperatorToBR(tVal[0]);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.SurveyItem#toXML()
	 */
	public Element toXML()
	{
		Element element = new Element("surveyItem");

		super.toXML(element);

		if (pageTitle != null)
		{
			element.setAttribute("pageTitle", pageTitle);
		}
		if (description != null)
		{
			element.setAttribute("description", description);
		}
		if (instructionFileName != null)
		{
			element.setAttribute("instructionFileName", instructionFileName);
		}
		if (instructionFileDisplayName != null)
		{
			element.setAttribute("instructionFileDisplayName", instructionFileDisplayName);
		}
		return element;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.AnswerType#drawQuestionConfigurationForm()
	 */
	public String drawConfigurationForm()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n<!-- PageBreak.drawConfigurationForm() begin -->\n");

		String pTitle = "";
		if (pageTitle != null)
		{
			pTitle = pageTitle;
		}
		sb.append("<table class=\"SurveyItemConfig\">\n");
		sb.append("<tr>");
		sb.append("<td class=\"FormLabel\" nowrap=\"nowrap\">Page Title:</td>");
		sb.append("<td>");
		sb.append("<textarea  class=\"FormItem\" ");
		sb.append("name=\"pageTitle\" ");
		sb.append("rows=\"3\" ");
		sb.append("cols=\"80\" ");
		// sb.append("maxlength=\"256\" ");
		// sb.append("id=\"required\" >");
		sb.append(" >");
		sb.append(LineSeperatorUtil.changeBRToSystemLineSeperator(pTitle));
		sb.append("</textarea>\n");
		sb.append("<br>\n");
		sb.append("<span class='FormText'> You may leave the title empty if you want to start a new page in the survey without a page title </span>\n");
		sb.append("</td>");
		sb.append("</tr>");

		sb.append("</table>\n");

		sb.append("\n<!-- PageBreak.drawConfigurationForm() end -->\n");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thirdi.surveyside.survey.SurveyItem#getLogic(java.lang.String)
	 */
	public Logic getLogic(String logicId)
	{
		// nothing to be done here
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thirdi.surveyside.survey.SurveyItem#setConfiguration(org.jdom.Element
	 * )
	 */
	public void setConfiguration(Element element)
	{
		super.setSurveyItemConfiguration(element);
		this.setPageTitle(element.getAttributeValue("pageTitle"));
		this.description = element.getAttributeValue("description");
		this.instructionFileName = element.getAttributeValue("instructionFileName");
		this.instructionFileDisplayName = element.getAttributeValue("instructionFileDisplayName");
	}
	
	public void setConfiguration(List<List<String>> fileContents)
	{
		try
		{
			this.pageTitle = fileContents.get(0).get(0);
			this.description = fileContents.get(0).get(1);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public Component drawResponseField(UnitFormQuery testProc, final SurveyResponse sResponse, Component parent, String[] flags,
			FormEventListner formEventListner)
	{
		SectionResponseQuery srq = null;
		try
		{
			if(sResponse != null)
				srq = SurveyResponseDelegate.getSectionResponseSummary(sResponse.getSurveyDefinition(), sResponse.getResponseId(), getSurveyItemId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		exp = new LockUnlockExpandPanel(new LockUnlockExpandPanel.LockUnlockListener() 
		{
			@Override
			public boolean unlock()
			{
				try
				{
					SurveyDelegate.releaseSectionEditLock(EtestApplication.getInstance().getUserContext(), getFormResponseContext().getResponseMaster().getOID(), 
							Section.this.getSurveyItemId());

					exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_UNLOCKED);
					exp.setLockedByPk(0);
					exp.setLockedByName(null);

					return true;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			
			@Override
			public boolean lock()
			{
				try
				{
					ObjectLock l = SurveyDelegate.lockSectionToEdit(EtestApplication.getInstance().getUserContext(),
							getFormResponseContext().getUser(), 
							getFormResponseContext().getResponseMaster().getOID(), 
							Section.this.getSurveyItemId());
					
					exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYME);
					exp.setLockedByPk(l.getUserPk());
					exp.setLockedByName("Self");

					return true;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		});
		
		final SectionResponseQuery srqq = srq;	
		Button printSectionButton = new Button("", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					UnitFormQuery testProcQ = null;
					if(getFormResponseContext().getTestProc() != null)
						testProcQ = TestProcManager.getTestProcQuery(getFormResponseContext().getTestProc().getPk());
					
					final InputStream pdfStream =  new PdfPrinter(EtestApplication.getInstance().getUserContext(), PdfPrinter.OPTION_PRINT_RESPONSE)
							.printSection(EtestApplication.getInstance().getUserContext(), getFormResponseContext().getProjectQuery(), 
							getFormResponseContext().getUnitQuery(), getFormResponseContext().getFormQuery(), 
							Section.this, testProcQ, getFormResponseContext().getResponseMaster(), sResponse, srqq, false);
					StreamResource streamResource = new StreamResource(	new StreamResource.StreamSource() {

						@Override
						public InputStream getStream() {
							return pdfStream;
							}
					}, getFormResponseContext().getFormQuery().getIdentityNumber() + "_" + new java.util.Date().getTime() + ".pdf");
					Page.getCurrent().open(streamResource, "Form", true);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		printSectionButton.setStyleName(BaseTheme.BUTTON_LINK);
		printSectionButton.setIcon(new ThemeResource("Images/print-medium.png"));
		printSectionButton.setDescription("Print Section");
		
		exp.addAdditionalButtons(printSectionButton);
		
		
		if(instructionFileName != null)
		{
			Button instructionButton = new Button(FontAwesome.PAPERCLIP);
			instructionButton.addClickListener(new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					FileResource streamResource = new FileResource(
							FileStoreManager.getFile(instructionFileName));
					Page.getCurrent().open(streamResource, "File", true);
				}
			});
			instructionButton.setDescription("Instruction: " + instructionFileDisplayName);
			instructionButton.addStyleName(ValoTheme.BUTTON_BORDERLESS + " " + ValoTheme.BUTTON_SMALL);
			exp.addAdditionalButtons(instructionButton);
		}
		
		try
		{
			boolean isUserInRole = ProjectDelegate.isUsersForUnitInRole(new ProjectOID(getFormResponseContext().getUnitQuery().getProjectPk(), null), 
					getFormResponseContext().getUser().getPk(), getFormResponseContext().getUnitQuery().getUnitPk(),
					getFormResponseContext().getWorkstationQuery().getOID(), User.ROLE_TESTER);
			if(isUserInRole || User.USER_PRIMARY.equals(EtestApplication.getInstance().getUserContext().getUser().getUserType()))
			{
				ObjectLockQuery lock = SurveyDelegate.getCurrentLock(getFormResponseContext().getResponseMaster().getOID(), 
						Section.this.getSurveyItemId());
			
				if(lock == null)
				{
					//i should be able to lock the section.. show provision to lock.
					exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_UNLOCKED);
				}
				else if(lock.getUserPk() == getFormResponseContext().getUser().getPk())
				{
					//locked my me.. 
					exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYME);
					exp.setLockedByPk(lock.getUserPk());
					exp.setLockedByName("Self");
				}
				else
				{
					//show locked by other user..
					exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYOTHER);
					exp.setLockedByPk(lock.getUserPk());
					User lockedUser = AccountDelegate.getUser(lock.getUserPk());
					exp.setLockedByName(lockedUser.getDisplayString());
				}
			}
			else
			{
				exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_NOACCESS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYOTHER);
		}
		
		exp.addStyleName("section");
		
		String title = pageTitle + ((description!=null)?(" - " + description):"");
		exp.setCaption(title);
//		exp.setDescription(description);
		if(srq != null)
		{
			exp.setStartDate(srq.getStartDate());
			exp.setCompletionDate(srq.getCompletionDate());
			exp.setPercentComplete(srq.getPercentComplete());
			exp.setNoOfComments(srq.getNoOfComments());
		}
		
		childLayoutContainer = new VerticalLayout();
		childLayoutContainer.addStyleName("section_child_container");
		childLayoutContainer.setWidth("100%");
		childLayoutContainer.setSpacing(true);
		
		exp.setBodyContent(childLayoutContainer);

		return exp;
	}
	
	public void updateSectionStatusSummary(SectionResponseQuery sectionResponse)
	{
		if(exp != null)
		{
			exp.setStartDate(sectionResponse.getStartDate());
			exp.setCompletionDate(sectionResponse.getCompletionDate());
			exp.setPercentComplete(sectionResponse.getPercentComplete());
			exp.setNoOfComments(sectionResponse.getNoOfComments());
		}
	}
	
	@Override
	public Component drawResponseDetail(UserContext userContext, final UnitFormQuery testProc, SurveyResponse sResponse, Component parent, 
			boolean expandedView, boolean isLatestResponse, String[] flags, final TestProcController testProcController)
	{
		SectionResponseQuery srq = null;
		try
		{
			if(sResponse != null)
			{
				srq = SurveyResponseDelegate.getSectionResponseSummary(sResponse.getSurveyDefinition(), sResponse.getResponseId(), getSurveyItemId());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		expp = new SectionDetailViewExpandPanel(new SectionDetailViewExpandPanel.LockUnlockListener() 
		{
			@Override
			public boolean unlock()
			{
				try
				{
					SurveyDelegate.releaseSectionEditLock(EtestApplication.getInstance().getUserContext(), getFormResponseContext().getResponseMaster().getOID(), 
							Section.this.getSurveyItemId());

					expp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_UNLOCKED);
					expp.setLockedByPk(0);
					expp.setLockedByName(null);

					return true;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			
			@Override
			public boolean lock()
			{
				return true;
			}
		});
		
		final SectionResponseQuery srqq = srq;	
		Button printSectionButton = new Button(FontAwesome.PRINT);
		printSectionButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					UnitFormQuery testProcQ = null;
					if(getFormResponseContext().getTestProc() != null)
						testProcQ = TestProcManager.getTestProcQuery(getFormResponseContext().getTestProc().getPk());
					
					PdfPrinter pdfPrinter = new PdfPrinter(EtestApplication.getInstance().getUserContext(), PdfPrinter.OPTION_PRINT_RESPONSE);
					final InputStream pdfStream =  pdfPrinter.printSection(EtestApplication.getInstance().getUserContext(), getFormResponseContext().getProjectQuery(), 
							getFormResponseContext().getUnitQuery(), getFormResponseContext().getFormQuery(), 
							Section.this, testProcQ, getFormResponseContext().getResponseMaster(), 
							getFormResponseContext().getSurveyResponse(), srqq, false);
					StreamResource streamResource = new StreamResource(	new StreamResource.StreamSource() {

						@Override
						public InputStream getStream() {
							return pdfStream;
							}
					}, getFormResponseContext().getFormQuery().getIdentityNumber() + "_" + new java.util.Date().getTime() + ".pdf");
					Page.getCurrent().open(streamResource, "Form", true);
					pdfPrinter.close();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		printSectionButton.setStyleName(ValoTheme.BUTTON_QUIET);
		printSectionButton.setDescription("Print Section");
		expp.addAdditionalButtons(printSectionButton);
		
		if(User.USER_PRIMARY.equals(EtestApplication.getInstance().getUserContext().getUser().getUserType()))
		{
			Button timeLogButton = new Button(FontAwesome.CLOCK_O);
			timeLogButton.setStyleName(ValoTheme.BUTTON_QUIET);
			timeLogButton.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event)
				{
					FormSection formSection = new FormDBManager().getFormSection(getSurveyItemId(), testProc.getFormPk());
					TestProcSectionObj testprocSection = TestProcManager.getTestProcSection(testProc.getOID(), formSection.getOID());
					Workorder wo = WorkorderDelegate.getWorkorderForEntity(testprocSection.getOID());
					if(wo != null)
					{
						WorkorderListReportFilter woFilter = new WorkorderListReportFilter();
						woFilter.setProjectOID(new ProjectOID(testProc.getProjectPk()));
						woFilter.setWorkstationOID(new WorkstationOID(testProc.getWorkstationPk()));
						woFilter.setWorkorderOIDs(Arrays.asList(new ReworkOrderOID[] {wo.getOID()}));
						EtestApplication.getInstance().showPanel(new WorkOrderListView(woFilter, EtestApplication.getInstance().getUserContext().getTimeZone(), EtestApplication.getInstance().getCurrentPanel()));
						
//						WorkOrderTimeEntryForm timeEntryForm = new WorkOrderTimeEntryForm(EtestApplication.getInstance().getUserContext().getTimeZone(), wo.getOID());
					}
					else
					{
						EtestApplication.getInstance().showWarning("Workorder is not activated yet, Lock the section to activate the workorder.");
					}
					
				}
			});
			expp.addAdditionalButtons(timeLogButton);
		}

		if(expandedView)
		{
			expp.expandView();
		}
		else
		{
			expp.collapseView();
		}
    	
		if(isLatestResponse && userContext.getSecurityManager().checkAccess(PlanSecurityManager.FORM_MANAGE_SECTION_LOCKS, 
				new SecurityContext(getFormResponseContext().getProjectQuery().getOID(), null, null, null, null)))
    	{
			expp.setShowUnlockButton(true);
			try
			{
				ObjectLockQuery lock = null;

				if(getFormResponseContext().getResponseMaster() != null)
				{
					lock = SurveyDelegate.getCurrentLock(getFormResponseContext().getResponseMaster().getOID(), 
							Section.this.getSurveyItemId());
				}
				if(lock == null)
				{
					expp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_UNLOCKED);
				}
				else if(lock.getUserPk() == getFormResponseContext().getUser().getPk())
				{
					//locked my me.. 
					expp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYME);
					expp.setLockedByPk(lock.getUserPk());
					expp.setLockedByName("Self");
				}
				else
				{
					//show locked by other user..
					expp.setLockStatus(LockUnlockExpandPanel.LOCKSTATUS_LOCKED_BYOTHER);
					expp.setLockedByPk(lock.getUserPk());
					User lockedUser = AccountDelegate.getUser(lock.getUserPk());
					expp.setLockedByName(lockedUser.getDisplayString());
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		
		
		String title = pageTitle + ((description!=null)?(" - " + description):"");
		expp.setCaption(title);
//		expp.setDescription(description);
		if(srq != null)
		{
			expp.setPercentComplete(srq.getPercentComplete());
			expp.setNoOfComments(srq.getNoOfComments());
			expp.setStartDate(srq.getStartDate());
			expp.setCompletionDate(srq.getCompletionDate());
		}
		
		childLayoutContainer = new VerticalLayout();
		childLayoutContainer.addStyleName("section_child_container");
		childLayoutContainer.setWidth("100%");
		childLayoutContainer.setSpacing(true);
		
		expp.setBodyContent(childLayoutContainer);

		return expp;
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

	public class ConfigForm extends CustomComponent
	{
		FormDesignListener	formDesignListener;
		Section				surveyItem;
		FileUploadFormMulti fileUpload;

		BeanFieldGroup<Section> fieldGroup;
		FormLayout content;

		public ConfigForm(Section surveyItem, FormDesignListener formDesignListener)
		{
			this.surveyItem = surveyItem;
			this.formDesignListener = formDesignListener;

			content = new FormLayout();
			content.setMargin(true);
			this.setCompositionRoot(content);
			
			BeanItem<Section> item = new BeanItem<Section>(surveyItem);

			fieldGroup = new BeanFieldGroup<Section>(Section.class);
			
			// We need an item data source before we create the fields to be able to
	        // find the properties, otherwise we have to specify them by hand
			fieldGroup.setItemDataSource(item);
			fieldGroup.setBuffered(true);

			List<String> displayFields = Arrays.asList( "pageTitle", "description" );
			
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

			fieldGroup.getField("pageTitle").setCaption("Section Title");
			fieldGroup.getField("pageTitle").setRequired(true);
			fieldGroup.getField("pageTitle").setRequiredError("Section Title cannot be left blank");
			fieldGroup.getField("pageTitle").focus();
			((TextField)fieldGroup.getField("pageTitle")).setMaxLength(8);
			((TextField)fieldGroup.getField("pageTitle")).setColumns(20);

			fieldGroup.getField("description").setRequired(true);
			fieldGroup.getField("description").setRequiredError("Description cannot be left blank");
			((TextField)fieldGroup.getField("description")).setColumns(40);
			((TextField)fieldGroup.getField("description")).setMaxLength(80);

			List<Attachment> instruction = new ArrayList<Attachment>();
			if(instructionFileName != null)
			{
				File instructionFile = FileStoreManager.getFile(instructionFileName);
				Attachment att = new Attachment();
				att.setFileDisplayName(instructionFileDisplayName);
				att.setFileName(instructionFileName);
				instruction.add(att);
			}
			
			fileUpload = new FileUploadFormMulti("Instruction", "pdf", instruction, false, true, false, true, new FileUploadListener() {
				
				@Override
				public void uploadComplete(List<AttachmentIntf> attachedFiles)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void uploadCancelled()
				{
					// TODO Auto-generated method stub
					
				}
			});
			content.addComponent(fileUpload);

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
				if(fileUpload.getAttachments() != null && fileUpload.getAttachments().size() > 0)
				{
					Attachment att = (Attachment) fileUpload.getAttachments().get(0);
					surveyItem.setInstructionFileDisplayName(att.getFileDisplayName());
					surveyItem.setInstructionFileName(att.getFileName());
				}
				else
				{
					surveyItem.setInstructionFileDisplayName(null);
					surveyItem.setInstructionFileName(null);
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
	}

}
