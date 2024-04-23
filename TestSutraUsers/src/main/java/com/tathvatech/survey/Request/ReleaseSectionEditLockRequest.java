package com.tathvatech.survey.Request;

import com.tathvatech.forms.oid.FormResponseOID;
import lombok.Data;

@Data
public class ReleaseSectionEditLockRequest {
    private FormResponseOID responseOID;
    private String sectionId;
}
