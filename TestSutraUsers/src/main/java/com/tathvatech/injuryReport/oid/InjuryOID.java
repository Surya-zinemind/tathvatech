package com.tathvatech.injuryReport.oid;

import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;

public class InjuryOID extends OID{

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
        return super.getPk();
    }

}

