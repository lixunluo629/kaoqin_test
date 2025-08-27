package org.springframework.jdbc.support.rowset;

import org.springframework.jdbc.InvalidResultSetAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/rowset/SqlRowSetMetaData.class */
public interface SqlRowSetMetaData {
    String getCatalogName(int i) throws InvalidResultSetAccessException;

    String getColumnClassName(int i) throws InvalidResultSetAccessException;

    int getColumnCount() throws InvalidResultSetAccessException;

    String[] getColumnNames() throws InvalidResultSetAccessException;

    int getColumnDisplaySize(int i) throws InvalidResultSetAccessException;

    String getColumnLabel(int i) throws InvalidResultSetAccessException;

    String getColumnName(int i) throws InvalidResultSetAccessException;

    int getColumnType(int i) throws InvalidResultSetAccessException;

    String getColumnTypeName(int i) throws InvalidResultSetAccessException;

    int getPrecision(int i) throws InvalidResultSetAccessException;

    int getScale(int i) throws InvalidResultSetAccessException;

    String getSchemaName(int i) throws InvalidResultSetAccessException;

    String getTableName(int i) throws InvalidResultSetAccessException;

    boolean isCaseSensitive(int i) throws InvalidResultSetAccessException;

    boolean isCurrency(int i) throws InvalidResultSetAccessException;

    boolean isSigned(int i) throws InvalidResultSetAccessException;
}
