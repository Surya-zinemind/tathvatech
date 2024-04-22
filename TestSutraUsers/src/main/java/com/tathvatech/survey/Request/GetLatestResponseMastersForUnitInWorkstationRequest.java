package com.tathvatech.survey.Request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class GetLatestResponseMastersForUnitInWorkstationRequest {
    private int unitPk;
    private ProjectOID projectOID;
    private WorkstationOID workstationOID;
}
