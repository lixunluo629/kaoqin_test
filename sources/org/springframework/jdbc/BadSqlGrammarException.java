package org.springframework.jdbc;

import java.sql.SQLException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/BadSqlGrammarException.class */
public class BadSqlGrammarException extends InvalidDataAccessResourceUsageException {
    private String sql;

    public BadSqlGrammarException(String task, String sql, SQLException ex) {
        super(task + "; bad SQL grammar [" + sql + "]", ex);
        this.sql = sql;
    }

    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    public String getSql() {
        return this.sql;
    }
}
