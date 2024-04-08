package com.tathvatech.ncr.common;

import com.tathvatech.ncr.enums.NcrEnum;
import com.tathvatech.user.OID.OID;
import com.tathvatech.user.OID.ReworkOrderOID;

import java.util.Date;
import java.util.List;



public class NcrItemQueryFilter
{
	public static enum SortOrder {
		Asc, Desc, TopDescAsc
	};
	
//	public static enum GroupBy {
//		WEEK, MONTH, YEAR,CUSTODIAN
//	}
	public static enum GroupBy {
		WEEKNO("Week"), YEAR("Year"), MONTH("Month"), CUSTODIANPK("Custodian");
		private final String displayName;

		GroupBy(String name)
		{
			this.displayName = name;
		}

		public String displayName()
		{
			return this.displayName;
		}

		public static GroupBy fromDisplayString(String displayString)
		{
			GroupBy[] vals = GroupBy.values();
			for (int i = 0; i < vals.length; i++)
			{
				if (vals[i].displayName.equalsIgnoreCase(displayString))
					return vals[i];
			}
			return null;
		}

		public static String getEnumByString(String code)
		{
			for (SplitBy e : SplitBy.values())
			{
				if (code.equals(e.name()))
					return e.displayName();
			}
			return null;
		}
	}
	public static enum SplitBy {
		DISPOSITIONFK("Disposition"), STATUS("Status"), AREAOFRESPONSIBILITYFK("Area of Responsibility"), SOURCE("Source");
		private final String displayName;

		SplitBy(String name)
		{
			this.displayName = name;
		}

		public String displayName()
		{
			return this.displayName;
		}

		public static SplitBy fromDisplayString(String displayString)
		{
			SplitBy[] vals = SplitBy.values();
			for (int i = 0; i < vals.length; i++)
			{
				if (vals[i].displayName.equalsIgnoreCase(displayString))
					return vals[i];
			}
			return null;
		}

		public static String getEnumByString(String code)
		{
			for (SplitBy e : SplitBy.values())
			{
				if (code.equals(e.name()))
					return e.displayName();
			}
			return null;
		}
	}
	public static enum GroupSet {
		PROJECT, DISPOSITION, PART,UNIT,SERIALNO,REWORKORDER,CUSTODIAN,SUPPLIER,AREAOFRESPONSIBILITY,
		APPROVEDGRAPH,PUBLISHEDGRAPH,CREATEDGRAPH,CLOSEDGRAPH
	};
	private int pk;
	private Integer ncrGroupFk;
	private Integer partFk;
	private Integer dispositionFk;
	private String custodian;
	private NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean;
	private NcrFailureCodeMasterBean ncrFailureCodeMasterBean;
    private Boolean isApprovalWaiting;
	private String createdByName;
	private String publishedByName;
	private String ncrNo;
	private List<Integer> projectPks;
	private List<Integer> sitePks;
	private String ncrGroupNo;
	private Date createdDateFrom;
	private Date createdDateTo;
	private String ncrGroupDesc;
	private String partName;
	private String supplierName;
	List<NcrEnum.NcrItemStatus> ncrItemStatusList;
	private List< NcrEnum.NcrGroupStatus> ncrGroupStatusList;
	private Boolean isPendingMyApproval;
	private Boolean pps8DRequired;
    private String ncrFunctionGraphViewMode;
	private List<Integer> functionMasterPks;
	private String[] orderByFilter;
	GroupSet groupSetFilter;
	private ReworkOrderOID reworkOrderOID;
	private Boolean isDispositionNull;
	@Deprecated
	private Object location;
	@Deprecated
	private Object source;
	
	private List<OID> locations;
	private List<OID> sources;
	
	
	
	private GroupBy groupBy;
	private SplitBy splitBy;
	private List<Integer> custodianPks;
	private List<Integer> dispositionPks;
	private List<Integer> partPks;
	private List<Integer> supplierPks; // added for NCR Graphical Reports
    private Object timeToPublish;
    private Object timeToDispositionApproval;
    private Object timeToClosed;
    private List<Integer> unitFks;
    private boolean isOpenItemInfoOnly=false;
    private boolean showResourceInfo=false;
	public NcrItemQueryFilter(){
		
	}

