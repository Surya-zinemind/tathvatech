/*
*Copyright (C) 2002 - 2003. All rights reserved.
*/

package com.tathvatech.common.metadata;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.tathvatech.ts.caf.util.ServiceLocator;

public class MetadataFactory
{
    private static Logger logger = Logger.getLogger(MetadataFactory.class);

    private static MetadataFactory instance = null;

    protected HashMap classMetadataMap = new HashMap();
    protected HashMap fieldMetadataMap = new HashMap();
    protected HashMap dataTypeConvertorMap = new HashMap();

	public static MetadataFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (MetadataFactory.class)
			{
				if (instance == null)
				{
					instance = new MetadataFactory();
					String metadataFileName = "";
					instance.loadMetadata(metadataFileName);
					if (logger.isDebugEnabled())
					{
						logger.debug("MetadataFactory instance created: " + instance);
					}
				}
			}
		}
		return instance;
	}

	private void loadMetadata(String entityConfigFileName)
	{
		SAXBuilder   builder = new SAXBuilder();
		try
		{

			Document     doc = builder.build(new File(entityConfigFileName));
//			Document     doc = builder.build(ClassLoader.getSystemResourceAsStream(fieldMetadataFileName));
			Element      fieldMetadataRoot = doc.getRootElement();

			logger.info("Loaded entity configurations...");

			Iterator     cIterator = fieldMetadataRoot.getChildren("entity-config").iterator();
			while (cIterator.hasNext())
			{
				Element      classElement = (Element) cIterator.next();

				//get the attributes for the class here
				String fileName    = classElement.getAttribute("name").getValue();
				MetadataLoader.loadMetadata(fileName, instance);
			}
		}
		catch (Throwable e)
		{
			logger.warn(e + " :: " + e.getMessage());
			if (logger.isDebugEnabled())
			{
				logger.debug(e.getMessage(), e);
			}
		}
	}

	public ClassMetadata getClassMetadata(String type)
    {
        return (ClassMetadata)classMetadataMap.get(type);
    }

    public FieldMetadata getFieldMetadata(String key)
    {
         return (FieldMetadata)fieldMetadataMap.get(key);
    }
    public String getDataTypeConvertor(String key)
    {
        return (String)dataTypeConvertorMap.get(key);
    }
}
