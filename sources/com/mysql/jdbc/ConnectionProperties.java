package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionProperties.class */
public interface ConnectionProperties {
    String exposeAsXml() throws SQLException;

    boolean getAllowLoadLocalInfile();

    boolean getAllowMultiQueries();

    boolean getAllowNanAndInf();

    boolean getAllowUrlInLocalInfile();

    boolean getAlwaysSendSetIsolation();

    boolean getAutoDeserialize();

    boolean getAutoGenerateTestcaseScript();

    boolean getAutoReconnectForPools();

    int getBlobSendChunkSize();

    boolean getCacheCallableStatements();

    boolean getCachePreparedStatements();

    boolean getCacheResultSetMetadata();

    boolean getCacheServerConfiguration();

    int getCallableStatementCacheSize();

    boolean getCapitalizeTypeNames();

    String getCharacterSetResults();

    boolean getClobberStreamingResults();

    String getClobCharacterEncoding();

    String getConnectionCollation();

    int getConnectTimeout();

    boolean getContinueBatchOnError();

    boolean getCreateDatabaseIfNotExist();

    int getDefaultFetchSize();

    boolean getDontTrackOpenResources();

    boolean getDumpQueriesOnException();

    boolean getDynamicCalendars();

    boolean getElideSetAutoCommits();

    boolean getEmptyStringsConvertToZero();

    boolean getEmulateLocators();

    boolean getEmulateUnsupportedPstmts();

    boolean getEnablePacketDebug();

    String getEncoding();

    boolean getExplainSlowQueries();

    boolean getFailOverReadOnly();

    boolean getGatherPerformanceMetrics();

    boolean getHoldResultsOpenOverStatementClose();

    boolean getIgnoreNonTxTables();

    int getInitialTimeout();

    boolean getInteractiveClient();

    boolean getIsInteractiveClient();

    boolean getJdbcCompliantTruncation();

    int getLocatorFetchBufferSize();

    String getLogger();

    String getLoggerClassName();

    boolean getLogSlowQueries();

    boolean getMaintainTimeStats();

    int getMaxQuerySizeToLog();

    int getMaxReconnects();

    int getMaxRows();

    int getMetadataCacheSize();

    boolean getNoDatetimeStringSync();

    boolean getNullCatalogMeansCurrent();

    boolean getNullNamePatternMatchesAll();

    int getPacketDebugBufferSize();

    boolean getParanoid();

    boolean getPedantic();

    int getPreparedStatementCacheSize();

    int getPreparedStatementCacheSqlLimit();

    boolean getProfileSql();

    boolean getProfileSQL();

    String getPropertiesTransform();

    int getQueriesBeforeRetryMaster();

    boolean getReconnectAtTxEnd();

    boolean getRelaxAutoCommit();

    int getReportMetricsIntervalMillis();

    boolean getRequireSSL();

    boolean getRollbackOnPooledClose();

    boolean getRoundRobinLoadBalance();

    boolean getRunningCTS13();

    int getSecondsBeforeRetryMaster();

    String getServerTimezone();

    String getSessionVariables();

    int getSlowQueryThresholdMillis();

    String getSocketFactoryClassName();

    int getSocketTimeout();

    boolean getStrictFloatingPoint();

    boolean getStrictUpdates();

    boolean getTinyInt1isBit();

    boolean getTraceProtocol();

    boolean getTransformedBitIsBoolean();

    boolean getUseCompression();

    boolean getUseFastIntParsing();

    boolean getUseHostsInPrivileges();

    boolean getUseInformationSchema();

    boolean getUseLocalSessionState();

    boolean getUseOldUTF8Behavior();

    boolean getUseOnlyServerErrorMessages();

    boolean getUseReadAheadInput();

    boolean getUseServerPreparedStmts();

    boolean getUseSqlStateCodes();

    boolean getUseSSL();

    boolean isUseSSLExplicit();

    boolean getUseStreamLengthsInPrepStmts();

    boolean getUseTimezone();

    boolean getUseUltraDevWorkAround();

    boolean getUseUnbufferedInput();

    boolean getUseUnicode();

    boolean getUseUsageAdvisor();

    boolean getYearIsDateType();

    String getZeroDateTimeBehavior();

    void setAllowLoadLocalInfile(boolean z);

    void setAllowMultiQueries(boolean z);

    void setAllowNanAndInf(boolean z);

    void setAllowUrlInLocalInfile(boolean z);

