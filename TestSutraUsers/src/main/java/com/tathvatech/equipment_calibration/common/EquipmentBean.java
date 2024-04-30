package com.tathvatech.equipment_calibration.common;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibrationAuthority;
import com.tathvatech.equipment_calibration.entity.EquipmentCalibrationIntervalEnum;
import com.tathvatech.equipment_calibration.enums.EquipmentStatusEnum;
import com.tathvatech.equipment_calibration.oid.EquipmentOID;
import com.tathvatech.equipment_calibration.oid.EquipmentTypeOID;
import com.tathvatech.equipment_calibration.oid.LocationOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UserOID;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EquipmentBean extends BaseResponseBean implements Serializable, Cloneable
{
    public static enum AUTHORITY_TYPE {
        Internal(0), External(1);

        AUTHORITY_TYPE(int value)
        {
            this.intValue = value;
        }

        public int getIntValue()
        {
            return intValue;
        }

        private int intValue;

        public static Integer getDisplayString(String code)
        {
            for (AUTHORITY_TYPE e : AUTHORITY_TYPE.values())
            {
                if (code.equals(e.name()))
                    return e.getIntValue();
            }
            return null;
        }
    }

    private int pk;
    private String equipmentId;
    private EquipmentTypeOID equipmentTypeOID;
    private String serialNo;
    private String reference;
    private String modelNo;
    private String description;
    private String instruction;
    private String manufacturer;
    private Date dateOfPurchase;
    private EStatusEnum estatus;

    private UserOID custodianOID;
    private EquipmentStatusEnum status;
    private Date statusUpdatedDate;
    private AUTHORITY_TYPE authorityType;
    private EquipmentCalibrationAuthority calibrationAuthority;
    private EquipmentCalibrationIntervalEnum calibrationInterval;
    private int upddatdBy;
    private Date updatedDate;
    private Date lastUpdated;

    private EquipmentCalibrationBean calibrationBean;

    private SiteOID siteOID;
    private LocationOID locationOID;

    private UserOID approvedBy;
    private Date approvedDate;
    private String approvedComment;

    private UserOID createdBy;
    private Date createdDate;

    private Float cost;
    private List<AttachmentIntf> attachments;

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

    public EquipmentTypeOID getEquipmentTypeOID()
    {
        return equipmentTypeOID;
    }

    public void setEquipmentTypeOID(EquipmentTypeOID equipmentTypeOID)
    {
        this.equipmentTypeOID = equipmentTypeOID;
    }

    public UserOID getCustodianOID()
    {
        return custodianOID;
    }

    public void setCustodianOID(UserOID custodianOID)
    {
        this.custodianOID = custodianOID;
    }

    public EquipmentCalibrationAuthority getCalibrationAuthority()
    {
        return calibrationAuthority;
    }

    public void setCalibrationAuthority(EquipmentCalibrationAuthority calibrationAuthority)
    {
        this.calibrationAuthority = calibrationAuthority;
    }

    public EquipmentCalibrationIntervalEnum getCalibrationInterval()
    {
        return calibrationInterval;
    }

    public void setCalibrationInterval(EquipmentCalibrationIntervalEnum calibrationInterval)
    {
        this.calibrationInterval = calibrationInterval;
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

    public EStatusEnum getEstatus()
    {
        return estatus;
    }

    public void setEstatus(EStatusEnum estatus)
    {
        this.estatus = estatus;
    }

    public EquipmentStatusEnum getStatus()
    {
        return status;
    }

    public void setStatus(EquipmentStatusEnum status)
    {
        this.status = status;
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

    public EquipmentCalibrationBean getCalibrationBean()
    {
        return calibrationBean;
    }

    public void setCalibrationBean(EquipmentCalibrationBean calibrationBean)
    {
        this.calibrationBean = calibrationBean;
    }

    public SiteOID getSiteOID()
    {
        return siteOID;
    }

    public void setSiteOID(SiteOID siteOID)
    {
        this.siteOID = siteOID;
    }

    public LocationOID getLocationOID()
    {
        return locationOID;
    }

    public void setLocationOID(LocationOID locationOID)
    {
        this.locationOID = locationOID;
    }

    public UserOID getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy(UserOID approvedBy)
    {
        this.approvedBy = approvedBy;
    }

    public String getApprovedComment()
    {
        return approvedComment;
    }

    public void setApprovedComment(String approvedComment)
    {
        this.approvedComment = approvedComment;
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

    public Date getApprovedDate()
    {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate)
    {
        this.approvedDate = approvedDate;
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

    public AUTHORITY_TYPE getAuthorityType()
    {
        return authorityType;
    }

    public void setAuthorityType(AUTHORITY_TYPE authorityType)
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

    public List<AttachmentIntf> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<AttachmentIntf> attachments)
    {
        this.attachments = attachments;
    }

    public Date getStatusUpdatedDate()
    {
        return statusUpdatedDate;
    }

    public void setStatusUpdatedDate(Date statusUpdatedDate)
    {
        this.statusUpdatedDate = statusUpdatedDate;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        return (getPk() == ((EquipmentBean) obj).getPk());
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public String toString()
    {
        return getEquipmentId();
    }

    public EquipmentOID getOID()
    {
        return new EquipmentOID(getPk(), getEquipmentId());
    }

}
