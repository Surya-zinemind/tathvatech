package com.tathvatech.equipmentcalibration.report;

import com.tathvatech.common.common.QueryObject;
import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.equipmentcalibration.enums.CalibrationStatusEnum;
import com.tathvatech.equipmentcalibration.enums.EquipmentStatusEnum;
import com.tathvatech.equipmentcalibration.oid.EquipmentTypeOID;
import com.tathvatech.equipmentcalibration.oid.LocationOID;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.report.response.ReportResponse;
import com.tathvatech.user.OID.Role;
import com.tathvatech.user.OID.SiteOID;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.enums.SiteRolesEnum;
import com.tathvatech.user.service.AuthorizationManager;

import java.util.*;

public class EquipmentListReport
{
    private final PersistWrapper persistWrapper;
    private final AuthorizationManager authorizationManager;
    private ReportRequest reportRequest;
    private EquipmentListFilter filter;
    private UserContext context;

    public EquipmentListReport(PersistWrapper persistWrapper, AuthorizationManager authorizationManager, UserContext context, ReportRequest reportRequest)
    {
        super();
        this.persistWrapper = persistWrapper;
        this.authorizationManager = authorizationManager;
        this.reportRequest = reportRequest;
        this.filter = (EquipmentListFilter) reportRequest.getFilter();
        this.context = context;
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
        List<EquipmentListReportRow> rows = persistWrapper.readList(EquipmentListReportRow.class, sql.toString(),
                qb.getParams().toArray());
        response.setReportData(rows);

        return response;
    }

