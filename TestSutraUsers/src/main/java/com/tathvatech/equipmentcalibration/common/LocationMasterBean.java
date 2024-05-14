package com.tathvatech.equipmentcalibration.common;

import com.tathvatech.equipmentcalibration.entity.LocationType;
import com.tathvatech.equipmentcalibration.oid.LocationOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.entity.Site;

import java.io.Serializable;
import java.util.Date;

public class LocationMasterBean extends BaseResponseBean implements Serializable, Cloneable
{
    private int pk;
    private LocationType locationType;
    private Site site;
    private LocationOID parentOID;
    private String name;
    private String description;
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

    public Site getSite()
    {

        return site;
    }

    public void setSite(Site site)
    {

        this.site = site;
    }

    public LocationOID getParentOID()
    {
        return parentOID;
    }

    public void setParentOID(LocationOID parentOID)
    {
        this.parentOID = parentOID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public LocationType getLocationType()
    {
        return locationType;
    }

    public void setLocationType(LocationType locationType)
    {
        this.locationType = locationType;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof LocationMasterBean))
        {
            return false;
        }
        return (getPk() == ((LocationMasterBean) obj).getPk());
    }

    @Override
    public int hashCode()
    {
        return Serializable.super.hashCode();
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public LocationOID getOID() {
        return new LocationOID(pk, site.getName() + "_" + locationType.getName() + "_" + name);
    }



}
