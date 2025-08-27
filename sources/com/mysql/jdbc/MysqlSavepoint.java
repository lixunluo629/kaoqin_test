package com.mysql.jdbc;

import java.rmi.server.UID;
import java.sql.SQLException;
import java.sql.Savepoint;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/MysqlSavepoint.class */
public class MysqlSavepoint implements Savepoint {
    private String savepointName;
    private ExceptionInterceptor exceptionInterceptor;

    private static String getUniqueId() {
        String uidStr = new UID().toString();
        int uidLength = uidStr.length();
        StringBuilder safeString = new StringBuilder(uidLength + 1);
        safeString.append('_');
        for (int i = 0; i < uidLength; i++) {
            char c = uidStr.charAt(i);
            if (Character.isLetter(c) || Character.isDigit(c)) {
                safeString.append(c);
            } else {
                safeString.append('_');
            }
        }
        return safeString.toString();
    }

    MysqlSavepoint(ExceptionInterceptor exceptionInterceptor) throws SQLException {
        this(getUniqueId(), exceptionInterceptor);
    }

    MysqlSavepoint(String name, ExceptionInterceptor exceptionInterceptor) throws SQLException {
        if (name == null || name.length() == 0) {
            throw SQLError.createSQLException("Savepoint name can not be NULL or empty", SQLError.SQL_STATE_ILLEGAL_ARGUMENT, exceptionInterceptor);
        }
        this.savepointName = name;
        this.exceptionInterceptor = exceptionInterceptor;
    }

    @Override // java.sql.Savepoint
    public int getSavepointId() throws SQLException {
        throw SQLError.createSQLException("Only named savepoints are supported.", SQLError.SQL_STATE_DRIVER_NOT_CAPABLE, this.exceptionInterceptor);
    }

    @Override // java.sql.Savepoint
    public String getSavepointName() throws SQLException {
        return this.savepointName;
    }
}
