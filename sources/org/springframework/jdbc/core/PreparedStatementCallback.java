package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/PreparedStatementCallback.class */
public interface PreparedStatementCallback<T> {
    T doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException;
}
