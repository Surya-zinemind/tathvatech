package com.tathvatech.timetracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sarvasutra.etest.components.bocomponents.DateRangeFilter;
import com.sarvasutra.etest.util.ListStringUtil;
import com.tathvatech.testsutra.ncr.common.NcrItemOID;
import com.tathvatech.testsutra.ncr.common.NcrUnitAssignOID;
import com.tathvatech.testsutra.openitemv2.common.OpenItemOID;
import com.tathvatech.testsutra.timetrack.api.WorkOrderItemResponseBean;
import com.tathvatech.testsutra.timetrack.api.WorkOrderUserBean;
import com.tathvatech.testsutra.timetrack.common.TimeQualifierMaster;
import com.tathvatech.testsutra.timetrack.common.TimeTypeEnum;
import com.tathvatech.testsutra.timetrack.common.WorkorderRequestBean;
import com.tathvatech.testsutra.timetrack.reports.opencheckinlistreport.OpenCheckinListReport;
import com.tathvatech.testsutra.timetrack.reports.opencheckinlistreport.OpenCheckinListReportRequest;
import com.tathvatech.testsutra.timetrack.reports.opencheckinlistreport.OpenCheckinListReportResultRow;
import com.tathvatech.testsutra.timetrack.reports.timelogsummary.TimeLogSummaryReport;
import com.tathvatech.testsutra.timetrack.reports.timelogsummary.TimeLogSummaryReportRequest;
import com.tathvatech.testsutra.timetrack.reports.timelogsummary.TimeLogSummaryReportRequest.GroupingCol;
import com.tathvatech.testsutra.timetrack.reports.timelogsummary.TimeLogSummaryReportResultRow;
import com.tathvatech.testsutra.timetrack.reports.timelogsummary.TimeSlotTypeEnum;
import com.tathvatech.testsutra.timetrack.reports.workorderlistreport.WorkorderListReportFilter;
import com.tathvatech.testsutra.timetrack.reports.workorderlistreport.WorkorderListReportResultRow;
import com.tathvatech.testsutra.vcr.common.VcrOID;
import com.tathvatech.ts.caf.ApplicationProperties;
import com.tathvatech.ts.caf.core.exception.AppException;
import com.tathvatech.ts.caf.db.PersistWrapper;
import com.tathvatech.ts.core.UserContext;
import com.tathvatech.ts.core.accounts.UserOID;
import com.tathvatech.ts.core.common.EStatusEnum;
import com.tathvatech.ts.core.common.EntityTypeEnum;
import com.tathvatech.ts.core.common.OID;
import com.tathvatech.ts.core.common.ReworkOrderOID;
import com.tathvatech.ts.core.common.WorkItem;
import com.tathvatech.ts.core.project.ModeOID;
import com.tathvatech.ts.core.project.TestProcOID;
import com.tathvatech.ts.core.project.TestProcSectionOID;
import com.tathvatech.ts.report.ReportTypes;
import com.thirdi.surveyside.reportv2.ReportRequest;
import com.thirdi.surveyside.reportv2.ReportsDelegate;

/**
 * @author Hari
 *
 * Currently work order is supported for types testproc, ncr, ncrUnitAssign, vcr, Mod, openitems.
 */
public class WorkorderManager
{
	public static Logger logger = Logger.getLogger(WorkorderManager.class);
	
