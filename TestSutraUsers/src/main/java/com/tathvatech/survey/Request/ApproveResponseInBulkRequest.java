package com.tathvatech.survey.Request;

import com.tathvatech.forms.common.AssignedTestsQuery;
import lombok.Data;

import java.util.List;

@Data
public class ApproveResponseInBulkRequest {
    private List<AssignedTestsQuery> selectedList;
    private String comment;
}
