package com.tathvatech.equipmentcalibration.common;
import com.tathvatech.equipmentcalibration.common.EquipmentBean.AUTHORITY_TYPE;

import com.tathvatech.equipmentcalibration.entity.EquipmentCalibrationAuthority;
import com.tathvatech.equipmentcalibration.enums.CalibrationStatusEnum;
import com.tathvatech.user.OID.UserOID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

public class EquipmentCalibrationBean {
    private int pk;
    private int equipmentFk;
    private String calibrationSequenceNo;
    private String calibrationReferenceNo;
    private Date calibrationDate;
    private Date nextCalibrationDate;
    private EquipmentCalibrationAuthority calibrationAuthority;
    private CalibrationStatusEnum calibationStatus;
    private UserOID createdBy;
    private Date createdDate;
    private String comment;

    private EquipmentBean.AUTHORITY_TYPE authorityType;
    private Date lastUpdated;
    private AttachmentInfoBean attachment;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getCalibrationSequenceNo()
    {
        return calibrationSequenceNo;
    }

    public void setCalibrationSequenceNo(String calibrationSequenceNo)
    {
        this.calibrationSequenceNo = calibrationSequenceNo;
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

    public EquipmentCalibrationAuthority getCalibrationAuthority()
    {
        return calibrationAuthority;
    }

    public void setCalibrationAuthority(EquipmentCalibrationAuthority calibrationAuthority)
    {
        this.calibrationAuthority = calibrationAuthority;
    }

    public CalibrationStatusEnum getCalibationStatus()
    {
        return calibationStatus;
    }

    public void setCalibationStatus(CalibrationStatusEnum calibationStatus)
    {
        this.calibationStatus = calibationStatus;
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

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public AttachmentInfoBean getAttachment()
    {
        return attachment;
    }

    public void setAttachment(AttachmentInfoBean attachment)
    {
        this.attachment = attachment;
    }

    public AUTHORITY_TYPE getAuthorityType()
    {
        return authorityType;
    }



    public void setAuthorityType( AUTHORITY_TYPE authorityType)
    {
        this.authorityType = authorityType;
    }

    public int getEquipmentFk()
    {
        return equipmentFk;
    }

    public void setEquipmentFk(int equipmentFk)
    {
        this.equipmentFk = equipmentFk;
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return (getPk() == ((EquipmentCalibrationBean) obj).getPk());
    }


}
