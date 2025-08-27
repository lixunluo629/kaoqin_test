package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ByteArrayRow.class */
public class ByteArrayRow extends ResultSetRow {
    byte[][] internalRowData;

    public ByteArrayRow(byte[][] internalRowData, ExceptionInterceptor exceptionInterceptor) {
        super(exceptionInterceptor);
        this.internalRowData = internalRowData;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public byte[] getColumnValue(int index) throws SQLException {
        return this.internalRowData[index];
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public void setColumnValue(int index, byte[] value) throws SQLException {
        this.internalRowData[index] = value;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public String getString(int index, String encoding, MySQLConnection conn) throws SQLException {
        byte[] columnData = this.internalRowData[index];
        if (columnData == null) {
            return null;
        }
        return getString(encoding, conn, columnData, 0, columnData.length);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public boolean isNull(int index) throws SQLException {
        return this.internalRowData[index] == null;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public boolean isFloatingPointNumber(int index) throws SQLException {
        byte[] numAsBytes = this.internalRowData[index];
        if (this.internalRowData[index] == null || this.internalRowData[index].length == 0) {
            return false;
        }
        for (int i = 0; i < numAsBytes.length; i++) {
            if (((char) numAsBytes[i]) == 'e' || ((char) numAsBytes[i]) == 'E') {
                return true;
            }
        }
        return false;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long length(int index) throws SQLException {
        if (this.internalRowData[index] == null) {
            return 0L;
        }
        return this.internalRowData[index].length;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getInt(int columnIndex) {
        if (this.internalRowData[columnIndex] == null) {
            return 0;
        }
        return StringUtils.getInt(this.internalRowData[columnIndex]);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long getLong(int columnIndex) {
        if (this.internalRowData[columnIndex] == null) {
            return 0L;
        }
        return StringUtils.getLong(this.internalRowData[columnIndex]);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Timestamp getTimestampFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs, boolean useGmtMillis, boolean useJDBCCompliantTimezoneShift) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getTimestampFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs, useGmtMillis, useJDBCCompliantTimezoneShift);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public double getNativeDouble(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return 0.0d;
        }
        return getNativeDouble(this.internalRowData[columnIndex], 0);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public float getNativeFloat(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return 0.0f;
        }
        return getNativeFloat(this.internalRowData[columnIndex], 0);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getNativeInt(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return 0;
        }
        return getNativeInt(this.internalRowData[columnIndex], 0);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long getNativeLong(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return 0L;
        }
        return getNativeLong(this.internalRowData[columnIndex], 0);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public short getNativeShort(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return (short) 0;
        }
        return getNativeShort(this.internalRowData[columnIndex], 0);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        byte[] bits = this.internalRowData[columnIndex];
        if (bits == null) {
            return null;
        }
        return getNativeTimestamp(bits, 0, bits.length, targetCalendar, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public void closeOpenStreams() {
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public InputStream getBinaryInputStream(int columnIndex) throws SQLException {
        if (this.internalRowData[columnIndex] == null) {
            return null;
        }
        return new ByteArrayInputStream(this.internalRowData[columnIndex]);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Reader getReader(int columnIndex) throws SQLException {
        InputStream stream = getBinaryInputStream(columnIndex);
        if (stream == null) {
            return null;
        }
        try {
            return new InputStreamReader(stream, this.metadata[columnIndex].getEncoding());
        } catch (UnsupportedEncodingException e) {
            SQLException sqlEx = SQLError.createSQLException("", this.exceptionInterceptor);
            sqlEx.initCause(e);
            throw sqlEx;
        }
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Time getTimeFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getTimeFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Date getDateFast(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getDateFast(columnIndex, this.internalRowData[columnIndex], 0, columnValue.length, conn, rs, targetCalendar);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Object getNativeDateTimeValue(int columnIndex, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getNativeDateTimeValue(columnIndex, columnValue, 0, columnValue.length, targetCalendar, jdbcType, mysqlType, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Date getNativeDate(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar cal) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getNativeDate(columnIndex, columnValue, 0, columnValue.length, conn, rs, cal);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        byte[] columnValue = this.internalRowData[columnIndex];
        if (columnValue == null) {
            return null;
        }
        return getNativeTime(columnIndex, columnValue, 0, columnValue.length, targetCalendar, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getBytesSize() {
        if (this.internalRowData == null) {
            return 0;
        }
        int bytesSize = 0;
        for (int i = 0; i < this.internalRowData.length; i++) {
            if (this.internalRowData[i] != null) {
                bytesSize += this.internalRowData[i].length;
            }
        }
        return bytesSize;
    }
}
