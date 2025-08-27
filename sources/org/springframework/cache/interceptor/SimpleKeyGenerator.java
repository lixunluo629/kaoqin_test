package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/SimpleKeyGenerator.class */
public class SimpleKeyGenerator implements KeyGenerator {
    @Override // org.springframework.cache.interceptor.KeyGenerator
    public Object generate(Object target, Method method, Object... params) {
        return generateKey(params);
    }

    public static Object generateKey(Object... params) {
        Object param;
        if (params.length == 0) {
            return SimpleKey.EMPTY;
        }
        if (params.length == 1 && (param = params[0]) != null && !param.getClass().isArray()) {
            return param;
        }
        return new SimpleKey(params);
    }
}
