package org.apache.ibatis.logging;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/logging/LogException.class */
public class LogException extends PersistenceException {
    private static final long serialVersionUID = 1022924004852350942L;

    public LogException() {
    }

    public LogException(String message) {
        super(message);
    }

    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogException(Throwable cause) {
        super(cause);
    }
}
