package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/core/RowCallbackHandler.class */
public interface RowCallbackHandler {
    void processRow(ResultSet resultSet) throws SQLException;
}
