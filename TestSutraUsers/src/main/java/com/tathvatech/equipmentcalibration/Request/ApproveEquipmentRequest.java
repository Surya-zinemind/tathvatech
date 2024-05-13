package com.tathvatech.equipmentcalibration.Request;

import com.tathvatech.equipmentcalibration.oid.EquipmentOID;
import lombok.Data;

@Data public class ApproveEquipmentRequest {
    private EquipmentOID equipmentOID;
    private String message;
}
