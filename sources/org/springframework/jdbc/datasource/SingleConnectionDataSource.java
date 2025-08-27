package org.springframework.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/SingleConnectionDataSource.class */
public class SingleConnectionDataSource extends DriverManagerDataSource implements SmartDataSource, DisposableBean {
    private boolean suppressClose;
    private Boolean autoCommit;
    private Connection target;
    private Connection connection;
    private final Object connectionMonitor;

    public SingleConnectionDataSource() {
        this.connectionMonitor = new Object();
    }

    public SingleConnectionDataSource(String url, String username, String password, boolean suppressClose) {
        super(url, username, password);
        this.connectionMonitor = new Object();
        this.suppressClose = suppressClose;
    }

    public SingleConnectionDataSource(String url, boolean suppressClose) {
        super(url);
        this.connectionMonitor = new Object();
        this.suppressClose = suppressClose;
    }

    public SingleConnectionDataSource(Connection target, boolean suppressClose) {
        this.connectionMonitor = new Object();
        Assert.notNull(target, "Connection must not be null");
        this.target = target;
        this.suppressClose = suppressClose;
        this.connection = suppressClose ? getCloseSuppressingConnectionProxy(target) : target;
    }

    public void setSuppressClose(boolean suppressClose) {
        this.suppressClose = suppressClose;
    }

    protected boolean isSuppressClose() {
        return this.suppressClose;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = Boolean.valueOf(autoCommit);
    }

    protected Boolean getAutoCommitValue() {
        return this.autoCommit;
    }

    @Override // org.springframework.jdbc.datasource.AbstractDriverBasedDataSource, javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        Connection connection;
        synchronized (this.connectionMonitor) {
            if (this.connection == null) {
                initConnection();
            }
            if (this.connection.isClosed()) {
                throw new SQLException("Connection was closed in SingleConnectionDataSource. Check that user code checks shouldClose() before closing Connections, or set 'suppressClose' to 'true'");
            }
            connection = this.connection;
        }
        return connection;
    }

    @Override // org.springframework.jdbc.datasource.AbstractDriverBasedDataSource, javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        if (ObjectUtils.nullSafeEquals(username, getUsername()) && ObjectUtils.nullSafeEquals(password, getPassword())) {
            return getConnection();
        }
        throw new SQLException("SingleConnectionDataSource does not support custom username and password");
    }

    @Override // org.springframework.jdbc.datasource.SmartDataSource
    public boolean shouldClose(Connection con) {
        boolean z;
        synchronized (this.connectionMonitor) {
            z = (con == this.connection || con == this.target) ? false : true;
        }
        return z;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        synchronized (this.connectionMonitor) {
            closeConnection();
        }
    }

    public void initConnection() throws SQLException {
        if (getUrl() == null) {
            throw new IllegalStateException("'url' property is required for lazily initializing a Connection");
        }
        synchronized (this.connectionMonitor) {
            closeConnection();
            this.target = getConnectionFromDriver(getUsername(), getPassword());
            prepareConnection(this.target);
            if (this.logger.isInfoEnabled()) {
                this.logger.info("Established shared JDBC Connection: " + this.target);
            }
            this.connection = isSuppressClose() ? getCloseSuppressingConnectionProxy(this.target) : this.target;
        }
    }

    public void resetConnection() {
        synchronized (this.connectionMonitor) {
            closeConnection();
            this.target = null;
            this.connection = null;
        }
    }

    protected void prepareConnection(Connection con) throws SQLException {
        Boolean autoCommit = getAutoCommitValue();
        if (autoCommit != null && con.getAutoCommit() != autoCommit.booleanValue()) {
            con.setAutoCommit(autoCommit.booleanValue());
        }
    }

    private void closeConnection() {
        if (this.target != null) {
            try {
                this.target.close();
            } catch (Throwable ex) {
                this.logger.warn("Could not close shared JDBC Connection", ex);
            }
        }
    }

    protected Connection getCloseSuppressingConnectionProxy(Connection target) {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new CloseSuppressingInvocationHandler(target));
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/SingleConnectionDataSource$CloseSuppressingInvocationHandler.class */
    private static class CloseSuppressingInvocationHandler implements InvocationHandler {
        private final Connection target;

        public CloseSuppressingInvocationHandler(Connection target) {
            this.target = target;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(proxy == args[0]);
            }
            if (method.getName().equals(IdentityNamingStrategy.HASH_CODE_KEY)) {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
            if (method.getName().equals("unwrap")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return proxy;
                }
            } else if (method.getName().equals("isWrapperFor")) {
                if (((Class) args[0]).isInstance(proxy)) {
                    return true;
                }
            } else {
                if (method.getName().equals("close")) {
                    return null;
                }
                if (method.getName().equals("isClosed")) {
                    return false;
                }
                if (method.getName().equals("getTargetConnection")) {
                    return this.target;
                }
            }
            try {
                return method.invoke(this.target, args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
