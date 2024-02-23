/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="entity_version")
public class EntityVersion extends  AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int entityPk;
	private int entityType;
	private String backupString;
	private String versionContext;
	private Date createdDate;
	private int createdBy;
	private Date lastUpdated;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getEntityPk() {
		return entityPk;
	}

	public void setEntityPk(int entityPk) {
		this.entityPk = entityPk;
	}

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public String getBackupString() {
		return backupString;
	}

	public void setBackupString(String backupString) {
		this.backupString = backupString;
	}

	public String getVersionContext() {
		return versionContext;
	}

	public void setVersionContext(String versionContext) {
		this.versionContext = versionContext;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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
    public EntityVersion()
    {
    }
}
