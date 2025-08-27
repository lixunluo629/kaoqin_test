package com.mysql.jdbc;

import com.itextpdf.layout.element.List;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ResultSetRow.class */
public abstract class ResultSetRow {
    protected ExceptionInterceptor exceptionInterceptor;
    protected Field[] metadata;

    public abstract void closeOpenStreams();

    public abstract InputStream getBinaryInputStream(int i) throws SQLException;

    public abstract byte[] getColumnValue(int i) throws SQLException;

    public abstract Date getDateFast(int i, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl, Calendar calendar) throws SQLException;

    public abstract int getInt(int i) throws SQLException;

    public abstract long getLong(int i) throws SQLException;

    public abstract Date getNativeDate(int i, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl, Calendar calendar) throws SQLException;

    public abstract Object getNativeDateTimeValue(int i, Calendar calendar, int i2, int i3, TimeZone timeZone, boolean z, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl) throws SQLException;

    public abstract double getNativeDouble(int i) throws SQLException;

    public abstract float getNativeFloat(int i) throws SQLException;

    public abstract int getNativeInt(int i) throws SQLException;

    public abstract long getNativeLong(int i) throws SQLException;

    public abstract short getNativeShort(int i) throws SQLException;

    public abstract Time getNativeTime(int i, Calendar calendar, TimeZone timeZone, boolean z, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl) throws SQLException;

    public abstract Timestamp getNativeTimestamp(int i, Calendar calendar, TimeZone timeZone, boolean z, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl) throws SQLException;

    public abstract Reader getReader(int i) throws SQLException;

    public abstract String getString(int i, String str, MySQLConnection mySQLConnection) throws SQLException;

    public abstract Time getTimeFast(int i, Calendar calendar, TimeZone timeZone, boolean z, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl) throws SQLException;

    public abstract Timestamp getTimestampFast(int i, Calendar calendar, TimeZone timeZone, boolean z, MySQLConnection mySQLConnection, ResultSetImpl resultSetImpl, boolean z2, boolean z3) throws SQLException;

    public abstract boolean isFloatingPointNumber(int i) throws SQLException;

    public abstract boolean isNull(int i) throws SQLException;

    public abstract long length(int i) throws SQLException;

    public abstract void setColumnValue(int i, byte[] bArr) throws SQLException;

    public abstract int getBytesSize();

    protected ResultSetRow(ExceptionInterceptor exceptionInterceptor) {
        this.exceptionInterceptor = exceptionInterceptor;
    }

