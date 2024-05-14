package com.tathvatech.survey.Request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateResponseRequest {
    private SurveyResponse surveyResponse;
    private List surveyQuestions;
}