    void setAlwaysSendSetIsolation(boolean z);

    void setAutoDeserialize(boolean z);

    void setAutoGenerateTestcaseScript(boolean z);

    void setAutoReconnect(boolean z);

    void setAutoReconnectForConnectionPools(boolean z);

    void setAutoReconnectForPools(boolean z);

    void setBlobSendChunkSize(String str) throws SQLException;

    void setCacheCallableStatements(boolean z);

    void setCachePreparedStatements(boolean z);

    void setCacheResultSetMetadata(boolean z);

    void setCacheServerConfiguration(boolean z);

    void setCallableStatementCacheSize(int i) throws SQLException;

    void setCapitalizeDBMDTypes(boolean z);

    void setCapitalizeTypeNames(boolean z);

    void setCharacterEncoding(String str);

    void setCharacterSetResults(String str);

    void setClobberStreamingResults(boolean z);

    void setClobCharacterEncoding(String str);

    void setConnectionCollation(String str);

    void setConnectTimeout(int i) throws SQLException;

    void setContinueBatchOnError(boolean z);

    void setCreateDatabaseIfNotExist(boolean z);

    void setDefaultFetchSize(int i) throws SQLException;

    void setDetectServerPreparedStmts(boolean z);

    void setDontTrackOpenResources(boolean z);

    void setDumpQueriesOnException(boolean z);

    void setDynamicCalendars(boolean z);

    void setElideSetAutoCommits(boolean z);

    void setEmptyStringsConvertToZero(boolean z);

    void setEmulateLocators(boolean z);

    void setEmulateUnsupportedPstmts(boolean z);

    void setEnablePacketDebug(boolean z);

    void setEncoding(String str);

    void setExplainSlowQueries(boolean z);

    void setFailOverReadOnly(boolean z);

    void setGatherPerformanceMetrics(boolean z);

    void setHoldResultsOpenOverStatementClose(boolean z);

    void setIgnoreNonTxTables(boolean z);

    void setInitialTimeout(int i) throws SQLException;

    void setIsInteractiveClient(boolean z);

    void setJdbcCompliantTruncation(boolean z);

    void setLocatorFetchBufferSize(String str) throws SQLException;

    void setLogger(String str);

    void setLoggerClassName(String str);

    void setLogSlowQueries(boolean z);

    void setMaintainTimeStats(boolean z);

    void setMaxQuerySizeToLog(int i) throws SQLException;

    void setMaxReconnects(int i) throws SQLException;

    void setMaxRows(int i) throws SQLException;

    void setMetadataCacheSize(int i) throws SQLException;

    void setNoDatetimeStringSync(boolean z);

    void setNullCatalogMeansCurrent(boolean z);

    void setNullNamePatternMatchesAll(boolean z);

    void setPacketDebugBufferSize(int i) throws SQLException;

    void setParanoid(boolean z);

    void setPedantic(boolean z);

    void setPreparedStatementCacheSize(int i) throws SQLException;

    void setPreparedStatementCacheSqlLimit(int i) throws SQLException;

    void setProfileSql(boolean z);

    void setProfileSQL(boolean z);

    void setPropertiesTransform(String str);

    void setQueriesBeforeRetryMaster(int i) throws SQLException;

    void setReconnectAtTxEnd(boolean z);

    void setRelaxAutoCommit(boolean z);

    void setReportMetricsIntervalMillis(int i) throws SQLException;

    void setRequireSSL(boolean z);

    void setRetainStatementAfterResultSetClose(boolean z);

    void setRollbackOnPooledClose(boolean z);

    void setRoundRobinLoadBalance(boolean z);

    void setRunningCTS13(boolean z);

    void setSecondsBeforeRetryMaster(int i) throws SQLException;

    void setServerTimezone(String str);

    void setSessionVariables(String str);

    void setSlowQueryThresholdMillis(int i) throws SQLException;

    void setSocketFactoryClassName(String str);

    void setSocketTimeout(int i) throws SQLException;

    void setStrictFloatingPoint(boolean z);

    void setStrictUpdates(boolean z);

    void setTinyInt1isBit(boolean z);

    void setTraceProtocol(boolean z);

    void setTransformedBitIsBoolean(boolean z);

    void setUseCompression(boolean z);

    void setUseFastIntParsing(boolean z);

    void setUseHostsInPrivileges(boolean z);

    void setUseInformationSchema(boolean z);

