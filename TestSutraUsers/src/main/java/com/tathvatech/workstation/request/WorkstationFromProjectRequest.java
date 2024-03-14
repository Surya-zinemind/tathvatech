package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class WorkstationFromProjectRequest {

   private  int projectPk;
  private  WorkstationOID workstationOID;

}
