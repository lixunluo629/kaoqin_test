package org.springframework.jdbc;

import java.sql.SQLException;
import org.springframework.dao.DataAccessResourceFailureException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/CannotGetJdbcConnectionException.class */
public class CannotGetJdbcConnectionException extends DataAccessResourceFailureException {
    public CannotGetJdbcConnectionException(String msg, SQLException ex) {
        super(msg, ex);
    }
}
