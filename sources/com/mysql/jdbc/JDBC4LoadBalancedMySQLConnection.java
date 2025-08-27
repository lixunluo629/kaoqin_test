package com.mysql.jdbc;

import java.sql.Array;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4LoadBalancedMySQLConnection.class */
public class JDBC4LoadBalancedMySQLConnection extends LoadBalancedMySQLConnection implements JDBC4MySQLConnection {
    public JDBC4LoadBalancedMySQLConnection(LoadBalancedConnectionProxy proxy) throws SQLException {
        super(proxy);
    }

    private JDBC4MySQLConnection getJDBC4Connection() {
        return (JDBC4MySQLConnection) getActiveMySQLConnection();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public SQLXML createSQLXML() throws SQLException {
        return getJDBC4Connection().createSQLXML();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getJDBC4Connection().createArrayOf(typeName, elements);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getJDBC4Connection().createStruct(typeName, attributes);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Properties getClientInfo() throws SQLException {
        return getJDBC4Connection().getClientInfo();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public String getClientInfo(String name) throws SQLException {
        return getJDBC4Connection().getClientInfo(name);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public boolean isValid(int timeout) throws SQLException {
        return getJDBC4Connection().isValid(timeout);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        getJDBC4Connection().setClientInfo(properties);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        getJDBC4Connection().setClientInfo(name, value);
    }

    @Override // java.sql.Wrapper, com.mysql.jdbc.JDBC4MySQLConnection
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkClosed();
        return iface.isInstance(this);
    }

    @Override // java.sql.Wrapper, com.mysql.jdbc.JDBC4MySQLConnection
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException e) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public java.sql.Blob createBlob() {
        return getJDBC4Connection().createBlob();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public java.sql.Clob createClob() {
        return getJDBC4Connection().createClob();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public NClob createNClob() {
        return getJDBC4Connection().createNClob();
    }

    @Override // com.mysql.jdbc.JDBC4MySQLConnection
    public JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
        JDBC4ClientInfoProvider clientInfoProviderImpl;
        synchronized (getThisAsProxy()) {
            clientInfoProviderImpl = getJDBC4Connection().getClientInfoProviderImpl();
        }
        return clientInfoProviderImpl;
    }
}
