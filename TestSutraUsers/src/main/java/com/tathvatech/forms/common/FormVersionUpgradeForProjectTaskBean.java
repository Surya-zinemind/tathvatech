package com.tathvatech.forms.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tathvatech.ts.core.project.ProjectFormQuery;
import com.tathvatech.ts.core.project.UnitFormQuery;
import com.tathvatech.ts.tasks.TaskDefBean;
import com.tathvatech.ts.tasks.TaskTypes;

public class FormVersionUpgradeForProjectTaskBean extends TaskDefBean
{
	int projectPk;
	String projectName;
	int formPk;
	String formName;
	String formDescription;
	String newRevision;
	int newVersion;
	String revisionLog;
	List<Integer> unitFormPks;
	boolean upgradeProjectForm;
	
	public FormVersionUpgradeForProjectTaskBean() {
		super(TaskTypes.FormVersionUpgradeForProject);
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

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getNewRevision() {
		return newRevision;
	}

	public void setNewRevision(String newRevision) {
		this.newRevision = newRevision;
	}

	public int getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}

	public String getRevisionLog() {
		return revisionLog;
	}

	public void setRevisionLog(String revisionLog) {
		this.revisionLog = revisionLog;
	}

	public List<Integer> getUnitFormPks() {
		return unitFormPks;
	}

	public void setUnitForms(List<Integer> unitFormPks) {
		this.unitFormPks = unitFormPks;
	}

	public boolean isUpgradeProjectForm() {
		return upgradeProjectForm;
	}

	public void setUpgradeProjectForm(boolean upgradeProjectForm) {
		this.upgradeProjectForm = upgradeProjectForm;
	}

	/**
	 * TODO:: we need to internationalize this
	 */
	@JsonIgnore
	public String getDescription()
	{
		return "Upgrade available for Form:" + formName + " - " + formDescription + "  on Project:" + projectName 
				+", New version:" + newRevision + "(" + newVersion + ")"; 
	}
	
	
	public static FormVersionUpgradeForProjectTaskBean fromDef(String taskDef) throws Exception
	{
        ObjectMapper objectMapper = new ObjectMapper();
        FormVersionUpgradeForProjectTaskBean bean = objectMapper.readValue(taskDef, FormVersionUpgradeForProjectTaskBean.class);
        return bean;
	}
	
	
}
