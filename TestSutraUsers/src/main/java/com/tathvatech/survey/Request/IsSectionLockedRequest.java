package com.tathvatech.survey.Request;

import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class IsSectionLockedRequest {
    private User user;
    private FormResponseOID responseOID;
    private String sectionId;
}
