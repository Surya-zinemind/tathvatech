package com.tathvatech.forms.common;

import com.tathvatech.survey.common.SurveyItem;
import com.tathvatech.survey.response.SurveyItemResponse;


public interface FormEventListner
{
	public void fillDataEvent(SurveyItem sitem, String[] messages);
	
	public void formCancelled();
	
	public void formSubmitted(SurveyItemResponse itemResponse);
}
