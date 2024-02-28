/*
 * Created on May 21, 2015
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.site.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.site.oid.SiteGroupOID;
import com.tathvatech.user.enums.SiteRolesEnum;


import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.Authorizable;
import com.tathvatech.user.enums.SiteActionsEnum;
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
@Table(name="site_group")
public class SiteGroup extends AbstractEntity implements Serializable, Authorizable
{
	@Id
	private long pk;
	private String name;
	private String description;
	private int createdBy;
	private Date createdDate;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
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

	/**
     * 
     */
    public SiteGroup()
    {
    }
    
    

	@Override
	public int hashCode() 
	{
		return (int) pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SiteGroup && ((SiteGroup)obj).getPk() == pk)
			return true;
		
		return false;
	}

	@Override

	public List<SiteRolesEnum> getSupportedRoles()
	{
		return Arrays.asList(SiteRolesEnum.values());
	}

	@Override

	public List<SiteActionsEnum> getSupportedActions()
	{
		return Arrays.asList(SiteActionsEnum.values());
	}

	public SiteGroupOID getOID()
	{
		return new SiteGroupOID((int) pk, name);
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
		return EntityTypeEnum.SiteGroup;
	}
}
