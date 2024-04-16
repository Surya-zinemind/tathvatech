/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.activitylogging.entity;

import com.tathvatech.common.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Hari
 *
 * The ActivityLog class is specific to FormSaveActions.. this should take care of the common ones.
 * TAB_ACTIVITY_LOG table is only for Form actions.
 */
@Entity
@Table(name="TAB_ACTIVITY_LOG")
public class ActivityLog extends AbstractEntity implements Serializable
{

	@Id
	private long pk;
	private int userPk;
	private int action;
	private String actionDescription;
	private Date actionStartTime;
	private Date actionEndTime;
	private int projectPk;
	private int testProcPk;
	private int unitPk;
	private int workstationPk;
	private int formPk;
	private String sectionId;
	private int responseId;
	
	int totalQCount = 0;
	int totalACount = 0;
	int passCount = 0;
	int failCount = 0;
	int dimentionalFailCount = 0; // where the item is isNumeric
	int naCount = 0;
	int commentsCount = 0;

	private Date lastUpdated;



	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public int getUserPk() {
		return userPk;
	}

	public void setUserPk(int userPk) {
		this.userPk = userPk;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public Date getActionStartTime() {
		return actionStartTime;
	}

	public void setActionStartTime(Date actionStartTime) {
		this.actionStartTime = actionStartTime;
	}

	public Date getActionEndTime() {
		return actionEndTime;
	}

	public void setActionEndTime(Date actionEndTime) {
		this.actionEndTime = actionEndTime;
	}

	public int getProjectPk() {
		return projectPk;
	}

	public void setProjectPk(int projectPk) {
		this.projectPk = projectPk;
	}

	public int getTestProcPk() {
		return testProcPk;
	}

	public void setTestProcPk(int testProcPk) {
		this.testProcPk = testProcPk;
	}

	public int getUnitPk() {
		return unitPk;
	}

	public void setUnitPk(int unitPk) {
		this.unitPk = unitPk;
	}

	public int getWorkstationPk() {
		return workstationPk;
	}

	public void setWorkstationPk(int workstationPk) {
		this.workstationPk = workstationPk;
	}

	public int getFormPk() {
		return formPk;
	}

	public void setFormPk(int formPk) {
		this.formPk = formPk;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public int getResponseId() {
		return responseId;
	}

	public void setResponseId(int responseId) {
		this.responseId = responseId;
	}

	public int getTotalQCount() {
		return totalQCount;
	}

	public void setTotalQCount(int totalQCount) {
		this.totalQCount = totalQCount;
	}

	public int getTotalACount() {
		return totalACount;
	}

	public void setTotalACount(int totalACount) {
		this.totalACount = totalACount;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getPassCount() {
		return passCount;
	}

	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}

	public int getFailCount() {
		return failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getDimentionalFailCount() {
		return dimentionalFailCount;
	}

	public void setDimentionalFailCount(int dimentionalFailCount) {
		this.dimentionalFailCount = dimentionalFailCount;
	}

	public int getNaCount() {
		return naCount;
	}

	public void setNaCount(int naCount) {
		this.naCount = naCount;
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
    public ActivityLog()
    {
    }
}
