package com.mysql.fabric.jdbc;

import com.mysql.fabric.FabricCommunicationException;
import com.mysql.fabric.FabricConnection;
import com.mysql.fabric.Server;
import com.mysql.fabric.ServerGroup;
import com.mysql.fabric.ShardMapping;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.CachedResultSetMetaData;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ConnectionPropertiesImpl;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Extension;
import com.mysql.jdbc.Field;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.MysqlIO;
import com.mysql.jdbc.NonRegisteringDriver;
import com.mysql.jdbc.ReplicationConnection;
import com.mysql.jdbc.ReplicationConnectionGroup;
import com.mysql.jdbc.ReplicationConnectionGroupManager;
import com.mysql.jdbc.ReplicationConnectionProxy;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.ServerPreparedStatement;
import com.mysql.jdbc.SingleByteCharsetConverter;
import com.mysql.jdbc.StatementImpl;
import com.mysql.jdbc.StatementInterceptorV2;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.log.LogFactory;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/fabric/jdbc/FabricMySQLConnectionProxy.class */
public class FabricMySQLConnectionProxy extends ConnectionPropertiesImpl implements FabricMySQLConnection, FabricMySQLConnectionProperties {
    private static final long serialVersionUID = 5845485979107347258L;
    private Log log;
    protected FabricConnection fabricConnection;
    protected ReplicationConnection currentConnection;
    protected String shardKey;
    protected String shardTable;
    protected String serverGroupName;
    protected ServerGroup serverGroup;
    protected String host;
    protected String port;
    protected String username;
    protected String password;
    protected String database;
    protected ShardMapping shardMapping;
    private String fabricShardKey;
    private String fabricShardTable;
    private String fabricServerGroup;
    private String fabricProtocol;
    private String fabricUsername;
    private String fabricPassword;
    private boolean reportErrors;
    private static final Set<String> replConnGroupLocks = Collections.synchronizedSet(new HashSet());
    private static final Class<?> JDBC4_NON_TRANSIENT_CONN_EXCEPTION;
    protected boolean closed = false;
    protected boolean transactionInProgress = false;
    protected Map<ServerGroup, ReplicationConnection> serverConnections = new HashMap();
    protected Set<String> queryTables = new HashSet();
    protected boolean readOnly = false;
    protected boolean autoCommit = true;
    protected int transactionIsolation = 4;

    static {
        Class<?> clazz = null;
        try {
            if (Util.isJdbc4()) {
                clazz = Class.forName("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException");
            }
        } catch (ClassNotFoundException e) {
        }
        JDBC4_NON_TRANSIENT_CONN_EXCEPTION = clazz;
    }

