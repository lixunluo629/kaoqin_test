package com.moredian.onpremise.core.utils;

import com.moredian.onpremise.core.common.constants.DateFormatConstants;
import com.moredian.onpremise.core.common.enums.OnpremiseErrorEnum;
import com.moredian.onpremise.core.exception.BizException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/MyDateUtils.class */
public class MyDateUtils extends DateUtils {
    public static final String TIME_OF_DAY_BEGIN = " 00:00:00";
    public static final String TIME_OF_DAY_END = " 23:59:59";
    public static final String TIME_OF_DAY_BEGIN_SHORT = " 00:00";
    public static final String TIME_OF_DAY_SHORT = " 23:59";
    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", DateFormatConstants.DATE_FOR_DIAGONAL, com.alibaba.excel.util.DateUtils.DATE_FORMAT_19_FORWARD_SLASH, "yyyy-MM-dd HH:mm:ss.SSS"};
    private static Date startTime = null;
    private static Date nextStartTime = null;
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYYMMDDHH = "yyyyMMddHH";

    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    public static String formatDate(Date date, Object... pattern) {
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static String getTime() {
        return formatDate(new Date(), DateFormatConstants.TIME_FOR_COLON);
    }

    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    public static int getMonth() {
        return getMonth(new Date());
    }

    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    public static String getDay(Date date) {
        return formatDate(date, "dd");
    }

    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    public static Date parseDate(String str) {
        try {
            return parseDate(str, parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String str, String pattern) {
        try {
            return parseDate(str, new String[]{pattern});
        } catch (ParseException e) {
            return null;
        }
    }

    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / 86400000;
    }

    public static long getTimeStampSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getTimeStampMills() {
        return System.currentTimeMillis();
    }

    public static String getCurrTimeStampString() {
        return String.valueOf(getTimeStampSeconds());
    }

    public static long getTimeStampSeconds(Date date) {
        return date.getTime() / 1000;
    }

    public static Date getDate(long timeStamp) {
        Date date = new Date();
        date.setTime(timeStamp);
        return date;
    }

    public static Date getDate(long timeStamp, Object... pattern) {
        Date date = new Date();
        date.setTime(timeStamp);
        return date;
    }

    public static String getDateString(long timeStamp, Object... pattern) {
        return formatDate(getDate(timeStamp), pattern);
    }

    public static String getDateString(String timeStamp) {
        return (timeStamp == null || "".equals(timeStamp)) ? "" : getDateString(Long.valueOf(timeStamp).longValue(), parsePatterns[1]);
    }

    public static String getSimpleDateString(String timeStamp) {
        return (timeStamp == null || "".equals(timeStamp)) ? "" : getDateString(Long.valueOf(timeStamp).longValue(), parsePatterns[0]);
    }

    public static int getDateLong() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String addDays(String now, int days) {
        Date nowDate = getDate(Long.valueOf(now).longValue());
        Date newDate = addDays(nowDate, days);
        return String.valueOf(getTimeStampSeconds(newDate));
    }

    public static String addMonths(String now, int months) {
        Date nowDate = getDate(Long.valueOf(now).longValue());
        Date newDate = addMonths(nowDate, months);
        return String.valueOf(getTimeStampSeconds(newDate));
    }

    public static Date addTime(int type, Date nowDate, int amount) {
        Date retDate = nowDate;
        if (1 == type) {
            retDate = addDays(nowDate, amount);
        } else if (2 == type) {
            retDate = addMonths(nowDate, amount);
        } else if (3 == type) {
            retDate = addMonths(nowDate, amount * 3);
        } else if (4 == type) {
            retDate = addYears(nowDate, amount);
        }
        return retDate;
    }

    public static Date setDayZero(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(11, 0);
        return cal.getTime();
    }

    public static Date setDayLast(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(12, 59);
        cal.set(13, 59);
        cal.set(11, 23);
        return cal.getTime();
    }

    public static int getHours(String time) {
        return Integer.valueOf(time.split(":")[0]).intValue();
    }

    public static int revisalHours(int hours) {
        return hours >= 24 ? hours - 24 : ((long) hours) < 0 ? hours + 24 : hours;
    }

    public static String revisalMinutes(int minutes) {
        return minutes >= 10 ? minutes + "" : "0" + minutes;
    }

    public static int getMinutes(String time) {
        return Integer.valueOf(time.split(":")[1]).intValue();
    }

    public static Timestamp getBeginTimeOfDate(String beginDate) {
        return Timestamp.valueOf(beginDate + TIME_OF_DAY_BEGIN);
    }

    public static Timestamp getEndTimeOfDate(String endDate) {
        return Timestamp.valueOf(endDate + TIME_OF_DAY_END);
    }

    public static Date getLastDayStartTime(Date date) {
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(date);
        lastDay.add(5, -1);
        lastDay.set(11, 0);
        lastDay.set(12, 0);
        lastDay.set(13, 0);
        lastDay.set(14, 0);
        startTime = lastDay.getTime();
        return startTime;
    }

    public static Date getDayStartTime(Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        today.set(11, 0);
        today.set(12, 0);
        today.set(13, 0);
        today.set(14, 0);
        startTime = today.getTime();
        return startTime;
    }

    public static Date getNextDayStartTime(Date date) {
        Calendar today = Calendar.getInstance();
        today.setTime(date);
        today.set(11, 0);
        today.set(12, 0);
        today.set(13, 0);
        today.set(14, 0);
        today.add(5, 1);
        nextStartTime = today.getTime();
        return nextStartTime;
    }

    public static Date getWeekStartTime(Date date) {
        Calendar weekStart = Calendar.getInstance();
        weekStart.setTime(date);
        weekStart.set(7, 2);
        weekStart.set(11, 0);
        weekStart.set(12, 0);
        weekStart.set(13, 0);
        weekStart.set(14, 0);
        Date startTime2 = weekStart.getTime();
        return startTime2;
    }

    public static Date getNextWeekStartTime(Date date) {
        Calendar weekStart = Calendar.getInstance();
        weekStart.setTime(date);
        weekStart.set(7, 2);
        weekStart.set(11, 0);
        weekStart.set(12, 0);
        weekStart.set(13, 0);
        weekStart.set(14, 0);
        weekStart.add(4, 1);
        Date nextStartTime2 = weekStart.getTime();
        return nextStartTime2;
    }

    public static Date getMonthStartTime(Date date) {
        Calendar monthStart = Calendar.getInstance();
        monthStart.setTime(date);
        monthStart.set(5, 1);
        monthStart.set(11, 0);
        monthStart.set(12, 0);
        monthStart.set(13, 0);
        monthStart.set(14, 0);
        startTime = monthStart.getTime();
        return startTime;
    }

    public static Date getNextMonthStartTime(Date date) {
        Calendar monthStart = Calendar.getInstance();
        monthStart.setTime(date);
        monthStart.set(5, 1);
        monthStart.set(11, 0);
        monthStart.set(12, 0);
        monthStart.set(13, 0);
        monthStart.set(14, 0);
        monthStart.add(2, 1);
        nextStartTime = monthStart.getTime();
        return nextStartTime;
    }

    public static Date getLastMonthStartTime(Date date) {
        Calendar monthStart = Calendar.getInstance();
        monthStart.setTime(date);
        monthStart.set(5, 1);
        monthStart.set(11, 0);
        monthStart.set(12, 0);
        monthStart.set(13, 0);
        monthStart.set(14, 0);
        monthStart.add(2, -1);
        nextStartTime = monthStart.getTime();
        return nextStartTime;
    }

    public static Date getYearStartTime(Date date) {
        Calendar yearStart = Calendar.getInstance();
        yearStart.setTime(date);
        yearStart.set(6, 1);
        yearStart.set(11, 0);
        yearStart.set(12, 0);
        yearStart.set(13, 0);
        yearStart.set(14, 0);
        startTime = yearStart.getTime();
        return startTime;
    }

    public static Date getNextYearStartTime(Date date) {
        Calendar yearStart = Calendar.getInstance();
        yearStart.setTime(date);
        yearStart.set(6, 1);
        yearStart.set(11, 0);
        yearStart.set(12, 0);
        yearStart.set(13, 0);
        yearStart.set(14, 0);
        yearStart.add(1, 1);
        nextStartTime = yearStart.getTime();
        return nextStartTime;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    public static Date getMonthStartTime(Date date, int i) {
        Calendar monthStart = Calendar.getInstance();
        monthStart.setTime(date);
        monthStart.set(5, 1);
        monthStart.set(11, 0);
        monthStart.set(12, 0);
        monthStart.set(13, 0);
        monthStart.set(14, 0);
        monthStart.add(2, i);
        nextStartTime = monthStart.getTime();
        return nextStartTime;
    }

    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date smdate2 = sdf.parse(sdf.format(smdate));
        Date bdate2 = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate2);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate2);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / 86400000;
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    public static int daysOfTwo(Date startDate, Date endDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(startDate);
        int day1 = aCalendar.get(6);
        aCalendar.setTime(endDate);
        int day2 = aCalendar.get(6);
        return Math.abs(day2 - day1);
    }

    public static Boolean getDateRange(String beginDate, String endDate) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long newDate = date.getTime();
        long begin = 0;
        long end = 0;
        try {
            c.setTime(sdf.parse(beginDate));
            begin = c.getTimeInMillis();
            c.setTime(sdf.parse(endDate));
            end = c.getTimeInMillis();
        } catch (ParseException var12) {
            var12.printStackTrace();
        }
        return newDate >= begin && newDate < end;
    }

