package com.tathvatech.survey.intf;


import com.tathvatech.survey.enums.AnswerPersistor;
import com.tathvatech.survey.response.SurveyItemResponse;

public interface SurveySaveItemBase {

	String getSurveyItemId();
    SurveyItemBase getParent();

	AnswerPersistor getPersistor(SurveyItemResponse answer);

}