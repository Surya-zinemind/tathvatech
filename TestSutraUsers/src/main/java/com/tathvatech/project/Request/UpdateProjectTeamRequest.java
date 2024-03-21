package com.tathvatech.project.Request;

import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.WorkstationOID;
import lombok.Data;

import java.util.Collection;

@Data
public class UpdateProjectTeamRequest {
  private   ProjectOID projectOID;
  private ProjectPartOID projectPartOID;
  private   WorkstationOID wsOID;
   private  Collection testList;
   private  Collection verifyList;
   private  Collection approveList;
}
