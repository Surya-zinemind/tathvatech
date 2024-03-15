/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.common;

import java.util.Date;

import com.tathvatech.ts.core.FormAssignable;
import com.tathvatech.ts.core.common.OID;

import net.sf.persist.annotations.NoTable;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@NoTable
public class ProjectPartQuery implements FormAssignable
{
	private int pk;
	private int projectPk;
	private String projectPartName;
	private String projectPartDescription;
	private String wbs;
	private String locationCode;
	private int orderNo;
	private Integer parentPk;
	private Date createdDate;
	private int estatus;
	private Date lastUpdated;

	private int partPk;
	private String partNo;
	private String partName;
	private String partDescription;
	
	private int projectPartTypePk;
	private String projectPartTypeName;
	private String projectPartTypeDescription;

	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getProjectPk() {
		return projectPk;
	}

	public void setProjectPk(int projectPk) {
		this.projectPk = projectPk;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getPartDescription() {
		return partDescription;
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}

	public String getWbs() {
		return wbs;
	}

	public void setWbs(String wbs) {
		this.wbs = wbs;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public int getProjectPartTypePk()
	{
		return projectPartTypePk;
	}

	public void setProjectPartTypePk(int projectPartTypePk)
	{
		this.projectPartTypePk = projectPartTypePk;
	}

	public String getProjectPartTypeName()
	{
		return projectPartTypeName;
	}

	public void setProjectPartTypeName(String projectPartTypeName)
	{
		this.projectPartTypeName = projectPartTypeName;
	}

	public String getProjectPartTypeDescription()
	{
		return projectPartTypeDescription;
	}

	public void setProjectPartTypeDescription(String projectPartTypeDescription)
	{
		this.projectPartTypeDescription = projectPartTypeDescription;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getParentPk() {
		return parentPk;
	}

	public void setParentPk(Integer parentPk) {
		this.parentPk = parentPk;
	}

	public String getProjectPartName()
	{
		return projectPartName;
	}

	public void setProjectPartName(String projectPartName)
	{
		this.projectPartName = projectPartName;
	}

	public String getProjectPartDescription()
	{
		return projectPartDescription;
	}

	public void setProjectPartDescription(String projectPartDescription)
	{
		this.projectPartDescription = projectPartDescription;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
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
    public ProjectPartQuery()
    {
    }
    
    public ProjectPartOID getOID()
    {
    	return new ProjectPartOID(pk, projectPartName);
    }

    @Override
    public OID getFormAssignableObjectOID()
    {
    	return getOID();
    }
    
    
    @Override
	public int hashCode() {
		return pk;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		return (pk == ((ProjectPartQuery)obj).getPk()); 
	}


	public static String sql = "select "
    		+ " pp.pk as pk, "
		    + " pp.projectPk, "
		    + " pp.partPk, "
		    + " pp.partTypePk as projectPartTypePk, "
		    + " pp.parentPk, "
		    + " pp.name as projectPartName, "
		    + " pp.description as projectPartDescription, "
		    + " pp.wbs, "
		    + " pp.locationCode, "
		    + " pp.createdDate, "
		    + " pp.estatus, "
		    + " pp.lastUpdated, "
		    + " pp.orderNo, "
			+ " part.name as partName, part.partNo as partNo, part.description as partDescription, "
			+ " pt.typeName as projectPartTypeName, pt.description as projectPartTypeDescription "
			+ " from "
			+ " project_part pp left outer join part_type pt on pp.partTypePk = pt.pk and pt.estatus = 1 "
			+ " join part on pp.partPk = part.pk and part.estatus = 1 "
			+ " where pp.estatus = 1 ";
}
