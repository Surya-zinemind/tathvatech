package com.tathvatech.ncr.report;

import com.tathvatech.ncr.common.NcrAreaOfResponsibilityBean;
import com.tathvatech.ncr.common.NcrFailureCodeMasterBean;
import com.tathvatech.ncr.entity.NcrDispositionTypes;
import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.report.request.ReportFilter;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.DateRangeFilter;

import java.util.List;

public class NcrItemListReportFilter  extends ReportFilter
{
    public static enum SortOrder {Asc, Desc};
    public static enum DispositionIsSet{Set, NotSet}

    private int pk;
    private Integer ncrGroupFk;
    private List<ProjectOID> projectOIDs;
    private List<SiteOID> siteOIDs;
    private List<UnitOID> unitOIDs;
    private String ncrNo;
    private List<NcrEnum.NcrGroupStatus> ncrGroupStatus;
    List<NcrEnum.NcrItemStatus> ncrItemStatusList;
    private List< NcrEnum.NcrGroupStatus> ncrGroupStatusList;
    private DateRangeFilter createdDate;
    private DateRangeFilter publishedDate;
    private DateRangeFilter approvedDate;
    private DateRangeFilter closedDate;
    private String ncrGroupDesc;
    private List<PartOID> partOIDs;
    private List<SupplierOID> supplierOIDs;
    private List<UserOID> custodianOIDs;
    private UserOID createdByOID;
    private UserOID publishedByOID;
    private DispositionIsSet dispositionIsSet;
    private List<NcrDispositionTypes> dispositions;
    private NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean;
    private NcrFailureCodeMasterBean ncrFailureCodeMasterBean ;
    private Object location;
    private Object source;
    private ReworkOrderOID reworkorderOID;
    private List<UserOID> functionApprovalPendingFor;
    private List<UserOID> specialProcessPendingFor;
    private boolean showApprovalsPendingForUserTeam; // if this is true, show pending approvals for the function team of the functionApprovalPendingUsers.
    private Boolean pps8DRequired;

    SortOrder sortOrder;

