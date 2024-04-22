package com.tathvatech.survey.Request;

import com.tathvatech.survey.common.SurveyDefinition;
import lombok.Data;

@Data
public class GetSectionResponseSummaryRequest {
    private SurveyDefinition sd;
    private int responseId;
}
