package com.tathvatech.survey.common;

import java.util.List;

import org.jdom.Element;

import com.tathvatech.ts.core.survey.surveyitem.SurveyItemBase;

public interface SurveyDefinitionBase {

	List loadSurveyItems(Element element, SurveyItemBase surveyItem);

	void addFlags(String flags);
	
	public List getFlags();

    public Survey getSurveyConfig();
    

}
