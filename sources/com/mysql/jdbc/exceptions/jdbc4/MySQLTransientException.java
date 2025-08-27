package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLTransientException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/jdbc4/MySQLTransientException.class */
public class MySQLTransientException extends SQLTransientException {
    static final long serialVersionUID = -1885878228558607563L;

    public MySQLTransientException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLTransientException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLTransientException(String reason) {
        super(reason);
    }

    public MySQLTransientException() {
    }
}
