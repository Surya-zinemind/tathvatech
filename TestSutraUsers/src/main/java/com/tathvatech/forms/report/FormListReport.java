package com.tathvatech.forms.report;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.forms.common.FormFilter;
import com.tathvatech.forms.enums.FormTypeEnum;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;

import java.util.ArrayList;
import java.util.List;


public class FormListReport
{
	public FormListReport()
	{
		
	}
	
	public ReportResponse runReport(ReportRequest reportRequest)
	{
		ReportResponse response = new ReportResponse();
		response.setStartIndex(reportRequest.getStartIndex());
		QueryObject baseQueryBuild = buildBaseQuery((FormFilter) reportRequest.getFilter());
		StringBuffer sql = new StringBuffer(baseQueryBuild.getQuery());
		List<Object> params = baseQueryBuild.getParams();
		if(reportRequest.isFetchRowCount())
		{
			//we need to find the count if the request is newRequest.
			StringBuffer countQB = new StringBuffer("select count(*) from ( ").append(sql).append(" )data");
			long totalRowCount = PersistWrapper.read(Long.class, countQB.toString(), (params.size() >0)?params.toArray(new Object[params.size()]):null);
			response.setTotalRows(totalRowCount);
		}

		sql.append(" order by form.createdDate desc");
		if(reportRequest.getFetchAllRows() == false)
		{
			sql.append(" limit ").append(reportRequest.getStartIndex()).append(", ").append(reportRequest.getRowsToFetch());
		}
		
		PersistWrapper p = new PersistWrapper();
		List<FormQuery> data = p.readList(FormQuery.class, sql.toString(), (params.size() >0)?params.toArray(new Object[params.size()]):null);
		
		response.setReportData(data);
		return response;
	}

	
	private QueryObject buildBaseQuery(FormFilter filter)
	{
		List<Object> params = new ArrayList();
		
		String sql = "select form.pk, form.formMainPk, form.accountPk, form.formType, form.identityNumber, form.description, form.description1," +
				"form.revision, form.createdBy as preparedByPk, form.approvedBy as approvedByPk, form.approvedDate, " +
				"approveUser.firstName as approvedByFirstName, approveUser.lastName as approvedByLastName,  " +
				"form.responsibleDivision, form.effectiveDate, form.createdDate, " +
				"form.versionNo, form.versionComment, form.superseded, form.status, user.firstName as preparedByFirstName, " +
				"user.lastName as preparedByLastName, resDiv.divisionName as responsibleDivisionName " +
				" from TAB_SURVEY form " +
				"left join TAB_USER approveUser on form.approvedBy=approveUser.pk " +
				"left join TAB_FORM_RESP_DIVISION resDiv on form.responsibleDivision = resDiv.pk " +
				"join TAB_USER user on form.createdBy = user.pk ";
		
		StringBuffer sb = new StringBuffer(sql);

		if(filter.getProjectFormFilter() != null)
		{
			sb.append(" join TAB_PROJECT_FORMS tpf on tpf.formPk = form.pk and tpf.projectPk=? ");
			params.add(filter.getProjectFormFilter().getProjectOID().getPk());
			if(filter.getProjectFormFilter().getNotNullWorkstationsOnly() != null && filter.getProjectFormFilter().getNotNullWorkstationsOnly() == true)
			{
				sb.append("  and tpf.workstationPk is not null ");
			}
			if(filter.getProjectFormFilter().getWorkstationOID() != null)
			{
				sb.append("  and tpf.workstationPk=? ");
				params.add(filter.getProjectFormFilter().getWorkstationOID().getPk());
			}
		}
		if(filter.getUnitFormFilter() != null)
		{
			sb.append(" join testproc_form_assign tfa on tfa.formFk = form.pk and tfa.current = 1 ");
			sb.append(" join unit_testproc ut on tfa.testProcFk = ut.pk ");
			sb.append(" join unit_testproc_h uth on uth.unitTestProcFk = ut.pk ");
			if(filter.getUnitFormFilter().getUnitOID() != null)
			{
				sb.append(" and uth.unitPk=?  ");
				params.add(filter.getUnitFormFilter().getUnitOID().getPk());
			}
			if(filter.getUnitFormFilter().getProjectOID() != null)
			{
				sb.append("  and uth.projectPk=? ");
				params.add(filter.getUnitFormFilter().getProjectOID().getPk());
			}
			if(filter.getUnitFormFilter().getWorkstationOID() != null)
			{
				sb.append("  and uth.workstationPk=? ");
				params.add(filter.getUnitFormFilter().getWorkstationOID().getPk());
			}
			sb.append(" and now() between uth.effectiveDateFrom and uth.effectiveDateTo ");
		}
		if(filter.getShowAllFormRevisions() == false)
		{
			sb.append(" join (select max(pk) as maxPk from TAB_SURVEY where 1 = 1 ");
			
			String[] statusVals;
			if(filter.getStatus() != null && filter.getStatus().length > 0)
			{
				statusVals = filter.getStatus();
			}
			else
			{
				statusVals = new String[]{Survey.STATUS_CLOSED, Survey.STATUS_OPEN};
			}	

			sb.append(" and status in (");
			String sep = "";
			for (int i = 0; i < statusVals.length; i++)
			{
				sb.append(sep).append("?");
				params.add(statusVals[i]);
				sep = ", ";
			}
			sb.append(") ");
			
			sb.append(" group by formMainPk) maxVerPks on form.pk = maxVerPks.maxPk "); 
		}
		
		sb.append(" where 1 = 1 ");
		sb.append(" and form.formType = ?");
		params.add(FormTypeEnum.TestForm.value());
		
		if(filter.getFormPk() != null)
		{
			sb.append(" and form.pk = ? ");
			params.add(filter.getFormPk());
		}
		if(filter.getFormMainPk() != null)
		{
			sb.append(" and form.formMainPk = ? ");
			params.add(filter.getFormMainPk());
		}
		if(filter.getShowAllFormRevisions() == true)
		{
			if(filter.getStatus() != null && filter.getStatus().length > 0)
			{
				sb.append(" and form.status in (");
				String sep = "";
				for (int i = 0; i < filter.getStatus().length; i++)
				{
					sb.append(sep).append("?");
					params.add(filter.getStatus()[i]);
					sep = ", ";
				}
				sb.append(") ");
			}
		}
		if(filter.getSearchString() != null && filter.getSearchString().trim().length() > 0)
		{
			sb.append(" and (upper(form.identityNumber) like ? or upper(form.description) like ?)");
			params.add("%"+filter.getSearchString().trim()+"%");
			params.add("%"+filter.getSearchString().trim()+"%");
		}
		
		
		return new QueryObject(sb.toString(), params);
	}
}
