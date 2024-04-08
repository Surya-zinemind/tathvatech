/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.utils.SequenceIdGenerator;
import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.forms.enums.FormTypeEnum;
import com.tathvatech.forms.intf.SectionBase;
import com.tathvatech.logic.common.Logic;
import com.tathvatech.survey.common.SurveyDefinition;

import com.tathvatech.survey.common.SurveyItem;
import com.tathvatech.survey.common.SurveyItemOrderComparator;
import com.tathvatech.survey.common.SurveySaveItem;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.exception.SurveyNotEditableException;
import com.tathvatech.survey.inf.SurveyItemBase;
import com.tathvatech.survey.inf.SurveyParamBase;
import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.service.AccountServiceImpl;

import lombok.RequiredArgsConstructor;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.Document;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@RequiredArgsConstructor
public class SurveyDefinitionManager extends Object
{
    private static final Logger logger = LoggerFactory.getLogger(SurveyDefinitionManager.class);

    private Survey survey;
    private SurveyDefinition surveyDefinition;
    private SequenceIdGenerator sequenceIdGenerator;
    private  SurveyDefFactory surveyDefFactory;
    private  SurveyMaster surveyMaster;

    /**
     * @param survey
     */
    public SurveyDefinitionManager(Survey survey, SurveyDefFactory surveyDefFactory)throws Exception
    {
        this.survey = survey;
        this.surveyDefinition = surveyDefFactory.getSurveyDefinition(new FormOID((int) survey.getPk(), survey.getIdentityNumber()));
        this.surveyDefFactory = surveyDefFactory;
    }
    
    public SurveyDefinitionManager(FormOID formOID, SurveyDefFactory surveyDefFactory)throws Exception
    {
        this.survey = surveyMaster.getSurveyByPk((int) formOID.getPk());
        this.surveyDefinition = surveyDefFactory.getSurveyDefinition(formOID);
        this.surveyDefFactory = surveyDefFactory;
    }

    public SurveyDefinitionManager(Survey survey) {
    }


    public Survey getSurvey()
	{
		return survey;
	}



	public SurveyDefinition getSurveyDefinition()
	{
		return surveyDefinition;
	}



	public SurveyItem addQuestion(SurveyItem sItem, SurveyItem parent, String previoisQuestionId)throws Exception
    {
		if(parent != null && (!(parent instanceof Container)))
		{
			throw new Exception("Form items can be added only to a Section.");
		}
		
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

		Thread.sleep(2); // just to make sure that a unique id is created
        String seq = new Long(new Date().getTime()).toString();
        String idString = "SurveyItem_" + seq;

        //set the question attribures
        sItem.setSurveyItemId(idString);
        sItem.setSurveyDefinition(surveyDefinition);
        sItem.setParent((SurveyItemBase) parent);
        
        List peerList;
        if(parent == null)
        {
        	peerList = surveyDefinition.getQuestions();
        	if(peerList == null)
        	{
        		peerList = new ArrayList();
        	}
        }
        else
        {	
	        if(((Container)parent).getChildren() == null)
	        {
	        	List children = new ArrayList();
	        	((Container)parent).setChildren(children);
	        }
	        
	        peerList = ((Container)parent).getChildren();
        }

        SurveyItemBase previousItem = surveyDefinition.getQuestion(previoisQuestionId);
        if(previousItem == null)
        {
        	//add to the end of the list if the previous question cannot be determined
        	sItem.setOrderNum(peerList.size() + 1);
        	peerList.add(sItem);
        }
        else
        {
        	sItem.setOrderNum(previousItem.getOrderNum() + 0.1f);
        	peerList.add(sItem);
        	packOrderNums(peerList);
        }
        
        //if the new item is a container, take care of adding its chilren.. eg. BomInspectionItemGroupAnswerType
        if(sItem instanceof Container)
		{
			List children = ((Container)sItem).getChildren();
			if(children != null)
			{
				for (int j=0; j<children.size(); j++)
				{
					SurveyItem aChild = (SurveyItem) children.get(j);
					aChild.setSurveyDefinition(surveyDefinition);
					aChild.setParent((SurveyItemBase) sItem);
					Thread.sleep(2); // just to make sure that a unique id is created
					String cseq = new Long(new Date().getTime()).toString();
			        String cidString = "SurveyItem_" + cseq;

			        //set the question attribures
			        aChild.setSurveyItemId(cidString);
					aChild.setOrderNum(j+1);
				}
			}
		}
        
        //Write to a file
        writeToFile();
        
        return sItem;
    }

