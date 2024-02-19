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
public class ProjectFormQuery
{
	private int pk;
	private String name;
	private int projectPk;
	private String projectName;
	private int projectPartPk;
	private String projectPartName;
	private int partPk;
	private String partName;
	private String partNo;
	private int partTypePk;
	private String partTypeName;
	private int workstationPk;
	private String workstationName;
	private String workstationDescription;
	private int formPk;
	private int formMainPk;
	private String formName;
	private String formDescription;
	private String formRevision;
	private String formResponsibleDivision;
	private int formVersion;
	private int appliedByUserFk;
	private Date lastUpdated;
	
	public String getDisplayDescriptor()
	{
		if(name != null && name.trim().length() > 0)
		{
			return name + " - " + formName;
		}
		else
		{
			return formName;
		}
	}
	
	public int getPk()
	{
		return pk;
	}

	public void setPk(int pk)
	{
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getFormPk()
	{
		return formPk;
	}

	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}

	public int getAppliedByUserFk() {
		return appliedByUserFk;
	}

	public void setAppliedByUserFk(int appliedByUserFk) {
		this.appliedByUserFk = appliedByUserFk;
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPartName() {
		return projectPartName;
	}

	public void setProjectPartName(String projectPartName) {
		this.projectPartName = projectPartName;
	}

	public String getPartNo() {
		return partNo;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public int getPartPk() {
		return partPk;
	}

	public void setPartPk(int partPk) {
		this.partPk = partPk;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getPartTypePk() {
		return partTypePk;
	}

	public void setPartTypePk(int partTypePk) {
		this.partTypePk = partTypePk;
	}

	public String getPartTypeName() {
		return partTypeName;
	}

	public void setPartTypeName(String partTypeName) {
		this.partTypeName = partTypeName;
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

	public String getFormRevision() {
		return formRevision;
	}

	public void setFormRevision(String formRevision) {
		this.formRevision = formRevision;
	}

	public int getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(int formVersion) {
		this.formVersion = formVersion;
	}

	public String getFormResponsibleDivision() {
		return formResponsibleDivision;
	}

	public void setFormResponsibleDivision(String formResponsibleDivision) {
		this.formResponsibleDivision = formResponsibleDivision;
	}

	public int getFormMainPk() {
		return formMainPk;
	}

	public void setFormMainPk(int formMainPk) {
		this.formMainPk = formMainPk;
	}

	public ProjectFormQuery()
    {
        super();
    }
	
	public static String sql = "select pf.*, project.projectName as projectName, project_part.name as projectPartName, "
	+" part.pk as partPk, part.name as partName, part_type.typeName as partTypeName, ws.workstationName as workstationName, "
	+" ws.description as workstationDescription, part.partNo, pf.appliedByUserFk, "
	+" form.identityNumber as formName, form.description as formDescription, form.versionNo as formVersion, form.responsibleDivision as formResponsibleDivision, "
	+" form.revision as formRevision, form.formMainPk as formMainPk "
	+" from "
	+" TAB_PROJECT_FORMS pf "
	+ " join TAB_PROJECT project on pf.projectPK = project.pk "
	+ " join project_part on pf.projectPartPk = project_part.pk "
	+ " join part on project_part.partPk = part.pk "
	+ " join part_type on project_part.partTypePk = part_type.pk "
	+ " join TAB_SURVEY form on pf.formPk = form.pk "
	+ " left join TAB_WORKSTATION ws on pf.workstationPk = ws.pk "
	+"  where  1 = 1  ";

	public ProjectFormOID getOID() 
	{
		return new ProjectFormOID(pk, "");
	}
	
}
