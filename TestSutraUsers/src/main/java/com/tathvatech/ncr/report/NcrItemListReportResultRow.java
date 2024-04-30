package com.tathvatech.ncr.report;

import com.tathvatech.ncr.oid.NcrItemOID;

import java.io.Serializable;
import java.util.Date;

public class NcrItemListReportResultRow implements Serializable
{
    private int pk;
    private String ncrNo;
    private String ncrDescription;
    private String ncrStatus;
    private Integer custodianPk;
    private String custodianName;
    private Integer createdBy;
    private Date createdDate;
    private String createdByName;
    private Integer cancelledBy;
    private Date cancelledDate;
    private String cancelledByName;
    private Integer publishedBy;
    private String publishedByName;
    private Date publishedDate;
    private String publishedComment;
    private Integer completedBy;
    private Date completedDate;
    private String completedByName;
    private String completedComment;
    private Integer closedBy;
    private String closedByName;
    private String closedDate;
    private String closedComment;
    private Integer lastRejectedBy;
    private String lastRejectedByName;
    private Date lastRejectedDate;
    private Integer lastApprovedBy;
    private String lastApprovedByName;
    private Date lastApprovedDate;
    private Float quantity;
    private Integer unitOfMeasureFk;
    private String unitOfMeasureName;
    private Integer areaOfResponsibilityFk;
    private String areaOfResponsibilityName;
    private Integer dispositionFk;
    private String disposition;
    private String dispositionComment;
    private String categoryFk;
    private String category;
    private String subCategoryFk;
    private String subCategory;
    private String mrfNo;
    private Date forecastStartDate;
    private Date forecastCompletionDate;
    private String carType;
    private Float estimatedHours;
    private String priority;
    private String severity;
    private String occurrence;
    private String detection;
    private String escape;
    private String reworkOrderFk;
    private String reworkOrderNo;
    private String rootCause;
    private String workInstructionOrComment;
    private String ppsor8d;
    private Integer sourceFk;
    private String sourceType;
    private String source;
    private String whereFoundFk;
    private String whereFoundType;
    private String whereFound;
    private Integer noOfDaysOpen;

    private Integer ncrGroupFk;
    private String ncrGroupStatus;
    private String ncrGroupDescription;
    private Integer siteFk;
    private String siteName;
    private String siteDescription;
    private Integer projectFk;
    private String projectName;
    private String projectDescription;
    private Integer partFk;
    private String partNo;
    private String partName;
    private String partDescription;
    private Integer partRevisionFk;
    private String partRevision;
    private Integer supplierFk;
    private String supplierId;
    private String supplierName;

    public Integer getLastRejectedBy()
    {
        return lastRejectedBy;
    }

    public void setLastRejectedBy(Integer lastRejectedBy)
    {
        this.lastRejectedBy = lastRejectedBy;
    }

    public String getLastRejectedByName()
    {
        return lastRejectedByName;
    }

    public void setLastRejectedByName(String lastRejectedByName)
    {
        this.lastRejectedByName = lastRejectedByName;
    }

    public Date getLastRejectedDate()
    {
        return lastRejectedDate;
    }

    public void setLastRejectedDate(Date lastRejectedDate)
    {
        this.lastRejectedDate = lastRejectedDate;
    }

    public Integer getLastApprovedBy()
    {
        return lastApprovedBy;
    }

    public void setLastApprovedBy(Integer lastApprovedBy)
    {
        this.lastApprovedBy = lastApprovedBy;
    }

    public String getLastApprovedByName()
    {
        return lastApprovedByName;
    }

    public void setLastApprovedByName(String lastApprovedByName)
    {
        this.lastApprovedByName = lastApprovedByName;
    }

    public Date getLastApprovedDate()
    {
        return lastApprovedDate;
    }

    public void setLastApprovedDate(Date lastApprovedDate)
    {
        this.lastApprovedDate = lastApprovedDate;
    }

    public String getUnitOfMeasureName()
    {
        return unitOfMeasureName;
    }

    public void setUnitOfMeasureName(String unitOfMeasureName)
    {
        this.unitOfMeasureName = unitOfMeasureName;
    }

    public String getAreaOfResponsibilityName()
    {
        return areaOfResponsibilityName;
    }

    public void setAreaOfResponsibilityName(String areaOfResponsibilityName)
    {
        this.areaOfResponsibilityName = areaOfResponsibilityName;
    }

    public String getWhereFound()
    {
        return whereFound;
    }

    public void setWhereFound(String whereFound)
    {
        this.whereFound = whereFound;
    }

    public String getNcrGroupDescription()
    {
        return ncrGroupDescription;
    }

    public void setNcrGroupDescription(String ncrGroupDescription)
    {
        this.ncrGroupDescription = ncrGroupDescription;
    }

    public String getSiteDescription()
    {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription)
    {
        this.siteDescription = siteDescription;
    }

    public String getSupplierId()
    {
        return supplierId;
    }

    public void setSupplierId(String supplierId)
    {
        this.supplierId = supplierId;
    }

