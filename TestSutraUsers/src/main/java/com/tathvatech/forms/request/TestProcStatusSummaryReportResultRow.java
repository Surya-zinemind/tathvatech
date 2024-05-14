package com.tathvatech.forms.request;

import lombok.Data;

import java.util.Date;



@Data
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
	private Integer		noOfComments;
	private Integer		totalQCount;
	private Integer 	totalACount;
	private Integer 	passCount;
	private Integer 	failCount;
	private Integer		dimentionalFailCount;
	private Integer 	naCount;
	private Integer oilTransferCount;
	private Integer ncrTransferCount;
	
	private Float testEstimateHours;
	

}
