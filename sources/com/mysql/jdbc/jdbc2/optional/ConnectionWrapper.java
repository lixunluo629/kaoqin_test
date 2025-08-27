package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.Extension;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.MysqlErrorNumbers;
import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.Util;
import com.mysql.jdbc.log.Log;
import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.Executor;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/jdbc2/optional/ConnectionWrapper.class */
public class ConnectionWrapper extends WrapperBase implements Connection {
    protected Connection mc;
    private String invalidHandleStr;
    private boolean closed;
    private boolean isForXa;
    private static final Constructor<?> JDBC_4_CONNECTION_WRAPPER_CTOR;

    static {
        if (!Util.isJdbc4()) {
            JDBC_4_CONNECTION_WRAPPER_CTOR = null;
            return;
        }
        try {
            JDBC_4_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4ConnectionWrapper").getConstructor(MysqlPooledConnection.class, Connection.class, Boolean.TYPE);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException(e2);
        } catch (SecurityException e3) {
            throw new RuntimeException(e3);
        }
    }

    protected static ConnectionWrapper getInstance(MysqlPooledConnection mysqlPooledConnection, Connection mysqlConnection, boolean forXa) throws SQLException {
        return !Util.isJdbc4() ? new ConnectionWrapper(mysqlPooledConnection, mysqlConnection, forXa) : (ConnectionWrapper) Util.handleNewInstance(JDBC_4_CONNECTION_WRAPPER_CTOR, new Object[]{mysqlPooledConnection, mysqlConnection, Boolean.valueOf(forXa)}, mysqlPooledConnection.getExceptionInterceptor());
    }

    public ConnectionWrapper(MysqlPooledConnection mysqlPooledConnection, Connection mysqlConnection, boolean forXa) throws SQLException {
        super(mysqlPooledConnection);
        this.mc = null;
        this.invalidHandleStr = "Logical handle no longer valid";
        this.mc = mysqlConnection;
        this.closed = false;
        this.isForXa = forXa;
        if (this.isForXa) {
            setInGlobalTx(false);
        }
    }

