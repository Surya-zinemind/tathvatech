package com.tathvatech.survey.inf;

import com.tathvatech.ts.core.survey.SurveyDefinitionBase;

public interface SurveyParamBase {

	void setSurveyDefinition(SurveyDefinitionBase surveyDefinition);

	void setDataType(int pType);

	void setName(String pName);

	String getName();

	int getDataType();

}
