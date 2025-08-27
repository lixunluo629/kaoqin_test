package org.springframework.jdbc.datasource.init;

import java.sql.Connection;
import java.sql.SQLException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/DatabasePopulator.class */
public interface DatabasePopulator {
    void populate(Connection connection) throws SQLException, ScriptException;
}
