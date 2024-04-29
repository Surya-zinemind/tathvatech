package com.tathvatech.survey.Request;

import com.tathvatech.survey.common.SurveyDefinition;
import lombok.Data;

@Data
public class GetSurveyResponseRequest {
    private Integer formPk;
    private int responseId;
}
