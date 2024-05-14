package com.tathvatech.ncr.report;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.project.controller.ProjectController;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tathvatech.user.common.UserContext;
import java.util.Collections;
import java.util.*;


public class NcrCorrectiveActionReport
{
    private  final Logger logger = LoggerFactory.getLogger(NcrCorrectiveActionReport.class);
    private final PersistWrapper persistWrapper;
    UserContext context;
    private QueryObject query;
    private ReportRequest reportRequest;
    private NcrCorrectiveActionReportFilter ncrCorrectiveActionFilter;

    public NcrCorrectiveActionReport(PersistWrapper persistWrapper, UserContext context, ReportRequest reportRequest)
    {
        this.persistWrapper = persistWrapper;
        this.context = context;
        this.reportRequest = reportRequest;
        this.ncrCorrectiveActionFilter = (NcrCorrectiveActionReportFilter) reportRequest.getFilter();

    }

    public ReportResponse runReport()
    {
        ReportResponse response = new ReportResponse();
        response.setStartIndex(reportRequest.getStartIndex());

        QueryObject qb = getQuery();

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
        List<NcrCorrectiveActionQuery> rows = persistWrapper.readList(NcrCorrectiveActionQuery.class, sql.toString(),
                qb.getParams().toArray());

        response.setReportData(rows);

        return response;
    }

    private QueryObject getQuery()
    {
        StringBuffer sqlQuery = new StringBuffer("");
        QueryObject queryObject = getSql();
        if (queryObject == null)
            return null;
        sqlQuery.append(queryObject.getQuery());
        query = new QueryObject();
        query.setQuery(sqlQuery.toString());
        query.setParams(queryObject.getParams());
        return query;
    }

    private QueryObject getSql()
    {
        List<Object> params = new ArrayList<Object>();
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" select ");
        sqlQuery.append(" nca.pk as pk ");
        sqlQuery.append(",").append(" concat(ncrGroup.ncrGroupNo, '.',  ncr.ncrNo) as ncrNo ");
        sqlQuery.append(",").append(" d8Stages.name as d8StageName ");
        sqlQuery.append(",").append(" ncah.correctiveAction as recommendedActions ");
        sqlQuery.append(",").append(" nca.status as status ");
        sqlQuery.append(",").append(" ncah.custodianPk as custodianPk  ");
        sqlQuery.append(",").append(" concat (cust.firstName , ' ', cust.lastName) as custodianName ");
        sqlQuery.append(",").append(" ncah.startForecastDate as forecastStartDate ");

        sqlQuery.append(",").append("ncsaCompleteTab.actionDate as completedDate ");
        sqlQuery.append(",").append("ncsaCompleteTab.actionComment as completedComment");
        sqlQuery.append(",").append("concat(completedByTab.firstName,' ',completedByTab.lastName) as completedByName");

        sqlQuery.append(",").append(" ncah.completionForecastDate as forecastCompletionDate");
        sqlQuery.append(System.lineSeparator());
        sqlQuery.append(" from");
        sqlQuery.append(" ncr_corrective_action nca ");
        sqlQuery.append(
                " inner join ncr_corrective_action_h ncah on nca.pk=ncah.ncrCorrectiveActionPk and now() between ncah.effectiveFrom and ncah.effectiveTo ");
        sqlQuery.append(" inner join ncr on nca.ncrFk=ncr.pk and ncr.ncrStatus not in (?,?,?) ");
        params.add(NcrEnum.NcrItemStatus.DRAFT.toString());
        params.add(NcrEnum.NcrItemStatus.REJECTED.toString());
        params.add(NcrEnum.NcrItemStatus.CANCELLED.toString());
        sqlQuery.append(" join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk");
        sqlQuery.append(" left join site on ncrGroup.siteFk = site.pk ");
        sqlQuery.append(" left join d8Stages_master d8Stages on ncah.d8stageFk = d8Stages.pk ");
        sqlQuery.append(" join TAB_PROJECT project on ncrGroup.projectFk = project.pk ");
        sqlQuery.append(" left join part part on ncrGroup.partFk = part.pk ");
        sqlQuery.append(" left join part_revision partRevisionT on ncrGroup.revisionFk = partRevisionT.pk ");
        sqlQuery.append(" left join supplier supplier on ncrGroup.supplierFk = supplier.pk ");
        sqlQuery.append(
                " left join supplier_h supplierDetail on supplierDetail.supplierFk = supplier.pk  and now() between supplierDetail.effectiveFrom and supplierDetail.effectiveTo ");
        sqlQuery.append(
                " left join supplier_sitegroup_mapping ssgm on ssgm.siteGroupFk = site.siteGroupFk and ssgm.supplierFk = ncrGroup.supplierFk ");
        sqlQuery.append(" left join TAB_USER cust on ncah.custodianPk = cust.pk");
        sqlQuery.append(
                " left join TAB_WORKSTATION locWorkstation on locWorkstation.pk = ncr.locationPk and ncr.locationType = 'Workstation' ");
        sqlQuery.append(
                " left join injury_location_master locInjuryLocation on locInjuryLocation.pk = ncr.locationPk and ncr.locationType = 'LocationType' ");
        sqlQuery.append(
                " left join ncr_where_found locWhereFound on locWhereFound.pk = ncr.locationPk and ncr.locationType = 'WhereFoundType' ");
        // hazard Completed

