package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class GetResponseMastersForRespondentRequest {
    private int surveyPk;
   private int respondentPk;
}
