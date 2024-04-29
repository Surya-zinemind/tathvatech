package com.tathvatech.forms.common;

import java.util.Date;

import com.tathvatech.workstation.inf.WorkstationOrderComparable;



public class AssignedTestsQuery implements WorkstationOrderComparable
{
	private int testProcPk;
	private String testProcName;
	private Date assignedDate;
	private String comment;
	private int projectPk;
	private String projectName;
	private String projectDescription;
	private int unitPk;
	private String unitName;
	private String unitDescription;
	private int workstationPk;
	private String workstationName;
	private String workstationDescription;
	private String workstationSiteName;
	private int workstationOrderNo;
	private int formPk;
	private int formMainPk;
	private String formName;
	private String formDescription;
	private int formVersion;
	private String formRevision;
	private int responseId;
	private String responseStatus;
	private float percentComplete;
	private int passCount;
	private int failCount;
	private int dimentionalFailCount;
	private int naCount;
	private int commentCount;
	private Integer oilTransferCount;
	public int totalQCount;
	public int totalACount;
	
	public int getTestProcPk()
	{
		return testProcPk;
	}
	public void setTestProcPk(int testProcPk)
	{
		this.testProcPk = testProcPk;
	}
	public String getTestProcName()
	{
		return testProcName;
	}
	public void setTestProcName(String testProcName)
	{
		this.testProcName = testProcName;
	}
	public Date getAssignedDate()
	{
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate)
	{
		this.assignedDate = assignedDate;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
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
	public int getWorkstationPk()
	{
		return workstationPk;
	}
	public void setWorkstationPk(int workstationPk)
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
	public String getWorkstationSiteName()
	{
		return workstationSiteName;
	}
	public void setWorkstationSiteName(String workstationSiteName)
	{
		this.workstationSiteName = workstationSiteName;
	}
	public int getWorkstationOrderNo()
	{
		return workstationOrderNo;
	}
	public void setWorkstationOrderNo(int workstationOrderNo)
	{
		this.workstationOrderNo = workstationOrderNo;
	}
	public int getFormPk()
	{
		return formPk;
	}
	public void setFormPk(int formPk)
	{
		this.formPk = formPk;
	}
	public int getFormMainPk()
	{
		return formMainPk;
	}
	public void setFormMainPk(int formMainPk)
	{
		this.formMainPk = formMainPk;
	}
	public String getFormName()
	{
		return formName;
	}
	public void setFormName(String formName)
	{
		this.formName = formName;
	}
	public String getFormDescription()
	{
		return formDescription;
	}
	public void setFormDescription(String formDescription)
	{
		this.formDescription = formDescription;
	}
	public int getFormVersion()
	{
		return formVersion;
	}
	public void setFormVersion(int formVersion)
	{
		this.formVersion = formVersion;
	}
	public String getFormRevision()
	{
		return formRevision;
	}
	public void setFormRevision(String formRevision)
	{
		this.formRevision = formRevision;
	}
	public int getResponseId()
	{
		return responseId;
	}
	public void setResponseId(int responseId)
	{
		this.responseId = responseId;
	}
	public String getResponseStatus()
	{
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}
	public float getPercentComplete()
	{
		return percentComplete;
	}
	public void setPercentComplete(float percentComplete)
	{
		this.percentComplete = percentComplete;
	}
	public int getPassCount()
	{
		return passCount;
	}
	public void setPassCount(int passCount)
	{
		this.passCount = passCount;
	}
	public int getFailCount()
	{
		return failCount;
	}
	public void setFailCount(int failCount)
	{
		this.failCount = failCount;
	}
	public int getDimentionalFailCount()
	{
		return dimentionalFailCount;
	}
	public void setDimentionalFailCount(int dimentionalFailCount)
	{
		this.dimentionalFailCount = dimentionalFailCount;
	}
	public int getNaCount()
	{
		return naCount;
	}
	public void setNaCount(int naCount)
	{
		this.naCount = naCount;
	}
	public int getCommentCount()
	{
		return commentCount;
	}
	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}
	public Integer getOilTransferCount()
	{
		return oilTransferCount;
	}
	public void setOilTransferCount(Integer oilTransferCount)
	{
		this.oilTransferCount = oilTransferCount;
	}
	public int getTotalQCount()
	{
		return totalQCount;
	}
	public void setTotalQCount(int totalQCount)
	{
		this.totalQCount = totalQCount;
	}
	public int getTotalACount()
	{
		return totalACount;
	}
	public void setTotalACount(int totalACount)
	{
		this.totalACount = totalACount;
	}
	@Override
	public int getOrderNo()
	{
		return workstationOrderNo;
	}
	@Override
	public int hashCode()
	{
		return testProcPk;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		return (testProcPk == ((AssignedTestsQuery)obj).getTestProcPk());
	}
	
}
