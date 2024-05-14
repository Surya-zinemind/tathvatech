package com.tathvatech.equipmentcalibration.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class EquipmentOID extends OID
{
    @JsonCreator
    public EquipmentOID(@JsonProperty("pk") int pk)
    {
        super(pk, EntityTypeEnum.Equipment, null);
    }

    public EquipmentOID(int pk, String displayText)
    {
        super(pk, EntityTypeEnum.Equipment, displayText);
    }


    @Override
    public boolean equals(Object obj)
    {
        try
        {
            if (((EquipmentOID) obj).getPk() == getPk())
            {
                return true;
            } else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return (int) getPk();
    }
}