    void setUseLocalSessionState(boolean z);

    void setUseOldUTF8Behavior(boolean z);

    void setUseOnlyServerErrorMessages(boolean z);

    void setUseReadAheadInput(boolean z);

    void setUseServerPreparedStmts(boolean z);

    void setUseSqlStateCodes(boolean z);

    void setUseSSL(boolean z);

    void setUseStreamLengthsInPrepStmts(boolean z);

    void setUseTimezone(boolean z);

    void setUseUltraDevWorkAround(boolean z);

    void setUseUnbufferedInput(boolean z);

    void setUseUnicode(boolean z);

    void setUseUsageAdvisor(boolean z);

    void setYearIsDateType(boolean z);

    void setZeroDateTimeBehavior(String str);

    boolean useUnbufferedInput();

    boolean getUseCursorFetch();

    void setUseCursorFetch(boolean z);

    boolean getOverrideSupportsIntegrityEnhancementFacility();

    void setOverrideSupportsIntegrityEnhancementFacility(boolean z);

    boolean getNoTimezoneConversionForTimeType();

    void setNoTimezoneConversionForTimeType(boolean z);

    boolean getNoTimezoneConversionForDateType();

    void setNoTimezoneConversionForDateType(boolean z);

    boolean getCacheDefaultTimezone();

    void setCacheDefaultTimezone(boolean z);

    boolean getUseJDBCCompliantTimezoneShift();

    void setUseJDBCCompliantTimezoneShift(boolean z);

    boolean getAutoClosePStmtStreams();

    void setAutoClosePStmtStreams(boolean z);

    boolean getProcessEscapeCodesForPrepStmts();

    void setProcessEscapeCodesForPrepStmts(boolean z);

    boolean getUseGmtMillisForDatetimes();

    void setUseGmtMillisForDatetimes(boolean z);

    boolean getDumpMetadataOnColumnNotFound();

    void setDumpMetadataOnColumnNotFound(boolean z);

    String getResourceId();

    void setResourceId(String str);

    boolean getRewriteBatchedStatements();

    void setRewriteBatchedStatements(boolean z);

    boolean getJdbcCompliantTruncationForReads();

    void setJdbcCompliantTruncationForReads(boolean z);

    boolean getUseJvmCharsetConverters();

    void setUseJvmCharsetConverters(boolean z);

    boolean getPinGlobalTxToPhysicalConnection();

    void setPinGlobalTxToPhysicalConnection(boolean z);

    void setGatherPerfMetrics(boolean z);

    boolean getGatherPerfMetrics();

    void setUltraDevHack(boolean z);

    boolean getUltraDevHack();

    void setInteractiveClient(boolean z);

    void setSocketFactory(String str);

    String getSocketFactory();

    void setUseServerPrepStmts(boolean z);

    boolean getUseServerPrepStmts();

    void setCacheCallableStmts(boolean z);

    boolean getCacheCallableStmts();

    void setCachePrepStmts(boolean z);

    boolean getCachePrepStmts();

    void setCallableStmtCacheSize(int i) throws SQLException;

    int getCallableStmtCacheSize();

    void setPrepStmtCacheSize(int i) throws SQLException;

    int getPrepStmtCacheSize();

    void setPrepStmtCacheSqlLimit(int i) throws SQLException;

    int getPrepStmtCacheSqlLimit();

    boolean getNoAccessToProcedureBodies();

    void setNoAccessToProcedureBodies(boolean z);

    boolean getUseOldAliasMetadataBehavior();

    void setUseOldAliasMetadataBehavior(boolean z);

    String getClientCertificateKeyStorePassword();

    void setClientCertificateKeyStorePassword(String str);

    String getClientCertificateKeyStoreType();

    void setClientCertificateKeyStoreType(String str);

    String getClientCertificateKeyStoreUrl();

    void setClientCertificateKeyStoreUrl(String str);

    String getTrustCertificateKeyStorePassword();

    void setTrustCertificateKeyStorePassword(String str);

    String getTrustCertificateKeyStoreType();

    void setTrustCertificateKeyStoreType(String str);

    String getTrustCertificateKeyStoreUrl();

    void setTrustCertificateKeyStoreUrl(String str);

    boolean getUseSSPSCompatibleTimezoneShift();

    void setUseSSPSCompatibleTimezoneShift(boolean z);

    boolean getTreatUtilDateAsTimestamp();

