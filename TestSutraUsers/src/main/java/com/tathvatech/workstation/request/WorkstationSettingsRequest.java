package com.tathvatech.workstation.request;

import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

import java.util.List;

@Data
public class WorkstationSettingsRequest {
   private ProjectOID copyFromProjectOID;
   private ProjectOID destinationProjectOID;
   private boolean copySites;
 private   boolean copyProjectFunctionTeams;
   private boolean copyParts;
  private  boolean copyOpenItemTeam;
  private  boolean copyProjectCoordinators;
  private  List<Object[]> workstationsToCopy;
}
