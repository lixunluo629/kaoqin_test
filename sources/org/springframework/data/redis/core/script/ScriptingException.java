package org.springframework.data.redis.core.script;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/ScriptingException.class */
public class ScriptingException extends NestedRuntimeException {
    public ScriptingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ScriptingException(String msg) {
        super(msg);
    }
}
