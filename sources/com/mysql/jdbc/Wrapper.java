package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/Wrapper.class */
public interface Wrapper {
    <T> T unwrap(Class<T> cls) throws SQLException;

    boolean isWrapperFor(Class<?> cls) throws SQLException;
}
