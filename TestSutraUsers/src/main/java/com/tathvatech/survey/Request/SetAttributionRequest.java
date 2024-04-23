package com.tathvatech.survey.Request;

import com.tathvatech.common.enums.WorkItem;
import com.tathvatech.user.OID.UserOID;
import lombok.Data;

@Data
public class SetAttributionRequest {
    private WorkItem workItem;
    private UserOID attributeToUserOID;
}
