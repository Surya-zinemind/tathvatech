package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class GetUnitsWithWorkstationsAssignedRequest {
   private ProjectOID projectOID;
   private WorkstationOID workstationOID;
}