    public static Date getLastDay(Date date) {
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(date);
        lastDay.add(5, -1);
        return lastDay.getTime();
    }

    public static Date getMonthTime(String queryMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        if (queryMonth == null) {
            queryMonth = sdf.format(new Date());
        }
        try {
            Date monthTime = sdf.parse(queryMonth);
            return monthTime;
        } catch (ParseException var4) {
            new BizException(OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), var4.getMessage());
            return null;
        }
    }

    public static int getDaysOfMonth(String queryMonth) {
        new SimpleDateFormat("yyyy年MM月");
        try {
            int days = daysBetween(getMonthStartTime(getMonthTime(queryMonth)), getNextMonthStartTime(getMonthTime(queryMonth)));
            return days;
        } catch (ParseException var4) {
            new BizException(OnpremiseErrorEnum.SYSTEM_ERROR.getErrorCode(), var4.getMessage());
            return 0;
        }
    }

    public static boolean isValidDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean checkHHMMTime(String time) throws NumberFormatException {
        if (checkHHMM(time)) {
            String[] temp = time.split(":");
            if ((temp[0].length() == 2 || temp[0].length() == 1) && temp[1].length() == 2) {
                try {
                    int h = Integer.parseInt(temp[0]);
                    int m = Integer.parseInt(temp[1]);
                    if (h >= 0 && h <= 24 && m <= 60 && m >= 0) {
                        return true;
                    }
                    return false;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    private static boolean checkHHMM(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
        try {
            dateFormat.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Integer formatIntegerDay(Long timeInMillis) {
        Date date = new Date(timeInMillis.longValue());
        return Integer.valueOf(Integer.parseInt(format("yyyyMMdd", date)));
    }

    public static Integer formatIntegerDay(Date date) {
        return Integer.valueOf(Integer.parseInt(format("yyyyMMdd", date)));
    }

    public static Integer formatIntegerDayHour(Date date) {
        return Integer.valueOf(Integer.parseInt(format(FORMAT_YYYYMMDDHH, date)));
    }

    public static String format(String format, Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }
}
