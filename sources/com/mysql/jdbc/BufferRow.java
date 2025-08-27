package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/BufferRow.class */
public class BufferRow extends ResultSetRow {
    private Buffer rowFromServer;
    private int homePosition;
    private int preNullBitmaskHomePosition;
    private int lastRequestedIndex;
    private int lastRequestedPos;
    private Field[] metadata;
    private boolean isBinaryEncoded;
    private boolean[] isNull;
    private List<InputStream> openStreams;

    public BufferRow(Buffer buf, Field[] fields, boolean isBinaryEncoded, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        super(exceptionInterceptor);
        this.homePosition = 0;
        this.preNullBitmaskHomePosition = 0;
        this.lastRequestedIndex = -1;
        this.rowFromServer = buf;
        this.metadata = fields;
        this.isBinaryEncoded = isBinaryEncoded;
        this.homePosition = this.rowFromServer.getPosition();
        this.preNullBitmaskHomePosition = this.homePosition;
        if (fields != null) {
            setMetadata(fields);
        }
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public synchronized void closeOpenStreams() throws IOException {
        if (this.openStreams != null) {
            Iterator<InputStream> iter = this.openStreams.iterator();
            while (iter.hasNext()) {
                try {
                    iter.next().close();
                } catch (IOException e) {
                }
            }
            this.openStreams.clear();
        }
    }

    private int findAndSeekToOffset(int index) throws SQLException {
        if (!this.isBinaryEncoded) {
            if (index == 0) {
                this.lastRequestedIndex = 0;
                this.lastRequestedPos = this.homePosition;
                this.rowFromServer.setPosition(this.homePosition);
                return 0;
            }
            if (index == this.lastRequestedIndex) {
                this.rowFromServer.setPosition(this.lastRequestedPos);
                return this.lastRequestedPos;
            }
            int startingIndex = 0;
            if (index > this.lastRequestedIndex) {
                if (this.lastRequestedIndex >= 0) {
                    startingIndex = this.lastRequestedIndex;
                } else {
                    startingIndex = 0;
                }
                this.rowFromServer.setPosition(this.lastRequestedPos);
            } else {
                this.rowFromServer.setPosition(this.homePosition);
            }
            for (int i = startingIndex; i < index; i++) {
                this.rowFromServer.fastSkipLenByteArray();
            }
            this.lastRequestedIndex = index;
            this.lastRequestedPos = this.rowFromServer.getPosition();
            return this.lastRequestedPos;
        }
        return findAndSeekToOffsetForBinaryEncoding(index);
    }

    private int findAndSeekToOffsetForBinaryEncoding(int index) throws SQLException {
        if (index == 0) {
            this.lastRequestedIndex = 0;
            this.lastRequestedPos = this.homePosition;
            this.rowFromServer.setPosition(this.homePosition);
            return 0;
        }
        if (index == this.lastRequestedIndex) {
            this.rowFromServer.setPosition(this.lastRequestedPos);
            return this.lastRequestedPos;
        }
        int startingIndex = 0;
        if (index > this.lastRequestedIndex) {
            if (this.lastRequestedIndex >= 0) {
                startingIndex = this.lastRequestedIndex;
            } else {
                startingIndex = 0;
                this.lastRequestedPos = this.homePosition;
            }
            this.rowFromServer.setPosition(this.lastRequestedPos);
        } else {
            this.rowFromServer.setPosition(this.homePosition);
        }
        for (int i = startingIndex; i < index; i++) {
            if (!this.isNull[i]) {
                int curPosition = this.rowFromServer.getPosition();
                switch (this.metadata[i].getMysqlType()) {
                    case 0:
                    case 15:
                    case 16:
                    case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
                    case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                    case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
                    case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
                    case 251:
                    case 252:
                    case 253:
                    case 254:
                    case 255:
                        this.rowFromServer.fastSkipLenByteArray();
                        break;
                    case 1:
                        this.rowFromServer.setPosition(curPosition + 1);
                        break;
                    case 2:
                    case 13:
                        this.rowFromServer.setPosition(curPosition + 2);
                        break;
                    case 3:
                    case 9:
                        this.rowFromServer.setPosition(curPosition + 4);
                        break;
                    case 4:
                        this.rowFromServer.setPosition(curPosition + 4);
                        break;
                    case 5:
                        this.rowFromServer.setPosition(curPosition + 8);
                        break;
                    case 6:
                        break;
                    case 7:
                    case 12:
                        this.rowFromServer.fastSkipLenByteArray();
                        break;
                    case 8:
                        this.rowFromServer.setPosition(curPosition + 8);
                        break;
                    case 10:
                        this.rowFromServer.fastSkipLenByteArray();
                        break;
                    case 11:
                        this.rowFromServer.fastSkipLenByteArray();
                        break;
                    default:
                        throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + this.metadata[i].getMysqlType() + Messages.getString("MysqlIO.98") + (i + 1) + Messages.getString("MysqlIO.99") + this.metadata.length + Messages.getString("MysqlIO.100"), SQLError.SQL_STATE_GENERAL_ERROR, this.exceptionInterceptor);
                }
            }
        }
        this.lastRequestedIndex = index;
        this.lastRequestedPos = this.rowFromServer.getPosition();
        return this.lastRequestedPos;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public synchronized InputStream getBinaryInputStream(int columnIndex) throws SQLException {
        if (this.isBinaryEncoded && isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        if (length == -1) {
            return null;
        }
        InputStream stream = new ByteArrayInputStream(this.rowFromServer.getByteBuffer(), offset, (int) length);
        if (this.openStreams == null) {
            this.openStreams = new LinkedList();
        }
        return stream;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public byte[] getColumnValue(int index) throws SQLException {
        findAndSeekToOffset(index);
        if (!this.isBinaryEncoded) {
            return this.rowFromServer.readLenByteArray(0);
        }
        if (this.isNull[index]) {
            return null;
        }
        switch (this.metadata[index].getMysqlType()) {
            case 0:
            case 7:
            case 10:
            case 11:
            case 12:
            case 15:
            case 16:
            case EscherProperties.GEOTEXT__STRETCHTOFITSHAPE /* 245 */:
            case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
                return this.rowFromServer.readLenByteArray(0);
            case 1:
                return new byte[]{this.rowFromServer.readByte()};
            case 2:
            case 13:
                return this.rowFromServer.getBytes(2);
            case 3:
            case 9:
                return this.rowFromServer.getBytes(4);
            case 4:
                return this.rowFromServer.getBytes(4);
            case 5:
                return this.rowFromServer.getBytes(8);
            case 6:
                return null;
            case 8:
                return this.rowFromServer.getBytes(8);
            default:
                throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + this.metadata[index].getMysqlType() + Messages.getString("MysqlIO.98") + (index + 1) + Messages.getString("MysqlIO.99") + this.metadata.length + Messages.getString("MysqlIO.100"), SQLError.SQL_STATE_GENERAL_ERROR, this.exceptionInterceptor);
        }
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getInt(int columnIndex) throws SQLException {
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        if (length == -1) {
            return 0;
        }
        return StringUtils.getInt(this.rowFromServer.getByteBuffer(), offset, offset + ((int) length));
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long getLong(int columnIndex) throws SQLException {
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        if (length == -1) {
            return 0L;
        }
        return StringUtils.getLong(this.rowFromServer.getByteBuffer(), offset, offset + ((int) length));
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public double getNativeDouble(int columnIndex) throws SQLException {
        if (isNull(columnIndex)) {
            return 0.0d;
        }
        findAndSeekToOffset(columnIndex);
        int offset = this.rowFromServer.getPosition();
        return getNativeDouble(this.rowFromServer.getByteBuffer(), offset);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public float getNativeFloat(int columnIndex) throws SQLException {
        if (isNull(columnIndex)) {
            return 0.0f;
        }
        findAndSeekToOffset(columnIndex);
        int offset = this.rowFromServer.getPosition();
        return getNativeFloat(this.rowFromServer.getByteBuffer(), offset);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getNativeInt(int columnIndex) throws SQLException {
        if (isNull(columnIndex)) {
            return 0;
        }
        findAndSeekToOffset(columnIndex);
        int offset = this.rowFromServer.getPosition();
        return getNativeInt(this.rowFromServer.getByteBuffer(), offset);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long getNativeLong(int columnIndex) throws SQLException {
        if (isNull(columnIndex)) {
            return 0L;
        }
        findAndSeekToOffset(columnIndex);
        int offset = this.rowFromServer.getPosition();
        return getNativeLong(this.rowFromServer.getByteBuffer(), offset);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public short getNativeShort(int columnIndex) throws SQLException {
        if (isNull(columnIndex)) {
            return (short) 0;
        }
        findAndSeekToOffset(columnIndex);
        int offset = this.rowFromServer.getPosition();
        return getNativeShort(this.rowFromServer.getByteBuffer(), offset);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getNativeTimestamp(this.rowFromServer.getByteBuffer(), offset, (int) length, targetCalendar, tz, rollForward, conn, rs);
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
    public String getString(int columnIndex, String encoding, MySQLConnection conn) throws SQLException {
        if (this.isBinaryEncoded && isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        if (length == -1) {
            return null;
        }
        if (length == 0) {
            return "";
        }
        int offset = this.rowFromServer.getPosition();
        return getString(encoding, conn, this.rowFromServer.getByteBuffer(), offset, (int) length);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Time getTimeFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getTimeFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, targetCalendar, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Timestamp getTimestampFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs, boolean useGmtMillis, boolean useJDBCCompliantTimezoneShift) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getTimestampFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, targetCalendar, tz, rollForward, conn, rs, useGmtMillis, useJDBCCompliantTimezoneShift);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public boolean isFloatingPointNumber(int index) throws SQLException {
        if (this.isBinaryEncoded) {
            switch (this.metadata[index].getSQLType()) {
                case 2:
                case 3:
                case 6:
                case 8:
                    return true;
                case 4:
                case 5:
                case 7:
                default:
                    return false;
            }
        }
        findAndSeekToOffset(index);
        long length = this.rowFromServer.readFieldLength();
        if (length == -1 || length == 0) {
            return false;
        }
        int offset = this.rowFromServer.getPosition();
        byte[] buffer = this.rowFromServer.getByteBuffer();
        for (int i = 0; i < ((int) length); i++) {
            char c = (char) buffer[offset + i];
            if (c == 'e' || c == 'E') {
                return true;
            }
        }
        return false;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public boolean isNull(int index) throws SQLException {
        if (!this.isBinaryEncoded) {
            findAndSeekToOffset(index);
            return this.rowFromServer.readFieldLength() == -1;
        }
        return this.isNull[index];
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public long length(int index) throws SQLException {
        findAndSeekToOffset(index);
        long length = this.rowFromServer.readFieldLength();
        if (length == -1) {
            return 0L;
        }
        return length;
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public void setColumnValue(int index, byte[] value) throws SQLException {
        throw new OperationNotSupportedException();
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public ResultSetRow setMetadata(Field[] f) throws SQLException {
        super.setMetadata(f);
        if (this.isBinaryEncoded) {
            setupIsNullBitmask();
        }
        return this;
    }

    private void setupIsNullBitmask() throws SQLException {
        if (this.isNull != null) {
            return;
        }
        this.rowFromServer.setPosition(this.preNullBitmaskHomePosition);
        int nullCount = (this.metadata.length + 9) / 8;
        byte[] nullBitMask = new byte[nullCount];
        for (int i = 0; i < nullCount; i++) {
            nullBitMask[i] = this.rowFromServer.readByte();
        }
        this.homePosition = this.rowFromServer.getPosition();
        this.isNull = new boolean[this.metadata.length];
        int nullMaskPos = 0;
        int bit = 4;
        for (int i2 = 0; i2 < this.metadata.length; i2++) {
            this.isNull[i2] = (nullBitMask[nullMaskPos] & bit) != 0;
            int i3 = bit << 1;
            bit = i3;
            if ((i3 & 255) == 0) {
                bit = 1;
                nullMaskPos++;
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Date getDateFast(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getDateFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, conn, rs, targetCalendar);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Date getNativeDate(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar cal) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getNativeDate(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, conn, rs, cal);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Object getNativeDateTimeValue(int columnIndex, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getNativeDateTimeValue(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, targetCalendar, jdbcType, mysqlType, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs) throws SQLException {
        if (isNull(columnIndex)) {
            return null;
        }
        findAndSeekToOffset(columnIndex);
        long length = this.rowFromServer.readFieldLength();
        int offset = this.rowFromServer.getPosition();
        return getNativeTime(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int) length, targetCalendar, tz, rollForward, conn, rs);
    }

    @Override // com.mysql.jdbc.ResultSetRow
    public int getBytesSize() {
        return this.rowFromServer.getBufLength();
    }
}
