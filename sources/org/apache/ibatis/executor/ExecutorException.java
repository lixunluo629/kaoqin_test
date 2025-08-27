package org.apache.ibatis.executor;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/executor/ExecutorException.class */
public class ExecutorException extends PersistenceException {
    private static final long serialVersionUID = 4060977051977364820L;

    public ExecutorException() {
    }

    public ExecutorException(String message) {
        super(message);
    }

    public ExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutorException(Throwable cause) {
        super(cause);
    }
}
