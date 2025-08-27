package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import com.mysql.jdbc.profiler.ProfilerEventHandler;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MySQLConnection.class */
public interface MySQLConnection extends Connection, ConnectionProperties {
    boolean isProxySet();

    void createNewIO(boolean z) throws SQLException;

    void dumpTestcaseQuery(String str);

    Connection duplicate() throws SQLException;

    ResultSetInternalMethods execSQL(StatementImpl statementImpl, String str, int i, Buffer buffer, int i2, int i3, boolean z, String str2, Field[] fieldArr) throws SQLException;

    ResultSetInternalMethods execSQL(StatementImpl statementImpl, String str, int i, Buffer buffer, int i2, int i3, boolean z, String str2, Field[] fieldArr, boolean z2) throws SQLException;

    String extractSqlFromPacket(String str, Buffer buffer, int i) throws SQLException;

    StringBuilder generateConnectionCommentBlock(StringBuilder sb);

    @Override // com.mysql.jdbc.Connection
    int getActiveStatementCount();

    @Override // com.mysql.jdbc.Connection
    int getAutoIncrementIncrement();

    CachedResultSetMetaData getCachedMetaData(String str);

    Calendar getCalendarInstanceForSessionOrNew();

    Timer getCancelTimer();

    String getCharacterSetMetadata();

    SingleByteCharsetConverter getCharsetConverter(String str) throws SQLException;

    @Deprecated
    String getCharsetNameForIndex(int i) throws SQLException;

    String getEncodingForIndex(int i) throws SQLException;

    TimeZone getDefaultTimeZone();

    String getErrorMessageEncoding();

    ExceptionInterceptor getExceptionInterceptor();

    @Override // com.mysql.jdbc.Connection
    String getHost();

    String getHostPortPair();

    long getId();

    @Override // com.mysql.jdbc.Connection
    long getIdleFor();

    MysqlIO getIO() throws SQLException;

    @Override // com.mysql.jdbc.Connection
    Log getLog() throws SQLException;

    int getMaxBytesPerChar(String str) throws SQLException;

    int getMaxBytesPerChar(Integer num, String str) throws SQLException;

    java.sql.Statement getMetadataSafeStatement() throws SQLException;

    int getNetBufferLength();

    @Override // com.mysql.jdbc.Connection
    Properties getProperties();

    boolean getRequiresEscapingEncoder();

    @Override // com.mysql.jdbc.Connection
    String getServerCharset();

    int getServerMajorVersion();

    int getServerMinorVersion();

    int getServerSubMinorVersion();

    @Override // com.mysql.jdbc.Connection
    TimeZone getServerTimezoneTZ();

    String getServerVariable(String str);

    String getServerVersion();

    Calendar getSessionLockedCalendar();

    @Override // com.mysql.jdbc.Connection
    String getStatementComment();

    List<StatementInterceptorV2> getStatementInterceptorsInstances();

    String getURL();

    String getUser();

    Calendar getUtcCalendar();

    void incrementNumberOfPreparedExecutes();

    void incrementNumberOfPrepares();

    void incrementNumberOfResultSetsCreated();

    void initializeResultsMetadataFromCache(String str, CachedResultSetMetaData cachedResultSetMetaData, ResultSetInternalMethods resultSetInternalMethods) throws SQLException;

    void initializeSafeStatementInterceptors() throws SQLException;

    @Override // com.mysql.jdbc.Connection
    boolean isAbonormallyLongQuery(long j);

    boolean isClientTzUTC();

    boolean isCursorFetchEnabled() throws SQLException;

    boolean isReadInfoMsgEnabled();

    @Override // java.sql.Connection
    boolean isReadOnly() throws SQLException;

    boolean isReadOnly(boolean z) throws SQLException;

    boolean isRunningOnJDK13();

    boolean isServerTzUTC();

    @Override // com.mysql.jdbc.Connection
    boolean lowerCaseTableNames();

    void pingInternal(boolean z, int i) throws SQLException;

    void realClose(boolean z, boolean z2, boolean z3, Throwable th) throws SQLException;

    void recachePreparedStatement(ServerPreparedStatement serverPreparedStatement) throws SQLException;

    void decachePreparedStatement(ServerPreparedStatement serverPreparedStatement) throws SQLException;

    void registerQueryExecutionTime(long j);

    void registerStatement(Statement statement);

    void reportNumberOfTablesAccessed(int i);

    boolean serverSupportsConvertFn() throws SQLException;

    @Override // com.mysql.jdbc.Connection
    void setProxy(MySQLConnection mySQLConnection);

    void setReadInfoMsgEnabled(boolean z);

    void setReadOnlyInternal(boolean z) throws SQLException;

    @Override // com.mysql.jdbc.Connection
    void shutdownServer() throws SQLException;

    boolean storesLowerCaseTableName();

    void throwConnectionClosedException() throws SQLException;

    void transactionBegun() throws SQLException;

    void transactionCompleted() throws SQLException;

    void unregisterStatement(Statement statement);

    void unSafeStatementInterceptors() throws SQLException;

    boolean useAnsiQuotedIdentifiers();

    String getConnectionAttributes() throws SQLException;

    @Deprecated
    MySQLConnection getLoadBalanceSafeProxy();

    MySQLConnection getMultiHostSafeProxy();

    MySQLConnection getActiveMySQLConnection();

    ProfilerEventHandler getProfilerEventHandlerInstance();

    void setProfilerEventHandlerInstance(ProfilerEventHandler profilerEventHandler);

    boolean isServerTruncatesFracSecs();

    String getQueryTimingUnits();
}
