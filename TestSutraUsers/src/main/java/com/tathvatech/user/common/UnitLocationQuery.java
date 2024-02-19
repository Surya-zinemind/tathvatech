/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.common;

import java.util.Date;

import net.sf.persist.annotations.NoTable;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@NoTable
public class UnitLocationQuery
{
	private int pk;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int unitPk;
	private String unitName;
	private int workstationPk;
	private String workstationName;
	private String status;
	private Date moveInDate;
	private Date moveOutDate;
	private Date firstFormAccessDate;
	private Date firstFormLockDate;
	private Date firstFormSaveDate;
	private Date lastFormAccessDate;
	private Date lastFormLockDate;
	private Date lastFormUnlockDate;
	private Date lastFormSaveDate;
	private Date completedDate;
	private Date lastUpdatedDate;
	private String movedInByFirstName;
	private String movedInByLastName;
	private String movedOutByFirstName;
	private String movedOutByLastName;

	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
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
	public int getUnitPk() {
		return unitPk;
	}
	public void setUnitPk(int unitPk) {
		this.unitPk = unitPk;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public int getWorkstationPk() {
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk) {
		this.workstationPk = workstationPk;
	}
	public String getWorkstationName() {
		return workstationName;
	}
	public void setWorkstationName(String workstationName) {
		this.workstationName = workstationName;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public Date getMoveInDate() {
		return moveInDate;
	}
	public void setMoveInDate(Date moveInDate) {
		this.moveInDate = moveInDate;
	}
	public Date getMoveOutDate() {
		return moveOutDate;
	}
	public void setMoveOutDate(Date moveOutDate) {
		this.moveOutDate = moveOutDate;
	}

	public Date getFirstFormAccessDate() {
		return firstFormAccessDate;
	}
	public void setFirstFormAccessDate(Date firstFormAccessDate) {
		this.firstFormAccessDate = firstFormAccessDate;
	}
	public Date getFirstFormLockDate() {
		return firstFormLockDate;
	}
	public void setFirstFormLockDate(Date firstFormLockDate) {
		this.firstFormLockDate = firstFormLockDate;
	}
	public Date getFirstFormSaveDate() {
		return firstFormSaveDate;
	}
	public void setFirstFormSaveDate(Date firstFormSaveDate) {
		this.firstFormSaveDate = firstFormSaveDate;
	}
	public Date getLastFormAccessDate() {
		return lastFormAccessDate;
	}
	public void setLastFormAccessDate(Date lastFormAccessDate) {
		this.lastFormAccessDate = lastFormAccessDate;
	}
	public Date getLastFormLockDate() {
		return lastFormLockDate;
	}
	public void setLastFormLockDate(Date lastFormLockDate) {
		this.lastFormLockDate = lastFormLockDate;
	}
	public Date getLastFormUnlockDate() {
		return lastFormUnlockDate;
	}
	public void setLastFormUnlockDate(Date lastFormUnlockDate) {
		this.lastFormUnlockDate = lastFormUnlockDate;
	}
	public Date getLastFormSaveDate() {
		return lastFormSaveDate;
	}
	public void setLastFormSaveDate(Date lastFormSaveDate) {
		this.lastFormSaveDate = lastFormSaveDate;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	public String getMovedInByFirstName() {
		return movedInByFirstName;
	}
	public void setMovedInByFirstName(String movedInByFirstName) {
		this.movedInByFirstName = movedInByFirstName;
	}
	public String getMovedInByLastName() {
		return movedInByLastName;
	}
	public void setMovedInByLastName(String movedInByLastName) {
		this.movedInByLastName = movedInByLastName;
	}
	public String getMovedOutByFirstName() {
		return movedOutByFirstName;
	}
	public void setMovedOutByFirstName(String movedOutByFirstName) {
		this.movedOutByFirstName = movedOutByFirstName;
	}
	public String getMovedOutByLastName() {
		return movedOutByLastName;
	}
	public void setMovedOutByLastName(String movedOutByLastName) {
		this.movedOutByLastName = movedOutByLastName;
	}

	public static String fetchSQL = "select uloc.pk, "
			+ " uloc.projectPk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription, "
			+ " uloc.unitPk, uh.unitName, uloc.workstationPk, ws.workstationName, uloc.status, " 
			+ " uloc.moveIndate, uloc.moveOutDate, uloc.firstFormAccessDate, uloc.firstFormLockDate, uloc.firstFormSaveDate, " 
			+ " uloc.lastFormAccessDate, uloc.lastFormLockDate, uloc.lastFormUnlockDate, uloc.lastFormSaveDate, uloc.completedDate, " 
			+ " uloc.lastUpdated as lastUpdatedDate, " 
			+ " miuser.firstName as movedInByFirstName, miuser.lastName as movedInByLastName, " 
			+ " mouser.firstName as movedOutByFirstName, mouser.lastName as movedOutByLastName  " 
			+ " from  " 
			+ " TAB_UNIT_LOCATION uloc "
			+ " join TAB_PROJECT p on uloc.projectPk = p.pk "
			+ " join TAB_UNIT u on uloc.unitPk = u.pk "
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo "
			+ " join unit_project_ref upr on uloc.unitPk = upr.unitPk and uloc.projectPk = upr.projectPk "
			+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
			+ " join TAB_WORKSTATION ws on uloc.workstationPk = ws.pk " 
			+ " left join TAB_USER miuser on uloc.movedInBy = miuser.pk " 
			+ " left join TAB_USER mouser on uloc.movedOutBy = mouser.pk  " 
			+ " where 1 = 1";
}
