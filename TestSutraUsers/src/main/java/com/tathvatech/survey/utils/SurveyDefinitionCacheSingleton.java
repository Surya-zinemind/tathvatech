package com.tathvatech.survey.utils;

import com.tathvatech.survey.common.SurveyDefinition;
import com.tathvatech.survey.service.SurveyDefFactory;
import com.tathvatech.user.OID.FormOID;
import org.apache.commons.collections4.map.LRUMap;



public class SurveyDefinitionCacheSingleton
{
	private SurveyDefFactory surveyDefFactory;
	private static SurveyDefinitionCacheSingleton instance;
	private LRUMap<FormOID, SurveyDefinition> sdMap = new LRUMap<FormOID, SurveyDefinition>(200);
	
	private SurveyDefinitionCacheSingleton()
	{
	}
	
	public static SurveyDefinitionCacheSingleton getInstance()
	{
		if(instance != null)
			return instance;
		else
		{
			synchronized (SurveyDefinitionCacheSingleton.class)
			{
				if(instance != null)
					return instance;
				else
				{
					instance = new SurveyDefinitionCacheSingleton();
					return instance;
				}
			}
		}
	}
	public synchronized SurveyDefinition getSurveyDefinition(FormOID formOID) throws Exception
	{
		SurveyDefinition sd = sdMap.get(formOID);
		if(sd == null)
		{
			sd = surveyDefFactory.getSurveyDefinition(formOID);
			sdMap.put(formOID, sd);
		}
		return sd;
	}

	public synchronized void clear()
	{
		sdMap.clear();
	}
}