	//TODO the formType condition check has to be refactored out.. looks bad
    public SurveyItem updateQuestion(SurveyItem _sItem)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(FormTypeEnum.TestForm.value() == survey.getFormType() && Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

  		SurveyItemBase item = surveyDefinition.getQuestion(_sItem.getSurveyItemId());
  		_sItem.setOrderNum(item.getOrderNum());
  		_sItem.setSurveyDefinition(surveyDefinition);
  		_sItem.setParent(item.getParent());
  		
		if(_sItem instanceof Container)
		{
			List children = ((Container)_sItem).getChildren();
			if(children != null)
			{
				for (int j=0; j<children.size(); j++)
				{
					SurveyItem aChild = (SurveyItem) children.get(j);
					aChild.setSurveyDefinition(_sItem.getSurveyDefinition());
					aChild.setParent((SurveyItemBase) _sItem);
					if(aChild.getSurveyItemId() == null)
					{
						Thread.sleep(2); // just to make sure that a unique id is created
				        String seq = new Long(new Date().getTime()).toString();
				        String idString = "SurveyItem_" + seq;
	
				        //set the question attribures
				        aChild.setSurveyItemId(idString);
						aChild.setOrderNum(j+1);
					}
				}
			}
		}

		Container parent = (Container)item.getParent();
  		List peerList;
  		if(parent != null)
  		{
  			peerList = parent.getChildren();
  		}
  		else
  		{
  			peerList = surveyDefinition.getQuestions();
  		}

  		peerList.remove(item);
  		peerList.add(_sItem);
  		Collections.sort(peerList, new SurveyItemOrderComparator());

		//Write to a file
  		writeToFile();
  		
        return _sItem;
    }

    public void clearAllQuestions()throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        surveyDefinition.getQuestions().removeAll(surveyDefinition.getQuestions());
        
