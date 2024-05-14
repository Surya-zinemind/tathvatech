package com.tathvatech.equipmentcalibration.common;

import java.util.Date;

public class EquipmentObj
{
    private int pk;
    private String equipmentId;
    private Integer equipmentTypeFk;
    private String reference;
    private String serialNo;
    private String modelNo;
    private String description;
    private String instruction;
    private String manufacturer;
    private Date dateOfPurchase;
    private Integer estatus;
    private int createdBy;
    private Date createdDate;

    private Integer custodianFk;
    private String status;
    private Date statusUpdatedDate;
    private Integer authorityType;
    private Integer calibrationAuthorityFk;
    private Integer calibrationIntervalFk;
    private Integer locationFk;
    private Float cost;
    private int siteFk;
    private Date effectiveFrom;
    private Date effectiveTo;
    private int upddatdBy;
    private Date updatedDate;
    private Date lastUpdated;
    private Integer approvedBy;
    private Date approvedDate;
    private String approvedComment;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getEquipmentId()
    {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId)
    {
        this.equipmentId = equipmentId;
    }

    public Integer getEquipmentTypeFk()
    {
        return equipmentTypeFk;
    }

    public void setEquipmentTypeFk(Integer equipmentTypeFk)
    {
        this.equipmentTypeFk = equipmentTypeFk;
    }

    public String getSerialNo()
    {
        return serialNo;
    }

    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }

    public String getModelNo()
    {
        return modelNo;
    }

    public void setModelNo(String modelNo)
    {
        this.modelNo = modelNo;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public Date getDateOfPurchase()
    {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase)
    {
        this.dateOfPurchase = dateOfPurchase;
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

    public int getUpddatdBy()
    {
        return upddatdBy;
    }

    public void setUpddatdBy(int upddatdBy)
    {
        this.upddatdBy = upddatdBy;
    }

    public Date getUpdatedDate()
    {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate)
    {
        this.updatedDate = updatedDate;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public int getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(int siteFk)
    {
        this.siteFk = siteFk;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public Integer getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy)
    {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate()
    {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate)
    {
        this.approvedDate = approvedDate;
    }

    public String getApprovedComment()
    {
        return approvedComment;
    }

    public void setApprovedComment(String approvedComment)
    {
        this.approvedComment = approvedComment;
    }

    public Float getCost()
    {
        return cost;
    }

    public void setCost(Float cost)
    {
        this.cost = cost;
    }



    public Integer getAuthorityType()
    {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType)
    {
        this.authorityType = authorityType;
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

}

