package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class WorkstationToUnitRequest {
  private   ProjectOID projectOID;
   private UnitOID unitOID;
   private WorkstationOID workstationOID;
}

