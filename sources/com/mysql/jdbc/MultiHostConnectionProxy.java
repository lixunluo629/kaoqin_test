package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MultiHostConnectionProxy.class */
public abstract class MultiHostConnectionProxy implements InvocationHandler {
    private static final String METHOD_GET_MULTI_HOST_SAFE_PROXY = "getMultiHostSafeProxy";
    private static final String METHOD_EQUALS = "equals";
    private static final String METHOD_HASH_CODE = "hashCode";
    private static final String METHOD_CLOSE = "close";
    private static final String METHOD_ABORT_INTERNAL = "abortInternal";
    private static final String METHOD_ABORT = "abort";
    private static final String METHOD_IS_CLOSED = "isClosed";
    private static final String METHOD_GET_AUTO_COMMIT = "getAutoCommit";
    private static final String METHOD_GET_CATALOG = "getCatalog";
    private static final String METHOD_GET_TRANSACTION_ISOLATION = "getTransactionIsolation";
    private static final String METHOD_GET_SESSION_MAX_ROWS = "getSessionMaxRows";
    List<String> hostList;
    Properties localProps;
    boolean autoReconnect;
    MySQLConnection thisAsConnection;
    MySQLConnection proxyConnection;
    MySQLConnection currentConnection;
    boolean isClosed;
    boolean closedExplicitly;
    String closedReason;
    protected Throwable lastExceptionDealtWith;
    private static Constructor<?> JDBC_4_MS_CONNECTION_CTOR;

    abstract boolean shouldExceptionTriggerConnectionSwitch(Throwable th);

    abstract boolean isMasterConnection();

    abstract void pickNewConnection() throws SQLException;

    abstract void doClose() throws SQLException;

    abstract void doAbortInternal() throws SQLException;

    abstract void doAbort(Executor executor) throws SQLException;

    abstract Object invokeMore(Object obj, Method method, Object[] objArr) throws Throwable;

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_MS_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4MultiHostMySQLConnection").getConstructor(MultiHostConnectionProxy.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MultiHostConnectionProxy$JdbcInterfaceProxy.class */
    class JdbcInterfaceProxy implements InvocationHandler {
        Object invokeOn;

        JdbcInterfaceProxy(Object toInvokeOn) {
            this.invokeOn = null;
            this.invokeOn = toInvokeOn;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object obj;
            if (MultiHostConnectionProxy.METHOD_EQUALS.equals(method.getName())) {
                return Boolean.valueOf(args[0].equals(this));
            }
            synchronized (MultiHostConnectionProxy.this) {
                Object result = null;
                try {
                    Object result2 = method.invoke(this.invokeOn, args);
                    result = MultiHostConnectionProxy.this.proxyIfReturnTypeIsJdbcInterface(method.getReturnType(), result2);
                } catch (InvocationTargetException e) {
                    MultiHostConnectionProxy.this.dealWithInvocationException(e);
                }
                obj = result;
            }
            return obj;
        }
    }

    MultiHostConnectionProxy() throws SQLException {
        this.autoReconnect = false;
        this.thisAsConnection = null;
        this.proxyConnection = null;
        this.currentConnection = null;
        this.isClosed = false;
        this.closedExplicitly = false;
        this.closedReason = null;
        this.lastExceptionDealtWith = null;
        this.thisAsConnection = getNewWrapperForThisAsConnection();
    }

    MultiHostConnectionProxy(List<String> hosts, Properties props) throws SQLException {
        this();
        initializeHostsSpecs(hosts, props);
    }

    int initializeHostsSpecs(List<String> hosts, Properties props) {
        this.autoReconnect = "true".equalsIgnoreCase(props.getProperty("autoReconnect")) || "true".equalsIgnoreCase(props.getProperty("autoReconnectForPools"));
        this.hostList = hosts;
        int numHosts = this.hostList.size();
        this.localProps = (Properties) props.clone();
        this.localProps.remove(NonRegisteringDriver.HOST_PROPERTY_KEY);
        this.localProps.remove(NonRegisteringDriver.PORT_PROPERTY_KEY);
        for (int i = 0; i < numHosts; i++) {
            this.localProps.remove("HOST." + (i + 1));
            this.localProps.remove("PORT." + (i + 1));
        }
        this.localProps.remove(NonRegisteringDriver.NUM_HOSTS_PROPERTY_KEY);
        return numHosts;
    }

    MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
        return (Util.isJdbc4() || JDBC_4_MS_CONNECTION_CTOR != null) ? (MySQLConnection) Util.handleNewInstance(JDBC_4_MS_CONNECTION_CTOR, new Object[]{this}, null) : new MultiHostMySQLConnection(this);
    }

    protected MySQLConnection getProxy() {
        return this.proxyConnection != null ? this.proxyConnection : this.thisAsConnection;
    }

    protected final void setProxy(MySQLConnection proxyConn) {
        this.proxyConnection = proxyConn;
        propagateProxyDown(proxyConn);
    }

    protected void propagateProxyDown(MySQLConnection proxyConn) {
        this.currentConnection.setProxy(proxyConn);
    }

    Object proxyIfReturnTypeIsJdbcInterface(Class<?> returnType, Object toProxy) {
        if (toProxy != null && Util.isJdbcInterface(returnType)) {
            Class<?> toProxyClass = toProxy.getClass();
            return Proxy.newProxyInstance(toProxyClass.getClassLoader(), Util.getImplementedInterfaces(toProxyClass), getNewJdbcInterfaceProxy(toProxy));
        }
        return toProxy;
    }

    InvocationHandler getNewJdbcInterfaceProxy(Object toProxy) {
        return new JdbcInterfaceProxy(toProxy);
    }

    void dealWithInvocationException(InvocationTargetException e) throws Throwable {
        Throwable t = e.getTargetException();
        if (t != null) {
            if (this.lastExceptionDealtWith != t && shouldExceptionTriggerConnectionSwitch(t)) {
                invalidateCurrentConnection();
                pickNewConnection();
                this.lastExceptionDealtWith = t;
            }
            throw t;
        }
        throw e;
    }

    synchronized void invalidateCurrentConnection() throws SQLException {
        invalidateConnection(this.currentConnection);
    }

    synchronized void invalidateConnection(MySQLConnection conn) throws SQLException {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.realClose(true, !conn.getAutoCommit(), true, null);
                }
            } catch (SQLException e) {
            }
        }
    }

    synchronized ConnectionImpl createConnectionForHost(String hostPortSpec) throws SQLException {
        Properties connProps = (Properties) this.localProps.clone();
        String[] hostPortPair = NonRegisteringDriver.parseHostPortPair(hostPortSpec);
        String hostName = hostPortPair[0];
        String portNumber = hostPortPair[1];
        String dbName = connProps.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
        if (hostName == null) {
            throw new SQLException("Could not find a hostname to start a connection to");
        }
        if (portNumber == null) {
            portNumber = "3306";
        }
        connProps.setProperty(NonRegisteringDriver.HOST_PROPERTY_KEY, hostName);
        connProps.setProperty(NonRegisteringDriver.PORT_PROPERTY_KEY, portNumber);
        connProps.setProperty("HOST.1", hostName);
        connProps.setProperty("PORT.1", portNumber);
        connProps.setProperty(NonRegisteringDriver.NUM_HOSTS_PROPERTY_KEY, "1");
        connProps.setProperty("roundRobinLoadBalance", "false");
        ConnectionImpl conn = (ConnectionImpl) ConnectionImpl.getInstance(hostName, Integer.parseInt(portNumber), connProps, dbName, "jdbc:mysql://" + hostName + ":" + portNumber + "/");
        conn.setProxy(getProxy());
        return conn;
    }

    void syncSessionState(Connection source, Connection target) throws SQLException {
        if (source == null || target == null) {
            return;
        }
        boolean prevUseLocalSessionState = source.getUseLocalSessionState();
        source.setUseLocalSessionState(true);
        boolean readOnly = source.isReadOnly();
        source.setUseLocalSessionState(prevUseLocalSessionState);
        syncSessionState(source, target, readOnly);
    }

    void syncSessionState(Connection source, Connection target, boolean readOnly) throws SQLException {
        if (target != null) {
            target.setReadOnly(readOnly);
        }
        if (source == null || target == null) {
            return;
        }
        boolean prevUseLocalSessionState = source.getUseLocalSessionState();
        source.setUseLocalSessionState(true);
        target.setAutoCommit(source.getAutoCommit());
        target.setCatalog(source.getCatalog());
        target.setTransactionIsolation(source.getTransactionIsolation());
        target.setSessionMaxRows(source.getSessionMaxRows());
        source.setUseLocalSessionState(prevUseLocalSessionState);
    }

    @Override // java.lang.reflect.InvocationHandler
    public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        String methodName = method.getName();
        if (METHOD_GET_MULTI_HOST_SAFE_PROXY.equals(methodName)) {
            return this.thisAsConnection;
        }
        if (METHOD_EQUALS.equals(methodName)) {
            return Boolean.valueOf(args[0].equals(this));
        }
        if ("hashCode".equals(methodName)) {
            return Integer.valueOf(hashCode());
        }
        if ("close".equals(methodName)) {
            doClose();
            this.isClosed = true;
            this.closedReason = "Connection explicitly closed.";
            this.closedExplicitly = true;
            return null;
        }
        if (METHOD_ABORT_INTERNAL.equals(methodName)) {
            doAbortInternal();
            this.currentConnection.abortInternal();
            this.isClosed = true;
            this.closedReason = "Connection explicitly closed.";
            return null;
        }
        if (METHOD_ABORT.equals(methodName) && args.length == 1) {
            doAbort((Executor) args[0]);
            this.isClosed = true;
            this.closedReason = "Connection explicitly closed.";
            return null;
        }
        if (METHOD_IS_CLOSED.equals(methodName)) {
            return Boolean.valueOf(this.isClosed);
        }
        try {
            return invokeMore(proxy, method, args);
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        } catch (Exception e2) {
            Class<?>[] declaredException = method.getExceptionTypes();
            for (Class<?> declEx : declaredException) {
                if (declEx.isAssignableFrom(e2.getClass())) {
                    throw e2;
                }
            }
            throw new IllegalStateException(e2.getMessage(), e2);
        }
    }

    protected boolean allowedOnClosedConnection(Method method) {
        String methodName = method.getName();
        return methodName.equals(METHOD_GET_AUTO_COMMIT) || methodName.equals(METHOD_GET_CATALOG) || methodName.equals(METHOD_GET_TRANSACTION_ISOLATION) || methodName.equals(METHOD_GET_SESSION_MAX_ROWS);
    }
}
