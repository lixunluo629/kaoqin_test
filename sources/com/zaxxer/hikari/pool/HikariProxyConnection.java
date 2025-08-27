package com.zaxxer.hikari.pool;

import com.zaxxer.hikari.util.FastList;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Wrapper;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/pool/HikariProxyConnection.class */
public class HikariProxyConnection extends ProxyConnection implements Wrapper, AutoCloseable, Connection {
    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public Statement createStatement() throws SQLException {
        try {
            return super.createStatement();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str) throws SQLException {
        try {
            return super.prepareStatement(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public CallableStatement prepareCall(String str) throws SQLException {
        try {
            return super.prepareCall(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public String nativeSQL(String str) throws SQLException {
        try {
            return this.delegate.nativeSQL(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void setAutoCommit(boolean z) throws SQLException {
        try {
            super.setAutoCommit(z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        try {
            return this.delegate.getAutoCommit();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void commit() throws SQLException {
        try {
            super.commit();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void rollback() throws SQLException {
        try {
            super.rollback();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public boolean isClosed() throws SQLException {
        try {
            return super.isClosed();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        try {
            return super.getMetaData();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void setReadOnly(boolean z) throws SQLException {
        try {
            super.setReadOnly(z);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        try {
            return this.delegate.isReadOnly();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void setCatalog(String str) throws SQLException {
        try {
            super.setCatalog(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public String getCatalog() throws SQLException {
        try {
            return this.delegate.getCatalog();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void setTransactionIsolation(int i) throws SQLException {
        try {
            super.setTransactionIsolation(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public int getTransactionIsolation() throws SQLException {
        try {
            return this.delegate.getTransactionIsolation();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        try {
            return this.delegate.getWarnings();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public void clearWarnings() throws SQLException {
        try {
            this.delegate.clearWarnings();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public Statement createStatement(int i, int i2) throws SQLException {
        try {
            return super.createStatement(i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str, int i, int i2) throws SQLException {
        try {
            return super.prepareStatement(str, i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public CallableStatement prepareCall(String str, int i, int i2) throws SQLException {
        try {
            return super.prepareCall(str, i, i2);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Map getTypeMap() throws SQLException {
        try {
            return this.delegate.getTypeMap();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map map) throws SQLException {
        try {
            this.delegate.setTypeMap(map);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public void setHoldability(int i) throws SQLException {
        try {
            this.delegate.setHoldability(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public int getHoldability() throws SQLException {
        try {
            return this.delegate.getHoldability();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        try {
            return this.delegate.setSavepoint();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String str) throws SQLException {
        try {
            return this.delegate.setSavepoint(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public void rollback(Savepoint savepoint) throws SQLException {
        try {
            super.rollback(savepoint);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        try {
            this.delegate.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public Statement createStatement(int i, int i2, int i3) throws SQLException {
        try {
            return super.createStatement(i, i2, i3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str, int i, int i2, int i3) throws SQLException {
        try {
            return super.prepareStatement(str, i, i2, i3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public CallableStatement prepareCall(String str, int i, int i2, int i3) throws SQLException {
        try {
            return super.prepareCall(str, i, i2, i3);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str, int i) throws SQLException {
        try {
            return super.prepareStatement(str, i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str, int[] iArr) throws SQLException {
        try {
            return super.prepareStatement(str, iArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection, java.sql.Connection
    public PreparedStatement prepareStatement(String str, String[] strArr) throws SQLException {
        try {
            return super.prepareStatement(str, strArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Clob createClob() throws SQLException {
        try {
            return this.delegate.createClob();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Blob createBlob() throws SQLException {
        try {
            return this.delegate.createBlob();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public NClob createNClob() throws SQLException {
        try {
            return this.delegate.createNClob();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public SQLXML createSQLXML() throws SQLException {
        try {
            return this.delegate.createSQLXML();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public boolean isValid(int i) throws SQLException {
        try {
            return this.delegate.isValid(i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public void setClientInfo(String str, String str2) throws SQLClientInfoException {
        this.delegate.setClientInfo(str, str2);
    }

    @Override // java.sql.Connection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.delegate.setClientInfo(properties);
    }

    @Override // java.sql.Connection
    public String getClientInfo(String str) throws SQLException {
        try {
            return this.delegate.getClientInfo(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Properties getClientInfo() throws SQLException {
        try {
            return this.delegate.getClientInfo();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Array createArrayOf(String str, Object[] objArr) throws SQLException {
        try {
            return this.delegate.createArrayOf(str, objArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // java.sql.Connection
    public Struct createStruct(String str, Object[] objArr) throws SQLException {
        try {
            return this.delegate.createStruct(str, objArr);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection
    public void setSchema(String str) throws SQLException {
        try {
            super.setSchema(str);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public String getSchema() throws SQLException {
        try {
            return this.delegate.getSchema();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public void abort(Executor executor) throws SQLException {
        try {
            this.delegate.abort(executor);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    @Override // com.zaxxer.hikari.pool.ProxyConnection
    public void setNetworkTimeout(Executor executor, int i) throws SQLException {
        try {
            super.setNetworkTimeout(executor, i);
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    public int getNetworkTimeout() throws SQLException {
        try {
            return this.delegate.getNetworkTimeout();
        } catch (SQLException e) {
            throw checkException(e);
        }
    }

    protected HikariProxyConnection(PoolEntry poolEntry, Connection connection, FastList fastList, ProxyLeakTask proxyLeakTask, long j, boolean z, boolean z2) {
        super(poolEntry, connection, fastList, proxyLeakTask, j, z, z2);
    }
}
