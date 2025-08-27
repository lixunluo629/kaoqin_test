package com.mysql.jdbc;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeParseException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC42ResultSet.class */
public class JDBC42ResultSet extends JDBC4ResultSet {
    public JDBC42ResultSet(long updateCount, long updateID, MySQLConnection conn, StatementImpl creatorStmt) {
        super(updateCount, updateID, conn, creatorStmt);
    }

    public JDBC42ResultSet(String catalog, Field[] fields, RowData tuples, MySQLConnection conn, StatementImpl creatorStmt) throws SQLException {
        super(catalog, fields, tuples, conn, creatorStmt);
    }

    @Override // com.mysql.jdbc.JDBC4ResultSet, com.mysql.jdbc.ResultSetImpl
    public <T> T getObject(int i, Class<T> cls) throws SQLException {
        if (cls == null) {
            throw SQLError.createSQLException("Type parameter can not be null", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, getExceptionInterceptor());
        }
        if (cls.equals(LocalDate.class)) {
            Date date = getDate(i);
            if (date == null) {
                return null;
            }
            return cls.cast(date.toLocalDate());
        }
        if (cls.equals(LocalDateTime.class)) {
            Timestamp timestamp = getTimestamp(i);
            if (timestamp == null) {
                return null;
            }
            return cls.cast(timestamp.toLocalDateTime());
        }
        if (cls.equals(LocalTime.class)) {
            Time time = getTime(i);
            if (time == null) {
                return null;
            }
            return cls.cast(time.toLocalTime());
        }
        if (cls.equals(OffsetDateTime.class)) {
            try {
                String string = getString(i);
                if (string == null) {
                    return null;
                }
                return cls.cast(OffsetDateTime.parse(string));
            } catch (DateTimeParseException e) {
            }
        } else if (cls.equals(OffsetTime.class)) {
            try {
                String string2 = getString(i);
                if (string2 == null) {
                    return null;
                }
                return cls.cast(OffsetTime.parse(string2));
            } catch (DateTimeParseException e2) {
            }
        }
        return (T) super.getObject(i, cls);
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType) throws SQLException {
        throw new NotUpdatable();
    }

    public void updateObject(int columnIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        throw new NotUpdatable();
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType) throws SQLException {
        throw new NotUpdatable();
    }

    public void updateObject(String columnLabel, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        throw new NotUpdatable();
    }
}
