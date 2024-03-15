package com.tathvatech.project.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.project.oid.ProjectStageOID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="project_stage")
public class ProjectStage extends AbstractEntity implements Serializable
{
	@Id
	long pk;
	int projectFk;
	String name;
	String description;
	int estatus;
	Date lastUpdated;

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
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
	public int getEstatus()
	{
		return estatus;
	}
	public void setEstatus(int estatus)
	{
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
	
	public ProjectStageOID getOID()
	{
		return new ProjectStageOID((int) pk, name);
	}
}
