package com.mysql.jdbc;

import java.sql.NClob;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/JDBC4NClob.class */
public class JDBC4NClob extends Clob implements NClob {
    JDBC4NClob(ExceptionInterceptor exceptionInterceptor) {
        super(exceptionInterceptor);
    }

    JDBC4NClob(String charDataInit, ExceptionInterceptor exceptionInterceptor) {
        super(charDataInit, exceptionInterceptor);
    }
}
