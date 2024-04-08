package com.tathvatech.forms.common;

import com.tathvatech.survey.common.SurveyItem;


public interface FormDesignListener
{

	void formItemConfigurationComplete(SurveyItem surveyItem) throws Exception;
    void deleteFormItem(String questionId)throws Exception;
	void formItemConfigurationCancelled(SurveyItem surveyItem);
	void itemExpanded();
	void itemCollapsed();
}
