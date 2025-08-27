package org.springframework.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Constants;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy.class */
public class LazyConnectionDataSourceProxy extends DelegatingDataSource {
    private static final Constants constants = new Constants(Connection.class);
    private static final Log logger = LogFactory.getLog(LazyConnectionDataSourceProxy.class);
    private Boolean defaultAutoCommit;
    private Integer defaultTransactionIsolation;

    public LazyConnectionDataSourceProxy() {
    }

    public LazyConnectionDataSourceProxy(DataSource targetDataSource) throws SQLException {
        setTargetDataSource(targetDataSource);
        afterPropertiesSet();
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = Boolean.valueOf(defaultAutoCommit);
    }

    public void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
        this.defaultTransactionIsolation = Integer.valueOf(defaultTransactionIsolation);
    }

    public void setDefaultTransactionIsolationName(String constantName) {
        setDefaultTransactionIsolation(constants.asNumber(constantName).intValue());
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws SQLException {
        super.afterPropertiesSet();
        if (this.defaultAutoCommit == null || this.defaultTransactionIsolation == null) {
            try {
                Connection con = getTargetDataSource().getConnection();
                try {
                    checkDefaultConnectionProperties(con);
                    con.close();
                } catch (Throwable th) {
                    con.close();
                    throw th;
                }
            } catch (SQLException ex) {
                logger.warn("Could not retrieve default auto-commit and transaction isolation settings", ex);
            }
        }
    }

    protected synchronized void checkDefaultConnectionProperties(Connection con) throws SQLException {
        if (this.defaultAutoCommit == null) {
            this.defaultAutoCommit = Boolean.valueOf(con.getAutoCommit());
        }
        if (this.defaultTransactionIsolation == null) {
            this.defaultTransactionIsolation = Integer.valueOf(con.getTransactionIsolation());
        }
    }

    protected Boolean defaultAutoCommit() {
        return this.defaultAutoCommit;
    }

    protected Integer defaultTransactionIsolation() {
        return this.defaultTransactionIsolation;
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new LazyConnectionInvocationHandler());
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, javax.sql.DataSource
    public Connection getConnection(String username, String password) throws SQLException {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new LazyConnectionInvocationHandler(this, username, password));
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/LazyConnectionDataSourceProxy$LazyConnectionInvocationHandler.class */
    private class LazyConnectionInvocationHandler implements InvocationHandler {
        private String username;
        private String password;
        private Integer transactionIsolation;
        private Boolean autoCommit;
        private boolean readOnly;
        private boolean closed;
        private Connection target;

        public LazyConnectionInvocationHandler() {
            this.readOnly = false;
            this.closed = false;
            this.autoCommit = LazyConnectionDataSourceProxy.this.defaultAutoCommit();
            this.transactionIsolation = LazyConnectionDataSourceProxy.this.defaultTransactionIsolation();
        }

        public LazyConnectionInvocationHandler(LazyConnectionDataSourceProxy lazyConnectionDataSourceProxy, String username, String password) {
            this();
            this.username = username;
            this.password = password;
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
            } else if (method.getName().equals("getTargetConnection")) {
                return getTargetConnection(method);
            }
            if (!hasTargetConnection()) {
                if (method.getName().equals("toString")) {
                    return "Lazy Connection proxy for target DataSource [" + LazyConnectionDataSourceProxy.this.getTargetDataSource() + "]";
                }
                if (method.getName().equals("getAutoCommit")) {
                    if (this.autoCommit != null) {
                        return this.autoCommit;
                    }
                } else {
                    if (method.getName().equals("setAutoCommit")) {
                        this.autoCommit = (Boolean) args[0];
                        return null;
                    }
                    if (method.getName().equals("getTransactionIsolation")) {
                        if (this.transactionIsolation != null) {
                            return this.transactionIsolation;
                        }
                    } else {
                        if (method.getName().equals("setTransactionIsolation")) {
                            this.transactionIsolation = (Integer) args[0];
                            return null;
                        }
                        if (method.getName().equals("isReadOnly")) {
                            return Boolean.valueOf(this.readOnly);
                        }
                        if (method.getName().equals("setReadOnly")) {
                            this.readOnly = ((Boolean) args[0]).booleanValue();
                            return null;
                        }
                        if (method.getName().equals("commit") || method.getName().equals("rollback") || method.getName().equals("getWarnings") || method.getName().equals("clearWarnings")) {
                            return null;
                        }
                        if (method.getName().equals("close")) {
                            this.closed = true;
                            return null;
                        }
                        if (method.getName().equals("isClosed")) {
                            return Boolean.valueOf(this.closed);
                        }
                        if (this.closed) {
                            throw new SQLException("Illegal operation: connection is closed");
                        }
                    }
                }
            }
            try {
                return method.invoke(getTargetConnection(method), args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }

        private boolean hasTargetConnection() {
            return this.target != null;
        }

        private Connection getTargetConnection(Method operation) throws SQLException {
            Connection connection;
            if (this.target == null) {
                if (LazyConnectionDataSourceProxy.logger.isDebugEnabled()) {
                    LazyConnectionDataSourceProxy.logger.debug("Connecting to database for operation '" + operation.getName() + "'");
                }
                if (this.username != null) {
                    connection = LazyConnectionDataSourceProxy.this.getTargetDataSource().getConnection(this.username, this.password);
                } else {
                    connection = LazyConnectionDataSourceProxy.this.getTargetDataSource().getConnection();
                }
                this.target = connection;
                LazyConnectionDataSourceProxy.this.checkDefaultConnectionProperties(this.target);
                if (this.readOnly) {
                    try {
                        this.target.setReadOnly(true);
                    } catch (Exception ex) {
                        LazyConnectionDataSourceProxy.logger.debug("Could not set JDBC Connection read-only", ex);
                    }
                }
                if (this.transactionIsolation != null && !this.transactionIsolation.equals(LazyConnectionDataSourceProxy.this.defaultTransactionIsolation())) {
                    this.target.setTransactionIsolation(this.transactionIsolation.intValue());
                }
                if (this.autoCommit != null && this.autoCommit.booleanValue() != this.target.getAutoCommit()) {
                    this.target.setAutoCommit(this.autoCommit.booleanValue());
                }
            } else if (LazyConnectionDataSourceProxy.logger.isDebugEnabled()) {
                LazyConnectionDataSourceProxy.logger.debug("Using existing database connection for operation '" + operation.getName() + "'");
            }
            return this.target;
        }
    }
}
