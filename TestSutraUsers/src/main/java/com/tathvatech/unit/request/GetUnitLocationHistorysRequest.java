package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

@Data
public class GetUnitLocationHistorysRequest {
   private UnitOID unitOID;
   private ProjectOID projectOID;

   private boolean includeChildren;
}
