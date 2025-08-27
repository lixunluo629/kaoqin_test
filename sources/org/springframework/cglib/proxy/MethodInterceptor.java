package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/proxy/MethodInterceptor.class */
public interface MethodInterceptor extends Callback {
    Object intercept(Object obj, Method method, Object[] objArr, MethodProxy methodProxy) throws Throwable;
}
