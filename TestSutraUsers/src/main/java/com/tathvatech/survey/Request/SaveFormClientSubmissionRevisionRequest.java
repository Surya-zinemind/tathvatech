package com.tathvatech.survey.Request;

import lombok.Data;

@Data
public class SaveFormClientSubmissionRevisionRequest {
    private int responseId;
    private String revision;
}
