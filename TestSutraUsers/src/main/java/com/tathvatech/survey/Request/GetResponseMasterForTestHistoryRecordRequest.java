package com.tathvatech.survey.Request;

import com.tathvatech.user.OID.FormOID;
import com.tathvatech.user.OID.TestProcOID;
import lombok.Data;

@Data
public class GetResponseMasterForTestHistoryRecordRequest {
    private TestProcOID testProcOID;
    private FormOID formOID;
}
