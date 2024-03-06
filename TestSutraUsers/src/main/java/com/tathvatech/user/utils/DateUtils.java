/*
 * Created on Feb 15, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.tathvatech.user.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.tathvatech.common.common.ApplicationProperties;

import com.tathvatech.user.common.UserContext;

/**
 * @author Hari
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DateUtils
{
	private TimeZone timeZone;

	public DateUtils(UserContext context)
	{
		this.timeZone = context.getTimeZone();
	}

	public DateUtils(TimeZone timeZone)
	{
		this.timeZone = timeZone;
	}

	/**
	 * @deprecated need to use the constructor with timezone or context
	 */
	public DateUtils()
	{
		this.timeZone = TimeZone.getTimeZone(ApplicationProperties.getDefaultTimezone());
	}

	/**
	 * set the time of the date to 12.00AM
	 * 
	 * @param date
	 * @return
	 */
	public Date set12AM(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal.getTime();
	}

	/**
	 * returns the first day of the next month in the previous year For eg. If
	 * the parameter passed in 12-Apr-2006, the date returned is 01-May-2005
	 * 
	 * @param date
	 * @return
	 */
	public Date getFirstDayOfNextMonthLastYear(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.YEAR, -1);
		cal.set(Calendar.DATE, 1);

		// now set time to 12.00AM
		return set12AM(cal.getTime());
	}

	/**
	 * returns the first day of the month for the date
	 * 
	 * @param
	 * @return
	 */
	public Date getFirstDayOfMonth(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);

		// now set time to 12.00AM
		return set12AM(cal.getTime());
	}

	/**
	 * returns the first day of the next month for the date
	 * 
	 * @param date
	 * @return
	 */
	public Date getFirstDayOfNextMonth(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);

		// now set time to 12.00AM
		return set12AM(cal.getTime());
	}

	/**
	 * @param date
	 * @return
	 */
	public Date getFirstDayPreviousMonth(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);

		// now set time to 12.00AM
		return set12AM(cal.getTime());
	}

	/**
	 * @param date
	 * @return
	 */
	public Date advanceOneDay(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	/**
	 * @deprecated use the instance method
	 * @param date
	 * @return
	 */
	public static Date getBeginningDay(Date date)
	{
		Date outDate = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
		return outDate;
	}

	/**
	 * @deprecated use the getLastSecondOfDay.. or create a new method which is
	 *             getLastMillisecondOfDay
	 * @param date
	 * @return
	 */
	public static Date getEndOfDay(Date date)
	{
		Date outDate = org.apache.commons.lang3.time.DateUtils.truncate(date, Calendar.DATE);
		outDate = org.apache.commons.lang3.time.DateUtils.addDays(outDate, 1);
		org.apache.commons.lang3.time.DateUtils.addMilliseconds(outDate, -1);
		return outDate;
	}

	/**
	 * @param date
	 * @return
	 */
	public Date getLastSecondOfDay(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		cal.add(Calendar.DATE, 1);
		cal.add(Calendar.SECOND, -1);
		Date outDate = cal.getTime();
		return outDate;
	}

	/**
	 * @param date
	 * @return
	 */
	public Date getBeginningOfNextDay(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);

		cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	/**
	 * Convert a millisecond duration to a string format
	 * 
	 * @param millis
	 *            A duration to convert to a string form
	 * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
	 */
	public static String getDurationBreakdown(long millis)
	{
		if (millis < 0)
		{
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		if (days > 0)
		{
			sb.append(days);
			sb.append("d ");
		}
		if (days > 0 || hours > 0)
		{
			sb.append(hours);
			sb.append("h ");
		}
		if (days > 0 || hours > 0 || minutes > 0)
		{
			sb.append(minutes);
			sb.append("m ");
		}
		sb.append(seconds);
		sb.append("s");

		return (sb.toString());
	}

	public static String getDurationBreakdownDateHour(long millis)
	{
		if (millis < 0)
		{
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);

		StringBuilder sb = new StringBuilder(64);
		if (days > 0)
		{
			sb.append(days);
			sb.append("D ");
		}
		if (days > 0 || hours > 0)
		{
			sb.append(hours);
			sb.append("H ");
		}

		return (sb.toString());
	}

	public static String getDurationBreakdownDaysHourMins(long millis)
	{
		if (millis < 0)
		{
			throw new IllegalArgumentException("Duration must not be less than zero!");
		} else if (millis == 0)
		{
			return "0 Days 0 Hours 0 Mins";
		} else
		{
			long days = TimeUnit.MILLISECONDS.toDays(millis);
			millis -= TimeUnit.DAYS.toMillis(days);
			long hours = TimeUnit.MILLISECONDS.toHours(millis);
			millis -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
			millis -= TimeUnit.MINUTES.toMillis(minutes);

			StringBuilder sb = new StringBuilder(64);
			sb.append(days);
			sb.append(" Days ");
			sb.append(hours);
			sb.append(" Hours ");
			sb.append(minutes);
			sb.append(" Mins");

			return (sb.toString());
		}
	}

	public static String getDurationBreakdownDays(long millis)
	{
		if (millis < 0)
		{
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);

		return days + " Days";
	}

	public static Date getNowDateForEffectiveDateFrom()
	{
		return new Date(new Date().getTime() / 1000 * 1000);
	}

	public Date getFirstDayOfThisWeek()
	{
		return getFirstDayOfWeek(new Date());
	}

	public Date getFirstDayOfWeek(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		return cal.getTime();
	}
	public Date getLastDayOfWeek(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get last of this week
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));

		return cal.getTime();
	}

	// get the beginning of the last day of the week
	public Date getLastDayOfThisWeek()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get last of this week
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));

		return cal.getTime();
	}

	// get the beginning of the next week
	public Date getBeginningOfNextWeek()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get last of this week
		cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));

		cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	public Date getFirstDayOfPreviousWeek()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		// previous week
		cal.add(Calendar.DATE, -7);

		return cal.getTime();
	}

	public Date getLastDayOfPreviousWeek()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		// last day of previous week
		cal.add(Calendar.DATE, -1);

		return cal.getTime();
	}

	public Date getFirstDayOfThisMonth()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	public Date getLastDayOfThisMonth()
	{

		return getLastDayOfMonth(new Date());
	}

	public Date getLastDayOfMonth(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this month
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	public Date getFirstDayOfNextMonth()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this month
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.add(Calendar.DATE, 1);

		return cal.getTime();
	}

	public Date getFirstDayOfPreviousMonth()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// go back a month.
		cal.add(Calendar.MONTH, -1);

		// get start of the month
		cal.set(Calendar.DATE, 1);

		return cal.getTime();
	}

	public Date getLastDayOfPreviousMonth()
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// go back a month.
		cal.add(Calendar.MONTH, -1);

		// get start of the month
		cal.set(Calendar.DATE, 1);

		// get end of month
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	public Date getBeginningDayNew(int day, int month, int year)
	{
		Calendar cal = new GregorianCalendar(timeZone);

		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);

		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		return cal.getTime();
	}

	public Date getBeginningDayNew(Date date)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		return cal.getTime();
	}

	public Date getAdvanceDays(Date date, int days)
	{
		Calendar cal = new GregorianCalendar(timeZone);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of
											// day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// addDays
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	public static Date getMaxDate()
	{
		return new Date(72036854775000L);
	}

	public static Date getMinDate()
	{
		return new Date(-72036854775000L);
	}

	/**
	 * This function shifts the date from one timezone to another. keeping the
	 * dd/mm/yy and time of day values same across both the timezones.
	 * 
	 * @param fromDate
	 * @param source
	 * @param dest
	 * @return
	 * @throws Exception
	 */
	public static Date shiftDate(Date fromDate, TimeZone source, TimeZone dest)
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			formatter.setTimeZone(source);
			String dateString = formatter.format(fromDate);

			formatter.setTimeZone(dest);
			Date result = formatter.parse(dateString);

			return result;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return fromDate;
		}

	}

	// get date count between from date and To date
	public int getDateCountBetweenRange(Date fromDate, Date toDate)
	{
		int count = 0;
		try
		{
			Calendar fromDateCal = new GregorianCalendar();
			fromDateCal.setFirstDayOfWeek(Calendar.MONDAY);
			fromDateCal.setTime(fromDate);
			fromDateCal.setTimeZone(timeZone);

			Calendar toDateCal = new GregorianCalendar();
			toDateCal.setFirstDayOfWeek(Calendar.MONDAY);
			toDateCal.setTime(toDate);
			toDateCal.setTimeZone(timeZone);

			long difference = toDateCal.getTime().getTime() - fromDateCal.getTime().getTime();
			float daysBetween = (difference / (1000 * 60 * 60 * 24));
			count = (int) daysBetween;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}

	// get week No list between from date and To date
	public List<String> getWeekNoBetweenRange(Date fromDate, Date toDate)
	{
		List<String> weekNoList = new ArrayList<>();
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			cal.setTime(getFirstDayOfWeek(fromDate));
			cal.setTimeZone(timeZone);
			Date toDateVal = toDate;
			while (cal.getTime().before(toDate))
			{
				weekNoList.add("" + cal.get(Calendar.WEEK_OF_YEAR) + "/" + (cal.get(Calendar.YEAR)));
				cal.add(Calendar.DATE, 7);
			}
			cal.clear();
			cal.setTime(toDateVal);
			if (!weekNoList.contains("" + cal.get(Calendar.WEEK_OF_YEAR) + "/" + (cal.get(Calendar.YEAR))))
				weekNoList.add("" + cal.get(Calendar.WEEK_OF_YEAR) + "/" + (cal.get(Calendar.YEAR)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return weekNoList;
	}

	// month list between range.
	public List<String> getMonthBetweenRange(Date fromDate, Date toDate)
	{
		List<String> monthList = new ArrayList<>();
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setTime(fromDate);
			cal.setTimeZone(timeZone);
			Date toDateVal = toDate;

			while (cal.getTime().before(toDate))
			{
				monthList.add("" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.YEAR)));
				cal.add(Calendar.MONTH, 1);
			}
			cal.clear();
			cal.setTime(toDateVal);
			if (!monthList.contains("" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.YEAR))))
				monthList.add("" + (cal.get(Calendar.MONTH) + 1) + "/" + (cal.get(Calendar.YEAR)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return monthList;
	}

	// year list between range.
	public List<String> getYearBetweenRange(Date fromDate, Date toDate)
	{
		List<String> yearList = new ArrayList<>();
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setTime(fromDate);
			cal.setTimeZone(timeZone);
			Date toDateVal = toDate;

			while (cal.getTime().before(toDate))
			{
				yearList.add("" + (cal.get(Calendar.YEAR)));
				cal.add(Calendar.YEAR, 1);
			}
			cal.clear();
			cal.setTime(toDateVal);
			if (!yearList.contains("" + (cal.get(Calendar.YEAR))))
				yearList.add("" + (cal.get(Calendar.YEAR)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return yearList;
	}

	public static void main(String[] args)
	{
		try
		{
			Calendar cal = new GregorianCalendar();
			cal.setTimeZone(TimeZone.getTimeZone("IST"));
			cal.set(Calendar.DATE, 10);
			cal.set(Calendar.MONTH, 2);
			cal.set(Calendar.YEAR, 2019);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			System.out.println(cal.getTime());
			Date newDate = shiftDate(cal.getTime(), TimeZone.getTimeZone("IST"), TimeZone.getTimeZone("UTC"));
			System.out.println(newDate);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
