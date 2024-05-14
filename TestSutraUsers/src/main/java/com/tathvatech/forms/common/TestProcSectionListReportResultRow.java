/*
 * Created on Nov 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.forms.common;

import java.util.Date;

import com.tathvatech.forms.oid.TestProcSectionOID;
import lombok.Data;


/**
 * @author Hari
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Data
public class TestProcSectionListReportResultRow
{
	private Integer pk;
	private String sectionName;
	private String sectionDescription;
	private Integer testProcPk;
	private String testName;
	private Integer projectTestProcPk;
	private Integer unitPk;
	private String unitName;
	private String unitDescription;
	private String unitSerialNumber;
	private Integer partPk;
	private String partName;
	private String partType;
	private Integer projectPartPk;
	private String projectPartName;
	private Integer projectPk;
	private String projectName;
	private String projectDescription;
	private Integer workstationPk;
	private String workstationName;
	private String workstationDescription;
	private String workstationStatus;
	private String workstationTimezoneId;
	private Integer formMainPk;
	private Integer formPk;
	private String formName;
	private String formDescription;
	private String formResponsibleDivision;
	private String formRevision;
	private Integer formVersion;

	
	private Integer		responseId;
	private String	responseRefNo;
	private Date	responseStartTime;
	private Date	responseCompleteTime;
	private Date	responseSyncTime;

	private Integer		percentComplete;
	private Integer		noOfComments;
	private Integer		totalQCount;
	private Integer 	totalACount;
	private Integer 	passCount;
	private Integer 	failCount;
	private Integer		dimentionalFailCount;
	private Integer 	naCount;

	private String	responseStatus;

	private Integer 	secResponsePk;
	private Integer		secPercentComplete;
	private Integer		secNoOfComments;
	private Integer		secTotalQCount;
	private Integer 	secTotalACount;
	private Integer 	secPassCount;
	private Integer 	secFailCount;
	private Integer		secDimentionalFailCount;
	private Integer 	secNaCount;
	private Date 	actualStartDate;
	private Date	actualCompletionDate;

	private Date forecastStartDate;
	private Date forecastEndDate;
	private Float forecastHours;
	private String forecastComment;
	private Integer forecastCreatedBy;
	private Date forecastCreatedDate;
	private String forecastCreatedByFirstName;
	private String forecastCreatedByLastName;	
		
	
	public String getDisplayDescriptor()
	{
		return sectionName + " - " + sectionDescription;
	}
	


	public TestProcSectionOID getOID() {
		return new TestProcSectionOID(pk, "");
	}
	
	@Override
	public boolean equals(Object obj) 
	{
		if(obj == null)return false;
		if(obj instanceof TestProcSectionListReportResultRow)
			return this.pk == ((TestProcSectionListReportResultRow)obj).getPk();
		
		return false;			
	}

	@Override
	public int hashCode()
	{
		return pk;
	}

}

