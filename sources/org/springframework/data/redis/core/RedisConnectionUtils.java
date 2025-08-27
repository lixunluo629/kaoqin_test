package org.springframework.data.redis.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.transaction.support.ResourceHolder;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisConnectionUtils.class */
public abstract class RedisConnectionUtils {
    private static final Log log = LogFactory.getLog(RedisConnectionUtils.class);

    public static RedisConnection bindConnection(RedisConnectionFactory factory) {
        return bindConnection(factory, false);
    }

    public static RedisConnection bindConnection(RedisConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, true, enableTranactionSupport);
    }

    public static RedisConnection getConnection(RedisConnectionFactory factory) {
        return getConnection(factory, false);
    }

    public static RedisConnection getConnection(RedisConnectionFactory factory, boolean enableTranactionSupport) {
        return doGetConnection(factory, true, false, enableTranactionSupport);
    }

    public static RedisConnection doGetConnection(RedisConnectionFactory factory, boolean allowCreate, boolean bind, boolean enableTransactionSupport) throws IllegalStateException {
        Assert.notNull(factory, "No RedisConnectionFactory specified");
        RedisConnectionHolder connHolder = (RedisConnectionHolder) TransactionSynchronizationManager.getResource(factory);
        if (connHolder != null) {
            if (enableTransactionSupport) {
                potentiallyRegisterTransactionSynchronisation(connHolder, factory);
            }
            return connHolder.getConnection();
        }
        if (!allowCreate) {
            throw new IllegalArgumentException("No connection found and allowCreate = false");
        }
        if (log.isDebugEnabled()) {
            log.debug("Opening RedisConnection");
        }
        RedisConnection conn = factory.getConnection();
        if (bind) {
            RedisConnection connectionToBind = conn;
            if (enableTransactionSupport && isActualNonReadonlyTransactionActive()) {
                connectionToBind = createConnectionProxy(conn, factory);
            }
            RedisConnectionHolder connHolder2 = new RedisConnectionHolder(connectionToBind);
            TransactionSynchronizationManager.bindResource(factory, connHolder2);
            if (enableTransactionSupport) {
                potentiallyRegisterTransactionSynchronisation(connHolder2, factory);
            }
            return connHolder2.getConnection();
        }
        return conn;
    }

    private static void potentiallyRegisterTransactionSynchronisation(RedisConnectionHolder connHolder, RedisConnectionFactory factory) throws IllegalStateException {
        if (isActualNonReadonlyTransactionActive() && !connHolder.isTransactionSyncronisationActive()) {
            connHolder.setTransactionSyncronisationActive(true);
            RedisConnection conn = connHolder.getConnection();
            conn.multi();
            TransactionSynchronizationManager.registerSynchronization(new RedisTransactionSynchronizer(connHolder, conn, factory));
        }
    }

    private static boolean isActualNonReadonlyTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive() && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    private static RedisConnection createConnectionProxy(RedisConnection connection, RedisConnectionFactory factory) {
        ProxyFactory proxyFactory = new ProxyFactory(connection);
        proxyFactory.addAdvice(new ConnectionSplittingInterceptor(factory));
        return (RedisConnection) RedisConnection.class.cast(proxyFactory.getProxy());
    }

    public static void releaseConnection(RedisConnection conn, RedisConnectionFactory factory) throws DataAccessException {
        if (conn == null) {
            return;
        }
        RedisConnectionHolder connHolder = (RedisConnectionHolder) TransactionSynchronizationManager.getResource(factory);
        if (connHolder != null && connHolder.isTransactionSyncronisationActive()) {
            if (log.isDebugEnabled()) {
                log.debug("Redis Connection will be closed when transaction finished.");
            }
        } else if (isConnectionTransactional(conn, factory) && TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
            unbindConnection(factory);
        } else if (!isConnectionTransactional(conn, factory)) {
            if (log.isDebugEnabled()) {
                log.debug("Closing Redis Connection");
            }
            conn.close();
        }
    }

    public static void unbindConnection(RedisConnectionFactory factory) throws DataAccessException {
        RedisConnectionHolder connHolder = (RedisConnectionHolder) TransactionSynchronizationManager.unbindResourceIfPossible(factory);
        if (connHolder != null) {
            if (connHolder.isTransactionSyncronisationActive()) {
                if (log.isDebugEnabled()) {
                    log.debug("Redis Connection will be closed when outer transaction finished.");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Closing bound connection.");
                }
                RedisConnection connection = connHolder.getConnection();
                connection.close();
            }
        }
    }

    public static boolean isConnectionTransactional(RedisConnection conn, RedisConnectionFactory connFactory) {
        RedisConnectionHolder connHolder;
        return (connFactory == null || (connHolder = (RedisConnectionHolder) TransactionSynchronizationManager.getResource(connFactory)) == null || conn != connHolder.getConnection()) ? false : true;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisConnectionUtils$RedisTransactionSynchronizer.class */
    private static class RedisTransactionSynchronizer extends TransactionSynchronizationAdapter {
        private final RedisConnectionHolder connHolder;
        private final RedisConnection connection;
        private final RedisConnectionFactory factory;

        private RedisTransactionSynchronizer(RedisConnectionHolder connHolder, RedisConnection connection, RedisConnectionFactory factory) {
            this.connHolder = connHolder;
            this.connection = connection;
            this.factory = factory;
        }

        @Override // org.springframework.transaction.support.TransactionSynchronizationAdapter, org.springframework.transaction.support.TransactionSynchronization
        public void afterCompletion(int status) throws IllegalStateException, DataAccessException {
            try {
                switch (status) {
                    case 0:
                        this.connection.exec();
                        break;
                    case 1:
                    case 2:
                    default:
                        this.connection.discard();
                        break;
                }
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug("Closing bound connection after transaction completed with " + status);
                }
                this.connHolder.setTransactionSyncronisationActive(false);
                this.connection.close();
                TransactionSynchronizationManager.unbindResource(this.factory);
            } catch (Throwable th) {
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug("Closing bound connection after transaction completed with " + status);
                }
                this.connHolder.setTransactionSyncronisationActive(false);
                this.connection.close();
                TransactionSynchronizationManager.unbindResource(this.factory);
                throw th;
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisConnectionUtils$ConnectionSplittingInterceptor.class */
    static class ConnectionSplittingInterceptor implements MethodInterceptor, org.springframework.cglib.proxy.MethodInterceptor {
        private final RedisConnectionFactory factory;

        public ConnectionSplittingInterceptor(RedisConnectionFactory factory) {
            this.factory = factory;
        }

        @Override // org.springframework.cglib.proxy.MethodInterceptor
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            RedisCommand commandToExecute = RedisCommand.failsafeCommandLookup(method.getName());
            if (isPotentiallyThreadBoundCommand(commandToExecute)) {
                if (RedisConnectionUtils.log.isDebugEnabled()) {
                    RedisConnectionUtils.log.debug(String.format("Invoke '%s' on bound conneciton", method.getName()));
                }
                return invoke(method, obj, args);
            }
            if (RedisConnectionUtils.log.isDebugEnabled()) {
                RedisConnectionUtils.log.debug(String.format("Invoke '%s' on unbound conneciton", method.getName()));
            }
            RedisConnection connection = this.factory.getConnection();
            try {
                Object objInvoke = invoke(method, connection, args);
                if (!connection.isClosed()) {
                    connection.close();
                }
                return objInvoke;
            } catch (Throwable th) {
                if (!connection.isClosed()) {
                    connection.close();
                }
                throw th;
            }
        }

        private Object invoke(Method method, Object target, Object[] args) throws Throwable {
            try {
                return method.invoke(target, args);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }

        @Override // org.aopalliance.intercept.MethodInterceptor
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return intercept(invocation.getThis(), invocation.getMethod(), invocation.getArguments(), null);
        }

        private boolean isPotentiallyThreadBoundCommand(RedisCommand command) {
            return RedisCommand.UNKNOWN.equals(command) || !command.isReadonly();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisConnectionUtils$RedisConnectionHolder.class */
    private static class RedisConnectionHolder implements ResourceHolder {
        private boolean unbound;
        private final RedisConnection conn;
        private boolean transactionSyncronisationActive;

        public RedisConnectionHolder(RedisConnection conn) {
            this.conn = conn;
        }

        @Override // org.springframework.transaction.support.ResourceHolder
        public boolean isVoid() {
            return this.unbound;
        }

        public RedisConnection getConnection() {
            return this.conn;
        }

        @Override // org.springframework.transaction.support.ResourceHolder
        public void reset() {
        }

        @Override // org.springframework.transaction.support.ResourceHolder
        public void unbound() {
            this.unbound = true;
        }

        public boolean isTransactionSyncronisationActive() {
            return this.transactionSyncronisationActive;
        }

        public void setTransactionSyncronisationActive(boolean transactionSyncronisationActive) {
            this.transactionSyncronisationActive = transactionSyncronisationActive;
        }
    }
}
