package com.mysql.jdbc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Blob.class */
public class Blob implements java.sql.Blob, OutputStreamWatcher {
    private byte[] binaryData = null;
    private boolean isClosed = false;
    private ExceptionInterceptor exceptionInterceptor;

    Blob(ExceptionInterceptor exceptionInterceptor) {
        setBinaryData(Constants.EMPTY_BYTE_ARRAY);
        this.exceptionInterceptor = exceptionInterceptor;
    }

    Blob(byte[] data, ExceptionInterceptor exceptionInterceptor) {
        setBinaryData(data);
        this.exceptionInterceptor = exceptionInterceptor;
    }

    Blob(byte[] data, ResultSetInternalMethods creatorResultSetToSet, int columnIndexToSet) {
        setBinaryData(data);
    }

    private synchronized byte[] getBinaryData() {
        return this.binaryData;
    }

    @Override // java.sql.Blob
    public synchronized InputStream getBinaryStream() throws SQLException {
        checkClosed();
        return new ByteArrayInputStream(getBinaryData());
    }

    @Override // java.sql.Blob
    public synchronized byte[] getBytes(long pos, int length) throws SQLException {
        checkClosed();
        if (pos < 1) {
            throw SQLError.createSQLException(Messages.getString("Blob.2"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        long pos2 = pos - 1;
        if (pos2 > this.binaryData.length) {
            throw SQLError.createSQLException("\"pos\" argument can not be larger than the BLOB's length.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        if (pos2 + length > this.binaryData.length) {
            throw SQLError.createSQLException("\"pos\" + \"length\" arguments can not be larger than the BLOB's length.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        byte[] newData = new byte[length];
        System.arraycopy(getBinaryData(), (int) pos2, newData, 0, length);
        return newData;
    }

    @Override // java.sql.Blob
    public synchronized long length() throws SQLException {
        checkClosed();
        return getBinaryData().length;
    }

    @Override // java.sql.Blob
    public synchronized long position(byte[] pattern, long start) throws SQLException {
        throw SQLError.createSQLException("Not implemented", this.exceptionInterceptor);
    }

    @Override // java.sql.Blob
    public synchronized long position(java.sql.Blob pattern, long start) throws SQLException {
        checkClosed();
        return position(pattern.getBytes(0L, (int) pattern.length()), start);
    }

    private synchronized void setBinaryData(byte[] newBinaryData) {
        this.binaryData = newBinaryData;
    }

    @Override // java.sql.Blob
    public synchronized OutputStream setBinaryStream(long indexToWriteAt) throws SQLException {
        checkClosed();
        if (indexToWriteAt < 1) {
            throw SQLError.createSQLException(Messages.getString("Blob.0"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        WatchableOutputStream bytesOut = new WatchableOutputStream();
        bytesOut.setWatcher(this);
        if (indexToWriteAt > 0) {
            bytesOut.write(this.binaryData, 0, (int) (indexToWriteAt - 1));
        }
        return bytesOut;
    }

    @Override // java.sql.Blob
    public synchronized int setBytes(long writeAt, byte[] bytes) throws SQLException {
        checkClosed();
        return setBytes(writeAt, bytes, 0, bytes.length);
    }

    @Override // java.sql.Blob
    public synchronized int setBytes(long writeAt, byte[] bytes, int offset, int length) throws SQLException, IOException {
        checkClosed();
        OutputStream bytesOut = setBinaryStream(writeAt);
        try {
            try {
                try {
                    bytesOut.write(bytes, offset, length);
                } catch (IOException ioEx) {
                    SQLException sqlEx = SQLError.createSQLException(Messages.getString("Blob.1"), SQLError.SQL_STATE_GENERAL_ERROR, this.exceptionInterceptor);
                    sqlEx.initCause(ioEx);
                    throw sqlEx;
                }
            } catch (IOException e) {
            }
            return length;
        } finally {
            bytesOut.close();
        }
    }

    public synchronized void streamClosed(byte[] byteData) {
        this.binaryData = byteData;
    }

    @Override // com.mysql.jdbc.OutputStreamWatcher
    public synchronized void streamClosed(WatchableOutputStream out) {
        int streamSize = out.size();
        if (streamSize < this.binaryData.length) {
            out.write(this.binaryData, streamSize, this.binaryData.length - streamSize);
        }
        this.binaryData = out.toByteArray();
    }

    @Override // java.sql.Blob
    public synchronized void truncate(long len) throws SQLException {
        checkClosed();
        if (len < 0) {
            throw SQLError.createSQLException("\"len\" argument can not be < 1.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        if (len > this.binaryData.length) {
            throw SQLError.createSQLException("\"len\" argument can not be larger than the BLOB's length.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        byte[] newData = new byte[(int) len];
        System.arraycopy(getBinaryData(), 0, newData, 0, (int) len);
        this.binaryData = newData;
    }

    @Override // java.sql.Blob
    public synchronized void free() throws SQLException {
        this.binaryData = null;
        this.isClosed = true;
    }

    @Override // java.sql.Blob
    public synchronized InputStream getBinaryStream(long pos, long length) throws SQLException {
        checkClosed();
        if (pos < 1) {
            throw SQLError.createSQLException("\"pos\" argument can not be < 1.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        long pos2 = pos - 1;
        if (pos2 > this.binaryData.length) {
            throw SQLError.createSQLException("\"pos\" argument can not be larger than the BLOB's length.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        if (pos2 + length > this.binaryData.length) {
            throw SQLError.createSQLException("\"pos\" + \"length\" arguments can not be larger than the BLOB's length.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
        return new ByteArrayInputStream(getBinaryData(), (int) pos2, (int) length);
    }

    private synchronized void checkClosed() throws SQLException {
        if (this.isClosed) {
            throw SQLError.createSQLException("Invalid operation on closed BLOB", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
    }
}
