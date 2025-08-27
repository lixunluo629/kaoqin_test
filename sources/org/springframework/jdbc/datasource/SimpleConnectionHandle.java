package org.springframework.jdbc.datasource;

import java.sql.Connection;
import org.springframework.util.Assert;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/SimpleConnectionHandle.class */
public class SimpleConnectionHandle implements ConnectionHandle {
    private final Connection connection;

    public SimpleConnectionHandle(Connection connection) {
        Assert.notNull(connection, "Connection must not be null");
        this.connection = connection;
    }

    @Override // org.springframework.jdbc.datasource.ConnectionHandle
    public Connection getConnection() {
        return this.connection;
    }

    @Override // org.springframework.jdbc.datasource.ConnectionHandle
    public void releaseConnection(Connection con) {
    }

    public String toString() {
        return "SimpleConnectionHandle: " + this.connection;
    }
}
