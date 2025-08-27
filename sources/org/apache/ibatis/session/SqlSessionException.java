package org.apache.ibatis.session;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/session/SqlSessionException.class */
public class SqlSessionException extends PersistenceException {
    private static final long serialVersionUID = 3833184690240265047L;

    public SqlSessionException() {
    }

    public SqlSessionException(String message) {
        super(message);
    }

    public SqlSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlSessionException(Throwable cause) {
        super(cause);
    }
}
