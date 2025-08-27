package com.mysql.jdbc;

import com.mysql.jdbc.MultiHostConnectionProxy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/FailoverConnectionProxy.class */
public class FailoverConnectionProxy extends MultiHostConnectionProxy {
    private static final String METHOD_SET_READ_ONLY = "setReadOnly";
    private static final String METHOD_SET_AUTO_COMMIT = "setAutoCommit";
    private static final String METHOD_COMMIT = "commit";
    private static final String METHOD_ROLLBACK = "rollback";
    private static final int NO_CONNECTION_INDEX = -1;
    private static final int DEFAULT_PRIMARY_HOST_INDEX = 0;
    private int secondsBeforeRetryPrimaryHost;
    private long queriesBeforeRetryPrimaryHost;
    private boolean failoverReadOnly;
    private int retriesAllDown;
    private int currentHostIndex;
    private int primaryHostIndex;
    private Boolean explicitlyReadOnly;
    private boolean explicitlyAutoCommit;
    private boolean enableFallBackToPrimaryHost;
    private long primaryHostFailTimeMillis;
    private long queriesIssuedSinceFailover;
    private static Class<?>[] INTERFACES_TO_PROXY;

    static {
        if (Util.isJdbc4()) {
            try {
                INTERFACES_TO_PROXY = new Class[]{Class.forName("com.mysql.jdbc.JDBC4MySQLConnection")};
                return;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        INTERFACES_TO_PROXY = new Class[]{MySQLConnection.class};
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/FailoverConnectionProxy$FailoverJdbcInterfaceProxy.class */
    class FailoverJdbcInterfaceProxy extends MultiHostConnectionProxy.JdbcInterfaceProxy {
        FailoverJdbcInterfaceProxy(Object toInvokeOn) {
            super(toInvokeOn);
        }

        @Override // com.mysql.jdbc.MultiHostConnectionProxy.JdbcInterfaceProxy, java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            boolean isExecute = methodName.startsWith("execute");
            if (FailoverConnectionProxy.this.connectedToSecondaryHost() && isExecute) {
                FailoverConnectionProxy.this.incrementQueriesIssuedSinceFailover();
            }
            Object result = super.invoke(proxy, method, args);
            if (FailoverConnectionProxy.this.explicitlyAutoCommit && isExecute && FailoverConnectionProxy.this.readyToFallBackToPrimaryHost()) {
                FailoverConnectionProxy.this.fallBackToPrimaryIfAvailable();
            }
            return result;
        }
    }

    public static Connection createProxyInstance(List<String> hosts, Properties props) throws SQLException {
        FailoverConnectionProxy connProxy = new FailoverConnectionProxy(hosts, props);
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), INTERFACES_TO_PROXY, connProxy);
    }

