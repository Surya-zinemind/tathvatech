package com.tathvatech.forms.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.TimeZone;

import com.tathvatech.common.wrapper.PersistWrapper;
import com.tathvatech.forms.common.ObjectScheduleRequestBean;
import com.tathvatech.forms.entity.EntitySchedule;

import com.tathvatech.user.OID.OID;
import com.tathvatech.user.common.UserContext;
import com.tathvatech.user.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityScheduleDAO
{
	Date now;
	TimeZone timezone;

	@Autowired
	private PersistWrapper persistWrapper;
	public EntityScheduleDAO(TimeZone timezone)
	{
		this.timezone = timezone;
		now = DateUtils.getNowDateForEffectiveDateFrom();
	}

	public EntitySchedule getEntitySchedule(OID oid)
	{
		EntitySchedule es = persistWrapper.read(EntitySchedule.class, 
				"select * from entity_schedule where objectPk = ? and objectType = ? and now() between effectiveDateFrom and effectiveDateTo",
				oid.getPk(), oid.getEntityType().getValue());
		return es;
		
	}
	public void save(UserContext context, ObjectScheduleRequestBean objectScheduleRequestBean)throws Exception
	{

		EntitySchedule es = persistWrapper.read(EntitySchedule.class, 
				"select * from entity_schedule where objectPk = ? and objectType = ? and now() between effectiveDateFrom and effectiveDateTo",
				objectScheduleRequestBean.getObjectOID().getPk(), objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
		
		if(es == null)
		{
			es = new EntitySchedule();
			es.setObjectPk((int) objectScheduleRequestBean.getObjectOID().getPk());
			es.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
			es.setCreatedBy((int) context.getUser().getPk());
			es.setCreatedDate(new Date());
			es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
			es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
			es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
			es.setEffectiveDateFrom(now);
			es.setEffectiveDateTo(DateUtils.getMaxDate());
			persistWrapper.createEntity(es);
		}
		else
		{
			// check if there is any change in the schedule
			if(Objects.equals(es.getForecastStartDate(), objectScheduleRequestBean.getStartForecast()) 
					&& Objects.equals(es.getForecastEndDate(), objectScheduleRequestBean.getEndForecast())
					&& Objects.equals(es.getEstimateHours(), objectScheduleRequestBean.getHoursEstimate()))
			{
				return; // no change means dont have to update, we are doing it above, but a double check.. may be this can be removed.
			}
			
			Calendar calEx = new GregorianCalendar();
			calEx.setTime(es.getCreatedDate());
			calEx.setTimeZone(timezone);

			Calendar calNow = new GregorianCalendar();
			calNow.setTime(new Date());
			calNow.setTimeZone(timezone);
		
			// need to check if the last record is made on the same day.. if so just update. else we create a history record.
			if(calEx.get(Calendar.DAY_OF_MONTH) == calNow.get(Calendar.DAY_OF_MONTH) &&
					calEx.get(Calendar.MONTH) == calNow.get(Calendar.MONTH) && 
					calEx.get(Calendar.YEAR) == calNow.get(Calendar.YEAR))
			{
				// so update
				es.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
				es.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
				es.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
				persistWrapper.update(es);
			}
			else
			{
				//invalidate old and create new
				es.setEffectiveDateTo(new Date(now.getTime() - 1000));
				persistWrapper.update(es);
				
				EntitySchedule esNew = new EntitySchedule();
				esNew.setObjectPk((int) objectScheduleRequestBean.getObjectOID().getPk());
				esNew.setObjectType(objectScheduleRequestBean.getObjectOID().getEntityType().getValue());
				esNew.setCreatedBy((int) context.getUser().getPk());
				esNew.setCreatedDate(new Date());
				esNew.setForecastStartDate(objectScheduleRequestBean.getStartForecast());
				esNew.setForecastEndDate(objectScheduleRequestBean.getEndForecast());
				esNew.setEstimateHours(objectScheduleRequestBean.getHoursEstimate());
				esNew.setEffectiveDateFrom(now);
				esNew.setEffectiveDateTo(DateUtils.getMaxDate());
				persistWrapper.createEntity(esNew);
			}
		}
			
	}
}
