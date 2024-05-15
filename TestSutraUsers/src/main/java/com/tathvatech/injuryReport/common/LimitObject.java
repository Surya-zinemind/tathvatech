package com.tathvatech.injuryReport.common;

import com.tathvatech.ts.core.accounts.UserOID;

public class LimitObject
{
    private UserOID userOID;
    private DateLimit limit;

    public LimitObject(UserOID userOID, DateLimit limit)
    {
        super();
        this.userOID = userOID;
        this.limit = limit;
    }

    public UserOID getUserOID()
    {
        return userOID;
    }

    public void setUserOID(UserOID userOID)
    {
        this.userOID = userOID;
    }

    public DateLimit getLimit()
    {
        return limit;
    }

    public void setLimit(DateLimit limit)
    {
        this.limit = limit;
    }
}

