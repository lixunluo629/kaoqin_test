package org.springframework.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/proxy/InvocationHandler.class */
public interface InvocationHandler extends Callback {
    Object invoke(Object obj, Method method, Object[] objArr) throws Throwable;
}
