package com.tathvatech.unit.request;

import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class CreateUnitAtWorkstationRequest {
   private  ProjectOID projectOID;
   private WorkstationOID workstationOID;
   private UnitBean unit;
   private boolean copyPartSpecificFormsToWorkstation;
   private boolean pendingReview;
}
