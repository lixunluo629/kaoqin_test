package com.mysql.jdbc;

import java.sql.SQLException;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/NotImplemented.class */
public class NotImplemented extends SQLException {
    static final long serialVersionUID = 7768433826547599990L;

    public NotImplemented() {
        super(Messages.getString("NotImplemented.0"), SQLError.SQL_STATE_DRIVER_NOT_CAPABLE);
    }
}