    public NcrItemListReportFilter() {

    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
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

    public DateRangeFilter getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(DateRangeFilter createdDate)
    {
        this.createdDate = createdDate;
    }

    public DateRangeFilter getPublishedDate()
    {
        return publishedDate;
    }

    public void setPublishedDate(DateRangeFilter publishedDate)
    {
        this.publishedDate = publishedDate;
    }

    public DateRangeFilter getApprovedDate()
    {
        return approvedDate;
    }

    public void setApprovedDate(DateRangeFilter approvedDate)
    {
        this.approvedDate = approvedDate;
    }

    public DateRangeFilter getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate(DateRangeFilter closedDate)
    {
        this.closedDate = closedDate;
    }

    public String getNcrNo()
    {
        return ncrNo;
    }

    public void setNcrNo(String ncrNo)
    {
        this.ncrNo = ncrNo;
    }

    public List<NcrEnum.NcrGroupStatus> getNcrGroupStatus()
    {
        return ncrGroupStatus;
    }

    public void setNcrGroupStatus(List<NcrEnum.NcrGroupStatus> ncrGroupStatus)
    {
        this.ncrGroupStatus = ncrGroupStatus;
    }

    public String getNcrGroupDesc() {
        return ncrGroupDesc;
    }

    public void setNcrGroupDesc(String ncrGroupDesc) {
        this.ncrGroupDesc = ncrGroupDesc;
    }

    public List<SupplierOID> getSupplierOIDs()
    {
        return supplierOIDs;
    }

    public void setSupplierOIDs(List<SupplierOID> supplierOIDs)
    {
        this.supplierOIDs = supplierOIDs;
    }

    public List<PartOID> getPartOIDs()
    {
        return partOIDs;
    }

    public void setPartOIDs(List<PartOID> partOIDs)
    {
        this.partOIDs = partOIDs;
    }

    public List<NcrEnum.NcrItemStatus> getNcrItemStatusList()
    {
        return ncrItemStatusList;
    }

    public void setNcrItemStatusList(List<NcrEnum.NcrItemStatus> ncrItemStatusList)
    {
        this.ncrItemStatusList = ncrItemStatusList;
    }

    public List<NcrEnum.NcrGroupStatus> getNcrGroupStatusList()
    {
        return ncrGroupStatusList;
    }

    public void setNcrGroupStatusList(List<NcrEnum.NcrGroupStatus> ncrGroupStatusList)
    {
        this.ncrGroupStatusList = ncrGroupStatusList;
    }

    public UserOID getCreatedByOID()
    {
        return createdByOID;
    }

    public void setCreatedByOID(UserOID createdByOID)
    {
        this.createdByOID = createdByOID;
    }

    public UserOID getPublishedByOID()
    {
        return publishedByOID;
    }

    public void setPublishedByOID(UserOID publishedByOID)
    {
        this.publishedByOID = publishedByOID;
    }

    public DispositionIsSet getDispositionIsSet()
    {
        return dispositionIsSet;
    }

    public void setDispositionIsSet(DispositionIsSet dispositionIsSet)
    {
        this.dispositionIsSet = dispositionIsSet;
    }

    public List<NcrDispositionTypes> getDispositions()
    {
        return dispositions;
    }

    public void setDispositions(List<NcrDispositionTypes> dispositions)
    {
        this.dispositions = dispositions;
    }

    public List<UserOID> getCustodianOIDs()
    {
        return custodianOIDs;
    }

    public void setCustodianOIDs(List<UserOID> custodianOIDs)
    {
        this.custodianOIDs = custodianOIDs;
    }

    public NcrAreaOfResponsibilityBean getNcrAreaOfResponsibilityBean()
    {
        return ncrAreaOfResponsibilityBean;
    }

    public void setNcrAreaOfResponsibilityBean(NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean)
    {
        this.ncrAreaOfResponsibilityBean = ncrAreaOfResponsibilityBean;
    }

    public NcrFailureCodeMasterBean getNcrFailureCodeMasterBean()
    {
        return ncrFailureCodeMasterBean;
    }

    public void setNcrFailureCodeMasterBean(NcrFailureCodeMasterBean ncrFailureCodeMasterBean)
    {
        this.ncrFailureCodeMasterBean = ncrFailureCodeMasterBean;
    }

    public Boolean getPps8DRequired()
    {
        return pps8DRequired;
    }

    public void setPps8DRequired(Boolean pps8dRequired)
    {
        pps8DRequired = pps8dRequired;
    }

    public List<UnitOID> getUnitOIDs()
    {
        return unitOIDs;
    }

    public void setUnitOIDs(List<UnitOID> unitOIDs)
    {
        this.unitOIDs = unitOIDs;
    }

    public List<UserOID> getFunctionApprovalPendingFor()
    {
        return functionApprovalPendingFor;
    }

    public void setFunctionApprovalPendingFor(List<UserOID> functionApprovalPendingFor)
    {
        this.functionApprovalPendingFor = functionApprovalPendingFor;
    }

    public boolean getShowApprovalsPendingForUserTeam()
    {
        return showApprovalsPendingForUserTeam;
    }

    public void setShowApprovalsPendingForUserTeam(boolean showApprovalsPendingForUserTeam)
    {
        this.showApprovalsPendingForUserTeam = showApprovalsPendingForUserTeam;
    }

    public Integer getNcrGroupFk()
    {
        return ncrGroupFk;
    }

    public void setNcrGroupFk(Integer ncrGroupFk)
    {
        this.ncrGroupFk = ncrGroupFk;
    }

    public Object getLocation()
    {
        return location;
    }

    public void setLocation(Object location)
    {
        this.location = location;
    }

    public Object getSource()
    {
        return source;
    }

    public void setSource(Object source)
    {
        this.source = source;
    }

    public ReworkOrderOID getReworkorderOID()
    {
        return reworkorderOID;
    }

    public void setReworkorderOID(ReworkOrderOID reworkorderOID)
    {
        this.reworkorderOID = reworkorderOID;
    }

    public List<UserOID> getSpecialProcessPendingFor()
    {
        return specialProcessPendingFor;
    }

    public void setSpecialProcessPendingFor(List<UserOID> specialProcessPendingFor)
    {
        this.specialProcessPendingFor = specialProcessPendingFor;
    }


}

