package com.tathvatech.injuryReport.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.user.OID.InjuryAfterTreatmentOID;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.DateRangeFilter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InjuryReportQueryFilter extends ReportFilter implements Cloneable
{
    public static enum SortOrder {
        Asc, Desc, TopDescAsc
    };

    public static enum FilterModeEnum {
        SelectAllOnNoSelect, SelectNoneOnNoSelect
    };



    private FilterModeEnum filterMode = FilterModeEnum.SelectAllOnNoSelect;
    private int pk;
    private List<OID> locations;
    private int workstationPk;
    private List<Integer> sitePks;
    private String injuryReportNo;
    private String injuredPerson;
    private Boolean reportingRequired;
    private Boolean workRelated;
    private List<String> statusList;
    private Integer supervisor;
    private DateRangeFilter injuredDate;
    private DateRangeFilter createdDate;
    private DateRangeFilter verifiedDate;
    private DateRangeFilter closedDate;
    private List<InjuryAfterTreatmentOID> afterTreatmentList;
    SortOrder sortOrder;
    // private int projectPk;
    private List<Integer> projectPks;
    private String[] natureOfInjury;
    private InjuryFilter.GraphType graphType = InjuryFilter.GraphType.lcation;
    private Integer typeOfInjury;
    private Integer typeOfPerson;
    private LimitObject pendingVerificationLimit;

    public InjuryReportQueryFilter()
    {

    }

    public InjuryReportQueryFilter(List<Integer> sitePks, String injuryReportNo, String injuredPerson,
                                   List<OID> locations, int workstationPk, Boolean reportingRequired, List<String> statusList,
                                   Integer supervisor, DateRangeFilter createdDate,DateRangeFilter verifiedDate,DateRangeFilter closedDate, DateRangeFilter injuredDate,
                                   List<InjuryAfterTreatmentOID> afterTreatmentList, Integer typeOfInjury, Integer typeOfPerson,
                                   List<Integer> projectPks)
    {

        this.sitePks = sitePks;
        this.injuryReportNo = injuryReportNo;
        this.injuredPerson = injuredPerson;
        this.locations = locations;
        this.workstationPk = workstationPk;
        this.reportingRequired = reportingRequired;
        this.statusList = statusList;
        this.supervisor = supervisor;
        this.createdDate = createdDate;
        this.verifiedDate=verifiedDate;
        this.closedDate=closedDate;
        this.injuredDate = injuredDate;
        this.afterTreatmentList = afterTreatmentList;
        this.typeOfInjury = typeOfInjury;
        this.typeOfPerson = typeOfPerson;
        this.projectPks = projectPks;
    }

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getInjuryReportNo()
    {
        return injuryReportNo;
    }

    public void setInjuryReportNo(String injuryReportNo)
    {
        this.injuryReportNo = injuryReportNo;
    }

    public String getInjuredPerson()
    {
        return injuredPerson;
    }

    public void setInjuredPerson(String injuredPerson)
    {
        this.injuredPerson = injuredPerson;
    }

    public Boolean getReportingRequired()
    {
        return reportingRequired;
    }

    public void setReportingRequired(Boolean reportingRequired)
    {
        this.reportingRequired = reportingRequired;
    }

    public Boolean getWorkRelated()
    {
        return workRelated;
    }

    public void setWorkRelated(Boolean workRelated)
    {
        this.workRelated = workRelated;
    }

    public List<String> getStatusList()
    {
        return statusList;
    }

    public void setStatusList(List<String> statusList)
    {
        this.statusList = statusList;
    }

    public Integer getSupervisor()
    {
        return supervisor;
    }

    public void setSupervisor(Integer supervisor)
    {
        this.supervisor = supervisor;
    }

    public DateRangeFilter getInjuredDate()
    {
        return injuredDate;
    }

    public void setInjuredDate(DateRangeFilter injuredDate)
    {
        this.injuredDate = injuredDate;
    }

    public DateRangeFilter getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(DateRangeFilter createdDate)
    {
        this.createdDate = createdDate;
    }

    public List<InjuryAfterTreatmentOID> getAfterTreatmentList()
    {
        return afterTreatmentList;
    }

    public void setAfterTreatmentList(List<InjuryAfterTreatmentOID> afterTreatmentList)
    {
        this.afterTreatmentList = afterTreatmentList;
    }

    public SortOrder getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public List<OID> getLocations()
    {
        return locations;
    }

    public void setLocations(List<OID> locations)
    {
        this.locations = locations;
    }

    public int getWorkstationPk()
    {
        return workstationPk;
    }

    public void setWorkstationPk(int workstationPk)
    {
        this.workstationPk = workstationPk;
    }

    public List<Integer> getSitePks()
    {
        return sitePks;
    }

    public void setSitePks(List<Integer> sitePks)
    {
        this.sitePks = sitePks;
    }

    public FilterModeEnum getFilterMode()
    {
        return filterMode;
    }

    public void setFilterMode(FilterModeEnum filterMode)
    {
        this.filterMode = filterMode;
    }

    // public int getProjectPk()
    // {
    // return projectPk;
    // }
    //
    // public void setProjectPk(int projectPk)
    // {
    // this.projectPk = projectPk;
    // }

    public String[] getNatureOfInjury()
    {
        return natureOfInjury;
    }

    public List<Integer> getProjectPks()
    {
        return projectPks;
    }

    public void setProjectPks(List<Integer> projectPks)
    {
        this.projectPks = projectPks;
    }

    public void setNatureOfInjury(String[] natureOfInjury)
    {
        this.natureOfInjury = natureOfInjury;
    }

    public InjuryFilter.GraphType getGraphType()
    {
        return graphType;
    }

    public void setGraphType(InjuryFilter.GraphType graphType)
    {
        this.graphType = graphType;
    }

    public Integer getTypeOfInjury()
    {
        return typeOfInjury;
    }

    public void setTypeOfInjury(Integer typeOfInjury)
    {
        this.typeOfInjury = typeOfInjury;
    }

    public Integer getTypeOfPerson()
    {
        return typeOfPerson;
    }

    public void setTypeOfPerson(Integer typeOfPerson)
    {
        this.typeOfPerson = typeOfPerson;
    }

    public LimitObject getPendingVerificationLimit()
    {
        return pendingVerificationLimit;
    }

    public void setPendingVerificationLimit(LimitObject pendingVerificationLimit)
    {
        this.pendingVerificationLimit = pendingVerificationLimit;
    }

    public DateRangeFilter getVerifiedDate()
    {
        return verifiedDate;
    }

    public void setVerifiedDate(DateRangeFilter verifiedDate)
    {
        this.verifiedDate = verifiedDate;
    }

    public DateRangeFilter getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate(DateRangeFilter closedDate)
    {
        this.closedDate = closedDate;
    }

}

