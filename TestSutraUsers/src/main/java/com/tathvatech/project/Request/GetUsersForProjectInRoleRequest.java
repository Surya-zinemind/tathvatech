package com.tathvatech.project.Request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetUsersForProjectInRoleRequest {
    private ProjectOID projectOID;
    private  String roleName;
}
