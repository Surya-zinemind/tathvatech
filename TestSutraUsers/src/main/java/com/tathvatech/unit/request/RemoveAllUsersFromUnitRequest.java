package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class RemoveAllUsersFromUnitRequest {
   private UnitOID unitOID;
   private ProjectOID projectOID;

   private WorkstationOID workstationOID;
}
