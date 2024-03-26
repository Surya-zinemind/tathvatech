package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetUnitsByPksRequest {
   private int[] unitPks;
    private ProjectOID projectOID;
}
