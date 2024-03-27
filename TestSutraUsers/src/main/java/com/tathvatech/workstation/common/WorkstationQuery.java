package com.tathvatech.workstation.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.user.OID.TSBeanBase;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.inf.WorkstationOrderComparable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkstationQuery extends TSBeanBase implements WorkstationOrderComparable, Serializable
{
	private long pk;
	private int sitePk;
	private String siteName;
	private String timeZone;
	private String workstationName;
	private String description;
	private int orderNo;
	private String status;
	private String createdByFirstName;
	private String createdByLastName;
	private int createdByPk;
	private Date createdDate;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getSitePk() {
		return sitePk;
	}
	public void setSitePk(int sitePk) {
		this.sitePk = sitePk;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getWorkstationName() {
		return workstationName;
	}
	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
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
	public String getCreatedBy() {
		return createdByFirstName + " " + createdByLastName;
	}
	public int getCreatedByPk()
	{
	    return this.createdByPk;
	}
	public void setCreatedByPk(int createdBy) {
		this.createdByPk = createdBy;
	}
	public Date getCreatedDate()
	{
		return createdDate;
	}
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}
	
	public WorkstationOID getOID()
	{
		return new WorkstationOID(Math.toIntExact(pk), getDisplayString());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof WorkstationQuery)
		{
			if(pk == ((WorkstationQuery)obj).getPk())
			{
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		return Math.toIntExact(pk);
	}
	@Override
	public String toString() {
		return "WS" + workstationName + " / " + siteName;
	}

	@JsonIgnore

	public String getDisplayString()
	{
//		Site site = SiteCache.getInstance().getSite(new SiteOID(this.getSitePk(), null));
		String siteName = "NA";
//		if(site != null)
//			siteName = site.getName();
		return siteName +  ((User.STATUS_INACTIVE.equals(status))?" (Inactive)":"");
	}

	@Override
	public String getDisplayText()
	{
		return getDisplayString();
	}

	public static String sql = "select ws.pk, ws.workstationName, ws.description, ws.orderNo, " +
		"ws.status, ws.createdDate, ws.createdBy as createdByPk, site.pk as sitePk, site.name as siteName, site.timeZone as timeZone, " +
		"user.firstName as createdByFirstName, " +
		"user.lastName as createdByLastName from TAB_WORKSTATION ws, TAB_USER user, site " +
		"where ws.createdBy = user.pk and ws.sitePk = site.pk";
}
