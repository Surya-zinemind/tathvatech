package com.tathvatech.activitylogging.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.tathvatech.activitylogging.common.ActivityLogCommonQuery;
import com.tathvatech.activitylogging.common.ActivityLogRequestBeanBase;
import com.tathvatech.activitylogging.entity.ActivityLog;
import com.tathvatech.activitylogging.entity.ActivityLogCommon;
import com.tathvatech.user.OID.UserOID;
import com.tathvatech.activitylogging.common.ActivityLogQuery;
import com.tathvatech.common.enums.BaseActions;
import com.tathvatech.common.exception.AppException;
import com.tathvatech.common.wrapper.PersistWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ActivityLoggingDelegate {
          private final PersistWrapper persistWrapper;

	public  List<ActivityLogQuery> getActivityLogs(Integer userPk, Date startDate, Date endDate, BaseActions[] actions) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		if(endDate == null)
			endDate = new Date();
		if(startDate == null)
		{
			cal.setTime(endDate);
			cal.add(Calendar.MONTH, -2);
			startDate = cal.getTime();
		}
		else
		{
			//get report for a max of 2 months
			cal.setTime(startDate);
			cal.add(Calendar.MONTH, 2);
			Date d = cal.getTime();
			if(d.before(endDate))
			{
				throw new AppException("Report can be fetched for a maximum of 2 months at a time. Please select the start and end dates accordingly");
			}
		}
		
		//add one date to EndDate
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();
		
		List objs = new ArrayList();
		StringBuffer sql = new StringBuffer(ActivityLogQuery.fetchQuery);
		if(userPk != null && userPk != 0)
		{
			sql.append(" and a.userPk = ? ");
			objs.add(userPk);
		}
		if(startDate != null)
		{
			sql.append("and a.actionEndTime >= ? ");
			objs.add(startDate);
		}
		if(endDate != null)
		{
			sql.append("and a.actionEndTime < ? ");
			objs.add(endDate);
		}
		if(actions != null && actions.length > 0)
		{
			sql.append("and action in (");
			
			StringBuffer as = new StringBuffer();
			for (int i = 0; i < actions.length; i++) {
				as.append(",?");
				objs.add(actions[i].value());
			}
			sql.append(as.substring(1));
			sql.append(")");
		}
		
		return persistWrapper.readList(ActivityLogQuery.class,
				sql.toString(), 
				objs.toArray(new Object[objs.size()]));
	}

	public  ActivityLogQuery getLastActivityForResponse(int responseId) throws Exception
	{
		String sql = ActivityLogQuery.fetchQuery + " and a.responseId = ? order by pk desc limit 0, 1";
		return persistWrapper.read(ActivityLogQuery.class,
				sql.toString(), 
				responseId);
	}
	
	public  ActivityLogQuery getLastActivityForResponse(int responseId, BaseActions[] actions) throws Exception
	{
		StringBuffer sql = new StringBuffer(ActivityLogQuery.fetchQuery);
				
		StringBuffer qs = new StringBuffer();
		Object[] params = new Object[actions.length + 1];
		for (int i = 0; i < actions.length; i++) 
		{
			qs.append("?,");
			params[i] = actions[i].value();
		}
		if(qs.length() > 0)
		{
			sql.append(" and a.action in (");
			sql.append(qs.substring(0, qs.length() -1));
			sql.append(")");
		}
		sql.append(" and a.responseId = ? order by pk desc limit 0, 1");
		
		params[params.length-1] = responseId;
		
		return persistWrapper.read(ActivityLogQuery.class,
				sql.toString(), 
				params);
	}
	@Transactional
	public  void logActivity(ActivityLogQuery act) throws Exception
    {
    	ActivityLog aLog = new ActivityLog();
    	aLog.setAction(act.getAction());
    	aLog.setActionDescription(act.getActionDescription());
    	aLog.setActionEndTime(act.getActionEndTime());
    	aLog.setActionStartTime(act.getActionStartTime());
    	aLog.setTestProcPk(act.getTestProcPk());
    	aLog.setFormPk(act.getFormPk());
    	aLog.setProjectPk(act.getProjectPk());
    	aLog.setResponseId(act.getResponseId());
    	aLog.setSectionId(act.getSectionId());
    	aLog.setUnitPk(act.getUnitPk());
    	aLog.setUserPk(act.getUserPk());
    	aLog.setWorkstationPk(act.getWorkstationPk());
    	
    	aLog.setTotalQCount(act.getTotalQCount());
    	aLog.setTotalACount(act.getTotalACount());
    	aLog.setPassCount(act.getPassCount());
    	aLog.setFailCount(act.getFailCount());
    	aLog.setDimentionalFailCount(act.getDimentionalFailCount());
    	aLog.setNaCount(act.getNaCount());
    	aLog.setCommentsCount(act.getCommentsCount());
		aLog.setLastUpdated(new Date());
    	
    	persistWrapper.createEntity(aLog);
    }

	
	/**
	 * This one writes the logs into the new table.
	 * @param activityLogBean
	 * @throws Exception
	 */
	public  void logActivity(ActivityLogRequestBeanBase activityLogBean)throws Exception
	{



			ActivityLogCommon act = new ActivityLogCommon();
			act.setAction(activityLogBean.getAction().value());
			act.setActionTime(new Date());
			act.setComment(activityLogBean.getComment());
			act.setExecutedByPk((int) activityLogBean.getExecutedBy().getPk());
			persistWrapper.createEntity(act);
		    

	}
	
	
	public  List<ActivityLogCommonQuery> getActivityLogs(UserOID userOID, Date startDate, Date endDate, BaseActions action) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		if(endDate == null)
			endDate = new Date();
		if(startDate == null)
		{
			cal.setTime(endDate);
			cal.add(Calendar.MONTH, -2);
			startDate = cal.getTime();
		}
		else
		{
			//get report for a max of 2 months
			cal.setTime(startDate);
			cal.add(Calendar.MONTH, 2);
			Date d = cal.getTime();
			if(d.before(endDate))
			{
				throw new AppException("Report can be fetched for a maximum of 2 months at a time. Please select the start and end dates accordingly");
			}
		}
		
		//add one date to EndDate
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		endDate = cal.getTime();
		
		List objs = new ArrayList();
		StringBuffer sql = new StringBuffer(ActivityLogCommonQuery.fetchQuery);
		if(userOID != null)
		{
			sql.append(" and a.executedByPk = ? ");
			objs.add(userOID.getPk());
		}
		if(startDate != null)
		{
			sql.append("and a.actionTime >= ? ");
			objs.add(startDate);
		}
		if(endDate != null)
		{
			sql.append("and a.actionTime < ? ");
			objs.add(endDate);
		}
		
		sql.append(" order by actionTime desc");
		
		return persistWrapper.readList(ActivityLogCommonQuery.class,
				sql.toString(), 
				objs.toArray(new Object[objs.size()]));
	}
	
}
