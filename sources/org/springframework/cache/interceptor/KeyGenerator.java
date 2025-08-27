package org.springframework.cache.interceptor;

import java.lang.reflect.Method;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/KeyGenerator.class */
public interface KeyGenerator {
    Object generate(Object obj, Method method, Object... objArr);
}