    protected final Date getDateFast(int columnIndex, byte[] dateAsBytes, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar) throws SQLException, NumberFormatException {
        int year;
        int month;
        int day;
        int year2;
        if (dateAsBytes == null) {
            return null;
        }
        boolean allZeroDate = true;
        boolean onlyTimePresent = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            try {
                if (dateAsBytes[offset + i] != 58) {
                    i++;
                } else {
                    onlyTimePresent = true;
                    break;
                }
            } catch (SQLException sqlEx) {
                throw sqlEx;
            } catch (Exception e) {
                SQLException sqlEx2 = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx2.initCause(e);
                throw sqlEx2;
            }
        }
        int i2 = 0;
        while (true) {
            if (i2 < length) {
                byte b = dateAsBytes[offset + i2];
                if (b == 32 || b == 45 || b == 47) {
                    onlyTimePresent = false;
                }
                if (b == 48 || b == 32 || b == 58 || b == 45 || b == 47 || b == 46) {
                    i2++;
                } else {
                    allZeroDate = false;
                    break;
                }
            } else {
                break;
            }
        }
        int decimalIndex = -1;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            if (dateAsBytes[offset + i3] != 46) {
                i3++;
            } else {
                decimalIndex = i3;
                break;
            }
        }
        if (decimalIndex > -1) {
            length = decimalIndex;
        }
        if (!onlyTimePresent && allZeroDate) {
            if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                return null;
            }
            if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                throw SQLError.createSQLException("Value '" + StringUtils.toString(dateAsBytes) + "' can not be represented as java.sql.Date", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            }
            return rs.fastDateCreate(targetCalendar, 1, 1, 1);
        }
        if (this.metadata[columnIndex].getMysqlType() == 7) {
            switch (length) {
                case 2:
                    int year3 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
                    if (year3 <= 69) {
                        year3 += 100;
                    }
                    return rs.fastDateCreate(targetCalendar, year3 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, 1, 1);
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 17:
                case 18:
                case 20:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                default:
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                case 4:
                    int year4 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
                    if (year4 <= 69) {
                        year4 += 100;
                    }
                    int month2 = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
                    return rs.fastDateCreate(targetCalendar, year4 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, month2, 1);
                case 6:
                case 10:
                case 12:
                    int year5 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 2);
                    if (year5 <= 69) {
                        year5 += 100;
                    }
                    int month3 = StringUtils.getInt(dateAsBytes, offset + 2, offset + 4);
                    int day2 = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
                    return rs.fastDateCreate(targetCalendar, year5 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, month3, day2);
                case 8:
                case 14:
                    int year6 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
                    int month4 = StringUtils.getInt(dateAsBytes, offset + 4, offset + 6);
                    int day3 = StringUtils.getInt(dateAsBytes, offset + 6, offset + 8);
                    return rs.fastDateCreate(targetCalendar, year6, month4, day3);
                case 19:
                case 21:
                case 29:
                    int year7 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
                    int month5 = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
                    int day4 = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
                    return rs.fastDateCreate(targetCalendar, year7, month5, day4);
            }
        }
        if (this.metadata[columnIndex].getMysqlType() == 13) {
            if (length == 2 || length == 1) {
                int year8 = StringUtils.getInt(dateAsBytes, offset, offset + length);
                if (year8 <= 69) {
                    year8 += 100;
                }
                year2 = year8 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
            } else {
                year2 = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
            }
            return rs.fastDateCreate(targetCalendar, year2, 1, 1);
        }
        if (this.metadata[columnIndex].getMysqlType() == 11) {
            return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
        }
        if (length < 10) {
            if (length == 8) {
                return rs.fastDateCreate(targetCalendar, 1970, 1, 1);
            }
            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{StringUtils.toString(dateAsBytes), Integer.valueOf(columnIndex + 1)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        if (length != 18) {
            year = StringUtils.getInt(dateAsBytes, offset + 0, offset + 4);
            month = StringUtils.getInt(dateAsBytes, offset + 5, offset + 7);
            day = StringUtils.getInt(dateAsBytes, offset + 8, offset + 10);
        } else {
            StringTokenizer st = new StringTokenizer(StringUtils.toString(dateAsBytes, offset, length, "ISO8859_1"), List.DEFAULT_LIST_SYMBOL);
            year = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken());
            day = Integer.parseInt(st.nextToken());
        }
        return rs.fastDateCreate(targetCalendar, year, month, day);
    }

    protected Date getNativeDate(int columnIndex, byte[] bits, int offset, int length, MySQLConnection conn, ResultSetImpl rs, Calendar cal) throws SQLException {
        int year = 0;
        int month = 0;
        int day = 0;
        if (length != 0) {
            year = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8);
            month = bits[offset + 2];
            day = bits[offset + 3];
        }
        if (length == 0 || (year == 0 && month == 0 && day == 0)) {
            if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                return null;
            }
            if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            }
            year = 1;
            month = 1;
            day = 1;
        }
        if (!rs.useLegacyDatetimeCode) {
            return TimeUtil.fastDateCreate(year, month, day, cal);
        }
        return rs.fastDateCreate(cal == null ? rs.getCalendarInstanceForSessionOrNew() : cal, year, month, day);
    }

    protected Object getNativeDateTimeValue(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        boolean populatedFromDateTimeValue;
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int seconds = 0;
        int nanos = 0;
        if (bits == null) {
            return null;
        }
        Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
        switch (mysqlType) {
            case 7:
            case 12:
                populatedFromDateTimeValue = true;
                if (length != 0) {
                    year = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8);
                    month = bits[offset + 2];
                    day = bits[offset + 3];
                    if (length > 4) {
                        hour = bits[offset + 4];
                        minute = bits[offset + 5];
                        seconds = bits[offset + 6];
                    }
                    if (length > 7) {
                        nanos = ((bits[offset + 7] & 255) | ((bits[offset + 8] & 255) << 8) | ((bits[offset + 9] & 255) << 16) | ((bits[offset + 10] & 255) << 24)) * 1000;
                        break;
                    }
                }
                break;
            case 8:
            case 9:
            default:
                populatedFromDateTimeValue = false;
                break;
            case 10:
                populatedFromDateTimeValue = true;
                if (length != 0) {
                    year = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8);
                    month = bits[offset + 2];
                    day = bits[offset + 3];
                    break;
                }
                break;
            case 11:
                populatedFromDateTimeValue = true;
                if (length != 0) {
                    hour = bits[offset + 5];
                    minute = bits[offset + 6];
                    seconds = bits[offset + 7];
                }
                year = 1970;
                month = 1;
                day = 1;
                break;
        }
        switch (jdbcType) {
            case 91:
                if (populatedFromDateTimeValue) {
                    if (year == 0 && month == 0 && day == 0) {
                        if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                            return null;
                        }
                        if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                            throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Date", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                        }
                        year = 1;
                        month = 1;
                        day = 1;
                    }
                    if (!rs.useLegacyDatetimeCode) {
                        return TimeUtil.fastDateCreate(year, month, day, targetCalendar);
                    }
                    return rs.fastDateCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day);
                }
                return rs.getNativeDateViaParseConversion(columnIndex + 1);
            case 92:
                if (populatedFromDateTimeValue) {
                    if (!rs.useLegacyDatetimeCode) {
                        return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
                    }
                    Time time = TimeUtil.fastTimeCreate(rs.getCalendarInstanceForSessionOrNew(), hour, minute, seconds, this.exceptionInterceptor);
                    Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
                    return adjustedTime;
                }
                return rs.getNativeTimeViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
            case 93:
                if (populatedFromDateTimeValue) {
                    if (year == 0 && month == 0 && day == 0) {
                        if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                            return null;
                        }
                        if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                            throw new SQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                        }
                        year = 1;
                        month = 1;
                        day = 1;
                    }
                    if (!rs.useLegacyDatetimeCode) {
                        return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
                    }
                    boolean useGmtMillis = conn.getUseGmtMillisForDatetimes();
                    Timestamp ts = rs.fastTimestampCreate(rs.getCalendarInstanceForSessionOrNew(), year, month, day, hour, minute, seconds, nanos, useGmtMillis);
                    Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
                    return adjustedTs;
                }
                return rs.getNativeTimestampViaParseConversion(columnIndex + 1, targetCalendar, tz, rollForward);
            default:
                throw new SQLException("Internal error - conversion method doesn't support this type", SQLError.SQL_STATE_GENERAL_ERROR);
        }
    }

    protected double getNativeDouble(byte[] bits, int offset) {
        long valueAsLong = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8) | ((bits[offset + 2] & 255) << 16) | ((bits[offset + 3] & 255) << 24) | ((bits[offset + 4] & 255) << 32) | ((bits[offset + 5] & 255) << 40) | ((bits[offset + 6] & 255) << 48) | ((bits[offset + 7] & 255) << 56);
        return Double.longBitsToDouble(valueAsLong);
    }

    protected float getNativeFloat(byte[] bits, int offset) {
        int asInt = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8) | ((bits[offset + 2] & 255) << 16) | ((bits[offset + 3] & 255) << 24);
        return Float.intBitsToFloat(asInt);
    }

    protected int getNativeInt(byte[] bits, int offset) {
        int valueAsInt = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8) | ((bits[offset + 2] & 255) << 16) | ((bits[offset + 3] & 255) << 24);
        return valueAsInt;
    }

    protected long getNativeLong(byte[] bits, int offset) {
        long valueAsLong = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8) | ((bits[offset + 2] & 255) << 16) | ((bits[offset + 3] & 255) << 24) | ((bits[offset + 4] & 255) << 32) | ((bits[offset + 5] & 255) << 40) | ((bits[offset + 6] & 255) << 48) | ((bits[offset + 7] & 255) << 56);
        return valueAsLong;
    }

    protected short getNativeShort(byte[] bits, int offset) {
        short asShort = (short) ((bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8));
        return asShort;
    }

    protected Time getNativeTime(int columnIndex, byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        int hour = 0;
        int minute = 0;
        int seconds = 0;
        if (length != 0) {
            hour = bits[offset + 5];
            minute = bits[offset + 6];
            seconds = bits[offset + 7];
        }
        if (!rs.useLegacyDatetimeCode) {
            return TimeUtil.fastTimeCreate(hour, minute, seconds, targetCalendar, this.exceptionInterceptor);
        }
        Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
        Time time = TimeUtil.fastTimeCreate(sessionCalendar, hour, minute, seconds, this.exceptionInterceptor);
        Time adjustedTime = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, time, conn.getServerTimezoneTZ(), tz, rollForward);
        return adjustedTime;
    }

    protected Timestamp getNativeTimestamp(byte[] bits, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int seconds = 0;
        int nanos = 0;
        if (length != 0) {
            year = (bits[offset + 0] & 255) | ((bits[offset + 1] & 255) << 8);
            month = bits[offset + 2];
            day = bits[offset + 3];
            if (length > 4) {
                hour = bits[offset + 4];
                minute = bits[offset + 5];
                seconds = bits[offset + 6];
            }
            if (length > 7) {
                nanos = ((bits[offset + 7] & 255) | ((bits[offset + 8] & 255) << 8) | ((bits[offset + 9] & 255) << 16) | ((bits[offset + 10] & 255) << 24)) * 1000;
            }
        }
        if (length == 0 || (year == 0 && month == 0 && day == 0)) {
            if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                return null;
            }
            if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            }
            year = 1;
            month = 1;
            day = 1;
        }
        if (!rs.useLegacyDatetimeCode) {
            return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minute, seconds, nanos);
        }
        boolean useGmtMillis = conn.getUseGmtMillisForDatetimes();
        Calendar sessionCalendar = conn.getUseJDBCCompliantTimezoneShift() ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew();
        Timestamp ts = rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minute, seconds, nanos, useGmtMillis);
        Timestamp adjustedTs = TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, ts, conn.getServerTimezoneTZ(), tz, rollForward);
        return adjustedTs;
    }

    protected String getString(String encoding, MySQLConnection conn, byte[] value, int offset, int length) throws SQLException {
        String stringVal;
        if (conn != null && conn.getUseUnicode()) {
            try {
                if (encoding == null) {
                    stringVal = StringUtils.toString(value);
                } else {
                    SingleByteCharsetConverter converter = conn.getCharsetConverter(encoding);
                    if (converter != null) {
                        stringVal = converter.toString(value, offset, length);
                    } else {
                        stringVal = StringUtils.toString(value, offset, length, encoding);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Unsupported_character_encoding____101") + encoding + "'.", "0S100", this.exceptionInterceptor);
            }
        } else {
            stringVal = StringUtils.toAsciiString(value, offset, length);
        }
        return stringVal;
    }

    protected Time getTimeFast(int columnIndex, byte[] timeAsBytes, int offset, int fullLength, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        int hr;
        int min;
        int sec;
        int decimalIndex = -1;
        if (timeAsBytes == null) {
            return null;
        }
        boolean allZeroTime = true;
        boolean onlyTimePresent = false;
        int i = 0;
        while (true) {
            if (i >= fullLength) {
                break;
            }
            try {
                if (timeAsBytes[offset + i] != 58) {
                    i++;
                } else {
                    onlyTimePresent = true;
                    break;
                }
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
        int i2 = 0;
        while (true) {
            if (i2 >= fullLength) {
                break;
            }
            if (timeAsBytes[offset + i2] != 46) {
                i2++;
            } else {
                decimalIndex = i2;
                break;
            }
        }
        int i3 = 0;
        while (true) {
            if (i3 < fullLength) {
                byte b = timeAsBytes[offset + i3];
                if (b == 32 || b == 45 || b == 47) {
                    onlyTimePresent = false;
                }
                if (b == 48 || b == 32 || b == 58 || b == 45 || b == 47 || b == 46) {
                    i3++;
                } else {
                    allZeroTime = false;
                    break;
                }
            } else {
                break;
            }
        }
        if (!onlyTimePresent && allZeroTime) {
            if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                return null;
            }
            if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                throw SQLError.createSQLException("Value '" + StringUtils.toString(timeAsBytes) + "' can not be represented as java.sql.Time", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            }
            return rs.fastTimeCreate(targetCalendar, 0, 0, 0);
        }
        Field timeColField = this.metadata[columnIndex];
        int length = fullLength;
        if (decimalIndex != -1) {
            length = decimalIndex;
            if (decimalIndex + 2 <= fullLength) {
                int nanos = StringUtils.getInt(timeAsBytes, offset + decimalIndex + 1, offset + fullLength);
                int numDigits = fullLength - (decimalIndex + 1);
                if (numDigits < 9) {
                    int factor = (int) Math.pow(10.0d, 9 - numDigits);
                    int i4 = nanos * factor;
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (timeColField.getMysqlType() == 7) {
            switch (length) {
                case 10:
                    hr = StringUtils.getInt(timeAsBytes, offset + 6, offset + 8);
                    min = StringUtils.getInt(timeAsBytes, offset + 8, offset + 10);
                    sec = 0;
                    break;
                case 11:
                case 13:
                case 15:
                case 16:
                case 17:
                case 18:
                default:
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257") + (columnIndex + 1) + "(" + timeColField + ").", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                case 12:
                case 14:
                    hr = StringUtils.getInt(timeAsBytes, (offset + length) - 6, (offset + length) - 4);
                    min = StringUtils.getInt(timeAsBytes, (offset + length) - 4, (offset + length) - 2);
                    sec = StringUtils.getInt(timeAsBytes, (offset + length) - 2, offset + length);
                    break;
                case 19:
                    hr = StringUtils.getInt(timeAsBytes, (offset + length) - 8, (offset + length) - 6);
                    min = StringUtils.getInt(timeAsBytes, (offset + length) - 5, (offset + length) - 3);
                    sec = StringUtils.getInt(timeAsBytes, (offset + length) - 2, offset + length);
                    break;
            }
            new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261") + columnIndex + "(" + timeColField + ").");
        } else if (timeColField.getMysqlType() == 12) {
            hr = StringUtils.getInt(timeAsBytes, offset + 11, offset + 13);
            min = StringUtils.getInt(timeAsBytes, offset + 14, offset + 16);
            sec = StringUtils.getInt(timeAsBytes, offset + 17, offset + 19);
            new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264") + (columnIndex + 1) + "(" + timeColField + ").");
        } else {
            if (timeColField.getMysqlType() == 10) {
                return rs.fastTimeCreate(null, 0, 0, 0);
            }
            if (length != 5 && length != 8) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Time____267") + StringUtils.toString(timeAsBytes) + Messages.getString("ResultSet.___in_column__268") + (columnIndex + 1), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            }
            hr = StringUtils.getInt(timeAsBytes, offset + 0, offset + 2);
            min = StringUtils.getInt(timeAsBytes, offset + 3, offset + 5);
            sec = length == 5 ? 0 : StringUtils.getInt(timeAsBytes, offset + 6, offset + 8);
        }
        Calendar sessionCalendar = rs.getCalendarInstanceForSessionOrNew();
        if (!rs.useLegacyDatetimeCode) {
            if (targetCalendar == null) {
                targetCalendar = Calendar.getInstance(tz, Locale.US);
            }
            return rs.fastTimeCreate(targetCalendar, hr, min, sec);
        }
        return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimeCreate(sessionCalendar, hr, min, sec), conn.getServerTimezoneTZ(), tz, rollForward);
    }

    protected Timestamp getTimestampFast(int columnIndex, byte[] timestampAsBytes, int offset, int length, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs, boolean useGmtMillis, boolean useJDBCCompliantTimezoneShift) throws SQLException {
        int year;
        int month;
        int day;
        try {
            Calendar sessionCalendar = TimeUtil.setProlepticIfNeeded(useJDBCCompliantTimezoneShift ? conn.getUtcCalendar() : rs.getCalendarInstanceForSessionOrNew(), targetCalendar);
            boolean allZeroTimestamp = true;
            boolean onlyTimePresent = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                if (timestampAsBytes[offset + i] != 58) {
                    i++;
                } else {
                    onlyTimePresent = true;
                    break;
                }
            }
            int i2 = 0;
            while (true) {
                if (i2 < length) {
                    byte b = timestampAsBytes[offset + i2];
                    if (b == 32 || b == 45 || b == 47) {
                        onlyTimePresent = false;
                    }
                    if (b == 48 || b == 32 || b == 58 || b == 45 || b == 47 || b == 46) {
                        i2++;
                    } else {
                        allZeroTimestamp = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!onlyTimePresent && allZeroTimestamp) {
                if ("convertToNull".equals(conn.getZeroDateTimeBehavior())) {
                    return null;
                }
                if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(conn.getZeroDateTimeBehavior())) {
                    throw SQLError.createSQLException("Value '" + StringUtils.toString(timestampAsBytes) + "' can not be represented as java.sql.Timestamp", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
                }
                if (!rs.useLegacyDatetimeCode) {
                    return TimeUtil.fastTimestampCreate(tz, 1, 1, 1, 0, 0, 0, 0);
                }
                return rs.fastTimestampCreate(null, 1, 1, 1, 0, 0, 0, 0, useGmtMillis);
            }
            if (this.metadata[columnIndex].getMysqlType() == 13) {
                if (!rs.useLegacyDatetimeCode) {
                    return TimeUtil.fastTimestampCreate(tz, StringUtils.getInt(timestampAsBytes, offset, 4), 1, 1, 0, 0, 0, 0);
                }
                return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimestampCreate(sessionCalendar, StringUtils.getInt(timestampAsBytes, offset, 4), 1, 1, 0, 0, 0, 0, useGmtMillis), conn.getServerTimezoneTZ(), tz, rollForward);
            }
            int hour = 0;
            int minutes = 0;
            int seconds = 0;
            int nanos = 0;
            int decimalIndex = -1;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (timestampAsBytes[offset + i3] != 46) {
                    i3++;
                } else {
                    decimalIndex = i3;
                    break;
                }
            }
            if (decimalIndex == (offset + length) - 1) {
                length--;
            } else if (decimalIndex != -1) {
                if (decimalIndex + 2 <= length) {
                    nanos = StringUtils.getInt(timestampAsBytes, offset + decimalIndex + 1, offset + length);
                    int numDigits = length - (decimalIndex + 1);
                    if (numDigits < 9) {
                        int factor = (int) Math.pow(10.0d, 9 - numDigits);
                        nanos *= factor;
                    }
                    length = decimalIndex;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            switch (length) {
                case 2:
                    int year2 = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                    if (year2 <= 69) {
                        year2 += 100;
                    }
                    year = year2 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = 1;
                    day = 1;
                    break;
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 17:
                case 18:
                case 27:
                case 28:
                default:
                    throw new SQLException("Bad format for Timestamp '" + StringUtils.toString(timestampAsBytes) + "' in column " + (columnIndex + 1) + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                case 4:
                    year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                    if (year <= 69) {
                        year += 100;
                    }
                    month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
                    day = 1;
                    break;
                case 6:
                    int year3 = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                    if (year3 <= 69) {
                        year3 += 100;
                    }
                    year = year3 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
                    day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
                    break;
                case 8:
                    boolean hasColon = false;
                    int i4 = 0;
                    while (true) {
                        if (i4 < length) {
                            if (timestampAsBytes[offset + i4] != 58) {
                                i4++;
                            } else {
                                hasColon = true;
                            }
                        }
                    }
                    if (hasColon) {
                        hour = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                        minutes = StringUtils.getInt(timestampAsBytes, offset + 3, offset + 5);
                        seconds = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
                        year = 1970;
                        month = 1;
                        day = 1;
                        break;
                    } else {
                        int year4 = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
                        int month2 = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
                        day = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
                        year = year4 - 1900;
                        month = month2 - 1;
                        break;
                    }
                case 10:
                    boolean hasDash = false;
                    int i5 = 0;
                    while (true) {
                        if (i5 < length) {
                            if (timestampAsBytes[offset + i5] != 45) {
                                i5++;
                            } else {
                                hasDash = true;
                            }
                        }
                    }
                    if (this.metadata[columnIndex].getMysqlType() == 10 || hasDash) {
                        year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
                        month = StringUtils.getInt(timestampAsBytes, offset + 5, offset + 7);
                        day = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
                        hour = 0;
                        minutes = 0;
                        break;
                    } else {
                        int year5 = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                        if (year5 <= 69) {
                            year5 += 100;
                        }
                        month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
                        day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
                        hour = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
                        minutes = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
                        year = year5 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                        break;
                    }
                    break;
                case 12:
                    int year6 = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 2);
                    if (year6 <= 69) {
                        year6 += 100;
                    }
                    year = year6 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = StringUtils.getInt(timestampAsBytes, offset + 2, offset + 4);
                    day = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
                    hour = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
                    minutes = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
                    seconds = StringUtils.getInt(timestampAsBytes, offset + 10, offset + 12);
                    break;
                case 14:
                    year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
                    month = StringUtils.getInt(timestampAsBytes, offset + 4, offset + 6);
                    day = StringUtils.getInt(timestampAsBytes, offset + 6, offset + 8);
                    hour = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
                    minutes = StringUtils.getInt(timestampAsBytes, offset + 10, offset + 12);
                    seconds = StringUtils.getInt(timestampAsBytes, offset + 12, offset + 14);
                    break;
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 29:
                    year = StringUtils.getInt(timestampAsBytes, offset + 0, offset + 4);
                    month = StringUtils.getInt(timestampAsBytes, offset + 5, offset + 7);
                    day = StringUtils.getInt(timestampAsBytes, offset + 8, offset + 10);
                    hour = StringUtils.getInt(timestampAsBytes, offset + 11, offset + 13);
                    minutes = StringUtils.getInt(timestampAsBytes, offset + 14, offset + 16);
                    seconds = StringUtils.getInt(timestampAsBytes, offset + 17, offset + 19);
                    break;
            }
            if (!rs.useLegacyDatetimeCode) {
                return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minutes, seconds, nanos);
            }
            return TimeUtil.changeTimezone(conn, sessionCalendar, targetCalendar, rs.fastTimestampCreate(sessionCalendar, year, month, day, hour, minutes, seconds, nanos, useGmtMillis), conn.getServerTimezoneTZ(), tz, rollForward);
        } catch (RuntimeException e) {
            SQLException sqlEx = SQLError.createSQLException("Cannot convert value '" + getString(columnIndex, "ISO8859_1", conn) + "' from column " + (columnIndex + 1) + " to TIMESTAMP.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
            sqlEx.initCause(e);
            throw sqlEx;
        }
    }

    public ResultSetRow setMetadata(Field[] f) throws SQLException {
        this.metadata = f;
        return this;
    }
}
