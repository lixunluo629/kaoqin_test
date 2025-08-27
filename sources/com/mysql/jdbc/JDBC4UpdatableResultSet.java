package com.mysql.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4UpdatableResultSet.class */
public class JDBC4UpdatableResultSet extends UpdatableResultSet {
    public JDBC4UpdatableResultSet(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        super(catalog, fields, tuples, conn, creatorStmt);
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        updateNCharacterStream(columnIndex, x, (int) length);
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        updateAsciiStream(findColumn(columnLabel), x);
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        updateAsciiStream(findColumn(columnLabel), x, length);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        updateBinaryStream(findColumn(columnLabel), x);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        updateBinaryStream(findColumn(columnLabel), x, length);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        updateBlob(findColumn(columnLabel), inputStream);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        updateBlob(findColumn(columnLabel), inputStream, length);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        updateCharacterStream(findColumn(columnLabel), reader);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        updateCharacterStream(findColumn(columnLabel), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        updateClob(findColumn(columnLabel), reader);
    }

    @Override // java.sql.ResultSet
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        updateClob(findColumn(columnLabel), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        updateNCharacterStream(findColumn(columnLabel), reader);
    }

    @Override // java.sql.ResultSet
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        updateNCharacterStream(findColumn(columnLabel), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        updateNClob(findColumn(columnLabel), reader);
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        updateNClob(findColumn(columnLabel), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        updateSQLXML(findColumn(columnLabel), xmlObject);
    }

    public void updateNCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
            if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
                throw new SQLException("Can not call updateNCharacterStream() when field's character set isn't UTF-8");
            }
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                ((JDBC4PreparedStatement) this.updater).setNCharacterStream(columnIndex, x, length);
            } else {
                ((JDBC4PreparedStatement) this.inserter).setNCharacterStream(columnIndex, x, length);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
                }
            }
        }
    }

    public void updateNCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        updateNCharacterStream(findColumn(columnName), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
            if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
                throw new SQLException("Can not call updateNClob() when field's character set isn't UTF-8");
            }
            if (nClob == null) {
                updateNull(columnIndex);
            } else {
                updateNCharacterStream(columnIndex, nClob.getCharacterStream(), (int) nClob.length());
            }
        }
    }

    @Override // java.sql.ResultSet
    public void updateNClob(String columnName, NClob nClob) throws SQLException {
        updateNClob(findColumn(columnName), nClob);
    }

    @Override // java.sql.ResultSet
    public void updateNString(int columnIndex, String x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
            if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
                throw new SQLException("Can not call updateNString() when field's character set isn't UTF-8");
            }
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                ((JDBC4PreparedStatement) this.updater).setNString(columnIndex, x);
            } else {
                ((JDBC4PreparedStatement) this.inserter).setNString(columnIndex, x);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, StringUtils.getBytes(x, this.charConverter, fieldEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
                }
            }
        }
    }

    @Override // java.sql.ResultSet
    public void updateNString(String columnName, String x) throws SQLException {
        updateNString(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public int getHoldability() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    protected NClob getNativeNClob(int columnIndex) throws SQLException {
        String stringVal = getStringForNClob(columnIndex);
        if (stringVal == null) {
            return null;
        }
        return getNClobFromString(stringVal, columnIndex);
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
            throw new SQLException("Can not call getNCharacterStream() when field's charset isn't UTF-8");
        }
        return getCharacterStream(columnIndex);
    }

    @Override // java.sql.ResultSet
    public Reader getNCharacterStream(String columnName) throws SQLException {
        return getNCharacterStream(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(int columnIndex) throws SQLException {
        String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
            throw new SQLException("Can not call getNClob() when field's charset isn't UTF-8");
        }
        if (!this.isBinaryEncoded) {
            String asString = getStringForNClob(columnIndex);
            if (asString == null) {
                return null;
            }
            return new JDBC4NClob(asString, getExceptionInterceptor());
        }
        return getNativeNClob(columnIndex);
    }

    @Override // java.sql.ResultSet
    public NClob getNClob(String columnName) throws SQLException {
        return getNClob(findColumn(columnName));
    }

    private final NClob getNClobFromString(String stringVal, int columnIndex) throws SQLException {
        return new JDBC4NClob(stringVal, getExceptionInterceptor());
    }

    @Override // java.sql.ResultSet
    public String getNString(int columnIndex) throws SQLException {
        String fieldEncoding = this.fields[columnIndex - 1].getEncoding();
        if (fieldEncoding == null || !fieldEncoding.equals("UTF-8")) {
            throw new SQLException("Can not call getNString() when field's charset isn't UTF-8");
        }
        return getString(columnIndex);
    }

    @Override // java.sql.ResultSet
    public String getNString(String columnName) throws SQLException {
        return getNString(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(int columnIndex) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public RowId getRowId(String columnLabel) throws SQLException {
        return getRowId(findColumn(columnLabel));
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return new JDBC4MysqlSQLXML(this, columnIndex, getExceptionInterceptor());
    }

    @Override // java.sql.ResultSet
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return getSQLXML(findColumn(columnLabel));
    }

    private String getStringForNClob(int columnIndex) throws SQLException {
        byte[] asBytes;
        String asString = null;
        try {
            if (!this.isBinaryEncoded) {
                asBytes = getBytes(columnIndex);
            } else {
                asBytes = getNativeBytes(columnIndex, true);
            }
            if (asBytes != null) {
                asString = new String(asBytes, "UTF-8");
            }
            return asString;
        } catch (UnsupportedEncodingException e) {
            throw SQLError.createSQLException("Unsupported character encoding UTF-8", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, com.mysql.jdbc.ResultSetInternalMethods, java.sql.ResultSet
    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkClosed();
        return iface.isInstance(this);
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException e) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }
}
