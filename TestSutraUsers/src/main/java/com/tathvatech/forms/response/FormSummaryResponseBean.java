package com.tathvatech.forms.response;

public class FormSummaryResponseBean
{
	private Integer workstationPk;
	private String workstationName;
	private String workstationDescription;

	private Integer projectPk;
	private String projectName;
	private String projectDescription;

	private Integer unitPk;
	private String unitName;
	
	private long notStartedCount;
	private long inProgressCount;
	private long pausedCount;
	private long completedCount;
	private long verifiedCount;
	private long approvedCount;
	
	private long totalFormCount;
	private int	 noOfComments;
	private int  failCount;
	private long  overDueCount;
	
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
	public long getNotStartedCount()
	{
		return notStartedCount;
	}
	public void setNotStartedCount(long notStartedCount)
	{
		this.notStartedCount = notStartedCount;
	}
	public long getInProgressCount()
	{
		return inProgressCount;
	}
	public void setInProgressCount(long inProgressCount)
	{
		this.inProgressCount = inProgressCount;
	}
	public long getPausedCount()
	{
		return pausedCount;
	}
	public void setPausedCount(long pausedCount)
	{
		this.pausedCount = pausedCount;
	}
	public long getCompletedCount()
	{
		return completedCount;
	}
	public void setCompletedCount(long completedCount)
	{
		this.completedCount = completedCount;
	}
	public long getVerifiedCount()
	{
		return verifiedCount;
	}
	public void setVerifiedCount(long verifiedCount)
	{
		this.verifiedCount = verifiedCount;
	}
	public long getApprovedCount()
	{
		return approvedCount;
	}
	public void setApprovedCount(long approvedCount)
	{
		this.approvedCount = approvedCount;
	}
	public long getTotalFormCount()
	{
		return totalFormCount;
	}
	public void setTotalFormCount(long totalFormCount)
	{
		this.totalFormCount = totalFormCount;
	}
	public int getNoOfComments()
	{
		return noOfComments;
	}
	public void setNoOfComments(int noOfComments)
	{
		this.noOfComments = noOfComments;
	}
	public int getFailCount()
	{
		return failCount;
	}
	public void setFailCount(int failCount)
	{
		this.failCount = failCount;
	}
	public long getOverDueCount()
	{
		return overDueCount;
	}
	public void setOverDueCount(long overDueCount)
	{
		this.overDueCount = overDueCount;
	}
	
	
	
}
