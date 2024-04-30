package com.tathvatech.equipment_calibration.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeName("EquipmentTypeOID")
public class EquipmentTypeOID extends OID
{
    @JsonCreator
    public EquipmentTypeOID(@JsonProperty("pk") int pk)
    {
        super(pk, EntityTypeEnum.EquipmentType, null);
    }

    public EquipmentTypeOID(int pk, String displayText)
    {
        super(pk, EntityTypeEnum.EquipmentType, displayText);
    }


    @Override
    public boolean equals(Object obj)
    {
        try
        {
            if (((EquipmentTypeOID) obj).getPk() == getPk())
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
