package com.tathvatech.unit.request;

import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

import java.util.List;

@Data
public class AddUnitToProjectRequest {
   private ProjectOID sourceProjectOID;
   private ProjectOID destinationProjectOID;
   private UnitBean rootUnitBean;
   private List<com.tathvatech.unit.common.UnitBean> unitBeanAndChildrenList;
  private  boolean addAsPlannedUnit;
}