    void setTreatUtilDateAsTimestamp(boolean z);

    boolean getUseFastDateParsing();

    void setUseFastDateParsing(boolean z);

    String getLocalSocketAddress();

    void setLocalSocketAddress(String str);

    void setUseConfigs(String str);

    String getUseConfigs();

    boolean getGenerateSimpleParameterMetadata();

    void setGenerateSimpleParameterMetadata(boolean z);

    boolean getLogXaCommands();

    void setLogXaCommands(boolean z);

    int getResultSetSizeThreshold();

    void setResultSetSizeThreshold(int i) throws SQLException;

    int getNetTimeoutForStreamingResults();

    void setNetTimeoutForStreamingResults(int i) throws SQLException;

    boolean getEnableQueryTimeouts();

    void setEnableQueryTimeouts(boolean z);

    boolean getPadCharsWithSpace();

    void setPadCharsWithSpace(boolean z);

    boolean getUseDynamicCharsetInfo();

    void setUseDynamicCharsetInfo(boolean z);

    String getClientInfoProvider();

    void setClientInfoProvider(String str);

    boolean getPopulateInsertRowWithDefaultValues();

    void setPopulateInsertRowWithDefaultValues(boolean z);

    String getLoadBalanceStrategy();

    void setLoadBalanceStrategy(String str);

    String getServerAffinityOrder();

    void setServerAffinityOrder(String str);

    boolean getTcpNoDelay();

    void setTcpNoDelay(boolean z);

    boolean getTcpKeepAlive();

    void setTcpKeepAlive(boolean z);

    int getTcpRcvBuf();

    void setTcpRcvBuf(int i) throws SQLException;

    int getTcpSndBuf();

    void setTcpSndBuf(int i) throws SQLException;

    int getTcpTrafficClass();

    void setTcpTrafficClass(int i) throws SQLException;

    boolean getUseNanosForElapsedTime();

    void setUseNanosForElapsedTime(boolean z);

    long getSlowQueryThresholdNanos();

    void setSlowQueryThresholdNanos(long j) throws SQLException;

    String getStatementInterceptors();

    void setStatementInterceptors(String str);

    boolean getUseDirectRowUnpack();

    void setUseDirectRowUnpack(boolean z);

    String getLargeRowSizeThreshold();

    void setLargeRowSizeThreshold(String str) throws SQLException;

    boolean getUseBlobToStoreUTF8OutsideBMP();

    void setUseBlobToStoreUTF8OutsideBMP(boolean z);

    String getUtf8OutsideBmpExcludedColumnNamePattern();

    void setUtf8OutsideBmpExcludedColumnNamePattern(String str);

    String getUtf8OutsideBmpIncludedColumnNamePattern();

    void setUtf8OutsideBmpIncludedColumnNamePattern(String str);

    boolean getIncludeInnodbStatusInDeadlockExceptions();

    void setIncludeInnodbStatusInDeadlockExceptions(boolean z);

    boolean getIncludeThreadDumpInDeadlockExceptions();

    void setIncludeThreadDumpInDeadlockExceptions(boolean z);

    boolean getIncludeThreadNamesAsStatementComment();

    void setIncludeThreadNamesAsStatementComment(boolean z);

    boolean getBlobsAreStrings();

    void setBlobsAreStrings(boolean z);

    boolean getFunctionsNeverReturnBlobs();

    void setFunctionsNeverReturnBlobs(boolean z);

    boolean getAutoSlowLog();

    void setAutoSlowLog(boolean z);

    String getConnectionLifecycleInterceptors();

    void setConnectionLifecycleInterceptors(String str);

    String getProfilerEventHandler();

    void setProfilerEventHandler(String str);

    boolean getVerifyServerCertificate();

    void setVerifyServerCertificate(boolean z);

    boolean getUseLegacyDatetimeCode();

    void setUseLegacyDatetimeCode(boolean z);

    boolean getSendFractionalSeconds();

    void setSendFractionalSeconds(boolean z);

    int getSelfDestructOnPingSecondsLifetime();

    void setSelfDestructOnPingSecondsLifetime(int i) throws SQLException;

    int getSelfDestructOnPingMaxOperations();

    void setSelfDestructOnPingMaxOperations(int i) throws SQLException;

    boolean getUseColumnNamesInFindColumn();

    void setUseColumnNamesInFindColumn(boolean z);

    boolean getUseLocalTransactionState();

