package org.apache.ibatis.exceptions;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/exceptions/PersistenceException.class */
public class PersistenceException extends IbatisException {
    private static final long serialVersionUID = -7537395265357977271L;

    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
