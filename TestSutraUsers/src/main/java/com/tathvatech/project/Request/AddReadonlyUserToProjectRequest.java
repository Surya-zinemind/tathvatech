package com.tathvatech.project.Request;

import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class AddReadonlyUserToProjectRequest {
    private int projectPk;
    private WorkstationOID workstationOID;
    private  int userPk;
}