	public static void createWorkorder(UserContext context, WorkorderRequestBean requestBean) throws Exception
	{
		if(!(EntityTypeEnum.TestProc == requestBean.getEntityOID().getEntityType() 
				|| EntityTypeEnum.TestProcSection == requestBean.getEntityOID().getEntityType()
				|| EntityTypeEnum.NCR == requestBean.getEntityOID().getEntityType()
				|| EntityTypeEnum.NcrUnitAssign == requestBean.getEntityOID().getEntityType()
				|| EntityTypeEnum.VCRItem == requestBean.getEntityOID().getEntityType()
				|| EntityTypeEnum.Mode == requestBean.getEntityOID().getEntityType()
				|| EntityTypeEnum.OpenItem == requestBean.getEntityOID().getEntityType()))
		{
			throw new AppException("Workorders not supported for type " + requestBean.getEntityOID().getEntityType());
		}

		Workorder workorder = getWorkorderForEntity(requestBean.getEntityOID());
		if(workorder != null)
			return; // workorder already exists. so just return;
		
		if(StringUtils.isNotBlank(requestBean.getExtWorkorderNo()))
		{
			workorder = getWorkOrderByWorkOrderNo(requestBean.getExtWorkorderNo());
			if(workorder == null)
			{
				workorder = createWorkorderEntity(context, requestBean);
			}
			else
			{
				throw new AppException("Workorder number is already assigned. Please provide a unique workorder no.");
			}
		}
		else
		{
			// create workorder with an empty wororder no.
			workorder = createWorkorderEntity(context, requestBean);
		}
	}
	
	public static List<TimeQualifierMaster> getTimeQualifierList(TimeTypeEnum timeType)
	{
		return PersistWrapper.readList(TimeQualifierMaster.class, 
				"select * from wo_time_qualifier_master where timeType = ? and estatus = 1", timeType.name());
	}

	protected static Workorder getWorkorder(ReworkOrderOID workorderOID)
	{
		StringBuffer sql = new StringBuffer("select wo.* from workorder wo where wo.pk = ? ");
		
		return PersistWrapper.read(Workorder.class, sql.toString(), workorderOID.getPk());
	}
	
	private static Workorder getWorkOrderByWorkOrderNo(String workorderNo)
	{
		return PersistWrapper.read(Workorder.class, "select * from workorder where (extWorkorderNumber = ?) or (extWorkorderNumber is null and workorderNumber = ? ) ", 
				workorderNo, workorderNo);
	}

	protected static Workorder getWorkorderForEntity(OID entityOID)
	{
		StringBuffer sql = new StringBuffer("select wo.* from workorder wo where wo.entityPk = ? and wo.entityType = ? and wo.estatus = 1 ");
		
		return PersistWrapper.read(Workorder.class, sql.toString(), entityOID.getPk(), entityOID.getEntityType().getValue());
	}

	protected static WorkItem getWorkItem(int entityPk, EntityTypeEnum entityType)
	{
		if(EntityTypeEnum.TestProcSection == entityType)
		{
			return new TestProcSectionOID(entityPk); 
		}
		throw new AppException("Unsupported Entity Type for Workorder.");
	}

	protected OID getEntityOIDForWorkorder(ReworkOrderOID workorderOID)
	{
		Workorder wo = PersistWrapper.readByPrimaryKey(Workorder.class, workorderOID.getPk());
		if(wo == null)
		{
			logger.error(String.format("Workorder for pk:%d not found", workorderOID.getPk()));
			throw new AppException("Invalid workorder");
		}
		if(EntityTypeEnum.TestProc.getValue() == wo.getEntityType())
		{
			return new TestProcOID(wo.getEntityPk()); 
		}
		else if(EntityTypeEnum.TestProcSection.getValue() == wo.getEntityType())
		{
			return new TestProcSectionOID(wo.getEntityPk()); 
		}
		else if(EntityTypeEnum.NCR.getValue() == wo.getEntityType())
		{
			return new NcrItemOID(wo.getEntityPk()); 
		}
		else if(EntityTypeEnum.NcrUnitAssign.getValue() == wo.getEntityType())
		{
			return new NcrUnitAssignOID(wo.getEntityPk()); 
		}
		else if(EntityTypeEnum.VCRItem.getValue() == wo.getEntityType())
		{
			return new VcrOID(wo.getEntityPk()); 
		}
		else if(EntityTypeEnum.Mode.getValue() == wo.getEntityType())
		{
			return new ModeOID(wo.getEntityPk(), null); 
		}
		else if(EntityTypeEnum.OpenItem.getValue() == wo.getEntityType())
		{
			return new OpenItemOID(wo.getEntityPk(), null); 
		}
		throw new AppException("Unsupported Entity Type for Workorder.");
	}

