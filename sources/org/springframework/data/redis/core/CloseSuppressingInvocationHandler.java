package org.springframework.data.redis.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.data.redis.connection.RedisConnection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/CloseSuppressingInvocationHandler.class */
class CloseSuppressingInvocationHandler implements InvocationHandler {
    private static final String CLOSE = "close";
    private static final String HASH_CODE = "hashCode";
    private static final String EQUALS = "equals";
    private final RedisConnection target;

    public CloseSuppressingInvocationHandler(RedisConnection target) {
        this.target = target;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals(EQUALS)) {
            return Boolean.valueOf(proxy == args[0]);
        }
        if (method.getName().equals("hashCode")) {
            return Integer.valueOf(System.identityHashCode(proxy));
        }
        if (method.getName().equals("close")) {
            return null;
        }
        try {
            Object retVal = method.invoke(this.target, args);
            return retVal;
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
}
