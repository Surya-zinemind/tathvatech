package com.tathvatech.equipmentcalibration.oid;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class LocationOID extends OID
{
    @JsonCreator
    public LocationOID(@JsonProperty("pk") int pk)
    {
        super(pk, EntityTypeEnum.Location, null);
    }

    public LocationOID(int pk, String displayText)
    {
        super(pk, EntityTypeEnum.Location, displayText);
    }


    @Override
    public boolean equals(Object obj)
    {
        try
        {
            if (((LocationOID) obj).getPk() == getPk())
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
