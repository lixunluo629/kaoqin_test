package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/BatchPreparedStatementSetter.class */
public interface BatchPreparedStatementSetter {
    void setValues(PreparedStatement preparedStatement, int i) throws SQLException;

    int getBatchSize();
}
