package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Buffer.class */
public class Buffer {
    static final int MAX_BYTES_TO_DUMP = 512;
    static final int NO_LENGTH_LIMIT = -1;
    static final long NULL_LENGTH = -1;
    private int bufLength;
    private byte[] byteBuffer;
    private int position;
    protected boolean wasMultiPacket;
    public static final short TYPE_ID_ERROR = 255;
    public static final short TYPE_ID_EOF = 254;
    public static final short TYPE_ID_AUTH_SWITCH = 254;
    public static final short TYPE_ID_LOCAL_INFILE = 251;
    public static final short TYPE_ID_OK = 0;

    public Buffer(byte[] buf) {
        this.bufLength = 0;
        this.position = 0;
        this.wasMultiPacket = false;
        this.byteBuffer = buf;
        setBufLength(buf.length);
    }

    Buffer(int size) {
        this.bufLength = 0;
        this.position = 0;
        this.wasMultiPacket = false;
        this.byteBuffer = new byte[size];
        setBufLength(this.byteBuffer.length);
        this.position = 4;
    }

    final void clear() {
        this.position = 4;
    }

    final void dump() {
        dump(getBufLength());
    }

    final String dump(int numBytes) {
        return StringUtils.dumpAsHex(getBytes(0, numBytes > getBufLength() ? getBufLength() : numBytes), numBytes > getBufLength() ? getBufLength() : numBytes);
    }

    final String dumpClampedBytes(int numBytes) {
        int numBytesToDump = numBytes < 512 ? numBytes : 512;
        String dumped = StringUtils.dumpAsHex(getBytes(0, numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump), numBytesToDump > getBufLength() ? getBufLength() : numBytesToDump);
        if (numBytesToDump < numBytes) {
            return dumped + " ....(packet exceeds max. dump length)";
        }
        return dumped;
    }

    final void dumpHeader() {
        for (int i = 0; i < 4; i++) {
            String hexVal = Integer.toHexString(readByte(i) & 255);
            if (hexVal.length() == 1) {
                hexVal = "0" + hexVal;
            }
            System.out.print(hexVal + SymbolConstants.SPACE_SYMBOL);
        }
    }

    final void dumpNBytes(int start, int nBytes) {
        StringBuilder asciiBuf = new StringBuilder();
        for (int i = start; i < start + nBytes && i < getBufLength(); i++) {
            String hexVal = Integer.toHexString(readByte(i) & 255);
            if (hexVal.length() == 1) {
                hexVal = "0" + hexVal;
            }
            System.out.print(hexVal + SymbolConstants.SPACE_SYMBOL);
            if (readByte(i) > 32 && readByte(i) < Byte.MAX_VALUE) {
                asciiBuf.append((char) readByte(i));
            } else {
                asciiBuf.append(".");
            }
            asciiBuf.append(SymbolConstants.SPACE_SYMBOL);
        }
        System.out.println("    " + asciiBuf.toString());
    }

    final void ensureCapacity(int additionalData) throws SQLException {
        if (this.position + additionalData > getBufLength()) {
            if (this.position + additionalData < this.byteBuffer.length) {
                setBufLength(this.byteBuffer.length);
                return;
            }
            int newLength = (int) (this.byteBuffer.length * 1.25d);
            if (newLength < this.byteBuffer.length + additionalData) {
                newLength = this.byteBuffer.length + ((int) (additionalData * 1.25d));
            }
            if (newLength < this.byteBuffer.length) {
                newLength = this.byteBuffer.length + additionalData;
            }
            byte[] newBytes = new byte[newLength];
            System.arraycopy(this.byteBuffer, 0, newBytes, 0, this.byteBuffer.length);
            this.byteBuffer = newBytes;
            setBufLength(this.byteBuffer.length);
        }
    }

    public int fastSkipLenString() {
        long len = readFieldLength();
        this.position = (int) (this.position + len);
        return (int) len;
    }

    public void fastSkipLenByteArray() {
        long len = readFieldLength();
        if (len == -1 || len == 0) {
            return;
        }
        this.position = (int) (this.position + len);
    }

    protected final byte[] getBufferSource() {
        return this.byteBuffer;
    }

    public int getBufLength() {
        return this.bufLength;
    }

    public byte[] getByteBuffer() {
        return this.byteBuffer;
    }

