package com.tathvatech.forms.request;

import com.tathvatech.user.OID.TestProcOID;
import lombok.Data;

@Data
public class UpgradeFormForUnitRequest {
    private TestProcOID testProcOID;
    private int surveyPk;
}
