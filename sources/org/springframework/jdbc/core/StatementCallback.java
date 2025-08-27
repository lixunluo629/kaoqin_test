package org.springframework.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/StatementCallback.class */
public interface StatementCallback<T> {
    T doInStatement(Statement statement) throws SQLException, DataAccessException;
}
