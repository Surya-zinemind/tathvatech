package com.tathvatech.injuryReport.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.tathvatech.testsutra.injury.service.Injury;
import com.tathvatech.testsutra.ncr.common.QueryObject;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.User;
import com.tathvatech.ts.core.authorization.AuthorizationDelegate;
import com.tathvatech.ts.core.authorization.AuthorizationManager;
import com.tathvatech.ts.core.authorization.Role;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.InjuryAfterTreatmentOID;
import com.tathvatech.ts.core.project.LocationTypeOID;
import com.tathvatech.ts.core.project.WorkstationOID;
import com.tathvatech.ts.core.sites.SiteRolesEnum;
import com.thirdi.surveyside.project.Project;
import com.thirdi.surveyside.project.ProjectDelegate;

public class InjuryReportQueryBuilder
{
    private QueryObject query;
    private UserContext context;
    private InjuryReportQueryFilter injuryQueryFilter;

    public InjuryReportQueryBuilder(UserContext context, InjuryReportQueryFilter injuryQueryFilter)
    {
        super();
        this.context = context;
        this.injuryQueryFilter = injuryQueryFilter;
    }

    public InjuryReportQueryFilter getInjuryQueryFilter()
    {
        return injuryQueryFilter;
    }

    public void setInjuryQueryFilter(InjuryReportQueryFilter injuryQueryFilter)
    {
        this.injuryQueryFilter = injuryQueryFilter;
    }

