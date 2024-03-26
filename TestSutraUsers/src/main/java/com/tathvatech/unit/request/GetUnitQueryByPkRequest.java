package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetUnitQueryByPkRequest {
    private int unitPk;
    private ProjectOID projectOID;
}
