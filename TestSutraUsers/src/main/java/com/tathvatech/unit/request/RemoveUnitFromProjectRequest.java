package com.tathvatech.unit.request;

import com.tathvatech.unit.enums.CommonEnums;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.UnitOID;
import lombok.Data;

@Data
public class RemoveUnitFromProjectRequest {
    private UnitOID unitOID;
    private ProjectOID projectOID;
   private  CommonEnums.DeleteOptionEnum deleteUnitOption;
}
