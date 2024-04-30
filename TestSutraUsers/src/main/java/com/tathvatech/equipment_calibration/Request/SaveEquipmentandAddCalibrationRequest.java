package com.tathvatech.equipment_calibration.Request;

import com.tathvatech.equipment_calibration.common.EquipmentBean;
import com.tathvatech.equipment_calibration.common.EquipmentCalibrationBean;
import lombok.Data;

@Data
public class SaveEquipmentandAddCalibrationRequest {
    private EquipmentBean equipmentBean;
   private EquipmentCalibrationBean calibrationBean;

}
