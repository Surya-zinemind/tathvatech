package com.tathvatech.forms.response;

public class FormWorkstationSummaryResponseBean
{

	private Integer workstationPk;
	private String workstationName;
	private String workstationDescription;

	private Integer projectPk;
	private String projectName;
	private String projectDescription;

	private Integer unitPk;
	private String unitName;

	private String status;
	private long inProgressCount;
	private long completedCount;
	private long totalFormCount;
	private int failCount;
	private float percentComplete;
	private int	noOfComments;
	public Integer getWorkstationPk()
	{
		return workstationPk;
	}
	public void setWorkstationPk(Integer workstationPk)
	{
		this.workstationPk = workstationPk;
	}
	public String getWorkstationName()
	{
		return workstationName;
	}
	public void setWorkstationName(String workstationName)
	{
		this.workstationName = workstationName;
	}
	public String getWorkstationDescription()
	{
		return workstationDescription;
	}
	public void setWorkstationDescription(String workstationDescription)
	{
		this.workstationDescription = workstationDescription;
	}
	public Integer getProjectPk()
	{
		return projectPk;
	}
	public void setProjectPk(Integer projectPk)
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
	public Integer getUnitPk()
	{
		return unitPk;
	}
	public void setUnitPk(Integer unitPk)
	{
		this.unitPk = unitPk;
	}
	public String getUnitName()
	{
		return unitName;
	}
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public long getInProgressCount()
	{
		return inProgressCount;
	}
	public void setInProgressCount(long inProgressCount)
	{
		this.inProgressCount = inProgressCount;
	}
	public long getCompletedCount()
	{
		return completedCount;
	}
	public void setCompletedCount(long completedCount)
	{
		this.completedCount = completedCount;
	}
	public long getTotalFormCount()
	{
		return totalFormCount;
	}
	public void setTotalFormCount(long totalFormCount)
	{
		this.totalFormCount = totalFormCount;
	}
	public int getFailCount()
	{
		return failCount;
	}
	public void setFailCount(int failCount)
	{
		this.failCount = failCount;
	}
	public float getPercentComplete()
	{
		return percentComplete;
	}
	public void setPercentComplete(float percentComplete)
	{
		this.percentComplete = percentComplete;
	}
	public int getNoOfComments()
	{
		return noOfComments;
	}
	public void setNoOfComments(int noOfComments)
	{
		this.noOfComments = noOfComments;
	}
	
	

}
