package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLDataException.class */
public class MySQLDataException extends MySQLNonTransientException {
    static final long serialVersionUID = 4317904269797988676L;

    public MySQLDataException() {
    }

    public MySQLDataException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLDataException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLDataException(String reason) {
        super(reason);
    }
}
