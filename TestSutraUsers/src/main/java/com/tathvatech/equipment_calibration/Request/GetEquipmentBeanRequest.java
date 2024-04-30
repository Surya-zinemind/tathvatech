package com.tathvatech.equipment_calibration.Request;

import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import com.tathvatech.user.OID.SiteOID;
import lombok.Data;

@Data
public class GetEquipmentBeanRequest {
   private  SiteOID siteOID;
   private EquipmentTypeOID equipmentTypeOID;
    private String serialNumber;
}
