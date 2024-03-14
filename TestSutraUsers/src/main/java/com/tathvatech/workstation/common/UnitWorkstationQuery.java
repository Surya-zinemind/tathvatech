/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.workstation.common;

import java.util.Date;



/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class UnitWorkstationQuery
{
	private int pk;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int unitPk;
	private String unitName;
	private String unitDescription;
	private int workstationPk;
	private String workstationName;
	private String workstationDescription;
	private int sitePk;
	private String siteName;
	private String siteDescription;
	private Date completionForecast;
	
	private String status;
	private Date moveInDate;
	private Date moveOutDate;
	private Integer movedInByUserPk;
	private Integer movedOutByUserPk;
	private Date firstFormAccessDate;
	private Date firstFormLockDate;
	private Date firstFormSaveDate;
	private Date lastFormAccessDate;
	private Date lastFormLockDate;
	private Date lastFormSaveDate;
	private Date completedDate;
	private Date lastFormUnlockDate;
	
	
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
	public String getProjectName()
	{
		return projectName;
	}
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}
	public String getProjectDescription()
	{
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription)
	{
		this.projectDescription = projectDescription;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}

	public int getWorkstationPk() {
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) {
		this.workstationPk = workstationPk;
	}
	public Date getCompletionForecast() {
		return completionForecast;
	}
	public void setCompletionForecast(Date completionForecast) {
		this.completionForecast = completionForecast;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getUnitDescription() {
		return unitDescription;
	}
	public void setUnitDescription(String unitDescription) {
		this.unitDescription = unitDescription;
	}
	public String getWorkstationName() {
		return workstationName;
	}
	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}
	public String getWorkstationDescription() {
		return workstationDescription;
	}
	public void setWorkstationDescription(String workstationDescription) {
		this.workstationDescription = workstationDescription;
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
	public String getSiteDescription() {
		return siteDescription;
	}
	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Date getMoveInDate()
	{
		return moveInDate;
	}
	public void setMoveInDate(Date moveInDate)
	{
		this.moveInDate = moveInDate;
	}
	public Date getMoveOutDate()
	{
		return moveOutDate;
	}
	public void setMoveOutDate(Date moveOutDate)
	{
		this.moveOutDate = moveOutDate;
	}
	public Integer getMovedInByUserPk()
	{
		return movedInByUserPk;
	}
	public void setMovedInByUserPk(Integer movedInByUserPk)
	{
		this.movedInByUserPk = movedInByUserPk;
	}
	public Integer getMovedOutByUserPk()
	{
		return movedOutByUserPk;
	}
	public void setMovedOutByUserPk(Integer movedOutByUserPk)
	{
		this.movedOutByUserPk = movedOutByUserPk;
	}
	public Date getFirstFormAccessDate()
	{
		return firstFormAccessDate;
	}
	public void setFirstFormAccessDate(Date firstFormAccessDate)
	{
		this.firstFormAccessDate = firstFormAccessDate;
	}
	public Date getFirstFormLockDate()
	{
		return firstFormLockDate;
	}
	public void setFirstFormLockDate(Date firstFormLockDate)
	{
		this.firstFormLockDate = firstFormLockDate;
	}
	public Date getFirstFormSaveDate()
	{
		return firstFormSaveDate;
	}
	public void setFirstFormSaveDate(Date firstFormSaveDate)
	{
		this.firstFormSaveDate = firstFormSaveDate;
	}
	public Date getLastFormAccessDate()
	{
		return lastFormAccessDate;
	}
	public void setLastFormAccessDate(Date lastFormAccessDate)
	{
		this.lastFormAccessDate = lastFormAccessDate;
	}
	public Date getLastFormLockDate()
	{
		return lastFormLockDate;
	}
	public void setLastFormLockDate(Date lastFormLockDate)
	{
		this.lastFormLockDate = lastFormLockDate;
	}
	public Date getLastFormSaveDate()
	{
		return lastFormSaveDate;
	}
	public void setLastFormSaveDate(Date lastFormSaveDate)
	{
		this.lastFormSaveDate = lastFormSaveDate;
	}
	public Date getCompletedDate()
	{
		return completedDate;
	}
	public void setCompletedDate(Date completedDate)
	{
		this.completedDate = completedDate;
	}
	public Date getLastFormUnlockDate()
	{
		return lastFormUnlockDate;
	}
	public void setLastFormUnlockDate(Date lastFormUnlockDate)
	{
		this.lastFormUnlockDate = lastFormUnlockDate;
	}
	public String getDisplayString()
	{
		return this.getUnitName() + " -> " + this.getWorkstationName();
	}
	
	@Override
	public int hashCode()
	{
		return pk;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		if(obj instanceof UnitWorkstationQuery)
			return this.pk == ((UnitWorkstationQuery)obj).getPk();
		
		return false;
	}

	public static String sql = "select uw.pk, uw.unitPk, uw.projectPk, uw.workstationPk, uw.completionForecast, "
			+ " ul.status as status, ul.moveInDate, ul.moveOutDate, ul.movedInBy as movedInByUserPk, ul.movedOutBy as movedOutByUserPk, ul.firstFormAccessDate, ul.firstFormLockDate, "
			+ " ul.firstFormSaveDate, ul.lastFormAccessDate, ul.lastFormLockDate, ul.lastFormSaveDate, ul.completedDate, ul.lastFormUnlockDate, "
			+ " p.projectName, p.projectDescription, "
			+ " uh.unitName as unitName, uh.unitDescription as unitDescription, "
			+ " w.workstationName as workstationName, w.description as workstationDescription, "
			+ " site.pk as sitePk, site.name as siteName, site.description as siteDescription "
			+ " from TAB_UNIT_WORKSTATIONS uw "
			+ " left outer join TAB_UNIT_LOCATION ul on ul.unitPk = uw.unitPk and ul.projectPk = uw.projectPk and ul.workstationPk = uw.workstationPk and ul.current = 1"
			+ " join TAB_PROJECT p on uw.projectPk = p.pk "
			+ " join TAB_UNIT u on uw.unitPk = u.pk "
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
			+ " join unit_project_ref upr on uw.unitPk = upr.unitPk and uw.projectPk = upr.projectPk "
			+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
			+ " join TAB_WORKSTATION w on uw.workstationPk = w.pk"
			+ " join site on w.sitePk = site.pk "
			+ " where 1 = 1";
}

