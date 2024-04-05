package com.tathvatech.ncr.common;

import java.io.Serializable;
import java.util.Date;




public class NcrItemQuery implements Serializable
{
	private int pk;
	private String ncrGroupNo;
	private String ncrGroupDesc;
	private String ncrDesc;
    private Integer custodianPk;
	private Integer createdBy;
	private Date createdDate;
	private Integer cancelledBy;
	private Date cancelledDate;
	private String locationOther;
	private Integer ncrGroupFk;
	private String publishedComment;
	private String completedComment;
	private String closedComment;
	private Integer unitOfMeasureFk;
	private String projectName;
	private String partNo;
	private String partName;
	private String partDescription;
	private String supplierName;
	private String ncrNo;
	private String disposition;
	private String category;
	private String subCategory;
	private String mrfNo;
	private String nCR_Description;
	private String nCR_Status;
	private String where_Found;
	private String custodianName;
	private Float quantity;
	private String unit_of_Measure;
	private String area_Of_Responsibility;
	private String createdByName;
	private String publishedByName;
	private Date publishedDate;
	private String completedByName;
	private Date completedDate;
	private String closedByName;
	private String closedDate;
	private Date forecastStartDate;
	private Date forecastCompletionDate;
	private String carType;
	private String hours;
	private String severity;
	private String occurrence;
	private String detection;
	private String escape;
	private String priority;
	private String rework_Order;
	private String rootCause;
	private String workInstructionOrComment;
	private String dispositionComment;
	private String ppsor8d;
	private String Source;
	private Integer partFk;
	private Integer revisionFk;
	private String latestRejectedByName;
	private Date latestRejectedDate;
	private Integer supplierFk;
	private Date latestApprovedDate;
	private Integer latestApprovedBy;
	private String latestApprovedByName;
	private String cancelledByName;
	private String siteName;
	private String siteDescription;
	private int tsSupplierId;
	private String supplierId;
	private String projectDescription;
	private Integer siteFk;
	private Integer projectFk;
	private Integer dispositionFk;
	private Integer noOfDaysOpen;
	private Integer areaOfResponsibilityFk;
	

	public String getNcrGroupNo()
	{
		return ncrGroupNo;
	}

	public void setNcrGroupNo(String ncrGroupNo)
	{
		this.ncrGroupNo = ncrGroupNo;
	}

	public String getNcrGroupDesc()
	{
		return ncrGroupDesc;
	}

	public void setNcrGroupDesc(String ncrGroupDesc)
	{
		this.ncrGroupDesc = ncrGroupDesc;
	}

	public String getSiteDescription()
	{
		return siteDescription;
	}

	public void setSiteDescription(String siteDescription)
	{
		this.siteDescription = siteDescription;
	}

	public int getTsSupplierId()
	{
		return tsSupplierId;
	}

	public void setTsSupplierId(int tsSupplierId)
	{
		this.tsSupplierId = tsSupplierId;
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

	public String getLatestRejectedByName()
	{
		return latestRejectedByName;
	}

	public Date getLatestApprovedDate()
	{
		return latestApprovedDate;
	}

	public void setLatestApprovedDate(Date latestApprovedDate)
	{
		this.latestApprovedDate = latestApprovedDate;
	}

	public Integer getLatestApprovedBy()
	{
		return latestApprovedBy;
	}

	public void setLatestApprovedBy(Integer latestApprovedBy)
	{
		this.latestApprovedBy = latestApprovedBy;
	}

	public String getLatestApprovedByName()
	{
		return latestApprovedByName;
	}

	public void setLatestApprovedByName(String latestApprovedByName)
	{
		this.latestApprovedByName = latestApprovedByName;
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

	public Integer getSupplierFk()
	{
		return supplierFk;
	}

	public void setSupplierFk(Integer supplierFk)
	{
		this.supplierFk = supplierFk;
	}

	public Date getLatestRejectedDate()
	{
		return latestRejectedDate;
	}

	public void setLatestRejectedDate(Date latestRejectedDate)
	{
		this.latestRejectedDate = latestRejectedDate;
	}

	public void setLatestRejectedByName(String latestRejectedByName)
	{
		this.latestRejectedByName = latestRejectedByName;
	}

	public Integer getRevisionFk()
	{
		return revisionFk;
	}

	public void setRevisionFk(Integer revisionFk)
	{
		this.revisionFk = revisionFk;
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

	public String getLocationOther()
	{
		return locationOther;
	}

	public void setLocationOther(String locationOther)
	{
		this.locationOther = locationOther;
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

	public String getnCR_Description()
	{
		return nCR_Description;
	}

	public void setnCR_Description(String nCR_Description)
	{
		this.nCR_Description = nCR_Description;
	}

	public String getnCR_Status()
	{
		return nCR_Status;
	}

	public void setnCR_Status(String nCR_Status)
	{
		this.nCR_Status = nCR_Status;
	}

	public String getWhere_Found()
	{
		return where_Found;
	}

	public void setWhere_Found(String where_Found)
	{
		this.where_Found = where_Found;
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

	public String getUnit_of_Measure()
	{
		return unit_of_Measure;
	}

	public void setUnit_of_Measure(String unit_of_Measure)
	{
		this.unit_of_Measure = unit_of_Measure;
	}

	public String getArea_Of_Responsibility()
	{
		return area_Of_Responsibility;
	}

	public void setArea_Of_Responsibility(String area_Of_Responsibility)
	{
		this.area_Of_Responsibility = area_Of_Responsibility;
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

	public String getCarType()
	{
		return carType;
	}

	public void setCarType(String carType)
	{
		this.carType = carType;
	}

	public String getHours()
	{
		return hours;
	}

	public void setHours(String hours)
	{
		this.hours = hours;
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

	public String getRework_Order()
	{
		return rework_Order;
	}

	public void setRework_Order(String rework_Order)
	{
		this.rework_Order = rework_Order;
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

	public String getSource()
	{
		return Source;
	}

	public void setSource(String source)
	{
		Source = source;
	}

	public String getNcrDesc()
	{
		return ncrDesc;
	}

	public void setNcrDesc(String ncrDesc)
	{
		this.ncrDesc = ncrDesc;
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
	

}
