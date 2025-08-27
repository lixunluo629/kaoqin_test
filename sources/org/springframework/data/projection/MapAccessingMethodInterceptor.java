package org.springframework.data.projection;

import java.lang.reflect.Method;
import java.util.Map;
import lombok.Generated;
import lombok.NonNull;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/projection/MapAccessingMethodInterceptor.class */
class MapAccessingMethodInterceptor implements MethodInterceptor {

    @NonNull
    private final Map<String, Object> map;

    @Generated
    public MapAccessingMethodInterceptor(@NonNull Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is marked @NonNull but is null");
        }
        this.map = map;
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (ReflectionUtils.isObjectMethod(method)) {
            return invocation.proceed();
        }
        Accessor accessor = new Accessor(method);
        if (accessor.isGetter()) {
            return this.map.get(accessor.getPropertyName());
        }
        if (accessor.isSetter()) {
            this.map.put(accessor.getPropertyName(), invocation.getArguments()[0]);
            return null;
        }
        throw new IllegalStateException("Should never get here!");
    }
}
