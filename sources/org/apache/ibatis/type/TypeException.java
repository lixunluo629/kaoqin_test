package org.apache.ibatis.type;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/TypeException.class */
public class TypeException extends PersistenceException {
    private static final long serialVersionUID = 8614420898975117130L;

    public TypeException() {
    }

    public TypeException(String message) {
        super(message);
    }

    public TypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeException(Throwable cause) {
        super(cause);
    }
}
