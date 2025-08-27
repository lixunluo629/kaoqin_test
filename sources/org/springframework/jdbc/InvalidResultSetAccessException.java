package org.springframework.jdbc;

import java.sql.SQLException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/InvalidResultSetAccessException.class */
public class InvalidResultSetAccessException extends InvalidDataAccessResourceUsageException {
    private String sql;

    public InvalidResultSetAccessException(String task, String sql, SQLException ex) {
        super(task + "; invalid ResultSet access for SQL [" + sql + "]", ex);
        this.sql = sql;
    }

    public InvalidResultSetAccessException(SQLException ex) {
        super(ex.getMessage(), ex);
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    public String getSql() {
        return this.sql;
    }
}
