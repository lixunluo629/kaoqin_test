package com.mysql.jdbc.exceptions.jdbc4;

import java.sql.SQLInvalidAuthorizationSpecException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/jdbc4/MySQLInvalidAuthorizationSpecException.class */
public class MySQLInvalidAuthorizationSpecException extends SQLInvalidAuthorizationSpecException {
    static final long serialVersionUID = 6878889837492500030L;

    public MySQLInvalidAuthorizationSpecException() {
    }

    public MySQLInvalidAuthorizationSpecException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLInvalidAuthorizationSpecException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLInvalidAuthorizationSpecException(String reason) {
        super(reason);
    }
}
