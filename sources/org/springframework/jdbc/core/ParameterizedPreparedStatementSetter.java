package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/ParameterizedPreparedStatementSetter.class */
public interface ParameterizedPreparedStatementSetter<T> {
    void setValues(PreparedStatement preparedStatement, T t) throws SQLException;
}
