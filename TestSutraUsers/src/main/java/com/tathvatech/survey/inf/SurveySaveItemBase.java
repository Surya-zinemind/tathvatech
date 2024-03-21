package com.tathvatech.survey.inf;

import com.tathvatech.ts.core.survey.response.AnswerPersistor;
import com.tathvatech.ts.core.survey.response.SurveyItemResponse;




public interface SurveySaveItemBase {

	String getSurveyItemId();
    SurveyItemBase getParent();

	AnswerPersistor getPersistor(SurveyItemResponse answer);

}
