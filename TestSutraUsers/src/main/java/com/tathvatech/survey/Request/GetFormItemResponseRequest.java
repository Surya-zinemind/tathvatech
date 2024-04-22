package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class GetFormItemResponseRequest {
    private int responseId;
    private String surveyItemId;
    private boolean createIfNoExist;
}
