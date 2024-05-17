package com.tathvatech.injuryReport.request;

import com.tathvatech.injuryReport.oid.InjuryOID;
import lombok.Data;

@Data
public class VerifyInjuryReportRequest {
    private InjuryOID injuryOID;
    private String message;
}
