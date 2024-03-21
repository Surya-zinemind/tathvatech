/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.project.common.ProjectSignatoryItemBean;
import com.tathvatech.project.common.ProjectSignatorySetBean;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Entity
@Table(name="project_signatory_set")
public class ProjectSignatorySet extends AbstractEntity implements Serializable
{
	@Id
	private long pk;
	private int projectFk;
	private String name;
	private String description;
	private int estatus = 1;
	private Date lastUpdated;

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

	public int getEstatus()
	{
		return estatus;
	}

	public void setEstatus(int estatus)
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
	@Transient
	@Autowired
	private PersistWrapper persistWrapper;
	public ProjectSignatorySetBean getBean()
	{
		ProjectSignatorySetBean bean = new ProjectSignatorySetBean();
		bean.setPk((int) pk);
		bean.setDescription(description);
		bean.setLastUpdated(lastUpdated);
		bean.setName(name);
		bean.setProjectFk(projectFk);
		
		List<ProjectSignatoryItem> items = persistWrapper.readList(ProjectSignatoryItem.class,
				"select * from project_signatory_item where projectSignatorySetFk = ? order by orderNo", pk);
		
		List<ProjectSignatoryItemBean> itemBeanList = new ArrayList<>();
		for (Iterator iterator = items.iterator(); iterator.hasNext();)
		{
			ProjectSignatoryItem item = (ProjectSignatoryItem) iterator.next();
			ProjectSignatoryItemBean itemBean = item.getBean();
			itemBeanList.add(itemBean);
		}
		bean.setSigatoryItems(itemBeanList);
		return bean;
	}
}
