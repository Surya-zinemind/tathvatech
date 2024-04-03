package com.tathvatech.unit.request;

import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class GetUnitsWithWorkstationsAssignedsRequest {
  private  ProjectOID projectOID;
  private ProjectPartOID projectPartOID;
  private  WorkstationOID workstationOID;
}
