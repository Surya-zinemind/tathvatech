package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

@Data
public class UnitWorkstationStatusRequest {
   private UnitOID unitOID;
   private  ProjectOID projectOID;
}
