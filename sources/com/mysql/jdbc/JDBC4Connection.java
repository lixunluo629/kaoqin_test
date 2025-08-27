package com.mysql.jdbc;

import java.sql.Array;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4Connection.class */
public class JDBC4Connection extends ConnectionImpl implements JDBC4MySQLConnection {
    private static final long serialVersionUID = 2877471301981509475L;
    private JDBC4ClientInfoProvider infoProvider;

    public JDBC4Connection(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
        super(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public SQLXML createSQLXML() throws SQLException {
        return new JDBC4MysqlSQLXML(getExceptionInterceptor());
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public Properties getClientInfo() throws SQLException {
        return getClientInfoProviderImpl().getClientInfo(this);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public String getClientInfo(String name) throws SQLException {
        return getClientInfoProviderImpl().getClientInfo(this, name);
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public boolean isValid(int timeout) throws SQLException {
        synchronized (getConnectionMutex()) {
            if (isClosed()) {
                return false;
            }
            try {
                try {
                    pingInternal(false, timeout * 1000);
                    return true;
                } catch (Throwable th) {
                    return false;
                }
            } catch (Throwable th2) {
                try {
                    abortInternal();
                } catch (Throwable th3) {
                }
                return false;
            }
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            getClientInfoProviderImpl().setClientInfo(this, properties);
        } catch (SQLClientInfoException ciEx) {
            throw ciEx;
        } catch (SQLException sqlEx) {
            SQLClientInfoException clientInfoEx = new SQLClientInfoException();
            clientInfoEx.initCause(sqlEx);
            throw clientInfoEx;
        }
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            getClientInfoProviderImpl().setClientInfo(this, name, value);
        } catch (SQLClientInfoException ciEx) {
            throw ciEx;
        } catch (SQLException sqlEx) {
            SQLClientInfoException clientInfoEx = new SQLClientInfoException();
            clientInfoEx.initCause(sqlEx);
            throw clientInfoEx;
        }
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
        return new Blob(getExceptionInterceptor());
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public java.sql.Clob createClob() {
        return new Clob(getExceptionInterceptor());
    }

    @Override // java.sql.Connection, com.mysql.jdbc.JDBC4MySQLConnection
    public NClob createNClob() {
        return new JDBC4NClob(getExceptionInterceptor());
    }

    @Override // com.mysql.jdbc.JDBC4MySQLConnection
    public JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
        JDBC4ClientInfoProvider jDBC4ClientInfoProvider;
        synchronized (getConnectionMutex()) {
            if (this.infoProvider == null) {
                try {
                    try {
                        this.infoProvider = (JDBC4ClientInfoProvider) Util.getInstance(getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor());
                    } catch (ClassCastException e) {
                        throw SQLError.createSQLException(Messages.getString("JDBC4Connection.ClientInfoNotImplemented", new Object[]{getClientInfoProvider()}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                } catch (SQLException sqlEx) {
                    if (sqlEx.getCause() instanceof ClassCastException) {
                        this.infoProvider = (JDBC4ClientInfoProvider) Util.getInstance("com.mysql.jdbc." + getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor());
                    }
                }
                this.infoProvider.initialize(this, this.props);
            }
            jDBC4ClientInfoProvider = this.infoProvider;
        }
        return jDBC4ClientInfoProvider;
    }
}