	private static Workorder createWorkorderEntity(UserContext context, WorkorderRequestBean requestBean) throws Exception
	{
		Workorder workorder = new Workorder();
		workorder.setCreatedBy(context.getUser().getPk());
		workorder.setCreatedDate(new Date());
		workorder.setWorkorderNumber(requestBean.getExtWorkorderNo());
		workorder.setEntityPk(requestBean.getEntityOID().getPk());
		workorder.setEntityType(requestBean.getEntityOID().getEntityType().getValue());
		workorder.setEstatus(EStatusEnum.Active.getValue());
		
		int pk = PersistWrapper.createEntity(workorder);
		workorder = PersistWrapper.readByPrimaryKey(Workorder.class, pk);
		workorder.setWorkorderNumber(pk+"");
		PersistWrapper.update(workorder);
		return PersistWrapper.readByPrimaryKey(Workorder.class, pk);
	}
	
	/**
	 * returns the workorder list and the user time logs against each of these workorders within the daterange matching the workorder filter. 
	 * @param timeZone
	 * @param filter find workorders matching this filter.
	 * @param timeComputeDateRange compute the user time logs within this date range.
	 * @return
	 * @throws Exception
	 */
	public static List<WorkOrderItemResponseBean> getWorkOrdersAndTimeLogs(WorkorderListReportFilter filter, DateRangeFilter timeComputeDateRange)throws Exception
	{
		String applicationURL = ApplicationProperties.getProperty("config/server/url");

		
		List<WorkOrderItemResponseBean> returnList = new ArrayList<WorkOrderItemResponseBean>();

		
		HashMap<ReworkOrderOID, List<TimeLogSummaryReportResultRow>> completedTimeSlotTimeMap = new HashMap<ReworkOrderOID, List<TimeLogSummaryReportResultRow>>();
		HashMap<ReworkOrderOID, List<OpenCheckinListReportResultRow>> openTimeSlotTimeMap = new HashMap<ReworkOrderOID, List<OpenCheckinListReportResultRow>>();
		
		filter.setShowActiveWorkordersOnly(true);
		List<WorkorderListReportResultRow> workorders = (List<WorkorderListReportResultRow>) ReportsDelegate.runReport(null, ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.WorkOrderListReport, filter)).getReportData();
		List<ReworkOrderOID> workorderOIDs = new ArrayList<ReworkOrderOID>();
		for (Iterator iterator = workorders.iterator(); iterator.hasNext();)
		{
			WorkorderListReportResultRow workorderListReportResultRow = (WorkorderListReportResultRow) iterator.next();
			workorderOIDs.add(workorderListReportResultRow.getOID());
		}

		
		
		
		if(workorderOIDs.size() > 0)
		{
			// get the completed timeslot summary report for all these work orders. and put them in a HashMap with the workorderOID as the key and the list
			//of completed timeslots as the value.. Basically grouping the completed timeslot rows by workorderOID.
			TimeLogSummaryReportRequest committedTimeLogReq = new TimeLogSummaryReportRequest();
			committedTimeLogReq.setTimeSlotTypes(new TimeSlotTypeEnum[] {TimeSlotTypeEnum.CommittedTimeSlot, TimeSlotTypeEnum.UnCommittedTimeSlot});
			committedTimeLogReq.setTimeTypes(new TimeTypeEnum[]{TimeTypeEnum.Work, TimeTypeEnum.Wait, TimeTypeEnum.Adjustment});
			committedTimeLogReq.setTimeComputeDateRangeFilter(timeComputeDateRange);
			committedTimeLogReq.setWorkorders(workorderOIDs.toArray(new ReworkOrderOID[] {}));
			committedTimeLogReq.setGroupingSet(Arrays.asList(new TimeLogSummaryReportRequest.GroupingCol[] {GroupingCol.Workorder, GroupingCol.User}));
			List<TimeLogSummaryReportResultRow> committedTimesList = new TimeLogSummaryReport(committedTimeLogReq).runReport().getReportResult();
			for (Iterator iterator = committedTimesList.iterator(); iterator.hasNext();)
			{
				TimeLogSummaryReportResultRow timeLogSummaryReportResultRow = (TimeLogSummaryReportResultRow) iterator.next();
				List<TimeLogSummaryReportResultRow> list = completedTimeSlotTimeMap.get(new ReworkOrderOID(timeLogSummaryReportResultRow.getWorkorderPk()));
				if(list == null)
				{
					list = new ArrayList<TimeLogSummaryReportResultRow>();
					completedTimeSlotTimeMap.put(new ReworkOrderOID(timeLogSummaryReportResultRow.getWorkorderPk()), list);
				}
				list.add(timeLogSummaryReportResultRow);
				
			}

			// get the open timeslot summary report for all these work orders. and put them in a HashMap with the workorderOID as the key and the list
			//of completed timeslots as the value.. Basically grouping the completed timeslot rows by workorderOID.
			OpenCheckinListReportRequest openCheckinReq = new OpenCheckinListReportRequest();
			openCheckinReq.setWorkorders(workorderOIDs.toArray(new ReworkOrderOID[] {}));
			List<OpenCheckinListReportResultRow> openCheckInList = new OpenCheckinListReport(openCheckinReq).runReport().getReportResult();
			for (Iterator iterator = openCheckInList.iterator(); iterator.hasNext();)
			{
				OpenCheckinListReportResultRow openCheckinListReportResultRow = (OpenCheckinListReportResultRow) iterator.next();
				List<OpenCheckinListReportResultRow> list = openTimeSlotTimeMap.get(new ReworkOrderOID(openCheckinListReportResultRow.getWorkorderPk()));
				if(list == null)
				{
					list = new ArrayList<OpenCheckinListReportResultRow>();
					openTimeSlotTimeMap.put(new ReworkOrderOID(openCheckinListReportResultRow.getWorkorderPk()), list);
				}
				list.add(openCheckinListReportResultRow);
			}
		}
		
