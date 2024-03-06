package com.tathvatech.project.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.part.ProjectPartOID;
import com.tathvatech.ts.core.project.ProjectFormQuery;
import com.tathvatech.ts.core.project.ProjectOID;

public class ProjectTemplateManager {

	public static List<ProjectFormQuery> getTestProcsForProjectPart(ProjectOID projectOID,
			ProjectPartOID projectPartOID) throws Exception
	{
		List<ProjectFormQuery> pForms = PersistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and project.pk =? and "
				+ "project_part.pk = ? order by ws.orderNo, form.identityNumber", 
				projectOID.getPk(), projectPartOID.getPk()); 

		return pForms;
	}

	public static List<ProjectFormQuery> getTestProcsForProjectPart(ProjectOID projectOID,
			ProjectPartOID projectPartOID, int workstationPk) throws Exception
	{
		List<ProjectFormQuery> pForms = PersistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and pf.projectPk=? and "
				+ "pf.projectPartPk = ? and pf.workstationPk=? order by form.identityNumber", 
				projectOID.getPk(), projectPartOID.getPk(), workstationPk); 

		return pForms;
	}

	public static List<ProjectFormQuery> getTestProcs(Collection<Integer> projectTestProcPks)throws Exception
	{
		if(projectTestProcPks.size() == 0)
			return new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<projectTestProcPks.size(); i++) 
		{
			sb.append(",?");
		}
		String inClause = sb.substring(1);
		
		return PersistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and pf.pk in (" + inClause + ")", projectTestProcPks.toArray(new Integer[projectTestProcPks.size()]));
	}
}
