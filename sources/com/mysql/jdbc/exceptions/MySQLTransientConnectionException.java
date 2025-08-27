package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLTransientConnectionException.class */
public class MySQLTransientConnectionException extends MySQLTransientException {
    static final long serialVersionUID = 8699144578759941201L;

    public MySQLTransientConnectionException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLTransientConnectionException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLTransientConnectionException(String reason) {
        super(reason);
    }

    public MySQLTransientConnectionException() {
    }
}
