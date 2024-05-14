/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.common;

import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ApplicationProperties
{
	private static Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

	private static ApplicationProperties instance;

	private static Document configDoc;
	
	static String mail_testMode = "true";
	static String enableEmail = "true";
	
	static String defaultTimezone = "UTC";
	static int SeqIdFetchLotSize =  10;
	
	public static String DRAFT_WATERMARK_FILENAME = "watermark_draft.png";
	public static String APPLICATION_HEADER_LOGO_FILENAME = "application_header_logo_file.png";
	public static String PRINT_HEADER_LOGO_FILENAME = "print_header_logo_file.png";
	public static String LOGIN_PAGE_LOGOFILE = "login_page_logo_file.png";
	public static String HISTORYBOOK_ZIP_DOWNLOAD_TEMPLATE_FILE = "HistoryBookZipDownloadTemplate.zip";
	public static String welcomeKitFileName = "TestSutra-QuickStart.wmv";
	
	public static ApplicationProperties getInstance()
	{
		if(instance == null)
		{
			throw new RuntimeException("Application configuration not loaded. makesure application-config.xml is located in the /WEB-INF directory");
		}
		return instance;
	}
	public static int getSeqIdFetchLotSize()
	{
		return Integer.parseInt(getPropertyFromConfig("config/SeqIdFetchLotSize", ""+SeqIdFetchLotSize));
	}
	public static String getDefaultTimezone()
	{
		return getPropertyFromConfig("config/DefaultTimezone", defaultTimezone);
	}
	public static String getDBTimezone()
	{
		return getPropertyFromConfig("config/DBTimezone", defaultTimezone);
	}
	public static String isEmailEnabled()
	{
		return getPropertyFromConfig("config/email/enable", "false");
	}
	public static String isEmailInTestMode()
	{
		return getPropertyFromConfig("config/email/TestMode", mail_testMode);
	}
	public static String getEmailTestToAddress()
	{
		return getPropertyFromConfig("config/email/TestToAddress", null);
	}
	public static String getOutgoingMailServer()
	{
		return getPropertyFromConfig("config/email/OutgoingServer", null);
	}
	public static String getOutgoingMailServerPort()
	{
		return getPropertyFromConfig("config/email/OutgoingServerPort", null);
	}
	public static String getOutgoingMailServerUsername()
	{
		return getPropertyFromConfig("config/email/OutgoingServerUsername", null);
	}
	public static String getOutgoingMailServerPassword()
	{
		return getPropertyFromConfig("config/email/OutgoingServerPassword", null);
	}
	public static String getEmailFromAddress()
	{
		return getPropertyFromConfig("config/email/FromAddress", null);
	}
	public static String getClientNickName()
	{
		return getPropertyFromConfig("config/clientdata/ClientNickName", "");
	}
	public static String getClientShortName()
	{
		return getPropertyFromConfig("config/clientdata/ClientShortName", "");
	}
	public static String getClientFullName()
	{
		return getPropertyFromConfig("config/clientdata/ClientFullName", "");
	}
	public static String getPrintFooterLine1()
	{
		return getPropertyFromConfig("config/clientdata/PrintFooterLine1", "Company copyright information");
	}
	public static  String getVechicleAcceptance_openItemListHeading1_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_openItemListHeading1_default", "");
	}
	public static String getVechicleAcceptance_openItemListHeading2_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_openItemListHeading2_default", "");
	}

	
	
	
	
	
	public static String getVechicleAcceptance_openItemListFooter_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_openItemListFooter_default", "");
	}
	public static String getVechicleAcceptance_authorizedSignatory_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_authorizedSignatory_default", "");
	}
	public static String getVechicleAcceptance_signatoryTitle_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_signatoryTitle_default", "");
	}
	public static String getVechicleAcceptance_certificateTitle1_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_certificateTitle1_default", "");
	}
	public static String getVechicleAcceptance_certificateTitle2_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_certificateTitle2_default", "");
	}
	public static String getVechicleAcceptance_certificateText_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_certificateText_default", "");
	}
	public static String getVechicleAcceptance_attachmentDesc_default()
	{
		return getPropertyFromConfig("config/clientdata/vechicleAcceptance_attachmentDesc_default", "");
	}
	
	
	public static String getHistoryBookPreparedByDefault()
	{
		return getPropertyFromConfig("config/clientdata/historyBookPreparedBy_default", "");
	}
	public static String getHistoryBookProcessStructurePrefix_default()
	{
		return getPropertyFromConfig("config/clientdata/historyBookProcessStructurePrefix_default", "");
	}
	public static String getHistoryBookConfidentialityStatus_default()
	{
		return getPropertyFromConfig("config/clientdata/historyBookConfidentialityStatus_default", "");
	}
	
	public static String getPdfPrintFont()
	{
		return getResourceFileRoot()+ "arial-unicode.ttf";
	}
	
	public static String getPropertyFromConfig(String configName, String defaultVal)
	{
		String val = ApplicationProperties.getProperty(configName);
		if(val == null)
			val = defaultVal;
		
		return val;
	}
	public static String getProperty(String name)
	{
		String result = null;
		try
		{
				Node node = XPathAPI.selectSingleNode(configDoc, name);

			if(node == null)
			{
				logger.warn("Can't find the node: " + name);
				result = null;
			}
			else if (node instanceof Attr)
			{
				result = ((Attr) node).getValue();
			}
			else
			{
				if(node.getFirstChild() != null)
				{
					result = node.getFirstChild().getNodeValue();
				}
				else
					result = null;
			}
		} catch (Exception e)
		{
			logger.error(String.format("Exception while retreiving CoreConfig. XPATH <%s> ",name), e);
		} 

//		if(result == null)
//		{
//			result = ApplicationProperties.getInstance().getProp(name);
//		}
		return result;

	}
	
	public static void init(InputStream iStream)
	{
		if(instance != null)
		{
			return;
		}
		
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(false);
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			configDoc = db.parse(iStream);
		}
		catch (Exception exp)
		{
			logger.error("Exception while parsing application-config.xml", exp);
		}

	
	}

	public static String getFormDefRoot()throws Exception
	{
		Context ctx = new InitialContext();
		return (String) ctx.lookup("java:comp/env/formdefroot");
	}
	public static String getResourceFileRoot()
	{
		try
		{
			Context ctx = new InitialContext();
			return (String) ctx.lookup("java:comp/env/resourceroot");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getFileRoot()
	{
		try
		{
			Context ctx = new InitialContext();
	//		return (String) ctx.lookup("java:comp/env/fileroot");
			return (String) ctx.lookup("java:comp/env/imageroot"); 
			// i am making this the same so that the android app can request an image and a file the same way..
			// got to have the name imageroot changed..and fileroot removed from the config
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static String getReportDefRoot()throws Exception
	{
		Context ctx = new InitialContext();
		return (String) ctx.lookup("java:comp/env/reportdefroot");
	}
	public static String getTempFileRoot()throws Exception
	{
		Context ctx = new InitialContext();
		return (String) ctx.lookup("java:comp/env/tempfileroot");
	}
}
