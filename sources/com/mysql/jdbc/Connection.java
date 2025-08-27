package com.mysql.jdbc;

import com.mysql.jdbc.log.Log;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Connection.class */
public interface Connection extends java.sql.Connection, ConnectionProperties {
    void changeUser(String str, String str2) throws SQLException;

    @Deprecated
    void clearHasTriedMaster();

    java.sql.PreparedStatement clientPrepareStatement(String str) throws SQLException;

    java.sql.PreparedStatement clientPrepareStatement(String str, int i) throws SQLException;

    java.sql.PreparedStatement clientPrepareStatement(String str, int i, int i2) throws SQLException;

    java.sql.PreparedStatement clientPrepareStatement(String str, int[] iArr) throws SQLException;

    java.sql.PreparedStatement clientPrepareStatement(String str, int i, int i2, int i3) throws SQLException;

    java.sql.PreparedStatement clientPrepareStatement(String str, String[] strArr) throws SQLException;

    int getActiveStatementCount();

    long getIdleFor();

    Log getLog() throws SQLException;

    @Deprecated
    String getServerCharacterEncoding();

    String getServerCharset();

    TimeZone getServerTimezoneTZ();

    String getStatementComment();

    @Deprecated
    boolean hasTriedMaster();

    boolean isInGlobalTx();

    void setInGlobalTx(boolean z);

    boolean isMasterConnection();

    boolean isNoBackslashEscapesSet();

    boolean isSameResource(Connection connection);

    boolean lowerCaseTableNames();

    boolean parserKnowsUnicode();

    void ping() throws SQLException;

    void resetServerState() throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str) throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str, int i) throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str, int i, int i2) throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str, int i, int i2, int i3) throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str, int[] iArr) throws SQLException;

    java.sql.PreparedStatement serverPrepareStatement(String str, String[] strArr) throws SQLException;

    void setFailedOver(boolean z);

    @Deprecated
    void setPreferSlaveDuringFailover(boolean z);

    void setStatementComment(String str);

    void shutdownServer() throws SQLException;

    boolean supportsIsolationLevel();

    boolean supportsQuotedIdentifiers();

    boolean supportsTransactions();

    boolean versionMeetsMinimum(int i, int i2, int i3) throws SQLException;

    void reportQueryTime(long j);

    boolean isAbonormallyLongQuery(long j);

    void initializeExtension(Extension extension) throws SQLException;

    int getAutoIncrementIncrement();

    boolean hasSameProperties(Connection connection);

    Properties getProperties();

    String getHost();

    void setProxy(MySQLConnection mySQLConnection);

    boolean isServerLocal() throws SQLException;

    int getSessionMaxRows();

    void setSessionMaxRows(int i) throws SQLException;

    void setSchema(String str) throws SQLException;

    String getSchema() throws SQLException;

    void abort(Executor executor) throws SQLException;

    void setNetworkTimeout(Executor executor, int i) throws SQLException;

    int getNetworkTimeout() throws SQLException;

    void abortInternal() throws SQLException;

    void checkClosed() throws SQLException;

    Object getConnectionMutex();
}
