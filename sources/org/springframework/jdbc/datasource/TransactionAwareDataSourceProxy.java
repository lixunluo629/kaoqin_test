package org.springframework.jdbc.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.beans.PropertyAccessor;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/TransactionAwareDataSourceProxy.class */
public class TransactionAwareDataSourceProxy extends DelegatingDataSource {
    private boolean reobtainTransactionalConnections;

    public TransactionAwareDataSourceProxy() {
        this.reobtainTransactionalConnections = false;
    }

    public TransactionAwareDataSourceProxy(DataSource targetDataSource) {
        super(targetDataSource);
        this.reobtainTransactionalConnections = false;
    }

    public void setReobtainTransactionalConnections(boolean reobtainTransactionalConnections) {
        this.reobtainTransactionalConnections = reobtainTransactionalConnections;
    }

    @Override // org.springframework.jdbc.datasource.DelegatingDataSource, javax.sql.DataSource
    public Connection getConnection() throws SQLException {
        DataSource ds = getTargetDataSource();
        Assert.state(ds != null, "'targetDataSource' is required");
        return getTransactionAwareConnectionProxy(ds);
    }

    protected Connection getTransactionAwareConnectionProxy(DataSource targetDataSource) {
        return (Connection) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[]{ConnectionProxy.class}, new TransactionAwareInvocationHandler(targetDataSource));
    }

    protected boolean shouldObtainFixedConnection(DataSource targetDataSource) {
        return (TransactionSynchronizationManager.isSynchronizationActive() && this.reobtainTransactionalConnections) ? false : true;
    }

    /* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/TransactionAwareDataSourceProxy$TransactionAwareInvocationHandler.class */
    private class TransactionAwareInvocationHandler implements InvocationHandler {
        private final DataSource targetDataSource;
        private Connection target;
        private boolean closed = false;

        public TransactionAwareInvocationHandler(DataSource targetDataSource) {
            this.targetDataSource = targetDataSource;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                return Boolean.valueOf(proxy == args[0]);
            }
            if (method.getName().equals(IdentityNamingStrategy.HASH_CODE_KEY)) {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
            if (method.getName().equals("toString")) {
                StringBuilder sb = new StringBuilder("Transaction-aware proxy for target Connection ");
                if (this.target != null) {
                    sb.append(PropertyAccessor.PROPERTY_KEY_PREFIX).append(this.target.toString()).append("]");
                } else {
                    sb.append(" from DataSource [").append(this.targetDataSource).append("]");
                }
                return sb.toString();
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
                    DataSourceUtils.doReleaseConnection(this.target, this.targetDataSource);
                    this.closed = true;
                    return null;
                }
                if (method.getName().equals("isClosed")) {
                    return Boolean.valueOf(this.closed);
                }
            }
            if (this.target == null) {
                if (method.getName().equals("getWarnings") || method.getName().equals("clearWarnings")) {
                    return null;
                }
                if (this.closed) {
                    throw new SQLException("Connection handle already closed");
                }
                if (TransactionAwareDataSourceProxy.this.shouldObtainFixedConnection(this.targetDataSource)) {
                    this.target = DataSourceUtils.doGetConnection(this.targetDataSource);
                }
            }
            Connection actualTarget = this.target;
            if (actualTarget == null) {
                actualTarget = DataSourceUtils.doGetConnection(this.targetDataSource);
            }
            if (method.getName().equals("getTargetConnection")) {
                return actualTarget;
            }
            try {
                try {
                    Object retVal = method.invoke(actualTarget, args);
                    if (retVal instanceof Statement) {
                        DataSourceUtils.applyTransactionTimeout((Statement) retVal, this.targetDataSource);
                    }
                    return retVal;
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            } finally {
                if (actualTarget != this.target) {
                    DataSourceUtils.doReleaseConnection(actualTarget, this.targetDataSource);
                }
            }
        }
    }
}
