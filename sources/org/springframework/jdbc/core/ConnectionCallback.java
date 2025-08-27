package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ConnectionCallback.class */
public interface ConnectionCallback<T> {
    T doInConnection(Connection connection) throws SQLException, DataAccessException;
}
