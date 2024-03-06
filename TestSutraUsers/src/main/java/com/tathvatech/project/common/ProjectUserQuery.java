/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.project.common;

import java.util.Date;

import net.sf.persist.annotations.NoTable;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@NoTable
public class ProjectUserQuery
{
	private int pk;
	private int projectPk;
	private String projectName;
	private int workstationPk;
	private String workstationName;
	private int projectPartPk;
	private String projectPartName;
	private int userPk;
	private String userFullName;
	private String userName;
	private int userHomeSitePk;
	private String userHomeSiteName;
	private String role;
	private Date lastUpdated;
	
    public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getProjectPk()
	{
		return projectPk;
	}

	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	
	public int getProjectPartPk() {
		return projectPartPk;
	}

	public void setProjectPartPk(int projectPartPk) {
		this.projectPartPk = projectPartPk;
	}

	public int getWorkstationPk() 
	{
		return workstationPk;
	}

	public void setWorkstationPk(int workstationPk) 
	{
		this.workstationPk = workstationPk;
	}

	public int getUserPk()
	{
		return userPk;
	}

	public void setUserPk(int userPk)
	{
		this.userPk = userPk;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWorkstationName() {
		return workstationName;
	}

	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}

	public String getProjectPartName() {
		return projectPartName;
	}

	public void setProjectPartName(String projectPartName) {
		this.projectPartName = projectPartName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserHomeSitePk()
	{
		return userHomeSitePk;
	}

	public void setUserHomeSitePk(int userHomeSitePk)
	{
		this.userHomeSitePk = userHomeSitePk;
	}

	public String getUserHomeSiteName()
	{
		return userHomeSiteName;
	}

	public void setUserHomeSiteName(String userHomeSiteName)
	{
		this.userHomeSiteName = userHomeSiteName;
	}

	public String getRole()
	{
		return role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}
	
	public String getUserDisplayString()
	{
		return getUserFullName() + " / " + userHomeSiteName;
	}
	
	public static String sql = " select pu.pk, pu.projectPk, pu.workstationPk, pu.projectPartPk, pu.userPk,  "
		+ " pu. role, pu.lastUpdated, u.userName, concat(u.firstName, ' ', u.lastName) as userFullName,  "
		+ " proj.projectName, ws.workstationName, pp.name as projectPartName, "
		+ " site.pk as userHomeSitePk, site.name as userHomeSiteName" 
		+ " from TAB_PROJECT_USERS pu" 
		+ " left outer join project_part pp on pu.projectPartPk = pp.pk and pp.estatus = 1"
		+ " join TAB_USER u on pu.userPk = u.pk"
		+ " join site site on u.sitePk = site.pk"
		+" join TAB_PROJECT proj on pu.projectPk = proj.pk"
		+" join TAB_WORKSTATION ws on pu.workstationPk = ws.pk "; 
}
