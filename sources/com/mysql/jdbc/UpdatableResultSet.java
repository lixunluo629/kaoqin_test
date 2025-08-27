package com.mysql.jdbc;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.poifs.common.POIFSConstants;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/UpdatableResultSet.class */
public class UpdatableResultSet extends ResultSetImpl {
    static final byte[] STREAM_DATA_MARKER = StringUtils.getBytes("** STREAM DATA **");
    protected SingleByteCharsetConverter charConverter;
    private String charEncoding;
    private byte[][] defaultColumnValue;
    private PreparedStatement deleter;
    private String deleteSQL;
    private boolean initializedCharConverter;
    protected PreparedStatement inserter;
    private String insertSQL;
    private boolean isUpdatable;
    private String notUpdatableReason;
    private List<Integer> primaryKeyIndicies;
    private String qualifiedAndQuotedTableName;
    private String quotedIdChar;
    private PreparedStatement refresher;
    private String refreshSQL;
    private ResultSetRow savedCurrentRow;
    protected PreparedStatement updater;
    private String updateSQL;
    private boolean populateInserterWithDefaultValues;
    private Map<String, Map<String, Map<String, Integer>>> databasesUsedToTablesUsed;

    protected UpdatableResultSet(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        super(catalog, fields, tuples, conn, creatorStmt);
        this.deleter = null;
        this.deleteSQL = null;
        this.initializedCharConverter = false;
        this.inserter = null;
        this.insertSQL = null;
        this.isUpdatable = false;
        this.notUpdatableReason = null;
        this.primaryKeyIndicies = null;
        this.quotedIdChar = null;
        this.refreshSQL = null;
        this.updater = null;
        this.updateSQL = null;
        this.populateInserterWithDefaultValues = false;
        this.databasesUsedToTablesUsed = null;
        checkUpdatability();
        this.populateInserterWithDefaultValues = this.connection.getPopulateInsertRowWithDefaultValues();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean absolute(int row) throws SQLException {
        return super.absolute(row);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void afterLast() throws SQLException {
        super.afterLast();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void beforeFirst() throws SQLException {
        super.beforeFirst();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void cancelRowUpdates() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (this.doingUpdates) {
                this.doingUpdates = false;
                this.updater.clearParameters();
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl
    protected void checkRowPos() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                super.checkRowPos();
            }
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v107, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v81, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v92, types: [java.lang.String] */
    protected void checkUpdatability() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 728
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.UpdatableResultSet.checkUpdatability():void");
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void deleteRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.isUpdatable) {
                throw new NotUpdatable(this.notUpdatableReason);
            }
            if (this.onInsertRow) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.1"), getExceptionInterceptor());
            }
            if (this.rowData.size() == 0) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.2"), getExceptionInterceptor());
            }
            if (isBeforeFirst()) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.3"), getExceptionInterceptor());
            }
            if (isAfterLast()) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.4"), getExceptionInterceptor());
            }
            if (this.deleter == null) {
                if (this.deleteSQL == null) {
                    generateStatements();
                }
                this.deleter = (PreparedStatement) this.connection.clientPrepareStatement(this.deleteSQL);
            }
            this.deleter.clearParameters();
            int numKeys = this.primaryKeyIndicies.size();
            for (int i = 0; i < numKeys; i++) {
                int index = this.primaryKeyIndicies.get(i).intValue();
                setParamValue(this.deleter, i + 1, this.thisRow, index, this.fields[index]);
            }
            this.deleter.executeUpdate();
            this.rowData.removeRow(this.rowData.getCurrentRowNumber());
            previous();
        }
    }

    private void setParamValue(PreparedStatement ps, int psIdx, ResultSetRow row, int rsIdx, Field field) throws SQLException {
        byte[] val = row.getColumnValue(rsIdx);
        if (val == null) {
            ps.setNull(psIdx, 0);
        }
        switch (field.getSQLType()) {
            case -6:
            case 4:
            case 5:
                ps.setInt(psIdx, row.getInt(rsIdx));
                break;
            case POIFSConstants.LARGEST_REGULAR_SECTOR_NUMBER /* -5 */:
                ps.setLong(psIdx, row.getLong(rsIdx));
                break;
            case -1:
            case 1:
            case 2:
            case 3:
            case 12:
                Field f = this.fields[rsIdx];
                ps.setString(psIdx, row.getString(rsIdx, f.getEncoding(), this.connection));
                break;
            case 0:
                ps.setNull(psIdx, 0);
                break;
            case 6:
            case 7:
            case 8:
            case 16:
                ps.setBytesNoEscapeNoQuotes(psIdx, val);
                break;
            case 91:
                ps.setDate(psIdx, row.getDateFast(rsIdx, this.connection, this, this.fastDefaultCal), this.fastDefaultCal);
                break;
            case 92:
                ps.setTime(psIdx, row.getTimeFast(rsIdx, this.fastDefaultCal, this.connection.getServerTimezoneTZ(), false, this.connection, this));
                break;
            case 93:
                ps.setTimestampInternal(psIdx, row.getTimestampFast(rsIdx, this.fastDefaultCal, this.connection.getServerTimezoneTZ(), false, this.connection, this, false, false), null, this.connection.getDefaultTimeZone(), false, field.getDecimals(), false);
                break;
            default:
                ps.setBytes(psIdx, val);
                break;
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
    private void extractDefaultValues() throws java.sql.SQLException {
        /*
            r6 = this;
            r0 = r6
            com.mysql.jdbc.MySQLConnection r0 = r0.connection
            java.sql.DatabaseMetaData r0 = r0.getMetaData()
            r7 = r0
            r0 = r6
            r1 = r6
            com.mysql.jdbc.Field[] r1 = r1.fields
            int r1 = r1.length
            byte[] r1 = new byte[r1]
            r0.defaultColumnValue = r1
            r0 = 0
            r8 = r0
            r0 = r6
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>>> r0 = r0.databasesUsedToTablesUsed
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
            r9 = r0
        L27:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lf9
            r0 = r9
            java.lang.Object r0 = r0.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            r10 = r0
            r0 = r10
            java.lang.Object r0 = r0.getValue()
            java.util.Map r0 = (java.util.Map) r0
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
            r11 = r0
        L51:
            r0 = r11
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lf6
            r0 = r11
            java.lang.Object r0 = r0.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            r12 = r0
            r0 = r12
            java.lang.Object r0 = r0.getKey()
            java.lang.String r0 = (java.lang.String) r0
            r13 = r0
            r0 = r12
            java.lang.Object r0 = r0.getValue()
            java.util.Map r0 = (java.util.Map) r0
            r14 = r0
            r0 = r7
            r1 = r6
            java.lang.String r1 = r1.catalog     // Catch: java.lang.Throwable -> Ldb
            r2 = 0
            r3 = r13
            java.lang.String r4 = "%"
            java.sql.ResultSet r0 = r0.getColumns(r1, r2, r3, r4)     // Catch: java.lang.Throwable -> Ldb
            r8 = r0
        L8f:
            r0 = r8
            boolean r0 = r0.next()     // Catch: java.lang.Throwable -> Ldb
            if (r0 == 0) goto Ld5
            r0 = r8
            java.lang.String r1 = "COLUMN_NAME"
            java.lang.String r0 = r0.getString(r1)     // Catch: java.lang.Throwable -> Ldb
            r15 = r0
            r0 = r8
            java.lang.String r1 = "COLUMN_DEF"
            byte[] r0 = r0.getBytes(r1)     // Catch: java.lang.Throwable -> Ldb
            r16 = r0
            r0 = r14
            r1 = r15
            boolean r0 = r0.containsKey(r1)     // Catch: java.lang.Throwable -> Ldb
            if (r0 == 0) goto Ld2
            r0 = r14
            r1 = r15
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> Ldb
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch: java.lang.Throwable -> Ldb
            int r0 = r0.intValue()     // Catch: java.lang.Throwable -> Ldb
            r17 = r0
            r0 = r6
            byte[][] r0 = r0.defaultColumnValue     // Catch: java.lang.Throwable -> Ldb
            r1 = r17
            r2 = r16
            r0[r1] = r2     // Catch: java.lang.Throwable -> Ldb
        Ld2:
            goto L8f
        Ld5:
            r0 = jsr -> Le3
        Ld8:
            goto Lf3
        Ldb:
            r18 = move-exception
            r0 = jsr -> Le3
        Le0:
            r1 = r18
            throw r1
        Le3:
            r19 = r0
            r0 = r8
            if (r0 == 0) goto Lf1
            r0 = r8
            r0.close()
            r0 = 0
            r8 = r0
        Lf1:
            ret r19
        Lf3:
            goto L51
        Lf6:
            goto L27
        Lf9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.UpdatableResultSet.extractDefaultValues():void");
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean first() throws SQLException {
        return super.first();
    }

    protected void generateStatements() throws SQLException {
        Map<String, String> tableNamesSoFar;
        String columnName;
        String tableName;
        if (!this.isUpdatable) {
            this.doingUpdates = false;
            this.onInsertRow = false;
            throw new NotUpdatable(this.notUpdatableReason);
        }
        String quotedId = getQuotedIdChar();
        if (this.connection.lowerCaseTableNames()) {
            tableNamesSoFar = new TreeMap<>((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
            this.databasesUsedToTablesUsed = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        } else {
            tableNamesSoFar = new TreeMap<>();
            this.databasesUsedToTablesUsed = new TreeMap();
        }
        this.primaryKeyIndicies = new ArrayList();
        StringBuilder fieldValues = new StringBuilder();
        StringBuilder keyValues = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder insertPlaceHolders = new StringBuilder();
        StringBuilder allTablesBuf = new StringBuilder();
        Map<Integer, String> columnIndicesToTable = new HashMap<>();
        boolean firstTime = true;
        boolean keysFirstTime = true;
        String equalsStr = this.connection.versionMeetsMinimum(3, 23, 0) ? "<=>" : SymbolConstants.EQUAL_SYMBOL;
        for (int i = 0; i < this.fields.length; i++) {
            StringBuilder tableNameBuffer = new StringBuilder();
            Map<String, Integer> updColumnNameToIndex = null;
            if (this.fields[i].getOriginalTableName() != null) {
                String databaseName = this.fields[i].getDatabaseName();
                if (databaseName != null && databaseName.length() > 0) {
                    tableNameBuffer.append(quotedId);
                    tableNameBuffer.append(databaseName);
                    tableNameBuffer.append(quotedId);
                    tableNameBuffer.append('.');
                }
                String tableOnlyName = this.fields[i].getOriginalTableName();
                tableNameBuffer.append(quotedId);
                tableNameBuffer.append(tableOnlyName);
                tableNameBuffer.append(quotedId);
                String fqTableName = tableNameBuffer.toString();
                if (!tableNamesSoFar.containsKey(fqTableName)) {
                    if (!tableNamesSoFar.isEmpty()) {
                        allTablesBuf.append(',');
                    }
                    allTablesBuf.append(fqTableName);
                    tableNamesSoFar.put(fqTableName, fqTableName);
                }
                columnIndicesToTable.put(Integer.valueOf(i), fqTableName);
                updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(databaseName, tableOnlyName);
            } else {
                String tableOnlyName2 = this.fields[i].getTableName();
                if (tableOnlyName2 != null) {
                    tableNameBuffer.append(quotedId);
                    tableNameBuffer.append(tableOnlyName2);
                    tableNameBuffer.append(quotedId);
                    String fqTableName2 = tableNameBuffer.toString();
                    if (!tableNamesSoFar.containsKey(fqTableName2)) {
                        if (!tableNamesSoFar.isEmpty()) {
                            allTablesBuf.append(',');
                        }
                        allTablesBuf.append(fqTableName2);
                        tableNamesSoFar.put(fqTableName2, fqTableName2);
                    }
                    columnIndicesToTable.put(Integer.valueOf(i), fqTableName2);
                    updColumnNameToIndex = getColumnsToIndexMapForTableAndDB(this.catalog, tableOnlyName2);
                }
            }
            String originalColumnName = this.fields[i].getOriginalName();
            if (this.connection.getIO().hasLongColumnInfo() && originalColumnName != null && originalColumnName.length() > 0) {
                columnName = originalColumnName;
            } else {
                columnName = this.fields[i].getName();
            }
            if (updColumnNameToIndex != null && columnName != null) {
                updColumnNameToIndex.put(columnName, Integer.valueOf(i));
            }
            String originalTableName = this.fields[i].getOriginalTableName();
            if (this.connection.getIO().hasLongColumnInfo() && originalTableName != null && originalTableName.length() > 0) {
                tableName = originalTableName;
            } else {
                tableName = this.fields[i].getTableName();
            }
            StringBuilder fqcnBuf = new StringBuilder();
            String databaseName2 = this.fields[i].getDatabaseName();
            if (databaseName2 != null && databaseName2.length() > 0) {
                fqcnBuf.append(quotedId);
                fqcnBuf.append(databaseName2);
                fqcnBuf.append(quotedId);
                fqcnBuf.append('.');
            }
            fqcnBuf.append(quotedId);
            fqcnBuf.append(tableName);
            fqcnBuf.append(quotedId);
            fqcnBuf.append('.');
            fqcnBuf.append(quotedId);
            fqcnBuf.append(columnName);
            fqcnBuf.append(quotedId);
            String qualifiedColumnName = fqcnBuf.toString();
            if (this.fields[i].isPrimaryKey()) {
                this.primaryKeyIndicies.add(Integer.valueOf(i));
                if (!keysFirstTime) {
                    keyValues.append(" AND ");
                } else {
                    keysFirstTime = false;
                }
                keyValues.append(qualifiedColumnName);
                keyValues.append(equalsStr);
                keyValues.append("?");
            }
            if (firstTime) {
                firstTime = false;
                fieldValues.append("SET ");
            } else {
                fieldValues.append(",");
                columnNames.append(",");
                insertPlaceHolders.append(",");
            }
            insertPlaceHolders.append("?");
            columnNames.append(qualifiedColumnName);
            fieldValues.append(qualifiedColumnName);
            fieldValues.append("=?");
        }
        this.qualifiedAndQuotedTableName = allTablesBuf.toString();
        this.updateSQL = "UPDATE " + this.qualifiedAndQuotedTableName + SymbolConstants.SPACE_SYMBOL + fieldValues.toString() + " WHERE " + keyValues.toString();
        this.insertSQL = "INSERT INTO " + this.qualifiedAndQuotedTableName + " (" + columnNames.toString() + ") VALUES (" + insertPlaceHolders.toString() + ")";
        this.refreshSQL = "SELECT " + columnNames.toString() + " FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
        this.deleteSQL = "DELETE FROM " + this.qualifiedAndQuotedTableName + " WHERE " + keyValues.toString();
    }

    private Map<String, Integer> getColumnsToIndexMapForTableAndDB(String databaseName, String tableName) {
        Map<String, Map<String, Integer>> tablesUsedToColumnsMap = this.databasesUsedToTablesUsed.get(databaseName);
        if (tablesUsedToColumnsMap == null) {
            if (this.connection.lowerCaseTableNames()) {
                tablesUsedToColumnsMap = new TreeMap((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
            } else {
                tablesUsedToColumnsMap = new TreeMap();
            }
            this.databasesUsedToTablesUsed.put(databaseName, tablesUsedToColumnsMap);
        }
        Map<String, Integer> nameToIndex = tablesUsedToColumnsMap.get(tableName);
        if (nameToIndex == null) {
            nameToIndex = new HashMap();
            tablesUsedToColumnsMap.put(tableName, nameToIndex);
        }
        return nameToIndex;
    }

    private SingleByteCharsetConverter getCharConverter() throws SQLException {
        if (!this.initializedCharConverter) {
            this.initializedCharConverter = true;
            if (this.connection.getUseUnicode()) {
                this.charEncoding = this.connection.getEncoding();
                this.charConverter = this.connection.getCharsetConverter(this.charEncoding);
            }
        }
        return this.charConverter;
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public int getConcurrency() throws SQLException {
        int i;
        synchronized (checkClosed().getConnectionMutex()) {
            i = this.isUpdatable ? 1008 : 1007;
        }
        return i;
    }

    private String getQuotedIdChar() throws SQLException {
        if (this.quotedIdChar == null) {
            boolean useQuotedIdentifiers = this.connection.supportsQuotedIdentifiers();
            if (useQuotedIdentifiers) {
                java.sql.DatabaseMetaData dbmd = this.connection.getMetaData();
                this.quotedIdChar = dbmd.getIdentifierQuoteString();
            } else {
                this.quotedIdChar = "";
            }
        }
        return this.quotedIdChar;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v17, types: [byte[], byte[][]] */
    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void insertRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.7"), getExceptionInterceptor());
            }
            this.inserter.executeUpdate();
            long autoIncrementId = this.inserter.getLastInsertID();
            int numFields = this.fields.length;
            ?? r0 = new byte[numFields];
            for (int i = 0; i < numFields; i++) {
                if (this.inserter.isNull(i)) {
                    r0[i] = 0;
                } else {
                    r0[i] = this.inserter.getBytesRepresentation(i);
                }
                if (this.fields[i].isAutoIncrement() && autoIncrementId > 0) {
                    r0[i] = StringUtils.getBytes(String.valueOf(autoIncrementId));
                    this.inserter.setBytesNoEscapeNoQuotes(i + 1, r0[i]);
                }
            }
            ResultSetRow resultSetRow = new ByteArrayRow(r0, getExceptionInterceptor());
            refreshRow(this.inserter, resultSetRow);
            this.rowData.addRow(resultSetRow);
            resetInserter();
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean isAfterLast() throws SQLException {
        return super.isAfterLast();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean isBeforeFirst() throws SQLException {
        return super.isBeforeFirst();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean isFirst() throws SQLException {
        return super.isFirst();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean isLast() throws SQLException {
        return super.isLast();
    }

    boolean isUpdatable() {
        return this.isUpdatable;
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean last() throws SQLException {
        return super.last();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void moveToCurrentRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.isUpdatable) {
                throw new NotUpdatable(this.notUpdatableReason);
            }
            if (this.onInsertRow) {
                this.onInsertRow = false;
                this.thisRow = this.savedCurrentRow;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v32 */
    /* JADX WARN: Type inference failed for: r0v48 */
    /* JADX WARN: Type inference failed for: r10v0, types: [byte[][]] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r10v6 */
    /* JADX WARN: Type inference failed for: r10v7 */
    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void moveToInsertRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.isUpdatable) {
                throw new NotUpdatable(this.notUpdatableReason);
            }
            if (this.inserter == null) {
                if (this.insertSQL == null) {
                    generateStatements();
                }
                this.inserter = (PreparedStatement) this.connection.clientPrepareStatement(this.insertSQL);
                this.inserter.parameterMetaData = new MysqlParameterMetadata(this.fields, this.fields.length, getExceptionInterceptor());
                if (this.populateInserterWithDefaultValues) {
                    extractDefaultValues();
                }
                resetInserter();
            } else {
                resetInserter();
            }
            int length = this.fields.length;
            this.onInsertRow = true;
            this.doingUpdates = false;
            this.savedCurrentRow = this.thisRow;
            ?? r10 = new byte[length];
            this.thisRow = new ByteArrayRow(r10, getExceptionInterceptor());
            this.thisRow.setMetadata(this.fields);
            int i = 0;
            ?? r102 = r10;
            while (i < length) {
                if (!this.populateInserterWithDefaultValues) {
                    this.inserter.setBytesNoEscapeNoQuotes(i + 1, StringUtils.getBytes("DEFAULT"));
                    r102 = (byte[][]) null;
                } else if (this.defaultColumnValue[i] != null) {
                    switch (this.fields[i].getMysqlType()) {
                        case 7:
                        case 10:
                        case 11:
                        case 12:
                        case 14:
                            if (this.defaultColumnValue[i].length > 7 && this.defaultColumnValue[i][0] == 67 && this.defaultColumnValue[i][1] == 85 && this.defaultColumnValue[i][2] == 82 && this.defaultColumnValue[i][3] == 82 && this.defaultColumnValue[i][4] == 69 && this.defaultColumnValue[i][5] == 78 && this.defaultColumnValue[i][6] == 84 && this.defaultColumnValue[i][7] == 95) {
                                this.inserter.setBytesNoEscapeNoQuotes(i + 1, this.defaultColumnValue[i]);
                                break;
                            } else {
                                this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
                                break;
                            }
                            break;
                        case 8:
                        case 9:
                        case 13:
                        default:
                            this.inserter.setBytes(i + 1, this.defaultColumnValue[i], false, false);
                            break;
                    }
                    byte[] bArr = new byte[this.defaultColumnValue[i].length];
                    System.arraycopy(this.defaultColumnValue[i], 0, bArr, 0, bArr.length);
                    (r102 == true ? 1 : 0)[i] = bArr;
                } else {
                    this.inserter.setNull(i + 1, 0);
                    (r102 == true ? 1 : 0)[i] = 0;
                }
                i++;
                r102 = r102;
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean next() throws SQLException {
        return super.next();
    }

    @Override // com.mysql.jdbc.ResultSetImpl
    public boolean prev() throws SQLException {
        return super.prev();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean previous() throws SQLException {
        return super.previous();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, com.mysql.jdbc.ResultSetInternalMethods
    public void realClose(boolean calledExplicitly) throws SQLException {
        MySQLConnection locallyScopedConn = this.connection;
        if (locallyScopedConn == null) {
            return;
        }
        synchronized (checkClosed().getConnectionMutex()) {
            SQLException sqlEx = null;
            if (this.useUsageAdvisor && this.deleter == null && this.inserter == null && this.refresher == null && this.updater == null) {
                this.connection.getProfilerEventHandlerInstance().processEvent((byte) 0, this.connection, this.owningStatement, this, 0L, new Throwable(), Messages.getString("UpdatableResultSet.34"));
            }
            try {
                if (this.deleter != null) {
                    this.deleter.close();
                }
            } catch (SQLException ex) {
                sqlEx = ex;
            }
            try {
                if (this.inserter != null) {
                    this.inserter.close();
                }
            } catch (SQLException ex2) {
                sqlEx = ex2;
            }
            try {
                if (this.refresher != null) {
                    this.refresher.close();
                }
            } catch (SQLException ex3) {
                sqlEx = ex3;
            }
            try {
                if (this.updater != null) {
                    this.updater.close();
                }
            } catch (SQLException ex4) {
                sqlEx = ex4;
            }
            super.realClose(calledExplicitly);
            if (sqlEx != null) {
                throw sqlEx;
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void refreshRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.isUpdatable) {
                throw new NotUpdatable();
            }
            if (this.onInsertRow) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.8"), getExceptionInterceptor());
            }
            if (this.rowData.size() == 0) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.9"), getExceptionInterceptor());
            }
            if (isBeforeFirst()) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.10"), getExceptionInterceptor());
            }
            if (isAfterLast()) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.11"), getExceptionInterceptor());
            }
            refreshRow(this.updater, this.thisRow);
        }
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    private void refreshRow(com.mysql.jdbc.PreparedStatement r8, com.mysql.jdbc.ResultSetRow r9) throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 504
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.UpdatableResultSet.refreshRow(com.mysql.jdbc.PreparedStatement, com.mysql.jdbc.ResultSetRow):void");
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean relative(int rows) throws SQLException {
        return super.relative(rows);
    }

    private void resetInserter() throws SQLException {
        this.inserter.clearParameters();
        for (int i = 0; i < this.fields.length; i++) {
            this.inserter.setNull(i + 1, 0);
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean rowDeleted() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean rowInserted() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public boolean rowUpdated() throws SQLException {
        throw SQLError.createSQLFeatureNotSupportedException();
    }

    @Override // com.mysql.jdbc.ResultSetImpl
    protected void setResultSetConcurrency(int concurrencyFlag) {
        super.setResultSetConcurrency(concurrencyFlag);
    }

    private byte[] stripBinaryPrefix(byte[] dataFrom) {
        return StringUtils.stripEnclosure(dataFrom, "_binary'", "'");
    }

    protected void syncUpdate() throws SQLException {
        if (this.updater == null) {
            if (this.updateSQL == null) {
                generateStatements();
            }
            this.updater = (PreparedStatement) this.connection.clientPrepareStatement(this.updateSQL);
            this.updater.parameterMetaData = new MysqlParameterMetadata(this.fields, this.fields.length, getExceptionInterceptor());
        }
        int numFields = this.fields.length;
        this.updater.clearParameters();
        for (int i = 0; i < numFields; i++) {
            if (this.thisRow.getColumnValue(i) != null) {
                if (this.fields[i].getvalueNeedsQuoting()) {
                    if (this.fields[i].isCharsetApplicableType() && !this.fields[i].getEncoding().equals(this.connection.getEncoding())) {
                        this.updater.setString(i + 1, this.thisRow.getString(i, this.fields[i].getEncoding(), this.connection));
                    } else {
                        this.updater.setBytes(i + 1, this.thisRow.getColumnValue(i), this.fields[i].isBinary(), false);
                    }
                } else {
                    this.updater.setBytesNoEscapeNoQuotes(i + 1, this.thisRow.getColumnValue(i));
                }
            } else {
                this.updater.setNull(i + 1, 0);
            }
        }
        int numKeys = this.primaryKeyIndicies.size();
        for (int i2 = 0; i2 < numKeys; i2++) {
            int idx = this.primaryKeyIndicies.get(i2).intValue();
            setParamValue(this.updater, numFields + i2 + 1, this.thisRow, idx, this.fields[idx]);
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setAsciiStream(columnIndex, x, length);
            } else {
                this.inserter.setAsciiStream(columnIndex, x, length);
                this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        updateAsciiStream(findColumn(columnName), x, length);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setBigDecimal(columnIndex, x);
            } else {
                this.inserter.setBigDecimal(columnIndex, x);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, StringUtils.getBytes(x.toString()));
                }
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        updateBigDecimal(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setBinaryStream(columnIndex, x, length);
            } else {
                this.inserter.setBinaryStream(columnIndex, x, length);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
                }
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        updateBinaryStream(findColumn(columnName), x, length);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBlob(int columnIndex, java.sql.Blob blob) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setBlob(columnIndex, blob);
            } else {
                this.inserter.setBlob(columnIndex, blob);
                if (blob == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
                }
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBlob(String columnName, java.sql.Blob blob) throws SQLException {
        updateBlob(findColumn(columnName), blob);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setBoolean(columnIndex, x);
            } else {
                this.inserter.setBoolean(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBoolean(String columnName, boolean x) throws SQLException {
        updateBoolean(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateByte(int columnIndex, byte x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setByte(columnIndex, x);
            } else {
                this.inserter.setByte(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateByte(String columnName, byte x) throws SQLException {
        updateByte(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setBytes(columnIndex, x);
            } else {
                this.inserter.setBytes(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, x);
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateBytes(String columnName, byte[] x) throws SQLException {
        updateBytes(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setCharacterStream(columnIndex, x, length);
            } else {
                this.inserter.setCharacterStream(columnIndex, x, length);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, STREAM_DATA_MARKER);
                }
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        updateCharacterStream(findColumn(columnName), reader, length);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateClob(int columnIndex, java.sql.Clob clob) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (clob == null) {
                updateNull(columnIndex);
            } else {
                updateCharacterStream(columnIndex, clob.getCharacterStream(), (int) clob.length());
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateDate(int columnIndex, Date x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setDate(columnIndex, x);
            } else {
                this.inserter.setDate(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateDate(String columnName, Date x) throws SQLException {
        updateDate(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateDouble(int columnIndex, double x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setDouble(columnIndex, x);
            } else {
                this.inserter.setDouble(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateDouble(String columnName, double x) throws SQLException {
        updateDouble(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateFloat(int columnIndex, float x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setFloat(columnIndex, x);
            } else {
                this.inserter.setFloat(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateFloat(String columnName, float x) throws SQLException {
        updateFloat(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateInt(int columnIndex, int x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setInt(columnIndex, x);
            } else {
                this.inserter.setInt(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateInt(String columnName, int x) throws SQLException {
        updateInt(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateLong(int columnIndex, long x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setLong(columnIndex, x);
            } else {
                this.inserter.setLong(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateLong(String columnName, long x) throws SQLException {
        updateLong(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateNull(int columnIndex) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setNull(columnIndex, 0);
            } else {
                this.inserter.setNull(columnIndex, 0);
                this.thisRow.setColumnValue(columnIndex - 1, null);
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateNull(String columnName) throws SQLException {
        updateNull(findColumn(columnName));
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(int columnIndex, Object x) throws SQLException {
        updateObjectInternal(columnIndex, x, null, 0);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        updateObjectInternal(columnIndex, x, null, scale);
    }

    protected void updateObjectInternal(int columnIndex, Object x, Integer targetType, int scaleOrLength) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                if (targetType == null) {
                    this.updater.setObject(columnIndex, x);
                } else {
                    this.updater.setObject(columnIndex, x, targetType.intValue());
                }
            } else {
                if (targetType == null) {
                    this.inserter.setObject(columnIndex, x);
                } else {
                    this.inserter.setObject(columnIndex, x, targetType.intValue());
                }
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(String columnName, Object x) throws SQLException {
        updateObject(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        updateObject(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateRow() throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.isUpdatable) {
                throw new NotUpdatable(this.notUpdatableReason);
            }
            if (this.doingUpdates) {
                this.updater.executeUpdate();
                refreshRow();
                this.doingUpdates = false;
            } else if (this.onInsertRow) {
                throw SQLError.createSQLException(Messages.getString("UpdatableResultSet.44"), getExceptionInterceptor());
            }
            syncUpdate();
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateShort(int columnIndex, short x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setShort(columnIndex, x);
            } else {
                this.inserter.setShort(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateShort(String columnName, short x) throws SQLException {
        updateShort(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateString(int columnIndex, String x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setString(columnIndex, x);
            } else {
                this.inserter.setString(columnIndex, x);
                if (x == null) {
                    this.thisRow.setColumnValue(columnIndex - 1, null);
                } else if (getCharConverter() != null) {
                    this.thisRow.setColumnValue(columnIndex - 1, StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharset(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
                } else {
                    this.thisRow.setColumnValue(columnIndex - 1, StringUtils.getBytes(x));
                }
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateString(String columnName, String x) throws SQLException {
        updateString(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateTime(int columnIndex, Time x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setTime(columnIndex, x);
            } else {
                this.inserter.setTime(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateTime(String columnName, Time x) throws SQLException {
        updateTime(findColumn(columnName), x);
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (!this.onInsertRow) {
                if (!this.doingUpdates) {
                    this.doingUpdates = true;
                    syncUpdate();
                }
                this.updater.setTimestamp(columnIndex, x);
            } else {
                this.inserter.setTimestamp(columnIndex, x);
                this.thisRow.setColumnValue(columnIndex - 1, this.inserter.getBytesRepresentation(columnIndex - 1));
            }
        }
    }

    @Override // com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        updateTimestamp(findColumn(columnName), x);
    }
}
