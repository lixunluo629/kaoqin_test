package net.sf.cglib.proxy;

import java.lang.reflect.Method;

/* loaded from: cglib-3.1.jar:net/sf/cglib/proxy/InvocationHandler.class */
public interface InvocationHandler extends Callback {
    Object invoke(Object obj, Method method, Object[] objArr) throws Throwable;
}
