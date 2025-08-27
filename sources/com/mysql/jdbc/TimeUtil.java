package com.mysql.jdbc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import org.apache.xmlbeans.SchemaType;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/TimeUtil.class */
public class TimeUtil {
    private static final String TIME_ZONE_MAPPINGS_RESOURCE = "/com/mysql/jdbc/TimeZoneMapping.properties";
    protected static final Method systemNanoTimeMethod;
    static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getDefault();
    private static Properties timeZoneMappings = null;

    static {
        Method aMethod;
        try {
            aMethod = System.class.getMethod("nanoTime", (Class[]) null);
        } catch (NoSuchMethodException e) {
            aMethod = null;
        } catch (SecurityException e2) {
            aMethod = null;
        }
        systemNanoTimeMethod = aMethod;
    }

    public static boolean nanoTimeAvailable() {
        return systemNanoTimeMethod != null;
    }

    public static final TimeZone getDefaultTimeZone(boolean useCache) {
        return (TimeZone) (useCache ? DEFAULT_TIMEZONE.clone() : TimeZone.getDefault().clone());
    }

    public static long getCurrentTimeNanosOrMillis() {
        if (systemNanoTimeMethod != null) {
            try {
                return ((Long) systemNanoTimeMethod.invoke(null, (Object[]) null)).longValue();
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
        return System.currentTimeMillis();
    }

    public static Time changeTimezone(MySQLConnection conn, Calendar sessionCalendar, Calendar targetCalendar, Time t, TimeZone fromTz, TimeZone toTz, boolean rollForward) {
        long toTime;
        if (conn != null) {
            if (conn.getUseTimezone() && !conn.getNoTimezoneConversionForTimeType()) {
                Calendar fromCal = Calendar.getInstance(fromTz);
                fromCal.setTime(t);
                int fromOffset = fromCal.get(15) + fromCal.get(16);
                Calendar toCal = Calendar.getInstance(toTz);
                toCal.setTime(t);
                int toOffset = toCal.get(15) + toCal.get(16);
                int offsetDiff = fromOffset - toOffset;
                long toTime2 = toCal.getTime().getTime();
                if (rollForward) {
                    toTime = toTime2 + offsetDiff;
                } else {
                    toTime = toTime2 - offsetDiff;
                }
                Time changedTime = new Time(toTime);
                return changedTime;
            }
            if (conn.getUseJDBCCompliantTimezoneShift() && targetCalendar != null) {
                Time adjustedTime = new Time(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, t));
                return adjustedTime;
            }
        }
        return t;
    }

    public static Timestamp changeTimezone(MySQLConnection conn, Calendar sessionCalendar, Calendar targetCalendar, Timestamp tstamp, TimeZone fromTz, TimeZone toTz, boolean rollForward) {
        long toTime;
        if (conn != null) {
            if (conn.getUseTimezone()) {
                Calendar fromCal = Calendar.getInstance(fromTz);
                fromCal.setTime(tstamp);
                int fromOffset = fromCal.get(15) + fromCal.get(16);
                Calendar toCal = Calendar.getInstance(toTz);
                toCal.setTime(tstamp);
                int toOffset = toCal.get(15) + toCal.get(16);
                int offsetDiff = fromOffset - toOffset;
                long toTime2 = toCal.getTime().getTime();
                if (rollForward) {
                    toTime = toTime2 + offsetDiff;
                } else {
                    toTime = toTime2 - offsetDiff;
                }
                Timestamp changedTimestamp = new Timestamp(toTime);
                return changedTimestamp;
            }
            if (conn.getUseJDBCCompliantTimezoneShift() && targetCalendar != null) {
                Timestamp adjustedTimestamp = new Timestamp(jdbcCompliantZoneShift(sessionCalendar, targetCalendar, tstamp));
                adjustedTimestamp.setNanos(tstamp.getNanos());
                return adjustedTimestamp;
            }
        }
        return tstamp;
    }

    private static long jdbcCompliantZoneShift(Calendar sessionCalendar, Calendar targetCalendar, Date dt) {
        long time;
        if (sessionCalendar == null) {
            sessionCalendar = new GregorianCalendar();
        }
        synchronized (sessionCalendar) {
            Date origCalDate = targetCalendar.getTime();
            Date origSessionDate = sessionCalendar.getTime();
            try {
                sessionCalendar.setTime(dt);
                targetCalendar.set(1, sessionCalendar.get(1));
                targetCalendar.set(2, sessionCalendar.get(2));
                targetCalendar.set(5, sessionCalendar.get(5));
                targetCalendar.set(11, sessionCalendar.get(11));
                targetCalendar.set(12, sessionCalendar.get(12));
                targetCalendar.set(13, sessionCalendar.get(13));
                targetCalendar.set(14, sessionCalendar.get(14));
                time = targetCalendar.getTime().getTime();
            } finally {
                sessionCalendar.setTime(origSessionDate);
                targetCalendar.setTime(origCalDate);
            }
        }
        return time;
    }

    static final java.sql.Date fastDateCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day) {
        java.sql.Date date;
        Calendar dateCal = cal;
        if (useGmtConversion) {
            if (gmtCalIfNeeded == null) {
                gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            }
            dateCal = gmtCalIfNeeded;
        }
        synchronized (dateCal) {
            Date origCalDate = dateCal.getTime();
            try {
                dateCal.clear();
                dateCal.set(14, 0);
                dateCal.set(year, month - 1, day, 0, 0, 0);
                long dateAsMillis = dateCal.getTimeInMillis();
                date = new java.sql.Date(dateAsMillis);
            } finally {
                dateCal.setTime(origCalDate);
            }
        }
        return date;
    }

