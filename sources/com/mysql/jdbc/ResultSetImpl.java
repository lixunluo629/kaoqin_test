package com.mysql.jdbc;

import com.itextpdf.layout.element.List;
import com.mysql.jdbc.log.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Date;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ResultSetImpl.class */
public class ResultSetImpl implements ResultSetInternalMethods {
    private static final Constructor<?> JDBC_4_RS_4_ARG_CTOR;
    private static final Constructor<?> JDBC_4_RS_5_ARG_CTOR;
    private static final Constructor<?> JDBC_4_UPD_RS_5_ARG_CTOR;
    protected static final double MIN_DIFF_PREC;
    protected static final double MAX_DIFF_PREC;
    static int resultCounter;
    protected String catalog;
    protected Map<String, Integer> columnLabelToIndex;
    protected Map<String, Integer> columnToIndexCache;
    protected boolean[] columnUsed;
    protected volatile MySQLConnection connection;
    protected int currentRow;
    protected boolean doingUpdates;
    Calendar fastDefaultCal;
    Calendar fastClientCal;
    protected int fetchDirection;
    protected int fetchSize;
    protected Field[] fields;
    protected char firstCharOfQuery;
    protected Map<String, Integer> fullColumnNameToIndex;
    protected Map<String, Integer> columnNameToIndex;
    protected boolean hasBuiltIndexMapping;
    protected boolean isBinaryEncoded;
    protected boolean isClosed;
    protected ResultSetInternalMethods nextResultSet;
    protected boolean onInsertRow;
    protected StatementImpl owningStatement;
    protected String pointOfOrigin;
    protected boolean reallyResult;
    protected int resultId;
    protected int resultSetConcurrency;
    protected int resultSetType;
    protected RowData rowData;
    protected String serverInfo;
    PreparedStatement statementUsedForFetchingRows;
    protected ResultSetRow thisRow;
    protected long updateCount;
    protected long updateId;
    private boolean useStrictFloatingPoint;
    protected boolean useUsageAdvisor;
    protected SQLWarning warningChain;
    protected boolean wasNullFlag;
    protected java.sql.Statement wrapperStatement;
    protected boolean retainOwningStatement;
    protected Calendar gmtCalendar;
    protected boolean useFastDateParsing;
    private boolean padCharsWithSpace;
    private boolean jdbcCompliantTruncationForReads;
    private boolean useFastIntParsing;
    private boolean useColumnNamesInFindColumn;
    private ExceptionInterceptor exceptionInterceptor;
    static final char[] EMPTY_SPACE;
    private boolean onValidRow;
    private String invalidRowReason;
    protected boolean useLegacyDatetimeCode;
    private TimeZone serverTimeZoneTz;

