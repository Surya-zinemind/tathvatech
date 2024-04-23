package com.tathvatech.survey.Request;

import com.tathvatech.forms.common.FormQuery;
import com.tathvatech.survey.entity.Survey;
import lombok.Data;

@Data
public class CreateSurveyNewVersionRequest {
    private Survey newVersion;
    private FormQuery baseRevision;
}
