package com.tathvatech.equipmentcalibration.Request;

import com.tathvatech.equipmentcalibration.oid.EquipmentTypeOID;
import com.tathvatech.user.OID.SiteOID;
import lombok.Data;

@Data
public class GetEquipmentBeanRequest {
   private  SiteOID siteOID;
   private EquipmentTypeOID equipmentTypeOID;
    private String serialNumber;
}