		//now construct the response bean each of which has the workorder detail and the list of users with their completed time timeslot total time and the checkin time.
		//users are added from committed, uncommitted and opencheckin lists.
		for (Iterator iterator = workorders.iterator(); iterator.hasNext();)
		{
			WorkorderListReportResultRow workorderListReportResultRow = (WorkorderListReportResultRow) iterator.next();
			
			WorkOrderItemResponseBean bean = new WorkOrderItemResponseBean();
			bean.setWorkorderPk(workorderListReportResultRow.getPk());
//			bean.setWorkorderNo(workorderListReportResultRow.);
			bean.setWorkItemPk(workorderListReportResultRow.getTestProcSectionPk());
			bean.setWorkItemType(EntityTypeEnum.fromValue(workorderListReportResultRow.getWorkItemType()).name());
			bean.setWorkItemName(workorderListReportResultRow.getSectionNo());
			bean.setWorkItemDescription(workorderListReportResultRow.getSectionDescription());
			bean.setWorkItemGroupPk(workorderListReportResultRow.getTestProcPk());
			bean.setWorkItemGroupName(ListStringUtil.appendIgnoreNulls(" - ", workorderListReportResultRow.getTestName(), workorderListReportResultRow.getFormName()));
			bean.setWorkItemGroupDescription(workorderListReportResultRow.getFormDescription());
			bean.setUnitPk(workorderListReportResultRow.getUnitPk());
			bean.setUnitName(workorderListReportResultRow.getUnitName());
			bean.setUnitDescription(workorderListReportResultRow.getUnitDescription());
			bean.setSerialNo(workorderListReportResultRow.getUnitSerialNumber());
			bean.setProjectPk(workorderListReportResultRow.getProjectPk());
			bean.setProjectName(workorderListReportResultRow.getProjectName());
			bean.setProjectDescription(workorderListReportResultRow.getProjectDescription());
			bean.setWorkstationPk(workorderListReportResultRow.getWorkstationPk());
			bean.setWorkstationName(workorderListReportResultRow.getWorkstationName());
			bean.setWorkstationDescription(workorderListReportResultRow.getWorkstationDescription());
			bean.setAttributionUserFk(workorderListReportResultRow.getAttributionUserFk());
			
			bean.setFormMainPk(workorderListReportResultRow.getFormMainPk());
			bean.setFormPk(workorderListReportResultRow.getFormPk());
			bean.setFormName(workorderListReportResultRow.getFormName());
			bean.setFormDescription(workorderListReportResultRow.getFormDescription());
			bean.setFormResponsibleDivision(workorderListReportResultRow.getFormResponsibleDivision());
			bean.setFormRevision(workorderListReportResultRow.getFormRevision());
			bean.setFormVersion(workorderListReportResultRow.getFormVersion());
			
			
			int totalCompletedTimeForWorkorder = 0;
			
			HashMap<UserOID, WorkOrderUserBean> userBeanMap = new HashMap<UserOID, WorkOrderUserBean>();

			List<TimeLogSummaryReportResultRow> committedList = completedTimeSlotTimeMap.get(new ReworkOrderOID(workorderListReportResultRow.getPk()));
			if(committedList != null)
			{
				for (Iterator iterator2 = committedList.iterator(); iterator2.hasNext();)
				{
					TimeLogSummaryReportResultRow timeLogSummaryReportResultRow = (TimeLogSummaryReportResultRow) iterator2.next();
					WorkOrderUserBean userBean = userBeanMap.get(new UserOID(timeLogSummaryReportResultRow.getUserPk()));
					if(userBean == null)
					{
						userBean = new WorkOrderUserBean();
						userBean.setUserPk(timeLogSummaryReportResultRow.getUserPk());
						userBean.setUserName(timeLogSummaryReportResultRow.getUserName());
						userBean.setUserFullName(timeLogSummaryReportResultRow.getUserFullName());
						if(timeLogSummaryReportResultRow.getUserProfilePicFileName() != null && timeLogSummaryReportResultRow.getUserProfilePicFileName().trim().length() > 0)
							userBean.setUserAvtar(applicationURL + "/timetracker?action=getpicture&image=" + timeLogSummaryReportResultRow.getUserProfilePicFileName());
						userBeanMap.put(new UserOID(timeLogSummaryReportResultRow.getUserPk()), userBean);
					}
					userBean.setCompletedTimeSlotsTimeMins(timeLogSummaryReportResultRow.getClockedTimeMins());
					totalCompletedTimeForWorkorder += timeLogSummaryReportResultRow.getClockedTimeMins();
				}
			}
			
			List<OpenCheckinListReportResultRow> openCheckinList = openTimeSlotTimeMap.get(new ReworkOrderOID(workorderListReportResultRow.getPk()));
			if(openCheckinList != null)
			{
				for (Iterator iterator2 = openCheckinList.iterator(); iterator2.hasNext();)
				{
					OpenCheckinListReportResultRow row = (OpenCheckinListReportResultRow) iterator2.next();
					WorkOrderUserBean userBean = userBeanMap.get(new UserOID(row.getUserPk()));
					if(userBean == null)
					{
						userBean = new WorkOrderUserBean();
						userBean.setUserPk(row.getUserPk());
						userBean.setUserName(row.getUserName());
						userBean.setUserFullName(row.getUserFullName());
						if(row.getUserProfilePicFileName() != null && row.getUserProfilePicFileName().trim().length() > 0)
							userBean.setUserAvtar(applicationURL + "/timetracker?action=getpicture&image=" + row.getUserProfilePicFileName());
						userBeanMap.put(new UserOID(row.getUserPk()), userBean);
					}
					userBean.setCurrentTimeSlotStartTime(row.getTimeslotStartTime());
					if(row.getTimeSlotTimeType() != null)
					{
						userBean.setCurrentTimeSlotTimeType(TimeTypeEnum.valueOf(row.getTimeSlotTimeType()));
					}
				}
			}
			
			bean.getUsers().addAll(userBeanMap.values());
			bean.setTotalCompletedTimeMinutes(totalCompletedTimeForWorkorder);
			
			returnList.add(bean);
		}
		
