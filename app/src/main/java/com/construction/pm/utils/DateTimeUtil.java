package com.construction.pm.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateTimeUtil {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String DATE_DISPLAY_FORMAT = "dd-MM-yyyy";

    public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATETIME_DISPLAY_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static String TIME_FORMAT = "HH:mm:ss";
    public static String TIME_DISPLAY_FORMAT = "HH:mm:ss";

    /* ------------------------ */
    /* -- From ... To String -- */
    /* ------------------------ */

    public static String ToDateString(final Calendar calendar) {
        return ToStringFormat(calendar, DATE_FORMAT);
    }

    public static String ToDateTimeString(final Calendar calendar) {
        return ToStringFormat(calendar, DATETIME_FORMAT);
    }

    public static String ToTimeString(final Calendar calendar) {
        return ToStringFormat(calendar, TIME_FORMAT);
    }

    public static String ToDateString(final String textDateDisplay) {
        return ToDateString(FromDateDisplayString(textDateDisplay));
    }

    public static String ToDateTimeString(final String textDateTimeDisplay) {
        return ToDateTimeString(FromDateTimeDisplayString(textDateTimeDisplay));
    }

    public static String ToTimeString(final String textTimeDisplay) {
        return ToTimeString(FromTimeDisplayString(textTimeDisplay));
    }

    public static String ToDateDisplayString(final Calendar calendar) {
        return ToStringFormat(calendar, DATE_DISPLAY_FORMAT);
    }

    public static String ToDateTimeDisplayString(final Calendar calendar) {
        return ToStringFormat(calendar, DATETIME_DISPLAY_FORMAT);
    }

    public static String ToTimeDisplayString(final Calendar calendar) {
        return ToStringFormat(calendar, TIME_DISPLAY_FORMAT);
    }

    public static String ToDateDisplayString(final String textDate) {
        return ToDateDisplayString(FromDateString(textDate));
    }

    public static String ToDateTimeDisplayString(final String textDateTime) {
        return ToDateTimeDisplayString(FromDateTimeString(textDateTime));
    }

    public static String ToTimeDisplayString(final String textTime) {
        return ToTimeDisplayString(FromTimeString(textTime));
    }

    public static String ToStringFormat(final Calendar calendar, final String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (calendar != null)
            return simpleDateFormat.format(calendar.getTime());
        else
            return null;
    }

    public static String ToDateTimeDisplaySimpleString(final Calendar calendar) {
        int years = calendar.get(Calendar.YEAR) - 1970;
        int months = calendar.get(Calendar.MONTH);
        int days = calendar.get(Calendar.DAY_OF_MONTH) - 1;
        int hours = calendar.get(Calendar.HOUR_OF_DAY) - 7;
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        List<String> separateDateList = new ArrayList<String>();
        if (years > 0)
            separateDateList.add(years > 1 ? String.valueOf(years) + " years" : "a year");
        else if (months > 0)
            separateDateList.add(months > 1 ? String.valueOf(months) + " months" : "a month");
        else if (days > 0)
            separateDateList.add(days > 1 ? String.valueOf(days) + " days" : "a day");
        else if (hours > 0)
            separateDateList.add(hours > 1 ? String.valueOf(hours) + " hours" : "an hour");
        else if (minutes > 0)
            separateDateList.add(minutes > 1 ? String.valueOf(minutes) + " minutes" : "a minute");
        else if (seconds > 0)
            separateDateList.add(seconds > 1 ? String.valueOf(seconds) + " seconds" : "a second");

        String result = null;
        if (separateDateList.size() > 0) {
            String[] separateDate = new String[separateDateList.size()];
            separateDateList.toArray(separateDate);
            result = TextUtils.join(" ", separateDate);
        }
        return result;
    }

    public static String ToTimeDisplaySimpleString(final Calendar fromCalendar, final Calendar toCalendar) {
        if (fromCalendar == null || toCalendar == null)
            return null;

        Calendar nowDate = Calendar.getInstance(fromCalendar.getTimeZone());
        nowDate.setTimeInMillis(toCalendar.getTimeInMillis());

        long dateDiffMillis = nowDate.getTimeInMillis() - fromCalendar.getTimeInMillis();
        Calendar dateDiff = Calendar.getInstance(fromCalendar.getTimeZone());
        dateDiff.setTimeInMillis(dateDiffMillis);

        String result = ToStringFormat(fromCalendar, "HHa");
        int days = dateDiff.get(Calendar.DAY_OF_MONTH) - 1;
        if (days > 0) {
            result += " - " + ToStringFormat(toCalendar, "HHa, d MMM");
        } else {
            result += " - " + ToStringFormat(toCalendar, "HHa");
        }
        return result;
    }

    /* -------------------------- */
    /* -- From ... To Calendar -- */
    /* -------------------------- */

    public static Calendar FromDateString(final String text) {
        return FromString(text, DATE_FORMAT);
    }

    public static Calendar FromDateTimeString(final String text) {
        return FromString(text, DATETIME_FORMAT);
    }

    public static Calendar FromTimeString(final String text) {
        return FromString(text, TIME_FORMAT);
    }

    public static Calendar FromDateDisplayString(final String text) {
        return FromString(text, DATE_DISPLAY_FORMAT);
    }

    public static Calendar FromDateTimeDisplayString(final String text) {
        return FromString(text, DATETIME_DISPLAY_FORMAT);
    }

    public static Calendar FromTimeDisplayString(final String text) {
        return FromString(text, TIME_DISPLAY_FORMAT);
    }

    public static Calendar FromString(final String text, final String format) {
        Calendar calendar;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        try {
            calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(text));
        }
        catch (Exception ex) {
            calendar = null;
        }

        return calendar;
    }

    public static Calendar FromDate(final int year, final int monthOfYear, final int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return calendar;
    }

    public static Calendar FromDateTime(final int year, final int monthOfYear, final int dayOfMonth, final int hour, final int minute, final int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar;
    }

    public static Calendar FromTime(final int hour, final int minute, final int second) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);

        return calendar;
    }
}
