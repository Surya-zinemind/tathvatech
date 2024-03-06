/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.project.oid.ProjectPartOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="project_part")
public class ProjectPart extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int projectPk;
	private int partPk;
	private Integer partTypePk;
	private Integer orderNo;
	private Integer parentPk;
	private String partNo; // this is temporary
	
	private String wbs;
	private String locationCode;
	
	private String name;
	private String description;
	private Date createdDate;
	private int estatus;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getProjectPk() {
		return projectPk;
	}

	public void setProjectPk(int projectPk) {
		this.projectPk = projectPk;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public Integer getPartTypePk() {
		return partTypePk;
	}

	public void setPartTypePk(Integer partTypePk) {
		this.partTypePk = partTypePk;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getParentPk() {
		return parentPk;
	}

	public void setParentPk(Integer parentPk) {
		this.parentPk = parentPk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWbs() {
		return wbs;
	}

	public void setWbs(String wbs) {
		this.wbs = wbs;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}


	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	/**
     * 
     */
    public ProjectPart()
    {
    }
    
    @Override
	public int hashCode()
	{
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		return ((ProjectPart)obj).getPk() == pk;
	}

	public ProjectPartOID getOID()
    {
    	return new ProjectPartOID((int) pk, name);
    }
}
