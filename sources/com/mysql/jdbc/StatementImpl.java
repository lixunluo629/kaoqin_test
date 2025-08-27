package com.mysql.jdbc;

import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
import com.mysql.jdbc.exceptions.MySQLTimeoutException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StatementImpl.class */
public class StatementImpl implements Statement {
    protected static final String PING_MARKER = "/* ping */";
    public static final byte USES_VARIABLES_FALSE = 0;
    public static final byte USES_VARIABLES_TRUE = 1;
    public static final byte USES_VARIABLES_UNKNOWN = -1;
    protected List<Object> batchedArgs;
    protected SingleByteCharsetConverter charConverter;
    protected String charEncoding;
    protected volatile MySQLConnection connection;
    protected String currentCatalog;
    protected boolean doEscapeProcessing;
    protected int maxFieldSize;
    protected boolean pedantic;
    protected boolean profileSQL;
    protected int statementId;
    protected boolean useUsageAdvisor;
    protected boolean holdResultsOpenOverClose;
    protected boolean continueBatchOnError;
    protected boolean useLegacyDatetimeCode;
    protected boolean sendFractionalSeconds;
    private ExceptionInterceptor exceptionInterceptor;
    private InputStream localInfileInputStream;
    protected final boolean version5013OrNewer;
    protected static final String[] ON_DUPLICATE_KEY_UPDATE_CLAUSE = {"ON", "DUPLICATE", "KEY", "UPDATE"};
    static int statementCounter = 1;
    protected Object cancelTimeoutMutex = new Object();
    protected boolean wasCancelled = false;
    protected boolean wasCancelledByTimeout = false;
    protected Reference<MySQLConnection> physicalConnection = null;
    private int fetchSize = 0;
    protected boolean isClosed = false;
    protected long lastInsertId = -1;
    protected int maxRows = -1;
    protected Set<ResultSetInternalMethods> openResults = new HashSet();
    protected ResultSetInternalMethods results = null;
    protected ResultSetInternalMethods generatedKeysResults = null;
    protected int resultSetConcurrency = 0;
    protected int resultSetType = 0;
    protected int timeoutInMillis = 0;
    protected long updateCount = -1;
    protected SQLWarning warningChain = null;
    protected boolean clearWarningsCalled = false;
    protected ArrayList<ResultSetRow> batchedGeneratedKeys = null;
    protected boolean retrieveGeneratedKeys = false;
    protected PingTarget pingTarget = null;
    protected boolean lastQueryIsOnDupKeyUpdate = false;
    protected final AtomicBoolean statementExecuting = new AtomicBoolean(false);
    private boolean isImplicitlyClosingResults = false;
    private int originalResultSetType = 0;
    private int originalFetchSize = 0;
    private boolean isPoolable = true;
    private boolean closeOnCompletion = false;

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StatementImpl$CancelTask.class */
    class CancelTask extends TimerTask {
        SQLException caughtWhileCancelling = null;
        StatementImpl toCancel;
        Properties origConnProps;
        String origConnURL;
        long origConnId;

