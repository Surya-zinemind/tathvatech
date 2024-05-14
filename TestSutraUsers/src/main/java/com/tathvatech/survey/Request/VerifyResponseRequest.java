package com.tathvatech.survey.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyResponseRequest {
    private SurveyResponse surveyResponse;
    private String comments;
}
