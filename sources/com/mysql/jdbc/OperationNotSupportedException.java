package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/OperationNotSupportedException.class */
class OperationNotSupportedException extends SQLException {
    static final long serialVersionUID = 474918612056813430L;

    OperationNotSupportedException() {
        super(Messages.getString("RowDataDynamic.3"), SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
    }
}
