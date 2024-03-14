/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.common;

import java.util.Date;
import java.util.List;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ProjectSignatorySetBean
{
	private int pk;
	private int projectFk;
	private String name;
	private String description;
	private boolean estatus = true;
	private Date lastUpdated;
	
	List<ProjectSignatoryItemBean> sigatoryItems;
	
    public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getProjectFk()
	{
		return projectFk;
	}

	public void setProjectFk(int projectFk)
	{
		this.projectFk = projectFk;
	}

	public boolean isEstatus()
	{
		return estatus;
	}

	public void setEstatus(boolean estatus)
	{
		this.estatus = estatus;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public List<ProjectSignatoryItemBean> getSigatoryItems()
	{
		return sigatoryItems;
	}

	public void setSigatoryItems(List<ProjectSignatoryItemBean> sigatoryItems)
	{
		this.sigatoryItems = sigatoryItems;
	}

	public ProjectSignatorySetOID getOID()
	{
		return new ProjectSignatorySetOID(pk, name);
	}
}
