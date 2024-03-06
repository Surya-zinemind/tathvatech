package com.tathvatech.forms.common;

import java.util.Iterator;
import java.util.List;

import com.tathvatech.ts.core.project.ProjectFormQuery;
import com.tathvatech.ts.core.project.UnitFormQuery;

/*
 * The match is made based on if the form used in the projectForm is the same as the one in the unitForm and
 * if the names for the test are the same
 */
public class TestProcMatchMaker {

	public static UnitFormQuery testItemContainsForm(ProjectFormQuery aPForm, List<UnitFormQuery> uForms) 
	{
		String pName = aPForm.getName(); // easy way to check for null also for the name is the assign it something if it is null.
		if(pName == null)
			pName = "dummy";
		for (Iterator iterator = uForms.iterator(); iterator.hasNext();) 
		{
			UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();
			String uName = unitFormQuery.getName();
			if(uName == null)
				uName = "dummy";
			if(unitFormQuery.getFormPk() == aPForm.getFormPk() && pName.equals(uName))
				return unitFormQuery;
		}
		return null;
	}

	public static UnitFormQuery getMatchingLowerVersionOfForm(ProjectFormQuery aPForm, List<UnitFormQuery> uForms) 
	{
		String pName = aPForm.getName(); // easy way to check for null also for the name is the assign it something if it is null.
		if(pName == null)
			pName = "dummy";
		for (Iterator iterator = uForms.iterator(); iterator.hasNext();) 
		{
			UnitFormQuery unitFormQuery = (UnitFormQuery) iterator.next();
			String uName = unitFormQuery.getName();
			if(uName == null)
				uName = "dummy";
			if(unitFormQuery.getFormPk() != aPForm.getFormPk() 
					&& unitFormQuery.getFormMainPk() == aPForm.getFormMainPk() 
					&& unitFormQuery.getFormVersion() < aPForm.getFormVersion()
					&& pName.equals(uName))
				return unitFormQuery;
		}
		return null;
	}

	public static boolean projectContainsForm(UnitFormQuery aForm, List<ProjectFormQuery> pForms) 
	{
		String uName = aForm.getName();
		if(uName == null)
			uName = "dummy";
		for (Iterator iterator = pForms.iterator(); iterator.hasNext();) 
		{
			ProjectFormQuery projectForm = (ProjectFormQuery) iterator.next();
			String pName = projectForm.getName(); // easy way to check for null also for the name is the assign it something if it is null.
			if(pName == null)
				pName = "dummy";
			if(projectForm.getFormPk() == aForm.getFormPk() && pName.equals(uName))
				return true;
		}
		return false;
	}

}