    static final java.sql.Date fastDateCreate(int year, int month, int day, Calendar targetCalendar) {
        java.sql.Date date;
        Calendar dateCal = targetCalendar == null ? new GregorianCalendar() : targetCalendar;
        synchronized (dateCal) {
            Date origCalDate = dateCal.getTime();
            try {
                dateCal.clear();
                dateCal.set(year, month - 1, day, 0, 0, 0);
                dateCal.set(14, 0);
                long dateAsMillis = dateCal.getTimeInMillis();
                date = new java.sql.Date(dateAsMillis);
            } finally {
                dateCal.setTime(origCalDate);
            }
        }
        return date;
    }

    static final Time fastTimeCreate(Calendar cal, int hour, int minute, int second, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        Time time;
        if (hour < 0 || hour > 24) {
            throw SQLError.createSQLException("Illegal hour value '" + hour + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        if (minute < 0 || minute > 59) {
            throw SQLError.createSQLException("Illegal minute value '" + minute + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        if (second < 0 || second > 59) {
            throw SQLError.createSQLException("Illegal minute value '" + second + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        synchronized (cal) {
            Date origCalDate = cal.getTime();
            try {
                cal.clear();
                cal.set(1970, 0, 1, hour, minute, second);
                long timeAsMillis = cal.getTimeInMillis();
                time = new Time(timeAsMillis);
            } finally {
                cal.setTime(origCalDate);
            }
        }
        return time;
    }

    static final Time fastTimeCreate(int hour, int minute, int second, Calendar targetCalendar, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        Time time;
        if (hour < 0 || hour > 23) {
            throw SQLError.createSQLException("Illegal hour value '" + hour + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        if (minute < 0 || minute > 59) {
            throw SQLError.createSQLException("Illegal minute value '" + minute + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        if (second < 0 || second > 59) {
            throw SQLError.createSQLException("Illegal minute value '" + second + "' for java.sql.Time type in value '" + timeFormattedString(hour, minute, second) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        Calendar cal = targetCalendar == null ? new GregorianCalendar() : targetCalendar;
        synchronized (cal) {
            Date origCalDate = cal.getTime();
            try {
                cal.clear();
                cal.set(1970, 0, 1, hour, minute, second);
                long timeAsMillis = cal.getTimeInMillis();
                time = new Time(timeAsMillis);
            } finally {
                cal.setTime(origCalDate);
            }
        }
        return time;
    }

    static final Timestamp fastTimestampCreate(boolean useGmtConversion, Calendar gmtCalIfNeeded, Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart) {
        Timestamp ts;
        synchronized (cal) {
            Date origCalDate = cal.getTime();
            try {
                cal.clear();
                cal.set(year, month - 1, day, hour, minute, seconds);
                int offsetDiff = 0;
                if (useGmtConversion) {
                    int fromOffset = cal.get(15) + cal.get(16);
                    if (gmtCalIfNeeded == null) {
                        gmtCalIfNeeded = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                    }
                    gmtCalIfNeeded.clear();
                    gmtCalIfNeeded.setTimeInMillis(cal.getTimeInMillis());
                    int toOffset = gmtCalIfNeeded.get(15) + gmtCalIfNeeded.get(16);
                    offsetDiff = fromOffset - toOffset;
                }
                if (secondsPart != 0) {
                    cal.set(14, secondsPart / SchemaType.SIZE_BIG_INTEGER);
                }
                long tsAsMillis = cal.getTimeInMillis();
                ts = new Timestamp(tsAsMillis + offsetDiff);
                ts.setNanos(secondsPart);
            } finally {
                cal.setTime(origCalDate);
            }
        }
        return ts;
    }

    static final Timestamp fastTimestampCreate(TimeZone tz, int year, int month, int day, int hour, int minute, int seconds, int secondsPart) {
        Calendar cal = tz == null ? new GregorianCalendar() : new GregorianCalendar(tz);
        cal.clear();
        cal.set(year, month - 1, day, hour, minute, seconds);
        long tsAsMillis = cal.getTimeInMillis();
        Timestamp ts = new Timestamp(tsAsMillis);
        ts.setNanos(secondsPart);
        return ts;
    }

    public static String getCanonicalTimezone(String timezoneStr, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        if (timezoneStr == null) {
            return null;
        }
        String timezoneStr2 = timezoneStr.trim();
        if (timezoneStr2.length() > 2 && ((timezoneStr2.charAt(0) == '+' || timezoneStr2.charAt(0) == '-') && Character.isDigit(timezoneStr2.charAt(1)))) {
            return "GMT" + timezoneStr2;
        }
        synchronized (TimeUtil.class) {
            if (timeZoneMappings == null) {
                loadTimeZoneMappings(exceptionInterceptor);
            }
        }
        String canonicalTz = timeZoneMappings.getProperty(timezoneStr2);
        if (canonicalTz != null) {
            return canonicalTz;
        }
        throw SQLError.createSQLException(Messages.getString("TimeUtil.UnrecognizedTimezoneId", new Object[]{timezoneStr2}), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, exceptionInterceptor);
    }

    private static String timeFormattedString(int hours, int minutes, int seconds) {
        StringBuilder buf = new StringBuilder(8);
        if (hours < 10) {
            buf.append("0");
        }
        buf.append(hours);
        buf.append(":");
        if (minutes < 10) {
            buf.append("0");
        }
        buf.append(minutes);
        buf.append(":");
        if (seconds < 10) {
            buf.append("0");
        }
        buf.append(seconds);
        return buf.toString();
    }

    public static Timestamp adjustTimestampNanosPrecision(Timestamp ts, int fsp, boolean serverRoundFracSecs) throws SQLException {
        int nanos;
        if (fsp < 0 || fsp > 6) {
            throw SQLError.createSQLException("fsp value must be in 0 to 6 range.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
        }
        Timestamp res = (Timestamp) ts.clone();
        int nanos2 = res.getNanos();
        double tail = Math.pow(10.0d, 9 - fsp);
        if (serverRoundFracSecs) {
            nanos = ((int) Math.round(nanos2 / tail)) * ((int) tail);
            if (nanos > 999999999) {
                nanos %= 1000000000;
                res.setTime(res.getTime() + 1000);
            }
        } else {
            nanos = ((int) (nanos2 / tail)) * ((int) tail);
        }
        res.setNanos(nanos);
        return res;
    }

    public static String formatNanos(int nanos, boolean serverSupportsFracSecs, int fsp) throws SQLException {
        int nanos2;
        if (nanos < 0 || nanos > 999999999) {
            throw SQLError.createSQLException("nanos value must be in 0 to 999999999 range but was " + nanos, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
        }
        if (fsp < 0 || fsp > 6) {
            throw SQLError.createSQLException("fsp value must be in 0 to 6 range but was " + fsp, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
        }
        if (!serverSupportsFracSecs || fsp == 0 || nanos == 0 || (nanos2 = (int) (nanos / Math.pow(10.0d, 9 - fsp))) == 0) {
            return "0";
        }
        String nanosString = Integer.toString(nanos2);
        String nanosString2 = "000000000".substring(0, fsp - nanosString.length()) + nanosString;
        int pos = fsp - 1;
        while (nanosString2.charAt(pos) == '0') {
            pos--;
        }
        return nanosString2.substring(0, pos + 1);
    }

    private static void loadTimeZoneMappings(ExceptionInterceptor exceptionInterceptor) throws SQLException, IOException {
        timeZoneMappings = new Properties();
        try {
            timeZoneMappings.load(TimeUtil.class.getResourceAsStream(TIME_ZONE_MAPPINGS_RESOURCE));
            String[] arr$ = TimeZone.getAvailableIDs();
            for (String tz : arr$) {
                if (!timeZoneMappings.containsKey(tz)) {
                    timeZoneMappings.put(tz, tz);
                }
            }
        } catch (IOException e) {
            throw SQLError.createSQLException(Messages.getString("TimeUtil.LoadTimeZoneMappingError"), SQLError.SQL_STATE_INVALID_CONNECTION_ATTRIBUTE, exceptionInterceptor);
        }
    }

    public static Timestamp truncateFractionalSeconds(Timestamp timestamp) {
        Timestamp truncatedTimestamp = new Timestamp(timestamp.getTime());
        truncatedTimestamp.setNanos(0);
        return truncatedTimestamp;
    }

    public static SimpleDateFormat getSimpleDateFormat(SimpleDateFormat cachedSimpleDateFormat, String pattern, Calendar cal, TimeZone tz) {
        SimpleDateFormat sdf = cachedSimpleDateFormat != null ? cachedSimpleDateFormat : new SimpleDateFormat(pattern, Locale.US);
        if (cal != null) {
            sdf.setCalendar((Calendar) cal.clone());
        }
        if (tz != null) {
            sdf.setTimeZone(tz);
        }
        return sdf;
    }

    public static Calendar setProlepticIfNeeded(Calendar origCalendar, Calendar refCalendar) {
        if (origCalendar != null && refCalendar != null && (origCalendar instanceof GregorianCalendar) && (refCalendar instanceof GregorianCalendar) && ((GregorianCalendar) refCalendar).getGregorianChange().getTime() == Long.MIN_VALUE) {
            origCalendar = (GregorianCalendar) origCalendar.clone();
            ((GregorianCalendar) origCalendar).setGregorianChange(new java.sql.Date(Long.MIN_VALUE));
            origCalendar.clear();
        }
        return origCalendar;
    }
}
