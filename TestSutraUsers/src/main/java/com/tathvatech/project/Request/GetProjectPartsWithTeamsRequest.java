package com.tathvatech.project.Request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class GetProjectPartsWithTeamsRequest {
    private ProjectOID projectOID;
    private WorkstationOID workstationOID;
}
