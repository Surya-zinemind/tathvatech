package com.tathvatech.unit.common;

import com.tathvatech.unit.oid.UnitInProjectOID;

import java.util.Date;




public class UnitInProjectQuery
{
	private int pk;
	private int unitPk;
	private String unitName;
	private String unitDescription;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int projectCoordinatorPk;
	private String projectCoordinatorName;
	private int addedByPk;
	private String addedByName;
	private Date addedDate;
	private Integer projectPartPk;
	private String projectPartName;
	private String status;

	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public int getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(int unitPk)
	{
		this.unitPk = unitPk;
	}
	public int getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(int projectPk)
	{
		this.projectPk = projectPk;
	}
	public UnitInProjectOID getOID()
	{
		return new UnitInProjectOID(pk);
	}
	public String getUnitName()
	{
		return unitName;
	}
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}
	public String getUnitDescription()
	{
		return unitDescription;
	}
	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
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
	public int getProjectCoordinatorPk()
	{
		return projectCoordinatorPk;
	}
	public void setProjectCoordinatorPk(int projectCoordinatorPk)
	{
		this.projectCoordinatorPk = projectCoordinatorPk;
	}
	public String getProjectCoordinatorName()
	{
		return projectCoordinatorName;
	}
	public void setProjectCoordinatorName(String projectCoordinatorName)
	{
		this.projectCoordinatorName = projectCoordinatorName;
	}
	public int getAddedByPk()
	{
		return addedByPk;
	}
	public void setAddedByPk(int addedByPk)
	{
		this.addedByPk = addedByPk;
	}
	public String getAddedByName()
	{
		return addedByName;
	}
	public void setAddedByName(String addedByName)
	{
		this.addedByName = addedByName;
	}
	public Date getAddedDate()
	{
		return addedDate;
	}
	public void setAddedDate(Date addedDate)
	{
		this.addedDate = addedDate;
	}
	public Integer getProjectPartPk()
	{
		return projectPartPk;
	}
	public void setProjectPartPk(Integer projectPartPk)
	{
		this.projectPartPk = projectPartPk;
	}
	public String getProjectPartName()
	{
		return projectPartName;
	}
	public void setProjectPartName(String projectPartName)
	{
		this.projectPartName = projectPartName;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	public static String sql = "select upr.pk, "
			+ " upr.unitPk, uh.unitName, uh.unitDescription, "
			+ " upr.projectPk, p.projectName, p.projectDescription, "
			+ " p.managerPk as projectCoordinatorPk, concat(mgr.firstName, ' ' , mgr.lastName) as projectCoordinatorName, "
			+ " upr.createdBy as addedByPk, concat(addedByUser.firstName, ' ' , addedByUser.lastName) as addedByName, "
			+ " upr.createdDate as addedDate, "
			+ " uprh.projectPartPk, pp.name as projectPartName, uprh.status as status from "
			+ " unit_project_ref upr "
			+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
			+ " join TAB_UNIT u on upr.unitPk = u.pk"
			+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
			+ " join TAB_PROJECT p on upr.projectPk = p.pk"
			+ " join project_part pp on uprh.projectPartPk = pp.pk"
			+ " join TAB_USER addedByUser on upr.createdBy = addedByUser.pk"
			+ " left outer join TAB_USER mgr on p.managerPk = mgr.pk"
			+ " where 1 = 1 ";
}
