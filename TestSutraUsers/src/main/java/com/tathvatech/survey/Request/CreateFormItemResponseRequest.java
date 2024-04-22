package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class CreateFormItemResponseRequest {
    private int responseId;
    private String surveyItemId;
}
