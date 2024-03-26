package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

@Data
public class GetUnitLocationHistoryRequest {
    private UnitOID unitOID;
    private ProjectOID projectOID;
}
