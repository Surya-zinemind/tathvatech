package com.tathvatech.forms.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.forms.common.TestProcSectionListFilter;
import com.tathvatech.forms.common.TestProcSectionListReportResultRow;
import com.tathvatech.testsutra.ncr.common.QueryObject;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.common.DummyWorkstation;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.project.UnitLocation;
import com.tathvatech.ts.core.survey.response.FormStatusEnum;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import com.thirdi.surveyside.project.UnitInProjectObj;
import com.thirdi.surveyside.project.UnitManager;
import com.thirdi.surveyside.utils.DateUtils;

public class TestProcSectionListReport
{
	private TestProcSectionListFilter filter;
	private Date forecastTestStartDateFrom = null;
	private Date forecastTestStartDateTo = null;
	private Date forecastTestCompletionDateFrom = null;
	private Date forecastTestCompletionDateTo = null;

	public TestProcSectionListReport(UserContext context, TestProcSectionListFilter filter)
	{
		this.filter = filter;
		
		if (filter.getForecastTestStartDateFilter() != null)
		{
			Date[] forecastStartDateRange = filter.getForecastTestStartDateFilter().getResolvedDateRangeValues();
			if(forecastStartDateRange != null)
			{
				forecastTestStartDateFrom = forecastStartDateRange[0];
				if(forecastStartDateRange != null)
					forecastTestStartDateFrom = DateUtils.shiftDate(forecastTestStartDateFrom, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
				
				forecastTestStartDateTo = forecastStartDateRange[1];
				if(forecastTestStartDateTo != null)
					forecastTestStartDateTo = DateUtils.shiftDate(forecastTestStartDateTo, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
			}
		}
		if (filter.getForecastTestCompletionDateFilter() != null)
		{
			Date[] forecastCompletionDateRange = filter.getForecastTestCompletionDateFilter().getResolvedDateRangeValues();
			if(forecastCompletionDateRange != null)
			{
				forecastTestCompletionDateFrom = forecastCompletionDateRange[0];
				if(forecastTestCompletionDateFrom != null)
					forecastTestCompletionDateFrom = DateUtils.shiftDate(forecastTestCompletionDateFrom, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
				
				forecastTestCompletionDateTo = forecastCompletionDateRange[1];
				if(forecastTestCompletionDateTo != null)
					forecastTestCompletionDateTo = DateUtils.shiftDate(forecastTestCompletionDateTo, context.getTimeZone(), TimeZone.getTimeZone("UTC"));
			}
		}
		
	}
	
	public  List<TestProcSectionListReportResultRow> getTestProcs()
	{
		QueryObject q = getSql();
    	return PersistWrapper.readList(TestProcSectionListReportResultRow.class, q.getQuery(), (q.getParams().size() > 0)?q.getParams().toArray(new Object[q.getParams().size()]):null);
	}
	
	public QueryObject getSql()
	{	
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select "
				+ " tfs.pk as pk, "
				+ " formSecMain.itemNo as sectionName, "
				+ " formSecMain.description as sectionDescription, "
				+ " ut.pk as testProcPk, "
				+ " uth.name as testName, "
				+ " uth.projectTestProcPk as projectTestProcPk, "
				+ " uth.projectPk as projectPk, "
				+ " p.projectName as projectName, "
				+ " p.projectDescription as projectDescription, "
				+ " part.pk as partPk, "
				+ " part.name as partName, "
				+ " partType.typeName as partType, "
				+ " pp.name as projectPartName, "
				+ " uth.workstationPk as workstationPk, "
				+ " w.workstationName as workstationName, "
				+ " w.description as workstationDescription, "
				+ " case when ul.status is null then '" + UnitLocation.STATUS_WAITING + "' else ul.status end as workstationStatus, "
				+ "site.timeZone as workstationTimezoneId, "
				+ " uth.unitPk as unitPk, "
				+ " tfa.formFk as formPk, "
				+ " uh.unitName as unitName, "
				+ " uh.unitDescription, "
				+ " uh.serialNo as unitSerialNumber, "
				+ " uprh.projectPartPk as projectPartPk, "
				+ " f.formMainPk as formMainPk, "
				+ " f.identityNumber as formName, "
				+ " f.description as formDescription, "
				+ " f.responsibleDivision as formResponsibleDivision, "
				+ " f.revision as formRevision, "
				+ " f.versionNo as formVersion, "
				+ " res.responseId, "
				+ " res.responseRefNo, "
				+ " res.noOfComments as noOfComments, "
				+ " res.percentComplete as percentComplete, "
				+ " res.totalQCount, "
				+ " res.totalACount, "
				+ " res.passCount, "
				+ " res.failCount, "
				+ " res.dimentionalFailCount, "
				+ " res.naCount, "
				+ " responseStartTime, "
				+ " responseCompleteTime, "
				+ " responseSyncTime, "
				+ " secRes.pk as secResponsePk, "
				+ " secRes.noOfComments as secNoOfComments, "
				+ " secRes.percentComplete as secPercentComplete, "
				+ " secRes.totalQCount as totalQCount, "
				+ " secRes.totalACount as totalACount, "
				+ " secRes.passCount as passCount, "
				+ " secRes.failCount as failCount, "
				+ " secRes.dimentionalFailCount as dimentionalFailCount, "
				+ " secRes.naCount as naCount, "
				+ " secRes.startDate as actualStartDate, "
				+ " secRes.completionDate as actualCompletionDate, "
				+ " case when res.status is null then 'Not Started' else res.status end as responseStatus, ");
		
		if(filter.getFetchWorkstationOrTestForecastAsSectionForecast() == true)
		{
			sb.append(" case when seces.forecastStartDate is not null then seces.forecastStartDate "
					+ " when tpes.forecastStartDate is not null then tpes.forecastStartDate "
					+ " else uwes.forecastStartDate end "
					+ " as forecastStartDate, ");
			
			sb.append(" case when seces.forecastEndDate is not null then seces.forecastEndDate "
					+ " when tpes.forecastEndDate is not null then tpes.forecastEndDate "
					+ " else uwes.forecastEndDate end "
					+ " as forecastEndDate, ");
		}
		else
		{
			sb.append(" seces.forecastStartDate as forecastStartDate, ");
			sb.append(" seces.forecastEndDate as forecastEndDate, ");
		}
		
		sb.append(" seces.estimateHours as forecastHours, "
		+ " seces.comment as forecastComment, "
		+ " seces.createdBy as forecastCreatedBy, "
		+ " seces.createdDate as forecastCreatedDate,"
		+ " secesUser.firstName as forecastCreatedByFirstName, "
		+ " secesUser.lastName as forecastCreatedByLastName "
		+ " from testproc_form_section tfs "
		+ " join form_section formSec on tfs.formSectionFk = formSec.pk "
		+ " join form_section_main formSecMain on formSec.formSectionMainPk = formSecMain.pk "
		+ " join testproc_form_assign tfa on tfs.testProcFormAssignFk = tfa.pk and tfa.current = 1"
		+ " join unit_testproc ut on tfa.testProcFk = ut.pk "
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
		+ " left outer join TAB_SECTION_RESPONSE secRes on secRes.formSectionFk = formSec.pk and secRes.responseId = res.responseId "
		+ " left outer join entity_schedule seces on seces.objectPk = tfs.pk and seces.objectType = " + EntityTypeEnum.TestProcSection.getValue() + " and now() between seces.effectiveDateFrom and seces.effectiveDateTo "
		+ " left outer join entity_schedule tpes on tpes.objectPk = ut.pk and tpes.objectType = " + EntityTypeEnum.TestProc.getValue() + " and now() between tpes.effectiveDateFrom and tpes.effectiveDateTo "
		+ " left outer join entity_schedule uwes on uwes.objectPk = uw.pk and uwes.objectType = " + EntityTypeEnum.UnitWorkstation.getValue() + " and now() between uwes.effectiveDateFrom and uwes.effectiveDateTo "
		+ " left outer join TAB_USER secesUser on seces.createdBy = secesUser.pk "
		+ " where 1 = 1 ");	

		if(filter.getTestProcOID() != null)
		{
			sb.append(" and ut.pk = ? ");
			params.add(filter.getTestProcOID().getPk());
		}
		if(filter.getUnitOID() != null)
		{
			if(filter.isIncludeChildren() == true)
			{
				if(filter.getProjectOID() == null)
				{
					throw new AppException("Project should be specified to include chileren of the selected unit");
				}
				UnitInProjectObj unit = UnitManager.getUnitInProject(filter.getUnitOID(), filter.getProjectOID());
				sb.append(" and ( (upr.unitPk = ? and upr.projectPk = ?) or (uprh.rootParentPk = ? and uprh.heiCode like ?) ) ");
				params.add(filter.getUnitOID().getPk());
				params.add(filter.getProjectOID().getPk());
				params.add(unit.getRootParentPk());
				params.add(unit.getHeiCode()+".%");
			}
			else
			{
				sb.append(" and u.pk = ?");
				params.add(filter.getUnitOID().getPk());
			}
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
		if(forecastTestStartDateFrom != null)
		{
			sb.append(" and convert_tz(forecastStartDate, site.timeZone, 'UTC') >= ? ");
			params.add(forecastTestStartDateFrom);
		}
		if(forecastTestStartDateTo != null)
		{
			sb.append(" and convert_tz(forecastStartDate, site.timeZone, 'UTC') < ? ");
			params.add(forecastTestStartDateTo);
		}
		if(forecastTestCompletionDateFrom != null)
		{
			sb.append(" and convert_tz(forecastEndDate, site.timeZone, 'UTC') >= ? ");
			params.add(forecastTestCompletionDateFrom);
		}
		if(forecastTestCompletionDateTo != null)
		{
			sb.append(" and convert_tz(forecastEndDate, site.timeZone, 'UTC') < ? ");
			params.add(forecastTestCompletionDateTo);
		}
		
		sb.append(" and 1 = 1");
		sb.append(" order by uprh.level, uprh.orderNo, w.orderNo, f.identityNumber, formSec.orderNo ");
		
		return new QueryObject(sb.toString(), params);
	}
	
}
