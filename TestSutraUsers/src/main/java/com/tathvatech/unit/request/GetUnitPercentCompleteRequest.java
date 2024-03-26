package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetUnitPercentCompleteRequest {
  private   int unitPk;
  private ProjectOID projectOID;
   private boolean includeChildren;
}
