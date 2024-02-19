/*
 * Created on May 21, 2015
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.enums.Action;
import com.tathvatech.user.OID.Authorizable;
import com.tathvatech.common.enums.Role;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.enums.SiteActionsEnum;
import com.tathvatech.user.enums.SiteRolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="site")
public class Site extends AbstractEntity implements Authorizable, Serializable
{
	@Id
	private long pk;
	private Integer siteGroupFk;
	private String name;
	private String description;
	private String address;
	private String timeZone;
	private Integer defaultSupplierFk;
	private int createdBy;
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

	public Integer getSiteGroupFk() {
		return siteGroupFk;
	}

	public void setSiteGroupFk(Integer siteGroupFk) {
		this.siteGroupFk = siteGroupFk;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Integer getDefaultSupplierFk() {
		return defaultSupplierFk;
	}

	public void setDefaultSupplierFk(Integer defaultSupplierFk) {
		this.defaultSupplierFk = defaultSupplierFk;
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
    public Site()
    {
    }
    
    

	@Override
	public int hashCode() 
	{
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Site && ((Site)obj).getPk() == pk)
			return true;
		
		return false;
	}

	@Override
	public List<? extends Role> getSupportedRoles()
	{
		return Arrays.asList(SiteRolesEnum.values());
	}

	@Override
	public List<? extends Action> getSupportedActions()
	{
		return Arrays.asList(SiteActionsEnum.values());
	}

	public SiteOID getOID()
	{
		return new SiteOID(pk, getDisplayText());
	}

	@Override
	public String toString() {
		return name;
	}
	
	public String getDisplayText()
	{
		if(description != null && description.trim().length() > 0)
			return name + " (" + description + ")";
		else
			return name;
	}

	@Override
	public EntityType getEntityType()
	{
		return EntityTypeEnum.Site;
	}
}
