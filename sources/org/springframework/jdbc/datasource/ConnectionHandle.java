package org.springframework.jdbc.datasource;

import java.sql.Connection;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/ConnectionHandle.class */
public interface ConnectionHandle {
    Connection getConnection();

    void releaseConnection(Connection connection);
}
