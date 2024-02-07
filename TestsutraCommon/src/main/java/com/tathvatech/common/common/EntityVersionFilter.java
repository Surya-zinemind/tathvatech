package com.tathvatech.common.common;

import java.util.Date;

import com.tathvatech.common.enums.EntityType;

public class EntityVersionFilter 
{
	EntityType entityType;
	Integer entityPk;
	String versionContext;
	Date createdDateFrom;
	Date createdDateTo;
	Integer limitFetchRecords;
	
	public EntityType getEntityType() {
		return entityType;
	}
	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}
	public Integer getEntityPk() {
		return entityPk;
	}
	public void setEntityPk(Integer entityPk) {
		this.entityPk = entityPk;
	}
	public String getVersionContext() {
		return versionContext;
	}
	public void setVersionContext(String versionContext) {
		this.versionContext = versionContext;
	}
	public Date getCreatedDateFrom() {
		return createdDateFrom;
	}
	public void setCreatedDateFrom(Date createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}
	public Date getCreatedDateTo() {
		return createdDateTo;
	}
	public void setCreatedDateTo(Date createdDateTo) {
		this.createdDateTo = createdDateTo;
	}
	public Integer getLimitFetchRecords() {
		return limitFetchRecords;
	}
	public void setLimitFetchRecords(Integer limitFetchRecords) {
		this.limitFetchRecords = limitFetchRecords;
	}
	
}
