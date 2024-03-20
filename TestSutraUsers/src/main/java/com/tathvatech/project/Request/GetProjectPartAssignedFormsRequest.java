package com.tathvatech.project.Request;

import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

@Data
public class GetProjectPartAssignedFormsRequest {
    private ProjectOID projectOID;
    private ProjectPartOID projectPartOID;
}
