package com.tathvatech.equipmentcalibration.enums;

import org.springframework.context.annotation.Bean;



public enum CalibrationStatusEnum {
    InCalibration("In calibration"), RecalibrationMinor("Recalibration minor adjustments"), RecalibrationMajor(
            "Recalibration major adjustments"), NonCalibratable(
            "Non calibratable"), InitialCalibration("Initial calibration");
    CalibrationStatusEnum(String displayString)
    {
        this.displayString = displayString;
    }

   public String getDisplayString()
    {
        return displayString;
    }

    private String displayString;

    public static String getDisplayString(String code)
    {
        for (CalibrationStatusEnum e : CalibrationStatusEnum.values())
        {
            if (code.equals(e.name()))
                return e.getDisplayString();
        }
        return null;
    }
}
