package org.springframework.jdbc.support.rowset;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.springframework.jdbc.InvalidResultSetAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/rowset/ResultSetWrappingSqlRowSetMetaData.class */
public class ResultSetWrappingSqlRowSetMetaData implements SqlRowSetMetaData {
    private final ResultSetMetaData resultSetMetaData;
    private String[] columnNames;

    public ResultSetWrappingSqlRowSetMetaData(ResultSetMetaData resultSetMetaData) {
        this.resultSetMetaData = resultSetMetaData;
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getCatalogName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getCatalogName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getColumnClassName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnClassName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public int getColumnCount() throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnCount();
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String[] getColumnNames() throws InvalidResultSetAccessException {
        if (this.columnNames == null) {
            this.columnNames = new String[getColumnCount()];
            for (int i = 0; i < getColumnCount(); i++) {
                this.columnNames[i] = getColumnName(i + 1);
            }
        }
        return this.columnNames;
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public int getColumnDisplaySize(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnDisplaySize(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getColumnLabel(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnLabel(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getColumnName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public int getColumnType(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnType(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getColumnTypeName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getColumnTypeName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public int getPrecision(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getPrecision(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public int getScale(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getScale(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getSchemaName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getSchemaName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public String getTableName(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.getTableName(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public boolean isCaseSensitive(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.isCaseSensitive(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public boolean isCurrency(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.isCurrency(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }

    @Override // org.springframework.jdbc.support.rowset.SqlRowSetMetaData
    public boolean isSigned(int column) throws InvalidResultSetAccessException {
        try {
            return this.resultSetMetaData.isSigned(column);
        } catch (SQLException se) {
            throw new InvalidResultSetAccessException(se);
        }
    }
}
