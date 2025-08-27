package com.zaxxer.hikari.pool;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Wrapper;
import java.util.Calendar;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariProxyPreparedStatement.class */
public class HikariProxyPreparedStatement extends ProxyPreparedStatement implements Wrapper, AutoCloseable, Statement, PreparedStatement {
    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class cls) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).isWrapperFor(cls);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public ResultSet executeQuery(String str) throws SQLException {
        try {
            return super.executeQuery(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public int executeUpdate(String str) throws SQLException {
        try {
            return super.executeUpdate(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getMaxFieldSize() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getMaxFieldSize();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setMaxFieldSize(int i) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setMaxFieldSize(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getMaxRows() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getMaxRows();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setMaxRows(int i) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setMaxRows(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setEscapeProcessing(boolean z) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setEscapeProcessing(z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getQueryTimeout() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getQueryTimeout();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setQueryTimeout(int i) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setQueryTimeout(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void cancel() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).cancel();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public SQLWarning getWarnings() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getWarnings();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void clearWarnings() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).clearWarnings();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setCursorName(String str) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setCursorName(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public boolean execute(String str) throws SQLException {
        try {
            return super.execute(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public ResultSet getResultSet() throws SQLException {
        try {
            return super.getResultSet();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getUpdateCount() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getUpdateCount();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public boolean getMoreResults() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getMoreResults();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setFetchDirection(int i) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setFetchDirection(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getFetchDirection() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getFetchDirection();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setFetchSize(int i) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setFetchSize(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getFetchSize() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getFetchSize();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getResultSetConcurrency() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getResultSetConcurrency();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getResultSetType() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getResultSetType();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void addBatch(String str) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).addBatch(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void clearBatch() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).clearBatch();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public int[] executeBatch() throws SQLException {
        try {
            return super.executeBatch();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public Connection getConnection() throws SQLException {
        try {
            return super.getConnection();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public boolean getMoreResults(int i) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getMoreResults(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            return super.getGeneratedKeys();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public int executeUpdate(String str, int i) throws SQLException {
        try {
            return super.executeUpdate(str, i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public int executeUpdate(String str, int[] iArr) throws SQLException {
        try {
            return super.executeUpdate(str, iArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public int executeUpdate(String str, String[] strArr) throws SQLException {
        try {
            return super.executeUpdate(str, strArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public boolean execute(String str, int i) throws SQLException {
        try {
            return super.execute(str, i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public boolean execute(String str, int[] iArr) throws SQLException {
        try {
            return super.execute(str, iArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement, java.sql.Statement
    public boolean execute(String str, String[] strArr) throws SQLException {
        try {
            return super.execute(str, strArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public int getResultSetHoldability() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getResultSetHoldability();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public boolean isClosed() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).isClosed();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public void setPoolable(boolean z) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setPoolable(z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Statement
    public boolean isPoolable() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).isPoolable();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public void closeOnCompletion() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).closeOnCompletion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public boolean isCloseOnCompletion() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).isCloseOnCompletion();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public long getLargeUpdateCount() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getLargeUpdateCount();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public void setLargeMaxRows(long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setLargeMaxRows(j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public long getLargeMaxRows() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getLargeMaxRows();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement
    public long[] executeLargeBatch() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeBatch();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement
    public long executeLargeUpdate(String str) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeUpdate(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement
    public long executeLargeUpdate(String str, int i) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeUpdate(str, i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement
    public long executeLargeUpdate(String str, int[] iArr) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeUpdate(str, iArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyStatement
    public long executeLargeUpdate(String str, String[] strArr) throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeUpdate(str, strArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyPreparedStatement, java.sql.PreparedStatement
    public ResultSet executeQuery() throws SQLException {
        try {
            return super.executeQuery();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyPreparedStatement, java.sql.PreparedStatement
    public int executeUpdate() throws SQLException {
        try {
            return super.executeUpdate();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNull(int i, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNull(i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBoolean(int i, boolean z) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBoolean(i, z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setByte(int i, byte b) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setByte(i, b);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setShort(int i, short s) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setShort(i, s);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setInt(int i, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setInt(i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setLong(int i, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setLong(i, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setFloat(int i, float f) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setFloat(i, f);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setDouble(int i, double d) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setDouble(i, d);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBigDecimal(i, bigDecimal);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setString(int i, String str) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setString(i, str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBytes(int i, byte[] bArr) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBytes(i, bArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setDate(int i, Date date) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setDate(i, date);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTime(int i, Time time) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setTime(i, time);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setTimestamp(i, timestamp);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int i, InputStream inputStream, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setAsciiStream(i, inputStream, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setUnicodeStream(int i, InputStream inputStream, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setUnicodeStream(i, inputStream, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int i, InputStream inputStream, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBinaryStream(i, inputStream, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void clearParameters() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).clearParameters();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setObject(int i, Object obj, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setObject(i, obj, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setObject(int i, Object obj) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setObject(i, obj);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyPreparedStatement, java.sql.PreparedStatement
    public boolean execute() throws SQLException {
        try {
            return super.execute();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void addBatch() throws SQLException {
        try {
            ((PreparedStatement) this.delegate).addBatch();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int i, Reader reader, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setCharacterStream(i, reader, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setRef(int i, Ref ref) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setRef(i, ref);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int i, Blob blob) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBlob(i, blob);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int i, Clob clob) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setClob(i, clob);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setArray(int i, Array array) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setArray(i, array);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public ResultSetMetaData getMetaData() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getMetaData();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setDate(int i, Date date, Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setDate(i, date, calendar);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTime(int i, Time time, Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setTime(i, time, calendar);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setTimestamp(i, timestamp, calendar);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNull(int i, int i2, String str) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNull(i, i2, str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setURL(int i, URL url) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setURL(i, url);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public ParameterMetaData getParameterMetaData() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).getParameterMetaData();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setRowId(int i, RowId rowId) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setRowId(i, rowId);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNString(int i, String str) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNString(i, str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNCharacterStream(int i, Reader reader, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNCharacterStream(i, reader, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int i, NClob nClob) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNClob(i, nClob);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int i, Reader reader, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setClob(i, reader, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int i, InputStream inputStream, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBlob(i, inputStream, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int i, Reader reader, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNClob(i, reader, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setSQLXML(int i, SQLXML sqlxml) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setSQLXML(i, sqlxml);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setObject(int i, Object obj, int i2, int i3) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setObject(i, obj, i2, i3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int i, InputStream inputStream, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setAsciiStream(i, inputStream, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int i, InputStream inputStream, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBinaryStream(i, inputStream, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int i, Reader reader, long j) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setCharacterStream(i, reader, j);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setAsciiStream(int i, InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setAsciiStream(i, inputStream);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBinaryStream(int i, InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBinaryStream(i, inputStream);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setCharacterStream(int i, Reader reader) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setCharacterStream(i, reader);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNCharacterStream(int i, Reader reader) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNCharacterStream(i, reader);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setClob(int i, Reader reader) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setClob(i, reader);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setBlob(int i, InputStream inputStream) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setBlob(i, inputStream);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.PreparedStatement
    public void setNClob(int i, Reader reader) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setNClob(i, reader);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public void setObject(int i, Object obj, SQLType sQLType, int i2) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setObject(i, obj, sQLType, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public void setObject(int i, Object obj, SQLType sQLType) throws SQLException {
        try {
            ((PreparedStatement) this.delegate).setObject(i, obj, sQLType);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyPreparedStatement
    public long executeLargeUpdate() throws SQLException {
        try {
            return ((PreparedStatement) this.delegate).executeLargeUpdate();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    HikariProxyPreparedStatement(ProxyConnection proxyConnection, PreparedStatement preparedStatement) {
        super(proxyConnection, preparedStatement);
    }
}
