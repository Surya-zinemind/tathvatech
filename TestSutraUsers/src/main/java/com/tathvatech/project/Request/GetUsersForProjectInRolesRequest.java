package com.tathvatech.project.Request;

import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class GetUsersForProjectInRolesRequest {
    private int projectPk;
    private WorkstationOID workstationOID;
    private String roleName;
}
