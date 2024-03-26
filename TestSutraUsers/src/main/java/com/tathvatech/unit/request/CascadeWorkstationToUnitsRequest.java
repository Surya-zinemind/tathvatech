package com.tathvatech.unit.request;

import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.workstation.common.WorkstationQuery;
import lombok.Data;

@Data
public class CascadeWorkstationToUnitsRequest {

   private ProjectQuery projectQuery;
   private WorkstationQuery workstationQuery;
   private Integer[] selectedUnitsForForm;
   private Integer[] selectedUnitsForTeam;

}
