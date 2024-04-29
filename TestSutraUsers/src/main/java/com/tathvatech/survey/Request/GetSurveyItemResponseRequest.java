package com.tathvatech.survey.Request;

import com.tathvatech.forms.response.ResponseMaster;
import com.tathvatech.survey.common.SurveyDefinition;
import lombok.Data;

@Data
public class GetSurveyItemResponseRequest {
    private Integer formPk;
   private String surveyItemId;
   private ResponseMaster[] responseMasterSet;
}
