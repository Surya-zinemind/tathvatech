package com.tathvatech.unit.request;

import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

@Data
public class GetUnitsForProjectRequest {
    private UnitFilterBean unitFilter;
    private UnitOID parent;
}