    private FailoverConnectionProxy(List<String> hosts, Properties props) throws SQLException, InterruptedException, ClassNotFoundException {
        super(hosts, props);
        this.currentHostIndex = -1;
        this.primaryHostIndex = 0;
        this.explicitlyReadOnly = null;
        this.explicitlyAutoCommit = true;
        this.enableFallBackToPrimaryHost = true;
        this.primaryHostFailTimeMillis = 0L;
        this.queriesIssuedSinceFailover = 0L;
        ConnectionPropertiesImpl connProps = new ConnectionPropertiesImpl();
        connProps.initializeProperties(props);
        this.secondsBeforeRetryPrimaryHost = connProps.getSecondsBeforeRetryMaster();
        this.queriesBeforeRetryPrimaryHost = connProps.getQueriesBeforeRetryMaster();
        this.failoverReadOnly = connProps.getFailOverReadOnly();
        this.retriesAllDown = connProps.getRetriesAllDown();
        this.enableFallBackToPrimaryHost = this.secondsBeforeRetryPrimaryHost > 0 || this.queriesBeforeRetryPrimaryHost > 0;
        pickNewConnection();
        this.explicitlyAutoCommit = this.currentConnection.getAutoCommit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    public MultiHostConnectionProxy.JdbcInterfaceProxy getNewJdbcInterfaceProxy(Object toProxy) {
        return new FailoverJdbcInterfaceProxy(toProxy);
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    boolean shouldExceptionTriggerConnectionSwitch(Throwable t) {
        if (!(t instanceof SQLException)) {
            return false;
        }
        String sqlState = ((SQLException) t).getSQLState();
        if ((sqlState != null && sqlState.startsWith("08")) || (t instanceof CommunicationsException)) {
            return true;
        }
        return false;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    boolean isMasterConnection() {
        return connectedToPrimaryHost();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void pickNewConnection() throws SQLException, InterruptedException {
        if (this.isClosed && this.closedExplicitly) {
            return;
        }
        if (!isConnected() || readyToFallBackToPrimaryHost()) {
            try {
                connectTo(this.primaryHostIndex);
                return;
            } catch (SQLException e) {
                resetAutoFallBackCounters();
                failOver(this.primaryHostIndex);
                return;
            }
        }
        failOver();
    }

    synchronized ConnectionImpl createConnectionForHostIndex(int hostIndex) throws SQLException {
        return createConnectionForHost(this.hostList.get(hostIndex));
    }

    private synchronized void connectTo(int hostIndex) throws SQLException {
        try {
            switchCurrentConnectionTo(hostIndex, createConnectionForHostIndex(hostIndex));
        } catch (SQLException e) {
            if (this.currentConnection != null) {
                StringBuilder msg = new StringBuilder("Connection to ").append(isPrimaryHostIndex(hostIndex) ? BeanDefinitionParserDelegate.PRIMARY_ATTRIBUTE : "secondary").append(" host '").append(this.hostList.get(hostIndex)).append("' failed");
                this.currentConnection.getLog().logWarn(msg.toString(), e);
            }
            throw e;
        }
    }

    private synchronized void switchCurrentConnectionTo(int hostIndex, MySQLConnection connection) throws SQLException {
        boolean readOnly;
        invalidateCurrentConnection();
        if (isPrimaryHostIndex(hostIndex)) {
            readOnly = this.explicitlyReadOnly == null ? false : this.explicitlyReadOnly.booleanValue();
        } else if (this.failoverReadOnly) {
            readOnly = true;
        } else if (this.explicitlyReadOnly != null) {
            readOnly = this.explicitlyReadOnly.booleanValue();
        } else if (this.currentConnection != null) {
            readOnly = this.currentConnection.isReadOnly();
        } else {
            readOnly = false;
        }
        syncSessionState(this.currentConnection, connection, readOnly);
        this.currentConnection = connection;
        this.currentHostIndex = hostIndex;
    }

    private synchronized void failOver() throws SQLException, InterruptedException {
        failOver(this.currentHostIndex);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized void failOver(int r6) throws java.sql.SQLException, java.lang.InterruptedException {
        /*
            r5 = this;
            r0 = r5
            int r0 = r0.currentHostIndex
            r7 = r0
            r0 = r5
            r1 = r6
            r2 = 0
            int r0 = r0.nextHost(r1, r2)
            r8 = r0
            r0 = r8
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = 0
            r11 = r0
            r0 = 0
            r12 = r0
            r0 = r7
            r1 = -1
            if (r0 == r1) goto L25
            r0 = r5
            r1 = r7
            boolean r0 = r0.isPrimaryHostIndex(r1)
            if (r0 == 0) goto L29
        L25:
            r0 = 1
            goto L2a
        L29:
            r0 = 0
        L2a:
            r13 = r0
        L2c:
            r0 = r13
            if (r0 != 0) goto L39
            r0 = r5
            r1 = r8
            boolean r0 = r0.isPrimaryHostIndex(r1)     // Catch: java.sql.SQLException -> L5b
            if (r0 == 0) goto L3d
        L39:
            r0 = 1
            goto L3e
        L3d:
            r0 = 0
        L3e:
            r13 = r0
            r0 = r5
            r1 = r8
            r0.connectTo(r1)     // Catch: java.sql.SQLException -> L5b
            r0 = r13
            if (r0 == 0) goto L55
            r0 = r5
            boolean r0 = r0.connectedToSecondaryHost()     // Catch: java.sql.SQLException -> L5b
            if (r0 == 0) goto L55
            r0 = r5
            r0.resetAutoFallBackCounters()     // Catch: java.sql.SQLException -> L5b
        L55:
            r0 = 1
            r12 = r0
            goto La7
        L5b:
            r14 = move-exception
            r0 = r14
            r10 = r0
            r0 = r5
            r1 = r14
            boolean r0 = r0.shouldExceptionTriggerConnectionSwitch(r1)
            if (r0 == 0) goto La4
            r0 = r5
            r1 = r8
            r2 = r11
            if (r2 <= 0) goto L75
            r2 = 1
            goto L76
        L75:
            r2 = 0
        L76:
            int r0 = r0.nextHost(r1, r2)
            r15 = r0
            r0 = r15
            r1 = r9
            if (r0 != r1) goto L9e
            r0 = r15
            r1 = r5
            r2 = r8
            r3 = 1
            int r1 = r1.nextHost(r2, r3)
            r2 = r1
            r15 = r2
            if (r0 != r1) goto L9e
            int r11 = r11 + 1
            r0 = 250(0xfa, double:1.235E-321)
            java.lang.Thread.sleep(r0)     // Catch: java.lang.InterruptedException -> L9c
            goto L9e
        L9c:
            r16 = move-exception
        L9e:
            r0 = r15
            r8 = r0
            goto La7
        La4:
            r0 = r14
            throw r0
        La7:
            r0 = r11
            r1 = r5
            int r1 = r1.retriesAllDown
            if (r0 >= r1) goto Lb5
            r0 = r12
            if (r0 == 0) goto L2c
        Lb5:
            r0 = r12
            if (r0 != 0) goto Lbd
            r0 = r10
            throw r0
        Lbd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.FailoverConnectionProxy.failOver(int):void");
    }

    synchronized void fallBackToPrimaryIfAvailable() {
        MySQLConnection connection = null;
        try {
            connection = createConnectionForHostIndex(this.primaryHostIndex);
            switchCurrentConnectionTo(this.primaryHostIndex, connection);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e2) {
                }
            }
            resetAutoFallBackCounters();
        }
    }

    private int nextHost(int currHostIdx, boolean vouchForPrimaryHost) {
        int nextHostIdx = (currHostIdx + 1) % this.hostList.size();
        if (isPrimaryHostIndex(nextHostIdx) && isConnected() && !vouchForPrimaryHost && this.enableFallBackToPrimaryHost && !readyToFallBackToPrimaryHost()) {
            nextHostIdx = nextHost(nextHostIdx, vouchForPrimaryHost);
        }
        return nextHostIdx;
    }

    synchronized void incrementQueriesIssuedSinceFailover() {
        this.queriesIssuedSinceFailover++;
    }

    synchronized boolean readyToFallBackToPrimaryHost() {
        return this.enableFallBackToPrimaryHost && connectedToSecondaryHost() && (secondsBeforeRetryPrimaryHostIsMet() || queriesBeforeRetryPrimaryHostIsMet());
    }

    synchronized boolean isConnected() {
        return this.currentHostIndex != -1;
    }

    synchronized boolean isPrimaryHostIndex(int hostIndex) {
        return hostIndex == this.primaryHostIndex;
    }

    synchronized boolean connectedToPrimaryHost() {
        return isPrimaryHostIndex(this.currentHostIndex);
    }

    synchronized boolean connectedToSecondaryHost() {
        return this.currentHostIndex >= 0 && !isPrimaryHostIndex(this.currentHostIndex);
    }

    private synchronized boolean secondsBeforeRetryPrimaryHostIsMet() {
        return this.secondsBeforeRetryPrimaryHost > 0 && Util.secondsSinceMillis(this.primaryHostFailTimeMillis) >= ((long) this.secondsBeforeRetryPrimaryHost);
    }

    private synchronized boolean queriesBeforeRetryPrimaryHostIsMet() {
        return this.queriesBeforeRetryPrimaryHost > 0 && this.queriesIssuedSinceFailover >= this.queriesBeforeRetryPrimaryHost;
    }

    private synchronized void resetAutoFallBackCounters() {
        this.primaryHostFailTimeMillis = System.currentTimeMillis();
        this.queriesIssuedSinceFailover = 0L;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doClose() throws SQLException {
        this.currentConnection.close();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doAbortInternal() throws SQLException {
        this.currentConnection.abortInternal();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doAbort(Executor executor) throws SQLException {
        this.currentConnection.abort(executor);
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    public synchronized Object invokeMore(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (METHOD_SET_READ_ONLY.equals(methodName)) {
            this.explicitlyReadOnly = (Boolean) args[0];
            if (this.failoverReadOnly && connectedToSecondaryHost()) {
                return null;
            }
        }
        if (this.isClosed && !allowedOnClosedConnection(method)) {
            if (this.autoReconnect && !this.closedExplicitly) {
                this.currentHostIndex = -1;
                pickNewConnection();
                this.isClosed = false;
                this.closedReason = null;
            } else {
                String reason = "No operations allowed after connection closed.";
                if (this.closedReason != null) {
                    reason = reason + "  " + this.closedReason;
                }
                throw SQLError.createSQLException(reason, SQLError.SQL_STATE_CONNECTION_NOT_OPEN, (ExceptionInterceptor) null);
            }
        }
        Object result = null;
        try {
            Object result2 = method.invoke(this.thisAsConnection, args);
            result = proxyIfReturnTypeIsJdbcInterface(method.getReturnType(), result2);
        } catch (InvocationTargetException e) {
            dealWithInvocationException(e);
        }
        if (METHOD_SET_AUTO_COMMIT.equals(methodName)) {
            this.explicitlyAutoCommit = ((Boolean) args[0]).booleanValue();
        }
        if ((this.explicitlyAutoCommit || METHOD_COMMIT.equals(methodName) || METHOD_ROLLBACK.equals(methodName)) && readyToFallBackToPrimaryHost()) {
            fallBackToPrimaryIfAvailable();
        }
        return result;
    }
}
