package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Extension.class */
public interface Extension {
    void init(Connection connection, Properties properties) throws SQLException;

    void destroy();
}
