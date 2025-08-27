package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NdbLoadBalanceExceptionChecker.class */
public class NdbLoadBalanceExceptionChecker extends StandardLoadBalanceExceptionChecker {
    @Override // com.mysql.jdbc.StandardLoadBalanceExceptionChecker, com.mysql.jdbc.LoadBalanceExceptionChecker
    public boolean shouldExceptionTriggerFailover(SQLException ex) {
        return super.shouldExceptionTriggerFailover(ex) || checkNdbException(ex);
    }

    private boolean checkNdbException(SQLException ex) {
        return ex.getMessage().startsWith("Lock wait timeout exceeded") || (ex.getMessage().startsWith("Got temporary error") && ex.getMessage().endsWith("from NDB"));
    }
}