	public NcrItemQueryFilter(Integer ncrGroupFk ,List<NcrEnum.NcrItemStatus> ncrStatusList,Integer dispositionFk,String custodian,NcrAreaOfResponsibilityBean ncrAreaOfResponsibilityBean,NcrFailureCodeMasterBean ncrFailureCodeMasterBean,
			String createdByName,String publishedByName,Boolean isApprovalWaiting,List<NcrEnum.NcrGroupStatus> ncrGroupStatusList,Boolean isPendingMyApproval,Boolean pps8DRequired,List<Integer> projectPks,List<Integer> sitePks,Date createdDateFrom,Date createdDateTo) {
        this.ncrGroupFk =ncrGroupFk;
        this.ncrItemStatusList = ncrStatusList;
		this.dispositionFk =dispositionFk;
		this.custodian=custodian;
		this.ncrAreaOfResponsibilityBean =ncrAreaOfResponsibilityBean;
		this.ncrFailureCodeMasterBean =ncrFailureCodeMasterBean;
		this.createdByName =createdByName;
		this.publishedByName= publishedByName;
		this.isApprovalWaiting= isApprovalWaiting;
		this.ncrGroupStatusList = ncrGroupStatusList;
		this.isPendingMyApproval =isPendingMyApproval;
		this.pps8DRequired=pps8DRequired;
		this.projectPks = projectPks;
		this.sitePks=sitePks;
		this.createdDateFrom=createdDateFrom;
		this.createdDateTo= createdDateTo;
	}

	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public Integer getNcrGroupFk()
	{
		return ncrGroupFk;
	}
	public void setNcrGroupFk(Integer ncrGroupFk)
	{
		this.ncrGroupFk = ncrGroupFk;
	}
	
	public Integer getPartFk()
	{
		return partFk;
	}
	public void setPartFk(Integer partFk)
	{
		this.partFk = partFk;
	}

	public Integer getDispositionFk()
	{
		return dispositionFk;
	}

	public void setDispositionFk(Integer dispositionFk)
	{
		this.dispositionFk = dispositionFk;
	}

	public String getCustodian()
	{
		return custodian;
	}

