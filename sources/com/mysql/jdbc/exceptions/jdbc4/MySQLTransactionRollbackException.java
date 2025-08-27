package com.mysql.jdbc.exceptions.jdbc4;

import com.mysql.jdbc.exceptions.DeadlockTimeoutRollbackMarker;
import java.sql.SQLTransactionRollbackException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/jdbc4/MySQLTransactionRollbackException.class */
public class MySQLTransactionRollbackException extends SQLTransactionRollbackException implements DeadlockTimeoutRollbackMarker {
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
