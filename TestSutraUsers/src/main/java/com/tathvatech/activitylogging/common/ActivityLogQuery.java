/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.activitylogging.common;

import java.util.Date;

import net.sf.persist.annotations.NoTable;

/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@NoTable
public class ActivityLogQuery
{

	private int pk;
	private int userPk;
	private String userName;
	private int action;
	private String actionDescription;
	private Date actionStartTime;
	private Date actionEndTime;
	private int projectPk;
	private String projectName;
	private int testProcPk;
	private int unitPk;
	private String unitName;
	private int workstationPk;
	private String workstationName;
	private int formPk;
	private String formName;
	private String sectionId;
	private int responseId;
	private Date lastUpdated;
	
	int totalQCount = 0;
	int totalACount = 0;
	int passCount = 0;
	int failCount = 0;
	int dimentionalFailCount = 0; // where the item is isNumeric
	int naCount = 0;
	int commentsCount = 0;

	public ActivityLogQuery(Integer userPk, BaseActions action, String actionDescription, Date actionStartDate, Date actionEndDate,
			Integer projectPk, Integer testProcPk, Integer unitPk, Integer workstationPk, Integer formPk, String sectionId, Integer responseId)
	{
		this.userPk = userPk;
		this.action = action.value();
		this.actionDescription = actionDescription;
		this.actionStartTime = actionStartDate;
		this.actionEndTime = actionEndDate;
		this.projectPk = projectPk;
		this.testProcPk = testProcPk;
		this.unitPk = unitPk;
		this.workstationPk = workstationPk;
		this.formPk = formPk;
		this.sectionId = sectionId;
		this.responseId = responseId;
	}
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public int getUserPk() {
		return userPk;
	}

	public void setUserPk(int userPk) {
		this.userPk = userPk;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
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

	public int getFormPk() {
		return formPk;
	}

	public void setFormPk(int formPk) {
		this.formPk = formPk;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
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

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
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
    public ActivityLogQuery()
    {
    }

    public static String fetchQuery = "select a.pk, a.projectPk, project.projectName, a.testProcPk, a.unitPk, uh.unitName, a.workstationPk, ws.workstationName, " +
    		"a.formPk, form.identityNumber as formName, a.sectionId, a.responseId, a.userPk, user.userName, " +
    		"a.action, a.actionDescription, a.actionStartTime, a.actionEndTime, a.lastUpdated, "
    		+ "a.totalQCount, a.totalACount, a.passCount, a.failCount, a.dimentionalFailCount, a.naCount, a.commentsCount " +
    		"from TAB_ACTIVITY_LOG a " +
    		"left join TAB_PROJECT project on a.projectPk = project.pk  " +
    		"left join TAB_UNIT unit on a.unitPk = unit.pk " +
    		"left join TAB_UNIT_H uh on uh.unitPk = unit.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo " +
    		"left join TAB_WORKSTATION ws on a.workstationPk = ws.pk " +
    		"left join TAB_SURVEY form on a.formPk = form.pk " +
    		"left join TAB_USER user on a.userPk = user.pk where 1 = 1 "; 

}
