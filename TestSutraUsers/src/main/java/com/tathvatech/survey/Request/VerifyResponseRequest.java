package com.tathvatech.survey.Request;

import com.tathvatech.survey.response.SurveyResponse;
import lombok.Data;

@Data
public class VerifyResponseRequest {
    private Integer formPk;
    private String comments;
}
