package com.tathvatech.unit.request;

import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.workstation.common.WorkstationQuery;
import lombok.Data;

@Data
public class SetWorkstationProjectPartTeamSetupToUnitsRequest {
  private  ProjectQuery projectQuery;
   private WorkstationQuery workstationQuery;
   private ProjectPartOID projectPartOID;
   private Integer[] selectedUnits;

  private  boolean copyDefaultTeamIfNoProjectPartTeamIsSet;
}
