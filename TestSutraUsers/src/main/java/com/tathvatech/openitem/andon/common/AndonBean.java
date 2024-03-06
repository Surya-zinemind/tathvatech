/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.openitem.andon.common;

import com.tathvatech.common.entity.AttachmentIntf;
import com.tathvatech.openitem.andon.entity.Andon;
import com.tathvatech.openitem.andon.enums.OILTypesEnum;
import com.tathvatech.openitem.andon.oids.AndonOID;
import com.tathvatech.user.OID.MRFOID;
import com.tathvatech.user.OID.OID;

import java.util.Date;
import java.util.List;






public class AndonBean
{
	private int pk;
	private int type = OILTypesEnum.Andon.value();
	private int projectPk;
	private Integer unitPk;
	private Integer workstationPk;
	private Integer andonTypePk;
	private String andonNo;
	private String description;
	private String assyPartTestNo;
	private String carType;
	private String partNo;
	private String partDesc;
	private String supplier;
	private String referenceNo;
	private Date referenceCreatedDate;
	private Boolean escape;
	private String rootCause;
	private Date forecastCompletionDate;
	private Integer custodianPk;
	private String disposition;
	private Integer category;
	private Integer subcategory;
	private String part;
	private Boolean partRequired;
	private Integer priority;
	private Integer quantity;
	private Integer unitOfMeasure;
	private Float hours;
	private String reworkOrder;
	private String resourceRequirement;
	private String source;
	private Integer severity;
	private Integer occurrence;
	private Integer detection;
	private String createdByInitials;
	private Date createdDate;
	private Integer attendedBy;
	private Date attendedDate;
	private String attendedComment;
	private Integer closedBy;
	private Date closedDate;
	private String closedComment;
	private String status;
	private Date lastUpdated;
	private MRFOID mrfOID;
	private List<AttachmentIntf> attachments;

	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getProjectPk()
	{
		return projectPk;
	}

	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}

	public Integer getUnitPk()
	{
		return unitPk;
	}

	public void setUnitPk(Integer unitPk)
	{
		this.unitPk = unitPk;
	}

	public Integer getWorkstationPk()
	{
		return workstationPk;
	}

	public void setWorkstationPk(Integer workstationPk)
	{
		this.workstationPk = workstationPk;
	}

	public Integer getAndonTypePk()
	{
		return andonTypePk;
	}

	public void setAndonTypePk(Integer andonTypePk)
	{
		this.andonTypePk = andonTypePk;
	}

	public String getAndonNo()
	{
		return andonNo;
	}

	public void setAndonNo(String andonNo)
	{
		this.andonNo = andonNo;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getAssyPartTestNo()
	{
		return assyPartTestNo;
	}

	public void setAssyPartTestNo(String assyPartTestNo)
	{
		this.assyPartTestNo = assyPartTestNo;
	}

	public String getCarType()
	{
		return carType;
	}

	public void setCarType(String carType)
	{
		this.carType = carType;
	}

	public String getPartNo()
	{
		return partNo;
	}

	public void setPartNo(String partNo)
	{
		this.partNo = partNo;
	}

	public String getPartDesc()
	{
		return partDesc;
	}

	public void setPartDesc(String partDesc)
	{
		this.partDesc = partDesc;
	}

	public String getSupplier()
	{
		return supplier;
	}

	public void setSupplier(String supplier)
	{
		this.supplier = supplier;
	}

	public String getReferenceNo()
	{
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo)
	{
		this.referenceNo = referenceNo;
	}

	public Date getReferenceCreatedDate()
	{
		return referenceCreatedDate;
	}

	public void setReferenceCreatedDate(Date referenceCreatedDate)
	{
		this.referenceCreatedDate = referenceCreatedDate;
	}

	public Boolean getEscape()
	{
		return escape;
	}

	public void setEscape(Boolean escape)
	{
		this.escape = escape;
	}

	public String getRootCause()
	{
		return rootCause;
	}

	public void setRootCause(String rootCause)
	{
		this.rootCause = rootCause;
	}

	public Date getForecastCompletionDate()
	{
		return forecastCompletionDate;
	}

	public void setForecastCompletionDate(Date forecastCompletionDate)
	{
		this.forecastCompletionDate = forecastCompletionDate;
	}

	public Integer getCustodianPk()
	{
		return custodianPk;
	}

	public void setCustodianPk(Integer custodianPk)
	{
		this.custodianPk = custodianPk;
	}

	public String getDisposition()
	{
		return disposition;
	}

	public void setDisposition(String disposition)
	{
		this.disposition = disposition;
	}

	public Integer getCategory()
	{
		return category;
	}

	public void setCategory(Integer category)
	{
		this.category = category;
	}

	public Integer getSubcategory()
	{
		return subcategory;
	}

	public void setSubcategory(Integer subcategory)
	{
		this.subcategory = subcategory;
	}

	public String getPart()
	{
		return part;
	}

	public void setPart(String part)
	{
		this.part = part;
	}

	public Boolean getPartRequired()
	{
		return partRequired;
	}

	public void setPartRequired(Boolean partRequired)
	{
		this.partRequired = partRequired;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	public Integer getUnitOfMeasure()
	{
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(Integer unitOfMeasure)
	{
		this.unitOfMeasure = unitOfMeasure;
	}

	public Float getHours()
	{
		return hours;
	}

	public void setHours(Float hours)
	{
		this.hours = hours;
	}

	public String getReworkOrder()
	{
		return reworkOrder;
	}

	public void setReworkOrder(String reworkOrder)
	{
		this.reworkOrder = reworkOrder;
	}

	public String getResourceRequirement()
	{
		return resourceRequirement;
	}

	public void setResourceRequirement(String resourceRequirement)
	{
		this.resourceRequirement = resourceRequirement;
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
	}

	public Integer getSeverity()
	{
		return severity;
	}

	public void setSeverity(Integer severity)
	{
		this.severity = severity;
	}

	public Integer getOccurrence()
	{
		return occurrence;
	}

	public void setOccurrence(Integer occurrence)
	{
		this.occurrence = occurrence;
	}

	public Integer getDetection()
	{
		return detection;
	}

	public void setDetection(Integer detection)
	{
		this.detection = detection;
	}

	public String getCreatedByInitials()
	{
		return createdByInitials;
	}

	public void setCreatedByInitials(String createdByInitials)
	{
		this.createdByInitials = createdByInitials;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public Integer getAttendedBy()
	{
		return attendedBy;
	}

	public void setAttendedBy(Integer attendedBy)
	{
		this.attendedBy = attendedBy;
	}

	public Date getAttendedDate()
	{
		return attendedDate;
	}

	public void setAttendedDate(Date attendedDate)
	{
		this.attendedDate = attendedDate;
	}

	public String getAttendedComment()
	{
		return attendedComment;
	}

	public void setAttendedComment(String attendedComment)
	{
		this.attendedComment = attendedComment;
	}

	public Integer getClosedBy()
	{
		return closedBy;
	}

	public void setClosedBy(Integer closedBy)
	{
		this.closedBy = closedBy;
	}

	public Date getClosedDate()
	{
		return closedDate;
	}

	public void setClosedDate(Date closedDate)
	{
		this.closedDate = closedDate;
	}

	public String getClosedComment()
	{
		return closedComment;
	}

	public void setClosedComment(String closedComment)
	{
		this.closedComment = closedComment;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public MRFOID getMrfOID()
	{
		return mrfOID;
	}

	public void setMrfOID(MRFOID mrfOID)
	{
		this.mrfOID = mrfOID;
	}

	public OID getOID()
	{
		return new AndonOID(pk, andonNo);
	}

	

	public List<AttachmentIntf> getAttachments()
	{
		return attachments;
	}

	public void setAttachments(List<AttachmentIntf> attachments)
	{
		this.attachments = attachments;
	}

	public Andon getAndon()
	{
		Andon andon = new Andon();
		andon.setPk(pk);
		andon.setType(type);
		andon.setProjectPk(projectPk);
		andon.setUnitPk(unitPk);
		andon.setWorkstationPk(workstationPk);
		andon.setAndonTypePk(andonTypePk);
		andon.setAndonNo(andonNo);
		andon.setDescription(description);
		andon.setAssyPartTestNo(assyPartTestNo);
		andon.setCarType(carType);
		andon.setPartNo(partNo);
		andon.setPartDesc(partDesc);
		andon.setSupplier(supplier);
		andon.setReferenceNo(referenceNo);
		andon.setReferenceCreatedDate(referenceCreatedDate);
		andon.setEscape(escape);
		andon.setRootCause(rootCause);
		andon.setForecastCompletionDate(forecastCompletionDate);
		andon.setCustodianPk(custodianPk);
		andon.setDisposition(disposition);
		andon.setCategory(category);
		andon.setSubcategory(subcategory);
		andon.setPart(assyPartTestNo);
		andon.setPartRequired(partRequired);
		andon.setPriority(priority);
		andon.setQuantity(quantity);
		andon.setUnitOfMeasure(unitOfMeasure);
		andon.setHours(hours);
		andon.setReworkOrder(reworkOrder);
		andon.setResourceRequirement(resourceRequirement);
		andon.setSource(source);
		andon.setSeverity(severity);
		andon.setOccurrence(occurrence);
		andon.setDetection(detection);
		andon.setCreatedByInitials(createdByInitials);
		andon.setCreatedDate(createdDate);
		andon.setAttendedBy(attendedBy);
		andon.setAttendedDate(attendedDate);
		andon.setAttendedComment(attendedComment);
		andon.setClosedBy(closedBy);
		andon.setClosedDate(closedDate);
		andon.setClosedComment(closedComment);
		andon.setStatus(status);
		andon.setLastUpdated(lastUpdated);
		return andon;
	}
}
