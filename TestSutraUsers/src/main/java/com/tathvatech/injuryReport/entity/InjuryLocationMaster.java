package com.tathvatech.injuryReport.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.LocationTypeOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "injury_location_master")
public class InjuryLocationMaster extends AbstractEntity implements Serializable
{
    @Id
    private long pk;
    private String name;
    private String status;
    private Integer parentPk;
    private Date createdDate;
    private Date lastUpdated;


    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getParentPk()
    {
        return parentPk;
    }

    public void setParentPk(Integer parentPk)
    {
        this.parentPk = parentPk;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
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
    public LocationTypeOID getOID()
    {
        return new LocationTypeOID((int) pk, name);
    }

}

