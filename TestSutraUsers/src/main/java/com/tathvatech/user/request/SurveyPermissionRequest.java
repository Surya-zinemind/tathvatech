package com.tathvatech.user.request;

import lombok.Data;

import java.util.List;

@Data
public class SurveyPermissionRequest {
    private int surveyPk;
    private List permsList;
}
