package com.tathvatech.forms.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.TestProcFilter;

import com.tathvatech.forms.enums.FormStatusEnum;
import com.tathvatech.unit.common.UnitFormQuery;
import com.tathvatech.unit.entity.UnitLocation;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.workstation.common.UnitInProjectObj;


public class TestProcListReport
{
	private PersistWrapper persistWrapper;
	private UnitManager unitManager;
	UserContext context;
	ReportRequest reportRequest;
	private TestProcFilter filter;
	private Date forecastTestStartDateFrom = null;
	private Date forecastTestStartDateTo = null;
	private Date forecastTestCompletionDateFrom = null;
	private Date forecastTestCompletionDateTo = null;

	public TestProcListReport(UserContext context, TestProcFilter filter, PersistWrapper persistWrapper, UnitManager unitManager, UnitManager unitManager1)
	{
		this(context, ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.TestProcListReport, filter), unitManager1);
        this.persistWrapper = persistWrapper;
        this.unitManager = unitManager;
    }
	
	public TestProcListReport(PersistWrapper persistWrapper, UnitManager unitManager, UserContext context, ReportRequest reportRequest)
	{
        this.persistWrapper = persistWrapper;
        this.unitManager = unitManager;
        this.context = context;
		this.reportRequest = reportRequest;
		this.filter = (TestProcFilter) reportRequest.getFilter();
		
		if (filter.getForecastTestStartDateFilter() != null)
		{
			Date[] forecastStartDateRange = filter.getForecastTestStartDateFilter().getResolvedDateRangeValues();
			if(forecastStartDateRange != null)
			{
				forecastTestStartDateFrom = forecastStartDateRange[0];
			//	if(forecastStartDateRange != null)
			//		forecastTestStartDateFrom = DateUtils.shiftDate(forecastTestStartDateFrom, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
				
				forecastTestStartDateTo = forecastStartDateRange[1];
			//	if(forecastTestStartDateTo != null)
			//		forecastTestStartDateTo = DateUtils.shiftDate(forecastTestStartDateTo, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
			}
		}
		if (filter.getForecastTestCompletionDateFilter() != null)
		{
			Date[] forecastCompletionDateRange = filter.getForecastTestCompletionDateFilter().getResolvedDateRangeValues();
			if(forecastCompletionDateRange != null)
			{
				forecastTestCompletionDateFrom = forecastCompletionDateRange[0];
				//if(forecastTestCompletionDateFrom != null)
				//	forecastTestCompletionDateFrom = DateUtils.shiftDate(forecastTestCompletionDateFrom, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
				
				forecastTestCompletionDateTo = forecastCompletionDateRange[1];
				//if(forecastTestCompletionDateTo != null)
				//	forecastTestCompletionDateTo = DateUtils.shiftDate(forecastTestCompletionDateTo, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
			}
		}
		
	}

	public TestProcListReport(UserContext context, TestProcFilter filter, UnitManager unitManager) {
        this.unitManager = unitManager;
    }

	public TestProcListReport(UserContext context, TestProcFilter filter) {
	}

	public  ReportResponse runReport()
	{
		ReportResponse response = new ReportResponse();
		response.setStartIndex(reportRequest.getStartIndex());
		
		QueryObject qb = getSql();
		if (reportRequest.isFetchRowCount())
		{
			// we need to find the count if the request is newRequest.
			StringBuffer countQB = new StringBuffer("select count(*) from ( ").append(qb.getQuery()).append(" )data");
			long totalRowCount = persistWrapper.read(Long.class, countQB.toString(),
					(qb.getParams().size() > 0) ? qb.getParams().toArray(new Object[qb.getParams().size()]) : null);
			response.setTotalRows(totalRowCount);
		}
		
		
		StringBuilder sql = new StringBuilder(qb.getQuery());
		if (reportRequest.getFetchAllRows() == false)
		{
			sql.append(" limit ").append(reportRequest.getStartIndex()).append(", ")
					.append(reportRequest.getRowsToFetch());
		}
		List<UnitFormQuery> rows = persistWrapper.readList(UnitFormQuery.class, sql.toString(),
				qb.getParams().toArray());

		response.setReportData(rows);

		return response;
	}
	
	public  List<UnitFormQuery> getTestProcs()
	{
		QueryObject q = getSql();
		return persistWrapper.readList(UnitFormQuery.class, q.getQuery(), (q.getParams().size() > 0)?q.getParams().toArray(new Object[q.getParams().size()]):null);
	}
	
	public QueryObject getSql()
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select ut.pk as pk, uth.name as name, uth.projectTestProcPk as projectTestProcPk, "
		+ " uth.projectPk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription, tfa.appliedByUserFk, "
		+ " part.pk as partPk, part.name as partName, partType.typeName as partType, pp.name as projectPartName, "
		+ " uth.workstationPk as workstationPk, w.workstationName as workstationName, w.description as workstationDescription, "
		+ " case when ul.status is null then '" + UnitLocation.STATUS_WAITING + "' else ul.status end as workstationStatus, site.timeZone as workstationTimezoneId, "
		+ " uth.unitPk as unitPk, tfa.formFk as formPk, "
		+ " uh.unitName as unitName, uh.unitDescription, uh.serialNo as unitSerialNumber, "
		+ " uprh.projectPartPk as projectPartPk, f.formMainPk as formMainPk, "
		+ " f.identityNumber as formName, f.description as formDescription, "
		+ " f.responsibleDivision as formResponsibleDivision, f.revision as formRevision, "
		+ " f.versionNo as formVersion, "
		+ " res.responseId, res.responseRefNo, "
		+ " res.noOfComments as noOfComments, res.percentComplete as percentComplete, "
		+ " res.totalQCount, res.totalACount, res.passCount, res.failCount, res.dimentionalFailCount, res.naCount, "
		+ " responseStartTime, responseCompleteTime, responseSyncTime, "
		+ " res.userPk as preparedByPk, prepUser.firstName as preparedByFirstName, case when res.status is null then 'Not Started' else res.status end as responseStatus, "
		+ " prepUser.lastName as preparedByLastName, res.verifiedBy as verifiedByPk, verifyUser.firstName as verifiedByFirstName,"
		+ " verifyUser.lastName as verifiedByLastName, res.approvedBy as approvedByPk, apprUser.firstName as approvedByFirstName,"
		+ " apprUser.lastName as approvedByLastName, res.verifiedDate, res.verifyComment, res.approvedDate, res.approveComment,");
		
		if(filter.getFetchWorkstationForecastAsTestForecast() == true)
		{
			sb.append(" case when es.forecastStartDate is null then uwes.forecastStartDate else es.forecastStartDate end as forecastStartDate, "
					+ " case when es.forecastEndDate is null then uwes.forecastEndDate else es.forecastEndDate end as forecastEndDate, ");
		}
		else
		{
			sb.append(" es.forecastStartDate as forecastStartDate, "
					+ " es.forecastEndDate as forecastEndDate, ");
		}
		
		sb.append(" es.estimateHours as forecastHours, "
		+ " es.comment as forecastComment, "
		+ " es.createdBy as forecastCreatedBy, "
		+ " es.createdDate as forecastCreatedDate,"
		+ " esUser.firstName as forecastCreatedByFirstName, esUser.lastName as forecastCreatedByLastName "
		+ " from unit_testproc ut"
		+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
		+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
		+ " join TAB_UNIT u on uth.unitPk = u.pk "
		+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
		+ " join TAB_PROJECT p on uth.projectPk = p.pk "
		+ " join TAB_WORKSTATION w on uth.workstationPk = w.pk and w.pk != " + DummyWorkstation.getPk() + ""
		+ " join TAB_UNIT_WORKSTATIONS uw on uth.unitPk = uW.unitPk and uth.projectPk = uw.projectPk and uth.workstationPk = uw.workstationPk "
		+ " join site on w.sitePk = site.pk"
		+ " join TAB_SURVEY f on tfa.formFk = f.pk "
		+ " join unit_project_ref upr on upr.unitPk = uth.unitPk and upr.projectPk = uth.projectPk"
		+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
		+ " left outer join project_part pp on uprh.projectPartPk = pp.pk "
		+ " left outer join part on pp.partPk = part.pk "
		+ " left outer join part_type partType on pp.partTypePk = partType.pk"
		+ " left outer join TAB_UNIT_LOCATION ul on uth.unitPk = ul.unitPk and uth.projectPk = ul.projectPk and uth.workstationPk = ul.workstationPk and ul.current = 1"
		+ " left outer join TAB_RESPONSE res on res.testProcPk = ut.pk and res.surveyPk = tfa.formFk and res.current = 1 "
		+ " left outer join entity_schedule es on es.objectPk = ut.pk and es.objectType = " + EntityTypeEnum.TestProc.getValue() + " and now() between es.effectiveDateFrom and es.effectiveDateTo "
		+ " left outer join entity_schedule uwes on uwes.objectPk = uw.pk and uwes.objectType = " + EntityTypeEnum.UnitWorkstation.getValue() + " and now() between uwes.effectiveDateFrom and uwes.effectiveDateTo "
		+ " left outer join TAB_USER esUser on es.createdBy = esUser.pk "
		+ " left outer join TAB_USER prepUser on res.userPk = prepUser.pk "
		+ " left outer join TAB_USER verifyUser on res.verifiedBy = verifyUser.pk "
		+ " left outer join TAB_USER apprUser on res.approvedBy = apprUser.pk ");

		if(filter.getUnitOID() != null)
		{
			if(filter.isIncludeChildren() == true)
			{
				if(filter.getProjectOIDForUnitHeirarchy() == null)
				{
					throw new AppException("Project should be specified to include chileren of the selected unit");
				}
				UnitInProjectObj unit = unitManager.getUnitInProject(filter.getUnitOID(), filter.getProjectOIDForUnitHeirarchy());
				sb.append(" join (select upr.unitPk from unit_project_ref upr "
						+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
						+ " where ( (upr.unitPk = ? and upr.projectPk = ?) or (upr.projectPk = ? and uprh.rootParentPk = ? and uprh.heiCode like ?) ) )units on units.unitPk = uth.unitPk ");
				params.add(unit.getUnitPk());
				params.add(filter.getProjectOIDForUnitHeirarchy().getPk());
				params.add(filter.getProjectOIDForUnitHeirarchy().getPk());
				params.add(unit.getRootParentPk());
				params.add(unit.getHeiCode()+".%");
			}
			else
			{
				sb.append(" join (select pk from tab_unit u where u.pk = ? )units on units.pk = uth.unitPk");
				params.add(filter.getUnitOID().getPk());
			}
		}
		
		
		sb.append(" where 1 = 1 ");	

		
		if(filter.getUnitOIDList() != null)
		{
			if (filter.getUnitOIDList() != null && filter.getUnitOIDList().size() > 0)
			{
				sb.append(" and u.pk in (");
				String sep = "";
				for (Iterator iterator = filter.getUnitOIDList().iterator(); iterator.hasNext();)
				{
					UnitOID aUnitOID = (UnitOID) iterator.next();
					sb.append(sep).append("?");
					sep = ", ";
					params.add(aUnitOID.getPk());
				}
				sb.append(")");
			}
		}

		if(filter.getUnitNameSearchString() != null && filter.getUnitNameSearchString().trim().length() > 0)
		{
			sb.append(" and (upper(uh.unitName) like ? or upper(uh.serialNo) like ? )");
			params.add("%"+filter.getUnitNameSearchString().toUpperCase()+"%");
			params.add("%"+filter.getUnitNameSearchString().toUpperCase()+"%");
		}
		if(filter.getProjectOID() != null)
		{
			sb.append(" and uth.projectPk = ?");
			params.add(filter.getProjectOID().getPk());
		}
		if(filter.getWorkstationOID() != null)
		{
			sb.append(" and uth.workstationPk = ?");
			params.add(filter.getWorkstationOID().getPk());
		}
		if(filter.getFormOID() != null)
		{
			sb.append(" and tfa.formFk = ?");
			params.add(filter.getFormOID().getPk());
		}
		if(filter.getFormNameSearchString() != null && filter.getFormNameSearchString().trim().length() > 0)
		{
			sb.append(" and (upper(f.identityNumber) like ? or upper(f.description) like ? )");
			params.add("%"+filter.getFormNameSearchString().toUpperCase()+"%");
			params.add("%"+filter.getFormNameSearchString().toUpperCase()+"%");
		}
		if(filter.getTestNameSearchString() != null && filter.getTestNameSearchString().trim().length() > 0)
		{
			sb.append(" and upper(uth.name) like ? ");
			params.add("%"+filter.getTestNameSearchString().toUpperCase()+"%");
		}
		if(filter.getTestStatus() != null && filter.getTestStatus().length > 0)
		{
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			
			String sep = "";
			for (int i = 0; i < filter.getTestStatus().length; i++)
			{
				if(filter.getTestStatus()[i] == FormStatusEnum.NOTSTARTED)
				{
					sb1.append("res.status is null");
				}
				else
				{
					sb2.append(sep).append("?");
					params.add(filter.getTestStatus()[i].getStatusString());
					sep = ",";
				}
			}
			if(sb2.length() > 0)
			{
				sb2.insert(0, " res.status in (");
				sb2.append(")");
			}
			
			if(sb1.length() > 0 && sb2.length() > 0)
			{
				sb1.append(" or ");
			}
			
			sb.append(" and (").append(sb1).append(sb2).append(")");
		}
		
		sb.append(" having 1 = 1 ");
		if(forecastTestStartDateFrom != null)
		{
			sb.append(" and convert_tz(forecastStartDate, 'UTC', site.timeZone) >= convert_tz(?, 'UTC', site.timeZone) ");
			params.add(forecastTestStartDateFrom);
		}
		if(forecastTestStartDateTo != null)
		{
			sb.append(" and convert_tz(forecastStartDate, 'UTC', site.timeZone) < convert_tz(?, 'UTC', site.timeZone) ");
			params.add(forecastTestStartDateTo);
		}
		if(forecastTestCompletionDateFrom != null)
		{
			sb.append(" and convert_tz(forecastEndDate, 'UTC', site.timeZone) >= convert_tz(?, 'UTC', site.timeZone)  ");
			params.add(forecastTestCompletionDateFrom);
		}
		if(forecastTestCompletionDateTo != null)
		{
			sb.append(" and convert_tz(forecastEndDate, 'UTC', site.timeZone) < convert_tz(?, 'UTC', site.timeZone) ");
			params.add(forecastTestCompletionDateTo);
		}
		
		sb.append(" order by uprh.level, uprh.orderNo, w.orderNo, f.identityNumber");
		return new QueryObject(sb.toString(), params);
	}
	
}
