package com.tathvatech.forms.common;

import java.util.List;

import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.project.TestProcObj;
import com.tathvatech.ts.core.project.UnitQuery;
import com.tathvatech.ts.core.project.WorkstationQuery;
import com.tathvatech.ts.core.survey.response.ResponseMasterNew;
import com.tathvatech.ts.core.survey.response.SurveyResponse;
import com.thirdi.surveyside.project.ProjectQuery;
import com.thirdi.surveyside.survey.FormQuery;

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
