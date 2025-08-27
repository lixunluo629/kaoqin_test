package org.apache.ibatis.reflection;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/ReflectionException.class */
public class ReflectionException extends PersistenceException {
    private static final long serialVersionUID = 7642570221267566591L;

    public ReflectionException() {
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
