package com.tathvatech.injuryReport.request;

import lombok.Data;

@Data
public class DeleteAssignAfterTreatmentRequest {
    private int injuryPk;
    private int AfterTreatmentMasterPk;
}