    final byte[] getBytes(int len) {
        byte[] b = new byte[len];
        System.arraycopy(this.byteBuffer, this.position, b, 0, len);
        this.position += len;
        return b;
    }

    byte[] getBytes(int offset, int len) {
        byte[] dest = new byte[len];
        System.arraycopy(this.byteBuffer, offset, dest, 0, len);
        return dest;
    }

    int getCapacity() {
        return this.byteBuffer.length;
    }

    public ByteBuffer getNioBuffer() {
        throw new IllegalArgumentException(Messages.getString("ByteArrayBuffer.0"));
    }

    public int getPosition() {
        return this.position;
    }

    final boolean isEOFPacket() {
        return (this.byteBuffer[0] & 255) == 254 && getBufLength() <= 5;
    }

    final boolean isAuthMethodSwitchRequestPacket() {
        return (this.byteBuffer[0] & 255) == 254;
    }

    final boolean isOKPacket() {
        return (this.byteBuffer[0] & 255) == 0;
    }

    final boolean isResultSetOKPacket() {
        return (this.byteBuffer[0] & 255) == 254 && getBufLength() < 16777215;
    }

    final boolean isRawPacket() {
        return (this.byteBuffer[0] & 255) == 1;
    }

    final long newReadLength() {
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int sw = bArr[i] & 255;
        switch (sw) {
            case 251:
                return 0L;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLongLong();
            default:
                return sw;
        }
    }

    final byte readByte() {
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        return bArr[i];
    }

    final byte readByte(int readAt) {
        return this.byteBuffer[readAt];
    }

    final long readFieldLength() {
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int sw = bArr[i] & 255;
        switch (sw) {
            case 251:
                return -1L;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLongLong();
            default:
                return sw;
        }
    }

    final int readInt() {
        byte[] b = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = b[i] & 255;
        int i3 = this.position;
        this.position = i3 + 1;
        return i2 | ((b[i3] & 255) << 8);
    }

    final int readIntAsLong() {
        byte[] b = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = b[i] & 255;
        int i3 = this.position;
        this.position = i3 + 1;
        int i4 = i2 | ((b[i3] & 255) << 8);
        int i5 = this.position;
        this.position = i5 + 1;
        int i6 = i4 | ((b[i5] & 255) << 16);
        int i7 = this.position;
        this.position = i7 + 1;
        return i6 | ((b[i7] & 255) << 24);
    }

    final byte[] readLenByteArray(int offset) {
        long len = readFieldLength();
        if (len == -1) {
            return null;
        }
        if (len == 0) {
            return Constants.EMPTY_BYTE_ARRAY;
        }
        this.position += offset;
        return getBytes((int) len);
    }

    final long readLength() {
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int sw = bArr[i] & 255;
        switch (sw) {
            case 251:
                return 0L;
            case 252:
                return readInt();
            case 253:
                return readLongInt();
            case 254:
                return readLong();
            default:
                return sw;
        }
    }

    final long readLong() {
        byte[] b = this.byteBuffer;
        this.position = this.position + 1;
        this.position = this.position + 1;
        long j = (b[r2] & 255) | ((b[r3] & 255) << 8);
        this.position = this.position + 1;
        long j2 = j | ((b[r3] & 255) << 16);
        this.position = this.position + 1;
        return j2 | ((b[r3] & 255) << 24);
    }

    final int readLongInt() {
        byte[] b = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int i2 = b[i] & 255;
        int i3 = this.position;
        this.position = i3 + 1;
        int i4 = i2 | ((b[i3] & 255) << 8);
        int i5 = this.position;
        this.position = i5 + 1;
        return i4 | ((b[i5] & 255) << 16);
    }

    final long readLongLong() {
        byte[] b = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        long j = b[i] & 255;
        this.position = this.position + 1;
        long j2 = j | ((b[r3] & 255) << 8);
        this.position = this.position + 1;
        long j3 = j2 | ((b[r3] & 255) << 16);
        this.position = this.position + 1;
        long j4 = j3 | ((b[r3] & 255) << 24);
        this.position = this.position + 1;
        long j5 = j4 | ((b[r3] & 255) << 32);
        this.position = this.position + 1;
        long j6 = j5 | ((b[r3] & 255) << 40);
        this.position = this.position + 1;
        long j7 = j6 | ((b[r3] & 255) << 48);
        this.position = this.position + 1;
        return j7 | ((b[r3] & 255) << 56);
    }

