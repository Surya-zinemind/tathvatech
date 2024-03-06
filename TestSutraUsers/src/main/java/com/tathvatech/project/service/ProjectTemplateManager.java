package com.tathvatech.project.service;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ProjectFormQuery;
import com.tathvatech.project.oid.ProjectPartOID;
import com.tathvatech.user.OID.ProjectOID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectTemplateManager {

	private final PersistWrapper persistWrapper;

	public  List<ProjectFormQuery> getTestProcsForProjectPart(ProjectOID projectOID,
																	ProjectPartOID projectPartOID) throws Exception
	{
		List<ProjectFormQuery> pForms = persistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and project.pk =? and "
				+ "project_part.pk = ? order by ws.orderNo, form.identityNumber", 
				projectOID.getPk(), projectPartOID.getPk()); 

		return pForms;
	}

	public  List<ProjectFormQuery> getTestProcsForProjectPart(ProjectOID projectOID,
			ProjectPartOID projectPartOID, long workstationPk) throws Exception
	{
		List<ProjectFormQuery> pForms = persistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and pf.projectPk=? and "
				+ "pf.projectPartPk = ? and pf.workstationPk=? order by form.identityNumber", 
				projectOID.getPk(), projectPartOID.getPk(), workstationPk); 

		return pForms;
	}

	public  List<ProjectFormQuery> getTestProcs(Collection<Integer> projectTestProcPks)throws Exception
	{
		if(projectTestProcPks.size() == 0)
			return new ArrayList();
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<projectTestProcPks.size(); i++) 
		{
			sb.append(",?");
		}
		String inClause = sb.substring(1);
		
		return persistWrapper.readList(ProjectFormQuery.class, ProjectFormQuery.sql + " and pf.pk in (" + inClause + ")", projectTestProcPks.toArray(new Integer[projectTestProcPks.size()]));
	}
}
