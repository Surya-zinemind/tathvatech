package com.tathvatech.timetracker.service;

import com.tathvatech.common.enums.EStatusEnum;
import com.tathvatech.common.enums.EntityTypeEnum;
import com.tathvatech.common.enums.WorkItem;
import com.tathvatech.forms.dao.TestProcDAO;
import com.tathvatech.forms.entity.TestProcFormAssign;
import com.tathvatech.forms.entity.TestProcFormSection;
import com.tathvatech.report.request.ReportRequest;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.project.service.ProjectService;
import com.tathvatech.survey.controller.SurveyDelegate;
import com.tathvatech.survey.service.SurveyMaster;
import com.tathvatech.user.OID.*;
import com.tathvatech.user.common.TestProcObj;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.entity.User;
import com.tathvatech.user.entity.UserQuery;
import com.tathvatech.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class TimeEntryManager
{
	private static final Logger logger = LoggerFactory.getLogger(TimeEntryManager.class);
	private final PersistWrapper persistWrapper;
	private final ProjectService projectService;
	private final SurveyMaster surveyMaster;

    public TimeEntryManager(PersistWrapper persistWrapper, ProjectService projectService, SurveyMaster surveyMaster) {
        this.persistWrapper = persistWrapper;
        this.projectService = projectService;
        this.surveyMaster = surveyMaster;
    }

    public  Date checkIn(UserContext context, ReworkOrderOID workorderOID, UserOID timeForUserOID,
							   ShiftInstanceForUserResponseBean shiftInstanceUserBean,
							   Date date, boolean forceCloseIfActiveOnOther, boolean setAsAttributionUser) throws Exception
	{
		if(date == null)
			date = new Date();  // default to current time.
		
		Workorder checkInWorkorder = WorkorderManager.getWorkorder(workorderOID);
		if(checkInWorkorder == null || EStatusEnum.Deleted.getValue() == checkInWorkorder.getEstatus())
		{
			throw new AppException("Invalid or unknown workorder. Please check the workorder number");
		}

		//check if there is an ongoing timeslot entry for user and if it is of Transfer type
		// if so he needs to end that.
		WorkorderTimeEntry ongoingTimeSlot = getOngoingTimeSlotForUser(timeForUserOID);
		if(ongoingTimeSlot != null && TimeTypeEnum.Transfer == TimeTypeEnum.valueOf(ongoingTimeSlot.getTimeType()))
		{
			ongoingTimeSlot.setEndTime(date);
			ongoingTimeSlot.setCommitted(1);
			persistWrapper.update(ongoingTimeSlot);
		}
		
		// check if there is any open checkin for the user. if its there it has to be closed or an error thrown based on forceclose flag
		WorkOrderTimeEntryListObj woTimeList = getUnCommittedAndRunningTimeSlotsForUser(timeForUserOID);
		if(woTimeList != null)
		{
			Workorder currentWorkorder =  woTimeList.getWorkorder();

			if(checkInWorkorder.equals(currentWorkorder))
				throw new AppException("User is already checked into the work order, Please continue your work");
			else
			{
				if(forceCloseIfActiveOnOther == false)
				{
						throw new AppException("User is currently checked into workorder No. " + currentWorkorder.getWorkorderNumber() );
				}
				else
				{
					checkOut(context, woTimeList.workorder.getOID(), timeForUserOID, date);
				}
			}
		}

		//create the shift instance if it is not already there.
		ShiftInstanceOID shiftInstanceOID = null;
		if(shiftInstanceUserBean.getShiftInstanceFk() == null || shiftInstanceUserBean.getShiftInstanceFk() < 1)
		{	
			ShiftInstance si = ShiftManager.createShiftInstance(new ShiftOID(shiftInstanceUserBean.getShiftFk()), shiftInstanceUserBean.getShiftDate());
			shiftInstanceOID = si.getOID();
		}
		else
		{
			ShiftInstance si = persistWrapper.readByPrimaryKey(ShiftInstance.class, shiftInstanceUserBean.getShiftInstanceFk());
			shiftInstanceOID = si.getOID();
		}
		
		// now create the new time entry.
		createTimeEntry(context.getUser().getOID(), checkInWorkorder, shiftInstanceOID, date, TimeTypeEnum.Work, timeForUserOID, null);
		
		//set attribution if requested
		if(setAsAttributionUser)
		{
	        Workorder wo = WorkorderManager.getWorkorder(workorderOID);
	        WorkItem workItem = WorkorderManager.getWorkItem(wo.getEntityPk(), (EntityTypeEnum) EntityTypeEnum.fromValue(wo.getEntityType()));
			surveyMaster.setAttribution(context, workItem, timeForUserOID);
		}
		
		return date;
	}
	
	public  Date pause(UserContext context, ReworkOrderOID workorderOID, UserOID timeForUserOID, int timeQualifierPk, Date date) throws Exception
	{
		if(date == null)
			date = new Date();  // default to current time.

		WorkorderTimeEntry timeEntry = getOngoingTimeSlotForUser(timeForUserOID);
		
		if(timeEntry == null)
		{
			throw new AppException("No active workorder for user, No timer to pause.");
		}
		else if (timeEntry.getWorkorderFk() != workorderOID.getPk())
		{
			logger.error(String.format("Invald workorder for user to pause on. userPk:%d is checked in on workorderPk:%d but tried to pause workorderPk:%d", 
					timeForUserOID.getPk(), timeEntry.getWorkorderFk(), workorderOID.getPk()));
			throw new AppException("Invalid workorder, You is not active on this workorder.");
		}
		else if(TimeTypeEnum.Work != TimeTypeEnum.valueOf(timeEntry.getTimeType()))
		{
			throw new AppException("Your is not active on the workorder, Pause ignored.");
		}
		else
		{
			timeEntry.setEndTime(date);
			persistWrapper.update(timeEntry);
			
			//now start time slot with wait as the time type.
			Workorder workorder = WorkorderManager.getWorkorder(workorderOID);
			createTimeEntry(context.getUser().getOID(), workorder, new ShiftInstanceOID(timeEntry.getShiftInstanceFk()), date, TimeTypeEnum.Wait, timeForUserOID, timeQualifierPk);
		
			return date;
		}
		
	}

	public Date restart(UserContext context, ReworkOrderOID workorderOID, UserOID timeForUserOID,
			ShiftInstanceForUserResponseBean shiftInstanceUserBean,
			Date date) throws Exception
	{
		if(date == null)
			date = new Date();  // default to current time.

		WorkorderTimeEntry timeEntry = getOngoingTimeSlotForUser(timeForUserOID);
		
		if(timeEntry == null)
		{
			throw new AppException("No active workorder for user, No timer to restart.");
		}
		else if (timeEntry.getWorkorderFk() != workorderOID.getPk())
		{
			logger.error(String.format("Invald workorder for user to restart. userPk:%d is active on workorderPk:%d but tried to restart workorderPk:%d", 
					timeForUserOID.getPk(), timeEntry.getWorkorderFk(), workorderOID.getPk()));
			throw new AppException("Trying to resume a workorder different from what you are blocked on. Please checkin to the new workorder. ");
		}
		else if(TimeTypeEnum.Wait != TimeTypeEnum.valueOf(timeEntry.getTimeType()))
		{
			throw new AppException("You are not blocked on the workorder, Restart ignored.");
		}
		else
		{
			timeEntry.setEndTime(date);
			persistWrapper.update(timeEntry);
			
			//create the shift instance if it is not already there.
			ShiftInstanceOID shiftInstanceOID = null;
			if(shiftInstanceUserBean.getShiftInstanceFk() == null || shiftInstanceUserBean.getShiftInstanceFk() < 1)
			{	
				ShiftInstance si = ShiftManager.createShiftInstance(new ShiftOID(shiftInstanceUserBean.getShiftFk()), shiftInstanceUserBean.getShiftDate());
				shiftInstanceOID = si.getOID();
			}
			else
			{
				ShiftInstance si = persistWrapper.readByPrimaryKey(ShiftInstance.class, shiftInstanceUserBean.getShiftInstanceFk());
				shiftInstanceOID = si.getOID();
			}
			
			
			//now start time slot with wait as the time type.
			Workorder workorder = WorkorderManager.getWorkorder(workorderOID);
			createTimeEntry(context.getUser().getOID(), workorder, shiftInstanceOID, date, TimeTypeEnum.Work, timeForUserOID, null);
			
			return date;
		}
	}

	public  Date checkOut(UserContext context, ReworkOrderOID workorderOID, UserOID timeForUserOID, Date date) throws Exception
	{
		if(date == null)
			date = new Date();  // default to current time.

		WorkOrderTimeEntryListObj timeEntry = getUnCommittedAndRunningTimeSlotsForUser(timeForUserOID);
		if(timeEntry == null)
		{
			return null;
		}	
		if(timeEntry.getWorkorder().getPk() != workorderOID.getPk())
		{
			throw new AppException("Invalid workorder to checkout; User is checked in to a different workorder.");
		}
		
		for (Iterator iterator = timeEntry.getTimeEntries().iterator(); iterator.hasNext();)
		{
			WorkorderTimeEntry aTimeEntry = (WorkorderTimeEntry) iterator.next();
			if(aTimeEntry.getEndTime() == null)
			{
				aTimeEntry.setEndTime(date);
			}
			aTimeEntry.setCommitted(1);
			
			persistWrapper.update(aTimeEntry);
		}
		
		// remove attribution if set
        Workorder wo = WorkorderManager.getWorkorder(workorderOID);
        WorkItem workItem = WorkorderManager.getWorkItem(wo.getEntityPk(), (EntityTypeEnum) EntityTypeEnum.fromValue(wo.getEntityType()));
        
        UserQuery currentAttributedUser = SurveyDelegate.getAttributionUser(workItem);
        if(currentAttributedUser != null && currentAttributedUser.getPk() == timeForUserOID.getPk())
        {
            SurveyDelegate.setAttribution(context, workItem, null);
        }
		
		return date;
	}

	
	public static Date checkOutAndTransfer(UserContext context, ReworkOrderOID workorderOID, UserOID timeForUserOID, int timeQualifierPk, Date date) throws Exception
	{
		if(date == null)
			date = new Date();  // default to current time.

		ShiftInstanceBean shiftInstanceBean = ShiftManager.getLastCheckedInShiftInstanceForUser(timeForUserOID);
		
		//do the checkout
		checkOut(context, workorderOID, timeForUserOID, date);
		
		//now create the transfer time entry.
		Workorder workorder = WorkorderManager.getWorkorder(workorderOID);
		createTimeEntry(context.getUser().getOID(), workorder, new ShiftInstanceOID(shiftInstanceBean.getPk()), date, TimeTypeEnum.Transfer, timeForUserOID, timeQualifierPk);
		
		
		// remove attribution if set
        Workorder wo = WorkorderManager.getWorkorder(workorderOID);
        WorkItem workItem = WorkorderManager.getWorkItem(wo.getEntityPk(), (EntityTypeEnum) EntityTypeEnum.fromValue(wo.getEntityType()));
        
        UserQuery currentAttributedUser = SurveyDelegate.getAttributionUser(workItem);
        if(currentAttributedUser != null && currentAttributedUser.getPk() == timeForUserOID.getPk())
        {
            SurveyDelegate.setAttribution(context, workItem, null);
        }

        return date;
	}

	public static void checkOutAllFromWorkOrder(UserContext context, WorkItem workItem)throws Exception
	{
		Date now = new Date();
		
		Workorder wo = WorkorderManager.getWorkorderForEntity((OID) workItem);
		if(wo != null)
		{
			OpenCheckinListReportRequest openCheckinReq = new OpenCheckinListReportRequest();
			openCheckinReq.setWorkorders(new ReworkOrderOID[] {wo.getOID()});
			List<OpenCheckinListReportResultRow> openCheckInList = new OpenCheckinListReport(openCheckinReq).runReport().getReportResult();
			
			for (Iterator iterator = openCheckInList.iterator(); iterator.hasNext();)
			{
				OpenCheckinListReportResultRow aRow = (OpenCheckinListReportResultRow) iterator.next();
				int userPk = aRow.getUserPk();

				//do the checkout
				checkOut(context, wo.getOID(), new UserOID(userPk), now);
			}
		}
	}
	
	public  Date checkOutAll(UserContext context, ProjectOID projectOID, WorkstationOID workstationOID,
								   ShiftInstanceOID shiftInstanceOID,
								   UserOID userOID, Date date)throws Exception
	{
		boolean allowCheckoutall = false;
		if(User.USER_PRIMARY.equals(UserRepository.getInstance().getUser((int) userOID.getPk()).getUserType()))
		{
			allowCheckoutall = true;
		}
		else
		{
			List<User> verifiersForWorkstation = projectService.getUsersForProjectInRole(projectOID.getPk(), workstationOID, User.ROLE_VERIFY);
			User tempU = new User();
			tempU.setPk(userOID.getPk());
			if((verifiersForWorkstation.contains(tempU)))
			{
				allowCheckoutall = true;
			}
		}
		if(allowCheckoutall == false)
		{
			throw new AppException("User not authorized to checkout all from workstation.");
		}
		
		Date now = new Date();
		
		List<WorkOrderItemResponseBean> returnList = new ArrayList<WorkOrderItemResponseBean>();

		
		HashMap<ReworkOrderOID, List<TimeLogSummaryReportResultRow>> completedTimeSlotTimeMap = new HashMap<ReworkOrderOID, List<TimeLogSummaryReportResultRow>>();
		HashMap<ReworkOrderOID, List<OpenCheckinListReportResultRow>> openTimeSlotTimeMap = new HashMap<ReworkOrderOID, List<OpenCheckinListReportResultRow>>();
		
		WorkorderListReportFilter filter = new WorkorderListReportFilter();
		filter.setWorkstationOID(workstationOID);
		filter.setProjectOID(projectOID);
		filter.setShowActiveWorkordersOnly(true);
		List<WorkorderListReportResultRow> workorders = (List<WorkorderListReportResultRow>) ReportsDelegate.runReport(null, ReportRequest.getSimpleReportRequestForFetchAllRows(ReportTypes.WorkOrderListReport, filter)).getReportData();
		List<ReworkOrderOID> workorderOIDs = new ArrayList<ReworkOrderOID>();
		for (Iterator iterator = workorders.iterator(); iterator.hasNext();)
		{
			WorkorderListReportResultRow workorderListReportResultRow = (WorkorderListReportResultRow) iterator.next();
			workorderOIDs.add(workorderListReportResultRow.getOID());
		}

		
		OpenCheckinListReportRequest openCheckinReq = new OpenCheckinListReportRequest();
		openCheckinReq.setShiftInstanceOID(shiftInstanceOID);
		openCheckinReq.setWorkorders(workorderOIDs.toArray(new ReworkOrderOID[] {}));
		List<OpenCheckinListReportResultRow> openCheckInList = new OpenCheckinListReport(openCheckinReq).runReport().getReportResult();
		
		for (Iterator iterator = openCheckInList.iterator(); iterator.hasNext();)
		{
			OpenCheckinListReportResultRow aRow = (OpenCheckinListReportResultRow) iterator.next();
			ReworkOrderOID workorderOID = new ReworkOrderOID(aRow.getWorkorderPk());
			
			checkOut(context, workorderOID, new UserOID(aRow.getUserPk()), now);
		}
		
		return now;
	}
	
	private  void createTimeEntry(UserOID createdByOID, Workorder workOrder, ShiftInstanceOID shiftInstanceOID, Date date, TimeTypeEnum timeType, UserOID timeForUserOID, Integer timeQualifierPk) throws Exception
	{
		WorkorderTimeEntry timeEntry = new WorkorderTimeEntry();
		timeEntry.setWorkorderFk(workOrder.getPk());
		
		fillTimeEntryDetailsForWorkorder(timeEntry, workOrder);
		
		timeEntry.setShiftInstanceFk(shiftInstanceOID.getPk());
		timeEntry.setStartTime(date);
		timeEntry.setCreatedBy(createdByOID.getPk());
		timeEntry.setCreatedDate(new Date());
		timeEntry.setTimeType(timeType.name());
		timeEntry.setUserFk(timeForUserOID.getPk());
		timeEntry.setTimeQualifierFk(timeQualifierPk);
		
		persistWrapper.createEntity(timeEntry);
	}
	
	private  void fillTimeEntryDetailsForWorkorder(WorkorderTimeEntry timeEntry, Workorder workorder) throws Exception
	{
		if(EntityTypeEnum.TestProc.getValue() == workorder.getEntityType())
		{
			TestProcObj obj = new TestProcDAO().getTestProc(workorder.getEntityPk());
			timeEntry.setTestProcFk(obj.getPk());
			timeEntry.setProjectFk(obj.getProjectPk());
			timeEntry.setUnitFk(obj.getUnitPk());
			timeEntry.setWorkstationFk(obj.getWorkstationPk());
			timeEntry.setFormFk(obj.getFormPk());
		}
		else if(EntityTypeEnum.TestProcSection.getValue() == workorder.getEntityType())
		{
			TestProcFormSection uts = persistWrapper.readByPrimaryKey(TestProcFormSection.class, workorder.getEntityPk());
			TestProcFormAssign utf = persistWrapper.readByPrimaryKey(TestProcFormAssign.class,
					uts.getTestProcFormAssignFk());
			TestProcObj obj = new TestProcDAO().getTestProc(utf.getTestProcFk());
			
			timeEntry.setTestProcSectionFk(uts.getPk());
			
			timeEntry.setTestProcFk(obj.getPk());
			timeEntry.setProjectFk(obj.getProjectPk());
			timeEntry.setUnitFk(obj.getUnitPk());
			timeEntry.setWorkstationFk(obj.getWorkstationPk());
			timeEntry.setFormFk(obj.getFormPk());
		}
		else
			throw new UnsupportedOperationException();
	}


	private  WorkOrderTimeEntryListObj getUnCommittedAndRunningTimeSlotsForUser(UserOID userOID) throws Exception
	{
		List<WorkorderTimeEntry> list = PersistWrapper.readList(WorkorderTimeEntry.class, 
				"select * from workorder_timeentry where committed  = 0 and userFk = ? ", userOID.getPk()); 

		if(list == null || list.size() == 0)
			return null;
		
		WorkOrderTimeEntryListObj returnObj = new WorkOrderTimeEntryListObj();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			WorkorderTimeEntry workorderTimeEntry = (WorkorderTimeEntry) iterator.next();
			if(returnObj.getWorkorder() == null)
			{
				Workorder wo = persistWrapper.readByPrimaryKey(Workorder.class, workorderTimeEntry.getWorkorderFk());
				returnObj.setWorkorder(wo);
			}
			else
			{
				if(returnObj.getWorkorder().getPk() != workorderTimeEntry.getWorkorderFk())
				{
					logger.error(String.format("UnCommitted time entries from multiple workorder for userPk:%d", userOID.getPk()));
					throw new AppException("Invalid time entries for user. Please contact your administator");
				}
			}
		}
		returnObj.setTimeEntries(list);
		
		return returnObj;
	}

	public  WorkorderTimeEntry getOngoingTimeSlotForUser(UserOID userOID)
	{
		return persistWrapper.read(WorkorderTimeEntry.class,
				"select * from workorder_timeentry where endTime is null and userFk = ?", userOID.getPk()); 
	}


	public  double getLoggedTime(ReworkOrderOID oid)
	{
		return persistWrapper.read(Double.class, "select sum(TIMESTAMPDIFF(MINUTE, IFNULL(checkinTime, NOW()), checkoutTime)) from workorder_timeentry where workorderFk = ?", null);
	}
}
