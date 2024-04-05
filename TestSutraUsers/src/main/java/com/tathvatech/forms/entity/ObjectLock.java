/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.entity;

import java.io.Serializable;
import java.util.Date;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.user.OID.Authorizable;
import com.tathvatech.user.entity.User;
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
@Table(name="tab_sectionlock")
public class ObjectLock extends AbstractEntity implements Serializable, Authorizable
{
	@Id
	private long pk;
	private int responseFk;
	private int formSectionFk;
	private String sectionId;
	private int userPk;
	private Integer attributionUserFk;
	private Date lockDate;
	
	private Date lastUpdated;
	
	private User lockedByUser;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}


	public User getLockedByUser()
	{
		return lockedByUser;
	}

	public void setLockedByUser(User lockedByUser)
	{
		this.lockedByUser = lockedByUser;
	}

	public int getResponseFk() {
		return responseFk;
	}

	public void setResponseFk(int responseFk) {
		this.responseFk = responseFk;
	}

	public int getFormSectionFk() {
		return formSectionFk;
	}

	public void setFormSectionFk(int formSectionFk) {
		this.formSectionFk = formSectionFk;
	}

	public String getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(String sectionId)
	{
		this.sectionId = sectionId;
	}

	public int getUserPk()
	{
		return userPk;
	}

	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}

	public Integer getAttributionUserFk()
	{
		return attributionUserFk;
	}

	public void setAttributionUserFk(Integer attributionUserFk)
	{
		this.attributionUserFk = attributionUserFk;
	}

	public Date getLockDate()
	{
		return lockDate;
	}

	public void setLockDate(Date lockDate)
	{
		this.lockDate = lockDate;
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
    public ObjectLock()
    {
    }
}
