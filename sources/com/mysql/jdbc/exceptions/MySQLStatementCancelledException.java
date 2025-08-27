package com.mysql.jdbc.exceptions;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/exceptions/MySQLStatementCancelledException.class */
public class MySQLStatementCancelledException extends MySQLNonTransientException {
    static final long serialVersionUID = -8762717748377197378L;

    public MySQLStatementCancelledException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public MySQLStatementCancelledException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public MySQLStatementCancelledException(String reason) {
        super(reason);
    }

    public MySQLStatementCancelledException() {
        super("Statement cancelled due to client request");
    }
}
