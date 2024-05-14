package com.tathvatech.equipmentcalibration.enums;

public enum EquipmentStatusEnum {
    Active("Active"), ActiveCalibrationNotRequired("Active - Calibration not required"), BeingCalibrated(
            "Being calibrated"), InActive("InActive"), Obsolete("Obsolete"), UnderRepair("Under repair");

    EquipmentStatusEnum(String displayString)
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
        for (EquipmentStatusEnum e : EquipmentStatusEnum.values())
        {
            if (code.equals(e.name()))
                return e.getDisplayString();
        }
        return null;
    }

    public static EquipmentStatusEnum fromDisplayString(String displayString)
    {
        EquipmentStatusEnum[] vals = EquipmentStatusEnum.values();
        for (int i = 0; i < vals.length; i++)
        {
            if (vals[i].displayString.equalsIgnoreCase(displayString))
                return vals[i];
        }
        return null;
    }
}
