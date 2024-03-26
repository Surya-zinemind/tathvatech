package com.tathvatech.unit.request;

import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

@Data
public class IsUsersForUnitInRoleRequest {
   private ProjectOID projectOID;
   private int userPk, int unitPk;
   private WorkstationOID workstationOID;
   private String roleName;
}
