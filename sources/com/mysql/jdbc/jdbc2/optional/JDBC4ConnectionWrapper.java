package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.jdbc2.optional.WrapperBase;
import java.lang.reflect.Proxy;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/JDBC4ConnectionWrapper.class */
public class JDBC4ConnectionWrapper extends ConnectionWrapper {
    public JDBC4ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, Connection mysqlConnection, boolean forXa) throws SQLException {
        super(mysqlPooledConnection, mysqlConnection, forXa);
    }

    @Override // com.mysql.jdbc.jdbc2.optional.ConnectionWrapper, java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        try {
            super.close();
        } finally {
            this.unwrappedInterfaces = null;
        }
    }

    @Override // java.sql.Connection
    public SQLXML createSQLXML() throws SQLException {
        checkClosed();
        try {
            return this.mc.createSQLXML();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        checkClosed();
        try {
            return this.mc.createArrayOf(typeName, elements);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        checkClosed();
        try {
            return this.mc.createStruct(typeName, attributes);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Properties getClientInfo() throws SQLException {
        checkClosed();
        try {
            return this.mc.getClientInfo();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public String getClientInfo(String name) throws SQLException {
        checkClosed();
        try {
            return this.mc.getClientInfo(name);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public synchronized boolean isValid(int timeout) throws SQLException {
        try {
            return this.mc.isValid(timeout);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return false;
        }
    }

    @Override // java.sql.Connection
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            checkClosed();
            this.mc.setClientInfo(properties);
        } catch (SQLException sqlException) {
            try {
                checkAndFireConnectionError(sqlException);
            } catch (SQLException sqlEx2) {
                SQLClientInfoException clientEx = new SQLClientInfoException();
                clientEx.initCause(sqlEx2);
                throw clientEx;
            }
        }
    }

    @Override // java.sql.Connection
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            checkClosed();
            this.mc.setClientInfo(name, value);
        } catch (SQLException sqlException) {
            try {
                checkAndFireConnectionError(sqlException);
            } catch (SQLException sqlEx2) {
                SQLClientInfoException clientEx = new SQLClientInfoException();
                clientEx.initCause(sqlEx2);
                throw clientEx;
            }
        }
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkClosed();
        boolean isInstance = iface.isInstance(this);
        return isInstance || iface.getName().equals("com.mysql.jdbc.Connection") || iface.getName().equals("com.mysql.jdbc.ConnectionProperties");
    }

    @Override // java.sql.Wrapper
    public synchronized <T> T unwrap(Class<T> iface) throws SQLException, IllegalArgumentException {
        try {
            if ("java.sql.Connection".equals(iface.getName()) || "java.sql.Wrapper.class".equals(iface.getName())) {
                return iface.cast(this);
            }
            if (this.unwrappedInterfaces == null) {
                this.unwrappedInterfaces = new HashMap();
            }
            Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
            if (cachedUnwrapped == null) {
                cachedUnwrapped = Proxy.newProxyInstance(this.mc.getClass().getClassLoader(), new Class[]{iface}, new WrapperBase.ConnectionErrorFiringInvocationHandler(this.mc));
                this.unwrappedInterfaces.put(iface, cachedUnwrapped);
            }
            return iface.cast(cachedUnwrapped);
        } catch (ClassCastException e) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, this.exceptionInterceptor);
        }
    }

    @Override // java.sql.Connection
    public Blob createBlob() throws SQLException {
        checkClosed();
        try {
            return this.mc.createBlob();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Clob createClob() throws SQLException {
        checkClosed();
        try {
            return this.mc.createClob();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public NClob createNClob() throws SQLException {
        checkClosed();
        try {
            return this.mc.createNClob();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }
}
