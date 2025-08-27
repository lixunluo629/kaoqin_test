package org.springframework.jdbc.datasource;

import java.sql.Connection;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/ConnectionProxy.class */
public interface ConnectionProxy extends Connection {
    Connection getTargetConnection();
}