    public FabricMySQLConnectionProxy(Properties props) throws SQLException {
        String exceptionInterceptors;
        this.reportErrors = false;
        this.fabricShardKey = props.getProperty(FabricMySQLDriver.FABRIC_SHARD_KEY_PROPERTY_KEY);
        this.fabricShardTable = props.getProperty(FabricMySQLDriver.FABRIC_SHARD_TABLE_PROPERTY_KEY);
        this.fabricServerGroup = props.getProperty(FabricMySQLDriver.FABRIC_SERVER_GROUP_PROPERTY_KEY);
        this.fabricProtocol = props.getProperty(FabricMySQLDriver.FABRIC_PROTOCOL_PROPERTY_KEY);
        this.fabricUsername = props.getProperty(FabricMySQLDriver.FABRIC_USERNAME_PROPERTY_KEY);
        this.fabricPassword = props.getProperty(FabricMySQLDriver.FABRIC_PASSWORD_PROPERTY_KEY);
        this.reportErrors = Boolean.valueOf(props.getProperty(FabricMySQLDriver.FABRIC_REPORT_ERRORS_PROPERTY_KEY)).booleanValue();
        props.remove(FabricMySQLDriver.FABRIC_SHARD_KEY_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_SHARD_TABLE_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_SERVER_GROUP_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_PROTOCOL_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_USERNAME_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_PASSWORD_PROPERTY_KEY);
        props.remove(FabricMySQLDriver.FABRIC_REPORT_ERRORS_PROPERTY_KEY);
        this.host = props.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY);
        this.port = props.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY);
        this.username = props.getProperty("user");
        this.password = props.getProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY);
        this.database = props.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY);
        if (this.username == null) {
            this.username = "";
        }
        if (this.password == null) {
            this.password = "";
        }
        String exceptionInterceptors2 = props.getProperty("exceptionInterceptors");
        if (exceptionInterceptors2 == null || "null".equals("exceptionInterceptors")) {
            exceptionInterceptors = "";
        } else {
            exceptionInterceptors = exceptionInterceptors2 + ",";
        }
        props.setProperty("exceptionInterceptors", exceptionInterceptors + "com.mysql.fabric.jdbc.ErrorReportingExceptionInterceptor");
        initializeProperties(props);
        if (this.fabricServerGroup != null && this.fabricShardTable != null) {
            throw SQLError.createSQLException("Server group and shard table are mutually exclusive. Only one may be provided.", SQLError.SQL_STATE_CONNECTION_REJECTED, (Throwable) null, getExceptionInterceptor(), this);
        }
        try {
            String url = this.fabricProtocol + "://" + this.host + ":" + this.port;
            this.fabricConnection = new FabricConnection(url, this.fabricUsername, this.fabricPassword);
            this.log = LogFactory.getLogger(getLogger(), "FabricMySQLConnectionProxy", null);
            setShardTable(this.fabricShardTable);
            setShardKey(this.fabricShardKey);
            setServerGroupName(this.fabricServerGroup);
        } catch (FabricCommunicationException ex) {
            throw SQLError.createSQLException("Unable to establish connection to the Fabric server", SQLError.SQL_STATE_CONNECTION_REJECTED, ex, getExceptionInterceptor(), this);
        }
    }

    synchronized SQLException interceptException(SQLException sqlEx, Connection conn, String groupName, String hostname, String portNumber) throws FabricCommunicationException {
        Server currentServer;
        if ((sqlEx.getSQLState() == null || !sqlEx.getSQLState().startsWith("08")) && !MySQLNonTransientConnectionException.class.isAssignableFrom(sqlEx.getClass()) && (JDBC4_NON_TRANSIENT_CONN_EXCEPTION == null || !JDBC4_NON_TRANSIENT_CONN_EXCEPTION.isAssignableFrom(sqlEx.getClass()))) {
            return null;
        }
        if ((sqlEx.getCause() != null && FabricCommunicationException.class.isAssignableFrom(sqlEx.getCause().getClass())) || (currentServer = this.serverGroup.getServer(hostname + ":" + portNumber)) == null) {
            return null;
        }
        if (this.reportErrors) {
            this.fabricConnection.getClient().reportServerError(currentServer, sqlEx.toString(), true);
        }
        try {
            if (replConnGroupLocks.add(this.serverGroup.getName())) {
                this.fabricConnection.refreshStatePassive();
                setCurrentServerGroup(this.serverGroup.getName());
                try {
                    syncGroupServersToReplicationConnectionGroup(ReplicationConnectionGroupManager.getConnectionGroup(groupName));
                    return null;
                } catch (SQLException ex) {
                    return ex;
                }
            }
            return SQLError.createSQLException("Fabric state syncing already in progress in another thread.", SQLError.SQL_STATE_CONNECTION_FAILURE, sqlEx, (ExceptionInterceptor) null);
        } catch (SQLException ex2) {
            return SQLError.createSQLException("Unable to refresh Fabric state. Failover impossible", SQLError.SQL_STATE_CONNECTION_FAILURE, ex2, (ExceptionInterceptor) null);
        } finally {
            replConnGroupLocks.remove(this.serverGroup.getName());
        }
    }

    private void refreshStateIfNecessary() throws SQLException {
        if (this.fabricConnection.isStateExpired()) {
            this.fabricConnection.refreshStatePassive();
            if (this.serverGroup != null) {
                setCurrentServerGroup(this.serverGroup.getName());
            }
        }
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void setShardKey(String shardKey) throws SQLException {
        ensureNoTransactionInProgress();
        this.currentConnection = null;
        if (shardKey != null) {
            if (this.serverGroupName != null) {
                throw SQLError.createSQLException("Shard key cannot be provided when server group is chosen directly.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
            }
            if (this.shardTable == null) {
                throw SQLError.createSQLException("Shard key cannot be provided without a shard table.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
            }
            setCurrentServerGroup(this.shardMapping.getGroupNameForKey(shardKey));
        } else if (this.shardTable != null) {
            setCurrentServerGroup(this.shardMapping.getGlobalGroupName());
        }
        this.shardKey = shardKey;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public String getShardKey() {
        return this.shardKey;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void setShardTable(String shardTable) throws SQLException {
        ensureNoTransactionInProgress();
        this.currentConnection = null;
        if (this.serverGroupName != null) {
            throw SQLError.createSQLException("Server group and shard table are mutually exclusive. Only one may be provided.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
        }
        this.shardKey = null;
        this.serverGroup = null;
        this.shardTable = shardTable;
        if (shardTable == null) {
            this.shardMapping = null;
            return;
        }
        String table = shardTable;
        String db = this.database;
        if (shardTable.contains(".")) {
            String[] pair = shardTable.split("\\.");
            db = pair[0];
            table = pair[1];
        }
        this.shardMapping = this.fabricConnection.getShardMapping(db, table);
        if (this.shardMapping == null) {
            throw SQLError.createSQLException("Shard mapping not found for table `" + shardTable + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
        }
        setCurrentServerGroup(this.shardMapping.getGlobalGroupName());
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public String getShardTable() {
        return this.shardTable;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void setServerGroupName(String serverGroupName) throws SQLException {
        ensureNoTransactionInProgress();
        this.currentConnection = null;
        if (serverGroupName != null) {
            setCurrentServerGroup(serverGroupName);
        }
        this.serverGroupName = serverGroupName;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public String getServerGroupName() {
        return this.serverGroupName;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void clearServerSelectionCriteria() throws SQLException {
        ensureNoTransactionInProgress();
        this.shardTable = null;
        this.shardKey = null;
        this.serverGroupName = null;
        this.serverGroup = null;
        this.queryTables.clear();
        this.currentConnection = null;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public ServerGroup getCurrentServerGroup() {
        return this.serverGroup;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void clearQueryTables() throws SQLException {
        ensureNoTransactionInProgress();
        this.currentConnection = null;
        this.queryTables.clear();
        setShardTable(null);
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public void addQueryTable(String tableName) throws SQLException {
        ensureNoTransactionInProgress();
        this.currentConnection = null;
        if (this.shardMapping == null) {
            if (this.fabricConnection.getShardMapping(this.database, tableName) != null) {
                setShardTable(tableName);
            }
        } else {
            ShardMapping mappingForTableName = this.fabricConnection.getShardMapping(this.database, tableName);
            if (mappingForTableName != null && !mappingForTableName.equals(this.shardMapping)) {
                throw SQLError.createSQLException("Cross-shard query not allowed", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
            }
        }
        this.queryTables.add(tableName);
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnection
    public Set<String> getQueryTables() {
        return this.queryTables;
    }

    protected void setCurrentServerGroup(String serverGroupName) throws SQLException {
        this.serverGroup = this.fabricConnection.getServerGroup(serverGroupName);
        if (this.serverGroup == null) {
            throw SQLError.createSQLException("Cannot find server group: `" + serverGroupName + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, (Throwable) null, getExceptionInterceptor(), this);
        }
        ReplicationConnectionGroup replConnGroup = ReplicationConnectionGroupManager.getConnectionGroup(serverGroupName);
        if (replConnGroup != null && replConnGroupLocks.add(this.serverGroup.getName())) {
            try {
                syncGroupServersToReplicationConnectionGroup(replConnGroup);
            } finally {
                replConnGroupLocks.remove(this.serverGroup.getName());
            }
        }
    }

    protected MySQLConnection getActiveMySQLConnectionChecked() throws SQLException {
        ReplicationConnection c = (ReplicationConnection) getActiveConnection();
        MySQLConnection mc = (MySQLConnection) c.getCurrentConnection();
        return mc;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getActiveMySQLConnection() {
        try {
            return getActiveMySQLConnectionChecked();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to determine active connection", ex);
        }
    }

    protected Connection getActiveConnectionPassive() {
        try {
            return getActiveConnection();
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to determine active connection", ex);
        }
    }

    private void syncGroupServersToReplicationConnectionGroup(ReplicationConnectionGroup replConnGroup) throws SQLException {
        String currentMasterString = null;
        if (replConnGroup.getMasterHosts().size() == 1) {
            currentMasterString = replConnGroup.getMasterHosts().iterator().next();
        }
        if (currentMasterString != null && (this.serverGroup.getMaster() == null || !currentMasterString.equals(this.serverGroup.getMaster().getHostPortString()))) {
            try {
                replConnGroup.removeMasterHost(currentMasterString, false);
            } catch (SQLException ex) {
                getLog().logWarn("Unable to remove master: " + currentMasterString, ex);
            }
        }
        Server newMaster = this.serverGroup.getMaster();
        if (newMaster != null && replConnGroup.getMasterHosts().size() == 0) {
            getLog().logInfo("Changing master for group '" + replConnGroup.getGroupName() + "' to: " + newMaster);
            try {
                if (!replConnGroup.getSlaveHosts().contains(newMaster.getHostPortString())) {
                    replConnGroup.addSlaveHost(newMaster.getHostPortString());
                }
                replConnGroup.promoteSlaveToMaster(newMaster.getHostPortString());
            } catch (SQLException ex2) {
                throw SQLError.createSQLException("Unable to promote new master '" + newMaster.toString() + "'", ex2.getSQLState(), ex2, (ExceptionInterceptor) null);
            }
        }
        for (Server s : this.serverGroup.getServers()) {
            if (s.isSlave()) {
                try {
                    replConnGroup.addSlaveHost(s.getHostPortString());
                } catch (SQLException ex3) {
                    getLog().logWarn("Unable to add slave: " + s.toString(), ex3);
                }
            }
        }
        for (String hostPortString : replConnGroup.getSlaveHosts()) {
            Server fabServer = this.serverGroup.getServer(hostPortString);
            if (fabServer == null || !fabServer.isSlave()) {
                try {
                    replConnGroup.removeSlaveHost(hostPortString, true);
                } catch (SQLException ex4) {
                    getLog().logWarn("Unable to remove slave: " + hostPortString, ex4);
                }
            }
        }
    }

    protected Connection getActiveConnection() throws SQLException {
        if (!this.transactionInProgress) {
            refreshStateIfNecessary();
        }
        if (this.currentConnection != null) {
            return this.currentConnection;
        }
        if (getCurrentServerGroup() == null) {
            throw SQLError.createSQLException("No server group selected.", SQLError.SQL_STATE_CONNECTION_REJECTED, (Throwable) null, getExceptionInterceptor(), this);
        }
        this.currentConnection = this.serverConnections.get(this.serverGroup);
        if (this.currentConnection != null) {
            return this.currentConnection;
        }
        List<String> masterHost = new ArrayList<>();
        List<String> slaveHosts = new ArrayList<>();
        for (Server s : this.serverGroup.getServers()) {
            if (s.isMaster()) {
                masterHost.add(s.getHostPortString());
            } else if (s.isSlave()) {
                slaveHosts.add(s.getHostPortString());
            }
        }
        Properties info = exposeAsProperties(null);
        ReplicationConnectionGroup replConnGroup = ReplicationConnectionGroupManager.getConnectionGroup(this.serverGroup.getName());
        if (replConnGroup != null && replConnGroupLocks.add(this.serverGroup.getName())) {
            try {
                syncGroupServersToReplicationConnectionGroup(replConnGroup);
            } finally {
                replConnGroupLocks.remove(this.serverGroup.getName());
            }
        }
        info.put("replicationConnectionGroup", this.serverGroup.getName());
        info.setProperty("user", this.username);
        info.setProperty(NonRegisteringDriver.PASSWORD_PROPERTY_KEY, this.password);
        info.setProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY, getCatalog());
        info.setProperty("connectionAttributes", "fabricHaGroup:" + this.serverGroup.getName());
        info.setProperty("retriesAllDown", "1");
        info.setProperty("allowMasterDownConnections", "true");
        info.setProperty("allowSlaveDownConnections", "true");
        info.setProperty("readFromMasterWhenNoSlaves", "true");
        this.currentConnection = ReplicationConnectionProxy.createProxyInstance(masterHost, info, slaveHosts, info);
        this.serverConnections.put(this.serverGroup, this.currentConnection);
        this.currentConnection.setProxy(this);
        this.currentConnection.setAutoCommit(this.autoCommit);
        this.currentConnection.setReadOnly(this.readOnly);
        this.currentConnection.setTransactionIsolation(this.transactionIsolation);
        return this.currentConnection;
    }

    private void ensureOpen() throws SQLException {
        if (this.closed) {
            throw SQLError.createSQLException("No operations allowed after connection closed.", SQLError.SQL_STATE_CONNECTION_NOT_OPEN, getExceptionInterceptor());
        }
    }

    private void ensureNoTransactionInProgress() throws SQLException {
        ensureOpen();
        if (this.transactionInProgress && !this.autoCommit) {
            throw SQLError.createSQLException("Not allow while a transaction is active.", SQLError.SQL_STATE_INVALID_TRANSACTION_STATE, getExceptionInterceptor());
        }
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        this.closed = true;
        for (ReplicationConnection c : this.serverConnections.values()) {
            try {
                c.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override // java.sql.Connection
    public boolean isClosed() {
        return this.closed;
    }

    @Override // java.sql.Connection
    public boolean isValid(int timeout) throws SQLException {
        return !this.closed;
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
        for (ReplicationConnection conn : this.serverConnections.values()) {
            conn.setReadOnly(readOnly);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection, java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        return this.readOnly;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isReadOnly(boolean useSessionStatus) throws SQLException {
        return this.readOnly;
    }

    @Override // java.sql.Connection
    public void setCatalog(String catalog) throws SQLException {
        this.database = catalog;
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setCatalog(catalog);
        }
    }

    @Override // java.sql.Connection
    public String getCatalog() {
        return this.database;
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        getActiveConnection().rollback();
        transactionCompleted();
    }

    @Override // java.sql.Connection
    public void rollback(Savepoint savepoint) throws SQLException {
        getActiveConnection().rollback();
        transactionCompleted();
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        getActiveConnection().commit();
        transactionCompleted();
    }

    @Override // java.sql.Connection
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setAutoCommit(this.autoCommit);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionBegun() throws SQLException {
        if (!this.autoCommit) {
            this.transactionInProgress = true;
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionCompleted() throws SQLException {
        this.transactionInProgress = false;
        refreshStateIfNecessary();
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() {
        return this.autoCommit;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public MySQLConnection getLoadBalanceSafeProxy() {
        return getMultiHostSafeProxy();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getMultiHostSafeProxy() {
        return getActiveMySQLConnection();
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionIsolation = level;
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setTransactionIsolation(level);
        }
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setTypeMap(map);
        }
    }

    @Override // java.sql.Connection
    public void setHoldability(int holdability) throws SQLException {
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setHoldability(holdability);
        }
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void setProxy(MySQLConnection proxy) {
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        return getActiveConnection().setSavepoint();
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String name) throws SQLException {
        this.transactionInProgress = true;
        return getActiveConnection().setSavepoint(name);
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint savepoint) {
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareCall(sql);
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql, columnIndexes);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        transactionBegun();
        return getActiveConnection().prepareStatement(sql, columnNames);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql, autoGenKeyIndex);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        transactionBegun();
        return getActiveConnection().clientPrepareStatement(sql, autoGenKeyColNames);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql, autoGenKeyIndex);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        transactionBegun();
        return getActiveConnection().serverPrepareStatement(sql, autoGenKeyColNames);
    }

    @Override // java.sql.Connection
    public Statement createStatement() throws SQLException {
        transactionBegun();
        return getActiveConnection().createStatement();
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        transactionBegun();
        return getActiveConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        transactionBegun();
        return getActiveConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return getActiveMySQLConnectionChecked().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
        return getActiveMySQLConnectionChecked().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, isBatch);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
        return getActiveMySQLConnectionChecked().extractSqlFromPacket(possibleSqlQuery, queryPacket, endOfQueryPacketPosition);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public StringBuilder generateConnectionCommentBlock(StringBuilder buf) {
        return getActiveMySQLConnection().generateConnectionCommentBlock(buf);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MysqlIO getIO() throws SQLException {
        return getActiveMySQLConnectionChecked().getIO();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getCalendarInstanceForSessionOrNew() {
        return getActiveMySQLConnection().getCalendarInstanceForSessionOrNew();
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public String getServerCharacterEncoding() {
        return getServerCharset();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getServerCharset() {
        return getActiveMySQLConnection().getServerCharset();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public TimeZone getServerTimezoneTZ() {
        return getActiveMySQLConnection().getServerTimezoneTZ();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        return getActiveConnection().versionMeetsMinimum(major, minor, subminor);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsIsolationLevel() {
        return getActiveConnectionPassive().supportsIsolationLevel();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsQuotedIdentifiers() {
        return getActiveConnectionPassive().supportsQuotedIdentifiers();
    }

    @Override // java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        return getActiveConnection().getMetaData();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getCharacterSetMetadata() {
        return getActiveMySQLConnection().getCharacterSetMetadata();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Statement getMetadataSafeStatement() throws SQLException {
        return getActiveMySQLConnectionChecked().getMetadataSafeStatement();
    }

    @Override // java.sql.Wrapper
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }

    @Override // java.sql.Wrapper
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unSafeStatementInterceptors() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsTransactions() {
        return true;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isRunningOnJDK13() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void createNewIO(boolean isForReconnect) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void dumpTestcaseQuery(String query) {
    }

    @Override // com.mysql.jdbc.Connection
    public void abortInternal() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isServerLocal() throws SQLException {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void shutdownServer() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void clearHasTriedMaster() {
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public boolean hasTriedMaster() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isInGlobalTx() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public void setInGlobalTx(boolean flag) {
        throw new RuntimeException("Global transactions not supported.");
    }

    @Override // com.mysql.jdbc.Connection
    public void changeUser(String userName, String newPassword) throws SQLException {
        throw SQLError.createSQLException("User change not allowed.", getExceptionInterceptor());
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricShardKey(String value) {
        this.fabricShardKey = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricShardKey() {
        return this.fabricShardKey;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricShardTable(String value) {
        this.fabricShardTable = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricShardTable() {
        return this.fabricShardTable;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricServerGroup(String value) {
        this.fabricServerGroup = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricServerGroup() {
        return this.fabricServerGroup;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricProtocol(String value) {
        this.fabricProtocol = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricProtocol() {
        return this.fabricProtocol;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricUsername(String value) {
        this.fabricUsername = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricUsername() {
        return this.fabricUsername;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricPassword(String value) {
        this.fabricPassword = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public String getFabricPassword() {
        return this.fabricPassword;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public void setFabricReportErrors(boolean value) {
        this.reportErrors = value;
    }

    @Override // com.mysql.fabric.jdbc.FabricMySQLConnectionProperties
    public boolean getFabricReportErrors() {
        return this.reportErrors;
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAllowLoadLocalInfile(boolean property) {
        super.setAllowLoadLocalInfile(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAllowLoadLocalInfile(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAllowMultiQueries(boolean property) {
        super.setAllowMultiQueries(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAllowMultiQueries(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAllowNanAndInf(boolean flag) {
        super.setAllowNanAndInf(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAllowNanAndInf(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAllowUrlInLocalInfile(boolean flag) {
        super.setAllowUrlInLocalInfile(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAllowUrlInLocalInfile(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAlwaysSendSetIsolation(boolean flag) {
        super.setAlwaysSendSetIsolation(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAlwaysSendSetIsolation(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoDeserialize(boolean flag) {
        super.setAutoDeserialize(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoDeserialize(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoGenerateTestcaseScript(boolean flag) {
        super.setAutoGenerateTestcaseScript(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoGenerateTestcaseScript(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnect(boolean flag) {
        super.setAutoReconnect(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoReconnect(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForConnectionPools(boolean property) {
        super.setAutoReconnectForConnectionPools(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoReconnectForConnectionPools(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForPools(boolean flag) {
        super.setAutoReconnectForPools(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoReconnectForPools(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setBlobSendChunkSize(String value) throws SQLException {
        super.setBlobSendChunkSize(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setBlobSendChunkSize(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStatements(boolean flag) {
        super.setCacheCallableStatements(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCacheCallableStatements(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCachePreparedStatements(boolean flag) {
        super.setCachePreparedStatements(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCachePreparedStatements(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCacheResultSetMetadata(boolean property) {
        super.setCacheResultSetMetadata(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCacheResultSetMetadata(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCacheServerConfiguration(boolean flag) {
        super.setCacheServerConfiguration(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCacheServerConfiguration(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCallableStatementCacheSize(int size) throws SQLException {
        super.setCallableStatementCacheSize(size);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCallableStatementCacheSize(size);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeDBMDTypes(boolean property) {
        super.setCapitalizeDBMDTypes(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCapitalizeDBMDTypes(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeTypeNames(boolean flag) {
        super.setCapitalizeTypeNames(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCapitalizeTypeNames(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCharacterEncoding(String encoding) {
        super.setCharacterEncoding(encoding);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCharacterEncoding(encoding);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCharacterSetResults(String characterSet) {
        super.setCharacterSetResults(characterSet);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCharacterSetResults(characterSet);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClobberStreamingResults(boolean flag) {
        super.setClobberStreamingResults(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClobberStreamingResults(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClobCharacterEncoding(String encoding) {
        super.setClobCharacterEncoding(encoding);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClobCharacterEncoding(encoding);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setConnectionCollation(String collation) {
        super.setConnectionCollation(collation);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setConnectionCollation(collation);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setConnectTimeout(int timeoutMs) throws SQLException {
        super.setConnectTimeout(timeoutMs);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setConnectTimeout(timeoutMs);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setContinueBatchOnError(boolean property) {
        super.setContinueBatchOnError(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setContinueBatchOnError(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCreateDatabaseIfNotExist(boolean flag) {
        super.setCreateDatabaseIfNotExist(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCreateDatabaseIfNotExist(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDefaultFetchSize(int n) throws SQLException {
        super.setDefaultFetchSize(n);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDefaultFetchSize(n);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDetectServerPreparedStmts(boolean property) {
        super.setDetectServerPreparedStmts(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDetectServerPreparedStmts(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDontTrackOpenResources(boolean flag) {
        super.setDontTrackOpenResources(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDontTrackOpenResources(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDumpQueriesOnException(boolean flag) {
        super.setDumpQueriesOnException(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDumpQueriesOnException(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDynamicCalendars(boolean flag) {
        super.setDynamicCalendars(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDynamicCalendars(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setElideSetAutoCommits(boolean flag) {
        super.setElideSetAutoCommits(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setElideSetAutoCommits(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEmptyStringsConvertToZero(boolean flag) {
        super.setEmptyStringsConvertToZero(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEmptyStringsConvertToZero(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEmulateLocators(boolean property) {
        super.setEmulateLocators(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEmulateLocators(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEmulateUnsupportedPstmts(boolean flag) {
        super.setEmulateUnsupportedPstmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEmulateUnsupportedPstmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEnablePacketDebug(boolean flag) {
        super.setEnablePacketDebug(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEnablePacketDebug(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEncoding(String property) {
        super.setEncoding(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEncoding(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setExplainSlowQueries(boolean flag) {
        super.setExplainSlowQueries(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setExplainSlowQueries(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setFailOverReadOnly(boolean flag) {
        super.setFailOverReadOnly(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setFailOverReadOnly(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setGatherPerformanceMetrics(boolean flag) {
        super.setGatherPerformanceMetrics(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setGatherPerformanceMetrics(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        super.setHoldResultsOpenOverStatementClose(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setHoldResultsOpenOverStatementClose(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setIgnoreNonTxTables(boolean property) {
        super.setIgnoreNonTxTables(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setIgnoreNonTxTables(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setInitialTimeout(int property) throws SQLException {
        super.setInitialTimeout(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setInitialTimeout(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setIsInteractiveClient(boolean property) {
        super.setIsInteractiveClient(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setIsInteractiveClient(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncation(boolean flag) {
        super.setJdbcCompliantTruncation(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setJdbcCompliantTruncation(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLocatorFetchBufferSize(String value) throws SQLException {
        super.setLocatorFetchBufferSize(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLocatorFetchBufferSize(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLogger(String property) {
        super.setLogger(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLogger(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoggerClassName(String className) {
        super.setLoggerClassName(className);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoggerClassName(className);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLogSlowQueries(boolean flag) {
        super.setLogSlowQueries(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLogSlowQueries(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setMaintainTimeStats(boolean flag) {
        super.setMaintainTimeStats(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setMaintainTimeStats(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setMaxQuerySizeToLog(int sizeInBytes) throws SQLException {
        super.setMaxQuerySizeToLog(sizeInBytes);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setMaxQuerySizeToLog(sizeInBytes);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setMaxReconnects(int property) throws SQLException {
        super.setMaxReconnects(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setMaxReconnects(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setMaxRows(int property) throws SQLException {
        super.setMaxRows(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setMaxRows(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setMetadataCacheSize(int value) throws SQLException {
        super.setMetadataCacheSize(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setMetadataCacheSize(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNoDatetimeStringSync(boolean flag) {
        super.setNoDatetimeStringSync(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNoDatetimeStringSync(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNullCatalogMeansCurrent(boolean value) {
        super.setNullCatalogMeansCurrent(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNullCatalogMeansCurrent(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNullNamePatternMatchesAll(boolean value) {
        super.setNullNamePatternMatchesAll(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNullNamePatternMatchesAll(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPacketDebugBufferSize(int size) throws SQLException {
        super.setPacketDebugBufferSize(size);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPacketDebugBufferSize(size);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setParanoid(boolean property) {
        super.setParanoid(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setParanoid(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPedantic(boolean property) {
        super.setPedantic(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPedantic(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSize(int cacheSize) throws SQLException {
        super.setPreparedStatementCacheSize(cacheSize);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPreparedStatementCacheSize(cacheSize);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) throws SQLException {
        super.setPreparedStatementCacheSqlLimit(cacheSqlLimit);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPreparedStatementCacheSqlLimit(cacheSqlLimit);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setProfileSql(boolean property) {
        super.setProfileSql(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setProfileSql(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setProfileSQL(boolean flag) {
        super.setProfileSQL(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setProfileSQL(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPropertiesTransform(String value) {
        super.setPropertiesTransform(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPropertiesTransform(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setQueriesBeforeRetryMaster(int property) throws SQLException {
        super.setQueriesBeforeRetryMaster(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setQueriesBeforeRetryMaster(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setReconnectAtTxEnd(boolean property) {
        super.setReconnectAtTxEnd(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setReconnectAtTxEnd(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRelaxAutoCommit(boolean property) {
        super.setRelaxAutoCommit(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRelaxAutoCommit(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setReportMetricsIntervalMillis(int millis) throws SQLException {
        super.setReportMetricsIntervalMillis(millis);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setReportMetricsIntervalMillis(millis);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRequireSSL(boolean property) {
        super.setRequireSSL(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRequireSSL(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRetainStatementAfterResultSetClose(boolean flag) {
        super.setRetainStatementAfterResultSetClose(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRetainStatementAfterResultSetClose(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRollbackOnPooledClose(boolean flag) {
        super.setRollbackOnPooledClose(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRollbackOnPooledClose(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRoundRobinLoadBalance(boolean flag) {
        super.setRoundRobinLoadBalance(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRoundRobinLoadBalance(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRunningCTS13(boolean flag) {
        super.setRunningCTS13(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRunningCTS13(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSecondsBeforeRetryMaster(int property) throws SQLException {
        super.setSecondsBeforeRetryMaster(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSecondsBeforeRetryMaster(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setServerTimezone(String property) {
        super.setServerTimezone(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setServerTimezone(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSessionVariables(String variables) {
        super.setSessionVariables(variables);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSessionVariables(variables);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdMillis(int millis) throws SQLException {
        super.setSlowQueryThresholdMillis(millis);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSlowQueryThresholdMillis(millis);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSocketFactoryClassName(String property) {
        super.setSocketFactoryClassName(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSocketFactoryClassName(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSocketTimeout(int property) throws SQLException {
        super.setSocketTimeout(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSocketTimeout(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setStrictFloatingPoint(boolean property) {
        super.setStrictFloatingPoint(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setStrictFloatingPoint(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setStrictUpdates(boolean property) {
        super.setStrictUpdates(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setStrictUpdates(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTinyInt1isBit(boolean flag) {
        super.setTinyInt1isBit(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTinyInt1isBit(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTraceProtocol(boolean flag) {
        super.setTraceProtocol(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTraceProtocol(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTransformedBitIsBoolean(boolean flag) {
        super.setTransformedBitIsBoolean(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTransformedBitIsBoolean(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseCompression(boolean property) {
        super.setUseCompression(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseCompression(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseFastIntParsing(boolean flag) {
        super.setUseFastIntParsing(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseFastIntParsing(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseHostsInPrivileges(boolean property) {
        super.setUseHostsInPrivileges(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseHostsInPrivileges(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseInformationSchema(boolean flag) {
        super.setUseInformationSchema(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseInformationSchema(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseLocalSessionState(boolean flag) {
        super.setUseLocalSessionState(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseLocalSessionState(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseOldUTF8Behavior(boolean flag) {
        super.setUseOldUTF8Behavior(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseOldUTF8Behavior(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseOnlyServerErrorMessages(boolean flag) {
        super.setUseOnlyServerErrorMessages(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseOnlyServerErrorMessages(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseReadAheadInput(boolean flag) {
        super.setUseReadAheadInput(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseReadAheadInput(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseServerPreparedStmts(boolean flag) {
        super.setUseServerPreparedStmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseServerPreparedStmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseSqlStateCodes(boolean flag) {
        super.setUseSqlStateCodes(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseSqlStateCodes(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseSSL(boolean property) {
        super.setUseSSL(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseSSL(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseStreamLengthsInPrepStmts(boolean property) {
        super.setUseStreamLengthsInPrepStmts(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseStreamLengthsInPrepStmts(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseTimezone(boolean property) {
        super.setUseTimezone(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseTimezone(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseUltraDevWorkAround(boolean property) {
        super.setUseUltraDevWorkAround(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseUltraDevWorkAround(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseUnbufferedInput(boolean flag) {
        super.setUseUnbufferedInput(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseUnbufferedInput(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseUnicode(boolean flag) {
        super.setUseUnicode(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseUnicode(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        super.setUseUsageAdvisor(useUsageAdvisorFlag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseUsageAdvisor(useUsageAdvisorFlag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setYearIsDateType(boolean flag) {
        super.setYearIsDateType(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setYearIsDateType(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setZeroDateTimeBehavior(String behavior) {
        super.setZeroDateTimeBehavior(behavior);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setZeroDateTimeBehavior(behavior);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseCursorFetch(boolean flag) {
        super.setUseCursorFetch(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseCursorFetch(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        super.setOverrideSupportsIntegrityEnhancementFacility(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setOverrideSupportsIntegrityEnhancementFacility(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNoTimezoneConversionForTimeType(boolean flag) {
        super.setNoTimezoneConversionForTimeType(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNoTimezoneConversionForTimeType(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        super.setUseJDBCCompliantTimezoneShift(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseJDBCCompliantTimezoneShift(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoClosePStmtStreams(boolean flag) {
        super.setAutoClosePStmtStreams(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoClosePStmtStreams(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        super.setProcessEscapeCodesForPrepStmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setProcessEscapeCodesForPrepStmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseGmtMillisForDatetimes(boolean flag) {
        super.setUseGmtMillisForDatetimes(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseGmtMillisForDatetimes(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        super.setDumpMetadataOnColumnNotFound(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDumpMetadataOnColumnNotFound(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setResourceId(String resourceId) {
        super.setResourceId(resourceId);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setResourceId(resourceId);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRewriteBatchedStatements(boolean flag) {
        super.setRewriteBatchedStatements(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRewriteBatchedStatements(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        super.setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseJvmCharsetConverters(boolean flag) {
        super.setUseJvmCharsetConverters(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseJvmCharsetConverters(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        super.setPinGlobalTxToPhysicalConnection(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPinGlobalTxToPhysicalConnection(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setGatherPerfMetrics(boolean flag) {
        super.setGatherPerfMetrics(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setGatherPerfMetrics(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUltraDevHack(boolean flag) {
        super.setUltraDevHack(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUltraDevHack(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setInteractiveClient(boolean property) {
        super.setInteractiveClient(property);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setInteractiveClient(property);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSocketFactory(String name) {
        super.setSocketFactory(name);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSocketFactory(name);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseServerPrepStmts(boolean flag) {
        super.setUseServerPrepStmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseServerPrepStmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStmts(boolean flag) {
        super.setCacheCallableStmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCacheCallableStmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCachePrepStmts(boolean flag) {
        super.setCachePrepStmts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCachePrepStmts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCallableStmtCacheSize(int cacheSize) throws SQLException {
        super.setCallableStmtCacheSize(cacheSize);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCallableStmtCacheSize(cacheSize);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSize(int cacheSize) throws SQLException {
        super.setPrepStmtCacheSize(cacheSize);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPrepStmtCacheSize(cacheSize);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSqlLimit(int sqlLimit) throws SQLException {
        super.setPrepStmtCacheSqlLimit(sqlLimit);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPrepStmtCacheSqlLimit(sqlLimit);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNoAccessToProcedureBodies(boolean flag) {
        super.setNoAccessToProcedureBodies(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNoAccessToProcedureBodies(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseOldAliasMetadataBehavior(boolean flag) {
        super.setUseOldAliasMetadataBehavior(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseOldAliasMetadataBehavior(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStorePassword(String value) {
        super.setClientCertificateKeyStorePassword(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClientCertificateKeyStorePassword(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreType(String value) {
        super.setClientCertificateKeyStoreType(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClientCertificateKeyStoreType(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreUrl(String value) {
        super.setClientCertificateKeyStoreUrl(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClientCertificateKeyStoreUrl(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStorePassword(String value) {
        super.setTrustCertificateKeyStorePassword(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTrustCertificateKeyStorePassword(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreType(String value) {
        super.setTrustCertificateKeyStoreType(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTrustCertificateKeyStoreType(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreUrl(String value) {
        super.setTrustCertificateKeyStoreUrl(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTrustCertificateKeyStoreUrl(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        super.setUseSSPSCompatibleTimezoneShift(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseSSPSCompatibleTimezoneShift(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTreatUtilDateAsTimestamp(boolean flag) {
        super.setTreatUtilDateAsTimestamp(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTreatUtilDateAsTimestamp(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseFastDateParsing(boolean flag) {
        super.setUseFastDateParsing(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseFastDateParsing(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLocalSocketAddress(String address) {
        super.setLocalSocketAddress(address);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLocalSocketAddress(address);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseConfigs(String configs) {
        super.setUseConfigs(configs);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseConfigs(configs);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setGenerateSimpleParameterMetadata(boolean flag) {
        super.setGenerateSimpleParameterMetadata(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setGenerateSimpleParameterMetadata(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLogXaCommands(boolean flag) {
        super.setLogXaCommands(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLogXaCommands(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setResultSetSizeThreshold(int threshold) throws SQLException {
        super.setResultSetSizeThreshold(threshold);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setResultSetSizeThreshold(threshold);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setNetTimeoutForStreamingResults(int value) throws SQLException {
        super.setNetTimeoutForStreamingResults(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setNetTimeoutForStreamingResults(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setEnableQueryTimeouts(boolean flag) {
        super.setEnableQueryTimeouts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setEnableQueryTimeouts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPadCharsWithSpace(boolean flag) {
        super.setPadCharsWithSpace(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPadCharsWithSpace(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseDynamicCharsetInfo(boolean flag) {
        super.setUseDynamicCharsetInfo(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseDynamicCharsetInfo(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setClientInfoProvider(String classname) {
        super.setClientInfoProvider(classname);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setClientInfoProvider(classname);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        super.setPopulateInsertRowWithDefaultValues(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPopulateInsertRowWithDefaultValues(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceStrategy(String strategy) {
        super.setLoadBalanceStrategy(strategy);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceStrategy(strategy);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTcpNoDelay(boolean flag) {
        super.setTcpNoDelay(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTcpNoDelay(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTcpKeepAlive(boolean flag) {
        super.setTcpKeepAlive(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTcpKeepAlive(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTcpRcvBuf(int bufSize) throws SQLException {
        super.setTcpRcvBuf(bufSize);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTcpRcvBuf(bufSize);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTcpSndBuf(int bufSize) throws SQLException {
        super.setTcpSndBuf(bufSize);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTcpSndBuf(bufSize);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setTcpTrafficClass(int classFlags) throws SQLException {
        super.setTcpTrafficClass(classFlags);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setTcpTrafficClass(classFlags);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseNanosForElapsedTime(boolean flag) {
        super.setUseNanosForElapsedTime(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseNanosForElapsedTime(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdNanos(long nanos) throws SQLException {
        super.setSlowQueryThresholdNanos(nanos);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSlowQueryThresholdNanos(nanos);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setStatementInterceptors(String value) {
        super.setStatementInterceptors(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setStatementInterceptors(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseDirectRowUnpack(boolean flag) {
        super.setUseDirectRowUnpack(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseDirectRowUnpack(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLargeRowSizeThreshold(String value) throws SQLException {
        super.setLargeRowSizeThreshold(value);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLargeRowSizeThreshold(value);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        super.setUseBlobToStoreUTF8OutsideBMP(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseBlobToStoreUTF8OutsideBMP(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        super.setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        super.setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        super.setIncludeInnodbStatusInDeadlockExceptions(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setIncludeInnodbStatusInDeadlockExceptions(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        super.setIncludeThreadDumpInDeadlockExceptions(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setIncludeThreadDumpInDeadlockExceptions(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        super.setIncludeThreadNamesAsStatementComment(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setIncludeThreadNamesAsStatementComment(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setBlobsAreStrings(boolean flag) {
        super.setBlobsAreStrings(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setBlobsAreStrings(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setFunctionsNeverReturnBlobs(boolean flag) {
        super.setFunctionsNeverReturnBlobs(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setFunctionsNeverReturnBlobs(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAutoSlowLog(boolean flag) {
        super.setAutoSlowLog(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAutoSlowLog(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setConnectionLifecycleInterceptors(String interceptors) {
        super.setConnectionLifecycleInterceptors(interceptors);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setConnectionLifecycleInterceptors(interceptors);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setProfilerEventHandler(String handler) {
        super.setProfilerEventHandler(handler);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setProfilerEventHandler(handler);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setVerifyServerCertificate(boolean flag) {
        super.setVerifyServerCertificate(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setVerifyServerCertificate(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseLegacyDatetimeCode(boolean flag) {
        super.setUseLegacyDatetimeCode(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseLegacyDatetimeCode(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingSecondsLifetime(int seconds) throws SQLException {
        super.setSelfDestructOnPingSecondsLifetime(seconds);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSelfDestructOnPingSecondsLifetime(seconds);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingMaxOperations(int maxOperations) throws SQLException {
        super.setSelfDestructOnPingMaxOperations(maxOperations);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setSelfDestructOnPingMaxOperations(maxOperations);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseColumnNamesInFindColumn(boolean flag) {
        super.setUseColumnNamesInFindColumn(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseColumnNamesInFindColumn(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseLocalTransactionState(boolean flag) {
        super.setUseLocalTransactionState(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseLocalTransactionState(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        super.setCompensateOnDuplicateKeyUpdateCounts(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setCompensateOnDuplicateKeyUpdateCounts(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setUseAffectedRows(boolean flag) {
        super.setUseAffectedRows(flag);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setUseAffectedRows(flag);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setPasswordCharacterEncoding(String characterSet) {
        super.setPasswordCharacterEncoding(characterSet);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setPasswordCharacterEncoding(characterSet);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
        super.setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setRetriesAllDown(int retriesAllDown) throws SQLException {
        super.setRetriesAllDown(retriesAllDown);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setRetriesAllDown(retriesAllDown);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setExceptionInterceptors(String exceptionInterceptors) {
        super.setExceptionInterceptors(exceptionInterceptors);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setExceptionInterceptors(exceptionInterceptors);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        super.setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException {
        super.setLoadBalancePingTimeout(loadBalancePingTimeout);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalancePingTimeout(loadBalancePingTimeout);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        super.setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        super.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        super.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        super.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        super.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        super.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) throws SQLException {
        super.setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        super.setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setAuthenticationPlugins(String authenticationPlugins) {
        super.setAuthenticationPlugins(authenticationPlugins);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setAuthenticationPlugins(authenticationPlugins);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        super.setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        super.setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setParseInfoCacheFactory(String factoryClassname) {
        super.setParseInfoCacheFactory(factoryClassname);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setParseInfoCacheFactory(factoryClassname);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setServerConfigCacheFactory(String factoryClassname) {
        super.setServerConfigCacheFactory(factoryClassname);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setServerConfigCacheFactory(factoryClassname);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        super.setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
        for (ReplicationConnection cp : this.serverConnections.values()) {
            cp.setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
        }
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties
    public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions) {
        super.setGetProceduresReturnsFunctions(getProcedureReturnsFunctions);
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getActiveStatementCount() {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public long getIdleFor() {
        return -1L;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Log getLog() {
        return this.log;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isMasterConnection() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isNoBackslashEscapesSet() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isSameResource(Connection c) {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean parserKnowsUnicode() {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public void ping() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public void resetServerState() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public void setFailedOver(boolean flag) {
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void setPreferSlaveDuringFailover(boolean flag) {
    }

    @Override // com.mysql.jdbc.Connection
    public void setStatementComment(String comment) {
    }

    @Override // com.mysql.jdbc.Connection
    public void reportQueryTime(long millisOrNanos) {
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        return false;
    }

    @Override // com.mysql.jdbc.Connection
    public void initializeExtension(Extension ex) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getAutoIncrementIncrement() {
        return -1;
    }

    @Override // com.mysql.jdbc.Connection
    public boolean hasSameProperties(Connection c) {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Properties getProperties() {
        return null;
    }

    @Override // com.mysql.jdbc.Connection
    public void setSchema(String schema) throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public String getSchema() throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.Connection
    public void abort(Executor executor) throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public int getNetworkTimeout() throws SQLException {
        return -1;
    }

    @Override // com.mysql.jdbc.Connection
    public void checkClosed() throws SQLException {
    }

    @Override // com.mysql.jdbc.Connection
    public Object getConnectionMutex() {
        return this;
    }

    @Override // com.mysql.jdbc.Connection
    public void setSessionMaxRows(int max) throws SQLException {
        for (ReplicationConnection c : this.serverConnections.values()) {
            c.setSessionMaxRows(max);
        }
    }

    @Override // com.mysql.jdbc.Connection
    public int getSessionMaxRows() {
        return getActiveConnectionPassive().getSessionMaxRows();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isProxySet() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Connection duplicate() throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public CachedResultSetMetaData getCachedMetaData(String sql) {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Timer getCancelTimer() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        return getEncodingForIndex(charsetIndex);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getEncodingForIndex(int charsetIndex) throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public TimeZone getDefaultTimeZone() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getErrorMessageEncoding() {
        return null;
    }

    @Override // com.mysql.jdbc.ConnectionPropertiesImpl, com.mysql.jdbc.ConnectionProperties, com.mysql.jdbc.MySQLConnection
    public ExceptionInterceptor getExceptionInterceptor() {
        if (this.currentConnection == null) {
            return null;
        }
        return this.currentConnection.getExceptionInterceptor();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getHost() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getHostPortPair() {
        return getActiveMySQLConnection().getHostPortPair();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public long getId() {
        return -1L;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getNetBufferLength() {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean getRequiresEscapingEncoder() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMajorVersion() {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMinorVersion() {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerSubMinorVersion() {
        return -1;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVariable(String variableName) {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVersion() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getSessionLockedCalendar() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getStatementComment() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getURL() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getUser() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getUtcCalendar() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPreparedExecutes() {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPrepares() {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfResultSetsCreated() {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeSafeStatementInterceptors() throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isClientTzUTC() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isCursorFetchEnabled() throws SQLException {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isReadInfoMsgEnabled() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isServerTzUTC() {
        return false;
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean lowerCaseTableNames() {
        return getActiveMySQLConnection().lowerCaseTableNames();
    }

    public void maxRowsChanged(com.mysql.jdbc.Statement stmt) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerQueryExecutionTime(long queryTimeMs) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerStatement(com.mysql.jdbc.Statement stmt) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean serverSupportsConvertFn() throws SQLException {
        return getActiveMySQLConnectionChecked().serverSupportsConvertFn();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadInfoMsgEnabled(boolean flag) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean storesLowerCaseTableName() {
        return getActiveMySQLConnection().storesLowerCaseTableName();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void throwConnectionClosedException() throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unregisterStatement(com.mysql.jdbc.Statement stmt) {
    }

    public void unsetMaxRows(com.mysql.jdbc.Statement stmt) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean useAnsiQuotedIdentifiers() {
        return false;
    }

    public boolean useMaxRows() {
        return false;
    }

    @Override // java.sql.Connection
    public void clearWarnings() {
    }

    @Override // java.sql.Connection
    public Properties getClientInfo() {
        return null;
    }

    @Override // java.sql.Connection
    public String getClientInfo(String name) {
        return null;
    }

    @Override // java.sql.Connection
    public int getHoldability() {
        return -1;
    }

    @Override // java.sql.Connection
    public int getTransactionIsolation() {
        return -1;
    }

    @Override // java.sql.Connection
    public Map<String, Class<?>> getTypeMap() {
        return null;
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        return getActiveMySQLConnectionChecked().getWarnings();
    }

    @Override // java.sql.Connection
    public String nativeSQL(String sql) throws SQLException {
        return getActiveMySQLConnectionChecked().nativeSQL(sql);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ProfilerEventHandler getProfilerEventHandlerInstance() {
        return null;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setProfilerEventHandlerInstance(ProfilerEventHandler h) {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void decachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isServerTruncatesFracSecs() {
        return getActiveMySQLConnection().isServerTruncatesFracSecs();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getQueryTimingUnits() {
        return getActiveMySQLConnection().getQueryTimingUnits();
    }
}
