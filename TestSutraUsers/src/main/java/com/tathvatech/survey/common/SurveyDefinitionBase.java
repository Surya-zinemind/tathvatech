package com.tathvatech.survey.common;

import com.tathvatech.survey.entity.Survey;
import com.tathvatech.survey.inf.SurveyItemBase;
import org.jdom2.Element;

import java.util.List;



public interface SurveyDefinitionBase {

	List loadSurveyItems(Element element, SurveyItemBase surveyItem);

	void addFlags(String flags);
	
	public List getFlags();

    public Survey getSurveyConfig();
    

}
