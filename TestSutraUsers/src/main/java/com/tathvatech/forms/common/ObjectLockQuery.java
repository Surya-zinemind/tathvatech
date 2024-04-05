/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class ObjectLockQuery
{
	public static final String	SECTION_EDIT_LOCK	= "sectionEditLock";
	private int pk;
	private int responseFk;
	private int formSectionFk;
	private String sectionId;
	private int userPk;
	private Integer attributionUserFk;
	private Date lockDate;
	
	private Date lastUpdated;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
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
    public ObjectLockQuery()
    {
    }

	public static String sql = " select  pk, responseFk, formSectionFk, sectionId, userPk, attributionUserFk, lockDate, lastUpdated from tab_sectionlock where 1 = 1 ";
}
