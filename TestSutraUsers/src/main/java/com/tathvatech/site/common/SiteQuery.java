/*
 * Created on May 21, 2015
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.site.common;

import java.util.Date;

import com.tathvatech.user.OID.SiteOID;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class SiteQuery
{
	private int pk;
	private String name;
	private String description;
	private String address;
	private String timeZone;
	private Integer defaultSupplierFk;
	private int createdBy;
	private Date createdDate;
	private long purchasedLicenseCount;
	private long purchasedReadOnlyCount;
	private long activeLicenseCount;
	private long activeReadOnlyCount;
	private int estatus;
	private int siteGroupPk;
	private String siteGroupName;
	private String siteGroupDescription;
	private Date lastUpdated;
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
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

	public long getPurchasedLicenseCount()
	{
		return purchasedLicenseCount;
	}

	public void setPurchasedLicenseCount(long purchasedLicenseCount)
	{
		this.purchasedLicenseCount = purchasedLicenseCount;
	}

	public long getPurchasedReadOnlyCount()
	{
		return purchasedReadOnlyCount;
	}

	public void setPurchasedReadOnlyCount(long purchasedReadOnlyCount)
	{
		this.purchasedReadOnlyCount = purchasedReadOnlyCount;
	}

	public long getActiveLicenseCount()
	{
		return activeLicenseCount;
	}

	public void setActiveLicenseCount(long activeLicenseCount)
	{
		this.activeLicenseCount = activeLicenseCount;
	}

	public long getActiveReadOnlyCount()
	{
		return activeReadOnlyCount;
	}

	public void setActiveReadOnlyCount(long activeReadOnlyCount)
	{
		this.activeReadOnlyCount = activeReadOnlyCount;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public int getSiteGroupPk() {
		return siteGroupPk;
	}

	public void setSiteGroupPk(int siteGroupPk) {
		this.siteGroupPk = siteGroupPk;
	}

	public String getSiteGroupName() {
		return siteGroupName;
	}

	public void setSiteGroupName(String siteGroupName) {
		this.siteGroupName = siteGroupName;
	}

	public String getSiteGroupDescription() {
		return siteGroupDescription;
	}

	public void setSiteGroupDescription(String siteGroupDescription) {
		this.siteGroupDescription = siteGroupDescription;
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
    public SiteQuery()
    {
    }
    
    

	@Override
	public int hashCode() 
	{
		return pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SiteQuery && ((SiteQuery)obj).getPk() == pk)
			return true;
		
		return false;
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
}
