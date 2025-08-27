package com.mysql.jdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ReplicationConnectionProxy.class */
public class ReplicationConnectionProxy extends MultiHostConnectionProxy implements PingTarget {
    private NonRegisteringDriver driver;
    protected boolean enableJMX;
    protected boolean allowMasterDownConnections;
    protected boolean allowSlaveDownConnections;
    protected boolean readFromMasterWhenNoSlavesOriginal;
    protected boolean readOnly;
    ReplicationConnectionGroup connectionGroup;
    private long connectionGroupID;
    private List<String> masterHosts;
    private Properties masterProperties;
    protected LoadBalancedConnection masterConnection;
    private List<String> slaveHosts;
    private Properties slaveProperties;
    protected LoadBalancedConnection slavesConnection;
    private static Constructor<?> JDBC_4_REPL_CONNECTION_CTOR;
    private static Class<?>[] INTERFACES_TO_PROXY;
    protected boolean readFromMasterWhenNoSlaves = false;
    private ReplicationConnection thisAsReplicationConnection = (ReplicationConnection) this.thisAsConnection;

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_REPL_CONNECTION_CTOR = Class.forName("com.mysql.jdbc.JDBC4ReplicationMySQLConnection").getConstructor(ReplicationConnectionProxy.class);
                INTERFACES_TO_PROXY = new Class[]{ReplicationConnection.class, Class.forName("com.mysql.jdbc.JDBC4MySQLConnection")};
                return;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        }
        INTERFACES_TO_PROXY = new Class[]{ReplicationConnection.class};
    }

    public static ReplicationConnection createProxyInstance(List<String> masterHostList, Properties masterProperties, List<String> slaveHostList, Properties slaveProperties) throws SQLException {
        ReplicationConnectionProxy connProxy = new ReplicationConnectionProxy(masterHostList, masterProperties, slaveHostList, slaveProperties);
        return (ReplicationConnection) Proxy.newProxyInstance(ReplicationConnection.class.getClassLoader(), INTERFACES_TO_PROXY, connProxy);
    }

    private ReplicationConnectionProxy(List<String> masterHostList, Properties masterProperties, List<String> slaveHostList, Properties slaveProperties) throws SQLException {
        this.enableJMX = false;
        this.allowMasterDownConnections = false;
        this.allowSlaveDownConnections = false;
        this.readFromMasterWhenNoSlavesOriginal = false;
        this.readOnly = false;
        this.connectionGroupID = -1L;
        String enableJMXAsString = masterProperties.getProperty("replicationEnableJMX", "false");
        try {
            this.enableJMX = Boolean.parseBoolean(enableJMXAsString);
            String allowMasterDownConnectionsAsString = masterProperties.getProperty("allowMasterDownConnections", "false");
            try {
                this.allowMasterDownConnections = Boolean.parseBoolean(allowMasterDownConnectionsAsString);
                String allowSlaveDownConnectionsAsString = masterProperties.getProperty("allowSlaveDownConnections", "false");
                try {
                    this.allowSlaveDownConnections = Boolean.parseBoolean(allowSlaveDownConnectionsAsString);
                    String readFromMasterWhenNoSlavesAsString = masterProperties.getProperty("readFromMasterWhenNoSlaves");
                    try {
                        this.readFromMasterWhenNoSlavesOriginal = Boolean.parseBoolean(readFromMasterWhenNoSlavesAsString);
                        String group = masterProperties.getProperty("replicationConnectionGroup", null);
                        if (group != null) {
                            this.connectionGroup = ReplicationConnectionGroupManager.getConnectionGroupInstance(group);
                            if (this.enableJMX) {
                                ReplicationConnectionGroupManager.registerJmx();
                            }
                            this.connectionGroupID = this.connectionGroup.registerReplicationConnection(this.thisAsReplicationConnection, masterHostList, slaveHostList);
                            this.slaveHosts = new ArrayList(this.connectionGroup.getSlaveHosts());
                            this.masterHosts = new ArrayList(this.connectionGroup.getMasterHosts());
                        } else {
                            this.slaveHosts = new ArrayList(slaveHostList);
                            this.masterHosts = new ArrayList(masterHostList);
                        }
                        this.driver = new NonRegisteringDriver();
                        this.slaveProperties = slaveProperties;
                        this.masterProperties = masterProperties;
                        resetReadFromMasterWhenNoSlaves();
                        try {
                            initializeSlavesConnection();
                        } catch (SQLException e) {
                            if (!this.allowSlaveDownConnections) {
                                if (this.connectionGroup != null) {
                                    this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection);
                                }
                                throw e;
                            }
                        }
                        SQLException exCaught = null;
                        try {
                            this.currentConnection = initializeMasterConnection();
                        } catch (SQLException e2) {
                            exCaught = e2;
                        }
                        if (this.currentConnection == null) {
                            if (this.allowMasterDownConnections && this.slavesConnection != null) {
                                this.readOnly = true;
                                this.currentConnection = this.slavesConnection;
                            } else {
                                if (this.connectionGroup != null) {
                                    this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection);
                                }
                                if (exCaught != null) {
                                    throw exCaught;
                                }
                                throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.initializationWithEmptyHostsLists"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                            }
                        }
                    } catch (Exception e3) {
                        throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForReadFromMasterWhenNoSlaves", new Object[]{readFromMasterWhenNoSlavesAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                    }
                } catch (Exception e4) {
                    throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForAllowSlaveDownConnections", new Object[]{allowSlaveDownConnectionsAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
                }
            } catch (Exception e5) {
                throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForAllowMasterDownConnections", new Object[]{allowMasterDownConnectionsAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
            }
        } catch (Exception e6) {
            throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.badValueForReplicationEnableJMX", new Object[]{enableJMXAsString}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    MySQLConnection getNewWrapperForThisAsConnection() throws SQLException {
        return (Util.isJdbc4() || JDBC_4_REPL_CONNECTION_CTOR != null) ? (MySQLConnection) Util.handleNewInstance(JDBC_4_REPL_CONNECTION_CTOR, new Object[]{this}, null) : new ReplicationMySQLConnection(this);
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    protected void propagateProxyDown(MySQLConnection proxyConn) {
        if (this.masterConnection != null) {
            this.masterConnection.setProxy(proxyConn);
        }
        if (this.slavesConnection != null) {
            this.slavesConnection.setProxy(proxyConn);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    boolean shouldExceptionTriggerConnectionSwitch(Throwable t) {
        return false;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    public boolean isMasterConnection() {
        return this.currentConnection != null && this.currentConnection == this.masterConnection;
    }

    public boolean isSlavesConnection() {
        return this.currentConnection != null && this.currentConnection == this.slavesConnection;
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void pickNewConnection() throws SQLException {
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void syncSessionState(Connection source, Connection target, boolean readOnlyStatus) throws SQLException {
        try {
            super.syncSessionState(source, target, readOnlyStatus);
        } catch (SQLException e) {
            try {
                super.syncSessionState(source, target, readOnlyStatus);
            } catch (SQLException e2) {
            }
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void doClose() throws SQLException {
        if (this.masterConnection != null) {
            this.masterConnection.close();
        }
        if (this.slavesConnection != null) {
            this.slavesConnection.close();
        }
        if (this.connectionGroup != null) {
            this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void doAbortInternal() throws SQLException {
        this.masterConnection.abortInternal();
        this.slavesConnection.abortInternal();
        if (this.connectionGroup != null) {
            this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    void doAbort(Executor executor) throws SQLException {
        this.masterConnection.abort(executor);
        this.slavesConnection.abort(executor);
        if (this.connectionGroup != null) {
            this.connectionGroup.handleCloseConnection(this.thisAsReplicationConnection);
        }
    }

    @Override // com.mysql.jdbc.MultiHostConnectionProxy
    Object invokeMore(Object proxy, Method method, Object[] args) throws Throwable {
        checkConnectionCapabilityForMethod(method);
        boolean invokeAgain = false;
        do {
            try {
                Object result = method.invoke(this.thisAsConnection, args);
                if (result != null && (result instanceof Statement)) {
                    ((Statement) result).setPingTarget(this);
                }
                return result;
            } catch (InvocationTargetException e) {
                if (invokeAgain) {
                    invokeAgain = false;
                } else if (e.getCause() != null && (e.getCause() instanceof SQLException) && ((SQLException) e.getCause()).getSQLState() == SQLError.SQL_STATE_INVALID_TRANSACTION_STATE && ((SQLException) e.getCause()).getErrorCode() == 1000001) {
                    try {
                        setReadOnly(this.readOnly);
                        invokeAgain = true;
                    } catch (SQLException e2) {
                    }
                }
            }
        } while (invokeAgain);
        throw e;
    }

    private void checkConnectionCapabilityForMethod(Method method) throws Throwable {
        if (this.masterHosts.isEmpty() && this.slaveHosts.isEmpty() && !ReplicationConnection.class.isAssignableFrom(method.getDeclaringClass())) {
            throw SQLError.createSQLException(Messages.getString("ReplicationConnectionProxy.noHostsInconsistentState"), SQLError.SQL_STATE_INVALID_TRANSACTION_STATE, MysqlErrorNumbers.ERROR_CODE_REPLICATION_CONNECTION_WITH_NO_HOSTS, true, (ExceptionInterceptor) null);
        }
    }

    @Override // com.mysql.jdbc.PingTarget
    public void doPing() throws SQLException {
        boolean isMasterConn = isMasterConnection();
        SQLException mastersPingException = null;
        SQLException slavesPingException = null;
        if (this.masterConnection != null) {
            try {
                this.masterConnection.ping();
            } catch (SQLException e) {
                mastersPingException = e;
            }
        } else {
            initializeMasterConnection();
        }
        if (this.slavesConnection != null) {
            try {
                this.slavesConnection.ping();
            } catch (SQLException e2) {
                slavesPingException = e2;
            }
        } else {
            try {
                initializeSlavesConnection();
                if (switchToSlavesConnectionIfNecessary()) {
                    isMasterConn = false;
                }
            } catch (SQLException e3) {
                if (this.masterConnection == null || !this.readFromMasterWhenNoSlaves) {
                    throw e3;
                }
            }
        }
        if (isMasterConn && mastersPingException != null) {
            if (this.slavesConnection != null && slavesPingException == null) {
                this.masterConnection = null;
                this.currentConnection = this.slavesConnection;
                this.readOnly = true;
            }
            throw mastersPingException;
        }
        if (isMasterConn) {
            return;
        }
        if (slavesPingException != null || this.slavesConnection == null) {
            if (this.masterConnection != null && this.readFromMasterWhenNoSlaves && mastersPingException == null) {
                this.slavesConnection = null;
                this.currentConnection = this.masterConnection;
                this.readOnly = true;
                this.currentConnection.setReadOnly(true);
            }
            if (slavesPingException != null) {
                throw slavesPingException;
            }
        }
    }

    private MySQLConnection initializeMasterConnection() throws SQLException {
        this.masterConnection = null;
        if (this.masterHosts.size() == 0) {
            return null;
        }
        LoadBalancedConnection newMasterConn = (LoadBalancedConnection) this.driver.connect(buildURL(this.masterHosts, this.masterProperties), this.masterProperties);
        newMasterConn.setProxy(getProxy());
        this.masterConnection = newMasterConn;
        return this.masterConnection;
    }

    private MySQLConnection initializeSlavesConnection() throws SQLException {
        this.slavesConnection = null;
        if (this.slaveHosts.size() == 0) {
            return null;
        }
        LoadBalancedConnection newSlavesConn = (LoadBalancedConnection) this.driver.connect(buildURL(this.slaveHosts, this.slaveProperties), this.slaveProperties);
        newSlavesConn.setProxy(getProxy());
        newSlavesConn.setReadOnly(true);
        this.slavesConnection = newSlavesConn;
        return this.slavesConnection;
    }

    private String buildURL(List<String> hosts, Properties props) {
        StringBuilder url = new StringBuilder(NonRegisteringDriver.LOADBALANCE_URL_PREFIX);
        boolean firstHost = true;
        for (String host : hosts) {
            if (!firstHost) {
                url.append(',');
            }
            url.append(host);
            firstHost = false;
        }
        url.append("/");
        String masterDb = props.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
        if (masterDb != null) {
            url.append(masterDb);
        }
        return url.toString();
    }

    private synchronized boolean switchToMasterConnection() throws SQLException {
        if (this.masterConnection == null || this.masterConnection.isClosed()) {
            try {
                if (initializeMasterConnection() == null) {
                    return false;
                }
            } catch (SQLException e) {
                this.currentConnection = null;
                throw e;
            }
        }
        if (!isMasterConnection() && this.masterConnection != null) {
            syncSessionState(this.currentConnection, this.masterConnection, false);
            this.currentConnection = this.masterConnection;
            return true;
        }
        return true;
    }

    private synchronized boolean switchToSlavesConnection() throws SQLException {
        if (this.slavesConnection == null || this.slavesConnection.isClosed()) {
            try {
                if (initializeSlavesConnection() == null) {
                    return false;
                }
            } catch (SQLException e) {
                this.currentConnection = null;
                throw e;
            }
        }
        if (!isSlavesConnection() && this.slavesConnection != null) {
            syncSessionState(this.currentConnection, this.slavesConnection, true);
            this.currentConnection = this.slavesConnection;
            return true;
        }
        return true;
    }

    private boolean switchToSlavesConnectionIfNecessary() throws SQLException {
        if (this.currentConnection == null || ((isMasterConnection() && (this.readOnly || (this.masterHosts.isEmpty() && this.currentConnection.isClosed()))) || (!isMasterConnection() && this.currentConnection.isClosed()))) {
            return switchToSlavesConnection();
        }
        return false;
    }

    public synchronized Connection getCurrentConnection() {
        return this.currentConnection == null ? LoadBalancedConnectionProxy.getNullLoadBalancedConnectionInstance() : this.currentConnection;
    }

    public long getConnectionGroupId() {
        return this.connectionGroupID;
    }

    public synchronized Connection getMasterConnection() {
        return this.masterConnection;
    }

    public synchronized void promoteSlaveToMaster(String hostPortPair) throws SQLException {
        this.masterHosts.add(hostPortPair);
        removeSlave(hostPortPair);
        if (this.masterConnection != null) {
            this.masterConnection.addHost(hostPortPair);
        }
        if (!this.readOnly && !isMasterConnection()) {
            switchToMasterConnection();
        }
    }

    public synchronized void removeMasterHost(String hostPortPair) throws SQLException {
        removeMasterHost(hostPortPair, true);
    }

    public synchronized void removeMasterHost(String hostPortPair, boolean waitUntilNotInUse) throws SQLException {
        removeMasterHost(hostPortPair, waitUntilNotInUse, false);
    }

    public synchronized void removeMasterHost(String hostPortPair, boolean waitUntilNotInUse, boolean isNowSlave) throws SQLException {
        if (isNowSlave) {
            this.slaveHosts.add(hostPortPair);
            resetReadFromMasterWhenNoSlaves();
        }
        this.masterHosts.remove(hostPortPair);
        if (this.masterConnection == null || this.masterConnection.isClosed()) {
            this.masterConnection = null;
            return;
        }
        if (waitUntilNotInUse) {
            this.masterConnection.removeHostWhenNotInUse(hostPortPair);
        } else {
            this.masterConnection.removeHost(hostPortPair);
        }
        if (this.masterHosts.isEmpty()) {
            this.masterConnection.close();
            this.masterConnection = null;
            switchToSlavesConnectionIfNecessary();
        }
    }

    public boolean isHostMaster(String hostPortPair) {
        if (hostPortPair == null) {
            return false;
        }
        for (String masterHost : this.masterHosts) {
            if (masterHost.equalsIgnoreCase(hostPortPair)) {
                return true;
            }
        }
        return false;
    }

    public synchronized Connection getSlavesConnection() {
        return this.slavesConnection;
    }

    public synchronized void addSlaveHost(String hostPortPair) throws SQLException {
        if (isHostSlave(hostPortPair)) {
            return;
        }
        this.slaveHosts.add(hostPortPair);
        resetReadFromMasterWhenNoSlaves();
        if (this.slavesConnection == null) {
            initializeSlavesConnection();
            switchToSlavesConnectionIfNecessary();
        } else {
            this.slavesConnection.addHost(hostPortPair);
        }
    }

    public synchronized void removeSlave(String hostPortPair) throws SQLException {
        removeSlave(hostPortPair, true);
    }

    public synchronized void removeSlave(String hostPortPair, boolean closeGently) throws SQLException {
        this.slaveHosts.remove(hostPortPair);
        resetReadFromMasterWhenNoSlaves();
        if (this.slavesConnection == null || this.slavesConnection.isClosed()) {
            this.slavesConnection = null;
            return;
        }
        if (closeGently) {
            this.slavesConnection.removeHostWhenNotInUse(hostPortPair);
        } else {
            this.slavesConnection.removeHost(hostPortPair);
        }
        if (this.slaveHosts.isEmpty()) {
            this.slavesConnection.close();
            this.slavesConnection = null;
            switchToMasterConnection();
            if (isMasterConnection()) {
                this.currentConnection.setReadOnly(this.readOnly);
            }
        }
    }

    public boolean isHostSlave(String hostPortPair) {
        if (hostPortPair == null) {
            return false;
        }
        for (String test : this.slaveHosts) {
            if (test.equalsIgnoreCase(hostPortPair)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void setReadOnly(boolean readOnly) throws SQLException {
        boolean switched;
        boolean switched2;
        if (readOnly) {
            if (!isSlavesConnection() || this.currentConnection.isClosed()) {
                SQLException exceptionCaught = null;
                try {
                    switched2 = switchToSlavesConnection();
                } catch (SQLException e) {
                    switched2 = false;
                    exceptionCaught = e;
                }
                if (!switched2 && this.readFromMasterWhenNoSlaves && switchToMasterConnection()) {
                    exceptionCaught = null;
                }
                if (exceptionCaught != null) {
                    throw exceptionCaught;
                }
            }
        } else if (!isMasterConnection() || this.currentConnection.isClosed()) {
            SQLException exceptionCaught2 = null;
            try {
                switched = switchToMasterConnection();
            } catch (SQLException e2) {
                switched = false;
                exceptionCaught2 = e2;
            }
            if (!switched && switchToSlavesConnectionIfNecessary()) {
                exceptionCaught2 = null;
            }
            if (exceptionCaught2 != null) {
                throw exceptionCaught2;
            }
        }
        this.readOnly = readOnly;
        if (this.readFromMasterWhenNoSlaves && isMasterConnection()) {
            this.currentConnection.setReadOnly(this.readOnly);
        }
    }

    public boolean isReadOnly() throws SQLException {
        return !isMasterConnection() || this.readOnly;
    }

    private void resetReadFromMasterWhenNoSlaves() {
        this.readFromMasterWhenNoSlaves = this.slaveHosts.isEmpty() || this.readFromMasterWhenNoSlavesOriginal;
    }
}
