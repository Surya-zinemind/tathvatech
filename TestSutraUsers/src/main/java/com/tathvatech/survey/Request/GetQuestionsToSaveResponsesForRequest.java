package com.tathvatech.survey.Request;

import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.survey.common.Section;
import com.tathvatech.survey.common.SurveyDefinition;
import lombok.Data;

import java.util.List;

@Data
public class GetQuestionsToSaveResponsesForRequest {
    private SurveyDefinition surveyDef;
    private List<Section> surveyQuestions;
    private FormResponseOID responseOID;
}
