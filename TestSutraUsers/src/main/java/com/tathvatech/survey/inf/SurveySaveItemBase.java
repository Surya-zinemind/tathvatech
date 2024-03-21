package com.tathvatech.survey.inf;


import com.tathvatech.survey.response.SurveyItemResponse;

public interface SurveySaveItemBase {

	String getSurveyItemId();
    SurveyItemBase getParent();

	AnswerPersistor getPersistor(SurveyItemResponse answer);

}
