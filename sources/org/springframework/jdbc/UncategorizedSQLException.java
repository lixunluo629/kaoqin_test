package org.springframework.jdbc;

import java.sql.SQLException;
import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/UncategorizedSQLException.class */
public class UncategorizedSQLException extends UncategorizedDataAccessException {
    private final String sql;

    public UncategorizedSQLException(String task, String sql, SQLException ex) {
        super(task + "; uncategorized SQLException for SQL [" + sql + "]; SQL state [" + ex.getSQLState() + "]; error code [" + ex.getErrorCode() + "]; " + ex.getMessage(), ex);
        this.sql = sql;
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    public String getSql() {
        return this.sql;
    }
}
