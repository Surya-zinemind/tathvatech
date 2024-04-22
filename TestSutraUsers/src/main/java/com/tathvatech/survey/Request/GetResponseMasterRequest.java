package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class GetResponseMasterRequest {
    private int surveyPk;
    private String responseId;
}