    void setUseLocalTransactionState(boolean z);

    boolean getCompensateOnDuplicateKeyUpdateCounts();

    void setCompensateOnDuplicateKeyUpdateCounts(boolean z);

    void setUseAffectedRows(boolean z);

    boolean getUseAffectedRows();

    void setPasswordCharacterEncoding(String str);

    String getPasswordCharacterEncoding();

    int getLoadBalanceBlacklistTimeout();

    void setLoadBalanceBlacklistTimeout(int i) throws SQLException;

    void setRetriesAllDown(int i) throws SQLException;

    int getRetriesAllDown();

    ExceptionInterceptor getExceptionInterceptor();

    void setExceptionInterceptors(String str);

    String getExceptionInterceptors();

    boolean getQueryTimeoutKillsConnection();

    void setQueryTimeoutKillsConnection(boolean z);

    int getMaxAllowedPacket();

    boolean getRetainStatementAfterResultSetClose();

    int getLoadBalancePingTimeout();

    void setLoadBalancePingTimeout(int i) throws SQLException;

    boolean getLoadBalanceValidateConnectionOnSwapServer();

    void setLoadBalanceValidateConnectionOnSwapServer(boolean z);

    String getLoadBalanceConnectionGroup();

    void setLoadBalanceConnectionGroup(String str);

    String getLoadBalanceExceptionChecker();

    void setLoadBalanceExceptionChecker(String str);

    String getLoadBalanceSQLStateFailover();

    void setLoadBalanceSQLStateFailover(String str);

    String getLoadBalanceSQLExceptionSubclassFailover();

    void setLoadBalanceSQLExceptionSubclassFailover(String str);

    boolean getLoadBalanceEnableJMX();

    void setLoadBalanceEnableJMX(boolean z);

    void setLoadBalanceHostRemovalGracePeriod(int i) throws SQLException;

    int getLoadBalanceHostRemovalGracePeriod();

    void setLoadBalanceAutoCommitStatementThreshold(int i) throws SQLException;

    int getLoadBalanceAutoCommitStatementThreshold();

    void setLoadBalanceAutoCommitStatementRegex(String str);

    String getLoadBalanceAutoCommitStatementRegex();

    void setAuthenticationPlugins(String str);

    String getAuthenticationPlugins();

    void setDisabledAuthenticationPlugins(String str);

    String getDisabledAuthenticationPlugins();

    void setDefaultAuthenticationPlugin(String str);

    String getDefaultAuthenticationPlugin();

    void setParseInfoCacheFactory(String str);

    String getParseInfoCacheFactory();

    void setServerConfigCacheFactory(String str);

    String getServerConfigCacheFactory();

    void setDisconnectOnExpiredPasswords(boolean z);

    boolean getDisconnectOnExpiredPasswords();

    boolean getAllowMasterDownConnections();

    void setAllowMasterDownConnections(boolean z);

    boolean getAllowSlaveDownConnections();

    void setAllowSlaveDownConnections(boolean z);

    boolean getReadFromMasterWhenNoSlaves();

    void setReadFromMasterWhenNoSlaves(boolean z);

    boolean getReplicationEnableJMX();

    void setReplicationEnableJMX(boolean z);

    void setGetProceduresReturnsFunctions(boolean z);

    boolean getGetProceduresReturnsFunctions();

    void setDetectCustomCollations(boolean z);

    boolean getDetectCustomCollations();

    String getConnectionAttributes() throws SQLException;

    String getServerRSAPublicKeyFile();

    void setServerRSAPublicKeyFile(String str) throws SQLException;

    boolean getAllowPublicKeyRetrieval();

    void setAllowPublicKeyRetrieval(boolean z) throws SQLException;

    void setDontCheckOnDuplicateKeyUpdateInSQL(boolean z);

    boolean getDontCheckOnDuplicateKeyUpdateInSQL();

    void setSocksProxyHost(String str);

    String getSocksProxyHost();

    void setSocksProxyPort(int i) throws SQLException;

    int getSocksProxyPort();

    boolean getReadOnlyPropagatesToServer();

    void setReadOnlyPropagatesToServer(boolean z);

    String getEnabledSSLCipherSuites();

    void setEnabledSSLCipherSuites(String str);

    String getEnabledTLSProtocols();

    void setEnabledTLSProtocols(String str);

    boolean getEnableEscapeProcessing();

    void setEnableEscapeProcessing(boolean z);
}
