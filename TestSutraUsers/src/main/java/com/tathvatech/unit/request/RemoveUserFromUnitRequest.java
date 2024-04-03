package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class RemoveUserFromUnitRequest {
    private int userPk;
    private int unitPk;
    private ProjectOID projectOID;
    private WorkstationOID workstationOID;
    private String role;
}
