package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MultiHostMySQLConnection.class */
public class MultiHostMySQLConnection implements MySQLConnection {
    protected MultiHostConnectionProxy thisAsProxy;

    public MultiHostMySQLConnection(MultiHostConnectionProxy proxy) {
        this.thisAsProxy = proxy;
    }

    protected MultiHostConnectionProxy getThisAsProxy() {
        return this.thisAsProxy;
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getActiveMySQLConnection() {
        MySQLConnection mySQLConnection;
        synchronized (this.thisAsProxy) {
            mySQLConnection = this.thisAsProxy.currentConnection;
        }
        return mySQLConnection;
    }

    @Override // com.mysql.jdbc.Connection
    public void abortInternal() throws SQLException {
        getActiveMySQLConnection().abortInternal();
    }

    @Override // com.mysql.jdbc.Connection
    public void changeUser(String userName, String newPassword) throws SQLException {
        getActiveMySQLConnection().changeUser(userName, newPassword);
    }

    @Override // com.mysql.jdbc.Connection
    public void checkClosed() throws SQLException {
        getActiveMySQLConnection().checkClosed();
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void clearHasTriedMaster() {
        getActiveMySQLConnection().clearHasTriedMaster();
    }

    @Override // java.sql.Connection
    public void clearWarnings() throws SQLException {
        getActiveMySQLConnection().clearWarnings();
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndex);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyIndexes);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql, autoGenKeyColNames);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().clientPrepareStatement(sql);
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        getActiveMySQLConnection().close();
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        getActiveMySQLConnection().commit();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void createNewIO(boolean isForReconnect) throws SQLException {
        getActiveMySQLConnection().createNewIO(isForReconnect);
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement() throws SQLException {
        return getActiveMySQLConnection().createStatement();
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // java.sql.Connection
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void dumpTestcaseQuery(String query) {
        getActiveMySQLConnection().dumpTestcaseQuery(query);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Connection duplicate() throws SQLException {
        return getActiveMySQLConnection().duplicate();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata, boolean isBatch) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata, isBatch);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ResultSetInternalMethods execSQL(StatementImpl callingStatement, String sql, int maxRows, Buffer packet, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata) throws SQLException {
        return getActiveMySQLConnection().execSQL(callingStatement, sql, maxRows, packet, resultSetType, resultSetConcurrency, streamResults, catalog, cachedMetadata);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String extractSqlFromPacket(String possibleSqlQuery, Buffer queryPacket, int endOfQueryPacketPosition) throws SQLException {
        return getActiveMySQLConnection().extractSqlFromPacket(possibleSqlQuery, queryPacket, endOfQueryPacketPosition);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String exposeAsXml() throws SQLException {
        return getActiveMySQLConnection().exposeAsXml();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowLoadLocalInfile() {
        return getActiveMySQLConnection().getAllowLoadLocalInfile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowMultiQueries() {
        return getActiveMySQLConnection().getAllowMultiQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowNanAndInf() {
        return getActiveMySQLConnection().getAllowNanAndInf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowUrlInLocalInfile() {
        return getActiveMySQLConnection().getAllowUrlInLocalInfile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAlwaysSendSetIsolation() {
        return getActiveMySQLConnection().getAlwaysSendSetIsolation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoClosePStmtStreams() {
        return getActiveMySQLConnection().getAutoClosePStmtStreams();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoDeserialize() {
        return getActiveMySQLConnection().getAutoDeserialize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoGenerateTestcaseScript() {
        return getActiveMySQLConnection().getAutoGenerateTestcaseScript();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoReconnectForPools() {
        return getActiveMySQLConnection().getAutoReconnectForPools();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoSlowLog() {
        return getActiveMySQLConnection().getAutoSlowLog();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getBlobSendChunkSize() {
        return getActiveMySQLConnection().getBlobSendChunkSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getBlobsAreStrings() {
        return getActiveMySQLConnection().getBlobsAreStrings();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheCallableStatements() {
        return getActiveMySQLConnection().getCacheCallableStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheCallableStmts() {
        return getActiveMySQLConnection().getCacheCallableStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCachePrepStmts() {
        return getActiveMySQLConnection().getCachePrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCachePreparedStatements() {
        return getActiveMySQLConnection().getCachePreparedStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheResultSetMetadata() {
        return getActiveMySQLConnection().getCacheResultSetMetadata();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheServerConfiguration() {
        return getActiveMySQLConnection().getCacheServerConfiguration();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getCallableStatementCacheSize() {
        return getActiveMySQLConnection().getCallableStatementCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getCallableStmtCacheSize() {
        return getActiveMySQLConnection().getCallableStmtCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCapitalizeTypeNames() {
        return getActiveMySQLConnection().getCapitalizeTypeNames();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getCharacterSetResults() {
        return getActiveMySQLConnection().getCharacterSetResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStorePassword() {
        return getActiveMySQLConnection().getClientCertificateKeyStorePassword();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStoreType() {
        return getActiveMySQLConnection().getClientCertificateKeyStoreType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStoreUrl() {
        return getActiveMySQLConnection().getClientCertificateKeyStoreUrl();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientInfoProvider() {
        return getActiveMySQLConnection().getClientInfoProvider();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClobCharacterEncoding() {
        return getActiveMySQLConnection().getClobCharacterEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getClobberStreamingResults() {
        return getActiveMySQLConnection().getClobberStreamingResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCompensateOnDuplicateKeyUpdateCounts() {
        return getActiveMySQLConnection().getCompensateOnDuplicateKeyUpdateCounts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getConnectTimeout() {
        return getActiveMySQLConnection().getConnectTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getConnectionCollation() {
        return getActiveMySQLConnection().getConnectionCollation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getConnectionLifecycleInterceptors() {
        return getActiveMySQLConnection().getConnectionLifecycleInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getContinueBatchOnError() {
        return getActiveMySQLConnection().getContinueBatchOnError();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCreateDatabaseIfNotExist() {
        return getActiveMySQLConnection().getCreateDatabaseIfNotExist();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getDefaultFetchSize() {
        return getActiveMySQLConnection().getDefaultFetchSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDontTrackOpenResources() {
        return getActiveMySQLConnection().getDontTrackOpenResources();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDumpMetadataOnColumnNotFound() {
        return getActiveMySQLConnection().getDumpMetadataOnColumnNotFound();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDumpQueriesOnException() {
        return getActiveMySQLConnection().getDumpQueriesOnException();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDynamicCalendars() {
        return getActiveMySQLConnection().getDynamicCalendars();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getElideSetAutoCommits() {
        return getActiveMySQLConnection().getElideSetAutoCommits();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmptyStringsConvertToZero() {
        return getActiveMySQLConnection().getEmptyStringsConvertToZero();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmulateLocators() {
        return getActiveMySQLConnection().getEmulateLocators();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmulateUnsupportedPstmts() {
        return getActiveMySQLConnection().getEmulateUnsupportedPstmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnablePacketDebug() {
        return getActiveMySQLConnection().getEnablePacketDebug();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnableQueryTimeouts() {
        return getActiveMySQLConnection().getEnableQueryTimeouts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEncoding() {
        return getActiveMySQLConnection().getEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getExceptionInterceptors() {
        return getActiveMySQLConnection().getExceptionInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getExplainSlowQueries() {
        return getActiveMySQLConnection().getExplainSlowQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getFailOverReadOnly() {
        return getActiveMySQLConnection().getFailOverReadOnly();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getFunctionsNeverReturnBlobs() {
        return getActiveMySQLConnection().getFunctionsNeverReturnBlobs();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGatherPerfMetrics() {
        return getActiveMySQLConnection().getGatherPerfMetrics();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGatherPerformanceMetrics() {
        return getActiveMySQLConnection().getGatherPerformanceMetrics();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGenerateSimpleParameterMetadata() {
        return getActiveMySQLConnection().getGenerateSimpleParameterMetadata();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIgnoreNonTxTables() {
        return getActiveMySQLConnection().getIgnoreNonTxTables();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeInnodbStatusInDeadlockExceptions() {
        return getActiveMySQLConnection().getIncludeInnodbStatusInDeadlockExceptions();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getInitialTimeout() {
        return getActiveMySQLConnection().getInitialTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getInteractiveClient() {
        return getActiveMySQLConnection().getInteractiveClient();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIsInteractiveClient() {
        return getActiveMySQLConnection().getIsInteractiveClient();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getJdbcCompliantTruncation() {
        return getActiveMySQLConnection().getJdbcCompliantTruncation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getJdbcCompliantTruncationForReads() {
        return getActiveMySQLConnection().getJdbcCompliantTruncationForReads();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLargeRowSizeThreshold() {
        return getActiveMySQLConnection().getLargeRowSizeThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceBlacklistTimeout() {
        return getActiveMySQLConnection().getLoadBalanceBlacklistTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalancePingTimeout() {
        return getActiveMySQLConnection().getLoadBalancePingTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceStrategy() {
        return getActiveMySQLConnection().getLoadBalanceStrategy();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerAffinityOrder() {
        return getActiveMySQLConnection().getServerAffinityOrder();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLoadBalanceValidateConnectionOnSwapServer() {
        return getActiveMySQLConnection().getLoadBalanceValidateConnectionOnSwapServer();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLocalSocketAddress() {
        return getActiveMySQLConnection().getLocalSocketAddress();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLocatorFetchBufferSize() {
        return getActiveMySQLConnection().getLocatorFetchBufferSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLogSlowQueries() {
        return getActiveMySQLConnection().getLogSlowQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLogXaCommands() {
        return getActiveMySQLConnection().getLogXaCommands();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLogger() {
        return getActiveMySQLConnection().getLogger();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoggerClassName() {
        return getActiveMySQLConnection().getLoggerClassName();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getMaintainTimeStats() {
        return getActiveMySQLConnection().getMaintainTimeStats();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxAllowedPacket() {
        return getActiveMySQLConnection().getMaxAllowedPacket();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxQuerySizeToLog() {
        return getActiveMySQLConnection().getMaxQuerySizeToLog();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxReconnects() {
        return getActiveMySQLConnection().getMaxReconnects();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxRows() {
        return getActiveMySQLConnection().getMaxRows();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMetadataCacheSize() {
        return getActiveMySQLConnection().getMetadataCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getNetTimeoutForStreamingResults() {
        return getActiveMySQLConnection().getNetTimeoutForStreamingResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoAccessToProcedureBodies() {
        return getActiveMySQLConnection().getNoAccessToProcedureBodies();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoDatetimeStringSync() {
        return getActiveMySQLConnection().getNoDatetimeStringSync();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoTimezoneConversionForTimeType() {
        return getActiveMySQLConnection().getNoTimezoneConversionForTimeType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoTimezoneConversionForDateType() {
        return getActiveMySQLConnection().getNoTimezoneConversionForDateType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheDefaultTimezone() {
        return getActiveMySQLConnection().getCacheDefaultTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNullCatalogMeansCurrent() {
        return getActiveMySQLConnection().getNullCatalogMeansCurrent();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNullNamePatternMatchesAll() {
        return getActiveMySQLConnection().getNullNamePatternMatchesAll();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getOverrideSupportsIntegrityEnhancementFacility() {
        return getActiveMySQLConnection().getOverrideSupportsIntegrityEnhancementFacility();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPacketDebugBufferSize() {
        return getActiveMySQLConnection().getPacketDebugBufferSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPadCharsWithSpace() {
        return getActiveMySQLConnection().getPadCharsWithSpace();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getParanoid() {
        return getActiveMySQLConnection().getParanoid();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getPasswordCharacterEncoding() {
        return getActiveMySQLConnection().getPasswordCharacterEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPedantic() {
        return getActiveMySQLConnection().getPedantic();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPinGlobalTxToPhysicalConnection() {
        return getActiveMySQLConnection().getPinGlobalTxToPhysicalConnection();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPopulateInsertRowWithDefaultValues() {
        return getActiveMySQLConnection().getPopulateInsertRowWithDefaultValues();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPrepStmtCacheSize() {
        return getActiveMySQLConnection().getPrepStmtCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPrepStmtCacheSqlLimit() {
        return getActiveMySQLConnection().getPrepStmtCacheSqlLimit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPreparedStatementCacheSize() {
        return getActiveMySQLConnection().getPreparedStatementCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPreparedStatementCacheSqlLimit() {
        return getActiveMySQLConnection().getPreparedStatementCacheSqlLimit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProcessEscapeCodesForPrepStmts() {
        return getActiveMySQLConnection().getProcessEscapeCodesForPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProfileSQL() {
        return getActiveMySQLConnection().getProfileSQL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProfileSql() {
        return getActiveMySQLConnection().getProfileSql();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getProfilerEventHandler() {
        return getActiveMySQLConnection().getProfilerEventHandler();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getPropertiesTransform() {
        return getActiveMySQLConnection().getPropertiesTransform();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getQueriesBeforeRetryMaster() {
        return getActiveMySQLConnection().getQueriesBeforeRetryMaster();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getQueryTimeoutKillsConnection() {
        return getActiveMySQLConnection().getQueryTimeoutKillsConnection();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReconnectAtTxEnd() {
        return getActiveMySQLConnection().getReconnectAtTxEnd();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRelaxAutoCommit() {
        return getActiveMySQLConnection().getRelaxAutoCommit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getReportMetricsIntervalMillis() {
        return getActiveMySQLConnection().getReportMetricsIntervalMillis();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRequireSSL() {
        return getActiveMySQLConnection().getRequireSSL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getResourceId() {
        return getActiveMySQLConnection().getResourceId();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getResultSetSizeThreshold() {
        return getActiveMySQLConnection().getResultSetSizeThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRetainStatementAfterResultSetClose() {
        return getActiveMySQLConnection().getRetainStatementAfterResultSetClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getRetriesAllDown() {
        return getActiveMySQLConnection().getRetriesAllDown();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRewriteBatchedStatements() {
        return getActiveMySQLConnection().getRewriteBatchedStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRollbackOnPooledClose() {
        return getActiveMySQLConnection().getRollbackOnPooledClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRoundRobinLoadBalance() {
        return getActiveMySQLConnection().getRoundRobinLoadBalance();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRunningCTS13() {
        return getActiveMySQLConnection().getRunningCTS13();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSecondsBeforeRetryMaster() {
        return getActiveMySQLConnection().getSecondsBeforeRetryMaster();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSelfDestructOnPingMaxOperations() {
        return getActiveMySQLConnection().getSelfDestructOnPingMaxOperations();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSelfDestructOnPingSecondsLifetime() {
        return getActiveMySQLConnection().getSelfDestructOnPingSecondsLifetime();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerTimezone() {
        return getActiveMySQLConnection().getServerTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSessionVariables() {
        return getActiveMySQLConnection().getSessionVariables();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSlowQueryThresholdMillis() {
        return getActiveMySQLConnection().getSlowQueryThresholdMillis();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public long getSlowQueryThresholdNanos() {
        return getActiveMySQLConnection().getSlowQueryThresholdNanos();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocketFactory() {
        return getActiveMySQLConnection().getSocketFactory();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocketFactoryClassName() {
        return getActiveMySQLConnection().getSocketFactoryClassName();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSocketTimeout() {
        return getActiveMySQLConnection().getSocketTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getStatementInterceptors() {
        return getActiveMySQLConnection().getStatementInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getStrictFloatingPoint() {
        return getActiveMySQLConnection().getStrictFloatingPoint();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getStrictUpdates() {
        return getActiveMySQLConnection().getStrictUpdates();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTcpKeepAlive() {
        return getActiveMySQLConnection().getTcpKeepAlive();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTcpNoDelay() {
        return getActiveMySQLConnection().getTcpNoDelay();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpRcvBuf() {
        return getActiveMySQLConnection().getTcpRcvBuf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpSndBuf() {
        return getActiveMySQLConnection().getTcpSndBuf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpTrafficClass() {
        return getActiveMySQLConnection().getTcpTrafficClass();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTinyInt1isBit() {
        return getActiveMySQLConnection().getTinyInt1isBit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTraceProtocol() {
        return getActiveMySQLConnection().getTraceProtocol();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTransformedBitIsBoolean() {
        return getActiveMySQLConnection().getTransformedBitIsBoolean();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTreatUtilDateAsTimestamp() {
        return getActiveMySQLConnection().getTreatUtilDateAsTimestamp();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStorePassword() {
        return getActiveMySQLConnection().getTrustCertificateKeyStorePassword();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStoreType() {
        return getActiveMySQLConnection().getTrustCertificateKeyStoreType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStoreUrl() {
        return getActiveMySQLConnection().getTrustCertificateKeyStoreUrl();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUltraDevHack() {
        return getActiveMySQLConnection().getUltraDevHack();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseAffectedRows() {
        return getActiveMySQLConnection().getUseAffectedRows();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseBlobToStoreUTF8OutsideBMP() {
        return getActiveMySQLConnection().getUseBlobToStoreUTF8OutsideBMP();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseColumnNamesInFindColumn() {
        return getActiveMySQLConnection().getUseColumnNamesInFindColumn();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseCompression() {
        return getActiveMySQLConnection().getUseCompression();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUseConfigs() {
        return getActiveMySQLConnection().getUseConfigs();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseCursorFetch() {
        return getActiveMySQLConnection().getUseCursorFetch();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseDirectRowUnpack() {
        return getActiveMySQLConnection().getUseDirectRowUnpack();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseDynamicCharsetInfo() {
        return getActiveMySQLConnection().getUseDynamicCharsetInfo();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseFastDateParsing() {
        return getActiveMySQLConnection().getUseFastDateParsing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseFastIntParsing() {
        return getActiveMySQLConnection().getUseFastIntParsing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseGmtMillisForDatetimes() {
        return getActiveMySQLConnection().getUseGmtMillisForDatetimes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseHostsInPrivileges() {
        return getActiveMySQLConnection().getUseHostsInPrivileges();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseInformationSchema() {
        return getActiveMySQLConnection().getUseInformationSchema();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseJDBCCompliantTimezoneShift() {
        return getActiveMySQLConnection().getUseJDBCCompliantTimezoneShift();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseJvmCharsetConverters() {
        return getActiveMySQLConnection().getUseJvmCharsetConverters();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLegacyDatetimeCode() {
        return getActiveMySQLConnection().getUseLegacyDatetimeCode();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getSendFractionalSeconds() {
        return getActiveMySQLConnection().getSendFractionalSeconds();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLocalSessionState() {
        return getActiveMySQLConnection().getUseLocalSessionState();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLocalTransactionState() {
        return getActiveMySQLConnection().getUseLocalTransactionState();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseNanosForElapsedTime() {
        return getActiveMySQLConnection().getUseNanosForElapsedTime();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOldAliasMetadataBehavior() {
        return getActiveMySQLConnection().getUseOldAliasMetadataBehavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOldUTF8Behavior() {
        return getActiveMySQLConnection().getUseOldUTF8Behavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOnlyServerErrorMessages() {
        return getActiveMySQLConnection().getUseOnlyServerErrorMessages();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseReadAheadInput() {
        return getActiveMySQLConnection().getUseReadAheadInput();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSSL() {
        return getActiveMySQLConnection().getUseSSL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSSPSCompatibleTimezoneShift() {
        return getActiveMySQLConnection().getUseSSPSCompatibleTimezoneShift();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseServerPrepStmts() {
        return getActiveMySQLConnection().getUseServerPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseServerPreparedStmts() {
        return getActiveMySQLConnection().getUseServerPreparedStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSqlStateCodes() {
        return getActiveMySQLConnection().getUseSqlStateCodes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseStreamLengthsInPrepStmts() {
        return getActiveMySQLConnection().getUseStreamLengthsInPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseTimezone() {
        return getActiveMySQLConnection().getUseTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUltraDevWorkAround() {
        return getActiveMySQLConnection().getUseUltraDevWorkAround();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUnbufferedInput() {
        return getActiveMySQLConnection().getUseUnbufferedInput();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUnicode() {
        return getActiveMySQLConnection().getUseUnicode();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUsageAdvisor() {
        return getActiveMySQLConnection().getUseUsageAdvisor();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUtf8OutsideBmpExcludedColumnNamePattern() {
        return getActiveMySQLConnection().getUtf8OutsideBmpExcludedColumnNamePattern();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUtf8OutsideBmpIncludedColumnNamePattern() {
        return getActiveMySQLConnection().getUtf8OutsideBmpIncludedColumnNamePattern();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getVerifyServerCertificate() {
        return getActiveMySQLConnection().getVerifyServerCertificate();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getYearIsDateType() {
        return getActiveMySQLConnection().getYearIsDateType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getZeroDateTimeBehavior() {
        return getActiveMySQLConnection().getZeroDateTimeBehavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowLoadLocalInfile(boolean property) {
        getActiveMySQLConnection().setAllowLoadLocalInfile(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowMultiQueries(boolean property) {
        getActiveMySQLConnection().setAllowMultiQueries(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowNanAndInf(boolean flag) {
        getActiveMySQLConnection().setAllowNanAndInf(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowUrlInLocalInfile(boolean flag) {
        getActiveMySQLConnection().setAllowUrlInLocalInfile(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAlwaysSendSetIsolation(boolean flag) {
        getActiveMySQLConnection().setAlwaysSendSetIsolation(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoClosePStmtStreams(boolean flag) {
        getActiveMySQLConnection().setAutoClosePStmtStreams(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoDeserialize(boolean flag) {
        getActiveMySQLConnection().setAutoDeserialize(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoGenerateTestcaseScript(boolean flag) {
        getActiveMySQLConnection().setAutoGenerateTestcaseScript(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnect(boolean flag) {
        getActiveMySQLConnection().setAutoReconnect(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForConnectionPools(boolean property) {
        getActiveMySQLConnection().setAutoReconnectForConnectionPools(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForPools(boolean flag) {
        getActiveMySQLConnection().setAutoReconnectForPools(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoSlowLog(boolean flag) {
        getActiveMySQLConnection().setAutoSlowLog(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setBlobSendChunkSize(String value) throws SQLException {
        getActiveMySQLConnection().setBlobSendChunkSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setBlobsAreStrings(boolean flag) {
        getActiveMySQLConnection().setBlobsAreStrings(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStatements(boolean flag) {
        getActiveMySQLConnection().setCacheCallableStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStmts(boolean flag) {
        getActiveMySQLConnection().setCacheCallableStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCachePrepStmts(boolean flag) {
        getActiveMySQLConnection().setCachePrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCachePreparedStatements(boolean flag) {
        getActiveMySQLConnection().setCachePreparedStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheResultSetMetadata(boolean property) {
        getActiveMySQLConnection().setCacheResultSetMetadata(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheServerConfiguration(boolean flag) {
        getActiveMySQLConnection().setCacheServerConfiguration(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCallableStatementCacheSize(int size) throws SQLException {
        getActiveMySQLConnection().setCallableStatementCacheSize(size);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCallableStmtCacheSize(int cacheSize) throws SQLException {
        getActiveMySQLConnection().setCallableStmtCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeDBMDTypes(boolean property) {
        getActiveMySQLConnection().setCapitalizeDBMDTypes(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeTypeNames(boolean flag) {
        getActiveMySQLConnection().setCapitalizeTypeNames(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCharacterEncoding(String encoding) {
        getActiveMySQLConnection().setCharacterEncoding(encoding);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCharacterSetResults(String characterSet) {
        getActiveMySQLConnection().setCharacterSetResults(characterSet);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStorePassword(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStorePassword(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreType(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStoreType(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreUrl(String value) {
        getActiveMySQLConnection().setClientCertificateKeyStoreUrl(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientInfoProvider(String classname) {
        getActiveMySQLConnection().setClientInfoProvider(classname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClobCharacterEncoding(String encoding) {
        getActiveMySQLConnection().setClobCharacterEncoding(encoding);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClobberStreamingResults(boolean flag) {
        getActiveMySQLConnection().setClobberStreamingResults(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        getActiveMySQLConnection().setCompensateOnDuplicateKeyUpdateCounts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectTimeout(int timeoutMs) throws SQLException {
        getActiveMySQLConnection().setConnectTimeout(timeoutMs);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectionCollation(String collation) {
        getActiveMySQLConnection().setConnectionCollation(collation);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectionLifecycleInterceptors(String interceptors) {
        getActiveMySQLConnection().setConnectionLifecycleInterceptors(interceptors);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setContinueBatchOnError(boolean property) {
        getActiveMySQLConnection().setContinueBatchOnError(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCreateDatabaseIfNotExist(boolean flag) {
        getActiveMySQLConnection().setCreateDatabaseIfNotExist(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDefaultFetchSize(int n) throws SQLException {
        getActiveMySQLConnection().setDefaultFetchSize(n);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDetectServerPreparedStmts(boolean property) {
        getActiveMySQLConnection().setDetectServerPreparedStmts(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDontTrackOpenResources(boolean flag) {
        getActiveMySQLConnection().setDontTrackOpenResources(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        getActiveMySQLConnection().setDumpMetadataOnColumnNotFound(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDumpQueriesOnException(boolean flag) {
        getActiveMySQLConnection().setDumpQueriesOnException(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDynamicCalendars(boolean flag) {
        getActiveMySQLConnection().setDynamicCalendars(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setElideSetAutoCommits(boolean flag) {
        getActiveMySQLConnection().setElideSetAutoCommits(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmptyStringsConvertToZero(boolean flag) {
        getActiveMySQLConnection().setEmptyStringsConvertToZero(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmulateLocators(boolean property) {
        getActiveMySQLConnection().setEmulateLocators(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmulateUnsupportedPstmts(boolean flag) {
        getActiveMySQLConnection().setEmulateUnsupportedPstmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnablePacketDebug(boolean flag) {
        getActiveMySQLConnection().setEnablePacketDebug(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnableQueryTimeouts(boolean flag) {
        getActiveMySQLConnection().setEnableQueryTimeouts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEncoding(String property) {
        getActiveMySQLConnection().setEncoding(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setExceptionInterceptors(String exceptionInterceptors) {
        getActiveMySQLConnection().setExceptionInterceptors(exceptionInterceptors);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setExplainSlowQueries(boolean flag) {
        getActiveMySQLConnection().setExplainSlowQueries(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setFailOverReadOnly(boolean flag) {
        getActiveMySQLConnection().setFailOverReadOnly(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setFunctionsNeverReturnBlobs(boolean flag) {
        getActiveMySQLConnection().setFunctionsNeverReturnBlobs(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGatherPerfMetrics(boolean flag) {
        getActiveMySQLConnection().setGatherPerfMetrics(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGatherPerformanceMetrics(boolean flag) {
        getActiveMySQLConnection().setGatherPerformanceMetrics(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGenerateSimpleParameterMetadata(boolean flag) {
        getActiveMySQLConnection().setGenerateSimpleParameterMetadata(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        getActiveMySQLConnection().setHoldResultsOpenOverStatementClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIgnoreNonTxTables(boolean property) {
        getActiveMySQLConnection().setIgnoreNonTxTables(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        getActiveMySQLConnection().setIncludeInnodbStatusInDeadlockExceptions(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setInitialTimeout(int property) throws SQLException {
        getActiveMySQLConnection().setInitialTimeout(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setInteractiveClient(boolean property) {
        getActiveMySQLConnection().setInteractiveClient(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIsInteractiveClient(boolean property) {
        getActiveMySQLConnection().setIsInteractiveClient(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncation(boolean flag) {
        getActiveMySQLConnection().setJdbcCompliantTruncation(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        getActiveMySQLConnection().setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLargeRowSizeThreshold(String value) throws SQLException {
        getActiveMySQLConnection().setLargeRowSizeThreshold(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
        getActiveMySQLConnection().setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException {
        getActiveMySQLConnection().setLoadBalancePingTimeout(loadBalancePingTimeout);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceStrategy(String strategy) {
        getActiveMySQLConnection().setLoadBalanceStrategy(strategy);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerAffinityOrder(String hostsList) {
        getActiveMySQLConnection().setServerAffinityOrder(hostsList);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        getActiveMySQLConnection().setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLocalSocketAddress(String address) {
        getActiveMySQLConnection().setLocalSocketAddress(address);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLocatorFetchBufferSize(String value) throws SQLException {
        getActiveMySQLConnection().setLocatorFetchBufferSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogSlowQueries(boolean flag) {
        getActiveMySQLConnection().setLogSlowQueries(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogXaCommands(boolean flag) {
        getActiveMySQLConnection().setLogXaCommands(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogger(String property) {
        getActiveMySQLConnection().setLogger(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoggerClassName(String className) {
        getActiveMySQLConnection().setLoggerClassName(className);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaintainTimeStats(boolean flag) {
        getActiveMySQLConnection().setMaintainTimeStats(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxQuerySizeToLog(int sizeInBytes) throws SQLException {
        getActiveMySQLConnection().setMaxQuerySizeToLog(sizeInBytes);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxReconnects(int property) throws SQLException {
        getActiveMySQLConnection().setMaxReconnects(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxRows(int property) throws SQLException {
        getActiveMySQLConnection().setMaxRows(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMetadataCacheSize(int value) throws SQLException {
        getActiveMySQLConnection().setMetadataCacheSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNetTimeoutForStreamingResults(int value) throws SQLException {
        getActiveMySQLConnection().setNetTimeoutForStreamingResults(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoAccessToProcedureBodies(boolean flag) {
        getActiveMySQLConnection().setNoAccessToProcedureBodies(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoDatetimeStringSync(boolean flag) {
        getActiveMySQLConnection().setNoDatetimeStringSync(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoTimezoneConversionForTimeType(boolean flag) {
        getActiveMySQLConnection().setNoTimezoneConversionForTimeType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoTimezoneConversionForDateType(boolean flag) {
        getActiveMySQLConnection().setNoTimezoneConversionForDateType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheDefaultTimezone(boolean flag) {
        getActiveMySQLConnection().setCacheDefaultTimezone(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNullCatalogMeansCurrent(boolean value) {
        getActiveMySQLConnection().setNullCatalogMeansCurrent(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNullNamePatternMatchesAll(boolean value) {
        getActiveMySQLConnection().setNullNamePatternMatchesAll(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        getActiveMySQLConnection().setOverrideSupportsIntegrityEnhancementFacility(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPacketDebugBufferSize(int size) throws SQLException {
        getActiveMySQLConnection().setPacketDebugBufferSize(size);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPadCharsWithSpace(boolean flag) {
        getActiveMySQLConnection().setPadCharsWithSpace(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setParanoid(boolean property) {
        getActiveMySQLConnection().setParanoid(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPasswordCharacterEncoding(String characterSet) {
        getActiveMySQLConnection().setPasswordCharacterEncoding(characterSet);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPedantic(boolean property) {
        getActiveMySQLConnection().setPedantic(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        getActiveMySQLConnection().setPinGlobalTxToPhysicalConnection(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        getActiveMySQLConnection().setPopulateInsertRowWithDefaultValues(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSize(int cacheSize) throws SQLException {
        getActiveMySQLConnection().setPrepStmtCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSqlLimit(int sqlLimit) throws SQLException {
        getActiveMySQLConnection().setPrepStmtCacheSqlLimit(sqlLimit);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSize(int cacheSize) throws SQLException {
        getActiveMySQLConnection().setPreparedStatementCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) throws SQLException {
        getActiveMySQLConnection().setPreparedStatementCacheSqlLimit(cacheSqlLimit);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        getActiveMySQLConnection().setProcessEscapeCodesForPrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfileSQL(boolean flag) {
        getActiveMySQLConnection().setProfileSQL(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfileSql(boolean property) {
        getActiveMySQLConnection().setProfileSql(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfilerEventHandler(String handler) {
        getActiveMySQLConnection().setProfilerEventHandler(handler);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPropertiesTransform(String value) {
        getActiveMySQLConnection().setPropertiesTransform(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setQueriesBeforeRetryMaster(int property) throws SQLException {
        getActiveMySQLConnection().setQueriesBeforeRetryMaster(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        getActiveMySQLConnection().setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReconnectAtTxEnd(boolean property) {
        getActiveMySQLConnection().setReconnectAtTxEnd(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRelaxAutoCommit(boolean property) {
        getActiveMySQLConnection().setRelaxAutoCommit(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReportMetricsIntervalMillis(int millis) throws SQLException {
        getActiveMySQLConnection().setReportMetricsIntervalMillis(millis);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRequireSSL(boolean property) {
        getActiveMySQLConnection().setRequireSSL(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setResourceId(String resourceId) {
        getActiveMySQLConnection().setResourceId(resourceId);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setResultSetSizeThreshold(int threshold) throws SQLException {
        getActiveMySQLConnection().setResultSetSizeThreshold(threshold);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRetainStatementAfterResultSetClose(boolean flag) {
        getActiveMySQLConnection().setRetainStatementAfterResultSetClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRetriesAllDown(int retriesAllDown) throws SQLException {
        getActiveMySQLConnection().setRetriesAllDown(retriesAllDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRewriteBatchedStatements(boolean flag) {
        getActiveMySQLConnection().setRewriteBatchedStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRollbackOnPooledClose(boolean flag) {
        getActiveMySQLConnection().setRollbackOnPooledClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRoundRobinLoadBalance(boolean flag) {
        getActiveMySQLConnection().setRoundRobinLoadBalance(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRunningCTS13(boolean flag) {
        getActiveMySQLConnection().setRunningCTS13(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSecondsBeforeRetryMaster(int property) throws SQLException {
        getActiveMySQLConnection().setSecondsBeforeRetryMaster(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingMaxOperations(int maxOperations) throws SQLException {
        getActiveMySQLConnection().setSelfDestructOnPingMaxOperations(maxOperations);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingSecondsLifetime(int seconds) throws SQLException {
        getActiveMySQLConnection().setSelfDestructOnPingSecondsLifetime(seconds);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerTimezone(String property) {
        getActiveMySQLConnection().setServerTimezone(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSessionVariables(String variables) {
        getActiveMySQLConnection().setSessionVariables(variables);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdMillis(int millis) throws SQLException {
        getActiveMySQLConnection().setSlowQueryThresholdMillis(millis);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdNanos(long nanos) throws SQLException {
        getActiveMySQLConnection().setSlowQueryThresholdNanos(nanos);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketFactory(String name) {
        getActiveMySQLConnection().setSocketFactory(name);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketFactoryClassName(String property) {
        getActiveMySQLConnection().setSocketFactoryClassName(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketTimeout(int property) throws SQLException {
        getActiveMySQLConnection().setSocketTimeout(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStatementInterceptors(String value) {
        getActiveMySQLConnection().setStatementInterceptors(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStrictFloatingPoint(boolean property) {
        getActiveMySQLConnection().setStrictFloatingPoint(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStrictUpdates(boolean property) {
        getActiveMySQLConnection().setStrictUpdates(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpKeepAlive(boolean flag) {
        getActiveMySQLConnection().setTcpKeepAlive(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpNoDelay(boolean flag) {
        getActiveMySQLConnection().setTcpNoDelay(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpRcvBuf(int bufSize) throws SQLException {
        getActiveMySQLConnection().setTcpRcvBuf(bufSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpSndBuf(int bufSize) throws SQLException {
        getActiveMySQLConnection().setTcpSndBuf(bufSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpTrafficClass(int classFlags) throws SQLException {
        getActiveMySQLConnection().setTcpTrafficClass(classFlags);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTinyInt1isBit(boolean flag) {
        getActiveMySQLConnection().setTinyInt1isBit(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTraceProtocol(boolean flag) {
        getActiveMySQLConnection().setTraceProtocol(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTransformedBitIsBoolean(boolean flag) {
        getActiveMySQLConnection().setTransformedBitIsBoolean(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTreatUtilDateAsTimestamp(boolean flag) {
        getActiveMySQLConnection().setTreatUtilDateAsTimestamp(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStorePassword(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStorePassword(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreType(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStoreType(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreUrl(String value) {
        getActiveMySQLConnection().setTrustCertificateKeyStoreUrl(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUltraDevHack(boolean flag) {
        getActiveMySQLConnection().setUltraDevHack(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseAffectedRows(boolean flag) {
        getActiveMySQLConnection().setUseAffectedRows(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        getActiveMySQLConnection().setUseBlobToStoreUTF8OutsideBMP(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseColumnNamesInFindColumn(boolean flag) {
        getActiveMySQLConnection().setUseColumnNamesInFindColumn(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseCompression(boolean property) {
        getActiveMySQLConnection().setUseCompression(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseConfigs(String configs) {
        getActiveMySQLConnection().setUseConfigs(configs);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseCursorFetch(boolean flag) {
        getActiveMySQLConnection().setUseCursorFetch(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseDirectRowUnpack(boolean flag) {
        getActiveMySQLConnection().setUseDirectRowUnpack(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseDynamicCharsetInfo(boolean flag) {
        getActiveMySQLConnection().setUseDynamicCharsetInfo(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseFastDateParsing(boolean flag) {
        getActiveMySQLConnection().setUseFastDateParsing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseFastIntParsing(boolean flag) {
        getActiveMySQLConnection().setUseFastIntParsing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseGmtMillisForDatetimes(boolean flag) {
        getActiveMySQLConnection().setUseGmtMillisForDatetimes(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseHostsInPrivileges(boolean property) {
        getActiveMySQLConnection().setUseHostsInPrivileges(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseInformationSchema(boolean flag) {
        getActiveMySQLConnection().setUseInformationSchema(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        getActiveMySQLConnection().setUseJDBCCompliantTimezoneShift(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseJvmCharsetConverters(boolean flag) {
        getActiveMySQLConnection().setUseJvmCharsetConverters(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLegacyDatetimeCode(boolean flag) {
        getActiveMySQLConnection().setUseLegacyDatetimeCode(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSendFractionalSeconds(boolean flag) {
        getActiveMySQLConnection().setSendFractionalSeconds(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLocalSessionState(boolean flag) {
        getActiveMySQLConnection().setUseLocalSessionState(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLocalTransactionState(boolean flag) {
        getActiveMySQLConnection().setUseLocalTransactionState(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseNanosForElapsedTime(boolean flag) {
        getActiveMySQLConnection().setUseNanosForElapsedTime(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOldAliasMetadataBehavior(boolean flag) {
        getActiveMySQLConnection().setUseOldAliasMetadataBehavior(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOldUTF8Behavior(boolean flag) {
        getActiveMySQLConnection().setUseOldUTF8Behavior(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOnlyServerErrorMessages(boolean flag) {
        getActiveMySQLConnection().setUseOnlyServerErrorMessages(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseReadAheadInput(boolean flag) {
        getActiveMySQLConnection().setUseReadAheadInput(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSSL(boolean property) {
        getActiveMySQLConnection().setUseSSL(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        getActiveMySQLConnection().setUseSSPSCompatibleTimezoneShift(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseServerPrepStmts(boolean flag) {
        getActiveMySQLConnection().setUseServerPrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseServerPreparedStmts(boolean flag) {
        getActiveMySQLConnection().setUseServerPreparedStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSqlStateCodes(boolean flag) {
        getActiveMySQLConnection().setUseSqlStateCodes(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseStreamLengthsInPrepStmts(boolean property) {
        getActiveMySQLConnection().setUseStreamLengthsInPrepStmts(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseTimezone(boolean property) {
        getActiveMySQLConnection().setUseTimezone(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUltraDevWorkAround(boolean property) {
        getActiveMySQLConnection().setUseUltraDevWorkAround(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUnbufferedInput(boolean flag) {
        getActiveMySQLConnection().setUseUnbufferedInput(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUnicode(boolean flag) {
        getActiveMySQLConnection().setUseUnicode(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        getActiveMySQLConnection().setUseUsageAdvisor(useUsageAdvisorFlag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        getActiveMySQLConnection().setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        getActiveMySQLConnection().setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setVerifyServerCertificate(boolean flag) {
        getActiveMySQLConnection().setVerifyServerCertificate(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setYearIsDateType(boolean flag) {
        getActiveMySQLConnection().setYearIsDateType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setZeroDateTimeBehavior(String behavior) {
        getActiveMySQLConnection().setZeroDateTimeBehavior(behavior);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean useUnbufferedInput() {
        return getActiveMySQLConnection().useUnbufferedInput();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public StringBuilder generateConnectionCommentBlock(StringBuilder buf) {
        return getActiveMySQLConnection().generateConnectionCommentBlock(buf);
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getActiveStatementCount() {
        return getActiveMySQLConnection().getActiveStatementCount();
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        return getActiveMySQLConnection().getAutoCommit();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public int getAutoIncrementIncrement() {
        return getActiveMySQLConnection().getAutoIncrementIncrement();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public CachedResultSetMetaData getCachedMetaData(String sql) {
        return getActiveMySQLConnection().getCachedMetaData(sql);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getCalendarInstanceForSessionOrNew() {
        return getActiveMySQLConnection().getCalendarInstanceForSessionOrNew();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Timer getCancelTimer() {
        return getActiveMySQLConnection().getCancelTimer();
    }

    @Override // java.sql.Connection
    public String getCatalog() throws SQLException {
        return getActiveMySQLConnection().getCatalog();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getCharacterSetMetadata() {
        return getActiveMySQLConnection().getCharacterSetMetadata();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public SingleByteCharsetConverter getCharsetConverter(String javaEncodingName) throws SQLException {
        return getActiveMySQLConnection().getCharsetConverter(javaEncodingName);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public String getCharsetNameForIndex(int charsetIndex) throws SQLException {
        return getEncodingForIndex(charsetIndex);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getEncodingForIndex(int collationIndex) throws SQLException {
        return getActiveMySQLConnection().getEncodingForIndex(collationIndex);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public TimeZone getDefaultTimeZone() {
        return getActiveMySQLConnection().getDefaultTimeZone();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getErrorMessageEncoding() {
        return getActiveMySQLConnection().getErrorMessageEncoding();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ExceptionInterceptor getExceptionInterceptor() {
        return getActiveMySQLConnection().getExceptionInterceptor();
    }

    @Override // java.sql.Connection
    public int getHoldability() throws SQLException {
        return getActiveMySQLConnection().getHoldability();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getHost() {
        return getActiveMySQLConnection().getHost();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getHostPortPair() {
        return getActiveMySQLConnection().getHostPortPair();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public long getId() {
        return getActiveMySQLConnection().getId();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public long getIdleFor() {
        return getActiveMySQLConnection().getIdleFor();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MysqlIO getIO() throws SQLException {
        return getActiveMySQLConnection().getIO();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    @Deprecated
    public MySQLConnection getLoadBalanceSafeProxy() {
        return getMultiHostSafeProxy();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public MySQLConnection getMultiHostSafeProxy() {
        return getThisAsProxy().getProxy();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Log getLog() throws SQLException {
        return getActiveMySQLConnection().getLog();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(String javaCharsetName) throws SQLException {
        return getActiveMySQLConnection().getMaxBytesPerChar(javaCharsetName);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getMaxBytesPerChar(Integer charsetIndex, String javaCharsetName) throws SQLException {
        return getActiveMySQLConnection().getMaxBytesPerChar(charsetIndex, javaCharsetName);
    }

    @Override // java.sql.Connection
    public java.sql.DatabaseMetaData getMetaData() throws SQLException {
        return getActiveMySQLConnection().getMetaData();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public java.sql.Statement getMetadataSafeStatement() throws SQLException {
        return getActiveMySQLConnection().getMetadataSafeStatement();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getNetBufferLength() {
        return getActiveMySQLConnection().getNetBufferLength();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public Properties getProperties() {
        return getActiveMySQLConnection().getProperties();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean getRequiresEscapingEncoder() {
        return getActiveMySQLConnection().getRequiresEscapingEncoder();
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

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMajorVersion() {
        return getActiveMySQLConnection().getServerMajorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerMinorVersion() {
        return getActiveMySQLConnection().getServerMinorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public int getServerSubMinorVersion() {
        return getActiveMySQLConnection().getServerSubMinorVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public TimeZone getServerTimezoneTZ() {
        return getActiveMySQLConnection().getServerTimezoneTZ();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVariable(String variableName) {
        return getActiveMySQLConnection().getServerVariable(variableName);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getServerVersion() {
        return getActiveMySQLConnection().getServerVersion();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getSessionLockedCalendar() {
        return getActiveMySQLConnection().getSessionLockedCalendar();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public String getStatementComment() {
        return getActiveMySQLConnection().getStatementComment();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public List<StatementInterceptorV2> getStatementInterceptorsInstances() {
        return getActiveMySQLConnection().getStatementInterceptorsInstances();
    }

    @Override // java.sql.Connection
    public int getTransactionIsolation() throws SQLException {
        return getActiveMySQLConnection().getTransactionIsolation();
    }

    @Override // java.sql.Connection
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getActiveMySQLConnection().getTypeMap();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getURL() {
        return getActiveMySQLConnection().getURL();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public String getUser() {
        return getActiveMySQLConnection().getUser();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public Calendar getUtcCalendar() {
        return getActiveMySQLConnection().getUtcCalendar();
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        return getActiveMySQLConnection().getWarnings();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean hasSameProperties(Connection c) {
        return getActiveMySQLConnection().hasSameProperties(c);
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public boolean hasTriedMaster() {
        return getActiveMySQLConnection().hasTriedMaster();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPreparedExecutes() {
        getActiveMySQLConnection().incrementNumberOfPreparedExecutes();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfPrepares() {
        getActiveMySQLConnection().incrementNumberOfPrepares();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void incrementNumberOfResultSetsCreated() {
        getActiveMySQLConnection().incrementNumberOfResultSetsCreated();
    }

    @Override // com.mysql.jdbc.Connection
    public void initializeExtension(Extension ex) throws SQLException {
        getActiveMySQLConnection().initializeExtension(ex);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeResultsMetadataFromCache(String sql, CachedResultSetMetaData cachedMetaData, ResultSetInternalMethods resultSet) throws SQLException {
        getActiveMySQLConnection().initializeResultsMetadataFromCache(sql, cachedMetaData, resultSet);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void initializeSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().initializeSafeStatementInterceptors();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        return getActiveMySQLConnection().isAbonormallyLongQuery(millisOrNanos);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isClientTzUTC() {
        return getActiveMySQLConnection().isClientTzUTC();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isCursorFetchEnabled() throws SQLException {
        return getActiveMySQLConnection().isCursorFetchEnabled();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isInGlobalTx() {
        return getActiveMySQLConnection().isInGlobalTx();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isMasterConnection() {
        return getThisAsProxy().isMasterConnection();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isNoBackslashEscapesSet() {
        return getActiveMySQLConnection().isNoBackslashEscapesSet();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isReadInfoMsgEnabled() {
        return getActiveMySQLConnection().isReadInfoMsgEnabled();
    }

    @Override // com.mysql.jdbc.MySQLConnection, java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        return getActiveMySQLConnection().isReadOnly();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isReadOnly(boolean useSessionStatus) throws SQLException {
        return getActiveMySQLConnection().isReadOnly(useSessionStatus);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isRunningOnJDK13() {
        return getActiveMySQLConnection().isRunningOnJDK13();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isSameResource(Connection otherConnection) {
        return getActiveMySQLConnection().isSameResource(otherConnection);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isServerTzUTC() {
        return getActiveMySQLConnection().isServerTzUTC();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public boolean lowerCaseTableNames() {
        return getActiveMySQLConnection().lowerCaseTableNames();
    }

    @Override // java.sql.Connection
    public String nativeSQL(String sql) throws SQLException {
        return getActiveMySQLConnection().nativeSQL(sql);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean parserKnowsUnicode() {
        return getActiveMySQLConnection().parserKnowsUnicode();
    }

    @Override // com.mysql.jdbc.Connection
    public void ping() throws SQLException {
        getActiveMySQLConnection().ping();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void pingInternal(boolean checkForClosedConnection, int timeoutMillis) throws SQLException {
        getActiveMySQLConnection().pingInternal(checkForClosedConnection, timeoutMillis);
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public java.sql.CallableStatement prepareCall(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareCall(sql);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndex);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyIndexes);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql, autoGenKeyColNames);
    }

    @Override // java.sql.Connection
    public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().prepareStatement(sql);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void realClose(boolean calledExplicitly, boolean issueRollback, boolean skipLocalTeardown, Throwable reason) throws SQLException {
        getActiveMySQLConnection().realClose(calledExplicitly, issueRollback, skipLocalTeardown, reason);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void recachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        getActiveMySQLConnection().recachePreparedStatement(pstmt);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void decachePreparedStatement(ServerPreparedStatement pstmt) throws SQLException {
        getActiveMySQLConnection().decachePreparedStatement(pstmt);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerQueryExecutionTime(long queryTimeMs) {
        getActiveMySQLConnection().registerQueryExecutionTime(queryTimeMs);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void registerStatement(Statement stmt) {
        getActiveMySQLConnection().registerStatement(stmt);
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        getActiveMySQLConnection().releaseSavepoint(arg0);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void reportNumberOfTablesAccessed(int numTablesAccessed) {
        getActiveMySQLConnection().reportNumberOfTablesAccessed(numTablesAccessed);
    }

    @Override // com.mysql.jdbc.Connection
    public void reportQueryTime(long millisOrNanos) {
        getActiveMySQLConnection().reportQueryTime(millisOrNanos);
    }

    @Override // com.mysql.jdbc.Connection
    public void resetServerState() throws SQLException {
        getActiveMySQLConnection().resetServerState();
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        getActiveMySQLConnection().rollback();
    }

    @Override // java.sql.Connection
    public void rollback(Savepoint savepoint) throws SQLException {
        getActiveMySQLConnection().rollback(savepoint);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndex);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyIndexes);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql, autoGenKeyColNames);
    }

    @Override // com.mysql.jdbc.Connection
    public java.sql.PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        return getActiveMySQLConnection().serverPrepareStatement(sql);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean serverSupportsConvertFn() throws SQLException {
        return getActiveMySQLConnection().serverSupportsConvertFn();
    }

    @Override // java.sql.Connection
    public void setAutoCommit(boolean autoCommitFlag) throws SQLException {
        getActiveMySQLConnection().setAutoCommit(autoCommitFlag);
    }

    @Override // java.sql.Connection
    public void setCatalog(String catalog) throws SQLException {
        getActiveMySQLConnection().setCatalog(catalog);
    }

    @Override // com.mysql.jdbc.Connection
    public void setFailedOver(boolean flag) {
        getActiveMySQLConnection().setFailedOver(flag);
    }

    @Override // java.sql.Connection
    public void setHoldability(int arg0) throws SQLException {
        getActiveMySQLConnection().setHoldability(arg0);
    }

    @Override // com.mysql.jdbc.Connection
    public void setInGlobalTx(boolean flag) {
        getActiveMySQLConnection().setInGlobalTx(flag);
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void setPreferSlaveDuringFailover(boolean flag) {
        getActiveMySQLConnection().setPreferSlaveDuringFailover(flag);
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void setProxy(MySQLConnection proxy) {
        getThisAsProxy().setProxy(proxy);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadInfoMsgEnabled(boolean flag) {
        getActiveMySQLConnection().setReadInfoMsgEnabled(flag);
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnly(readOnlyFlag);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setReadOnlyInternal(boolean readOnlyFlag) throws SQLException {
        getActiveMySQLConnection().setReadOnlyInternal(readOnlyFlag);
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        return getActiveMySQLConnection().setSavepoint();
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String name) throws SQLException {
        return getActiveMySQLConnection().setSavepoint(name);
    }

    @Override // com.mysql.jdbc.Connection
    public void setStatementComment(String comment) {
        getActiveMySQLConnection().setStatementComment(comment);
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        getActiveMySQLConnection().setTransactionIsolation(level);
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.Connection
    public void shutdownServer() throws SQLException {
        getActiveMySQLConnection().shutdownServer();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean storesLowerCaseTableName() {
        return getActiveMySQLConnection().storesLowerCaseTableName();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsIsolationLevel() {
        return getActiveMySQLConnection().supportsIsolationLevel();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsQuotedIdentifiers() {
        return getActiveMySQLConnection().supportsQuotedIdentifiers();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsTransactions() {
        return getActiveMySQLConnection().supportsTransactions();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void throwConnectionClosedException() throws SQLException {
        getActiveMySQLConnection().throwConnectionClosedException();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionBegun() throws SQLException {
        getActiveMySQLConnection().transactionBegun();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void transactionCompleted() throws SQLException {
        getActiveMySQLConnection().transactionCompleted();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unregisterStatement(Statement stmt) {
        getActiveMySQLConnection().unregisterStatement(stmt);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void unSafeStatementInterceptors() throws SQLException {
        getActiveMySQLConnection().unSafeStatementInterceptors();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean useAnsiQuotedIdentifiers() {
        return getActiveMySQLConnection().useAnsiQuotedIdentifiers();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        return getActiveMySQLConnection().versionMeetsMinimum(major, minor, subminor);
    }

    @Override // java.sql.Connection
    public boolean isClosed() throws SQLException {
        return getThisAsProxy().isClosed;
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getHoldResultsOpenOverStatementClose() {
        return getActiveMySQLConnection().getHoldResultsOpenOverStatementClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceConnectionGroup() {
        return getActiveMySQLConnection().getLoadBalanceConnectionGroup();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLoadBalanceEnableJMX() {
        return getActiveMySQLConnection().getLoadBalanceEnableJMX();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceExceptionChecker() {
        return getActiveMySQLConnection().getLoadBalanceExceptionChecker();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceSQLExceptionSubclassFailover() {
        return getActiveMySQLConnection().getLoadBalanceSQLExceptionSubclassFailover();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceSQLStateFailover() {
        return getActiveMySQLConnection().getLoadBalanceSQLStateFailover();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        getActiveMySQLConnection().setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        getActiveMySQLConnection().setLoadBalanceEnableJMX(loadBalanceEnableJMX);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        getActiveMySQLConnection().setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        getActiveMySQLConnection().setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        getActiveMySQLConnection().setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceHostRemovalGracePeriod(int loadBalanceHostRemovalGracePeriod) throws SQLException {
        getActiveMySQLConnection().setLoadBalanceHostRemovalGracePeriod(loadBalanceHostRemovalGracePeriod);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceHostRemovalGracePeriod() {
        return getActiveMySQLConnection().getLoadBalanceHostRemovalGracePeriod();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public boolean isProxySet() {
        return getActiveMySQLConnection().isProxySet();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceAutoCommitStatementRegex() {
        return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementRegex();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceAutoCommitStatementThreshold() {
        return getActiveMySQLConnection().getLoadBalanceAutoCommitStatementThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        getActiveMySQLConnection().setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) throws SQLException {
        getActiveMySQLConnection().setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeThreadDumpInDeadlockExceptions() {
        return getActiveMySQLConnection().getIncludeThreadDumpInDeadlockExceptions();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        getActiveMySQLConnection().setIncludeThreadDumpInDeadlockExceptions(flag);
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getActiveMySQLConnection().setTypeMap(map);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeThreadNamesAsStatementComment() {
        return getActiveMySQLConnection().getIncludeThreadNamesAsStatementComment();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        getActiveMySQLConnection().setIncludeThreadNamesAsStatementComment(flag);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isServerLocal() throws SQLException {
        return getActiveMySQLConnection().isServerLocal();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAuthenticationPlugins(String authenticationPlugins) {
        getActiveMySQLConnection().setAuthenticationPlugins(authenticationPlugins);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getAuthenticationPlugins() {
        return getActiveMySQLConnection().getAuthenticationPlugins();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        getActiveMySQLConnection().setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getDisabledAuthenticationPlugins() {
        return getActiveMySQLConnection().getDisabledAuthenticationPlugins();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        getActiveMySQLConnection().setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getDefaultAuthenticationPlugin() {
        return getActiveMySQLConnection().getDefaultAuthenticationPlugin();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setParseInfoCacheFactory(String factoryClassname) {
        getActiveMySQLConnection().setParseInfoCacheFactory(factoryClassname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getParseInfoCacheFactory() {
        return getActiveMySQLConnection().getParseInfoCacheFactory();
    }

    @Override // com.mysql.jdbc.Connection
    public void setSchema(String schema) throws SQLException {
        getActiveMySQLConnection().setSchema(schema);
    }

    @Override // com.mysql.jdbc.Connection
    public String getSchema() throws SQLException {
        return getActiveMySQLConnection().getSchema();
    }

    @Override // com.mysql.jdbc.Connection
    public void abort(Executor executor) throws SQLException {
        getActiveMySQLConnection().abort(executor);
    }

    @Override // com.mysql.jdbc.Connection
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        getActiveMySQLConnection().setNetworkTimeout(executor, milliseconds);
    }

    @Override // com.mysql.jdbc.Connection
    public int getNetworkTimeout() throws SQLException {
        return getActiveMySQLConnection().getNetworkTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerConfigCacheFactory(String factoryClassname) {
        getActiveMySQLConnection().setServerConfigCacheFactory(factoryClassname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerConfigCacheFactory() {
        return getActiveMySQLConnection().getServerConfigCacheFactory();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        getActiveMySQLConnection().setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDisconnectOnExpiredPasswords() {
        return getActiveMySQLConnection().getDisconnectOnExpiredPasswords();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions) {
        getActiveMySQLConnection().setGetProceduresReturnsFunctions(getProcedureReturnsFunctions);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGetProceduresReturnsFunctions() {
        return getActiveMySQLConnection().getGetProceduresReturnsFunctions();
    }

    @Override // com.mysql.jdbc.Connection
    public Object getConnectionMutex() {
        return getActiveMySQLConnection().getConnectionMutex();
    }

    @Override // com.mysql.jdbc.MySQLConnection, com.mysql.jdbc.ConnectionProperties
    public String getConnectionAttributes() throws SQLException {
        return getActiveMySQLConnection().getConnectionAttributes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowMasterDownConnections() {
        return getActiveMySQLConnection().getAllowMasterDownConnections();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
        getActiveMySQLConnection().setAllowMasterDownConnections(connectIfMasterDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowSlaveDownConnections() {
        return getActiveMySQLConnection().getAllowSlaveDownConnections();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowSlaveDownConnections(boolean connectIfSlaveDown) {
        getActiveMySQLConnection().setAllowSlaveDownConnections(connectIfSlaveDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReadFromMasterWhenNoSlaves() {
        return getActiveMySQLConnection().getReadFromMasterWhenNoSlaves();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReadFromMasterWhenNoSlaves(boolean useMasterIfSlavesDown) {
        getActiveMySQLConnection().setReadFromMasterWhenNoSlaves(useMasterIfSlavesDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReplicationEnableJMX() {
        return getActiveMySQLConnection().getReplicationEnableJMX();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReplicationEnableJMX(boolean replicationEnableJMX) {
        getActiveMySQLConnection().setReplicationEnableJMX(replicationEnableJMX);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDetectCustomCollations(boolean detectCustomCollations) {
        getActiveMySQLConnection().setDetectCustomCollations(detectCustomCollations);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDetectCustomCollations() {
        return getActiveMySQLConnection().getDetectCustomCollations();
    }

    @Override // com.mysql.jdbc.Connection
    public int getSessionMaxRows() {
        return getActiveMySQLConnection().getSessionMaxRows();
    }

    @Override // com.mysql.jdbc.Connection
    public void setSessionMaxRows(int max) throws SQLException {
        getActiveMySQLConnection().setSessionMaxRows(max);
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public ProfilerEventHandler getProfilerEventHandlerInstance() {
        return getActiveMySQLConnection().getProfilerEventHandlerInstance();
    }

    @Override // com.mysql.jdbc.MySQLConnection
    public void setProfilerEventHandlerInstance(ProfilerEventHandler h) {
        getActiveMySQLConnection().setProfilerEventHandlerInstance(h);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerRSAPublicKeyFile() {
        return getActiveMySQLConnection().getServerRSAPublicKeyFile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerRSAPublicKeyFile(String serverRSAPublicKeyFile) throws SQLException {
        getActiveMySQLConnection().setServerRSAPublicKeyFile(serverRSAPublicKeyFile);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowPublicKeyRetrieval() {
        return getActiveMySQLConnection().getAllowPublicKeyRetrieval();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowPublicKeyRetrieval(boolean allowPublicKeyRetrieval) throws SQLException {
        getActiveMySQLConnection().setAllowPublicKeyRetrieval(allowPublicKeyRetrieval);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDontCheckOnDuplicateKeyUpdateInSQL(boolean dontCheckOnDuplicateKeyUpdateInSQL) {
        getActiveMySQLConnection().setDontCheckOnDuplicateKeyUpdateInSQL(dontCheckOnDuplicateKeyUpdateInSQL);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDontCheckOnDuplicateKeyUpdateInSQL() {
        return getActiveMySQLConnection().getDontCheckOnDuplicateKeyUpdateInSQL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocksProxyHost(String socksProxyHost) {
        getActiveMySQLConnection().setSocksProxyHost(socksProxyHost);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocksProxyHost() {
        return getActiveMySQLConnection().getSocksProxyHost();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocksProxyPort(int socksProxyPort) throws SQLException {
        getActiveMySQLConnection().setSocksProxyPort(socksProxyPort);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSocksProxyPort() {
        return getActiveMySQLConnection().getSocksProxyPort();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReadOnlyPropagatesToServer() {
        return getActiveMySQLConnection().getReadOnlyPropagatesToServer();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReadOnlyPropagatesToServer(boolean flag) {
        getActiveMySQLConnection().setReadOnlyPropagatesToServer(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEnabledSSLCipherSuites() {
        return getActiveMySQLConnection().getEnabledSSLCipherSuites();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnabledSSLCipherSuites(String cipherSuites) {
        getActiveMySQLConnection().setEnabledSSLCipherSuites(cipherSuites);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEnabledTLSProtocols() {
        return getActiveMySQLConnection().getEnabledTLSProtocols();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnabledTLSProtocols(String protocols) {
        getActiveMySQLConnection().setEnabledTLSProtocols(protocols);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnableEscapeProcessing() {
        return getActiveMySQLConnection().getEnableEscapeProcessing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnableEscapeProcessing(boolean flag) {
        getActiveMySQLConnection().setEnableEscapeProcessing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean isUseSSLExplicit() {
        return getActiveMySQLConnection().isUseSSLExplicit();
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
