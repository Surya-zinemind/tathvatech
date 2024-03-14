/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.workstation.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.entity.Site;
import com.tathvatech.user.entity.SiteCache;
import com.tathvatech.workstation.enums.WorkstationActionsEnum;
import com.tathvatech.workstation.enums.WorkstationRolesEnum;
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
@Table(name="TAB_WORKSTATION")
public class Workstation extends AbstractEntity implements Serializable,Authorizable
{
	@Id
	private long pk;
	private int accountPk;
	private int sitePk;
	private String workstationName;
	private String description;
	private int orderNo;
	private int createdBy;
	private Date createdDate;
	private String status;
	private int estatus;
	private int updatedBy;
	private Date lastUpdated;


	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getAccountPk()
	{
		return accountPk;
	}

	public void setAccountPk(int accountPk)
	{
		this.accountPk = accountPk;
	}

	public int getSitePk() {
		return sitePk;
	}

	public void setSitePk(int sitePk) {
		this.sitePk = sitePk;
	}

	public String getWorkstationName() {
		return workstationName;
	}

	public void setWorkstationName(String workstationName) {
		if (workstationName != null){
			this.workstationName = workstationName.trim();
		}

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

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
	{
		this.estatus = estatus;
	}

	public int getUpdatedBy()
	{
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy)
	{
		this.updatedBy = updatedBy;
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
    public Workstation()
    {
    }

	public String getDisplayString()
	{
		//		Site site = SiteCache.getInstance().getSite(new SiteOID(this.getSitePk(), null));
		String siteName = "NA";
//		if(site != null)
//			siteName = site.getName();
		return "WS" + workstationName + ((description != null)?(" " + description): "") + " / " + siteName;
	}

	@Override
	public List<? extends Role> getSupportedRoles()
	{
		return Arrays.asList(WorkstationRolesEnum.values());
	}

	@Override
	public List<? extends Action> getSupportedActions()
	{
		return Arrays.asList(WorkstationActionsEnum.values());
	}

	public WorkstationOID getOID()
	{
		return new WorkstationOID((int) pk, getDisplayString());
	}

	public static String STATUS_OPEN = "Open";
    public static final String STATUS_CLOSED = "Closed";

	@Override
	public EntityType getEntityType()
	{
		return EntityTypeEnum.Workstation;
	}

	@Override
	public String getDisplayText()
	{
		return getDisplayString();
	}
}