    public QueryObject getQuery()
    {
        List<Object> params = new ArrayList();
        StringBuffer sqlQuery = new StringBuffer("");

        sqlQuery.append(" SELECT injury.pk as pk");
        sqlQuery.append(",").append("injury.injuryReportNo as injuryReportNo");
        sqlQuery.append(",").append("injury.referenceRequired as referenceRequired");
        sqlQuery.append(",").append("injury.referenceNo as referenceNo ");
        sqlQuery.append(",").append("injury.injuredPerson as injuredPerson");
        sqlQuery.append(",").append("injury.typeOfPerson as typeOfPerson");
        sqlQuery.append(",").append("injury.typeOfInjury as typeOfInjury");
        sqlQuery.append(",").append("injury.dateOfInjury as dateOfInjury");
        sqlQuery.append(",").append("injury.detailsEquipment as detailsEquipment");
        sqlQuery.append(",").append("injury.detailsWitnes as detailsWitnes ");
        sqlQuery.append(",").append("injury.detailsTreatment as detailsTreatment");
        sqlQuery.append(",").append("injury.treatedBy as treatedBy");
        sqlQuery.append(",").append("injury.rootCauseOfInjury as rootCauseOfInjury");
        sqlQuery.append(",").append("injury.supplimentaryCause as supplimentaryCause");
        sqlQuery.append(",").append("injury.precautionDone as precautionDone");
        sqlQuery.append(",").append("injury.precautionRequired as precautionRequired");
        sqlQuery.append(",").append("injury.reportingRequired as reportingRequired ");
        sqlQuery.append(",").append("injury.reportingNumber as reportingNumber");
        // sqlQuery.append(",").append("injury.treatedBy as treatedBy");
        // sqlQuery.append(",").append("injury.rootCauseOfInjury as
        // rootCauseOfInjury");
        // sqlQuery.append(",").append("injury.supplimentaryCause as
        // supplimentaryCause");
        sqlQuery.append(",").append("injury.fatality as fatality");
        sqlQuery.append(",").append("injury.headfront as headfront");
        sqlQuery.append(",").append("injury.headback as headback");
        sqlQuery.append(",").append("injury.face as face");
        sqlQuery.append(",").append("injury.lefteye as lefteye ");
        sqlQuery.append(",").append("injury.righteye as righteye");
        sqlQuery.append(",").append("injury.leftear as leftear");
        sqlQuery.append(",").append("injury.neckfront as neckfront");
        sqlQuery.append(",").append("injury.neckback as neckback");
        sqlQuery.append(",").append("injury.upperbodyfront as upperbodyfront");
        sqlQuery.append(",").append("injury.upperbodyback as upperbodyback");
        sqlQuery.append(",").append("injury.lowerbodyfront as lowerbodyfront");
        sqlQuery.append(",").append("injury.lowerbodyback as lowerbodyback");
        sqlQuery.append(",").append("injury.shoulderleft as shoulderleft ");
        sqlQuery.append(",").append("injury.shoulderright as shoulderright");
        sqlQuery.append(",").append("injury.upperarmright as upperarmright");
        sqlQuery.append(",").append("injury.lowerarmright as lowerarmright");
        sqlQuery.append(",").append("injury.lowerarmleft as lowerarmleft");
        sqlQuery.append(",").append("injury.elbowright as elbowright");
        sqlQuery.append(",").append("injury.wristleft as wristleft");
        sqlQuery.append(",").append("injury.wristright as wristright");
        sqlQuery.append(",").append("injury.fingersleft as fingersleft");
        sqlQuery.append(",").append("injury.fingersright as fingersright");
        sqlQuery.append(",").append("injury.upperlegright as upperlegright");
        sqlQuery.append(",").append("injury.upperlegleft as upperlegleft");
        sqlQuery.append(",").append("injury.kneeright as kneeright");
        sqlQuery.append(",").append("injury.kneeleft as kneeleft");
        sqlQuery.append(",").append("injury.lowerlegright as lowerlegright");
        sqlQuery.append(",").append("injury.lowerlegleft as lowerlegleft");
        sqlQuery.append(",").append("injury.ankleleft as ankleleft");
        sqlQuery.append(",").append("injury.ankleright as ankleright");
        sqlQuery.append(",").append("injury.footright as footright");
        sqlQuery.append(",").append("injury.footleft as footleft");
        sqlQuery.append(",").append("injury.elbowleft as elbowleft");
        sqlQuery.append(",").append("injury.upperarmleft as upperarmleft");
        sqlQuery.append(",").append("injury.rightear as rightear");
        sqlQuery.append(",").append("injury.other as other");
        sqlQuery.append(",").append("injury.description as description");
        sqlQuery.append(",").append("injury.sitePk as sitePk");
        sqlQuery.append(",").append("injury.createdBy as createdBy");
        sqlQuery.append(",").append("injury.createdByInitial as createdByInitial");
        sqlQuery.append(",").append("injury.createdDate as createdDate");
        sqlQuery.append(",").append("injury.acknowledgedDate as acknowledgedDate");
        sqlQuery.append(",").append("injury.closedDate as closedDate");
        sqlQuery.append(",").append("injury.projectPk as projectPk");
        sqlQuery.append(",").append("injury.locationPk as locationPk");
        sqlQuery.append(",").append("injury.locationType as locationType");
        sqlQuery.append(",").append("injury.custodianPk as custodianPk");
        sqlQuery.append(",").append("injury.status as status");
        sqlQuery.append(",").append("injury.lastUpdated as lastUpdated");
        sqlQuery.append(",").append("injury.acknowledgedBy as acknowledgedBy");
        sqlQuery.append(",").append("injury.verifiedDate as verifiedDate");
        sqlQuery.append(",").append("injury.verifiedBy as verifiedBy");
        sqlQuery.append(",").append("injury.closedBy as closedBy");
        sqlQuery.append(",").append("injury.detailsEquipmentStatus as detailsEquipmentStatus");
        sqlQuery.append(",").append("injury.detailsWitnesStatus as detailsWitnesStatus");
        sqlQuery.append(",").append("injury.otherParts as otherParts");
        sqlQuery.append(",").append("injury.locationOther as locationOther");
        sqlQuery.append(",").append("injury.supervisedBy as supervisedBy");
        sqlQuery.append(",").append("case when createdByTab.pk is not null then concat(createdByTab.firstName,' ',createdByTab.lastName) else injury.createdByInitial end as createdByName");
        sqlQuery.append(",")
                .append("concat(aknowledgedByTab.firstName,' ',aknowledgedByTab.lastName) as aknowledgeName");
        sqlQuery.append(",").append("concat(verifiedByTab.firstName,' ',verifiedByTab.lastName) as verifiedByName");
        sqlQuery.append(",").append("concat(closedByTab.firstName,' ',closedByTab.lastName) as closedByName");
        sqlQuery.append(",")
                .append("concat(supervisedByTab.firstName,' ',supervisedByTab.lastName) as supervisedByName");
        sqlQuery.append(",").append(
                " case when injury.locationType = 'Workstation' then concat(workstation.workstationName,' - ',workstation.description) when locationType = 'Location' then injuryLocation.name  end as locationName");

        sqlQuery.append(",").append("site.description as siteDescription");
        sqlQuery.append(",").append("site.name as siteName");
        sqlQuery.append(",").append(
                "CONCAT(COALESCE(project.projectName,''),'-',COALESCE(project.projectDescription,'')) as projectName");
        sqlQuery.append(" from ");
        sqlQuery.append(" injury ");
        sqlQuery.append(" join site on site.pk = injury.sitePk");
        sqlQuery.append(" left join TAB_PROJECT project on project.pk = injury.projectPk");
        sqlQuery.append(" left join TAB_USER createdByTab on createdByTab.pk = injury.createdBy");
        sqlQuery.append(" left join TAB_USER aknowledgedByTab on aknowledgedByTab.pk = injury.acknowledgedBy");
        sqlQuery.append(" left join TAB_USER verifiedByTab on verifiedByTab.pk = injury.verifiedBy");
        sqlQuery.append(" left join TAB_USER closedByTab on closedByTab.pk = injury.closedBy");
        sqlQuery.append(" left join TAB_USER supervisedByTab on supervisedByTab.pk = injury.supervisedBy");
        sqlQuery.append(" left join TAB_WORKSTATION workstation on workstation.pk =injury.locationPk ");
        sqlQuery.append(" left join injury_location_master injuryLocation on injuryLocation.pk = injury.locationPk");
        sqlQuery.append(
                " left join injury_assign_after_treatment injuryAssignAfterTreatment on injuryAssignAfterTreatment.injuryPk =injury.pk");
        sqlQuery.append(
                " left join injury_after_treatment_master injuryAfterTreatmentMaster on injuryAssignAfterTreatment.afterTreatmentMasterPk =injuryAfterTreatmentMaster.pk");
        sqlQuery.append(" where 1=1 ");

        applyPermissionBasedFilter(sqlQuery, params);

        if (injuryQueryFilter.getPk() != 0)
        {
            sqlQuery.append(" and injury.pk=?");
            params.add(injuryQueryFilter.getPk());
        } else
        {

            if (injuryQueryFilter.getInjuryReportNo() != null
                    && injuryQueryFilter.getInjuryReportNo().trim().length() > 0)
            {
                sqlQuery.append(" and (lower(injury.injuryReportNo) like lower(?)) ");
                params.add("%" + injuryQueryFilter.getInjuryReportNo().trim() + "%");
            }
            if (injuryQueryFilter.getInjuredPerson() != null
                    && injuryQueryFilter.getInjuredPerson().trim().length() > 0)
            {
                sqlQuery.append(" and (lower(injury.injuredPerson) like lower(?)) ");
                params.add("%" + injuryQueryFilter.getInjuredPerson().trim() + "%");

            }

            if (injuryQueryFilter.getSitePks() != null && injuryQueryFilter.getSitePks().size() != 0)
            {
                sqlQuery.append(" and injury.sitePk in ");
                String filterSitePks = Arrays.deepToString(injuryQueryFilter.getSitePks().toArray());
                filterSitePks = filterSitePks.replace('[', '(');
                filterSitePks = filterSitePks.replace(']', ')');
                sqlQuery.append(filterSitePks);
            }
            if (injuryQueryFilter.getPendingVerificationLimit() != null)
            {
                LimitObject limitObject = injuryQueryFilter.getPendingVerificationLimit();
                AuthorizationManager authorizationManager = new AuthorizationManager();
                List<Integer> sitePks = null;
                if (limitObject.getLimit().equals(DateLimit.MORETHAN1DAYS))
                {
                    sitePks = authorizationManager.getEntitiesWithRole(limitObject.getUserOID(), EntityTypeEnum.Site,
                            new Role[] { SiteRolesEnum.HSECoordinator });
                } else if (limitObject.getLimit().equals(DateLimit.MORETHAN2DAYS)
                        || limitObject.getLimit().equals(DateLimit.MORETHAN7DAYS))
                {
                    sitePks = authorizationManager.getEntitiesWithRole(limitObject.getUserOID(), EntityTypeEnum.Site,
                            new Role[] { SiteRolesEnum.HSEDirector });
                }

                if (sitePks != null && sitePks.size() > 0)
                {
                    String filterSitePks = Arrays.deepToString(sitePks.toArray());
                    filterSitePks = filterSitePks.replace('[', '(');
                    filterSitePks = filterSitePks.replace(']', ')');
                    sqlQuery.append(" and injury.sitePk in ");
                    sqlQuery.append(filterSitePks);
                    sqlQuery.append(" and injury.status=? ");
                    params.add(Injury.StatusEnum.Open.name());
                    if (limitObject.getLimit().equals(DateLimit.MORETHAN1DAYS))
                    {
                        sqlQuery.append(" and injury.createdDate <= subdate(NOW(), 1) ");
                    } else if (limitObject.getLimit().equals(DateLimit.MORETHAN2DAYS))
                    {
                        sqlQuery.append(" and injury.createdDate <= subdate(NOW(), 2) ");
                    } else if (limitObject.getLimit().equals(DateLimit.MORETHAN7DAYS))
                    {
                        sqlQuery.append(" and injury.createdDate <= subdate(NOW(), 7) ");
                    }
                } else
                {
                    sqlQuery.append(" and 1=0 ");
                }
            }

            if (injuryQueryFilter.getCreatedDate() != null)
            {
                Date[] dateRange = injuryQueryFilter.getCreatedDate().getResolvedDateRangeValues();
                if (dateRange != null)
                {
                    Date createdDateFrom = dateRange[0];
                    Date createdDateTo = dateRange[1];
                    if (createdDateFrom != null)
                    {
                        sqlQuery.append(" and injury.createdDate > ? ");
                        params.add(createdDateFrom);
                    }
                    if (createdDateTo != null)
                    {
                        sqlQuery.append(" and injury.createdDate  < ?");
                        params.add(createdDateTo);
                    }

                }

            }

            if (injuryQueryFilter.getInjuredDate() != null)
            {
                Date[] dateRange = injuryQueryFilter.getInjuredDate().getResolvedDateRangeValues();
                if (dateRange != null)
                {
                    Date createdDateFrom = dateRange[0];
                    Date createdDateTo = dateRange[1];
                    if (createdDateFrom != null)
                    {
                        sqlQuery.append(" and injury.dateOfInjury > ? ");
                        params.add(createdDateFrom);
                    }
                    if (createdDateTo != null)
                    {
                        sqlQuery.append(" and injury.dateOfInjury  < ?");
                        params.add(createdDateTo);
                    }

                }

            }

            if (injuryQueryFilter.getSupervisor() != null)
            {
                sqlQuery.append(" and injury.supervisedBy=? ");
                params.add(injuryQueryFilter.getSupervisor());
            }
            if (injuryQueryFilter.getTypeOfInjury() != null)
            {
                sqlQuery.append(" and injury.typeOfInjury=? ");
                params.add(injuryQueryFilter.getTypeOfInjury());
            }
            if (injuryQueryFilter.getTypeOfPerson() != null)
            {
                sqlQuery.append(" and injury.typeOfPerson=? ");
                params.add(injuryQueryFilter.getTypeOfPerson());
            }
            if (injuryQueryFilter.getReportingRequired() != null && injuryQueryFilter.getReportingRequired())
            {
                sqlQuery.append(" and injury.reportingRequired =1 ");
            } else if (injuryQueryFilter.getReportingRequired() != null && !injuryQueryFilter.getReportingRequired())
            {
                sqlQuery.append(" and injury.reportingRequired =0 ");
            }
            // if (injuryQueryFilter.getWorkRelated() != null &&
            // injuryQueryFilter.getWorkRelated())
            // {
            // sqlQuery.append(" and injury.typeOfInjury =1 ");
            // }else if (injuryQueryFilter.getWorkRelated() != null &&
            // !injuryQueryFilter.getWorkRelated())
            // {
            // sqlQuery.append(" and injury.typeOfInjury =0 ");
            // }
            if (injuryQueryFilter.getWorkstationPk() != 0)
            {
                sqlQuery.append(" and injury.locationType = 'Workstation' and injury.locationPk=?");
                params.add(injuryQueryFilter.getWorkstationPk());
            }
            if (injuryQueryFilter.getLocations() != null && injuryQueryFilter.getLocations().size() > 0)
            {
                String commaFlag = "";
                StringBuffer workstationBuffer = new StringBuffer();
                for (Object location : injuryQueryFilter.getLocations())
                {
                    if (location instanceof WorkstationOID && ((WorkstationOID) location).getPk() != 0)
                    {
                        workstationBuffer.append(commaFlag).append(((WorkstationOID) location).getPk());
                        commaFlag = ",";
                    }
                }
                commaFlag = "";
                StringBuffer locationBuffer = new StringBuffer();
                for (Object location : injuryQueryFilter.getLocations())
                {
                    if (location instanceof LocationTypeOID && ((LocationTypeOID) location).getPk() != 0)
                    {
                        locationBuffer.append(commaFlag).append(((LocationTypeOID) location).getPk());
                        commaFlag = ",";
                    }
                }
                sqlQuery.append(" and ((injury.locationType = 'Workstation' and injury.locationPk in (");
                if (workstationBuffer.length() > 0)
                {
                    sqlQuery.append(workstationBuffer);
                } else
                {
                    sqlQuery.append(0);
                }
                sqlQuery.append("))  ");
                sqlQuery.append(" or (injury.locationType = 'Location' and injury.locationPk in (");
                if (locationBuffer.length() > 0)
                {
                    sqlQuery.append(locationBuffer);
                } else
                {
                    sqlQuery.append(0);
                }
                sqlQuery.append(") ))");
            }
            if ((injuryQueryFilter.getStatusList() == null || injuryQueryFilter.getStatusList().size() == 0))
            {
                sqlQuery.append(" and injury.status = 'None' ");
            } else
            {
                if (injuryQueryFilter.getStatusList() != null && injuryQueryFilter.getStatusList().size() > 0)
                {
                    sqlQuery.append(" and injury.status in (");
                    String sep = "";
                    for (String status : injuryQueryFilter.getStatusList())
                    {
                        sqlQuery.append(sep);
                        sqlQuery.append("'").append(status).append("'");
                        sep = ",";
                    }
                    sqlQuery.append(")");
                }
            }

            if (injuryQueryFilter.getAfterTreatmentList() != null
                    && injuryQueryFilter.getAfterTreatmentList().size() > 0)
            {
                sqlQuery.append("and injuryAfterTreatmentMaster.pk in (");
                String sep = "";
                for (InjuryAfterTreatmentOID oid : injuryQueryFilter.getAfterTreatmentList())
                {
                    sqlQuery.append(sep);
                    sqlQuery.append("'").append(oid.getPk()).append("'");
                    sep = ",";
                }
                sqlQuery.append(")");
            }
            if (injuryQueryFilter.getNatureOfInjury() != null && injuryQueryFilter.getNatureOfInjury().length > 0)
            {
                sqlQuery.append(" and ( ");
                String orflag = "";
                for (String name : injuryQueryFilter.getNatureOfInjury())
                {
                    sqlQuery.append(orflag).append(" injury." + name + " is true");
                    orflag = " or ";
                }
                sqlQuery.append(" ) ");
            }
        }
        if (injuryQueryFilter.getProjectPks() != null && injuryQueryFilter.getProjectPks().size() > 0)
        {
            sqlQuery.append(" and (injury.projectPk in ");
            String filterProjectPks = Arrays.deepToString(injuryQueryFilter.getProjectPks().toArray());
            filterProjectPks = filterProjectPks.replace('[', '(');
            filterProjectPks = filterProjectPks.replace(']', ')');
            sqlQuery.append(filterProjectPks);
            if (injuryQueryFilter.getProjectPks().contains(0))
            {
                sqlQuery.append(" or injury.projectPk is null ");
            }
            sqlQuery.append(" ) ");
        }
        if (injuryQueryFilter.getVerifiedDate() != null)
        {
            Date[] dateRange = injuryQueryFilter.getVerifiedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                Date createdDateFrom = dateRange[0];
                Date createdDateTo = dateRange[1];
                if (createdDateFrom != null)
                {
                    sqlQuery.append(" and injury.verifiedDate > ? ");
                    params.add(createdDateFrom);
                }
                if (createdDateTo != null)
                {
                    sqlQuery.append(" and injury.verifiedDate  < ?");
                    params.add(createdDateTo);
                }
            }
        }
        if (injuryQueryFilter.getClosedDate() != null)
        {
            Date[] dateRange = injuryQueryFilter.getClosedDate().getResolvedDateRangeValues();
            if (dateRange != null)
            {
                Date createdDateFrom = dateRange[0];
                Date createdDateTo = dateRange[1];
                if (createdDateFrom != null)
                {
                    sqlQuery.append(" and injury.closedDate > ? ");
                    params.add(createdDateFrom);
                }
                if (createdDateTo != null)
                {
                    sqlQuery.append(" and injury.closedDate  < ?");
                    params.add(createdDateTo);
                }
            }
        }
        // if (injuryQueryFilter.getProjectPk() != 0)
        // {
        //
        // sqlQuery.append(" and injury.projectPk in (?) ");
        // params.add(injuryQueryFilter.getProjectPk());
        // }

