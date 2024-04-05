/*
 * Created on Apr 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.survey.common;

import com.tathvatech.survey.response.SimpleSurveyItemResponse;
import com.tathvatech.survey.response.SurveyItemResponse;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SurveyItemResponseFactory extends SimpleSurveyItemResponse
{
    public static SurveyItemResponse getSurveyItemResponse(SurveySaveItem sItem)
    {
        return new SimpleSurveyItemResponse();
    }
}
