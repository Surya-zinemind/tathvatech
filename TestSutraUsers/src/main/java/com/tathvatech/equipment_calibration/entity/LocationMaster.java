package com.tathvatech.equipment_calibration.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.Authorizable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "location_master")
public class LocationMaster extends AbstractEntity implements Serializable {
    @Id
    private long pk;
    private int siteFk;
    private int locationTypeFk;
    private Integer parentPk;
    private String name;
    private String description;
    private Integer estatus;
    private int createdBy;
    private Date createdDate;
    private Date lastUpdated;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(int siteFk)
    {
        this.siteFk = siteFk;
    }

    public int getLocationTypeFk()
    {
        return locationTypeFk;
    }

    public void setLocationTypeFk(int locationTypeFk)
    {
        this.locationTypeFk = locationTypeFk;
    }

    public Integer getParentPk()
    {
        return parentPk;
    }

    public void setParentPk(Integer parentPk)
    {
        this.parentPk = parentPk;
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

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
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

}
