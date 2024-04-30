package com.tathvatech.equipment_calibration.Request;

import com.tathvatech.equipment_calibration.common.EquipmentCalibrationBean;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import com.tathvatech.user.common.UserContext;
import lombok.Data;

@Data
public class SaveCalibrationDataRequest {
    private EquipmentOID equipmentOID;
    private EquipmentCalibrationBean caliBean;

}