    @Override // java.sql.Connection
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();
        if (autoCommit && isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            this.mc.setAutoCommit(autoCommit);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public boolean getAutoCommit() throws SQLException {
        checkClosed();
        try {
            return this.mc.getAutoCommit();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return false;
        }
    }

    @Override // java.sql.Connection
    public void setCatalog(String catalog) throws SQLException {
        checkClosed();
        try {
            this.mc.setCatalog(catalog);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public String getCatalog() throws SQLException {
        checkClosed();
        try {
            return this.mc.getCatalog();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public boolean isClosed() throws SQLException {
        return this.closed || this.mc.isClosed();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isMasterConnection() {
        return this.mc.isMasterConnection();
    }

    @Override // java.sql.Connection
    public void setHoldability(int arg0) throws SQLException {
        checkClosed();
        try {
            this.mc.setHoldability(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public int getHoldability() throws SQLException {
        checkClosed();
        try {
            return this.mc.getHoldability();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return 1;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public long getIdleFor() {
        return this.mc.getIdleFor();
    }

    @Override // java.sql.Connection
    public DatabaseMetaData getMetaData() throws SQLException {
        checkClosed();
        try {
            return this.mc.getMetaData();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public void setReadOnly(boolean readOnly) throws SQLException {
        checkClosed();
        try {
            this.mc.setReadOnly(readOnly);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public boolean isReadOnly() throws SQLException {
        checkClosed();
        try {
            return this.mc.isReadOnly();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return false;
        }
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint() throws SQLException {
        checkClosed();
        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            return this.mc.setSavepoint();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Savepoint setSavepoint(String arg0) throws SQLException {
        checkClosed();
        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't set autocommit to 'true' on an XAConnection", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            return this.mc.setSavepoint(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public void setTransactionIsolation(int level) throws SQLException {
        checkClosed();
        try {
            this.mc.setTransactionIsolation(level);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public int getTransactionIsolation() throws SQLException {
        checkClosed();
        try {
            return this.mc.getTransactionIsolation();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return 4;
        }
    }

    @Override // java.sql.Connection
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkClosed();
        try {
            return this.mc.getTypeMap();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public SQLWarning getWarnings() throws SQLException {
        checkClosed();
        try {
            return this.mc.getWarnings();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public void clearWarnings() throws SQLException {
        checkClosed();
        try {
            this.mc.clearWarnings();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection, java.lang.AutoCloseable
    public void close() throws SQLException {
        close(true);
    }

    @Override // java.sql.Connection
    public void commit() throws SQLException {
        checkClosed();
        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call commit() on an XAConnection associated with a global transaction", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            this.mc.commit();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public Statement createStatement() throws SQLException {
        checkClosed();
        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement());
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();
        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException {
        checkClosed();
        try {
            return StatementWrapper.getInstance(this, this.pooledConnection, this.mc.createStatement(arg0, arg1, arg2));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public String nativeSQL(String sql) throws SQLException {
        checkClosed();
        try {
            return this.mc.nativeSQL(sql);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql) throws SQLException {
        checkClosed();
        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();
        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException {
        checkClosed();
        try {
            return CallableStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareCall(arg0, arg1, arg2, arg3));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    public PreparedStatement clientPrepare(String sql) throws SQLException {
        checkClosed();
        try {
            return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    public PreparedStatement clientPrepare(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();
        try {
            return new PreparedStatementWrapper(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkClosed();
        PreparedStatement res = null;
        try {
            res = PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
        return res;
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1, arg2, arg3));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.prepareStatement(arg0, arg1));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // java.sql.Connection
    public void releaseSavepoint(Savepoint arg0) throws SQLException {
        checkClosed();
        try {
            this.mc.releaseSavepoint(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public void rollback() throws SQLException {
        checkClosed();
        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            this.mc.rollback();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // java.sql.Connection
    public void rollback(Savepoint arg0) throws SQLException {
        checkClosed();
        if (isInGlobalTx()) {
            throw SQLError.createSQLException("Can't call rollback() on an XAConnection associated with a global transaction", SQLError.SQL_STATE_INVALID_TRANSACTION_TERMINATION, MysqlErrorNumbers.ER_XA_RMERR, this.exceptionInterceptor);
        }
        try {
            this.mc.rollback(arg0);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isSameResource(Connection c) {
        if (c instanceof ConnectionWrapper) {
            return this.mc.isSameResource(((ConnectionWrapper) c).mc);
        }
        return this.mc.isSameResource(c);
    }

    protected void close(boolean fireClosedEvent) throws SQLException {
        synchronized (this.pooledConnection) {
            if (this.closed) {
                return;
            }
            if (!isInGlobalTx() && this.mc.getRollbackOnPooledClose() && !getAutoCommit()) {
                rollback();
            }
            if (fireClosedEvent) {
                this.pooledConnection.callConnectionEventListeners(2, null);
            }
            this.closed = true;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void checkClosed() throws SQLException {
        if (this.closed) {
            throw SQLError.createSQLException(this.invalidHandleStr, this.exceptionInterceptor);
        }
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isInGlobalTx() {
        return this.mc.isInGlobalTx();
    }

    @Override // com.mysql.jdbc.Connection
    public void setInGlobalTx(boolean flag) {
        this.mc.setInGlobalTx(flag);
    }

    @Override // com.mysql.jdbc.Connection
    public void ping() throws SQLException {
        if (this.mc != null) {
            this.mc.ping();
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void changeUser(String userName, String newPassword) throws SQLException {
        checkClosed();
        try {
            this.mc.changeUser(userName, newPassword);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void clearHasTriedMaster() {
        this.mc.clearHasTriedMaster();
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyIndex));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyIndexes));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement clientPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.clientPrepareStatement(sql, autoGenKeyColNames));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public int getActiveStatementCount() {
        return this.mc.getActiveStatementCount();
    }

    @Override // com.mysql.jdbc.Connection
    public Log getLog() throws SQLException {
        return this.mc.getLog();
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public String getServerCharacterEncoding() {
        return getServerCharset();
    }

    @Override // com.mysql.jdbc.Connection
    public String getServerCharset() {
        return this.mc.getServerCharset();
    }

    @Override // com.mysql.jdbc.Connection
    public TimeZone getServerTimezoneTZ() {
        return this.mc.getServerTimezoneTZ();
    }

    @Override // com.mysql.jdbc.Connection
    public String getStatementComment() {
        return this.mc.getStatementComment();
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public boolean hasTriedMaster() {
        return this.mc.hasTriedMaster();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isAbonormallyLongQuery(long millisOrNanos) {
        return this.mc.isAbonormallyLongQuery(millisOrNanos);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isNoBackslashEscapesSet() {
        return this.mc.isNoBackslashEscapesSet();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean lowerCaseTableNames() {
        return this.mc.lowerCaseTableNames();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean parserKnowsUnicode() {
        return this.mc.parserKnowsUnicode();
    }

    @Override // com.mysql.jdbc.Connection
    public void reportQueryTime(long millisOrNanos) {
        this.mc.reportQueryTime(millisOrNanos);
    }

    @Override // com.mysql.jdbc.Connection
    public void resetServerState() throws SQLException {
        checkClosed();
        try {
            this.mc.resetServerState();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql) throws SQLException {
        checkClosed();
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int autoGenKeyIndex) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyIndex));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, int[] autoGenKeyIndexes) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyIndexes));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public PreparedStatement serverPrepareStatement(String sql, String[] autoGenKeyColNames) throws SQLException {
        try {
            return PreparedStatementWrapper.getInstance(this, this.pooledConnection, this.mc.serverPrepareStatement(sql, autoGenKeyColNames));
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.Connection
    public void setFailedOver(boolean flag) {
        this.mc.setFailedOver(flag);
    }

    @Override // com.mysql.jdbc.Connection
    @Deprecated
    public void setPreferSlaveDuringFailover(boolean flag) {
        this.mc.setPreferSlaveDuringFailover(flag);
    }

    @Override // com.mysql.jdbc.Connection
    public void setStatementComment(String comment) {
        this.mc.setStatementComment(comment);
    }

    @Override // com.mysql.jdbc.Connection
    public void shutdownServer() throws SQLException {
        checkClosed();
        try {
            this.mc.shutdownServer();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsIsolationLevel() {
        return this.mc.supportsIsolationLevel();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsQuotedIdentifiers() {
        return this.mc.supportsQuotedIdentifiers();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean supportsTransactions() {
        return this.mc.supportsTransactions();
    }

    @Override // com.mysql.jdbc.Connection
    public boolean versionMeetsMinimum(int major, int minor, int subminor) throws SQLException {
        checkClosed();
        try {
            return this.mc.versionMeetsMinimum(major, minor, subminor);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return false;
        }
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String exposeAsXml() throws SQLException {
        checkClosed();
        try {
            return this.mc.exposeAsXml();
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
            return null;
        }
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowLoadLocalInfile() {
        return this.mc.getAllowLoadLocalInfile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowMultiQueries() {
        return this.mc.getAllowMultiQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowNanAndInf() {
        return this.mc.getAllowNanAndInf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowUrlInLocalInfile() {
        return this.mc.getAllowUrlInLocalInfile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAlwaysSendSetIsolation() {
        return this.mc.getAlwaysSendSetIsolation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoClosePStmtStreams() {
        return this.mc.getAutoClosePStmtStreams();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoDeserialize() {
        return this.mc.getAutoDeserialize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoGenerateTestcaseScript() {
        return this.mc.getAutoGenerateTestcaseScript();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoReconnectForPools() {
        return this.mc.getAutoReconnectForPools();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAutoSlowLog() {
        return this.mc.getAutoSlowLog();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getBlobSendChunkSize() {
        return this.mc.getBlobSendChunkSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getBlobsAreStrings() {
        return this.mc.getBlobsAreStrings();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheCallableStatements() {
        return this.mc.getCacheCallableStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheCallableStmts() {
        return this.mc.getCacheCallableStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCachePrepStmts() {
        return this.mc.getCachePrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCachePreparedStatements() {
        return this.mc.getCachePreparedStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheResultSetMetadata() {
        return this.mc.getCacheResultSetMetadata();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheServerConfiguration() {
        return this.mc.getCacheServerConfiguration();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getCallableStatementCacheSize() {
        return this.mc.getCallableStatementCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getCallableStmtCacheSize() {
        return this.mc.getCallableStmtCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCapitalizeTypeNames() {
        return this.mc.getCapitalizeTypeNames();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getCharacterSetResults() {
        return this.mc.getCharacterSetResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStorePassword() {
        return this.mc.getClientCertificateKeyStorePassword();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStoreType() {
        return this.mc.getClientCertificateKeyStoreType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientCertificateKeyStoreUrl() {
        return this.mc.getClientCertificateKeyStoreUrl();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClientInfoProvider() {
        return this.mc.getClientInfoProvider();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getClobCharacterEncoding() {
        return this.mc.getClobCharacterEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getClobberStreamingResults() {
        return this.mc.getClobberStreamingResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getConnectTimeout() {
        return this.mc.getConnectTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getConnectionCollation() {
        return this.mc.getConnectionCollation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getConnectionLifecycleInterceptors() {
        return this.mc.getConnectionLifecycleInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getContinueBatchOnError() {
        return this.mc.getContinueBatchOnError();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCreateDatabaseIfNotExist() {
        return this.mc.getCreateDatabaseIfNotExist();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getDefaultFetchSize() {
        return this.mc.getDefaultFetchSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDontTrackOpenResources() {
        return this.mc.getDontTrackOpenResources();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDumpMetadataOnColumnNotFound() {
        return this.mc.getDumpMetadataOnColumnNotFound();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDumpQueriesOnException() {
        return this.mc.getDumpQueriesOnException();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDynamicCalendars() {
        return this.mc.getDynamicCalendars();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getElideSetAutoCommits() {
        return this.mc.getElideSetAutoCommits();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmptyStringsConvertToZero() {
        return this.mc.getEmptyStringsConvertToZero();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmulateLocators() {
        return this.mc.getEmulateLocators();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEmulateUnsupportedPstmts() {
        return this.mc.getEmulateUnsupportedPstmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnablePacketDebug() {
        return this.mc.getEnablePacketDebug();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnableQueryTimeouts() {
        return this.mc.getEnableQueryTimeouts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEncoding() {
        return this.mc.getEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getExplainSlowQueries() {
        return this.mc.getExplainSlowQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getFailOverReadOnly() {
        return this.mc.getFailOverReadOnly();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getFunctionsNeverReturnBlobs() {
        return this.mc.getFunctionsNeverReturnBlobs();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGatherPerfMetrics() {
        return this.mc.getGatherPerfMetrics();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGatherPerformanceMetrics() {
        return this.mc.getGatherPerformanceMetrics();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGenerateSimpleParameterMetadata() {
        return this.mc.getGenerateSimpleParameterMetadata();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getHoldResultsOpenOverStatementClose() {
        return this.mc.getHoldResultsOpenOverStatementClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIgnoreNonTxTables() {
        return this.mc.getIgnoreNonTxTables();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeInnodbStatusInDeadlockExceptions() {
        return this.mc.getIncludeInnodbStatusInDeadlockExceptions();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getInitialTimeout() {
        return this.mc.getInitialTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getInteractiveClient() {
        return this.mc.getInteractiveClient();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIsInteractiveClient() {
        return this.mc.getIsInteractiveClient();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getJdbcCompliantTruncation() {
        return this.mc.getJdbcCompliantTruncation();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getJdbcCompliantTruncationForReads() {
        return this.mc.getJdbcCompliantTruncationForReads();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLargeRowSizeThreshold() {
        return this.mc.getLargeRowSizeThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceStrategy() {
        return this.mc.getLoadBalanceStrategy();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerAffinityOrder() {
        return this.mc.getServerAffinityOrder();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLocalSocketAddress() {
        return this.mc.getLocalSocketAddress();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLocatorFetchBufferSize() {
        return this.mc.getLocatorFetchBufferSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLogSlowQueries() {
        return this.mc.getLogSlowQueries();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLogXaCommands() {
        return this.mc.getLogXaCommands();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLogger() {
        return this.mc.getLogger();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoggerClassName() {
        return this.mc.getLoggerClassName();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getMaintainTimeStats() {
        return this.mc.getMaintainTimeStats();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxQuerySizeToLog() {
        return this.mc.getMaxQuerySizeToLog();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxReconnects() {
        return this.mc.getMaxReconnects();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxRows() {
        return this.mc.getMaxRows();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMetadataCacheSize() {
        return this.mc.getMetadataCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getNetTimeoutForStreamingResults() {
        return this.mc.getNetTimeoutForStreamingResults();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoAccessToProcedureBodies() {
        return this.mc.getNoAccessToProcedureBodies();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoDatetimeStringSync() {
        return this.mc.getNoDatetimeStringSync();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoTimezoneConversionForTimeType() {
        return this.mc.getNoTimezoneConversionForTimeType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNoTimezoneConversionForDateType() {
        return this.mc.getNoTimezoneConversionForDateType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCacheDefaultTimezone() {
        return this.mc.getCacheDefaultTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNullCatalogMeansCurrent() {
        return this.mc.getNullCatalogMeansCurrent();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getNullNamePatternMatchesAll() {
        return this.mc.getNullNamePatternMatchesAll();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getOverrideSupportsIntegrityEnhancementFacility() {
        return this.mc.getOverrideSupportsIntegrityEnhancementFacility();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPacketDebugBufferSize() {
        return this.mc.getPacketDebugBufferSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPadCharsWithSpace() {
        return this.mc.getPadCharsWithSpace();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getParanoid() {
        return this.mc.getParanoid();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPedantic() {
        return this.mc.getPedantic();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPinGlobalTxToPhysicalConnection() {
        return this.mc.getPinGlobalTxToPhysicalConnection();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getPopulateInsertRowWithDefaultValues() {
        return this.mc.getPopulateInsertRowWithDefaultValues();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPrepStmtCacheSize() {
        return this.mc.getPrepStmtCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPrepStmtCacheSqlLimit() {
        return this.mc.getPrepStmtCacheSqlLimit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPreparedStatementCacheSize() {
        return this.mc.getPreparedStatementCacheSize();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getPreparedStatementCacheSqlLimit() {
        return this.mc.getPreparedStatementCacheSqlLimit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProcessEscapeCodesForPrepStmts() {
        return this.mc.getProcessEscapeCodesForPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProfileSQL() {
        return this.mc.getProfileSQL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getProfileSql() {
        return this.mc.getProfileSql();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getPropertiesTransform() {
        return this.mc.getPropertiesTransform();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getQueriesBeforeRetryMaster() {
        return this.mc.getQueriesBeforeRetryMaster();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReconnectAtTxEnd() {
        return this.mc.getReconnectAtTxEnd();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRelaxAutoCommit() {
        return this.mc.getRelaxAutoCommit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getReportMetricsIntervalMillis() {
        return this.mc.getReportMetricsIntervalMillis();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRequireSSL() {
        return this.mc.getRequireSSL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getResourceId() {
        return this.mc.getResourceId();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getResultSetSizeThreshold() {
        return this.mc.getResultSetSizeThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRewriteBatchedStatements() {
        return this.mc.getRewriteBatchedStatements();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRollbackOnPooledClose() {
        return this.mc.getRollbackOnPooledClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRoundRobinLoadBalance() {
        return this.mc.getRoundRobinLoadBalance();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRunningCTS13() {
        return this.mc.getRunningCTS13();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSecondsBeforeRetryMaster() {
        return this.mc.getSecondsBeforeRetryMaster();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerTimezone() {
        return this.mc.getServerTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSessionVariables() {
        return this.mc.getSessionVariables();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSlowQueryThresholdMillis() {
        return this.mc.getSlowQueryThresholdMillis();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public long getSlowQueryThresholdNanos() {
        return this.mc.getSlowQueryThresholdNanos();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocketFactory() {
        return this.mc.getSocketFactory();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocketFactoryClassName() {
        return this.mc.getSocketFactoryClassName();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSocketTimeout() {
        return this.mc.getSocketTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getStatementInterceptors() {
        return this.mc.getStatementInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getStrictFloatingPoint() {
        return this.mc.getStrictFloatingPoint();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getStrictUpdates() {
        return this.mc.getStrictUpdates();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTcpKeepAlive() {
        return this.mc.getTcpKeepAlive();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTcpNoDelay() {
        return this.mc.getTcpNoDelay();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpRcvBuf() {
        return this.mc.getTcpRcvBuf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpSndBuf() {
        return this.mc.getTcpSndBuf();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getTcpTrafficClass() {
        return this.mc.getTcpTrafficClass();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTinyInt1isBit() {
        return this.mc.getTinyInt1isBit();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTraceProtocol() {
        return this.mc.getTraceProtocol();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTransformedBitIsBoolean() {
        return this.mc.getTransformedBitIsBoolean();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getTreatUtilDateAsTimestamp() {
        return this.mc.getTreatUtilDateAsTimestamp();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStorePassword() {
        return this.mc.getTrustCertificateKeyStorePassword();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStoreType() {
        return this.mc.getTrustCertificateKeyStoreType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getTrustCertificateKeyStoreUrl() {
        return this.mc.getTrustCertificateKeyStoreUrl();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUltraDevHack() {
        return this.mc.getUltraDevHack();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseBlobToStoreUTF8OutsideBMP() {
        return this.mc.getUseBlobToStoreUTF8OutsideBMP();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseCompression() {
        return this.mc.getUseCompression();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUseConfigs() {
        return this.mc.getUseConfigs();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseCursorFetch() {
        return this.mc.getUseCursorFetch();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseDirectRowUnpack() {
        return this.mc.getUseDirectRowUnpack();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseDynamicCharsetInfo() {
        return this.mc.getUseDynamicCharsetInfo();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseFastDateParsing() {
        return this.mc.getUseFastDateParsing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseFastIntParsing() {
        return this.mc.getUseFastIntParsing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseGmtMillisForDatetimes() {
        return this.mc.getUseGmtMillisForDatetimes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseHostsInPrivileges() {
        return this.mc.getUseHostsInPrivileges();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseInformationSchema() {
        return this.mc.getUseInformationSchema();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseJDBCCompliantTimezoneShift() {
        return this.mc.getUseJDBCCompliantTimezoneShift();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseJvmCharsetConverters() {
        return this.mc.getUseJvmCharsetConverters();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLocalSessionState() {
        return this.mc.getUseLocalSessionState();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseNanosForElapsedTime() {
        return this.mc.getUseNanosForElapsedTime();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOldAliasMetadataBehavior() {
        return this.mc.getUseOldAliasMetadataBehavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOldUTF8Behavior() {
        return this.mc.getUseOldUTF8Behavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseOnlyServerErrorMessages() {
        return this.mc.getUseOnlyServerErrorMessages();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseReadAheadInput() {
        return this.mc.getUseReadAheadInput();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSSL() {
        return this.mc.getUseSSL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSSPSCompatibleTimezoneShift() {
        return this.mc.getUseSSPSCompatibleTimezoneShift();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseServerPrepStmts() {
        return this.mc.getUseServerPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseServerPreparedStmts() {
        return this.mc.getUseServerPreparedStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseSqlStateCodes() {
        return this.mc.getUseSqlStateCodes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseStreamLengthsInPrepStmts() {
        return this.mc.getUseStreamLengthsInPrepStmts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseTimezone() {
        return this.mc.getUseTimezone();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUltraDevWorkAround() {
        return this.mc.getUseUltraDevWorkAround();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUnbufferedInput() {
        return this.mc.getUseUnbufferedInput();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUnicode() {
        return this.mc.getUseUnicode();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseUsageAdvisor() {
        return this.mc.getUseUsageAdvisor();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUtf8OutsideBmpExcludedColumnNamePattern() {
        return this.mc.getUtf8OutsideBmpExcludedColumnNamePattern();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getUtf8OutsideBmpIncludedColumnNamePattern() {
        return this.mc.getUtf8OutsideBmpIncludedColumnNamePattern();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getYearIsDateType() {
        return this.mc.getYearIsDateType();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getZeroDateTimeBehavior() {
        return this.mc.getZeroDateTimeBehavior();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowLoadLocalInfile(boolean property) {
        this.mc.setAllowLoadLocalInfile(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowMultiQueries(boolean property) {
        this.mc.setAllowMultiQueries(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowNanAndInf(boolean flag) {
        this.mc.setAllowNanAndInf(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowUrlInLocalInfile(boolean flag) {
        this.mc.setAllowUrlInLocalInfile(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAlwaysSendSetIsolation(boolean flag) {
        this.mc.setAlwaysSendSetIsolation(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoClosePStmtStreams(boolean flag) {
        this.mc.setAutoClosePStmtStreams(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoDeserialize(boolean flag) {
        this.mc.setAutoDeserialize(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoGenerateTestcaseScript(boolean flag) {
        this.mc.setAutoGenerateTestcaseScript(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnect(boolean flag) {
        this.mc.setAutoReconnect(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForConnectionPools(boolean property) {
        this.mc.setAutoReconnectForConnectionPools(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoReconnectForPools(boolean flag) {
        this.mc.setAutoReconnectForPools(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAutoSlowLog(boolean flag) {
        this.mc.setAutoSlowLog(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setBlobSendChunkSize(String value) throws SQLException {
        this.mc.setBlobSendChunkSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setBlobsAreStrings(boolean flag) {
        this.mc.setBlobsAreStrings(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStatements(boolean flag) {
        this.mc.setCacheCallableStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheCallableStmts(boolean flag) {
        this.mc.setCacheCallableStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCachePrepStmts(boolean flag) {
        this.mc.setCachePrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCachePreparedStatements(boolean flag) {
        this.mc.setCachePreparedStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheResultSetMetadata(boolean property) {
        this.mc.setCacheResultSetMetadata(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheServerConfiguration(boolean flag) {
        this.mc.setCacheServerConfiguration(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCallableStatementCacheSize(int size) throws SQLException {
        this.mc.setCallableStatementCacheSize(size);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCallableStmtCacheSize(int cacheSize) throws SQLException {
        this.mc.setCallableStmtCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeDBMDTypes(boolean property) {
        this.mc.setCapitalizeDBMDTypes(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCapitalizeTypeNames(boolean flag) {
        this.mc.setCapitalizeTypeNames(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCharacterEncoding(String encoding) {
        this.mc.setCharacterEncoding(encoding);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCharacterSetResults(String characterSet) {
        this.mc.setCharacterSetResults(characterSet);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStorePassword(String value) {
        this.mc.setClientCertificateKeyStorePassword(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreType(String value) {
        this.mc.setClientCertificateKeyStoreType(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientCertificateKeyStoreUrl(String value) {
        this.mc.setClientCertificateKeyStoreUrl(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClientInfoProvider(String classname) {
        this.mc.setClientInfoProvider(classname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClobCharacterEncoding(String encoding) {
        this.mc.setClobCharacterEncoding(encoding);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setClobberStreamingResults(boolean flag) {
        this.mc.setClobberStreamingResults(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectTimeout(int timeoutMs) throws SQLException {
        this.mc.setConnectTimeout(timeoutMs);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectionCollation(String collation) {
        this.mc.setConnectionCollation(collation);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setConnectionLifecycleInterceptors(String interceptors) {
        this.mc.setConnectionLifecycleInterceptors(interceptors);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setContinueBatchOnError(boolean property) {
        this.mc.setContinueBatchOnError(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCreateDatabaseIfNotExist(boolean flag) {
        this.mc.setCreateDatabaseIfNotExist(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDefaultFetchSize(int n) throws SQLException {
        this.mc.setDefaultFetchSize(n);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDetectServerPreparedStmts(boolean property) {
        this.mc.setDetectServerPreparedStmts(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDontTrackOpenResources(boolean flag) {
        this.mc.setDontTrackOpenResources(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDumpMetadataOnColumnNotFound(boolean flag) {
        this.mc.setDumpMetadataOnColumnNotFound(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDumpQueriesOnException(boolean flag) {
        this.mc.setDumpQueriesOnException(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDynamicCalendars(boolean flag) {
        this.mc.setDynamicCalendars(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setElideSetAutoCommits(boolean flag) {
        this.mc.setElideSetAutoCommits(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmptyStringsConvertToZero(boolean flag) {
        this.mc.setEmptyStringsConvertToZero(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmulateLocators(boolean property) {
        this.mc.setEmulateLocators(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEmulateUnsupportedPstmts(boolean flag) {
        this.mc.setEmulateUnsupportedPstmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnablePacketDebug(boolean flag) {
        this.mc.setEnablePacketDebug(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnableQueryTimeouts(boolean flag) {
        this.mc.setEnableQueryTimeouts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEncoding(String property) {
        this.mc.setEncoding(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setExplainSlowQueries(boolean flag) {
        this.mc.setExplainSlowQueries(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setFailOverReadOnly(boolean flag) {
        this.mc.setFailOverReadOnly(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setFunctionsNeverReturnBlobs(boolean flag) {
        this.mc.setFunctionsNeverReturnBlobs(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGatherPerfMetrics(boolean flag) {
        this.mc.setGatherPerfMetrics(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGatherPerformanceMetrics(boolean flag) {
        this.mc.setGatherPerformanceMetrics(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGenerateSimpleParameterMetadata(boolean flag) {
        this.mc.setGenerateSimpleParameterMetadata(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setHoldResultsOpenOverStatementClose(boolean flag) {
        this.mc.setHoldResultsOpenOverStatementClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIgnoreNonTxTables(boolean property) {
        this.mc.setIgnoreNonTxTables(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeInnodbStatusInDeadlockExceptions(boolean flag) {
        this.mc.setIncludeInnodbStatusInDeadlockExceptions(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setInitialTimeout(int property) throws SQLException {
        this.mc.setInitialTimeout(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setInteractiveClient(boolean property) {
        this.mc.setInteractiveClient(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIsInteractiveClient(boolean property) {
        this.mc.setIsInteractiveClient(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncation(boolean flag) {
        this.mc.setJdbcCompliantTruncation(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setJdbcCompliantTruncationForReads(boolean jdbcCompliantTruncationForReads) {
        this.mc.setJdbcCompliantTruncationForReads(jdbcCompliantTruncationForReads);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLargeRowSizeThreshold(String value) throws SQLException {
        this.mc.setLargeRowSizeThreshold(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceStrategy(String strategy) {
        this.mc.setLoadBalanceStrategy(strategy);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerAffinityOrder(String hostsList) {
        this.mc.setServerAffinityOrder(hostsList);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLocalSocketAddress(String address) {
        this.mc.setLocalSocketAddress(address);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLocatorFetchBufferSize(String value) throws SQLException {
        this.mc.setLocatorFetchBufferSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogSlowQueries(boolean flag) {
        this.mc.setLogSlowQueries(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogXaCommands(boolean flag) {
        this.mc.setLogXaCommands(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLogger(String property) {
        this.mc.setLogger(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoggerClassName(String className) {
        this.mc.setLoggerClassName(className);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaintainTimeStats(boolean flag) {
        this.mc.setMaintainTimeStats(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxQuerySizeToLog(int sizeInBytes) throws SQLException {
        this.mc.setMaxQuerySizeToLog(sizeInBytes);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxReconnects(int property) throws SQLException {
        this.mc.setMaxReconnects(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMaxRows(int property) throws SQLException {
        this.mc.setMaxRows(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setMetadataCacheSize(int value) throws SQLException {
        this.mc.setMetadataCacheSize(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNetTimeoutForStreamingResults(int value) throws SQLException {
        this.mc.setNetTimeoutForStreamingResults(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoAccessToProcedureBodies(boolean flag) {
        this.mc.setNoAccessToProcedureBodies(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoDatetimeStringSync(boolean flag) {
        this.mc.setNoDatetimeStringSync(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoTimezoneConversionForTimeType(boolean flag) {
        this.mc.setNoTimezoneConversionForTimeType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNoTimezoneConversionForDateType(boolean flag) {
        this.mc.setNoTimezoneConversionForDateType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCacheDefaultTimezone(boolean flag) {
        this.mc.setCacheDefaultTimezone(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNullCatalogMeansCurrent(boolean value) {
        this.mc.setNullCatalogMeansCurrent(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setNullNamePatternMatchesAll(boolean value) {
        this.mc.setNullNamePatternMatchesAll(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setOverrideSupportsIntegrityEnhancementFacility(boolean flag) {
        this.mc.setOverrideSupportsIntegrityEnhancementFacility(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPacketDebugBufferSize(int size) throws SQLException {
        this.mc.setPacketDebugBufferSize(size);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPadCharsWithSpace(boolean flag) {
        this.mc.setPadCharsWithSpace(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setParanoid(boolean property) {
        this.mc.setParanoid(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPedantic(boolean property) {
        this.mc.setPedantic(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPinGlobalTxToPhysicalConnection(boolean flag) {
        this.mc.setPinGlobalTxToPhysicalConnection(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPopulateInsertRowWithDefaultValues(boolean flag) {
        this.mc.setPopulateInsertRowWithDefaultValues(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSize(int cacheSize) throws SQLException {
        this.mc.setPrepStmtCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPrepStmtCacheSqlLimit(int sqlLimit) throws SQLException {
        this.mc.setPrepStmtCacheSqlLimit(sqlLimit);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSize(int cacheSize) throws SQLException {
        this.mc.setPreparedStatementCacheSize(cacheSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPreparedStatementCacheSqlLimit(int cacheSqlLimit) throws SQLException {
        this.mc.setPreparedStatementCacheSqlLimit(cacheSqlLimit);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProcessEscapeCodesForPrepStmts(boolean flag) {
        this.mc.setProcessEscapeCodesForPrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfileSQL(boolean flag) {
        this.mc.setProfileSQL(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfileSql(boolean property) {
        this.mc.setProfileSql(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPropertiesTransform(String value) {
        this.mc.setPropertiesTransform(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setQueriesBeforeRetryMaster(int property) throws SQLException {
        this.mc.setQueriesBeforeRetryMaster(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReconnectAtTxEnd(boolean property) {
        this.mc.setReconnectAtTxEnd(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRelaxAutoCommit(boolean property) {
        this.mc.setRelaxAutoCommit(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReportMetricsIntervalMillis(int millis) throws SQLException {
        this.mc.setReportMetricsIntervalMillis(millis);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRequireSSL(boolean property) {
        this.mc.setRequireSSL(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setResourceId(String resourceId) {
        this.mc.setResourceId(resourceId);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setResultSetSizeThreshold(int threshold) throws SQLException {
        this.mc.setResultSetSizeThreshold(threshold);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRetainStatementAfterResultSetClose(boolean flag) {
        this.mc.setRetainStatementAfterResultSetClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRewriteBatchedStatements(boolean flag) {
        this.mc.setRewriteBatchedStatements(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRollbackOnPooledClose(boolean flag) {
        this.mc.setRollbackOnPooledClose(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRoundRobinLoadBalance(boolean flag) {
        this.mc.setRoundRobinLoadBalance(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRunningCTS13(boolean flag) {
        this.mc.setRunningCTS13(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSecondsBeforeRetryMaster(int property) throws SQLException {
        this.mc.setSecondsBeforeRetryMaster(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerTimezone(String property) {
        this.mc.setServerTimezone(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSessionVariables(String variables) {
        this.mc.setSessionVariables(variables);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdMillis(int millis) throws SQLException {
        this.mc.setSlowQueryThresholdMillis(millis);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSlowQueryThresholdNanos(long nanos) throws SQLException {
        this.mc.setSlowQueryThresholdNanos(nanos);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketFactory(String name) {
        this.mc.setSocketFactory(name);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketFactoryClassName(String property) {
        this.mc.setSocketFactoryClassName(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocketTimeout(int property) throws SQLException {
        this.mc.setSocketTimeout(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStatementInterceptors(String value) {
        this.mc.setStatementInterceptors(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStrictFloatingPoint(boolean property) {
        this.mc.setStrictFloatingPoint(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setStrictUpdates(boolean property) {
        this.mc.setStrictUpdates(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpKeepAlive(boolean flag) {
        this.mc.setTcpKeepAlive(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpNoDelay(boolean flag) {
        this.mc.setTcpNoDelay(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpRcvBuf(int bufSize) throws SQLException {
        this.mc.setTcpRcvBuf(bufSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpSndBuf(int bufSize) throws SQLException {
        this.mc.setTcpSndBuf(bufSize);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTcpTrafficClass(int classFlags) throws SQLException {
        this.mc.setTcpTrafficClass(classFlags);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTinyInt1isBit(boolean flag) {
        this.mc.setTinyInt1isBit(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTraceProtocol(boolean flag) {
        this.mc.setTraceProtocol(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTransformedBitIsBoolean(boolean flag) {
        this.mc.setTransformedBitIsBoolean(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTreatUtilDateAsTimestamp(boolean flag) {
        this.mc.setTreatUtilDateAsTimestamp(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStorePassword(String value) {
        this.mc.setTrustCertificateKeyStorePassword(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreType(String value) {
        this.mc.setTrustCertificateKeyStoreType(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setTrustCertificateKeyStoreUrl(String value) {
        this.mc.setTrustCertificateKeyStoreUrl(value);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUltraDevHack(boolean flag) {
        this.mc.setUltraDevHack(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseBlobToStoreUTF8OutsideBMP(boolean flag) {
        this.mc.setUseBlobToStoreUTF8OutsideBMP(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseCompression(boolean property) {
        this.mc.setUseCompression(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseConfigs(String configs) {
        this.mc.setUseConfigs(configs);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseCursorFetch(boolean flag) {
        this.mc.setUseCursorFetch(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseDirectRowUnpack(boolean flag) {
        this.mc.setUseDirectRowUnpack(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseDynamicCharsetInfo(boolean flag) {
        this.mc.setUseDynamicCharsetInfo(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseFastDateParsing(boolean flag) {
        this.mc.setUseFastDateParsing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseFastIntParsing(boolean flag) {
        this.mc.setUseFastIntParsing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseGmtMillisForDatetimes(boolean flag) {
        this.mc.setUseGmtMillisForDatetimes(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseHostsInPrivileges(boolean property) {
        this.mc.setUseHostsInPrivileges(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseInformationSchema(boolean flag) {
        this.mc.setUseInformationSchema(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseJDBCCompliantTimezoneShift(boolean flag) {
        this.mc.setUseJDBCCompliantTimezoneShift(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseJvmCharsetConverters(boolean flag) {
        this.mc.setUseJvmCharsetConverters(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLocalSessionState(boolean flag) {
        this.mc.setUseLocalSessionState(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseNanosForElapsedTime(boolean flag) {
        this.mc.setUseNanosForElapsedTime(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOldAliasMetadataBehavior(boolean flag) {
        this.mc.setUseOldAliasMetadataBehavior(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOldUTF8Behavior(boolean flag) {
        this.mc.setUseOldUTF8Behavior(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseOnlyServerErrorMessages(boolean flag) {
        this.mc.setUseOnlyServerErrorMessages(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseReadAheadInput(boolean flag) {
        this.mc.setUseReadAheadInput(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSSL(boolean property) {
        this.mc.setUseSSL(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSSPSCompatibleTimezoneShift(boolean flag) {
        this.mc.setUseSSPSCompatibleTimezoneShift(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseServerPrepStmts(boolean flag) {
        this.mc.setUseServerPrepStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseServerPreparedStmts(boolean flag) {
        this.mc.setUseServerPreparedStmts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseSqlStateCodes(boolean flag) {
        this.mc.setUseSqlStateCodes(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseStreamLengthsInPrepStmts(boolean property) {
        this.mc.setUseStreamLengthsInPrepStmts(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseTimezone(boolean property) {
        this.mc.setUseTimezone(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUltraDevWorkAround(boolean property) {
        this.mc.setUseUltraDevWorkAround(property);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUnbufferedInput(boolean flag) {
        this.mc.setUseUnbufferedInput(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUnicode(boolean flag) {
        this.mc.setUseUnicode(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseUsageAdvisor(boolean useUsageAdvisorFlag) {
        this.mc.setUseUsageAdvisor(useUsageAdvisorFlag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpExcludedColumnNamePattern(String regexPattern) {
        this.mc.setUtf8OutsideBmpExcludedColumnNamePattern(regexPattern);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUtf8OutsideBmpIncludedColumnNamePattern(String regexPattern) {
        this.mc.setUtf8OutsideBmpIncludedColumnNamePattern(regexPattern);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setYearIsDateType(boolean flag) {
        this.mc.setYearIsDateType(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setZeroDateTimeBehavior(String behavior) {
        this.mc.setZeroDateTimeBehavior(behavior);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean useUnbufferedInput() {
        return this.mc.useUnbufferedInput();
    }

    @Override // com.mysql.jdbc.Connection
    public void initializeExtension(Extension ex) throws SQLException {
        this.mc.initializeExtension(ex);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getProfilerEventHandler() {
        return this.mc.getProfilerEventHandler();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setProfilerEventHandler(String handler) {
        this.mc.setProfilerEventHandler(handler);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getVerifyServerCertificate() {
        return this.mc.getVerifyServerCertificate();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setVerifyServerCertificate(boolean flag) {
        this.mc.setVerifyServerCertificate(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLegacyDatetimeCode() {
        return this.mc.getUseLegacyDatetimeCode();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLegacyDatetimeCode(boolean flag) {
        this.mc.setUseLegacyDatetimeCode(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getSendFractionalSeconds() {
        return this.mc.getSendFractionalSeconds();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSendFractionalSeconds(boolean flag) {
        this.mc.setSendFractionalSeconds(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSelfDestructOnPingMaxOperations() {
        return this.mc.getSelfDestructOnPingMaxOperations();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSelfDestructOnPingSecondsLifetime() {
        return this.mc.getSelfDestructOnPingSecondsLifetime();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingMaxOperations(int maxOperations) throws SQLException {
        this.mc.setSelfDestructOnPingMaxOperations(maxOperations);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSelfDestructOnPingSecondsLifetime(int seconds) throws SQLException {
        this.mc.setSelfDestructOnPingSecondsLifetime(seconds);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseColumnNamesInFindColumn() {
        return this.mc.getUseColumnNamesInFindColumn();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseColumnNamesInFindColumn(boolean flag) {
        this.mc.setUseColumnNamesInFindColumn(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseLocalTransactionState() {
        return this.mc.getUseLocalTransactionState();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseLocalTransactionState(boolean flag) {
        this.mc.setUseLocalTransactionState(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getCompensateOnDuplicateKeyUpdateCounts() {
        return this.mc.getCompensateOnDuplicateKeyUpdateCounts();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setCompensateOnDuplicateKeyUpdateCounts(boolean flag) {
        this.mc.setCompensateOnDuplicateKeyUpdateCounts(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getUseAffectedRows() {
        return this.mc.getUseAffectedRows();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setUseAffectedRows(boolean flag) {
        this.mc.setUseAffectedRows(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getPasswordCharacterEncoding() {
        return this.mc.getPasswordCharacterEncoding();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setPasswordCharacterEncoding(String characterSet) {
        this.mc.setPasswordCharacterEncoding(characterSet);
    }

    @Override // com.mysql.jdbc.Connection
    public int getAutoIncrementIncrement() {
        return this.mc.getAutoIncrementIncrement();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceBlacklistTimeout() {
        return this.mc.getLoadBalanceBlacklistTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceBlacklistTimeout(int loadBalanceBlacklistTimeout) throws SQLException {
        this.mc.setLoadBalanceBlacklistTimeout(loadBalanceBlacklistTimeout);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalancePingTimeout() {
        return this.mc.getLoadBalancePingTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalancePingTimeout(int loadBalancePingTimeout) throws SQLException {
        this.mc.setLoadBalancePingTimeout(loadBalancePingTimeout);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLoadBalanceValidateConnectionOnSwapServer() {
        return this.mc.getLoadBalanceValidateConnectionOnSwapServer();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceValidateConnectionOnSwapServer(boolean loadBalanceValidateConnectionOnSwapServer) {
        this.mc.setLoadBalanceValidateConnectionOnSwapServer(loadBalanceValidateConnectionOnSwapServer);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setRetriesAllDown(int retriesAllDown) throws SQLException {
        this.mc.setRetriesAllDown(retriesAllDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getRetriesAllDown() {
        return this.mc.getRetriesAllDown();
    }

    @Override // com.mysql.jdbc.ConnectionProperties, com.mysql.jdbc.MySQLConnection
    public ExceptionInterceptor getExceptionInterceptor() {
        return this.pooledConnection.getExceptionInterceptor();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getExceptionInterceptors() {
        return this.mc.getExceptionInterceptors();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setExceptionInterceptors(String exceptionInterceptors) {
        this.mc.setExceptionInterceptors(exceptionInterceptors);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getQueryTimeoutKillsConnection() {
        return this.mc.getQueryTimeoutKillsConnection();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setQueryTimeoutKillsConnection(boolean queryTimeoutKillsConnection) {
        this.mc.setQueryTimeoutKillsConnection(queryTimeoutKillsConnection);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean hasSameProperties(Connection c) {
        return this.mc.hasSameProperties(c);
    }

    @Override // com.mysql.jdbc.Connection
    public Properties getProperties() {
        return this.mc.getProperties();
    }

    @Override // com.mysql.jdbc.Connection
    public String getHost() {
        return this.mc.getHost();
    }

    @Override // com.mysql.jdbc.Connection
    public void setProxy(MySQLConnection conn) {
        this.mc.setProxy(conn);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getRetainStatementAfterResultSetClose() {
        return this.mc.getRetainStatementAfterResultSetClose();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getMaxAllowedPacket() {
        return this.mc.getMaxAllowedPacket();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceConnectionGroup() {
        return this.mc.getLoadBalanceConnectionGroup();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getLoadBalanceEnableJMX() {
        return this.mc.getLoadBalanceEnableJMX();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceExceptionChecker() {
        return this.mc.getLoadBalanceExceptionChecker();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceSQLExceptionSubclassFailover() {
        return this.mc.getLoadBalanceSQLExceptionSubclassFailover();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceSQLStateFailover() {
        return this.mc.getLoadBalanceSQLStateFailover();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceConnectionGroup(String loadBalanceConnectionGroup) {
        this.mc.setLoadBalanceConnectionGroup(loadBalanceConnectionGroup);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceEnableJMX(boolean loadBalanceEnableJMX) {
        this.mc.setLoadBalanceEnableJMX(loadBalanceEnableJMX);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceExceptionChecker(String loadBalanceExceptionChecker) {
        this.mc.setLoadBalanceExceptionChecker(loadBalanceExceptionChecker);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLExceptionSubclassFailover(String loadBalanceSQLExceptionSubclassFailover) {
        this.mc.setLoadBalanceSQLExceptionSubclassFailover(loadBalanceSQLExceptionSubclassFailover);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceSQLStateFailover(String loadBalanceSQLStateFailover) {
        this.mc.setLoadBalanceSQLStateFailover(loadBalanceSQLStateFailover);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getLoadBalanceAutoCommitStatementRegex() {
        return this.mc.getLoadBalanceAutoCommitStatementRegex();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceAutoCommitStatementThreshold() {
        return this.mc.getLoadBalanceAutoCommitStatementThreshold();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementRegex(String loadBalanceAutoCommitStatementRegex) {
        this.mc.setLoadBalanceAutoCommitStatementRegex(loadBalanceAutoCommitStatementRegex);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceAutoCommitStatementThreshold(int loadBalanceAutoCommitStatementThreshold) throws SQLException {
        this.mc.setLoadBalanceAutoCommitStatementThreshold(loadBalanceAutoCommitStatementThreshold);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setLoadBalanceHostRemovalGracePeriod(int loadBalanceHostRemovalGracePeriod) throws SQLException {
        this.mc.setLoadBalanceHostRemovalGracePeriod(loadBalanceHostRemovalGracePeriod);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getLoadBalanceHostRemovalGracePeriod() {
        return this.mc.getLoadBalanceHostRemovalGracePeriod();
    }

    @Override // java.sql.Connection
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkClosed();
        try {
            this.mc.setTypeMap(map);
        } catch (SQLException sqlException) {
            checkAndFireConnectionError(sqlException);
        }
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeThreadDumpInDeadlockExceptions() {
        return this.mc.getIncludeThreadDumpInDeadlockExceptions();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadDumpInDeadlockExceptions(boolean flag) {
        this.mc.setIncludeThreadDumpInDeadlockExceptions(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getIncludeThreadNamesAsStatementComment() {
        return this.mc.getIncludeThreadNamesAsStatementComment();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setIncludeThreadNamesAsStatementComment(boolean flag) {
        this.mc.setIncludeThreadNamesAsStatementComment(flag);
    }

    @Override // com.mysql.jdbc.Connection
    public boolean isServerLocal() throws SQLException {
        return this.mc.isServerLocal();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAuthenticationPlugins(String authenticationPlugins) {
        this.mc.setAuthenticationPlugins(authenticationPlugins);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getAuthenticationPlugins() {
        return this.mc.getAuthenticationPlugins();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDisabledAuthenticationPlugins(String disabledAuthenticationPlugins) {
        this.mc.setDisabledAuthenticationPlugins(disabledAuthenticationPlugins);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getDisabledAuthenticationPlugins() {
        return this.mc.getDisabledAuthenticationPlugins();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDefaultAuthenticationPlugin(String defaultAuthenticationPlugin) {
        this.mc.setDefaultAuthenticationPlugin(defaultAuthenticationPlugin);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getDefaultAuthenticationPlugin() {
        return this.mc.getDefaultAuthenticationPlugin();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setParseInfoCacheFactory(String factoryClassname) {
        this.mc.setParseInfoCacheFactory(factoryClassname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getParseInfoCacheFactory() {
        return this.mc.getParseInfoCacheFactory();
    }

    @Override // com.mysql.jdbc.Connection
    public void setSchema(String schema) throws SQLException {
        this.mc.setSchema(schema);
    }

    @Override // com.mysql.jdbc.Connection
    public String getSchema() throws SQLException {
        return this.mc.getSchema();
    }

    @Override // com.mysql.jdbc.Connection
    public void abort(Executor executor) throws SQLException {
        this.mc.abort(executor);
    }

    @Override // com.mysql.jdbc.Connection
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.mc.setNetworkTimeout(executor, milliseconds);
    }

    @Override // com.mysql.jdbc.Connection
    public int getNetworkTimeout() throws SQLException {
        return this.mc.getNetworkTimeout();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerConfigCacheFactory(String factoryClassname) {
        this.mc.setServerConfigCacheFactory(factoryClassname);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerConfigCacheFactory() {
        return this.mc.getServerConfigCacheFactory();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDisconnectOnExpiredPasswords(boolean disconnectOnExpiredPasswords) {
        this.mc.setDisconnectOnExpiredPasswords(disconnectOnExpiredPasswords);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDisconnectOnExpiredPasswords() {
        return this.mc.getDisconnectOnExpiredPasswords();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setGetProceduresReturnsFunctions(boolean getProcedureReturnsFunctions) {
        this.mc.setGetProceduresReturnsFunctions(getProcedureReturnsFunctions);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getGetProceduresReturnsFunctions() {
        return this.mc.getGetProceduresReturnsFunctions();
    }

    @Override // com.mysql.jdbc.Connection
    public void abortInternal() throws SQLException {
        this.mc.abortInternal();
    }

    @Override // com.mysql.jdbc.Connection
    public Object getConnectionMutex() {
        return this.mc.getConnectionMutex();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowMasterDownConnections() {
        return this.mc.getAllowMasterDownConnections();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowMasterDownConnections(boolean connectIfMasterDown) {
        this.mc.setAllowMasterDownConnections(connectIfMasterDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowSlaveDownConnections() {
        return this.mc.getAllowSlaveDownConnections();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowSlaveDownConnections(boolean connectIfSlaveDown) {
        this.mc.setAllowSlaveDownConnections(connectIfSlaveDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReadFromMasterWhenNoSlaves() {
        return this.mc.getReadFromMasterWhenNoSlaves();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReadFromMasterWhenNoSlaves(boolean useMasterIfSlavesDown) {
        this.mc.setReadFromMasterWhenNoSlaves(useMasterIfSlavesDown);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReplicationEnableJMX() {
        return this.mc.getReplicationEnableJMX();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReplicationEnableJMX(boolean replicationEnableJMX) {
        this.mc.setReplicationEnableJMX(replicationEnableJMX);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getConnectionAttributes() throws SQLException {
        return this.mc.getConnectionAttributes();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDetectCustomCollations(boolean detectCustomCollations) {
        this.mc.setDetectCustomCollations(detectCustomCollations);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDetectCustomCollations() {
        return this.mc.getDetectCustomCollations();
    }

    @Override // com.mysql.jdbc.Connection
    public int getSessionMaxRows() {
        return this.mc.getSessionMaxRows();
    }

    @Override // com.mysql.jdbc.Connection
    public void setSessionMaxRows(int max) throws SQLException {
        this.mc.setSessionMaxRows(max);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getServerRSAPublicKeyFile() {
        return this.mc.getServerRSAPublicKeyFile();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setServerRSAPublicKeyFile(String serverRSAPublicKeyFile) throws SQLException {
        this.mc.setServerRSAPublicKeyFile(serverRSAPublicKeyFile);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getAllowPublicKeyRetrieval() {
        return this.mc.getAllowPublicKeyRetrieval();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setAllowPublicKeyRetrieval(boolean allowPublicKeyRetrieval) throws SQLException {
        this.mc.setAllowPublicKeyRetrieval(allowPublicKeyRetrieval);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setDontCheckOnDuplicateKeyUpdateInSQL(boolean dontCheckOnDuplicateKeyUpdateInSQL) {
        this.mc.setDontCheckOnDuplicateKeyUpdateInSQL(dontCheckOnDuplicateKeyUpdateInSQL);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getDontCheckOnDuplicateKeyUpdateInSQL() {
        return this.mc.getDontCheckOnDuplicateKeyUpdateInSQL();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocksProxyHost(String socksProxyHost) {
        this.mc.setSocksProxyHost(socksProxyHost);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getSocksProxyHost() {
        return this.mc.getSocksProxyHost();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setSocksProxyPort(int socksProxyPort) throws SQLException {
        this.mc.setSocksProxyPort(socksProxyPort);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public int getSocksProxyPort() {
        return this.mc.getSocksProxyPort();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getReadOnlyPropagatesToServer() {
        return this.mc.getReadOnlyPropagatesToServer();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setReadOnlyPropagatesToServer(boolean flag) {
        this.mc.setReadOnlyPropagatesToServer(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEnabledSSLCipherSuites() {
        return this.mc.getEnabledSSLCipherSuites();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnabledSSLCipherSuites(String cipherSuites) {
        this.mc.setEnabledSSLCipherSuites(cipherSuites);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public String getEnabledTLSProtocols() {
        return this.mc.getEnabledTLSProtocols();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnabledTLSProtocols(String protocols) {
        this.mc.setEnabledTLSProtocols(protocols);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean getEnableEscapeProcessing() {
        return this.mc.getEnableEscapeProcessing();
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public void setEnableEscapeProcessing(boolean flag) {
        this.mc.setEnableEscapeProcessing(flag);
    }

    @Override // com.mysql.jdbc.ConnectionProperties
    public boolean isUseSSLExplicit() {
        return this.mc.isUseSSLExplicit();
    }
}
