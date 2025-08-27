package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/LoadBalanceExceptionChecker.class */
public interface LoadBalanceExceptionChecker extends Extension {
    boolean shouldExceptionTriggerFailover(SQLException sQLException);
}
