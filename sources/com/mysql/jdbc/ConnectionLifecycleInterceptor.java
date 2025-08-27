package com.mysql.jdbc;

import java.sql.SQLException;
import java.sql.Savepoint;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/ConnectionLifecycleInterceptor.class */
public interface ConnectionLifecycleInterceptor extends Extension {
    void close() throws SQLException;

    boolean commit() throws SQLException;

    boolean rollback() throws SQLException;

    boolean rollback(Savepoint savepoint) throws SQLException;

    boolean setAutoCommit(boolean z) throws SQLException;

    boolean setCatalog(String str) throws SQLException;

    boolean transactionBegun() throws SQLException;

    boolean transactionCompleted() throws SQLException;
}
