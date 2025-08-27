package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLTimeoutException.class */
public class MySQLTimeoutException extends MySQLTransientException {
    static final long serialVersionUID = -789621240523230339L;

    public MySQLTimeoutException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLTimeoutException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLTimeoutException(String reason) {
        super(reason);
    }

    public MySQLTimeoutException() {
        super("Statement cancelled due to timeout or client request");
    }
}