    static {
        if (Util.isJdbc4()) {
            try {
                String jdbc4ClassName = Util.isJdbc42() ? "com.mysql.jdbc.JDBC42ResultSet" : "com.mysql.jdbc.JDBC4ResultSet";
                JDBC_4_RS_4_ARG_CTOR = Class.forName(jdbc4ClassName).getConstructor(Long.TYPE, Long.TYPE, MySQLConnection.class, StatementImpl.class);
                JDBC_4_RS_5_ARG_CTOR = Class.forName(jdbc4ClassName).getConstructor(String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class);
                JDBC_4_UPD_RS_5_ARG_CTOR = Class.forName(Util.isJdbc42() ? "com.mysql.jdbc.JDBC42UpdatableResultSet" : "com.mysql.jdbc.JDBC4UpdatableResultSet").getConstructor(String.class, Field[].class, RowData.class, MySQLConnection.class, StatementImpl.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            } catch (SecurityException e3) {
                throw new RuntimeException(e3);
            }
        } else {
            JDBC_4_RS_4_ARG_CTOR = null;
            JDBC_4_RS_5_ARG_CTOR = null;
            JDBC_4_UPD_RS_5_ARG_CTOR = null;
        }
        MIN_DIFF_PREC = Float.parseFloat(Float.toString(Float.MIN_VALUE)) - Double.parseDouble(Float.toString(Float.MIN_VALUE));
        MAX_DIFF_PREC = Float.parseFloat(Float.toString(Float.MAX_VALUE)) - Double.parseDouble(Float.toString(Float.MAX_VALUE));
        resultCounter = 1;
        EMPTY_SPACE = new char[255];
        for (int i = 0; i < EMPTY_SPACE.length; i++) {
            EMPTY_SPACE[i] = ' ';
        }
    }

    protected static BigInteger convertLongToUlong(long longVal) {
        byte[] asBytes = {(byte) (longVal >>> 56), (byte) (longVal >>> 48), (byte) (longVal >>> 40), (byte) (longVal >>> 32), (byte) (longVal >>> 24), (byte) (longVal >>> 16), (byte) (longVal >>> 8), (byte) (longVal & 255)};
        return new BigInteger(1, asBytes);
    }

    protected static ResultSetImpl getInstance(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        return !Util.isJdbc4() ? new ResultSetImpl(updateCount, updateID, conn, creatorStmt) : (ResultSetImpl) Util.handleNewInstance(JDBC_4_RS_4_ARG_CTOR, new Object[]{Long.valueOf(updateCount), Long.valueOf(updateID), conn, creatorStmt}, conn.getExceptionInterceptor());
    }

    protected static ResultSetImpl getInstance(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt, boolean isUpdatable) throws SQLException {
        if (Util.isJdbc4()) {
            return !isUpdatable ? (ResultSetImpl) Util.handleNewInstance(JDBC_4_RS_5_ARG_CTOR, new Object[]{catalog, fields, tuples, conn, creatorStmt}, conn.getExceptionInterceptor()) : (ResultSetImpl) Util.handleNewInstance(JDBC_4_UPD_RS_5_ARG_CTOR, new Object[]{catalog, fields, tuples, conn, creatorStmt}, conn.getExceptionInterceptor());
        }
        if (!isUpdatable) {
            return new ResultSetImpl(catalog, fields, tuples, conn, creatorStmt);
        }
        return new UpdatableResultSet(catalog, fields, tuples, conn, creatorStmt);
    }

    public ResultSetImpl(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt) {
        this.catalog = null;
        this.columnLabelToIndex = null;
        this.columnToIndexCache = null;
        this.columnUsed = null;
        this.currentRow = -1;
        this.doingUpdates = false;
        this.fastDefaultCal = null;
        this.fastClientCal = null;
        this.fetchDirection = 1000;
        this.fetchSize = 0;
        this.fullColumnNameToIndex = null;
        this.columnNameToIndex = null;
        this.hasBuiltIndexMapping = false;
        this.isBinaryEncoded = false;
        this.isClosed = false;
        this.nextResultSet = null;
        this.onInsertRow = false;
        this.reallyResult = false;
        this.resultSetConcurrency = 0;
        this.resultSetType = 0;
        this.serverInfo = null;
        this.thisRow = null;
        this.updateId = -1L;
        this.useStrictFloatingPoint = false;
        this.useUsageAdvisor = false;
        this.warningChain = null;
        this.wasNullFlag = false;
        this.gmtCalendar = null;
        this.useFastDateParsing = false;
        this.padCharsWithSpace = false;
        this.useFastIntParsing = true;
        this.onValidRow = false;
        this.invalidRowReason = null;
        this.updateCount = updateCount;
        this.updateId = updateID;
        this.reallyResult = false;
        this.fields = new Field[0];
        this.connection = conn;
        this.owningStatement = creatorStmt;
        this.retainOwningStatement = false;
        if (this.connection != null) {
            this.exceptionInterceptor = this.connection.getExceptionInterceptor();
            this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
            this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
            this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
            this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
            this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
        }
    }

    public ResultSetImpl(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        this.catalog = null;
        this.columnLabelToIndex = null;
        this.columnToIndexCache = null;
        this.columnUsed = null;
        this.currentRow = -1;
        this.doingUpdates = false;
        this.fastDefaultCal = null;
        this.fastClientCal = null;
        this.fetchDirection = 1000;
        this.fetchSize = 0;
        this.fullColumnNameToIndex = null;
        this.columnNameToIndex = null;
        this.hasBuiltIndexMapping = false;
        this.isBinaryEncoded = false;
        this.isClosed = false;
        this.nextResultSet = null;
        this.onInsertRow = false;
        this.reallyResult = false;
        this.resultSetConcurrency = 0;
        this.resultSetType = 0;
        this.serverInfo = null;
        this.thisRow = null;
        this.updateId = -1L;
        this.useStrictFloatingPoint = false;
        this.useUsageAdvisor = false;
        this.warningChain = null;
        this.wasNullFlag = false;
        this.gmtCalendar = null;
        this.useFastDateParsing = false;
        this.padCharsWithSpace = false;
        this.useFastIntParsing = true;
        this.onValidRow = false;
        this.invalidRowReason = null;
        this.connection = conn;
        this.retainOwningStatement = false;
        if (this.connection != null) {
            this.exceptionInterceptor = this.connection.getExceptionInterceptor();
            this.useStrictFloatingPoint = this.connection.getStrictFloatingPoint();
            this.useFastDateParsing = this.connection.getUseFastDateParsing();
            this.retainOwningStatement = this.connection.getRetainStatementAfterResultSetClose();
            this.jdbcCompliantTruncationForReads = this.connection.getJdbcCompliantTruncationForReads();
            this.useFastIntParsing = this.connection.getUseFastIntParsing();
            this.serverTimeZoneTz = this.connection.getServerTimezoneTZ();
            this.padCharsWithSpace = this.connection.getPadCharsWithSpace();
            this.useUsageAdvisor = this.connection.getUseUsageAdvisor();
        }
        this.owningStatement = creatorStmt;
        this.catalog = catalog;
        this.fields = fields;
        this.rowData = tuples;
        this.updateCount = this.rowData.size();
        this.reallyResult = true;
        if (this.rowData.size() > 0) {
            if (this.updateCount == 1 && this.thisRow == null) {
                this.rowData.close();
                this.updateCount = -1L;
            }
        } else {
            this.thisRow = null;
        }
        this.rowData.setOwner(this);
        if (this.fields != null) {
            initializeWithMetadata();
        }
        this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode();
        this.useColumnNamesInFindColumn = this.connection.getUseColumnNamesInFindColumn();
        setRowPositionValidity();
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void initializeWithMetadata() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.rowData.setMetadata(this.fields);
            this.columnToIndexCache = new HashMap();
            if (this.useUsageAdvisor) {
                this.columnUsed = new boolean[this.fields.length];
                this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable());
                int i = resultCounter;
                resultCounter = i + 1;
                this.resultId = i;
            }
            if (this.connection.getGatherPerformanceMetrics()) {
                this.connection.incrementNumberOfResultSetsCreated();
                Set<String> tableNamesSet = new HashSet<>();
                for (int i2 = 0; i2 < this.fields.length; i2++) {
                    Field f = this.fields[i2];
                    String tableName = f.getOriginalTableName();
                    if (tableName == null) {
                        tableName = f.getTableName();
                    }
                    if (tableName != null) {
                        if (this.connection.lowerCaseTableNames()) {
                            tableName = tableName.toLowerCase();
                        }
                        tableNamesSet.add(tableName);
                    }
                }
                this.connection.reportNumberOfTablesAccessed(tableNamesSet.size());
            }
        }
    }

    private synchronized Calendar getFastDefaultCalendar() {
        if (this.fastDefaultCal == null) {
            this.fastDefaultCal = new GregorianCalendar(Locale.US);
            this.fastDefaultCal.setTimeZone(getDefaultTimeZone());
        }
        return this.fastDefaultCal;
    }

    private synchronized Calendar getFastClientCalendar() {
        if (this.fastClientCal == null) {
            this.fastClientCal = new GregorianCalendar(Locale.US);
        }
        return this.fastClientCal;
    }

    @Override // java.sql.ResultSet
    public boolean absolute(int row) throws SQLException {
        boolean b;
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.rowData.size() == 0) {
                b = false;
            } else {
                if (this.onInsertRow) {
                    this.onInsertRow = false;
                }
                if (this.doingUpdates) {
                    this.doingUpdates = false;
                }
                if (this.thisRow != null) {
                    this.thisRow.closeOpenStreams();
                }
                if (row == 0) {
                    beforeFirst();
                    b = false;
                } else if (row == 1) {
                    b = first();
                } else if (row == -1) {
                    b = last();
                } else if (row > this.rowData.size()) {
                    afterLast();
                    b = false;
                } else if (row < 0) {
                    int newRowPosition = this.rowData.size() + row + 1;
                    if (newRowPosition <= 0) {
                        beforeFirst();
                        b = false;
                    } else {
                        b = absolute(newRowPosition);
                    }
                } else {
                    int row2 = row - 1;
                    this.rowData.setCurrentRow(row2);
                    this.thisRow = this.rowData.getAt(row2);
                    b = true;
                }
            }
            setRowPositionValidity();
            z = b;
        }
        return z;
    }

    @Override // java.sql.ResultSet
    public void afterLast() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.onInsertRow) {
                this.onInsertRow = false;
            }
            if (this.doingUpdates) {
                this.doingUpdates = false;
            }
            if (this.thisRow != null) {
                this.thisRow.closeOpenStreams();
            }
            if (this.rowData.size() != 0) {
                this.rowData.afterLast();
                this.thisRow = null;
            }
            setRowPositionValidity();
        }
    }

    @Override // java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.onInsertRow) {
                this.onInsertRow = false;
            }
            if (this.doingUpdates) {
                this.doingUpdates = false;
            }
            if (this.rowData.size() == 0) {
                return;
            }
            if (this.thisRow != null) {
                this.thisRow.closeOpenStreams();
            }
            this.rowData.beforeFirst();
            this.thisRow = null;
            setRowPositionValidity();
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void buildIndexMapping() throws SQLException {
        int numFields = this.fields.length;
        this.columnLabelToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.fullColumnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        this.columnNameToIndex = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = numFields - 1; i >= 0; i--) {
            Integer index = Integer.valueOf(i);
            String columnName = this.fields[i].getOriginalName();
            String columnLabel = this.fields[i].getName();
            String fullColumnName = this.fields[i].getFullName();
            if (columnLabel != null) {
                this.columnLabelToIndex.put(columnLabel, index);
            }
            if (fullColumnName != null) {
                this.fullColumnNameToIndex.put(fullColumnName, index);
            }
            if (columnName != null) {
                this.columnNameToIndex.put(columnName, index);
            }
        }
        this.hasBuiltIndexMapping = true;
    }

    @Override // java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        throw new NotUpdatable();
    }

    protected final MySQLConnection checkClosed() throws SQLException {
        MySQLConnection c = this.connection;
        if (c == null) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Operation_not_allowed_after_ResultSet_closed_144"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
        return c;
    }

    protected final void checkColumnBounds(int columnIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (columnIndex < 1) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_low", new Object[]{Integer.valueOf(columnIndex), Integer.valueOf(this.fields.length)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (columnIndex > this.fields.length) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Column_Index_out_of_range_high", new Object[]{Integer.valueOf(columnIndex), Integer.valueOf(this.fields.length)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (this.useUsageAdvisor) {
                this.columnUsed[columnIndex - 1] = true;
            }
        }
    }

    protected void checkRowPos() throws SQLException {
        checkClosed();
        if (!this.onValidRow) {
            throw SQLError.createSQLException(this.invalidRowReason, SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
        }
    }

    private void setRowPositionValidity() throws SQLException {
        if (!this.rowData.isDynamic() && this.rowData.size() == 0) {
            this.invalidRowReason = Messages.getString("ResultSet.Illegal_operation_on_empty_result_set");
            this.onValidRow = false;
        } else if (this.rowData.isBeforeFirst()) {
            this.invalidRowReason = Messages.getString("ResultSet.Before_start_of_result_set_146");
            this.onValidRow = false;
        } else if (this.rowData.isAfterLast()) {
            this.invalidRowReason = Messages.getString("ResultSet.After_end_of_result_set_148");
            this.onValidRow = false;
        } else {
            this.onValidRow = true;
            this.invalidRowReason = null;
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public synchronized void clearNextResult() {
        this.nextResultSet = null;
    }

    @Override // java.sql.ResultSet
    public void clearWarnings() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            this.warningChain = null;
        }
    }

    @Override // java.sql.ResultSet, java.lang.AutoCloseable
    public void close() throws SQLException {
        realClose(true);
    }

    private int convertToZeroWithEmptyCheck() throws SQLException {
        if (this.connection.getEmptyStringsConvertToZero()) {
            return 0;
        }
        throw SQLError.createSQLException("Can't convert empty string ('') to numeric", SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST, getExceptionInterceptor());
    }

    private String convertToZeroLiteralStringWithEmptyCheck() throws SQLException {
        if (this.connection.getEmptyStringsConvertToZero()) {
            return "0";
        }
        throw SQLError.createSQLException("Can't convert empty string ('') to numeric", SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST, getExceptionInterceptor());
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public ResultSetInternalMethods copy() throws SQLException {
        ResultSetImpl rs;
        synchronized (checkClosed().getConnectionMutex()) {
            rs = getInstance(this.catalog, this.fields, this.rowData, this.connection, this.owningStatement, false);
            if (this.isBinaryEncoded) {
                rs.setBinaryEncoded();
            }
        }
        return rs;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void redefineFieldsForDBMD(Field[] f) {
        this.fields = f;
        for (int i = 0; i < this.fields.length; i++) {
            this.fields[i].setUseOldNameMetadata(true);
            this.fields[i].setConnection(this.connection);
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void populateCachedMetaData(CachedResultSetMetaData cachedMetaData) throws SQLException {
        cachedMetaData.fields = this.fields;
        cachedMetaData.columnNameToIndex = this.columnLabelToIndex;
        cachedMetaData.fullColumnNameToIndex = this.fullColumnNameToIndex;
        cachedMetaData.metadata = getMetaData();
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void initializeFromCachedMetaData(CachedResultSetMetaData cachedMetaData) {
        this.fields = cachedMetaData.fields;
        this.columnLabelToIndex = cachedMetaData.columnNameToIndex;
        this.fullColumnNameToIndex = cachedMetaData.fullColumnNameToIndex;
        this.hasBuiltIndexMapping = true;
    }

    @Override // java.sql.ResultSet
    public void deleteRow() throws SQLException {
        throw new NotUpdatable();
    }

    private String extractStringFromNativeColumn(int columnIndex, int mysqlType) throws SQLException {
        int columnIndexMinusOne = columnIndex - 1;
        this.wasNullFlag = false;
        if (this.thisRow.isNull(columnIndexMinusOne)) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        String encoding = this.fields[columnIndexMinusOne].getCollationIndex() == 63 ? this.connection.getEncoding() : this.fields[columnIndexMinusOne].getEncoding();
        return this.thisRow.getString(columnIndex - 1, encoding, this.connection);
    }

    protected Date fastDateCreate(Calendar cal, int year, int month, int day) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            Calendar targetCalendar = cal;
            if (cal == null) {
                if (this.connection.getNoTimezoneConversionForDateType()) {
                    targetCalendar = getFastClientCalendar();
                } else {
                    targetCalendar = getFastDefaultCalendar();
                }
            }
            if (!this.useLegacyDatetimeCode) {
                return TimeUtil.fastDateCreate(year, month, day, targetCalendar);
            }
            boolean useGmtMillis = cal == null && !this.connection.getNoTimezoneConversionForDateType() && this.connection.getUseGmtMillisForDatetimes();
            return TimeUtil.fastDateCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : targetCalendar, targetCalendar, year, month, day);
        }
    }

    protected Time fastTimeCreate(Calendar cal, int hour, int minute, int second) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.useLegacyDatetimeCode) {
                return TimeUtil.fastTimeCreate(hour, minute, second, cal, getExceptionInterceptor());
            }
            if (cal == null) {
                cal = getFastDefaultCalendar();
            }
            return TimeUtil.fastTimeCreate(cal, hour, minute, second, getExceptionInterceptor());
        }
    }

    protected Timestamp fastTimestampCreate(Calendar cal, int year, int month, int day, int hour, int minute, int seconds, int secondsPart, boolean useGmtMillis) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.useLegacyDatetimeCode) {
                return TimeUtil.fastTimestampCreate(cal.getTimeZone(), year, month, day, hour, minute, seconds, secondsPart);
            }
            if (cal == null) {
                cal = getFastDefaultCalendar();
            }
            return TimeUtil.fastTimestampCreate(useGmtMillis, useGmtMillis ? getGmtCalendar() : null, cal, year, month, day, hour, minute, seconds, secondsPart);
        }
    }

    @Override // java.sql.ResultSet
    public int findColumn(String columnName) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.hasBuiltIndexMapping) {
                buildIndexMapping();
            }
            Integer index = this.columnToIndexCache.get(columnName);
            if (index != null) {
                return index.intValue() + 1;
            }
            Integer index2 = this.columnLabelToIndex.get(columnName);
            if (index2 == null && this.useColumnNamesInFindColumn) {
                index2 = this.columnNameToIndex.get(columnName);
            }
            if (index2 == null) {
                index2 = this.fullColumnNameToIndex.get(columnName);
            }
            if (index2 != null) {
                this.columnToIndexCache.put(columnName, index2);
                return index2.intValue() + 1;
            }
            for (int i = 0; i < this.fields.length; i++) {
                if (this.fields[i].getName().equalsIgnoreCase(columnName)) {
                    return i + 1;
                }
                if (this.fields[i].getFullName().equalsIgnoreCase(columnName)) {
                    return i + 1;
                }
            }
            throw SQLError.createSQLException(Messages.getString("ResultSet.Column____112") + columnName + Messages.getString("ResultSet.___not_found._113"), SQLError.SQL_STATE_COLUMN_NOT_FOUND, getExceptionInterceptor());
        }
    }

    @Override // java.sql.ResultSet
    public boolean first() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            boolean b = true;
            if (this.rowData.isEmpty()) {
                b = false;
            } else {
                if (this.onInsertRow) {
                    this.onInsertRow = false;
                }
                if (this.doingUpdates) {
                    this.doingUpdates = false;
                }
                this.rowData.beforeFirst();
                this.thisRow = this.rowData.next();
            }
            setRowPositionValidity();
            z = b;
        }
        return z;
    }

    @Override // java.sql.ResultSet
    public Array getArray(int i) throws SQLException {
        checkColumnBounds(i);
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public Array getArray(String colName) throws SQLException {
        return getArray(findColumn(colName));
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        checkRowPos();
        if (!this.isBinaryEncoded) {
            return getBinaryStream(columnIndex);
        }
        return getNativeBinaryStream(columnIndex);
    }

    @Override // java.sql.ResultSet
    public InputStream getAsciiStream(String columnName) throws SQLException {
        return getAsciiStream(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException, NumberFormatException {
        if (!this.isBinaryEncoded) {
            String stringVal = getString(columnIndex);
            if (stringVal != null) {
                if (stringVal.length() == 0) {
                    BigDecimal val = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
                    return val;
                }
                try {
                    BigDecimal val2 = new BigDecimal(stringVal);
                    return val2;
                } catch (NumberFormatException e) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
            return null;
        }
        return getNativeBigDecimal(columnIndex);
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException, NumberFormatException {
        BigDecimal val;
        if (!this.isBinaryEncoded) {
            String stringVal = getString(columnIndex);
            if (stringVal != null) {
                if (stringVal.length() == 0) {
                    BigDecimal val2 = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
                    try {
                        return val2.setScale(scale);
                    } catch (ArithmeticException e) {
                        try {
                            return val2.setScale(scale, 4);
                        } catch (ArithmeticException e2) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                }
                try {
                    val = new BigDecimal(stringVal);
                } catch (NumberFormatException e3) {
                    if (this.fields[columnIndex - 1].getMysqlType() == 16) {
                        long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
                        val = new BigDecimal(valueAsLong);
                    } else {
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{Integer.valueOf(columnIndex), stringVal}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                }
                try {
                    return val.setScale(scale);
                } catch (ArithmeticException e4) {
                    try {
                        return val.setScale(scale, 4);
                    } catch (ArithmeticException e5) {
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{Integer.valueOf(columnIndex), stringVal}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                }
            }
            return null;
        }
        return getNativeBigDecimal(columnIndex, scale);
    }

    @Override // java.sql.ResultSet
    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return getBigDecimal(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        return getBigDecimal(findColumn(columnName), scale);
    }

    private final BigDecimal getBigDecimalFromString(String stringVal, int columnIndex, int scale) throws SQLException {
        if (stringVal != null) {
            try {
                if (stringVal.length() == 0) {
                    BigDecimal bdVal = new BigDecimal(convertToZeroLiteralStringWithEmptyCheck());
                    try {
                        return bdVal.setScale(scale);
                    } catch (ArithmeticException e) {
                        try {
                            return bdVal.setScale(scale, 4);
                        } catch (ArithmeticException e2) {
                            throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                        }
                    }
                }
                try {
                    return new BigDecimal(stringVal).setScale(scale);
                } catch (ArithmeticException e3) {
                    try {
                        return new BigDecimal(stringVal).setScale(scale, 4);
                    } catch (ArithmeticException e4) {
                        throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                    }
                }
            } catch (NumberFormatException e5) {
                if (this.fields[columnIndex - 1].getMysqlType() == 16) {
                    long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
                    try {
                        return new BigDecimal(valueAsLong).setScale(scale);
                    } catch (ArithmeticException e6) {
                        try {
                            return new BigDecimal(valueAsLong).setScale(scale, 4);
                        } catch (ArithmeticException e7) {
                            throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                        }
                    }
                }
                if (this.fields[columnIndex - 1].getMysqlType() == 1 && this.connection.getTinyInt1isBit() && this.fields[columnIndex - 1].getLength() == 1) {
                    return new BigDecimal(stringVal.equalsIgnoreCase("true") ? 1 : 0).setScale(scale);
                }
                throw new SQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        }
        return null;
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        checkRowPos();
        if (!this.isBinaryEncoded) {
            checkColumnBounds(columnIndex);
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
                return null;
            }
            this.wasNullFlag = false;
            return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
        }
        return getNativeBinaryStream(columnIndex);
    }

    @Override // java.sql.ResultSet
    public InputStream getBinaryStream(String columnName) throws SQLException {
        return getBinaryStream(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public java.sql.Blob getBlob(int columnIndex) throws SQLException {
        if (!this.isBinaryEncoded) {
            checkRowPos();
            checkColumnBounds(columnIndex);
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
            } else {
                this.wasNullFlag = false;
            }
            if (this.wasNullFlag) {
                return null;
            }
            if (!this.connection.getEmulateLocators()) {
                return new Blob(this.thisRow.getColumnValue(columnIndexMinusOne), getExceptionInterceptor());
            }
            return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
        }
        return getNativeBlob(columnIndex);
    }

    @Override // java.sql.ResultSet
    public java.sql.Blob getBlob(String colName) throws SQLException {
        return getBlob(findColumn(colName));
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(int columnIndex) throws SQLException, NumberFormatException {
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        Field field = this.fields[columnIndexMinusOne];
        if (field.getMysqlType() == 16) {
            return byteArrayToBoolean(columnIndexMinusOne);
        }
        this.wasNullFlag = false;
        int sqlType = field.getSQLType();
        switch (sqlType) {
            case -7:
            case -6:
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                long boolVal = getLong(columnIndex, false);
                return boolVal == -1 || boolVal > 0;
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            default:
                if (this.connection.getPedantic()) {
                    switch (sqlType) {
                        case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                        case -3:
                        case -2:
                        case 70:
                        case 91:
                        case 92:
                        case 93:
                        case 2000:
                        case 2002:
                        case 2003:
                        case 2004:
                        case 2005:
                        case 2006:
                            throw SQLError.createSQLException("Required type conversion not allowed", SQLError.SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST, getExceptionInterceptor());
                    }
                }
                if (sqlType == -2 || sqlType == -3 || sqlType == -4 || sqlType == 2004) {
                    return byteArrayToBoolean(columnIndexMinusOne);
                }
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getBoolean()", columnIndex, this.thisRow.getColumnValue(columnIndexMinusOne), this.fields[columnIndex], new int[]{16, 5, 1, 2, 3, 8, 4});
                }
                String stringVal = getString(columnIndex);
                return getBooleanFromString(stringVal);
            case 16:
                if (field.getMysqlType() == -1) {
                    String stringVal2 = getString(columnIndex);
                    return getBooleanFromString(stringVal2);
                }
                long boolVal2 = getLong(columnIndex, false);
                return boolVal2 == -1 || boolVal2 > 0;
        }
    }

    private boolean byteArrayToBoolean(int columnIndexMinusOne) throws SQLException {
        Object value = this.thisRow.getColumnValue(columnIndexMinusOne);
        if (value == null) {
            this.wasNullFlag = true;
            return false;
        }
        this.wasNullFlag = false;
        if (((byte[]) value).length == 0) {
            return false;
        }
        byte boolVal = ((byte[]) value)[0];
        if (boolVal == 49) {
            return true;
        }
        if (boolVal == 48) {
            return false;
        }
        return boolVal == -1 || boolVal > 0;
    }

    @Override // java.sql.ResultSet
    public boolean getBoolean(String columnName) throws SQLException {
        return getBoolean(findColumn(columnName));
    }

    private final boolean getBooleanFromString(String stringVal) throws SQLException {
        if (stringVal != null && stringVal.length() > 0) {
            int c = Character.toLowerCase(stringVal.charAt(0));
            return c == 116 || c == 121 || c == 49 || stringVal.equals("-1");
        }
        return false;
    }

    @Override // java.sql.ResultSet
    public byte getByte(int columnIndex) throws SQLException, NumberFormatException {
        if (!this.isBinaryEncoded) {
            String stringVal = getString(columnIndex);
            if (this.wasNullFlag || stringVal == null) {
                return (byte) 0;
            }
            return getByteFromString(stringVal, columnIndex);
        }
        return getNativeByte(columnIndex);
    }

    @Override // java.sql.ResultSet
    public byte getByte(String columnName) throws SQLException {
        return getByte(findColumn(columnName));
    }

    private final byte getByteFromString(String stringVal, int columnIndex) throws SQLException, NumberFormatException {
        if (stringVal != null && stringVal.length() == 0) {
            return (byte) convertToZeroWithEmptyCheck();
        }
        if (stringVal == null) {
            return (byte) 0;
        }
        String stringVal2 = stringVal.trim();
        try {
            int decimalIndex = stringVal2.indexOf(".");
            if (decimalIndex != -1) {
                double valueAsDouble = Double.parseDouble(stringVal2);
                if (this.jdbcCompliantTruncationForReads && (valueAsDouble < -128.0d || valueAsDouble > 127.0d)) {
                    throwRangeException(stringVal2, columnIndex, -6);
                }
                return (byte) valueAsDouble;
            }
            long valueAsLong = Long.parseLong(stringVal2);
            if (this.jdbcCompliantTruncationForReads && (valueAsLong < -128 || valueAsLong > 127)) {
                throwRangeException(String.valueOf(valueAsLong), columnIndex, -6);
            }
            return (byte) valueAsLong;
        } catch (NumberFormatException e) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Value____173") + stringVal2 + Messages.getString("ResultSet.___is_out_of_range_[-127,127]_174"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(int columnIndex) throws SQLException {
        return getBytes(columnIndex, false);
    }

    protected byte[] getBytes(int columnIndex, boolean noConversion) throws SQLException {
        if (!this.isBinaryEncoded) {
            checkRowPos();
            checkColumnBounds(columnIndex);
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
            } else {
                this.wasNullFlag = false;
            }
            if (this.wasNullFlag) {
                return null;
            }
            return this.thisRow.getColumnValue(columnIndexMinusOne);
        }
        return getNativeBytes(columnIndex, noConversion);
    }

    @Override // java.sql.ResultSet
    public byte[] getBytes(String columnName) throws SQLException {
        return getBytes(findColumn(columnName));
    }

    private final byte[] getBytesFromString(String stringVal) throws SQLException {
        if (stringVal != null) {
            return StringUtils.getBytes(stringVal, this.connection.getEncoding(), this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), this.connection, getExceptionInterceptor());
        }
        return null;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public int getBytesSize() throws SQLException {
        RowData localRowData = this.rowData;
        checkClosed();
        if (localRowData instanceof RowDataStatic) {
            int bytesSize = 0;
            int numRows = localRowData.size();
            for (int i = 0; i < numRows; i++) {
                bytesSize += localRowData.getAt(i).getBytesSize();
            }
            return bytesSize;
        }
        return -1;
    }

    protected Calendar getCalendarInstanceForSessionOrNew() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.connection != null) {
                return this.connection.getCalendarInstanceForSessionOrNew();
            }
            return new GregorianCalendar();
        }
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        if (!this.isBinaryEncoded) {
            checkColumnBounds(columnIndex);
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
                return null;
            }
            this.wasNullFlag = false;
            return this.thisRow.getReader(columnIndexMinusOne);
        }
        return getNativeCharacterStream(columnIndex);
    }

    @Override // java.sql.ResultSet
    public Reader getCharacterStream(String columnName) throws SQLException {
        return getCharacterStream(findColumn(columnName));
    }

    private final Reader getCharacterStreamFromString(String stringVal) throws SQLException {
        if (stringVal != null) {
            return new StringReader(stringVal);
        }
        return null;
    }

    @Override // java.sql.ResultSet
    public java.sql.Clob getClob(int i) throws SQLException, NumberFormatException {
        if (!this.isBinaryEncoded) {
            String asString = getStringForClob(i);
            if (asString == null) {
                return null;
            }
            return new Clob(asString, getExceptionInterceptor());
        }
        return getNativeClob(i);
    }

    @Override // java.sql.ResultSet
    public java.sql.Clob getClob(String colName) throws SQLException {
        return getClob(findColumn(colName));
    }

    private final java.sql.Clob getClobFromString(String stringVal) throws SQLException {
        return new Clob(stringVal, getExceptionInterceptor());
    }

    @Override // java.sql.ResultSet
    public int getConcurrency() throws SQLException {
        return 1007;
    }

    @Override // java.sql.ResultSet
    public String getCursorName() throws SQLException {
        throw SQLError.createSQLException(Messages.getString("ResultSet.Positioned_Update_not_supported"), SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, getExceptionInterceptor());
    }

    @Override // java.sql.ResultSet
    public Date getDate(int columnIndex) throws SQLException {
        return getDate(columnIndex, (Calendar) null);
    }

    @Override // java.sql.ResultSet
    public Date getDate(int columnIndex, Calendar cal) throws SQLException, NumberFormatException {
        if (this.isBinaryEncoded) {
            return getNativeDate(columnIndex, cal);
        }
        if (!this.useFastDateParsing) {
            String stringVal = getStringInternal(columnIndex, false);
            if (stringVal == null) {
                return null;
            }
            return getDateFromString(stringVal, columnIndex, cal);
        }
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        Date tmpDate = this.thisRow.getDateFast(columnIndexMinusOne, this.connection, this, cal);
        if (this.thisRow.isNull(columnIndexMinusOne) || tmpDate == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        return tmpDate;
    }

    @Override // java.sql.ResultSet
    public Date getDate(String columnName) throws SQLException {
        return getDate(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return getDate(findColumn(columnName), cal);
    }

    private final Date getDateFromString(String stringVal, int columnIndex, Calendar targetCalendar) throws SQLException, NumberFormatException {
        int year;
        int month;
        int day;
        int year2;
        try {
            this.wasNullFlag = false;
            if (stringVal == null) {
                this.wasNullFlag = true;
                return null;
            }
            String stringVal2 = stringVal.trim();
            int dec = stringVal2.indexOf(".");
            if (dec > -1) {
                stringVal2 = stringVal2.substring(0, dec);
            }
            if (stringVal2.equals("0") || stringVal2.equals("0000-00-00") || stringVal2.equals("0000-00-00 00:00:00") || stringVal2.equals("00000000000000") || stringVal2.equals("0")) {
                if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                    this.wasNullFlag = true;
                    return null;
                }
                if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(this.connection.getZeroDateTimeBehavior())) {
                    throw SQLError.createSQLException("Value '" + stringVal2 + "' can not be represented as java.sql.Date", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
                return fastDateCreate(targetCalendar, 1, 1, 1);
            }
            if (this.fields[columnIndex - 1].getMysqlType() == 7) {
                switch (stringVal2.length()) {
                    case 2:
                        int year3 = Integer.parseInt(stringVal2.substring(0, 2));
                        if (year3 <= 69) {
                            year3 += 100;
                        }
                        return fastDateCreate(targetCalendar, year3 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, 1, 1);
                    case 3:
                    case 5:
                    case 7:
                    case 9:
                    case 11:
                    case 13:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 20:
                    default:
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{stringVal2, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    case 4:
                        int year4 = Integer.parseInt(stringVal2.substring(0, 4));
                        if (year4 <= 69) {
                            year4 += 100;
                        }
                        int month2 = Integer.parseInt(stringVal2.substring(2, 4));
                        return fastDateCreate(targetCalendar, year4 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, month2, 1);
                    case 6:
                    case 10:
                    case 12:
                        int year5 = Integer.parseInt(stringVal2.substring(0, 2));
                        if (year5 <= 69) {
                            year5 += 100;
                        }
                        int month3 = Integer.parseInt(stringVal2.substring(2, 4));
                        int day2 = Integer.parseInt(stringVal2.substring(4, 6));
                        return fastDateCreate(targetCalendar, year5 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP, month3, day2);
                    case 8:
                    case 14:
                        int year6 = Integer.parseInt(stringVal2.substring(0, 4));
                        int month4 = Integer.parseInt(stringVal2.substring(4, 6));
                        int day3 = Integer.parseInt(stringVal2.substring(6, 8));
                        return fastDateCreate(targetCalendar, year6, month4, day3);
                    case 19:
                    case 21:
                        int year7 = Integer.parseInt(stringVal2.substring(0, 4));
                        int month5 = Integer.parseInt(stringVal2.substring(5, 7));
                        int day4 = Integer.parseInt(stringVal2.substring(8, 10));
                        return fastDateCreate(targetCalendar, year7, month5, day4);
                }
            }
            if (this.fields[columnIndex - 1].getMysqlType() == 13) {
                if (stringVal2.length() == 2 || stringVal2.length() == 1) {
                    int year8 = Integer.parseInt(stringVal2);
                    if (year8 <= 69) {
                        year8 += 100;
                    }
                    year2 = year8 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                } else {
                    year2 = Integer.parseInt(stringVal2.substring(0, 4));
                }
                return fastDateCreate(targetCalendar, year2, 1, 1);
            }
            if (this.fields[columnIndex - 1].getMysqlType() == 11) {
                return fastDateCreate(targetCalendar, 1970, 1, 1);
            }
            if (stringVal2.length() < 10) {
                if (stringVal2.length() == 8) {
                    return fastDateCreate(targetCalendar, 1970, 1, 1);
                }
                throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{stringVal2, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (stringVal2.length() != 18) {
                year = Integer.parseInt(stringVal2.substring(0, 4));
                month = Integer.parseInt(stringVal2.substring(5, 7));
                day = Integer.parseInt(stringVal2.substring(8, 10));
            } else {
                StringTokenizer st = new StringTokenizer(stringVal2, List.DEFAULT_LIST_SYMBOL);
                year = Integer.parseInt(st.nextToken());
                month = Integer.parseInt(st.nextToken());
                day = Integer.parseInt(st.nextToken());
            }
            return fastDateCreate(targetCalendar, year, month, day);
        } catch (SQLException sqlEx) {
            throw sqlEx;
        } catch (Exception e) {
            SQLException sqlEx2 = SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Date", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            sqlEx2.initCause(e);
            throw sqlEx2;
        }
    }

    private TimeZone getDefaultTimeZone() {
        return this.useLegacyDatetimeCode ? this.connection.getDefaultTimeZone() : this.serverTimeZoneTz;
    }

    @Override // java.sql.ResultSet
    public double getDouble(int columnIndex) throws SQLException {
        if (!this.isBinaryEncoded) {
            return getDoubleInternal(columnIndex);
        }
        return getNativeDouble(columnIndex);
    }

    @Override // java.sql.ResultSet
    public double getDouble(String columnName) throws SQLException {
        return getDouble(findColumn(columnName));
    }

    private final double getDoubleFromString(String stringVal, int columnIndex) throws SQLException {
        return getDoubleInternal(stringVal, columnIndex);
    }

    protected double getDoubleInternal(int colIndex) throws SQLException {
        return getDoubleInternal(getString(colIndex), colIndex);
    }

    protected double getDoubleInternal(String stringVal, int colIndex) throws SQLException, NumberFormatException {
        if (stringVal == null) {
            return 0.0d;
        }
        try {
            if (stringVal.length() == 0) {
                return convertToZeroWithEmptyCheck();
            }
            double d = Double.parseDouble(stringVal);
            if (this.useStrictFloatingPoint) {
                if (d == 2.147483648E9d) {
                    d = 2.147483647E9d;
                } else if (d == 1.0000000036275E-15d) {
                    d = 1.0E-15d;
                } else if (d == 9.999999869911E14d) {
                    d = 9.99999999999999E14d;
                } else if (d == 1.4012984643248E-45d || d == 1.4013E-45d) {
                    d = 1.4E-45d;
                } else if (d == 3.4028234663853E37d) {
                    d = 3.4028235E37d;
                } else if (d == -2.14748E9d) {
                    d = -2.147483648E9d;
                } else if (d == 3.40282E37d) {
                    d = 3.4028235E37d;
                }
            }
            return d;
        } catch (NumberFormatException e) {
            if (this.fields[colIndex - 1].getMysqlType() == 16) {
                long valueAsLong = getNumericRepresentationOfSQLBitType(colIndex);
                return valueAsLong;
            }
            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_number", new Object[]{stringVal, Integer.valueOf(colIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.ResultSet
    public int getFetchDirection() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.fetchDirection;
        }
        return i;
    }

    @Override // java.sql.ResultSet
    public int getFetchSize() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.fetchSize;
        }
        return i;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public char getFirstCharOfQuery() {
        char c;
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                c = this.firstCharOfQuery;
            }
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // java.sql.ResultSet
    public float getFloat(int columnIndex) throws SQLException, NumberFormatException {
        if (!this.isBinaryEncoded) {
            String val = getString(columnIndex);
            return getFloatFromString(val, columnIndex);
        }
        return getNativeFloat(columnIndex);
    }

    @Override // java.sql.ResultSet
    public float getFloat(String columnName) throws SQLException {
        return getFloat(findColumn(columnName));
    }

    private final float getFloatFromString(String val, int columnIndex) throws SQLException, NumberFormatException {
        if (val != null) {
            try {
                if (val.length() == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                float f = Float.parseFloat(val);
                if (this.jdbcCompliantTruncationForReads && (f == Float.MIN_VALUE || f == Float.MAX_VALUE)) {
                    double valAsDouble = Double.parseDouble(val);
                    if (valAsDouble < 1.401298464324817E-45d - MIN_DIFF_PREC || valAsDouble > 3.4028234663852886E38d - MAX_DIFF_PREC) {
                        throwRangeException(String.valueOf(valAsDouble), columnIndex, 6);
                    }
                }
                return f;
            } catch (NumberFormatException e) {
                try {
                    Double valueAsDouble = new Double(val);
                    float valueAsFloat = valueAsDouble.floatValue();
                    if (this.jdbcCompliantTruncationForReads && ((this.jdbcCompliantTruncationForReads && valueAsFloat == Float.NEGATIVE_INFINITY) || valueAsFloat == Float.POSITIVE_INFINITY)) {
                        throwRangeException(valueAsDouble.toString(), columnIndex, 6);
                    }
                    return valueAsFloat;
                } catch (NumberFormatException e2) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getFloat()_-____200") + val + Messages.getString("ResultSet.___in_column__201") + columnIndex, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return 0.0f;
    }

    @Override // java.sql.ResultSet
    public int getInt(int columnIndex) throws SQLException, NumberFormatException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (!this.isBinaryEncoded) {
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
                return 0;
            }
            this.wasNullFlag = false;
            if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
                long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
                if (this.jdbcCompliantTruncationForReads && (valueAsLong < -2147483648L || valueAsLong > 2147483647L)) {
                    throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
                }
                return (int) valueAsLong;
            }
            if (this.useFastIntParsing) {
                if (this.thisRow.length(columnIndexMinusOne) == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
                if (!needsFullParse) {
                    try {
                        return getIntWithOverflowCheck(columnIndexMinusOne);
                    } catch (NumberFormatException e) {
                        try {
                            return parseIntAsDouble(columnIndex, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getEncoding(), this.connection));
                        } catch (NumberFormatException e2) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getEncoding(), this.connection) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                }
            }
            try {
                String val = getString(columnIndex);
                if (val == null) {
                    return 0;
                }
                if (val.length() == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
                    int intVal = Integer.parseInt(val);
                    checkForIntegerTruncation(columnIndexMinusOne, null, intVal);
                    return intVal;
                }
                int intVal2 = parseIntAsDouble(columnIndex, val);
                checkForIntegerTruncation(columnIndex, null, intVal2);
                return intVal2;
            } catch (NumberFormatException e3) {
                try {
                    return parseIntAsDouble(columnIndex, null);
                } catch (NumberFormatException e4) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____74") + ((String) null) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return getNativeInt(columnIndex);
    }

    @Override // java.sql.ResultSet
    public int getInt(String columnName) throws SQLException {
        return getInt(findColumn(columnName));
    }

    private final int getIntFromString(String val, int columnIndex) throws SQLException, NumberFormatException {
        if (val != null) {
            try {
                if (val.length() == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
                    String val2 = val.trim();
                    int valueAsInt = Integer.parseInt(val2);
                    if (this.jdbcCompliantTruncationForReads && (valueAsInt == Integer.MIN_VALUE || valueAsInt == Integer.MAX_VALUE)) {
                        long valueAsLong = Long.parseLong(val2);
                        if (valueAsLong < -2147483648L || valueAsLong > 2147483647L) {
                            throwRangeException(String.valueOf(valueAsLong), columnIndex, 4);
                        }
                    }
                    return valueAsInt;
                }
                double valueAsDouble = Double.parseDouble(val);
                if (this.jdbcCompliantTruncationForReads && (valueAsDouble < -2.147483648E9d || valueAsDouble > 2.147483647E9d)) {
                    throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
                }
                return (int) valueAsDouble;
            } catch (NumberFormatException e) {
                try {
                    double valueAsDouble2 = Double.parseDouble(val);
                    if (this.jdbcCompliantTruncationForReads && (valueAsDouble2 < -2.147483648E9d || valueAsDouble2 > 2.147483647E9d)) {
                        throwRangeException(String.valueOf(valueAsDouble2), columnIndex, 4);
                    }
                    return (int) valueAsDouble2;
                } catch (NumberFormatException e2) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getInt()_-____206") + val + Messages.getString("ResultSet.___in_column__207") + columnIndex, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return 0;
    }

    @Override // java.sql.ResultSet
    public long getLong(int columnIndex) throws SQLException {
        return getLong(columnIndex, true);
    }

    private long getLong(int columnIndex, boolean overflowCheck) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (!this.isBinaryEncoded) {
            int columnIndexMinusOne = columnIndex - 1;
            if (this.thisRow.isNull(columnIndexMinusOne)) {
                this.wasNullFlag = true;
                return 0L;
            }
            this.wasNullFlag = false;
            if (this.fields[columnIndexMinusOne].getMysqlType() == 16) {
                return getNumericRepresentationOfSQLBitType(columnIndex);
            }
            if (this.useFastIntParsing) {
                if (this.thisRow.length(columnIndexMinusOne) == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                boolean needsFullParse = this.thisRow.isFloatingPointNumber(columnIndexMinusOne);
                if (!needsFullParse) {
                    try {
                        return getLongWithOverflowCheck(columnIndexMinusOne, overflowCheck);
                    } catch (NumberFormatException e) {
                        try {
                            return parseLongAsDouble(columnIndexMinusOne, this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getEncoding(), this.connection));
                        } catch (NumberFormatException e2) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + this.thisRow.getString(columnIndexMinusOne, this.fields[columnIndexMinusOne].getEncoding(), this.connection) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                }
            }
            try {
                String val = getString(columnIndex);
                if (val == null) {
                    return 0L;
                }
                if (val.length() == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1) {
                    return parseLongWithOverflowCheck(columnIndexMinusOne, null, val, overflowCheck);
                }
                return parseLongAsDouble(columnIndexMinusOne, val);
            } catch (NumberFormatException e3) {
                try {
                    return parseLongAsDouble(columnIndexMinusOne, null);
                } catch (NumberFormatException e4) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____79") + ((String) null) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return getNativeLong(columnIndex, overflowCheck, true);
    }

    @Override // java.sql.ResultSet
    public long getLong(String columnName) throws SQLException {
        return getLong(findColumn(columnName));
    }

    private final long getLongFromString(String val, int columnIndexZeroBased) throws SQLException {
        if (val != null) {
            try {
                if (val.length() == 0) {
                    return convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1) {
                    return parseLongWithOverflowCheck(columnIndexZeroBased, null, val, true);
                }
                return parseLongAsDouble(columnIndexZeroBased, val);
            } catch (NumberFormatException e) {
                try {
                    return parseLongAsDouble(columnIndexZeroBased, val);
                } catch (NumberFormatException e2) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getLong()_-____211") + val + Messages.getString("ResultSet.___in_column__212") + (columnIndexZeroBased + 1), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return 0L;
    }

    @Override // java.sql.ResultSet
    public java.sql.ResultSetMetaData getMetaData() throws SQLException {
        checkClosed();
        return new ResultSetMetaData(this.fields, this.connection.getUseOldAliasMetadataBehavior(), this.connection.getYearIsDateType(), getExceptionInterceptor());
    }

    protected Array getNativeArray(int i) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    protected InputStream getNativeAsciiStream(int columnIndex) throws SQLException {
        checkRowPos();
        return getNativeBinaryStream(columnIndex);
    }

    protected BigDecimal getNativeBigDecimal(int columnIndex) throws SQLException {
        checkColumnBounds(columnIndex);
        int scale = this.fields[columnIndex - 1].getDecimals();
        return getNativeBigDecimal(columnIndex, scale);
    }

    protected BigDecimal getNativeBigDecimal(int columnIndex, int scale) throws SQLException {
        String stringVal;
        checkColumnBounds(columnIndex);
        Field f = this.fields[columnIndex - 1];
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (value == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        switch (f.getSQLType()) {
            case 2:
            case 3:
                stringVal = StringUtils.toAsciiString((byte[]) value);
                break;
            default:
                stringVal = getNativeString(columnIndex);
                break;
        }
        return getBigDecimalFromString(stringVal, columnIndex, scale);
    }

    protected InputStream getNativeBinaryStream(int columnIndex) throws SQLException {
        checkRowPos();
        int columnIndexMinusOne = columnIndex - 1;
        if (this.thisRow.isNull(columnIndexMinusOne)) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        switch (this.fields[columnIndexMinusOne].getSQLType()) {
            case -7:
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case 2004:
                return this.thisRow.getBinaryInputStream(columnIndexMinusOne);
            default:
                byte[] b = getNativeBytes(columnIndex, false);
                if (b != null) {
                    return new ByteArrayInputStream(b);
                }
                return null;
        }
    }

    protected java.sql.Blob getNativeBlob(int columnIndex) throws SQLException {
        byte[] dataAsBytes;
        checkRowPos();
        checkColumnBounds(columnIndex);
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (value == null) {
            this.wasNullFlag = true;
        } else {
            this.wasNullFlag = false;
        }
        if (this.wasNullFlag) {
            return null;
        }
        int mysqlType = this.fields[columnIndex - 1].getMysqlType();
        switch (mysqlType) {
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
                dataAsBytes = (byte[]) value;
                break;
            default:
                dataAsBytes = getNativeBytes(columnIndex, false);
                break;
        }
        if (!this.connection.getEmulateLocators()) {
            return new Blob(dataAsBytes, getExceptionInterceptor());
        }
        return new BlobFromLocator(this, columnIndex, getExceptionInterceptor());
    }

    public static boolean arraysEqual(byte[] left, byte[] right) {
        if (left == null) {
            return right == null;
        }
        if (right == null || left.length != right.length) {
            return false;
        }
        for (int i = 0; i < left.length; i++) {
            if (left[i] != right[i]) {
                return false;
            }
        }
        return true;
    }

    protected byte getNativeByte(int columnIndex) throws SQLException {
        return getNativeByte(columnIndex, true);
    }

    protected byte getNativeByte(int columnIndex, boolean overflowCheck) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (value == null) {
            this.wasNullFlag = true;
            return (byte) 0;
        }
        this.wasNullFlag = false;
        int columnIndex2 = columnIndex - 1;
        Field field = this.fields[columnIndex2];
        switch (field.getMysqlType()) {
            case 1:
                byte valueAsByte = ((byte[]) value)[0];
                if (!field.isUnsigned()) {
                    return valueAsByte;
                }
                short valueAsShort = valueAsByte >= 0 ? valueAsByte : (short) (valueAsByte + 256);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsShort > 127) {
                    throwRangeException(String.valueOf((int) valueAsShort), columnIndex2 + 1, -6);
                }
                return (byte) valueAsShort;
            case 2:
            case 13:
                short valueAsShort2 = getNativeShort(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsShort2 < -128 || valueAsShort2 > 127)) {
                    throwRangeException(String.valueOf((int) valueAsShort2), columnIndex2 + 1, -6);
                }
                return (byte) valueAsShort2;
            case 3:
            case 9:
                int valueAsInt = getNativeInt(columnIndex2 + 1, false);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsInt < -128 || valueAsInt > 127)) {
                    throwRangeException(String.valueOf(valueAsInt), columnIndex2 + 1, -6);
                }
                return (byte) valueAsInt;
            case 4:
                float valueAsFloat = getNativeFloat(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsFloat < -128.0f || valueAsFloat > 127.0f)) {
                    throwRangeException(String.valueOf(valueAsFloat), columnIndex2 + 1, -6);
                }
                return (byte) valueAsFloat;
            case 5:
                double valueAsDouble = getNativeDouble(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble < -128.0d || valueAsDouble > 127.0d)) {
                    throwRangeException(String.valueOf(valueAsDouble), columnIndex2 + 1, -6);
                }
                return (byte) valueAsDouble;
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getByte()", columnIndex2, this.thisRow.getColumnValue(columnIndex2 - 1), this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getByteFromString(getNativeString(columnIndex2 + 1), columnIndex2 + 1);
            case 8:
                long valueAsLong = getNativeLong(columnIndex2 + 1, false, true);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong < -128 || valueAsLong > 127)) {
                    throwRangeException(String.valueOf(valueAsLong), columnIndex2 + 1, -6);
                }
                return (byte) valueAsLong;
            case 16:
                long valueAsLong2 = getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong2 < -128 || valueAsLong2 > 127)) {
                    throwRangeException(String.valueOf(valueAsLong2), columnIndex2 + 1, -6);
                }
                return (byte) valueAsLong2;
        }
    }

    protected byte[] getNativeBytes(int columnIndex, boolean noConversion) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (value == null) {
            this.wasNullFlag = true;
        } else {
            this.wasNullFlag = false;
        }
        if (this.wasNullFlag) {
            return null;
        }
        Field field = this.fields[columnIndex - 1];
        int mysqlType = field.getMysqlType();
        if (noConversion) {
            mysqlType = 252;
        }
        switch (mysqlType) {
            case 15:
            case 253:
            case 254:
                if (value instanceof byte[]) {
                    return (byte[]) value;
                }
                break;
            case 16:
            case EscherProperties.GEOTEXT__NOMEASUREALONGPATH /* 249 */:
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
            case 251:
            case 252:
                return (byte[]) value;
        }
        int sqlType = field.getSQLType();
        if (sqlType == -3 || sqlType == -2) {
            return (byte[]) value;
        }
        return getBytesFromString(getNativeString(columnIndex));
    }

    protected Reader getNativeCharacterStream(int columnIndex) throws SQLException, NumberFormatException {
        int columnIndexMinusOne = columnIndex - 1;
        switch (this.fields[columnIndexMinusOne].getSQLType()) {
            case -1:
            case 1:
            case 12:
            case 2005:
                if (this.thisRow.isNull(columnIndexMinusOne)) {
                    this.wasNullFlag = true;
                    return null;
                }
                this.wasNullFlag = false;
                return this.thisRow.getReader(columnIndexMinusOne);
            default:
                String asString = getStringForClob(columnIndex);
                if (asString == null) {
                    return null;
                }
                return getCharacterStreamFromString(asString);
        }
    }

    protected java.sql.Clob getNativeClob(int columnIndex) throws SQLException, NumberFormatException {
        String stringVal = getStringForClob(columnIndex);
        if (stringVal == null) {
            return null;
        }
        return getClobFromString(stringVal);
    }

    private String getNativeConvertToString(int columnIndex, Field field) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            int sqlType = field.getSQLType();
            int mysqlType = field.getMysqlType();
            switch (sqlType) {
                case -7:
                    return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
                case -6:
                    byte tinyintVal = getNativeByte(columnIndex, false);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    if (!field.isUnsigned() || tinyintVal >= 0) {
                        return String.valueOf((int) tinyintVal);
                    }
                    short unsignedTinyVal = (short) (tinyintVal & 255);
                    return String.valueOf((int) unsignedTinyVal);
                case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                    if (!field.isUnsigned()) {
                        long longVal = getNativeLong(columnIndex, false, true);
                        if (this.wasNullFlag) {
                            return null;
                        }
                        return String.valueOf(longVal);
                    }
                    long longVal2 = getNativeLong(columnIndex, false, false);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    return String.valueOf(convertLongToUlong(longVal2));
                case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
                case -3:
                case -2:
                    if (!field.isBlob()) {
                        return extractStringFromNativeColumn(columnIndex, mysqlType);
                    }
                    if (!field.isBinary()) {
                        return extractStringFromNativeColumn(columnIndex, mysqlType);
                    }
                    byte[] data = getBytes(columnIndex);
                    Object obj = data;
                    if (this.connection.getAutoDeserialize() && data != null && data.length >= 2) {
                        if (data[0] == -84 && data[1] == -19) {
                            try {
                                ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
                                ObjectInputStream objIn = new ObjectInputStream(bytesIn);
                                obj = objIn.readObject();
                                objIn.close();
                                bytesIn.close();
                            } catch (IOException e) {
                                obj = data;
                            } catch (ClassNotFoundException cnfe) {
                                throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
                            }
                        }
                        return obj.toString();
                    }
                    return extractStringFromNativeColumn(columnIndex, mysqlType);
                case -1:
                case 1:
                case 12:
                    return extractStringFromNativeColumn(columnIndex, mysqlType);
                case 2:
                case 3:
                    String stringVal = StringUtils.toAsciiString(this.thisRow.getColumnValue(columnIndex - 1));
                    if (stringVal != null) {
                        this.wasNullFlag = false;
                        if (stringVal.length() == 0) {
                            BigDecimal val = new BigDecimal(0);
                            return val.toString();
                        }
                        try {
                            BigDecimal val2 = new BigDecimal(stringVal);
                            return val2.toString();
                        } catch (NumberFormatException e2) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                    this.wasNullFlag = true;
                    return null;
                case 4:
                    int intVal = getNativeInt(columnIndex, false);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    if (!field.isUnsigned() || intVal >= 0 || field.getMysqlType() == 9) {
                        return String.valueOf(intVal);
                    }
                    long longVal3 = intVal & 4294967295L;
                    return String.valueOf(longVal3);
                case 5:
                    int intVal2 = getNativeInt(columnIndex, false);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    if (!field.isUnsigned() || intVal2 >= 0) {
                        return String.valueOf(intVal2);
                    }
                    return String.valueOf(intVal2 & 65535);
                case 6:
                case 8:
                    double doubleVal = getNativeDouble(columnIndex);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    return String.valueOf(doubleVal);
                case 7:
                    float floatVal = getNativeFloat(columnIndex);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    return String.valueOf(floatVal);
                case 16:
                    boolean booleanVal = getBoolean(columnIndex);
                    if (this.wasNullFlag) {
                        return null;
                    }
                    return String.valueOf(booleanVal);
                case 91:
                    if (mysqlType == 13) {
                        short shortVal = getNativeShort(columnIndex);
                        if (!this.connection.getYearIsDateType()) {
                            if (this.wasNullFlag) {
                                return null;
                            }
                            return String.valueOf((int) shortVal);
                        }
                        if (field.getLength() == 2) {
                            if (shortVal <= 69) {
                                shortVal = (short) (shortVal + 100);
                            }
                            shortVal = (short) (shortVal + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP);
                        }
                        return fastDateCreate(null, shortVal, 1, 1).toString();
                    }
                    if (this.connection.getNoDatetimeStringSync()) {
                        byte[] asBytes = getNativeBytes(columnIndex, true);
                        if (asBytes == null) {
                            return null;
                        }
                        if (asBytes.length == 0) {
                            return "0000-00-00";
                        }
                        int year = (asBytes[0] & 255) | ((asBytes[1] & 255) << 8);
                        byte b = asBytes[2];
                        byte b2 = asBytes[3];
                        if (year == 0 && b == 0 && b2 == 0) {
                            return "0000-00-00";
                        }
                    }
                    Date dt = getNativeDate(columnIndex);
                    if (dt == null) {
                        return null;
                    }
                    return String.valueOf(dt);
                case 92:
                    Time tm = getNativeTime(columnIndex, null, this.connection.getDefaultTimeZone(), false);
                    if (tm == null) {
                        return null;
                    }
                    return String.valueOf(tm);
                case 93:
                    if (this.connection.getNoDatetimeStringSync()) {
                        byte[] asBytes2 = getNativeBytes(columnIndex, true);
                        if (asBytes2 == null) {
                            return null;
                        }
                        if (asBytes2.length == 0) {
                            return "0000-00-00 00:00:00";
                        }
                        int year2 = (asBytes2[0] & 255) | ((asBytes2[1] & 255) << 8);
                        byte b3 = asBytes2[2];
                        byte b4 = asBytes2[3];
                        if (year2 == 0 && b3 == 0 && b4 == 0) {
                            return "0000-00-00 00:00:00";
                        }
                    }
                    Timestamp tstamp = getNativeTimestamp(columnIndex, null, this.connection.getDefaultTimeZone(), false);
                    if (tstamp == null) {
                        return null;
                    }
                    String result = String.valueOf(tstamp);
                    if (!this.connection.getNoDatetimeStringSync()) {
                        return result;
                    }
                    if (result.endsWith(".0")) {
                        return result.substring(0, result.length() - 2);
                    }
                    return extractStringFromNativeColumn(columnIndex, mysqlType);
                default:
                    return extractStringFromNativeColumn(columnIndex, mysqlType);
            }
        }
    }

    protected Date getNativeDate(int columnIndex) throws SQLException {
        return getNativeDate(columnIndex, null);
    }

    protected Date getNativeDate(int columnIndex, Calendar cal) throws SQLException {
        Date dateToReturn;
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
        if (mysqlType == 10) {
            dateToReturn = this.thisRow.getNativeDate(columnIndexMinusOne, this.connection, this, cal);
        } else {
            TimeZone tz = cal != null ? cal.getTimeZone() : getDefaultTimeZone();
            boolean rollForward = (tz == null || tz.equals(getDefaultTimeZone())) ? false : true;
            dateToReturn = (Date) this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 91, mysqlType, tz, rollForward, this.connection, this);
        }
        if (dateToReturn == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        return dateToReturn;
    }

    Date getNativeDateViaParseConversion(int columnIndex) throws SQLException {
        if (this.useUsageAdvisor) {
            issueConversionViaParsingWarning("getDate()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[]{10});
        }
        String stringVal = getNativeString(columnIndex);
        return getDateFromString(stringVal, columnIndex, null);
    }

    protected double getNativeDouble(int columnIndex) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndex2 = columnIndex - 1;
        if (this.thisRow.isNull(columnIndex2)) {
            this.wasNullFlag = true;
            return 0.0d;
        }
        this.wasNullFlag = false;
        Field f = this.fields[columnIndex2];
        switch (f.getMysqlType()) {
            case 1:
                if (!f.isUnsigned()) {
                    return getNativeByte(columnIndex2 + 1);
                }
                return getNativeShort(columnIndex2 + 1);
            case 2:
            case 13:
                if (!f.isUnsigned()) {
                    return getNativeShort(columnIndex2 + 1);
                }
                return getNativeInt(columnIndex2 + 1);
            case 3:
            case 9:
                if (!f.isUnsigned()) {
                    return getNativeInt(columnIndex2 + 1);
                }
                return getNativeLong(columnIndex2 + 1);
            case 4:
                return getNativeFloat(columnIndex2 + 1);
            case 5:
                return this.thisRow.getNativeDouble(columnIndex2);
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                String stringVal = getNativeString(columnIndex2 + 1);
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getDouble()", columnIndex2, stringVal, this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getDoubleFromString(stringVal, columnIndex2 + 1);
            case 8:
                long valueAsLong = getNativeLong(columnIndex2 + 1);
                if (!f.isUnsigned()) {
                    return valueAsLong;
                }
                BigInteger asBigInt = convertLongToUlong(valueAsLong);
                return asBigInt.doubleValue();
            case 16:
                return getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
        }
    }

    protected float getNativeFloat(int columnIndex) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndex2 = columnIndex - 1;
        if (this.thisRow.isNull(columnIndex2)) {
            this.wasNullFlag = true;
            return 0.0f;
        }
        this.wasNullFlag = false;
        Field f = this.fields[columnIndex2];
        switch (f.getMysqlType()) {
            case 1:
                if (!f.isUnsigned()) {
                    return getNativeByte(columnIndex2 + 1);
                }
                return getNativeShort(columnIndex2 + 1);
            case 2:
            case 13:
                if (!f.isUnsigned()) {
                    return getNativeShort(columnIndex2 + 1);
                }
                return getNativeInt(columnIndex2 + 1);
            case 3:
            case 9:
                if (!f.isUnsigned()) {
                    return getNativeInt(columnIndex2 + 1);
                }
                return getNativeLong(columnIndex2 + 1);
            case 4:
                return this.thisRow.getNativeFloat(columnIndex2);
            case 5:
                Double valueAsDouble = new Double(getNativeDouble(columnIndex2 + 1));
                float valueAsFloat = valueAsDouble.floatValue();
                if ((this.jdbcCompliantTruncationForReads && valueAsFloat == Float.NEGATIVE_INFINITY) || valueAsFloat == Float.POSITIVE_INFINITY) {
                    throwRangeException(valueAsDouble.toString(), columnIndex2 + 1, 6);
                }
                return (float) getNativeDouble(columnIndex2 + 1);
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                String stringVal = getNativeString(columnIndex2 + 1);
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getFloat()", columnIndex2, stringVal, this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getFloatFromString(stringVal, columnIndex2 + 1);
            case 8:
                long valueAsLong = getNativeLong(columnIndex2 + 1);
                if (!f.isUnsigned()) {
                    return valueAsLong;
                }
                BigInteger asBigInt = convertLongToUlong(valueAsLong);
                return asBigInt.floatValue();
            case 16:
                return getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
        }
    }

    protected int getNativeInt(int columnIndex) throws SQLException {
        return getNativeInt(columnIndex, true);
    }

    protected int getNativeInt(int columnIndex, boolean overflowCheck) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndex2 = columnIndex - 1;
        if (this.thisRow.isNull(columnIndex2)) {
            this.wasNullFlag = true;
            return 0;
        }
        this.wasNullFlag = false;
        Field f = this.fields[columnIndex2];
        switch (f.getMysqlType()) {
            case 1:
                byte tinyintVal = getNativeByte(columnIndex2 + 1, false);
                if (!f.isUnsigned() || tinyintVal >= 0) {
                    return tinyintVal;
                }
                return tinyintVal + 256;
            case 2:
            case 13:
                short asShort = getNativeShort(columnIndex2 + 1, false);
                if (!f.isUnsigned() || asShort >= 0) {
                    return asShort;
                }
                return asShort + 65536;
            case 3:
            case 9:
                int valueAsInt = this.thisRow.getNativeInt(columnIndex2);
                if (!f.isUnsigned()) {
                    return valueAsInt;
                }
                long valueAsLong = valueAsInt >= 0 ? valueAsInt : valueAsInt + 4294967296L;
                if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsLong > 2147483647L) {
                    throwRangeException(String.valueOf(valueAsLong), columnIndex2 + 1, 4);
                }
                return (int) valueAsLong;
            case 4:
                double valueAsDouble = getNativeFloat(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble < -2.147483648E9d || valueAsDouble > 2.147483647E9d)) {
                    throwRangeException(String.valueOf(valueAsDouble), columnIndex2 + 1, 4);
                }
                return (int) valueAsDouble;
            case 5:
                double valueAsDouble2 = getNativeDouble(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble2 < -2.147483648E9d || valueAsDouble2 > 2.147483647E9d)) {
                    throwRangeException(String.valueOf(valueAsDouble2), columnIndex2 + 1, 4);
                }
                return (int) valueAsDouble2;
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                String stringVal = getNativeString(columnIndex2 + 1);
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getInt()", columnIndex2, stringVal, this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getIntFromString(stringVal, columnIndex2 + 1);
            case 8:
                long valueAsLong2 = getNativeLong(columnIndex2 + 1, false, true);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong2 < -2147483648L || valueAsLong2 > 2147483647L)) {
                    throwRangeException(String.valueOf(valueAsLong2), columnIndex2 + 1, 4);
                }
                return (int) valueAsLong2;
            case 16:
                long valueAsLong3 = getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong3 < -2147483648L || valueAsLong3 > 2147483647L)) {
                    throwRangeException(String.valueOf(valueAsLong3), columnIndex2 + 1, 4);
                }
                return (int) valueAsLong3;
        }
    }

    protected long getNativeLong(int columnIndex) throws SQLException {
        return getNativeLong(columnIndex, true, true);
    }

    protected long getNativeLong(int columnIndex, boolean overflowCheck, boolean expandUnsignedLong) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndex2 = columnIndex - 1;
        if (this.thisRow.isNull(columnIndex2)) {
            this.wasNullFlag = true;
            return 0L;
        }
        this.wasNullFlag = false;
        Field f = this.fields[columnIndex2];
        switch (f.getMysqlType()) {
            case 1:
                if (!f.isUnsigned()) {
                    return getNativeByte(columnIndex2 + 1);
                }
                return getNativeInt(columnIndex2 + 1);
            case 2:
                if (!f.isUnsigned()) {
                    return getNativeShort(columnIndex2 + 1);
                }
                return getNativeInt(columnIndex2 + 1, false);
            case 3:
            case 9:
                int asInt = getNativeInt(columnIndex2 + 1, false);
                if (!f.isUnsigned() || asInt >= 0) {
                    return asInt;
                }
                return asInt + 4294967296L;
            case 4:
                double valueAsDouble = getNativeFloat(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble < -9.223372036854776E18d || valueAsDouble > 9.223372036854776E18d)) {
                    throwRangeException(String.valueOf(valueAsDouble), columnIndex2 + 1, -5);
                }
                return (long) valueAsDouble;
            case 5:
                double valueAsDouble2 = getNativeDouble(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble2 < -9.223372036854776E18d || valueAsDouble2 > 9.223372036854776E18d)) {
                    throwRangeException(String.valueOf(valueAsDouble2), columnIndex2 + 1, -5);
                }
                return (long) valueAsDouble2;
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                String stringVal = getNativeString(columnIndex2 + 1);
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getLong()", columnIndex2, stringVal, this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getLongFromString(stringVal, columnIndex2 + 1);
            case 8:
                long valueAsLong = this.thisRow.getNativeLong(columnIndex2);
                if (!f.isUnsigned() || !expandUnsignedLong) {
                    return valueAsLong;
                }
                BigInteger asBigInt = convertLongToUlong(valueAsLong);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (asBigInt.compareTo(new BigInteger(String.valueOf(Long.MAX_VALUE))) > 0 || asBigInt.compareTo(new BigInteger(String.valueOf(Long.MIN_VALUE))) < 0)) {
                    throwRangeException(asBigInt.toString(), columnIndex2 + 1, -5);
                }
                return getLongFromString(asBigInt.toString(), columnIndex2);
            case 13:
                return getNativeShort(columnIndex2 + 1);
            case 16:
                return getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
        }
    }

    protected Ref getNativeRef(int i) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    protected short getNativeShort(int columnIndex) throws SQLException {
        return getNativeShort(columnIndex, true);
    }

    protected short getNativeShort(int columnIndex, boolean overflowCheck) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndex2 = columnIndex - 1;
        if (this.thisRow.isNull(columnIndex2)) {
            this.wasNullFlag = true;
            return (short) 0;
        }
        this.wasNullFlag = false;
        Field f = this.fields[columnIndex2];
        switch (f.getMysqlType()) {
            case 1:
                byte tinyintVal = getNativeByte(columnIndex2 + 1, false);
                if (!f.isUnsigned() || tinyintVal >= 0) {
                    return tinyintVal;
                }
                return (short) (tinyintVal + 256);
            case 2:
            case 13:
                short asShort = this.thisRow.getNativeShort(columnIndex2);
                if (!f.isUnsigned()) {
                    return asShort;
                }
                int valueAsInt = asShort & 65535;
                if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsInt > 32767) {
                    throwRangeException(String.valueOf(valueAsInt), columnIndex2 + 1, 5);
                }
                return (short) valueAsInt;
            case 3:
            case 9:
                if (!f.isUnsigned()) {
                    int valueAsInt2 = getNativeInt(columnIndex2 + 1, false);
                    if ((overflowCheck && this.jdbcCompliantTruncationForReads && valueAsInt2 > 32767) || valueAsInt2 < -32768) {
                        throwRangeException(String.valueOf(valueAsInt2), columnIndex2 + 1, 5);
                    }
                    return (short) valueAsInt2;
                }
                long valueAsLong = getNativeLong(columnIndex2 + 1, false, true);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && valueAsLong > 32767) {
                    throwRangeException(String.valueOf(valueAsLong), columnIndex2 + 1, 5);
                }
                return (short) valueAsLong;
            case 4:
                float valueAsFloat = getNativeFloat(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsFloat < -32768.0f || valueAsFloat > 32767.0f)) {
                    throwRangeException(String.valueOf(valueAsFloat), columnIndex2 + 1, 5);
                }
                return (short) valueAsFloat;
            case 5:
                double valueAsDouble = getNativeDouble(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsDouble < -32768.0d || valueAsDouble > 32767.0d)) {
                    throwRangeException(String.valueOf(valueAsDouble), columnIndex2 + 1, 5);
                }
                return (short) valueAsDouble;
            case 6:
            case 7:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            default:
                String stringVal = getNativeString(columnIndex2 + 1);
                if (this.useUsageAdvisor) {
                    issueConversionViaParsingWarning("getShort()", columnIndex2, stringVal, this.fields[columnIndex2], new int[]{5, 1, 2, 3, 8, 4});
                }
                return getShortFromString(stringVal, columnIndex2 + 1);
            case 8:
                long valueAsLong2 = getNativeLong(columnIndex2 + 1, false, false);
                if (!f.isUnsigned()) {
                    if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong2 < -32768 || valueAsLong2 > 32767)) {
                        throwRangeException(String.valueOf(valueAsLong2), columnIndex2 + 1, 5);
                    }
                    return (short) valueAsLong2;
                }
                BigInteger asBigInt = convertLongToUlong(valueAsLong2);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (asBigInt.compareTo(new BigInteger(String.valueOf(Font.COLOR_NORMAL))) > 0 || asBigInt.compareTo(new BigInteger(String.valueOf(-32768))) < 0)) {
                    throwRangeException(asBigInt.toString(), columnIndex2 + 1, 5);
                }
                return (short) getIntFromString(asBigInt.toString(), columnIndex2 + 1);
            case 16:
                long valueAsLong3 = getNumericRepresentationOfSQLBitType(columnIndex2 + 1);
                if (overflowCheck && this.jdbcCompliantTruncationForReads && (valueAsLong3 < -32768 || valueAsLong3 > 32767)) {
                    throwRangeException(String.valueOf(valueAsLong3), columnIndex2 + 1, 5);
                }
                return (short) valueAsLong3;
        }
    }

    protected String getNativeString(int columnIndex) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (this.fields == null) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_133"), SQLError.SQL_STATE_INVALID_COLUMN_NUMBER, getExceptionInterceptor());
        }
        if (this.thisRow.isNull(columnIndex - 1)) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        Field field = this.fields[columnIndex - 1];
        String stringVal = getNativeConvertToString(columnIndex, field);
        int mysqlType = field.getMysqlType();
        if (mysqlType != 7 && mysqlType != 10 && field.isZeroFill() && stringVal != null) {
            int origLength = stringVal.length();
            StringBuilder zeroFillBuf = new StringBuilder(origLength);
            long numZeros = field.getLength() - origLength;
            long j = 0;
            while (true) {
                long i = j;
                if (i >= numZeros) {
                    break;
                }
                zeroFillBuf.append('0');
                j = i + 1;
            }
            zeroFillBuf.append(stringVal);
            stringVal = zeroFillBuf.toString();
        }
        return stringVal;
    }

    private Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        Time timeVal;
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
        if (mysqlType == 11) {
            timeVal = this.thisRow.getNativeTime(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
        } else {
            timeVal = (Time) this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 92, mysqlType, tz, rollForward, this.connection, this);
        }
        if (timeVal == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        return timeVal;
    }

    Time getNativeTimeViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        if (this.useUsageAdvisor) {
            issueConversionViaParsingWarning("getTime()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[]{11});
        }
        String strTime = getNativeString(columnIndex);
        return getTimeFromString(strTime, targetCalendar, columnIndex, tz, rollForward);
    }

    private Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        Timestamp tsVal;
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        int mysqlType = this.fields[columnIndexMinusOne].getMysqlType();
        switch (mysqlType) {
            case 7:
            case 12:
                tsVal = this.thisRow.getNativeTimestamp(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
                break;
            default:
                tsVal = (Timestamp) this.thisRow.getNativeDateTimeValue(columnIndexMinusOne, null, 93, mysqlType, tz, rollForward, this.connection, this);
                break;
        }
        if (tsVal == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        return tsVal;
    }

    Timestamp getNativeTimestampViaParseConversion(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
        if (this.useUsageAdvisor) {
            issueConversionViaParsingWarning("getTimestamp()", columnIndex, this.thisRow.getColumnValue(columnIndex - 1), this.fields[columnIndex - 1], new int[]{7, 12});
        }
        String strTimestamp = getNativeString(columnIndex);
        return getTimestampFromString(columnIndex, targetCalendar, strTimestamp, tz, rollForward);
    }

    protected InputStream getNativeUnicodeStream(int columnIndex) throws SQLException {
        checkRowPos();
        return getBinaryStream(columnIndex);
    }

    protected URL getNativeURL(int colIndex) throws SQLException, NumberFormatException {
        String val = getString(colIndex);
        if (val == null) {
            return null;
        }
        try {
            return new URL(val);
        } catch (MalformedURLException e) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____141") + val + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public synchronized ResultSetInternalMethods getNextResultSet() {
        return this.nextResultSet;
    }

    @Override // java.sql.ResultSet
    public Object getObject(int columnIndex) throws SQLException, NumberFormatException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        if (this.thisRow.isNull(columnIndexMinusOne)) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        Field field = this.fields[columnIndexMinusOne];
        switch (field.getSQLType()) {
            case -7:
                if (field.getMysqlType() == 16 && !field.isSingleBit()) {
                    return getObjectDeserializingIfNeeded(columnIndex);
                }
                return Boolean.valueOf(getBoolean(columnIndex));
            case -6:
                if (!field.isUnsigned()) {
                    return Integer.valueOf(getByte(columnIndex));
                }
                return Integer.valueOf(getInt(columnIndex));
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                if (!field.isUnsigned()) {
                    return Long.valueOf(getLong(columnIndex));
                }
                String stringVal = getString(columnIndex);
                if (stringVal == null) {
                    return null;
                }
                try {
                    return new BigInteger(stringVal);
                } catch (NumberFormatException e) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigInteger", new Object[]{Integer.valueOf(columnIndex), stringVal}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
                if (field.getMysqlType() == 255) {
                    return getBytes(columnIndex);
                }
                return getObjectDeserializingIfNeeded(columnIndex);
            case -1:
                if (!field.isOpaqueBinary()) {
                    return getStringForClob(columnIndex);
                }
                return getBytes(columnIndex);
            case 1:
            case 12:
                if (!field.isOpaqueBinary()) {
                    return getString(columnIndex);
                }
                return getBytes(columnIndex);
            case 2:
            case 3:
                String stringVal2 = getString(columnIndex);
                if (stringVal2 != null) {
                    if (stringVal2.length() == 0) {
                        BigDecimal val = new BigDecimal(0);
                        return val;
                    }
                    try {
                        BigDecimal val2 = new BigDecimal(stringVal2);
                        return val2;
                    } catch (NumberFormatException e2) {
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal2, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                }
                return null;
            case 4:
                if (!field.isUnsigned() || field.getMysqlType() == 9) {
                    return Integer.valueOf(getInt(columnIndex));
                }
                return Long.valueOf(getLong(columnIndex));
            case 5:
                return Integer.valueOf(getInt(columnIndex));
            case 6:
            case 8:
                return new Double(getDouble(columnIndex));
            case 7:
                return new Float(getFloat(columnIndex));
            case 16:
                return Boolean.valueOf(getBoolean(columnIndex));
            case 91:
                if (field.getMysqlType() == 13 && !this.connection.getYearIsDateType()) {
                    return Short.valueOf(getShort(columnIndex));
                }
                return getDate(columnIndex);
            case 92:
                return getTime(columnIndex);
            case 93:
                return getTimestamp(columnIndex);
            default:
                return getString(columnIndex);
        }
    }

    private Object getObjectDeserializingIfNeeded(int columnIndex) throws SQLException, ClassNotFoundException, IOException {
        Field field = this.fields[columnIndex - 1];
        if (field.isBinary() || field.isBlob()) {
            byte[] data = getBytes(columnIndex);
            if (this.connection.getAutoDeserialize()) {
                Object obj = data;
                if (data != null && data.length >= 2) {
                    if (data[0] == -84 && data[1] == -19) {
                        try {
                            ByteArrayInputStream bytesIn = new ByteArrayInputStream(data);
                            ObjectInputStream objIn = new ObjectInputStream(bytesIn);
                            obj = objIn.readObject();
                            objIn.close();
                            bytesIn.close();
                        } catch (IOException e) {
                            obj = data;
                        } catch (ClassNotFoundException cnfe) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Class_not_found___91") + cnfe.toString() + Messages.getString("ResultSet._while_reading_serialized_object_92"), getExceptionInterceptor());
                        }
                    } else {
                        return getString(columnIndex);
                    }
                }
                return obj;
            }
            return data;
        }
        return getBytes(columnIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T getObject(int i, Class<T> cls) throws SQLException {
        if (cls == null) {
            throw SQLError.createSQLException("Type parameter can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        if (cls.equals(String.class)) {
            return (T) getString(i);
        }
        if (cls.equals(BigDecimal.class)) {
            return (T) getBigDecimal(i);
        }
        if (cls.equals(Boolean.class) || cls.equals(Boolean.TYPE)) {
            return (T) Boolean.valueOf(getBoolean(i));
        }
        if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)) {
            return (T) Integer.valueOf(getInt(i));
        }
        if (cls.equals(Long.class) || cls.equals(Long.TYPE)) {
            return (T) Long.valueOf(getLong(i));
        }
        if (cls.equals(Float.class) || cls.equals(Float.TYPE)) {
            return (T) Float.valueOf(getFloat(i));
        }
        if (cls.equals(Double.class) || cls.equals(Double.TYPE)) {
            return (T) Double.valueOf(getDouble(i));
        }
        if (cls.equals(byte[].class)) {
            return (T) getBytes(i);
        }
        if (cls.equals(Date.class)) {
            return (T) getDate(i);
        }
        if (cls.equals(Time.class)) {
            return (T) getTime(i);
        }
        if (cls.equals(Timestamp.class)) {
            return (T) getTimestamp(i);
        }
        if (cls.equals(Clob.class)) {
            return (T) getClob(i);
        }
        if (cls.equals(Blob.class)) {
            return (T) getBlob(i);
        }
        if (cls.equals(Array.class)) {
            return (T) getArray(i);
        }
        if (cls.equals(Ref.class)) {
            return (T) getRef(i);
        }
        if (cls.equals(URL.class)) {
            return (T) getURL(i);
        }
        if (this.connection.getAutoDeserialize()) {
            try {
                return cls.cast(getObject(i));
            } catch (ClassCastException e) {
                SQLException sQLExceptionCreateSQLException = SQLError.createSQLException("Conversion not supported for type " + cls.getName(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                sQLExceptionCreateSQLException.initCause(e);
                throw sQLExceptionCreateSQLException;
            }
        }
        throw SQLError.createSQLException("Conversion not supported for type " + cls.getName(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
    }

    public <T> T getObject(String str, Class<T> cls) throws SQLException {
        return (T) getObject(findColumn(str), cls);
    }

    @Override // java.sql.ResultSet
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return getObject(i);
    }

    @Override // java.sql.ResultSet
    public Object getObject(String columnName) throws SQLException {
        return getObject(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        return getObject(findColumn(colName), map);
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public Object getObjectStoredProc(int columnIndex, int desiredSqlType) throws SQLException, NumberFormatException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (value == null) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        Field field = this.fields[columnIndex - 1];
        switch (desiredSqlType) {
            case -7:
            case 16:
                return Boolean.valueOf(getBoolean(columnIndex));
            case -6:
                return Integer.valueOf(getInt(columnIndex));
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                if (field.isUnsigned()) {
                    return getBigDecimal(columnIndex);
                }
                return Long.valueOf(getLong(columnIndex));
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
                return getBytes(columnIndex);
            case -1:
                return getStringForClob(columnIndex);
            case 1:
            case 12:
                return getString(columnIndex);
            case 2:
            case 3:
                String stringVal = getString(columnIndex);
                if (stringVal != null) {
                    if (stringVal.length() == 0) {
                        BigDecimal val = new BigDecimal(0);
                        return val;
                    }
                    try {
                        BigDecimal val2 = new BigDecimal(stringVal);
                        return val2;
                    } catch (NumberFormatException e) {
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_BigDecimal", new Object[]{stringVal, Integer.valueOf(columnIndex)}), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                }
                return null;
            case 4:
                if (!field.isUnsigned() || field.getMysqlType() == 9) {
                    return Integer.valueOf(getInt(columnIndex));
                }
                return Long.valueOf(getLong(columnIndex));
            case 5:
                return Integer.valueOf(getInt(columnIndex));
            case 6:
                if (!this.connection.getRunningCTS13()) {
                    return new Double(getFloat(columnIndex));
                }
                return new Float(getFloat(columnIndex));
            case 7:
                return new Float(getFloat(columnIndex));
            case 8:
                return new Double(getDouble(columnIndex));
            case 91:
                if (field.getMysqlType() == 13 && !this.connection.getYearIsDateType()) {
                    return Short.valueOf(getShort(columnIndex));
                }
                return getDate(columnIndex);
            case 92:
                return getTime(columnIndex);
            case 93:
                return getTimestamp(columnIndex);
            default:
                return getString(columnIndex);
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public Object getObjectStoredProc(int i, Map<Object, Object> map, int desiredSqlType) throws SQLException {
        return getObjectStoredProc(i, desiredSqlType);
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public Object getObjectStoredProc(String columnName, int desiredSqlType) throws SQLException {
        return getObjectStoredProc(findColumn(columnName), desiredSqlType);
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public Object getObjectStoredProc(String colName, Map<Object, Object> map, int desiredSqlType) throws SQLException {
        return getObjectStoredProc(findColumn(colName), map, desiredSqlType);
    }

    @Override // java.sql.ResultSet
    public Ref getRef(int i) throws SQLException {
        checkColumnBounds(i);
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public Ref getRef(String colName) throws SQLException {
        return getRef(findColumn(colName));
    }

    @Override // java.sql.ResultSet
    public int getRow() throws SQLException {
        int row;
        checkClosed();
        int currentRowNumber = this.rowData.getCurrentRowNumber();
        if (!this.rowData.isDynamic()) {
            if (currentRowNumber < 0 || this.rowData.isAfterLast() || this.rowData.isEmpty()) {
                row = 0;
            } else {
                row = currentRowNumber + 1;
            }
        } else {
            row = currentRowNumber + 1;
        }
        return row;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public String getServerInfo() {
        String str;
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                str = this.serverInfo;
            }
            return str;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private long getNumericRepresentationOfSQLBitType(int columnIndex) throws SQLException {
        Object value = this.thisRow.getColumnValue(columnIndex - 1);
        if (this.fields[columnIndex - 1].isSingleBit() || ((byte[]) value).length == 1) {
            return ((byte[]) value)[0];
        }
        byte[] asBytes = (byte[]) value;
        int shift = 0;
        long[] steps = new long[asBytes.length];
        for (int i = asBytes.length - 1; i >= 0; i--) {
            steps[i] = (asBytes[i] & 255) << shift;
            shift += 8;
        }
        long valueAsLong = 0;
        for (int i2 = 0; i2 < asBytes.length; i2++) {
            valueAsLong |= steps[i2];
        }
        return valueAsLong;
    }

    @Override // java.sql.ResultSet
    public short getShort(int columnIndex) throws SQLException {
        checkRowPos();
        checkColumnBounds(columnIndex);
        if (!this.isBinaryEncoded) {
            if (this.thisRow.isNull(columnIndex - 1)) {
                this.wasNullFlag = true;
                return (short) 0;
            }
            this.wasNullFlag = false;
            if (this.fields[columnIndex - 1].getMysqlType() == 16) {
                long valueAsLong = getNumericRepresentationOfSQLBitType(columnIndex);
                if (this.jdbcCompliantTruncationForReads && (valueAsLong < -32768 || valueAsLong > 32767)) {
                    throwRangeException(String.valueOf(valueAsLong), columnIndex, 5);
                }
                return (short) valueAsLong;
            }
            if (this.useFastIntParsing) {
                byte[] shortAsBytes = this.thisRow.getColumnValue(columnIndex - 1);
                if (shortAsBytes.length == 0) {
                    return (short) convertToZeroWithEmptyCheck();
                }
                boolean needsFullParse = false;
                for (int i = 0; i < shortAsBytes.length; i++) {
                    if (((char) shortAsBytes[i]) == 'e' || ((char) shortAsBytes[i]) == 'E') {
                        needsFullParse = true;
                        break;
                    }
                }
                if (!needsFullParse) {
                    try {
                        return parseShortWithOverflowCheck(columnIndex, shortAsBytes, null);
                    } catch (NumberFormatException e) {
                        try {
                            return parseShortAsDouble(columnIndex, StringUtils.toString(shortAsBytes));
                        } catch (NumberFormatException e2) {
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + StringUtils.toString(shortAsBytes) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        }
                    }
                }
            }
            try {
                String val = getString(columnIndex);
                if (val == null) {
                    return (short) 0;
                }
                if (val.length() == 0) {
                    return (short) convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
                    return parseShortWithOverflowCheck(columnIndex, null, val);
                }
                return parseShortAsDouble(columnIndex, val);
            } catch (NumberFormatException e3) {
                try {
                    return parseShortAsDouble(columnIndex, null);
                } catch (NumberFormatException e4) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____96") + ((String) null) + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return getNativeShort(columnIndex);
    }

    @Override // java.sql.ResultSet
    public short getShort(String columnName) throws SQLException {
        return getShort(findColumn(columnName));
    }

    private final short getShortFromString(String val, int columnIndex) throws SQLException {
        if (val != null) {
            try {
                if (val.length() == 0) {
                    return (short) convertToZeroWithEmptyCheck();
                }
                if (val.indexOf("e") == -1 && val.indexOf("E") == -1 && val.indexOf(".") == -1) {
                    return parseShortWithOverflowCheck(columnIndex, null, val);
                }
                return parseShortAsDouble(columnIndex, val);
            } catch (NumberFormatException e) {
                try {
                    return parseShortAsDouble(columnIndex, val);
                } catch (NumberFormatException e2) {
                    throw SQLError.createSQLException(Messages.getString("ResultSet.Invalid_value_for_getShort()_-____217") + val + Messages.getString("ResultSet.___in_column__218") + columnIndex, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
            }
        }
        return (short) 0;
    }

    @Override // java.sql.ResultSet
    public java.sql.Statement getStatement() throws SQLException {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                if (this.wrapperStatement != null) {
                    return this.wrapperStatement;
                }
                return this.owningStatement;
            }
        } catch (SQLException e) {
            if (!this.retainOwningStatement) {
                throw SQLError.createSQLException("Operation not allowed on closed ResultSet. Statements can be retained over result set closure by setting the connection property \"retainStatementAfterResultSetClose\" to \"true\".", SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
            if (this.wrapperStatement != null) {
                return this.wrapperStatement;
            }
            return this.owningStatement;
        }
    }

    @Override // java.sql.ResultSet
    public String getString(int columnIndex) throws SQLException, NumberFormatException {
        int fieldLength;
        int currentLength;
        String stringVal = getStringInternal(columnIndex, true);
        if (this.padCharsWithSpace && stringVal != null) {
            Field f = this.fields[columnIndex - 1];
            if (f.getMysqlType() == 254 && (currentLength = stringVal.length()) < (fieldLength = ((int) f.getLength()) / f.getMaxBytesPerCharacter())) {
                StringBuilder paddedBuf = new StringBuilder(fieldLength);
                paddedBuf.append(stringVal);
                int difference = fieldLength - currentLength;
                paddedBuf.append(EMPTY_SPACE, 0, difference);
                stringVal = paddedBuf.toString();
            }
        }
        return stringVal;
    }

    @Override // java.sql.ResultSet
    public String getString(String columnName) throws SQLException {
        return getString(findColumn(columnName));
    }

    private String getStringForClob(int columnIndex) throws SQLException, NumberFormatException {
        byte[] asBytes;
        String asString = null;
        String forcedEncoding = this.connection.getClobCharacterEncoding();
        if (forcedEncoding == null) {
            if (!this.isBinaryEncoded) {
                asString = getString(columnIndex);
            } else {
                asString = getNativeString(columnIndex);
            }
        } else {
            try {
                if (!this.isBinaryEncoded) {
                    asBytes = getBytes(columnIndex);
                } else {
                    asBytes = getNativeBytes(columnIndex, true);
                }
                if (asBytes != null) {
                    asString = StringUtils.toString(asBytes, forcedEncoding);
                }
            } catch (UnsupportedEncodingException e) {
                throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
        }
        return asString;
    }

    protected String getStringInternal(int columnIndex, boolean checkDateTypes) throws SQLException, NumberFormatException {
        if (!this.isBinaryEncoded) {
            checkRowPos();
            checkColumnBounds(columnIndex);
            if (this.fields == null) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Query_generated_no_fields_for_ResultSet_99"), SQLError.SQL_STATE_INVALID_COLUMN_NUMBER, getExceptionInterceptor());
            }
            int internalColumnIndex = columnIndex - 1;
            if (this.thisRow.isNull(internalColumnIndex)) {
                this.wasNullFlag = true;
                return null;
            }
            this.wasNullFlag = false;
            Field metadata = this.fields[internalColumnIndex];
            if (metadata.getMysqlType() == 16) {
                if (metadata.isSingleBit()) {
                    byte[] value = this.thisRow.getColumnValue(internalColumnIndex);
                    if (value.length == 0) {
                        return String.valueOf(convertToZeroWithEmptyCheck());
                    }
                    return String.valueOf((int) value[0]);
                }
                return String.valueOf(getNumericRepresentationOfSQLBitType(columnIndex));
            }
            String encoding = metadata.getCollationIndex() == 63 ? this.connection.getEncoding() : metadata.getEncoding();
            String stringVal = this.thisRow.getString(internalColumnIndex, encoding, this.connection);
            if (metadata.getMysqlType() == 13) {
                if (!this.connection.getYearIsDateType()) {
                    return stringVal;
                }
                Date dt = getDateFromString(stringVal, columnIndex, null);
                if (dt == null) {
                    this.wasNullFlag = true;
                    return null;
                }
                this.wasNullFlag = false;
                return dt.toString();
            }
            if (checkDateTypes && !this.connection.getNoDatetimeStringSync()) {
                switch (metadata.getSQLType()) {
                    case 91:
                        Date dt2 = getDateFromString(stringVal, columnIndex, null);
                        if (dt2 == null) {
                            this.wasNullFlag = true;
                            return null;
                        }
                        this.wasNullFlag = false;
                        return dt2.toString();
                    case 92:
                        Time tm = getTimeFromString(stringVal, null, columnIndex, getDefaultTimeZone(), false);
                        if (tm == null) {
                            this.wasNullFlag = true;
                            return null;
                        }
                        this.wasNullFlag = false;
                        return tm.toString();
                    case 93:
                        Timestamp ts = getTimestampFromString(columnIndex, null, stringVal, getDefaultTimeZone(), false);
                        if (ts == null) {
                            this.wasNullFlag = true;
                            return null;
                        }
                        this.wasNullFlag = false;
                        return ts.toString();
                }
            }
            return stringVal;
        }
        return getNativeString(columnIndex);
    }

    @Override // java.sql.ResultSet
    public Time getTime(int columnIndex) throws SQLException {
        return getTimeInternal(columnIndex, null, getDefaultTimeZone(), false);
    }

    @Override // java.sql.ResultSet
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return getTimeInternal(columnIndex, cal, cal != null ? cal.getTimeZone() : getDefaultTimeZone(), true);
    }

    @Override // java.sql.ResultSet
    public Time getTime(String columnName) throws SQLException {
        return getTime(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return getTime(findColumn(columnName), cal);
    }

    private Time getTimeFromString(String timeAsString, Calendar targetCalendar, int columnIndex, TimeZone tz, boolean rollForward) throws SQLException {
        int hr;
        int min;
        int sec;
        synchronized (checkClosed().getConnectionMutex()) {
            try {
                if (timeAsString == null) {
                    this.wasNullFlag = true;
                    return null;
                }
                String timeAsString2 = timeAsString.trim();
                int dec = timeAsString2.indexOf(".");
                if (dec > -1) {
                    timeAsString2 = timeAsString2.substring(0, dec);
                }
                if (timeAsString2.equals("0") || timeAsString2.equals("0000-00-00") || timeAsString2.equals("0000-00-00 00:00:00") || timeAsString2.equals("00000000000000")) {
                    if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                        this.wasNullFlag = true;
                        return null;
                    }
                    if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(this.connection.getZeroDateTimeBehavior())) {
                        throw SQLError.createSQLException("Value '" + timeAsString2 + "' can not be represented as java.sql.Time", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                    return fastTimeCreate(targetCalendar, 0, 0, 0);
                }
                this.wasNullFlag = false;
                Field timeColField = this.fields[columnIndex - 1];
                if (timeColField.getMysqlType() == 7) {
                    int length = timeAsString2.length();
                    switch (length) {
                        case 10:
                            hr = Integer.parseInt(timeAsString2.substring(6, 8));
                            min = Integer.parseInt(timeAsString2.substring(8, 10));
                            sec = 0;
                            break;
                        case 11:
                        case 13:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        default:
                            throw SQLError.createSQLException(Messages.getString("ResultSet.Timestamp_too_small_to_convert_to_Time_value_in_column__257") + columnIndex + "(" + this.fields[columnIndex - 1] + ").", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                        case 12:
                        case 14:
                            hr = Integer.parseInt(timeAsString2.substring(length - 6, length - 4));
                            min = Integer.parseInt(timeAsString2.substring(length - 4, length - 2));
                            sec = Integer.parseInt(timeAsString2.substring(length - 2, length));
                            break;
                        case 19:
                            hr = Integer.parseInt(timeAsString2.substring(length - 8, length - 6));
                            min = Integer.parseInt(timeAsString2.substring(length - 5, length - 3));
                            sec = Integer.parseInt(timeAsString2.substring(length - 2, length));
                            break;
                    }
                    SQLWarning precisionLost = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_TIMESTAMP_to_Time_with_getTime()_on_column__261") + columnIndex + "(" + this.fields[columnIndex - 1] + ").");
                    if (this.warningChain == null) {
                        this.warningChain = precisionLost;
                    } else {
                        this.warningChain.setNextWarning(precisionLost);
                    }
                } else if (timeColField.getMysqlType() == 12) {
                    hr = Integer.parseInt(timeAsString2.substring(11, 13));
                    min = Integer.parseInt(timeAsString2.substring(14, 16));
                    sec = Integer.parseInt(timeAsString2.substring(17, 19));
                    SQLWarning precisionLost2 = new SQLWarning(Messages.getString("ResultSet.Precision_lost_converting_DATETIME_to_Time_with_getTime()_on_column__264") + columnIndex + "(" + this.fields[columnIndex - 1] + ").");
                    if (this.warningChain == null) {
                        this.warningChain = precisionLost2;
                    } else {
                        this.warningChain.setNextWarning(precisionLost2);
                    }
                } else {
                    if (timeColField.getMysqlType() == 10) {
                        return fastTimeCreate(targetCalendar, 0, 0, 0);
                    }
                    if (timeAsString2.length() != 5 && timeAsString2.length() != 8) {
                        throw SQLError.createSQLException(Messages.getString("ResultSet.Bad_format_for_Time____267") + timeAsString2 + Messages.getString("ResultSet.___in_column__268") + columnIndex, SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                    }
                    hr = Integer.parseInt(timeAsString2.substring(0, 2));
                    min = Integer.parseInt(timeAsString2.substring(3, 5));
                    sec = timeAsString2.length() == 5 ? 0 : Integer.parseInt(timeAsString2.substring(6));
                }
                Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
                return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimeCreate(sessionCalendar, hr, min, sec), this.connection.getServerTimezoneTZ(), tz, rollForward);
            } catch (RuntimeException ex) {
                SQLException sqlEx = SQLError.createSQLException(ex.toString(), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                sqlEx.initCause(ex);
                throw sqlEx;
            }
        }
    }

    private Time getTimeInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException, NumberFormatException {
        checkRowPos();
        if (this.isBinaryEncoded) {
            return getNativeTime(columnIndex, targetCalendar, tz, rollForward);
        }
        if (!this.useFastDateParsing) {
            String timeAsString = getStringInternal(columnIndex, false);
            return getTimeFromString(timeAsString, targetCalendar, columnIndex, tz, rollForward);
        }
        checkColumnBounds(columnIndex);
        int columnIndexMinusOne = columnIndex - 1;
        if (this.thisRow.isNull(columnIndexMinusOne)) {
            this.wasNullFlag = true;
            return null;
        }
        this.wasNullFlag = false;
        return this.thisRow.getTimeFast(columnIndexMinusOne, targetCalendar, tz, rollForward, this.connection, this);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return getTimestampInternal(columnIndex, null, getDefaultTimeZone(), false);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return getTimestampInternal(columnIndex, cal, cal != null ? cal.getTimeZone() : getDefaultTimeZone(), true);
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String columnName) throws SQLException {
        return getTimestamp(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        return getTimestamp(findColumn(columnName), cal);
    }

    private Timestamp getTimestampFromString(int columnIndex, Calendar targetCalendar, String timestampValue, TimeZone tz, boolean rollForward) throws SQLException, NumberFormatException {
        int year;
        int month;
        int day;
        try {
            this.wasNullFlag = false;
            if (timestampValue == null) {
                this.wasNullFlag = true;
                return null;
            }
            String timestampValue2 = timestampValue.trim();
            int length = timestampValue2.length();
            Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
            boolean useGmtMillis = this.connection.getUseGmtMillisForDatetimes();
            if (length > 0 && timestampValue2.charAt(0) == '0' && (timestampValue2.equals("0000-00-00") || timestampValue2.equals("0000-00-00 00:00:00") || timestampValue2.equals("00000000000000") || timestampValue2.equals("0"))) {
                if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior())) {
                    this.wasNullFlag = true;
                    return null;
                }
                if (SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE.equals(this.connection.getZeroDateTimeBehavior())) {
                    throw SQLError.createSQLException("Value '" + timestampValue2 + "' can not be represented as java.sql.Timestamp", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
                }
                return fastTimestampCreate(null, 1, 1, 1, 0, 0, 0, 0, useGmtMillis);
            }
            if (this.fields[columnIndex - 1].getMysqlType() == 13) {
                if (!this.useLegacyDatetimeCode) {
                    return TimeUtil.fastTimestampCreate(tz, Integer.parseInt(timestampValue2.substring(0, 4)), 1, 1, 0, 0, 0, 0);
                }
                return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, Integer.parseInt(timestampValue2.substring(0, 4)), 1, 1, 0, 0, 0, 0, useGmtMillis), this.connection.getServerTimezoneTZ(), tz, rollForward);
            }
            int hour = 0;
            int minutes = 0;
            int seconds = 0;
            int nanos = 0;
            int decimalIndex = timestampValue2.indexOf(".");
            if (decimalIndex == length - 1) {
                length--;
            } else if (decimalIndex != -1) {
                if (decimalIndex + 2 <= length) {
                    nanos = Integer.parseInt(timestampValue2.substring(decimalIndex + 1));
                    int numDigits = length - (decimalIndex + 1);
                    if (numDigits < 9) {
                        int factor = (int) Math.pow(10.0d, 9 - numDigits);
                        nanos *= factor;
                    }
                    length = decimalIndex;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            switch (length) {
                case 2:
                    int year2 = Integer.parseInt(timestampValue2.substring(0, 2));
                    if (year2 <= 69) {
                        year2 += 100;
                    }
                    year = year2 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = 1;
                    day = 1;
                    break;
                case 3:
                case 5:
                case 7:
                case 9:
                case 11:
                case 13:
                case 15:
                case 16:
                case 17:
                case 18:
                default:
                    throw new SQLException("Bad format for Timestamp '" + timestampValue2 + "' in column " + columnIndex + ".", SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
                case 4:
                    int year3 = Integer.parseInt(timestampValue2.substring(0, 2));
                    if (year3 <= 69) {
                        year3 += 100;
                    }
                    year = year3 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = Integer.parseInt(timestampValue2.substring(2, 4));
                    day = 1;
                    break;
                case 6:
                    int year4 = Integer.parseInt(timestampValue2.substring(0, 2));
                    if (year4 <= 69) {
                        year4 += 100;
                    }
                    year = year4 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = Integer.parseInt(timestampValue2.substring(2, 4));
                    day = Integer.parseInt(timestampValue2.substring(4, 6));
                    break;
                case 8:
                    if (timestampValue2.indexOf(":") != -1) {
                        hour = Integer.parseInt(timestampValue2.substring(0, 2));
                        minutes = Integer.parseInt(timestampValue2.substring(3, 5));
                        seconds = Integer.parseInt(timestampValue2.substring(6, 8));
                        year = 1970;
                        month = 1;
                        day = 1;
                        break;
                    } else {
                        int year5 = Integer.parseInt(timestampValue2.substring(0, 4));
                        int month2 = Integer.parseInt(timestampValue2.substring(4, 6));
                        day = Integer.parseInt(timestampValue2.substring(6, 8));
                        year = year5 - 1900;
                        month = month2 - 1;
                        break;
                    }
                case 10:
                    if (this.fields[columnIndex - 1].getMysqlType() == 10 || timestampValue2.indexOf("-") != -1) {
                        year = Integer.parseInt(timestampValue2.substring(0, 4));
                        month = Integer.parseInt(timestampValue2.substring(5, 7));
                        day = Integer.parseInt(timestampValue2.substring(8, 10));
                        hour = 0;
                        minutes = 0;
                        break;
                    } else {
                        int year6 = Integer.parseInt(timestampValue2.substring(0, 2));
                        if (year6 <= 69) {
                            year6 += 100;
                        }
                        month = Integer.parseInt(timestampValue2.substring(2, 4));
                        day = Integer.parseInt(timestampValue2.substring(4, 6));
                        hour = Integer.parseInt(timestampValue2.substring(6, 8));
                        minutes = Integer.parseInt(timestampValue2.substring(8, 10));
                        year = year6 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                        break;
                    }
                case 12:
                    int year7 = Integer.parseInt(timestampValue2.substring(0, 2));
                    if (year7 <= 69) {
                        year7 += 100;
                    }
                    year = year7 + MysqlErrorNumbers.ER_SLAVE_SQL_THREAD_MUST_STOP;
                    month = Integer.parseInt(timestampValue2.substring(2, 4));
                    day = Integer.parseInt(timestampValue2.substring(4, 6));
                    hour = Integer.parseInt(timestampValue2.substring(6, 8));
                    minutes = Integer.parseInt(timestampValue2.substring(8, 10));
                    seconds = Integer.parseInt(timestampValue2.substring(10, 12));
                    break;
                case 14:
                    year = Integer.parseInt(timestampValue2.substring(0, 4));
                    month = Integer.parseInt(timestampValue2.substring(4, 6));
                    day = Integer.parseInt(timestampValue2.substring(6, 8));
                    hour = Integer.parseInt(timestampValue2.substring(8, 10));
                    minutes = Integer.parseInt(timestampValue2.substring(10, 12));
                    seconds = Integer.parseInt(timestampValue2.substring(12, 14));
                    break;
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                    year = Integer.parseInt(timestampValue2.substring(0, 4));
                    month = Integer.parseInt(timestampValue2.substring(5, 7));
                    day = Integer.parseInt(timestampValue2.substring(8, 10));
                    hour = Integer.parseInt(timestampValue2.substring(11, 13));
                    minutes = Integer.parseInt(timestampValue2.substring(14, 16));
                    seconds = Integer.parseInt(timestampValue2.substring(17, 19));
                    break;
            }
            if (!this.useLegacyDatetimeCode) {
                return TimeUtil.fastTimestampCreate(tz, year, month, day, hour, minutes, seconds, nanos);
            }
            return TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, fastTimestampCreate(sessionCalendar, year, month, day, hour, minutes, seconds, nanos, useGmtMillis), this.connection.getServerTimezoneTZ(), tz, rollForward);
        } catch (RuntimeException e) {
            SQLException sqlEx = SQLError.createSQLException("Cannot convert value '" + timestampValue + "' from column " + columnIndex + " to TIMESTAMP.", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            sqlEx.initCause(e);
            throw sqlEx;
        }
    }

    private Timestamp getTimestampInternal(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException, NumberFormatException {
        Timestamp tsVal;
        if (this.isBinaryEncoded) {
            return getNativeTimestamp(columnIndex, targetCalendar, tz, rollForward);
        }
        if (!this.useFastDateParsing) {
            String timestampValue = getStringInternal(columnIndex, false);
            tsVal = getTimestampFromString(columnIndex, targetCalendar, timestampValue, tz, rollForward);
        } else {
            checkClosed();
            checkRowPos();
            checkColumnBounds(columnIndex);
            tsVal = this.thisRow.getTimestampFast(columnIndex - 1, targetCalendar, tz, rollForward, this.connection, this, this.connection.getUseGmtMillisForDatetimes(), this.connection.getUseJDBCCompliantTimezoneShift());
        }
        if (tsVal == null) {
            this.wasNullFlag = true;
        } else {
            this.wasNullFlag = false;
        }
        return tsVal;
    }

    @Override // java.sql.ResultSet
    public int getType() throws SQLException {
        return this.resultSetType;
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        if (!this.isBinaryEncoded) {
            checkRowPos();
            return getBinaryStream(columnIndex);
        }
        return getNativeBinaryStream(columnIndex);
    }

    @Override // java.sql.ResultSet
    @Deprecated
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        return getUnicodeStream(findColumn(columnName));
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public long getUpdateCount() {
        return this.updateCount;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public long getUpdateID() {
        return this.updateId;
    }

    @Override // java.sql.ResultSet
    public URL getURL(int colIndex) throws SQLException, NumberFormatException {
        String val = getString(colIndex);
        if (val == null) {
            return null;
        }
        try {
            return new URL(val);
        } catch (MalformedURLException e) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____104") + val + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.ResultSet
    public URL getURL(String colName) throws SQLException {
        String val = getString(colName);
        if (val == null) {
            return null;
        }
        try {
            return new URL(val);
        } catch (MalformedURLException e) {
            throw SQLError.createSQLException(Messages.getString("ResultSet.Malformed_URL____107") + val + "'", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
    }

    @Override // java.sql.ResultSet
    public SQLWarning getWarnings() throws SQLException {
        SQLWarning sQLWarning;
        synchronized (checkClosed().getConnectionMutex()) {
            sQLWarning = this.warningChain;
        }
        return sQLWarning;
    }

    @Override // java.sql.ResultSet
    public void insertRow() throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        boolean b;
        synchronized (checkClosed().getConnectionMutex()) {
            b = this.rowData.isAfterLast();
        }
        return b;
    }

    @Override // java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        boolean zIsBeforeFirst;
        synchronized (checkClosed().getConnectionMutex()) {
            zIsBeforeFirst = this.rowData.isBeforeFirst();
        }
        return zIsBeforeFirst;
    }

    @Override // java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        boolean zIsFirst;
        synchronized (checkClosed().getConnectionMutex()) {
            zIsFirst = this.rowData.isFirst();
        }
        return zIsFirst;
    }

    @Override // java.sql.ResultSet
    public boolean isLast() throws SQLException {
        boolean zIsLast;
        synchronized (checkClosed().getConnectionMutex()) {
            zIsLast = this.rowData.isLast();
        }
        return zIsLast;
    }

    private void issueConversionViaParsingWarning(String methodName, int columnIndex, Object value, Field fieldInfo, int[] typesWithNoParseConversion) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            StringBuilder originalQueryBuf = new StringBuilder();
            if (this.owningStatement != null && (this.owningStatement instanceof PreparedStatement)) {
                originalQueryBuf.append(Messages.getString("ResultSet.CostlyConversionCreatedFromQuery"));
                originalQueryBuf.append(((PreparedStatement) this.owningStatement).originalSql);
                originalQueryBuf.append("\n\n");
            } else {
                originalQueryBuf.append(".");
            }
            StringBuilder convertibleTypesBuf = new StringBuilder();
            for (int i : typesWithNoParseConversion) {
                convertibleTypesBuf.append(MysqlDefs.typeToName(i));
                convertibleTypesBuf.append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
            Object[] objArr = new Object[8];
            objArr[0] = methodName;
            objArr[1] = Integer.valueOf(columnIndex + 1);
            objArr[2] = fieldInfo.getOriginalName();
            objArr[3] = fieldInfo.getOriginalTableName();
            objArr[4] = originalQueryBuf.toString();
            objArr[5] = value != null ? value.getClass().getName() : ResultSetMetaData.getClassNameForJavaType(fieldInfo.getSQLType(), fieldInfo.isUnsigned(), fieldInfo.getMysqlType(), fieldInfo.isBinary() || fieldInfo.isBlob(), fieldInfo.isOpaqueBinary(), this.connection.getYearIsDateType());
            objArr[6] = MysqlDefs.typeToName(fieldInfo.getMysqlType());
            objArr[7] = convertibleTypesBuf.toString();
            String message = Messages.getString("ResultSet.CostlyConversion", objArr);
            this.connection.getProfilerEventHandlerInstance().processEvent((byte) 0, this.connection, this.owningStatement, this, 0L, new Throwable(), message);
        }
    }

    @Override // java.sql.ResultSet
    public boolean last() throws SQLException {
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            boolean b = true;
            if (this.rowData.size() == 0) {
                b = false;
            } else {
                if (this.onInsertRow) {
                    this.onInsertRow = false;
                }
                if (this.doingUpdates) {
                    this.doingUpdates = false;
                }
                if (this.thisRow != null) {
                    this.thisRow.closeOpenStreams();
                }
                this.rowData.beforeLast();
                this.thisRow = this.rowData.next();
            }
            setRowPositionValidity();
            z = b;
        }
        return z;
    }

    @Override // java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public boolean next() throws SQLException {
        boolean b;
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.onInsertRow) {
                this.onInsertRow = false;
            }
            if (this.doingUpdates) {
                this.doingUpdates = false;
            }
            if (!reallyResult()) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.ResultSet_is_from_UPDATE._No_Data_115"), SQLError.SQL_STATE_GENERAL_ERROR, getExceptionInterceptor());
            }
            if (this.thisRow != null) {
                this.thisRow.closeOpenStreams();
            }
            if (this.rowData.size() == 0) {
                b = false;
            } else {
                this.thisRow = this.rowData.next();
                if (this.thisRow == null) {
                    b = false;
                } else {
                    clearWarnings();
                    b = true;
                }
            }
            setRowPositionValidity();
            z = b;
        }
        return z;
    }

    private int parseIntAsDouble(int columnIndex, String val) throws SQLException, NumberFormatException {
        if (val == null) {
            return 0;
        }
        double valueAsDouble = Double.parseDouble(val);
        if (this.jdbcCompliantTruncationForReads && (valueAsDouble < -2.147483648E9d || valueAsDouble > 2.147483647E9d)) {
            throwRangeException(String.valueOf(valueAsDouble), columnIndex, 4);
        }
        return (int) valueAsDouble;
    }

    private int getIntWithOverflowCheck(int columnIndex) throws SQLException, NumberFormatException {
        int intValue = this.thisRow.getInt(columnIndex);
        checkForIntegerTruncation(columnIndex, null, intValue);
        return intValue;
    }

    private void checkForIntegerTruncation(int columnIndex, byte[] valueAsBytes, int intValue) throws SQLException, NumberFormatException {
        if (this.jdbcCompliantTruncationForReads) {
            if (intValue == Integer.MIN_VALUE || intValue == Integer.MAX_VALUE) {
                String valueAsString = null;
                if (valueAsBytes == null) {
                    valueAsString = this.thisRow.getString(columnIndex, this.fields[columnIndex].getEncoding(), this.connection);
                }
                long valueAsLong = Long.parseLong(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
                if (valueAsLong < -2147483648L || valueAsLong > 2147483647L) {
                    throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndex + 1, 4);
                }
            }
        }
    }

    private long parseLongAsDouble(int columnIndexZeroBased, String val) throws SQLException, NumberFormatException {
        if (val == null) {
            return 0L;
        }
        double valueAsDouble = Double.parseDouble(val);
        if (this.jdbcCompliantTruncationForReads && (valueAsDouble < -9.223372036854776E18d || valueAsDouble > 9.223372036854776E18d)) {
            throwRangeException(val, columnIndexZeroBased + 1, -5);
        }
        return (long) valueAsDouble;
    }

    private long getLongWithOverflowCheck(int columnIndexZeroBased, boolean doOverflowCheck) throws SQLException, NumberFormatException {
        long longValue = this.thisRow.getLong(columnIndexZeroBased);
        if (doOverflowCheck) {
            checkForLongTruncation(columnIndexZeroBased, null, longValue);
        }
        return longValue;
    }

    private long parseLongWithOverflowCheck(int columnIndexZeroBased, byte[] valueAsBytes, String valueAsString, boolean doCheck) throws SQLException, NumberFormatException {
        long longValue;
        if (valueAsBytes == null && valueAsString == null) {
            return 0L;
        }
        if (valueAsBytes != null) {
            longValue = StringUtils.getLong(valueAsBytes);
        } else {
            longValue = Long.parseLong(valueAsString.trim());
        }
        if (doCheck && this.jdbcCompliantTruncationForReads) {
            checkForLongTruncation(columnIndexZeroBased, valueAsBytes, longValue);
        }
        return longValue;
    }

    private void checkForLongTruncation(int columnIndexZeroBased, byte[] valueAsBytes, long longValue) throws SQLException, NumberFormatException {
        if (longValue == Long.MIN_VALUE || longValue == Long.MAX_VALUE) {
            String valueAsString = null;
            if (valueAsBytes == null) {
                valueAsString = this.thisRow.getString(columnIndexZeroBased, this.fields[columnIndexZeroBased].getEncoding(), this.connection);
            }
            double valueAsDouble = Double.parseDouble(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
            if (valueAsDouble < -9.223372036854776E18d || valueAsDouble > 9.223372036854776E18d) {
                throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndexZeroBased + 1, -5);
            }
        }
    }

    private short parseShortAsDouble(int columnIndex, String val) throws SQLException, NumberFormatException {
        if (val == null) {
            return (short) 0;
        }
        double valueAsDouble = Double.parseDouble(val);
        if (this.jdbcCompliantTruncationForReads && (valueAsDouble < -32768.0d || valueAsDouble > 32767.0d)) {
            throwRangeException(String.valueOf(valueAsDouble), columnIndex, 5);
        }
        return (short) valueAsDouble;
    }

    private short parseShortWithOverflowCheck(int columnIndex, byte[] valueAsBytes, String valueAsString) throws SQLException, NumberFormatException {
        short shortValue;
        if (valueAsBytes == null && valueAsString == null) {
            return (short) 0;
        }
        if (valueAsBytes != null) {
            shortValue = StringUtils.getShort(valueAsBytes);
        } else {
            valueAsString = valueAsString.trim();
            shortValue = Short.parseShort(valueAsString);
        }
        if (this.jdbcCompliantTruncationForReads && (shortValue == Short.MIN_VALUE || shortValue == Short.MAX_VALUE)) {
            long valueAsLong = Long.parseLong(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString);
            if (valueAsLong < -32768 || valueAsLong > 32767) {
                throwRangeException(valueAsString == null ? StringUtils.toString(valueAsBytes) : valueAsString, columnIndex, 5);
            }
        }
        return shortValue;
    }

    public boolean prev() throws SQLException {
        boolean b;
        boolean z;
        synchronized (checkClosed().getConnectionMutex()) {
            int rowIndex = this.rowData.getCurrentRowNumber();
            if (this.thisRow != null) {
                this.thisRow.closeOpenStreams();
            }
            if (rowIndex - 1 >= 0) {
                int rowIndex2 = rowIndex - 1;
                this.rowData.setCurrentRow(rowIndex2);
                this.thisRow = this.rowData.getAt(rowIndex2);
                b = true;
            } else if (rowIndex - 1 == -1) {
                this.rowData.setCurrentRow(rowIndex - 1);
                this.thisRow = null;
                b = false;
            } else {
                b = false;
            }
            setRowPositionValidity();
            z = b;
        }
        return z;
    }

    @Override // java.sql.ResultSet
    public boolean previous() throws SQLException {
        boolean zPrev;
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.onInsertRow) {
                this.onInsertRow = false;
            }
            if (this.doingUpdates) {
                this.doingUpdates = false;
            }
            zPrev = prev();
        }
        return zPrev;
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x01f3, code lost:
    
        r14.rowData.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0209, code lost:
    
        if (r14.statementUsedForFetchingRows == null) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x020c, code lost:
    
        r14.statementUsedForFetchingRows.realClose(true, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x022d, code lost:
    
        r14.rowData = null;
        r14.fields = null;
        r14.columnLabelToIndex = null;
        r14.fullColumnNameToIndex = null;
        r14.columnToIndexCache = null;
        r14.warningChain = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x024f, code lost:
    
        if (r14.retainOwningStatement != false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0252, code lost:
    
        r14.owningStatement = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0257, code lost:
    
        r14.catalog = null;
        r14.serverInfo = null;
        r14.thisRow = null;
        r14.fastDefaultCal = null;
        r14.fastClientCal = null;
        r14.connection = null;
        r14.isClosed = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x027c, code lost:
    
        if (0 == 0) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0281, code lost:
    
        throw null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x01d3, code lost:
    
        throw r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01de, code lost:
    
        if (r15 == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01e1, code lost:
    
        r14.owningStatement.removeOpenResultSet(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01f0, code lost:
    
        if (r14.rowData == null) goto L68;
     */
    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r22v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r22v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r22v4 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r22v6 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r23v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r23v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 22, insn: 0x021a: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r22 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('exceptionDuringClose' java.sql.SQLException)]) (LINE:6654), block:B:72:0x021a */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01f3 A[Catch: SQLException -> 0x01ff, all -> 0x0289, TRY_ENTER, TRY_LEAVE, TryCatch #0 {SQLException -> 0x01ff, blocks: (B:65:0x01f3, B:100:0x01f3), top: B:112:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x020c A[Catch: SQLException -> 0x0218, all -> 0x0289, TRY_ENTER, TRY_LEAVE, TryCatch #2 {SQLException -> 0x0218, blocks: (B:70:0x020c, B:103:0x020c), top: B:112:0x0017, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0252 A[Catch: all -> 0x0289, TryCatch #1 {, blocks: (B:7:0x0013, B:10:0x001b, B:12:0x001d, B:16:0x0028, B:17:0x004e, B:19:0x0058, B:21:0x005f, B:23:0x0074, B:24:0x00bc, B:26:0x00c3, B:28:0x00cf, B:30:0x00db, B:32:0x00e7, B:33:0x012a, B:35:0x0131, B:37:0x0139, B:39:0x0145, B:40:0x0151, B:42:0x015b, B:44:0x0165, B:46:0x016d, B:47:0x0176, B:48:0x0186, B:49:0x018c, B:51:0x0194, B:58:0x01d4, B:62:0x01e1, B:63:0x01e9, B:65:0x01f3, B:68:0x0205, B:70:0x020c, B:76:0x022d, B:78:0x0252, B:79:0x0257, B:82:0x0281, B:85:0x0285, B:74:0x021f, B:93:0x01d4, B:97:0x01e1, B:98:0x01e9, B:100:0x01f3, B:101:0x0205, B:103:0x020c, B:104:0x022d, B:106:0x0252, B:107:0x0257, B:110:0x0281, B:57:0x01d3), top: B:113:0x0013, inners: #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x027f  */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r22v0, names: [exceptionDuringClose], types: [java.sql.SQLException] */
    /* JADX WARN: Type inference failed for: r23v0, names: [sqlEx], types: [java.sql.SQLException] */
    @Override // com.mysql.jdbc.ResultSetInternalMethods
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void realClose(boolean r15) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 657
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.ResultSetImpl.realClose(boolean):void");
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods, java.sql.ResultSet
    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public boolean reallyResult() {
        if (this.rowData != null) {
            return true;
        }
        return this.reallyResult;
    }

    @Override // java.sql.ResultSet
    public void refreshRow() throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public boolean relative(int rows) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.rowData.size() == 0) {
                setRowPositionValidity();
                return false;
            }
            if (this.thisRow != null) {
                this.thisRow.closeOpenStreams();
            }
            this.rowData.moveRowRelative(rows);
            this.thisRow = this.rowData.getAt(this.rowData.getCurrentRowNumber());
            setRowPositionValidity();
            return (this.rowData.isAfterLast() || this.rowData.isBeforeFirst()) ? false : true;
        }
    }

    @Override // java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    protected void setBinaryEncoded() {
        this.isBinaryEncoded = true;
    }

    @Override // java.sql.ResultSet
    public void setFetchDirection(int direction) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (direction != 1000 && direction != 1001 && direction != 1002) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Illegal_value_for_fetch_direction_64"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.fetchDirection = direction;
        }
    }

    @Override // java.sql.ResultSet
    public void setFetchSize(int rows) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (rows < 0) {
                throw SQLError.createSQLException(Messages.getString("ResultSet.Value_must_be_between_0_and_getMaxRows()_66"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            this.fetchSize = rows;
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void setFirstCharOfQuery(char c) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.firstCharOfQuery = c;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized void setNextResultSet(ResultSetInternalMethods nextResultSet) {
        this.nextResultSet = nextResultSet;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public void setOwningStatement(StatementImpl owningStatement) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.owningStatement = owningStatement;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized void setResultSetConcurrency(int concurrencyFlag) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.resultSetConcurrency = concurrencyFlag;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized void setResultSetType(int typeFlag) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.resultSetType = typeFlag;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setServerInfo(String info) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.serverInfo = info;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public synchronized void setStatementUsedForFetchingRows(PreparedStatement stmt) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.statementUsedForFetchingRows = stmt;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public synchronized void setWrapperStatement(java.sql.Statement wrapperStatement) {
        try {
            synchronized (checkClosed().getConnectionMutex()) {
                this.wrapperStatement = wrapperStatement;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void throwRangeException(String valueAsString, int columnIndex, int jdbcType) throws SQLException {
        String datatype;
        switch (jdbcType) {
            case -6:
                datatype = "TINYINT";
                break;
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                datatype = "BIGINT";
                break;
            case POIFSConstants.DIFAT_SECTOR_BLOCK /* -4 */:
            case -3:
            case -2:
            case -1:
            case 0:
            case 1:
            case 2:
            default:
                datatype = " (JDBC type '" + jdbcType + "')";
                break;
            case 3:
                datatype = "DECIMAL";
                break;
            case 4:
                datatype = "INTEGER";
                break;
            case 5:
                datatype = "SMALLINT";
                break;
            case 6:
                datatype = "FLOAT";
                break;
            case 7:
                datatype = "REAL";
                break;
            case 8:
                datatype = "DOUBLE";
                break;
        }
        throw SQLError.createSQLException("'" + valueAsString + "' in column '" + columnIndex + "' is outside valid range for the datatype " + datatype + ".", SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE, getExceptionInterceptor());
    }

    public String toString() {
        if (this.reallyResult) {
            return super.toString();
        }
        return "Result set representing update count of " + this.updateCount;
    }

    @Override // java.sql.ResultSet
    public void updateArray(int arg0, Array arg1) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateArray(String arg0, Array arg1) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        updateAsciiStream(findColumn(columnName), x, length);
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        updateBigDecimal(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        updateBinaryStream(findColumn(columnName), x, length);
    }

    @Override // java.sql.ResultSet
    public void updateBlob(int arg0, java.sql.Blob arg1) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBlob(String arg0, java.sql.Blob arg1) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        updateBoolean(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateByte(String columnName, byte x) throws SQLException {
        updateByte(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        updateBytes(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        updateCharacterStream(findColumn(columnName), reader, length);
    }

    @Override // java.sql.ResultSet
    public void updateClob(int arg0, java.sql.Clob arg1) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateClob(String columnName, java.sql.Clob clob) throws SQLException {
        updateClob(findColumn(columnName), clob);
    }

    @Override // java.sql.ResultSet
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateDate(String columnName, Date x) throws SQLException {
        updateDate(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateDouble(String columnName, double x) throws SQLException {
        updateDouble(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateFloat(String columnName, float x) throws SQLException {
        updateFloat(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateInt(String columnName, int x) throws SQLException {
        updateInt(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateLong(String columnName, long x) throws SQLException {
        updateLong(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateNull(int columnIndex) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateNull(String columnName) throws SQLException {
        updateNull(findColumn(columnName));
    }

    @Override // java.sql.ResultSet
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateObject(String columnName, Object x) throws SQLException {
        updateObject(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        updateObject(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateRef(int arg0, Ref arg1) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateRef(String arg0, Ref arg1) throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // java.sql.ResultSet
    public void updateRow() throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateShort(String columnName, short x) throws SQLException {
        updateShort(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateString(String columnName, String x) throws SQLException {
        updateString(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateTime(String columnName, Time x) throws SQLException {
        updateTime(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new NotUpdatable();
    }

    @Override // java.sql.ResultSet
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        updateTimestamp(findColumn(columnName), x);
    }

    @Override // java.sql.ResultSet
    public boolean wasNull() throws SQLException {
        return this.wasNullFlag;
    }

    protected Calendar getGmtCalendar() {
        if (this.gmtCalendar == null) {
            this.gmtCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        }
        return this.gmtCalendar;
    }

    protected ExceptionInterceptor getExceptionInterceptor() {
        return this.exceptionInterceptor;
    }

    @Override // com.mysql.jdbc.ResultSetInternalMethods
    public int getId() {
        return this.resultId;
    }
}
