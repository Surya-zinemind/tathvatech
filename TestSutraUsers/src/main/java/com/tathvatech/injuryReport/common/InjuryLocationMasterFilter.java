package com.tathvatech.injuryReport.common;

import java.util.Date;

public class InjuryLocationMasterFilter
{
    private String searchString; // this is usually a user entered string, match this to purchase order number in case of Purchase Order

    private Date fromDate;
    private Date toDate;
    public String getSearchString()
    {
        return searchString;
    }
    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }
    public Date getFromDate()
    {
        return fromDate;
    }
    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }
    public Date getToDate()
    {
        return toDate;
    }
    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

}

