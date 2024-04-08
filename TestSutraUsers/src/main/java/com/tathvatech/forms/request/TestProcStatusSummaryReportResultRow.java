package com.tathvatech.forms.request;

import java.util.Date;




public class TestProcStatusSummaryReportResultRow
{
	Integer projectPk;
	String projectName;
	String projectDescription;
	Integer projectPartPk;
	String projectPartName;
	Integer unitPk;
	String unitName;
	String unitDescription;
	Integer workstationPk;
	String workstationName;
	String workstationDescription;
	Integer sitePk;
	String siteName;
	Date workstationMoveInDate;
	Date workstationStartDate;
	Date workstationCompleteDate;
	Date workstationForecastStartDate;
	Date workstationForecastEndDate;
	Float workstationEstimateHours;
	String workstationStatus;
	Integer testPk;
	String testName;
	Integer formPk;
	String formName;
	String formDescription;
	private Date forecastStartDate;
	private Date forecastEndDate;
	String forecastStartDateString; // format is dd/mm/yyyy 
	String forecastEndDateString; // format is dd/mm/yyyy
	
	
	long totalFormCount;
	long notStartedCount;
	long inProgressCount;
	long pausedCount;
	long completedCount;
	long verifiedCount;
	long approvedCount;
	long approvedWithCommentsCount;

	private float		percentComplete;
	private int		noOfComments;
	private int		totalQCount;
	private int 	totalACount;
	private int 	passCount;
	private int 	failCount;
	private int		dimentionalFailCount;
	private int 	naCount;
	private int oilTransferCount;
	private int ncrTransferCount;
	
	private Float testEstimateHours;
	
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
	public String getUnitDescription()
	{
		return unitDescription;
	}
	public void setUnitDescription(String unitDescription)
	{
		this.unitDescription = unitDescription;
	}
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
	public Integer getSitePk()
	{
		return sitePk;
	}
	public void setSitePk(Integer sitePk)
	{
		this.sitePk = sitePk;
	}
	public String getSiteName()
	{
		return siteName;
	}
	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}
	public Date getWorkstationMoveInDate()
	{
		return workstationMoveInDate;
	}
	public void setWorkstationMoveInDate(Date workstationMoveInDate)
	{
		this.workstationMoveInDate = workstationMoveInDate;
	}
	public Date getWorkstationStartDate()
	{
		return workstationStartDate;
	}
	public void setWorkstationStartDate(Date workstationStartDate)
	{
		this.workstationStartDate = workstationStartDate;
	}
	public Date getWorkstationCompleteDate()
	{
		return workstationCompleteDate;
	}
	public void setWorkstationCompleteDate(Date workstationCompleteDate)
	{
		this.workstationCompleteDate = workstationCompleteDate;
	}
	public Date getWorkstationForecastStartDate()
	{
		return workstationForecastStartDate;
	}
	public void setWorkstationForecastStartDate(Date workstationForecastStartDate)
	{
		this.workstationForecastStartDate = workstationForecastStartDate;
	}
	public Date getWorkstationForecastEndDate()
	{
		return workstationForecastEndDate;
	}
	public void setWorkstationForecastEndDate(Date workstationForecastEndDate)
	{
		this.workstationForecastEndDate = workstationForecastEndDate;
	}
	public Float getWorkstationEstimateHours()
	{
		return workstationEstimateHours;
	}
	public void setWorkstationEstimateHours(Float workstationEstimateHours)
	{
		this.workstationEstimateHours = workstationEstimateHours;
	}
	public String getWorkstationStatus()
	{
		return workstationStatus;
	}
	public void setWorkstationStatus(String workstationStatus)
	{
		this.workstationStatus = workstationStatus;
	}
	public Integer getTestPk()
	{
		return testPk;
	}
	public void setTestPk(Integer testPk)
	{
		this.testPk = testPk;
	}
	public String getTestName()
	{
		return testName;
	}
	public void setTestName(String testName)
	{
		this.testName = testName;
	}
	public Integer getFormPk()
	{
		return formPk;
	}
	public void setFormPk(Integer formPk)
	{
		this.formPk = formPk;
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
	public Date getForecastStartDate()
	{
		return forecastStartDate;
	}
	public void setForecastStartDate(Date forecastStartDate)
	{
		this.forecastStartDate = forecastStartDate;
	}
	public Date getForecastEndDate()
	{
		return forecastEndDate;
	}
	public void setForecastEndDate(Date forecastEndDate)
	{
		this.forecastEndDate = forecastEndDate;
	}
	public String getForecastStartDateString()
	{
		return forecastStartDateString;
	}
	public void setForecastStartDateString(String forecastStartDateString)
	{
		this.forecastStartDateString = forecastStartDateString;
	}
	public String getForecastEndDateString()
	{
		return forecastEndDateString;
	}
	public void setForecastEndDateString(String forecastEndDateString)
	{
		this.forecastEndDateString = forecastEndDateString;
	}
	public long getTotalFormCount()
	{
		return totalFormCount;
	}
	public void setTotalFormCount(long totalFormCount)
	{
		this.totalFormCount = totalFormCount;
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
	public long getApprovedWithCommentsCount()
	{
		return approvedWithCommentsCount;
	}
	public void setApprovedWithCommentsCount(long approvedWithCommentsCount)
	{
		this.approvedWithCommentsCount = approvedWithCommentsCount;
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
	public int getOilTransferCount()
	{
		return oilTransferCount;
	}
	public void setOilTransferCount(int oilTransferCount)
	{
		this.oilTransferCount = oilTransferCount;
	}
	public int getNcrTransferCount()
	{
		return ncrTransferCount;
	}
	public void setNcrTransferCount(int ncrTransferCount)
	{
		this.ncrTransferCount = ncrTransferCount;
	}
	public Float getTestEstimateHours()
	{
		return testEstimateHours;
	}
	public void setTestEstimateHours(Float testEstimateHours)
	{
		this.testEstimateHours = testEstimateHours;
	}
}
