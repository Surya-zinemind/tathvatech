package com.tathvatech.equipmentcalibration.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "equipment_calibration")
public class EquipmentCalibration extends AbstractEntity implements Serializable
{
    @Id
    private long pk;
    private int equipmentFk;
    private String calibrationSequenceNo;
    private String calibrationReferenceNo;
    private Date calibrationDate;
    private Date nextCalibrationDate;
    private Integer calibrationAuthorityFk;
    private String calibationStatus;
    private int createdBy;
    private Date createdDate;
    private String comment;
    private int current;
    private int authorityType;
    private Date lastUpdated;

    @Override
    public long getPk() {
        return pk;
    }

    public void setPk(long pk) {
        this.pk = pk;
    }

    public int getEquipmentFk()
    {
        return equipmentFk;
    }

    public void setEquipmentFk(int equipmentFk)
    {
        this.equipmentFk = equipmentFk;
    }

    public String getCalibrationReferenceNo()
    {
        return calibrationReferenceNo;
    }

    public void setCalibrationReferenceNo(String calibrationReferenceNo)
    {
        this.calibrationReferenceNo = calibrationReferenceNo;
    }

    public Date getCalibrationDate()
    {
        return calibrationDate;
    }

    public void setCalibrationDate(Date calibrationDate)
    {
        this.calibrationDate = calibrationDate;
    }

    public Date getNextCalibrationDate()
    {
        return nextCalibrationDate;
    }

    public void setNextCalibrationDate(Date nextCalibrationDate)
    {
        this.nextCalibrationDate = nextCalibrationDate;
    }

    public Integer getCalibrationAuthorityFk()
    {
        return calibrationAuthorityFk;
    }

    public void setCalibrationAuthorityFk(Integer calibrationAuthorityFk)
    {
        this.calibrationAuthorityFk = calibrationAuthorityFk;
    }

    public String getCalibationStatus()
    {
        return calibationStatus;
    }

    public void setCalibationStatus(String calibationStatus)
    {
        this.calibationStatus = calibationStatus;
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

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getCurrent()
    {
        return current;
    }

    public void setCurrent(int current)
    {
        this.current = current;
    }


    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public String getCalibrationSequenceNo()
    {
        return calibrationSequenceNo;
    }

    public void setCalibrationSequenceNo(String calibrationSequenceNo)
    {
        this.calibrationSequenceNo = calibrationSequenceNo;
    }

    public int getAuthorityType()
    {
        return authorityType;
    }

    public void setAuthorityType(int authorityType)
    {
        this.authorityType = authorityType;
    }

}

