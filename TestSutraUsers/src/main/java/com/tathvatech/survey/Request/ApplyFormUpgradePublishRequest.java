package com.tathvatech.survey.Request;

import lombok.Data;

import java.util.List;

@Data
public class ApplyFormUpgradePublishRequest {
    private int surveyPk;
    private List projectListToUpgrade;
    private List unitFormsListToUpgrade;
}
