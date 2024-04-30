package com.tathvatech.equipment_calibration.utils;

import com.tathvatech.user.common.UserContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter
{
    public static String dateTimeFormat = "dd-MM-yyyy HH:mm";
    public static String dateTimeFormat12Clock = "dd-MM-yyyy hh:mm a";
    public static String dateTimeFormat24Clock = "dd-MM-yyyy HH:mm";
    public static String dateFormat = "dd-MM-yyyy";
    public static String dateFormat3LetterMonth = "dd-MMM-yyyy";
    public static String dateTimeFormat3LetterMonth24Clock = "dd-MMM-yyyy HH:mm";
    public static String yearFormat = "yyyy";
    public static String dateDbFormat = "yyyy-MM-dd";
    public static String dateTimeFormatWithDayofWeek = "E, dd MMM HH:mm";
    public static String dateformatWithMonthNme = "dd MMMM yyyy";
    public static String timeFormat = "HH:mm";
    public static String dateformatMonthNmeYear = "MMM yyyy";
    private DateFormat yearFormatter;
    private DateFormat dateFormatter;
    private DateFormat dateFormatterWith3LetterMonth;
    private DateFormat dateTimeFormatterWith3LetterMonth;
    private DateFormat dateTimeFormatter;
    private DateFormat dateTimeFormatWithDayofWeekFormatter;
    private DateFormat dateDbFormatter;
    private DateFormat dateformatterWithMonthNme;
    private DateFormat dateformatterMonthNmeYear;
    private DateFormat timeFormatter;

    public static DateFormatter getInstance(TimeZone timeZone)
    {
        DateFormatter dt = new DateFormatter();
        dt.yearFormatter.setTimeZone(timeZone);
        dt.dateFormatter.setTimeZone(timeZone);
        dt.dateTimeFormatter.setTimeZone(timeZone);
        dt.dateDbFormatter.setTimeZone(timeZone);
        dt.dateTimeFormatWithDayofWeekFormatter.setTimeZone(timeZone);
        dt.dateFormatterWith3LetterMonth.setTimeZone(timeZone);
        dt.dateTimeFormatterWith3LetterMonth.setTimeZone(timeZone);
        dt.dateformatterWithMonthNme.setTimeZone(timeZone);
        dt.timeFormatter.setTimeZone(timeZone);
        dt.dateformatterMonthNmeYear.setTimeZone(timeZone);
        return dt;
    }

    public static DateFormatter getInstance(UserContext userContext)
    {
        return getInstance(userContext.getTimeZone());
    }

    private DateFormatter()
    {
        yearFormatter = new SimpleDateFormat("yyyy");
        dateFormatter = new SimpleDateFormat(dateFormat);
        dateTimeFormatter = new SimpleDateFormat(dateTimeFormat);
        dateDbFormatter = new SimpleDateFormat(dateDbFormat);
        dateTimeFormatWithDayofWeekFormatter = new SimpleDateFormat(dateTimeFormatWithDayofWeek);
        dateFormatterWith3LetterMonth = new SimpleDateFormat(dateFormat3LetterMonth);
        dateTimeFormatterWith3LetterMonth = new SimpleDateFormat(dateTimeFormat3LetterMonth24Clock);
        dateformatterWithMonthNme = new SimpleDateFormat(dateformatWithMonthNme);
        dateformatterMonthNmeYear = new SimpleDateFormat(dateformatMonthNmeYear);
        timeFormatter = new SimpleDateFormat(timeFormat);
    }

    public DateFormat getYearFormatter()
    {
        return yearFormatter;
    }

    public DateFormat getDateFormatter()
    {
        return dateFormatter;
    }

    public DateFormat getDateTimeFormatter()
    {
        return dateTimeFormatter;
    }

    public DateFormat getDateDbFormatter()
    {
        return dateDbFormatter;
    }

    public DateFormat getMonthNameYearFormatter()
    {
        return dateformatterMonthNmeYear;
    }

    public Date parseDate(String dateString) throws Exception
    {
        return dateFormatter.parse(dateString);
    }

    public Date parseDateTime(String dateTimeString) throws Exception
    {
        return dateTimeFormatter.parse(dateTimeString);
    }

    public String formatTime(Date date)
    {
        if (date == null)
            return "";
        return timeFormatter.format(date);
    }

    public String formatDate(Date date)
    {
        if (date == null)
            return "";
        return dateFormatter.format(date);
    }

    public String formatDate3LetterMonth(Date date)
    {
        if (date == null)
            return "";
        return dateFormatterWith3LetterMonth.format(date);
    }

    public String formatDateTime3LetterMonth(Date date)
    {
        if (date == null)
            return "";
        return dateTimeFormatterWith3LetterMonth.format(date);
    }

    public String formatDateTime(Date date)
    {
        return dateTimeFormatter.format(date);
    }

    public String dateTimeFormatWithDayofWeek(Date date)
    {
        return dateTimeFormatWithDayofWeekFormatter.format(date);
    }

    public String formatDateDb(Date date)
    {
        return dateDbFormatter.format(date);
    }

    public String getYear(Date date)
    {
        return yearFormatter.format(date);
    }

    public String formatDateWithMonthNme(Date date)
    {
        if (date == null)
            return "";
        return dateformatterWithMonthNme.format(date);
    }

    public String formatDateMonthNmeYear(Date date)
    {
        if (date == null)
            return "";
        return dateformatterMonthNmeYear.format(date);
    }

}