		return returnList;
	}
	
	
	public static List<WorkOrderItemResponseBean> getUserTimeLogSummary(UserOID userOID, DateRangeFilter dateRangeFilter)throws Exception
	{
		String applicationURL = ApplicationProperties.getProperty("config/server/url");

		List<WorkOrderItemResponseBean> returnList = new ArrayList<WorkOrderItemResponseBean>();

		// get the time timelogs for the user and put them in a hashmap with the workorderOID as the key
		TimeLogSummaryReportRequest timeLogReportRequest = new TimeLogSummaryReportRequest();
		timeLogReportRequest.setUserOID(userOID);
		timeLogReportRequest.setTimeComputeDateRangeFilter(dateRangeFilter);
		timeLogReportRequest.setTimeSlotTypes(new TimeSlotTypeEnum[] {TimeSlotTypeEnum.RunningTimeSlot, TimeSlotTypeEnum.CommittedTimeSlot, TimeSlotTypeEnum.UnCommittedTimeSlot});
		timeLogReportRequest.setTimeTypes(new TimeTypeEnum[]{TimeTypeEnum.Work, TimeTypeEnum.Transfer, TimeTypeEnum.Wait, TimeTypeEnum.Adjustment});
		timeLogReportRequest.setGroupingSet(Arrays.asList(new TimeLogSummaryReportRequest.GroupingCol[] {GroupingCol.Workorder, GroupingCol.User}));
		List<TimeLogSummaryReportResultRow> committedTimesList = new TimeLogSummaryReport(timeLogReportRequest).runReport().getReportResult();
		
		List<ReworkOrderOID> workOrders = new ArrayList<>();

		HashMap<ReworkOrderOID, TimeLogSummaryReportResultRow> timeLogMap = new HashMap<ReworkOrderOID, TimeLogSummaryReportResultRow>();
		for (Iterator iterator = committedTimesList.iterator(); iterator.hasNext();)
		{
			TimeLogSummaryReportResultRow timeLogSummaryReportResultRow = (TimeLogSummaryReportResultRow) iterator.next();
			
			ReworkOrderOID reworkOrderOID = new ReworkOrderOID(timeLogSummaryReportResultRow.getWorkorderPk());
			workOrders.add(reworkOrderOID);
			timeLogMap.put(reworkOrderOID, timeLogSummaryReportResultRow);
		}
		
		
		if(workOrders.size() == 0)
			return returnList;

		// get the open timeslot summary report for all these work orders. and put them in a HashMap with the workorderOID as the key .
		OpenCheckinListReportRequest openCheckinReq = new OpenCheckinListReportRequest();
		openCheckinReq.setUsers(new UserOID[] {timeLogReportRequest.getUserOID()});
		openCheckinReq.setWorkorders(workOrders.toArray(new ReworkOrderOID[]{}));
		List<OpenCheckinListReportResultRow> openCheckInList = new OpenCheckinListReport(openCheckinReq).runReport().getReportResult();

		HashMap<ReworkOrderOID, OpenCheckinListReportResultRow> openCheckinMap = new HashMap<ReworkOrderOID, OpenCheckinListReportResultRow>();
		for (Iterator iterator = openCheckInList.iterator(); iterator.hasNext();)
		{
			OpenCheckinListReportResultRow openCheckinListReportResultRow = (OpenCheckinListReportResultRow) iterator.next();

			ReworkOrderOID reworkOrderOID = new ReworkOrderOID(openCheckinListReportResultRow.getWorkorderPk());
			openCheckinMap.put(reworkOrderOID, openCheckinListReportResultRow);
		}
		
		if(workOrders.size() == 0)
			return returnList;
		
		// now get the workorder details row for these workordrs.
		WorkorderListReportFilter f1 = new WorkorderListReportFilter();
		f1.setWorkorderOIDs(workOrders);
		List<WorkorderListReportResultRow> workorders = (List<WorkorderListReportResultRow>) ReportsDelegate.runReport(null, ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.WorkOrderListReport, f1)).getReportData();
		
		
		//now construct the response bean each of which has the workorder detail and the list of users with their completed time timeslot total time and the checkin time.
		//users are added from committed, uncommitted and opencheckin lists.
		for (Iterator iterator = workorders.iterator(); iterator.hasNext();)
		{
			WorkorderListReportResultRow workorderListReportResultRow = (WorkorderListReportResultRow) iterator.next();
			
			WorkOrderItemResponseBean bean = new WorkOrderItemResponseBean();
			bean.setWorkorderPk(workorderListReportResultRow.getPk());
//			bean.setWorkorderNo(workorderListReportResultRow.);
			bean.setWorkItemPk(workorderListReportResultRow.getTestProcSectionPk());
			bean.setWorkItemName(workorderListReportResultRow.getSectionNo());
			bean.setWorkItemDescription(workorderListReportResultRow.getSectionDescription());
			bean.setWorkItemGroupPk(workorderListReportResultRow.getTestProcPk());
			bean.setWorkItemGroupName(ListStringUtil.appendIgnoreNulls(" - ", workorderListReportResultRow.getTestName(), workorderListReportResultRow.getFormName()));
			bean.setWorkItemGroupDescription(workorderListReportResultRow.getFormDescription());
			bean.setUnitPk(workorderListReportResultRow.getUnitPk());
			bean.setUnitName(workorderListReportResultRow.getUnitName());
			bean.setUnitDescription(workorderListReportResultRow.getUnitDescription());
			bean.setSerialNo(workorderListReportResultRow.getUnitSerialNumber());
			bean.setProjectPk(workorderListReportResultRow.getProjectPk());
			bean.setProjectName(workorderListReportResultRow.getProjectName());
			bean.setProjectDescription(workorderListReportResultRow.getProjectDescription());
			bean.setWorkstationPk(workorderListReportResultRow.getWorkstationPk());
			bean.setWorkstationName(workorderListReportResultRow.getWorkstationName());
			bean.setWorkstationDescription(workorderListReportResultRow.getWorkstationDescription());
			bean.setFormMainPk(workorderListReportResultRow.getFormMainPk());
			bean.setFormPk(workorderListReportResultRow.getFormPk());
			bean.setFormName(workorderListReportResultRow.getFormName());
			bean.setFormDescription(workorderListReportResultRow.getFormDescription());
			bean.setFormResponsibleDivision(workorderListReportResultRow.getFormResponsibleDivision());
			bean.setFormRevision(workorderListReportResultRow.getFormRevision());
			bean.setFormVersion(workorderListReportResultRow.getFormVersion());
			
			TimeLogSummaryReportResultRow timeLogRow = timeLogMap.get(new ReworkOrderOID(workorderListReportResultRow.getPk()));
			bean.setTotalCompletedTimeMinutes(timeLogRow.getClockedTimeMins());
			
			WorkOrderUserBean userBean = new WorkOrderUserBean();
			userBean.setUserPk(timeLogRow.getUserPk());
			userBean.setUserName(timeLogRow.getUserName());
			userBean.setUserFullName(timeLogRow.getUserFullName());
			if(timeLogRow.getUserProfilePicFileName() != null && timeLogRow.getUserProfilePicFileName().trim().length() > 0)
				userBean.setUserAvtar(applicationURL + "/timetracker?action=getpicture&image=" + timeLogRow.getUserProfilePicFileName());
			userBean.setCompletedTimeSlotsTimeMins((int)timeLogRow.getClockedTimeMins());
			
			OpenCheckinListReportResultRow openCheckinRow = openCheckinMap.get(new ReworkOrderOID(workorderListReportResultRow.getPk()));
			if(openCheckinRow != null)
			{
				userBean.setCurrentTimeSlotStartTime(openCheckinRow.getTimeslotStartTime());
				if(openCheckinRow.getTimeSlotTimeType() != null)
				{
					userBean.setCurrentTimeSlotTimeType(TimeTypeEnum.valueOf(openCheckinRow.getTimeSlotTimeType()));
				}
			}

			
			bean.getUsers().add(userBean);
			
			returnList.add(bean);
		}
		
		
		return returnList;
	}
	
}
