package com.tathvatech.equipment_calibration.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_type")
public class EquipmentType extends AbstractEntity implements Serializable
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long pk;
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

    public EquipmentTypeOID getOID()
    {
        return new EquipmentTypeOID((int) pk, name);
    }

    @Override
    public int hashCode()
    {

        return (int) pk;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof EquipmentType))
        {
            return false;
        }
        return (getPk() == ((EquipmentType) obj).getPk());
    }

}

