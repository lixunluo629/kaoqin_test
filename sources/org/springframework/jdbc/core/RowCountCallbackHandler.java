package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.support.JdbcUtils;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/RowCountCallbackHandler.class */
public class RowCountCallbackHandler implements RowCallbackHandler {
    private int rowCount;
    private int columnCount;
    private int[] columnTypes;
    private String[] columnNames;

    @Override // org.springframework.jdbc.core.RowCallbackHandler
    public final void processRow(ResultSet rs) throws SQLException {
        if (this.rowCount == 0) {
            ResultSetMetaData rsmd = rs.getMetaData();
            this.columnCount = rsmd.getColumnCount();
            this.columnTypes = new int[this.columnCount];
            this.columnNames = new String[this.columnCount];
            for (int i = 0; i < this.columnCount; i++) {
                this.columnTypes[i] = rsmd.getColumnType(i + 1);
                this.columnNames[i] = JdbcUtils.lookupColumnName(rsmd, i + 1);
            }
        }
        int i2 = this.rowCount;
        this.rowCount = i2 + 1;
        processRow(rs, i2);
    }

    protected void processRow(ResultSet rs, int rowNum) throws SQLException {
    }

    public final int[] getColumnTypes() {
        return this.columnTypes;
    }

    public final String[] getColumnNames() {
        return this.columnNames;
    }

    public final int getRowCount() {
        return this.rowCount;
    }

    public final int getColumnCount() {
        return this.columnCount;
    }
}
