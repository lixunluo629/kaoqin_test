package org.apache.ibatis.jdbc;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/jdbc/RuntimeSqlException.class */
public class RuntimeSqlException extends RuntimeException {
    private static final long serialVersionUID = 5224696788505678598L;

    public RuntimeSqlException() {
    }

    public RuntimeSqlException(String message) {
        super(message);
    }

    public RuntimeSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeSqlException(Throwable cause) {
        super(cause);
    }
}
