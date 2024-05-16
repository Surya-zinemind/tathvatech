package com.tathvatech.injuryReport.oid;


import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.OID;

public class InjuryOID extends OID {

    public InjuryOID(int pk, String displayText)
    {
        super(pk, EntityTypeEnum.Injury, displayText);
    }

    @Override
    public boolean equals(Object obj)
    {
        try
        {
            if(((InjuryOID)obj).getPk() == getPk())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (int) super.getPk();
    }

}