    final int readnBytes() {
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        int sw = bArr[i] & 255;
        switch (sw) {
            case 1:
                byte[] bArr2 = this.byteBuffer;
                int i2 = this.position;
                this.position = i2 + 1;
                return bArr2[i2] & 255;
            case 2:
                return readInt();
            case 3:
                return readLongInt();
            case 4:
                return (int) readLong();
            default:
                return 255;
        }
    }

    public final String readString() {
        int len = 0;
        int maxLen = getBufLength();
        for (int i = this.position; i < maxLen && this.byteBuffer[i] != 0; i++) {
            len++;
        }
        String s = StringUtils.toString(this.byteBuffer, this.position, len);
        this.position += len + 1;
        return s;
    }

    final String readString(String encoding, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        int len = 0;
        int maxLen = getBufLength();
        for (int i = this.position; i < maxLen && this.byteBuffer[i] != 0; i++) {
            len++;
        }
        try {
            try {
                return StringUtils.toString(this.byteBuffer, this.position, len, encoding);
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
            }
        } finally {
            this.position += len + 1;
        }
    }

    final String readString(String encoding, ExceptionInterceptor exceptionInterceptor, int expectedLength) throws SQLException {
        try {
            if (this.position + expectedLength > getBufLength()) {
                throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.2"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
            }
            try {
                return StringUtils.toString(this.byteBuffer, this.position, expectedLength, encoding);
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("ByteArrayBuffer.1") + encoding + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
            }
        } finally {
            this.position += expectedLength;
        }
    }

    public void setBufLength(int bufLengthToSet) {
        this.bufLength = bufLengthToSet;
    }

    public void setByteBuffer(byte[] byteBufferToSet) {
        this.byteBuffer = byteBufferToSet;
    }

    public void setPosition(int positionToSet) {
        this.position = positionToSet;
    }

    public void setWasMultiPacket(boolean flag) {
        this.wasMultiPacket = flag;
    }

    public String toString() {
        return dumpClampedBytes(getPosition());
    }

    public String toSuperString() {
        return super.toString();
    }

    public boolean wasMultiPacket() {
        return this.wasMultiPacket;
    }

    public final void writeByte(byte b) throws SQLException {
        ensureCapacity(1);
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = b;
    }

