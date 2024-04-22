package com.tathvatech.survey.Request;

import com.tathvatech.forms.response.ResponseMasterNew;
import lombok.Data;

@Data
public class AddToResponseVACommentsRequest {
    private ResponseMasterNew responseMaster;
    private String vCommentToAdd;
    private String aCommentToAdd;
}
