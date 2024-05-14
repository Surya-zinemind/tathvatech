package com.tathvatech.survey.Request;

import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

@Data
public class RejectResponseRequest {
    private SurveyResponse surveyResponse;
    private String comments;
}
