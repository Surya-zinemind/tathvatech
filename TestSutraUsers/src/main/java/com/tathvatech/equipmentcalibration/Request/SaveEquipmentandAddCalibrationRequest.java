package com.tathvatech.equipmentcalibration.Request;

import com.tathvatech.equipmentcalibration.common.EquipmentBean;
import com.tathvatech.equipmentcalibration.common.EquipmentCalibrationBean;
import lombok.Data;

@Data
public class SaveEquipmentandAddCalibrationRequest {
    private EquipmentBean equipmentBean;
   private EquipmentCalibrationBean calibrationBean;

}
