package com.tathvatech.site.entity;

import com.tathvatech.common.entity.AbstractEntity;
import com.tathvatech.common.enums.EntityType;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.site.enums.ProjectSiteConfigActionsEnum;
import com.tathvatech.site.enums.ProjectSiteConfigRolesEnum;
import com.tathvatech.site.oid.ProjectSiteConfigOID;
import com.tathvatech.user.OID.Action;
import com.tathvatech.user.OID.Authorizable;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.entity.Site;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;





@Entity
@Table(name="project_site_config")
public class ProjectSiteConfig extends AbstractEntity implements Serializable,Authorizable
{
	@Id
	private long pk;
	private int projectFk;
	private int siteFk;
	private int managerFk;
	private int estatus;
	private Date lastUpdated;

	@Override
	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getProjectFk() {
		return projectFk;
	}
	public void setProjectFk(int projectFk) {
		this.projectFk = projectFk;
	}
	public int getSiteFk() {
		return siteFk;
	}
	public void setSiteFk(int siteFk) {
		this.siteFk = siteFk;
	}
	public int getManagerFk() {
		return managerFk;
	}
	public void setManagerFk(int managerFk) {
		this.managerFk = managerFk;
	}
	public int getEstatus() {
		return estatus;
	}
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public OID getOID()
	{
		Site site = SiteDelegate.getSite(siteFk);
		return new ProjectSiteConfigOID(pk, site.getName());
	}
	@Override

	public List<? extends Role> getSupportedRoles()
	{
		return Arrays.asList(ProjectSiteConfigRolesEnum.values());
	}

	@Override

	public List<? extends Action> getSupportedActions()
	{
		return Arrays.asList(ProjectSiteConfigActionsEnum.values());
	}
	@Override
	public EntityType getEntityType()
	{
		return EntityTypeEnum.ProjectSiteConfig;
	}
	@Override
	public String getDisplayText()
	{
		Site site = SiteDelegate.getSite(siteFk);
		return site.getName();
	}

}
