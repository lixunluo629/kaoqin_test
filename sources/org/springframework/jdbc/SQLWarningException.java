package org.springframework.jdbc;

import java.sql.SQLWarning;
import org.springframework.dao.UncategorizedDataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/SQLWarningException.class */
public class SQLWarningException extends UncategorizedDataAccessException {
    public SQLWarningException(String msg, SQLWarning ex) {
        super(msg, ex);
    }

    public SQLWarning SQLWarning() {
        return (SQLWarning) getCause();
    }
}
