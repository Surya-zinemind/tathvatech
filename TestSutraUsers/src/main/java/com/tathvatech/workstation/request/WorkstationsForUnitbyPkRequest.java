package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class WorkstationsForUnitbyPkRequest {
  private  int unitPk;
   private ProjectOID projectOID;
}
