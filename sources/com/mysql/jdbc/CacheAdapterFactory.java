package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Properties;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CacheAdapterFactory.class */
public interface CacheAdapterFactory<K, V> {
    CacheAdapter<K, V> getInstance(Connection connection, String str, int i, int i2, Properties properties) throws SQLException;
}