    public String getProjectDescription()
    {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription)
    {
        this.projectDescription = projectDescription;
    }

    public Integer getSiteFk()
    {
        return siteFk;
    }

    public void setSiteFk(Integer siteFk)
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

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public Integer getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy)
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

    public Integer getCancelledBy()
    {
        return cancelledBy;
    }

    public void setCancelledBy(Integer cancelledBy)
    {
        this.cancelledBy = cancelledBy;
    }

    public Date getCancelledDate()
    {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate)
    {
        this.cancelledDate = cancelledDate;
    }

    public Integer getNcrGroupFk()
    {
        return ncrGroupFk;
    }

    public void setNcrGroupFk(Integer ncrGroupFk)
    {
        this.ncrGroupFk = ncrGroupFk;
    }

    public String getPublishedComment()
    {
        return publishedComment;
    }

    public void setPublishedComment(String publishedComment)
    {
        this.publishedComment = publishedComment;
    }

    public String getClosedComment()
    {
        return closedComment;
    }

    public void setClosedComment(String closedComment)
    {
        this.closedComment = closedComment;
    }

    public Integer getUnitOfMeasureFk()
    {
        return unitOfMeasureFk;
    }

    public void setUnitOfMeasureFk(Integer unitOfMeasureFk)
    {
        this.unitOfMeasureFk = unitOfMeasureFk;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getPartNo()
    {
        return partNo;
    }

    public void setPartNo(String partNo)
    {
        this.partNo = partNo;
    }

    public String getPartName()
    {
        return partName;
    }

    public void setPartName(String partName)
    {
        this.partName = partName;
    }

    public String getPartDescription()
    {
        return partDescription;
    }

    public void setPartDescription(String partDescription)
    {
        this.partDescription = partDescription;
    }

    public String getSupplierName()
    {
        return supplierName;
    }

    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    public String getNcrNo()
    {
        return ncrNo;
    }

    public void setNcrNo(String ncrNo)
    {
        this.ncrNo = ncrNo;
    }

    public String getDisposition()
    {
        return disposition;
    }

    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(String subCategory)
    {
        this.subCategory = subCategory;
    }

    public String getMrfNo()
    {
        return mrfNo;
    }

    public void setMrfNo(String mrfNo)
    {
        this.mrfNo = mrfNo;
    }

    public String getCustodianName()
    {
        return custodianName;
    }

    public void setCustodianName(String custodianName)
    {
        this.custodianName = custodianName;
    }

    public Float getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Float quantity)
    {
        this.quantity = quantity;
    }

    public String getCreatedByName()
    {
        return createdByName;
    }

    public void setCreatedByName(String createdByName)
    {
        this.createdByName = createdByName;
    }

    public String getPublishedByName()
    {
        return publishedByName;
    }

    public void setPublishedByName(String publishedByName)
    {
        this.publishedByName = publishedByName;
    }

    public Date getPublishedDate()
    {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    public String getCompletedByName()
    {
        return completedByName;
    }

    public void setCompletedByName(String completedByName)
    {
        this.completedByName = completedByName;
    }

    public String getClosedByName()
    {
        return closedByName;
    }

    public String getCompletedComment()
    {
        return completedComment;
    }

    public void setCompletedComment(String completedComment)
    {
        this.completedComment = completedComment;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }

    public void setClosedByName(String closedByName)
    {
        this.closedByName = closedByName;
    }

    public String getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate(String closedDate)
    {
        this.closedDate = closedDate;
    }

    public String getCarType()
    {
        return carType;
    }

    public void setCarType(String carType)
    {
        this.carType = carType;
    }

    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getOccurrence()
    {
        return occurrence;
    }

    public void setOccurrence(String occurrence)
    {
        this.occurrence = occurrence;
    }

    public String getDetection()
    {
        return detection;
    }

    public void setDetection(String detection)
    {
        this.detection = detection;
    }

    public String getEscape()
    {
        return escape;
    }

    public void setEscape(String escape)
    {
        this.escape = escape;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getRootCause()
    {
        return rootCause;
    }

    public void setRootCause(String rootCause)
    {
        this.rootCause = rootCause;
    }

    public String getWorkInstructionOrComment()
    {
        return workInstructionOrComment;
    }

    public void setWorkInstructionOrComment(String workInstructionOrComment)
    {
        this.workInstructionOrComment = workInstructionOrComment;
    }

    public String getDispositionComment()
    {
        return dispositionComment;
    }

    public void setDispositionComment(String dispositionComment)
    {
        this.dispositionComment = dispositionComment;
    }

    public String getPpsor8d()
    {
        return ppsor8d;
    }

    public void setPpsor8d(String ppsor8d)
    {
        this.ppsor8d = ppsor8d;
    }

    public Integer getPartFk()
    {
        return partFk;
    }

    public void setPartFk(Integer partFk)
    {
        this.partFk = partFk;
    }

    public String getCancelledByName()
    {
        return cancelledByName;
    }

    public void setCancelledByName(String cancelledByName)
    {
        this.cancelledByName = cancelledByName;
    }

    public Integer getProjectFk()
    {
        return projectFk;
    }

    public void setProjectFk(Integer projectFk)
    {
        this.projectFk = projectFk;
    }

    public Integer getDispositionFk()
    {
        return dispositionFk;
    }

    public void setDispositionFk(Integer dispositionFk)
    {
        this.dispositionFk = dispositionFk;
    }

    public Integer getCustodianPk()
    {
        return custodianPk;
    }

    public void setCustodianPk(Integer custodianPk)
    {
        this.custodianPk = custodianPk;
    }

    public Integer getNoOfDaysOpen()
    {
        return noOfDaysOpen;
    }

    public void setNoOfDaysOpen(Integer noOfDaysOpen)
    {
        this.noOfDaysOpen = noOfDaysOpen;
    }

    public Integer getAreaOfResponsibilityFk()
    {
        return areaOfResponsibilityFk;
    }

    public void setAreaOfResponsibilityFk(Integer areaOfResponsibilityFk)
    {
        this.areaOfResponsibilityFk = areaOfResponsibilityFk;
    }

    public String getNcrDescription()
    {
        return ncrDescription;
    }

    public void setNcrDescription(String ncrDescription)
    {
        this.ncrDescription = ncrDescription;
    }

    public String getNcrStatus()
    {
        return ncrStatus;
    }

    public void setNcrStatus(String ncrStatus)
    {
        this.ncrStatus = ncrStatus;
    }

    public Integer getPublishedBy()
    {
        return publishedBy;
    }

    public void setPublishedBy(Integer publishedBy)
    {
        this.publishedBy = publishedBy;
    }

    public Integer getCompletedBy()
    {
        return completedBy;
    }

    public void setCompletedBy(Integer completedBy)
    {
        this.completedBy = completedBy;
    }

    public Integer getClosedBy()
    {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy)
    {
        this.closedBy = closedBy;
    }

    public String getCategoryFk()
    {
        return categoryFk;
    }

    public void setCategoryFk(String categoryFk)
    {
        this.categoryFk = categoryFk;
    }

    public String getSubCategoryFk()
    {
        return subCategoryFk;
    }

    public void setSubCategoryFk(String subCategoryFk)
    {
        this.subCategoryFk = subCategoryFk;
    }

    public Date getForecastStartDate()
    {
        return forecastStartDate;
    }

    public void setForecastStartDate(Date forecastStartDate)
    {
        this.forecastStartDate = forecastStartDate;
    }

    public Date getForecastCompletionDate()
    {
        return forecastCompletionDate;
    }

    public void setForecastCompletionDate(Date forecastCompletionDate)
    {
        this.forecastCompletionDate = forecastCompletionDate;
    }

    public Float getEstimatedHours()
    {
        return estimatedHours;
    }

    public void setEstimatedHours(Float estimatedHours)
    {
        this.estimatedHours = estimatedHours;
    }

    public String getReworkOrderFk()
    {
        return reworkOrderFk;
    }

    public void setReworkOrderFk(String reworkOrderFk)
    {
        this.reworkOrderFk = reworkOrderFk;
    }

    public String getReworkOrderNo()
    {
        return reworkOrderNo;
    }

    public void setReworkOrderNo(String reworkOrderNo)
    {
        this.reworkOrderNo = reworkOrderNo;
    }

    public Integer getSourceFk()
    {
        return sourceFk;
    }

    public void setSourceFk(Integer sourceFk)
    {
        this.sourceFk = sourceFk;
    }

    public String getSourceType()
    {
        return sourceType;
    }

    public void setSourceType(String sourceType)
    {
        this.sourceType = sourceType;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getWhereFoundFk()
    {
        return whereFoundFk;
    }

    public void setWhereFoundFk(String whereFoundFk)
    {
        this.whereFoundFk = whereFoundFk;
    }

    public String getWhereFoundType()
    {
        return whereFoundType;
    }

    public void setWhereFoundType(String whereFoundType)
    {
        this.whereFoundType = whereFoundType;
    }

    public String getNcrGroupStatus()
    {
        return ncrGroupStatus;
    }

    public void setNcrGroupStatus(String ncrGroupStatus)
    {
        this.ncrGroupStatus = ncrGroupStatus;
    }

    public Integer getPartRevisionFk()
    {
        return partRevisionFk;
    }

    public void setPartRevisionFk(Integer partRevisionFk)
    {
        this.partRevisionFk = partRevisionFk;
    }

    public String getPartRevision()
    {
        return partRevision;
    }

    public void setPartRevision(String partRevision)
    {
        this.partRevision = partRevision;
    }

    public Integer getSupplierFk()
    {
        return supplierFk;
    }

    public void setSupplierFk(Integer supplierFk)
    {
        this.supplierFk = supplierFk;
    }

    public NcrItemOID getOID()
    {
        return new NcrItemOID(pk, ncrNo);
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof NcrItemListReportResultRow))
            return false;
        return (getPk() == ((NcrItemListReportResultRow) obj).getPk());
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

}

