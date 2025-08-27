package org.springframework.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/support/SqlValue.class */
public interface SqlValue {
    void setValue(PreparedStatement preparedStatement, int i) throws SQLException;

    void cleanup();
}