		//Write to a file
        writeToFile();
    }
    
    public void deleteQuestion(String questionId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        SurveyItemBase dItem = surveyDefinition.getQuestion(questionId);
        Container parent = (Container) dItem.getParent();
        List peerList;
        if(parent != null)
        {
        	peerList = parent.getChildren();
        }
        else
        {
        	peerList = surveyDefinition.getQuestions();
        }
        peerList.remove(dItem);
        packOrderNums(peerList);
        
		//Write to a file
        writeToFile();
    }
    
    private void packOrderNums(List children)
	{
    	Collections.sort(children, new SurveyItemOrderComparator());
    	
    	long ordNo = 1;
    	for (Iterator iterator = children.iterator(); iterator.hasNext();)
		{
			SurveyItem aItem = (SurveyItem) iterator.next();
			aItem.setOrderNum(ordNo);
			ordNo++;
		}
	}

	public void hideQuestion(String questionId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        String surveyName = survey.getIdentityNumber();
  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder builder = new SAXBuilder();
		Document surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

        Element rootElement = surveyDoc.getRootElement();

        Element qElement = null;

        //locate the element to be removed
        List qList = rootElement.getChildren("surveyItem");
        if (qList != null)
        {
            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String qId = element.getAttributeValue("id");
                if (qId.equals(questionId))
                {
                    qElement = element;
                }
            }
        }

        //hide hthe question
        qElement.setAttribute("hidden", "true");

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
    }

    *//**
     * @param questionId
     * @param newLocationId
     *//*
    public void moveQuestionToLocation(String questionId, String newLocationId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        if(questionId.equals(newLocationId))
        {
            return;
        }

  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

        SurveyDefinition sd = surveyDefFactory.getSurveyDefinition(new FormOID((int) survey.getPk(), survey.getIdentityNumber()));
        List questions = sd.getQuestions();

        SurveyItemBase questionToMove = sd.getQuestion(questionId);
        if(questionToMove == null)
        {
            return;
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("question to copy - " + questionToMove.getIdentifier());
            }
        }

  		//initialize the parser and remove all contents
		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

		Element rootElement = surveyDoc.getRootElement();
        rootElement.removeChildren("surveyItem");

        long order = 1;
        //if newLocationId == 0, then place the question in the beginning
        if("0".equals(newLocationId))
        {
            questionToMove.setOrderNum(order);
            Element qElement = questionToMove.toXML();
            rootElement.addContent(qElement);
            order++;
        }

        for (Iterator qItr = questions.iterator(); qItr.hasNext();)
        {
            SurveyItem question = (SurveyItem) qItr.next();
            String qId = question.getSurveyItemId();

            //if this is the same question as questionToCopy dont add , else add
            if(!(qId.equals(questionToMove.getSurveyItemId())))
            {
                question.setOrderNum(order);
                Element qElement = question.toXML();
                rootElement.addContent(qElement);
                order++;
            }

            if (qId.equals(newLocationId))
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("This is the move to location - adding the question here");
                }
                questionToMove.setOrderNum(order);
                Element qElement = questionToMove.toXML();
                rootElement.addContent(qElement);
                order++;
            }
        }

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();

    }

    /**
     * @param questionId
     * @param newLocationId
     */
    public SurveyItem copyQuestionToLocation(String questionId, String newLocationId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        SurveyItemBase questionToCopy = surveyDefinition.getQuestion(questionId);
        if(questionToCopy == null)
        {
            return null;
        }
        else
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("question to copy - " + questionToCopy.getIdentifier());
            }
        }
        
        SurveyItemBase parent = (SurveyItemBase) questionToCopy.getParent();
        
        //Now create a copy of that Question
        SurveyItem newQuestion = copyQuestionTree(surveyDefinition, questionToCopy, parent);
        
        if(questionToCopy.getParent() == null)
        {
        	List items = surveyDefinition.getQuestions();
        	if(items == null)
        	{
        		items = new ArrayList();
        	}
        	newQuestion.setOrderNum(items.size() + 1);
        	items.add(newQuestion);
        }
        else
        {	
	        if(((Container)parent).getChildren() == null)
	        {
	        	List children = new ArrayList();
	        	((Container)parent).setChildren(children);
	        }
	        
	        newQuestion.setOrderNum(((Container)parent).getChildren().size() + 1);
	        ((Container)parent).getChildren().add(newQuestion);
        }

        //Write to a file
        writeToFile();
        
        return newQuestion;
    }
    
    /**
     * @param questionId
     * @param newLocationId
     */
    public SurveyItem copyQuestionToLocation(FormQuery sourceForm, SurveyItem sourceItem, String destinationParentItemId, String newLocationId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        
        SurveyDefinition sourceSd = surveyDefFactory.getSurveyDefinition(new FormOID(sourceForm.getPk(), sourceForm.getIdentityNumber()));
        if(sourceItem == null)
        {
            return null;
        }
        
        SurveyItemBase parent = null;
        if(destinationParentItemId != null)
        {
        	parent = surveyDefinition.getQuestion(destinationParentItemId);
        }
        //Now create a copy of that Question
        SurveyItem newQuestion = copyQuestionTree(sourceSd, sourceItem, parent);
        
        if(parent == null)
        {
        	List items = surveyDefinition.getQuestions();
        	if(items == null)
        	{
        		items = new ArrayList();
        	}
        	newQuestion.setOrderNum(items.size() + 1);
        	items.add(newQuestion);
        }
        else
        {	
	        if(((Container)parent).getChildren() == null)
	        {
	        	List children = new ArrayList();
	        	((Container)parent).setChildren(children);
	        }
	        
	        newQuestion.setOrderNum(((Container)parent).getChildren().size() + 1);
	        ((Container)parent).getChildren().add(newQuestion);
        }

        //Write to a file
        writeToFile();
        
        return newQuestion;
    }

    private SurveyItem copyQuestionTree(SurveyDefinition sd, SurveyItemBase questionToCopy, SurveyItemBase parent) throws Exception
    {
        SurveyItem newQuestion = (SurveyItem) SurveyItemManager.getInstance().getSurveyItem(questionToCopy.getTypeName());
        newQuestion.setSurveyDefinition(sd);
        newQuestion.setConfiguration(questionToCopy.toXML());
		Thread.sleep(2); // just to make sure that a unique id is created
        String seq = "" + new Date().getTime();
        String idString = ("SurveyItem_" + seq);
        newQuestion.setSurveyItemId(idString);
        newQuestion.setParent((SurveyItemBase) parent);
    	
        if(questionToCopy instanceof Container)
        {
        	if(((Container) questionToCopy).getChildren().size() > 0)
        	{
        		List newChildren = new ArrayList();
        		((Container)newQuestion).setChildren(newChildren);
            	for (Iterator iterator = ((Container)questionToCopy).getChildren().iterator(); iterator.hasNext();)
    			{
    				SurveyItem aChild = (SurveyItem) iterator.next();
    				newChildren.add(copyQuestionTree(sd, aChild, newQuestion));
    			}
        	}
        }
        return newQuestion;
    }

    /**
     * @param questionId
     */
    public void moveQuestionUp(String questionId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }
        
        SurveyItemBase qToMove = this.surveyDefinition.getQuestion(questionId);
        Container parent = (Container)qToMove.getParent();
        List peerList;
        if(parent != null)
        {
        	peerList = parent.getChildren();
        }
        else
        {
        	peerList = surveyDefinition.getQuestions();
        }
        
        int location = 0;
        for(int i=0; i<peerList.size(); i++)
        {
        	SurveyItem aItem = (SurveyItem)peerList.get(i);
        	if(aItem.equals(qToMove))
        	{
        		location = i;
        	}
        }
        if(location == 0) // already the last item
        {
        	return;
        }
        
        SurveyItem prevItem = (SurveyItem)peerList.get(location-1);
        float temp = qToMove.getOrderNum();
        qToMove.setOrderNum(prevItem.getOrderNum());
        prevItem.setOrderNum(temp);

        //sort the list according to the orderNum
        Collections.sort(peerList, new SurveyItemOrderComparator());

		//Write to a file
        writeToFile();
    }
    
    /**
     * @param questionId
     */
    public void moveQuestionDown(String questionId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }
        
        SurveyItemBase qToMove = this.surveyDefinition.getQuestion(questionId);
        Container parent = (Container)qToMove.getParent();
        List peerList;
        if(parent != null)
        {
        	peerList = parent.getChildren();
        }
        else
        {
        	peerList = surveyDefinition.getQuestions();
        }
        
        int location = 0;
        for(int i=0; i<peerList.size(); i++)
        {
        	SurveyItem aItem = (SurveyItem)peerList.get(i);
        	if(aItem.equals(qToMove))
        	{
        		location = i;
        	}
        }
        if(location == peerList.size()-1) // already the last item
        {
        	return;
        }
        
        SurveyItem nextItem = (SurveyItem)peerList.get(location+1);
        float temp = qToMove.getOrderNum();
        qToMove.setOrderNum(nextItem.getOrderNum());
        nextItem.setOrderNum(temp);

        //sort the list according to the orderNum
        Collections.sort(peerList, new SurveyItemOrderComparator());

		//Write to a file
        writeToFile();
    }
    
    /**
     * @param paramList
     */
    public void setSurveyParameters(List paramList)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        int surveyPk = (int) survey.getPk();
  		String surveyFileName = survey.getDefFileName();
        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

		Element rootElement = surveyDoc.getRootElement();

        rootElement.removeChild("surveyParams");

        Element paramRoot = new Element("surveyParams");
        for (Iterator iter = paramList.iterator(); iter.hasNext();)
        {
            SurveyParamBase aParam = (SurveyParamBase) iter.next();
            Element aParamElement = new Element("surveyParam");
            aParamElement.setAttribute("name", aParam.getName());
            aParamElement.setAttribute("dataType", new Integer(aParam.getDataType()).toString());
            paramRoot.addContent(aParamElement);
        }

        rootElement.addContent(paramRoot);

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
    }

    public Logic addLogic(SurveyItem sItem, Logic logic)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        String surveyName = survey.getIdentityNumber();
  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

        Element rootElement = surveyDoc.getRootElement();

        //locate the SurveyItem{question} element to be modified
        Element qElement = null;
        List qList = rootElement.getChildren("surveyItem");
        if (qList != null)
        {
            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String qId = element.getAttributeValue("id");
                if (qId.equals(sItem.getSurveyItemId()))
                {
                    qElement = element;
                    break;
                }
            }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Located the surveyItem - " + qElement.getAttributeValue("text"));
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("Logic is of type" + logic.getClass().getName());
        }

        String seq = sequenceIdGenerator.getNext(SequenceIdGenerator.LOGIC, false);
        if (logger.isDebugEnabled())
        {
            logger.debug("Sequence number is - " + seq);
        }
        //set the question attribures
        logic.setSurveyItemId(seq);
        logic.setOrderNum(Long.parseLong(seq));

        Element lElement = logic.toXML();

        qElement.addContent(lElement);

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();

        return logic;
    }

    /**
     * @param sItem
     * @param logic
     */
    public void deleteLogic(SurveyItem sItem, Logic logic)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("sItem - " + sItem);
            logger.debug("logic - " + logic);
        }
        if(sItem == null || logic == null)
        {
            return;
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("SurveyItemId - " + sItem.getSurveyItemId());
            logger.debug("LogicId - " + logic.getSurveyItemId());
        }
        String surveyName = survey.getIdentityNumber();
  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

        Element rootElement = surveyDoc.getRootElement();

        //locate the SurveyItem{question} element to be modified
        Element qElement = null;
        List qList = rootElement.getChildren("surveyItem");
        if (qList != null)
        {
            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String qId = element.getAttributeValue("id");
                if (qId.equals(sItem.getSurveyItemId()))
                {
                    qElement = element;
                    break;
                }
            }
        }

        //now locate the logic element
        Element lElement = null;
        List lList = qElement.getChildren("logic");
        if (lList != null)
        {
            for (Iterator qItr = lList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String lId = element.getAttributeValue("id");
                if (lId.equals(logic.getSurveyItemId()))
                {
                    lElement = element;
                    break;
                }
            }
        }

        //remove the located element
        qElement.removeContent(lElement);

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
    }

    /**
     * saves a logic to the surveydefinition file
     * @param item
     * @param logic
     * @param paramMap
     * @return
     */
    public Logic updateLogic(SurveyItem item, Logic logic)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("SurveyItemId - " + item.getSurveyItemId());
            logger.debug("LogicId - " + logic.getSurveyItemId());
        }
        String surveyName = survey.getIdentityNumber();
  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

        Element rootElement = surveyDoc.getRootElement();

        //locate the SurveyItem{question} element to be modified
        Element qElement = null;
        List qList = rootElement.getChildren("surveyItem");
        if (qList != null)
        {
            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String qId = element.getAttributeValue("id");
                if (qId.equals(item.getSurveyItemId()))
                {
                    qElement = element;
                    break;
                }
            }
        }

        //now locate the logic element
        Element lElement = null;
        List lList = qElement.getChildren("logic");
        if (lList != null)
        {
            for (Iterator qItr = lList.iterator(); qItr.hasNext();)
            {
                Element element = (Element) qItr.next();
                String lId = element.getAttributeValue("id");
                if (lId.equals(logic.getSurveyItemId()))
                {
                    lElement = element;
                    break;
                }
            }
        }

        //create the new updated element
        String seq = lElement.getAttributeValue("orderNum");

        logic.setSurveyItemId(lElement.getAttributeValue("id"));
        logic.setOrderNum(Long.parseLong(seq));

        Element logicElement = logic.toXML();

        qElement.removeContent(lElement);
        qElement.addContent(logicElement);

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();

        return logic;
    }


    public void moveLogicToLocation(SurveyItem sItem, String logicId, String newLocationId)throws Exception
    {
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("SurveyItem - " + sItem);
            logger.debug("logicId - " + logicId);
            logger.debug("newLocationId - " + newLocationId);
        }

        if(logicId.equals(newLocationId))
        {
            return;
        }
        List logicList  = sItem.getLogicList();
        if (logger.isDebugEnabled())
        {
            logger.debug("Logic move for SurveyItem - " + sItem.getIdentifier());
            logger.debug("original Logiclist size - " + logicList.size());
            logger.debug("Logic items in order - ");
            for (Iterator iter = logicList.iterator(); iter.hasNext();)
            {
                SurveyItem element = (SurveyItem) iter.next();
                logger.debug(element.getSurveyItemId() + " orderNum - " + element.getOrderNum());
            }
        }
        if (logicList == null)
        {
            return;
        }

        SurveyItem logicToMove = null;
        for (Iterator iter = logicList.iterator(); iter.hasNext();)
        {
            SurveyItem lItem = (SurveyItem) iter.next();
            if(lItem.getSurveyItemId().equals(logicId))
            {
                logicToMove = (Logic)lItem;
                break;
            }
        }

        if(logicToMove == null)
        {
            return;
        }

        List newLogicList = new ArrayList();

        long order = 1;
        if(newLocationId.equals("0"))
        {
            logicToMove.setOrderNum(order);
            newLogicList.add(logicToMove);
            order++;
        }
        for (Iterator qItr = logicList.iterator(); qItr.hasNext();)
        {
            SurveyItem logicItem = (SurveyItem) qItr.next();
            if(!(logicItem.getSurveyItemId().equals(logicId)))
            {
                logicItem.setOrderNum(order);
                newLogicList.add(logicItem);
                order++;
            }
            if(logicItem.getSurveyItemId().equals(newLocationId))
            {
                logicToMove.setOrderNum(order);
                newLogicList.add(logicToMove);
                order++;
            }
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("rearranged logic list size - " + newLogicList.size());
            logger.debug("Logic items in order - ");
            for (Iterator iter = newLogicList.iterator(); iter.hasNext();)
            {
                SurveyItem element = (SurveyItem) iter.next();
                logger.debug(element.getSurveyItemId() + " orderNum - " + element.getOrderNum());
            }
        }
        //set the newLogicList to the surveyItem
        sItem.setLogicList(newLogicList);

        //write the SurveyItemList to file
  		String surveyFileName = survey.getDefFileName();

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;
        SurveyDefinition sd = surveyDefFactory.getSurveyDefinition(new FormOID((int) survey.getPk(), survey.getIdentityNumber()));

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

		Element rootElement = surveyDoc.getRootElement();
        rootElement.removeChildren("surveyItem");

        for (Iterator iter = sd.getQuestions().iterator(); iter.hasNext();)
        {
            SurveyItem surveyItem = (SurveyItem)iter.next();

            if(surveyItem.getSurveyItemId().equals(sItem.getSurveyItemId()))
            {
                surveyItem = sItem;
            }

            Element qElement = surveyItem.toXML();
            rootElement.addContent(qElement);
        }

        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
    }

	public void setFormInstruction(File file, String fileDisplayName)throws Exception
	{
        int surveyPk = (int) survey.getPk();
  		String surveyFileName = survey.getDefFileName();
        String filePath = ApplicationProperties.getFormDefRoot();
        surveyFileName = filePath + surveyFileName;

		FileInputStream inStream = new FileInputStream(new File(surveyFileName));
		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
        SAXBuilder   builder = new SAXBuilder();
		Document     surveyDoc = (Document) builder.build(reader);
		reader.close();
		inStream.close();

		Element rootElement = surveyDoc.getRootElement();

        rootElement.removeChild("formInstruction");

        if(file != null)
        {
        	Element formInstruction = new Element("formInstruction");
        	formInstruction.setAttribute("fileName", (fileDisplayName != null)? fileDisplayName: "");
        	formInstruction.setAttribute("filePath", (file != null)?file.getName():"");
        	rootElement.addContent(formInstruction);
        }
        
        //Write to a file
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
        
        // set that into the survey definition
        this.surveyDefinition.setFormInstructionFile(file);
        this.surveyDefinition.setFormInstructionFileDisplayName(fileDisplayName);
	}
	
	public boolean sectionBreak(String questionId)throws Exception
	{
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        SurveyItemBase bItem = surveyDefinition.getQuestion(questionId);
        SectionBase section = (SectionBase)bItem.getParent();
        section = (SectionBase) surveyDefinition.getQuestion(section.getSurveyItemId()); 
        // section should be a reference of the Object from the SurveyDefinition
        //it should be the case when it is loaded but somehow the parent is not getting set as the same
        //object from SurveyDefinition.. something to investigate during the loading process..
        
        Container sectionsParent = (Container) section.getParent();

        List oldChildList = new ArrayList();
        List newChildList = new ArrayList();
        boolean broke = false;
        for (Iterator iterator = section.getChildren().iterator(); iterator.hasNext();)
		{
			SurveyItem oneItem = (SurveyItem) iterator.next();
			if(bItem.equals(oneItem))
			{
				broke = true;
			}
			if(broke)
			{
				newChildList.add(oneItem);
			}
			else
			{
				oldChildList.add(oneItem);
			}
		}
        
		SectionBase newSection = (SectionBase) SurveyItemManager.getInstance().getSurveyItem(SurveyItemManager.Section.getName());
		newSection.setPageTitle(section.getPageTitle());
		newSection.setDescription(section.getDescription());
		newSection.setFlags(section.getFlags());
		Thread.sleep(2); // just to make sure that a unique id is created
		String seq = new Long(new Date().getTime()).toString();
        String idString = "SurveyItem_" + seq;
        newSection.setSurveyItemId(idString);
        newSection.setOrderNum((float)(section.getOrderNum() + 0.1));
        newSection.setParent((SurveyItem)sectionsParent);
        
        section.setChildren(oldChildList);
        newSection.setChildren(newChildList);
        
        List pChildList;
        if(sectionsParent != null)
        {
        	pChildList = sectionsParent.getChildren();
        }
        else
        {
        	pChildList = surveyDefinition.getQuestions();
        }

        pChildList.add(newSection);
        
        packOrderNums(pChildList);
        packOrderNums(oldChildList);
        packOrderNums(newChildList);
        
		//Write to a file
        writeToFile();
        
		return true;
    }

	public boolean sectionStitch(String questionId)throws Exception
	{
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        List peerList = surveyDefinition.getQuestions();
        
        Container selectedSection = (Container) surveyDefinition.getQuestion(questionId);
        int selectedItemIndex = peerList.indexOf(selectedSection);
        if(selectedItemIndex == 0)
        {
        	return false;
        }
        Container previousSection = (Container) surveyDefinition.getQuestions().get(selectedItemIndex-1);
        List prevContainerChildren = previousSection.getChildren();

        int ordNo = prevContainerChildren.size();
        for (Iterator iterator = selectedSection.getChildren().iterator(); iterator.hasNext();)
		{
			SurveyItem oneItem = (SurveyItem) iterator.next();
			ordNo++;
			oneItem.setOrderNum(ordNo);
			prevContainerChildren.add(oneItem);
		}

        peerList.remove(selectedSection);
        
        packOrderNums(peerList);
        
		//Write to a file
        writeToFile();
        
		return true;
    }

	public boolean sectioncopy(String questionId)throws Exception
	{
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        List peerList = surveyDefinition.getQuestions();
        
        Container selectedSection = (Container) surveyDefinition.getQuestion(questionId);
        List containerChildren = selectedSection.getChildren();

        
        
        packOrderNums(peerList);
        
		//Write to a file
        writeToFile();
        
		return true;
    }

	public boolean breakBomTable(String questionId)throws Exception
	{
        String status = survey.getStatus();
        if (logger.isDebugEnabled())
        {
            logger.debug("Survey status is - " + status);
        }
        if(Survey.STATUS_OPEN.equals(status))
        {
        	throw new SurveyNotEditableException();
        }

        SurveyItemBase bItem = surveyDefinition.getQuestion(questionId);
        Container bom = (Container)bItem.getParent();
        
        // if the break is done on the first child in the table, we will end up with an empty table above.. so dont allow that
        if(bom.getChildren() == null || bom.getChildren().size() == 1)
        {
        	return false;
        }
        if(bom.getChildren().get(0).equals(bItem))
        {
        	return false;
        }
        
        Container bomsParent = (Container)((SurveyItem)bom).getParent();

        List oldBomList = new ArrayList();
        List newBomList = new ArrayList();
        boolean broke = false;
        for (Iterator iterator = bom.getChildren().iterator(); iterator.hasNext();)
		{
			SurveyItem oneItem = (SurveyItem) iterator.next();
			if(bItem.equals(oneItem))
			{
				broke = true;
			}
			if(broke)
			{
				newBomList.add(oneItem);
			}
			else
			{
				oldBomList.add(oneItem);
			}
		}
        SurveyItem newBom = (SurveyItem) SurveyItemManager.getInstance().getSurveyItem(((SurveyItem)bom).getTypeName());
        newBom.setSurveyDefinition(surveyDefinition);
        newBom.setConfiguration(((SurveyItem)bom).toXML());
		Thread.sleep(2); // just to make sure that a unique id is created
        String seq = new Long(new Date().getTime()).toString();
        String idString = "SurveyItem_" + seq;
        newBom.setSurveyItemId(idString);
        newBom.setOrderNum((float)(((SurveyItem)bom).getOrderNum() + 0.1));
        newBom.setParent((SurveyItemBase) bomsParent);
        
        bomsParent.getChildren().add(newBom);
        
        bom.setChildren(oldBomList);
        ((Container)newBom).setChildren(newBomList);
        
        packOrderNums(bomsParent.getChildren());
        packOrderNums(oldBomList);
        packOrderNums(newBomList);
        
		//Write to a file
        writeToFile();

        return true;
	}
	
	private void writeToFile()throws Exception
	{
        //Write to a file
        Element surveyElement = new Element("survey");
        surveyElement.setAttribute("name", survey.getIdentityNumber());

        if(surveyDefinition.getFormInstructionFile() != null)
        {
	        Element formInstruction = new Element("formInstruction");
	        formInstruction.setAttribute("fileName", surveyDefinition.getFormInstructionFileDisplayName());
	        formInstruction.setAttribute("filePath", surveyDefinition.getFormInstructionFile().getName());
	        surveyElement.addContent(formInstruction);
        }        
        
        Document surveyDoc = new Document(surveyElement);
        for (Iterator iterator = surveyDefinition.getQuestions().iterator(); iterator.hasNext();) 
        {
			SurveyItem saItem = (SurveyItem) iterator.next();
			if(saItem instanceof SurveySaveItem && ((SurveySaveItem)saItem).isHidden())
			{
				continue;
			}
			Element elem = saItem.toXML();
			surveyElement.addContent(elem);
		}

        String filePath = ApplicationProperties.getFormDefRoot();
  		String surveyFileName = survey.getDefFileName();
        surveyFileName = filePath + surveyFileName;
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        FileOutputStream outStream = new FileOutputStream(surveyFileName, false);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        outputter.output((org.jdom2.Document) surveyDoc, writer);
        writer.close();
        outStream.close();
	}
}
