package org.springframework.jdbc.datasource.init;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/ScriptException.class */
public abstract class ScriptException extends DataAccessException {
    public ScriptException(String message) {
        super(message);
    }

    public ScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
