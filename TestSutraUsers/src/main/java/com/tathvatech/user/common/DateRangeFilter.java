package com.tathvatech.user.common;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tathvatech.user.utils.DateUtils;


public class DateRangeFilter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum DateRangePresets {
		Today, ThisWeek, PreviousWeek, Next7Days, ThisMonth, PreviousMonth, Next30Days, Custom
	};

	private DateRangePresets dateRangePreset;
	private TimeZone dateRangePresetTimeZone;
	private Date fromDateCustom;
	private Date toDateCustom;

	public DateRangeFilter()
	{
		super();
	}

	public DateRangeFilter(DateRangePresets dateRangePreset, TimeZone dateRangePresetTimeZone)
	{
		super();
		this.dateRangePreset = dateRangePreset;
		this.dateRangePresetTimeZone = dateRangePresetTimeZone;
	}

	public DateRangeFilter(Date fromDateCustom, Date toDateCustom)
	{
		super();
		this.dateRangePreset = DateRangePresets.Custom;
		this.fromDateCustom = fromDateCustom;
		this.toDateCustom = toDateCustom;
	}

	public DateRangeFilter(Date fromDateCustom, Date toDateCustom, TimeZone dateRangePresetTimeZone)
	{
		super();
		this.dateRangePreset = DateRangePresets.Custom;
		this.fromDateCustom = fromDateCustom;
		this.toDateCustom = toDateCustom;
		this.dateRangePresetTimeZone = dateRangePresetTimeZone;
	}

	public DateRangePresets getDateRangePreset()
	{
		return dateRangePreset;
	}

	public void setDateRangePreset(DateRangePresets dateRangePreset)
	{
		this.dateRangePreset = dateRangePreset;
	}

	public TimeZone getDateRangePresetTimeZone()
	{
		return dateRangePresetTimeZone;
	}

	public void setDateRangePresetTimeZone(TimeZone dateRangePresetTimeZone)
	{
		this.dateRangePresetTimeZone = dateRangePresetTimeZone;
	}

	public Date getFromDateCustom()
	{
		return fromDateCustom;
	}

	public void setFromDateCustom(Date fromDateCustom)
	{
		this.fromDateCustom = fromDateCustom;
	}

	public Date getToDateCustom()
	{
		return toDateCustom;
	}

	public void setToDateCustom(Date toDateCustom)
	{
		this.toDateCustom = toDateCustom;
	}

	@JsonIgnore
	public Date[] getResolvedDateRangeValues()
	{
		TimeZone timeZone = dateRangePresetTimeZone;
		if (timeZone == null)
			timeZone = TimeZone.getTimeZone("UTC");

		if (dateRangePreset == null)
		{
			fromDateCustom = new DateUtils(timeZone).getBeginningDayNew(fromDateCustom);

			toDateCustom = new DateUtils(timeZone).getBeginningOfNextDay(toDateCustom);
			return new Date[] { fromDateCustom, toDateCustom };
		} else if (DateRangePresets.Today == dateRangePreset) // today
		{
			Date fromDate = new DateUtils(timeZone).getBeginningDayNew(new Date());
			Date toDate = new DateUtils(timeZone).getBeginningOfNextDay(fromDate);
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.ThisWeek == dateRangePreset) // this week
		{
			Date fromDate = new DateUtils(timeZone).getFirstDayOfThisWeek();
			Date toDate = new DateUtils(timeZone).getBeginningOfNextWeek();
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.PreviousWeek == dateRangePreset) // previous
																		// week
		{
			Date fromDate = new DateUtils(timeZone).getFirstDayOfPreviousWeek();
			Date toDate = new DateUtils(timeZone).getFirstDayOfThisWeek();
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.Next7Days == dateRangePreset) // Next 7
																	// days;
		{
			Date fromDate = new DateUtils(timeZone).getBeginningDayNew(new Date());
			Date toDate = new DateUtils(timeZone).getAdvanceDays(fromDate, 7);
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.ThisMonth == dateRangePreset) // this month
		{
			Date fromDate = new DateUtils(timeZone).getFirstDayOfThisMonth();
			Date toDate = new DateUtils(timeZone).getFirstDayOfNextMonth();
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.PreviousMonth == dateRangePreset) // Previous
																		// month
		{
			Date fromDate = new DateUtils(timeZone).getFirstDayOfPreviousMonth();
			Date toDate = new DateUtils(timeZone).getFirstDayOfThisMonth();
			return new Date[] { fromDate, toDate };
		} else if (DateRangePresets.Next30Days == dateRangePreset) // Next 30
																	// days;
		{
			Date fromDate = new DateUtils(timeZone).getBeginningDayNew(new Date());
			Date toDate = new DateUtils(timeZone).getAdvanceDays(fromDate, 30);
			return new Date[] { fromDate, toDate };
		} else // custom
		{
			Date fromDate = null;
			if (fromDateCustom != null)
			{
				fromDate = new DateUtils(timeZone).getBeginningDayNew(fromDateCustom);
			}
			Date toDate = null;
			if (toDateCustom != null)
			{
				toDate = new DateUtils(timeZone).getAdvanceDays(toDateCustom, 1);
			}
			return new Date[] { fromDate, toDate };
		}
	}
}
