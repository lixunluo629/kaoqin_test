package org.apache.ibatis.scripting;

import org.apache.ibatis.exceptions.PersistenceException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/scripting/ScriptingException.class */
public class ScriptingException extends PersistenceException {
    private static final long serialVersionUID = 7642570221267566591L;

    public ScriptingException() {
    }

    public ScriptingException(String message) {
        super(message);
    }

    public ScriptingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptingException(Throwable cause) {
        super(cause);
    }
}
