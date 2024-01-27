package com.tathvatech.user.entity;


import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table(name = "equipment")
public class Equipment
{
	private int pk;
	private Integer equipmentTypeFk;
	private String modelNo;
	private String description;
	private String manufacturer;
	private Date dateOfPurchase;

	private Integer estatus;
	private int createdBy;
	private Date createdDate;
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

	public Integer getEquipmentTypeFk()
	{
		return equipmentTypeFk;
	}

	public void setEquipmentTypeFk(Integer equipmentTypeFk)
	{
		this.equipmentTypeFk = equipmentTypeFk;
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

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public Integer getApprovedBy()
	{
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy)
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

	public Date getApprovedDate()
	{
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate)
	{
		this.approvedDate = approvedDate;
	}
}

