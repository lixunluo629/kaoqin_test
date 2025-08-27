package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionPropertiesTransform.class */
public interface ConnectionPropertiesTransform {
    Properties transformProperties(Properties properties) throws SQLException;
}