        sqlQuery.append(
                " left join ncr_corrective_status_actions ncsaCompleteTab on ncsaCompleteTab.ncrCorrectiveActionPk=nca.pk "
                        + " and ncsaCompleteTab.isCurrent=1 and ncsaCompleteTab.actionName='"
                        + NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVECOMPLETED.toString() + "' ");
        sqlQuery.append(" left join TAB_USER completedByTab on completedByTab.pk = ncsaCompleteTab.actionBy");
        sqlQuery.append(System.lineSeparator());
        sqlQuery.append(" where 1=1 ");

        if (ncrCorrectiveActionFilter.getPk() != 0)
        {
            sqlQuery.append(" and nca.pk = ?");
            params.add(ncrCorrectiveActionFilter.getPk());
        } else
        {
            if (ncrCorrectiveActionFilter.getD8Stages() != null && ncrCorrectiveActionFilter.getD8Stages().size() > 0)
            {
                sqlQuery.append(" and ncah.d8StageFk in ( ");
                String ssep = "";
                for (Integer dValue : ncrCorrectiveActionFilter.getD8Stages())
                {
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(dValue);
                }
                sqlQuery.append(") ");
            }

            if (ncrCorrectiveActionFilter.getNcrNo() != null
                    && ncrCorrectiveActionFilter.getNcrNo().trim().length() > 0)
            {
                sqlQuery.append(" and concat(lower(ncrGroup.ncrGroupNo),'.',lower(ncr.ncrNo)) like lower(?)");
                params.add("%" + ncrCorrectiveActionFilter.getNcrNo().trim() + "%");
            }
            if (ncrCorrectiveActionFilter.getRecommendedActions() != null
                    && ncrCorrectiveActionFilter.getRecommendedActions().trim().length() > 0)
            {
                sqlQuery.append(" and lower(ncah.correctiveAction) like lower(?)");
                params.add("%" + ncrCorrectiveActionFilter.getRecommendedActions().trim() + "%");
            }
            if (ncrCorrectiveActionFilter.getProjectOIDs() != null
                    && ncrCorrectiveActionFilter.getProjectOIDs().size() > 0)
            {
                sqlQuery.append(" and ncrGroup.projectFk in (");

                String ssep = "";
                for (Iterator iterator = ncrCorrectiveActionFilter.getProjectOIDs().iterator(); iterator.hasNext();)
                {
                    ProjectOID aProject = (ProjectOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aProject.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrCorrectiveActionFilter.getSupplierOIDs() != null
                    && ncrCorrectiveActionFilter.getSupplierOIDs().size() > 0)
            {
                sqlQuery.append(" and supplier.pk in (");

                String ssep = "";
                for (Iterator iterator = ncrCorrectiveActionFilter.getSupplierOIDs().iterator(); iterator.hasNext();)
                {
                    SupplierOID aOID = (SupplierOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrCorrectiveActionFilter.getPartOIDs() != null && ncrCorrectiveActionFilter.getPartOIDs().size() > 0)
            {
                sqlQuery.append(" and ncrGroup.partFk in (");

                String ssep = "";
                for (Iterator iterator = ncrCorrectiveActionFilter.getPartOIDs().iterator(); iterator.hasNext();)
                {
                    PartOID aOID = (PartOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ", ";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrCorrectiveActionFilter.getCustodianOIDs() != null
                    && ncrCorrectiveActionFilter.getCustodianOIDs().size() != 0)
            {
                sqlQuery.append(" and ncah.custodianPk in (");

                String ssep = "";
                for (Iterator iterator = ncrCorrectiveActionFilter.getCustodianOIDs().iterator(); iterator.hasNext();)
                {
                    UserOID aOID = (UserOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrCorrectiveActionFilter.getSiteOIDs() != null && ncrCorrectiveActionFilter.getSiteOIDs().size() != 0)
            {
                sqlQuery.append(" and ncrGroup.siteFk in (");

                String ssep = "";
                for (Iterator iterator = ncrCorrectiveActionFilter.getSiteOIDs().iterator(); iterator.hasNext();)
                {
                    SiteOID aSite = (SiteOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aSite.getPk());
                }
                sqlQuery.append(") ");
            }

            if (ncrCorrectiveActionFilter.getForecastStartDate() != null)
            {
                Date[] dateRange = ncrCorrectiveActionFilter.getForecastStartDate().getResolvedDateRangeValues();
                if (dateRange != null)
                {
                    Date createdDateFrom = dateRange[0];
                    Date createdDateTo = dateRange[1];
                    if (createdDateFrom != null)
                    {
                        sqlQuery.append(" and ncah.startForecastDate > ? ");
                        params.add(createdDateFrom);
                    }
                    if (createdDateTo != null)
                    {
                        sqlQuery.append(" and ncah.startForecastDate  < ?");
                        params.add(createdDateTo);
                    }
                }
            }
            if (ncrCorrectiveActionFilter.getForecastCompletionDate() != null)
            {
                Date[] dateRange = ncrCorrectiveActionFilter.getForecastCompletionDate().getResolvedDateRangeValues();
                if (dateRange != null)
                {
                    Date createdDateFrom = dateRange[0];
                    Date createdDateTo = dateRange[1];
                    if (createdDateFrom != null)
                    {
                        sqlQuery.append(" and ncah.completionForecastDate > ? ");
                        params.add(createdDateFrom);
                    }
                    if (createdDateTo != null)
                    {
                        sqlQuery.append(" and ncah.completionForecastDate  < ?");
                        params.add(createdDateTo);
                    }
                }
            }
            if (ncrCorrectiveActionFilter.getActionCompletionWaitingFor() != null
                    && ncrCorrectiveActionFilter.getActionCompletionWaitingFor().size() > 0)
            {
                Integer[] actionWaitingFor = new Integer[ncrCorrectiveActionFilter.getActionCompletionWaitingFor()
                        .size()];
                int i = 0;
                for (UserOID userOID : ncrCorrectiveActionFilter.getActionCompletionWaitingFor())
                {
                    actionWaitingFor[i++] = Math.toIntExact(userOID.getPk());
                }
                String custodians = Arrays.deepToString(actionWaitingFor);
                custodians = custodians.replace('[', '(');
                custodians = custodians.replace(']', ')');
                sqlQuery.append(" and ncah.custodianPk in ").append(custodians);
                sqlQuery.append(" and ( nca.status is null or nca.status =? )");
                params.add(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString());
            }
            if (ncrCorrectiveActionFilter.getActiveActionsCompletionWaitingFor() != null
                    && ncrCorrectiveActionFilter.getActiveActionsCompletionWaitingFor().size() > 0)
            {
                Integer[] actionWaitingFor = new Integer[ncrCorrectiveActionFilter
                        .getActiveActionsCompletionWaitingFor().size()];
                int i = 0;
                for (UserOID userOID : ncrCorrectiveActionFilter.getActiveActionsCompletionWaitingFor())
                {
                    actionWaitingFor[i++] = Math.toIntExact(userOID.getPk());
                }
                String custodians = Arrays.deepToString(actionWaitingFor);
                custodians = custodians.replace('[', '(');
                custodians = custodians.replace(']', ')');
                sqlQuery.append(" and ncah.custodianPk in ").append(custodians);
                sqlQuery.append(" and ( nca.status is null or nca.status =? )");
                sqlQuery.append(" and  ?>=ncah.startForecastDate ");
                params.add(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString());
                DateUtils dateUtil = new DateUtils(context.getTimeZone());
                params.add(dateUtil.getBeginningDayNew(new Date()));
            }
            if (ncrCorrectiveActionFilter.getOverduedActionsCompletionWaitingFor() != null
                    && ncrCorrectiveActionFilter.getOverduedActionsCompletionWaitingFor().size() > 0)
            {
                Integer[] actionWaitingFor = new Integer[ncrCorrectiveActionFilter
                        .getOverduedActionsCompletionWaitingFor().size()];
                int i = 0;
                for (UserOID userOID : ncrCorrectiveActionFilter.getOverduedActionsCompletionWaitingFor())
                {
                    actionWaitingFor[i++] = Math.toIntExact(userOID.getPk());
                }
                String custodians = Arrays.deepToString(actionWaitingFor);
                custodians = custodians.replace('[', '(');
                custodians = custodians.replace(']', ')');
                sqlQuery.append(" and ncah.custodianPk in ").append(custodians);
                sqlQuery.append(" and ( nca.status is null or nca.status =? )");
                sqlQuery.append(" and  ? >ncah.completionForecastDate ");
                params.add(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.toString());
                DateUtils dateUtil = new DateUtils(context.getTimeZone());
                params.add(dateUtil.getBeginningDayNew(new Date()));
            }
            if (ncrCorrectiveActionFilter.getStatus() != null && ncrCorrectiveActionFilter.getStatus().size() > 0)
            {
                sqlQuery.append(" and (");
                if (ncrCorrectiveActionFilter.getStatus().contains(NcrEnum.NcrCorrectiveStatusEnum.OPEN))
                {
                    sqlQuery.append(" nca.status is null or nca.status=? or ");
                    params.add(NcrEnum.NcrCorrectiveStatusEnum.CORRECTIVEREOPEN.name());
                }
                sqlQuery.append(" nca.status in (");
                String comma = "";
                for (NcrEnum.NcrCorrectiveStatusEnum status : ncrCorrectiveActionFilter.getStatus())
                {
                    sqlQuery.append(comma).append("'").append(status.name()).append("'");
                    comma = ",";
                }
                sqlQuery.append(" )) ");
            }

        }
        sqlQuery.append(" ORDER BY nca.lastUpdated DESC");

        QueryObject conditionQuery = new QueryObject();
        conditionQuery.setQuery(sqlQuery.toString());
        conditionQuery.setParams(Collections.singletonList(params));
        return conditionQuery;
    }

}

