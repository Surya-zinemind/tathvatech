package com.tathvatech.equipmentcalibration.Request;

import com.tathvatech.equipmentcalibration.common.EquipmentCalibrationBean;
import com.tathvatech.equipmentcalibration.oid.EquipmentOID;
import lombok.Data;

@Data
public class SaveCalibrationDataRequest {
    private EquipmentOID equipmentOID;
    private EquipmentCalibrationBean caliBean;

}
