package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLTransactionRollbackException.class */
public class MySQLTransactionRollbackException extends MySQLTransientException implements DeadlockTimeoutRollbackMarker {
    static final long serialVersionUID = 6034999468737801730L;

    public MySQLTransactionRollbackException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLTransactionRollbackException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLTransactionRollbackException(String reason) {
        super(reason);
    }

    public MySQLTransactionRollbackException() {
    }
}
