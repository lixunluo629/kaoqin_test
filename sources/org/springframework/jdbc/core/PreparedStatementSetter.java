package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/PreparedStatementSetter.class */
public interface PreparedStatementSetter {
    void setValues(PreparedStatement preparedStatement) throws SQLException;
}
