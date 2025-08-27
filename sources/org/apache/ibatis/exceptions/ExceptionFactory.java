package org.apache.ibatis.exceptions;

import org.apache.ibatis.executor.ErrorContext;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/exceptions/ExceptionFactory.class */
public class ExceptionFactory {
    private ExceptionFactory() {
    }

    public static RuntimeException wrapException(String message, Exception e) {
        return new PersistenceException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }
}
