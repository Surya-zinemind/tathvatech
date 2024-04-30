package com.tathvatech.equipment_calibration.common;

import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UserOID;

import java.io.Serializable;
import java.util.Date;

public class CalibrationSiteCurrencyBean extends BaseResponseBean implements Serializable, Cloneable
{
    private int pk;
    private SiteOID siteOID;
    private String currency;
    private String abbreviation;
    private Integer estatus;
    private UserOID createdBy;
    private Date createdDate;
    private Date lastUpdated;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public SiteOID getSiteOID()
    {
        return siteOID;
    }

    public void setSiteOID(SiteOID siteOID)
    {
        this.siteOID = siteOID;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public Integer getEstatus()
    {
        return estatus;
    }

    public void setEstatus(Integer estatus)
    {
        this.estatus = estatus;
    }

    public UserOID getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(UserOID createdBy)
    {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return (getPk() == ((CalibrationSiteCurrencyBean) obj).getPk());
    }

    @Override
    public int hashCode()
    {
        return Serializable.super.hashCode();
    }

    @Override
    public String toString()
    {
        return getSiteOID().getDisplayText() + " - " + getCurrency();
    }

}
