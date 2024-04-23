package com.tathvatech.survey.Request;

import com.tathvatech.survey.entity.Survey;
import lombok.Data;

@Data
public class CreateSurveyByCopyRequest {
    private Survey survey;
    private int sourceSurveyPk;
}
