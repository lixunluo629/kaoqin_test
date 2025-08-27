package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/MysqlPooledConnection.class */
public class MysqlPooledConnection implements PooledConnection {
    private static final Constructor<?> JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR;
    public static final int CONNECTION_ERROR_EVENT = 1;
    public static final int CONNECTION_CLOSED_EVENT = 2;
    private Connection physicalConn;
    private ExceptionInterceptor exceptionInterceptor;
    private java.sql.Connection logicalHandle = null;
    private Map<ConnectionEventListener, ConnectionEventListener> connectionEventListeners = new HashMap();

    static {
        if (!Util.isJdbc4()) {
            JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = null;
            return;
        }
        try {
            JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlPooledConnection").getConstructor(Connection.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (SecurityException e3) {
            throw new RuntimeException(e3);
        }
    }

    protected static MysqlPooledConnection getInstance(Connection connection) throws SQLException {
        return !Util.isJdbc4() ? new MysqlPooledConnection(connection) : (MysqlPooledConnection) Util.handleNewInstance(JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR, new Object[]{connection}, connection.getExceptionInterceptor());
    }

    public MysqlPooledConnection(Connection connection) {
        this.physicalConn = connection;
        this.exceptionInterceptor = this.physicalConn.getExceptionInterceptor();
    }

    @Override // javax.sql.PooledConnection
    public synchronized void addConnectionEventListener(ConnectionEventListener connectioneventlistener) {
        if (this.connectionEventListeners != null) {
            this.connectionEventListeners.put(connectioneventlistener, connectioneventlistener);
        }
    }

    @Override // javax.sql.PooledConnection
    public synchronized void removeConnectionEventListener(ConnectionEventListener connectioneventlistener) {
        if (this.connectionEventListeners != null) {
            this.connectionEventListeners.remove(connectioneventlistener);
        }
    }

    @Override // javax.sql.PooledConnection
    public synchronized java.sql.Connection getConnection() throws SQLException {
        return getConnection(true, false);
    }

    protected synchronized java.sql.Connection getConnection(boolean resetServerState, boolean forXa) throws SQLException {
        if (this.physicalConn == null) {
            SQLException sqlException = SQLError.createSQLException("Physical Connection doesn't exist", this.exceptionInterceptor);
            callConnectionEventListeners(1, sqlException);
            throw sqlException;
        }
        try {
            if (this.logicalHandle != null) {
                ((ConnectionWrapper) this.logicalHandle).close(false);
            }
            if (resetServerState) {
                this.physicalConn.resetServerState();
            }
            this.logicalHandle = ConnectionWrapper.getInstance(this, this.physicalConn, forXa);
            return this.logicalHandle;
        } catch (SQLException sqlException2) {
            callConnectionEventListeners(1, sqlException2);
            throw sqlException2;
        }
    }

    @Override // javax.sql.PooledConnection
    public synchronized void close() throws SQLException {
        if (this.physicalConn != null) {
            this.physicalConn.close();
            this.physicalConn = null;
        }
        if (this.connectionEventListeners != null) {
            this.connectionEventListeners.clear();
            this.connectionEventListeners = null;
        }
    }

    protected synchronized void callConnectionEventListeners(int eventType, SQLException sqlException) {
        if (this.connectionEventListeners == null) {
            return;
        }
        Iterator<Map.Entry<ConnectionEventListener, ConnectionEventListener>> iterator = this.connectionEventListeners.entrySet().iterator();
        ConnectionEvent connectionevent = new ConnectionEvent(this, sqlException);
        while (iterator.hasNext()) {
            ConnectionEventListener connectioneventlistener = iterator.next().getValue();
            if (eventType == 2) {
                connectioneventlistener.connectionClosed(connectionevent);
            } else if (eventType == 1) {
                connectioneventlistener.connectionErrorOccurred(connectionevent);
            }
        }
    }

    protected ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }
}
