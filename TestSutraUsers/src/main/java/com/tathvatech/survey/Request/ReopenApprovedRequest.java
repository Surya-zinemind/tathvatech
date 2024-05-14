package com.tathvatech.survey.Request;

import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

@Data
public class ReopenApprovedRequest {
    private SurveyResponse surveyResponse;
    private String comments;
}
