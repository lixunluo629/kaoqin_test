package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLIntegrityConstraintViolationException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/jdbc4/MySQLIntegrityConstraintViolationException.class */
public class MySQLIntegrityConstraintViolationException extends SQLIntegrityConstraintViolationException {
    static final long serialVersionUID = -5528363270635808904L;

    public MySQLIntegrityConstraintViolationException() {
    }

    public MySQLIntegrityConstraintViolationException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLIntegrityConstraintViolationException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLIntegrityConstraintViolationException(String reason) {
        super(reason);
    }
}
