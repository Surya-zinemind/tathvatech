package com.tathvatech.user.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TAB_USER_UNIT_PREFIX")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUnitPrefix extends AbstractEntity
{
	@Id
	private long pk;
	private long userPk;
	private long unitPk;
	private int refPrefix;
	
    private Date lastUpdated;

    public long getPk()
    {
        return pk;
    }
    public void setPk(long pk)
    {
        this.pk = pk;
    }
	public long getUserPk()
	{
		return userPk;
	}
	public void setUserPk(long userPk)
	{
		this.userPk = userPk;
	}
	public long getUnitPk() {
		return unitPk;
	}
	public void setUnitPk(long unitPk) {
		this.unitPk = unitPk;
	}
	public int getRefPrefix() {
		return refPrefix;
	}
	public void setRefPrefix(int refPrefix) {
		this.refPrefix = refPrefix;
	}
	public Date getLastUpdated()
	{
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
}
