package com.tathvatech.forms.report;

import com.tathvatech.common.common.ApplicationProperties;
import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.enums.FormStatusEnum;
import com.tathvatech.forms.request.TestProcStatusSummaryReportRequest;
import com.tathvatech.forms.request.TestProcStatusSummaryReportResult;
import com.tathvatech.forms.request.TestProcStatusSummaryReportResultRow;
import com.tathvatech.unit.service.UnitManager;
import com.tathvatech.user.OID.ProjectOID;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UnitOID;
import com.tathvatech.user.OID.WorkstationOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.forms.response.ResponseMasterNew;
import com.tathvatech.workstation.common.DummyWorkstation;
import com.tathvatech.workstation.common.UnitInProjectObj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class TestProcStatusSummaryReport
{
	private final PersistWrapper persistWrapper;
	private final DummyWorkstation dummyWorkstation;
	private final UnitManager unitManager;
	private static String dbtimezone = ApplicationProperties.getDBTimezone();

	UserContext context;
	private TestProcStatusSummaryReportRequest request;
	List<TestProcStatusSummaryReportRequest.GroupingCol> groupingColList = new ArrayList<>();

	public TestProcStatusSummaryReport(PersistWrapper persistWrapper, DummyWorkstation dummyWorkstation, UnitManager unitManager, UserContext context, TestProcStatusSummaryReportRequest request)
	{
        this.persistWrapper = persistWrapper;
        this.dummyWorkstation = dummyWorkstation;
        this.unitManager = unitManager;
        this.context = context;
		this.request = request;
		if (request.getGroupingSet() != null && request.getGroupingSet().size() > 0)
			groupingColList.addAll(request.getGroupingSet());

		// Without projectPk we cannot join to the UNIT_WORKSTATION And
		// UNIT_LOCATION tables to get the values.
		// so we have to join to the project table and for that we need a
		// grouping on project if it is mentioned in the filter.
		// TODO:: may be adding this grouping based on the filter and adding all
		// if none is present is not a good approach.
		if (request.getUnitOID() != null && request.isIncludeChildrenUnits() == false
				&& !groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.unit))
		{
			groupingColList.add(TestProcStatusSummaryReportRequest.GroupingCol.unit);
		}

		if (request.getProjectOIDList() != null && request.getProjectOIDList().size() == 1
				&& !groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.project))
		{
			groupingColList.add(TestProcStatusSummaryReportRequest.GroupingCol.project);
		}

		// if no grouping are mentioned, this means that the stats data is on
		// each form. which is same as grouping on all columns.
		if (groupingColList == null || groupingColList.size() == 0)
		{
			groupingColList = Arrays.asList(new TestProcStatusSummaryReportRequest.GroupingCol[] {
					TestProcStatusSummaryReportRequest.GroupingCol.project,
					TestProcStatusSummaryReportRequest.GroupingCol.ProjectPart,
					TestProcStatusSummaryReportRequest.GroupingCol.unit,
					TestProcStatusSummaryReportRequest.GroupingCol.workstation,
					TestProcStatusSummaryReportRequest.GroupingCol.form });
		}
	}

	public TestProcStatusSummaryReportResult runReport()
	{
		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate) && groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate))
			throw new AppException("You cannot group by the forecast start and end dates at the same time.");
		
		StringBuffer sb = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sb.append("select ");
		
		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.project))
			sb.append("projectPk, projectName, projectDescription ");
		else
			sb.append("null as projectPk, null as projectName, null as projectDescription ");

		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.ProjectPart))
			sb.append(", projectPartPk, projectPartName ");
		else
			sb.append(", null as projectPartPk, null as projectPartName ");

		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.unit))
			sb.append(" , unitPk, unitName, unitDescription ");
		else
			sb.append(" , null as unitPk, null as unitName, null as unitDescription ");
			
		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.workstation))
			sb.append(" , workstationPk, workstationName, workstationDescription, sitePk, siteName ");
		else
			sb.append(" , null as workstationPk, null as workstationName, null as workstationDescription, null as sitePk, null as siteName ");

		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.unit) && groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.project) && groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.workstation))
			sb.append(" , workstationMoveInDate, workstationStartDate, workstationCompleteDate,  workstationForecastStartDate, workstationForecastEndDate, workstationEstimateHours, workstationStatus ");
		else
			sb.append(" , null as workstationMoveInDate, null as workstationStartDate, null as workstationCompleteDate, null as workstationForecastStartDate, null as workstationForecastEndDate, null as workstationEstimateHours, null as workstationStatus ");
		// please note.. in this report, we are treating workstationEstimateHours only as a regular workstation attribute and not as a stats data which can be summed up. 
		// we get multiple rows with the same value for the workstation estimate hours as we get test level rows, so summing them will have a wrong value for the workstationEstimateHours
		
		if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.form))
			sb.append(" , testPk, testName, formPk, formName, formDescription ");
		else
			sb.append(" , null as testPk, null as testName, null as formPk, null as formName, null as formDescription ");

		if (request.getGroupingSet().contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate))
		{
			sb.append(" , DATE(convert_tz(forecastStartDate, ?, siteTimeZone)) as forecastStartDate ");
			sb.append(" , concat(DAY(convert_tz(forecastStartDate, ?, siteTimeZone)), '/', MONTH(convert_tz(forecastStartDate, ?, siteTimeZone)), '/', YEAR(convert_tz(forecastStartDate, ?, siteTimeZone))) as forecastStartDateString ");
			params.add(dbtimezone);
			params.add(dbtimezone);
			params.add(dbtimezone);
			params.add(dbtimezone);
		}
		else
		{
			sb.append(" , null as forecastStartDate, null as  forecastStartDateString");
		}

		if (request.getGroupingSet().contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate))
		{
			sb.append(" , DATE(convert_tz(forecastEndDate, ?, siteTimeZone)) as forecastEndDate ");
			sb.append(" , concat(DAY(convert_tz(forecastEndDate, ?, siteTimeZone)), '/', MONTH(convert_tz(forecastEndDate, ?, siteTimeZone)), '/', YEAR(convert_tz(forecastEndDate, ?, siteTimeZone))) as forecastEndDateString ");
			params.add(dbtimezone);
			params.add(dbtimezone);
			params.add(dbtimezone);
			params.add(dbtimezone);
		}
		else
		{
			sb.append(" , null as forecastEndDate, null as forecastEndDateString ");
		}

		sb.append(" , sum(statsData.counter) as totalFormCount, sum(notStarted) as notStartedCount, sum(inProgress) as inProgressCount, sum(paused) as pausedCount, sum(completed) as completedCount, sum(verified) as verifiedCount, sum(approved) as approvedCount, sum(approvedWithComments) as approvedWithCommentsCount ");
		sb.append(" , case when sum(statsData.counter) != 0 then (sum(statsData.percentComplete)/sum(statsData.counter)) else 0 end as percentComplete"
				+ " , sum(statsData.noOfComments) as noOfComments, sum(statsData.totalQCount) as totalQCount "
				+ " , sum(statsData.totalACount) as totalACount, sum(statsData.passCount) as passCount, sum(statsData.failCount) as failCount, sum(statsData.dimentionalFailCount) as dimentionalFailCount, sum(statsData.naCount) as naCount "
				+ " , sum(statsData.testEstimateHours) as testEstimateHours "
				+ " , sum(statsData.oilTransferCount) as oilTransferCount "
				+ " , sum(statsData.ncrTransferCount) as ncrTransferCount ");

		sb.append(" from ");
		
		sb.append(" ( ");
		QueryObject ob = getStatsDataSql();
		sb.append(ob.getQuery());
		params.addAll(ob.getParams());
		sb.append(") statsData ");

		
		if(groupingColList != null && groupingColList.size() > 0)
		{
			sb.append(" group by ");
			
			String groupSep = "";
			for (Iterator iterator = groupingColList.iterator(); iterator.hasNext();)
			{
				TestProcStatusSummaryReportRequest.GroupingCol aCol = (TestProcStatusSummaryReportRequest.GroupingCol) iterator.next();
				if(TestProcStatusSummaryReportRequest.GroupingCol.project == aCol)
				{
					sb.append(groupSep).append("projectPk");
					groupSep = ",";
				}
				if(TestProcStatusSummaryReportRequest.GroupingCol.ProjectPart == aCol)
				{
					sb.append(groupSep).append("projectPartPk");
					groupSep = ",";
				}
				else if(TestProcStatusSummaryReportRequest.GroupingCol.unit == aCol)
				{
					sb.append(groupSep).append("unitPk");
					groupSep = ",";
				}
				else if(TestProcStatusSummaryReportRequest.GroupingCol.workstation == aCol)
				{
					sb.append(groupSep).append("workstationPk");
					groupSep = ",";
				}
				else if(TestProcStatusSummaryReportRequest.GroupingCol.form == aCol)
				{
					sb.append(groupSep).append("testPk");
					groupSep = ",";
				}
				if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate))
				{
					sb.append(groupSep).append("forecastStartDate");
					groupSep = ",";
				}
				if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate))
				{
					sb.append(groupSep).append("forecastEndDate");
					groupSep = ",";
				}
			}
		}
		
		
		if(groupingColList != null && groupingColList.size() > 0)
		{
			sb.append(" order by ");
			String orderSep = "";
			
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.project))
			{
				sb.append(orderSep).append(" projectCreatedDate ");
				orderSep = ", ";
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.ProjectPart))
			{
				sb.append(orderSep).append(" projectPartName ");
				orderSep = ", ";
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.unit) && groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.project))
			{
				sb.append(orderSep).append(" unitLevel, unitOrderNo ");
				orderSep = ", ";
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.workstation))
			{
				sb.append(orderSep).append(" workstationOrderNo ");
				orderSep = ", ";
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.form))
			{
				sb.append(orderSep).append(" testPk ");
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate))
			{
				sb.append(orderSep).append(" forecastStartDate ");
			}
			if(groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate))
			{
				sb.append(orderSep).append(" forecastEndDate ");
			}
		}
		List<TestProcStatusSummaryReportResultRow> rows = persistWrapper.readList(TestProcStatusSummaryReportResultRow.class, sb.toString(), params.toArray());
		TestProcStatusSummaryReportResult result = new TestProcStatusSummaryReportResult();
		result.setReportResult(rows);
		
		return result;
	}

	private QueryObject getStatsDataSql()
	{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("select ut.pk as testPk, uth.name as testName, "
				+ " uth.projectPk as projectPk, p.projectName as projectName, p.projectDescription as projectDescription, p.createdDate as projectCreatedDate, "
				+ " projpart.pk as projectPartPk, projpart.name as projectPartName, "
				+ " uth.workstationPk as workstationPk, w.workstationName as workstationName, w.description as workstationDescription, w.orderNo as workstationOrderNo, "
				+ " s.pk as sitePk, s.name as siteName, s.timeZone as siteTimeZone, "
				+ " uth.unitPk as unitPk, uh.unitName as unitName, uh.unitDescription as unitDescription, uprh.level as unitLevel, uprh.orderNo as unitOrderNo, "
				+ " ul.moveInDate as workstationMoveInDate, ul.firstFormSaveDate as workstationStartDate, ul.completedDate as workstationCompleteDate, (case when ul.status is null then 'Waiting' else ul.status end) as workstationStatus, "
				+ " DATE(convert_tz(wses.forecastStartDate,'"+dbtimezone+"', s.timeZone)) as workstationForecastStartDate, "
				+ " DATE(convert_tz(wses.forecastEndDate,'"+dbtimezone+"', s.timeZone) ) as workstationForecastEndDate, "
				+ " tfa.formFk as formPk, f.identityNumber as formName, f.description as formDescription, "
				+ " case when es.forecastStartDate is null then DATE(convert_tz(wses.forecastStartDate,'"+dbtimezone+"', s.timeZone) ) else DATE(convert_tz(es.forecastStartDate,'"+dbtimezone+"', s.timeZone)) end as forecastStartDate, "
				+ " case when es.forecastEndDate is null then DATE(convert_tz(wses.forecastEndDate,'"+dbtimezone+"', s.timeZone)) else DATE(convert_tz(es.forecastEndDate,'"+dbtimezone+"', s.timeZone)) end as forecastEndDate, "
				+ " res.responseId, " + " res.noOfComments as noOfComments, res.percentComplete as percentComplete, "
				+ " res.totalQCount, res.totalACount, res.passCount, res.failCount, res.dimentionalFailCount, res.naCount, "
				+ " 1 as counter, " + " case when res.status is null then 1 else 0  end as notStarted, "
				+ " case when res.status = '" + ResponseMasterNew.STATUS_INPROGRESS + "' then 1 else 0  end as inProgress, " 
				+ " case when res.status = '" + ResponseMasterNew.STATUS_PAUSED + "' then 1 else 0  end as paused, " 
				+ " case when res.status = '" + ResponseMasterNew.STATUS_COMPLETE + "' then 1 else 0  end as completed, " 
				+ " case when res.status = '" + ResponseMasterNew.STATUS_VERIFIED + "' then 1 else 0  end as verified, " 
				+ " case when res.status = '" + ResponseMasterNew.STATUS_APPROVED + "' then 1 else 0  end as approved, " 
				+ " case when res.status = '" + ResponseMasterNew.STATUS_APPROVED_WITH_COMMENTS + "' then 1 else 0  end as approvedWithComments, " 
				+ " es.estimateHours as testEstimateHours, "
				+ " wses.estimateHours as workstationEstimateHours, "
				+ " oilTransferT.transferCount as oilTransferCount, "
				+ " ncrTransferT.transferCount as ncrTransferCount " 
				+ " from unit_testproc ut "
				+ " join testproc_form_assign tfa on tfa.testProcFk = ut.pk and tfa.current = 1 "
				+ " join unit_testproc_h uth on uth.unitTestProcFk = ut.pk and now() between uth.effectiveDateFrom and uth.effectiveDateTo "
				+ " left outer join entity_schedule es on es.objectPk = ut.pk and es.objectType = "
				+ EntityTypeEnum.TestProc.getValue() + " and now() between es.effectiveDateFrom and es.effectiveDateTo "
				+ " join TAB_UNIT u on uth.unitPk = u.pk "
				+ " join TAB_UNIT_H uh on uh.unitPk = u.pk and now() between uh.effectiveDateFrom and uh.effectiveDateTo"
				+ " join TAB_PROJECT p on uth.projectPk = p.pk "
				+ " join TAB_WORKSTATION w on uth.workstationPk = w.pk and w.pk != " + dummyWorkstation.getPk()
				+ " join TAB_SURVEY f on tfa.formFk = f.pk "
				+ " join unit_project_ref upr on upr.unitPk = uth.unitPk and upr.projectPk = uth.projectPk "
				+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
				+ " join site s on w.sitePk = s.pk "
				+ " join TAB_UNIT_WORKSTATIONS uw on uw.projectPk = uth.projectPk and uw.unitPk = uth.unitPk and uw.workstationPk = uth.workstationPk "
				+ " left outer join project_part projpart on uprh.projectPartPk = projpart.pk "
				+ " left outer join entity_schedule wses on wses.objectPk = uw.pk and wses.objectType = "
				+ EntityTypeEnum.UnitWorkstation.getValue()
				+ " and now() between wses.effectiveDateFrom and wses.effectiveDateTo "
				+ " left outer join TAB_UNIT_LOCATION ul on ul.unitPk = uth.unitPk and ul.projectPk = uth.projectPk and ul.workstationPk = uth.workstationPk and ul.current = 1 "
				+ " left outer join TAB_RESPONSE res on res.testProcPk = ut.pk and res.surveyPk = tfa.formFk and res.current = 1 "
				+ " left join (select iresp.responseId, count(tot.oilItemType) as transferCount from testitem_oil_transfer tot "
				+ " 		join tab_item_response iresp on tot.itemResponsePk = iresp.pk and tot.oilItemType = "
				+ EntityTypeEnum.OpenItem.getValue()
				+ " group by iresp.responseId) oilTransferT on res.responseId = oilTransferT.responseId "
				+ " left join (select iresp.responseId, count(tot.oilItemType) as transferCount from testitem_oil_transfer tot "
				+ " 		join tab_item_response iresp on tot.itemResponsePk = iresp.pk and tot.oilItemType = "
				+ EntityTypeEnum.NCR.getValue()
				+ " group by iresp.responseId) ncrTransferT on res.responseId = ncrTransferT.responseId ");
		

		if (request.getUnitOID() != null)
		{
			if (request.isIncludeChildrenUnits() == true)
			{
				if (request.getProjectOIDForUnitHeirarchy() == null)
				{
					throw new AppException(
							"Project for unit heirarchy should be specified to include chileren of the selected unit");
				}
				UnitInProjectObj upr = unitManager.getUnitInProject(request.getUnitOID(),
						request.getProjectOIDForUnitHeirarchy());
				sb.append(" join (select upr.unitPk from unit_project_ref upr "
						+ " join unit_project_ref_h uprh on uprh.unitInProjectPk = upr.pk and now() between uprh.effectiveDateFrom and uprh.effectiveDateTo and uprh.status != 'Removed' "
						+ " where ( (upr.unitPk = ? and upr.projectPk = ?) or (upr.projectPk = ? and uprh.rootParentPk = ? and uprh.heiCode like ?) ) )units on units.unitPk = uth.unitPk ");
				params.add(request.getUnitOID().getPk());
				params.add(request.getProjectOIDForUnitHeirarchy().getPk());
				params.add(request.getProjectOIDForUnitHeirarchy().getPk());
				params.add(upr.getRootParentPk());
				params.add(upr.getHeiCode() + ".%");
			} 
			else
			{
				sb.append(" join (select pk from tab_unit u where u.pk = ? )units on units.pk = uth.unitPk");
				params.add(request.getUnitOID().getPk());
			}
		}
		
		
		sb.append(" where  uth.workstationPk != " + dummyWorkstation.getPk() + "");
		
		
		if (request.getProjectOIDList() != null && request.getProjectOIDList().size() > 0)
		{
			sb.append(" and uth.projectPk in (");
			String sep = "";
			for (Iterator iterator = request.getProjectOIDList().iterator(); iterator.hasNext();)
			{
				ProjectOID aProjectOID = (ProjectOID) iterator.next();
				sb.append(sep).append("?");
				sep = ", ";
				params.add(aProjectOID.getPk());
			}
			sb.append(")");
		}
		if (request.getProjectNameSearchString() != null && request.getProjectNameSearchString().trim().length() > 0)
		{
			sb.append(" and upper(p.projectName) like ? ");
			params.add("%" + request.getProjectNameSearchString().toUpperCase() + "%");
		}
		if (request.getUnitNameSearchString() != null && request.getUnitNameSearchString().trim().length() > 0)
		{
			sb.append(" and upper(uh.unitName) like ? ");
			params.add("%" + request.getUnitNameSearchString().toUpperCase() + "%");
		}
		if (request.getUnitOIDList() != null && request.getUnitOIDList().size() > 0)
		{
			sb.append(" and u.pk in (");
			String sep = "";
			for (Iterator iterator = request.getUnitOIDList().iterator(); iterator.hasNext();)
			{
				UnitOID aUnitOID = (UnitOID) iterator.next();
				sb.append(sep).append("?");
				sep = ", ";
				params.add(aUnitOID.getPk());
			}
			sb.append(")");
		}
		if (request.getWorkstationOIDList() != null && request.getWorkstationOIDList().size() > 0)
		{
			sb.append(" and uth.workstationPk in (");

			String sep = "";
			for (Iterator iterator = request.getWorkstationOIDList().iterator(); iterator.hasNext();)
			{
				WorkstationOID aWs = (WorkstationOID) iterator.next();
				sb.append(sep).append("?");
				sep = ", ";
				params.add(aWs.getPk());
			}
			sb.append(") ");
		}
		if (request.getSiteOIDList() != null && request.getSiteOIDList().size() > 0)
		{
			sb.append(" and s.pk in (");

			String sep = "";
			for (Iterator iterator = request.getSiteOIDList().iterator(); iterator.hasNext();)
			{
				SiteOID aSite = (SiteOID) iterator.next();
				sb.append(sep).append("?");
				sep = ", ";
				params.add(aSite.getPk());
			}
			sb.append(") ");
		}
		if (request.getSubmittedDateFilter() != null)
		{
			Date[] dates = request.getSubmittedDateFilter().getResolvedDateRangeValues();
			if (dates[0] != null)
			{
				sb.append(" and res.responseCompleteTime >= ? ");
				params.add(dates[0]);
			}
			if (dates[1] != null)
			{
				sb.append(" and res.responseCompleteTime < ? ");
				params.add(dates[1]);
			}
		}
		if (request.getVerifiedDateFilter() != null)
		{
			Date[] dates = request.getVerifiedDateFilter().getResolvedDateRangeValues();
			if (dates[0] != null)
			{
				sb.append(" and res.verifiedDate >= ? ");
				params.add(dates[0]);
			}
			if (dates[1] != null)
			{
				sb.append(" and res.verifiedDate < ? ");
				params.add(dates[1]);
			}
		}
		if (request.getApprovedDateFilter() != null)
		{
			Date[] dates = request.getApprovedDateFilter().getResolvedDateRangeValues();
			if (dates[0] != null)
			{
				sb.append(" and res.approvedDate >= ? ");
				params.add(dates[0]);
			}
			if (dates[1] != null)
			{
				sb.append(" and res.approvedDate < ? ");
				params.add(dates[1]);
			}
		}

		if (request.getResponseStatus() != null && request.getResponseStatus().length > 0)
		{
			StringBuffer sb1 = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();

			String sep = "";
			for (int i = 0; i < request.getResponseStatus().length; i++)
			{
				if (request.getResponseStatus()[i] == FormStatusEnum.NOTSTARTED)
				{
					sb1.append("res.status is null");
				} else
				{
					sb2.append(sep).append("?");
					params.add(request.getResponseStatus()[i].getStatusString());
					sep = ",";
				}
			}
			if (sb2.length() > 0)
			{
				sb2.insert(0, " res.status in (");
				sb2.append(")");
			}

			if (sb1.length() > 0 && sb2.length() > 0)
			{
				sb1.append(" or ");
			}

			sb.append(" and (").append(sb1).append(sb2).append(")");
		}

		sb.append(" having 1 = 1  ");

		if (groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestStartDate))
		{
			sb.append(" and forecastStartDate is not null ");
		}
		if (groupingColList.contains(TestProcStatusSummaryReportRequest.GroupingCol.forecastTestEndDate))
		{
			sb.append(" and forecastEndDate is not null ");
		}
		if (request.getForecastStartDate() != null)
		{
			Date[] dates = request.getForecastStartDate().getResolvedDateRangeValues();
			if (dates[0] != null)
			{
				sb.append(" and convert_tz(forecastStartDate, ?, siteTimeZone)  >= convert_tz(?, ?, siteTimeZone ) ");
				params.add(dbtimezone);
				params.add(dates[0]);
				params.add(dbtimezone);
			}
			if (dates[1] != null)
			{
				sb.append(" and convert_tz(forecastStartDate, ?, siteTimeZone) < convert_tz(?, ?, siteTimeZone ) ");
				params.add(dbtimezone);
				params.add(dates[1]);
				params.add(dbtimezone);
			}

		}
		if (request.getForecastEndDate() != null)
		{
			Date[] dates = request.getForecastEndDate().getResolvedDateRangeValues();
			if (dates[0] != null)
			{
				sb.append(" and convert_tz(forecastEndDate, ?, siteTimeZone)  >= convert_tz(?, ?, siteTimeZone ) ");
				params.add(dbtimezone);
				params.add(dates[0]);
				params.add(dbtimezone);
			}
			if (dates[1] != null)
			{
				sb.append(" and convert_tz(forecastEndDate, ?, siteTimeZone) < convert_tz(?, ?, siteTimeZone ) ");
				params.add(dbtimezone);
				params.add(dates[1]);
				params.add(dbtimezone);
			}
		}
		sb.append(" and 1 = 1");

		return new QueryObject(sb.toString(), params);
	}
}
