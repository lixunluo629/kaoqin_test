package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ExceptionInterceptor.class */
public interface ExceptionInterceptor extends Extension {
    SQLException interceptException(SQLException sQLException, Connection connection);
}
