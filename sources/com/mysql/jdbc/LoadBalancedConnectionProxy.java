package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executor;
import org.springframework.boot.context.config.RandomValuePropertySource;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalancedConnectionProxy.class */
public class LoadBalancedConnectionProxy extends MultiHostConnectionProxy implements PingTarget {
    private ConnectionGroup connectionGroup;
    private long connectionGroupProxyID;
    protected Map<String, ConnectionImpl> liveConnections;
    private Map<String, Integer> hostsToListIndexMap;
    private Map<ConnectionImpl, String> connectionsToHostsMap;
    private long[] responseTimes;
    private int retriesAllDown;
    private BalanceStrategy balancer;
    private int autoCommitSwapThreshold;
    public static final String BLACKLIST_TIMEOUT_PROPERTY_KEY = "loadBalanceBlacklistTimeout";
    private int globalBlacklistTimeout;
    private static Map<String, Long> globalBlacklist = new HashMap();
    public static final String HOST_REMOVAL_GRACE_PERIOD_PROPERTY_KEY = "loadBalanceHostRemovalGracePeriod";
    private int hostRemovalGracePeriod;
    private LoadBalanceExceptionChecker exceptionChecker;
    private static Constructor<?> JDBC_4_LB_CONNECTION_CTOR;
    private static Class<?>[] INTERFACES_TO_PROXY;
    private static LoadBalancedConnection nullLBConnectionInstance;
    private long totalPhysicalConnections = 0;
    private Set<String> hostsToRemove = new HashSet();
    private boolean inTransaction = false;
    private long transactionStartTime = 0;
    private long transactionCount = 0;

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_LB_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4LoadBalancedMySQLConnection").getConstructor(LoadBalancedConnectionProxy.class);
                INTERFACES_TO_PROXY = new Class[]{LoadBalancedConnection.class, Class.forName("com.mysql.jdbc.JDBC4MySQLConnection")};
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        } else {
            INTERFACES_TO_PROXY = new Class[]{LoadBalancedConnection.class};
        }
        nullLBConnectionInstance = null;
    }

    public static LoadBalancedConnection createProxyInstance(List<String> hosts, Properties props) throws SQLException {
        LoadBalancedConnectionProxy connProxy = new LoadBalancedConnectionProxy(hosts, props);
        return (LoadBalancedConnection) Proxy.newProxyInstance(LoadBalancedConnection.class.getClassLoader(), INTERFACES_TO_PROXY, connProxy);
    }

    private LoadBalancedConnectionProxy(List<String> hosts, Properties props) throws SQLException {
        this.connectionGroup = null;
        this.connectionGroupProxyID = 0L;
        this.autoCommitSwapThreshold = 0;
        this.globalBlacklistTimeout = 0;
        this.hostRemovalGracePeriod = 0;
        String group = props.getProperty("loadBalanceConnectionGroup", null);
        String enableJMXAsString = props.getProperty("loadBalanceEnableJMX", "false");
        try {
            boolean enableJMX = Boolean.parseBoolean(enableJMXAsString);
            if (group != null) {
                this.connectionGroup = ConnectionGroupManager.getConnectionGroupInstance(group);
                if (enableJMX) {
                    ConnectionGroupManager.registerJmx();
                }
                this.connectionGroupProxyID = this.connectionGroup.registerConnectionProxy(this, hosts);
                hosts = new ArrayList(this.connectionGroup.getInitialHosts());
            }
            int numHosts = initializeHostsSpecs(hosts, props);
            this.liveConnections = new HashMap(numHosts);
            this.hostsToListIndexMap = new HashMap(numHosts);
            for (int i = 0; i < numHosts; i++) {
                this.hostsToListIndexMap.put(this.hostList.get(i), Integer.valueOf(i));
            }
            this.connectionsToHostsMap = new HashMap(numHosts);
            this.responseTimes = new long[numHosts];
            String retriesAllDownAsString = this.localProps.getProperty("retriesAllDown", "120");
            try {
                this.retriesAllDown = Integer.parseInt(retriesAllDownAsString);
                String blacklistTimeoutAsString = this.localProps.getProperty(BLACKLIST_TIMEOUT_PROPERTY_KEY, "0");
                try {
                    this.globalBlacklistTimeout = Integer.parseInt(blacklistTimeoutAsString);
                    String hostRemovalGracePeriodAsString = this.localProps.getProperty(HOST_REMOVAL_GRACE_PERIOD_PROPERTY_KEY, "15000");
                    try {
                        this.hostRemovalGracePeriod = Integer.parseInt(hostRemovalGracePeriodAsString);
                        String strategy = this.localProps.getProperty("loadBalanceStrategy", RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME);
                        if (RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME.equals(strategy)) {
                            this.balancer = (BalanceStrategy) Util.loadExtensions(null, props, RandomBalanceStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
                        } else if ("bestResponseTime".equals(strategy)) {
                            this.balancer = (BalanceStrategy) Util.loadExtensions(null, props, BestResponseTimeBalanceStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
                        } else if ("serverAffinity".equals(strategy)) {
                            this.balancer = (BalanceStrategy) Util.loadExtensions(null, props, ServerAffinityStrategy.class.getName(), "InvalidLoadBalanceStrategy", null).get(0);
                        } else {
                            this.balancer = (BalanceStrategy) Util.loadExtensions(null, props, strategy, "InvalidLoadBalanceStrategy", null).get(0);
                        }
                        String autoCommitSwapThresholdAsString = props.getProperty("loadBalanceAutoCommitStatementThreshold", "0");
                        try {
                            this.autoCommitSwapThreshold = Integer.parseInt(autoCommitSwapThresholdAsString);
                            String autoCommitSwapRegex = props.getProperty("loadBalanceAutoCommitStatementRegex", "");
                            if (!"".equals(autoCommitSwapRegex)) {
                                try {
                                    "".matches(autoCommitSwapRegex);
                                } catch (Exception e) {
                                    throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceAutoCommitStatementRegex", new Object[]{autoCommitSwapRegex}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                                }
                            }
                            if (this.autoCommitSwapThreshold > 0) {
                                String statementInterceptors = this.localProps.getProperty("statementInterceptors");
                                if (statementInterceptors == null) {
                                    this.localProps.setProperty("statementInterceptors", "com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
                                } else if (statementInterceptors.length() > 0) {
                                    this.localProps.setProperty("statementInterceptors", statementInterceptors + ",com.mysql.jdbc.LoadBalancedAutoCommitInterceptor");
                                }
                                props.setProperty("statementInterceptors", this.localProps.getProperty("statementInterceptors"));
                            }
                            this.balancer.init(null, props);
                            String lbExceptionChecker = this.localProps.getProperty("loadBalanceExceptionChecker", "com.mysql.jdbc.StandardLoadBalanceExceptionChecker");
                            this.exceptionChecker = (LoadBalanceExceptionChecker) Util.loadExtensions(null, props, lbExceptionChecker, "InvalidLoadBalanceExceptionChecker", null).get(0);
                            pickNewConnection();
                        } catch (NumberFormatException e2) {
                            throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceAutoCommitStatementThreshold", new Object[]{autoCommitSwapThresholdAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                        }
                    } catch (NumberFormatException e3) {
                        throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceHostRemovalGracePeriod", new Object[]{hostRemovalGracePeriodAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                    }
                } catch (NumberFormatException e4) {
                    throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceBlacklistTimeout", new Object[]{blacklistTimeoutAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                }
            } catch (NumberFormatException e5) {
                throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForRetriesAllDown", new Object[]{retriesAllDownAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
            }
        } catch (Exception e6) {
            throw SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.badValueForLoadBalanceEnableJMX", new Object[]{enableJMXAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
        return (Util.isJdbc4() || JDBC_4_LB_CONNECTION_CTOR != null) ? (MySQLConnection) Util.handleNewInstance(JDBC_4_LB_CONNECTION_CTOR, new Object[]{this}, null) : new LoadBalancedMySQLConnection(this);
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    protected void propagateProxyDown(MySQLConnection proxyConn) {
        for (ConnectionImpl c : this.liveConnections.values()) {
            c.setProxy(proxyConn);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    boolean shouldExceptionTriggerConnectionSwitch(Throwable t) {
        return (t instanceof SQLException) && this.exceptionChecker.shouldExceptionTriggerFailover((SQLException) t);
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    boolean isMasterConnection() {
        return true;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void invalidateConnection(MySQLConnection conn) throws SQLException {
        super.invalidateConnection(conn);
        if (isGlobalBlacklistEnabled()) {
            addToGlobalBlacklist(this.connectionsToHostsMap.get(conn));
        }
        this.liveConnections.remove(this.connectionsToHostsMap.get(conn));
        Object mappedHost = this.connectionsToHostsMap.remove(conn);
        if (mappedHost != null && this.hostsToListIndexMap.containsKey(mappedHost)) {
            int hostIndex = this.hostsToListIndexMap.get(mappedHost).intValue();
            synchronized (this.responseTimes) {
                this.responseTimes[hostIndex] = 0;
            }
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void pickNewConnection() throws SQLException {
        if (this.isClosed && this.closedExplicitly) {
            return;
        }
        if (this.currentConnection == null) {
            this.currentConnection = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[]) this.responseTimes.clone(), this.retriesAllDown);
            return;
        }
        if (this.currentConnection.isClosed()) {
            invalidateCurrentConnection();
        }
        int pingTimeout = this.currentConnection.getLoadBalancePingTimeout();
        boolean pingBeforeReturn = this.currentConnection.getLoadBalanceValidateConnectionOnSwapServer();
        int hostsToTry = this.hostList.size();
        for (int hostsTried = 0; hostsTried < hostsToTry; hostsTried++) {
            ConnectionImpl newConn = null;
            try {
                newConn = this.balancer.pickConnection(this, Collections.unmodifiableList(this.hostList), Collections.unmodifiableMap(this.liveConnections), (long[]) this.responseTimes.clone(), this.retriesAllDown);
                if (this.currentConnection != null) {
                    if (pingBeforeReturn) {
                        if (pingTimeout == 0) {
                            newConn.ping();
                        } else {
                            newConn.pingInternal(true, pingTimeout);
                        }
                    }
                    syncSessionState(this.currentConnection, newConn);
                }
                this.currentConnection = newConn;
                return;
            } catch (SQLException e) {
                if (shouldExceptionTriggerConnectionSwitch(e) && newConn != null) {
                    invalidateConnection(newConn);
                }
            }
        }
        this.isClosed = true;
        this.closedReason = "Connection closed after inability to pick valid new connection during load-balance.";
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    public synchronized ConnectionImpl createConnectionForHost(String hostPortSpec) throws SQLException {
        ConnectionImpl conn = super.createConnectionForHost(hostPortSpec);
        this.liveConnections.put(hostPortSpec, conn);
        this.connectionsToHostsMap.put(conn, hostPortSpec);
        this.totalPhysicalConnections++;
        Iterator i$ = conn.getStatementInterceptorsInstances().iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            StatementInterceptorV2 stmtInterceptor = i$.next();
            if (stmtInterceptor instanceof LoadBalancedAutoCommitInterceptor) {
                ((LoadBalancedAutoCommitInterceptor) stmtInterceptor).resumeCounters();
                break;
            }
        }
        return conn;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void syncSessionState(Connection source, Connection target, boolean readOnly) throws SQLException {
        LoadBalancedAutoCommitInterceptor lbAutoCommitStmtInterceptor = null;
        Iterator i$ = ((MySQLConnection) target).getStatementInterceptorsInstances().iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            StatementInterceptorV2 stmtInterceptor = i$.next();
            if (stmtInterceptor instanceof LoadBalancedAutoCommitInterceptor) {
                lbAutoCommitStmtInterceptor = (LoadBalancedAutoCommitInterceptor) stmtInterceptor;
                lbAutoCommitStmtInterceptor.pauseCounters();
                break;
            }
        }
        super.syncSessionState(source, target, readOnly);
        if (lbAutoCommitStmtInterceptor != null) {
            lbAutoCommitStmtInterceptor.resumeCounters();
        }
    }

    private synchronized void closeAllConnections() {
        for (ConnectionImpl c : this.liveConnections.values()) {
            try {
                c.close();
            } catch (SQLException e) {
            }
        }
        if (!this.isClosed) {
            this.balancer.destroy();
            if (this.connectionGroup != null) {
                this.connectionGroup.closeConnectionProxy(this);
            }
        }
        this.liveConnections.clear();
        this.connectionsToHostsMap.clear();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doClose() {
        closeAllConnections();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doAbortInternal() {
        for (ConnectionImpl c : this.liveConnections.values()) {
            try {
                c.abortInternal();
            } catch (SQLException e) {
            }
        }
        if (!this.isClosed) {
            this.balancer.destroy();
            if (this.connectionGroup != null) {
                this.connectionGroup.closeConnectionProxy(this);
            }
        }
        this.liveConnections.clear();
        this.connectionsToHostsMap.clear();
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    synchronized void doAbort(Executor executor) {
        for (ConnectionImpl c : this.liveConnections.values()) {
            try {
                c.abort(executor);
            } catch (SQLException e) {
            }
        }
        if (!this.isClosed) {
            this.balancer.destroy();
            if (this.connectionGroup != null) {
                this.connectionGroup.closeConnectionProxy(this);
            }
        }
        this.liveConnections.clear();
        this.connectionsToHostsMap.clear();
    }

    /*  JADX ERROR: Types fix failed
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
        	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryPossibleTypes(FixTypesVisitor.java:183)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:242)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
        */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x0153: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:54:0x0153 */
    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    public synchronized java.lang.Object invokeMore(java.lang.Object r8, java.lang.reflect.Method r9, java.lang.Object[] r10) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 354
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.LoadBalancedConnectionProxy.invokeMore(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]):java.lang.Object");
    }

    @Override // com.mysql.jdbc.PingTarget
    public synchronized void doPing() throws SQLException {
        SQLException se = null;
        boolean foundHost = false;
        int pingTimeout = this.currentConnection.getLoadBalancePingTimeout();
        for (String host : this.hostList) {
            ConnectionImpl conn = this.liveConnections.get(host);
            if (conn != null) {
                if (pingTimeout == 0) {
                    try {
                        conn.ping();
                    } catch (SQLException e) {
                        if (host.equals(this.connectionsToHostsMap.get(this.currentConnection))) {
                            closeAllConnections();
                            this.isClosed = true;
                            this.closedReason = "Connection closed because ping of current connection failed.";
                            throw e;
                        }
                        if (e.getMessage().equals(Messages.getString("Connection.exceededConnectionLifetime"))) {
                            if (se == null) {
                                se = e;
                            }
                        } else {
                            se = e;
                            if (isGlobalBlacklistEnabled()) {
                                addToGlobalBlacklist(host);
                            }
                        }
                        this.liveConnections.remove(this.connectionsToHostsMap.get(conn));
                    }
                } else {
                    conn.pingInternal(true, pingTimeout);
                }
                foundHost = true;
            }
        }
        if (!foundHost) {
            closeAllConnections();
            this.isClosed = true;
            this.closedReason = "Connection closed due to inability to ping any active connections.";
            if (se != null) {
                throw se;
            }
            ((ConnectionImpl) this.currentConnection).throwConnectionClosedException();
        }
    }

    public void addToGlobalBlacklist(String host, long timeout) {
        if (isGlobalBlacklistEnabled()) {
            synchronized (globalBlacklist) {
                globalBlacklist.put(host, Long.valueOf(timeout));
            }
        }
    }

    public void addToGlobalBlacklist(String host) {
        addToGlobalBlacklist(host, System.currentTimeMillis() + this.globalBlacklistTimeout);
    }

    public boolean isGlobalBlacklistEnabled() {
        return this.globalBlacklistTimeout > 0;
    }

    public synchronized Map<String, Long> getGlobalBlacklist() {
        if (!isGlobalBlacklistEnabled()) {
            if (this.hostsToRemove.isEmpty()) {
                return new HashMap(1);
            }
            HashMap<String, Long> fakedBlacklist = new HashMap<>();
            for (String h : this.hostsToRemove) {
                fakedBlacklist.put(h, Long.valueOf(System.currentTimeMillis() + 5000));
            }
            return fakedBlacklist;
        }
        Map<String, Long> blacklistClone = new HashMap<>(globalBlacklist.size());
        synchronized (globalBlacklist) {
            blacklistClone.putAll(globalBlacklist);
        }
        Set<String> keys = blacklistClone.keySet();
        keys.retainAll(this.hostList);
        Iterator<String> i = keys.iterator();
        while (i.hasNext()) {
            String host = i.next();
            Long timeout = globalBlacklist.get(host);
            if (timeout != null && timeout.longValue() < System.currentTimeMillis()) {
                synchronized (globalBlacklist) {
                    globalBlacklist.remove(host);
                }
                i.remove();
            }
        }
        if (keys.size() == this.hostList.size()) {
            return new HashMap(1);
        }
        return blacklistClone;
    }

    public void removeHostWhenNotInUse(String hostPortPair) throws SQLException {
        if (this.hostRemovalGracePeriod <= 0) {
            removeHost(hostPortPair);
            return;
        }
        int timeBetweenChecks = this.hostRemovalGracePeriod > 1000 ? 1000 : this.hostRemovalGracePeriod;
        synchronized (this) {
            addToGlobalBlacklist(hostPortPair, System.currentTimeMillis() + this.hostRemovalGracePeriod + timeBetweenChecks);
            long cur = System.currentTimeMillis();
            while (System.currentTimeMillis() < cur + this.hostRemovalGracePeriod) {
                this.hostsToRemove.add(hostPortPair);
                if (!hostPortPair.equals(this.currentConnection.getHostPortPair())) {
                    removeHost(hostPortPair);
                    return;
                }
                try {
                    Thread.sleep(timeBetweenChecks);
                } catch (InterruptedException e) {
                }
            }
            removeHost(hostPortPair);
        }
    }

    public synchronized void removeHost(String hostPortPair) throws SQLException {
        if (this.connectionGroup != null && this.connectionGroup.getInitialHosts().size() == 1 && this.connectionGroup.getInitialHosts().contains(hostPortPair)) {
            throw SQLError.createSQLException("Cannot remove only configured host.", null);
        }
        this.hostsToRemove.add(hostPortPair);
        this.connectionsToHostsMap.remove(this.liveConnections.remove(hostPortPair));
        if (this.hostsToListIndexMap.remove(hostPortPair) != null) {
            long[] newResponseTimes = new long[this.responseTimes.length - 1];
            int newIdx = 0;
            for (String h : this.hostList) {
                if (!this.hostsToRemove.contains(h)) {
                    Integer idx = this.hostsToListIndexMap.get(h);
                    if (idx != null && idx.intValue() < this.responseTimes.length) {
                        newResponseTimes[newIdx] = this.responseTimes[idx.intValue()];
                    }
                    int i = newIdx;
                    newIdx++;
                    this.hostsToListIndexMap.put(h, Integer.valueOf(i));
                }
            }
            this.responseTimes = newResponseTimes;
        }
        if (hostPortPair.equals(this.currentConnection.getHostPortPair())) {
            invalidateConnection(this.currentConnection);
            pickNewConnection();
        }
    }

    public synchronized boolean addHost(String hostPortPair) {
        if (this.hostsToListIndexMap.containsKey(hostPortPair)) {
            return false;
        }
        long[] newResponseTimes = new long[this.responseTimes.length + 1];
        System.arraycopy(this.responseTimes, 0, newResponseTimes, 0, this.responseTimes.length);
        this.responseTimes = newResponseTimes;
        if (!this.hostList.contains(hostPortPair)) {
            this.hostList.add(hostPortPair);
        }
        this.hostsToListIndexMap.put(hostPortPair, Integer.valueOf(this.responseTimes.length - 1));
        this.hostsToRemove.remove(hostPortPair);
        return true;
    }

    public synchronized boolean inTransaction() {
        return this.inTransaction;
    }

    public synchronized long getTransactionCount() {
        return this.transactionCount;
    }

    public synchronized long getActivePhysicalConnectionCount() {
        return this.liveConnections.size();
    }

    public synchronized long getTotalPhysicalConnectionCount() {
        return this.totalPhysicalConnections;
    }

    public synchronized long getConnectionGroupProxyID() {
        return this.connectionGroupProxyID;
    }

    public synchronized String getCurrentActiveHost() {
        Object o;
        MySQLConnection c = this.currentConnection;
        if (c != null && (o = this.connectionsToHostsMap.get(c)) != null) {
            return o.toString();
        }
        return null;
    }

    public synchronized long getCurrentTransactionDuration() {
        if (this.inTransaction && this.transactionStartTime > 0) {
            return System.nanoTime() - this.transactionStartTime;
        }
        return 0L;
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalancedConnectionProxy$NullLoadBalancedConnectionProxy.class */
    private static class NullLoadBalancedConnectionProxy implements InvocationHandler {
        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SQLException exceptionToThrow = SQLError.createSQLException(Messages.getString("LoadBalancedConnectionProxy.unusableConnection"), SQLError.SQL_STATE_INVALID_TRANSACTION_STATE, 1000001, true, (ExceptionInterceptor) null);
            Class<?>[] declaredException = method.getExceptionTypes();
            for (Class<?> declEx : declaredException) {
                if (declEx.isAssignableFrom(exceptionToThrow.getClass())) {
                    throw exceptionToThrow;
                }
            }
            throw new IllegalStateException(exceptionToThrow.getMessage(), exceptionToThrow);
        }
    }

    static synchronized LoadBalancedConnection getNullLoadBalancedConnectionInstance() {
        if (nullLBConnectionInstance == null) {
            nullLBConnectionInstance = (LoadBalancedConnection) Proxy.newProxyInstance(LoadBalancedConnection.class.getClassLoader(), INTERFACES_TO_PROXY, new NullLoadBalancedConnectionProxy());
        }
        return nullLBConnectionInstance;
    }
}
