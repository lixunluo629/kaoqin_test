package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/StatementInterceptor.class */
public interface StatementInterceptor extends Extension {
    @Override // com.mysql.jdbc.Extension
    void init(Connection connection, Properties properties) throws SQLException;

    ResultSetInternalMethods preProcess(String str, Statement statement, Connection connection) throws SQLException;

    ResultSetInternalMethods postProcess(String str, Statement statement, ResultSetInternalMethods resultSetInternalMethods, Connection connection) throws SQLException;

    boolean executeTopLevelOnly();

    @Override // com.mysql.jdbc.Extension
    void destroy();
}
