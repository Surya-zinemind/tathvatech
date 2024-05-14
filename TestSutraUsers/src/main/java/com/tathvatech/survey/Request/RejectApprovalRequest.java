package com.tathvatech.survey.Request;

import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

@Data
public class RejectApprovalRequest {
    private SurveyResponse surveyResponse;
    private String comments;
}
