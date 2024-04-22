package com.tathvatech.survey.Request;

import com.tathvatech.forms.response.FormResponseBean;
import lombok.Data;

@Data
public class SaveSyncErrorResponseRequest {
    private int responseId;
    private FormResponseBean formResponseBean;
}
