package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/RowDataDynamic.class */
public class RowDataDynamic implements RowData {
    private int columnCount;
    private Field[] metadata;

    /* renamed from: io, reason: collision with root package name */
    private MysqlIO f1io;
    private boolean isBinaryEncoded;
    private ResultSetRow nextRow;
    private ResultSetImpl owner;
    private boolean useBufferRowExplicit;
    private boolean moreResultsExisted;
    private ExceptionInterceptor exceptionInterceptor;
    private int index = -1;
    private boolean isAfterEnd = false;
    private boolean noMoreRows = false;
    private boolean streamerClosed = false;
    private boolean wasEmpty = false;

    public RowDataDynamic(MysqlIO io2, int colCount, Field[] fields, boolean isBinaryEncoded) throws SQLException {
        this.isBinaryEncoded = false;
        this.f1io = io2;
        this.columnCount = colCount;
        this.isBinaryEncoded = isBinaryEncoded;
        this.metadata = fields;
        this.exceptionInterceptor = this.f1io.getExceptionInterceptor();
        this.useBufferRowExplicit = MysqlIO.useBufferRowExplicit(this.metadata);
    }

    @Override // com.mysql.jdbc.RowData
    public void addRow(ResultSetRow row) throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public void afterLast() throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public void beforeFirst() throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public void beforeLast() throws SQLException {
        notSupported();
    }

    /*  JADX ERROR: NullPointerException in pass: RegionMakerVisitor
        java.lang.NullPointerException
        */
    /* JADX WARN: Finally extract failed */
    @Override // com.mysql.jdbc.RowData
    public void close() throws java.sql.SQLException {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.RowDataDynamic.close():void");
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetRow getAt(int ind) throws SQLException {
        notSupported();
        return null;
    }

    @Override // com.mysql.jdbc.RowData
    public int getCurrentRowNumber() throws SQLException {
        notSupported();
        return -1;
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetInternalMethods getOwner() {
        return this.owner;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean hasNext() throws SQLException {
        boolean hasNext = this.nextRow != null;
        if (!hasNext && !this.streamerClosed) {
            this.f1io.closeStreamer(this);
            this.streamerClosed = true;
        }
        return hasNext;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isAfterLast() throws SQLException {
        return this.isAfterEnd;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isBeforeFirst() throws SQLException {
        return this.index < 0;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isDynamic() {
        return true;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isEmpty() throws SQLException {
        notSupported();
        return false;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isFirst() throws SQLException {
        notSupported();
        return false;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isLast() throws SQLException {
        notSupported();
        return false;
    }

    @Override // com.mysql.jdbc.RowData
    public void moveRowRelative(int rows) throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetRow next() throws SQLException {
        nextRecord();
        if (this.nextRow == null && !this.streamerClosed && !this.moreResultsExisted) {
            this.f1io.closeStreamer(this);
            this.streamerClosed = true;
        }
        if (this.nextRow != null && this.index != Integer.MAX_VALUE) {
            this.index++;
        }
        return this.nextRow;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void nextRecord() throws SQLException {
        try {
            if (!this.noMoreRows) {
                this.nextRow = this.f1io.nextRow(this.metadata, this.columnCount, this.isBinaryEncoded, 1007, true, this.useBufferRowExplicit, true, null);
                if (this.nextRow == null) {
                    this.noMoreRows = true;
                    this.isAfterEnd = true;
                    this.moreResultsExisted = this.f1io.tackOnMoreStreamingResults(this.owner);
                    if (this.index == -1) {
                        this.wasEmpty = true;
                    }
                }
            } else {
                this.nextRow = null;
                this.isAfterEnd = true;
            }
        } catch (SQLException e) {
            if (e instanceof StreamingNotifiable) {
                ((StreamingNotifiable) e).setWasStreamingResults();
            }
            this.noMoreRows = true;
            throw e;
        } catch (Exception ex) {
            SQLException sqlEx = SQLError.createSQLException(Messages.getString("RowDataDynamic.2", new String[]{ex.getClass().getName(), ex.getMessage(), Util.stackTraceToString(ex)}), SQLError.SQL_STATE_GENERAL_ERROR, this.exceptionInterceptor);
            sqlEx.initCause(ex);
            throw sqlEx;
        }
    }

    private void notSupported() throws SQLException {
        throw new OperationNotSupportedException();
    }

    @Override // com.mysql.jdbc.RowData
    public void removeRow(int ind) throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public void setCurrentRow(int rowNumber) throws SQLException {
        notSupported();
    }

    @Override // com.mysql.jdbc.RowData
    public void setOwner(ResultSetImpl rs) {
        this.owner = rs;
    }

    @Override // com.mysql.jdbc.RowData
    public int size() {
        return -1;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean wasEmpty() {
        return this.wasEmpty;
    }

    @Override // com.mysql.jdbc.RowData
    public void setMetadata(Field[] metadata) {
        this.metadata = metadata;
    }
}
