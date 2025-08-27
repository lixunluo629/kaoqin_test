package com.mysql.jdbc;

import ch.qos.logback.core.joran.action.ActionConst;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import com.mysql.jdbc.PreparedStatement;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.ParameterMetaData;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ServerPreparedStatement.class */
public class ServerPreparedStatement extends PreparedStatement {
    private static final Constructor<?> JDBC_4_SPS_CTOR;
    protected static final int BLOB_STREAM_READ_BUF_SIZE = 8192;
    private boolean hasOnDuplicateKeyUpdate;
    private boolean detectedLongParameterSwitch;
    private int fieldCount;
    private boolean invalid;
    private SQLException invalidationException;
    private Buffer outByteBuffer;
    private BindValue[] parameterBindings;
    private Field[] parameterFields;
    private Field[] resultFields;
    private boolean sendTypesToServer;
    private long serverStatementId;
    private int stringTypeCode;
    private boolean serverNeedsResetBeforeEachExecution;
    protected boolean isCached;
    private boolean useAutoSlowLog;
    private Calendar serverTzCalendar;
    private Calendar defaultTzCalendar;
    private boolean hasCheckedRewrite;
    private boolean canRewrite;
    private int locationOfOnDuplicateKeyUpdate;

    static {
        if (Util.isJdbc4()) {
            try {
                String jdbc4ClassName = Util.isJdbc42() ? "com.mysql.jdbc.JDBC42ServerPreparedStatement" : "com.mysql.jdbc.JDBC4ServerPreparedStatement";
                JDBC_4_SPS_CTOR = Class.forName(jdbc4ClassName).getConstructor(MySQLConnection.class, String.class, String.class, Integer.TYPE, Integer.TYPE);
                return;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        }
        JDBC_4_SPS_CTOR = null;
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ServerPreparedStatement$BatchedBindValues.class */
    public static class BatchedBindValues {
        public BindValue[] batchedParameterValues;

        BatchedBindValues(BindValue[] paramVals) {
            int numParams = paramVals.length;
            this.batchedParameterValues = new BindValue[numParams];
            for (int i = 0; i < numParams; i++) {
                this.batchedParameterValues[i] = new BindValue(paramVals[i]);
            }
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ServerPreparedStatement$BindValue.class */
    public static class BindValue {
        public long boundBeforeExecutionNum;
        public long bindLength;
        public int bufferType;
        public double doubleBinding;
        public float floatBinding;
        public boolean isLongData;
        public boolean isNull;
        public boolean isSet;
        public long longBinding;
        public Object value;
        public Calendar calendar;

        BindValue() {
            this.boundBeforeExecutionNum = 0L;
            this.isSet = false;
        }

        BindValue(BindValue copyMe) {
            this.boundBeforeExecutionNum = 0L;
            this.isSet = false;
            this.value = copyMe.value;
            this.isSet = copyMe.isSet;
            this.isLongData = copyMe.isLongData;
            this.isNull = copyMe.isNull;
            this.bufferType = copyMe.bufferType;
            this.bindLength = copyMe.bindLength;
            this.longBinding = copyMe.longBinding;
            this.floatBinding = copyMe.floatBinding;
            this.doubleBinding = copyMe.doubleBinding;
            this.calendar = copyMe.calendar;
        }

        void reset() {
            this.isNull = false;
            this.isSet = false;
            this.value = null;
            this.isLongData = false;
            this.longBinding = 0L;
            this.floatBinding = 0.0f;
            this.doubleBinding = 0.0d;
            this.calendar = null;
        }

        public String toString() {
            return toString(false);
        }

        public String toString(boolean quoteIfNeeded) {
            if (this.isLongData) {
                return "' STREAM DATA '";
            }
            if (this.isNull) {
                return ActionConst.NULL;
            }
            switch (this.bufferType) {
                case 1:
                case 2:
                case 3:
                case 8:
                    return String.valueOf(this.longBinding);
                case 4:
                    return String.valueOf(this.floatBinding);
                case 5:
                    return String.valueOf(this.doubleBinding);
                case 7:
                case 10:
                case 11:
                case 12:
                case 15:
                case 253:
                case 254:
                    if (quoteIfNeeded) {
                        return "'" + String.valueOf(this.value) + "'";
                    }
                    return String.valueOf(this.value);
                default:
                    if (this.value instanceof byte[]) {
                        return "byte data";
                    }
                    if (quoteIfNeeded) {
                        return "'" + String.valueOf(this.value) + "'";
                    }
                    return String.valueOf(this.value);
            }
        }

        long getBoundLength() {
            if (this.isNull) {
                return 0L;
            }
            if (this.isLongData) {
                return this.bindLength;
            }
            switch (this.bufferType) {
                case 0:
                case 15:
                case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                case 253:
                case 254:
                    if (this.value instanceof byte[]) {
                        return ((byte[]) this.value).length;
                    }
                    return ((String) this.value).length();
                case 1:
                    return 1L;
                case 2:
                    return 2L;
                case 3:
                    return 4L;
                case 4:
                    return 4L;
                case 5:
                    return 8L;
                case 7:
                case 12:
                    return 11L;
                case 8:
                    return 8L;
                case 10:
                    return 7L;
                case 11:
                    return 9L;
                default:
                    return 0L;
            }
        }
    }

    private void storeTime(Buffer intoBuf, Time tm) throws SQLException {
        intoBuf.ensureCapacity(9);
        intoBuf.writeByte((byte) 8);
        intoBuf.writeByte((byte) 0);
        intoBuf.writeLong(0L);
        Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
        synchronized (sessionCalendar) {
            Date oldTime = sessionCalendar.getTime();
            try {
                sessionCalendar.setTime(tm);
                intoBuf.writeByte((byte) sessionCalendar.get(11));
                intoBuf.writeByte((byte) sessionCalendar.get(12));
                intoBuf.writeByte((byte) sessionCalendar.get(13));
            } finally {
                sessionCalendar.setTime(oldTime);
            }
        }
    }

    protected static ServerPreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, int resultSetType, int resultSetConcurrency) throws SQLException {
        if (!Util.isJdbc4()) {
            return new ServerPreparedStatement(conn, sql, catalog, resultSetType, resultSetConcurrency);
        }
        try {
            return (ServerPreparedStatement) JDBC_4_SPS_CTOR.newInstance(conn, sql, catalog, Integer.valueOf(resultSetType), Integer.valueOf(resultSetConcurrency));
        } catch (IllegalAccessException e) {
            throw new SQLException(e.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
        } catch (IllegalArgumentException e2) {
            throw new SQLException(e2.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
        } catch (InstantiationException e3) {
            throw new SQLException(e3.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
        } catch (InvocationTargetException e4) {
            Throwable target = e4.getTargetException();
            if (target instanceof SQLException) {
                throw ((SQLException) target);
            }
            throw new SQLException(target.toString(), SQLError.SQL_STATE_GENERAL_ERROR);
        }
    }

    protected ServerPreparedStatement(MySQLConnection conn, String sql, String catalog, int resultSetType, int resultSetConcurrency) throws SQLException {
        super(conn, catalog);
        this.hasOnDuplicateKeyUpdate = false;
        this.detectedLongParameterSwitch = false;
        this.invalid = false;
        this.sendTypesToServer = false;
        this.stringTypeCode = 254;
        this.isCached = false;
        this.hasCheckedRewrite = false;
        this.canRewrite = false;
        this.locationOfOnDuplicateKeyUpdate = -2;
        checkNullOrEmptyQuery(sql);
        int startOfStatement = findStartOfStatement(sql);
        this.firstCharOfStmt = StringUtils.firstAlphaCharUc(sql, startOfStatement);
        this.hasOnDuplicateKeyUpdate = this.firstCharOfStmt == 'I' && containsOnDuplicateKeyInString(sql);
        if (this.connection.versionMeetsMinimum(5, 0, 0)) {
            this.serverNeedsResetBeforeEachExecution = !this.connection.versionMeetsMinimum(5, 0, 3);
        } else {
            this.serverNeedsResetBeforeEachExecution = !this.connection.versionMeetsMinimum(4, 1, 10);
        }
        this.useAutoSlowLog = this.connection.getAutoSlowLog();
        this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
        String statementComment = this.connection.getStatementComment();
        this.originalSql = statementComment == null ? sql : "/* " + statementComment + " */ " + sql;
        if (this.connection.versionMeetsMinimum(4, 1, 2)) {
            this.stringTypeCode = 253;
        } else {
            this.stringTypeCode = 254;
        }
        try {
            serverPrepare(sql);
            setResultSetType(resultSetType);
            setResultSetConcurrency(resultSetConcurrency);
            this.parameterTypes = new int[this.parameterCount];
        } catch (SQLException sqlEx) {
            realClose(false, true);
            throw sqlEx;
        } catch (Exception ex) {
            realClose(false, true);
            SQLException sqlEx2 = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            sqlEx2.initCause(ex);
            throw sqlEx2;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void addBatch() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.batchedArgs == null) {
                this.batchedArgs = new ArrayList();
            }
            this.batchedArgs.add(new BatchedBindValues(this.parameterBindings));
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    @Override // com.mysql.jdbc.PreparedStatement
    public java.lang.String asSql(boolean r6) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.asSql(boolean):java.lang.String");
    }

    @Override // com.mysql.jdbc.StatementImpl
    protected MySQLConnection checkClosed() throws SQLException {
        if (this.invalid) {
            throw this.invalidationException;
        }
        return super.checkClosed();
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void clearParameters() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            clearParametersInternal(true);
        }
    }

    private void clearParametersInternal(boolean clearServerParameters) throws SQLException {
        boolean hadLongData = false;
        if (this.parameterBindings != null) {
            for (int i = 0; i < this.parameterCount; i++) {
                if (this.parameterBindings[i] != null && this.parameterBindings[i].isLongData) {
                    hadLongData = true;
                }
                this.parameterBindings[i].reset();
            }
        }
        if (clearServerParameters && hadLongData) {
            serverResetStatement();
            this.detectedLongParameterSwitch = false;
        }
    }

    protected void setClosed(boolean flag) {
        this.isClosed = flag;
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement, java.lang.AutoCloseable
    public void close() throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return;
        }
        synchronized (locallyScopedConn.getConnectionMutex()) {
            if (this.isCached && isPoolable() && !this.isClosed) {
                clearParameters();
                this.isClosed = true;
                this.connection.recachePreparedStatement(this);
            } else {
                this.isClosed = false;
                realClose(true, true);
            }
        }
    }

    private void dumpCloseForTestcase() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder buf = new StringBuilder();
            this.connection.generateConnectionCommentBlock(buf);
            buf.append("DEALLOCATE PREPARE debug_stmt_");
            buf.append(this.statementId);
            buf.append(";\n");
            this.connection.dumpTestcaseQuery(buf.toString());
        }
    }

    private void dumpExecuteForTestcase() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < this.parameterCount; i++) {
                this.connection.generateConnectionCommentBlock(buf);
                buf.append("SET @debug_stmt_param");
                buf.append(this.statementId);
                buf.append("_");
                buf.append(i);
                buf.append(SymbolConstants.EQUAL_SYMBOL);
                if (this.parameterBindings[i].isNull) {
                    buf.append(ActionConst.NULL);
                } else {
                    buf.append(this.parameterBindings[i].toString(true));
                }
                buf.append(";\n");
            }
            this.connection.generateConnectionCommentBlock(buf);
            buf.append("EXECUTE debug_stmt_");
            buf.append(this.statementId);
            if (this.parameterCount > 0) {
                buf.append(" USING ");
                for (int i2 = 0; i2 < this.parameterCount; i2++) {
                    if (i2 > 0) {
                        buf.append(", ");
                    }
                    buf.append("@debug_stmt_param");
                    buf.append(this.statementId);
                    buf.append("_");
                    buf.append(i2);
                }
            }
            buf.append(";\n");
            this.connection.dumpTestcaseQuery(buf.toString());
        }
    }

    private void dumpPrepareForTestcase() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder buf = new StringBuilder(this.originalSql.length() + 64);
            this.connection.generateConnectionCommentBlock(buf);
            buf.append("PREPARE debug_stmt_");
            buf.append(this.statementId);
            buf.append(" FROM \"");
            buf.append(this.originalSql);
            buf.append("\";\n");
            this.connection.dumpTestcaseQuery(buf.toString());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:0x0204, code lost:
    
        r17.cancel();
        r0.getCancelTimer().purge();
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0214, code lost:
    
        resetCancelledState();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01fc, code lost:
    
        throw r22;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0204 A[Catch: all -> 0x0242, all -> 0x025d, TryCatch #1 {all -> 0x0242, blocks: (B:9:0x0048, B:11:0x0052, B:13:0x006a, B:17:0x0081, B:19:0x009b, B:23:0x00a8, B:25:0x00b4, B:29:0x00d6, B:30:0x00e3, B:32:0x00eb, B:34:0x010d, B:38:0x011e, B:73:0x01e9, B:39:0x0124, B:42:0x0138, B:44:0x0142, B:46:0x0157, B:47:0x015f, B:48:0x0165, B:54:0x017e, B:55:0x0188, B:59:0x0195, B:107:0x017e, B:53:0x017d, B:62:0x019d, B:64:0x01ac, B:66:0x01b4, B:68:0x01bc, B:71:0x01cc, B:72:0x01e8, B:82:0x0204, B:83:0x0214, B:86:0x021f, B:87:0x022a, B:110:0x0204, B:111:0x0214, B:79:0x01fc, B:91:0x0235), top: B:115:0x0048, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x021f A[Catch: all -> 0x0242, all -> 0x025d, TryCatch #1 {all -> 0x0242, blocks: (B:9:0x0048, B:11:0x0052, B:13:0x006a, B:17:0x0081, B:19:0x009b, B:23:0x00a8, B:25:0x00b4, B:29:0x00d6, B:30:0x00e3, B:32:0x00eb, B:34:0x010d, B:38:0x011e, B:73:0x01e9, B:39:0x0124, B:42:0x0138, B:44:0x0142, B:46:0x0157, B:47:0x015f, B:48:0x0165, B:54:0x017e, B:55:0x0188, B:59:0x0195, B:107:0x017e, B:53:0x017d, B:62:0x019d, B:64:0x01ac, B:66:0x01b4, B:68:0x01bc, B:71:0x01cc, B:72:0x01e8, B:82:0x0204, B:83:0x0214, B:86:0x021f, B:87:0x022a, B:110:0x0204, B:111:0x0214, B:79:0x01fc, B:91:0x0235), top: B:115:0x0048, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0235 A[Catch: all -> 0x0242, all -> 0x025d, TryCatch #1 {all -> 0x0242, blocks: (B:9:0x0048, B:11:0x0052, B:13:0x006a, B:17:0x0081, B:19:0x009b, B:23:0x00a8, B:25:0x00b4, B:29:0x00d6, B:30:0x00e3, B:32:0x00eb, B:34:0x010d, B:38:0x011e, B:73:0x01e9, B:39:0x0124, B:42:0x0138, B:44:0x0142, B:46:0x0157, B:47:0x015f, B:48:0x0165, B:54:0x017e, B:55:0x0188, B:59:0x0195, B:107:0x017e, B:53:0x017d, B:62:0x019d, B:64:0x01ac, B:66:0x01b4, B:68:0x01bc, B:71:0x01cc, B:72:0x01e8, B:82:0x0204, B:83:0x0214, B:86:0x021f, B:87:0x022a, B:110:0x0204, B:111:0x0214, B:79:0x01fc, B:91:0x0235), top: B:115:0x0048, outer: #4 }] */
    @Override // com.mysql.jdbc.PreparedStatement
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected long[] executeBatchSerially(int r8) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 612
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.executeBatchSerially(int):long[]");
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch) throws SQLException {
        ResultSetInternalMethods resultSetInternalMethodsServerExecute;
        synchronized (checkClosed().getConnectionMutex()) {
            this.numberOfExecutions++;
            try {
                resultSetInternalMethodsServerExecute = serverExecute(maxRowsToRetrieve, createStreamingResultSet, metadataFromCache);
            } catch (SQLException e) {
                sqlEx = e;
                if (this.connection.getEnablePacketDebug()) {
                    this.connection.getIO().dumpPacketRingBuffer();
                }
                if (this.connection.getDumpQueriesOnException()) {
                    String extractedSql = toString();
                    StringBuilder messageBuf = new StringBuilder(extractedSql.length() + 32);
                    messageBuf.append("\n\nQuery being executed when exception was thrown:\n");
                    messageBuf.append(extractedSql);
                    messageBuf.append("\n\n");
                    sqlEx = ConnectionImpl.appendMessageToException(sqlEx, messageBuf.toString(), getExceptionInterceptor());
                }
                throw sqlEx;
            } catch (Exception ex) {
                if (this.connection.getEnablePacketDebug()) {
                    this.connection.getIO().dumpPacketRingBuffer();
                }
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                if (this.connection.getDumpQueriesOnException()) {
                    String extractedSql2 = toString();
                    StringBuilder messageBuf2 = new StringBuilder(extractedSql2.length() + 32);
                    messageBuf2.append("\n\nQuery being executed when exception was thrown:\n");
                    messageBuf2.append(extractedSql2);
                    messageBuf2.append("\n\n");
                    sqlEx = ConnectionImpl.appendMessageToException(sqlEx, messageBuf2.toString(), getExceptionInterceptor());
                }
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
        return resultSetInternalMethodsServerExecute;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected Buffer fillSendPacket() throws SQLException {
        return null;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths) throws SQLException {
        return null;
    }

    protected BindValue getBinding(int parameterIndex, boolean forLongData) throws SQLException {
        BindValue bindValue;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.parameterBindings.length == 0) {
                throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.8"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            int parameterIndex2 = parameterIndex - 1;
            if (parameterIndex2 < 0 || parameterIndex2 >= this.parameterBindings.length) {
                throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.9") + (parameterIndex2 + 1) + Messages.getString("ServerPreparedStatement.10") + this.parameterBindings.length, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.parameterBindings[parameterIndex2] == null) {
                this.parameterBindings[parameterIndex2] = new BindValue();
            } else if (this.parameterBindings[parameterIndex2].isLongData && !forLongData) {
                this.detectedLongParameterSwitch = true;
            }
            bindValue = this.parameterBindings[parameterIndex2];
        }
        return bindValue;
    }

    public BindValue[] getParameterBindValues() {
        return this.parameterBindings;
    }

    byte[] getBytes(int parameterIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            BindValue bindValue = getBinding(parameterIndex, false);
            if (bindValue.isNull) {
                return null;
            }
            if (bindValue.isLongData) {
                throw SQLError.createSQLFeatureNotSupportedException();
            }
            if (this.outByteBuffer == null) {
                this.outByteBuffer = new Buffer(this.connection.getNetBufferLength());
            }
            this.outByteBuffer.clear();
            int originalPosition = this.outByteBuffer.getPosition();
            storeBinding(this.outByteBuffer, bindValue, this.connection.getIO());
            int newPosition = this.outByteBuffer.getPosition();
            int length = newPosition - originalPosition;
            byte[] valueAsBytes = new byte[length];
            System.arraycopy(this.outByteBuffer.getByteBuffer(), originalPosition, valueAsBytes, 0, length);
            return valueAsBytes;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public java.sql.ResultSetMetaData getMetaData() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.resultFields == null) {
                return null;
            }
            return new ResultSetMetaData(this.resultFields, this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public ParameterMetaData getParameterMetaData() throws SQLException {
        MysqlParameterMetadata mysqlParameterMetadata;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.parameterMetaData == null) {
                this.parameterMetaData = new MysqlParameterMetadata(this.parameterFields, this.parameterCount, getExceptionInterceptor());
            }
            mysqlParameterMetadata = this.parameterMetaData;
        }
        return mysqlParameterMetadata;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    boolean isNull(int paramIndex) {
        throw new IllegalArgumentException(Messages.getString("ServerPreparedStatement.7"));
    }

    @Override // com.mysql.jdbc.PreparedStatement, com.mysql.jdbc.StatementImpl
    protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return;
        }
        synchronized (locallyScopedConn.getConnectionMutex()) {
            if (this.connection != null) {
                if (this.connection.getAutoGenerateTestcaseScript()) {
                    dumpCloseForTestcase();
                }
                SQLException exceptionDuringClose = null;
                if (calledExplicitly && !this.connection.isClosed()) {
                    synchronized (this.connection.getConnectionMutex()) {
                        try {
                            MysqlIO mysql = this.connection.getIO();
                            Buffer packet = mysql.getSharedSendPacket();
                            packet.writeByte((byte) 25);
                            packet.writeLong(this.serverStatementId);
                            mysql.sendCommand(25, null, packet, true, null, 0);
                        } catch (SQLException sqlEx) {
                            exceptionDuringClose = sqlEx;
                        }
                    }
                }
                if (this.isCached) {
                    this.connection.decachePreparedStatement(this);
                    this.isCached = false;
                }
                super.realClose(calledExplicitly, closeOpenResults);
                clearParametersInternal(false);
                this.parameterBindings = null;
                this.parameterFields = null;
                this.resultFields = null;
                if (exceptionDuringClose != null) {
                    throw exceptionDuringClose;
                }
            }
        }
    }

    protected void rePrepare() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.invalidationException = null;
            try {
                serverPrepare(this.originalSql);
            } catch (SQLException sqlEx) {
                this.invalidationException = sqlEx;
            } catch (Exception ex) {
                this.invalidationException = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                this.invalidationException.initCause(ex);
            }
            if (this.invalidationException != null) {
                this.invalid = true;
                this.parameterBindings = null;
                this.parameterFields = null;
                this.resultFields = null;
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
                try {
                    closeAllOpenResults();
                } catch (Exception e3) {
                }
                if (this.connection != null && !this.connection.getDontTrackOpenResources()) {
                    this.connection.unregisterStatement(this);
                }
            }
        }
    }

    @Override // com.mysql.jdbc.StatementImpl
    boolean isCursorRequired() throws SQLException {
        return this.resultFields != null && this.connection.isCursorFetchEnabled() && getResultSetType() == 1003 && getResultSetConcurrency() == 1007 && getFetchSize() > 0;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r31v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 31, insn: 0x0541: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = 
  (r31 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('timeoutTask' com.mysql.jdbc.StatementImpl$CancelTask)])
 (LINE:1378), block:B:196:0x0537 */
    private com.mysql.jdbc.ResultSetInternalMethods serverExecute(int r16, boolean r17, com.mysql.jdbc.Field[] r18) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 1379
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.serverExecute(int, boolean, com.mysql.jdbc.Field[]):com.mysql.jdbc.ResultSetInternalMethods");
    }

    private void serverLongData(int parameterIndex, BindValue longData) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            MysqlIO mysql = this.connection.getIO();
            Buffer packet = mysql.getSharedSendPacket();
            Object value = longData.value;
            if (value instanceof byte[]) {
                packet.clear();
                packet.writeByte((byte) 24);
                packet.writeLong(this.serverStatementId);
                packet.writeInt(parameterIndex);
                packet.writeBytesNoNull((byte[]) longData.value);
                mysql.sendCommand(24, null, packet, true, null, 0);
            } else if (value instanceof InputStream) {
                storeStream(mysql, parameterIndex, packet, (InputStream) value);
            } else if (value instanceof java.sql.Blob) {
                storeStream(mysql, parameterIndex, packet, ((java.sql.Blob) value).getBinaryStream());
            } else if (value instanceof Reader) {
                storeReader(mysql, parameterIndex, packet, (Reader) value);
            } else {
                throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.18") + value.getClass().getName() + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
        }
    }

    private void serverPrepare(String sql) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            MysqlIO mysql = this.connection.getIO();
            if (this.connection.getAutoGenerateTestcaseScript()) {
                dumpPrepareForTestcase();
            }
            try {
                try {
                    long begin = this.connection.getProfileSql() ? System.currentTimeMillis() : 0L;
                    this.isLoadDataQuery = StringUtils.startsWithIgnoreCaseAndWs(sql, "LOAD DATA");
                    String characterEncoding = null;
                    String connectionEncoding = this.connection.getEncoding();
                    if (!this.isLoadDataQuery && this.connection.getUseUnicode() && connectionEncoding != null) {
                        characterEncoding = connectionEncoding;
                    }
                    Buffer prepareResultPacket = mysql.sendCommand(22, sql, null, false, characterEncoding, 0);
                    if (this.connection.versionMeetsMinimum(4, 1, 1)) {
                        prepareResultPacket.setPosition(1);
                    } else {
                        prepareResultPacket.setPosition(0);
                    }
                    this.serverStatementId = prepareResultPacket.readLong();
                    this.fieldCount = prepareResultPacket.readInt();
                    this.parameterCount = prepareResultPacket.readInt();
                    this.parameterBindings = new BindValue[this.parameterCount];
                    for (int i = 0; i < this.parameterCount; i++) {
                        this.parameterBindings[i] = new BindValue();
                    }
                    this.connection.incrementNumberOfPrepares();
                    if (this.profileSQL) {
                        this.connection.getProfilerEventHandlerInstance().processEvent((byte) 2, this.connection, this, null, mysql.getCurrentTimeNanosOrMillis() - begin, new Throwable(), truncateQueryToLog(sql));
                    }
                    boolean checkEOF = !mysql.isEOFDeprecated();
                    if (this.parameterCount > 0 && this.connection.versionMeetsMinimum(4, 1, 2) && !mysql.isVersion(5, 0, 0)) {
                        this.parameterFields = new Field[this.parameterCount];
                        for (int i2 = 0; i2 < this.parameterCount; i2++) {
                            Buffer metaDataPacket = mysql.readPacket();
                            this.parameterFields[i2] = mysql.unpackField(metaDataPacket, false);
                        }
                        if (checkEOF) {
                            mysql.readPacket();
                        }
                    }
                    if (this.fieldCount > 0) {
                        this.resultFields = new Field[this.fieldCount];
                        for (int i3 = 0; i3 < this.fieldCount; i3++) {
                            Buffer fieldPacket = mysql.readPacket();
                            this.resultFields[i3] = mysql.unpackField(fieldPacket, false);
                        }
                        if (checkEOF) {
                            mysql.readPacket();
                        }
                    }
                } finally {
                    this.connection.getIO().clearInputStream();
                }
            } catch (SQLException e) {
                sqlEx = e;
                if (this.connection.getDumpQueriesOnException()) {
                    StringBuilder messageBuf = new StringBuilder(this.originalSql.length() + 32);
                    messageBuf.append("\n\nQuery being prepared when exception was thrown:\n\n");
                    messageBuf.append(this.originalSql);
                    sqlEx = ConnectionImpl.appendMessageToException(sqlEx, messageBuf.toString(), getExceptionInterceptor());
                }
                throw sqlEx;
            }
        }
    }

    private String truncateQueryToLog(String sql) throws SQLException {
        String query;
        String str;
        synchronized (checkClosed().getConnectionMutex()) {
            if (sql.length() > this.connection.getMaxQuerySizeToLog()) {
                StringBuilder queryBuf = new StringBuilder(this.connection.getMaxQuerySizeToLog() + 12);
                queryBuf.append(sql.substring(0, this.connection.getMaxQuerySizeToLog()));
                queryBuf.append(Messages.getString("MysqlIO.25"));
                query = queryBuf.toString();
            } else {
                query = sql;
            }
            str = query;
        }
        return str;
    }

    private void serverResetStatement() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            MysqlIO mysql = this.connection.getIO();
            Buffer packet = mysql.getSharedSendPacket();
            packet.clear();
            packet.writeByte((byte) 26);
            packet.writeLong(this.serverStatementId);
            try {
                try {
                    mysql.sendCommand(26, null, packet, !this.connection.versionMeetsMinimum(4, 1, 2), null, 0);
                } catch (SQLException sqlEx) {
                    throw sqlEx;
                } catch (Exception ex) {
                    SQLException sqlEx2 = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                    sqlEx2.initCause(ex);
                    throw sqlEx2;
                }
            } finally {
                mysql.clearInputStream();
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setArray(int i, Array x) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                BindValue binding = getBinding(parameterIndex, true);
                resetToType(binding, 252);
                binding.value = x;
                binding.isLongData = true;
                if (this.connection.getUseStreamLengthsInPrepStmts()) {
                    binding.bindLength = length;
                } else {
                    binding.bindLength = -1L;
                }
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, 3);
            } else {
                BindValue binding = getBinding(parameterIndex, false);
                if (this.connection.versionMeetsMinimum(5, 0, 3)) {
                    resetToType(binding, EscherProperties.GEOTEXT__CHARBOUNDINGBOX);
                } else {
                    resetToType(binding, this.stringTypeCode);
                }
                binding.value = StringUtils.fixDecimalExponent(StringUtils.consistentToString(x));
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                BindValue binding = getBinding(parameterIndex, true);
                resetToType(binding, 252);
                binding.value = x;
                binding.isLongData = true;
                if (this.connection.getUseStreamLengthsInPrepStmts()) {
                    binding.bindLength = length;
                } else {
                    binding.bindLength = -1L;
                }
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setBlob(int parameterIndex, java.sql.Blob x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                BindValue binding = getBinding(parameterIndex, true);
                resetToType(binding, 252);
                binding.value = x;
                binding.isLongData = true;
                if (this.connection.getUseStreamLengthsInPrepStmts()) {
                    binding.bindLength = x.length();
                } else {
                    binding.bindLength = -1L;
                }
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        setByte(parameterIndex, x ? (byte) 1 : (byte) 0);
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setByte(int parameterIndex, byte x) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 1);
        binding.longBinding = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        checkClosed();
        if (x == null) {
            setNull(parameterIndex, -2);
            return;
        }
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 253);
        binding.value = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (reader == null) {
                setNull(parameterIndex, -2);
            } else {
                BindValue binding = getBinding(parameterIndex, true);
                resetToType(binding, 252);
                binding.value = reader;
                binding.isLongData = true;
                if (this.connection.getUseStreamLengthsInPrepStmts()) {
                    binding.bindLength = length;
                } else {
                    binding.bindLength = -1L;
                }
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setClob(int parameterIndex, java.sql.Clob x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (x == null) {
                setNull(parameterIndex, -2);
            } else {
                BindValue binding = getBinding(parameterIndex, true);
                resetToType(binding, 252);
                binding.value = x.getCharacterStream();
                binding.isLongData = true;
                if (this.connection.getUseStreamLengthsInPrepStmts()) {
                    binding.bindLength = x.length();
                } else {
                    binding.bindLength = -1L;
                }
            }
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
        setDate(parameterIndex, x, null);
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 91);
            return;
        }
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 10);
        binding.value = x;
        if (cal != null) {
            binding.calendar = (Calendar) cal.clone();
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setDouble(int parameterIndex, double x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.connection.getAllowNanAndInf() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || Double.isNaN(x))) {
                throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            BindValue binding = getBinding(parameterIndex, false);
            resetToType(binding, 5);
            binding.doubleBinding = x;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setFloat(int parameterIndex, float x) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 4);
        binding.floatBinding = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setInt(int parameterIndex, int x) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 3);
        binding.longBinding = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setLong(int parameterIndex, long x) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 8);
        binding.longBinding = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 6);
        binding.isNull = true;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 6);
        binding.isNull = true;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setRef(int i, Ref x) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setShort(int parameterIndex, short x) throws SQLException {
        checkClosed();
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 2);
        binding.longBinding = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setString(int parameterIndex, String x) throws SQLException {
        checkClosed();
        if (x == null) {
            setNull(parameterIndex, 1);
            return;
        }
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, this.stringTypeCode);
        binding.value = x;
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setTime(int parameterIndex, Time x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimeInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
        }
    }

    private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 92);
            return;
        }
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 11);
        if (!this.useLegacyDatetimeCode) {
            binding.value = x;
            if (targetCalendar != null) {
                binding.calendar = (Calendar) targetCalendar.clone();
                return;
            }
            return;
        }
        Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
        binding.value = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimestampInternal(parameterIndex, x, null, this.connection.getDefaultTimeZone(), false);
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
        }
    }

    private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        if (x == null) {
            setNull(parameterIndex, 93);
            return;
        }
        BindValue binding = getBinding(parameterIndex, false);
        resetToType(binding, 12);
        if (!this.sendFractionalSeconds) {
            x = TimeUtil.truncateFractionalSeconds(x);
        }
        if (!this.useLegacyDatetimeCode) {
            binding.value = x;
        } else {
            Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
            binding.value = TimeUtil.changeTimezone(this.connection, TimeUtil.setProlepticIfNeeded(sessionCalendar, targetCalendar), targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
        }
        if (targetCalendar != null) {
            binding.calendar = (Calendar) targetCalendar.clone();
        }
    }

    protected void resetToType(BindValue oldValue, int bufferType) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            oldValue.reset();
            if ((bufferType != 6 || oldValue.bufferType == 0) && oldValue.bufferType != bufferType) {
                this.sendTypesToServer = true;
                oldValue.bufferType = bufferType;
            }
            oldValue.isSet = true;
            oldValue.boundBeforeExecutionNum = this.numberOfExecutions;
        }
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        checkClosed();
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.PreparedStatement, java.sql.PreparedStatement
    public void setURL(int parameterIndex, URL x) throws SQLException {
        checkClosed();
        setString(parameterIndex, x.toString());
    }

    private void storeBinding(Buffer packet, BindValue bindValue, MysqlIO mysql) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                Object value = bindValue.value;
                switch (bindValue.bufferType) {
                    case 0:
                    case 15:
                    case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                    case 253:
                    case 254:
                        if (value instanceof byte[]) {
                            packet.writeLenBytes((byte[]) value);
                        } else if (!this.isLoadDataQuery) {
                            packet.writeLenString((String) value, this.charEncoding, this.connection.getServerCharset(), this.charConverter, this.connection.parserKnowsUnicode(), this.connection);
                        } else {
                            packet.writeLenBytes(StringUtils.getBytes((String) value));
                        }
                        return;
                    case 1:
                        packet.writeByte((byte) bindValue.longBinding);
                        return;
                    case 2:
                        packet.ensureCapacity(2);
                        packet.writeInt((int) bindValue.longBinding);
                        return;
                    case 3:
                        packet.ensureCapacity(4);
                        packet.writeLong((int) bindValue.longBinding);
                        return;
                    case 4:
                        packet.ensureCapacity(4);
                        packet.writeFloat(bindValue.floatBinding);
                        return;
                    case 5:
                        packet.ensureCapacity(8);
                        packet.writeDouble(bindValue.doubleBinding);
                        return;
                    case 7:
                    case 10:
                    case 12:
                        storeDateTime(packet, (Date) value, mysql, bindValue.bufferType, bindValue.calendar);
                        return;
                    case 8:
                        packet.ensureCapacity(8);
                        packet.writeLongLong(bindValue.longBinding);
                        return;
                    case 11:
                        storeTime(packet, (Time) value);
                        return;
                    default:
                        return;
                }
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException(Messages.getString("ServerPreparedStatement.22") + this.connection.getEncoding() + "'", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
        }
    }

    private void storeDateTime412AndOlder(Buffer intoBuf, Date dt, int bufferType) throws SQLException {
        Calendar sessionCalendar;
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.useLegacyDatetimeCode) {
                if (bufferType == 10) {
                    sessionCalendar = getDefaultTzCalendar();
                } else {
                    sessionCalendar = getServerTzCalendar();
                }
            } else {
                sessionCalendar = ((dt instanceof Timestamp) && this.connection.getUseJDBCCompliantTimezoneShift()) ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
            }
            Date oldTime = sessionCalendar.getTime();
            try {
                intoBuf.ensureCapacity(8);
                intoBuf.writeByte((byte) 7);
                sessionCalendar.setTime(dt);
                int year = sessionCalendar.get(1);
                int month = sessionCalendar.get(2) + 1;
                int date = sessionCalendar.get(5);
                intoBuf.writeInt(year);
                intoBuf.writeByte((byte) month);
                intoBuf.writeByte((byte) date);
                if (dt instanceof java.sql.Date) {
                    intoBuf.writeByte((byte) 0);
                    intoBuf.writeByte((byte) 0);
                    intoBuf.writeByte((byte) 0);
                } else {
                    intoBuf.writeByte((byte) sessionCalendar.get(11));
                    intoBuf.writeByte((byte) sessionCalendar.get(12));
                    intoBuf.writeByte((byte) sessionCalendar.get(13));
                }
            } finally {
                sessionCalendar.setTime(oldTime);
            }
        }
    }

    private void storeDateTime(Buffer intoBuf, Date dt, MysqlIO mysql, int bufferType, Calendar cal) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.connection.versionMeetsMinimum(4, 1, 3)) {
                storeDateTime413AndNewer(intoBuf, dt, bufferType, cal);
            } else {
                storeDateTime412AndOlder(intoBuf, dt, bufferType);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x006e A[Catch: all -> 0x0129, all -> 0x0142, TryCatch #1 {all -> 0x0129, blocks: (B:20:0x0061, B:22:0x006e, B:23:0x0086, B:26:0x0095, B:28:0x00d6, B:32:0x0113, B:29:0x00e8), top: B:51:0x0061, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0091  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00d6 A[Catch: all -> 0x0129, all -> 0x0142, TryCatch #1 {all -> 0x0129, blocks: (B:20:0x0061, B:22:0x006e, B:23:0x0086, B:26:0x0095, B:28:0x00d6, B:32:0x0113, B:29:0x00e8), top: B:51:0x0061, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00e8 A[Catch: all -> 0x0129, all -> 0x0142, TryCatch #1 {all -> 0x0129, blocks: (B:20:0x0061, B:22:0x006e, B:23:0x0086, B:26:0x0095, B:28:0x00d6, B:32:0x0113, B:29:0x00e8), top: B:51:0x0061, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0113 A[Catch: all -> 0x0129, all -> 0x0142, TryCatch #1 {all -> 0x0129, blocks: (B:20:0x0061, B:22:0x006e, B:23:0x0086, B:26:0x0095, B:28:0x00d6, B:32:0x0113, B:29:0x00e8), top: B:51:0x0061, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void storeDateTime413AndNewer(com.mysql.jdbc.Buffer r5, java.util.Date r6, int r7, java.util.Calendar r8) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 331
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.storeDateTime413AndNewer(com.mysql.jdbc.Buffer, java.util.Date, int, java.util.Calendar):void");
    }

    private Calendar getServerTzCalendar() throws SQLException {
        Calendar calendar;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.serverTzCalendar == null) {
                this.serverTzCalendar = new GregorianCalendar(this.connection.getServerTimezoneTZ());
            }
            calendar = this.serverTzCalendar;
        }
        return calendar;
    }

    private Calendar getDefaultTzCalendar() throws SQLException {
        Calendar calendar;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.defaultTzCalendar == null) {
                this.defaultTzCalendar = new GregorianCalendar(TimeZone.getDefault());
            }
            calendar = this.defaultTzCalendar;
        }
        return calendar;
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void storeReader(com.mysql.jdbc.MysqlIO r10, int r11, com.mysql.jdbc.Buffer r12, java.io.Reader r13) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 428
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.storeReader(com.mysql.jdbc.MysqlIO, int, com.mysql.jdbc.Buffer, java.io.Reader):void");
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void storeStream(com.mysql.jdbc.MysqlIO r9, int r10, com.mysql.jdbc.Buffer r11, java.io.InputStream r12) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ServerPreparedStatement.storeStream(com.mysql.jdbc.MysqlIO, int, com.mysql.jdbc.Buffer, java.io.InputStream):void");
    }

    @Override // com.mysql.jdbc.PreparedStatement
    public String toString() {
        StringBuilder toStringBuf = new StringBuilder();
        toStringBuf.append("com.mysql.jdbc.ServerPreparedStatement[");
        toStringBuf.append(this.serverStatementId);
        toStringBuf.append("] - ");
        try {
            toStringBuf.append(asSql());
        } catch (SQLException sqlEx) {
            toStringBuf.append(Messages.getString("ServerPreparedStatement.6"));
            toStringBuf.append(sqlEx);
        }
        return toStringBuf.toString();
    }

    protected long getServerStatementId() {
        return this.serverStatementId;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.hasCheckedRewrite) {
                this.hasCheckedRewrite = true;
                this.canRewrite = canRewrite(this.originalSql, isOnDuplicateKeyUpdate(), getLocationOfOnDuplicateKeyUpdate(), 0);
                this.parseInfo = new PreparedStatement.ParseInfo(this.originalSql, this.connection, this.connection.getMetaData(), this.charEncoding, this.charConverter);
            }
            z = this.canRewrite;
        }
        return z;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected int getLocationOfOnDuplicateKeyUpdate() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.locationOfOnDuplicateKeyUpdate == -2) {
                this.locationOfOnDuplicateKeyUpdate = getOnDuplicateKeyLocation(this.originalSql, this.connection.getDontCheckOnDuplicateKeyUpdateInSQL(), this.connection.getRewriteBatchedStatements(), this.connection.isNoBackslashEscapesSet());
            }
            i = this.locationOfOnDuplicateKeyUpdate;
        }
        return i;
    }

    protected boolean isOnDuplicateKeyUpdate() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            z = getLocationOfOnDuplicateKeyUpdate() != -1;
        }
        return z;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) throws SQLException {
        long[] jArr;
        synchronized (checkClosed().getConnectionMutex()) {
            long sizeOfEntireBatch = 10;
            long maxSizeOfParameterSet = 0;
            for (int i = 0; i < numBatchedArgs; i++) {
                BindValue[] paramArg = ((BatchedBindValues) this.batchedArgs.get(i)).batchedParameterValues;
                long sizeOfParameterSet = 0 + ((this.parameterCount + 7) / 8);
                long sizeOfParameterSet2 = sizeOfParameterSet + (this.parameterCount * 2);
                for (int j = 0; j < this.parameterBindings.length; j++) {
                    if (!paramArg[j].isNull) {
                        long size = paramArg[j].getBoundLength();
                        if (!paramArg[j].isLongData) {
                            sizeOfParameterSet2 += size;
                        } else if (size != -1) {
                            sizeOfParameterSet2 += size;
                        }
                    }
                }
                sizeOfEntireBatch += sizeOfParameterSet2;
                if (sizeOfParameterSet2 > maxSizeOfParameterSet) {
                    maxSizeOfParameterSet = sizeOfParameterSet2;
                }
            }
            jArr = new long[]{maxSizeOfParameterSet, sizeOfEntireBatch};
        }
        return jArr;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected int setOneBatchedParameterSet(java.sql.PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
        BindValue[] paramArg = ((BatchedBindValues) paramSet).batchedParameterValues;
        for (int j = 0; j < paramArg.length; j++) {
            if (paramArg[j].isNull) {
                int i = batchedParamIndex;
                batchedParamIndex++;
                batchedStatement.setNull(i, 0);
            } else if (paramArg[j].isLongData) {
                Object value = paramArg[j].value;
                if (value instanceof InputStream) {
                    int i2 = batchedParamIndex;
                    batchedParamIndex++;
                    batchedStatement.setBinaryStream(i2, (InputStream) value, (int) paramArg[j].bindLength);
                } else {
                    int i3 = batchedParamIndex;
                    batchedParamIndex++;
                    batchedStatement.setCharacterStream(i3, (Reader) value, (int) paramArg[j].bindLength);
                }
            } else {
                switch (paramArg[j].bufferType) {
                    case 0:
                    case 15:
                    case EscherProperties.GEOTEXT__CHARBOUNDINGBOX /* 246 */:
                    case 253:
                    case 254:
                        Object value2 = paramArg[j].value;
                        if (value2 instanceof byte[]) {
                            batchedStatement.setBytes(batchedParamIndex, (byte[]) value2);
                        } else {
                            batchedStatement.setString(batchedParamIndex, (String) value2);
                        }
                        if (batchedStatement instanceof ServerPreparedStatement) {
                            BindValue asBound = ((ServerPreparedStatement) batchedStatement).getBinding(batchedParamIndex, false);
                            asBound.bufferType = paramArg[j].bufferType;
                        }
                        batchedParamIndex++;
                        break;
                    case 1:
                        int i4 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setByte(i4, (byte) paramArg[j].longBinding);
                        break;
                    case 2:
                        int i5 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setShort(i5, (short) paramArg[j].longBinding);
                        break;
                    case 3:
                        int i6 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setInt(i6, (int) paramArg[j].longBinding);
                        break;
                    case 4:
                        int i7 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setFloat(i7, paramArg[j].floatBinding);
                        break;
                    case 5:
                        int i8 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setDouble(i8, paramArg[j].doubleBinding);
                        break;
                    case 7:
                    case 12:
                        int i9 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setTimestamp(i9, (Timestamp) paramArg[j].value);
                        break;
                    case 8:
                        int i10 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setLong(i10, paramArg[j].longBinding);
                        break;
                    case 10:
                        int i11 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setDate(i11, (java.sql.Date) paramArg[j].value);
                        break;
                    case 11:
                        int i12 = batchedParamIndex;
                        batchedParamIndex++;
                        batchedStatement.setTime(i12, (Time) paramArg[j].value);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown type when re-binding parameter into batched statement for parameter index " + batchedParamIndex);
                }
            }
        }
        return batchedParamIndex;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected boolean containsOnDuplicateKeyUpdateInSQL() {
        return this.hasOnDuplicateKeyUpdate;
    }

    @Override // com.mysql.jdbc.PreparedStatement
    protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches) throws SQLException {
        PreparedStatement pstmt;
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                pstmt = (PreparedStatement) ((Wrapper) localConn.prepareStatement(this.parseInfo.getSqlForBatch(numBatches), this.resultSetType, this.resultSetConcurrency)).unwrap(PreparedStatement.class);
                pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
            } catch (UnsupportedEncodingException e) {
                SQLException sqlEx = SQLError.createSQLException("Unable to prepare batch statement", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
                sqlEx.initCause(e);
                throw sqlEx;
            }
        }
        return pstmt;
    }

    @Override // com.mysql.jdbc.StatementImpl, java.sql.Statement
    public void setPoolable(boolean poolable) throws SQLException {
        if (!poolable) {
            this.connection.decachePreparedStatement(this);
        }
        super.setPoolable(poolable);
    }
}
