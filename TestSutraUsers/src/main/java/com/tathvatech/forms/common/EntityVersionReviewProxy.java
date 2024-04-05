/*
 * Created on Jun 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import java.io.Serializable;
import java.util.Date;



/**
 * @author Hari
 * 
 */


public class EntityVersionReviewProxy implements Serializable
{
	private int pk;
	private int	entityPk;
	private int entityType;
	private Date createdDate;
	private Date updatedDate;
	private int createdByPk;
	private String createdByName;
	private String status;
	private Date lastUpdated;
	
	public static String sql = "select evr.pk as pk, evr.entityPk as entityPk, "
			+ " evr.entityType as entityType, evr.createdDate as createdDate, "
			+ " evr.updatedDate as updatedDate, evr.createdBy as createdByPk, "
			+ " concat(user.firstname, ' ', user.lastName) as createdByName , evr.status as status "
			+ " from entity_review_version evr join tab_user user on evr.createdBy = user.pk where 1 = 1 ";
	
	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}
	public Date getCreatedDate()
	{
		return createdDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	public int getCreatedByPk() {
		return createdByPk;
	}

	public void setCreatedByPk(int createdByPk) {
		this.createdByPk = createdByPk;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