    public final void writeBytesNoNull(byte[] bytes) throws SQLException {
        int len = bytes.length;
        ensureCapacity(len);
        System.arraycopy(bytes, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeBytesNoNull(byte[] bytes, int offset, int length) throws SQLException {
        ensureCapacity(length);
        System.arraycopy(bytes, offset, this.byteBuffer, this.position, length);
        this.position += length;
    }

    final void writeDouble(double d) throws SQLException {
        long l = Double.doubleToLongBits(d);
        writeLongLong(l);
    }

    final void writeFieldLength(long length) throws SQLException {
        if (length < 251) {
            writeByte((byte) length);
            return;
        }
        if (length < org.aspectj.apache.bcel.Constants.EXCEPTION_THROWER) {
            ensureCapacity(3);
            writeByte((byte) -4);
            writeInt((int) length);
        } else if (length < 16777216) {
            ensureCapacity(4);
            writeByte((byte) -3);
            writeLongInt((int) length);
        } else {
            ensureCapacity(9);
            writeByte((byte) -2);
            writeLongLong(length);
        }
    }

    final void writeFloat(float f) throws SQLException {
        ensureCapacity(4);
        int i = Float.floatToIntBits(f);
        byte[] b = this.byteBuffer;
        int i2 = this.position;
        this.position = i2 + 1;
        b[i2] = (byte) (i & 255);
        int i3 = this.position;
        this.position = i3 + 1;
        b[i3] = (byte) (i >>> 8);
        int i4 = this.position;
        this.position = i4 + 1;
        b[i4] = (byte) (i >>> 16);
        int i5 = this.position;
        this.position = i5 + 1;
        b[i5] = (byte) (i >>> 24);
    }

    final void writeInt(int i) throws SQLException {
        ensureCapacity(2);
        byte[] b = this.byteBuffer;
        int i2 = this.position;
        this.position = i2 + 1;
        b[i2] = (byte) (i & 255);
        int i3 = this.position;
        this.position = i3 + 1;
        b[i3] = (byte) (i >>> 8);
    }

    final void writeLenBytes(byte[] b) throws SQLException {
        int len = b.length;
        ensureCapacity(len + 9);
        writeFieldLength(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeLenString(String s, String encoding, String serverEncoding, SingleByteCharsetConverter converter, boolean parserKnowsUnicode, MySQLConnection conn) throws SQLException, UnsupportedEncodingException {
        byte[] b;
        if (converter != null) {
            b = converter.toBytes(s);
        } else {
            b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn, conn.getExceptionInterceptor());
        }
        int len = b.length;
        ensureCapacity(len + 9);
        writeFieldLength(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeLong(long i) throws SQLException {
        ensureCapacity(4);
        byte[] b = this.byteBuffer;
        int i2 = this.position;
        this.position = i2 + 1;
        b[i2] = (byte) (i & 255);
        int i3 = this.position;
        this.position = i3 + 1;
        b[i3] = (byte) (i >>> 8);
        int i4 = this.position;
        this.position = i4 + 1;
        b[i4] = (byte) (i >>> 16);
        int i5 = this.position;
        this.position = i5 + 1;
        b[i5] = (byte) (i >>> 24);
    }

    final void writeLongInt(int i) throws SQLException {
        ensureCapacity(3);
        byte[] b = this.byteBuffer;
        int i2 = this.position;
        this.position = i2 + 1;
        b[i2] = (byte) (i & 255);
        int i3 = this.position;
        this.position = i3 + 1;
        b[i3] = (byte) (i >>> 8);
        int i4 = this.position;
        this.position = i4 + 1;
        b[i4] = (byte) (i >>> 16);
    }

    final void writeLongLong(long i) throws SQLException {
        ensureCapacity(8);
        byte[] b = this.byteBuffer;
        int i2 = this.position;
        this.position = i2 + 1;
        b[i2] = (byte) (i & 255);
        int i3 = this.position;
        this.position = i3 + 1;
        b[i3] = (byte) (i >>> 8);
        int i4 = this.position;
        this.position = i4 + 1;
        b[i4] = (byte) (i >>> 16);
        int i5 = this.position;
        this.position = i5 + 1;
        b[i5] = (byte) (i >>> 24);
        int i6 = this.position;
        this.position = i6 + 1;
        b[i6] = (byte) (i >>> 32);
        int i7 = this.position;
        this.position = i7 + 1;
        b[i7] = (byte) (i >>> 40);
        int i8 = this.position;
        this.position = i8 + 1;
        b[i8] = (byte) (i >>> 48);
        int i9 = this.position;
        this.position = i9 + 1;
        b[i9] = (byte) (i >>> 56);
    }

    final void writeString(String s) throws SQLException {
        ensureCapacity((s.length() * 3) + 1);
        writeStringNoNull(s);
        byte[] bArr = this.byteBuffer;
        int i = this.position;
        this.position = i + 1;
        bArr[i] = 0;
    }

    final void writeString(String s, String encoding, MySQLConnection conn) throws SQLException {
        ensureCapacity((s.length() * 3) + 1);
        try {
            writeStringNoNull(s, encoding, encoding, false, conn);
            byte[] bArr = this.byteBuffer;
            int i = this.position;
            this.position = i + 1;
            bArr[i] = 0;
        } catch (UnsupportedEncodingException ue) {
            throw new SQLException(ue.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
        }
    }

    final void writeStringNoNull(String s) throws SQLException {
        int len = s.length();
        ensureCapacity(len * 3);
        System.arraycopy(StringUtils.getBytes(s), 0, this.byteBuffer, this.position, len);
        this.position += len;
    }

    final void writeStringNoNull(String s, String encoding, String serverEncoding, boolean parserKnowsUnicode, MySQLConnection conn) throws SQLException, UnsupportedEncodingException {
        byte[] b = StringUtils.getBytes(s, encoding, serverEncoding, parserKnowsUnicode, conn, conn.getExceptionInterceptor());
        int len = b.length;
        ensureCapacity(len);
        System.arraycopy(b, 0, this.byteBuffer, this.position, len);
        this.position += len;
    }
}
