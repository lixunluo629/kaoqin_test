package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/CallableStatementCreator.class */
public interface CallableStatementCreator {
    CallableStatement createCallableStatement(Connection connection) throws SQLException;
}
