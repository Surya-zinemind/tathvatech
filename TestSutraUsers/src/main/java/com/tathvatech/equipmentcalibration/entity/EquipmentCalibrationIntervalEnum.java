package com.tathvatech.equipmentcalibration.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.user.enums.DBEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "equipment_calibration_interval_m")
public class EquipmentCalibrationIntervalEnum extends DBEnum
{
    @Id
    private long pk;
    private String name;
    private String description;
    private int estatus;
    private Date createdDate;
    private int createdBy;
    private Date lastUpdated;

    public EquipmentCalibrationIntervalEnum(PersistWrapper persistWrapper)
    {
        super(persistWrapper);

    }
    public EquipmentCalibrationIntervalEnum(@JsonProperty("pk") int pk,PersistWrapper persistWrapper)
    {
        super(persistWrapper);
        this.pk = pk;
    }

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

    public int getEstatus()
    {
        return estatus;
    }

    public void setEstatus(int estatus)
    {
        this.estatus = estatus;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(int createdBy)
    {
        this.createdBy = createdBy;
    }


    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getDisplayText()
    {
        return getName() + " - " + getDescription();
    }
}