        sqlQuery.append(" group by injury.pk");
        sqlQuery.append(" ORDER BY createdDate DESC");

        QueryObject conditionQuery = new QueryObject();
        conditionQuery.setQuery(sqlQuery.toString());
        conditionQuery.setParams(params);
        return conditionQuery;
    }

    private void applyPermissionBasedFilter(StringBuffer sqlQuery, List<Object> params)
    {
        if (User.USER_PRIMARY.equals(context.getUser().getUserType()))
            return;
        List<Integer> hseDPks = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site,
                SiteRolesEnum.HSEDirector);
        // get the roles related to NCR for the user.
        List<Integer> hseCPks = new AuthorizationDelegate().getEntitiesWithRole(context, EntityTypeEnum.Site,
                SiteRolesEnum.HSECoordinator);
        List<Integer> sitePks = new ArrayList<Integer>();
        if (hseDPks != null)
        {
            sitePks.addAll(hseDPks);
        }
        if (hseCPks != null)
        {
            sitePks.addAll(hseCPks);
        }

        List<Project> projects = ProjectDelegate.getProjectsWhereTheUserIsReadOnly(context);
        if ((sitePks == null || sitePks.size() == 0) && (projects == null || projects.size() == 0))
        {
            sqlQuery.append(" and injury.pk = -1 "); // items to be displayed.
            return;
        }

        sqlQuery.append(" and ("); // permission based AND start.

        String permissionOrSep = "";
        if (sitePks != null && sitePks.size() > 0)
        {
            sqlQuery.append(permissionOrSep);
            sqlQuery.append(" injury.sitePk in (");
            String sep = "";
            for (Iterator iterator = sitePks.iterator(); iterator.hasNext();)
            {
                Integer aSitePk = (Integer) iterator.next();
                sqlQuery.append(sep).append("?");
                params.add(aSitePk);
                sep = ",";
            }
            sqlQuery.append(")");
            permissionOrSep = " or ";
        }
        if (projects != null && projects.size() > 0)
        {
            sqlQuery.append(permissionOrSep);
            sqlQuery.append(" injury.projectPk in (");
            String sep = "";
            for (Iterator iterator = projects.iterator(); iterator.hasNext();)
            {
                Project aProject = (Project) iterator.next();
                sqlQuery.append(sep).append("?");
                params.add(aProject.getPk());
                sep = ",";
            }
            sqlQuery.append(")");
        }

        sqlQuery.append(") "); // permission based AND end.

    }

}

