package com.tathvatech.ncr.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.DateRangeFilter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NcrCorrectiveActionReportFilter  extends ReportFilter
{
    private int pk;
    private String ncrNo;
    private String recommendedActions;
    private List<NcrEnum.NcrCorrectiveStatusEnum> status;
    private List<PartOID> partOIDs;
    private List<SupplierOID> supplierOIDs;
    private List<ProjectOID> projectOIDs;
    private List<SiteOID> siteOIDs;
    private DateRangeFilter forecastStartDate;
    private DateRangeFilter forecastCompletionDate;
    private List<UserOID> custodianOIDs;
    private List<UserOID> actionCompletionWaitingFor;
    private List<UserOID> activeActionsCompletionWaitingFor;
    private List<UserOID> overduedActionsCompletionWaitingFor;
    private List<Integer> d8Stages;

    public NcrCorrectiveActionReportFilter() {

    }
    public int getPk()
    {
        return pk;
    }
    public void setPk(int pk)
    {
        this.pk = pk;
    }
    public String getNcrNo()
    {
        return ncrNo;
    }
    public void setNcrNo(String ncrNo)
    {
        this.ncrNo = ncrNo;
    }
    public String getRecommendedActions()
    {
        return recommendedActions;
    }
    public void setRecommendedActions(String recommendedActions)
    {
        this.recommendedActions = recommendedActions;
    }


    public DateRangeFilter getForecastStartDate()
    {
        return forecastStartDate;
    }
    public void setForecastStartDate(DateRangeFilter forecastStartDate)
    {
        this.forecastStartDate = forecastStartDate;
    }
    public DateRangeFilter getForecastCompletionDate()
    {
        return forecastCompletionDate;
    }
    public void setForecastCompletionDate(DateRangeFilter forecastCompletionDate)
    {
        this.forecastCompletionDate = forecastCompletionDate;
    }
    public List<UserOID> getCustodianOIDs()
    {
        return custodianOIDs;
    }
    public void setCustodianOIDs(List<UserOID> custodianOIDs)
    {
        this.custodianOIDs = custodianOIDs;
    }
    public List<UserOID> getActionCompletionWaitingFor()
    {
        return actionCompletionWaitingFor;
    }
    public void setActionCompletionWaitingFor(List<UserOID> actionCompletionWaitingFor)
    {
        this.actionCompletionWaitingFor = actionCompletionWaitingFor;
    }
    public List<UserOID> getActiveActionsCompletionWaitingFor()
    {
        return activeActionsCompletionWaitingFor;
    }
    public void setActiveActionsCompletionWaitingFor(List<UserOID> activeActionsCompletionWaitingFor)
    {
        this.activeActionsCompletionWaitingFor = activeActionsCompletionWaitingFor;
    }
    public List<UserOID> getOverduedActionsCompletionWaitingFor()
    {
        return overduedActionsCompletionWaitingFor;
    }
    public void setOverduedActionsCompletionWaitingFor(List<UserOID> overduedActionsCompletionWaitingFor)
    {
        this.overduedActionsCompletionWaitingFor = overduedActionsCompletionWaitingFor;
    }
    public List<PartOID> getPartOIDs()
    {
        return partOIDs;
    }
    public void setPartOIDs(List<PartOID> partOIDs)
    {
        this.partOIDs = partOIDs;
    }
    public List<SupplierOID> getSupplierOIDs()
    {
        return supplierOIDs;
    }
    public void setSupplierOIDs(List<SupplierOID> supplierOIDs)
    {
        this.supplierOIDs = supplierOIDs;
    }
    public List<ProjectOID> getProjectOIDs()
    {
        return projectOIDs;
    }
    public void setProjectOIDs(List<ProjectOID> projectOIDs)
    {
        this.projectOIDs = projectOIDs;
    }
    public List<SiteOID> getSiteOIDs()
    {
        return siteOIDs;
    }
    public void setSiteOIDs(List<SiteOID> siteOIDs)
    {
        this.siteOIDs = siteOIDs;
    }
    public List<NcrEnum.NcrCorrectiveStatusEnum> getStatus()
    {
        return status;
    }
    public void setStatus(List<NcrEnum.NcrCorrectiveStatusEnum> status)
    {
        this.status = status;
    }
    public List<Integer> getD8Stages()
    {
        return d8Stages;
    }
    public void setD8Stages(List<Integer> d8Stages)
    {
        this.d8Stages = d8Stages;
    }




}

