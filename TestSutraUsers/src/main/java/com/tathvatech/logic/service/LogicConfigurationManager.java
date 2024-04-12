/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.logic.service;


import com.tathvatech.logic.common.*;
import com.tathvatech.site.service.SiteServiceImpl;
import com.tathvatech.survey.common.SurveyDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogicConfigurationManager extends Object
{
    private static final Logger logger = LoggerFactory.getLogger(LogicConfigurationManager.class);
    
    private SurveyDefinition surveyDefinition;

    /**
     * @param
     */
    public LogicConfigurationManager(SurveyDefinition surveyDef)
    {
        this.surveyDefinition = surveyDef;
    }

    //TODO:: this should be implemented in a better way
    public Condition getCondition(String surveyItemTypeName, String subjectId)
    {
        logger.debug("Looking up Condition for - " + surveyItemTypeName);
        if("TextBoxAnswerType".equals(surveyItemTypeName))
        {
            return new TextBoxCondition(subjectId, surveyDefinition);
        }
        if("TextAreaAnswerType".equals(surveyItemTypeName))
        {
            return new TextBoxCondition(subjectId, surveyDefinition);
        }
        else if("SurveyParamType".equals(surveyItemTypeName))
        {
            return new TextBoxCondition(subjectId, surveyDefinition);
        }
        else if("RadioButtonAnswerType".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else if("HorizontalRadioButtonAnswerType".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else if("CheckBoxAnswerType".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else if("HorizontalCheckBoxAnswerType".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else if("DropDownAnswerType".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else if("MatrixSingleAnswerAnswerType".equals(surveyItemTypeName))
        {
            return new MatrixCondition(subjectId, surveyDefinition);
        }
        else if("MatrixSingleAnswerPerRowAnswerType".equals(surveyItemTypeName))
        {
            return new MatrixCondition(subjectId, surveyDefinition);
        }
        else if("MatrixMultipleAnswerAnswerType".equals(surveyItemTypeName))
        {
            return new MatrixCondition(subjectId, surveyDefinition);
        }
        else if("MultiTextBoxAnswerType".equals(surveyItemTypeName))
        {
            return new MultiTextCondition(subjectId, surveyDefinition);
        }
        else if("ConstantSumAnswerType".equals(surveyItemTypeName))
        {
            return new MultiTextCondition(subjectId, surveyDefinition);
        }
        else if("MatrixDropDownAnswerType".equals(surveyItemTypeName))
        {
            return new Matrix3DCondition(subjectId, surveyDefinition);
        }

        
        
        
//Conditions for the Master table values follows
        else if("Start".equals(surveyItemTypeName))
        {
            return new MultipleChoiceCondition(subjectId, surveyDefinition);
        }
        else
        {
            // when an invalid condition is instantiated.. it could be default to TextBoxCondition.
            return new TextBoxCondition(subjectId, surveyDefinition);
        }
    }


//    public Logic addLogic(SurveyItem sItem, Logic logic)throws Exception
//    {
//        String surveyName = survey.getString("Survey_surveyName");
//  		String surveyFileName = survey.getString("Survey_defFileName");
//  		SAXBuilder   builder = new SAXBuilder();
//  		Document surveyDoc = builder.build(new File(surveyFileName));
//
//        Element rootElement = surveyDoc.getRootElement();
//
//        //locate the SurveyItem{question} element to be modified
//        Element qElement = null;
//        List qList = rootElement.getChildren("surveyItem");
//        if (qList != null)
//        {
//            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
//            {
//                Element element = (Element) qItr.next();
//                String qId = element.getAttributeValue("id");
//                if (qId.equals(sItem.getSurveyItemId()))
//                {
//                    qElement = element;
//                    break;
//                }
//            }
//        }
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("Located the surveyItem - " + qElement.getAttributeValue("text"));
//        }
//        
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("Logic is of type" + logic.getClass().getName());
//        }
//
//        String seq = SequenceIdGenerator.getNext(SequenceIdGenerator.LOGIC);
//        if (logger.isDebugEnabled())
//        {
//            logger.debug("Sequence number is - " + seq);
//        }
//        //set the question attribures
//        logic.setSurveyItemId(seq);
//        logic.setOrderNum(Long.parseLong(seq));
//        
//        Element lElement = logic.toXML();
//        
//        qElement.addContent(lElement);
//
//        //Write to a file 
//        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
//        FileWriter writer = new FileWriter(surveyFileName, false);
//        outputter.output(surveyDoc, writer);
//        writer.close();
//        
//        return logic;
//    }
//
//    /**
//     * saves a logic to the surveydefinition file
//     * @param item
//     * @param logic
//     * @param paramMap
//     * @return
//     */
//    public Logic updateLogic(SurveyItem item, Logic logic)throws Exception
//    {
//        String surveyName = survey.getString("Survey_surveyName");
//  		String surveyFileName = survey.getString("Survey_defFileName");
//  		SAXBuilder   builder = new SAXBuilder();
//  		Document surveyDoc = builder.build(new File(surveyFileName));
//
//        Element rootElement = surveyDoc.getRootElement();
//
//        //locate the SurveyItem{question} element to be modified
//        Element qElement = null;
//        List qList = rootElement.getChildren("surveyItem");
//        if (qList != null)
//        {
//            for (Iterator qItr = qList.iterator(); qItr.hasNext();)
//            {
//                Element element = (Element) qItr.next();
//                String qId = element.getAttributeValue("id");
//                if (qId.equals(item.getSurveyItemId()))
//                {
//                    qElement = element;
//                    break;
//                }
//            }
//        }
//        
//        //now locate the logic element
//        Element lElement = null;
//        List lList = qElement.getChildren("logic");
//        if (lList != null)
//        {
//            for (Iterator qItr = lList.iterator(); qItr.hasNext();)
//            {
//                Element element = (Element) qItr.next();
//                String lId = element.getAttributeValue("id");
//                if (lId.equals(logic.getSurveyItemId()))
//                {
//                    lElement = element;
//                    break;
//                }
//            }
//        }
//        
//        //create the new updated element
//        String seq = lElement.getAttributeValue("orderNum"); 
//        
//        logic.setSurveyItemId(lElement.getAttributeValue("id"));
//        logic.setOrderNum(Long.parseLong(seq));
//
//        Element logicElement = logic.toXML();
//        
//        rootElement.removeContent(lElement);
//        rootElement.addContent(logicElement);
//        
//        //Write to a file 
//        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
//        FileWriter writer = new FileWriter(surveyFileName, false);
//        outputter.output(surveyDoc, writer);
//        writer.close();
//        
//        return logic;
//    }
}
