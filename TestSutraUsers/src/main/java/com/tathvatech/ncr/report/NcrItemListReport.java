package com.tathvatech.ncr.report;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.ncr.common.InjuryLocationMasterQuery;
import com.tathvatech.ncr.common.NcrDispositionBean;
import com.tathvatech.ncr.common.NcrWhereFoundBean;
import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.workstation.common.WorkstationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class NcrItemListReport
{
    private  final Logger logger = LoggerFactory.getLogger(NcrItemListReport.class);
    private  PersistWrapper persistWrapper ;
    UserContext context;
    private QueryObject query;
    private ReportRequest reportRequest;
    private final NcrItemListReportFilter ncrItemReportFilter;

    private Date createdDateFrom;
    private Date createdDateTo;
    private Date publishedDateFrom;
    private Date publishedDateTo;
    private Date approvedDateFrom;
    private Date approvedDateTo;
    private Date closedDateFrom;
    private Date closedDateTo;

    public NcrItemListReport(UserContext context, ReportRequest reportRequest)
    {

        this.context = context;
        this.reportRequest = reportRequest;

        this.ncrItemReportFilter = (NcrItemListReportFilter) reportRequest.getFilter();

        if (ncrItemReportFilter.getCreatedDate() != null)
        {
            Date[] createdDateRange = ncrItemReportFilter.getCreatedDate().getResolvedDateRangeValues();
            if (createdDateRange != null)
            {
                createdDateFrom = createdDateRange[0];

                createdDateTo = createdDateRange[1];
            }
        }
        if (ncrItemReportFilter.getPublishedDate() != null)
        {
            Date[] dateRange = ncrItemReportFilter.getPublishedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                publishedDateFrom = dateRange[0];

                publishedDateTo = dateRange[1];
            }
        }

        if (ncrItemReportFilter.getApprovedDate() != null)
        {
            Date[] dateRange = ncrItemReportFilter.getApprovedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                approvedDateFrom = dateRange[0];

                approvedDateTo = dateRange[1];
            }
        }

        if (ncrItemReportFilter.getClosedDate() != null)
        {
            Date[] dateRange = ncrItemReportFilter.getClosedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                closedDateFrom = dateRange[0];

                closedDateTo = dateRange[1];
            }
        }
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
        List<NcrItemListReportResultRow> rows = persistWrapper.readList(NcrItemListReportResultRow.class,
                sql.toString(), qb.getParams().toArray());

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
        sqlQuery.append(" ncr.pk as pk ,");
        sqlQuery.append(" concat(ncrGroup.ncrGroupNo, '.',  ncr.ncrNo) as ncrNo, ");
        sqlQuery.append(" ncr.ncrDesc as ncrDescription ,");
        sqlQuery.append(" ncr.ncrStatus as ncrStatus, ");
        sqlQuery.append(" ncr.custodianPk as custodianPk , ");
        sqlQuery.append(" concat (cust.firstName , ' ', cust.lastName) as custodianName, ");
        sqlQuery.append(" ncr.createdBy as createdBy, ");
        sqlQuery.append(" ncr.createdDate as createdDate, ");
        sqlQuery.append(" concat (createdByT.firstName , ' ', createdByT.lastName) as createdByName,  ");
        sqlQuery.append(" ncr.cancelledBy as cancelledBy, ");
        sqlQuery.append(" ncr.cancelledDate as cancelledDate,");
        sqlQuery.append(" concat (cancelledByT.firstName ,' ', cancelledByT.lastName) as cancelledByName,  ");
        sqlQuery.append(" ncr.publishedBy as publishedBy, ");
        sqlQuery.append(" concat (publishedByT.firstName ,' ', publishedByT.lastName) as publishedByName,  ");
        sqlQuery.append(" ncr.publishedDate as publishedDate,  ");
        sqlQuery.append(" ncr.publishedComment as publishedComment, ");
        sqlQuery.append(" ncr.verifiedBy as completedBy, ");
        sqlQuery.append(" ncr.verifiedDate as completedDate,  ");
        sqlQuery.append(" concat (completedByT.firstName , ' ', completedByT.lastName) as completedByName,  ");
        sqlQuery.append(" ncr.verifiedComment as completedComment, ");
        sqlQuery.append(" ncr.closedBy as closedBy, ");
        sqlQuery.append(" concat (closedByT.firstName , ' ', closedByT.lastName) as closedByName,  ");
        sqlQuery.append(" ncr.closedDate as closedDate,  ");
        sqlQuery.append(" ncr.closedComment as closedComment, ");
        sqlQuery.append(" ncrLastRejection.approvedBy as lastRejectedBy, ");
        sqlQuery.append(" ncrLastRejection.approvedDate as lastRejectedDate, ");
        sqlQuery.append(
                " concat(ncrLastRejectionUserT.firstName, ' ', ncrLastRejectionUserT.lastName)  as lastRejectedByName, ");
        sqlQuery.append(" ncrLastApproval.approvedBy as lastApprovedBy, ");
        sqlQuery.append(" ncrLastApproval.approvedDate as lastApprovedDate, ");
        sqlQuery.append(
                " concat(ncrLastApprovalUserT.firstName, ' ', ncrLastApprovalUserT.lastName)  as lastApprovedByName, ");
        sqlQuery.append(" ncr.quantity as quantity,  ");
        sqlQuery.append(" ncr.unitOfMeasureFk as unitOfMeasureFk,  ");
        sqlQuery.append(" quantityUnit.name as unitOfMeasureName,  ");
        sqlQuery.append(" ncr.areaOfResponsibilityFk as areaOfResponsibilityFk,");
        sqlQuery.append(" aor.name as areaOfResponsibilityName,  ");
        sqlQuery.append(" ncr.dispositionFk as dispositionFk, ");
        sqlQuery.append(" dispositionT.name as disposition, ");
        sqlQuery.append(" ncr.dispositionComment as dispositionComment, ");
        // if subcat.parent is null, that means the selected value is a cat.
        sqlQuery.append(" case when subcat.parentFk is null then subcat.pk else cat.pk end as categoryFk, ");
        sqlQuery.append(" case when subcat.parentFk is null then subcat.name else cat.name end as category, ");
        // if subcat.parent is null, that means the selected value is a cat.
        sqlQuery.append(" case when subcat.parentFk is null then null else subcat.pk end as subCategoryFk, ");
        sqlQuery.append(" case when subcat.parentFk is null then null else subcat.name end as subCategory, ");
        sqlQuery.append(" ncr.mrfNo as mrfNo, ");
        sqlQuery.append(" ncr.forecastStartDate as forecastStartDate,  ");
        sqlQuery.append(" ncr.forecastCompletionDate as forecastCompletionDate,  ");
        sqlQuery.append(" ncr.carType as carType,  ");
        sqlQuery.append(" ncr.hours as estimatedHours,  ");
        sqlQuery.append(" ncr.priority as priority,  ");
        sqlQuery.append(" ncr.severity as severity,  ");
        sqlQuery.append(" ncr.occurrence as occurrence,  ");
        sqlQuery.append(" ncr.detection as detection,  ");
        sqlQuery.append(" ncr.escape as escape,  ");
        sqlQuery.append(" ncr.reworkOrderFk as reworkOrderFk,  ");
        sqlQuery.append(" rwo.reworkOrderNumber as reworkOrderNo,  ");
        sqlQuery.append(" ncr.rootCause as rootCause,  ");
        sqlQuery.append(" ncr.workInstructionOrComment as workInstructionOrComment,  ");
        sqlQuery.append(" ncr.ppsor8d as ppsor8d,  ");
        sqlQuery.append(" ncr.sourcePk as sourceFk,  ");
        sqlQuery.append(" ncr.sourceType as sourceType,  ");
        sqlQuery.append(
                " case when sourceType = 'Workstation' then srcWorkstation.workstationName when sourceType = 'LocationType' then srcInjuryLocation.name when sourceType = 'WhereFoundType' then srcWhereFound.name end as source , ");
        sqlQuery.append(" ncr.locationPk as whereFoundFk,  ");
        sqlQuery.append(" ncr.locationType as whereFoundType,  ");
        sqlQuery.append(
                " case when locationType = 'Workstation' then locWorkstation.workstationName when locationType = 'LocationType' then locInjuryLocation.name  when locationType = 'WhereFoundType' then locWhereFound.name end as whereFound,");
        sqlQuery.append(
                " case when (ncrLastApproval.approvedDate is not null and ncr.publishedDate is not null) then datediff(ncrLastApproval.approvedDate , ncr.publishedDate) when ncrLastApproval.approvedDate is  null then datediff( now() , ncr.publishedDate) end as noOfDaysOpen,  ");
        sqlQuery.append(" ncr.ncrGroupFk as ncrGroupFk, ");
        sqlQuery.append(" ncrGroup.status as ncrGroupStatus , ");
        sqlQuery.append(" ncrGroup.groupDescription as ncrGroupDescription , ");
        sqlQuery.append(" site.pk as siteFk, ");
        sqlQuery.append(" site.name as siteName, ");
        sqlQuery.append(" site.description as siteDescription, ");
        sqlQuery.append(" project.pk as projectFk, ");
        sqlQuery.append(" project.projectName as projectName, ");
        sqlQuery.append(" project.projectDescription as projectDescription, ");
        sqlQuery.append(" part.pk as partFk, ");
        sqlQuery.append(" part.partNo as partNo, ");
        sqlQuery.append(" part.name as partName, ");
        sqlQuery.append(" part.description as partDescription, ");
        sqlQuery.append(" ncrGroup.revisionFk as partRevisionFk, ");
        sqlQuery.append(" partRevisionT.revision as partRevision, ");
        sqlQuery.append(" supplier.pk as supplierFk, ");
        sqlQuery.append(" ssgm.supplierId as supplierId, ");
        sqlQuery.append(" supplierDetail.supplierName as supplierName ");
        sqlQuery.append(System.lineSeparator());

        sqlQuery.append(" from");
        sqlQuery.append(" ncr ");
        sqlQuery.append(" join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk");
        sqlQuery.append(" left join site on ncrGroup.siteFk = site.pk ");
        sqlQuery.append(" join TAB_PROJECT project on ncrGroup.projectFk = project.pk ");
        sqlQuery.append(" left join TAB_USER createdByT on ncr.createdBy = createdByT.pk ");
        sqlQuery.append(" left join TAB_USER publishedByT on ncr.publishedBy = publishedByT.pk");
        sqlQuery.append(" left join TAB_USER completedByT on ncr.verifiedBy = completedByT.pk ");
        sqlQuery.append(" left join TAB_USER closedByT on ncr.closedBy = closedByT.pk");
        sqlQuery.append(" left join TAB_USER cancelledByT on ncr.cancelledBy = cancelledByT.pk");
        sqlQuery.append(" left join part part on ncrGroup.partFk = part.pk ");
        sqlQuery.append(" left join part_revision partRevisionT on ncrGroup.revisionFk = partRevisionT.pk ");
        sqlQuery.append(" left join supplier supplier on ncrGroup.supplierFk = supplier.pk ");
        sqlQuery.append(
                " left join supplier_h supplierDetail on supplierDetail.supplierFk = supplier.pk  and now() between supplierDetail.effectiveFrom and supplierDetail.effectiveTo ");
        sqlQuery.append(
                " left join supplier_sitegroup_mapping ssgm on ssgm.siteGroupFk = site.siteGroupFk and ssgm.supplierFk = ncrGroup.supplierFk ");
        sqlQuery.append(" left join ncr_disposition_master dispositionT on ncr.dispositionFk = dispositionT.pk");
        sqlQuery.append(" left join rework_order_entry rwo on ncr.reworkOrderFk = rwo.pk ");
        sqlQuery.append(" left join ncr_failure_code_master subcat on ncr.ncrFailureCodeFk = subcat.pk ");
        sqlQuery.append(" left join ncr_failure_code_master cat on subcat.parentFk = cat.pk ");
        sqlQuery.append(" left join unit_of_measure quantityUnit on ncr.unitOfMeasureFk = quantityUnit.pk");
        sqlQuery.append(" left join ncr_area_of_responsibility_master aor on ncr.areaOfResponsibilityFk = aor.pk ");
        sqlQuery.append(" left join ncr_area_of_responsibility_master aorParent on aorParent.pk = aor.parentFk ");
        sqlQuery.append(" left join TAB_USER cust on ncr.custodianPk = cust.pk");
        sqlQuery.append(
                " left join TAB_WORKSTATION locWorkstation on locWorkstation.pk = ncr.locationPk and ncr.locationType = 'Workstation' ");
        sqlQuery.append(
                " left join injury_location_master locInjuryLocation on locInjuryLocation.pk = ncr.locationPk and ncr.locationType = 'LocationType' ");
        sqlQuery.append(
                " left join ncr_where_found locWhereFound on locWhereFound.pk = ncr.locationPk and ncr.locationType = 'WhereFoundType' ");
        sqlQuery.append(
                " left join TAB_WORKSTATION srcWorkstation on srcWorkstation.pk = ncr.sourcePk  and ncr.sourceType = 'Workstation' ");
        sqlQuery.append(
                " left join injury_location_master srcInjuryLocation on srcInjuryLocation.pk = ncr.sourcePk and ncr.sourceType = 'LocationType' ");
        sqlQuery.append(
                " left join ncr_where_found srcWhereFound on srcWhereFound.pk = ncr.sourcePk  and ncr.sourceType = 'WhereFoundType' ");
        sqlQuery.append(
                " left join ncr_function ncrLastRejection on  ncrLastRejection.pk=( SELECT ncr_function.pk from ncr_function inner join ncr_function_master on ncr_function_master.pk=ncr_function.functionMasterFk  where  ncr_function.objectFk=ncr.pk and ncr_function.objectType ='NCR' and approvedStatus ='REJECTED' and ncr_function_master.special=0 order by approvedDate desc Limit 1)");
        sqlQuery.append(
                " left join TAB_USER ncrLastRejectionUserT on ncrLastRejection.approvedBy = ncrLastRejectionUserT.pk ");
        sqlQuery.append(
                " left join ncr_function ncrLastApproval on ncrLastApproval.pk=(SELECT ncr_function.pk from ncr_function inner join ncr_function_master on ncr_function_master.pk=ncr_function.functionMasterFk where ncr_function.objectFk=ncr.pk and ncr_function.objectType ='NCR' and approvedStatus ='APPROVED' and ncr_function_master.special=0 order by approvedDate desc Limit 1 )");
        sqlQuery.append(
                " left join TAB_USER ncrLastApprovalUserT on ncrLastApproval.approvedBy = ncrLastApprovalUserT.pk ");
        if (ncrItemReportFilter.getUnitOIDs() != null)
        {
            sqlQuery.append(" left join ncr_unit_assign on ncr_unit_assign.ncrFk=ncr.pk ");
        }

        if (ncrItemReportFilter.getFunctionApprovalPendingFor() != null
                && ncrItemReportFilter.getFunctionApprovalPendingFor().size() > 0)
        {
            QueryObject qb = getNcrsWithFunctionApproval(ncrItemReportFilter);
            sqlQuery.append(System.lineSeparator());

            sqlQuery.append("join (").append(qb.getQuery()).append(")functionData on functionData.ncrFk = ncr.pk");
            params.addAll(qb.getParams());

        }

        if (ncrItemReportFilter.getSpecialProcessPendingFor() != null
                && ncrItemReportFilter.getSpecialProcessPendingFor().size() > 0)
        {
            QueryObject qb = getNcrsWithSpecialProcessFunction(ncrItemReportFilter);
            sqlQuery.append(System.lineSeparator());

            sqlQuery.append("join (").append(qb.getQuery()).append(")specialProcessData on specialProcessData.ncrFk = ncr.pk");
            params.addAll(qb.getParams());

        }

        sqlQuery.append(System.lineSeparator());
        sqlQuery.append(" where 1=1 ");

        sqlQuery.append(" and ncr.eStatus = ?");
        params.add(EStatusEnum.Active.getValue());

        if (ncrItemReportFilter.getPk() != 0)
        {
            sqlQuery.append(" and ncr.pk = ?");
            params.add(ncrItemReportFilter.getPk());
        } else
        {
            if (ncrItemReportFilter.getNcrGroupFk() != null)
            {
                sqlQuery.append(" and ncr.ncrGroupFk = ?");
                params.add(ncrItemReportFilter.getNcrGroupFk());
            }
            if (ncrItemReportFilter.getNcrNo() != null && ncrItemReportFilter.getNcrNo().trim().length() > 0)
            {
                sqlQuery.append(" and lower(ncrGroupNo) like lower(?)");
                params.add("%" + ncrItemReportFilter.getNcrNo().trim() + "%");
            }
            if (ncrItemReportFilter.getNcrGroupDesc() != null
                    && ncrItemReportFilter.getNcrGroupDesc().trim().length() > 0)
            {
                sqlQuery.append(" and lower(ncrGroup.groupDescription) like lower(?)");
                params.add("%" + ncrItemReportFilter.getNcrGroupDesc().trim() + "%");
            }
            if (ncrItemReportFilter.getNcrGroupStatusList() != null
                    && ncrItemReportFilter.getNcrGroupStatusList().size() > 0)
            {
                sqlQuery.append(" and ncrGroup.status in (");
                String comma = "";
                for (NcrEnum.NcrGroupStatus status : ncrItemReportFilter.getNcrGroupStatusList())
                {
                    sqlQuery.append(comma).append("'").append(status.name()).append("'");
                    comma = ",";
                }
                sqlQuery.append(" ) ");
            }

            if (createdDateFrom != null)
            {
                sqlQuery.append(" and ncrGroup.createdDate > ? ");
                params.add(createdDateFrom);
            }
            if (createdDateTo != null)
            {
                sqlQuery.append(" and ncrGroup.createdDate  < ?");
                params.add(createdDateTo);
            }

            if (ncrItemReportFilter.getSiteOIDs() != null && ncrItemReportFilter.getSiteOIDs().size() != 0)
            {
                sqlQuery.append(" and ncrGroup.siteFk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getSiteOIDs().iterator(); iterator.hasNext();)
                {
                    SiteOID aSite = (SiteOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aSite.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getUnitOIDs() != null && ncrItemReportFilter.getUnitOIDs().size() > 0)
            {
                sqlQuery.append(" and ncr_unit_assign.unitFk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getUnitOIDs().iterator(); iterator.hasNext();)
                {
                    UnitOID aUnit = (UnitOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aUnit.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getProjectOIDs() != null && ncrItemReportFilter.getProjectOIDs().size() > 0)
            {
                sqlQuery.append(" and ncrGroup.projectFk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getProjectOIDs().iterator(); iterator.hasNext();)
                {
                    ProjectOID aProject = (ProjectOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aProject.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getSupplierOIDs() != null && ncrItemReportFilter.getSupplierOIDs().size() > 0)
            {
                sqlQuery.append(" and supplier.pk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getSupplierOIDs().iterator(); iterator.hasNext();)
                {
                    SupplierOID aOID = (SupplierOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getPartOIDs() != null && ncrItemReportFilter.getPartOIDs().size() > 0)
            {
                sqlQuery.append(" and ncrGroup.partFk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getPartOIDs().iterator(); iterator.hasNext();)
                {
                    PartOID aOID = (PartOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ", ";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getCustodianOIDs() != null && ncrItemReportFilter.getCustodianOIDs().size() != 0)
            {
                sqlQuery.append(" and ncr.custodianPk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getCustodianOIDs().iterator(); iterator.hasNext();)
                {
                    UserOID aOID = (UserOID) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getDispositionIsSet() != null)
            {
                if (NcrItemListReportFilter.DispositionIsSet.Set == ncrItemReportFilter.getDispositionIsSet())
                {
                    sqlQuery.append(" and not(ncr.dispositionFk is null or ncr.dispositionFk = 0) ");
                } else if (NcrItemListReportFilter.DispositionIsSet.NotSet == ncrItemReportFilter.getDispositionIsSet())
                {
                    sqlQuery.append(" and (ncr.dispositionFk is null or ncr.dispositionFk = 0) ");
                }
            }
            if (ncrItemReportFilter.getDispositions() != null && ncrItemReportFilter.getDispositions().size() != 0)
            {
                sqlQuery.append(" and ncr.dispositionFk in (");

                String ssep = "";
                for (Iterator iterator = ncrItemReportFilter.getDispositions().iterator(); iterator.hasNext();)
                {
                    NcrDispositionBean aOID = (NcrDispositionBean) iterator.next();
                    sqlQuery.append(ssep).append("?");
                    ssep = ",";
                    params.add(aOID.getPk());
                }
                sqlQuery.append(") ");
            }
            if (ncrItemReportFilter.getNcrAreaOfResponsibilityBean() != null)
            {
                if (ncrItemReportFilter.getNcrAreaOfResponsibilityBean().getParentFk() == null) // select
                // aor
                // is
                // the
                // selected
                // parent
                // or
                // its
                // children
                {
                    sqlQuery.append(" and (aor.pk = ? or aorParent.pk = ? ) ");
                    params.add(ncrItemReportFilter.getNcrAreaOfResponsibilityBean().getPk());
                    params.add(ncrItemReportFilter.getNcrAreaOfResponsibilityBean().getPk());
                } else
                {
                    sqlQuery.append(" and aor.pk = ? ");
                    params.add(ncrItemReportFilter.getNcrAreaOfResponsibilityBean().getPk());
                }
            }
            if (ncrItemReportFilter.getNcrFailureCodeMasterBean() != null)
            {
                sqlQuery.append(" and ncr.ncrFailureCodeFk = ? ");
                params.add(ncrItemReportFilter.getNcrFailureCodeMasterBean().getPk());
            }
            if (ncrItemReportFilter.getCreatedByOID() != null)
            {
                sqlQuery.append(" and ncr.createdBy = ? ");
                params.add(ncrItemReportFilter.getCreatedByOID().getPk());
            }
            if (ncrItemReportFilter.getPublishedByOID() != null)
            {
                sqlQuery.append(" and ncr.publishedBy = ? ");
                params.add(ncrItemReportFilter.getPublishedByOID().getPk());
            }
            if (ncrItemReportFilter.getNcrItemStatusList() != null
                    && ncrItemReportFilter.getNcrItemStatusList().size() > 0)
            {
                sqlQuery.append(" and ncr.ncrStatus in (");
                String comma = "";
                for (NcrEnum.NcrItemStatus status : ncrItemReportFilter.getNcrItemStatusList())
                {
                    sqlQuery.append(comma).append("?");
                    comma = ", ";
                    params.add(status.name());
                }
                sqlQuery.append(" ) ");
            }
            if (ncrItemReportFilter.getLocation() != null)
            {
                sqlQuery.append(" and ncr.locationType = ? and ncr.locationPk=?");

                if (ncrItemReportFilter.getLocation() instanceof NcrWhereFoundBean)
                {
                    NcrWhereFoundBean ncrWhereFoundBean = (NcrWhereFoundBean) ncrItemReportFilter.getLocation();
                    params.add(EntityTypeEnum.WhereFoundType.name());
                    params.add(ncrWhereFoundBean.getPk());
                } else if (ncrItemReportFilter.getLocation() instanceof WorkstationQuery)
                {
                    WorkstationQuery workstationQuery = (WorkstationQuery) ncrItemReportFilter.getLocation();
                    params.add(EntityTypeEnum.Workstation.name());
                    params.add(workstationQuery.getPk());
                } else if (ncrItemReportFilter.getLocation() instanceof InjuryLocationMasterQuery)
                {
                    InjuryLocationMasterQuery injuryLocationMasterQuery = (InjuryLocationMasterQuery) ncrItemReportFilter
                            .getLocation();
                    params.add(EntityTypeEnum.LocationType.name());
                    params.add(injuryLocationMasterQuery.getPk());
                }

            }
            if (ncrItemReportFilter.getSource() != null)
            {
                sqlQuery.append(" and ncr.sourceType = ? and ncr.sourcePk=?");
                if (ncrItemReportFilter.getSource() instanceof NcrWhereFoundBean)
                {
                    NcrWhereFoundBean ncrWhereFoundBean = (NcrWhereFoundBean) ncrItemReportFilter.getSource();
                    params.add(EntityTypeEnum.WhereFoundType.name());
                    params.add(ncrWhereFoundBean.getPk());
                } else if (ncrItemReportFilter.getSource() instanceof WorkstationQuery)
                {
                    WorkstationQuery workstationQuery = (WorkstationQuery) ncrItemReportFilter.getSource();
                    params.add(EntityTypeEnum.Workstation.name());
                    params.add(workstationQuery.getPk());
                } else if (ncrItemReportFilter.getSource() instanceof InjuryLocationMasterQuery)
                {
                    InjuryLocationMasterQuery injuryLocationMasterQuery = (InjuryLocationMasterQuery) ncrItemReportFilter
                            .getSource();
                    params.add(EntityTypeEnum.LocationType.name());
                    params.add(injuryLocationMasterQuery.getPk());
                }
            }
            if (ncrItemReportFilter.getReworkorderOID() != null)
            {
                sqlQuery.append(" and ncr.reworkOrderFk = ?");
                params.add(ncrItemReportFilter.getReworkorderOID().getPk());
            }

            if (ncrItemReportFilter.getPps8DRequired() != null && ncrItemReportFilter.getPps8DRequired())
            {
                sqlQuery.append(" and ncr.ppsor8d =1 ");
            } else if (ncrItemReportFilter.getPps8DRequired() != null && !ncrItemReportFilter.getPps8DRequired())
            {
                sqlQuery.append(" and ncr.ppsor8d is NULL ");
            }

        }
        sqlQuery.append(" ORDER BY ncr.createdDate DESC");

        QueryObject conditionQuery = new QueryObject();
        conditionQuery.setQuery(sqlQuery.toString());
        conditionQuery.setParams(params);
        return conditionQuery;
    }

    private QueryObject getNcrsWithFunctionApproval(NcrItemListReportFilter filter)
    {
        QueryObject qb = getFunctionApprovalSubQuery(filter);

        StringBuilder sb = new StringBuilder("select distinct functionNcrs.ncrFk from (");
        sb.append(qb.getQuery());
        sb.append(") functionNcrs ");

        return new QueryObject(sb.toString(), qb.getParams());
    }

    private QueryObject getFunctionApprovalSubQuery(NcrItemListReportFilter filter)
    {
        // create a user selection string like (222, 234, 453 )
        List<UserOID> userOIDList = filter.getFunctionApprovalPendingFor();
        List<Object> userParams = new ArrayList<>();
        StringBuffer userListSb = new StringBuffer("(");
        String usep = "";
        for (Iterator iterator = userOIDList.iterator(); iterator.hasNext();)
        {
            UserOID userOID = (UserOID) iterator.next();
            userListSb.append(usep).append("?");
            userParams.add(userOID.getPk());
            usep = ", ";
        }
        userListSb.append(")");

        // now start the real sql
        List<Object> params = new ArrayList<>();
        StringBuffer sql = new StringBuffer(""
                + " select distinct functionRecs.ncrFunctionFk, functionRecs.ncrFk, sum(selectedUsersCountT) as selectedUsersCount, sum(otherUserCountT) as otherUserCount "
                + " from " + " ( "
                + " select functionT.pk as ncrFunctionFk, functionT.objectFk as ncrFk, notifierT.userFk, ");
        sql.append(" case when notifierT.userFk in ").append(userListSb.toString())
                .append(" then 1 else 0 end as selectedUsersCountT, ");
        params.addAll(userParams);
        sql.append(" case when notifierT.userFk not in ").append(userListSb.toString())
                .append(" then 1 else 0 end as otherUserCountT ");
        params.addAll(userParams);
        sql.append(" from ncr_function functionT "
                + "join ncr_function_master on ncr_function_master.pk=functionT.functionMasterFk and ncr_function_master.special=false"
                + " join ncr on functionT.objectFk = ncr.pk and functionT.objectType = 'NCR' ");
        if (ncrItemReportFilter.getNcrItemStatusList() != null && ncrItemReportFilter.getNcrItemStatusList().size() > 0)
        {
            sql.append(" and ncr.ncrStatus in (");
            String comma = "";
            for (NcrEnum.NcrItemStatus status : ncrItemReportFilter.getNcrItemStatusList())
            {
                sql.append(comma).append("?");
                comma = ", ";
                params.add(status.name());
            }
            sql.append(" ) ");
        }

        sql.append(" " + " join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk "
                        + " JOIN project_site_config projectSiteConfig ON projectSiteConfig.projectfk = ncrGroup.projectfk AND projectSiteConfig.sitefk = ncrGroup.sitefk "
                        + " JOIN acl aclT ON aclT.objectpk = projectSiteConfig.pk AND aclT.objecttype =  ")
                .append(EntityTypeEnum.ProjectSiteConfig.getValue());

        sql.append(" AND aclT.userpk IN ").append(userListSb.toString());
        params.addAll(userParams);

        sql.append("" + " 		AND aclT.roleid = CASE "
                + "             WHEN functionT.functionmasterfk = 3022 THEN 'InternalTransport' "
                + "             WHEN functionT.functionmasterfk = 3018 THEN 'PlanningControl' "
                + "             WHEN functionT.functionmasterfk = 3001 THEN  'ProjectProductManagement' "
                + "             WHEN functionT.functionmasterfk = 3011 THEN  'MethodsProcessEngineering' "
                + "             WHEN functionT.functionmasterfk = 3016 THEN  'SupplyChain' "
                + "             WHEN functionT.functionmasterfk = 3017 THEN  'Sourcing' "
                + "             WHEN functionT.functionmasterfk = 3020 THEN  'Warehouse' "
                + "             WHEN functionT.functionmasterfk = 3006 THEN  'Engineering' "
                + "             WHEN functionT.functionmasterfk = 3019 THEN  'QualityAssurance' "
                + "             WHEN functionT.functionmasterfk = 3030 THEN  'ComponentProduction' "
                + "             WHEN functionT.functionmasterfk = 3040 THEN  'Weldingshop' "
                + "             WHEN functionT.functionmasterfk = 3050 THEN  'Carbody' "
                + "             WHEN functionT.functionmasterfk = 3060 THEN  'Paintshop' "
                + "             WHEN functionT.functionmasterfk = 3070 THEN  'PreAssembly' "
                + "             WHEN functionT.functionmasterfk = 3080 THEN  'FinalAssembly' "
                + "             WHEN functionT.functionmasterfk = 3100 THEN  'Retrofit' "
                + "             WHEN functionT.functionmasterfk = 3200 THEN  'Refurbishment' "
                + "             WHEN functionT.functionmasterfk = 3300 THEN  'Testing' "
                + "             WHEN functionT.functionmasterfk = 3400 THEN  'ShippingToCustomer' "
                + "             WHEN functionT.functionmasterfk = 3500 THEN  'ProductIntrodution' "
                + "             WHEN functionT.functionmasterfk = 3600 THEN  'FieldMaintenanceService' "
                + "             WHEN functionT.functionmasterfk = 3700 THEN  'SupplierQualityAssurance' "
                + "             WHEN functionT.functionmasterfk = 3800 THEN  'Finance' "
                + "             WHEN functionT.functionmasterfk = 3900 THEN  'SiteGM' "
                + "				WHEN functionT.functionmasterfk = 3901 THEN 'Installation' "
                + "           end "
                + " left join ncr_function_notifiers notifierT on notifierT.ncrFunctionFk = functionT.pk " + " where "
                + " functionT.objecttype = 'NCR' " + " AND functionT.approverequired = 1 "
                + " AND functionT.approvedstatus IS NULL " + " AND functionT.iscurrent = 1 )functionRecs "
                + " group by functionRecs.ncrFunctionFk, functionRecs.ncrFk ");

        if (filter.getShowApprovalsPendingForUserTeam() == false)
        {
            sql.append(" having selectedUsersCount > 0 or otherUserCount = 0");
        }

        return new QueryObject(sql.toString(), params);
    }

    private QueryObject getNcrsWithSpecialProcessFunction(NcrItemListReportFilter filter)
    {
        QueryObject qb = getSpecialProcessFunctionSubQuery(filter);

        StringBuilder sb = new StringBuilder("select distinct specialProcessNcrs.ncrFk from (");
        sb.append(qb.getQuery());
        sb.append(") specialProcessNcrs ");

        return new QueryObject(sb.toString(), qb.getParams());
    }

    private QueryObject getSpecialProcessFunctionSubQuery(NcrItemListReportFilter filter)
    {
        // create a user selection string like (222, 234, 453 )
        List<UserOID> userOIDList = filter.getSpecialProcessPendingFor();
        List<Object> userParams = new ArrayList<>();
        StringBuffer userListSb = new StringBuffer("(");
        String usep = "";
        for (Iterator iterator = userOIDList.iterator(); iterator.hasNext();)
        {
            UserOID userOID = (UserOID) iterator.next();
            userListSb.append(usep).append("?");
            userParams.add(userOID.getPk());
            usep = ", ";
        }
        userListSb.append(")");

        // now start the real sql
        List<Object> params = new ArrayList<>();
        StringBuffer sql = new StringBuffer(""
                + " select distinct functionRecs.ncrFunctionFk, functionRecs.ncrFk, sum(selectedUsersCountT) as selectedUsersCount, sum(otherUserCountT) as otherUserCount "
                + " from " + " ( "
                + " select functionT.pk as ncrFunctionFk, functionT.objectFk as ncrFk, notifierT.userFk, ");
        sql.append(" case when notifierT.userFk in ").append(userListSb.toString())
                .append(" then 1 else 0 end as selectedUsersCountT, ");
        params.addAll(userParams);
        sql.append(" case when notifierT.userFk not in ").append(userListSb.toString())
                .append(" then 1 else 0 end as otherUserCountT ");
        params.addAll(userParams);
        sql.append(" from ncr_function functionT "
                + "join ncr_function_master on ncr_function_master.pk=functionT.functionMasterFk and ncr_function_master.special=true"
                + " join ncr on functionT.objectFk = ncr.pk and functionT.objectType = 'NCR' ");
        if (ncrItemReportFilter.getNcrItemStatusList() != null && ncrItemReportFilter.getNcrItemStatusList().size() > 0)
        {
            sql.append(" and ncr.ncrStatus in (");
            String comma = "";
            for (NcrEnum.NcrItemStatus status : ncrItemReportFilter.getNcrItemStatusList())
            {
                sql.append(comma).append("?");
                comma = ", ";
                params.add(status.name());
            }
            sql.append(" ) ");
        }

        sql.append(" " + " join ncr_group ncrGroup on ncr.ncrGroupFk = ncrGroup.pk "
                        + " JOIN project_site_config projectSiteConfig ON projectSiteConfig.projectfk = ncrGroup.projectfk AND projectSiteConfig.sitefk = ncrGroup.sitefk "
                        + " JOIN acl aclT ON aclT.objectpk = projectSiteConfig.pk AND aclT.objecttype =  ")
                .append(EntityTypeEnum.ProjectSiteConfig.getValue());

        sql.append(" AND aclT.userpk IN ").append(userListSb.toString());
        params.addAll(userParams);

        sql.append("" + " 		AND aclT.roleid = CASE "
                + "             WHEN functionT.functionmasterfk = 4001 THEN 'A_Welding' "
                + "             WHEN functionT.functionmasterfk = 4002 THEN 'B_BondingSealing' "
                + "             WHEN functionT.functionmasterfk = 4003 THEN  'C_SurfaceTreatment' "
                + "             WHEN functionT.functionmasterfk = 4004 THEN  'D_ElectricalConnection' "
                + "             WHEN functionT.functionmasterfk = 4005 THEN  'E_TorqueTighten' "
                + "             WHEN functionT.functionmasterfk = 4006 THEN  'F_Riveting' "
                + "             WHEN functionT.functionmasterfk = 4007 THEN  'G_Casting' "
                + "             WHEN functionT.functionmasterfk = 4008 THEN  'H_Forging' "
                + "             WHEN functionT.functionmasterfk = 4009 THEN  'I_HeatTreatment' "
                + "             WHEN functionT.functionmasterfk = 4010 THEN  'J_Moulding' "
                + "             WHEN functionT.functionmasterfk = 4011 THEN  'K_Laminating' "
                + "             WHEN functionT.functionmasterfk = 4012 THEN  'L_ForceshrinkFitting' "
                + "             WHEN functionT.functionmasterfk = 4013 THEN  'M_PottingImpregnation' "
                + "             WHEN functionT.functionmasterfk = 4014 THEN  'N_StressReliefTreatment' "
                + "             WHEN functionT.functionmasterfk = 4015 THEN  'O_3DPrinting' "
                + "             WHEN functionT.functionmasterfk = 4016 THEN  'P_Sintering' "
                + "             end "
                + " left join ncr_function_notifiers notifierT on notifierT.ncrFunctionFk = functionT.pk " + " where "
                + " functionT.objecttype = 'NCR' " + " AND functionT.approverequired = 1 "
                + " AND functionT.approvedstatus IS NULL " + " AND functionT.iscurrent = 1 )functionRecs "
                + " group by functionRecs.ncrFunctionFk, functionRecs.ncrFk ");

        if (filter.getShowApprovalsPendingForUserTeam() == false)
        {
            sql.append(" having selectedUsersCount > 0 or otherUserCount = 0");
        }

        return new QueryObject(sql.toString(), Collections.singletonList(params));
    }

}

