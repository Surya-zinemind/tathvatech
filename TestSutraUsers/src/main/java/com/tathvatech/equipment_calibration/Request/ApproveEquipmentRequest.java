package com.tathvatech.equipment_calibration.Request;

import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import lombok.Data;

@Data public class ApproveEquipmentRequest {
    private EquipmentOID equipmentOID;
    private String message;
}
