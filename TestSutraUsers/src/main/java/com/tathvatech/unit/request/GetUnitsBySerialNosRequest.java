package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetUnitsBySerialNosRequest {
  private   String[] serialNos;
   private ProjectOID projectOID;
}
