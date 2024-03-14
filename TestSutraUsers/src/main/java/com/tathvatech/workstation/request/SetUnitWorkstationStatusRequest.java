package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class SetUnitWorkstationStatusRequest {
   private ProjectOID projectOID;
    private int unitPk;
    private WorkstationOID workstationOID;
    private String status;
}