    private QueryObject getQuery()
    {
        StringBuffer sb = new StringBuffer();
        List<Object> params = new ArrayList<Object>();

        sb.append(" SELECT ");
        sb.append(
                " eq.pk,site.pk as siteFk, site.name as siteName, eqh.equipmentId,eqh.reference, eqh.serialNo, eq.modelNo, eq.description, eq.manufacturer, eq.dateOfPurchase, ");
        sb.append(" eqh.statusUpdatedDate,");
        sb.append(" eq.equipmentTypeFk, eqType.name as equipmentType, ");
        sb.append(" concat(createdByTab.firstName,' ',createdByTab.lastName) as createdByName,");

        sb.append(" eq.createdDate, ");
        sb.append(" eqh.custodianFk, concat (custodianTab.firstName, ' ', custodianTab.lastName) as custodianName, ");
        sb.append(" eq.approvedDate, concat (approverTab.firstName, ' ', approverTab.lastName) as approvedByName, ");
        sb.append(" eqh.locationFk, ");
        sb.append(" concat(locationSite.name,'_',locationType.name,'_',location.name) as location,");

        sb.append(" eqh.status,eqh.instruction,eqh.cost,csc.currency, ");
        sb.append(" eqh.calibrationAuthorityFk, caliAuth.name as calibrationAutority, ");
        sb.append(
                " eqh.calibrationIntervalFk, concat(caliInterval.name,' - ',caliInterval.description) as calibrationInterval, ");
        sb.append(
                " caliData.calibrationReferenceNo as calibrationReferenceNo, caliData.calibrationDate as lastCalibrationDate, caliData.nextCalibrationDate as nextCalibrationDate, caliData.calibationStatus,caliData.comment as calibrationComment, ");
        sb.append(" concat(calibDataCreatedByTab.firstName,' ',calibDataCreatedByTab.lastName) as calibratedByName ");

        sb.append(" FROM equipment eq ");
        sb.append(
                " inner join equipment_h eqh on eqh.equipmentFK=eq.pk and now() between eqh.effectiveFrom and eqh.effectiveTo");
        sb.append(" left join equipment_type eqType on eqType.pk=eq.equipmentTypeFk");
        sb.append(" join site on site.pk=eqh.siteFk ");
        sb.append(" left join TAB_USER custodianTab on eqh.custodianFk = custodianTab.pk ");
        sb.append(" left join TAB_USER approverTab on eq.approvedBy = approverTab.pk ");
        sb.append(" left join TAB_USER createdByTab on eq.createdBy = createdByTab.pk ");
        sb.append(" left join location_master location on eqh.locationFk=location.pk ");
        sb.append(" left join location_type locationType on location.locationTypeFk=locationType.pk ");
        sb.append(" left join site locationSite  on location.siteFk=locationSite.pk ");

        sb.append(" left join equipment_calibration_authority caliAuth on eqh.calibrationAuthorityFk = caliAuth.pk  ");
        sb.append(
                " left join equipment_calibration_interval_m caliInterval on eqh.calibrationIntervalFk = caliInterval.pk  ");
        // sb.append(" left join (select * from equipment_calibration group by
        // equipment_calibration.equipmentFk order by equipment_calibration.pk
        // desc) caliData on caliData.equipmentFk = eq.pk");
        sb.append(" left join equipment_calibration caliData on caliData.equipmentFk = eq.pk  and caliData.current=1 ");
        sb.append(" left join TAB_USER calibDataCreatedByTab on caliData.createdBy = calibDataCreatedByTab.pk ");
        sb.append(" left join calibration_site_currency_ref csc on csc.siteFk=eqh.siteFk ");
        sb.append(" where 1=1");
        sb.append(" and eq.estatus = ? ");
        params.add(EStatusEnum.Active.getValue());

        if(filter.getEquipmentOID() != null)
        {
            sb.append(" and eq.pk  = ? ");
            params.add(filter.getEquipmentOID().getPk());
        }

        if (Optional.ofNullable(filter.getEquipmentId()).isPresent() && filter.getEquipmentId().length() > 0)
        {
            sb.append(" and LOWER(eqh.equipmentId) like lower(?) ");
            params.add("%" + filter.getEquipmentId() + "%");
        }
        if (Optional.ofNullable(filter.getDescription()).isPresent() && filter.getDescription().length() > 0)
        {
            sb.append(" and LOWER(eq.description) like lower(?) ");
            params.add("%" + filter.getDescription() + "%");
        }
        if (Optional.ofNullable(filter.getSerialNo()).isPresent() && filter.getSerialNo().length() > 0)
        {
            sb.append(" and LOWER(eqh.serialNo) like lower(?) ");
            params.add("%" + filter.getSerialNo() + "%");
        }
        if (Optional.ofNullable(filter.getReference()).isPresent() && filter.getReference().length() > 0)
        {
            sb.append(" and LOWER(eqh.reference) like lower(?) ");
            params.add("%" + filter.getReference() + "%");
        }
        if (Optional.ofNullable(filter.getSearchString()).isPresent() && filter.getSearchString().length() > 0)
        {
            sb.append(" and (LOWER(eqh.equipmentId) like lower(?) or LOWER(eqh.serialNo) like lower(?)) ");
            params.add(filter.getSearchString() + "%");
            params.add(filter.getSearchString() + "%");
        }
        if (Optional.ofNullable(filter.getLocations()).isPresent() && filter.getLocations().size() > 0)
        {
            boolean unAllocatedLocation = false;

            sb.append(" and ( eqh.locationFk in (");
            String comma = "";
            for (LocationOID locationOID : filter.getLocations())
            {
                sb.append(comma).append("?");
                params.add(locationOID.getPk());
                comma = ",";
                if (locationOID.getPk() == 0)
                {
                    unAllocatedLocation = true;
                }
            }
            sb.append(" ) ");
            if (unAllocatedLocation)
            {
                sb.append(" or eqh.locationFk is null ");
            }
            sb.append(") ");
        }
        if (Optional.ofNullable(filter.getSiteOIDs()).isPresent() && filter.getSiteOIDs().size() != 0)
        {
            sb.append(" and eqh.siteFk in (");
            String ssep = "";
            for (SiteOID aSite : filter.getSiteOIDs())
            {
                sb.append(ssep).append("?");
                ssep = ",";
                params.add(aSite.getPk());
            }
            sb.append(") ");
        }
        if (Optional.ofNullable(filter.getCustodian()).isPresent() && filter.getCustodian().size() > 0)
        {
            boolean unAllocatedCustodian = false;

            sb.append(" and ( eqh.custodianFk in (");
            String comma = "";
            for (UserOID userOID : filter.getCustodian())
            {
                sb.append(comma).append("?");
                params.add(userOID.getPk());
                comma = ",";
                if (userOID.getPk() == 0)
                {
                    unAllocatedCustodian = true;
                }
            }
            sb.append(" ) ");
            if (unAllocatedCustodian)
            {
                sb.append(" or eqh.custodianFk is null ");
            }
            sb.append(") ");
        }
        if (Optional.ofNullable(filter.getEquipmentTypes()).isPresent() && filter.getEquipmentTypes().size() > 0)
        {
            sb.append(" and eq.equipmentTypeFk in (");
            String comma = "";
            for (EquipmentTypeOID type : filter.getEquipmentTypes())
            {
                sb.append(comma).append("?");
                params.add(type.getPk());
                comma = ",";
            }
            sb.append(" ) ");

        }
        if (Optional.ofNullable(filter.getEquipmentStatusList()).isPresent()
                && filter.getEquipmentStatusList().size() > 0)
        {
            sb.append(" and eqh.status in (");
            String comma = "";
            for (EquipmentStatusEnum type : filter.getEquipmentStatusList())
            {
                sb.append(comma).append("?");
                params.add(type.name());
                comma = ",";
            }
            sb.append(" ) ");
        }
        if (Optional.ofNullable(filter.getCalibrationStatusList()).isPresent()
                && filter.getCalibrationStatusList().size() > 0)
        {
            sb.append(" and caliData.calibationStatus in (");
            String comma = "";
            for (CalibrationStatusEnum type : filter.getCalibrationStatusList())
            {
                sb.append(comma).append("?");
                params.add(type.name());
                comma = ",";
            }
            sb.append(" ) ");

        }
        if (Optional.ofNullable(filter.getEquipmentAuthority()).isPresent()
                && filter.getEquipmentAuthority().size() > 0)
        {
            sb.append(" and eqh.calibrationAuthorityFk in (");
            String comma = "";
            for (Integer pk : filter.getEquipmentAuthority())
            {
                sb.append(comma).append("?");
                params.add(pk);
                comma = ",";
            }
            sb.append(" ) ");

        }
        if (Optional.ofNullable(filter.getOverdueOn()).isPresent())
        {
            Date[] dateRange = filter.getOverdueOn().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                Date createdDateFrom = dateRange[0];
                Date createdDateTo = dateRange[1];
                if (createdDateFrom != null)
                {
                    sb.append(" and caliData.nextCalibrationDate > ? ");
                    params.add(createdDateFrom);
                }
                if (createdDateTo != null)
                {
                    sb.append(" and caliData.nextCalibrationDate  < ?");
                    params.add(createdDateTo);
                }
                if (createdDateFrom != null || createdDateTo != null)
                {
                    sb.append(" and eqh.status = ? ");
                    params.add(EquipmentStatusEnum.Active.name());
                }
            }

        }
        if (Optional.ofNullable(filter.getCreatedDate()).isPresent())
        {
            Date[] dateRange = filter.getCreatedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                Date createdDateFrom = dateRange[0];
                Date createdDateTo = dateRange[1];
                if (createdDateFrom != null)
                {
                    sb.append(" and eq.createdDate > ? ");
                    params.add(createdDateFrom);
                }
                if (createdDateTo != null)
                {
                    sb.append(" and eq.createdDate  < ?");
                    params.add(createdDateTo);
                }

            }

        }

        if (Optional.ofNullable(filter.getApprovedBy()).isPresent() && filter.getApprovedBy().size() > 0)
        {
            sb.append(" and eq.approvedBy in (");
            String comma = "";
            for (UserOID userOID : filter.getCustodian())
            {
                sb.append(comma).append("?");
                params.add(userOID.getPk());
                comma = ",";
            }
            sb.append(" ) ");
        }
        if (filter.getApprovedStatus() != null)
        {
            if (EquipmentListFilter.APPROVEDSTATUS.DRAFT.equals(filter.getApprovedStatus()))
            {
                sb.append(" and eq.approvedBy is null ");
            } else
            {
                sb.append(" and eq.approvedBy is not null ");

            }
        }

        if (Optional.ofNullable(filter.getPendingApprovalForCalibrationCoordinator()).isPresent()
                && filter.getPendingApprovalForCalibrationCoordinator().size() > 0)
        {

            List<Integer> calibrationCoordinatorSites = new ArrayList<Integer>();
            for (UserOID userOID : filter.getPendingApprovalForCalibrationCoordinator())
            {
                List<Integer> sites = authorizationManager.getEntitiesWithRole(userOID, EntityTypeEnum.Site,
                        new Role[] { SiteRolesEnum.CalibrationCoordinator });
                if (sites != null)
                {
                    calibrationCoordinatorSites.addAll(sites);
                }
            }
            String filterCalibrationCoordinatorSitePks = Arrays.deepToString(calibrationCoordinatorSites.toArray());
            filterCalibrationCoordinatorSitePks = filterCalibrationCoordinatorSitePks.replace('[', '(');
            filterCalibrationCoordinatorSitePks = filterCalibrationCoordinatorSitePks.replace(']', ')');
            sb.append(" and eq.approvedBy is null ");

            if (calibrationCoordinatorSites != null && calibrationCoordinatorSites.size() > 0)
            {
                if (calibrationCoordinatorSites != null && calibrationCoordinatorSites.size() > 0)
                {
                    sb.append(" and eqh.siteFk in ").append(filterCalibrationCoordinatorSitePks);
                }
            } else
            {
                sb.append(" and 0=1 ");
            }

        }

        if (Optional.ofNullable(filter.getPendingApprovalForSiteAdmin()).isPresent()
                && filter.getPendingApprovalForSiteAdmin().size() > 0)
        {

            List<Integer> adminsSites = new ArrayList<Integer>();
            for (UserOID userOID : filter.getPendingApprovalForSiteAdmin())
            {
                List<Integer> sites = authorizationManager.getEntitiesWithRole(userOID, EntityTypeEnum.Site,
                        new Role[] { SiteRolesEnum.SiteAdmin });
                if (sites != null)
                {
                    adminsSites.addAll(sites);
                }
            }
            String filterCalibrationCoordinatorSitePks = Arrays.deepToString(adminsSites.toArray());
            filterCalibrationCoordinatorSitePks = filterCalibrationCoordinatorSitePks.replace('[', '(');
            filterCalibrationCoordinatorSitePks = filterCalibrationCoordinatorSitePks.replace(']', ')');
            sb.append(" and eq.approvedBy is null ");

            if (adminsSites != null && adminsSites.size() > 0)
            {
                if (adminsSites != null && adminsSites.size() > 0)
                {
                    sb.append(" and eqh.siteFk in ").append(filterCalibrationCoordinatorSitePks);
                }
            } else
            {
                sb.append(" and 0=1 ");
            }

        }
        if (Optional.ofNullable(filter.getPendingForApprovalLimit()).isPresent())
        {
            if (filter.getPendingForApprovalLimit().equals(EquipmentListFilter.PendingforApprovalLimit.BEFORE1MONTH))
            {
                sb.append(" and eq.createdDate <= subdate(NOW(), 30) ");
            } else if (filter.getPendingForApprovalLimit().equals(EquipmentListFilter.PendingforApprovalLimit.BEFORE5DAYS))
            {
                sb.append(" and eq.createdDate <= subdate(NOW(), 5) ");
            } else if (filter.getPendingForApprovalLimit().equals(EquipmentListFilter.PendingforApprovalLimit.ALL))
            {
                // all date information
            }
            sb.append(" and eq.approvedBy is null ");
        }

        sb.append(" ORDER BY eq.createdDate DESC");

        return new QueryObject(sb.toString(), params);
    }

}