        CancelTask(StatementImpl cancellee) throws SQLException {
            this.origConnProps = null;
            this.origConnURL = "";
            this.origConnId = 0L;
            this.toCancel = cancellee;
            this.origConnProps = new Properties();
            Properties props = StatementImpl.this.connection.getProperties();
            Enumeration<?> keys = props.propertyNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement().toString();
                this.origConnProps.setProperty(key, props.getProperty(key));
            }
            this.origConnURL = StatementImpl.this.connection.getURL();
            this.origConnId = StatementImpl.this.connection.getId();
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            Thread cancelThread = new Thread() { // from class: com.mysql.jdbc.StatementImpl.CancelTask.1
                /* JADX WARN: Code restructure failed: missing block: B:34:0x0129, code lost:
                
                    throw r15;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:51:0x0130, code lost:
                
                    r10.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:53:0x0149, code lost:
                
                    if (0 == 0) goto L47;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:54:0x014c, code lost:
                
                    r9.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:55:0x0164, code lost:
                
                    r8.this$1.toCancel = null;
                    r8.this$1.origConnProps = null;
                    r8.this$1.origConnURL = null;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:58:0x0130, code lost:
                
                    r10.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:60:0x0149, code lost:
                
                    if (0 == 0) goto L47;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:61:0x014c, code lost:
                
                    r9.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:62:0x0164, code lost:
                
                    r8.this$1.toCancel = null;
                    r8.this$1.origConnProps = null;
                    r8.this$1.origConnURL = null;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:65:0x0130, code lost:
                
                    r10.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:67:0x0149, code lost:
                
                    if (0 == 0) goto L47;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:68:0x014c, code lost:
                
                    r9.close();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:69:0x0164, code lost:
                
                    r8.this$1.toCancel = null;
                    r8.this$1.origConnProps = null;
                    r8.this$1.origConnURL = null;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:76:?, code lost:
                
                    return;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:77:?, code lost:
                
                    return;
                 */
                /* JADX WARN: Removed duplicated region for block: B:37:0x0130 A[Catch: SQLException -> 0x0139, TRY_ENTER, TRY_LEAVE, TryCatch #6 {SQLException -> 0x0139, blocks: (B:37:0x0130, B:51:0x0130, B:58:0x0130, B:65:0x0130), top: B:70:0x0004 }] */
                /* JADX WARN: Removed duplicated region for block: B:43:0x014c A[Catch: SQLException -> 0x0155, TRY_ENTER, TRY_LEAVE, TryCatch #4 {SQLException -> 0x0155, blocks: (B:43:0x014c, B:54:0x014c, B:61:0x014c, B:68:0x014c, B:3:0x0004, B:5:0x0019, B:7:0x0022, B:8:0x0050, B:9:0x005d, B:10:0x005e, B:12:0x0071, B:15:0x00e2, B:16:0x00fa, B:13:0x00a1, B:20:0x0102, B:22:0x0105, B:26:0x010d), top: B:73:0x0004, inners: #1 }] */
                @Override // java.lang.Thread, java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public void run() throws java.sql.SQLException {
                    /*
                        Method dump skipped, instructions count: 383
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.CancelTask.AnonymousClass1.run():void");
                }
            };
            cancelThread.start();
        }
    }

    public StatementImpl(MySQLConnection c, String catalog) throws SQLException {
        this.charConverter = null;
        this.charEncoding = null;
        this.connection = null;
        this.currentCatalog = null;
        this.doEscapeProcessing = true;
        this.maxFieldSize = MysqlIO.getMaxBuf();
        this.pedantic = false;
        this.profileSQL = false;
        this.useUsageAdvisor = false;
        this.holdResultsOpenOverClose = false;
        this.continueBatchOnError = false;
        if (c == null || c.isClosed()) {
            throw SQLError.createSQLException(Messages.getString("Statement.0"), SQLError.SQL_STATE_CONNECTION_NOT_OPEN, (ExceptionInterceptor) null);
        }
        this.connection = c;
        this.exceptionInterceptor = this.connection.getExceptionInterceptor();
        this.currentCatalog = catalog;
        this.pedantic = this.connection.getPedantic();
        this.continueBatchOnError = this.connection.getContinueBatchOnError();
        this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
        this.sendFractionalSeconds = this.connection.getSendFractionalSeconds();
        this.doEscapeProcessing = this.connection.getEnableEscapeProcessing();
        if (!this.connection.getDontTrackOpenResources()) {
            this.connection.registerStatement(this);
        }
        this.maxFieldSize = this.connection.getMaxAllowedPacket();
        int defaultFetchSize = this.connection.getDefaultFetchSize();
        if (defaultFetchSize != 0) {
            setFetchSize(defaultFetchSize);
        }
        if (this.connection.getUseUnicode()) {
            this.charEncoding = this.connection.getEncoding();
            this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
        }
        boolean profiling = this.connection.getProfileSql() || this.connection.getUseUsageAdvisor() || this.connection.getLogSlowQueries();
        if (this.connection.getAutoGenerateTestcaseScript() || profiling) {
            int i = statementCounter;
            statementCounter = i + 1;
            this.statementId = i;
        }
        if (profiling) {
            this.profileSQL = this.connection.getProfileSql();
            this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
        }
        int maxRowsConn = this.connection.getMaxRows();
        if (maxRowsConn != -1) {
            setMaxRows(maxRowsConn);
        }
        this.holdResultsOpenOverClose = this.connection.getHoldResultsOpenOverStatementClose();
        this.version5013OrNewer = this.connection.versionMeetsMinimum(5, 0, 13);
    }

    @Override // java.sql.Statement
    public void addBatch(String sql) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.batchedArgs == null) {
                this.batchedArgs = new ArrayList();
            }
            if (sql != null) {
                this.batchedArgs.add(sql);
            }
        }
    }

    public List<Object> getBatchedArgs() {
        if (this.batchedArgs == null) {
            return null;
        }
        return Collections.unmodifiableList(this.batchedArgs);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0072, code lost:
    
        throw r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0079, code lost:
    
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0080, code lost:
    
        if (r6 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0083, code lost:
    
        r6.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0079  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0083  */
    @Override // java.sql.Statement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void cancel() throws java.sql.SQLException {
        /*
            r5 = this;
            r0 = r5
            java.util.concurrent.atomic.AtomicBoolean r0 = r0.statementExecuting
            boolean r0 = r0.get()
            if (r0 != 0) goto Lb
            return
        Lb:
            r0 = r5
            boolean r0 = r0.isClosed
            if (r0 != 0) goto L8b
            r0 = r5
            com.mysql.jdbc.MySQLConnection r0 = r0.connection
            if (r0 == 0) goto L8b
            r0 = r5
            com.mysql.jdbc.MySQLConnection r0 = r0.connection
            r1 = 5
            r2 = 0
            r3 = 0
            boolean r0 = r0.versionMeetsMinimum(r1, r2, r3)
            if (r0 == 0) goto L8b
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            r0 = r5
            com.mysql.jdbc.MySQLConnection r0 = r0.connection     // Catch: java.lang.Throwable -> L6d
            com.mysql.jdbc.Connection r0 = r0.duplicate()     // Catch: java.lang.Throwable -> L6d
            r6 = r0
            r0 = r6
            java.sql.Statement r0 = r0.createStatement()     // Catch: java.lang.Throwable -> L6d
            r7 = r0
            r0 = r7
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6d
            r2 = r1
            r2.<init>()     // Catch: java.lang.Throwable -> L6d
            java.lang.String r2 = "KILL QUERY "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L6d
            r2 = r5
            com.mysql.jdbc.MySQLConnection r2 = r2.connection     // Catch: java.lang.Throwable -> L6d
            com.mysql.jdbc.MysqlIO r2 = r2.getIO()     // Catch: java.lang.Throwable -> L6d
            long r2 = r2.getThreadId()     // Catch: java.lang.Throwable -> L6d
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> L6d
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L6d
            boolean r0 = r0.execute(r1)     // Catch: java.lang.Throwable -> L6d
            r0 = r5
            r1 = 1
            r0.wasCancelled = r1     // Catch: java.lang.Throwable -> L6d
            r0 = jsr -> L73
        L6a:
            goto L8b
        L6d:
            r8 = move-exception
            r0 = jsr -> L73
        L71:
            r1 = r8
            throw r1
        L73:
            r9 = r0
            r0 = r7
            if (r0 == 0) goto L7f
            r0 = r7
            r0.close()
        L7f:
            r0 = r6
            if (r0 == 0) goto L89
            r0 = r6
            r0.close()
        L89:
            ret r9
        L8b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.cancel():void");
    }

    protected MySQLConnection checkClosed() throws SQLException {
        MySQLConnection c = this.connection;
        if (c == null) {
            throw SQLError.createSQLException(Messages.getString("Statement.49"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        return c;
    }

    protected void checkForDml(String sql, char firstStatementChar) throws SQLException, IOException {
        if (firstStatementChar == 'I' || firstStatementChar == 'U' || firstStatementChar == 'D' || firstStatementChar == 'A' || firstStatementChar == 'C' || firstStatementChar == 'T' || firstStatementChar == 'R') {
            String noCommentSql = StringUtils.stripComments(sql, "'\"", "'\"", true, false, true, true);
            if (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "INSERT") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "UPDATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DELETE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DROP") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "CREATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "ALTER") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "TRUNCATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "RENAME")) {
                throw SQLError.createSQLException(Messages.getString("Statement.57"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
        }
    }

    protected void checkNullOrEmptyQuery(String sql) throws SQLException {
        if (sql == null) {
            throw SQLError.createSQLException(Messages.getString("Statement.59"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        if (sql.length() == 0) {
            throw SQLError.createSQLException(Messages.getString("Statement.61"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.Statement
    public void clearBatch() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.batchedArgs != null) {
                this.batchedArgs.clear();
            }
        }
    }

    @Override // java.sql.Statement
    public void clearWarnings() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.clearWarningsCalled = true;
            this.warningChain = null;
        }
    }

    @Override // java.sql.Statement, java.lang.AutoCloseable
    public void close() throws SQLException {
        realClose(true, true);
    }

    protected void closeAllOpenResults() throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return;
        }
        synchronized (locallyScopedConn.getConnectionMutex()) {
            if (this.openResults != null) {
                for (ResultSetInternalMethods element : this.openResults) {
                    try {
                        element.realClose(false);
                    } catch (SQLException sqlEx) {
                        AssertionFailedException.shouldNotHappen(sqlEx);
                    }
                }
                this.openResults.clear();
            }
        }
    }

    protected void implicitlyCloseAllOpenResults() throws SQLException {
        this.isImplicitlyClosingResults = true;
        try {
            if (!this.connection.getHoldResultsOpenOverStatementClose() && !this.connection.getDontTrackOpenResources() && !this.holdResultsOpenOverClose) {
                if (this.results != null) {
                    this.results.realClose(false);
                }
                if (this.generatedKeysResults != null) {
                    this.generatedKeysResults.realClose(false);
                }
                closeAllOpenResults();
            }
        } finally {
            this.isImplicitlyClosingResults = false;
        }
    }

    @Override // com.mysql.jdbc.Statement
    public void removeOpenResultSet(ResultSetInternalMethods rs) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (this.openResults != null) {
                    this.openResults.remove(rs);
                }
                boolean hasMoreResults = rs.getNextResultSet() != null;
                if (this.results == rs && !hasMoreResults) {
                    this.results = null;
                }
                if (this.generatedKeysResults == rs) {
                    this.generatedKeysResults = null;
                }
                if (!this.isImplicitlyClosingResults && !hasMoreResults) {
                    checkAndPerformCloseOnCompletionAction();
                }
            }
        } catch (SQLException e) {
        }
    }

    @Override // com.mysql.jdbc.Statement
    public int getOpenResultSetCount() {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (this.openResults != null) {
                    return this.openResults.size();
                }
                return 0;
            }
        } catch (SQLException e) {
            return 0;
        }
    }

    private void checkAndPerformCloseOnCompletionAction() {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (isCloseOnCompletion() && !this.connection.getDontTrackOpenResources() && getOpenResultSetCount() == 0 && ((this.results == null || !this.results.reallyResult() || this.results.isClosed()) && (this.generatedKeysResults == null || !this.generatedKeysResults.reallyResult() || this.generatedKeysResults.isClosed()))) {
                    realClose(false, false);
                }
            }
        } catch (SQLException e) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ResultSetInternalMethods createResultSetUsingServerFetch(String sql) throws SQLException {
        ResultSetInternalMethods rs;
        synchronized (checkClosed().getConnectionMutex()) {
            java.sql.PreparedStatement preparedStatementPrepareStatement = this.connection.prepareStatement(sql, this.resultSetType, this.resultSetConcurrency);
            preparedStatementPrepareStatement.setFetchSize(this.fetchSize);
            if (this.maxRows > -1) {
                preparedStatementPrepareStatement.setMaxRows(this.maxRows);
            }
            statementBegins();
            preparedStatementPrepareStatement.execute();
            rs = ((StatementImpl) preparedStatementPrepareStatement).getResultSetInternal();
            rs.setStatementUsedForFetchingRows((PreparedStatement) preparedStatementPrepareStatement);
            this.results = rs;
        }
        return rs;
    }

    protected boolean createStreamingResultSet() {
        return this.resultSetType == 1003 && this.resultSetConcurrency == 1007 && this.fetchSize == Integer.MIN_VALUE;
    }

    @Override // com.mysql.jdbc.Statement
    public void enableStreamingResults() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.originalResultSetType = this.resultSetType;
            this.originalFetchSize = this.fetchSize;
            setFetchSize(Integer.MIN_VALUE);
            setResultSetType(1003);
        }
    }

    @Override // com.mysql.jdbc.Statement
    public void disableStreamingResults() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.fetchSize == Integer.MIN_VALUE && this.resultSetType == 1003) {
                setFetchSize(this.originalFetchSize);
                setResultSetType(this.originalResultSetType);
            }
        }
    }

    protected void setupStreamingTimeout(MySQLConnection con) throws SQLException {
        if (createStreamingResultSet() && con.getNetTimeoutForStreamingResults() > 0) {
            executeSimpleNonQuery(con, "SET net_write_timeout=" + con.getNetTimeoutForStreamingResults());
        }
    }

    @Override // java.sql.Statement
    public boolean execute(String sql) throws SQLException {
        return executeInternal(sql, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:134:0x023d, code lost:
    
        r21.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x024f, code lost:
    
        if (0 == 0) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0252, code lost:
    
        r0.setCatalog(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0235, code lost:
    
        throw r27;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0261 A[Catch: all -> 0x02d1, all -> 0x02ed, TryCatch #1 {all -> 0x02d1, blocks: (B:34:0x00c2, B:36:0x00ce, B:38:0x00e3, B:39:0x00ec, B:40:0x00f5, B:42:0x0107, B:103:0x0261, B:107:0x028a, B:108:0x029a, B:110:0x02a6, B:113:0x02b7, B:44:0x0117, B:46:0x0120, B:48:0x0127, B:50:0x0133, B:51:0x014e, B:53:0x015e, B:54:0x0170, B:56:0x017c, B:58:0x018a, B:61:0x0197, B:63:0x019f, B:65:0x01ce, B:67:0x01d6, B:68:0x01db, B:69:0x01dc, B:70:0x01e5, B:71:0x01ec, B:72:0x01ed, B:74:0x01f4, B:76:0x01fe, B:78:0x0213, B:79:0x0219, B:77:0x020a, B:81:0x021c, B:96:0x023d, B:99:0x0252, B:85:0x0224, B:87:0x0227, B:134:0x023d, B:137:0x0252, B:93:0x0235), top: B:141:0x00c2, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x023d A[Catch: all -> 0x02d1, all -> 0x02ed, TryCatch #1 {all -> 0x02d1, blocks: (B:34:0x00c2, B:36:0x00ce, B:38:0x00e3, B:39:0x00ec, B:40:0x00f5, B:42:0x0107, B:103:0x0261, B:107:0x028a, B:108:0x029a, B:110:0x02a6, B:113:0x02b7, B:44:0x0117, B:46:0x0120, B:48:0x0127, B:50:0x0133, B:51:0x014e, B:53:0x015e, B:54:0x0170, B:56:0x017c, B:58:0x018a, B:61:0x0197, B:63:0x019f, B:65:0x01ce, B:67:0x01d6, B:68:0x01db, B:69:0x01dc, B:70:0x01e5, B:71:0x01ec, B:72:0x01ed, B:74:0x01f4, B:76:0x01fe, B:78:0x0213, B:79:0x0219, B:77:0x020a, B:81:0x021c, B:96:0x023d, B:99:0x0252, B:85:0x0224, B:87:0x0227, B:134:0x023d, B:137:0x0252, B:93:0x0235), top: B:141:0x00c2, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0252 A[Catch: all -> 0x02d1, all -> 0x02ed, TryCatch #1 {all -> 0x02d1, blocks: (B:34:0x00c2, B:36:0x00ce, B:38:0x00e3, B:39:0x00ec, B:40:0x00f5, B:42:0x0107, B:103:0x0261, B:107:0x028a, B:108:0x029a, B:110:0x02a6, B:113:0x02b7, B:44:0x0117, B:46:0x0120, B:48:0x0127, B:50:0x0133, B:51:0x014e, B:53:0x015e, B:54:0x0170, B:56:0x017c, B:58:0x018a, B:61:0x0197, B:63:0x019f, B:65:0x01ce, B:67:0x01d6, B:68:0x01db, B:69:0x01dc, B:70:0x01e5, B:71:0x01ec, B:72:0x01ed, B:74:0x01f4, B:76:0x01fe, B:78:0x0213, B:79:0x0219, B:77:0x020a, B:81:0x021c, B:96:0x023d, B:99:0x0252, B:85:0x0224, B:87:0x0227, B:134:0x023d, B:137:0x0252, B:93:0x0235), top: B:141:0x00c2, outer: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean executeInternal(java.lang.String r12, boolean r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 757
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.executeInternal(java.lang.String, boolean):boolean");
    }

    protected void statementBegins() {
        this.clearWarningsCalled = false;
        this.statementExecuting.set(true);
        MySQLConnection activeMySQLConnection = this.connection.getMultiHostSafeProxy().getActiveMySQLConnection();
        while (true) {
            MySQLConnection physicalConn = activeMySQLConnection;
            if (!(physicalConn instanceof ConnectionImpl)) {
                activeMySQLConnection = physicalConn.getMultiHostSafeProxy().getActiveMySQLConnection();
            } else {
                this.physicalConnection = new WeakReference(physicalConn);
                return;
            }
        }
    }

    protected void resetCancelledState() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.cancelTimeoutMutex == null) {
                return;
            }
            synchronized (this.cancelTimeoutMutex) {
                this.wasCancelled = false;
                this.wasCancelledByTimeout = false;
            }
        }
    }

    @Override // java.sql.Statement
    public boolean execute(String sql, int returnGeneratedKeys) throws SQLException {
        return executeInternal(sql, returnGeneratedKeys == 1);
    }

    @Override // java.sql.Statement
    public boolean execute(String sql, int[] generatedKeyIndices) throws SQLException {
        return executeInternal(sql, generatedKeyIndices != null && generatedKeyIndices.length > 0);
    }

    @Override // java.sql.Statement
    public boolean execute(String sql, String[] generatedKeyNames) throws SQLException {
        return executeInternal(sql, generatedKeyNames != null && generatedKeyNames.length > 0);
    }

    @Override // java.sql.Statement
    public int[] executeBatch() throws SQLException {
        return Util.truncateAndConvertToInt(executeBatchInternal());
    }

    /* JADX WARN: Code restructure failed: missing block: B:104:0x025d, code lost:
    
        throw r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0265, code lost:
    
        r11.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x0275, code lost:
    
        resetCancelledState();
        r7.timeoutInMillis = r0;
        clearBatch();
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0265, code lost:
    
        r11.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x0275, code lost:
    
        resetCancelledState();
        r7.timeoutInMillis = r0;
        clearBatch();
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0241, code lost:
    
        return r13;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0265 A[Catch: all -> 0x0284, DONT_GENERATE, TryCatch #1 {, blocks: (B:4:0x000e, B:6:0x0017, B:7:0x003a, B:8:0x003b, B:10:0x0046, B:15:0x0058, B:16:0x0065, B:17:0x006d, B:19:0x007c, B:23:0x00b4, B:27:0x00c3, B:100:0x024a, B:107:0x0265, B:108:0x0275, B:30:0x00d5, B:33:0x00d9, B:37:0x00e6, B:39:0x00f2, B:40:0x010a, B:43:0x011a, B:47:0x0138, B:49:0x0164, B:53:0x0172, B:74:0x01eb, B:77:0x01f6, B:78:0x0201, B:55:0x017a, B:57:0x0189, B:59:0x0191, B:61:0x0199, B:64:0x01a9, B:67:0x01bb, B:69:0x01c3, B:72:0x01df, B:73:0x01ea, B:71:0x01d4, B:81:0x0207, B:83:0x020f, B:84:0x0214, B:85:0x0215, B:114:0x024a, B:118:0x0265, B:119:0x0275, B:93:0x023e, B:89:0x0232, B:115:0x024a, B:99:0x0249, B:122:0x0265, B:123:0x0275, B:104:0x025d, B:12:0x0052, B:13:0x0056), top: B:125:0x000e, inners: #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected long[] executeBatchInternal() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 651
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.executeBatchInternal():long[]");
    }

    protected final boolean hasDeadlockOrTimeoutRolledBackTx(SQLException ex) {
        int vendorCode = ex.getErrorCode();
        switch (vendorCode) {
            case MysqlErrorNumbers.ER_LOCK_WAIT_TIMEOUT /* 1205 */:
                return !this.version5013OrNewer;
            case MysqlErrorNumbers.ER_LOCK_TABLE_FULL /* 1206 */:
            case MysqlErrorNumbers.ER_LOCK_DEADLOCK /* 1213 */:
                return true;
            default:
                return false;
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0170 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0033 A[Catch: all -> 0x01f2, all -> 0x0244, LOOP:0: B:9:0x002d->B:11:0x0033, LOOP_END, TryCatch #0 {all -> 0x01f2, blocks: (B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:58:0x01e4, B:42:0x0183, B:22:0x00ad), top: B:109:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00a7  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00ad A[Catch: all -> 0x01f2, all -> 0x0244, TryCatch #0 {all -> 0x01f2, blocks: (B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:58:0x01e4, B:42:0x0183, B:22:0x00ad), top: B:109:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00cd  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00df A[Catch: all -> 0x01f2, all -> 0x0244, TryCatch #0 {all -> 0x01f2, blocks: (B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:58:0x01e4, B:42:0x0183, B:22:0x00ad), top: B:109:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01a7 A[Catch: all -> 0x01f2, all -> 0x0244, TryCatch #0 {all -> 0x01f2, blocks: (B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:58:0x01e4, B:42:0x0183, B:22:0x00ad), top: B:109:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01ce A[Catch: all -> 0x01f2, all -> 0x0244, TryCatch #0 {all -> 0x01f2, blocks: (B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:58:0x01e4, B:42:0x0183, B:22:0x00ad), top: B:109:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0201 A[Catch: all -> 0x0244, TryCatch #1 {, blocks: (B:6:0x0015, B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:70:0x0201, B:71:0x0212, B:74:0x021b, B:83:0x0236, B:61:0x01ee, B:93:0x0236, B:80:0x022f, B:58:0x01e4, B:42:0x0183, B:22:0x00ad, B:97:0x0201, B:98:0x0212, B:101:0x021b, B:105:0x0236, B:67:0x01f9), top: B:110:0x0015, inners: #0, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x021b A[Catch: all -> 0x0228, all -> 0x0244, TryCatch #3 {all -> 0x0228, blocks: (B:74:0x021b, B:101:0x021b), top: B:113:0x0025, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0236 A[Catch: all -> 0x0244, DONT_GENERATE, TryCatch #1 {, blocks: (B:6:0x0015, B:8:0x0025, B:11:0x0033, B:12:0x0041, B:16:0x0064, B:18:0x0071, B:19:0x008e, B:25:0x00b8, B:31:0x00df, B:33:0x0113, B:36:0x0134, B:37:0x014f, B:35:0x0126, B:38:0x0168, B:40:0x0170, B:43:0x0193, B:46:0x01a7, B:48:0x01af, B:49:0x01b4, B:50:0x01b5, B:53:0x01ce, B:54:0x01d9, B:70:0x0201, B:71:0x0212, B:74:0x021b, B:83:0x0236, B:61:0x01ee, B:93:0x0236, B:80:0x022f, B:58:0x01e4, B:42:0x0183, B:22:0x00ad, B:97:0x0201, B:98:0x0212, B:101:0x021b, B:105:0x0236, B:67:0x01f9), top: B:110:0x0015, inners: #0, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private long[] executeBatchUsingMultiQueries(boolean r7, int r8, int r9) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 588
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.executeBatchUsingMultiQueries(boolean, int, int):long[]");
    }

    /* JADX WARN: Type inference failed for: r0v20, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v32, types: [byte[], byte[][]] */
    protected int processMultiCountsAndKeys(StatementImpl batchedStatement, int updateCountCounter, long[] updateCounts) throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            int updateCountCounter2 = updateCountCounter + 1;
            updateCounts[updateCountCounter] = batchedStatement.getLargeUpdateCount();
            boolean doGenKeys = this.batchedGeneratedKeys != null;
            if (doGenKeys) {
                long generatedKey = batchedStatement.getLastInsertID();
                this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[]{StringUtils.getBytes(Long.toString(generatedKey))}, getExceptionInterceptor()));
            }
            while (true) {
                if (batchedStatement.getMoreResults() || batchedStatement.getLargeUpdateCount() != -1) {
                    int i2 = updateCountCounter2;
                    updateCountCounter2++;
                    updateCounts[i2] = batchedStatement.getLargeUpdateCount();
                    if (doGenKeys) {
                        long generatedKey2 = batchedStatement.getLastInsertID();
                        this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[]{StringUtils.getBytes(Long.toString(generatedKey2))}, getExceptionInterceptor()));
                    }
                } else {
                    i = updateCountCounter2;
                }
            }
        }
        return i;
    }

    protected SQLException handleExceptionForBatch(int endOfBatchIndex, int numValuesPerBatch, long[] updateCounts, SQLException ex) throws SQLException {
        for (int j = endOfBatchIndex; j > endOfBatchIndex - numValuesPerBatch; j--) {
            updateCounts[j] = -3;
        }
        if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) {
            return ex;
        }
        long[] newUpdateCounts = new long[endOfBatchIndex];
        System.arraycopy(updateCounts, 0, newUpdateCounts, 0, endOfBatchIndex);
        throw SQLError.createBatchUpdateException(ex, newUpdateCounts, getExceptionInterceptor());
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x01c9, code lost:
    
        throw r24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01d9, code lost:
    
        r18.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01eb, code lost:
    
        if (0 == 0) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01ee, code lost:
    
        r0.setCatalog(null);
     */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01d9 A[Catch: all -> 0x0239, TryCatch #2 {, blocks: (B:4:0x000c, B:6:0x002d, B:8:0x0036, B:9:0x003f, B:11:0x0041, B:13:0x004d, B:15:0x0065, B:16:0x006e, B:17:0x0077, B:19:0x0092, B:20:0x00a0, B:23:0x00a8, B:25:0x00b1, B:27:0x00b8, B:29:0x00c4, B:30:0x00df, B:32:0x00ef, B:33:0x0101, B:35:0x010d, B:37:0x011b, B:38:0x0122, B:40:0x0158, B:42:0x0160, B:43:0x0165, B:44:0x0166, B:45:0x0179, B:46:0x0180, B:47:0x0181, B:49:0x0188, B:51:0x0192, B:53:0x01a7, B:54:0x01ad, B:52:0x019e, B:56:0x01b0, B:69:0x01ca, B:71:0x01d9, B:74:0x01ee, B:76:0x01f8, B:78:0x020a, B:82:0x0232, B:83:0x0237, B:79:0x021a, B:81:0x0226, B:60:0x01b8, B:62:0x01bb, B:90:0x01ca, B:92:0x01d9, B:95:0x01ee, B:68:0x01c9), top: B:99:0x000c, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01ee A[Catch: all -> 0x0239, TryCatch #2 {, blocks: (B:4:0x000c, B:6:0x002d, B:8:0x0036, B:9:0x003f, B:11:0x0041, B:13:0x004d, B:15:0x0065, B:16:0x006e, B:17:0x0077, B:19:0x0092, B:20:0x00a0, B:23:0x00a8, B:25:0x00b1, B:27:0x00b8, B:29:0x00c4, B:30:0x00df, B:32:0x00ef, B:33:0x0101, B:35:0x010d, B:37:0x011b, B:38:0x0122, B:40:0x0158, B:42:0x0160, B:43:0x0165, B:44:0x0166, B:45:0x0179, B:46:0x0180, B:47:0x0181, B:49:0x0188, B:51:0x0192, B:53:0x01a7, B:54:0x01ad, B:52:0x019e, B:56:0x01b0, B:69:0x01ca, B:71:0x01d9, B:74:0x01ee, B:76:0x01f8, B:78:0x020a, B:82:0x0232, B:83:0x0237, B:79:0x021a, B:81:0x0226, B:60:0x01b8, B:62:0x01bb, B:90:0x01ca, B:92:0x01d9, B:95:0x01ee, B:68:0x01c9), top: B:99:0x000c, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x020a A[Catch: all -> 0x0239, TryCatch #2 {, blocks: (B:4:0x000c, B:6:0x002d, B:8:0x0036, B:9:0x003f, B:11:0x0041, B:13:0x004d, B:15:0x0065, B:16:0x006e, B:17:0x0077, B:19:0x0092, B:20:0x00a0, B:23:0x00a8, B:25:0x00b1, B:27:0x00b8, B:29:0x00c4, B:30:0x00df, B:32:0x00ef, B:33:0x0101, B:35:0x010d, B:37:0x011b, B:38:0x0122, B:40:0x0158, B:42:0x0160, B:43:0x0165, B:44:0x0166, B:45:0x0179, B:46:0x0180, B:47:0x0181, B:49:0x0188, B:51:0x0192, B:53:0x01a7, B:54:0x01ad, B:52:0x019e, B:56:0x01b0, B:69:0x01ca, B:71:0x01d9, B:74:0x01ee, B:76:0x01f8, B:78:0x020a, B:82:0x0232, B:83:0x0237, B:79:0x021a, B:81:0x0226, B:60:0x01b8, B:62:0x01bb, B:90:0x01ca, B:92:0x01d9, B:95:0x01ee, B:68:0x01c9), top: B:99:0x000c, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x021a A[Catch: all -> 0x0239, TryCatch #2 {, blocks: (B:4:0x000c, B:6:0x002d, B:8:0x0036, B:9:0x003f, B:11:0x0041, B:13:0x004d, B:15:0x0065, B:16:0x006e, B:17:0x0077, B:19:0x0092, B:20:0x00a0, B:23:0x00a8, B:25:0x00b1, B:27:0x00b8, B:29:0x00c4, B:30:0x00df, B:32:0x00ef, B:33:0x0101, B:35:0x010d, B:37:0x011b, B:38:0x0122, B:40:0x0158, B:42:0x0160, B:43:0x0165, B:44:0x0166, B:45:0x0179, B:46:0x0180, B:47:0x0181, B:49:0x0188, B:51:0x0192, B:53:0x01a7, B:54:0x01ad, B:52:0x019e, B:56:0x01b0, B:69:0x01ca, B:71:0x01d9, B:74:0x01ee, B:76:0x01f8, B:78:0x020a, B:82:0x0232, B:83:0x0237, B:79:0x021a, B:81:0x0226, B:60:0x01b8, B:62:0x01bb, B:90:0x01ca, B:92:0x01d9, B:95:0x01ee, B:68:0x01c9), top: B:99:0x000c, inners: #0 }] */
    @Override // java.sql.Statement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.sql.ResultSet executeQuery(java.lang.String r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 576
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.executeQuery(java.lang.String):java.sql.ResultSet");
    }

    protected void doPingInstead() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.pingTarget != null) {
                this.pingTarget.doPing();
            } else {
                this.connection.ping();
            }
            ResultSetInternalMethods fakeSelectOneResultSet = generatePingResultSet();
            this.results = fakeSelectOneResultSet;
        }
    }

    /* JADX WARN: Type inference failed for: r3v3, types: [byte[], byte[][]] */
    protected ResultSetInternalMethods generatePingResultSet() throws SQLException {
        ResultSetInternalMethods resultSetInternalMethods;
        synchronized (checkClosed().getConnectionMutex()) {
            Field[] fields = {new Field(null, "1", -5, 1)};
            ArrayList<ResultSetRow> rows = new ArrayList<>();
            byte[] colVal = {49};
            rows.add(new ByteArrayRow(new byte[]{colVal}, getExceptionInterceptor()));
            resultSetInternalMethods = (ResultSetInternalMethods) DatabaseMetaData.buildResultSet(fields, rows, this.connection);
        }
        return resultSetInternalMethods;
    }

    protected void executeSimpleNonQuery(MySQLConnection c, String nonQuery) throws SQLException {
        c.execSQL(this, nonQuery, -1, null, 1003, 1007, false, this.currentCatalog, null, false).close();
    }

    @Override // java.sql.Statement
    public int executeUpdate(String sql) throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate(sql));
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x01ec, code lost:
    
        throw r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01fd, code lost:
    
        r20.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0210, code lost:
    
        if (0 == 0) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0213, code lost:
    
        r0.setCatalog(null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x021d, code lost:
    
        if (r14 != false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0220, code lost:
    
        r12.statementExecuting.set(false);
     */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01fd A[Catch: all -> 0x0257, TryCatch #2 {, blocks: (B:4:0x000d, B:8:0x0037, B:12:0x0044, B:14:0x0051, B:16:0x006c, B:17:0x0075, B:18:0x007e, B:20:0x0089, B:21:0x00ae, B:22:0x00af, B:24:0x00b9, B:25:0x00c9, B:26:0x00ca, B:30:0x00e8, B:31:0x00f0, B:33:0x00fa, B:35:0x0101, B:37:0x010e, B:38:0x012a, B:40:0x013b, B:41:0x014f, B:43:0x017a, B:45:0x0182, B:46:0x0187, B:47:0x0188, B:48:0x019c, B:49:0x01a3, B:50:0x01a4, B:52:0x01ab, B:54:0x01b5, B:56:0x01ca, B:57:0x01d0, B:55:0x01c1, B:59:0x01d3, B:72:0x01ed, B:74:0x01fd, B:77:0x0213, B:80:0x0220, B:82:0x022a, B:83:0x0255, B:63:0x01db, B:65:0x01de, B:90:0x01ed, B:92:0x01fd, B:95:0x0213, B:98:0x0220, B:71:0x01ec), top: B:102:0x000d, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0213 A[Catch: all -> 0x0257, TryCatch #2 {, blocks: (B:4:0x000d, B:8:0x0037, B:12:0x0044, B:14:0x0051, B:16:0x006c, B:17:0x0075, B:18:0x007e, B:20:0x0089, B:21:0x00ae, B:22:0x00af, B:24:0x00b9, B:25:0x00c9, B:26:0x00ca, B:30:0x00e8, B:31:0x00f0, B:33:0x00fa, B:35:0x0101, B:37:0x010e, B:38:0x012a, B:40:0x013b, B:41:0x014f, B:43:0x017a, B:45:0x0182, B:46:0x0187, B:47:0x0188, B:48:0x019c, B:49:0x01a3, B:50:0x01a4, B:52:0x01ab, B:54:0x01b5, B:56:0x01ca, B:57:0x01d0, B:55:0x01c1, B:59:0x01d3, B:72:0x01ed, B:74:0x01fd, B:77:0x0213, B:80:0x0220, B:82:0x022a, B:83:0x0255, B:63:0x01db, B:65:0x01de, B:90:0x01ed, B:92:0x01fd, B:95:0x0213, B:98:0x0220, B:71:0x01ec), top: B:102:0x000d, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0220 A[Catch: all -> 0x0257, TryCatch #2 {, blocks: (B:4:0x000d, B:8:0x0037, B:12:0x0044, B:14:0x0051, B:16:0x006c, B:17:0x0075, B:18:0x007e, B:20:0x0089, B:21:0x00ae, B:22:0x00af, B:24:0x00b9, B:25:0x00c9, B:26:0x00ca, B:30:0x00e8, B:31:0x00f0, B:33:0x00fa, B:35:0x0101, B:37:0x010e, B:38:0x012a, B:40:0x013b, B:41:0x014f, B:43:0x017a, B:45:0x0182, B:46:0x0187, B:47:0x0188, B:48:0x019c, B:49:0x01a3, B:50:0x01a4, B:52:0x01ab, B:54:0x01b5, B:56:0x01ca, B:57:0x01d0, B:55:0x01c1, B:59:0x01d3, B:72:0x01ed, B:74:0x01fd, B:77:0x0213, B:80:0x0220, B:82:0x022a, B:83:0x0255, B:63:0x01db, B:65:0x01de, B:90:0x01ed, B:92:0x01fd, B:95:0x0213, B:98:0x0220, B:71:0x01ec), top: B:102:0x000d, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected long executeUpdateInternal(java.lang.String r13, boolean r14, boolean r15) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 607
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.executeUpdateInternal(java.lang.String, boolean, boolean):long");
    }

    @Override // java.sql.Statement
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate(sql, autoGeneratedKeys));
    }

    @Override // java.sql.Statement
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate(sql, columnIndexes));
    }

    @Override // java.sql.Statement
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return Util.truncateAndConvertToInt(executeLargeUpdate(sql, columnNames));
    }

    protected Calendar getCalendarInstanceForSessionOrNew() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.connection != null) {
                return this.connection.getCalendarInstanceForSessionOrNew();
            }
            return new GregorianCalendar();
        }
    }

    @Override // java.sql.Statement
    public java.sql.Connection getConnection() throws SQLException {
        MySQLConnection mySQLConnection;
        synchronized (checkClosed().getConnectionMutex()) {
            mySQLConnection = this.connection;
        }
        return mySQLConnection;
    }

    @Override // java.sql.Statement
    public int getFetchDirection() throws SQLException {
        return 1000;
    }

    @Override // java.sql.Statement
    public int getFetchSize() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.fetchSize;
        }
        return i;
    }

    @Override // java.sql.Statement
    public ResultSet getGeneratedKeys() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.retrieveGeneratedKeys) {
                throw SQLError.createSQLException(Messages.getString("Statement.GeneratedKeysNotRequested"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.batchedGeneratedKeys == null) {
                if (this.lastQueryIsOnDupKeyUpdate) {
                    ResultSetInternalMethods generatedKeysInternal = getGeneratedKeysInternal(1L);
                    this.generatedKeysResults = generatedKeysInternal;
                    return generatedKeysInternal;
                }
                ResultSetInternalMethods generatedKeysInternal2 = getGeneratedKeysInternal();
                this.generatedKeysResults = generatedKeysInternal2;
                return generatedKeysInternal2;
            }
            Field[] fields = {new Field("", "GENERATED_KEY", -5, 20)};
            fields[0].setConnection(this.connection);
            this.generatedKeysResults = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(this.batchedGeneratedKeys), this.connection, this, false);
            return this.generatedKeysResults;
        }
    }

    protected ResultSetInternalMethods getGeneratedKeysInternal() throws SQLException {
        long numKeys = getLargeUpdateCount();
        return getGeneratedKeysInternal(numKeys);
    }

    /* JADX WARN: Type inference failed for: r0v37, types: [byte[], byte[][]] */
    protected ResultSetInternalMethods getGeneratedKeysInternal(long numKeys) throws SQLException {
        ResultSetImpl gkRs;
        synchronized (checkClosed().getConnectionMutex()) {
            Field[] fields = {new Field("", "GENERATED_KEY", -5, 20)};
            fields[0].setConnection(this.connection);
            fields[0].setUseOldNameMetadata(true);
            ArrayList<ResultSetRow> rowSet = new ArrayList<>();
            long beginAt = getLastInsertID();
            if (beginAt < 0) {
                fields[0].setUnsigned();
            }
            if (this.results != null) {
                String serverInfo = this.results.getServerInfo();
                if (numKeys > 0 && this.results.getFirstCharOfQuery() == 'R' && serverInfo != null && serverInfo.length() > 0) {
                    numKeys = getRecordCountFromInfo(serverInfo);
                }
                if (beginAt != 0 && numKeys > 0) {
                    for (int i = 0; i < numKeys; i++) {
                        ?? r0 = new byte[1];
                        if (beginAt > 0) {
                            r0[0] = StringUtils.getBytes(Long.toString(beginAt));
                        } else {
                            byte[] asBytes = {(byte) (beginAt >>> 56), (byte) (beginAt >>> 48), (byte) (beginAt >>> 40), (byte) (beginAt >>> 32), (byte) (beginAt >>> 24), (byte) (beginAt >>> 16), (byte) (beginAt >>> 8), (byte) (beginAt & 255)};
                            BigInteger val = new BigInteger(1, asBytes);
                            r0[0] = val.toString().getBytes();
                        }
                        rowSet.add(new ByteArrayRow(r0, getExceptionInterceptor()));
                        beginAt += this.connection.getAutoIncrementIncrement();
                    }
                }
            }
            gkRs = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(rowSet), this.connection, this, false);
        }
        return gkRs;
    }

    @Override // com.mysql.jdbc.Statement
    public int getId() {
        return this.statementId;
    }

    public long getLastInsertID() {
        long j;
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                j = this.lastInsertId;
            }
            return j;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long getLongUpdateCount() {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (this.results == null) {
                    return -1L;
                }
                if (this.results.reallyResult()) {
                    return -1L;
                }
                return this.updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // java.sql.Statement
    public int getMaxFieldSize() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.maxFieldSize;
        }
        return i;
    }

    @Override // java.sql.Statement
    public int getMaxRows() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.maxRows <= 0) {
                return 0;
            }
            return this.maxRows;
        }
    }

    @Override // java.sql.Statement
    public boolean getMoreResults() throws SQLException {
        return getMoreResults(1);
    }

    @Override // java.sql.Statement
    public boolean getMoreResults(int current) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.results == null) {
                return false;
            }
            boolean streamingMode = createStreamingResultSet();
            if (streamingMode && this.results.reallyResult()) {
                while (this.results.next()) {
                }
            }
            ResultSetInternalMethods nextResultSet = this.results.getNextResultSet();
            switch (current) {
                case 1:
                    if (this.results != null) {
                        if (!streamingMode && !this.connection.getDontTrackOpenResources()) {
                            this.results.realClose(false);
                        }
                        this.results.clearNextResult();
                        break;
                    }
                    break;
                case 2:
                    if (!this.connection.getDontTrackOpenResources()) {
                        this.openResults.add(this.results);
                    }
                    this.results.clearNextResult();
                    break;
                case 3:
                    if (this.results != null) {
                        if (!streamingMode && !this.connection.getDontTrackOpenResources()) {
                            this.results.realClose(false);
                        }
                        this.results.clearNextResult();
                    }
                    closeAllOpenResults();
                    break;
                default:
                    throw SQLError.createSQLException(Messages.getString("Statement.19"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.results = nextResultSet;
            if (this.results == null || this.results.reallyResult()) {
                this.updateCount = -1L;
                this.lastInsertId = -1L;
            } else {
                this.updateCount = this.results.getUpdateCount();
                this.lastInsertId = this.results.getUpdateID();
            }
            boolean moreResults = this.results != null && this.results.reallyResult();
            if (!moreResults) {
                checkAndPerformCloseOnCompletionAction();
            }
            return moreResults;
        }
    }

    @Override // java.sql.Statement
    public int getQueryTimeout() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.timeoutInMillis / 1000;
        }
        return i;
    }

    private long getRecordCountFromInfo(String serverInfo) throws NumberFormatException {
        StringBuilder recordsBuf = new StringBuilder();
        char c = 0;
        int length = serverInfo.length();
        int i = 0;
        while (i < length) {
            c = serverInfo.charAt(i);
            if (Character.isDigit(c)) {
                break;
            }
            i++;
        }
        recordsBuf.append(c);
        while (true) {
            i++;
            if (i >= length) {
                break;
            }
            c = serverInfo.charAt(i);
            if (!Character.isDigit(c)) {
                break;
            }
            recordsBuf.append(c);
        }
        long recordsCount = Long.parseLong(recordsBuf.toString());
        StringBuilder duplicatesBuf = new StringBuilder();
        while (i < length) {
            c = serverInfo.charAt(i);
            if (Character.isDigit(c)) {
                break;
            }
            i++;
        }
        duplicatesBuf.append(c);
        while (true) {
            i++;
            if (i >= length) {
                break;
            }
            char c2 = serverInfo.charAt(i);
            if (!Character.isDigit(c2)) {
                break;
            }
            duplicatesBuf.append(c2);
        }
        long duplicatesCount = Long.parseLong(duplicatesBuf.toString());
        return recordsCount - duplicatesCount;
    }

    @Override // java.sql.Statement
    public ResultSet getResultSet() throws SQLException {
        ResultSetInternalMethods resultSetInternalMethods;
        synchronized (checkClosed().getConnectionMutex()) {
            resultSetInternalMethods = (this.results == null || !this.results.reallyResult()) ? null : this.results;
        }
        return resultSetInternalMethods;
    }

    @Override // java.sql.Statement
    public int getResultSetConcurrency() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.resultSetConcurrency;
        }
        return i;
    }

    @Override // java.sql.Statement
    public int getResultSetHoldability() throws SQLException {
        return 1;
    }

    protected ResultSetInternalMethods getResultSetInternal() {
        ResultSetInternalMethods resultSetInternalMethods;
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                resultSetInternalMethods = this.results;
            }
            return resultSetInternalMethods;
        } catch (SQLException e) {
            return this.results;
        }
    }

    @Override // java.sql.Statement
    public int getResultSetType() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.resultSetType;
        }
        return i;
    }

    @Override // java.sql.Statement
    public int getUpdateCount() throws SQLException {
        return Util.truncateAndConvertToInt(getLargeUpdateCount());
    }

    @Override // java.sql.Statement
    public SQLWarning getWarnings() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.clearWarningsCalled) {
                return null;
            }
            if (this.connection.versionMeetsMinimum(4, 1, 0)) {
                SQLWarning pendingWarningsFromServer = SQLError.convertShowWarningsToSQLWarnings(this.connection);
                if (this.warningChain != null) {
                    this.warningChain.setNextWarning(pendingWarningsFromServer);
                } else {
                    this.warningChain = pendingWarningsFromServer;
                }
                return this.warningChain;
            }
            return this.warningChain;
        }
    }

    protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null || this.isClosed) {
            return;
        }
        if (!locallyScopedConn.getDontTrackOpenResources()) {
            locallyScopedConn.unregisterStatement(this);
        }
        if (this.useUsageAdvisor && !calledExplicitly) {
            this.connection.getProfilerEventHandlerInstance().processEvent((byte) 0, this.connection, this, null, 0L, new Throwable(), Messages.getString("Statement.63"));
        }
        if (closeOpenResults) {
            closeOpenResults = (this.holdResultsOpenOverClose || this.connection.getDontTrackOpenResources()) ? false : true;
        }
        if (closeOpenResults) {
            if (this.results != null) {
                try {
                    this.results.close();
                } catch (Exception e) {
                }
            }
            if (this.generatedKeysResults != null) {
                try {
                    this.generatedKeysResults.close();
                } catch (Exception e2) {
                }
            }
            closeAllOpenResults();
        }
        this.isClosed = true;
        this.results = null;
        this.generatedKeysResults = null;
        this.connection = null;
        this.warningChain = null;
        this.openResults = null;
        this.batchedGeneratedKeys = null;
        this.localInfileInputStream = null;
        this.pingTarget = null;
    }

    @Override // java.sql.Statement
    public void setCursorName(String name) throws SQLException {
    }

    @Override // java.sql.Statement
    public void setEscapeProcessing(boolean enable) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.doEscapeProcessing = enable;
        }
    }

    @Override // java.sql.Statement
    public void setFetchDirection(int direction) throws SQLException {
        switch (direction) {
            case 1000:
            case 1001:
            case 1002:
                return;
            default:
                throw SQLError.createSQLException(Messages.getString("Statement.5"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.Statement
    public void setFetchSize(int rows) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (rows >= 0 || rows == Integer.MIN_VALUE) {
                if (this.maxRows <= 0 || rows <= getMaxRows()) {
                    this.fetchSize = rows;
                }
            }
            throw SQLError.createSQLException(Messages.getString("Statement.7"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // com.mysql.jdbc.Statement
    public void setHoldResultsOpenOverClose(boolean holdResultsOpenOverClose) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.holdResultsOpenOverClose = holdResultsOpenOverClose;
            }
        } catch (SQLException e) {
        }
    }

    @Override // java.sql.Statement
    public void setMaxFieldSize(int max) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (max < 0) {
                throw SQLError.createSQLException(Messages.getString("Statement.11"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            int maxBuf = this.connection != null ? this.connection.getMaxAllowedPacket() : MysqlIO.getMaxBuf();
            if (max > maxBuf) {
                throw SQLError.createSQLException(Messages.getString("Statement.13", new Object[]{Long.valueOf(maxBuf)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.maxFieldSize = max;
        }
    }

    @Override // java.sql.Statement
    public void setMaxRows(int max) throws SQLException {
        setLargeMaxRows(max);
    }

    @Override // java.sql.Statement
    public void setQueryTimeout(int seconds) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (seconds < 0) {
                throw SQLError.createSQLException(Messages.getString("Statement.21"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.timeoutInMillis = seconds * 1000;
        }
    }

    void setResultSetConcurrency(int concurrencyFlag) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.resultSetConcurrency = concurrencyFlag;
            }
        } catch (SQLException e) {
        }
    }

    void setResultSetType(int typeFlag) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.resultSetType = typeFlag;
            }
        } catch (SQLException e) {
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    protected void getBatchedGeneratedKeys(java.sql.Statement r10) throws java.sql.SQLException {
        /*
            r9 = this;
            r0 = r9
            com.mysql.jdbc.MySQLConnection r0 = r0.checkClosed()
            java.lang.Object r0 = r0.getConnectionMutex()
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            r0 = r9
            boolean r0 = r0.retrieveGeneratedKeys     // Catch: java.lang.Throwable -> L6a
            if (r0 == 0) goto L65
            r0 = 0
            r12 = r0
            r0 = r10
            java.sql.ResultSet r0 = r0.getGeneratedKeys()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r12 = r0
        L1c:
            r0 = r12
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            if (r0 == 0) goto L49
            r0 = r9
            java.util.ArrayList<com.mysql.jdbc.ResultSetRow> r0 = r0.batchedGeneratedKeys     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            com.mysql.jdbc.ByteArrayRow r1 = new com.mysql.jdbc.ByteArrayRow     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r2 = r1
            r3 = 1
            byte[] r3 = new byte[r3]     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r4 = r3
            r5 = 0
            r6 = r12
            r7 = 1
            byte[] r6 = r6.getBytes(r7)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r4[r5] = r6     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r4 = r9
            com.mysql.jdbc.ExceptionInterceptor r4 = r4.getExceptionInterceptor()     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            r2.<init>(r3, r4)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L4f java.lang.Throwable -> L6a
            goto L1c
        L49:
            r0 = jsr -> L57
        L4c:
            goto L65
        L4f:
            r13 = move-exception
            r0 = jsr -> L57
        L54:
            r1 = r13
            throw r1     // Catch: java.lang.Throwable -> L6a
        L57:
            r14 = r0
            r0 = r12
            if (r0 == 0) goto L63
            r0 = r12
            r0.close()     // Catch: java.lang.Throwable -> L6a
        L63:
            ret r14     // Catch: java.lang.Throwable -> L6a
        L65:
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6a
            goto L71
        L6a:
            r15 = move-exception
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6a
            r0 = r15
            throw r0
        L71:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.getBatchedGeneratedKeys(java.sql.Statement):void");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    protected void getBatchedGeneratedKeys(int r10) throws java.sql.SQLException {
        /*
            r9 = this;
            r0 = r9
            com.mysql.jdbc.MySQLConnection r0 = r0.checkClosed()
            java.lang.Object r0 = r0.getConnectionMutex()
            r1 = r0
            r11 = r1
            monitor-enter(r0)
            r0 = r9
            boolean r0 = r0.retrieveGeneratedKeys     // Catch: java.lang.Throwable -> L92
            if (r0 == 0) goto L8d
            r0 = 0
            r12 = r0
            r0 = r10
            if (r0 != 0) goto L21
            r0 = r9
            com.mysql.jdbc.ResultSetInternalMethods r0 = r0.getGeneratedKeysInternal()     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r12 = r0
            goto L28
        L21:
            r0 = r9
            r1 = r10
            long r1 = (long) r1     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            com.mysql.jdbc.ResultSetInternalMethods r0 = r0.getGeneratedKeysInternal(r1)     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r12 = r0
        L28:
            r0 = r12
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            if (r0 == 0) goto L55
            r0 = r9
            java.util.ArrayList<com.mysql.jdbc.ResultSetRow> r0 = r0.batchedGeneratedKeys     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            com.mysql.jdbc.ByteArrayRow r1 = new com.mysql.jdbc.ByteArrayRow     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r2 = r1
            r3 = 1
            byte[] r3 = new byte[r3]     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r4 = r3
            r5 = 0
            r6 = r12
            r7 = 1
            byte[] r6 = r6.getBytes(r7)     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r4[r5] = r6     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r4 = r9
            com.mysql.jdbc.ExceptionInterceptor r4 = r4.getExceptionInterceptor()     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            r2.<init>(r3, r4)     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            boolean r0 = r0.add(r1)     // Catch: java.lang.Throwable -> L5b java.lang.Throwable -> L92
            goto L28
        L55:
            r0 = jsr -> L63
        L58:
            goto L8d
        L5b:
            r13 = move-exception
            r0 = jsr -> L63
        L60:
            r1 = r13
            throw r1     // Catch: java.lang.Throwable -> L92
        L63:
            r14 = r0
            r0 = r9
            r1 = 1
            r0.isImplicitlyClosingResults = r1     // Catch: java.lang.Throwable -> L92
            r0 = r12
            if (r0 == 0) goto L74
            r0 = r12
            r0.close()     // Catch: java.lang.Throwable -> L7a java.lang.Throwable -> L92
        L74:
            r0 = jsr -> L82
        L77:
            goto L8b
        L7a:
            r15 = move-exception
            r0 = jsr -> L82
        L7f:
            r1 = r15
            throw r1     // Catch: java.lang.Throwable -> L92
        L82:
            r16 = r0
            r0 = r9
            r1 = 0
            r0.isImplicitlyClosingResults = r1     // Catch: java.lang.Throwable -> L92
            ret r16     // Catch: java.lang.Throwable -> L92
        L8b:
            ret r14     // Catch: java.lang.Throwable -> L92
        L8d:
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L92
            goto L99
        L92:
            r17 = move-exception
            r0 = r11
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L92
            r0 = r17
            throw r0
        L99:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.StatementImpl.getBatchedGeneratedKeys(int):void");
    }

    private boolean useServerFetch() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = this.connection.isCursorFetchEnabled() && this.fetchSize > 0 && this.resultSetConcurrency == 1007 && this.resultSetType == 1003;
        }
        return z;
    }

    @Override // java.sql.Statement
    public boolean isClosed() throws SQLException {
        boolean z;
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return true;
        }
        synchronized (locallyScopedConn.getConnectionMutex()) {
            z = this.isClosed;
        }
        return z;
    }

    @Override // java.sql.Statement
    public boolean isPoolable() throws SQLException {
        return this.isPoolable;
    }

    @Override // java.sql.Statement
    public void setPoolable(boolean poolable) throws SQLException {
        this.isPoolable = poolable;
    }

    @Override // java.sql.Wrapper, com.mysql.jdbc.Wrapper
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkClosed();
        return iface.isInstance(this);
    }

    @Override // java.sql.Wrapper, com.mysql.jdbc.Wrapper
    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException e) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    protected static int findStartOfStatement(String sql) {
        int statementStartPos = 0;
        if (StringUtils.startsWithIgnoreCaseAndWs(sql, ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER)) {
            int statementStartPos2 = sql.indexOf("*/");
            if (statementStartPos2 == -1) {
                statementStartPos = 0;
            } else {
                statementStartPos = statementStartPos2 + 2;
            }
        } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, ScriptUtils.DEFAULT_COMMENT_PREFIX) || StringUtils.startsWithIgnoreCaseAndWs(sql, "#")) {
            statementStartPos = sql.indexOf(10);
            if (statementStartPos == -1) {
                statementStartPos = sql.indexOf(13);
                if (statementStartPos == -1) {
                    statementStartPos = 0;
                }
            }
        }
        return statementStartPos;
    }

    @Override // com.mysql.jdbc.Statement
    public InputStream getLocalInfileInputStream() {
        return this.localInfileInputStream;
    }

    @Override // com.mysql.jdbc.Statement
    public void setLocalInfileInputStream(InputStream stream) {
        this.localInfileInputStream = stream;
    }

    @Override // com.mysql.jdbc.Statement
    public void setPingTarget(PingTarget pingTarget) {
        this.pingTarget = pingTarget;
    }

    @Override // com.mysql.jdbc.Statement
    public ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    protected boolean containsOnDuplicateKeyInString(String sql) {
        return getOnDuplicateKeyLocation(sql, this.connection.getDontCheckOnDuplicateKeyUpdateInSQL(), this.connection.getRewriteBatchedStatements(), this.connection.isNoBackslashEscapesSet()) != -1;
    }

    protected static int getOnDuplicateKeyLocation(String sql, boolean dontCheckOnDuplicateKeyUpdateInSQL, boolean rewriteBatchedStatements, boolean noBackslashEscapes) {
        if (!dontCheckOnDuplicateKeyUpdateInSQL || rewriteBatchedStatements) {
            return StringUtils.indexOfIgnoreCase(0, sql, ON_DUPLICATE_KEY_UPDATE_CLAUSE, "\"'`", "\"'`", noBackslashEscapes ? StringUtils.SEARCH_MODE__MRK_COM_WS : StringUtils.SEARCH_MODE__ALL);
        }
        return -1;
    }

    public void closeOnCompletion() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.closeOnCompletion = true;
        }
    }

    public boolean isCloseOnCompletion() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = this.closeOnCompletion;
        }
        return z;
    }

    public long[] executeLargeBatch() throws SQLException {
        return executeBatchInternal();
    }

    public long executeLargeUpdate(String sql) throws SQLException {
        return executeUpdateInternal(sql, false, false);
    }

    public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return executeUpdateInternal(sql, false, autoGeneratedKeys == 1);
    }

    public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return executeUpdateInternal(sql, false, columnIndexes != null && columnIndexes.length > 0);
    }

    public long executeLargeUpdate(String sql, String[] columnNames) throws SQLException {
        return executeUpdateInternal(sql, false, columnNames != null && columnNames.length > 0);
    }

    public long getLargeMaxRows() throws SQLException {
        return getMaxRows();
    }

    public long getLargeUpdateCount() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.results == null) {
                return -1L;
            }
            if (this.results.reallyResult()) {
                return -1L;
            }
            return this.results.getUpdateCount();
        }
    }

    public void setLargeMaxRows(long max) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (max > 50000000 || max < 0) {
                throw SQLError.createSQLException(Messages.getString("Statement.15") + max + " > 50000000.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (max == 0) {
                max = -1;
            }
            this.maxRows = (int) max;
        }
    }

    boolean isCursorRequired() throws SQLException {
        return false;
    }
}
