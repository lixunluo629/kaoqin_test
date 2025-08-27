package org.apache.ibatis.builder;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/builder/BuilderException.class */
public class BuilderException extends PersistenceException {
    private static final long serialVersionUID = -3885164021020443281L;

    public BuilderException() {
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }
}