	public void setCustodian(String custodian)
	{
		this.custodian = custodian;
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

	public String getCreatedByName()
	{
		return createdByName;
	}

	public void setCreatedByName(String createdByName)
	{
		this.createdByName = createdByName;
	}

	public String getPublishedByName()
	{
		return publishedByName;
	}

	public void setPublishedByName(String publishedByName)
	{
		this.publishedByName = publishedByName;
	}

	public Boolean getIsApprovalWaiting()
	{
		return isApprovalWaiting;
	}

	public void setIsApprovalWaiting(Boolean isApprovalWaiting)
	{
		this.isApprovalWaiting = isApprovalWaiting;
	}

	public List<Integer> getProjectPks()
	{
		return projectPks;
	}

	public void setProjectPks(List<Integer> projectPks)
	{
		this.projectPks = projectPks;
	}

	public List<Integer> getSitePks()
	{
		return sitePks;
	}

	public void setSitePks(List<Integer> sitePks)
	{
		this.sitePks = sitePks;
	}

	public String getNcrGroupNo()
	{
		return ncrGroupNo;
	}

	public void setNcrGroupNo(String ncrGroupNo)
	{
		this.ncrGroupNo = ncrGroupNo;
	}

	public List<NcrEnum.NcrGroupStatus> getNcrGroupStatusList()
	{
		return ncrGroupStatusList;
	}

	public void setNcrGroupStatusList(List<NcrEnum.NcrGroupStatus> ncrGroupStatusList)
	{
		this.ncrGroupStatusList = ncrGroupStatusList;
	}

	public Date getCreatedDateFrom()
	{
		return createdDateFrom;
	}

	public void setCreatedDateFrom(Date createdDateFrom)
	{
		this.createdDateFrom = createdDateFrom;
	}

	public Date getCreatedDateTo()
	{
		return createdDateTo;
	}

	public void setCreatedDateTo(Date createdDateTo)
	{
		this.createdDateTo = createdDateTo;
	}

	public String getNcrGroupDesc()
	{
		return ncrGroupDesc;
	}

	public void setNcrGroupDesc(String ncrGroupDesc)
	{
		this.ncrGroupDesc = ncrGroupDesc;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public List<NcrEnum.NcrItemStatus> getNcrItemStatusList()
	{
		return ncrItemStatusList;
	}

	public void setNcrItemStatusList(List<NcrEnum.NcrItemStatus> ncrItemStatusList)
	{
		this.ncrItemStatusList = ncrItemStatusList;
	}

	public String getNcrNo()
	{
		return ncrNo;
	}

	public void setNcrNo(String ncrNo)
	{
		this.ncrNo = ncrNo;
	}

	public Boolean getIsPendingMyApproval()
	{
		return isPendingMyApproval;
	}

	public void setIsPendingMyApproval(Boolean isPendingMyApproval)
	{
		this.isPendingMyApproval = isPendingMyApproval;
	}

	public Boolean getPps8DRequired()
	{
		return pps8DRequired;
	}

	public void setPps8DRequired(Boolean pps8dRequired)
	{
		pps8DRequired = pps8dRequired;
	}

	public String getNcrFunctionGraphViewMode()
	{
		return ncrFunctionGraphViewMode;
	}

	public void setNcrFunctionGraphViewMode(String ncrFunctionGraphViewMode)
	{
		this.ncrFunctionGraphViewMode = ncrFunctionGraphViewMode;
	}

	public List<Integer> getFunctionMasterPks()
	{
		return functionMasterPks;
	}

	public void setFunctionMasterPks(List<Integer> functionMasterPks)
	{
		this.functionMasterPks = functionMasterPks;
	}

	public String[] getOrderByFilter()
	{
		return orderByFilter;
	}

	public void setOrderByFilter(String[] orderByFilter)
	{
		this.orderByFilter = orderByFilter;
	}

	public GroupSet getGroupSetFilter()
	{
		return groupSetFilter;
	}

	public void setGroupSetFilter(GroupSet groupSetFilter)
	{
		this.groupSetFilter = groupSetFilter;
	}

	public ReworkOrderOID getReworkOrderOID()
	{
		return reworkOrderOID;
	}

	public void setReworkOrderOID(ReworkOrderOID reworkOrderOID)
	{
		this.reworkOrderOID = reworkOrderOID;
	}

	public Boolean getIsDispositionNull()
	{
		return isDispositionNull;
	}

	public void setIsDispositionNull(Boolean isDispositionNull)
	{
		this.isDispositionNull = isDispositionNull;
	}
	@Deprecated
	public Object getLocation()
	{
		return location;
	}
	@Deprecated
	public void setLocation(Object location)
	{
		this.location = location;
	}
	@Deprecated
	public Object getSource()
	{
		return source;
	}
	@Deprecated
	public void setSource(Object source)
	{
		this.source = source;
	}

	public GroupBy getGroupBy()
	{
		return groupBy;
	}

	public void setGroupBy(GroupBy groupBy)
	{
		this.groupBy = groupBy;
	}

	public SplitBy getSplitBy()
	{
		return splitBy;
	}

	public void setSplitBy(SplitBy splitBy)
	{
		this.splitBy = splitBy;
	}

	public List<Integer> getCustodianPks()
	{
		return custodianPks;
	}

	public void setCustodianPks(List<Integer> custodianPks)
	{
		this.custodianPks = custodianPks;
	}

	public List<Integer> getDispositionPks()
	{
		return dispositionPks;
	}

	public void setDispositionPks(List<Integer> dispositionPks)
	{
		this.dispositionPks = dispositionPks;
	}

	public List<Integer> getPartPks()
	{
		return partPks;
	}

	public void setPartPks(List<Integer> partPks)
	{
		this.partPks = partPks;
	}

	public List<Integer> getSupplierPks()
	{
		return supplierPks;
	}

	public void setSupplierPks(List<Integer> supplierPks)
	{
		this.supplierPks = supplierPks;
	}

	public Object getTimeToPublish()
	{
		return timeToPublish;
	}

	public void setTimeToPublish(Object timeToPublish)
	{
		this.timeToPublish = timeToPublish;
	}

	public Object getTimeToDispositionApproval()
	{
		return timeToDispositionApproval;
	}

	public void setTimeToDispositionApproval(Object timeToDispositionApproval)
	{
		this.timeToDispositionApproval = timeToDispositionApproval;
	}

	public Object getTimeToClosed()
	{
		return timeToClosed;
	}

	public void setTimeToClosed(Object timeToClosed)
	{
		this.timeToClosed = timeToClosed;
	}

	public List<Integer> getUnitFks()
	{
		return unitFks;
	}

	public void setUnitFks(List<Integer> unitFks)
	{
		this.unitFks = unitFks;
	}

	public boolean isOpenItemInfoOnly()
	{
		return isOpenItemInfoOnly;
	}

	public void setOpenItemInfoOnly(boolean isOpenItemInfoOnly)
	{
		this.isOpenItemInfoOnly = isOpenItemInfoOnly;
	}

	public List<OID> getLocations()
	{
		return locations;
	}

	public void setLocations(List<OID> locations)
	{
		this.locations = locations;
	}

	public List<OID> getSources()
	{
		return sources;
	}

	public void setSources(List<OID> sources)
	{
		this.sources = sources;
	}

	public boolean getShowResourceInfo()
	{
		return showResourceInfo;
	}

	public void setShowResourceInfo(boolean showResourceInfo)
	{
		this.showResourceInfo = showResourceInfo;
	}

	
	
	
}
