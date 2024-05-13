package com.tathvatech.equipmentcalibration.report;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tathvatech.equipmentcalibration.enums.CalibrationStatusEnum;
import com.tathvatech.equipmentcalibration.enums.EquipmentStatusEnum;
import com.tathvatech.equipmentcalibration.oid.EquipmentOID;
import com.tathvatech.equipmentcalibration.oid.EquipmentTypeOID;
import com.tathvatech.equipmentcalibration.oid.LocationOID;
import com.tathvatech.forms.common.TestProcFilter;
import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.DateRangeFilter;

import java.util.List;

public class EquipmentListFilter extends ReportFilter implements Cloneable
{
    public enum APPROVEDSTATUS {
        DRAFT, APPROVED
    };
    public static enum PendingforApprovalLimit {
        BEFORE1MONTH,BEFORE5DAYS, ALL
    }
    private EquipmentOID equipmentOID;
    private String equipmentId;
    private String description;;
    private String searchString;
    private String serialNo;
    private String reference;
    private List<UserOID> custodian;
    private List<EquipmentTypeOID> equipmentTypes;
    private List<EquipmentStatusEnum> equipmentStatusList;
    private List<CalibrationStatusEnum> calibrationStatusList;
    private List<Integer> equipmentAuthority;
    private List<LocationOID> locations;
    private DateRangeFilter overdueOn;
    private APPROVEDSTATUS approvedStatus;
    private List<UserOID> approvedBy;
    private List<SiteOID> siteOIDs;
    private DateRangeFilter createdDate;
    private List<UserOID> pendingApprovalForCalibrationCoordinator;
    private List<UserOID> pendingApprovalForSiteAdmin;

    private PendingforApprovalLimit pendingForApprovalLimit;
    public String getSearchString()
    {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }

    public EquipmentOID getEquipmentOID()
    {
        return equipmentOID;
    }

    public void setEquipmentOID(EquipmentOID equipmentOID)
    {
        this.equipmentOID = equipmentOID;
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

    public List<EquipmentTypeOID> getEquipmentTypes()
    {
        return equipmentTypes;
    }

    public void setEquipmentTypes(List<EquipmentTypeOID> equipmentTypes)
    {
        this.equipmentTypes = equipmentTypes;
    }

    public List<EquipmentStatusEnum> getEquipmentStatusList()
    {
        return equipmentStatusList;
    }

    public void setEquipmentStatusList(List<EquipmentStatusEnum> equipmentStatusList)
    {
        this.equipmentStatusList = equipmentStatusList;
    }

    public List<CalibrationStatusEnum> getCalibrationStatusList()
    {
        return calibrationStatusList;
    }

    public void setCalibrationStatusList(List<CalibrationStatusEnum> calibrationStatusList)
    {
        this.calibrationStatusList = calibrationStatusList;
    }

    public List<UserOID> getCustodian()
    {
        return custodian;
    }

    public void setCustodian(List<UserOID> custodian)
    {
        this.custodian = custodian;
    }

    public List<Integer> getEquipmentAuthority()
    {
        return equipmentAuthority;
    }

    public void setEquipmentAuthority(List<Integer> equipmentAuthority)
    {
        this.equipmentAuthority = equipmentAuthority;
    }

    public List<LocationOID> getLocations()
    {
        return locations;
    }

    public void setLocations(List<LocationOID> locations)
    {
        this.locations = locations;
    }

    public DateRangeFilter getOverdueOn()
    {
        return overdueOn;
    }

    public void setOverdueOn(DateRangeFilter overdueOn)
    {
        this.overdueOn = overdueOn;
    }

    public List<UserOID> getApprovedBy()
    {
        return approvedBy;
    }

    public void setApprovedBy(List<UserOID> approvedBy)
    {
        this.approvedBy = approvedBy;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public APPROVEDSTATUS getApprovedStatus()
    {
        return approvedStatus;
    }

    public void setApprovedStatus(APPROVEDSTATUS approvedStatus)
    {
        this.approvedStatus = approvedStatus;
    }

    public List<SiteOID> getSiteOIDs()
    {
        return siteOIDs;
    }

    public void setSiteOIDs(List<SiteOID> siteOIDs)
    {
        this.siteOIDs = siteOIDs;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public DateRangeFilter getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(DateRangeFilter createdDate)
    {
        this.createdDate = createdDate;
    }

    public List<UserOID> getPendingApprovalForCalibrationCoordinator()
    {
        return pendingApprovalForCalibrationCoordinator;
    }

    public void setPendingApprovalForCalibrationCoordinator(List<UserOID> pendingApprovalForCalibrationCoordinator)
    {
        this.pendingApprovalForCalibrationCoordinator = pendingApprovalForCalibrationCoordinator;
    }

    public List<UserOID> getPendingApprovalForSiteAdmin()
    {
        return pendingApprovalForSiteAdmin;
    }

    public void setPendingApprovalForSiteAdmin(List<UserOID> pendingApprovalForSiteAdmin)
    {
        this.pendingApprovalForSiteAdmin = pendingApprovalForSiteAdmin;
    }

    public PendingforApprovalLimit getPendingForApprovalLimit()
    {
        return pendingForApprovalLimit;
    }

    public void setPendingForApprovalLimit(PendingforApprovalLimit pendingForApprovalLimit)
    {
        this.pendingForApprovalLimit = pendingForApprovalLimit;
    }

}
