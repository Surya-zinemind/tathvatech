package com.tathvatech.forms.common;

import java.util.List;

import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.project.common.ProjectQuery;
import com.tathvatech.survey.response.SurveyResponse;
import com.tathvatech.unit.common.UnitQuery;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.entity.User;
import com.tathvatech.workstation.common.WorkstationQuery;

public class FormResponseContext
{
	User user;
	ProjectQuery projectQuery;
	UnitQuery unitQuery;
	TestProcObj testProc;
	WorkstationQuery workstationQuery;
	FormQuery formQuery;
	ResponseMasterNew responseMaster;
	SurveyResponse surveyResponse;
	List<String> options;

	public FormResponseContext(User user, TestProcObj testProc, ProjectQuery projectQuery, UnitQuery unitQuery, WorkstationQuery workstationQuery, 
			FormQuery formQuery, ResponseMasterNew responseMaster, SurveyResponse surveyResponse)
	{
		this.user = user;
		this.testProc = testProc;
		this.projectQuery = projectQuery;
		this.unitQuery = unitQuery;
		this.workstationQuery = workstationQuery;
		this.formQuery = formQuery;
		this.responseMaster = responseMaster;
		this.surveyResponse = surveyResponse;
	}

	
	public List<String> getOptions() {
		return options;
	}


	public void setOptions(List<String> options) {
		this.options = options;
	}


	public User getUser()
	{
		return user;
	}

	public TestProcObj getTestProc() {
		return testProc;
	}

	public ProjectQuery getProjectQuery()
	{
		return projectQuery;
	}

	public UnitQuery getUnitQuery()
	{
		return unitQuery;
	}

	public WorkstationQuery getWorkstationQuery()
	{
		return workstationQuery;
	}

	public FormQuery getFormQuery()
	{
		return formQuery;
	}

	public SurveyResponse getSurveyResponse()
	{
		return surveyResponse;
	}

	public ResponseMasterNew getResponseMaster()
	{
		return responseMaster;
	}
}
