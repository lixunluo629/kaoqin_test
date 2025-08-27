package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/RowDataStatic.class */
public class RowDataStatic implements RowData {
    private Field[] metadata;
    private int index = -1;
    ResultSetImpl owner;
    private List<ResultSetRow> rows;

    public RowDataStatic(List<ResultSetRow> rows) {
        this.rows = rows;
    }

    @Override // com.mysql.jdbc.RowData
    public void addRow(ResultSetRow row) {
        this.rows.add(row);
    }

    @Override // com.mysql.jdbc.RowData
    public void afterLast() {
        if (this.rows.size() > 0) {
            this.index = this.rows.size();
        }
    }

    @Override // com.mysql.jdbc.RowData
    public void beforeFirst() {
        if (this.rows.size() > 0) {
            this.index = -1;
        }
    }

    @Override // com.mysql.jdbc.RowData
    public void beforeLast() {
        if (this.rows.size() > 0) {
            this.index = this.rows.size() - 2;
        }
    }

    @Override // com.mysql.jdbc.RowData
    public void close() {
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetRow getAt(int atIndex) throws SQLException {
        if (atIndex < 0 || atIndex >= this.rows.size()) {
            return null;
        }
        return this.rows.get(atIndex).setMetadata(this.metadata);
    }

    @Override // com.mysql.jdbc.RowData
    public int getCurrentRowNumber() {
        return this.index;
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetInternalMethods getOwner() {
        return this.owner;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean hasNext() {
        boolean hasMore = this.index + 1 < this.rows.size();
        return hasMore;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isAfterLast() {
        return this.index >= this.rows.size() && this.rows.size() != 0;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isBeforeFirst() {
        return this.index == -1 && this.rows.size() != 0;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isDynamic() {
        return false;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isEmpty() {
        return this.rows.size() == 0;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isFirst() {
        return this.index == 0;
    }

    @Override // com.mysql.jdbc.RowData
    public boolean isLast() {
        return this.rows.size() != 0 && this.index == this.rows.size() - 1;
    }

    @Override // com.mysql.jdbc.RowData
    public void moveRowRelative(int rowsToMove) {
        if (this.rows.size() > 0) {
            this.index += rowsToMove;
            if (this.index < -1) {
                beforeFirst();
            } else if (this.index > this.rows.size()) {
                afterLast();
            }
        }
    }

    @Override // com.mysql.jdbc.RowData
    public ResultSetRow next() throws SQLException {
        this.index++;
        if (this.index > this.rows.size()) {
            afterLast();
            return null;
        }
        if (this.index < this.rows.size()) {
            ResultSetRow row = this.rows.get(this.index);
            return row.setMetadata(this.metadata);
        }
        return null;
    }

    @Override // com.mysql.jdbc.RowData
    public void removeRow(int atIndex) {
        this.rows.remove(atIndex);
    }

    @Override // com.mysql.jdbc.RowData
    public void setCurrentRow(int newIndex) {
        this.index = newIndex;
    }

    @Override // com.mysql.jdbc.RowData
    public void setOwner(ResultSetImpl rs) {
        this.owner = rs;
    }

    @Override // com.mysql.jdbc.RowData
    public int size() {
        return this.rows.size();
    }

    @Override // com.mysql.jdbc.RowData
    public boolean wasEmpty() {
        return this.rows != null && this.rows.size() == 0;
    }

    @Override // com.mysql.jdbc.RowData
    public void setMetadata(Field[] metadata) {
        this.metadata = metadata;
    }
}
