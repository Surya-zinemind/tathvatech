package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class GetSectionResponseSummarysRequest {
    private int responseId;
    private String sectionId;
}
