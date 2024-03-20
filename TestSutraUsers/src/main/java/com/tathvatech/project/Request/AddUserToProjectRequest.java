package com.tathvatech.project.Request;

import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class AddUserToProjectRequest {
    private int projectPk;
    private WorkstationOID workstationOID;
    private int userPk;
    private String role;
}
