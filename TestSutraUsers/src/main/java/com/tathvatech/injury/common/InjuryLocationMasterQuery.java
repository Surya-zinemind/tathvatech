package com.tathvatech.injury.common;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.tathvatech.ts.core.common.TSBeanBase;
import com.tathvatech.ts.core.project.LocationTypeOID;

import net.sf.persist.annotations.NoTable;
@NoTable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("InjuryLocationMasterQuery")
public class InjuryLocationMasterQuery extends TSBeanBase implements Serializable {
	private int pk;
	private String name;
	private String status;
	private Integer parentPk;
	private Date createdDate;
	private Date lastUpdated;

	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public Integer getParentPk()
	{
		return parentPk;
	}
	public void setParentPk(Integer parentPk)
	{
		this.parentPk = parentPk;
	}

	public static String sql="SELECT pk,parentPk, name, status, createdDate, lastUpdated FROM injury_location_master where 1=1 ";

	public String getDisplayString(){
		return name;
	}
	@Override
	public String getDisplayText()
	{
		return getDisplayString();
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj instanceof InjuryLocationMasterQuery)
		{
			return (((InjuryLocationMasterQuery) obj).getPk() == getPk());
		} else
		{
			return false;
		}
	}

	public LocationTypeOID getOID()
	{
		return new LocationTypeOID(pk, name);
	}
}
