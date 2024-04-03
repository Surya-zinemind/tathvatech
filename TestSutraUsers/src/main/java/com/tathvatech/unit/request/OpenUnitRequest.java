package com.tathvatech.unit.request;

import com.tathvatech.unit.common.UnitBean;
import com.tathvatech.user.OID.ProjectOID;
import lombok.Data;

import java.util.List;

@Data
public class OpenUnitRequest {

   private UnitBean rootUnitToOpen;
   private List<UnitBean> unitBeanAndChildrenList;
   private ProjectOID lastOpenProjectOID;
    private ProjectOID destinationProjectOID;
}
