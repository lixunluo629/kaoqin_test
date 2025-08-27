package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.SQLType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC42UpdatableResultSet.class */
public class JDBC42UpdatableResultSet extends JDBC4UpdatableResultSet {
    public JDBC42UpdatableResultSet(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        super(catalog, fields, tuples, conn, creatorStmt);
    }

    private int translateAndCheckSqlType(SQLType sqlType) throws SQLException {
        return JDBC42Helper.translateAndCheckSqlType(sqlType, getExceptionInterceptor());
    }

    @Override // com.mysql.jdbc.ResultSetImpl
    public <T> T getObject(int i, Class<T> cls) throws SQLException {
        synchronized (checkClosed().getConnectionMutex()) {
            if (cls == null) {
                throw SQLError.createSQLException("Type parameter can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
            }
            if (cls.equals(LocalDate.class)) {
                return cls.cast(getDate(i).toLocalDate());
            }
            if (cls.equals(LocalDateTime.class)) {
                return cls.cast(getTimestamp(i).toLocalDateTime());
            }
            if (cls.equals(LocalTime.class)) {
                return cls.cast(getTime(i).toLocalTime());
            }
            if (cls.equals(OffsetDateTime.class)) {
                try {
                    return cls.cast(OffsetDateTime.parse(getString(i)));
                } catch (DateTimeParseException e) {
                }
            } else if (cls.equals(OffsetTime.class)) {
                try {
                    return cls.cast(OffsetTime.parse(getString(i)));
                } catch (DateTimeParseException e2) {
                }
            }
            return (T) super.getObject(i, cls);
        }
    }

    @Override // com.mysql.jdbc.UpdatableResultSet, com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(int columnIndex, Object x) throws SQLException {
        super.updateObject(columnIndex, JDBC42Helper.convertJavaTimeToJavaSql(x));
    }

    @Override // com.mysql.jdbc.UpdatableResultSet, com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        super.updateObject(columnIndex, JDBC42Helper.convertJavaTimeToJavaSql(x), scaleOrLength);
    }

    @Override // com.mysql.jdbc.UpdatableResultSet, com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(String columnLabel, Object x) throws SQLException {
        super.updateObject(columnLabel, JDBC42Helper.convertJavaTimeToJavaSql(x));
    }

    @Override // com.mysql.jdbc.UpdatableResultSet, com.mysql.jdbc.ResultSetImpl, java.sql.ResultSet
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        super.updateObject(columnLabel, JDBC42Helper.convertJavaTimeToJavaSql(x), scaleOrLength);
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        super.updateObjectInternal(columnIndex, JDBC42Helper.convertJavaTimeToJavaSql(x), Integer.valueOf(translateAndCheckSqlType(targetSqlType)), 0);
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        super.updateObjectInternal(columnIndex, JDBC42Helper.convertJavaTimeToJavaSql(x), Integer.valueOf(translateAndCheckSqlType(targetSqlType)), scaleOrLength);
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
        super.updateObjectInternal(findColumn(columnLabel), JDBC42Helper.convertJavaTimeToJavaSql(x), Integer.valueOf(translateAndCheckSqlType(targetSqlType)), 0);
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        super.updateObjectInternal(findColumn(columnLabel), JDBC42Helper.convertJavaTimeToJavaSql(x), Integer.valueOf(translateAndCheckSqlType(targetSqlType)), scaleOrLength);
    }
}
