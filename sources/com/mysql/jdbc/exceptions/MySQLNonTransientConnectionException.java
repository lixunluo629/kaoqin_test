package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLNonTransientConnectionException.class */
public class MySQLNonTransientConnectionException extends MySQLNonTransientException {
    static final long serialVersionUID = -3050543822763367670L;

    public MySQLNonTransientConnectionException() {
    }

    public MySQLNonTransientConnectionException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLNonTransientConnectionException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLNonTransientConnectionException(String reason) {
        super(reason);
    }
}
