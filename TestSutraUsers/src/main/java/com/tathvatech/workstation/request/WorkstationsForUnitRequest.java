package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class WorkstationsForUnitRequest {
    private int unitPk;
   private ProjectOID projectOID;
   private boolean includeChildUnits;
}
