package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/CallableStatementCallback.class */
public interface CallableStatementCallback<T> {
    T doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException;
}
