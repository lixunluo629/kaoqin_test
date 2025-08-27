package net.sf.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/MethodInterceptor.class */
public interface MethodInterceptor extends Callback {
    Object intercept(Object obj, Method method, Object[] objArr, MethodProxy methodProxy) throws Throwable;
}
