package com.tathvatech.equipment_calibration.report;

import com.tathvatech.common.common.EntitySelectorItem;
import com.tathvatech.common.utils.ListStringUtil;
import com.tathvatech.equipment_calibration.utils.DateFormatter;

import java.util.Date;
import java.util.TimeZone;

public class EquipmentListReportRow implements EntitySelectorItem
{
    private int pk;
    private int equipmentTypeFk;
    private int siteFk;
    private String siteName;
    private String equipmentType;
    private String equipmentId;
    private String reference;
    private String serialNo;
    private String modelNo;
    private String description;
    private String instruction;
    private String manufacturer;
    private Date dateOfPurchase;
    private Integer custodianFk;
    private String custodianName;
    private String status;
    private String calibrationReferenceNo;
    private int calibrationAuthorityFk;
    private String calibrationAutority;
    private int calibrationIntervalFk;
    private String calibrationInterval;
    private Date lastCalibrationDate;
    private Date nextCalibrationDate;
    private String calibationStatus;
    private String calibratedByName;
    private int locationFk;
    private String location;
    private String approvedByName;
    private Date approvedDate;
    private Date statusUpdatedDate;
    private String createdByName;
    private Date createdDate;
    private Float cost;
    private String currency;
    private String calibrationComment;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public int getEquipmentTypeFk()
    {
        return equipmentTypeFk;
    }

    public void setEquipmentTypeFk(int equipmentTypeFk)
    {
        this.equipmentTypeFk = equipmentTypeFk;
    }

    public String getEquipmentType()
    {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType)
    {
        this.equipmentType = equipmentType;
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

    public Integer getCustodianFk()
    {
        return custodianFk;
    }

    public void setCustodianFk(Integer custodianFk)
    {
        this.custodianFk = custodianFk;
    }

    public String getCustodianName()
    {
        return custodianName;
    }

    public void setCustodianName(String custodianName)
    {
        this.custodianName = custodianName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getCalibrationAuthorityFk()
    {
        return calibrationAuthorityFk;
    }

    public void setCalibrationAuthorityFk(int calibrationAuthorityFk)
    {
        this.calibrationAuthorityFk = calibrationAuthorityFk;
    }

    public String getCalibrationAutority()
    {
        return calibrationAutority;
    }

    public void setCalibrationAutority(String calibrationAutority)
    {
        this.calibrationAutority = calibrationAutority;
    }

    public int getCalibrationIntervalFk()
    {
        return calibrationIntervalFk;
    }

    public void setCalibrationIntervalFk(int calibrationIntervalFk)
    {
        this.calibrationIntervalFk = calibrationIntervalFk;
    }

    public String getCalibrationInterval()
    {
        return calibrationInterval;
    }

    public void setCalibrationInterval(String calibrationInterval)
    {
        this.calibrationInterval = calibrationInterval;
    }

    public Date getLastCalibrationDate()
    {
        return lastCalibrationDate;
    }

    public void setLastCalibrationDate(Date lastCalibrationDate)
    {
        this.lastCalibrationDate = lastCalibrationDate;
    }

    public Date getNextCalibrationDate()
    {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(Date nextCalibrationDate)
    {
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public String getCalibationStatus()
    {
        return calibationStatus;
    }

    public void setCalibationStatus(String calibationStatus)
    {
        this.calibationStatus = calibationStatus;
    }

    public int getLocationFk()
    {
        return locationFk;
    }

    public void setLocationFk(int locationFk)
    {
        this.locationFk = locationFk;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getCalibrationReferenceNo()
    {
        return calibrationReferenceNo;
    }

    public void setCalibrationReferenceNo(String calibrationReferenceNo)
    {
        this.calibrationReferenceNo = calibrationReferenceNo;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public String getApprovedByName()
    {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName)
    {
        this.approvedByName = approvedByName;
    }

    public Date getApprovedDate()
    {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate)
    {
        this.approvedDate = approvedDate;
    }

    public int getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(int siteFk)
    {
        this.siteFk = siteFk;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public Date getStatusUpdatedDate()
    {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(Date statusUpdatedDate)
    {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    public String getCreatedByName()
    {
        return createdByName;
    }

    public void setCreatedByName(String createdByName)
    {
        this.createdByName = createdByName;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public Float getCost()
    {
        return cost;
    }

    public void setCost(Float cost)
    {
        this.cost = cost;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public String getCalibratedByName()
    {
        return calibratedByName;
    }

    public void setCalibratedByName(String calibratedByName)
    {
        this.calibratedByName = calibratedByName;
    }


    public String getCalibrationComment()
    {
        return calibrationComment;
    }

    public void setCalibrationComment(String calibrationComment)
    {
        this.calibrationComment = calibrationComment;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return (getPk() == ((EquipmentListReportRow) obj).getPk());
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    public String getShortString()
    {
        return getEquipmentId();
    }

    public String getLongString()
    {
        return toString();
    }

    @Override
    public String toString()
    {
        DateFormatter dateFormatter = DateFormatter.getInstance(TimeZone.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("EquipmentId: ").append(getEquipmentId());
        sb.append(", Type: ").append(ListStringUtil.showString(getEquipmentType()));
        sb.append(", Serial No: ").append(ListStringUtil.showString(getSerialNo()));
        sb.append(", Valid until: ");
        if(nextCalibrationDate != null)
            sb.append(dateFormatter.formatDate(nextCalibrationDate));
        else
            sb.append("Not specified");
        return  sb.toString();
    }
}
