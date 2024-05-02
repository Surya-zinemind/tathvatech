package com.tathvatech.survey.Request;

import com.tathvatech.forms.response.ResponseMasterNew;
import lombok.Data;

@Data
public class ApproveResponseRequest {
    private Integer responseId;
    private String comments;
}
