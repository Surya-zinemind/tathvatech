package com.tathvatech.equipmentcalibration.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_h")
public class EquipmentH extends  AbstractEntity implements Serializable
{
   @Id
   private long pk;
    private Integer equipmentFK;
    private Integer custodianFk;
    private String status;
    private Date statusUpdatedDate;
    private Integer authorityType;
    private Integer calibrationAuthorityFk;
    private Integer calibrationIntervalFk;
    private int siteFk;
    private Integer locationFk;
    private Date effectiveFrom;
    private Date effectiveTo;
    private int createdBy;
    private Float cost;
    private Date createdDate;
    private Date lastUpdated;

    private String equipmentId;
    private String serialNo;
    private String reference;
    private String instruction;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public Integer getEquipmentFK()
    {
        return equipmentFK;
    }

    public void setEquipmentFK(Integer equipmentFK)
    {
        this.equipmentFK = equipmentFK;
    }

    public Integer getCustodianFk()
    {
        return custodianFk;
    }

    public void setCustodianFk(Integer custodianFk)
    {
        this.custodianFk = custodianFk;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }



    public Integer getAuthorityType()
    {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType)
    {
        this.authorityType = authorityType;
    }

    public Integer getCalibrationAuthorityFk()
    {
        return calibrationAuthorityFk;
    }

    public void setCalibrationAuthorityFk(Integer calibrationAuthorityFk)
    {
        this.calibrationAuthorityFk = calibrationAuthorityFk;
    }

    public Integer getCalibrationIntervalFk()
    {
        return calibrationIntervalFk;
    }

    public void setCalibrationIntervalFk(Integer calibrationIntervalFk)
    {
        this.calibrationIntervalFk = calibrationIntervalFk;
    }

    public Integer getLocationFk()
    {
        return locationFk;
    }

    public void setLocationFk(Integer locationFk)
    {
        this.locationFk = locationFk;
    }

    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }

    public Date getEffectiveTo()
    {
        return effectiveTo;
    }

    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
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

    public int getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(int siteFk)
    {
        this.siteFk = siteFk;
    }

    public String getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public Float getCost()
    {
        return cost;
    }

    public void setCost(Float cost)
    {
        this.cost = cost;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public Date getStatusUpdatedDate()
    {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(Date statusUpdatedDate)
    {
        this.statusUpdatedDate = statusUpdatedDate;
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

