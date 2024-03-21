package com.tathvatech.unit.common;

import java.io.Serializable;
import java.util.Date;

import net.sf.persist.annotations.NoColumn;
import net.sf.persist.annotations.NoTable;

/**
 * This is the real UnitQuery with no project related information, 
 * The UnitQuery class should be renamed as UnitInProjectQuery.
 * @author Hari
 *
 */
@NoTable
public class UnitEntityQuery implements Serializable
{
    int pk;
	private String partName;
    private int partPk;
    private Integer partRevisionPk;
    private String partRevision;
	private String partNo;
	private int supplierFk;
	private int siteGroupFk;
	private String serialNo;
	private String unitName;
	private String description;
	private Date createDate;
    String createdByFirstName;
    String createdByLastName;
	private Date lastUpdatedDate;

	private String status;

    public int getPk()
    {
	return pk;
    }

    public void setPk(int pk)
    {
	this.pk = pk;
    }

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public Integer getPartRevisionPk() {
		return partRevisionPk;
	}

	public void setPartRevisionPk(Integer partRevisionPk) {
		this.partRevisionPk = partRevisionPk;
	}

	public String getPartRevision() {
		return partRevision;
	}

	public void setPartRevision(String partRevision) {
		this.partRevision = partRevision;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public int getSupplierFk() {
		return supplierFk;
	}

	public void setSupplierFk(int supplierFk) {
		this.supplierFk = supplierFk;
	}

	public int getSiteGroupFk() {
		return siteGroupFk;
	}

	public void setSiteGroupFk(int siteGroupFk) {
		this.siteGroupFk = siteGroupFk;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getUnitName()
    {
	return unitName;
    }

    public void setUnitName(String unitName)
    {
	this.unitName = unitName;
    }

    public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

    public Date getCreateDate()
    {
	return createDate;
    }

    public void setCreateDate(Date createDate)
    {
	this.createDate = createDate;
    }

    public String getStatus()
    {
	return status;
    }

    public void setStatus(String status)
    {
	this.status = status;
    }

	public String getCreatedByFirstName()
	{
		return createdByFirstName;
	}

	public void setCreatedByFirstName(String createdByFirstName)
	{
		this.createdByFirstName = createdByFirstName;
	}

	public String getCreatedByLastName()
	{
		return createdByLastName;
	}

	public void setCreatedByLastName(String createdByLastName)
	{
		this.createdByLastName = createdByLastName;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Override
	public boolean equals(Object obj)
	{
		try
		{
			if(((UnitEntityQuery)obj).getPk() == this.pk)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{
		return  unitName;
	}
	
	public String getDisplayDescriptor()
	{
		StringBuffer sb = new StringBuffer(this.unitName);
		if(description != null && description.trim().length() > 0)
		{
			sb.append(" - ").append(description);
		}
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return pk;
	}

	public UnitOID getOID() {
		return new UnitOID(pk, unitName);
	}
}
