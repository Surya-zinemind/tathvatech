package com.tathvatech.survey.Request;

import com.tathvatech.forms.oid.FormResponseOID;
import com.tathvatech.user.entity.User;
import lombok.Data;

@Data
public class GetLockedSectionIdsRequest {
    private User user;
    private FormResponseOID responseOID;
}
