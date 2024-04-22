/*
 * Created on Sep 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.service;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.ServiceLocator;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.entity.Survey;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.user.OID.FormOID;


import org.jdom2.Document;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;




/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Service

public class SurveyDefFactory
{
    private static final Logger logger = LoggerFactory.getLogger(SurveyDefFactory.class);

    @Autowired
    @Lazy
    private SurveyMaster surveyMaster;
  // private static ReferenceMap defMap = new ReferenceMap(ReferenceMap.SOFT, ReferenceMap.SOFT);
    

   public  SurveyDefinition getSurveyDefinition(FormOID surveyOID) throws Exception
    {

        SurveyDefinition surveyDef = null;
//        surveyDef = (SurveyDefinition) defMap.get(surveyOID.getPk());
//        if(surveyDef != null)
//        	return surveyDef;
        
    	Survey survey = surveyMaster.getSurveyByPk((int) surveyOID.getPk());
        if(survey == null)
        {
            logger.warn("Survey not found, pk - " + surveyOID.getPk());
            return null;
        }

        String surveyDefFile = survey.getDefFileName();
        if (logger.isDebugEnabled())
        {
            logger.debug("SurveyName is " + surveyDefFile);
            logger.debug("Survey Definition filename is " + surveyDefFile);
        }
        if(surveyDefFile == null || surveyDefFile.trim().length() == 0)
        {
            logger.error("Invalid survey entry, survey with no Def file defined, pk - " + surveyOID.getPk());
            throw new Exception("unexpected error, The survey cannot be loaded.");
        }

        String filePath = ApplicationProperties.getFormDefRoot();
        surveyDefFile = filePath + surveyDefFile;
        logger.debug("sdf-" + surveyDefFile);

		try
		{
			FileInputStream inStream = new FileInputStream(new File(surveyDefFile));
			InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
	        SAXBuilder builder = new SAXBuilder();
			Document     doc = (Document) builder.build(reader);
			reader.close();
			inStream.close();

			surveyDef = new SurveyDefinition(survey, doc.getRootElement());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			logger.warn(e + " :: " + e.getMessage());
	        if (logger.isDebugEnabled())
			{
	        	logger.debug(e.getMessage(), e);
			}
		}
//		defMap.put(surveyOID.getPk(), surveyDef);
        return surveyDef;
    }


    public static void createSurveyDefFile(Survey survey)throws Exception
    {
        try
        {
            Element surveyElement = new Element("survey");
            Document surveyDoc = new Document(surveyElement);

            if (logger.isDebugEnabled())
            {
                logger.debug("File name for the new Survey - " + survey.getDefFileName());
            }
            surveyElement.setAttribute("name", survey.getIdentityNumber());

            //Write to a file
            String filePath = ApplicationProperties.getFormDefRoot();
            String fullFilePath = filePath + survey.getDefFileName();

            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            FileOutputStream outStream = new FileOutputStream(fullFilePath, false);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
            outputter.output((org.jdom2.Document) surveyDoc, writer);
            writer.close();
            outStream.close();
        }
        catch (IOException e)
        {
            logger.error("Could not create survey definition file for survey " + survey.getIdentityNumber() + " :: " + e.getMessage());
            if (logger.isDebugEnabled())
    		{
            	logger.debug(e.getMessage(), e);
    		}
            throw new Exception();
        }
    }

    public  void createSurveyByCopy(Survey survey, int sourceSurveyPk)throws Exception
    {
        String filePath = ApplicationProperties.getFormDefRoot();

        try
		{
            String surveyDefFile = surveyMaster.getSurveyDefFileName(sourceSurveyPk);

            if(surveyDefFile == null || surveyDefFile.trim().length() == 0)
            {
                throw new AppException("MSG-surveyToCopyNotFound");
            }
            surveyDefFile = filePath + surveyDefFile;

    		FileInputStream inStream = new FileInputStream(new File(surveyDefFile));
    		InputStreamReader reader = new InputStreamReader(inStream, "UTF-8");
            SAXBuilder   builder = new SAXBuilder();
    		Document doc = (Document) builder.build(reader);
    		reader.close();
    		inStream.close();

			Element surveyElement = doc.getRootElement();
			surveyElement.setAttribute("name", survey.getIdentityNumber());

	        String newFullFilePath = filePath + survey.getDefFileName();

	        //Write to a file
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            FileOutputStream outStream = new FileOutputStream(newFullFilePath, false);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
            outputter.output((org.jdom2.Document) doc, writer);
            writer.close();
            outStream.close();
        }
        catch (IOException e)
        {
            logger.error("Error creating survey definition by copy" + " :: " + e.getMessage());
            if (logger.isDebugEnabled())
    		{
            	logger.debug(e.getMessage(), e);
    		}
            throw new Exception();
        }
    }



    private static void createSurveyTables(String tableName)throws Exception
    {
        String mainTableSql = "create table "+ tableName + " (responseId varchar(25), surveyPk varchar(25), responseTime datetime, ipaddress varchar(20),  respondentPk varchar(25), lastSurveyItem varchar(25), status varchar(20), responseMode varchar(20))";
        String descTableSql = "create table " + tableName + "_desc ( responseId varchar(25), surveyPk varchar(25), questionId varchar(25), key1 int, key2 int, key3 int, key4 varchar(5000) )";
//        String textTableSql = "create table " + tableName + "_text ( responseId varchar(25), surveyPk varchar(25), questionId varchar(25), answerText varchar(5000))";

		Connection conn = null;
		Statement stmt = null;
        try
        {
            conn = ServiceLocator.locate().getConnection();
			stmt = conn.createStatement();

			if (logger.isDebugEnabled())
            {
                logger.debug("MainTableSQL - " + mainTableSql);
                logger.debug("DescTableSQL - " + descTableSql);
//                logger.debug("TextTableSQL - " + textTableSql);
            }

			stmt.execute(mainTableSql);
			stmt.execute(descTableSql);
//			stmt.execute(textTableSql);
	    }
        catch (SQLException e)
        {
            logger.error("Error while creating tables specific to the survey" + " :: " + e.getMessage());
            if (logger.isDebugEnabled())
    		{
            	logger.debug(e.getMessage(), e);
    		}
            throw new Exception();
        }
        finally
        {
            try
            {
		        if(stmt != null)
		        {
		            stmt.close();
		        }
		        if(conn != null)
		        {
		        	conn.close();
		        }
            }
            catch(Exception e)
            {
            }
        }
    }

    public  SurveyDefinitionManager getSurveyDefinitionManager(int surveyPk) throws Exception
    {
        Survey survey = surveyMaster.getSurveyByPk(surveyPk);
        SurveyDefinitionManager surveyMgr = new SurveyDefinitionManager(survey);

		return surveyMgr;
    }

    public  SurveyDefinitionManager getSurveyDefinitionManager(SurveyDefinition surveyDef) throws Exception
    {
        SurveyDefinitionManager surveyMgr = null;

        surveyMgr = new SurveyDefinitionManager(surveyDef.getSurveyConfig());

		return surveyMgr;
    }
}